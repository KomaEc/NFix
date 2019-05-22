package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.File;
import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.BufferManager;
import org.codehaus.groovy.tools.shell.ComplexCommandSupport;
import org.codehaus.groovy.tools.shell.Shell;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class RecordCommand extends ComplexCommandSupport {
   private File file;
   private PrintWriter writer;
   private Object do_start;
   private Object do_stop;
   private Object do_status;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205533L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205533 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public RecordCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "record", "\\r"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.do_start = new RecordCommand._closure1(this, this);
         this.do_stop = new RecordCommand._closure2(this, this);
         this.do_status = new RecordCommand._closure3(this, this);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         ScriptBytecodeAdapter.setGroovyObjectProperty(ScriptBytecodeAdapter.createList(new Object[]{"start", "stop", "status"}), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand(), this, "functions");
         ScriptBytecodeAdapter.setGroovyObjectProperty("status", $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand(), this, "defaultFunction");
         var2[0].callCurrent(this, (Object)(new RecordCommand._closure4(this, this)));
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public boolean isRecording() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareNotEqual(this.file, (Object)null) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public Object recordInput(String line) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var3.record(line, 8), (Object)null);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 13);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert line != null", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[1].callGroovyObjectGetProperty(this))) {
         var2[2].call(this.writer, (Object)line);
         return var2[3].call(this.writer);
      } else {
         return null;
      }
   }

   public Object recordResult(Object result) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[4].callGroovyObjectGetProperty(this))) {
         var2[5].call(this.writer, (Object)(new GStringImpl(new Object[]{var2[6].call($get$$class$java$lang$String(), (Object)result)}, new String[]{"// RESULT: ", ""})));
         return var2[7].call(this.writer);
      } else {
         return null;
      }
   }

   public Object recordError(Throwable cause) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var3.record(cause, 8), (Object)null);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 14);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert cause != null", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[8].callGroovyObjectGetProperty(this))) {
         var2[9].call(this.writer, (Object)(new GStringImpl(new Object[]{cause}, new String[]{"// ERROR: ", ""})));
         var2[10].call(var2[11].callGetProperty(cause), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(var3[1].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{itx.get()}, new String[]{"//    ", ""})));
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5()) {
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
               var0[0] = "println";
               var0[1] = "writer";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5(), var0);
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
            private static Class $get$$class$java$lang$Object() {
               Class var10000 = $class$java$lang$Object;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Object = class$("java.lang.Object");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_recordError_closure5 = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand$_recordError_closure5");
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
         }));
         return var2[12].call(this.writer);
      } else {
         return null;
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand()) {
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
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getDo_start() {
      return this.do_start;
   }

   public void setDo_start(Object var1) {
      this.do_start = var1;
   }

   public Object getDo_stop() {
      return this.do_stop;
   }

   public void setDo_stop(Object var1) {
      this.do_stop = var1;
   }

   public Object getDo_status() {
      return this.do_status;
   }

   public void setDo_status(Object var1) {
      this.do_status = var1;
   }

   // $FF: synthetic method
   public Object super$3$this$dist$get$3(String var1) {
      return super.this$dist$get$3(var1);
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
   public BufferManager super$2$getBuffers() {
      return super.getBuffers();
   }

   // $FF: synthetic method
   public String super$2$getHelp() {
      return super.getHelp();
   }

   // $FF: synthetic method
   public Object super$3$execute(List var1) {
      return super.execute(var1);
   }

   // $FF: synthetic method
   public void super$3$this$dist$set$3(String var1, Object var2) {
      super.this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public History super$2$getHistory() {
      return super.getHistory();
   }

   // $FF: synthetic method
   public List super$3$createCompletors() {
      return super.createCompletors();
   }

   // $FF: synthetic method
   public void super$3$super$2$this$dist$set$2(String var1, Object var2) {
      super.super$2$this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Map super$2$getVariables() {
      return super.getVariables();
   }

   // $FF: synthetic method
   public Object super$3$executeFunction(List var1) {
      return super.executeFunction(var1);
   }

   // $FF: synthetic method
   public List super$2$getBuffer() {
      return super.getBuffer();
   }

   // $FF: synthetic method
   public Object super$3$getDo_all() {
      return super.getDo_all();
   }

   // $FF: synthetic method
   public void super$3$setDo_all(Object var1) {
      super.setDo_all(var1);
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$2$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$2$assertNoArguments(List var1) {
      super.assertNoArguments(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$3$super$2$this$dist$get$2(String var1) {
      return super.super$2$this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public Completor super$2$getCompletor() {
      return super.getCompletor();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$get$2(String var1) {
      return super.this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Object super$3$super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.super$2$this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   public Closure super$3$loadFunction(String var1) {
      return super.loadFunction(var1);
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "addShutdownHook";
      var0[1] = "recording";
      var0[2] = "println";
      var0[3] = "flush";
      var0[4] = "recording";
      var0[5] = "println";
      var0[6] = "valueOf";
      var0[7] = "flush";
      var0[8] = "recording";
      var0[9] = "println";
      var0[10] = "each";
      var0[11] = "stackTrace";
      var0[12] = "flush";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[13];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport = class$("org.codehaus.groovy.tools.shell.ComplexCommandSupport");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static final Integer $const$0 = (Integer)1;
      // $FF: synthetic field
      private static final Integer $const$1 = (Integer)0;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1;
      // $FF: synthetic field
      private static Class $class$java$util$Date;
      // $FF: synthetic field
      private static Class $class$java$lang$String;
      // $FF: synthetic field
      private static Class $class$java$io$File;
      // $FF: synthetic field
      private static Class $class$java$io$PrintWriter;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object args) {
         Object argsx = new Reference(args);
         CallSite[] var3 = $getCallSiteArray();
         if (DefaultTypeTransformation.booleanUnbox(var3[0].callGroovyObjectGetProperty(this))) {
            var3[1].callCurrent(this, (Object)(new GStringImpl(new Object[]{var3[2].callGroovyObjectGetProperty(this)}, new String[]{"Already recording to: ", ""})));
         }

         if (ScriptBytecodeAdapter.compareNotEqual(var3[3].call(argsx.get()), $const$0)) {
            ScriptBytecodeAdapter.setGroovyObjectProperty((File)ScriptBytecodeAdapter.castToType(var3[4].call($get$$class$java$io$File(), "groovysh-", ".txt"), $get$$class$java$io$File()), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1(), this, "file");
         } else {
            ScriptBytecodeAdapter.setGroovyObjectProperty(var3[5].callConstructor($get$$class$java$io$File(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.asType(var3[6].call(argsx.get(), (Object)$const$1), $get$$class$java$lang$String()), $get$$class$java$lang$String())), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1(), this, "file");
         }

         if (DefaultTypeTransformation.booleanUnbox(var3[7].callGetProperty(var3[8].callGroovyObjectGetProperty(this)))) {
            var3[9].call(var3[10].callGetProperty(var3[11].callGroovyObjectGetProperty(this)));
         }

         ScriptBytecodeAdapter.setGroovyObjectProperty((PrintWriter)ScriptBytecodeAdapter.castToType(var3[12].call(var3[13].callGroovyObjectGetProperty(this)), $get$$class$java$io$PrintWriter()), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1(), this, "writer");
         var3[14].call(var3[15].callGroovyObjectGetProperty(this), var3[16].call("// OPENED: ", (Object)var3[17].callConstructor($get$$class$java$util$Date())));
         var3[18].call(var3[19].callGroovyObjectGetProperty(this));
         var3[20].call(var3[21].callGetProperty(var3[22].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var3[23].callGroovyObjectGetProperty(this)}, new String[]{"Recording session to: ", ""})));
         return var3[24].callGroovyObjectGetProperty(this);
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1()) {
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
         var0[0] = "recording";
         var0[1] = "fail";
         var0[2] = "file";
         var0[3] = "size";
         var0[4] = "createTempFile";
         var0[5] = "<$constructor$>";
         var0[6] = "getAt";
         var0[7] = "parentFile";
         var0[8] = "file";
         var0[9] = "mkdirs";
         var0[10] = "parentFile";
         var0[11] = "file";
         var0[12] = "newPrintWriter";
         var0[13] = "file";
         var0[14] = "println";
         var0[15] = "writer";
         var0[16] = "plus";
         var0[17] = "<$constructor$>";
         var0[18] = "flush";
         var0[19] = "writer";
         var0[20] = "println";
         var0[21] = "out";
         var0[22] = "io";
         var0[23] = "file";
         var0[24] = "file";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[25];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure1 = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand$_closure1");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$util$Date() {
         Class var10000 = $class$java$util$Date;
         if (var10000 == null) {
            var10000 = $class$java$util$Date = class$("java.util.Date");
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
      private static Class $get$$class$java$io$File() {
         Class var10000 = $class$java$io$File;
         if (var10000 == null) {
            var10000 = $class$java$io$File = class$("java.io.File");
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
      static Class class$(String var0) {
         try {
            return Class.forName(var0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }
   }

   class _closure2 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2;
      // $FF: synthetic field
      private static Class $class$java$util$Date;
      // $FF: synthetic field
      private static Class $class$java$io$File;
      // $FF: synthetic field
      private static Class $class$java$io$PrintWriter;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         if (!DefaultTypeTransformation.booleanUnbox(var2[0].callGroovyObjectGetProperty(this))) {
            var2[1].callCurrent(this, (Object)"Not recording");
         }

         var2[2].call(var2[3].callGroovyObjectGetProperty(this), var2[4].call("// CLOSED: ", (Object)var2[5].callConstructor($get$$class$java$util$Date())));
         var2[6].call(var2[7].callGroovyObjectGetProperty(this));
         var2[8].call(var2[9].callGroovyObjectGetProperty(this));
         ScriptBytecodeAdapter.setGroovyObjectProperty((PrintWriter)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$io$PrintWriter()), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2(), this, "writer");
         var2[10].call(var2[11].callGetProperty(var2[12].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var2[13].callGroovyObjectGetProperty(this), var2[14].call(var2[15].callGroovyObjectGetProperty(this))}, new String[]{"Recording stopped; session saved as: ", " (", " bytes)"})));
         Object tmp = var2[16].callGroovyObjectGetProperty(this);
         ScriptBytecodeAdapter.setGroovyObjectProperty((File)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$io$File()), $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2(), this, "file");
         return tmp;
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[17].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2()) {
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
         var0[0] = "recording";
         var0[1] = "fail";
         var0[2] = "println";
         var0[3] = "writer";
         var0[4] = "plus";
         var0[5] = "<$constructor$>";
         var0[6] = "flush";
         var0[7] = "writer";
         var0[8] = "close";
         var0[9] = "writer";
         var0[10] = "println";
         var0[11] = "out";
         var0[12] = "io";
         var0[13] = "file";
         var0[14] = "length";
         var0[15] = "file";
         var0[16] = "file";
         var0[17] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[18];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2(), var0);
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
      private static Class $get$$class$java$lang$Object() {
         Class var10000 = $class$java$lang$Object;
         if (var10000 == null) {
            var10000 = $class$java$lang$Object = class$("java.lang.Object");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure2 = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand$_closure2");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$util$Date() {
         Class var10000 = $class$java$util$Date;
         if (var10000 == null) {
            var10000 = $class$java$util$Date = class$("java.util.Date");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$io$File() {
         Class var10000 = $class$java$io$File;
         if (var10000 == null) {
            var10000 = $class$java$io$File = class$("java.io.File");
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
      static Class class$(String var0) {
         try {
            return Class.forName(var0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }
   }

   class _closure3 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         if (!DefaultTypeTransformation.booleanUnbox(var2[0].callGroovyObjectGetProperty(this))) {
            var2[1].call(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this)), (Object)"Not recording");
            return null;
         } else {
            var2[4].call(var2[5].callGetProperty(var2[6].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var2[7].callGroovyObjectGetProperty(this), var2[8].call(var2[9].callGroovyObjectGetProperty(this))}, new String[]{"Recording to file: ", " (", " bytes)"})));
            return var2[10].callGroovyObjectGetProperty(this);
         }
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3()) {
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
         var0[0] = "recording";
         var0[1] = "println";
         var0[2] = "out";
         var0[3] = "io";
         var0[4] = "println";
         var0[5] = "out";
         var0[6] = "io";
         var0[7] = "file";
         var0[8] = "length";
         var0[9] = "file";
         var0[10] = "file";
         var0[11] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[12];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure3 = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand$_closure3");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$lang$Object() {
         Class var10000 = $class$java$lang$Object;
         if (var10000 == null) {
            var10000 = $class$java$lang$Object = class$("java.lang.Object");
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

   class _closure4 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         return DefaultTypeTransformation.booleanUnbox(var2[0].callGroovyObjectGetProperty(this)) ? var2[1].callCurrent(this) : null;
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4()) {
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
         var0[0] = "recording";
         var0[1] = "do_stop";
         var0[2] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[3];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$RecordCommand$_closure4 = class$("org.codehaus.groovy.tools.shell.commands.RecordCommand$_closure4");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$lang$Object() {
         Class var10000 = $class$java$lang$Object;
         if (var10000 == null) {
            var10000 = $class$java$lang$Object = class$("java.lang.Object");
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
