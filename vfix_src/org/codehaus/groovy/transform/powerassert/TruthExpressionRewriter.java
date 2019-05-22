package org.codehaus.groovy.transform.powerassert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
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
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

public class TruthExpressionRewriter implements GroovyCodeVisitor {
   private final SourceText sourceText;
   private final AssertionRewriter assertionRewriter;
   private Expression result;

   private TruthExpressionRewriter(SourceText sourceText, AssertionRewriter assertionRewriter) {
      this.sourceText = sourceText;
      this.assertionRewriter = assertionRewriter;
   }

   public static Expression rewrite(Expression truthExpr, SourceText sourceText, AssertionRewriter assertionRewriter) {
      return (new TruthExpressionRewriter(sourceText, assertionRewriter)).rewrite(truthExpr);
   }

   public void visitMethodCallExpression(MethodCallExpression expr) {
      MethodCallExpression conversion = new MethodCallExpression(expr.isImplicitThis() ? expr.getObjectExpression() : this.rewrite(expr.getObjectExpression()), this.rewrite(expr.getMethod()), this.rewrite(expr.getArguments()));
      conversion.setSafe(expr.isSafe());
      conversion.setSpreadSafe(expr.isSpreadSafe());
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion, (ASTNode)expr.getMethod());
   }

   public void visitStaticMethodCallExpression(StaticMethodCallExpression expr) {
      StaticMethodCallExpression conversion = new StaticMethodCallExpression(expr.getOwnerType(), expr.getMethod(), this.rewrite(expr.getArguments()));
      conversion.setSourcePosition(expr);
      conversion.setMetaMethod(expr.getMetaMethod());
      this.result = this.record(conversion);
   }

   public void visitBytecodeExpression(BytecodeExpression expr) {
      unsupported();
   }

   public void visitArgumentlistExpression(ArgumentListExpression expr) {
      ArgumentListExpression conversion = new ArgumentListExpression(this.rewriteAll(expr.getExpressions()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitPropertyExpression(PropertyExpression expr) {
      PropertyExpression conversion = new PropertyExpression(expr.isImplicitThis() ? expr.getObjectExpression() : this.rewrite(expr.getObjectExpression()), expr.getProperty(), expr.isSafe());
      conversion.setSourcePosition(expr);
      conversion.setSpreadSafe(expr.isSpreadSafe());
      conversion.setStatic(expr.isStatic());
      conversion.setImplicitThis(expr.isImplicitThis());
      this.result = this.record(conversion, (ASTNode)expr.getProperty());
   }

   public void visitAttributeExpression(AttributeExpression expr) {
      AttributeExpression conversion = new AttributeExpression(expr.isImplicitThis() ? expr.getObjectExpression() : this.rewrite(expr.getObjectExpression()), expr.getProperty(), expr.isSafe());
      conversion.setSourcePosition(expr);
      conversion.setSpreadSafe(expr.isSpreadSafe());
      conversion.setStatic(expr.isStatic());
      conversion.setImplicitThis(expr.isImplicitThis());
      this.result = this.record(conversion, (ASTNode)expr.getProperty());
   }

   public void visitFieldExpression(FieldExpression expr) {
      this.result = this.record(expr);
   }

   public void visitMethodPointerExpression(MethodPointerExpression expr) {
      MethodPointerExpression conversion = new MethodPointerExpression(this.rewrite(expr.getExpression()), this.rewrite(expr.getMethodName()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitVariableExpression(VariableExpression expr) {
      this.result = this.record(expr);
   }

   public void visitDeclarationExpression(DeclarationExpression expr) {
      unsupported();
   }

   /** @deprecated */
   @Deprecated
   public void visitRegexExpression(RegexExpression expr) {
      unsupported();
   }

   public void visitBinaryExpression(BinaryExpression expr) {
      Expression left = expr.getLeftExpression();
      Expression right = expr.getRightExpression();
      Token op = expr.getOperation();
      BinaryExpression conversion = new BinaryExpression(Types.ofType(op.getType(), 1100) ? left : this.rewrite(left), op, this.rewrite(right));
      conversion.setSourcePosition(expr);
      this.result = Types.ofType(op.getType(), 1500) ? this.record(conversion, this.sourceText.getNormalizedColumn(op.getText(), right.getLineNumber(), right.getColumnNumber())) : this.record(conversion, (Token)op);
   }

   public void visitConstantExpression(ConstantExpression expr) {
      this.result = expr;
   }

   public void visitClassExpression(ClassExpression expr) {
      this.result = expr;
   }

   public void visitUnaryMinusExpression(UnaryMinusExpression expr) {
      UnaryMinusExpression conversion = new UnaryMinusExpression(this.rewrite(expr.getExpression()));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitUnaryPlusExpression(UnaryPlusExpression expr) {
      UnaryPlusExpression conversion = new UnaryPlusExpression(this.rewrite(expr.getExpression()));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitBitwiseNegationExpression(BitwiseNegationExpression expr) {
      BitwiseNegationExpression conversion = new BitwiseNegationExpression(this.rewrite(expr.getExpression()));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitCastExpression(CastExpression expr) {
      CastExpression conversion = new CastExpression(expr.getType(), this.rewrite(expr.getExpression()), expr.isIgnoringAutoboxing());
      conversion.setSourcePosition(expr);
      conversion.setCoerce(expr.isCoerce());
      this.result = conversion;
   }

   public void visitClosureListExpression(ClosureListExpression expr) {
      this.result = expr;
   }

   public void visitNotExpression(NotExpression expr) {
      NotExpression conversion = new NotExpression(this.rewrite(expr.getExpression()));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitListExpression(ListExpression expr) {
      ListExpression conversion = new ListExpression(this.rewriteAll(expr.getExpressions()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitRangeExpression(RangeExpression expr) {
      RangeExpression conversion = new RangeExpression(this.rewrite(expr.getFrom()), this.rewrite(expr.getTo()), expr.isInclusive());
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitMapExpression(MapExpression expr) {
      MapExpression conversion = new MapExpression(this.rewriteAllCompatibly(expr.getMapEntryExpressions()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitMapEntryExpression(MapEntryExpression expr) {
      MapEntryExpression conversion = new MapEntryExpression(this.rewrite(expr.getKeyExpression()), this.rewrite(expr.getValueExpression()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitConstructorCallExpression(ConstructorCallExpression expr) {
      ConstructorCallExpression conversion = new ConstructorCallExpression(expr.getType(), this.rewrite(expr.getArguments()));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitGStringExpression(GStringExpression expr) {
      GStringExpression conversion = new GStringExpression(expr.getText(), expr.getStrings(), this.rewriteAll(expr.getValues()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitArrayExpression(ArrayExpression expr) {
      ArrayExpression conversion = new ArrayExpression(expr.getElementType(), this.rewriteAll(expr.getExpressions()), this.rewriteAll(expr.getSizeExpression()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitSpreadExpression(SpreadExpression expr) {
      SpreadExpression conversion = new SpreadExpression(this.rewrite(expr.getExpression()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitSpreadMapExpression(SpreadMapExpression expr) {
      this.result = expr;
   }

   public void visitTernaryExpression(TernaryExpression expr) {
      TernaryExpression conversion = new TernaryExpression(new BooleanExpression(this.rewrite(expr.getBooleanExpression().getExpression())), this.rewrite(expr.getTrueExpression()), this.rewrite(expr.getFalseExpression()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitShortTernaryExpression(ElvisOperatorExpression expr) {
      ElvisOperatorExpression conversion = new ElvisOperatorExpression(this.rewrite(expr.getBooleanExpression().getExpression()), this.rewrite(expr.getFalseExpression()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   public void visitPrefixExpression(PrefixExpression expr) {
      PrefixExpression conversion = new PrefixExpression(expr.getOperation(), this.unrecord(this.rewrite(expr.getExpression())));
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitPostfixExpression(PostfixExpression expr) {
      PostfixExpression conversion = new PostfixExpression(this.unrecord(this.rewrite(expr.getExpression())), expr.getOperation());
      conversion.setSourcePosition(expr);
      this.result = this.record(conversion);
   }

   public void visitBooleanExpression(BooleanExpression expr) {
      this.result = this.rewrite(expr.getExpression());
   }

   public void visitClosureExpression(ClosureExpression expr) {
      expr.getCode().visit(this.assertionRewriter);
      this.result = expr;
   }

   public void visitTupleExpression(TupleExpression expr) {
      TupleExpression conversion = new TupleExpression(this.rewriteAll(expr.getExpressions()));
      conversion.setSourcePosition(expr);
      this.result = conversion;
   }

   private Expression record(Expression value, int column) {
      return new MethodCallExpression(AssertionRewriter.recorderVariable, "record", new ArgumentListExpression(value, new ConstantExpression(column)));
   }

   private Expression record(Expression value) {
      return this.record(value, this.sourceText.getNormalizedColumn(value.getLineNumber(), value.getColumnNumber()));
   }

   private Expression record(Expression value, ASTNode node) {
      return this.record(value, this.sourceText.getNormalizedColumn(node.getLineNumber(), node.getColumnNumber()));
   }

   private Expression record(Expression value, Token token) {
      return this.record(value, this.sourceText.getNormalizedColumn(token.getStartLine(), token.getStartColumn()));
   }

   private Expression unrecord(Expression expr) {
      if (!(expr instanceof MethodCallExpression)) {
         return expr;
      } else {
         MethodCallExpression methodExpr = (MethodCallExpression)expr;
         Expression targetExpr = methodExpr.getObjectExpression();
         if (!(targetExpr instanceof VariableExpression)) {
            return expr;
         } else {
            VariableExpression var = (VariableExpression)targetExpr;
            if (var != AssertionRewriter.recorderVariable) {
               return expr;
            } else {
               return !methodExpr.getMethodAsString().equals("record") ? expr : ((ArgumentListExpression)methodExpr.getArguments()).getExpression(0);
            }
         }
      }
   }

   private <T extends Expression> T rewriteCompatibly(T expr) {
      expr.visit(this);
      return this.result;
   }

   private Expression rewrite(Expression expr) {
      return this.rewriteCompatibly(expr);
   }

   private <T extends Expression> List<T> rewriteAllCompatibly(List<T> exprs) {
      List<T> result = new ArrayList(exprs.size());
      Iterator i$ = exprs.iterator();

      while(i$.hasNext()) {
         T expr = (Expression)i$.next();
         result.add(this.rewriteCompatibly(expr));
      }

      return result;
   }

   private List<Expression> rewriteAll(List<Expression> exprs) {
      return this.rewriteAllCompatibly(exprs);
   }

   private static void unsupported() {
      throw new UnsupportedOperationException();
   }

   public void visitBlockStatement(BlockStatement stat) {
      unsupported();
   }

   public void visitForLoop(ForStatement stat) {
      unsupported();
   }

   public void visitWhileLoop(WhileStatement stat) {
      unsupported();
   }

   public void visitDoWhileLoop(DoWhileStatement stat) {
      unsupported();
   }

   public void visitIfElse(IfStatement stat) {
      unsupported();
   }

   public void visitExpressionStatement(ExpressionStatement stat) {
      unsupported();
   }

   public void visitReturnStatement(ReturnStatement stat) {
      unsupported();
   }

   public void visitAssertStatement(AssertStatement stat) {
      unsupported();
   }

   public void visitTryCatchFinally(TryCatchStatement stat) {
      unsupported();
   }

   public void visitSwitch(SwitchStatement stat) {
      unsupported();
   }

   public void visitCaseStatement(CaseStatement stat) {
      unsupported();
   }

   public void visitBreakStatement(BreakStatement stat) {
      unsupported();
   }

   public void visitContinueStatement(ContinueStatement stat) {
      unsupported();
   }

   public void visitThrowStatement(ThrowStatement stat) {
      unsupported();
   }

   public void visitSynchronizedStatement(SynchronizedStatement stat) {
      unsupported();
   }

   public void visitCatchStatement(CatchStatement stat) {
      unsupported();
   }
}
