package org.codehaus.groovy.tools.shell;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public abstract class ShellRunner implements Runnable, GroovyObject {
   protected final Logger log;
   private final Shell shell;
   private boolean running;
   private boolean breakOnNull;
   private Closure errorHandler;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204066L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204066 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ShellRunner;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Shell;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;

   public ShellRunner(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var2[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var2[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.running = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.breakOnNull = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      this.errorHandler = (Closure)ScriptBytecodeAdapter.castToType(new ShellRunner._closure1(this, this), $get$$class$groovy$lang$Closure());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(shell, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert shell", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      this.shell = (Shell)ScriptBytecodeAdapter.castToType(shell, $get$$class$org$codehaus$groovy$tools$shell$Shell());
   }

   public void run() {
      CallSite[] var1 = $getCallSiteArray();
      var1[2].call(this.log, (Object)"Running");
      this.running = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);

      while(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.running))) {
         try {
            this.running = DefaultTypeTransformation.booleanUnbox(var1[3].callCurrent(this));
         } catch (ExitNotification var6) {
            throw (Throwable)var6;
         } catch (Throwable var7) {
            var1[4].call(this.log, new GStringImpl(new Object[]{var7}, new String[]{"Work failed: ", ""}), var7);
            if (DefaultTypeTransformation.booleanUnbox(this.errorHandler)) {
               var1[5].call(this.errorHandler, (Object)var7);
            }
         } finally {
            ;
         }
      }

      var1[6].call(this.log, (Object)"Finished");
   }

   protected boolean work() {
      CallSite[] var1 = $getCallSiteArray();
      Object line = var1[7].callCurrent(this);
      if (DefaultTypeTransformation.booleanUnbox(var1[8].callGetProperty(this.log))) {
         var1[9].call(this.log, (Object)(new GStringImpl(new Object[]{line}, new String[]{"Read line: ", ""})));
      }

      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(line, (Object)null) && DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.breakOnNull)) ? Boolean.TRUE : Boolean.FALSE)) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      } else {
         if (ScriptBytecodeAdapter.compareGreaterThan(var1[10].call(var1[11].call(line)), $const$0)) {
            var1[12].call(this.shell, (Object)line);
         }

         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      }
   }

   protected abstract String readLine();

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$ShellRunner()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$ShellRunner();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$ShellRunner(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$ShellRunner(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public final Shell getShell() {
      return this.shell;
   }

   public boolean getRunning() {
      return this.running;
   }

   public boolean isRunning() {
      return this.running;
   }

   public void setRunning(boolean var1) {
      this.running = var1;
   }

   public boolean getBreakOnNull() {
      return this.breakOnNull;
   }

   public boolean isBreakOnNull() {
      return this.breakOnNull;
   }

   public void setBreakOnNull(boolean var1) {
      this.breakOnNull = var1;
   }

   public Closure getErrorHandler() {
      return this.errorHandler;
   }

   public void setErrorHandler(Closure var1) {
      this.errorHandler = var1;
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
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
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "create";
      var0[1] = "class";
      var0[2] = "debug";
      var0[3] = "work";
      var0[4] = "debug";
      var0[5] = "call";
      var0[6] = "debug";
      var0[7] = "readLine";
      var0[8] = "debugEnabled";
      var0[9] = "debug";
      var0[10] = "size";
      var0[11] = "trim";
      var0[12] = "leftShift";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[13];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$ShellRunner(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Logger() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger = class$("org.codehaus.groovy.tools.shell.util.Logger");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Shell() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Shell;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Shell = class$("org.codehaus.groovy.tools.shell.Shell");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object e) {
         Object ex = new Reference(e);
         CallSite[] var3 = $getCallSiteArray();
         var3[0].call(var3[1].callGroovyObjectGetProperty(this), ex.get());
         Boolean var10000 = Boolean.FALSE;
         ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1(), this, "running");
         return var10000;
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1()) {
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
      private static void $createCallSiteArray_1(String[] var0) {
         var0[0] = "debug";
         var0[1] = "log";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$ShellRunner$_closure1 = class$("org.codehaus.groovy.tools.shell.ShellRunner$_closure1");
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
}
