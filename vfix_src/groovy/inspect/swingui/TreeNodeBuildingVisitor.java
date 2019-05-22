package groovy.inspect.swingui;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.DynamicVariable;
import org.codehaus.groovy.ast.Parameter;
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
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
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
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

private class TreeNodeBuildingVisitor extends CodeVisitorSupport implements GroovyObject {
   private DefaultMutableTreeNode currentNode;
   private Object adapter;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204251L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204251 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$VariableExpression;
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
   private static Class $class$org$codehaus$groovy$ast$expr$MethodCallExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$SwitchStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BooleanExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ArrayExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$Parameter;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$WhileStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$DoWhileStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClosureListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BinaryExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$ExpressionStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MethodPointerExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClosureExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$CatchStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$TupleExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ClassExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ConstructorCallExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$SpreadExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$TryCatchStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
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
   private static Class $class$org$codehaus$groovy$ast$expr$RegexExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$ListExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$AssertStatement;
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
   private static Class $class$org$codehaus$groovy$classgen$BytecodeExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$CastExpression;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$RangeExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$BlockStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$stmt$BreakStatement;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$MapExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$PropertyExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$NotExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$TernaryExpression;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$expr$UnaryMinusExpression;

   private TreeNodeBuildingVisitor(Object adapter) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      if (!DefaultTypeTransformation.booleanUnbox(adapter)) {
         throw (Throwable)var2[0].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: adapter");
      } else {
         this.adapter = adapter;
      }
   }

   private void addNode(Object node, Class expectedSubclass, Closure superMethod) {
      CallSite[] var4 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(var4[1].call(expectedSubclass), var4[2].call(var4[3].call(node)))) {
         if (ScriptBytecodeAdapter.compareEqual(this.currentNode, (Object)null)) {
            this.currentNode = (DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType((DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType(var4[4].call(this.adapter, node), $get$$class$javax$swing$tree$DefaultMutableTreeNode()), $get$$class$javax$swing$tree$DefaultMutableTreeNode());
            var4[5].call(superMethod, (Object)node);
         } else {
            DefaultMutableTreeNode temp = this.currentNode;
            this.currentNode = (DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType((DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType(var4[6].call(this.adapter, node), $get$$class$javax$swing$tree$DefaultMutableTreeNode()), $get$$class$javax$swing$tree$DefaultMutableTreeNode());
            var4[7].call(temp, (Object)this.currentNode);
            ScriptBytecodeAdapter.setProperty(temp, $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), this.currentNode, "parent");
            var4[8].call(superMethod, (Object)node);
            this.currentNode = (DefaultMutableTreeNode)ScriptBytecodeAdapter.castToType(temp, $get$$class$javax$swing$tree$DefaultMutableTreeNode());
         }
      } else {
         var4[9].call(superMethod, (Object)node);
      }

   }

   public void visitBlockStatement(BlockStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[10].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$BlockStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBlockStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBlockStatement_closure1 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBlockStatement_closure1");
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

   public void visitForLoop(ForStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[11].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$ForStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitForLoop", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitForLoop_closure2 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitForLoop_closure2");
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

   public void visitWhileLoop(WhileStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[12].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$WhileStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitWhileLoop", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitWhileLoop_closure3 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitWhileLoop_closure3");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitDoWhileLoop(DoWhileStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[13].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$DoWhileStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitDoWhileLoop", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitDoWhileLoop_closure4");
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

   public void visitIfElse(IfStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[14].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$IfStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitIfElse", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitIfElse_closure5 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitIfElse_closure5");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitExpressionStatement(ExpressionStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[15].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$ExpressionStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitExpressionStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitExpressionStatement_closure6 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitExpressionStatement_closure6");
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

   public void visitReturnStatement(ReturnStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[16].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$ReturnStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitReturnStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitReturnStatement_closure7 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitReturnStatement_closure7");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitAssertStatement(AssertStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[17].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$AssertStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitAssertStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAssertStatement_closure8 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitAssertStatement_closure8");
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

   public void visitTryCatchFinally(TryCatchStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[18].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$TryCatchStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitTryCatchFinally", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitTryCatchFinally_closure9");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   protected void visitEmptyStatement(EmptyStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[19].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$EmptyStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitEmptyStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitEmptyStatement_closure10 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitEmptyStatement_closure10");
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

   public void visitSwitch(SwitchStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[20].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$SwitchStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitSwitch", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSwitch_closure11 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitSwitch_closure11");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitCaseStatement(CaseStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[21].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$CaseStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitCaseStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCaseStatement_closure12 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitCaseStatement_closure12");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitBreakStatement(BreakStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[22].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$BreakStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBreakStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBreakStatement_closure13 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBreakStatement_closure13");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitContinueStatement(ContinueStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[23].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$ContinueStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitContinueStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitContinueStatement_closure14 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitContinueStatement_closure14");
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

   public void visitSynchronizedStatement(SynchronizedStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[24].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$SynchronizedStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitSynchronizedStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitSynchronizedStatement_closure15");
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

   public void visitThrowStatement(ThrowStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[25].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$ThrowStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitThrowStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitThrowStatement_closure16 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitThrowStatement_closure16");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitMethodCallExpression(MethodCallExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[26].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$MethodCallExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitMethodCallExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitMethodCallExpression_closure17");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitStaticMethodCallExpression(StaticMethodCallExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[27].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitStaticMethodCallExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitStaticMethodCallExpression_closure18");
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

   public void visitConstructorCallExpression(ConstructorCallExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[28].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ConstructorCallExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitConstructorCallExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitConstructorCallExpression_closure19");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitBinaryExpression(BinaryExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[29].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$BinaryExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBinaryExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBinaryExpression_closure20 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBinaryExpression_closure20");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitTernaryExpression(TernaryExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[30].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$TernaryExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitTernaryExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTernaryExpression_closure21 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitTernaryExpression_closure21");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitShortTernaryExpression(ElvisOperatorExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[31].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ElvisOperatorExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitShortTernaryExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitShortTernaryExpression_closure22");
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

   public void visitPostfixExpression(PostfixExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[32].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$PostfixExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitPostfixExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPostfixExpression_closure23 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitPostfixExpression_closure23");
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

   public void visitPrefixExpression(PrefixExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[33].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$PrefixExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitPrefixExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPrefixExpression_closure24 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitPrefixExpression_closure24");
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

   public void visitBooleanExpression(BooleanExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[34].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$BooleanExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBooleanExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBooleanExpression_closure25 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBooleanExpression_closure25");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitNotExpression(NotExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[35].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$NotExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitNotExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitNotExpression_closure26 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitNotExpression_closure26");
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

   public void visitClosureExpression(ClosureExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[36].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ClosureExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            var3[0].callSafe(var3[1].callGetProperty(itx.get()), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object parameter) {
                  Object parameterx = new Reference(parameter);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].callCurrent(this, (Object)parameterx.get());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56()) {
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
                  var0[0] = "visitParameter";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[1];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56() {
                  Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitClosureExpression_closure27_closure56");
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
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitClosureExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27()) {
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
            var0[1] = "parameters";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureExpression_closure27 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitClosureExpression_closure27");
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

   public void visitParameter(Parameter node) {
      Parameter node = new Reference(node);
      CallSite[] var3 = $getCallSiteArray();
      var3[37].callCurrent(this, node.get(), $get$$class$org$codehaus$groovy$ast$Parameter(), new GeneratedClosure(this, this, node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$Parameter;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox(var2[0].callGetProperty(this.node.get())) ? var2[1].callSafe(var2[2].callGetProperty(this.node.get()), this.getThisObject()) : null;
         }

         public Parameter getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return (Parameter)ScriptBytecodeAdapter.castToType(this.node.get(), $get$$class$org$codehaus$groovy$ast$Parameter());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28()) {
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
            var0[0] = "initialExpression";
            var0[1] = "visit";
            var0[2] = "initialExpression";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitParameter_closure28 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitParameter_closure28");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      });
   }

   public void visitTupleExpression(TupleExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[38].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$TupleExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitTupleExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitTupleExpression_closure29 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitTupleExpression_closure29");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitListExpression(ListExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[39].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ListExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitListExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListExpression_closure30 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitListExpression_closure30");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitArrayExpression(ArrayExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[40].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ArrayExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitArrayExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArrayExpression_closure31 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitArrayExpression_closure31");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitMapExpression(MapExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[41].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$MapExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitMapExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapExpression_closure32 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitMapExpression_closure32");
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

   public void visitMapEntryExpression(MapEntryExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[42].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitMapEntryExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitMapEntryExpression_closure33");
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

   public void visitRangeExpression(RangeExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[43].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$RangeExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitRangeExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRangeExpression_closure34 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitRangeExpression_closure34");
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

   public void visitSpreadExpression(SpreadExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[44].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$SpreadExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitSpreadExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadExpression_closure35 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitSpreadExpression_closure35");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitSpreadMapExpression(SpreadMapExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[45].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$SpreadMapExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitSpreadMapExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitSpreadMapExpression_closure36");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitMethodPointerExpression(MethodPointerExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[46].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$MethodPointerExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitMethodPointerExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitMethodPointerExpression_closure37");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitUnaryMinusExpression(UnaryMinusExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[47].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$UnaryMinusExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitUnaryMinusExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitUnaryMinusExpression_closure38");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitUnaryPlusExpression(UnaryPlusExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[48].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$UnaryPlusExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitUnaryPlusExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitUnaryPlusExpression_closure39");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitBitwiseNegationExpression(BitwiseNegationExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[49].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBitwiseNegationExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBitwiseNegationExpression_closure40");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitCastExpression(CastExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[50].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$CastExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitCastExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCastExpression_closure41 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitCastExpression_closure41");
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

   public void visitConstantExpression(ConstantExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[51].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ConstantExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitConstantExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitConstantExpression_closure42 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitConstantExpression_closure42");
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

   public void visitClassExpression(ClassExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[52].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ClassExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitClassExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClassExpression_closure43 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitClassExpression_closure43");
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

   public void visitVariableExpression(VariableExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[53].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$VariableExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$Parameter;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$DynamicVariable;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(VariableExpression it) {
            VariableExpression itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(var3[0].callGetProperty(itx.get()))) {
               if (var3[1].callGetProperty(itx.get()) instanceof Parameter) {
                  return var3[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Parameter)ScriptBytecodeAdapter.castToType(var3[3].callGetProperty(itx.get()), $get$$class$org$codehaus$groovy$ast$Parameter()), $get$$class$org$codehaus$groovy$ast$Parameter()));
               } else {
                  return var3[4].callGetProperty(itx.get()) instanceof DynamicVariable ? var3[5].callCurrent(this, var3[6].callGetProperty(itx.get()), $get$$class$org$codehaus$groovy$ast$DynamicVariable(), new GeneratedClosure(this, this.getThisObject()) {
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$java$lang$Object;
                     // $FF: synthetic field
                     private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57;

                     public {
                        CallSite[] var3 = $getCallSiteArray();
                     }

                     public Object doCall(Object it) {
                        Object itx = new Reference(it);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].callSafe(var3[1].callGetProperty(itx.get()), this.getThisObject());
                     }

                     public Object doCall() {
                        CallSite[] var1 = $getCallSiteArray();
                        return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57()) {
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
                        var0[1] = "initialExpression";
                        var0[2] = "doCall";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[3];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57(), var0);
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
                     private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57() {
                        Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57;
                        if (var10000 == null) {
                           var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitVariableExpression_closure44_closure57");
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
                  }) : null;
               }
            } else {
               return null;
            }
         }

         public Object call(VariableExpression it) {
            VariableExpression itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[7].callCurrent(this, (Object)itx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44()) {
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
            var0[0] = "accessedVariable";
            var0[1] = "accessedVariable";
            var0[2] = "visitParameter";
            var0[3] = "accessedVariable";
            var0[4] = "accessedVariable";
            var0[5] = "addNode";
            var0[6] = "accessedVariable";
            var0[7] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[8];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitVariableExpression_closure44 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitVariableExpression_closure44");
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
         private static Class $get$$class$org$codehaus$groovy$ast$DynamicVariable() {
            Class var10000 = $class$org$codehaus$groovy$ast$DynamicVariable;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$DynamicVariable = class$("org.codehaus.groovy.ast.DynamicVariable");
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

   public void visitDeclarationExpression(DeclarationExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[54].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$DeclarationExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitDeclarationExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitDeclarationExpression_closure45");
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

   public void visitPropertyExpression(PropertyExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[55].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$PropertyExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitPropertyExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitPropertyExpression_closure46 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitPropertyExpression_closure46");
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

   public void visitAttributeExpression(AttributeExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[56].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$AttributeExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitAttributeExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitAttributeExpression_closure47 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitAttributeExpression_closure47");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitFieldExpression(FieldExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[57].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$FieldExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitFieldExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitFieldExpression_closure48 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitFieldExpression_closure48");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   /** @deprecated */
   @Deprecated
   public void visitRegexExpression(RegexExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[58].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$RegexExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitRegexExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitRegexExpression_closure49 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitRegexExpression_closure49");
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

   public void visitGStringExpression(GStringExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[59].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$GStringExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitGStringExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitGStringExpression_closure50 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitGStringExpression_closure50");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitCatchStatement(CatchStatement node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[60].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$stmt$CatchStatement(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(var3[0].callGetProperty(itx.get()))) {
               var3[1].callCurrent(this, (Object)var3[2].callGetProperty(itx.get()));
            }

            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitCatchStatement", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51()) {
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
            var0[0] = "variable";
            var0[1] = "visitParameter";
            var0[2] = "variable";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitCatchStatement_closure51 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitCatchStatement_closure51");
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

   public void visitArgumentlistExpression(ArgumentListExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[61].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ArgumentListExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitArgumentlistExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitArgumentlistExpression_closure52");
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

   public void visitClosureListExpression(ClosureListExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[62].callCurrent(this, node, $get$$class$org$codehaus$groovy$ast$expr$ClosureListExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitClosureListExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitClosureListExpression_closure53 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitClosureListExpression_closure53");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   public void visitBytecodeExpression(BytecodeExpression node) {
      CallSite[] var2 = $getCallSiteArray();
      var2[63].callCurrent(this, node, $get$$class$org$codehaus$groovy$classgen$BytecodeExpression(), new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$CodeVisitorSupport;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$ast$CodeVisitorSupport(), (GroovyObject)this.getThisObject(), "visitBytecodeExpression", new Object[]{itx.get()});
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[0].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54()) {
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
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54(), var0);
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
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitBytecodeExpression_closure54");
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
         private static Class $get$$class$org$codehaus$groovy$ast$CodeVisitorSupport() {
            Class var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$CodeVisitorSupport = class$("org.codehaus.groovy.ast.CodeVisitorSupport");
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

   protected void visitListOfExpressions(List<? extends Expression> list) {
      CallSite[] var2 = $getCallSiteArray();
      var2[64].call(list, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression;
         // $FF: synthetic field
         private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Expression node) {
            Expression nodex = new Reference(node);
            CallSite[] var3 = $getCallSiteArray();
            return nodex.get() instanceof NamedArgumentListExpression ? var3[0].callCurrent(this, nodex.get(), $get$$class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression(), new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(itx.get(), this.getThisObject());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58()) {
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
                  return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58(), var0);
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
               private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58() {
                  Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58;
                  if (var10000 == null) {
                     var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitListOfExpressions_closure55_closure58");
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
            }) : var3[1].call(nodex.get(), this.getThisObject());
         }

         public Object call(Expression node) {
            Expression nodex = new Reference(node);
            CallSite[] var3 = $getCallSiteArray();
            return var3[2].callCurrent(this, (Object)nodex.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55()) {
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
            var0[0] = "addNode";
            var0[1] = "visit";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression() {
            Class var10000 = $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$expr$NamedArgumentListExpression = class$("org.codehaus.groovy.ast.expr.NamedArgumentListExpression");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55() {
            Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55;
            if (var10000 == null) {
               var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor$_visitListOfExpressions_closure55 = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor$_visitListOfExpressions_closure55");
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor()) {
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

   public DefaultMutableTreeNode getCurrentNode() {
      return this.currentNode;
   }

   public void setCurrentNode(DefaultMutableTreeNode var1) {
      this.currentNode = var1;
   }

   // $FF: synthetic method
   public void this$3$addNode(Object var1, Class var2, Closure var3) {
      this.addNode(var1, var2, var3);
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
      var0[1] = "getName";
      var0[2] = "getName";
      var0[3] = "getClass";
      var0[4] = "make";
      var0[5] = "call";
      var0[6] = "make";
      var0[7] = "add";
      var0[8] = "call";
      var0[9] = "call";
      var0[10] = "addNode";
      var0[11] = "addNode";
      var0[12] = "addNode";
      var0[13] = "addNode";
      var0[14] = "addNode";
      var0[15] = "addNode";
      var0[16] = "addNode";
      var0[17] = "addNode";
      var0[18] = "addNode";
      var0[19] = "addNode";
      var0[20] = "addNode";
      var0[21] = "addNode";
      var0[22] = "addNode";
      var0[23] = "addNode";
      var0[24] = "addNode";
      var0[25] = "addNode";
      var0[26] = "addNode";
      var0[27] = "addNode";
      var0[28] = "addNode";
      var0[29] = "addNode";
      var0[30] = "addNode";
      var0[31] = "addNode";
      var0[32] = "addNode";
      var0[33] = "addNode";
      var0[34] = "addNode";
      var0[35] = "addNode";
      var0[36] = "addNode";
      var0[37] = "addNode";
      var0[38] = "addNode";
      var0[39] = "addNode";
      var0[40] = "addNode";
      var0[41] = "addNode";
      var0[42] = "addNode";
      var0[43] = "addNode";
      var0[44] = "addNode";
      var0[45] = "addNode";
      var0[46] = "addNode";
      var0[47] = "addNode";
      var0[48] = "addNode";
      var0[49] = "addNode";
      var0[50] = "addNode";
      var0[51] = "addNode";
      var0[52] = "addNode";
      var0[53] = "addNode";
      var0[54] = "addNode";
      var0[55] = "addNode";
      var0[56] = "addNode";
      var0[57] = "addNode";
      var0[58] = "addNode";
      var0[59] = "addNode";
      var0[60] = "addNode";
      var0[61] = "addNode";
      var0[62] = "addNode";
      var0[63] = "addNode";
      var0[64] = "each";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[65];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MapEntryExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MapEntryExpression = class$("org.codehaus.groovy.ast.expr.MapEntryExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$VariableExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$VariableExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$VariableExpression = class$("org.codehaus.groovy.ast.expr.VariableExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MethodCallExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MethodCallExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MethodCallExpression = class$("org.codehaus.groovy.ast.expr.MethodCallExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$BooleanExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$BooleanExpression = class$("org.codehaus.groovy.ast.expr.BooleanExpression");
      }

      return var10000;
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$UnaryPlusExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$UnaryPlusExpression = class$("org.codehaus.groovy.ast.expr.UnaryPlusExpression");
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
   private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
      Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
      if (var10000 == null) {
         var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
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
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$DoWhileStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$DoWhileStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$DoWhileStatement = class$("org.codehaus.groovy.ast.stmt.DoWhileStatement");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$BitwiseNegationExpression = class$("org.codehaus.groovy.ast.expr.BitwiseNegationExpression");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$TreeNodeBuildingVisitor() {
      Class var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TreeNodeBuildingVisitor = class$("groovy.inspect.swingui.TreeNodeBuildingVisitor");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$SpreadExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$SpreadExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$SpreadExpression = class$("org.codehaus.groovy.ast.expr.SpreadExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$StaticMethodCallExpression = class$("org.codehaus.groovy.ast.expr.StaticMethodCallExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$RegexExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$RegexExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$RegexExpression = class$("org.codehaus.groovy.ast.expr.RegexExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$AssertStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$AssertStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$AssertStatement = class$("org.codehaus.groovy.ast.stmt.AssertStatement");
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
   private static Class $get$$class$org$codehaus$groovy$classgen$BytecodeExpression() {
      Class var10000 = $class$org$codehaus$groovy$classgen$BytecodeExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$classgen$BytecodeExpression = class$("org.codehaus.groovy.classgen.BytecodeExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$stmt$BlockStatement() {
      Class var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement = class$("org.codehaus.groovy.ast.stmt.BlockStatement");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$MapExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$MapExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$MapExpression = class$("org.codehaus.groovy.ast.expr.MapExpression");
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
   private static Class $get$$class$org$codehaus$groovy$ast$expr$TernaryExpression() {
      Class var10000 = $class$org$codehaus$groovy$ast$expr$TernaryExpression;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$expr$TernaryExpression = class$("org.codehaus.groovy.ast.expr.TernaryExpression");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
