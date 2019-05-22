package soot.shimple.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.PatchingChain;
import soot.TrapManager;
import soot.Unit;
import soot.UnitBox;
import soot.options.Options;
import soot.shimple.PhiExpr;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public class SPatchingChain extends PatchingChain<Unit> {
   private static final Logger logger = LoggerFactory.getLogger(SPatchingChain.class);
   Body body = null;
   boolean debug;
   protected Map<UnitBox, Unit> boxToPhiNode = new HashMap();
   protected Set<Unit> phiNodeSet = new HashSet();
   protected Map<SUnitBox, Boolean> boxToNeedsPatching = new HashMap();

   public SPatchingChain(Body aBody, Chain<Unit> aChain) {
      super(aChain);
      this.body = aBody;
      this.debug = Options.v().debug();
      if (aBody instanceof ShimpleBody) {
         this.debug |= ((ShimpleBody)aBody).getOptions().debug();
      }

   }

   public boolean add(Unit o) {
      this.processPhiNode(o);
      return super.add(o);
   }

   public void swapWith(Unit out, Unit in) {
      this.processPhiNode(in);
      Shimple.redirectPointers(out, in);
      super.insertBefore(in, out);
      super.remove(out);
   }

   public void insertAfter(Unit toInsert, Unit point) {
      this.processPhiNode(toInsert);
      super.insertAfter(toInsert, point);
      Unit unit = point;
      if (point.fallsThrough()) {
         if (!point.branches()) {
            Set<Unit> trappedUnits = Collections.emptySet();
            if (this.body != null) {
               trappedUnits = TrapManager.getTrappedUnitsOf(this.body);
            }

            if (!trappedUnits.contains(point)) {
               Shimple.redirectPointers(point, toInsert);
               return;
            }
         }

         UnitBox[] boxes = (UnitBox[])((UnitBox[])point.getBoxesPointingToThis().toArray(new UnitBox[0]));
         UnitBox[] var5 = boxes;
         int var6 = boxes.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            UnitBox ub = var5[var7];
            if (ub.getUnit() != unit) {
               throw new RuntimeException("Assertion failed.");
            }

            if (!ub.isBranchTarget()) {
               SUnitBox box = this.getSBox(ub);
               Boolean needsPatching = (Boolean)this.boxToNeedsPatching.get(box);
               if (needsPatching == null || box.isUnitChanged()) {
                  if (!this.boxToPhiNode.containsKey(box)) {
                     this.reprocessPhiNodes();
                     if (!this.boxToPhiNode.containsKey(box) && this.debug) {
                        throw new RuntimeException("SPatchingChain has pointers from a Phi node that has never been seen.");
                     }
                  }

                  this.computeNeedsPatching();
                  needsPatching = (Boolean)this.boxToNeedsPatching.get(box);
                  if (needsPatching == null) {
                     if (this.debug) {
                        logger.warn("Orphaned UnitBox to " + unit + "?  SPatchingChain will not move the pointer.");
                     }
                     continue;
                  }
               }

               if (needsPatching) {
                  box.setUnit(toInsert);
                  box.setUnitChanged(false);
               }
            }
         }
      }

   }

   public void insertAfter(List<Unit> toInsert, Unit point) {
      Iterator var3 = toInsert.iterator();

      while(var3.hasNext()) {
         Unit unit = (Unit)var3.next();
         this.processPhiNode(unit);
      }

      super.insertAfter(toInsert, point);
   }

   public void insertBefore(List<Unit> toInsert, Unit point) {
      Iterator var3 = toInsert.iterator();

      while(var3.hasNext()) {
         Unit unit = (Unit)var3.next();
         this.processPhiNode(unit);
      }

      super.insertBefore(toInsert, point);
   }

   public void insertBefore(Unit toInsert, Unit point) {
      this.processPhiNode(toInsert);
      super.insertBefore(toInsert, point);
   }

   public void addFirst(Unit u) {
      this.processPhiNode(u);
      super.addFirst(u);
   }

   public void addLast(Unit u) {
      this.processPhiNode(u);
      super.addLast(u);
   }

   public boolean remove(Unit obj) {
      if (this.contains(obj)) {
         Shimple.redirectToPreds(this.body, obj);
      }

      return super.remove(obj);
   }

   protected void processPhiNode(Unit o) {
      Unit phiNode = o;
      PhiExpr phi = Shimple.getPhiExpr(o);
      if (phi != null) {
         if (!this.phiNodeSet.contains(o)) {
            Iterator var4 = phi.getUnitBoxes().iterator();

            while(var4.hasNext()) {
               UnitBox box = (UnitBox)var4.next();
               this.boxToPhiNode.put(box, phiNode);
               this.phiNodeSet.add(phiNode);
            }

         }
      }
   }

   protected void reprocessPhiNodes() {
      Set<Unit> phiNodes = new HashSet(this.boxToPhiNode.values());
      this.boxToPhiNode = new HashMap();
      this.phiNodeSet = new HashSet();
      this.boxToNeedsPatching = new HashMap();
      Iterator phiNodesIt = phiNodes.iterator();

      while(phiNodesIt.hasNext()) {
         this.processPhiNode((Unit)phiNodesIt.next());
      }

   }

   protected void computeNeedsPatching() {
      Set<UnitBox> boxes = this.boxToPhiNode.keySet();
      if (!boxes.isEmpty()) {
         MultiMap<Unit, UnitBox> trackedPhiToBoxes = new HashMultiMap();
         Set<UnitBox> trackedBranchTargets = new HashSet();
         Iterator boxesIt = this.iterator();

         while(true) {
            Unit u;
            Set boxes;
            label69:
            do {
               while(boxesIt.hasNext()) {
                  u = (Unit)boxesIt.next();
                  List<UnitBox> boxesToTrack = u.getBoxesPointingToThis();
                  Iterator boxesIt;
                  if (boxesToTrack != null) {
                     boxesIt = boxesToTrack.iterator();

                     while(boxesIt.hasNext()) {
                        UnitBox boxToTrack = (UnitBox)boxesIt.next();
                        if (!boxToTrack.isBranchTarget()) {
                           trackedPhiToBoxes.put(this.boxToPhiNode.get(boxToTrack), boxToTrack);
                        }
                     }
                  }

                  if (u.fallsThrough() && u.branches()) {
                     trackedBranchTargets.addAll(u.getUnitBoxes());
                  }

                  if (u.fallsThrough() && !trackedBranchTargets.contains(u)) {
                     boxes = trackedPhiToBoxes.get(u);
                     continue label69;
                  }

                  boxesIt = trackedPhiToBoxes.values().iterator();

                  while(boxesIt.hasNext()) {
                     SUnitBox box = this.getSBox((UnitBox)boxesIt.next());
                     this.boxToNeedsPatching.put(box, Boolean.FALSE);
                     box.setUnitChanged(false);
                  }

                  trackedPhiToBoxes = new HashMultiMap();
               }

               boxesIt = trackedPhiToBoxes.values().iterator();

               while(boxesIt.hasNext()) {
                  SUnitBox box = this.getSBox((UnitBox)boxesIt.next());
                  this.boxToNeedsPatching.put(box, Boolean.FALSE);
                  box.setUnitChanged(false);
               }

               return;
            } while(boxes == null);

            Iterator var14 = boxes.iterator();

            while(var14.hasNext()) {
               UnitBox ub = (UnitBox)var14.next();
               SUnitBox box = this.getSBox(ub);
               this.boxToNeedsPatching.put(box, Boolean.TRUE);
               box.setUnitChanged(false);
            }

            trackedPhiToBoxes.remove(u);
         }
      }
   }

   protected SUnitBox getSBox(UnitBox box) {
      if (!(box instanceof SUnitBox)) {
         throw new RuntimeException("Shimple box not an SUnitBox?");
      } else {
         return (SUnitBox)box;
      }
   }

   public Iterator<Unit> iterator() {
      return new SPatchingChain.SPatchingIterator(this.innerChain);
   }

   public Iterator<Unit> iterator(Unit u) {
      return new SPatchingChain.SPatchingIterator(this.innerChain, u);
   }

   public Iterator<Unit> iterator(Unit head, Unit tail) {
      return new SPatchingChain.SPatchingIterator(this.innerChain, head, tail);
   }

   protected class SPatchingIterator extends PatchingChain<Unit>.PatchingIterator {
      SPatchingIterator(Chain<Unit> innerChain) {
         super(innerChain);
      }

      SPatchingIterator(Chain<Unit> innerChain, Unit u) {
         super(innerChain, u);
      }

      SPatchingIterator(Chain<Unit> innerChain, Unit head, Unit tail) {
         super(innerChain, head, tail);
      }

      public void remove() {
         Unit victim = this.lastObject;
         if (!this.state) {
            throw new IllegalStateException("remove called before first next() call");
         } else {
            Shimple.redirectToPreds(SPatchingChain.this.body, victim);
            Unit successor;
            if ((successor = SPatchingChain.this.getSuccOf(victim)) == null) {
               successor = SPatchingChain.this.getPredOf(victim);
            }

            this.innerIterator.remove();
            victim.redirectJumpsToThisTo(successor);
         }
      }
   }
}
