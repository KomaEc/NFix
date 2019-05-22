package soot.dava.toolkits.base.AST.traversals;

import java.util.List;
import soot.Local;
import soot.Value;
import soot.dava.internal.AST.ASTMethodNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;

public class InitializationDeclarationShortcut extends DepthFirstAdapter {
   AugmentedStmt ofInterest;
   boolean possible = false;
   Local definedLocal = null;
   int seenBefore = 0;

   public InitializationDeclarationShortcut(AugmentedStmt ofInterest) {
      this.ofInterest = ofInterest;
   }

   public InitializationDeclarationShortcut(boolean verbose, AugmentedStmt ofInterest) {
      super(verbose);
      this.ofInterest = ofInterest;
   }

   public boolean isShortcutPossible() {
      return this.possible;
   }

   public void inASTMethodNode(ASTMethodNode node) {
      Stmt s = this.ofInterest.get_Stmt();
      if (!(s instanceof DefinitionStmt)) {
         this.possible = false;
      } else {
         Value defined = ((DefinitionStmt)s).getLeftOp();
         if (!(defined instanceof Local)) {
            this.possible = false;
         } else {
            List declaredLocals = node.getDeclaredLocals();
            if (!declaredLocals.contains(defined)) {
               this.possible = false;
            } else {
               this.definedLocal = (Local)defined;
            }
         }
      }
   }

   public void inDefinitionStmt(DefinitionStmt s) {
      if (this.definedLocal != null) {
         Value defined = s.getLeftOp();
         if (defined instanceof Local) {
            if (defined.equals(this.definedLocal)) {
               if (s.equals(this.ofInterest.get_Stmt())) {
                  if (this.seenBefore == 0) {
                     this.possible = true;
                  } else {
                     this.possible = false;
                  }
               } else {
                  ++this.seenBefore;
               }
            }

         }
      }
   }
}
