package groovy.swing.binding;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AbstractSyntheticMetaMethods implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203044L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203044 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClassRegistry;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovySystem;
   // $FF: synthetic field
   private static Class $class$groovy$lang$ExpandoMetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$AbstractSyntheticMetaMethods;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;

   public AbstractSyntheticMetaMethods() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void enhance(Object o, Map enhancedMethods) {
      CallSite[] var2 = $getCallSiteArray();
      Class klass = (Class)ScriptBytecodeAdapter.castToType(var2[0].call(o), $get$$class$java$lang$Class());
      MetaClassRegistry mcr = (MetaClassRegistry)ScriptBytecodeAdapter.castToType(var2[1].callGetProperty($get$$class$groovy$lang$GroovySystem()), $get$$class$groovy$lang$MetaClassRegistry());
      MetaClass mc = new Reference((MetaClass)ScriptBytecodeAdapter.castToType(var2[2].call(mcr, (Object)klass), $get$$class$groovy$lang$MetaClass()));
      Boolean init = Boolean.FALSE;
      var2[3].call(mcr, (Object)klass);
      mc.set(var2[4].callConstructor($get$$class$groovy$lang$ExpandoMetaClass(), (Object)klass));
      init = Boolean.TRUE;
      var2[5].call(enhancedMethods, (Object)(new GeneratedClosure($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), mc) {
         private Reference<T> mc;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$lang$MetaClass;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.mc = (Reference)mc;
         }

         public Object doCall(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareEqual(var5[0].call(this.mc.get(), kx.get()), (Object)null) ? var5[1].call(this.mc.get(), kx.get(), vx.get()) : null;
         }

         public Object call(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return var5[2].callCurrent(this, kx.get(), vx.get());
         }

         public MetaClass getMc() {
            CallSite[] var1 = $getCallSiteArray();
            return (MetaClass)ScriptBytecodeAdapter.castToType(this.mc.get(), $get$$class$groovy$lang$MetaClass());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1()) {
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
            var0[0] = "getMetaMethod";
            var0[1] = "registerInstanceMethod";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1(), var0);
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
         private static Class $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1() {
            Class var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods$_enhance_closure1 = class$("groovy.swing.binding.AbstractSyntheticMetaMethods$_enhance_closure1");
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
      if (DefaultTypeTransformation.booleanUnbox(init)) {
         var2[6].call(mc.get());
         var2[7].call(mcr, klass, mc.get());
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods()) {
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
      Class var10000 = $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "getClass";
      var0[1] = "metaClassRegistry";
      var0[2] = "getMetaClass";
      var0[3] = "removeMetaClass";
      var0[4] = "<$constructor$>";
      var0[5] = "each";
      var0[6] = "initialize";
      var0[7] = "setMetaClass";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[8];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClassRegistry() {
      Class var10000 = $class$groovy$lang$MetaClassRegistry;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClassRegistry = class$("groovy.lang.MetaClassRegistry");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovySystem() {
      Class var10000 = $class$groovy$lang$GroovySystem;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovySystem = class$("groovy.lang.GroovySystem");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$ExpandoMetaClass() {
      Class var10000 = $class$groovy$lang$ExpandoMetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$ExpandoMetaClass = class$("groovy.lang.ExpandoMetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods() {
      Class var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$AbstractSyntheticMetaMethods = class$("groovy.swing.binding.AbstractSyntheticMetaMethods");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
