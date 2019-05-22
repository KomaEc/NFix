package soot.jimple.toolkits.thread;

import java.util.Iterator;
import soot.IntType;
import soot.RefLikeType;
import soot.Type;
import soot.jimple.FieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;

public class EncapsulatedMethodAnalysis {
   boolean isMethodPure = true;
   boolean isMethodConditionallyPure = true;

   public EncapsulatedMethodAnalysis(UnitGraph g) {
      Iterator stmtIt = g.iterator();

      while(stmtIt.hasNext()) {
         Stmt s = (Stmt)stmtIt.next();
         if (s.containsFieldRef()) {
            FieldRef ref = s.getFieldRef();
            if (ref instanceof StaticFieldRef && Type.toMachineType(((StaticFieldRef)ref).getType()) instanceof RefLikeType) {
               this.isMethodPure = false;
               this.isMethodConditionallyPure = false;
               return;
            }
         }
      }

      Iterator paramTypesIt = g.getBody().getMethod().getParameterTypes().iterator();

      Type paramType;
      do {
         if (!paramTypesIt.hasNext()) {
            return;
         }

         paramType = (Type)paramTypesIt.next();
      } while(Type.toMachineType(paramType) == IntType.v());

      this.isMethodPure = false;
   }

   public boolean isPure() {
      return this.isMethodPure;
   }

   public boolean isConditionallyPure() {
      return this.isMethodConditionallyPure;
   }
}
