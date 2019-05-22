package soot.dava.toolkits.base.misc;

import java.util.ArrayList;
import java.util.Iterator;
import soot.G;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.dava.DavaBody;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.jimple.ThrowStmt;

public class ThrowNullConverter {
   private final RefType npeRef = RefType.v(Scene.v().loadClassAndSupport("java.lang.NullPointerException"));

   public ThrowNullConverter(Singletons.Global g) {
   }

   public static ThrowNullConverter v() {
      return G.v().soot_dava_toolkits_base_misc_ThrowNullConverter();
   }

   public void convert(DavaBody body) {
      Iterator it = body.getUnits().iterator();

      while(it.hasNext()) {
         Unit u = (Unit)it.next();
         if (u instanceof ThrowStmt) {
            ValueBox opBox = ((ThrowStmt)u).getOpBox();
            Value op = opBox.getValue();
            if (op.getType() instanceof NullType) {
               opBox.setValue(new DNewInvokeExpr(this.npeRef, (SootMethodRef)null, new ArrayList()));
            }
         }
      }

   }
}
