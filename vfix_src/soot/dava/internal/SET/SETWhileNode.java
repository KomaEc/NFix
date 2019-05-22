package soot.dava.internal.SET;

import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;

public class SETWhileNode extends SETCycleNode {
   public SETWhileNode(AugmentedStmt characterizingStmt, IterableSet body) {
      super(characterizingStmt, body);
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
      return new ASTWhileNode(this.get_Label(), (ConditionExpr)((IfStmt)this.get_CharacterizingStmt().get_Stmt()).getCondition(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))));
   }

   public AugmentedStmt get_EntryStmt() {
      return this.get_CharacterizingStmt();
   }
}
