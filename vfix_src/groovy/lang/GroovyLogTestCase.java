package groovy.lang;

import groovy.util.GroovyTestCase;
import java.lang.ref.SoftReference;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestResult;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GroovyLogTestCase extends GroovyTestCase implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1024;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205605L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205605 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyLogTestCase;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$util$logging$SimpleFormatter;
   // $FF: synthetic field
   private static Class $class$java$util$logging$StreamHandler;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Logger;
   // $FF: synthetic field
   private static Class $class$java$io$ByteArrayOutputStream;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Level;

   public GroovyLogTestCase() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static String stringLog(Level level, String qualifier, Closure yield) {
      CallSite[] var3 = $getCallSiteArray();
      Logger logger = (Logger)ScriptBytecodeAdapter.castToType(var3[0].call($get$$class$java$util$logging$Logger(), (Object)qualifier), $get$$class$java$util$logging$Logger());
      Object usesParentHandlers = var3[1].callGetProperty(logger);
      ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$lang$GroovyLogTestCase(), logger, "useParentHandlers");
      Object out = var3[2].callConstructor($get$$class$java$io$ByteArrayOutputStream(), (Object)$const$0);
      Handler stringHandler = var3[3].callConstructor($get$$class$java$util$logging$StreamHandler(), out, var3[4].callConstructor($get$$class$java$util$logging$SimpleFormatter()));
      ScriptBytecodeAdapter.setProperty(var3[5].callGetProperty($get$$class$java$util$logging$Level()), $get$$class$groovy$lang$GroovyLogTestCase(), stringHandler, "level");
      var3[6].call(logger, (Object)stringHandler);
      var3[7].callStatic($get$$class$groovy$lang$GroovyLogTestCase(), level, qualifier, yield);
      ScriptBytecodeAdapter.setProperty(var3[8].callGetProperty($get$$class$java$util$logging$Level()), $get$$class$groovy$lang$GroovyLogTestCase(), logger, "level");
      var3[9].call(stringHandler);
      var3[10].call(out);
      var3[11].call(logger, (Object)stringHandler);
      ScriptBytecodeAdapter.setProperty(usesParentHandlers, $get$$class$groovy$lang$GroovyLogTestCase(), logger, "useParentHandlers");
      return (String)ScriptBytecodeAdapter.castToType(var3[12].call(out), $get$$class$java$lang$String());
   }

   public static Object withLevel(Level level, String qualifier, Closure yield) {
      CallSite[] var3 = $getCallSiteArray();
      Logger logger = (Logger)ScriptBytecodeAdapter.castToType(var3[13].call($get$$class$java$util$logging$Logger(), (Object)qualifier), $get$$class$java$util$logging$Logger());
      Object loglevel = var3[14].callGetProperty(logger);
      if (!DefaultTypeTransformation.booleanUnbox(var3[15].call(logger, (Object)level))) {
         ScriptBytecodeAdapter.setProperty(level, $get$$class$groovy$lang$GroovyLogTestCase(), logger, "level");
      }

      Object result = var3[16].call(yield);
      ScriptBytecodeAdapter.setProperty(loglevel, $get$$class$groovy$lang$GroovyLogTestCase(), logger, "level");
      return result;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$lang$GroovyLogTestCase()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$lang$GroovyLogTestCase();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$lang$GroovyLogTestCase(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$lang$GroovyLogTestCase(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   // $FF: synthetic method
   public String super$3$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public String super$4$shouldFailWithCause(Class var1, Closure var2) {
      return super.shouldFailWithCause(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$tearDown() {
      super.tearDown();
   }

   // $FF: synthetic method
   public void super$4$assertScript(String var1) {
      super.assertScript(var1);
   }

   // $FF: synthetic method
   public String super$4$shouldFail(Class var1, Closure var2) {
      return super.shouldFail(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$fixEOLs(String var1) {
      return super.fixEOLs(var1);
   }

   // $FF: synthetic method
   public void super$3$runTest() {
      super.runTest();
   }

   // $FF: synthetic method
   public void super$4$assertInspect(Object var1, String var2) {
      super.assertInspect(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$4$getTestClassName() {
      return super.getTestClassName();
   }

   // $FF: synthetic method
   public String super$4$shouldFail(Closure var1) {
      return super.shouldFail(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$4$assertContains(int var1, int[] var2) {
      super.assertContains(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setName(String var1) {
      super.setName(var1);
   }

   // $FF: synthetic method
   public int super$3$countTestCases() {
      return super.countTestCases();
   }

   // $FF: synthetic method
   public TestResult super$3$run() {
      return super.run();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public String super$4$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$3$run(TestResult var1) {
      super.run(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$4$assertContains(char var1, char[] var2) {
      super.assertContains(var1, var2);
   }

   // $FF: synthetic method
   public TestResult super$3$createResult() {
      return super.createResult();
   }

   // $FF: synthetic method
   public String super$4$getMethodName() {
      return super.getMethodName();
   }

   // $FF: synthetic method
   public void super$3$setUp() {
      super.setUp();
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, Object[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, int[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$4$notYetImplemented() {
      return super.notYetImplemented();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$4$assertToString(Object var1, String var2) {
      super.assertToString(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, char[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$runBare() {
      super.runBare();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$4$assertArrayEquals(Object[] var1, Object[] var2) {
      super.assertArrayEquals(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "getLogger";
      var0[1] = "useParentHandlers";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "<$constructor$>";
      var0[5] = "ALL";
      var0[6] = "addHandler";
      var0[7] = "withLevel";
      var0[8] = "OFF";
      var0[9] = "flush";
      var0[10] = "close";
      var0[11] = "removeHandler";
      var0[12] = "toString";
      var0[13] = "getLogger";
      var0[14] = "level";
      var0[15] = "isLoggable";
      var0[16] = "call";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[17];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$lang$GroovyLogTestCase(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyLogTestCase() {
      Class var10000 = $class$groovy$lang$GroovyLogTestCase;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyLogTestCase = class$("groovy.lang.GroovyLogTestCase");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$SimpleFormatter() {
      Class var10000 = $class$java$util$logging$SimpleFormatter;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$SimpleFormatter = class$("java.util.logging.SimpleFormatter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$StreamHandler() {
      Class var10000 = $class$java$util$logging$StreamHandler;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$StreamHandler = class$("java.util.logging.StreamHandler");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$Logger() {
      Class var10000 = $class$java$util$logging$Logger;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Logger = class$("java.util.logging.Logger");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$ByteArrayOutputStream() {
      Class var10000 = $class$java$io$ByteArrayOutputStream;
      if (var10000 == null) {
         var10000 = $class$java$io$ByteArrayOutputStream = class$("java.io.ByteArrayOutputStream");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$Level() {
      Class var10000 = $class$java$util$logging$Level;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Level = class$("java.util.logging.Level");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
