package soot.toDex;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.JimpleValueSwitch;
import soot.util.Switch;

public class TemporaryRegisterLocal implements Local {
   private static final long serialVersionUID = 1L;
   private Type type;

   public TemporaryRegisterLocal(Type regType) {
      this.setType(regType);
   }

   public Local clone() {
      throw new RuntimeException("Not implemented");
   }

   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Type getType() {
      return this.type;
   }

   public void toString(UnitPrinter up) {
      throw new RuntimeException("Not implemented.");
   }

   public void apply(Switch sw) {
      ((JimpleValueSwitch)sw).caseLocal(this);
   }

   public boolean equivTo(Object o) {
      return this.equals(o);
   }

   public int equivHashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.type == null ? 0 : this.type.hashCode());
      return result;
   }

   public void setNumber(int number) {
      throw new RuntimeException("Not implemented.");
   }

   public int getNumber() {
      throw new RuntimeException("Not implemented.");
   }

   public String getName() {
      throw new RuntimeException("Not implemented.");
   }

   public void setName(String name) {
      throw new RuntimeException("Not implemented.");
   }

   public void setType(Type t) {
      this.type = t;
   }
}
