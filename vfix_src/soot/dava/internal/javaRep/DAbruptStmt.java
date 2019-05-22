package soot.dava.internal.javaRep;

import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.jimple.internal.AbstractStmt;

public class DAbruptStmt extends AbstractStmt {
   private String command;
   private SETNodeLabel label;
   public boolean surpressDestinationLabel;

   public DAbruptStmt(String command, SETNodeLabel label) {
      this.command = command;
      this.label = label;
      label.set_Name();
      this.surpressDestinationLabel = false;
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return false;
   }

   public Object clone() {
      return new DAbruptStmt(this.command, this.label);
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append(this.command);
      if (!this.surpressDestinationLabel && this.label.toString() != null) {
         b.append(" ");
         b.append(this.label.toString());
      }

      return b.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal(this.command);
      if (!this.surpressDestinationLabel && this.label.toString() != null) {
         up.literal(" ");
         up.literal(this.label.toString());
      }

   }

   public boolean is_Continue() {
      return this.command.equals("continue");
   }

   public boolean is_Break() {
      return this.command.equals("break");
   }

   public void setLabel(SETNodeLabel label) {
      this.label = label;
   }

   public SETNodeLabel getLabel() {
      return this.label;
   }
}
