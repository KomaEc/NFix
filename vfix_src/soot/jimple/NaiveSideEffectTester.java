package soot.jimple;

import java.util.Iterator;
import soot.Local;
import soot.SideEffectTester;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;

public class NaiveSideEffectTester implements SideEffectTester {
   public void newMethod(SootMethod m) {
   }

   public boolean unitCanReadFrom(Unit u, Value v) {
      Stmt s = (Stmt)u;
      if (v instanceof Constant) {
         return false;
      } else if (v instanceof Expr) {
         throw new RuntimeException("can't deal with expr");
      } else if (s.containsInvokeExpr() && !(v instanceof Local)) {
         return true;
      } else {
         Iterator useIt = u.getUseBoxes().iterator();

         while(useIt.hasNext()) {
            Value use = (Value)useIt.next();
            if (use.equivTo(v)) {
               return true;
            }

            Iterator vUseIt = v.getUseBoxes().iterator();

            while(vUseIt.hasNext()) {
               if (use.equivTo(vUseIt.next())) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean unitCanWriteTo(Unit u, Value v) {
      Stmt s = (Stmt)u;
      if (v instanceof Constant) {
         return false;
      } else if (v instanceof Expr) {
         throw new RuntimeException("can't deal with expr");
      } else if (s.containsInvokeExpr() && !(v instanceof Local)) {
         return true;
      } else {
         Iterator defIt = u.getDefBoxes().iterator();

         Value def;
         do {
            if (!defIt.hasNext()) {
               return false;
            }

            def = ((ValueBox)((ValueBox)defIt.next())).getValue();
            Iterator useIt = v.getUseBoxes().iterator();

            while(useIt.hasNext()) {
               Value use = ((ValueBox)useIt.next()).getValue();
               if (def.equivTo(use)) {
                  return true;
               }
            }

            if (def.equivTo(v)) {
               return true;
            }
         } while(!(v instanceof InstanceFieldRef) || !(def instanceof InstanceFieldRef) || ((InstanceFieldRef)v).getField() != ((InstanceFieldRef)def).getField());

         return true;
      }
   }
}
