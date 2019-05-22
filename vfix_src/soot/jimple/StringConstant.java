package soot.jimple;

import soot.RefType;
import soot.Type;
import soot.util.StringTools;
import soot.util.Switch;

public class StringConstant extends Constant {
   public final String value;

   private StringConstant(String s) {
      this.value = s;
   }

   public static StringConstant v(String value) {
      return new StringConstant(value);
   }

   public boolean equals(Object c) {
      return c instanceof StringConstant && ((StringConstant)c).value.equals(this.value);
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   public String toString() {
      return StringTools.getQuotedStringOf(this.value);
   }

   public Type getType() {
      return RefType.v("java.lang.String");
   }

   public void apply(Switch sw) {
      ((ConstantSwitch)sw).caseStringConstant(this);
   }
}
