package soot.jimple;

import soot.Unit;
import soot.UnitPrinter;
import soot.jimple.internal.AbstractStmt;

public class PlaceholderStmt extends AbstractStmt {
   private Unit source;

   public String toString() {
      return "<placeholder: " + this.source.toString() + ">";
   }

   public void toString(UnitPrinter up) {
      up.literal("<placeholder: ");
      this.source.toString(up);
      up.literal(">");
   }

   PlaceholderStmt(Unit source) {
      this.source = source;
   }

   public Unit getSource() {
      return this.source;
   }

   public boolean fallsThrough() {
      throw new RuntimeException();
   }

   public boolean branches() {
      throw new RuntimeException();
   }

   public Object clone() {
      throw new RuntimeException();
   }
}
