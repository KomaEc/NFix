package soot.jimple.internal;

import java.util.List;
import soot.AbstractUnit;
import soot.Unit;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ArrayRef;
import soot.jimple.ConvertToBaf;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleToBafContext;
import soot.jimple.Stmt;

public abstract class AbstractStmt extends AbstractUnit implements Stmt, ConvertToBaf {
   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newNopInst();
      out.add(u);
      u.addAllTagsOf(this);
   }

   public boolean containsInvokeExpr() {
      return false;
   }

   public InvokeExpr getInvokeExpr() {
      throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
   }

   public ValueBox getInvokeExprBox() {
      throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
   }

   public boolean containsArrayRef() {
      return false;
   }

   public ArrayRef getArrayRef() {
      throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
   }

   public ValueBox getArrayRefBox() {
      throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
   }

   public boolean containsFieldRef() {
      return false;
   }

   public FieldRef getFieldRef() {
      throw new RuntimeException("getFieldRef() called with no FieldRef present!");
   }

   public ValueBox getFieldRefBox() {
      throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
   }
}
