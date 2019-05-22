package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.SootField;

public class FullRWSet extends RWSet {
   public int size() {
      throw new RuntimeException("Unsupported");
   }

   public boolean getCallsNative() {
      return true;
   }

   public boolean setCallsNative() {
      throw new RuntimeException("Unsupported");
   }

   public Set getGlobals() {
      throw new RuntimeException("Unsupported");
   }

   public Set getFields() {
      throw new RuntimeException("Unsupported");
   }

   public PointsToSet getBaseForField(Object f) {
      throw new RuntimeException("Unsupported");
   }

   public boolean hasNonEmptyIntersection(RWSet other) {
      return other != null;
   }

   public boolean union(RWSet other) {
      throw new RuntimeException("Unsupported");
   }

   public boolean addGlobal(SootField global) {
      throw new RuntimeException("Unsupported");
   }

   public boolean addFieldRef(PointsToSet otherBase, Object field) {
      throw new RuntimeException("Unsupported");
   }

   public boolean isEquivTo(RWSet other) {
      return other instanceof FullRWSet;
   }
}
