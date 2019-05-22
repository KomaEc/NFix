package groovy.swing.binding;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import javax.swing.JList;
import javax.swing.ListModel;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JListMetaMethods implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203065L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203065 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$AbstractSyntheticMetaMethods;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListMetaMethods;

   public JListMetaMethods() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void enhanceMetaClass(JList list) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$groovy$swing$binding$AbstractSyntheticMetaMethods(), list, ScriptBytecodeAdapter.createMap(new Object[]{"getElements", new GeneratedClosure($get$$class$groovy$swing$binding$JListMetaMethods(), $get$$class$groovy$swing$binding$JListMetaMethods()) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$javax$swing$ListModel;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            ListModel model = new Reference((ListModel)ScriptBytecodeAdapter.castToType(var1[0].callGetProperty(var1[1].callGroovyObjectGetProperty(this)), $get$$class$javax$swing$ListModel()));
            Object results = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
            Integer size = new Reference((Integer)ScriptBytecodeAdapter.castToType(var1[2].callGetProperty(model.get()), $get$$class$java$lang$Integer()));
            Reference i = new Reference($const$0);

            while(ScriptBytecodeAdapter.compareLessThan(i.get(), size.get())) {
               results.set(var1[3].call(results.get(), var1[4].call(model.get(), i.get())));
               Object var6 = i.get();
               i.set(var1[5].call(i.get()));
            }

            return results.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1()) {
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
            var0[0] = "model";
            var0[1] = "delegate";
            var0[2] = "size";
            var0[3] = "plus";
            var0[4] = "getElementAt";
            var0[5] = "next";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1(), var0);
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
         private static Class $get$$class$java$lang$Integer() {
            Class var10000 = $class$java$lang$Integer;
            if (var10000 == null) {
               var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$javax$swing$ListModel() {
            Class var10000 = $class$javax$swing$ListModel;
            if (var10000 == null) {
               var10000 = $class$javax$swing$ListModel = class$("javax.swing.ListModel");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1() {
            Class var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure1 = class$("groovy.swing.binding.JListMetaMethods$_enhanceMetaClass_closure1");
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
      }, "getSelectedElement", new GeneratedClosure($get$$class$groovy$swing$binding$JListMetaMethods(), $get$$class$groovy$swing$binding$JListMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callGetProperty(var1[1].callGroovyObjectGetProperty(this));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2()) {
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
            var0[0] = "selectedValue";
            var0[1] = "delegate";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2() {
            Class var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure2 = class$("groovy.swing.binding.JListMetaMethods$_enhanceMetaClass_closure2");
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
      }, "getSelectedElements", new GeneratedClosure($get$$class$groovy$swing$binding$JListMetaMethods(), $get$$class$groovy$swing$binding$JListMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callGetProperty(var1[1].callGroovyObjectGetProperty(this));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3()) {
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
            var0[0] = "selectedValues";
            var0[1] = "delegate";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3() {
            Class var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure3 = class$("groovy.swing.binding.JListMetaMethods$_enhanceMetaClass_closure3");
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
      }, "setSelectedElement", new GeneratedClosure($get$$class$groovy$swing$binding$JListMetaMethods(), $get$$class$groovy$swing$binding$JListMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object item) {
            Object itemx = new Reference(item);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(var3[1].callGroovyObjectGetProperty(this), itemx.get(), Boolean.TRUE);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4()) {
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
            var0[0] = "setSelectedValue";
            var0[1] = "delegate";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4() {
            Class var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure4 = class$("groovy.swing.binding.JListMetaMethods$_enhanceMetaClass_closure4");
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
      }, "setSelectedValue", new GeneratedClosure($get$$class$groovy$swing$binding$JListMetaMethods(), $get$$class$groovy$swing$binding$JListMetaMethods()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object item) {
            Object itemx = new Reference(item);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(var3[1].callGroovyObjectGetProperty(this), itemx.get(), Boolean.TRUE);
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5()) {
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
            var0[0] = "setSelectedValue";
            var0[1] = "delegate";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5(), var0);
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
         private static Class $get$$class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5() {
            Class var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$binding$JListMetaMethods$_enhanceMetaClass_closure5 = class$("groovy.swing.binding.JListMetaMethods$_enhanceMetaClass_closure5");
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
      }}));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$JListMetaMethods()) {
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
      Class var10000 = $get$$class$groovy$swing$binding$JListMetaMethods();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$JListMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$JListMetaMethods(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "enhance";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[1];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$JListMetaMethods(), var0);
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
   private static Class $get$$class$groovy$swing$binding$JListMetaMethods() {
      Class var10000 = $class$groovy$swing$binding$JListMetaMethods;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListMetaMethods = class$("groovy.swing.binding.JListMetaMethods");
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
