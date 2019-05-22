package soot.jimple.toolkits.thread.synchronization;

import java.util.Collections;
import java.util.List;
import soot.NullType;
import soot.SootClass;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.util.Switch;

public class NewStaticLock implements Value {
   SootClass sc;
   static int nextidnum = 1;
   int idnum;

   public NewStaticLock(SootClass sc) {
      this.sc = sc;
      this.idnum = nextidnum++;
   }

   public SootClass getLockClass() {
      return this.sc;
   }

   public List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Object clone() {
      return new NewStaticLock(this.sc);
   }

   public boolean equivTo(Object c) {
      return this.equals(c);
   }

   public boolean equals(Object c) {
      if (c instanceof NewStaticLock) {
         return ((NewStaticLock)c).idnum == this.idnum;
      } else {
         return false;
      }
   }

   public int equivHashCode() {
      return this.hashCode();
   }

   public int hashCode() {
      return this.idnum;
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
      return "<new static lock in " + this.sc.toString() + ">";
   }
}
