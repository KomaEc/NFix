package soot.jimple.toolkits.infoflow;

import java.util.Collections;
import java.util.List;
import soot.NullType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public class AbstractDataSource implements Value {
   Object sourcename;

   public AbstractDataSource(Object sourcename) {
      this.sourcename = sourcename;
   }

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Object clone() {
      return new AbstractDataSource(this.sourcename);
   }

   public boolean equivTo(Object c) {
      if (this.sourcename instanceof Value) {
         return c instanceof AbstractDataSource && ((Value)this.sourcename).equivTo(((AbstractDataSource)c).sourcename);
      } else {
         return c instanceof AbstractDataSource && ((AbstractDataSource)c).sourcename.equals(this.sourcename);
      }
   }

   public boolean equals(Object c) {
      return c instanceof AbstractDataSource && ((AbstractDataSource)c).sourcename.equals(this.sourcename);
   }

   public int equivHashCode() {
      return this.sourcename instanceof Value ? ((Value)this.sourcename).equivHashCode() : this.sourcename.hashCode();
   }

   public void toString(UnitPrinter up) {
   }

   public Type getType() {
      return NullType.v();
   }

   public void apply(Switch sw) {
      throw new RuntimeException("Not Implemented");
   }

   public String toString() {
      return "sourceof<" + this.sourcename.toString() + ">";
   }
}
