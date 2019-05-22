package org.codehaus.groovy.classgen;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class genArrayAccess extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205406L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205406 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$classgen$genArrayAccess;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public genArrayAccess() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public genArrayAccess(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$org$codehaus$groovy$classgen$genArrayAccess(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      return var1[1].callCurrent(this, (Object)(new GStringImpl(new Object[]{var1[2].callCurrent(this)}, new String[]{"\npackage org.codehaus.groovy.runtime.dgmimpl;\n\nimport groovy.lang.MetaClassImpl;\nimport groovy.lang.MetaMethod;\nimport org.codehaus.groovy.runtime.callsite.CallSite;\nimport org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite;\nimport org.codehaus.groovy.reflection.CachedClass;\nimport org.codehaus.groovy.reflection.ReflectionCache;\n\npublic class ArrayOperations {\n  ", "\n}\n"})));
   }

   public Object genInners() {
      CallSite[] var1 = $getCallSiteArray();
      Object res = new Reference("");
      Map primitives = ScriptBytecodeAdapter.createMap(new Object[]{"boolean", "Boolean", "byte", "Byte", "char", "Character", "short", "Short", "int", "Integer", "long", "Long", "float", "Float", "double", "Double"});
      var1[3].call(primitives, (Object)(new GeneratedClosure(this, this, res) {
         private Reference<T> res;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.res = (Reference)res;
         }

         public Object doCall(Object primName, Object clsName) {
            Object primNamex = new Reference(primName);
            Object clsNamex = new Reference(clsName);
            CallSite[] var5 = $getCallSiteArray();
            Object var10000 = var5[0].call(this.res.get(), (Object)(new GStringImpl(new Object[]{clsNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get(), primNamex.get(), clsNamex.get(), primNamex.get()}, new String[]{"\n         public static class ", "ArrayGetAtMetaMethod extends ArrayGetAtMetaMethod {\n            private static final CachedClass ARR_CLASS = ReflectionCache.getCachedClass(", "[].class);\n\n            public Class getReturnType() {\n                return ", ".class;\n            }\n\n            public final CachedClass getDeclaringClass() {\n                return ARR_CLASS;\n            }\n\n            public Object invoke(Object object, Object[] args) {\n                final ", "[] objects = (", "[]) object;\n                return objects[normaliseIndex(((Integer) args[0]).intValue(), objects.length)];\n            }\n\n            public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {\n                if (!(args [0] instanceof Integer))\n                  return PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args);\n                else\n                    return new PojoMetaMethodSite(site, metaClass, metaMethod, params) {\n                        public Object invoke(Object receiver, Object[] args) {\n                            final ", "[] objects = (", "[]) receiver;\n                            return objects[normaliseIndex(((Integer) args[0]).intValue(), objects.length)];\n                        }\n\n                        public Object callBinop(Object receiver, Object arg) {\n                            if ((receiver instanceof ", "[] && arg instanceof Integer)\n                                    && checkMetaClass()) {\n                                final ", "[] objects = (", "[]) receiver;\n                                return objects[normaliseIndex(((Integer) arg).intValue(), objects.length)];\n                            }\n                            else\n                              return super.callBinop(receiver,arg);\n                        }\n\n                        public Object invokeBinop(Object receiver, Object arg) {\n                            final ", "[] objects = (", "[]) receiver;\n                            return objects[normaliseIndex(((Integer) arg).intValue(), objects.length)];\n                        }\n                    };\n            }\n         }\n\n\n        public static class ", "ArrayPutAtMetaMethod extends ArrayPutAtMetaMethod {\n            private static final CachedClass OBJECT_CLASS = ReflectionCache.OBJECT_CLASS;\n            private static final CachedClass ARR_CLASS = ReflectionCache.getCachedClass(", "[].class);\n            private static final CachedClass [] PARAM_CLASS_ARR = new CachedClass[] {INTEGER_CLASS, OBJECT_CLASS};\n\n            public ", "ArrayPutAtMetaMethod() {\n                parameterTypes = PARAM_CLASS_ARR;\n            }\n\n            public final CachedClass getDeclaringClass() {\n                return ARR_CLASS;\n            }\n\n            public Object invoke(Object object, Object[] args) {\n                final ", "[] objects = (", "[]) object;\n                final int index = normaliseIndex(((Integer) args[0]).intValue(), objects.length);\n                Object newValue = args[1];\n                if (!(newValue instanceof ", ")) {\n                    Number n = (Number) newValue;\n                    objects[index] = ((Number)newValue).", "Value();\n                }\n                else\n                  objects[index] = ((", ")args[1]).", "Value();\n                return null;\n            }\n\n            public CallSite createPojoCallSite(CallSite site, MetaClassImpl metaClass, MetaMethod metaMethod, Class[] params, Object receiver, Object[] args) {\n                if (!(args [0] instanceof Integer) || !(args [1] instanceof ", "))\n                  return PojoMetaMethodSite.createNonAwareCallSite(site, metaClass, metaMethod, params, args);\n                else\n                    return new PojoMetaMethodSite(site, metaClass, metaMethod, params) {\n                        public Object call(Object receiver, Object[] args) {\n                            if ((receiver instanceof ", "[] && args[0] instanceof Integer && args[1] instanceof ", " )\n                                    && checkMetaClass()) {\n                                final ", "[] objects = (", "[]) receiver;\n                                objects[normaliseIndex(((Integer) args[0]).intValue(), objects.length)] = ((", ")args[1]).", "Value();\n                                return null;\n                            }\n                            else\n                              return super.call(receiver,args);\n                        }\n                    };\n            }\n        }\n\n       "})));
            this.res.set(var10000);
            return var10000;
         }

         public Object call(Object primName, Object clsName) {
            Object primNamex = new Reference(primName);
            Object clsNamex = new Reference(clsName);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, primNamex.get(), clsNamex.get());
         }

         public Object getRes() {
            CallSite[] var1 = $getCallSiteArray();
            return this.res.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1()) {
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
            var0[0] = "plus";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1() {
            Class var10000 = $class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$classgen$genArrayAccess$_genInners_closure1 = class$("org.codehaus.groovy.classgen.genArrayAccess$_genInners_closure1");
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
      return res.get();
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genArrayAccess()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$classgen$genArrayAccess();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$classgen$genArrayAccess(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$classgen$genArrayAccess(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "genInners";
      var0[3] = "each";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[4];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genArrayAccess(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$classgen$genArrayAccess() {
      Class var10000 = $class$org$codehaus$groovy$classgen$genArrayAccess;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$classgen$genArrayAccess = class$("org.codehaus.groovy.classgen.genArrayAccess");
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
}
