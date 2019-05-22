package org.codehaus.groovy.ast.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyInterceptable;
import groovy.lang.MetaClass;
import groovy.lang.PackageScope;
import groovy.lang.Range;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MixinNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

@PackageScope
public class AstSpecificationCompiler implements GroovyInterceptable {
   private boolean returnScriptBodyOnly;
   private CompilePhase phase;
   private String source;
   private final List<ASTNode> expression;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203783L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203783 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$syntax$Token;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$VariableExpression;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$AnnotationConstantExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$ImportNode;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$EmptyStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$DeclarationExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ArgumentListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$FieldExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ConstantExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ForStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MethodCallExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BooleanExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$AnnotationNode;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$ClassHelper;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$GenericsType;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$Parameter;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$WhileStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$ClassNode;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$syntax$Types;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClosureListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BinaryExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ExpressionStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MethodPointerExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$Expression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$DynamicVariable;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClosureExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$CatchStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$Statement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$TupleExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClassExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$FieldNode;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ConstructorCallExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$SpreadExpression;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$SpreadMapExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ThrowStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$GStringExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$PostfixExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$AttributeExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ReturnStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$RegexExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$CaseStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$IfStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ContinueStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$PrefixExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$CastExpression;
   // $FF: synthetic field
   private static Class array$$class$org$codehaus$groovy$ast$Parameter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$RangeExpression;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MapExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$BreakStatement;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$PropertyExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$NotExpression;
   // $FF: synthetic field
   private static Class array$$class$org$codehaus$groovy$ast$ClassNode;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$UnaryMinusExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$TernaryExpression;

   public AstSpecificationCompiler(Closure spec) {
      CallSite[] var2 = $getCallSiteArray();
      this.returnScriptBodyOnly = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.expression = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ScriptBytecodeAdapter.setGroovyObjectProperty(this, $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), spec, "delegate");
      var2[0].call(spec);
   }

   public List<ASTNode> getExpression() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(this.expression, $get$$class$java$util$List());
   }

   private List<ASTNode> enforceConstraints(String methodName, List<Class> spec) {
      String methodName = new Reference(methodName);
      List spec = new Reference(spec);
      CallSite[] var5 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareNotEqual(var5[1].call(spec.get()), var5[2].call(this.expression))) {
         throw (Throwable)var5[3].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)(new GStringImpl(new Object[]{methodName.get(), spec.get(), var5[4].callSafe(this.expression, (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].callGetProperty(itx.get());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1()) {
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
               var0[0] = "class";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure1 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_enforceConstraints_closure1");
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
         }))}, new String[]{"", " could not be invoked. Expected to receive parameters ", " but found ", ""})));
      } else {
         return (List)ScriptBytecodeAdapter.castToType(var5[5].call(ScriptBytecodeAdapter.createRange($const$0, var5[6].call(var5[7].call(spec.get()), (Object)$const$1), true), (Object)(new GeneratedClosure(this, this, spec, methodName) {
            private Reference<T> spec;
            private Reference<T> methodName;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$IllegalArgumentException;
            // $FF: synthetic field
            private static Class $class$java$util$List;
            // $FF: synthetic field
            private static Class $class$java$lang$String;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.spec = (Reference)spec;
               this.methodName = (Reference)methodName;
            }

            public Object doCall(int it) {
               Integer itx = new Reference(DefaultTypeTransformation.box(it));
               CallSite[] var3 = $getCallSiteArray();
               Object actualClass = var3[0].callGetProperty(var3[1].call(var3[2].callGroovyObjectGetProperty(this), itx.get()));
               Object expectedClass = var3[3].call(this.spec.get(), itx.get());
               if (!DefaultTypeTransformation.booleanUnbox(var3[4].call(expectedClass, actualClass))) {
                  throw (Throwable)var3[5].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)(new GStringImpl(new Object[]{this.methodName.get(), this.spec.get(), var3[6].callSafe(var3[7].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].callGetProperty(itx.get());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29()) {
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
                        var0[0] = "class";
                        var0[1] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[2];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29() {
                        Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2_closure29 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_enforceConstraints_closure2_closure29");
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
                  }))}, new String[]{"", " could not be invoked. Expected to receive parameters ", " but found ", ""})));
               } else {
                  return var3[8].call(var3[9].callGroovyObjectGetProperty(this), itx.get());
               }
            }

            public Object call(int it) {
               Integer itx = new Reference(DefaultTypeTransformation.box(it));
               CallSite[] var3 = $getCallSiteArray();
               return var3[10].callCurrent(this, (Object)itx.get());
            }

            public List<Class> getSpec() {
               CallSite[] var1 = $getCallSiteArray();
               return (List)ScriptBytecodeAdapter.castToType(this.spec.get(), $get$$class$java$util$List());
            }

            public String getMethodName() {
               CallSite[] var1 = $getCallSiteArray();
               return (String)ScriptBytecodeAdapter.castToType(this.methodName.get(), $get$$class$java$lang$String());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2()) {
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
               var0[0] = "class";
               var0[1] = "getAt";
               var0[2] = "expression";
               var0[3] = "getAt";
               var0[4] = "isAssignableFrom";
               var0[5] = "<$constructor$>";
               var0[6] = "collect";
               var0[7] = "expression";
               var0[8] = "getAt";
               var0[9] = "expression";
               var0[10] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[11];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2(), var0);
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
            private static Class $get$$class$java$lang$IllegalArgumentException() {
               Class var10000 = $class$java$lang$IllegalArgumentException;
               if (var10000 == null) {
                  var10000 = $class$java$lang$IllegalArgumentException = class$("java.lang.IllegalArgumentException");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$util$List() {
               Class var10000 = $class$java$util$List;
               if (var10000 == null) {
                  var10000 = $class$java$util$List = class$("java.util.List");
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_enforceConstraints_closure2 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_enforceConstraints_closure2");
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
         })), $get$$class$java$util$List());
      }
   }

   private void captureAndCreateNode(String name, Closure argBlock, Closure constructorStatement) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(argBlock)) {
         throw (Throwable)var4[8].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"nodes of type ", " require arguments to be specified"})));
      } else {
         Object oldProps = var4[9].callConstructor($get$$class$java$util$ArrayList(), (Object)this.expression);
         var4[10].call(this.expression);
         var4[11].callConstructor($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), (Object)argBlock);
         Object result = var4[12].call(constructorStatement, (Object)this.expression);
         var4[13].call(this.expression);
         var4[14].call(this.expression, (Object)oldProps);
         var4[15].call(this.expression, (Object)result);
      }
   }

   private void makeNode(Class target, String typeAlias, List<Class<? super ASTNode>> ctorArgs, Closure argBlock) {
      Class target = new Reference(target);
      String typeAlias = new Reference(typeAlias);
      List ctorArgs = new Reference(ctorArgs);
      CallSite[] var8 = $getCallSiteArray();
      var8[16].callCurrent(this, var8[17].callGetProperty(var8[18].callGetProperty(target.get())), argBlock, new GeneratedClosure(this, this, ctorArgs, typeAlias, target) {
         private Reference<T> ctorArgs;
         private Reference<T> typeAlias;
         private Reference<T> target;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.ctorArgs = (Reference)ctorArgs;
            this.typeAlias = (Reference)typeAlias;
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            CallSite var10000 = var2[0];
            Object var10001 = this.target.get();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[1].callCurrent(this, this.typeAlias.get(), this.ctorArgs.get())};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.call(var10001, ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public List<Class<? super ASTNode>> getCtorArgs() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.ctorArgs.get(), $get$$class$java$util$List());
         }

         public String getTypeAlias() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.typeAlias.get(), $get$$class$java$lang$String());
         }

         public Class getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3()) {
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
            var0[0] = "newInstance";
            var0[1] = "enforceConstraints";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3(), var0);
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
         private static Class $get$$class$java$util$List() {
            Class var10000 = $class$java$util$List;
            if (var10000 == null) {
               var10000 = $class$java$util$List = class$("java.util.List");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNode_closure3 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeNode_closure3");
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
      });
   }

   private void makeNodeFromList(Class target, Closure argBlock) {
      Class target = new Reference(target);
      CallSite[] var4 = $getCallSiteArray();
      var4[19].callCurrent(this, var4[20].callGetProperty(target.get()), argBlock, new GeneratedClosure(this, this, target) {
         private Reference<T> target;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(this.target.get(), var2[1].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[2].callGroovyObjectGetProperty(this)));
         }

         public Class getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4()) {
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
            var0[0] = "newInstance";
            var0[1] = "<$constructor$>";
            var0[2] = "expression";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4(), var0);
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
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeFromList_closure4 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeNodeFromList_closure4");
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
      });
   }

   private void makeListOfNodes(Closure argBlock, String input) {
      CallSite[] var3 = $getCallSiteArray();
      var3[21].callCurrent(this, input, argBlock, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[1].callGroovyObjectGetProperty(this));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "expression";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeListOfNodes_closure5 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeListOfNodes_closure5");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
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
      });
   }

   private void makeArrayOfNodes(Object target, Closure argBlock) {
      Object target = new Reference(target);
      CallSite[] var4 = $getCallSiteArray();
      var4[22].callCurrent(this, var4[23].callGetProperty(var4[24].callGetProperty(target.get())), argBlock, new GeneratedClosure(this, this, target) {
         private Reference<T> target;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].call(var2[1].callGroovyObjectGetProperty(this), this.target.get());
         }

         public Object getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Object)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Object());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6()) {
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
            var0[0] = "toArray";
            var0[1] = "expression";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeArrayOfNodes_closure6 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeArrayOfNodes_closure6");
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
      });
   }

   private void makeNodeWithClassParameter(Class target, String alias, List<Class> spec, Closure argBlock, Class type) {
      Class target = new Reference(target);
      String alias = new Reference(alias);
      List spec = new Reference(spec);
      Class type = new Reference(type);
      CallSite[] var10 = $getCallSiteArray();
      var10[25].callCurrent(this, var10[26].callGetProperty(var10[27].callGetProperty(target.get())), argBlock, new GeneratedClosure(this, this, spec, alias, target, type) {
         private Reference<T> spec;
         private Reference<T> alias;
         private Reference<T> target;
         private Reference<T> type;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.spec = (Reference)spec;
            this.alias = (Reference)alias;
            this.target = (Reference)target;
            this.type = (Reference)type;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, var2[2].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()));
            CallSite var10000 = var2[3];
            Object var10001 = this.target.get();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[4].callCurrent(this, this.alias.get(), this.spec.get())};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.call(var10001, ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public List<Class> getSpec() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.spec.get(), $get$$class$java$util$List());
         }

         public String getAlias() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.alias.get(), $get$$class$java$lang$String());
         }

         public Class getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
         }

         public Class getType() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "make";
            var0[3] = "newInstance";
            var0[4] = "enforceConstraints";
            var0[5] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithClassParameter_closure7 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeNodeWithClassParameter_closure7");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$java$util$List() {
            Class var10000 = $class$java$util$List;
            if (var10000 == null) {
               var10000 = $class$java$util$List = class$("java.util.List");
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
      });
   }

   private void makeNodeWithStringParameter(Class target, String alias, List<Class> spec, Closure argBlock, String text) {
      Class target = new Reference(target);
      String alias = new Reference(alias);
      List spec = new Reference(spec);
      String text = new Reference(text);
      CallSite[] var10 = $getCallSiteArray();
      var10[28].callCurrent(this, var10[29].callGetProperty(var10[30].callGetProperty(target.get())), argBlock, new GeneratedClosure(this, this, spec, text, alias, target) {
         private Reference<T> spec;
         private Reference<T> text;
         private Reference<T> alias;
         private Reference<T> target;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$List;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.spec = (Reference)spec;
            this.text = (Reference)text;
            this.alias = (Reference)alias;
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, this.text.get());
            CallSite var10000 = var2[2];
            Object var10001 = this.target.get();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[3].callCurrent(this, this.alias.get(), this.spec.get())};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.call(var10001, ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public List<Class> getSpec() {
            CallSite[] var1 = $getCallSiteArray();
            return (List)ScriptBytecodeAdapter.castToType(this.spec.get(), $get$$class$java$util$List());
         }

         public String getText() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.text.get(), $get$$class$java$lang$String());
         }

         public String getAlias() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.alias.get(), $get$$class$java$lang$String());
         }

         public Class getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "newInstance";
            var0[3] = "enforceConstraints";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_makeNodeWithStringParameter_closure8 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_makeNodeWithStringParameter_closure8");
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
         private static Class $get$$class$java$util$List() {
            Class var10000 = $class$java$util$List;
            if (var10000 == null) {
               var10000 = $class$java$util$List = class$("java.util.List");
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
      });
   }

   private void cast(Class type, Closure argBlock) {
      CallSite[] var3 = $getCallSiteArray();
      var3[31].callCurrent(this, (Object[])ArrayUtil.createArray($get$$class$org$codehaus$groovy$ast$expr$CastExpression(), "cast", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock, type));
   }

   private void constructorCall(Class type, Closure argBlock) {
      CallSite[] var3 = $getCallSiteArray();
      var3[32].callCurrent(this, (Object[])ArrayUtil.createArray($get$$class$org$codehaus$groovy$ast$expr$ConstructorCallExpression(), "constructorCall", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock, type));
   }

   private void methodCall(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[33].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$MethodCallExpression(), "methodCall", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void annotationConstant(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[34].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$AnnotationConstantExpression(), "annotationConstant", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$AnnotationNode()}), argBlock);
   }

   private void postfix(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[35].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$PostfixExpression(), "postfix", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$syntax$Token()}), argBlock);
   }

   private void field(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[36].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$FieldExpression(), "field", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$FieldNode()}), argBlock);
   }

   private void map(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[37].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$MapExpression(), argBlock);
   }

   private void tuple(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[38].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$TupleExpression(), argBlock);
   }

   private void mapEntry(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[39].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression(), "mapEntry", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void gString(String verbatimText, Closure argBlock) {
      CallSite[] var3 = $getCallSiteArray();
      var3[40].callCurrent(this, (Object[])ArrayUtil.createArray($get$$class$org$codehaus$groovy$ast$expr$GStringExpression(), "gString", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$java$lang$String(), $get$$class$java$util$List(), $get$$class$java$util$List()}), argBlock, verbatimText));
   }

   private void methodPointer(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[41].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$MethodPointerExpression(), "methodPointer", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void property(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[42].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$PropertyExpression(), "property", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void range(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[43].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$RangeExpression(), "range", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$java$lang$Boolean()}), argBlock);
   }

   private void empty() {
      CallSite[] var1 = $getCallSiteArray();
      var1[44].call(this.expression, (Object)var1[45].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$EmptyStatement()));
   }

   private void label(String label) {
      CallSite[] var2 = $getCallSiteArray();
      var2[46].call(this.expression, (Object)label);
   }

   private void importNode(Class target, String alias) {
      CallSite[] var3 = $getCallSiteArray();
      var3[47].call(this.expression, (Object)var3[48].callConstructor($get$$class$org$codehaus$groovy$ast$ImportNode(), var3[49].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)target), alias));
   }

   private void catchStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[50].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$CatchStatement(), "catchStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$Parameter(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void throwStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[51].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$ThrowStatement(), "throwStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void synchronizedStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[52].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$SynchronizedStatement(), "synchronizedStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void returnStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[53].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$ReturnStatement(), "returnStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void ternary(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[54].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$TernaryExpression(), "ternary", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), $get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void elvisOperator(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[55].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression(), "elvisOperator", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void breakStatement(String label) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(label)) {
         var2[56].call(this.expression, (Object)var2[57].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$BreakStatement(), (Object)label));
      } else {
         var2[58].call(this.expression, (Object)var2[59].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$BreakStatement()));
      }

   }

   private void continueStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(argBlock)) {
         var2[60].call(this.expression, (Object)var2[61].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$ContinueStatement()));
      } else {
         var2[62].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$ContinueStatement(), "continueStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$java$lang$String()}), argBlock);
      }

   }

   private void caseStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[63].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$CaseStatement(), "caseStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void defaultCase(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[64].callCurrent(this, (Object)argBlock);
   }

   private void prefix(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[65].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$PrefixExpression(), "prefix", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$syntax$Token(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void not(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[66].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$NotExpression(), "not", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void dynamicVariable(String variable, boolean isStatic) {
      CallSite[] var3 = $getCallSiteArray();
      var3[67].call(this.expression, (Object)var3[68].callConstructor($get$$class$org$codehaus$groovy$ast$DynamicVariable(), variable, DefaultTypeTransformation.box(isStatic)));
   }

   private void exceptions(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[69].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((ClassNode[])ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[0]), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), argBlock);
   }

   private void annotations(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[70].callCurrent(this, argBlock, "List<AnnotationNode>");
   }

   private void strings(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[71].callCurrent(this, argBlock, "List<ConstantExpression>");
   }

   private void values(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[72].callCurrent(this, argBlock, "List<Expression>");
   }

   private void inclusive(boolean value) {
      CallSite[] var2 = $getCallSiteArray();
      var2[73].call(this.expression, (Object)DefaultTypeTransformation.box(value));
   }

   private void constant(Object value) {
      CallSite[] var2 = $getCallSiteArray();
      var2[74].call(this.expression, (Object)var2[75].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)value));
   }

   private void ifStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[76].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$IfStatement(), "ifStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), $get$$class$org$codehaus$groovy$ast$stmt$Statement(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void spread(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[77].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$SpreadExpression(), "spread", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void spreadMap(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[78].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$SpreadMapExpression(), "spreadMap", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   /** @deprecated */
   @Deprecated
   private void regex(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[79].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$RegexExpression(), "regex", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void whileStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[80].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$WhileStatement(), "whileStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void forStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[81].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$ForStatement(), "forStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$Parameter(), $get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void closureList(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[82].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$ClosureListExpression(), argBlock);
   }

   private void declaration(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[83].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$DeclarationExpression(), "declaration", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$syntax$Token(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void list(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[84].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$ListExpression(), argBlock);
   }

   private void bitwiseNegation(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[85].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression(), "bitwiseNegation", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void closure(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[86].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression(), "closure", ScriptBytecodeAdapter.createList(new Object[]{$get$array$$class$org$codehaus$groovy$ast$Parameter(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}), argBlock);
   }

   private void booleanExpression(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[87].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), "booleanExpression", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void binary(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[88].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$BinaryExpression(), "binary", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$syntax$Token(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void unaryPlus(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[89].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$UnaryPlusExpression(), "unaryPlus", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void classExpression(Class type) {
      CallSite[] var2 = $getCallSiteArray();
      var2[90].call(this.expression, (Object)var2[91].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ClassExpression(), (Object)var2[92].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)type)));
   }

   private void unaryMinus(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[93].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$UnaryMinusExpression(), "unaryMinus", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void attribute(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[94].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$AttributeExpression(), "attribute", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void expression(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[95].callCurrent(this, $get$$class$org$codehaus$groovy$ast$stmt$ExpressionStatement(), "expression", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$Expression()}), argBlock);
   }

   private void namedArgumentList(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[96].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression(), argBlock);
   }

   private void interfaces(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[97].callCurrent(this, argBlock, "List<ClassNode>");
   }

   private void mixins(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[98].callCurrent(this, argBlock, "List<MixinNode>");
   }

   private void genericsTypes(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[99].callCurrent(this, argBlock, "List<GenericsTypes>");
   }

   private void classNode(Class target) {
      CallSite[] var2 = $getCallSiteArray();
      var2[100].call(this.expression, (Object)var2[101].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), target, Boolean.FALSE));
   }

   private void parameters(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[102].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((Parameter[])ScriptBytecodeAdapter.asType(ScriptBytecodeAdapter.createList(new Object[0]), $get$array$$class$org$codehaus$groovy$ast$Parameter()), $get$array$$class$org$codehaus$groovy$ast$Parameter()), argBlock);
   }

   private void block(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[103].callCurrent(this, "BlockStatement", argBlock, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$BlockStatement;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$VariableScope;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$BlockStatement(), var2[1].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[2].callGroovyObjectGetProperty(this)), var2[3].callConstructor($get$$class$org$codehaus$groovy$ast$VariableScope()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "<$constructor$>";
            var0[2] = "expression";
            var0[3] = "<$constructor$>";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_block_closure9 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_block_closure9");
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$BlockStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement = class$("org.codehaus.groovy.ast.stmt.BlockStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$VariableScope() {
            Class var10000 = $class$org$codehaus$groovy$ast$VariableScope;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$VariableScope = class$("org.codehaus.groovy.ast.VariableScope");
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
      });
   }

   private void parameter(Map<String, Class> args, Closure argBlock) {
      Closure argBlock = new Reference(argBlock);
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(args)) {
         throw (Throwable)var4[104].callConstructor($get$$class$java$lang$IllegalArgumentException());
      } else if (ScriptBytecodeAdapter.compareGreaterThan(var4[105].call(args), $const$1)) {
         throw (Throwable)var4[106].callConstructor($get$$class$java$lang$IllegalArgumentException());
      } else {
         if (DefaultTypeTransformation.booleanUnbox(argBlock.get())) {
            var4[107].call(args, (Object)(new GeneratedClosure(this, this, argBlock) {
               private Reference<T> argBlock;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10;
               // $FF: synthetic field
               private static Class $class$groovy$lang$Closure;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.argBlock = (Reference)argBlock;
               }

               public Object doCall(Object name, Object type) {
                  Object namex = new Reference(name);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].callCurrent(this, "Parameter", this.argBlock.get(), new GeneratedClosure(this, this.getThisObject(), namex, typex) {
                     private Reference<T> name;
                     private Reference<T> type;
                     // $FF: synthetic field
                     private static final Integer $const$0 = (Integer)0;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$ast$ClassHelper;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$ast$Parameter;
                     // $FF: synthetic field
                     private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30;

                     public {
                        CallSite[] var5 = $getCallSiteArray();
                        this.name = (Reference)name;
                        this.type = (Reference)type;
                     }

                     public Object doCall(Object it) {
                        CallSite[] var2 = $getCallSiteArray();
                        return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$Parameter(), var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()), this.name.get(), var2[2].call(var2[3].callGroovyObjectGetProperty(this), (Object)$const$0));
                     }

                     public Object getName() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.name.get();
                     }

                     public Object getType() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.type.get();
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30()) {
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
                        var0[0] = "<$constructor$>";
                        var0[1] = "make";
                        var0[2] = "getAt";
                        var0[3] = "expression";
                        var0[4] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[5];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30(), var0);
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
                     private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
                        Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
                     private static Class $get$$class$org$codehaus$groovy$ast$Parameter() {
                        Class var10000 = $class$org$codehaus$groovy$ast$Parameter;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$ast$Parameter = class$("org.codehaus.groovy.ast.Parameter");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30() {
                        Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30;
                        if (var10000 == null) {
                           var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10_closure30 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_parameter_closure10_closure30");
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
                  });
               }

               public Object call(Object name, Object type) {
                  Object namex = new Reference(name);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, namex.get(), typex.get());
               }

               public Closure getArgBlock() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Closure)ScriptBytecodeAdapter.castToType(this.argBlock.get(), $get$$class$groovy$lang$Closure());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10()) {
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
                  var0[0] = "captureAndCreateNode";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure10 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_parameter_closure10");
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
            }));
         } else {
            var4[108].call(args, (Object)(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$ClassHelper;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$Parameter;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object name, Object type) {
                  Object namex = new Reference(name);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].call(var5[1].callGroovyObjectGetProperty(this), var5[2].callConstructor($get$$class$org$codehaus$groovy$ast$Parameter(), var5[3].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)typex.get()), namex.get()));
               }

               public Object call(Object name, Object type) {
                  Object namex = new Reference(name);
                  Object typex = new Reference(type);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[4].callCurrent(this, namex.get(), typex.get());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11()) {
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
                  var0[0] = "leftShift";
                  var0[1] = "expression";
                  var0[2] = "<$constructor$>";
                  var0[3] = "make";
                  var0[4] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[5];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
                  Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_parameter_closure11 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_parameter_closure11");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$org$codehaus$groovy$ast$Parameter() {
                  Class var10000 = $class$org$codehaus$groovy$ast$Parameter;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$Parameter = class$("org.codehaus.groovy.ast.Parameter");
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

      }
   }

   private void array(Class type, Closure argBlock) {
      Class type = new Reference(type);
      CallSite[] var4 = $getCallSiteArray();
      var4[109].callCurrent(this, "ArrayExpression", argBlock, new GeneratedClosure(this, this, type) {
         private Reference<T> type;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$ArrayExpression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.type = (Reference)type;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ArrayExpression(), var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()), var2[2].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[3].callGroovyObjectGetProperty(this)));
         }

         public Class getType() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "make";
            var0[2] = "<$constructor$>";
            var0[3] = "expression";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$ArrayExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$ArrayExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$ArrayExpression = class$("org.codehaus.groovy.ast.expr.ArrayExpression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_array_closure12 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_array_closure12");
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
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
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
      });
   }

   private void genericsType(Class type, Closure argBlock) {
      Class type = new Reference(type);
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(argBlock)) {
         var4[110].callCurrent(this, "GenericsType", argBlock, new GeneratedClosure(this, this, type) {
            private Reference<T> type;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$ClassHelper;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$GenericsType;
            // $FF: synthetic field
            private static Class array$$class$org$codehaus$groovy$ast$ClassNode;
            // $FF: synthetic field
            private static Class $class$java$lang$Class;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.type = (Reference)type;
            }

            public Object doCall(Object it) {
               CallSite[] var2 = $getCallSiteArray();
               return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$GenericsType(), var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()), ScriptBytecodeAdapter.createPojoWrapper((ClassNode[])ScriptBytecodeAdapter.asType(var2[2].call(var2[3].callGroovyObjectGetProperty(this), (Object)$const$0), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), var2[4].call(var2[5].callGroovyObjectGetProperty(this), (Object)$const$1));
            }

            public Class getType() {
               CallSite[] var1 = $getCallSiteArray();
               return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13()) {
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
               var0[0] = "<$constructor$>";
               var0[1] = "make";
               var0[2] = "getAt";
               var0[3] = "expression";
               var0[4] = "getAt";
               var0[5] = "expression";
               var0[6] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[7];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_genericsType_closure13 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_genericsType_closure13");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
               Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
            private static Class $get$$class$org$codehaus$groovy$ast$GenericsType() {
               Class var10000 = $class$org$codehaus$groovy$ast$GenericsType;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$GenericsType = class$("org.codehaus.groovy.ast.GenericsType");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
               Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
               if (var10000 == null) {
                  var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
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
         });
      } else {
         var4[111].call(this.expression, (Object)var4[112].callConstructor($get$$class$org$codehaus$groovy$ast$GenericsType(), (Object)var4[113].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)type.get())));
      }

   }

   private void upperBound(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[114].callCurrent(this, argBlock, "List<ClassNode>");
   }

   private void lowerBound(Class target) {
      CallSite[] var2 = $getCallSiteArray();
      var2[115].call(this.expression, (Object)var2[116].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)target));
   }

   private void member(String name, Closure argBlock) {
      String name = new Reference(name);
      CallSite[] var4 = $getCallSiteArray();
      var4[117].callCurrent(this, "Annotation Member", argBlock, new GeneratedClosure(this, this, name) {
         private Reference<T> name;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.name = (Reference)name;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return ScriptBytecodeAdapter.createList(new Object[]{this.name.get(), var2[0].call(var2[1].callGroovyObjectGetProperty(this), (Object)$const$0)});
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14()) {
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
            var0[0] = "getAt";
            var0[1] = "expression";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_member_closure14 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_member_closure14");
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
      });
   }

   private void argumentList(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(argBlock)) {
         var2[118].call(this.expression, (Object)var2[119].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression()));
      } else {
         var2[120].callCurrent(this, $get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression(), argBlock);
      }

   }

   private void annotation(Class target, Closure argBlock) {
      Class target = new Reference(target);
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(argBlock)) {
         var4[121].callCurrent(this, "ArgumentListExpression", argBlock, new GeneratedClosure(this, this, target) {
            private Reference<T> target;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$ClassHelper;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$AnnotationNode;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$java$lang$Class;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.target = (Reference)target;
            }

            public Object doCall(Object it) {
               CallSite[] var2 = $getCallSiteArray();
               Object node = new Reference(var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$AnnotationNode(), (Object)var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.target.get())));
               var2[2].callSafe(var2[3].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject(), node) {
                  private Reference<T> node;
                  // $FF: synthetic field
                  private static final Integer $const$0 = (Integer)0;
                  // $FF: synthetic field
                  private static final Integer $const$1 = (Integer)1;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$java$lang$Object;
                  // $FF: synthetic field
                  private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31;

                  public {
                     CallSite[] var4 = $getCallSiteArray();
                     this.node = (Reference)node;
                  }

                  public Object doCall(Object it) {
                     Object itx = new Reference(it);
                     CallSite[] var3 = $getCallSiteArray();
                     return var3[0].call(this.node.get(), var3[1].call(itx.get(), (Object)$const$0), var3[2].call(itx.get(), (Object)$const$1));
                  }

                  public Object getNode() {
                     CallSite[] var1 = $getCallSiteArray();
                     return this.node.get();
                  }

                  public Object doCall() {
                     CallSite[] var1 = $getCallSiteArray();
                     return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31()) {
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
                     var0[0] = "addMember";
                     var0[1] = "getAt";
                     var0[2] = "getAt";
                     var0[3] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[4];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31(), var0);
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
                  private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31() {
                     Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31;
                     if (var10000 == null) {
                        var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15_closure31 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_annotation_closure15_closure31");
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
               return node.get();
            }

            public Class getTarget() {
               CallSite[] var1 = $getCallSiteArray();
               return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15()) {
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
               var0[0] = "<$constructor$>";
               var0[1] = "make";
               var0[2] = "each";
               var0[3] = "expression";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
               Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$ast$AnnotationNode() {
               Class var10000 = $class$org$codehaus$groovy$ast$AnnotationNode;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$AnnotationNode = class$("org.codehaus.groovy.ast.AnnotationNode");
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
            private static Class $get$$class$java$lang$Class() {
               Class var10000 = $class$java$lang$Class;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Class = class$("java.lang.Class");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_annotation_closure15 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_annotation_closure15");
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
         });
      } else {
         var4[122].call(this.expression, (Object)var4[123].callConstructor($get$$class$org$codehaus$groovy$ast$AnnotationNode(), (Object)var4[124].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)target.get())));
      }

   }

   private void mixin(String name, int modifiers, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      CallSite[] var6 = $getCallSiteArray();
      var6[125].callCurrent(this, "AttributeExpression", argBlock, new GeneratedClosure(this, this, name, modifiers) {
         private Reference<T> name;
         private Reference<T> modifiers;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$MixinNode;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$ClassNode;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.name = (Reference)name;
            this.modifiers = (Reference)modifiers;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareGreaterThan(var2[0].call(var2[1].callGroovyObjectGetProperty(this)), $const$0) ? var2[2].callConstructor($get$$class$org$codehaus$groovy$ast$MixinNode(), this.name.get(), this.modifiers.get(), var2[3].call(var2[4].callGroovyObjectGetProperty(this), (Object)$const$1), ScriptBytecodeAdapter.createPojoWrapper((ClassNode[])ScriptBytecodeAdapter.asType(var2[5].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[6].call(var2[7].callGroovyObjectGetProperty(this), (Object)$const$0)), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), $get$array$$class$org$codehaus$groovy$ast$ClassNode())) : var2[8].callConstructor($get$$class$org$codehaus$groovy$ast$MixinNode(), this.name.get(), this.modifiers.get(), var2[9].call(var2[10].callGroovyObjectGetProperty(this), (Object)$const$1));
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16()) {
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
            var0[0] = "size";
            var0[1] = "expression";
            var0[2] = "<$constructor$>";
            var0[3] = "getAt";
            var0[4] = "expression";
            var0[5] = "<$constructor$>";
            var0[6] = "getAt";
            var0[7] = "expression";
            var0[8] = "<$constructor$>";
            var0[9] = "getAt";
            var0[10] = "expression";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mixin_closure16 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_mixin_closure16");
            }

            return var10000;
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$MixinNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$MixinNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$MixinNode = class$("org.codehaus.groovy.ast.MixinNode");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
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
         private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
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
      });
   }

   private void classNode(String name, int modifiers, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      CallSite[] var6 = $getCallSiteArray();
      var6[126].callCurrent(this, "ClassNode", argBlock, new GeneratedClosure(this, this, name, modifiers) {
         private Reference<T> name;
         private Reference<T> modifiers;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)3;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$MixinNode;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$GenericsType;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$ClassNode;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.name = (Reference)name;
            this.modifiers = (Reference)modifiers;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object result = new Reference(var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$ClassNode(), (Object[])ArrayUtil.createArray(this.name.get(), this.modifiers.get(), var2[1].call(var2[2].callGroovyObjectGetProperty(this), (Object)$const$0), ScriptBytecodeAdapter.createPojoWrapper((ClassNode[])ScriptBytecodeAdapter.asType(var2[3].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[4].call(var2[5].callGroovyObjectGetProperty(this), (Object)$const$1)), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), ScriptBytecodeAdapter.createPojoWrapper((MixinNode[])ScriptBytecodeAdapter.asType(var2[6].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[7].call(var2[8].callGroovyObjectGetProperty(this), (Object)$const$2)), $get$array$$class$org$codehaus$groovy$ast$MixinNode()), $get$array$$class$org$codehaus$groovy$ast$MixinNode()))));
            if (DefaultTypeTransformation.booleanUnbox(var2[9].call(var2[10].callGroovyObjectGetProperty(this), (Object)$const$3))) {
               var2[11].call(result.get(), (Object)ScriptBytecodeAdapter.createPojoWrapper((GenericsType[])ScriptBytecodeAdapter.asType(var2[12].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[13].call(var2[14].callGroovyObjectGetProperty(this), (Object)$const$3)), $get$array$$class$org$codehaus$groovy$ast$GenericsType()), $get$array$$class$org$codehaus$groovy$ast$GenericsType()));
            }

            return result.get();
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[15].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "getAt";
            var0[2] = "expression";
            var0[3] = "<$constructor$>";
            var0[4] = "getAt";
            var0[5] = "expression";
            var0[6] = "<$constructor$>";
            var0[7] = "getAt";
            var0[8] = "expression";
            var0[9] = "getAt";
            var0[10] = "expression";
            var0[11] = "setGenericsTypes";
            var0[12] = "<$constructor$>";
            var0[13] = "getAt";
            var0[14] = "expression";
            var0[15] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[16];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$array$$class$org$codehaus$groovy$ast$MixinNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$MixinNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$MixinNode = class$("[Lorg.codehaus.groovy.ast.MixinNode;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_classNode_closure17 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_classNode_closure17");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$array$$class$org$codehaus$groovy$ast$GenericsType() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$GenericsType;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$GenericsType = class$("[Lorg.codehaus.groovy.ast.GenericsType;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
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
         private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
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
      });
   }

   private void assertStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[127].callCurrent(this, "AssertStatement", argBlock, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)2;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$Expression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$AssertStatement;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$BooleanExpression;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            CallSite var10000;
            Class var10001;
            Object[] var10002;
            Object[] var10003;
            int[] var3;
            if (ScriptBytecodeAdapter.compareLessThan(var2[0].call(var2[1].callGroovyObjectGetProperty(this)), $const$0)) {
               var10000 = var2[2];
               var10001 = $get$$class$org$codehaus$groovy$ast$stmt$AssertStatement();
               var10002 = new Object[0];
               var10003 = new Object[]{var2[3].callCurrent(this, "assertStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$BooleanExpression()}))};
               var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
               return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
            } else {
               var10000 = var2[4];
               var10001 = $get$$class$org$codehaus$groovy$ast$stmt$AssertStatement();
               var10002 = new Object[0];
               var10003 = new Object[]{var2[5].callCurrent(this, "assertStatement", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}))};
               var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
               return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
            }
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18()) {
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
            var0[0] = "size";
            var0[1] = "expression";
            var0[2] = "<$constructor$>";
            var0[3] = "enforceConstraints";
            var0[4] = "<$constructor$>";
            var0[5] = "enforceConstraints";
            var0[6] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[7];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_assertStatement_closure18 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_assertStatement_closure18");
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$AssertStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$AssertStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$AssertStatement = class$("org.codehaus.groovy.ast.stmt.AssertStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$expr$BooleanExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression = class$("org.codehaus.groovy.ast.expr.BooleanExpression");
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
      });
   }

   private void tryCatch(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[128].callCurrent(this, "TryCatchStatement", argBlock, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object result = new Reference(var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement(), var2[1].call(var2[2].callGroovyObjectGetProperty(this), (Object)$const$0), var2[3].call(var2[4].callGroovyObjectGetProperty(this), (Object)$const$1)));
            Object catchStatements = var2[5].call(var2[6].call(var2[7].callGroovyObjectGetProperty(this)));
            var2[8].call(catchStatements, (Object)(new GeneratedClosure(this, this.getThisObject(), result) {
               private Reference<T> result;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.result = (Reference)result;
               }

               public Object doCall(Object statement) {
                  Object statementx = new Reference(statement);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(this.result.get(), statementx.get());
               }

               public Object getResult() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.result.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32()) {
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
                  var0[0] = "addCatch";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[1];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19_closure32 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_tryCatch_closure19_closure32");
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
            return result.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "getAt";
            var0[2] = "expression";
            var0[3] = "getAt";
            var0[4] = "expression";
            var0[5] = "tail";
            var0[6] = "tail";
            var0[7] = "expression";
            var0[8] = "each";
            var0[9] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[10];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_tryCatch_closure19 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_tryCatch_closure19");
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement = class$("org.codehaus.groovy.ast.stmt.TryCatchStatement");
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
      });
   }

   private void variable(String variable) {
      CallSite[] var2 = $getCallSiteArray();
      var2[129].call(this.expression, (Object)var2[130].callConstructor($get$$class$org$codehaus$groovy$ast$expr$VariableExpression(), (Object)variable));
   }

   private void method(String name, int modifiers, Class returnType, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      Class returnType = new Reference(returnType);
      CallSite[] var8 = $getCallSiteArray();
      var8[131].callCurrent(this, "MethodNode", argBlock, new GeneratedClosure(this, this, name, modifiers, returnType) {
         private Reference<T> name;
         private Reference<T> modifiers;
         private Reference<T> returnType;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)3;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$MethodNode;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.name = (Reference)name;
            this.modifiers = (Reference)modifiers;
            this.returnType = (Reference)returnType;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object result = new Reference(var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$MethodNode(), (Object[])ArrayUtil.createArray(this.name.get(), this.modifiers.get(), var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.returnType.get()), var2[2].call(var2[3].callGroovyObjectGetProperty(this), (Object)$const$0), var2[4].call(var2[5].callGroovyObjectGetProperty(this), (Object)$const$1), var2[6].call(var2[7].callGroovyObjectGetProperty(this), (Object)$const$2))));
            if (DefaultTypeTransformation.booleanUnbox(var2[8].call(var2[9].callGroovyObjectGetProperty(this), (Object)$const$3))) {
               Object annotations = var2[10].call(var2[11].callGroovyObjectGetProperty(this), (Object)$const$3);
               var2[12].call(result.get(), var2[13].callConstructor($get$$class$java$util$ArrayList(), (Object)annotations));
            }

            return result.get();
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Class getReturnType() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.returnType.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[14].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "make";
            var0[2] = "getAt";
            var0[3] = "expression";
            var0[4] = "getAt";
            var0[5] = "expression";
            var0[6] = "getAt";
            var0[7] = "expression";
            var0[8] = "getAt";
            var0[9] = "expression";
            var0[10] = "getAt";
            var0[11] = "expression";
            var0[12] = "addAnnotations";
            var0[13] = "<$constructor$>";
            var0[14] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[15];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_method_closure20 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_method_closure20");
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
         private static Class $get$$class$org$codehaus$groovy$ast$MethodNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$MethodNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$MethodNode = class$("org.codehaus.groovy.ast.MethodNode");
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
      });
   }

   private void token(String value) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         throw (Throwable)var2[132].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: value");
      } else {
         Object tokenID = var2[133].call($get$$class$org$codehaus$groovy$syntax$Types(), (Object)value);
         if (ScriptBytecodeAdapter.compareEqual(tokenID, var2[134].callGetProperty($get$$class$org$codehaus$groovy$syntax$Types()))) {
            tokenID = var2[135].call($get$$class$org$codehaus$groovy$syntax$Types(), (Object)value);
         }

         if (ScriptBytecodeAdapter.compareEqual(tokenID, var2[136].callGetProperty($get$$class$org$codehaus$groovy$syntax$Types()))) {
            throw (Throwable)var2[137].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)(new GStringImpl(new Object[]{value}, new String[]{"could not find token for ", ""})));
         } else {
            var2[138].call(this.expression, (Object)var2[139].callConstructor($get$$class$org$codehaus$groovy$syntax$Token(), tokenID, value, $const$2, $const$2));
         }
      }
   }

   private void range(Range range) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(range, (Object)null)) {
         throw (Throwable)var2[140].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: range");
      } else {
         var2[141].call(this.expression, (Object)var2[142].callConstructor($get$$class$org$codehaus$groovy$ast$expr$RangeExpression(), var2[143].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)var2[144].call(range)), var2[145].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)var2[146].call(range)), Boolean.TRUE));
      }
   }

   private void switchStatement(Closure argBlock) {
      CallSite[] var2 = $getCallSiteArray();
      var2[147].callCurrent(this, "SwitchStatement", argBlock, new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$SwitchStatement;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object switchExpression = var2[0].call(var2[1].callGroovyObjectGetProperty(this));
            Object caseStatements = var2[2].call(var2[3].call(var2[4].callGroovyObjectGetProperty(this)));
            Object defaultExpression = var2[5].call(var2[6].call(var2[7].callGroovyObjectGetProperty(this)));
            return var2[8].callConstructor($get$$class$org$codehaus$groovy$ast$stmt$SwitchStatement(), switchExpression, caseStatements, defaultExpression);
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21()) {
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
            var0[0] = "head";
            var0[1] = "expression";
            var0[2] = "tail";
            var0[3] = "tail";
            var0[4] = "expression";
            var0[5] = "head";
            var0[6] = "tail";
            var0[7] = "expression";
            var0[8] = "<$constructor$>";
            var0[9] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[10];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$SwitchStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$SwitchStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$SwitchStatement = class$("org.codehaus.groovy.ast.stmt.SwitchStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_switchStatement_closure21 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_switchStatement_closure21");
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
      });
   }

   private void mapEntry(Map map) {
      CallSite[] var2 = $getCallSiteArray();
      var2[148].call(var2[149].call(map), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$ConstantExpression;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(var3[1].callGroovyObjectGetProperty(this), var3[2].callConstructor($get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression(), var3[3].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)var3[4].callGetProperty(itx.get())), var3[5].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)var3[6].callGetProperty(itx.get()))));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22()) {
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
            var0[0] = "leftShift";
            var0[1] = "expression";
            var0[2] = "<$constructor$>";
            var0[3] = "<$constructor$>";
            var0[4] = "key";
            var0[5] = "<$constructor$>";
            var0[6] = "value";
            var0[7] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[8];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression = class$("org.codehaus.groovy.ast.expr.MapEntryExpression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_mapEntry_closure22 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_mapEntry_closure22");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$expr$ConstantExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression = class$("org.codehaus.groovy.ast.expr.ConstantExpression");
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

   private void fieldNode(String name, int modifiers, Class type, Class owner, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      Class type = new Reference(type);
      Class owner = new Reference(owner);
      CallSite[] var10 = $getCallSiteArray();
      var10[150].callCurrent(this, "FieldNode", argBlock, new GeneratedClosure(this, this, name, owner, modifiers, type) {
         private Reference<T> name;
         private Reference<T> owner;
         private Reference<T> modifiers;
         private Reference<T> type;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$FieldNode;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$Expression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.name = (Reference)name;
            this.owner = (Reference)owner;
            this.modifiers = (Reference)modifiers;
            this.type = (Reference)type;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, var2[2].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.owner.get()));
            var2[3].call(var2[4].callGroovyObjectGetProperty(this), $const$0, var2[5].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()));
            var2[6].call(var2[7].callGroovyObjectGetProperty(this), $const$0, this.modifiers.get());
            var2[8].call(var2[9].callGroovyObjectGetProperty(this), $const$0, this.name.get());
            CallSite var10000 = var2[10];
            Class var10001 = $get$$class$org$codehaus$groovy$ast$FieldNode();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[11].callCurrent(this, "fieldNode", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$java$lang$String(), $get$$class$java$lang$Integer(), $get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}))};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public Class getOwner() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.owner.get(), $get$$class$java$lang$Class());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Class getType() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "make";
            var0[3] = "add";
            var0[4] = "expression";
            var0[5] = "make";
            var0[6] = "add";
            var0[7] = "expression";
            var0[8] = "add";
            var0[9] = "expression";
            var0[10] = "<$constructor$>";
            var0[11] = "enforceConstraints";
            var0[12] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[13];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$FieldNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$FieldNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$FieldNode = class$("org.codehaus.groovy.ast.FieldNode");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_fieldNode_closure23 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_fieldNode_closure23");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
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
      });
   }

   private void innerClass(String name, int modifiers, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      CallSite[] var6 = $getCallSiteArray();
      var6[151].callCurrent(this, "InnerClassNode", argBlock, new GeneratedClosure(this, this, name, modifiers) {
         private Reference<T> name;
         private Reference<T> modifiers;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static final Integer $const$3 = (Integer)3;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$MixinNode;
         // $FF: synthetic field
         private static Class $class$java$util$ArrayList;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$InnerClassNode;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.name = (Reference)name;
            this.modifiers = (Reference)modifiers;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$InnerClassNode(), (Object[])ArrayUtil.createArray(var2[1].call(var2[2].callGroovyObjectGetProperty(this), (Object)$const$0), this.name.get(), this.modifiers.get(), var2[3].call(var2[4].callGroovyObjectGetProperty(this), (Object)$const$1), ScriptBytecodeAdapter.createPojoWrapper((ClassNode[])ScriptBytecodeAdapter.asType(var2[5].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[6].call(var2[7].callGroovyObjectGetProperty(this), (Object)$const$2)), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), $get$array$$class$org$codehaus$groovy$ast$ClassNode()), ScriptBytecodeAdapter.createPojoWrapper((MixinNode[])ScriptBytecodeAdapter.asType(var2[8].callConstructor($get$$class$java$util$ArrayList(), (Object)var2[9].call(var2[10].callGroovyObjectGetProperty(this), (Object)$const$3)), $get$array$$class$org$codehaus$groovy$ast$MixinNode()), $get$array$$class$org$codehaus$groovy$ast$MixinNode())));
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "getAt";
            var0[2] = "expression";
            var0[3] = "getAt";
            var0[4] = "expression";
            var0[5] = "<$constructor$>";
            var0[6] = "getAt";
            var0[7] = "expression";
            var0[8] = "<$constructor$>";
            var0[9] = "getAt";
            var0[10] = "expression";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$array$$class$org$codehaus$groovy$ast$MixinNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$MixinNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$MixinNode = class$("[Lorg.codehaus.groovy.ast.MixinNode;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ArrayList() {
            Class var10000 = $class$java$util$ArrayList;
            if (var10000 == null) {
               var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$InnerClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$InnerClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$InnerClassNode = class$("org.codehaus.groovy.ast.InnerClassNode");
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
         private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_innerClass_closure24 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_innerClass_closure24");
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
      });
   }

   private void propertyNode(String name, int modifiers, Class type, Class owner, Closure argBlock) {
      String name = new Reference(name);
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      Class type = new Reference(type);
      Class owner = new Reference(owner);
      CallSite[] var10 = $getCallSiteArray();
      var10[152].callCurrent(this, "PropertyNode", argBlock, new GeneratedClosure(this, this, name, owner, type, modifiers) {
         private Reference<T> name;
         private Reference<T> owner;
         private Reference<T> type;
         private Reference<T> modifiers;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$PropertyNode;

         public {
            CallSite[] var7 = $getCallSiteArray();
            this.name = (Reference)name;
            this.owner = (Reference)owner;
            this.type = (Reference)type;
            this.modifiers = (Reference)modifiers;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$org$codehaus$groovy$ast$PropertyNode(), (Object[])ArrayUtil.createArray(this.name.get(), this.modifiers.get(), var2[1].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.type.get()), var2[2].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.owner.get()), var2[3].call(var2[4].callGroovyObjectGetProperty(this), (Object)$const$0), var2[5].call(var2[6].callGroovyObjectGetProperty(this), (Object)$const$1), var2[7].call(var2[8].callGroovyObjectGetProperty(this), (Object)$const$2)));
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public Class getOwner() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.owner.get(), $get$$class$java$lang$Class());
         }

         public Class getType() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.type.get(), $get$$class$java$lang$Class());
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[9].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25()) {
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
            var0[0] = "<$constructor$>";
            var0[1] = "make";
            var0[2] = "make";
            var0[3] = "getAt";
            var0[4] = "expression";
            var0[5] = "getAt";
            var0[6] = "expression";
            var0[7] = "getAt";
            var0[8] = "expression";
            var0[9] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[10];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_propertyNode_closure25 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_propertyNode_closure25");
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
         private static Class $get$$class$org$codehaus$groovy$ast$PropertyNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$PropertyNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$PropertyNode = class$("org.codehaus.groovy.ast.PropertyNode");
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
      });
   }

   private void staticMethodCall(Class target, String name, Closure argBlock) {
      Class target = new Reference(target);
      String name = new Reference(name);
      CallSite[] var6 = $getCallSiteArray();
      var6[153].callCurrent(this, "StaticMethodCallExpression", argBlock, new GeneratedClosure(this, this, name, target) {
         private Reference<T> name;
         private Reference<T> target;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$Expression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.name = (Reference)name;
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, this.name.get());
            var2[2].call(var2[3].callGroovyObjectGetProperty(this), $const$0, var2[4].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), (Object)this.target.get()));
            CallSite var10000 = var2[5];
            Class var10001 = $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[6].callCurrent(this, "staticMethodCall", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$java$lang$String(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}))};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public Class getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (Class)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$java$lang$Class());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "add";
            var0[3] = "expression";
            var0[4] = "make";
            var0[5] = "<$constructor$>";
            var0[6] = "enforceConstraints";
            var0[7] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[8];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression = class$("org.codehaus.groovy.ast.expr.StaticMethodCallExpression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure26 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_staticMethodCall_closure26");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
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
      });
   }

   private void staticMethodCall(MethodClosure target, Closure argBlock) {
      MethodClosure target = new Reference(target);
      CallSite[] var4 = $getCallSiteArray();
      var4[154].callCurrent(this, "StaticMethodCallExpression", argBlock, new GeneratedClosure(this, this, target) {
         private Reference<T> target;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$runtime$MethodClosure;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassHelper;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$Expression;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.target = (Reference)target;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, var2[2].callGroovyObjectGetProperty(this.target.get()));
            var2[3].call(var2[4].callGroovyObjectGetProperty(this), $const$0, var2[5].call($get$$class$org$codehaus$groovy$ast$ClassHelper(), var2[6].callGetProperty(var2[7].callGroovyObjectGetProperty(this.target.get())), Boolean.FALSE));
            CallSite var10000 = var2[8];
            Class var10001 = $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[9].callCurrent(this, "staticMethodCall", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$java$lang$String(), $get$$class$org$codehaus$groovy$ast$expr$Expression()}))};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public MethodClosure getTarget() {
            CallSite[] var1 = $getCallSiteArray();
            return (MethodClosure)ScriptBytecodeAdapter.castToType(this.target.get(), $get$$class$org$codehaus$groovy$runtime$MethodClosure());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[10].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "method";
            var0[3] = "add";
            var0[4] = "expression";
            var0[5] = "makeWithoutCaching";
            var0[6] = "class";
            var0[7] = "owner";
            var0[8] = "<$constructor$>";
            var0[9] = "enforceConstraints";
            var0[10] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[11];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression = class$("org.codehaus.groovy.ast.expr.StaticMethodCallExpression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_staticMethodCall_closure27 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_staticMethodCall_closure27");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
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
      });
   }

   private void constructor(int modifiers, Closure argBlock) {
      Integer modifiers = new Reference(DefaultTypeTransformation.box(modifiers));
      CallSite[] var4 = $getCallSiteArray();
      var4[155].callCurrent(this, "ConstructorNode", argBlock, new GeneratedClosure(this, this, modifiers) {
         private Reference<T> modifiers;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$Parameter;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ConstructorNode;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28;
         // $FF: synthetic field
         private static Class array$$class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$Statement;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.modifiers = (Reference)modifiers;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), $const$0, this.modifiers.get());
            CallSite var10000 = var2[2];
            Class var10001 = $get$$class$org$codehaus$groovy$ast$ConstructorNode();
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{var2[3].callCurrent(this, "constructor", ScriptBytecodeAdapter.createList(new Object[]{$get$$class$java$lang$Integer(), $get$array$$class$org$codehaus$groovy$ast$Parameter(), $get$array$$class$org$codehaus$groovy$ast$ClassNode(), $get$$class$org$codehaus$groovy$ast$stmt$Statement()}))};
            int[] var3 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.callConstructor(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var3));
         }

         public int getModifiers() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(this.modifiers.get(), $get$$class$java$lang$Integer()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28()) {
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
            var0[0] = "add";
            var0[1] = "expression";
            var0[2] = "<$constructor$>";
            var0[3] = "enforceConstraints";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28(), var0);
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
         private static Class $get$array$$class$org$codehaus$groovy$ast$Parameter() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$Parameter;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$Parameter = class$("[Lorg.codehaus.groovy.ast.Parameter;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$ConstructorNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ConstructorNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ConstructorNode = class$("org.codehaus.groovy.ast.ConstructorNode");
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler$_constructor_closure28 = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler$_constructor_closure28");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$Statement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$Statement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$Statement = class$("org.codehaus.groovy.ast.stmt.Statement");
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
      });
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   private void importNode(Class target) {
      CallSite[] var2 = $getCallSiteArray();
      var2[156].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(target, $get$$class$java$lang$Class()), ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String()), $get$$class$java$lang$String()));
   }

   private void breakStatement() {
      CallSite[] var1 = $getCallSiteArray();
      var1[157].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String()), $get$$class$java$lang$String()));
   }

   private void continueStatement() {
      CallSite[] var1 = $getCallSiteArray();
      var1[158].callCurrent(this, (Object)ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()));
   }

   private void dynamicVariable(String variable) {
      CallSite[] var2 = $getCallSiteArray();
      var2[159].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(variable, $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.FALSE, Boolean.TYPE));
   }

   private void parameter(Map<String, Class> args) {
      CallSite[] var2 = $getCallSiteArray();
      var2[160].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(args, $get$$class$java$util$Map()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()));
   }

   private void genericsType(Class type) {
      Class type = new Reference(type);
      CallSite[] var3 = $getCallSiteArray();
      var3[161].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((Class)ScriptBytecodeAdapter.castToType(type.get(), $get$$class$java$lang$Class()), $get$$class$java$lang$Class()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()));
   }

   private void annotation(Class target) {
      Class target = new Reference(target);
      CallSite[] var3 = $getCallSiteArray();
      var3[162].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((Class)ScriptBytecodeAdapter.castToType(target.get(), $get$$class$java$lang$Class()), $get$$class$java$lang$Class()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()));
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
   public List this$2$enforceConstraints(String var1, List var2) {
      return this.enforceConstraints(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$captureAndCreateNode(String var1, Closure var2, Closure var3) {
      this.captureAndCreateNode(var1, var2, var3);
   }

   // $FF: synthetic method
   public void this$2$makeNode(Class var1, String var2, List var3, Closure var4) {
      this.makeNode(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void this$2$makeNodeFromList(Class var1, Closure var2) {
      this.makeNodeFromList(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$makeListOfNodes(Closure var1, String var2) {
      this.makeListOfNodes(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$makeArrayOfNodes(Object var1, Closure var2) {
      this.makeArrayOfNodes(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$makeNodeWithClassParameter(Class var1, String var2, List var3, Closure var4, Class var5) {
      this.makeNodeWithClassParameter(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void this$2$makeNodeWithStringParameter(Class var1, String var2, List var3, Closure var4, String var5) {
      this.makeNodeWithStringParameter(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void this$2$cast(Class var1, Closure var2) {
      this.cast(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$constructorCall(Class var1, Closure var2) {
      this.constructorCall(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$methodCall(Closure var1) {
      this.methodCall(var1);
   }

   // $FF: synthetic method
   public void this$2$annotationConstant(Closure var1) {
      this.annotationConstant(var1);
   }

   // $FF: synthetic method
   public void this$2$postfix(Closure var1) {
      this.postfix(var1);
   }

   // $FF: synthetic method
   public void this$2$field(Closure var1) {
      this.field(var1);
   }

   // $FF: synthetic method
   public void this$2$map(Closure var1) {
      this.map(var1);
   }

   // $FF: synthetic method
   public void this$2$tuple(Closure var1) {
      this.tuple(var1);
   }

   // $FF: synthetic method
   public void this$2$mapEntry(Closure var1) {
      this.mapEntry(var1);
   }

   // $FF: synthetic method
   public void this$2$gString(String var1, Closure var2) {
      this.gString(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$methodPointer(Closure var1) {
      this.methodPointer(var1);
   }

   // $FF: synthetic method
   public void this$2$property(Closure var1) {
      this.property(var1);
   }

   // $FF: synthetic method
   public void this$2$range(Closure var1) {
      this.range(var1);
   }

   // $FF: synthetic method
   public void this$2$empty() {
      this.empty();
   }

   // $FF: synthetic method
   public void this$2$label(String var1) {
      this.label(var1);
   }

   // $FF: synthetic method
   public void this$2$importNode(Class var1, String var2) {
      this.importNode(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$catchStatement(Closure var1) {
      this.catchStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$throwStatement(Closure var1) {
      this.throwStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$synchronizedStatement(Closure var1) {
      this.synchronizedStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$returnStatement(Closure var1) {
      this.returnStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$ternary(Closure var1) {
      this.ternary(var1);
   }

   // $FF: synthetic method
   public void this$2$elvisOperator(Closure var1) {
      this.elvisOperator(var1);
   }

   // $FF: synthetic method
   public void this$2$breakStatement(String var1) {
      this.breakStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$continueStatement(Closure var1) {
      this.continueStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$caseStatement(Closure var1) {
      this.caseStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$defaultCase(Closure var1) {
      this.defaultCase(var1);
   }

   // $FF: synthetic method
   public void this$2$prefix(Closure var1) {
      this.prefix(var1);
   }

   // $FF: synthetic method
   public void this$2$not(Closure var1) {
      this.not(var1);
   }

   // $FF: synthetic method
   public void this$2$dynamicVariable(String var1, boolean var2) {
      this.dynamicVariable(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$exceptions(Closure var1) {
      this.exceptions(var1);
   }

   // $FF: synthetic method
   public void this$2$annotations(Closure var1) {
      this.annotations(var1);
   }

   // $FF: synthetic method
   public void this$2$strings(Closure var1) {
      this.strings(var1);
   }

   // $FF: synthetic method
   public void this$2$values(Closure var1) {
      this.values(var1);
   }

   // $FF: synthetic method
   public void this$2$inclusive(boolean var1) {
      this.inclusive(var1);
   }

   // $FF: synthetic method
   public void this$2$constant(Object var1) {
      this.constant(var1);
   }

   // $FF: synthetic method
   public void this$2$ifStatement(Closure var1) {
      this.ifStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$spread(Closure var1) {
      this.spread(var1);
   }

   // $FF: synthetic method
   public void this$2$spreadMap(Closure var1) {
      this.spreadMap(var1);
   }

   // $FF: synthetic method
   public void this$2$regex(Closure var1) {
      this.regex(var1);
   }

   // $FF: synthetic method
   public void this$2$whileStatement(Closure var1) {
      this.whileStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$forStatement(Closure var1) {
      this.forStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$closureList(Closure var1) {
      this.closureList(var1);
   }

   // $FF: synthetic method
   public void this$2$declaration(Closure var1) {
      this.declaration(var1);
   }

   // $FF: synthetic method
   public void this$2$list(Closure var1) {
      this.list(var1);
   }

   // $FF: synthetic method
   public void this$2$bitwiseNegation(Closure var1) {
      this.bitwiseNegation(var1);
   }

   // $FF: synthetic method
   public void this$2$closure(Closure var1) {
      this.closure(var1);
   }

   // $FF: synthetic method
   public void this$2$booleanExpression(Closure var1) {
      this.booleanExpression(var1);
   }

   // $FF: synthetic method
   public void this$2$binary(Closure var1) {
      this.binary(var1);
   }

   // $FF: synthetic method
   public void this$2$unaryPlus(Closure var1) {
      this.unaryPlus(var1);
   }

   // $FF: synthetic method
   public void this$2$classExpression(Class var1) {
      this.classExpression(var1);
   }

   // $FF: synthetic method
   public void this$2$unaryMinus(Closure var1) {
      this.unaryMinus(var1);
   }

   // $FF: synthetic method
   public void this$2$attribute(Closure var1) {
      this.attribute(var1);
   }

   // $FF: synthetic method
   public void this$2$expression(Closure var1) {
      this.expression(var1);
   }

   // $FF: synthetic method
   public void this$2$namedArgumentList(Closure var1) {
      this.namedArgumentList(var1);
   }

   // $FF: synthetic method
   public void this$2$interfaces(Closure var1) {
      this.interfaces(var1);
   }

   // $FF: synthetic method
   public void this$2$mixins(Closure var1) {
      this.mixins(var1);
   }

   // $FF: synthetic method
   public void this$2$genericsTypes(Closure var1) {
      this.genericsTypes(var1);
   }

   // $FF: synthetic method
   public void this$2$classNode(Class var1) {
      this.classNode(var1);
   }

   // $FF: synthetic method
   public void this$2$parameters(Closure var1) {
      this.parameters(var1);
   }

   // $FF: synthetic method
   public void this$2$block(Closure var1) {
      this.block(var1);
   }

   // $FF: synthetic method
   public void this$2$parameter(Map var1, Closure var2) {
      this.parameter(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$array(Class var1, Closure var2) {
      this.array(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$genericsType(Class var1, Closure var2) {
      this.genericsType(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$upperBound(Closure var1) {
      this.upperBound(var1);
   }

   // $FF: synthetic method
   public void this$2$lowerBound(Class var1) {
      this.lowerBound(var1);
   }

   // $FF: synthetic method
   public void this$2$member(String var1, Closure var2) {
      this.member(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$argumentList(Closure var1) {
      this.argumentList(var1);
   }

   // $FF: synthetic method
   public void this$2$annotation(Class var1, Closure var2) {
      this.annotation(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$mixin(String var1, int var2, Closure var3) {
      this.mixin(var1, var2, var3);
   }

   // $FF: synthetic method
   public void this$2$classNode(String var1, int var2, Closure var3) {
      this.classNode(var1, var2, var3);
   }

   // $FF: synthetic method
   public void this$2$assertStatement(Closure var1) {
      this.assertStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$tryCatch(Closure var1) {
      this.tryCatch(var1);
   }

   // $FF: synthetic method
   public void this$2$variable(String var1) {
      this.variable(var1);
   }

   // $FF: synthetic method
   public void this$2$method(String var1, int var2, Class var3, Closure var4) {
      this.method(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void this$2$token(String var1) {
      this.token(var1);
   }

   // $FF: synthetic method
   public void this$2$range(Range var1) {
      this.range(var1);
   }

   // $FF: synthetic method
   public void this$2$switchStatement(Closure var1) {
      this.switchStatement(var1);
   }

   // $FF: synthetic method
   public void this$2$mapEntry(Map var1) {
      this.mapEntry(var1);
   }

   // $FF: synthetic method
   public void this$2$fieldNode(String var1, int var2, Class var3, Class var4, Closure var5) {
      this.fieldNode(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void this$2$innerClass(String var1, int var2, Closure var3) {
      this.innerClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public void this$2$propertyNode(String var1, int var2, Class var3, Class var4, Closure var5) {
      this.propertyNode(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void this$2$staticMethodCall(Class var1, String var2, Closure var3) {
      this.staticMethodCall(var1, var2, var3);
   }

   // $FF: synthetic method
   public void this$2$staticMethodCall(MethodClosure var1, Closure var2) {
      this.staticMethodCall(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$constructor(int var1, Closure var2) {
      this.constructor(var1, var2);
   }

   // $FF: synthetic method
   public void this$2$importNode(Class var1) {
      this.importNode(var1);
   }

   // $FF: synthetic method
   public void this$2$breakStatement() {
      this.breakStatement();
   }

   // $FF: synthetic method
   public void this$2$continueStatement() {
      this.continueStatement();
   }

   // $FF: synthetic method
   public void this$2$dynamicVariable(String var1) {
      this.dynamicVariable(var1);
   }

   // $FF: synthetic method
   public void this$2$parameter(Map var1) {
      this.parameter(var1);
   }

   // $FF: synthetic method
   public void this$2$genericsType(Class var1) {
      this.genericsType(var1);
   }

   // $FF: synthetic method
   public void this$2$annotation(Class var1) {
      this.annotation(var1);
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
      var0[0] = "call";
      var0[1] = "size";
      var0[2] = "size";
      var0[3] = "<$constructor$>";
      var0[4] = "collect";
      var0[5] = "collect";
      var0[6] = "minus";
      var0[7] = "size";
      var0[8] = "<$constructor$>";
      var0[9] = "<$constructor$>";
      var0[10] = "clear";
      var0[11] = "<$constructor$>";
      var0[12] = "call";
      var0[13] = "clear";
      var0[14] = "addAll";
      var0[15] = "add";
      var0[16] = "captureAndCreateNode";
      var0[17] = "simpleName";
      var0[18] = "class";
      var0[19] = "captureAndCreateNode";
      var0[20] = "simpleName";
      var0[21] = "captureAndCreateNode";
      var0[22] = "captureAndCreateNode";
      var0[23] = "simpleName";
      var0[24] = "class";
      var0[25] = "captureAndCreateNode";
      var0[26] = "simpleName";
      var0[27] = "class";
      var0[28] = "captureAndCreateNode";
      var0[29] = "simpleName";
      var0[30] = "class";
      var0[31] = "makeNodeWithClassParameter";
      var0[32] = "makeNodeWithClassParameter";
      var0[33] = "makeNode";
      var0[34] = "makeNode";
      var0[35] = "makeNode";
      var0[36] = "makeNode";
      var0[37] = "makeNodeFromList";
      var0[38] = "makeNodeFromList";
      var0[39] = "makeNode";
      var0[40] = "makeNodeWithStringParameter";
      var0[41] = "makeNode";
      var0[42] = "makeNode";
      var0[43] = "makeNode";
      var0[44] = "leftShift";
      var0[45] = "<$constructor$>";
      var0[46] = "leftShift";
      var0[47] = "leftShift";
      var0[48] = "<$constructor$>";
      var0[49] = "make";
      var0[50] = "makeNode";
      var0[51] = "makeNode";
      var0[52] = "makeNode";
      var0[53] = "makeNode";
      var0[54] = "makeNode";
      var0[55] = "makeNode";
      var0[56] = "leftShift";
      var0[57] = "<$constructor$>";
      var0[58] = "leftShift";
      var0[59] = "<$constructor$>";
      var0[60] = "leftShift";
      var0[61] = "<$constructor$>";
      var0[62] = "makeNode";
      var0[63] = "makeNode";
      var0[64] = "block";
      var0[65] = "makeNode";
      var0[66] = "makeNode";
      var0[67] = "leftShift";
      var0[68] = "<$constructor$>";
      var0[69] = "makeArrayOfNodes";
      var0[70] = "makeListOfNodes";
      var0[71] = "makeListOfNodes";
      var0[72] = "makeListOfNodes";
      var0[73] = "leftShift";
      var0[74] = "leftShift";
      var0[75] = "<$constructor$>";
      var0[76] = "makeNode";
      var0[77] = "makeNode";
      var0[78] = "makeNode";
      var0[79] = "makeNode";
      var0[80] = "makeNode";
      var0[81] = "makeNode";
      var0[82] = "makeNodeFromList";
      var0[83] = "makeNode";
      var0[84] = "makeNodeFromList";
      var0[85] = "makeNode";
      var0[86] = "makeNode";
      var0[87] = "makeNode";
      var0[88] = "makeNode";
      var0[89] = "makeNode";
      var0[90] = "leftShift";
      var0[91] = "<$constructor$>";
      var0[92] = "make";
      var0[93] = "makeNode";
      var0[94] = "makeNode";
      var0[95] = "makeNode";
      var0[96] = "makeNodeFromList";
      var0[97] = "makeListOfNodes";
      var0[98] = "makeListOfNodes";
      var0[99] = "makeListOfNodes";
      var0[100] = "leftShift";
      var0[101] = "make";
      var0[102] = "makeArrayOfNodes";
      var0[103] = "captureAndCreateNode";
      var0[104] = "<$constructor$>";
      var0[105] = "size";
      var0[106] = "<$constructor$>";
      var0[107] = "each";
      var0[108] = "each";
      var0[109] = "captureAndCreateNode";
      var0[110] = "captureAndCreateNode";
      var0[111] = "leftShift";
      var0[112] = "<$constructor$>";
      var0[113] = "make";
      var0[114] = "makeListOfNodes";
      var0[115] = "leftShift";
      var0[116] = "make";
      var0[117] = "captureAndCreateNode";
      var0[118] = "leftShift";
      var0[119] = "<$constructor$>";
      var0[120] = "makeNodeFromList";
      var0[121] = "captureAndCreateNode";
      var0[122] = "leftShift";
      var0[123] = "<$constructor$>";
      var0[124] = "make";
      var0[125] = "captureAndCreateNode";
      var0[126] = "captureAndCreateNode";
      var0[127] = "captureAndCreateNode";
      var0[128] = "captureAndCreateNode";
      var0[129] = "leftShift";
      var0[130] = "<$constructor$>";
      var0[131] = "captureAndCreateNode";
      var0[132] = "<$constructor$>";
      var0[133] = "lookupKeyword";
      var0[134] = "UNKNOWN";
      var0[135] = "lookupSymbol";
      var0[136] = "UNKNOWN";
      var0[137] = "<$constructor$>";
      var0[138] = "leftShift";
      var0[139] = "<$constructor$>";
      var0[140] = "<$constructor$>";
      var0[141] = "leftShift";
      var0[142] = "<$constructor$>";
      var0[143] = "<$constructor$>";
      var0[144] = "getFrom";
      var0[145] = "<$constructor$>";
      var0[146] = "getTo";
      var0[147] = "captureAndCreateNode";
      var0[148] = "each";
      var0[149] = "entrySet";
      var0[150] = "captureAndCreateNode";
      var0[151] = "captureAndCreateNode";
      var0[152] = "captureAndCreateNode";
      var0[153] = "captureAndCreateNode";
      var0[154] = "captureAndCreateNode";
      var0[155] = "captureAndCreateNode";
      var0[156] = "importNode";
      var0[157] = "breakStatement";
      var0[158] = "continueStatement";
      var0[159] = "dynamicVariable";
      var0[160] = "parameter";
      var0[161] = "genericsType";
      var0[162] = "annotation";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[163];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$syntax$Token() {
      Class var10000 = $class$org$codehaus$groovy$syntax$Token;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$syntax$Token = class$("org.codehaus.groovy.syntax.Token");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression = class$("org.codehaus.groovy.ast.expr.MapEntryExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$VariableExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$VariableExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$VariableExpression = class$("org.codehaus.groovy.ast.expr.VariableExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$IllegalArgumentException() {
      Class var10000 = $class$java$lang$IllegalArgumentException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalArgumentException = class$("java.lang.IllegalArgumentException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$AnnotationConstantExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$AnnotationConstantExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$AnnotationConstantExpression = class$("org.codehaus.groovy.ast.expr.AnnotationConstantExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$ImportNode() {
      Class var10000 = $class$org$codehaus$groovy$ast$ImportNode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$ImportNode = class$("org.codehaus.groovy.ast.ImportNode");
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
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$EmptyStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$EmptyStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$EmptyStatement = class$("org.codehaus.groovy.ast.stmt.EmptyStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$DeclarationExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$DeclarationExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$DeclarationExpression = class$("org.codehaus.groovy.ast.expr.DeclarationExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ArgumentListExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ArgumentListExpression = class$("org.codehaus.groovy.ast.expr.ArgumentListExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$FieldExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$FieldExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$FieldExpression = class$("org.codehaus.groovy.ast.expr.FieldExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ConstantExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression = class$("org.codehaus.groovy.ast.expr.ConstantExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ForStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement = class$("org.codehaus.groovy.ast.stmt.ForStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MethodCallExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MethodCallExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MethodCallExpression = class$("org.codehaus.groovy.ast.expr.MethodCallExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$BooleanExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression = class$("org.codehaus.groovy.ast.expr.BooleanExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$AnnotationNode() {
      Class var10000 = $class$org$codehaus$groovy$ast$AnnotationNode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$AnnotationNode = class$("org.codehaus.groovy.ast.AnnotationNode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$ClassHelper() {
      Class var10000 = $class$org$codehaus$groovy$ast$ClassHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$ClassHelper = class$("org.codehaus.groovy.ast.ClassHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$UnaryPlusExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression = class$("org.codehaus.groovy.ast.expr.UnaryPlusExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$GenericsType() {
      Class var10000 = $class$org$codehaus$groovy$ast$GenericsType;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$GenericsType = class$("org.codehaus.groovy.ast.GenericsType");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$Parameter() {
      Class var10000 = $class$org$codehaus$groovy$ast$Parameter;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$Parameter = class$("org.codehaus.groovy.ast.Parameter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$WhileStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$WhileStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$WhileStatement = class$("org.codehaus.groovy.ast.stmt.WhileStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
      Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
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
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$syntax$Types() {
      Class var10000 = $class$org$codehaus$groovy$syntax$Types;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$syntax$Types = class$("org.codehaus.groovy.syntax.Types");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ClosureListExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ClosureListExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ClosureListExpression = class$("org.codehaus.groovy.ast.expr.ClosureListExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$BinaryExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$BinaryExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$BinaryExpression = class$("org.codehaus.groovy.ast.expr.BinaryExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression = class$("org.codehaus.groovy.ast.expr.NamedArgumentListExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ExpressionStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ExpressionStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ExpressionStatement = class$("org.codehaus.groovy.ast.stmt.ExpressionStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MethodPointerExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MethodPointerExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MethodPointerExpression = class$("org.codehaus.groovy.ast.expr.MethodPointerExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$DynamicVariable() {
      Class var10000 = $class$org$codehaus$groovy$ast$DynamicVariable;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$DynamicVariable = class$("org.codehaus.groovy.ast.DynamicVariable");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ClosureExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ClosureExpression = class$("org.codehaus.groovy.ast.expr.ClosureExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$CatchStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$CatchStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$CatchStatement = class$("org.codehaus.groovy.ast.stmt.CatchStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$Statement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$Statement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$Statement = class$("org.codehaus.groovy.ast.stmt.Statement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression = class$("org.codehaus.groovy.ast.expr.BitwiseNegationExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$TupleExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$TupleExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$TupleExpression = class$("org.codehaus.groovy.ast.expr.TupleExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ClassExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ClassExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ClassExpression = class$("org.codehaus.groovy.ast.expr.ClassExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$FieldNode() {
      Class var10000 = $class$org$codehaus$groovy$ast$FieldNode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$FieldNode = class$("org.codehaus.groovy.ast.FieldNode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ConstructorCallExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ConstructorCallExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ConstructorCallExpression = class$("org.codehaus.groovy.ast.expr.ConstructorCallExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression = class$("org.codehaus.groovy.ast.expr.ElvisOperatorExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$SpreadExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$SpreadExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$SpreadExpression = class$("org.codehaus.groovy.ast.expr.SpreadExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$SpreadMapExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$SpreadMapExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$SpreadMapExpression = class$("org.codehaus.groovy.ast.expr.SpreadMapExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ThrowStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ThrowStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ThrowStatement = class$("org.codehaus.groovy.ast.stmt.ThrowStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$GStringExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$GStringExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$GStringExpression = class$("org.codehaus.groovy.ast.expr.GStringExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$PostfixExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$PostfixExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$PostfixExpression = class$("org.codehaus.groovy.ast.expr.PostfixExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$AttributeExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$AttributeExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$AttributeExpression = class$("org.codehaus.groovy.ast.expr.AttributeExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ReturnStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ReturnStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ReturnStatement = class$("org.codehaus.groovy.ast.stmt.ReturnStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ListExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ListExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ListExpression = class$("org.codehaus.groovy.ast.expr.ListExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$RegexExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$RegexExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$RegexExpression = class$("org.codehaus.groovy.ast.expr.RegexExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$SynchronizedStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement = class$("org.codehaus.groovy.ast.stmt.SynchronizedStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$CaseStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$CaseStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$CaseStatement = class$("org.codehaus.groovy.ast.stmt.CaseStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$IfStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement = class$("org.codehaus.groovy.ast.stmt.IfStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ContinueStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ContinueStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ContinueStatement = class$("org.codehaus.groovy.ast.stmt.ContinueStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$PrefixExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$PrefixExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$PrefixExpression = class$("org.codehaus.groovy.ast.expr.PrefixExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$CastExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$CastExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$CastExpression = class$("org.codehaus.groovy.ast.expr.CastExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$org$codehaus$groovy$ast$Parameter() {
      Class var10000 = array$$class$org$codehaus$groovy$ast$Parameter;
      if (var10000 == null) {
         var10000 = array$$class$org$codehaus$groovy$ast$Parameter = class$("[Lorg.codehaus.groovy.ast.Parameter;");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$RangeExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$RangeExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$RangeExpression = class$("org.codehaus.groovy.ast.expr.RangeExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MapExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MapExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MapExpression = class$("org.codehaus.groovy.ast.expr.MapExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$BreakStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$BreakStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$BreakStatement = class$("org.codehaus.groovy.ast.stmt.BreakStatement");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$ArrayList() {
      Class var10000 = $class$java$util$ArrayList;
      if (var10000 == null) {
         var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$PropertyExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$PropertyExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$PropertyExpression = class$("org.codehaus.groovy.ast.expr.PropertyExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$NotExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$NotExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$NotExpression = class$("org.codehaus.groovy.ast.expr.NotExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$org$codehaus$groovy$ast$ClassNode() {
      Class var10000 = array$$class$org$codehaus$groovy$ast$ClassNode;
      if (var10000 == null) {
         var10000 = array$$class$org$codehaus$groovy$ast$ClassNode = class$("[Lorg.codehaus.groovy.ast.ClassNode;");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$UnaryMinusExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$UnaryMinusExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$UnaryMinusExpression = class$("org.codehaus.groovy.ast.expr.UnaryMinusExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$expr$TernaryExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$TernaryExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$TernaryExpression = class$("org.codehaus.groovy.ast.expr.TernaryExpression");
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
