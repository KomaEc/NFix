package soot.dava.internal.SET;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.AST.ASTTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.toolkits.base.finders.ExceptionFinder;
import soot.dava.toolkits.base.finders.ExceptionNode;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;

public class SETTryNode extends SETNode {
   private ExceptionNode en;
   private DavaBody davaBody;
   private AugmentedStmtGraph asg;
   private final HashMap<IterableSet, IterableSet> cb2clone;

   public SETTryNode(IterableSet body, ExceptionNode en, AugmentedStmtGraph asg, DavaBody davaBody) {
      super(body);
      this.en = en;
      this.asg = asg;
      this.davaBody = davaBody;
      this.add_SubBody(en.get_TryBody());
      this.cb2clone = new HashMap();
      Iterator it = en.get_CatchList().iterator();

      while(it.hasNext()) {
         IterableSet catchBody = (IterableSet)it.next();
         IterableSet clone = (IterableSet)catchBody.clone();
         this.cb2clone.put(catchBody, clone);
         this.add_SubBody(clone);
      }

      this.entryStmt = null;
      it = body.iterator();

      while(it.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)it.next();
         Iterator pit = as.cpreds.iterator();

         while(pit.hasNext()) {
            if (!body.contains(pit.next())) {
               this.entryStmt = as;
               return;
            }
         }
      }

   }

   public AugmentedStmt get_EntryStmt() {
      return this.entryStmt != null ? this.entryStmt : (AugmentedStmt)this.en.get_TryBody().getFirst();
   }

   public IterableSet get_NaturalExits() {
      IterableSet c = new IterableSet();
      Iterator it = this.subBodies.iterator();

      while(it.hasNext()) {
         Iterator eit = ((SETNode)((IterableSet)this.body2childChain.get(it.next())).getLast()).get_NaturalExits().iterator();

         while(eit.hasNext()) {
            Object o = eit.next();
            if (!c.contains(o)) {
               c.add(o);
            }
         }
      }

      return c;
   }

   public ASTNode emit_AST() {
      LinkedList<Object> catchList = new LinkedList();
      HashMap<Object, Object> exceptionMap = new HashMap();
      HashMap<Object, Object> paramMap = new HashMap();
      Iterator it = this.en.get_CatchList().iterator();

      while(true) {
         while(it.hasNext()) {
            IterableSet originalCatchBody = (IterableSet)it.next();
            IterableSet catchBody = (IterableSet)this.cb2clone.get(originalCatchBody);
            List<Object> astBody = this.emit_ASTBody((IterableSet)this.body2childChain.get(catchBody));
            exceptionMap.put(astBody, this.en.get_Exception(originalCatchBody));
            catchList.addLast(astBody);
            Iterator bit = catchBody.iterator();

            while(bit.hasNext()) {
               Stmt s = ((AugmentedStmt)bit.next()).get_Stmt();
               if (s instanceof GotoStmt) {
                  s = (Stmt)((GotoStmt)s).getTarget();
               }

               if (s instanceof IdentityStmt) {
                  IdentityStmt ids = (IdentityStmt)s;
                  Value rightOp = ids.getRightOp();
                  Value leftOp = ids.getLeftOp();
                  if (rightOp instanceof CaughtExceptionRef) {
                     paramMap.put(astBody, leftOp);
                     break;
                  }
               }
            }
         }

         return new ASTTryNode(this.get_Label(), this.emit_ASTBody((IterableSet)this.body2childChain.get(this.en.get_TryBody())), catchList, exceptionMap, paramMap);
      }
   }

   protected boolean resolve(SETNode parent) {
      Iterator sbit = parent.get_SubBodies().iterator();

      while(true) {
         IterableSet subBody;
         do {
            if (!sbit.hasNext()) {
               return true;
            }

            subBody = (IterableSet)sbit.next();
         } while(!subBody.intersects(this.en.get_TryBody()));

         IterableSet childChain = (IterableSet)parent.get_Body2ChildChain().get(subBody);
         Iterator ccit = childChain.iterator();

         while(ccit.hasNext()) {
            SETNode child = (SETNode)ccit.next();
            IterableSet childBody = child.get_Body();
            if (childBody.intersects(this.en.get_TryBody()) && !childBody.isSubsetOf(this.en.get_TryBody())) {
               if (childBody.isSupersetOf(this.get_Body())) {
                  return true;
               }

               IterableSet newTryBody = childBody.intersection(this.en.get_TryBody());
               Iterator cit;
               if (newTryBody.isStrictSubsetOf(this.en.get_TryBody())) {
                  this.en.splitOff_ExceptionNode(newTryBody, this.asg, this.davaBody.get_ExceptionFacts());
                  cit = this.davaBody.get_ExceptionFacts().iterator();

                  while(cit.hasNext()) {
                     ((ExceptionNode)cit.next()).refresh_CatchBody(ExceptionFinder.v());
                  }

                  return false;
               }

               cit = this.en.get_CatchList().iterator();

               label71:
               while(cit.hasNext()) {
                  Iterator bit = ((IterableSet)this.cb2clone.get(cit.next())).snapshotIterator();

                  while(true) {
                     while(true) {
                        if (!bit.hasNext()) {
                           continue label71;
                        }

                        AugmentedStmt as = (AugmentedStmt)bit.next();
                        if (!childBody.contains(as)) {
                           this.remove_AugmentedStmt(as);
                        } else if (child instanceof SETControlFlowNode && !(child instanceof SETUnconditionalWhileNode)) {
                           SETControlFlowNode scfn = (SETControlFlowNode)child;
                           if (scfn.get_CharacterizingStmt() == as || as.cpreds.size() == 1 && as.get_Stmt() instanceof GotoStmt && scfn.get_CharacterizingStmt() == as.cpreds.get(0)) {
                              this.remove_AugmentedStmt(as);
                           }
                        }
                     }
                  }
               }

               return true;
            }
         }
      }
   }
}
