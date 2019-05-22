package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTDoWhileNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;

public class SETDoWhileNode extends SETCycleNode {
   private AugmentedStmt entryPoint;

   public SETDoWhileNode(AugmentedStmt characterizingStmt, AugmentedStmt entryPoint, IterableSet body) {
      super(characterizingStmt, body);
      this.entryPoint = entryPoint;
      IterableSet subBody = (IterableSet)body.clone();
      subBody.remove(characterizingStmt);
      this.add_SubBody(subBody);
   }

   public IterableSet get_NaturalExits() {
      IterableSet c = new IterableSet();
      c.add(this.get_CharacterizingStmt());
      return c;
   }

   public ASTNode emit_AST() {
      return new ASTDoWhileNode(this.get_Label(), (ConditionExpr)((IfStmt)this.get_CharacterizingStmt().get_Stmt()).getCondition(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))));
   }

   public AugmentedStmt get_EntryStmt() {
      return this.entryPoint;
   }
}
