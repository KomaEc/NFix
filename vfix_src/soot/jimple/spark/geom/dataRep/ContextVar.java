package soot.jimple.spark.geom.dataRep;

import soot.jimple.spark.pag.Node;
import soot.util.Numberable;

public abstract class ContextVar implements Numberable {
   public Node var = null;
   public int id = -1;

   protected ContextVar() {
   }

   public void setNumber(int number) {
      this.id = number;
   }

   public int getNumber() {
      return this.id;
   }

   public abstract boolean contains(ContextVar var1);

   public abstract boolean merge(ContextVar var1);

   public abstract boolean intersect(ContextVar var1);
}
