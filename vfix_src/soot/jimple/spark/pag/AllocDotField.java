package soot.jimple.spark.pag;

import soot.Type;
import soot.util.Numberable;

public class AllocDotField extends Node {
   protected AllocNode base;
   protected SparkField field;

   public AllocNode getBase() {
      return this.base;
   }

   public SparkField getField() {
      return this.field;
   }

   public String toString() {
      return "AllocDotField " + this.getNumber() + " " + this.base + "." + this.field;
   }

   AllocDotField(PAG pag, AllocNode base, SparkField field) {
      super(pag, (Type)null);
      if (field == null) {
         throw new RuntimeException("null field");
      } else {
         this.base = base;
         this.field = field;
         base.addField(this, field);
         pag.getAllocDotFieldNodeNumberer().add((Numberable)this);
      }
   }
}
