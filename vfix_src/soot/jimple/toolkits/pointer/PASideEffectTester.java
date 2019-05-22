package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;

public class PASideEffectTester implements SideEffectTester {
   PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
   SideEffectAnalysis sea = Scene.v().getSideEffectAnalysis();
   HashMap<Unit, RWSet> unitToRead;
   HashMap<Unit, RWSet> unitToWrite;
   HashMap<Local, PointsToSet> localToReachingObjects;
   SootMethod currentMethod;

   public PASideEffectTester() {
      if (G.v().Union_factory == null) {
         G.v().Union_factory = new UnionFactory() {
            public Union newUnion() {
               return FullObjectSet.v();
            }
         };
      }

   }

   public void newMethod(SootMethod m) {
      this.unitToRead = new HashMap();
      this.unitToWrite = new HashMap();
      this.localToReachingObjects = new HashMap();
      this.currentMethod = m;
      this.sea.findNTRWSets(this.currentMethod);
   }

   protected RWSet readSet(Unit u) {
      RWSet ret = (RWSet)this.unitToRead.get(u);
      if (ret == null) {
         this.unitToRead.put(u, ret = this.sea.readSet(this.currentMethod, (Stmt)u));
      }

      return ret;
   }

   protected RWSet writeSet(Unit u) {
      RWSet ret = (RWSet)this.unitToWrite.get(u);
      if (ret == null) {
         this.unitToWrite.put(u, ret = this.sea.writeSet(this.currentMethod, (Stmt)u));
      }

      return ret;
   }

   protected PointsToSet reachingObjects(Local l) {
      PointsToSet ret = (PointsToSet)this.localToReachingObjects.get(l);
      if (ret == null) {
         this.localToReachingObjects.put(l, ret = this.pa.reachingObjects(l));
      }

      return ret;
   }

   public boolean unitCanReadFrom(Unit u, Value v) {
      return this.valueTouchesRWSet(this.readSet(u), v, u.getUseBoxes());
   }

   public boolean unitCanWriteTo(Unit u, Value v) {
      return this.valueTouchesRWSet(this.writeSet(u), v, u.getDefBoxes());
   }

   protected boolean valueTouchesRWSet(RWSet s, Value v, List boxes) {
      Iterator boxIt = v.getUseBoxes().iterator();

      ValueBox box;
      do {
         if (!boxIt.hasNext()) {
            if (v instanceof Constant) {
               return false;
            }

            if (v instanceof Expr) {
               throw new RuntimeException("can't deal with expr");
            }

            boxIt = boxes.iterator();

            Value boxed;
            do {
               if (!boxIt.hasNext()) {
                  if (v instanceof Local) {
                     return false;
                  }

                  PointsToSet o1;
                  PointsToSet o2;
                  if (v instanceof InstanceFieldRef) {
                     InstanceFieldRef ifr = (InstanceFieldRef)v;
                     if (s == null) {
                        return false;
                     }

                     o1 = s.getBaseForField(ifr.getField());
                     if (o1 == null) {
                        return false;
                     }

                     o2 = this.reachingObjects((Local)ifr.getBase());
                     if (o2 == null) {
                        return false;
                     }

                     return o1.hasNonEmptyIntersection(o2);
                  }

                  if (v instanceof ArrayRef) {
                     ArrayRef ar = (ArrayRef)v;
                     if (s == null) {
                        return false;
                     }

                     o1 = s.getBaseForField("ARRAY_ELEMENTS_NODE");
                     if (o1 == null) {
                        return false;
                     }

                     o2 = this.reachingObjects((Local)ar.getBase());
                     if (o2 == null) {
                        return false;
                     }

                     return o1.hasNonEmptyIntersection(o2);
                  }

                  if (v instanceof StaticFieldRef) {
                     StaticFieldRef sfr = (StaticFieldRef)v;
                     if (s == null) {
                        return false;
                     }

                     return s.getGlobals().contains(sfr.getField());
                  }

                  throw new RuntimeException("Forgot to handle value " + v);
               }

               box = (ValueBox)boxIt.next();
               boxed = box.getValue();
            } while(!boxed.equivTo(v));

            return true;
         }

         box = (ValueBox)boxIt.next();
      } while(!this.valueTouchesRWSet(s, box.getValue(), boxes));

      return true;
   }
}
