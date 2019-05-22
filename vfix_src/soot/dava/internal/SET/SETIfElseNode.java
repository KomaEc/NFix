package soot.dava.internal.SET;

import java.util.List;
import soot.dava.internal.AST.ASTIfElseNode;
import soot.dava.internal.AST.ASTIfNode;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.util.IterableSet;

public class SETIfElseNode extends SETDagNode {
   private IterableSet ifBody;
   private IterableSet elseBody;

   public SETIfElseNode(AugmentedStmt characterizingStmt, IterableSet body, IterableSet ifBody, IterableSet elseBody) {
      super(characterizingStmt, body);
      this.ifBody = ifBody;
      this.elseBody = elseBody;
      this.add_SubBody(ifBody);
      this.add_SubBody(elseBody);
   }

   public IterableSet get_NaturalExits() {
      IterableSet c = new IterableSet();
      IterableSet ifChain = (IterableSet)this.body2childChain.get(this.ifBody);
      if (!ifChain.isEmpty()) {
         c.addAll(((SETNode)ifChain.getLast()).get_NaturalExits());
      }

      IterableSet elseChain = (IterableSet)this.body2childChain.get(this.elseBody);
      if (!elseChain.isEmpty()) {
         c.addAll(((SETNode)elseChain.getLast()).get_NaturalExits());
      }

      return c;
   }

   public ASTNode emit_AST() {
      List<Object> astBody0 = this.emit_ASTBody((IterableSet)this.body2childChain.get(this.ifBody));
      List<Object> astBody1 = this.emit_ASTBody((IterableSet)this.body2childChain.get(this.elseBody));
      ConditionExpr ce = (ConditionExpr)((IfStmt)this.get_CharacterizingStmt().get_Stmt()).getCondition();
      if (astBody0.isEmpty()) {
         List<Object> tbody = astBody0;
         astBody0 = astBody1;
         astBody1 = tbody;
         ce = ConditionFlipper.flip(ce);
      }

      return (ASTNode)(astBody1.isEmpty() ? new ASTIfNode(this.get_Label(), ce, astBody0) : new ASTIfElseNode(this.get_Label(), ce, astBody0, astBody1));
   }
}
