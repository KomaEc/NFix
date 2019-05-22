package soot.baf;

import soot.Unit;
import soot.baf.internal.AbstractInst;

public class PlaceholderInst extends AbstractInst {
   private Unit source;

   public final String getName() {
      return "<placeholder>";
   }

   public String toString() {
      return "<placeholder: " + this.source.toString() + ">";
   }

   PlaceholderInst(Unit source) {
      this.source = source;
   }

   public Object clone() {
      return new PlaceholderInst(this.getSource());
   }

   public Unit getSource() {
      return this.source;
   }
}
