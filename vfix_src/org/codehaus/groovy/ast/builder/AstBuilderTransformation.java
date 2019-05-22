package org.codehaus.groovy.ast.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(
   phase = CompilePhase.SEMANTIC_ANALYSIS
)
public class AstBuilderTransformation implements ASTTransformation, GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203722L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203722 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation;

   public AstBuilderTransformation() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
      CallSite[] var3 = $getCallSiteArray();
      Object transformer = new Reference(var3[0].callConstructor($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), var3[1].callGetProperty(var3[2].call(sourceUnit)), var3[3].callGetProperty(var3[4].call(sourceUnit)), var3[5].callGetProperty(sourceUnit), sourceUnit));
      var3[6].callSafe(nodes, (Object)(new GeneratedClosure(this, this, transformer) {
         private Reference<T> transformer;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.transformer = (Reference)transformer;
         }

         public Object doCall(ASTNode it) {
            ASTNode itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox((!DefaultTypeTransformation.booleanUnbox(itx.get()) ? Boolean.TRUE : Boolean.FALSE) instanceof AnnotationNode && (!DefaultTypeTransformation.booleanUnbox(itx.get()) ? Boolean.TRUE : Boolean.FALSE) instanceof ClassNode ? Boolean.TRUE : Boolean.FALSE) ? var3[0].call(itx.get(), this.transformer.get()) : null;
         }

         public Object call(ASTNode it) {
            ASTNode itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[1].callCurrent(this, (Object)itx.get());
         }

         public Object getTransformer() {
            CallSite[] var1 = $getCallSiteArray();
            return this.transformer.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1()) {
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
            var0[0] = "visit";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure1 = class$("org.codehaus.groovy.ast.builder.AstBuilderTransformation$_visit_closure1");
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
      var3[7].callSafe(var3[8].call(sourceUnit), transformer.get());
      var3[9].callSafe(var3[10].callSafe(var3[11].call(sourceUnit)), transformer.get());
      var3[12].callSafe(var3[13].callSafe(var3[14].call(sourceUnit)), (Object)(new GeneratedClosure(this, this, transformer) {
         private Reference<T> transformer;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.transformer = (Reference)transformer;
         }

         public Object doCall(ClassNode param1) {
            // $FF: Couldn't be decompiled
         }

         public Object call(ClassNode classNode) {
            ClassNode classNodex = new Reference(classNode);
            CallSite[] var3 = $getCallSiteArray();
            return var3[8].callCurrent(this, (Object)classNodex.get());
         }

         public Object getTransformer() {
            CallSite[] var1 = $getCallSiteArray();
            return this.transformer.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2()) {
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
            var0[1] = "methods";
            var0[2] = "each";
            var0[3] = "constructors";
            var0[4] = "each";
            var0[5] = "fields";
            var0[6] = "each";
            var0[7] = "objectInitializers";
            var0[8] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure2 = class$("org.codehaus.groovy.ast.builder.AstBuilderTransformation$_visit_closure2");
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
      var3[15].callSafe(var3[16].callSafe(var3[17].call(sourceUnit)), (Object)(new GeneratedClosure(this, this, transformer) {
         private Reference<T> transformer;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.transformer = (Reference)transformer;
         }

         public Object doCall(MethodNode node) {
            MethodNode nodex = new Reference(node);
            CallSite[] var3 = $getCallSiteArray();
            CallSite var10000 = var3[0];
            Object var10001 = var3[1].callGetPropertySafe(var3[2].callGetPropertySafe(nodex.get()));
            Object var10005 = this.getThisObject();
            Reference transformer = this.transformer;
            var10000.callSafe(var10001, (Object)(new GeneratedClosure(this, var10005, transformer) {
               private Reference<T> transformer;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8;

               public {
                  Reference transformerx = new Reference(transformer);
                  CallSite[] var5 = $getCallSiteArray();
                  this.transformer = (Reference)((Reference)transformerx.get());
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callSafe(itx.get(), this.transformer.get());
               }

               public Object getTransformer() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.transformer.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8()) {
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
                  var0[0] = "visit";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3_closure8 = class$("org.codehaus.groovy.ast.builder.AstBuilderTransformation$_visit_closure3_closure8");
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
            return var3[3].callSafe(var3[4].callGetPropertySafe(nodex.get()), transformer.get());
         }

         public Object call(MethodNode node) {
            MethodNode nodex = new Reference(node);
            CallSite[] var3 = $getCallSiteArray();
            return var3[5].callCurrent(this, (Object)nodex.get());
         }

         public Object getTransformer() {
            CallSite[] var1 = $getCallSiteArray();
            return this.transformer.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3()) {
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
            var0[1] = "defaultValue";
            var0[2] = "parameters";
            var0[3] = "visit";
            var0[4] = "code";
            var0[5] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation$_visit_closure3 = class$("org.codehaus.groovy.ast.builder.AstBuilderTransformation$_visit_closure3");
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
      if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "<$constructor$>";
      var0[1] = "imports";
      var0[2] = "getAST";
      var0[3] = "importPackages";
      var0[4] = "getAST";
      var0[5] = "source";
      var0[6] = "each";
      var0[7] = "visit";
      var0[8] = "getAST";
      var0[9] = "visit";
      var0[10] = "getStatementBlock";
      var0[11] = "getAST";
      var0[12] = "each";
      var0[13] = "getClasses";
      var0[14] = "getAST";
      var0[15] = "each";
      var0[16] = "getMethods";
      var0[17] = "getAST";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation(), var0);
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderTransformation() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderTransformation = class$("org.codehaus.groovy.ast.builder.AstBuilderTransformation");
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
