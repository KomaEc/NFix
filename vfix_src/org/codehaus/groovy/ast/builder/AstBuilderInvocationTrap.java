package org.codehaus.groovy.ast.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.RangeExpression;
import org.codehaus.groovy.ast.expr.RegexExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.SpreadMapExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.codehaus.groovy.ast.expr.UnaryPlusExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.classgen.BytecodeExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.io.ReaderSource;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

private class AstBuilderInvocationTrap extends CodeVisitorSupport implements GroovyObject {
   private final List<String> factoryTargets;
   private final ReaderSource source;
   private final SourceUnit sourceUnit;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204698L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204698 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ArgumentListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ConstantExpression;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$io$ReaderSource;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$SourceUnit;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$syntax$SyntaxException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$messages$SyntaxErrorMessage;

   public AstBuilderInvocationTrap(List<ImportNode> imports, List<String> importPackages, ReaderSource source, SourceUnit sourceUnit) {
      CallSite[] var5 = $getCallSiteArray();
      this.factoryTargets = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      if (!DefaultTypeTransformation.booleanUnbox(source)) {
         throw (Throwable)var5[0].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: source");
      } else if (!DefaultTypeTransformation.booleanUnbox(sourceUnit)) {
         throw (Throwable)var5[1].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: sourceUnit");
      } else {
         this.source = (ReaderSource)ScriptBytecodeAdapter.castToType(source, $get$$class$org$codehaus$groovy$control$io$ReaderSource());
         this.sourceUnit = (SourceUnit)ScriptBytecodeAdapter.castToType(sourceUnit, $get$$class$org$codehaus$groovy$control$SourceUnit());
         var5[2].call(this.factoryTargets, (Object)"org.codehaus.groovy.ast.builder.AstBuilder");
         var5[3].callSafe(imports, (Object)(new AstBuilderInvocationTrap._closure1(this, this)));
         if (DefaultTypeTransformation.booleanUnbox(var5[4].call(importPackages, (Object)"org.codehaus.groovy.ast.builder."))) {
            var5[5].call(this.factoryTargets, (Object)"AstBuilder");
         }

      }
   }

   private void addError(String msg, ASTNode expr) {
      CallSite[] var3 = $getCallSiteArray();
      Integer line = (Integer)ScriptBytecodeAdapter.castToType(var3[6].call(expr), $get$$class$java$lang$Integer());
      Integer col = (Integer)ScriptBytecodeAdapter.castToType(var3[7].call(expr), $get$$class$java$lang$Integer());
      var3[8].call(var3[9].call(this.sourceUnit), var3[10].callConstructor($get$$class$org$codehaus$groovy$control$messages$SyntaxErrorMessage(), var3[11].callConstructor($get$$class$org$codehaus$groovy$syntax$SyntaxException(), var3[12].call(msg, (Object)"\n"), line, col), this.sourceUnit));
   }

   public void visitMethodCallExpression(MethodCallExpression call) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[13].callCurrent(this, (Object)call))) {
         Object closureExpression = var2[14].callSafe(var2[15].callGetProperty(var2[16].callGetProperty(call)), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return itx.get() instanceof ClosureExpression ? Boolean.TRUE : Boolean.FALSE;
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2()) {
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
               var0[0] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[1];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure2 = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap$_visitMethodCallExpression_closure2");
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
         Object otherArgs = var2[17].callSafe(var2[18].callGetProperty(var2[19].callGetProperty(call)), (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return !(itx.get() instanceof ClosureExpression) ? Boolean.TRUE : Boolean.FALSE;
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3()) {
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
               var0[0] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[1];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_visitMethodCallExpression_closure3 = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap$_visitMethodCallExpression_closure3");
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
         String source = (String)ScriptBytecodeAdapter.castToType(var2[20].callCurrent(this, (Object)closureExpression), $get$$class$java$lang$String());
         var2[21].call(otherArgs, var2[22].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)source));
         ScriptBytecodeAdapter.setProperty(var2[23].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression(), (Object)otherArgs), $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), call, "arguments");
         ScriptBytecodeAdapter.setProperty(var2[24].callConstructor($get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), (Object)"buildFromBlock"), $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), call, "method");
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), call, "spreadSafe");
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), call, "safe");
         ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), call, "implicitThis");
      } else {
         var2[25].call(var2[26].call(call), (Object)this);
         var2[27].call(var2[28].call(call), (Object)this);
         var2[29].call(var2[30].call(call), (Object)this);
      }

   }

   private boolean isBuildInvocation(MethodCallExpression call) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(call, (Object)null)) {
         throw (Throwable)var2[31].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: call");
      } else {
         if (DefaultTypeTransformation.booleanUnbox(var2[32].callGetProperty(call) instanceof ConstantExpression && ScriptBytecodeAdapter.compareEqual("buildFromCode", var2[33].callGetPropertySafe(var2[34].callGetProperty(call))) ? Boolean.TRUE : Boolean.FALSE)) {
            String name = (String)ScriptBytecodeAdapter.castToType(var2[35].callGetPropertySafe(var2[36].callGetPropertySafe(var2[37].callGetProperty(call))), $get$$class$java$lang$String());
            if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(name) && DefaultTypeTransformation.booleanUnbox(var2[38].call(this.factoryTargets, (Object)name)) ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var2[39].callGetProperty(call)) && var2[40].callGetProperty(call) instanceof TupleExpression ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(var2[41].callSafe(var2[42].callGetProperty(var2[43].callGetProperty(call)), (Object)(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return itx.get() instanceof ClosureExpression ? Boolean.TRUE : Boolean.FALSE;
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4()) {
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
                  var0[0] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[1];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_isBuildInvocation_closure4 = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap$_isBuildInvocation_closure4");
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
            })))) {
               return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
            }
         }

         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      }
   }

   private String convertClosureToSource(ClosureExpression expression) {
      ClosureExpression expression = new Reference(expression);
      CallSite[] var3 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(expression.get(), (Object)null)) {
         throw (Throwable)var3[44].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: expression");
      } else {
         Object lineRange = ScriptBytecodeAdapter.createRange(var3[45].callGetProperty(expression.get()), var3[46].callGetProperty(expression.get()), true);
         Object source = var3[47].callSafe(var3[48].callSafe(var3[49].call(lineRange, (Object)(new GeneratedClosure(this, this, expression) {
            private Reference<T> expression;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static final Integer $const$1 = (Integer)1;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$expr$ClosureExpression;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.expression = (Reference)expression;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               Object line = new Reference(var3[0].call(var3[1].callGroovyObjectGetProperty(this), itx.get(), (Object)null));
               if (ScriptBytecodeAdapter.compareEqual(line.get(), (Object)null)) {
                  var3[2].callCurrent(this, new GStringImpl(new Object[]{itx.get(), var3[3].callGetProperty(var3[4].callGroovyObjectGetProperty(this))}, new String[]{"Error calculating source code for expression. Trying to read line ", " from ", ""}), this.expression.get());
               }

               if (ScriptBytecodeAdapter.compareEqual(itx.get(), var3[5].callGetProperty(this.expression.get()))) {
                  line.set(var3[6].call(line.get(), $const$0, var3[7].call(var3[8].callGetProperty(this.expression.get()), (Object)$const$1)));
               }

               if (ScriptBytecodeAdapter.compareEqual(itx.get(), var3[9].callGetProperty(this.expression.get()))) {
                  line.set(var3[10].call(line.get(), var3[11].call(var3[12].callGetProperty(this.expression.get()), (Object)$const$1)));
               }

               return line.get();
            }

            public ClosureExpression getExpression() {
               CallSite[] var1 = $getCallSiteArray();
               return (ClosureExpression)ScriptBytecodeAdapter.castToType(this.expression.get(), $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[13].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5()) {
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
               var0[0] = "getLine";
               var0[1] = "source";
               var0[2] = "addError";
               var0[3] = "class";
               var0[4] = "source";
               var0[5] = "lastLineNumber";
               var0[6] = "substring";
               var0[7] = "minus";
               var0[8] = "lastColumnNumber";
               var0[9] = "lineNumber";
               var0[10] = "substring";
               var0[11] = "minus";
               var0[12] = "columnNumber";
               var0[13] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[14];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_convertClosureToSource_closure5 = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap$_convertClosureToSource_closure5");
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
            private static Class $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression() {
               Class var10000 = $class$org$codehaus$groovy$ast$expr$ClosureExpression;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$expr$ClosureExpression = class$("org.codehaus.groovy.ast.expr.ClosureExpression");
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
         })), (Object)"\n"));
         if (!DefaultTypeTransformation.booleanUnbox(var3[50].call(source, (Object)"{"))) {
            var3[51].callCurrent(this, var3[52].call("Error converting ClosureExpression into source code. ", (Object)(new GStringImpl(new Object[]{source}, new String[]{"Closures must start with {. Found: ", ""}))), expression.get());
         }

         return (String)ScriptBytecodeAdapter.castToType(source, $get$$class$java$lang$String());
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap()) {
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
   public void this$3$addError(String var1, ASTNode var2) {
      this.addError(var1, var2);
   }

   // $FF: synthetic method
   public boolean this$3$isBuildInvocation(MethodCallExpression var1) {
      return this.isBuildInvocation(var1);
   }

   // $FF: synthetic method
   public String this$3$convertClosureToSource(ClosureExpression var1) {
      return this.convertClosureToSource(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$2$visitArrayExpression(ArrayExpression var1) {
      super.visitArrayExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitContinueStatement(ContinueStatement var1) {
      super.visitContinueStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBinaryExpression(BinaryExpression var1) {
      super.visitBinaryExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitConstantExpression(ConstantExpression var1) {
      super.visitConstantExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitTernaryExpression(TernaryExpression var1) {
      super.visitTernaryExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitConstructorCallExpression(ConstructorCallExpression var1) {
      super.visitConstructorCallExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitForLoop(ForStatement var1) {
      super.visitForLoop(var1);
   }

   // $FF: synthetic method
   public void super$2$visitListOfExpressions(List var1) {
      super.visitListOfExpressions(var1);
   }

   // $FF: synthetic method
   public void super$2$visitGStringExpression(GStringExpression var1) {
      super.visitGStringExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitVariableExpression(VariableExpression var1) {
      super.visitVariableExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBreakStatement(BreakStatement var1) {
      super.visitBreakStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitUnaryMinusExpression(UnaryMinusExpression var1) {
      super.visitUnaryMinusExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitMethodCallExpression(MethodCallExpression var1) {
      super.visitMethodCallExpression(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$visitRangeExpression(RangeExpression var1) {
      super.visitRangeExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitExpressionStatement(ExpressionStatement var1) {
      super.visitExpressionStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitMapEntryExpression(MapEntryExpression var1) {
      super.visitMapEntryExpression(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$2$visitDeclarationExpression(DeclarationExpression var1) {
      super.visitDeclarationExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitListExpression(ListExpression var1) {
      super.visitListExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitMapExpression(MapExpression var1) {
      super.visitMapExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitPostfixExpression(PostfixExpression var1) {
      super.visitPostfixExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitSynchronizedStatement(SynchronizedStatement var1) {
      super.visitSynchronizedStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBitwiseNegationExpression(BitwiseNegationExpression var1) {
      super.visitBitwiseNegationExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitCastExpression(CastExpression var1) {
      super.visitCastExpression(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$2$visitStaticMethodCallExpression(StaticMethodCallExpression var1) {
      super.visitStaticMethodCallExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitCaseStatement(CaseStatement var1) {
      super.visitCaseStatement(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$visitFieldExpression(FieldExpression var1) {
      super.visitFieldExpression(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$2$visitThrowStatement(ThrowStatement var1) {
      super.visitThrowStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitSpreadMapExpression(SpreadMapExpression var1) {
      super.visitSpreadMapExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitMethodPointerExpression(MethodPointerExpression var1) {
      super.visitMethodPointerExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitClosureExpression(ClosureExpression var1) {
      super.visitClosureExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitSwitch(SwitchStatement var1) {
      super.visitSwitch(var1);
   }

   // $FF: synthetic method
   public void super$2$visitTryCatchFinally(TryCatchStatement var1) {
      super.visitTryCatchFinally(var1);
   }

   // $FF: synthetic method
   public void super$2$visitEmptyStatement(EmptyStatement var1) {
      super.visitEmptyStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitIfElse(IfStatement var1) {
      super.visitIfElse(var1);
   }

   // $FF: synthetic method
   public void super$2$visitClosureListExpression(ClosureListExpression var1) {
      super.visitClosureListExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitPrefixExpression(PrefixExpression var1) {
      super.visitPrefixExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBlockStatement(BlockStatement var1) {
      super.visitBlockStatement(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$2$visitShortTernaryExpression(ElvisOperatorExpression var1) {
      super.visitShortTernaryExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitSpreadExpression(SpreadExpression var1) {
      super.visitSpreadExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitRegexExpression(RegexExpression var1) {
      super.visitRegexExpression(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$visitTupleExpression(TupleExpression var1) {
      super.visitTupleExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitReturnStatement(ReturnStatement var1) {
      super.visitReturnStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitCatchStatement(CatchStatement var1) {
      super.visitCatchStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitNotExpression(NotExpression var1) {
      super.visitNotExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitPropertyExpression(PropertyExpression var1) {
      super.visitPropertyExpression(var1);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$visitAttributeExpression(AttributeExpression var1) {
      super.visitAttributeExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBooleanExpression(BooleanExpression var1) {
      super.visitBooleanExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitUnaryPlusExpression(UnaryPlusExpression var1) {
      super.visitUnaryPlusExpression(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$2$visitBytecodeExpression(BytecodeExpression var1) {
      super.visitBytecodeExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitDoWhileLoop(DoWhileStatement var1) {
      super.visitDoWhileLoop(var1);
   }

   // $FF: synthetic method
   public void super$2$visitWhileLoop(WhileStatement var1) {
      super.visitWhileLoop(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public void super$2$visitClassExpression(ClassExpression var1) {
      super.visitClassExpression(var1);
   }

   // $FF: synthetic method
   public void super$2$visitAssertStatement(AssertStatement var1) {
      super.visitAssertStatement(var1);
   }

   // $FF: synthetic method
   public void super$2$visitArgumentlistExpression(ArgumentListExpression var1) {
      super.visitArgumentlistExpression(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "leftShift";
      var0[3] = "each";
      var0[4] = "contains";
      var0[5] = "leftShift";
      var0[6] = "getLineNumber";
      var0[7] = "getColumnNumber";
      var0[8] = "addErrorAndContinue";
      var0[9] = "getErrorCollector";
      var0[10] = "<$constructor$>";
      var0[11] = "<$constructor$>";
      var0[12] = "plus";
      var0[13] = "isBuildInvocation";
      var0[14] = "find";
      var0[15] = "expressions";
      var0[16] = "arguments";
      var0[17] = "findAll";
      var0[18] = "expressions";
      var0[19] = "arguments";
      var0[20] = "convertClosureToSource";
      var0[21] = "leftShift";
      var0[22] = "<$constructor$>";
      var0[23] = "<$constructor$>";
      var0[24] = "<$constructor$>";
      var0[25] = "visit";
      var0[26] = "getObjectExpression";
      var0[27] = "visit";
      var0[28] = "getMethod";
      var0[29] = "visit";
      var0[30] = "getArguments";
      var0[31] = "<$constructor$>";
      var0[32] = "method";
      var0[33] = "value";
      var0[34] = "method";
      var0[35] = "name";
      var0[36] = "type";
      var0[37] = "objectExpression";
      var0[38] = "contains";
      var0[39] = "arguments";
      var0[40] = "arguments";
      var0[41] = "find";
      var0[42] = "expressions";
      var0[43] = "arguments";
      var0[44] = "<$constructor$>";
      var0[45] = "lineNumber";
      var0[46] = "lastLineNumber";
      var0[47] = "trim";
      var0[48] = "join";
      var0[49] = "collect";
      var0[50] = "startsWith";
      var0[51] = "addError";
      var0[52] = "plus";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[53];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$ConstantExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$ConstantExpression = class$("org.codehaus.groovy.ast.expr.ConstantExpression");
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$control$io$ReaderSource() {
      Class var10000 = $class$org$codehaus$groovy$control$io$ReaderSource;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$io$ReaderSource = class$("org.codehaus.groovy.control.io.ReaderSource");
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
   private static Class $get$$class$org$codehaus$groovy$control$SourceUnit() {
      Class var10000 = $class$org$codehaus$groovy$control$SourceUnit;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$SourceUnit = class$("org.codehaus.groovy.control.SourceUnit");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$syntax$SyntaxException() {
      Class var10000 = $class$org$codehaus$groovy$syntax$SyntaxException;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$syntax$SyntaxException = class$("org.codehaus.groovy.syntax.SyntaxException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$control$messages$SyntaxErrorMessage() {
      Class var10000 = $class$org$codehaus$groovy$control$messages$SyntaxErrorMessage;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$messages$SyntaxErrorMessage = class$("org.codehaus.groovy.control.messages.SyntaxErrorMessage");
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
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(ImportNode importStatement) {
         ImportNode importStatementx = new Reference(importStatement);
         CallSite[] var3 = $getCallSiteArray();
         return ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(var3[1].callGetProperty(importStatementx.get())), "org.codehaus.groovy.ast.builder.AstBuilder") ? var3[2].call(var3[3].callGroovyObjectGetProperty(this), var3[4].callGetProperty(importStatementx.get())) : null;
      }

      public Object call(ImportNode importStatement) {
         ImportNode importStatementx = new Reference(importStatement);
         CallSite[] var3 = $getCallSiteArray();
         return var3[5].callCurrent(this, (Object)importStatementx.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1()) {
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
         var0[1] = "type";
         var0[2] = "leftShift";
         var0[3] = "factoryTargets";
         var0[4] = "alias";
         var0[5] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[6];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilderInvocationTrap$_closure1 = class$("org.codehaus.groovy.ast.builder.AstBuilderInvocationTrap$_closure1");
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
