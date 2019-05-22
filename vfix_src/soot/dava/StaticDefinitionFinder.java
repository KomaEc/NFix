package soot.dava;

import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;

public class StaticDefinitionFinder extends DepthFirstAdapter {
   SootMethod method;
   boolean finalFieldDefined;

   public StaticDefinitionFinder(SootMethod method) {
      this.method = method;
      this.finalFieldDefined = false;
   }

   public StaticDefinitionFinder(boolean verbose, SootMethod method) {
      super(verbose);
      this.method = method;
      this.finalFieldDefined = false;
   }

   public void inDefinitionStmt(DefinitionStmt s) {
      Value leftOp = s.getLeftOp();
      if (leftOp instanceof FieldRef) {
         SootField field = ((FieldRef)leftOp).getField();
         if (field.isFinal()) {
            this.finalFieldDefined = true;
         }
      }

   }

   public boolean anyFinalFieldDefined() {
      return this.finalFieldDefined;
   }
}
