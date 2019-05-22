package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;

public class BafLocal implements Local {
   String name;
   Type type;
   int fixedHashCode;
   boolean isHashCodeChosen;
   private Local originalLocal;
   private int number = 0;

   public BafLocal(String name, Type t) {
      this.name = name;
      this.type = t;
   }

   public boolean equivTo(Object o) {
      return this.equals(o);
   }

   public int equivHashCode() {
      return this.name.hashCode() * 101 + this.type.hashCode() * 17;
   }

   public Object clone() {
      BafLocal baf = new BafLocal(this.name, this.type);
      baf.originalLocal = this.originalLocal;
      return baf;
   }

   public Local getOriginalLocal() {
      return this.originalLocal;
   }

   public void setOriginalLocal(Local l) {
      this.originalLocal = l;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Type getType() {
      return this.type;
   }

   public void setType(Type t) {
      this.type = t;
   }

   public String toString() {
      return this.getName();
   }

   public void toString(UnitPrinter up) {
      up.local(this);
   }

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public void apply(Switch s) {
      throw new RuntimeException("invalid case switch");
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }
}
