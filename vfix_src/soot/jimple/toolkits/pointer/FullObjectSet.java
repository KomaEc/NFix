package soot.jimple.toolkits.pointer;

import java.util.Collections;
import java.util.Set;
import soot.AnySubType;
import soot.G;
import soot.PointsToSet;
import soot.RefType;
import soot.Singletons;
import soot.Type;
import soot.jimple.ClassConstant;

public class FullObjectSet extends Union {
   private final Set<Type> types;

   public FullObjectSet(Singletons.Global g) {
      this(RefType.v("java.lang.Object"));
   }

   public static FullObjectSet v() {
      return G.v().soot_jimple_toolkits_pointer_FullObjectSet();
   }

   public static FullObjectSet v(RefType t) {
      return t.getClassName().equals("java.lang.Object") ? v() : new FullObjectSet(t);
   }

   private FullObjectSet(RefType declaredType) {
      Type type = AnySubType.v(declaredType);
      this.types = Collections.singleton(type);
   }

   public Type type() {
      return (Type)this.types.iterator().next();
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean hasNonEmptyIntersection(PointsToSet other) {
      return other != null;
   }

   public Set<Type> possibleTypes() {
      return this.types;
   }

   public boolean addAll(PointsToSet s) {
      return false;
   }

   public Set<String> possibleStringConstants() {
      return null;
   }

   public Set<ClassConstant> possibleClassConstants() {
      return null;
   }

   public int depth() {
      return 0;
   }
}
