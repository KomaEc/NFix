package soot;

import java.util.HashSet;
import soot.jimple.Constant;
import soot.jimple.IdentityRef;
import soot.jimple.Jimple;

public abstract class AbstractUnitPrinter implements UnitPrinter {
   protected boolean startOfLine = true;
   protected String indent = "        ";
   protected StringBuffer output = new StringBuffer();
   protected AttributesUnitPrinter pt;
   protected HashSet<String> quotableLocals;

   public void setPositionTagger(AttributesUnitPrinter pt) {
      this.pt = pt;
      pt.setUnitPrinter(this);
   }

   public AttributesUnitPrinter getPositionTagger() {
      return this.pt;
   }

   public void startUnit(Unit u) {
      this.handleIndent();
      if (this.pt != null) {
         this.pt.startUnit(u);
      }

   }

   public void endUnit(Unit u) {
      if (this.pt != null) {
         this.pt.endUnit(u);
      }

   }

   public void startUnitBox(UnitBox ub) {
      this.handleIndent();
   }

   public void endUnitBox(UnitBox ub) {
   }

   public void startValueBox(ValueBox vb) {
      this.handleIndent();
      if (this.pt != null) {
         this.pt.startValueBox(vb);
      }

   }

   public void endValueBox(ValueBox vb) {
      if (this.pt != null) {
         this.pt.endValueBox(vb);
      }

   }

   public void noIndent() {
      this.startOfLine = false;
   }

   public void incIndent() {
      this.indent = this.indent + "    ";
   }

   public void decIndent() {
      if (this.indent.length() >= 4) {
         this.indent = this.indent.substring(4);
      }

   }

   public void setIndent(String indent) {
      this.indent = indent;
   }

   public String getIndent() {
      return this.indent;
   }

   public abstract void literal(String var1);

   public abstract void type(Type var1);

   public abstract void methodRef(SootMethodRef var1);

   public abstract void fieldRef(SootFieldRef var1);

   public abstract void identityRef(IdentityRef var1);

   public abstract void unitRef(Unit var1, boolean var2);

   public void newline() {
      this.output.append("\n");
      this.startOfLine = true;
      if (this.pt != null) {
         this.pt.newline();
      }

   }

   public void local(Local l) {
      this.handleIndent();
      if (this.quotableLocals == null) {
         this.initializeQuotableLocals();
      }

      if (this.quotableLocals.contains(l.getName())) {
         this.output.append("'" + l.getName() + "'");
      } else {
         this.output.append(l.getName());
      }

   }

   public void constant(Constant c) {
      this.handleIndent();
      this.output.append(c.toString());
   }

   public String toString() {
      String ret = this.output.toString();
      this.output = new StringBuffer();
      return ret;
   }

   public StringBuffer output() {
      return this.output;
   }

   protected void handleIndent() {
      if (this.startOfLine) {
         this.output.append(this.indent);
      }

      this.startOfLine = false;
   }

   protected void initializeQuotableLocals() {
      this.quotableLocals = new HashSet();
      this.quotableLocals.addAll(Jimple.jimpleKeywordList());
   }
}
