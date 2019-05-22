package soot.jimple.toolkits.pointer;

import java.util.Set;
import soot.PointsToSet;
import soot.SootField;

public abstract class RWSet {
   public abstract boolean getCallsNative();

   public abstract boolean setCallsNative();

   public abstract int size();

   public abstract Set<?> getGlobals();

   public abstract Set<?> getFields();

   public abstract PointsToSet getBaseForField(Object var1);

   public abstract boolean hasNonEmptyIntersection(RWSet var1);

   public abstract boolean union(RWSet var1);

   public abstract boolean addGlobal(SootField var1);

   public abstract boolean addFieldRef(PointsToSet var1, Object var2);

   public abstract boolean isEquivTo(RWSet var1);
}
