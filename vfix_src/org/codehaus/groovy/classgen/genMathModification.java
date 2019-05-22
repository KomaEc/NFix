package org.codehaus.groovy.classgen;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class genMathModification extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205445L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205445 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$classgen$genMathModification;

   public genMathModification() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public genMathModification(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$org$codehaus$groovy$classgen$genMathModification(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      Object ops = ScriptBytecodeAdapter.createList(new Object[]{"plus", "minus", "multiply", "div", "or", "and", "xor", "intdiv", "mod", "leftShift", "rightShift", "rightShiftUnsigned"});
      Object numbers = new Reference(ScriptBytecodeAdapter.createMap(new Object[]{"Byte", "byte", "Short", "short", "Integer", "int", "Long", "long", "Float", "float", "Double", "double"}));
      var1[1].call(ops, (Object)(new GeneratedClosure(this, this, numbers) {
         private Reference<T> numbers;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.numbers = (Reference)numbers;
         }

         public Object doCall(Object op) {
            Object opx = new Reference(op);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(this.numbers.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), opx) {
               private Reference<T> op;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.op = (Reference)op;
               }

               public Object doCall(Object wrappedType, Object typex) {
                  Object type = new Reference(typex);
                  CallSite[] var4 = $getCallSiteArray();
                  return var4[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{type.get(), this.op.get()}, new String[]{"public boolean ", "_", ";"})));
               }

               public Object call(Object wrappedType, Object typex) {
                  Object type = new Reference(typex);
                  CallSite[] var4 = $getCallSiteArray();
                  return var4[1].callCurrent(this, wrappedType, type.get());
               }

               public Object getOp() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.op.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4()) {
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
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4() {
                  Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1_closure4 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure1_closure4");
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

         public Object getNumbers() {
            CallSite[] var1 = $getCallSiteArray();
            return this.numbers.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1()) {
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
            var0[0] = "each";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure1() {
            Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure1 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure1");
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
      var1[2].call(ops, (Object)(new GeneratedClosure(this, this, numbers) {
         private Reference<T> numbers;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.numbers = (Reference)numbers;
         }

         public Object doCall(Object op) {
            Object opx = new Reference(op);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{opx.get()}, new String[]{"if (\"", "\".equals(name)) {"})));
            var3[1].call(this.numbers.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), opx) {
               private Reference<T> op;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.op = (Reference)op;
               }

               public Object doCall(Object wrappedType, Object type) {
                  Object wrappedTypex = new Reference(wrappedType);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{wrappedTypex.get(), typex.get(), this.op.get()}, new String[]{"if (klazz==", ".class) {\n                ", "_", " = true;\n            }"})));
               }

               public Object call(Object wrappedType, Object type) {
                  Object wrappedTypex = new Reference(wrappedType);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, wrappedTypex.get(), typex.get());
               }

               public Object getOp() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.op.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5()) {
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
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5() {
                  Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure5 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure2_closure5");
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
            var3[2].callCurrent(this, (Object)"if (klazz==Object.class) {");
            var3[3].call(this.numbers.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), opx) {
               private Reference<T> op;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.op = (Reference)op;
               }

               public Object doCall(Object wrappedType, Object typex) {
                  Object type = new Reference(typex);
                  CallSite[] var4 = $getCallSiteArray();
                  return var4[0].callCurrent(this, (Object)(new GStringImpl(new Object[]{type.get(), this.op.get()}, new String[]{"", "_", " = true;"})));
               }

               public Object call(Object wrappedType, Object typex) {
                  Object type = new Reference(typex);
                  CallSite[] var4 = $getCallSiteArray();
                  return var4[1].callCurrent(this, wrappedType, type.get());
               }

               public Object getOp() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.op.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6()) {
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
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6() {
                  Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2_closure6 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure2_closure6");
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
            var3[4].callCurrent(this, (Object)"}");
            return var3[5].callCurrent(this, (Object)"}");
         }

         public Object getNumbers() {
            CallSite[] var1 = $getCallSiteArray();
            return this.numbers.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2()) {
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
            var0[2] = "println";
            var0[3] = "each";
            var0[4] = "println";
            var0[5] = "println";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure2() {
            Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure2 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure2");
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
      return var1[3].call(ops, (Object)(new GeneratedClosure(this, this, numbers) {
         private Reference<T> numbers;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.numbers = (Reference)numbers;
         }

         public Object doCall(Object op) {
            Object opx = new Reference(op);
            CallSite[] var3 = $getCallSiteArray();
            CallSite var10000 = var3[0];
            Object var10001 = this.numbers.get();
            Object var10005 = this.getThisObject();
            Reference numbers = this.numbers;
            return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, opx, numbers) {
               private Reference<T> op;
               private Reference<T> numbers;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7;

               public {
                  Reference numbersx = new Reference(numbers);
                  CallSite[] var6 = $getCallSiteArray();
                  this.op = (Reference)op;
                  this.numbers = (Reference)((Reference)numbersx.get());
               }

               public Object doCall(Object wrappedType1, Object type1) {
                  Object wrappedType1x = new Reference(wrappedType1);
                  Object type1x = new Reference(type1);
                  CallSite[] var5 = $getCallSiteArray();
                  CallSite var10000 = var5[0];
                  Object var10001 = this.numbers.get();
                  Object var10005 = this.getThisObject();
                  Reference op = this.op;
                  return var10000.call(var10001, (Object)(new GeneratedClosure(this, var10005, op, wrappedType1x, type1x) {
                     private Reference<T> op;
                     private Reference<T> wrappedType1;
                     private Reference<T> type1;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8;

                     public {
                        Reference op = new Reference(opx);
                        CallSite[] var7 = $getCallSiteArray();
                        this.op = (Reference)((Reference)op.get());
                        this.wrappedType1 = (Reference)wrappedType1;
                        this.type1 = (Reference)type1;
                     }

                     public Object doCall(Object wrappedType2, Object type2) {
                        Object wrappedType2x = new Reference(wrappedType2);
                        Object type2x = new Reference(type2);
                        CallSite[] var5 = $getCallSiteArray();
                        Object math = new Reference(var5[0].callCurrent(this, this.wrappedType1.get(), wrappedType2x.get()));
                        if (DefaultTypeTransformation.booleanUnbox(var5[1].call(math.get(), this.op.get()))) {
                           var5[2].callCurrent(this, (Object)(new GStringImpl(new Object[]{var5[3].callGetProperty(math.get()), this.op.get(), this.type1.get(), type2x.get(), this.type1.get(), this.op.get(), this.op.get(), ScriptBytecodeAdapter.compareNotEqual(var5[4].callGetProperty(math.get()), this.type1.get()) ? var5[5].call(var5[6].call("((", (Object)var5[7].callGetProperty(math.get())), (Object)")op1)") : "op1", var5[8].call(math.get(), this.op.get()), ScriptBytecodeAdapter.compareNotEqual(var5[9].callGetProperty(math.get()), type2x.get()) ? var5[10].call(var5[11].call("((", (Object)var5[12].callGetProperty(math.get())), (Object)")op2)") : "op2"}, new String[]{"public static ", " ", "(", " op1, ", " op2) {\n                   if (instance.", "_", ") {\n                      return ", "Slow(op1, op2);\n                   }\n                   else {\n                      return ", " ", " ", ";\n                   }\n                }"})));
                           return var5[13].callCurrent(this, (Object)(new GStringImpl(new Object[]{var5[14].callGetProperty(math.get()), this.op.get(), this.type1.get(), type2x.get(), this.op.get(), var5[15].callGetProperty(math.get())}, new String[]{"private static ", " ", "Slow(", " op1,", " op2) {\n                      return ((Number)InvokerHelper.invokeMethod(op1, \"", "\", op2)).", "Value();\n                }"})));
                        } else {
                           return null;
                        }
                     }

                     public Object call(Object wrappedType2, Object type2) {
                        Object wrappedType2x = new Reference(wrappedType2);
                        Object type2x = new Reference(type2);
                        CallSite[] var5 = $getCallSiteArray();
                        return var5[16].callCurrent(this, wrappedType2x.get(), type2x.get());
                     }

                     public Object getOp() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.op.get();
                     }

                     public Object getWrappedType1() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.wrappedType1.get();
                     }

                     public Object getType1() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.type1.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8()) {
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
                        var0[0] = "getMath";
                        var0[1] = "getAt";
                        var0[2] = "println";
                        var0[3] = "resType";
                        var0[4] = "resType";
                        var0[5] = "plus";
                        var0[6] = "plus";
                        var0[7] = "resType";
                        var0[8] = "getAt";
                        var0[9] = "resType";
                        var0[10] = "plus";
                        var0[11] = "plus";
                        var0[12] = "resType";
                        var0[13] = "println";
                        var0[14] = "resType";
                        var0[15] = "resType";
                        var0[16] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[17];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8() {
                        Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7_closure8 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure3_closure7_closure8");
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

               public Object call(Object wrappedType1, Object type1) {
                  Object wrappedType1x = new Reference(wrappedType1);
                  Object type1x = new Reference(type1);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, wrappedType1x.get(), type1x.get());
               }

               public Object getOp() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.op.get();
               }

               public Object getNumbers() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.numbers.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7()) {
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
                  var0[0] = "each";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7() {
                  Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3_closure7 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure3_closure7");
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

         public Object getNumbers() {
            CallSite[] var1 = $getCallSiteArray();
            return this.numbers.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3()) {
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
            var0[0] = "each";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification$_run_closure3() {
            Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$classgen$genMathModification$_run_closure3 = class$("org.codehaus.groovy.classgen.genMathModification$_run_closure3");
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

   public Object isFloatingPoint(Object number) {
      CallSite[] var2 = $getCallSiteArray();
      return !ScriptBytecodeAdapter.compareEqual(number, "Double") && !ScriptBytecodeAdapter.compareEqual(number, "Float") ? Boolean.FALSE : Boolean.TRUE;
   }

   public Object isLong(Object number) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.compareEqual(number, "Long") ? Boolean.TRUE : Boolean.FALSE;
   }

   public Object getMath(Object left, Object right) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var3[4].callCurrent(this, (Object)left)) && !DefaultTypeTransformation.booleanUnbox(var3[5].callCurrent(this, (Object)right)) ? Boolean.FALSE : Boolean.TRUE)) {
         return ScriptBytecodeAdapter.createMap(new Object[]{"resType", "double", "plus", "+", "minus", "-", "multiply", "*", "div", "/"});
      } else {
         return DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var3[6].callCurrent(this, (Object)left)) && !DefaultTypeTransformation.booleanUnbox(var3[7].callCurrent(this, (Object)right)) ? Boolean.FALSE : Boolean.TRUE) ? ScriptBytecodeAdapter.createMap(new Object[]{"resType", "long", "plus", "+", "minus", "-", "multiply", "*", "div", "/", "or", "|", "and", "&", "xor", "^", "intdiv", "/", "mod", "%", "leftShift", "<<", "rightShift", ">>", "rightShiftUnsigned", ">>>"}) : ScriptBytecodeAdapter.createMap(new Object[]{"resType", "int", "plus", "+", "minus", "-", "multiply", "*", "div", "/", "or", "|", "and", "&", "xor", "^", "intdiv", "/", "mod", "%", "leftShift", "<<", "rightShift", ">>", "rightShiftUnsigned", ">>>"});
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$classgen$genMathModification()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$classgen$genMathModification();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$classgen$genMathModification(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$classgen$genMathModification(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "each";
      var0[3] = "each";
      var0[4] = "isFloatingPoint";
      var0[5] = "isFloatingPoint";
      var0[6] = "isLong";
      var0[7] = "isLong";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[8];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$classgen$genMathModification(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$classgen$genMathModification() {
      Class var10000 = $class$org$codehaus$groovy$classgen$genMathModification;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$classgen$genMathModification = class$("org.codehaus.groovy.classgen.genMathModification");
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
