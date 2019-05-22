package groovy.inspect.swingui;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Stack;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
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
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
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
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstNodeToScriptVisitor extends CompilationUnit.PrimaryClassNodeOperation implements GroovyCodeVisitor, GroovyClassVisitor, GroovyObject {
   private Writer _out;
   private Stack<String> classNameStack;
   private String _indent;
   private boolean readyToIndent;
   private boolean showScriptFreeForm;
   private boolean showScriptClass;
   private boolean scriptHasBeenVisited;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)-2;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204115L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204115 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$util$Stack;
   // $FF: synthetic field
   private static Class $class$java$lang$UnsupportedOperationException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$VariableExpression;
   // $FF: synthetic field
   private static Class $class$java$lang$reflect$Modifier;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ArgumentListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ConstantExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ForStatement;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$io$Writer;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$Expression;

   public AstNodeToScriptVisitor(Writer writer, boolean showScriptFreeForm, boolean showScriptClass) {
      CallSite[] var4 = $getCallSiteArray();
      this.classNameStack = (Stack)ScriptBytecodeAdapter.castToType(var4[0].callConstructor($get$$class$java$util$Stack()), $get$$class$java$util$Stack());
      this._indent = (String)ScriptBytecodeAdapter.castToType("", $get$$class$java$lang$String());
      this.readyToIndent = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this._out = (Writer)ScriptBytecodeAdapter.castToType(writer, $get$$class$java$io$Writer());
      this.showScriptFreeForm = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(showScriptFreeForm));
      this.showScriptClass = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(showScriptClass));
      this.scriptHasBeenVisited = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
   }

   public AstNodeToScriptVisitor(Writer writer, boolean showScriptFreeForm) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createPojoWrapper(writer, $get$$class$java$io$Writer()), ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.box(showScriptFreeForm), $get$$class$java$lang$Boolean()), Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this((Writer)var10001[0]);
         break;
      case 1:
         this((Writer)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      case 2:
         this((Writer)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]), DefaultTypeTransformation.booleanUnbox(var10001[2]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public AstNodeToScriptVisitor(Writer writer) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createPojoWrapper(writer, $get$$class$java$io$Writer()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this((Writer)var10001[0]);
         break;
      case 1:
         this((Writer)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      case 2:
         this((Writer)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]), DefaultTypeTransformation.booleanUnbox(var10001[2]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.showScriptFreeForm)) && !DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.scriptHasBeenVisited)) ? Boolean.TRUE : Boolean.FALSE)) {
         this.scriptHasBeenVisited = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
         var4[1].callSafe(var4[2].callSafe(var4[3].callSafe(source)), (Object)this);
      }

      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.showScriptClass)) && DefaultTypeTransformation.booleanUnbox(var4[4].call(classNode)) ? Boolean.FALSE : Boolean.TRUE)) {
         var4[5].callCurrent(this, (Object)classNode);
      }

   }

   public void print(Object parameter) {
      CallSite[] var2 = $getCallSiteArray();
      Object output = var2[6].call(parameter);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.readyToIndent))) {
         var2[7].call(this._out, (Object)this._indent);

         for(this.readyToIndent = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE); DefaultTypeTransformation.booleanUnbox(var2[8].call(output, (Object)" ")); output = var2[9].call(output, (Object)ScriptBytecodeAdapter.createRange($const$0, $const$1, true))) {
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[10].call(var2[11].call(this._out), (Object)" ")) && DefaultTypeTransformation.booleanUnbox(var2[12].call(output, (Object)" "))) {
         output = var2[13].call(output, (Object)ScriptBytecodeAdapter.createRange($const$0, $const$1, true));
      }

      var2[14].call(this._out, (Object)output);
   }

   public Object println(Object parameter) {
      CallSite[] var2 = $getCallSiteArray();
      throw (Throwable)var2[15].callConstructor($get$$class$java$lang$UnsupportedOperationException(), (Object)"Wrong API");
   }

   public Object indented(Closure block) {
      CallSite[] var2 = $getCallSiteArray();
      String startingIndent = this._indent;
      this._indent = (String)ScriptBytecodeAdapter.castToType((String)ScriptBytecodeAdapter.castToType(var2[16].call(this._indent, (Object)"    "), $get$$class$java$lang$String()), $get$$class$java$lang$String());
      var2[17].call(block);
      this._indent = (String)ScriptBytecodeAdapter.castToType(startingIndent, $get$$class$java$lang$String());
      return startingIndent;
   }

   public Object printLineBreak() {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(var1[18].call(var1[19].call(this._out), (Object)"\n"))) {
         var1[20].call(this._out, (Object)"\n");
      }

      Boolean var10000 = Boolean.TRUE;
      this.readyToIndent = DefaultTypeTransformation.booleanUnbox(var10000);
      return var10000;
   }

   public Object printDoubleBreak() {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(var1[21].call(var1[22].call(this._out), (Object)"\n\n"))) {
         if (DefaultTypeTransformation.booleanUnbox(var1[23].call(var1[24].call(this._out), (Object)"\n"))) {
            var1[25].call(this._out, (Object)"\n");
         } else {
            var1[26].call(this._out, (Object)"\n");
            var1[27].call(this._out, (Object)"\n");
         }
      }

      Boolean var10000 = Boolean.TRUE;
      this.readyToIndent = DefaultTypeTransformation.booleanUnbox(var10000);
      return var10000;
   }

   public Object AstNodeToScriptAdapter(Writer out) {
      CallSite[] var2 = $getCallSiteArray();
      this._out = (Writer)ScriptBytecodeAdapter.castToType(out, $get$$class$java$io$Writer());
      return out;
   }

   public void visitClass(ClassNode node) {
      ClassNode node = new Reference(node);
      CallSite[] var3 = $getCallSiteArray();
      var3[28].call(this.classNameStack, (Object)var3[29].callGetProperty(node.get()));
      if (DefaultTypeTransformation.booleanUnbox(var3[30].callGetPropertySafe(node.get()))) {
         if (DefaultTypeTransformation.booleanUnbox(var3[31].call(var3[32].callGetProperty(var3[33].callGetProperty(node.get())), (Object)"."))) {
            var3[34].callCurrent(this, (Object)var3[35].call(var3[36].callGetProperty(var3[37].callGetProperty(node.get())), (Object)ScriptBytecodeAdapter.createRange($const$2, $const$3, true)));
         } else {
            var3[38].callCurrent(this, (Object)var3[39].callGetProperty(var3[40].callGetProperty(node.get())));
         }

         var3[41].callCurrent(this);
      }

      var3[42].callCurrent(this, (Object)var3[43].callGetProperty(node.get()));
      var3[44].callCurrent(this, (Object)(new GStringImpl(new Object[]{var3[45].callGetProperty(node.get())}, new String[]{"class ", ""})));
      var3[46].callCurrent(this, (Object)var3[47].callGetPropertySafe(node.get()));
      Boolean first = new Reference(Boolean.TRUE);
      var3[48].callSafe(var3[49].callGetProperty(node.get()), (Object)(new GeneratedClosure(this, this, first) {
         private Reference<T> first;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.first = (Reference)first;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
               var3[0].callCurrent(this, (Object)", ");
            } else {
               var3[1].callCurrent(this, (Object)" implements ");
            }

            this.first.set(Boolean.FALSE);
            return var3[2].callCurrent(this, (Object)itx.get());
         }

         public Boolean getFirst() {
            CallSite[] var1 = $getCallSiteArray();
            return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1()) {
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
            var0[1] = "print";
            var0[2] = "visitType";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1(), var0);
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
         private static Class $get$$class$java$lang$Boolean() {
            Class var10000 = $class$java$lang$Boolean;
            if (var10000 == null) {
               var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure1 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure1");
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
      var3[50].callCurrent(this, (Object)" extends ");
      var3[51].callCurrent(this, (Object)var3[52].callGetProperty(node.get()));
      var3[53].callCurrent(this, (Object)" { ");
      var3[54].callCurrent(this);
      var3[55].callCurrent(this, (Object)(new GeneratedClosure(this, this, node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$ClassNode;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callSafe(var2[1].callGetPropertySafe(this.node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26()) {
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
                  var0[0] = "visitProperty";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure26 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure2_closure26");
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
            }));
            var2[2].callCurrent(this);
            var2[3].callSafe(var2[4].callGetPropertySafe(this.node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27()) {
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
                  var0[0] = "visitField";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure27 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure2_closure27");
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
            }));
            var2[5].callCurrent(this);
            var2[6].callSafe(var2[7].callGetPropertySafe(this.node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28()) {
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
                  var0[0] = "visitConstructor";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure28 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure2_closure28");
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
            var2[8].callCurrent(this);
            return var2[9].callSafe(var2[10].callGetPropertySafe(this.node.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29()) {
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
                  var0[0] = "visitMethod";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2_closure29 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure2_closure29");
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
            }));
         }

         public ClassNode getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return (ClassNode)ScriptBytecodeAdapter.castToType(this.node.get(), $get$$class$org$codehaus$groovy$ast$ClassNode());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[11].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2()) {
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
            var0[1] = "properties";
            var0[2] = "printLineBreak";
            var0[3] = "each";
            var0[4] = "fields";
            var0[5] = "printDoubleBreak";
            var0[6] = "each";
            var0[7] = "declaredConstructors";
            var0[8] = "printLineBreak";
            var0[9] = "each";
            var0[10] = "methods";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$ClassNode() {
            Class var10000 = $class$org$codehaus$groovy$ast$ClassNode;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$ClassNode = class$("org.codehaus.groovy.ast.ClassNode");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClass_closure2 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClass_closure2");
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
      var3[56].callCurrent(this, (Object)"}");
      var3[57].callCurrent(this);
      var3[58].call(this.classNameStack);
   }

   private void visitGenerics(GenericsType... generics) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(generics)) {
         var2[59].callCurrent(this, (Object)"<");
         Boolean first = new Reference(Boolean.TRUE);
         var2[60].call(generics, (Object)(new GeneratedClosure(this, this, first) {
            private Reference<T> first;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Boolean;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.first = (Reference)first;
            }

            public Object doCall(GenericsType it) {
               GenericsType itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
                  var3[0].callCurrent(this, (Object)", ");
               }

               this.first.set(Boolean.FALSE);
               var3[1].callCurrent(this, (Object)var3[2].callGetProperty(itx.get()));
               if (DefaultTypeTransformation.booleanUnbox(var3[3].callGetProperty(itx.get()))) {
                  var3[4].callCurrent(this, (Object)" extends ");
                  Boolean innerFirst = new Reference(Boolean.TRUE);
                  var3[5].call(var3[6].callGetProperty(itx.get()), (Object)(new GeneratedClosure(this, this.getThisObject(), innerFirst) {
                     private Reference<T> innerFirst;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Boolean;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.innerFirst = (Reference)innerFirst;
                     }

                     public Object doCall(ClassNode upperBound) {
                        ClassNode upperBoundx = new Reference(upperBound);
                        CallSite[] var3 = $getCallSiteArray();
                        if (!DefaultTypeTransformation.booleanUnbox(this.innerFirst.get())) {
                           var3[0].callCurrent(this, (Object)" & ");
                        }

                        this.innerFirst.set(Boolean.FALSE);
                        return var3[1].callCurrent(this, (Object)upperBoundx.get());
                     }

                     public Object call(ClassNode upperBound) {
                        ClassNode upperBoundx = new Reference(upperBound);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[2].callCurrent(this, (Object)upperBoundx.get());
                     }

                     public Boolean getInnerFirst() {
                        CallSite[] var1 = $getCallSiteArray();
                        return (Boolean)ScriptBytecodeAdapter.castToType(this.innerFirst.get(), $get$$class$java$lang$Boolean());
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30()) {
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
                        var0[1] = "visitType";
                        var0[2] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[3];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30(), var0);
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
                     private static Class $get$$class$java$lang$Boolean() {
                        Class var10000 = $class$java$lang$Boolean;
                        if (var10000 == null) {
                           var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
                        }

                        return var10000;
                     }

                     // $FF: synthetic method
                     private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30() {
                        Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3_closure30 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitGenerics_closure3_closure30");
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

               if (DefaultTypeTransformation.booleanUnbox(var3[7].callGetProperty(itx.get()))) {
                  var3[8].callCurrent(this, (Object)" super ");
                  return var3[9].callCurrent(this, (Object)var3[10].callGetProperty(itx.get()));
               } else {
                  return null;
               }
            }

            public Object call(GenericsType it) {
               GenericsType itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[11].callCurrent(this, (Object)itx.get());
            }

            public Boolean getFirst() {
               CallSite[] var1 = $getCallSiteArray();
               return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3()) {
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
               var0[1] = "print";
               var0[2] = "name";
               var0[3] = "upperBounds";
               var0[4] = "print";
               var0[5] = "each";
               var0[6] = "upperBounds";
               var0[7] = "lowerBound";
               var0[8] = "print";
               var0[9] = "visitType";
               var0[10] = "lowerBound";
               var0[11] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[12];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3(), var0);
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
            private static Class $get$$class$java$lang$Boolean() {
               Class var10000 = $class$java$lang$Boolean;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3() {
               Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitGenerics_closure3 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitGenerics_closure3");
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
         var2[61].callCurrent(this, (Object)">");
      }

   }

   public void visitConstructor(ConstructorNode node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[62].callCurrent(this, (Object)node);
   }

   private String visitParameters(Object parameters) {
      CallSite[] var2 = $getCallSiteArray();
      Boolean first = new Reference(Boolean.TRUE);
      return (String)ScriptBytecodeAdapter.castToType(var2[63].call(parameters, (Object)(new GeneratedClosure(this, this, first) {
         private Reference<T> first;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.first = (Reference)first;
         }

         public Object doCall(Parameter it) {
            Parameter itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
               var3[0].callCurrent(this, (Object)", ");
            }

            this.first.set(Boolean.FALSE);
            var3[1].callCurrent(this, (Object)var3[2].callGetProperty(itx.get()));
            var3[3].callCurrent(this, (Object)var3[4].call(" ", (Object)var3[5].callGetProperty(itx.get())));
            if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[6].callGetProperty(itx.get())) && !(var3[7].callGetProperty(itx.get()) instanceof EmptyExpression) ? Boolean.TRUE : Boolean.FALSE)) {
               var3[8].callCurrent(this, (Object)" = ");
               return var3[9].call(var3[10].callGetProperty(itx.get()), this.getThisObject());
            } else {
               return null;
            }
         }

         public Object call(Parameter it) {
            Parameter itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[11].callCurrent(this, (Object)itx.get());
         }

         public Boolean getFirst() {
            CallSite[] var1 = $getCallSiteArray();
            return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4()) {
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
            var0[1] = "visitType";
            var0[2] = "type";
            var0[3] = "print";
            var0[4] = "plus";
            var0[5] = "name";
            var0[6] = "initialExpression";
            var0[7] = "initialExpression";
            var0[8] = "print";
            var0[9] = "visit";
            var0[10] = "initialExpression";
            var0[11] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[12];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitParameters_closure4 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitParameters_closure4");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      })), $get$$class$java$lang$String());
   }

   public void visitMethod(MethodNode node) {
      MethodNode node = new Reference(node);
      CallSite[] var3 = $getCallSiteArray();
      var3[64].callCurrent(this, (Object)var3[65].callGetProperty(node.get()));
      if (ScriptBytecodeAdapter.compareEqual(var3[66].callGetProperty(node.get()), "<init>")) {
         var3[67].callCurrent(this, (Object)(new GStringImpl(new Object[]{var3[68].call(this.classNameStack)}, new String[]{"", "("})));
         var3[69].callCurrent(this, (Object)var3[70].callGetProperty(node.get()));
         var3[71].callCurrent(this, (Object)") {");
         var3[72].callCurrent(this);
      } else if (ScriptBytecodeAdapter.compareEqual(var3[73].callGetProperty(node.get()), "<clinit>")) {
         var3[74].callCurrent(this, (Object)"static { ");
         var3[75].callCurrent(this);
      } else {
         var3[76].callCurrent(this, (Object)var3[77].callGetProperty(node.get()));
         var3[78].callCurrent(this, (Object)(new GStringImpl(new Object[]{var3[79].callGetProperty(node.get())}, new String[]{" ", "("})));
         var3[80].callCurrent(this, (Object)var3[81].callGetProperty(node.get()));
         var3[82].callCurrent(this, (Object)")");
         if (DefaultTypeTransformation.booleanUnbox(var3[83].callGetProperty(node.get()))) {
            Boolean first = new Reference(Boolean.TRUE);
            var3[84].callCurrent(this, (Object)" throws ");
            var3[85].call(var3[86].callGetProperty(node.get()), (Object)(new GeneratedClosure(this, this, first) {
               private Reference<T> first;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$Boolean;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.first = (Reference)first;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
                     var3[0].callCurrent(this, (Object)", ");
                  }

                  this.first.set(Boolean.FALSE);
                  return var3[1].callCurrent(this, (Object)itx.get());
               }

               public Boolean getFirst() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5()) {
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
                  var0[1] = "visitType";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5(), var0);
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
               private static Class $get$$class$java$lang$Boolean() {
                  Class var10000 = $class$java$lang$Boolean;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure5 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitMethod_closure5");
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

         var3[87].callCurrent(this, (Object)" {");
         var3[88].callCurrent(this);
      }

      var3[89].callCurrent(this, (Object)(new GeneratedClosure(this, this, node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$MethodNode;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.node.get()), this.getThisObject());
         }

         public MethodNode getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return (MethodNode)ScriptBytecodeAdapter.castToType(this.node.get(), $get$$class$org$codehaus$groovy$ast$MethodNode());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6()) {
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
            var0[1] = "code";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitMethod_closure6 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitMethod_closure6");
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
      }));
      var3[90].callCurrent(this);
      var3[91].callCurrent(this, (Object)"}");
      var3[92].callCurrent(this);
   }

   private Object visitModifiers(int modifiers) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[93].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[94].callCurrent(this, (Object)"abstract ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[95].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[96].callCurrent(this, (Object)"final ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[97].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[98].callCurrent(this, (Object)"interface ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[99].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[100].callCurrent(this, (Object)"native ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[101].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[102].callCurrent(this, (Object)"private ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[103].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[104].callCurrent(this, (Object)"protected ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[105].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[106].callCurrent(this, (Object)"public ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[107].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[108].callCurrent(this, (Object)"static ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[109].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[110].callCurrent(this, (Object)"synchronized ");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[111].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers)))) {
         var2[112].callCurrent(this, (Object)"transient ");
      }

      return DefaultTypeTransformation.booleanUnbox(var2[113].call($get$$class$java$lang$reflect$Modifier(), (Object)DefaultTypeTransformation.box(modifiers))) ? var2[114].callCurrent(this, (Object)"volatile ") : null;
   }

   public void visitField(FieldNode node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[115].callSafe(var2[116].callGetPropertySafe(node), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callCurrent(this, (Object)itx.get());
            return var3[1].callCurrent(this);
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7()) {
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
            var0[0] = "visitAnnotationNode";
            var0[1] = "printLineBreak";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitField_closure7 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitField_closure7");
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
      }));
      var2[117].callCurrent(this, (Object)var2[118].callGetProperty(node));
      var2[119].callCurrent(this, (Object)var2[120].callGetProperty(node));
      var2[121].callCurrent(this, (Object)(new GStringImpl(new Object[]{var2[122].callGetProperty(node)}, new String[]{" ", " "})));
      var2[123].callCurrent(this);
   }

   public void visitAnnotationNode(AnnotationNode node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[124].callCurrent(this, (Object)var2[125].call("@", (Object)var2[126].callGetPropertySafe(var2[127].callGetPropertySafe(node))));
      if (DefaultTypeTransformation.booleanUnbox(var2[128].callGetPropertySafe(node))) {
         var2[129].callCurrent(this, (Object)"(");
         Boolean first = new Reference(Boolean.TRUE);
         var2[130].call(var2[131].callGetProperty(node), (Object)(new GeneratedClosure(this, this, first) {
            private Reference<T> first;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8;
            // $FF: synthetic field
            private static Class $class$java$lang$Boolean;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.first = (Reference)first;
            }

            public Object doCall(String name, Expression value) {
               String namex = new Reference(name);
               Expression valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               if (DefaultTypeTransformation.booleanUnbox(this.first.get())) {
                  this.first.set(Boolean.FALSE);
               } else {
                  var5[0].callCurrent(this, (Object)", ");
               }

               var5[1].callCurrent(this, (Object)var5[2].call(namex.get(), (Object)" = "));
               return var5[3].call(valuex.get(), this.getThisObject());
            }

            public Object call(String name, Expression value) {
               String namex = new Reference(name);
               Expression valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[4].callCurrent(this, namex.get(), valuex.get());
            }

            public Boolean getFirst() {
               CallSite[] var1 = $getCallSiteArray();
               return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8()) {
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
               var0[1] = "print";
               var0[2] = "plus";
               var0[3] = "visit";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8(), var0);
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
            private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8() {
               Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitAnnotationNode_closure8 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitAnnotationNode_closure8");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
         var2[132].callCurrent(this, (Object)")");
      }

   }

   public void visitProperty(PropertyNode node) {
      CallSite[] var2 = $getCallSiteArray();
   }

   public void visitBlockStatement(BlockStatement block) {
      CallSite[] var2 = $getCallSiteArray();
      var2[133].callSafe(var2[134].callGetPropertySafe(block), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].call(itx.get(), this.getThisObject());
            return var3[1].callCurrent(this);
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9()) {
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
            var0[1] = "printLineBreak";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitBlockStatement_closure9 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitBlockStatement_closure9");
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
      if (!DefaultTypeTransformation.booleanUnbox(var2[135].call(var2[136].call(this._out), (Object)"\n"))) {
         var2[137].callCurrent(this);
      }

   }

   public void visitForLoop(ForStatement statement) {
      ForStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[138].callCurrent(this, (Object)"for (");
      if (ScriptBytecodeAdapter.compareNotEqual(var3[139].callGetPropertySafe(statement.get()), var3[140].callGetProperty($get$$class$org$codehaus$groovy$ast$stmt$ForStatement()))) {
         var3[141].callCurrent(this, (Object)ScriptBytecodeAdapter.createList(new Object[]{var3[142].callGetProperty(statement.get())}));
         var3[143].callCurrent(this, (Object)" : ");
      }

      if (var3[144].callGetPropertySafe(statement.get()) instanceof ListExpression) {
         var3[145].callSafe(var3[146].callGetPropertySafe(statement.get()), (Object)this);
      } else {
         var3[147].callSafe(var3[148].callGetPropertySafe(statement.get()), (Object)this);
      }

      var3[149].callCurrent(this, (Object)") {");
      var3[150].callCurrent(this);
      var3[151].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$ForStatement;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public ForStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (ForStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$ForStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10()) {
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
            var0[1] = "loopBlock";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$ForStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement = class$("org.codehaus.groovy.ast.stmt.ForStatement");
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitForLoop_closure10 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitForLoop_closure10");
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
      var3[152].callCurrent(this, (Object)"}");
      var3[153].callCurrent(this);
   }

   public void visitIfElse(IfStatement ifElse) {
      IfStatement ifElse = new Reference(ifElse);
      CallSite[] var3 = $getCallSiteArray();
      var3[154].callCurrent(this, (Object)"if (");
      var3[155].callSafe(var3[156].callGetPropertySafe(ifElse.get()), (Object)this);
      var3[157].callCurrent(this, (Object)") {");
      var3[158].callCurrent(this);
      var3[159].callCurrent(this, (Object)(new GeneratedClosure(this, this, ifElse) {
         private Reference<T> ifElse;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$IfStatement;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.ifElse = (Reference)ifElse;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.ifElse.get()), this.getThisObject());
         }

         public IfStatement getIfElse() {
            CallSite[] var1 = $getCallSiteArray();
            return (IfStatement)ScriptBytecodeAdapter.castToType(this.ifElse.get(), $get$$class$org$codehaus$groovy$ast$stmt$IfStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11()) {
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
            var0[1] = "ifBlock";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$IfStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement = class$("org.codehaus.groovy.ast.stmt.IfStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure11 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitIfElse_closure11");
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
      var3[160].callCurrent(this);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[161].callGetPropertySafe(ifElse.get())) && !(var3[162].callGetProperty(ifElse.get()) instanceof EmptyStatement) ? Boolean.TRUE : Boolean.FALSE)) {
         var3[163].callCurrent(this, (Object)"} else {");
         var3[164].callCurrent(this);
         var3[165].callCurrent(this, (Object)(new GeneratedClosure(this, this, ifElse) {
            private Reference<T> ifElse;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$stmt$IfStatement;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.ifElse = (Reference)ifElse;
            }

            public Object doCall(Object it) {
               CallSite[] var2 = $getCallSiteArray();
               return var2[0].callSafe(var2[1].callGetPropertySafe(this.ifElse.get()), this.getThisObject());
            }

            public IfStatement getIfElse() {
               CallSite[] var1 = $getCallSiteArray();
               return (IfStatement)ScriptBytecodeAdapter.castToType(this.ifElse.get(), $get$$class$org$codehaus$groovy$ast$stmt$IfStatement());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12()) {
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
               var0[1] = "elseBlock";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$stmt$IfStatement() {
               Class var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$stmt$IfStatement = class$("org.codehaus.groovy.ast.stmt.IfStatement");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12() {
               Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitIfElse_closure12 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitIfElse_closure12");
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
         var3[166].callCurrent(this);
      }

      var3[167].callCurrent(this, (Object)"}");
      var3[168].callCurrent(this);
   }

   public void visitExpressionStatement(ExpressionStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[169].call(var2[170].callGetProperty(statement), (Object)this);
   }

   public void visitReturnStatement(ReturnStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[171].callCurrent(this);
      var2[172].callCurrent(this, (Object)"return ");
      var2[173].call(var2[174].call(statement), (Object)this);
      var2[175].callCurrent(this);
   }

   public void visitSwitch(SwitchStatement statement) {
      SwitchStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[176].callCurrent(this, (Object)"switch (");
      var3[177].callSafe(var3[178].callGetPropertySafe(statement.get()), (Object)this);
      var3[179].callCurrent(this, (Object)") {");
      var3[180].callCurrent(this);
      var3[181].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$SwitchStatement;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31()) {
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
                  var0[0] = "visitCaseStatement";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31() {
                  Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13_closure31 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitSwitch_closure13_closure31");
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
            if (DefaultTypeTransformation.booleanUnbox(var2[2].callGetPropertySafe(this.statement.get()))) {
               var2[3].callCurrent(this, (Object)"default: ");
               var2[4].callCurrent(this);
               return var2[5].callSafe(var2[6].callGetPropertySafe(this.statement.get()), this.getThisObject());
            } else {
               return null;
            }
         }

         public SwitchStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (SwitchStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$SwitchStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13()) {
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
            var0[1] = "caseStatements";
            var0[2] = "defaultStatement";
            var0[3] = "print";
            var0[4] = "printLineBreak";
            var0[5] = "visit";
            var0[6] = "defaultStatement";
            var0[7] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[8];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSwitch_closure13 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitSwitch_closure13");
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
      var3[182].callCurrent(this, (Object)"}");
      var3[183].callCurrent(this);
   }

   public void visitCaseStatement(CaseStatement statement) {
      CaseStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[184].callCurrent(this, (Object)"case ");
      var3[185].callSafe(var3[186].callGetPropertySafe(statement.get()), (Object)this);
      var3[187].callCurrent(this, (Object)":");
      var3[188].callCurrent(this);
      var3[189].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$CaseStatement;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public CaseStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (CaseStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$CaseStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14()) {
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
            var0[1] = "code";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCaseStatement_closure14 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitCaseStatement_closure14");
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$CaseStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$CaseStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$CaseStatement = class$("org.codehaus.groovy.ast.stmt.CaseStatement");
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

   public void visitBreakStatement(BreakStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[190].callCurrent(this, (Object)"break");
      var2[191].callCurrent(this);
   }

   public void visitContinueStatement(ContinueStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[192].callCurrent(this, (Object)"continue");
      var2[193].callCurrent(this);
   }

   public void visitMethodCallExpression(MethodCallExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      Expression objectExp = (Expression)ScriptBytecodeAdapter.castToType(var2[194].call(expression), $get$$class$org$codehaus$groovy$ast$expr$Expression());
      if (objectExp instanceof VariableExpression) {
         var2[195].callCurrent(this, objectExp, Boolean.FALSE);
      } else {
         var2[196].call(objectExp, (Object)this);
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[197].callGetProperty(expression))) {
         var2[198].callCurrent(this, (Object)"*");
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[199].callGetProperty(expression))) {
         var2[200].callCurrent(this, (Object)"?");
      }

      var2[201].callCurrent(this, (Object)".");
      Expression method = (Expression)ScriptBytecodeAdapter.castToType(var2[202].call(expression), $get$$class$org$codehaus$groovy$ast$expr$Expression());
      if (method instanceof ConstantExpression) {
         var2[203].callCurrent(this, method, Boolean.TRUE);
      } else {
         var2[204].call(method, (Object)this);
      }

      var2[205].call(var2[206].call(expression), (Object)this);
   }

   public void visitStaticMethodCallExpression(StaticMethodCallExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[207].callCurrent(this, (Object)var2[208].call(var2[209].call(var2[210].callGetPropertySafe(var2[211].callGetPropertySafe(expression)), (Object)"."), var2[212].callGetPropertySafe(expression)));
      if (var2[213].callGetPropertySafe(expression) instanceof VariableExpression) {
         var2[214].callCurrent(this, (Object)"(");
         var2[215].callSafe(var2[216].callGetPropertySafe(expression), (Object)this);
         var2[217].callCurrent(this, (Object)")");
      } else {
         var2[218].callSafe(var2[219].callGetPropertySafe(expression), (Object)this);
      }

   }

   public void visitConstructorCallExpression(ConstructorCallExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var2[220].callSafe(expression))) {
         var2[221].callCurrent(this, (Object)"super");
      } else if (DefaultTypeTransformation.booleanUnbox(var2[222].callSafe(expression))) {
         var2[223].callCurrent(this, (Object)"this ");
      } else {
         var2[224].callCurrent(this, (Object)"new ");
         var2[225].callCurrent(this, (Object)var2[226].callGetPropertySafe(expression));
      }

      var2[227].callSafe(var2[228].callGetPropertySafe(expression), (Object)this);
   }

   public void visitBinaryExpression(BinaryExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[229].callSafe(var2[230].callGetPropertySafe(expression), (Object)this);
      var2[231].callCurrent(this, (Object)(new GStringImpl(new Object[]{var2[232].callGetProperty(var2[233].callGetProperty(expression))}, new String[]{" ", " "})));
      var2[234].call(var2[235].callGetProperty(expression), (Object)this);
      if (ScriptBytecodeAdapter.compareEqual(var2[236].callGetPropertySafe(var2[237].callGetPropertySafe(expression)), "[")) {
         var2[238].callCurrent(this, (Object)"]");
      }

   }

   public void visitPostfixExpression(PostfixExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[239].callCurrent(this, (Object)"(");
      var2[240].callSafe(var2[241].callGetPropertySafe(expression), (Object)this);
      var2[242].callCurrent(this, (Object)")");
      var2[243].callCurrent(this, (Object)var2[244].callGetPropertySafe(var2[245].callGetPropertySafe(expression)));
   }

   public void visitPrefixExpression(PrefixExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[246].callCurrent(this, (Object)var2[247].callGetPropertySafe(var2[248].callGetPropertySafe(expression)));
      var2[249].callCurrent(this, (Object)"(");
      var2[250].callSafe(var2[251].callGetPropertySafe(expression), (Object)this);
      var2[252].callCurrent(this, (Object)")");
   }

   public void visitClosureExpression(ClosureExpression expression) {
      ClosureExpression expression = new Reference(expression);
      CallSite[] var3 = $getCallSiteArray();
      var3[253].callCurrent(this, (Object)"{ ");
      if (DefaultTypeTransformation.booleanUnbox(var3[254].callGetPropertySafe(expression.get()))) {
         var3[255].callCurrent(this, (Object)var3[256].callGetPropertySafe(expression.get()));
         var3[257].callCurrent(this, (Object)" ->");
      }

      var3[258].callCurrent(this);
      var3[259].callCurrent(this, (Object)(new GeneratedClosure(this, this, expression) {
         private Reference<T> expression;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$ClosureExpression;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.expression = (Reference)expression;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.expression.get()), this.getThisObject());
         }

         public ClosureExpression getExpression() {
            CallSite[] var1 = $getCallSiteArray();
            return (ClosureExpression)ScriptBytecodeAdapter.castToType(this.expression.get(), $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15()) {
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
            var0[1] = "code";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureExpression_closure15 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClosureExpression_closure15");
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
      }));
      var3[260].callCurrent(this, (Object)"}");
   }

   public void visitTupleExpression(TupleExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[261].callCurrent(this, (Object)"(");
      var2[262].callCurrent(this, (Object)var2[263].callGetPropertySafe(expression));
      var2[264].callCurrent(this, (Object)")");
   }

   public void visitRangeExpression(RangeExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[265].callCurrent(this, (Object)"(");
      var2[266].callSafe(var2[267].callGetPropertySafe(expression), (Object)this);
      var2[268].callCurrent(this, (Object)"..");
      var2[269].callSafe(var2[270].callGetPropertySafe(expression), (Object)this);
      var2[271].callCurrent(this, (Object)")");
   }

   public void visitPropertyExpression(PropertyExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[272].callSafe(var2[273].callGetPropertySafe(expression), (Object)this);
      if (DefaultTypeTransformation.booleanUnbox(var2[274].callGetPropertySafe(expression))) {
         var2[275].callCurrent(this, (Object)"*");
      } else if (DefaultTypeTransformation.booleanUnbox(var2[276].callSafe(expression))) {
         var2[277].callCurrent(this, (Object)"?");
      }

      var2[278].callCurrent(this, (Object)".");
      if (var2[279].callGetPropertySafe(expression) instanceof ConstantExpression) {
         var2[280].callCurrent(this, var2[281].callGetPropertySafe(expression), Boolean.TRUE);
      } else {
         var2[282].callSafe(var2[283].callGetPropertySafe(expression), (Object)this);
      }

   }

   public void visitAttributeExpression(AttributeExpression attributeExpression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[284].callCurrent(this, (Object)attributeExpression);
   }

   public void visitFieldExpression(FieldExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[285].callCurrent(this, (Object)var2[286].callGetPropertySafe(var2[287].callGetPropertySafe(expression)));
   }

   public void visitConstantExpression(ConstantExpression expression, boolean unwrapQuotes) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var3[288].callGetProperty(expression) instanceof String && !DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(unwrapQuotes)) ? Boolean.TRUE : Boolean.FALSE)) {
         Object escaped = var3[289].call(var3[290].call((String)ScriptBytecodeAdapter.castToType(var3[291].callGetProperty(expression), $get$$class$java$lang$String()), "\n", "\\\\n"), "'", "\\\\'");
         var3[292].callCurrent(this, (Object)(new GStringImpl(new Object[]{escaped}, new String[]{"'", "'"})));
      } else {
         var3[293].callCurrent(this, (Object)var3[294].callGetProperty(expression));
      }

   }

   public void visitClassExpression(ClassExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[295].callCurrent(this, (Object)var2[296].callGetProperty(expression));
   }

   public void visitVariableExpression(VariableExpression expression, boolean spacePad) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(spacePad))) {
         var3[297].callCurrent(this, (Object)var3[298].call(var3[299].call(" ", (Object)var3[300].callGetProperty(expression)), (Object)" "));
      } else {
         var3[301].callCurrent(this, (Object)var3[302].callGetProperty(expression));
      }

   }

   public void visitDeclarationExpression(DeclarationExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      if (var2[303].callGetPropertySafe(expression) instanceof ArgumentListExpression) {
         var2[304].callCurrent(this, (Object)"def ");
         var2[305].callCurrent(this, var2[306].callGetPropertySafe(expression), Boolean.TRUE);
         var2[307].callCurrent(this, (Object)(new GStringImpl(new Object[]{var2[308].callGetProperty(var2[309].callGetProperty(expression))}, new String[]{" ", " "})));
         var2[310].call(var2[311].callGetProperty(expression), (Object)this);
         if (ScriptBytecodeAdapter.compareEqual(var2[312].callGetPropertySafe(var2[313].callGetPropertySafe(expression)), "[")) {
            var2[314].callCurrent(this, (Object)"]");
         }
      } else {
         var2[315].callCurrent(this, (Object)var2[316].callGetPropertySafe(var2[317].callGetPropertySafe(expression)));
         var2[318].callCurrent(this, (Object)expression);
      }

   }

   public void visitGStringExpression(GStringExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[319].callCurrent(this, (Object)var2[320].call(var2[321].call("\"", (Object)var2[322].callGetProperty(expression)), (Object)"\""));
   }

   public void visitSpreadExpression(SpreadExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[323].callCurrent(this, (Object)"*");
      var2[324].callSafe(var2[325].callGetPropertySafe(expression), (Object)this);
   }

   public void visitNotExpression(NotExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[326].callCurrent(this, (Object)"!(");
      var2[327].callSafe(var2[328].callGetPropertySafe(expression), (Object)this);
      var2[329].callCurrent(this, (Object)")");
   }

   public void visitUnaryMinusExpression(UnaryMinusExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[330].callCurrent(this, (Object)"-(");
      var2[331].callSafe(var2[332].callGetPropertySafe(expression), (Object)this);
      var2[333].callCurrent(this, (Object)")");
   }

   public void visitUnaryPlusExpression(UnaryPlusExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[334].callCurrent(this, (Object)"+(");
      var2[335].callSafe(var2[336].callGetPropertySafe(expression), (Object)this);
      var2[337].callCurrent(this, (Object)")");
   }

   public void visitCastExpression(CastExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[338].callCurrent(this, (Object)"((");
      var2[339].callSafe(var2[340].callGetPropertySafe(expression), (Object)this);
      var2[341].callCurrent(this, (Object)") as ");
      var2[342].callCurrent(this, (Object)var2[343].callGetPropertySafe(expression));
      var2[344].callCurrent(this, (Object)")");
   }

   public void visitType(ClassNode classNode) {
      CallSite[] var2 = $getCallSiteArray();
      Object name = var2[345].callGetProperty(classNode);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.findRegex(name, "^\\[+L")) && DefaultTypeTransformation.booleanUnbox(var2[346].call(name, (Object)";")) ? Boolean.TRUE : Boolean.FALSE)) {
         Integer numDimensions = (Integer)ScriptBytecodeAdapter.castToType(var2[347].call(name, (Object)"L"), $get$$class$java$lang$Integer());
         var2[348].callCurrent(this, (Object)var2[349].call(new GStringImpl(new Object[]{var2[350].call(var2[351].callGetProperty(classNode), (Object)ScriptBytecodeAdapter.createRange(var2[352].call(numDimensions, (Object)$const$0), $const$3, true))}, new String[]{"", ""}), (Object)var2[353].call("[]", (Object)numDimensions)));
      } else {
         var2[354].callCurrent(this, (Object)name);
      }

      var2[355].callCurrent(this, (Object)var2[356].callGetPropertySafe(classNode));
   }

   public void visitArgumentlistExpression(ArgumentListExpression expression, boolean showTypes) {
      Boolean showTypes = new Reference(DefaultTypeTransformation.box(showTypes));
      CallSite[] var4 = $getCallSiteArray();
      var4[357].callCurrent(this, (Object)"(");
      Integer count = new Reference((Integer)ScriptBytecodeAdapter.castToType(var4[358].callSafe(var4[359].callGetPropertySafe(expression)), $get$$class$java$lang$Integer()));
      var4[360].call(var4[361].callGetProperty(expression), (Object)(new GeneratedClosure(this, this, count, showTypes) {
         private Reference<T> count;
         private Reference<T> showTypes;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.count = (Reference)count;
            this.showTypes = (Reference)showTypes;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(this.showTypes.get())) {
               var3[0].callCurrent(this, (Object)var3[1].callGetProperty(itx.get()));
               var3[2].callCurrent(this, (Object)" ");
            }

            if (itx.get() instanceof VariableExpression) {
               var3[3].callCurrent(this, itx.get(), Boolean.FALSE);
            } else if (itx.get() instanceof ConstantExpression) {
               var3[4].callCurrent(this, itx.get(), Boolean.FALSE);
            } else {
               var3[5].call(itx.get(), this.getThisObject());
            }

            Object var4 = this.count.get();
            this.count.set(var3[6].call(this.count.get()));
            return DefaultTypeTransformation.booleanUnbox(this.count.get()) ? var3[7].callCurrent(this, (Object)", ") : null;
         }

         public Integer getCount() {
            CallSite[] var1 = $getCallSiteArray();
            return (Integer)ScriptBytecodeAdapter.castToType(this.count.get(), $get$$class$java$lang$Integer());
         }

         public boolean getShowTypes() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(this.showTypes.get(), $get$$class$java$lang$Boolean()));
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[8].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16()) {
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
            var0[0] = "visitType";
            var0[1] = "type";
            var0[2] = "print";
            var0[3] = "visitVariableExpression";
            var0[4] = "visitConstantExpression";
            var0[5] = "visit";
            var0[6] = "previous";
            var0[7] = "print";
            var0[8] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitArgumentlistExpression_closure16");
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
         private static Class $get$$class$java$lang$Boolean() {
            Class var10000 = $class$java$lang$Boolean;
            if (var10000 == null) {
               var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
      var4[362].callCurrent(this, (Object)")");
   }

   public void visitBytecodeExpression(BytecodeExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[363].callCurrent(this, (Object)"/*BytecodeExpression*/");
      var2[364].callCurrent(this);
   }

   public void visitMapExpression(MapExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[365].callCurrent(this, (Object)"[");
      var2[366].callCurrent(this, (Object)var2[367].callGetPropertySafe(expression));
      var2[368].callCurrent(this, (Object)"]");
   }

   public void visitMapEntryExpression(MapEntryExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      if (var2[369].callGetPropertySafe(expression) instanceof SpreadMapExpression) {
         var2[370].callCurrent(this, (Object)"*");
      } else {
         var2[371].callSafe(var2[372].callGetPropertySafe(expression), (Object)this);
      }

      var2[373].callCurrent(this, (Object)": ");
      var2[374].callSafe(var2[375].callGetPropertySafe(expression), (Object)this);
   }

   public void visitListExpression(ListExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[376].callCurrent(this, (Object)"[");
      var2[377].callCurrent(this, (Object)var2[378].callGetPropertySafe(expression));
      var2[379].callCurrent(this, (Object)"]");
   }

   public void visitTryCatchFinally(TryCatchStatement statement) {
      TryCatchStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[380].callCurrent(this, (Object)"try {");
      var3[381].callCurrent(this);
      var3[382].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public TryCatchStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (TryCatchStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17()) {
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
            var0[1] = "tryStatement";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement = class$("org.codehaus.groovy.ast.stmt.TryCatchStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure17 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitTryCatchFinally_closure17");
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
      var3[383].callCurrent(this);
      var3[384].callCurrent(this, (Object)"} ");
      var3[385].callCurrent(this);
      var3[386].callSafe(var3[387].callGetPropertySafe(statement.get()), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(CatchStatement catchStatement) {
            CatchStatement catchStatementx = new Reference(catchStatement);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].callCurrent(this, (Object)catchStatementx.get());
         }

         public Object call(CatchStatement catchStatement) {
            CatchStatement catchStatementx = new Reference(catchStatement);
            CallSite[] var3 = $getCallSiteArray();
            return var3[1].callCurrent(this, (Object)catchStatementx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18()) {
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
            var0[0] = "visitCatchStatement";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure18 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitTryCatchFinally_closure18");
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
      var3[388].callCurrent(this, (Object)"finally { ");
      var3[389].callCurrent(this);
      var3[390].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public TryCatchStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (TryCatchStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19()) {
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
            var0[1] = "finallyStatement";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$TryCatchStatement = class$("org.codehaus.groovy.ast.stmt.TryCatchStatement");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitTryCatchFinally_closure19 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitTryCatchFinally_closure19");
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
      var3[391].callCurrent(this, (Object)"} ");
      var3[392].callCurrent(this);
   }

   public void visitThrowStatement(ThrowStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[393].callCurrent(this, (Object)"throw ");
      var2[394].callSafe(var2[395].callGetPropertySafe(statement), (Object)this);
      var2[396].callCurrent(this);
   }

   public void visitSynchronizedStatement(SynchronizedStatement statement) {
      SynchronizedStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[397].callCurrent(this, (Object)"synchronized (");
      var3[398].callSafe(var3[399].callGetPropertySafe(statement.get()), (Object)this);
      var3[400].callCurrent(this, (Object)") {");
      var3[401].callCurrent(this);
      var3[402].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public SynchronizedStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (SynchronizedStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$SynchronizedStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20()) {
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
            var0[1] = "code";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$SynchronizedStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$SynchronizedStatement = class$("org.codehaus.groovy.ast.stmt.SynchronizedStatement");
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitSynchronizedStatement_closure20");
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
      var3[403].callCurrent(this, (Object)"}");
   }

   public void visitTernaryExpression(TernaryExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[404].callSafe(var2[405].callGetPropertySafe(expression), (Object)this);
      var2[406].callCurrent(this, (Object)" ? ");
      var2[407].callSafe(var2[408].callGetPropertySafe(expression), (Object)this);
      var2[409].callCurrent(this, (Object)" : ");
      var2[410].callSafe(var2[411].callGetPropertySafe(expression), (Object)this);
   }

   public void visitShortTernaryExpression(ElvisOperatorExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[412].callCurrent(this, (Object)expression);
   }

   public void visitBooleanExpression(BooleanExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[413].callSafe(var2[414].callGetPropertySafe(expression), (Object)this);
   }

   public void visitWhileLoop(WhileStatement statement) {
      WhileStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[415].callCurrent(this, (Object)"while (");
      var3[416].callSafe(var3[417].callGetPropertySafe(statement.get()), (Object)this);
      var3[418].callCurrent(this, (Object)") {");
      var3[419].callCurrent(this);
      var3[420].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$WhileStatement;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public WhileStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (WhileStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$WhileStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21()) {
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
            var0[1] = "loopBlock";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitWhileLoop_closure21 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitWhileLoop_closure21");
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$WhileStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$WhileStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$WhileStatement = class$("org.codehaus.groovy.ast.stmt.WhileStatement");
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
      var3[421].callCurrent(this);
      var3[422].callCurrent(this, (Object)"}");
      var3[423].callCurrent(this);
   }

   public void visitDoWhileLoop(DoWhileStatement statement) {
      DoWhileStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[424].callCurrent(this, (Object)"do {");
      var3[425].callCurrent(this);
      var3[426].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$DoWhileStatement;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetPropertySafe(this.statement.get()), this.getThisObject());
         }

         public DoWhileStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (DoWhileStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$DoWhileStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22()) {
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
            var0[1] = "loopBlock";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitDoWhileLoop_closure22 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitDoWhileLoop_closure22");
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
         private static Class $get$$class$org$codehaus$groovy$ast$stmt$DoWhileStatement() {
            Class var10000 = $class$org$codehaus$groovy$ast$stmt$DoWhileStatement;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$stmt$DoWhileStatement = class$("org.codehaus.groovy.ast.stmt.DoWhileStatement");
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
      var3[427].callCurrent(this, (Object)"} while (");
      var3[428].callSafe(var3[429].callGetPropertySafe(statement.get()), (Object)this);
      var3[430].callCurrent(this, (Object)")");
      var3[431].callCurrent(this);
   }

   public void visitCatchStatement(CatchStatement statement) {
      CatchStatement statement = new Reference(statement);
      CallSite[] var3 = $getCallSiteArray();
      var3[432].callCurrent(this, (Object)"catch (");
      var3[433].callCurrent(this, (Object)ScriptBytecodeAdapter.createList(new Object[]{var3[434].callGetProperty(statement.get())}));
      var3[435].callCurrent(this, (Object)") {");
      var3[436].callCurrent(this);
      var3[437].callCurrent(this, (Object)(new GeneratedClosure(this, this, statement) {
         private Reference<T> statement;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$stmt$CatchStatement;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.statement = (Reference)statement;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callSafe(var2[1].callGetProperty(this.statement.get()), this.getThisObject());
         }

         public CatchStatement getStatement() {
            CallSite[] var1 = $getCallSiteArray();
            return (CatchStatement)ScriptBytecodeAdapter.castToType(this.statement.get(), $get$$class$org$codehaus$groovy$ast$stmt$CatchStatement());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23()) {
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
            var0[1] = "code";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitCatchStatement_closure23 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitCatchStatement_closure23");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      var3[438].callCurrent(this, (Object)"} ");
      var3[439].callCurrent(this);
   }

   public void visitBitwiseNegationExpression(BitwiseNegationExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[440].callCurrent(this, (Object)"~(");
      var2[441].callSafe(var2[442].callGetPropertySafe(expression), (Object)this);
      var2[443].callCurrent(this, (Object)") ");
   }

   public void visitAssertStatement(AssertStatement statement) {
      CallSite[] var2 = $getCallSiteArray();
      var2[444].callCurrent(this, (Object)"assert ");
      var2[445].callSafe(var2[446].callGetPropertySafe(statement), (Object)this);
      var2[447].callCurrent(this, (Object)" : ");
      var2[448].callSafe(var2[449].callGetPropertySafe(statement), (Object)this);
   }

   public void visitClosureListExpression(ClosureListExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      Boolean first = new Reference(Boolean.TRUE);
      var2[450].callSafe(var2[451].callGetPropertySafe(expression), (Object)(new GeneratedClosure(this, this, first) {
         private Reference<T> first;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.first = (Reference)first;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
               var3[0].callCurrent(this, (Object)";");
            }

            this.first.set(Boolean.FALSE);
            return var3[1].call(itx.get(), this.getThisObject());
         }

         public Boolean getFirst() {
            CallSite[] var1 = $getCallSiteArray();
            return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24()) {
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
            var0[1] = "visit";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24(), var0);
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
         private static Class $get$$class$java$lang$Boolean() {
            Class var10000 = $class$java$lang$Boolean;
            if (var10000 == null) {
               var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitClosureListExpression_closure24 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitClosureListExpression_closure24");
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

   public void visitMethodPointerExpression(MethodPointerExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[452].callSafe(var2[453].callGetPropertySafe(expression), (Object)this);
      var2[454].callCurrent(this, (Object)".&");
      var2[455].callSafe(var2[456].callGetPropertySafe(expression), (Object)this);
   }

   public void visitArrayExpression(ArrayExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[457].callCurrent(this, (Object)"new ");
      var2[458].callCurrent(this, (Object)var2[459].callGetPropertySafe(expression));
      var2[460].callCurrent(this, (Object)"[");
      var2[461].callCurrent(this, (Object)var2[462].callGetPropertySafe(expression));
      var2[463].callCurrent(this, (Object)"]");
   }

   private void visitExpressionsAndCommaSeparate(List<? super Expression> expressions) {
      CallSite[] var2 = $getCallSiteArray();
      Boolean first = new Reference(Boolean.TRUE);
      var2[464].callSafe(expressions, (Object)(new GeneratedClosure(this, this, first) {
         private Reference<T> first;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.first = (Reference)first;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(this.first.get())) {
               var3[0].callCurrent(this, (Object)", ");
            }

            this.first.set(Boolean.FALSE);
            return var3[1].call(itx.get(), this.getThisObject());
         }

         public Boolean getFirst() {
            CallSite[] var1 = $getCallSiteArray();
            return (Boolean)ScriptBytecodeAdapter.castToType(this.first.get(), $get$$class$java$lang$Boolean());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25()) {
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
            var0[1] = "visit";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25() {
            Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25 = class$("groovy.inspect.swingui.AstNodeToScriptVisitor$_visitExpressionsAndCommaSeparate_closure25");
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
         private static Class $get$$class$java$lang$Boolean() {
            Class var10000 = $class$java$lang$Boolean;
            if (var10000 == null) {
               var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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

   public void visitSpreadMapExpression(SpreadMapExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[465].callCurrent(this, (Object)"*:");
      var2[466].callSafe(var2[467].callGetPropertySafe(expression), (Object)this);
   }

   /** @deprecated */
   @Deprecated
   public void visitRegexExpression(RegexExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[468].callCurrent(this, (Object)var2[469].call(var2[470].call("/", (Object)var2[471].callGetProperty(node)), (Object)"/"));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public void visitConstantExpression(ConstantExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[472].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(expression, $get$$class$org$codehaus$groovy$ast$expr$ConstantExpression()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.FALSE, Boolean.TYPE));
   }

   public void visitVariableExpression(VariableExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[473].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(expression, $get$$class$org$codehaus$groovy$ast$expr$VariableExpression()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE));
   }

   public void visitArgumentlistExpression(ArgumentListExpression expression) {
      CallSite[] var2 = $getCallSiteArray();
      var2[474].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(expression, $get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.FALSE, Boolean.TYPE));
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

   public Stack<String> getClassNameStack() {
      return this.classNameStack;
   }

   public void setClassNameStack(Stack<String> var1) {
      this.classNameStack = var1;
   }

   public String get_indent() {
      return this._indent;
   }

   public void set_indent(String var1) {
      this._indent = var1;
   }

   public boolean getReadyToIndent() {
      return this.readyToIndent;
   }

   public boolean isReadyToIndent() {
      return this.readyToIndent;
   }

   public void setReadyToIndent(boolean var1) {
      this.readyToIndent = var1;
   }

   public boolean getShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public boolean isShowScriptFreeForm() {
      return this.showScriptFreeForm;
   }

   public void setShowScriptFreeForm(boolean var1) {
      this.showScriptFreeForm = var1;
   }

   public boolean getShowScriptClass() {
      return this.showScriptClass;
   }

   public boolean isShowScriptClass() {
      return this.showScriptClass;
   }

   public void setShowScriptClass(boolean var1) {
      this.showScriptClass = var1;
   }

   public boolean getScriptHasBeenVisited() {
      return this.scriptHasBeenVisited;
   }

   public boolean isScriptHasBeenVisited() {
      return this.scriptHasBeenVisited;
   }

   public void setScriptHasBeenVisited(boolean var1) {
      this.scriptHasBeenVisited = var1;
   }

   // $FF: synthetic method
   public void this$3$visitGenerics(GenericsType[] var1) {
      this.visitGenerics(var1);
   }

   // $FF: synthetic method
   public String this$3$visitParameters(Object var1) {
      return this.visitParameters(var1);
   }

   // $FF: synthetic method
   public Object this$3$visitModifiers(int var1) {
      return this.visitModifiers(var1);
   }

   // $FF: synthetic method
   public void this$3$visitExpressionsAndCommaSeparate(List var1) {
      this.visitExpressionsAndCommaSeparate(var1);
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
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public boolean super$2$needSortedInput() {
      return super.needSortedInput();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "visit";
      var0[2] = "getStatementBlock";
      var0[3] = "getAST";
      var0[4] = "isScript";
      var0[5] = "visitClass";
      var0[6] = "toString";
      var0[7] = "print";
      var0[8] = "startsWith";
      var0[9] = "getAt";
      var0[10] = "endsWith";
      var0[11] = "toString";
      var0[12] = "startsWith";
      var0[13] = "getAt";
      var0[14] = "print";
      var0[15] = "<$constructor$>";
      var0[16] = "plus";
      var0[17] = "call";
      var0[18] = "endsWith";
      var0[19] = "toString";
      var0[20] = "print";
      var0[21] = "endsWith";
      var0[22] = "toString";
      var0[23] = "endsWith";
      var0[24] = "toString";
      var0[25] = "print";
      var0[26] = "print";
      var0[27] = "print";
      var0[28] = "push";
      var0[29] = "name";
      var0[30] = "package";
      var0[31] = "endsWith";
      var0[32] = "text";
      var0[33] = "package";
      var0[34] = "print";
      var0[35] = "getAt";
      var0[36] = "text";
      var0[37] = "package";
      var0[38] = "print";
      var0[39] = "text";
      var0[40] = "package";
      var0[41] = "printDoubleBreak";
      var0[42] = "visitModifiers";
      var0[43] = "modifiers";
      var0[44] = "print";
      var0[45] = "name";
      var0[46] = "visitGenerics";
      var0[47] = "genericsTypes";
      var0[48] = "each";
      var0[49] = "interfaces";
      var0[50] = "print";
      var0[51] = "visitType";
      var0[52] = "superClass";
      var0[53] = "print";
      var0[54] = "printDoubleBreak";
      var0[55] = "indented";
      var0[56] = "print";
      var0[57] = "printLineBreak";
      var0[58] = "pop";
      var0[59] = "print";
      var0[60] = "each";
      var0[61] = "print";
      var0[62] = "visitMethod";
      var0[63] = "each";
      var0[64] = "visitModifiers";
      var0[65] = "modifiers";
      var0[66] = "name";
      var0[67] = "print";
      var0[68] = "peek";
      var0[69] = "visitParameters";
      var0[70] = "parameters";
      var0[71] = "print";
      var0[72] = "printLineBreak";
      var0[73] = "name";
      var0[74] = "print";
      var0[75] = "printLineBreak";
      var0[76] = "visitType";
      var0[77] = "returnType";
      var0[78] = "print";
      var0[79] = "name";
      var0[80] = "visitParameters";
      var0[81] = "parameters";
      var0[82] = "print";
      var0[83] = "exceptions";
      var0[84] = "print";
      var0[85] = "each";
      var0[86] = "exceptions";
      var0[87] = "print";
      var0[88] = "printLineBreak";
      var0[89] = "indented";
      var0[90] = "printLineBreak";
      var0[91] = "print";
      var0[92] = "printDoubleBreak";
      var0[93] = "isAbstract";
      var0[94] = "print";
      var0[95] = "isFinal";
      var0[96] = "print";
      var0[97] = "isInterface";
      var0[98] = "print";
      var0[99] = "isNative";
      var0[100] = "print";
      var0[101] = "isPrivate";
      var0[102] = "print";
      var0[103] = "isProtected";
      var0[104] = "print";
      var0[105] = "isPublic";
      var0[106] = "print";
      var0[107] = "isStatic";
      var0[108] = "print";
      var0[109] = "isSynchronized";
      var0[110] = "print";
      var0[111] = "isTransient";
      var0[112] = "print";
      var0[113] = "isVolatile";
      var0[114] = "print";
      var0[115] = "each";
      var0[116] = "annotations";
      var0[117] = "visitModifiers";
      var0[118] = "modifiers";
      var0[119] = "visitType";
      var0[120] = "type";
      var0[121] = "print";
      var0[122] = "name";
      var0[123] = "printLineBreak";
      var0[124] = "print";
      var0[125] = "plus";
      var0[126] = "name";
      var0[127] = "classNode";
      var0[128] = "members";
      var0[129] = "print";
      var0[130] = "each";
      var0[131] = "members";
      var0[132] = "print";
      var0[133] = "each";
      var0[134] = "statements";
      var0[135] = "endsWith";
      var0[136] = "toString";
      var0[137] = "printLineBreak";
      var0[138] = "print";
      var0[139] = "variable";
      var0[140] = "FOR_LOOP_DUMMY";
      var0[141] = "visitParameters";
      var0[142] = "variable";
      var0[143] = "print";
      var0[144] = "collectionExpression";
      var0[145] = "visit";
      var0[146] = "collectionExpression";
      var0[147] = "visit";
      var0[148] = "collectionExpression";
      var0[149] = "print";
      var0[150] = "printLineBreak";
      var0[151] = "indented";
      var0[152] = "print";
      var0[153] = "printLineBreak";
      var0[154] = "print";
      var0[155] = "visit";
      var0[156] = "booleanExpression";
      var0[157] = "print";
      var0[158] = "printLineBreak";
      var0[159] = "indented";
      var0[160] = "printLineBreak";
      var0[161] = "elseBlock";
      var0[162] = "elseBlock";
      var0[163] = "print";
      var0[164] = "printLineBreak";
      var0[165] = "indented";
      var0[166] = "printLineBreak";
      var0[167] = "print";
      var0[168] = "printLineBreak";
      var0[169] = "visit";
      var0[170] = "expression";
      var0[171] = "printLineBreak";
      var0[172] = "print";
      var0[173] = "visit";
      var0[174] = "getExpression";
      var0[175] = "printLineBreak";
      var0[176] = "print";
      var0[177] = "visit";
      var0[178] = "expression";
      var0[179] = "print";
      var0[180] = "printLineBreak";
      var0[181] = "indented";
      var0[182] = "print";
      var0[183] = "printLineBreak";
      var0[184] = "print";
      var0[185] = "visit";
      var0[186] = "expression";
      var0[187] = "print";
      var0[188] = "printLineBreak";
      var0[189] = "indented";
      var0[190] = "print";
      var0[191] = "printLineBreak";
      var0[192] = "print";
      var0[193] = "printLineBreak";
      var0[194] = "getObjectExpression";
      var0[195] = "visitVariableExpression";
      var0[196] = "visit";
      var0[197] = "spreadSafe";
      var0[198] = "print";
      var0[199] = "safe";
      var0[200] = "print";
      var0[201] = "print";
      var0[202] = "getMethod";
      var0[203] = "visitConstantExpression";
      var0[204] = "visit";
      var0[205] = "visit";
      var0[206] = "getArguments";
      var0[207] = "print";
      var0[208] = "plus";
      var0[209] = "plus";
      var0[210] = "name";
      var0[211] = "ownerType";
      var0[212] = "method";
      var0[213] = "arguments";
      var0[214] = "print";
      var0[215] = "visit";
      var0[216] = "arguments";
      var0[217] = "print";
      var0[218] = "visit";
      var0[219] = "arguments";
      var0[220] = "isSuperCall";
      var0[221] = "print";
      var0[222] = "isThisCall";
      var0[223] = "print";
      var0[224] = "print";
      var0[225] = "visitType";
      var0[226] = "type";
      var0[227] = "visit";
      var0[228] = "arguments";
      var0[229] = "visit";
      var0[230] = "leftExpression";
      var0[231] = "print";
      var0[232] = "text";
      var0[233] = "operation";
      var0[234] = "visit";
      var0[235] = "rightExpression";
      var0[236] = "text";
      var0[237] = "operation";
      var0[238] = "print";
      var0[239] = "print";
      var0[240] = "visit";
      var0[241] = "expression";
      var0[242] = "print";
      var0[243] = "print";
      var0[244] = "text";
      var0[245] = "operation";
      var0[246] = "print";
      var0[247] = "text";
      var0[248] = "operation";
      var0[249] = "print";
      var0[250] = "visit";
      var0[251] = "expression";
      var0[252] = "print";
      var0[253] = "print";
      var0[254] = "parameters";
      var0[255] = "visitParameters";
      var0[256] = "parameters";
      var0[257] = "print";
      var0[258] = "printLineBreak";
      var0[259] = "indented";
      var0[260] = "print";
      var0[261] = "print";
      var0[262] = "visitExpressionsAndCommaSeparate";
      var0[263] = "expressions";
      var0[264] = "print";
      var0[265] = "print";
      var0[266] = "visit";
      var0[267] = "from";
      var0[268] = "print";
      var0[269] = "visit";
      var0[270] = "to";
      var0[271] = "print";
      var0[272] = "visit";
      var0[273] = "objectExpression";
      var0[274] = "spreadSafe";
      var0[275] = "print";
      var0[276] = "isSafe";
      var0[277] = "print";
      var0[278] = "print";
      var0[279] = "property";
      var0[280] = "visitConstantExpression";
      var0[281] = "property";
      var0[282] = "visit";
      var0[283] = "property";
      var0[284] = "visitPropertyExpression";
      var0[285] = "print";
      var0[286] = "name";
      var0[287] = "field";
      var0[288] = "value";
      var0[289] = "replaceAll";
      var0[290] = "replaceAll";
      var0[291] = "value";
      var0[292] = "print";
      var0[293] = "print";
      var0[294] = "value";
      var0[295] = "print";
      var0[296] = "text";
      var0[297] = "print";
      var0[298] = "plus";
      var0[299] = "plus";
      var0[300] = "name";
      var0[301] = "print";
      var0[302] = "name";
      var0[303] = "leftExpression";
      var0[304] = "print";
      var0[305] = "visitArgumentlistExpression";
      var0[306] = "leftExpression";
      var0[307] = "print";
      var0[308] = "text";
      var0[309] = "operation";
      var0[310] = "visit";
      var0[311] = "rightExpression";
      var0[312] = "text";
      var0[313] = "operation";
      var0[314] = "print";
      var0[315] = "visitType";
      var0[316] = "type";
      var0[317] = "leftExpression";
      var0[318] = "visitBinaryExpression";
      var0[319] = "print";
      var0[320] = "plus";
      var0[321] = "plus";
      var0[322] = "text";
      var0[323] = "print";
      var0[324] = "visit";
      var0[325] = "expression";
      var0[326] = "print";
      var0[327] = "visit";
      var0[328] = "expression";
      var0[329] = "print";
      var0[330] = "print";
      var0[331] = "visit";
      var0[332] = "expression";
      var0[333] = "print";
      var0[334] = "print";
      var0[335] = "visit";
      var0[336] = "expression";
      var0[337] = "print";
      var0[338] = "print";
      var0[339] = "visit";
      var0[340] = "expression";
      var0[341] = "print";
      var0[342] = "visitType";
      var0[343] = "type";
      var0[344] = "print";
      var0[345] = "name";
      var0[346] = "endsWith";
      var0[347] = "indexOf";
      var0[348] = "print";
      var0[349] = "plus";
      var0[350] = "getAt";
      var0[351] = "name";
      var0[352] = "plus";
      var0[353] = "multiply";
      var0[354] = "print";
      var0[355] = "visitGenerics";
      var0[356] = "genericsTypes";
      var0[357] = "print";
      var0[358] = "size";
      var0[359] = "expressions";
      var0[360] = "each";
      var0[361] = "expressions";
      var0[362] = "print";
      var0[363] = "print";
      var0[364] = "printLineBreak";
      var0[365] = "print";
      var0[366] = "visitExpressionsAndCommaSeparate";
      var0[367] = "mapEntryExpressions";
      var0[368] = "print";
      var0[369] = "keyExpression";
      var0[370] = "print";
      var0[371] = "visit";
      var0[372] = "keyExpression";
      var0[373] = "print";
      var0[374] = "visit";
      var0[375] = "valueExpression";
      var0[376] = "print";
      var0[377] = "visitExpressionsAndCommaSeparate";
      var0[378] = "expressions";
      var0[379] = "print";
      var0[380] = "print";
      var0[381] = "printLineBreak";
      var0[382] = "indented";
      var0[383] = "printLineBreak";
      var0[384] = "print";
      var0[385] = "printLineBreak";
      var0[386] = "each";
      var0[387] = "catchStatements";
      var0[388] = "print";
      var0[389] = "printLineBreak";
      var0[390] = "indented";
      var0[391] = "print";
      var0[392] = "printLineBreak";
      var0[393] = "print";
      var0[394] = "visit";
      var0[395] = "expression";
      var0[396] = "printLineBreak";
      var0[397] = "print";
      var0[398] = "visit";
      var0[399] = "expression";
      var0[400] = "print";
      var0[401] = "printLineBreak";
      var0[402] = "indented";
      var0[403] = "print";
      var0[404] = "visit";
      var0[405] = "booleanExpression";
      var0[406] = "print";
      var0[407] = "visit";
      var0[408] = "trueExpression";
      var0[409] = "print";
      var0[410] = "visit";
      var0[411] = "falseExpression";
      var0[412] = "visitTernaryExpression";
      var0[413] = "visit";
      var0[414] = "expression";
      var0[415] = "print";
      var0[416] = "visit";
      var0[417] = "booleanExpression";
      var0[418] = "print";
      var0[419] = "printLineBreak";
      var0[420] = "indented";
      var0[421] = "printLineBreak";
      var0[422] = "print";
      var0[423] = "printLineBreak";
      var0[424] = "print";
      var0[425] = "printLineBreak";
      var0[426] = "indented";
      var0[427] = "print";
      var0[428] = "visit";
      var0[429] = "booleanExpression";
      var0[430] = "print";
      var0[431] = "printLineBreak";
      var0[432] = "print";
      var0[433] = "visitParameters";
      var0[434] = "variable";
      var0[435] = "print";
      var0[436] = "printLineBreak";
      var0[437] = "indented";
      var0[438] = "print";
      var0[439] = "printLineBreak";
      var0[440] = "print";
      var0[441] = "visit";
      var0[442] = "expression";
      var0[443] = "print";
      var0[444] = "print";
      var0[445] = "visit";
      var0[446] = "booleanExpression";
      var0[447] = "print";
      var0[448] = "visit";
      var0[449] = "messageExpression";
      var0[450] = "each";
      var0[451] = "expressions";
      var0[452] = "visit";
      var0[453] = "expression";
      var0[454] = "print";
      var0[455] = "visit";
      var0[456] = "methodName";
      var0[457] = "print";
      var0[458] = "visitType";
      var0[459] = "elementType";
      var0[460] = "print";
      var0[461] = "visitExpressionsAndCommaSeparate";
      var0[462] = "sizeExpression";
      var0[463] = "print";
      var0[464] = "each";
      var0[465] = "print";
      var0[466] = "visit";
      var0[467] = "expression";
      var0[468] = "print";
      var0[469] = "plus";
      var0[470] = "plus";
      var0[471] = "text";
      var0[472] = "visitConstantExpression";
      var0[473] = "visitVariableExpression";
      var0[474] = "visitArgumentlistExpression";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[475];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor(), var0);
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
   private static Class $get$$class$java$util$Stack() {
      Class var10000 = $class$java$util$Stack;
      if (var10000 == null) {
         var10000 = $class$java$util$Stack = class$("java.util.Stack");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$UnsupportedOperationException() {
      Class var10000 = $class$java$lang$UnsupportedOperationException;
      if (var10000 == null) {
         var10000 = $class$java$lang$UnsupportedOperationException = class$("java.lang.UnsupportedOperationException");
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
   private static Class $get$$class$java$lang$reflect$Modifier() {
      Class var10000 = $class$java$lang$reflect$Modifier;
      if (var10000 == null) {
         var10000 = $class$java$lang$reflect$Modifier = class$("java.lang.reflect.Modifier");
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
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$ForStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$ForStatement = class$("org.codehaus.groovy.ast.stmt.ForStatement");
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
   private static Class $get$$class$java$io$Writer() {
      Class var10000 = $class$java$io$Writer;
      if (var10000 == null) {
         var10000 = $class$java$io$Writer = class$("java.io.Writer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor() {
      Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor = class$("groovy.inspect.swingui.AstNodeToScriptVisitor");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$Expression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$Expression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$Expression = class$("org.codehaus.groovy.ast.expr.Expression");
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
