package soot.dava.internal.SET;

import java.util.Iterator;
import soot.Value;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTSynchronizedBlockNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.util.IterableSet;

public class SETSynchronizedBlockNode extends SETNode {
   private Value local;

   public SETSynchronizedBlockNode(ExceptionNode en, Value local) {
      super(en.get_Body());
      this.add_SubBody(en.get_TryBody());
      this.add_SubBody(en.get_CatchBody());
      this.local = local;
   }

   public IterableSet get_NaturalExits() {
      return ((SETNode)((IterableSet)this.body2childChain.get(this.subBodies.get(0))).getLast()).get_NaturalExits();
   }

   public ASTNode emit_AST() {
      return new ASTSynchronizedBlockNode(this.get_Label(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.subBodies.get(0))), this.local);
   }

   public AugmentedStmt get_EntryStmt() {
      return ((SETNode)((IterableSet)this.body2childChain.get(this.subBodies.get(0))).getFirst()).get_EntryStmt();
   }

   protected boolean resolve(SETNode parent) {
      Iterator sbit = parent.get_SubBodies().iterator();

      IterableSet subBody;
      do {
         if (!sbit.hasNext()) {
            return true;
         }

         subBody = (IterableSet)sbit.next();
      } while(!subBody.intersects(this.get_Body()));

      return subBody.isSupersetOf(this.get_Body());
   }
}
