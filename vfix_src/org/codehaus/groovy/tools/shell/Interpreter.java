package org.codehaus.groovy.tools.shell;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.List;
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

public class Interpreter implements GroovyObject {
   private static final String SCRIPT_FILENAME = (String)"groovysh_evaluate";
   private final Logger log;
   private final GroovyShell shell;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204005L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204005 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Binding;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Parser;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Interpreter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyShell;

   public Interpreter(ClassLoader classLoader, Binding binding) {
      CallSite[] var3 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var3[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var3[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var4 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var4.record(classLoader, 8))) {
            var4.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert classLoader", var4), (Object)null);
         }
      } catch (Throwable var11) {
         var4.clear();
         throw var11;
      }

      ValueRecorder var5 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var5.record(binding, 8))) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert binding", var5), (Object)null);
         }
      } catch (Throwable var10) {
         var5.clear();
         throw var10;
      }

      this.shell = (GroovyShell)ScriptBytecodeAdapter.castToType(var3[2].callConstructor($get$$class$groovy$lang$GroovyShell(), classLoader, binding), $get$$class$groovy$lang$GroovyShell());
   }

   public Binding getContext() {
      CallSite[] var1 = $getCallSiteArray();
      return (Binding)ScriptBytecodeAdapter.castToType(var1[3].call(this.shell), $get$$class$groovy$lang$Binding());
   }

   public GroovyClassLoader getClassLoader() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyClassLoader)ScriptBytecodeAdapter.castToType(var1[4].call(this.shell), $get$$class$groovy$lang$GroovyClassLoader());
   }

   public Object evaluate(List buffer) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(buffer, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert buffer", var3), (Object)null);
         }
      } catch (Throwable var14) {
         var3.clear();
         throw var14;
      }

      Object source = var2[5].call(buffer, (Object)var2[6].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$Parser()));
      Object result = null;
      Reference type = new Reference((Class)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Class()));
      boolean var11 = false;

      try {
         var11 = true;
         Script script = (Script)ScriptBytecodeAdapter.castToType(var2[7].call(this.shell, source, SCRIPT_FILENAME), $get$$class$groovy$lang$Script());
         type.set((Class)ScriptBytecodeAdapter.castToType(var2[8].call(script), $get$$class$java$lang$Class()));
         var2[9].call(this.log, (Object)(new GStringImpl(new Object[]{script}, new String[]{"Compiled script: ", ""})));
         if (DefaultTypeTransformation.booleanUnbox(var2[10].call(var2[11].callGetProperty(type.get()), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(itx.get()), "main") ? Boolean.TRUE : Boolean.FALSE;
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1()) {
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
               var0[0] = "name";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure1 = class$("org.codehaus.groovy.tools.shell.Interpreter$_evaluate_closure1");
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
         })))) {
            result = var2[12].call(script);
         }

         var2[13].call(this.log, (Object)(new GStringImpl(new Object[]{var2[14].call($get$$class$java$lang$String(), (Object)result), var2[15].callSafe(result)}, new String[]{"Evaluation result: ", " (", ")"})));
         var2[16].call(var2[17].callGetProperty(type.get()), (Object)(new GeneratedClosure(this, this, type) {
            private Reference<T> type;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$runtime$MethodClosure;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2;
            // $FF: synthetic field
            private static Class $class$java$lang$Class;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.type = (Reference)type;
            }

            public Object doCall(Method m) {
               Method mx = new Reference(m);
               CallSite[] var3 = $getCallSiteArray();
               if (!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.isCase(var3[0].callGetProperty(mx.get()), ScriptBytecodeAdapter.createList(new Object[]{"main", "run"})) && !DefaultTypeTransformation.booleanUnbox(var3[1].call(var3[2].callGetProperty(mx.get()), (Object)"super$")) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var3[3].call(var3[4].callGetProperty(mx.get()), (Object)"class$")) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var3[5].call(var3[6].callGetProperty(mx.get()), (Object)"$")) ? Boolean.FALSE : Boolean.TRUE)) {
                  var3[7].call(var3[8].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{mx.get()}, new String[]{"Saving method definition: ", ""})));
                  CallSite var10000 = var3[9];
                  Object var10001 = var3[10].callGroovyObjectGetProperty(this);
                  GStringImpl var10002 = new GStringImpl(new Object[]{var3[11].callGetProperty(mx.get())}, new String[]{"", ""});
                  Object var4 = var3[12].callConstructor($get$$class$org$codehaus$groovy$runtime$MethodClosure(), var3[13].call(this.type.get()), var3[14].callGetProperty(mx.get()));
                  var10000.call(var10001, var10002, var4);
                  return var4;
               } else {
                  return null;
               }
            }

            public Object call(Method m) {
               Method mx = new Reference(m);
               CallSite[] var3 = $getCallSiteArray();
               return var3[15].callCurrent(this, (Object)mx.get());
            }

            public Class getType() {
               CallSite[] var1 = $getCallSiteArray();
               return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2()) {
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
               var0[0] = "name";
               var0[1] = "startsWith";
               var0[2] = "name";
               var0[3] = "startsWith";
               var0[4] = "name";
               var0[5] = "startsWith";
               var0[6] = "name";
               var0[7] = "debug";
               var0[8] = "log";
               var0[9] = "putAt";
               var0[10] = "context";
               var0[11] = "name";
               var0[12] = "<$constructor$>";
               var0[13] = "newInstance";
               var0[14] = "name";
               var0[15] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[16];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$runtime$MethodClosure() {
               Class var10000 = $class$org$codehaus$groovy$runtime$MethodClosure;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$runtime$MethodClosure = class$("org.codehaus.groovy.runtime.MethodClosure");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter$_evaluate_closure2 = class$("org.codehaus.groovy.tools.shell.Interpreter$_evaluate_closure2");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$Class() {
               Class var10000 = $class$java$lang$Class;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Class = class$("java.lang.Class");
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
         var11 = false;
      } finally {
         if (var11) {
            Object cache = var2[23].callGetProperty(var2[24].callGroovyObjectGetProperty(this));
            var2[25].call(cache, var2[26].callGetPropertySafe(type.get()));
            var2[27].call(cache, (Object)"$_run_closure");
         }
      }

      Object cache = var2[18].callGetProperty(var2[19].callGroovyObjectGetProperty(this));
      var2[20].call(cache, var2[21].callGetPropertySafe(type.get()));
      var2[22].call(cache, (Object)"$_run_closure");
      return result;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Interpreter()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$Interpreter();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$Interpreter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$Interpreter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public static final String getSCRIPT_FILENAME() {
      return SCRIPT_FILENAME;
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
      var0[2] = "<$constructor$>";
      var0[3] = "getContext";
      var0[4] = "getClassLoader";
      var0[5] = "join";
      var0[6] = "NEWLINE";
      var0[7] = "parse";
      var0[8] = "getClass";
      var0[9] = "debug";
      var0[10] = "any";
      var0[11] = "declaredMethods";
      var0[12] = "run";
      var0[13] = "debug";
      var0[14] = "valueOf";
      var0[15] = "getClass";
      var0[16] = "each";
      var0[17] = "declaredMethods";
      var0[18] = "classCache";
      var0[19] = "classLoader";
      var0[20] = "remove";
      var0[21] = "name";
      var0[22] = "remove";
      var0[23] = "classCache";
      var0[24] = "classLoader";
      var0[25] = "remove";
      var0[26] = "name";
      var0[27] = "remove";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[28];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Interpreter(), var0);
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
   private static Class $get$$class$groovy$lang$Binding() {
      Class var10000 = $class$groovy$lang$Binding;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Binding = class$("groovy.lang.Binding");
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
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Parser() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Parser;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Parser = class$("org.codehaus.groovy.tools.shell.Parser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Interpreter() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter = class$("org.codehaus.groovy.tools.shell.Interpreter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
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
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyShell() {
      Class var10000 = $class$groovy$lang$GroovyShell;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyShell = class$("groovy.lang.GroovyShell");
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
