package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.jimple.ThrowStmt;
import soot.toolkits.graph.DominatorNode;
import soot.toolkits.graph.MHGDominatorsFinder;
import soot.toolkits.graph.MHGPostDominatorsFinder;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

public class EnhancedUnitGraph extends UnitGraph {
   protected Hashtable<Unit, Unit> try2nop = null;
   protected Hashtable<Unit, Unit> handler2header = null;

   public EnhancedUnitGraph(Body body) {
      super(body);
      this.try2nop = new Hashtable();
      this.handler2header = new Hashtable();
      int size = this.unitChain.size() + body.getTraps().size() + 2;
      this.unitToSuccs = new HashMap(size * 2 + 1, 0.7F);
      this.unitToPreds = new HashMap(size * 2 + 1, 0.7F);
      this.buildUnexceptionalEdges(this.unitToSuccs, this.unitToPreds);
      this.addAuxiliaryExceptionalEdges();
      this.buildHeadsAndTails();
      this.handleExplicitThrowEdges();
      this.buildHeadsAndTails();
      this.handleMultipleReturns();
      this.buildHeadsAndTails();
      this.removeBogusHeads();
      this.buildHeadsAndTails();
   }

   protected void handleMultipleReturns() {
      if (this.getTails().size() > 1) {
         Unit stop = new ExitStmt();
         List<Unit> predsOfstop = new ArrayList();

         Object tailSuccs;
         for(Iterator var3 = this.getTails().iterator(); var3.hasNext(); ((List)tailSuccs).add(stop)) {
            Unit tail = (Unit)var3.next();
            predsOfstop.add(tail);
            tailSuccs = (List)this.unitToSuccs.get(tail);
            if (tailSuccs == null) {
               tailSuccs = new ArrayList();
               this.unitToSuccs.put(tail, tailSuccs);
            }
         }

         this.unitToPreds.put(stop, predsOfstop);
         this.unitToSuccs.put(stop, new ArrayList());
         Chain<Unit> units = this.body.getUnits().getNonPatchingChain();
         if (!units.contains(stop)) {
            units.addLast(stop);
         }
      }

   }

   protected void removeBogusHeads() {
      Chain<Unit> units = this.body.getUnits();

      label55:
      for(Unit trueHead = (Unit)units.getFirst(); this.getHeads().size() > 1; this.buildHeadsAndTails()) {
         Iterator var3 = this.getHeads().iterator();

         label52:
         while(true) {
            Unit head;
            do {
               if (!var3.hasNext()) {
                  continue label55;
               }

               head = (Unit)var3.next();
            } while(trueHead == head);

            this.unitToPreds.remove(head);
            Iterator var5 = ((List)this.unitToSuccs.get(head)).iterator();

            while(true) {
               ArrayList tobeRemoved;
               List predOfSuccs;
               do {
                  if (!var5.hasNext()) {
                     this.unitToSuccs.remove(head);
                     if (units.contains(head)) {
                        units.remove(head);
                     }
                     continue label52;
                  }

                  Unit succ = (Unit)var5.next();
                  tobeRemoved = new ArrayList();
                  predOfSuccs = (List)this.unitToPreds.get(succ);
               } while(predOfSuccs == null);

               Iterator var9 = predOfSuccs.iterator();

               while(var9.hasNext()) {
                  Unit pred = (Unit)var9.next();
                  if (pred == head) {
                     tobeRemoved.add(pred);
                  }
               }

               predOfSuccs.removeAll(tobeRemoved);
            }
         }
      }

   }

   protected void handleExplicitThrowEdges() {
      MHGDominatorTree<Unit> dom = new MHGDominatorTree(new MHGDominatorsFinder(this));
      MHGDominatorTree<Unit> pdom = new MHGDominatorTree(new MHGPostDominatorsFinder(this));
      Hashtable<Unit, Unit> x2mergePoint = new Hashtable();
      List<Unit> tails = this.getTails();
      Iterator itr = tails.iterator();

      while(true) {
         Unit tail;
         Unit mergePoint;
         label159:
         while(true) {
            do {
               if (!itr.hasNext()) {
                  return;
               }

               tail = (Unit)itr.next();
            } while(!(tail instanceof ThrowStmt));

            DominatorNode<Unit> x = dom.getDode(tail);
            DominatorNode<Unit> parentOfX = dom.getParentOf(x);
            Unit xgode = (Unit)x.getGode();
            DominatorNode<Unit> xpdomDode = pdom.getDode(xgode);
            Unit parentXGode = (Unit)parentOfX.getGode();

            for(DominatorNode parentpdomDode = pdom.getDode(parentXGode); pdom.isDominatorOf(xpdomDode, parentpdomDode); parentpdomDode = pdom.getDode(parentXGode)) {
               x = parentOfX;
               parentOfX = dom.getParentOf(parentOfX);
               if (parentOfX == null) {
                  break;
               }

               xgode = (Unit)x.getGode();
               xpdomDode = pdom.getDode(xgode);
               parentXGode = (Unit)parentOfX.getGode();
            }

            if (parentOfX != null) {
               x = parentOfX;
            }

            xgode = (Unit)x.getGode();
            xpdomDode = pdom.getDode(xgode);
            mergePoint = null;
            if (x2mergePoint.containsKey(xgode)) {
               mergePoint = (Unit)x2mergePoint.get(xgode);
               break;
            }

            List<DominatorNode<Unit>> domChilds = dom.getChildrenOf(x);
            Unit child1god = null;
            Unit child2god = null;
            Iterator domItr = domChilds.iterator();

            DominatorNode initialY;
            Unit u;
            DominatorNode y;
            while(domItr.hasNext()) {
               initialY = (DominatorNode)domItr.next();
               u = (Unit)initialY.getGode();
               y = pdom.getDode(u);
               List<Unit> path = this.getExtendedBasicBlockPathBetween(u, tail);
               if (path == null || path.isEmpty()) {
                  if (pdom.isDominatorOf(y, xpdomDode)) {
                     mergePoint = (Unit)initialY.getGode();
                     break;
                  }

                  if (child1god == null) {
                     child1god = u;
                  } else if (child2god == null) {
                     child2god = u;
                  }
               }
            }

            if (mergePoint == null) {
               DominatorNode y;
               DominatorNode yDodeInDom;
               if (child1god != null && child2god != null) {
                  y = pdom.getDode(child1god);
                  initialY = pdom.getDode(child2god);

                  for(yDodeInDom = y.getParent(); yDodeInDom != null; yDodeInDom = yDodeInDom.getParent()) {
                     if (pdom.isDominatorOf(yDodeInDom, initialY)) {
                        mergePoint = (Unit)yDodeInDom.getGode();
                        break;
                     }
                  }
               } else if (child1god != null || child2god != null) {
                  y = null;
                  if (child1god != null) {
                     y = pdom.getDode(child1god);
                  } else if (child2god != null) {
                     y = pdom.getDode(child2god);
                  }

                  initialY = dom.getDode(y.getGode());

                  for(yDodeInDom = initialY; dom.isDominatorOf(x, yDodeInDom); yDodeInDom = dom.getDode(y.getGode())) {
                     y = y.getParent();
                     if (y == null) {
                        break;
                     }
                  }

                  if (y != null) {
                     mergePoint = (Unit)y.getGode();
                  } else {
                     mergePoint = (Unit)initialY.getGode();
                  }
               }
            }

            if (mergePoint == null) {
               List<Unit> xSucc = (List)this.unitToSuccs.get(x.getGode());
               if (xSucc != null) {
                  Iterator var25 = xSucc.iterator();

                  while(var25.hasNext()) {
                     u = (Unit)var25.next();
                     if (!dom.isDominatorOf(dom.getDode(u), dom.getDode(tail))) {
                        y = pdom.getDode(u);

                        while(dom.isDominatorOf(x, y)) {
                           y = y.getParent();
                           if (y == null) {
                              continue label159;
                           }
                        }

                        mergePoint = (Unit)y.getGode();
                        break;
                     }
                  }
               }
            } else if (dom.isDominatorOf(dom.getDode(mergePoint), dom.getDode(tail))) {
               continue;
            }

            if (mergePoint != null) {
               x2mergePoint.put(xgode, mergePoint);
               break;
            }
         }

         List<Unit> throwSuccs = (List)this.unitToSuccs.get(tail);
         if (throwSuccs == null) {
            throwSuccs = new ArrayList();
            this.unitToSuccs.put(tail, throwSuccs);
         }

         ((List)throwSuccs).add(mergePoint);
         List<Unit> mergePreds = (List)this.unitToPreds.get(mergePoint);
         if (mergePreds == null) {
            mergePreds = new ArrayList();
            this.unitToPreds.put(mergePoint, mergePreds);
         }

         ((List)mergePreds).add(tail);
      }
   }

   protected void addAuxiliaryExceptionalEdges() {
      Iterator trapIt = this.body.getTraps().iterator();

      Unit b;
      while(trapIt.hasNext()) {
         Trap trap = (Trap)trapIt.next();
         Unit handler = trap.getHandlerUnit();

         for(b = handler; ((List)this.unitToPreds.get(b)).size() > 0; b = (Unit)((List)this.unitToPreds.get(b)).get(0)) {
         }

         this.handler2header.put(handler, b);
         if (this.try2nop.containsKey(trap.getBeginUnit())) {
            Unit var10000 = (Unit)this.try2nop.get(trap.getBeginUnit());
         } else {
            Unit ehnop = new EHNopStmt();
            this.try2nop.put(trap.getBeginUnit(), ehnop);
         }
      }

      Hashtable<Unit, Boolean> nop2added = new Hashtable();
      Iterator trapIt = this.body.getTraps().iterator();

      while(true) {
         label61:
         while(true) {
            Unit handler;
            do {
               if (!trapIt.hasNext()) {
                  return;
               }

               Trap trap = (Trap)trapIt.next();
               b = trap.getBeginUnit();
               handler = trap.getHandlerUnit();
               handler = (Unit)this.handler2header.get(handler);
            } while(!this.unitToPreds.containsKey(handler));

            List<Unit> handlerPreds = (List)this.unitToPreds.get(handler);
            Iterator preditr = handlerPreds.iterator();

            while(preditr.hasNext()) {
               if (this.try2nop.containsValue(preditr.next())) {
                  continue label61;
               }
            }

            Unit ehnop = (Unit)this.try2nop.get(b);
            if (!nop2added.containsKey(ehnop)) {
               List<Unit> predsOfB = this.getPredsOf(b);
               List<Unit> predsOfehnop = new ArrayList(predsOfB);

               Object succsOfA;
               for(Iterator var9 = predsOfB.iterator(); var9.hasNext(); ((List)succsOfA).add(ehnop)) {
                  Unit a = (Unit)var9.next();
                  succsOfA = (List)this.unitToSuccs.get(a);
                  if (succsOfA == null) {
                     succsOfA = new ArrayList();
                     this.unitToSuccs.put(a, succsOfA);
                  } else {
                     ((List)succsOfA).remove(b);
                  }
               }

               predsOfB.clear();
               predsOfB.add(ehnop);
               this.unitToPreds.put(ehnop, predsOfehnop);
            }

            List<Unit> succsOfehnop = (List)this.unitToSuccs.get(ehnop);
            if (succsOfehnop == null) {
               succsOfehnop = new ArrayList();
               this.unitToSuccs.put(ehnop, succsOfehnop);
            }

            if (!((List)succsOfehnop).contains(b)) {
               ((List)succsOfehnop).add(b);
            }

            ((List)succsOfehnop).add(handler);
            List<Unit> predsOfhandler = (List)this.unitToPreds.get(handler);
            if (predsOfhandler == null) {
               predsOfhandler = new ArrayList();
               this.unitToPreds.put(handler, predsOfhandler);
            }

            ((List)predsOfhandler).add(ehnop);
            Chain<Unit> units = this.body.getUnits().getNonPatchingChain();
            if (!units.contains(ehnop)) {
               units.insertBefore((Object)ehnop, b);
            }

            nop2added.put(ehnop, Boolean.TRUE);
         }
      }
   }
}
