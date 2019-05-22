package soot.jimple.spark.sets;

import java.util.Collections;
import java.util.Set;
import soot.G;
import soot.PointsToSet;
import soot.Singletons;
import soot.Type;
import soot.jimple.ClassConstant;
import soot.jimple.spark.pag.Node;

public class EmptyPointsToSet extends PointsToSetInternal {
   public EmptyPointsToSet(Singletons.Global g) {
      super((Type)null);
   }

   public static EmptyPointsToSet v() {
      return G.v().soot_jimple_spark_sets_EmptyPointsToSet();
   }

   public EmptyPointsToSet(Singletons.Global g, Type type) {
      super(type);
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      return false;
   }

   public Set<Type> possibleTypes() {
      return Collections.emptySet();
   }

   public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      throw new RuntimeException("can't add into empty immutable set");
   }

   public boolean forall(P2SetVisitor v) {
      return false;
   }

   public boolean add(Node n) {
      throw new RuntimeException("can't add into empty immutable set");
   }

   public boolean contains(Node n) {
      return false;
   }

   public Set<String> possibleStringConstants() {
      return Collections.emptySet();
   }

   public Set<ClassConstant> possibleClassConstants() {
      return Collections.emptySet();
   }

   public String toString() {
      return "{}";
   }
}
