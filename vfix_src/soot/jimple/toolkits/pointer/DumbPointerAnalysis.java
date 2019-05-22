package soot.jimple.toolkits.pointer;

import soot.Context;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Singletons;
import soot.SootField;
import soot.Type;

public class DumbPointerAnalysis implements PointsToAnalysis {
   public DumbPointerAnalysis(Singletons.Global g) {
   }

   public static DumbPointerAnalysis v() {
      return G.v().soot_jimple_toolkits_pointer_DumbPointerAnalysis();
   }

   public PointsToSet reachingObjects(Local l) {
      Type t = l.getType();
      return t instanceof RefType ? FullObjectSet.v((RefType)t) : FullObjectSet.v();
   }

   public PointsToSet reachingObjects(Context c, Local l) {
      return this.reachingObjects(l);
   }

   public PointsToSet reachingObjects(SootField f) {
      Type t = f.getType();
      return t instanceof RefType ? FullObjectSet.v((RefType)t) : FullObjectSet.v();
   }

   public PointsToSet reachingObjects(PointsToSet s, SootField f) {
      return this.reachingObjects(f);
   }

   public PointsToSet reachingObjects(Local l, SootField f) {
      return this.reachingObjects(f);
   }

   public PointsToSet reachingObjects(Context c, Local l, SootField f) {
      return this.reachingObjects(f);
   }

   public PointsToSet reachingObjectsOfArrayElement(PointsToSet s) {
      return FullObjectSet.v();
   }
}
