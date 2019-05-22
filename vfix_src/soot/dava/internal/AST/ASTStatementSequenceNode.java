package soot.dava.internal.AST;

import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.UnitPrinter;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.ASTAnalysis;
import soot.dava.toolkits.base.AST.ASTWalker;
import soot.dava.toolkits.base.AST.TryContentsFinder;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public class ASTStatementSequenceNode extends ASTNode {
   private List<AugmentedStmt> statementSequence;

   public ASTStatementSequenceNode(List<AugmentedStmt> statementSequence) {
      this.statementSequence = statementSequence;
   }

   public Object clone() {
      return new ASTStatementSequenceNode(this.statementSequence);
   }

   public void perform_Analysis(ASTAnalysis a) {
      if (a.getAnalysisDepth() > 0) {
         Iterator var2 = this.statementSequence.iterator();

         while(var2.hasNext()) {
            AugmentedStmt as = (AugmentedStmt)var2.next();
            ASTWalker.v().walk_stmt(a, as.get_Stmt());
         }
      }

      if (a instanceof TryContentsFinder) {
         TryContentsFinder.v().add_ExceptionSet(this, TryContentsFinder.v().remove_CurExceptionSet());
      }

   }

   public void toString(UnitPrinter up) {
      Iterator var2 = this.statementSequence.iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         Unit u = as.get_Stmt();
         up.startUnit(u);
         u.toString(up);
         up.literal(";");
         up.endUnit(u);
         up.newline();
      }

   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      Iterator var2 = this.statementSequence.iterator();

      while(var2.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var2.next();
         b.append(as.get_Stmt().toString());
         b.append(";");
         b.append("\n");
      }

      return b.toString();
   }

   public List<AugmentedStmt> getStatements() {
      return this.statementSequence;
   }

   public void apply(Analysis a) {
      a.caseASTStatementSequenceNode(this);
   }

   public void setStatements(List<AugmentedStmt> statementSequence) {
      this.statementSequence = statementSequence;
   }
}
