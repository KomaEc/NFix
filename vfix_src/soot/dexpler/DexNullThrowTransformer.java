package soot.dexpler;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethodRef;
import soot.Unit;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.scalar.LocalCreation;

public class DexNullThrowTransformer extends BodyTransformer {
   public static DexNullThrowTransformer v() {
      return new DexNullThrowTransformer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      LocalCreation lc = new LocalCreation(b.getLocals(), "ex");
      Iterator unitIt = b.getUnits().snapshotIterator();

      while(true) {
         ThrowStmt throwStmt;
         do {
            Unit u;
            do {
               if (!unitIt.hasNext()) {
                  return;
               }

               u = (Unit)unitIt.next();
            } while(!(u instanceof ThrowStmt));

            throwStmt = (ThrowStmt)u;
         } while(throwStmt.getOp() != NullConstant.v() && !throwStmt.getOp().equals(IntConstant.v(0)) && !throwStmt.getOp().equals(LongConstant.v(0L)));

         this.createThrowStmt(b, throwStmt, lc);
      }
   }

   private void createThrowStmt(Body body, Unit oldStmt, LocalCreation lc) {
      RefType tp = RefType.v("java.lang.NullPointerException");
      Local lcEx = lc.newLocal(tp);
      SootMethodRef constructorRef = Scene.v().makeConstructorRef(tp.getSootClass(), Collections.singletonList(RefType.v("java.lang.String")));
      Stmt newExStmt = Jimple.v().newAssignStmt(lcEx, Jimple.v().newNewExpr(tp));
      body.getUnits().insertBefore((Unit)newExStmt, (Unit)oldStmt);
      Stmt invConsStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lcEx, constructorRef, Collections.singletonList(StringConstant.v("Null throw statement replaced by Soot"))));
      body.getUnits().insertBefore((Unit)invConsStmt, (Unit)oldStmt);
      body.getUnits().swapWith((Unit)oldStmt, (Unit)Jimple.v().newThrowStmt(lcEx));
   }
}
