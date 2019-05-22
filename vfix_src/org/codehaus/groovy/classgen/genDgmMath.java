package org.codehaus.groovy.classgen;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class genDgmMath extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)256;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205441L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205441 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$classgen$genDgmMath;

   public genDgmMath() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public genDgmMath(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$org$codehaus$groovy$classgen$genDgmMath(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object types = new Reference(ScriptBytecodeAdapter.createList(new Object[]{"Integer", "Long", "Float", "Double"}));
      var1[1].callCurrent(this, (Object)"\npublic CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {\n    NumberMath m = NumberMath.getMath((Number)receiver, (Number)args[0]);\n");
      var1[2].call(types.get(), (Object)(new GeneratedClosure(this, this, types) {
         private Reference<T> types;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.types = (Reference)types;
         }

         public Object doCall(Object a) {
            Object ax = new Reference(a);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{ax.get()}, new String[]{"\n    if (receiver instanceof ", ") {"})));
            var3[1].call(this.types.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), ax) {
               private Reference<T> a;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.a = (Reference)a;
               }

               public Object doCall(Object b) {
                  Object bx = new Reference(b);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{bx.get(), var3[1].callCurrent(this, this.a.get(), bx.get()), this.a.get(), bx.get(), var3[2].callCurrent(this, this.a.get(), bx.get()), this.a.get(), bx.get()}, new String[]{"\n        if (args[0] instanceof ", ")\n            return new NumberNumberCallSite (site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]){\n                public final Object invoke(Object receiver, Object[] args) {\n                    return ", ".INSTANCE.addImpl((", ")receiver,(", ")args[0]);\n                }\n\n                public final Object invokeBinop(Object receiver, Object arg) {\n                    return ", ".INSTANCE.addImpl((", ")receiver,(", ")arg);\n                }\n            };\n        "})));
               }

               public Object getA() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.a.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2()) {
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
                  var0[0] = "print";
                  var0[1] = "getMath";
                  var0[2] = "getMath";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1_closure2 = class$("org.codehaus.groovy.classgen.genDgmMath$_run_closure1_closure2");
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
            return var3[2].callCurrent(this, (Object)"}");
         }

         public Object getTypes() {
            CallSite[] var1 = $getCallSiteArray();
            return this.types.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1()) {
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
            var0[0] = "print";
            var0[1] = "each";
            var0[2] = "println";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1() {
            Class var10000 = $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$classgen$genDgmMath$_run_closure1 = class$("org.codehaus.groovy.classgen.genDgmMath$_run_closure1");
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
      var1[3].callCurrent(this, (Object)"\n    return new NumberNumberCallSite (site, metaClass, metaMethod, params, (Number)receiver, (Number)args[0]){\n        public final Object invoke(Object receiver, Object[] args) {\n            return math.addImpl((Number)receiver,(Number)args[0]);\n        }\n\n        public final Object invokeBinop(Object receiver, Object arg) {\n            return math.addImpl((Number)receiver,(Number)arg);\n        }\n}\n");
      Object i = null;
      Object var4 = var1[4].call(ScriptBytecodeAdapter.createRange($const$0, $const$1, true));

      while(((Iterator)var4).hasNext()) {
         i = ((Iterator)var4).next();
         var1[5].callCurrent(this, (Object)(new GStringImpl(new Object[]{i}, new String[]{"public Object invoke", " (Object receiver, "})));
         Object j = null;
         Object var6 = var1[6].call(ScriptBytecodeAdapter.createRange($const$2, var1[7].call(i, (Object)$const$2), true));

         while(((Iterator)var6).hasNext()) {
            j = ((Iterator)var6).next();
            var1[8].callCurrent(this, (Object)(new GStringImpl(new Object[]{j}, new String[]{"Object a", ", "})));
         }

         var1[9].callCurrent(this, (Object)(new GStringImpl(new Object[]{i}, new String[]{"Object a", ") {"})));
         var1[10].callCurrent(this, (Object)"  return invoke (receiver, new Object[] {");
         j = null;
         var6 = var1[11].call(ScriptBytecodeAdapter.createRange($const$2, var1[12].call(i, (Object)$const$2), true));

         while(((Iterator)var6).hasNext()) {
            j = ((Iterator)var6).next();
            var1[13].callCurrent(this, (Object)(new GStringImpl(new Object[]{j}, new String[]{"a", ", "})));
         }

         var1[14].callCurrent(this, (Object)(new GStringImpl(new Object[]{i}, new String[]{"a", "} );"})));
         var1[15].callCurrent(this, (Object)"}");
      }

      return null;
   }

   public Object getMath(Object a, Object b) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(a, "Double") && !ScriptBytecodeAdapter.compareEqual(b, "Double") ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(a, "Float") ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(b, "Float") ? Boolean.FALSE : Boolean.TRUE)) {
         return "FloatingPointMath";
      } else {
         return DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(a, "Long") && !ScriptBytecodeAdapter.compareEqual(b, "Long") ? Boolean.FALSE : Boolean.TRUE) ? "LongMath" : "IntegerMath";
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genDgmMath()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$classgen$genDgmMath();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$classgen$genDgmMath(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$classgen$genDgmMath(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$println() {
      super.println();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$print(Object var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object[] var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$evaluate(String var1) {
      return super.evaluate(var1);
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
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Binding super$3$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setBinding(Binding var1) {
      super.setBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$3$run(File var1, String[] var2) {
      super.run(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$evaluate(File var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$3$println(Object var1) {
      super.println(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "runScript";
      var0[1] = "println";
      var0[2] = "each";
      var0[3] = "println";
      var0[4] = "iterator";
      var0[5] = "print";
      var0[6] = "iterator";
      var0[7] = "minus";
      var0[8] = "print";
      var0[9] = "println";
      var0[10] = "print";
      var0[11] = "iterator";
      var0[12] = "minus";
      var0[13] = "print";
      var0[14] = "println";
      var0[15] = "println";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[16];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genDgmMath(), var0);
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
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
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
   private static Class $get$$class$org$codehaus$groovy$classgen$genDgmMath() {
      Class var10000 = $class$org$codehaus$groovy$classgen$genDgmMath;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$classgen$genDgmMath = class$("org.codehaus.groovy.classgen.genDgmMath");
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
