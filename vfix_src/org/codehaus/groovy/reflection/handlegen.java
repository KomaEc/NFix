package org.codehaus.groovy.reflection;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class handlegen extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205453L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205453 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$reflection$handlegen;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public handlegen() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public handlegen(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$org$codehaus$groovy$reflection$handlegen(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object types = new Reference(ScriptBytecodeAdapter.createList(new Object[]{"boolean", "char", "byte", "short", "int", "long", "float", "double", "Object"}));
      return var1[1].call(types.get(), (Object)(new GeneratedClosure(this, this, types) {
         private Reference<T> types;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$reflection$handlegen$_run_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.types = (Reference)types;
         }

         public Object doCall(Object arg1) {
            Object arg1x = new Reference(arg1);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{arg1x.get()}, new String[]{"public Object invoke(Object receiver, ", " arg1) throws Throwable { return invoke(receiver,ArrayUtil.createArray(arg1)); }"})));
            CallSite var10000 = var3[1];
            Object var10001 = this.types.get();
            Object var10005 = this.getThisObject();
            Reference types = this.types;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, arg1x, types) {
               private Reference<T> arg1;
               private Reference<T> types;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2;

               public {
                  Reference typesx = new Reference(types);
                  CallSite[] var6 = $getCallSiteArray();
                  this.arg1 = (Reference)arg1;
                  this.types = (Reference)((Reference)typesx.get());
               }

               public Object doCall(Object arg2) {
                  Object arg2x = new Reference(arg2);
                  CallSite[] var3 = $getCallSiteArray();
                  var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{this.arg1.get(), arg2x.get()}, new String[]{"public Object invoke(Object receiver, ", " arg1, ", " arg2) throws Throwable { return invoke(receiver,ArrayUtil.createArray(arg1,arg2)); }"})));
                  CallSite var10000 = var3[1];
                  Object var10001 = this.types.get();
                  Object var10005 = this.getThisObject();
                  Reference arg1 = this.arg1;
                  return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, arg2x, arg1) {
                     private Reference<T> arg2;
                     private Reference<T> arg1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3;

                     public {
                        Reference arg1x = new Reference(arg1);
                        CallSite[] var6 = $getCallSiteArray();
                        this.arg2 = (Reference)arg2;
                        this.arg1 = (Reference)((Reference)arg1x.get());
                     }

                     public Object doCall(Object arg3) {
                        Object arg3x = new Reference(arg3);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{this.arg1.get(), this.arg2.get(), arg3x.get()}, new String[]{"public Object invoke(Object receiver, ", " arg1, ", " arg2, ", " arg3) throws Throwable { return invoke(receiver,ArrayUtil.createArray(arg1,arg2,arg3)); }"})));
                     }

                     public Object getArg2() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.arg2.get();
                     }

                     public Object getArg1() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.arg1.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3()) {
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
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[1];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3() {
                        Class var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2_closure3 = class$("org.codehaus.groovy.reflection.handlegen$_run_closure1_closure2_closure3");
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
               }

               public Object getArg1() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.arg1.get();
               }

               public Object getTypes() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.types.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2()) {
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
                  var0[1] = "each";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1_closure2 = class$("org.codehaus.groovy.reflection.handlegen$_run_closure1_closure2");
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
         }

         public Object getTypes() {
            CallSite[] var1 = $getCallSiteArray();
            return this.types.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1()) {
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
            var0[1] = "each";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$reflection$handlegen$_run_closure1() {
            Class var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$reflection$handlegen$_run_closure1 = class$("org.codehaus.groovy.reflection.handlegen$_run_closure1");
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
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$reflection$handlegen()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$reflection$handlegen();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$reflection$handlegen(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$reflection$handlegen(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "each";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[2];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$reflection$handlegen(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$reflection$handlegen() {
      Class var10000 = $class$org$codehaus$groovy$reflection$handlegen;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$reflection$handlegen = class$("org.codehaus.groovy.reflection.handlegen");
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
