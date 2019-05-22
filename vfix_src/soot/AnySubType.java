package soot;

import soot.util.Switch;

public class AnySubType extends RefLikeType {
   private RefType base;

   private AnySubType(RefType base) {
      this.base = base;
   }

   public static AnySubType v(RefType base) {
      if (base.getAnySubType() == null) {
         synchronized(base) {
            if (base.getAnySubType() == null) {
               base.setAnySubType(new AnySubType(base));
            }
         }
      }

      return base.getAnySubType();
   }

   public String toString() {
      return "Any_subtype_of_" + this.base;
   }

   public void apply(Switch sw) {
      ((TypeSwitch)sw).caseAnySubType(this);
   }

   public Type getArrayElementType() {
      throw new RuntimeException("Attempt to get array base type of a non-array");
   }

   public RefType getBase() {
      return this.base;
   }

   public void setBase(RefType base) {
      this.base = base;
   }
}
