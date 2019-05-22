package soot.jimple.spark.pag;

import soot.G;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.toolkits.scalar.Pair;

public class Parm implements SparkField {
   private final int index;
   private final SootMethod method;
   private int number = 0;

   private Parm(SootMethod m, int i) {
      this.index = i;
      this.method = m;
      Scene.v().getFieldNumberer().add(this);
   }

   public static Parm v(SootMethod m, int index) {
      Pair<SootMethod, Integer> p = new Pair(m, new Integer(index));
      Parm ret = (Parm)G.v().Parm_pairToElement.get(p);
      if (ret == null) {
         G.v().Parm_pairToElement.put(p, ret = new Parm(m, index));
      }

      return ret;
   }

   public static final void delete() {
      G.v().Parm_pairToElement = null;
   }

   public String toString() {
      return "Parm " + this.index + " to " + this.method;
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }

   public int getIndex() {
      return this.index;
   }

   public Type getType() {
      return this.index == -2 ? this.method.getReturnType() : this.method.getParameterType(this.index);
   }
}
