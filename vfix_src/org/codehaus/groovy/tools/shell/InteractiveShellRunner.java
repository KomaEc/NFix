package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.jline.ConsoleReader;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class InteractiveShellRunner extends ShellRunner implements Runnable, GroovyObject {
   private final ConsoleReader reader;
   private final Closure prompt;
   private final CommandsMultiCompletor completor;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204783L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204783 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ShellRunner;
   // $FF: synthetic field
   private static Class $class$jline$ConsoleReader;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$io$PrintWriter;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor;

   public InteractiveShellRunner(Shell shell, Closure prompt) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$ShellRunner());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0]);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         this.prompt = (Closure)ScriptBytecodeAdapter.castToType(prompt, $get$$class$groovy$lang$Closure());
         this.reader = (ConsoleReader)ScriptBytecodeAdapter.castToType(var3[0].callConstructor($get$$class$jline$ConsoleReader(), var3[1].callGetProperty(var3[2].callGroovyObjectGetProperty(shell)), var3[3].callConstructor($get$$class$java$io$PrintWriter(), var3[4].callGetProperty(var3[5].callGroovyObjectGetProperty(shell)), Boolean.TRUE)), $get$$class$jline$ConsoleReader());
         this.completor = (CommandsMultiCompletor)ScriptBytecodeAdapter.castToType(var3[6].callConstructor($get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor()), $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor());
         var3[7].call(this.reader, (Object)this.completor);
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public void run() {
      CallSite[] var1 = $getCallSiteArray();
      Object command = null;
      Object var3 = var1[8].call(var1[9].callGroovyObjectGetProperty(var1[10].callGroovyObjectGetProperty(this)));

      while(((Iterator)var3).hasNext()) {
         command = ((Iterator)var3).next();
         var1[11].call(this.completor, (Object)command);
      }

      var1[12].call(this.completor);
      ScriptBytecodeAdapter.invokeMethodOnSuper0($get$$class$org$codehaus$groovy$tools$shell$ShellRunner(), this, "run");
   }

   public void setHistory(History history) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(history, $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), this.reader, "history");
   }

   public void setHistoryFile(File file) {
      CallSite[] var2 = $getCallSiteArray();
      Object dir = var2[13].callGetProperty(file);
      if (!DefaultTypeTransformation.booleanUnbox(var2[14].call(dir))) {
         var2[15].call(dir);
         var2[16].call(var2[17].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{dir}, new String[]{"Created base directory for history file: ", ""})));
      }

      var2[18].call(var2[19].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{file}, new String[]{"Using history file: ", ""})));
      ScriptBytecodeAdapter.setProperty(file, $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), var2[20].callGetProperty(this.reader), "historyFile");
   }

   protected String readLine() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public final ConsoleReader getReader() {
      return this.reader;
   }

   public final Closure getPrompt() {
      return this.prompt;
   }

   public final CommandsMultiCompletor getCompletor() {
      return this.completor;
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$2$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$setRunning(boolean var1) {
      super.setRunning(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public boolean super$2$getRunning() {
      return super.getRunning();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$get$2(String var1) {
      return super.this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public void super$2$run() {
      super.run();
   }

   // $FF: synthetic method
   public void super$2$setErrorHandler(Closure var1) {
      super.setErrorHandler(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$setBreakOnNull(boolean var1) {
      super.setBreakOnNull(var1);
   }

   // $FF: synthetic method
   public boolean super$2$getBreakOnNull() {
      return super.getBreakOnNull();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Shell super$2$getShell() {
      return super.getShell();
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$2$isRunning() {
      return super.isRunning();
   }

   // $FF: synthetic method
   public MetaClass super$2$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Closure super$2$getErrorHandler() {
      return super.getErrorHandler();
   }

   // $FF: synthetic method
   public boolean super$2$work() {
      return super.work();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   public Object super$2$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$2$isBreakOnNull() {
      return super.isBreakOnNull();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "inputStream";
      var0[2] = "io";
      var0[3] = "<$constructor$>";
      var0[4] = "outputStream";
      var0[5] = "io";
      var0[6] = "<$constructor$>";
      var0[7] = "addCompletor";
      var0[8] = "iterator";
      var0[9] = "registry";
      var0[10] = "shell";
      var0[11] = "leftShift";
      var0[12] = "refresh";
      var0[13] = "parentFile";
      var0[14] = "exists";
      var0[15] = "mkdirs";
      var0[16] = "debug";
      var0[17] = "log";
      var0[18] = "debug";
      var0[19] = "log";
      var0[20] = "history";
      var0[21] = "readLine";
      var0[22] = "call";
      var0[23] = "debug";
      var0[24] = "log";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[25];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner = class$("org.codehaus.groovy.tools.shell.InteractiveShellRunner");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ShellRunner() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ShellRunner;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ShellRunner = class$("org.codehaus.groovy.tools.shell.ShellRunner");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$jline$ConsoleReader() {
      Class var10000 = $class$jline$ConsoleReader;
      if (var10000 == null) {
         var10000 = $class$jline$ConsoleReader = class$("com.gzoltar.shaded.jline.ConsoleReader");
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
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$PrintWriter() {
      Class var10000 = $class$java$io$PrintWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$PrintWriter = class$("java.io.PrintWriter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor = class$("org.codehaus.groovy.tools.shell.CommandsMultiCompletor");
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
