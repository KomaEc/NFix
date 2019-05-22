package soot;

import java.util.Set;
import soot.jimple.ClassConstant;

public interface PointsToSet {
   boolean isEmpty();

   boolean hasNonEmptyIntersection(PointsToSet var1);

   Set<Type> possibleTypes();

   Set<String> possibleStringConstants();

   Set<ClassConstant> possibleClassConstants();
}
