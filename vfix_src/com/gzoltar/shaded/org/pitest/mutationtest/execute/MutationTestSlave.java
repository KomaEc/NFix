package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.boot.HotSwapAgent;
import com.gzoltar.shaded.org.pitest.classinfo.ClassByteArraySource;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.ClassloaderByteArraySource;
import com.gzoltar.shaded.org.pitest.functional.F3;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.mocksupport.BendJavassistToMyWillTransformer;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.FindTestUnits;
import com.gzoltar.shaded.org.pitest.util.CommandLineMessage;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.Glob;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.MemoryWatchdog;
import com.gzoltar.shaded.org.pitest.util.SafeDataInputStream;
import java.io.IOException;
import java.lang.management.MemoryNotificationInfo;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class MutationTestSlave {
   private static final Logger LOG = Log.getLogger();
   private final SafeDataInputStream dis;
   private final Reporter reporter;

   public MutationTestSlave(SafeDataInputStream dis, Reporter reporter) {
      this.dis = dis;
      this.reporter = reporter;
   }

   public void run() {
      try {
         SlaveArguments paramsFromParent = (SlaveArguments)this.dis.read(SlaveArguments.class);
         Log.setVerbose(paramsFromParent.isVerbose());
         ClassLoader loader = IsolationUtils.getContextClassLoader();
         ClassByteArraySource byteSource = new ClassloaderByteArraySource(loader);
         F3<ClassName, ClassLoader, byte[], Boolean> hotswap = new HotSwap(byteSource);
         MutationTestWorker worker = new MutationTestWorker(hotswap, paramsFromParent.engine.createMutator(byteSource), loader);
         List<TestUnit> tests = findTestsForTestClasses(loader, paramsFromParent.testClasses, paramsFromParent.pitConfig);
         worker.run(paramsFromParent.mutations, this.reporter, new TimeOutDecoratedTestSource(paramsFromParent.timeoutStrategy, tests, this.reporter));
         this.reporter.done(ExitCode.OK);
      } catch (Throwable var7) {
         LOG.log(Level.WARNING, "Error during mutation test", var7);
         this.reporter.done(ExitCode.UNKNOWN_ERROR);
      }

   }

   public static void main(String[] args) {
      LOG.log(Level.FINE, "slave started");
      enablePowerMockSupport();
      int port = Integer.valueOf(args[0]);
      Socket s = null;

      try {
         s = new Socket("localhost", port);
         SafeDataInputStream dis = new SafeDataInputStream(s.getInputStream());
         Reporter reporter = new DefaultReporter(s.getOutputStream());
         addMemoryWatchDog(reporter);
         MutationTestSlave instance = new MutationTestSlave(dis, reporter);
         instance.run();
      } catch (UnknownHostException var11) {
         LOG.log(Level.WARNING, "Error during mutation test", var11);
      } catch (IOException var12) {
         LOG.log(Level.WARNING, "Error during mutation test", var12);
      } catch (RuntimeException var13) {
         LOG.log(Level.WARNING, "RuntimeException during mutation test", var13);
      } finally {
         if (s != null) {
            safelyCloseSocket(s);
         }

      }

   }

   private static List<TestUnit> findTestsForTestClasses(ClassLoader loader, Collection<ClassName> testClasses, Configuration pitConfig) {
      Collection<Class<?>> tcs = FCollection.flatMap(testClasses, ClassName.nameToClass(loader));
      FindTestUnits finder = new FindTestUnits(pitConfig);
      return finder.findTestUnitsForAllSuppliedClasses(tcs);
   }

   private static void enablePowerMockSupport() {
      HotSwapAgent.addTransformer(new BendJavassistToMyWillTransformer(Prelude.or(new Glob("com/gzoltar/shaded/javassist/*"))));
   }

   private static void safelyCloseSocket(Socket s) {
      if (s != null) {
         try {
            s.close();
         } catch (IOException var2) {
            LOG.log(Level.WARNING, "Couldn't close socket", var2);
         }
      }

   }

   private static void addMemoryWatchDog(final Reporter r) {
      NotificationListener listener = new NotificationListener() {
         public void handleNotification(Notification notification, Object handback) {
            String type = notification.getType();
            if (type.equals("java.management.memory.threshold.exceeded")) {
               CompositeData cd = (CompositeData)notification.getUserData();
               MemoryNotificationInfo memInfo = MemoryNotificationInfo.from(cd);
               CommandLineMessage.report(memInfo.getPoolName() + " has exceeded the shutdown threshold : " + memInfo.getCount() + " times.\n" + memInfo.getUsage());
               r.done(ExitCode.OUT_OF_MEMORY);
            } else {
               MutationTestSlave.LOG.warning("Unknown notification: " + notification);
            }

         }
      };
      MemoryWatchdog.addWatchDogToAllPools(90L, listener);
   }
}
