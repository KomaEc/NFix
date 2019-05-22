package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.RefSwitch;
import soot.util.Switch;

public class JCaughtExceptionRef implements CaughtExceptionRef {
   public boolean equivTo(Object c) {
      return c instanceof CaughtExceptionRef;
   }

   public int equivHashCode() {
      return 1729;
   }

   public Object clone() {
      return new JCaughtExceptionRef();
   }

   public String toString() {
      return "@caughtexception";
   }

   public void toString(UnitPrinter up) {
      up.identityRef(this);
   }

   public final List<ValueBox> getUseBoxes() {
      return Collections.emptyList();
   }

   public Type getType() {
      return RefType.v("java.lang.Throwable");
   }

   public void apply(Switch sw) {
      ((RefSwitch)sw).caseCaughtExceptionRef(this);
   }
}
