package org.codehaus.groovy.ast;

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

public interface GroovyCodeVisitor {
   void visitBlockStatement(BlockStatement var1);

   void visitForLoop(ForStatement var1);

   void visitWhileLoop(WhileStatement var1);

   void visitDoWhileLoop(DoWhileStatement var1);

   void visitIfElse(IfStatement var1);

   void visitExpressionStatement(ExpressionStatement var1);

   void visitReturnStatement(ReturnStatement var1);

   void visitAssertStatement(AssertStatement var1);

   void visitTryCatchFinally(TryCatchStatement var1);

   void visitSwitch(SwitchStatement var1);

   void visitCaseStatement(CaseStatement var1);

   void visitBreakStatement(BreakStatement var1);

   void visitContinueStatement(ContinueStatement var1);

   void visitThrowStatement(ThrowStatement var1);

   void visitSynchronizedStatement(SynchronizedStatement var1);

   void visitCatchStatement(CatchStatement var1);

   void visitMethodCallExpression(MethodCallExpression var1);

   void visitStaticMethodCallExpression(StaticMethodCallExpression var1);

   void visitConstructorCallExpression(ConstructorCallExpression var1);

   void visitTernaryExpression(TernaryExpression var1);

   void visitShortTernaryExpression(ElvisOperatorExpression var1);

   void visitBinaryExpression(BinaryExpression var1);

   void visitPrefixExpression(PrefixExpression var1);

   void visitPostfixExpression(PostfixExpression var1);

   void visitBooleanExpression(BooleanExpression var1);

   void visitClosureExpression(ClosureExpression var1);

   void visitTupleExpression(TupleExpression var1);

   void visitMapExpression(MapExpression var1);

   void visitMapEntryExpression(MapEntryExpression var1);

   void visitListExpression(ListExpression var1);

   void visitRangeExpression(RangeExpression var1);

   void visitPropertyExpression(PropertyExpression var1);

   void visitAttributeExpression(AttributeExpression var1);

   void visitFieldExpression(FieldExpression var1);

   void visitMethodPointerExpression(MethodPointerExpression var1);

   void visitConstantExpression(ConstantExpression var1);

   void visitClassExpression(ClassExpression var1);

   void visitVariableExpression(VariableExpression var1);

   void visitDeclarationExpression(DeclarationExpression var1);

   /** @deprecated */
   @Deprecated
   void visitRegexExpression(RegexExpression var1);

   void visitGStringExpression(GStringExpression var1);

   void visitArrayExpression(ArrayExpression var1);

   void visitSpreadExpression(SpreadExpression var1);

   void visitSpreadMapExpression(SpreadMapExpression var1);

   void visitNotExpression(NotExpression var1);

   void visitUnaryMinusExpression(UnaryMinusExpression var1);

   void visitUnaryPlusExpression(UnaryPlusExpression var1);

   void visitBitwiseNegationExpression(BitwiseNegationExpression var1);

   void visitCastExpression(CastExpression var1);

   void visitArgumentlistExpression(ArgumentListExpression var1);

   void visitClosureListExpression(ClosureListExpression var1);

   void visitBytecodeExpression(BytecodeExpression var1);
}
