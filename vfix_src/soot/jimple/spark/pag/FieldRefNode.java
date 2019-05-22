package soot.jimple.spark.pag;

import soot.Type;
import soot.util.Numberable;

public class FieldRefNode extends ValNode {
   protected VarNode base;
   protected SparkField field;

   public VarNode getBase() {
      return this.base;
   }

   public Node getReplacement() {
      if (this.replacement == this) {
         if (this.base.replacement == this.base) {
            return this;
         } else {
            Node baseRep = this.base.getReplacement();
            FieldRefNode newRep = this.pag.makeFieldRefNode((VarNode)baseRep, this.field);
            newRep.mergeWith(this);
            return this.replacement = newRep.getReplacement();
         }
      } else {
         return this.replacement = this.replacement.getReplacement();
      }
   }

   public SparkField getField() {
      return this.field;
   }

   public String toString() {
      return "FieldRefNode " + this.getNumber() + " " + this.base + "." + this.field;
   }

   FieldRefNode(PAG pag, VarNode base, SparkField field) {
      super(pag, (Type)null);
      if (field == null) {
         throw new RuntimeException("null field");
      } else {
         this.base = base;
         this.field = field;
         base.addField(this, field);
         pag.getFieldRefNodeNumberer().add((Numberable)this);
      }
   }
}
