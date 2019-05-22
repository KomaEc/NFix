package org.codehaus.groovy.transform.powerassert;

import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.Token;

public class AssertionRewriter extends StatementReplacingVisitorSupport {
   static final VariableExpression recorderVariable = new VariableExpression("$valueRecorder");
   private static final ClassNode verifierClass = ClassHelper.makeWithoutCaching(AssertionVerifier.class);
   private static final ClassNode recorderClass = ClassHelper.makeWithoutCaching(ValueRecorder.class);
   private final SourceUnit sourceUnit;
   private final Janitor janitor = new Janitor();
   private boolean assertFound;

   private AssertionRewriter(SourceUnit sourceUnit) {
      this.sourceUnit = sourceUnit;
   }

   public static void rewrite(SourceUnit sourceUnit) {
      (new AssertionRewriter(sourceUnit)).visitModule();
   }

   private void visitModule() {
      ModuleNode module = this.sourceUnit.getAST();

      try {
         List<ClassNode> classes = module.getClasses();
         Iterator i$ = classes.iterator();

         while(i$.hasNext()) {
            ClassNode clazz = (ClassNode)i$.next();
            this.visitClass(clazz);
         }
      } finally {
         this.janitor.cleanup();
      }

   }

   public void visitClass(ClassNode node) {
      this.visitAnnotations(node);
      node.visitContents(this);
      this.visitInstanceInitializer(node.getObjectInitializerStatements());
   }

   private void visitInstanceInitializer(List<Statement> stats) {
      boolean old = this.assertFound;
      this.assertFound = false;
      Iterator i$ = stats.iterator();

      while(i$.hasNext()) {
         Statement stat = (Statement)i$.next();
         stat.visit(this);
      }

      if (this.assertFound) {
         defineRecorderVariable(stats);
      }

      this.assertFound = old;
   }

   public void visitConstructor(ConstructorNode constructor) {
      boolean old = this.assertFound;
      this.assertFound = false;
      super.visitConstructor(constructor);
      if (this.assertFound) {
         defineRecorderVariable((BlockStatement)constructor.getCode());
      }

      this.assertFound = old;
   }

   public void visitMethod(MethodNode method) {
      boolean old = this.assertFound;
      this.assertFound = false;
      super.visitMethod(method);
      if (this.assertFound) {
         defineRecorderVariable((BlockStatement)method.getCode());
      }

      this.assertFound = old;
   }

   public void visitClosureExpression(ClosureExpression expr) {
      boolean old = this.assertFound;
      this.assertFound = false;
      super.visitClosureExpression(expr);
      if (this.assertFound) {
         defineRecorderVariable((BlockStatement)expr.getCode());
      }

      this.assertFound = old;
   }

   public void visitAssertStatement(AssertStatement stat) {
      super.visitAssertStatement(stat);
      this.rewriteAssertion(stat);
   }

   private void rewriteAssertion(AssertStatement stat) {
      if (stat.getMessageExpression() == ConstantExpression.NULL) {
         SourceText text;
         try {
            text = new SourceText(stat, this.sourceUnit, this.janitor);
         } catch (SourceTextNotAvailableException var6) {
            return;
         }

         this.assertFound = true;
         ExpressionStatement verifyCall = new ExpressionStatement(new MethodCallExpression(new ClassExpression(verifierClass), "verify", new ArgumentListExpression(TruthExpressionRewriter.rewrite(stat.getBooleanExpression(), text, this), new ConstantExpression(text.getNormalizedText()), recorderVariable)));
         BlockStatement tryBlock = new BlockStatement();
         tryBlock.addStatement(verifyCall);
         tryBlock.setSourcePosition(stat);
         TryCatchStatement tryCatchStat = new TryCatchStatement(tryBlock, new ExpressionStatement(new MethodCallExpression(recorderVariable, "clear", ArgumentListExpression.EMPTY_ARGUMENTS)));
         this.replaceVisitedStatementWith(tryCatchStat);
      }
   }

   private static void defineRecorderVariable(BlockStatement block) {
      defineRecorderVariable(block.getStatements());
   }

   private static void defineRecorderVariable(List<Statement> stats) {
      int insertPos = startsWithConstructorCall(stats) ? 1 : 0;
      stats.add(insertPos, new ExpressionStatement(new DeclarationExpression(recorderVariable, Token.newSymbol(100, -1, -1), new ConstructorCallExpression(recorderClass, ArgumentListExpression.EMPTY_ARGUMENTS))));
   }

   private static boolean startsWithConstructorCall(List<Statement> stats) {
      if (stats.size() == 0) {
         return false;
      } else {
         Statement stat = (Statement)stats.get(0);
         return stat instanceof ExpressionStatement && ((ExpressionStatement)stat).getExpression() instanceof ConstructorCallExpression;
      }
   }

   protected SourceUnit getSourceUnit() {
      throw new UnsupportedOperationException("getSourceUnit");
   }
}
