package soot.dava.internal.SET;

import java.util.HashSet;
import java.util.Iterator;
import soot.dava.internal.asg.AugmentedStmt;
import soot.jimple.GotoStmt;
import soot.util.IterableSet;

public abstract class SETControlFlowNode extends SETNode {
   private AugmentedStmt characterizingStmt;

   public SETControlFlowNode(AugmentedStmt characterizingStmt, IterableSet<AugmentedStmt> body) {
      super(body);
      this.characterizingStmt = characterizingStmt;
   }

   public AugmentedStmt get_CharacterizingStmt() {
      return this.characterizingStmt;
   }

   protected boolean resolve(SETNode parent) {
      Iterator var2 = parent.get_SubBodies().iterator();

      while(true) {
         IterableSet subBody;
         do {
            if (!var2.hasNext()) {
               return true;
            }

            subBody = (IterableSet)var2.next();
         } while(!subBody.contains(this.get_EntryStmt()));

         IterableSet<SETNode> childChain = (IterableSet)parent.get_Body2ChildChain().get(subBody);
         HashSet childUnion = new HashSet();
         Iterator var6 = childChain.iterator();

         while(var6.hasNext()) {
            SETNode child = (SETNode)var6.next();
            IterableSet childBody = child.get_Body();
            childUnion.addAll(childBody);
            if (childBody.contains(this.characterizingStmt)) {
               Iterator asIt = this.get_Body().snapshotIterator();

               while(true) {
                  while(asIt.hasNext()) {
                     AugmentedStmt as = (AugmentedStmt)asIt.next();
                     if (!childBody.contains(as)) {
                        this.remove_AugmentedStmt(as);
                     } else if (child instanceof SETControlFlowNode && !(child instanceof SETUnconditionalWhileNode)) {
                        SETControlFlowNode scfn = (SETControlFlowNode)child;
                        if (scfn.get_CharacterizingStmt() == as || as.cpreds.size() == 1 && as.get_Stmt() instanceof GotoStmt && scfn.get_CharacterizingStmt() == as.cpreds.get(0)) {
                           this.remove_AugmentedStmt(as);
                        }
                     }
                  }

                  return true;
               }
            }
         }
      }
   }
}
