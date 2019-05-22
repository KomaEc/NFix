package soot.jimple.spark.geom.dataRep;

import java.util.Set;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.scalar.Pair;
import soot.util.Numberable;

public class PlainConstraint implements Numberable {
   public int type;
   public Pair<IVarAbstraction, IVarAbstraction> expr = new Pair();
   public IVarAbstraction otherSide = null;
   public int code;
   public SparkField f = null;
   public Set<Edge> interCallEdges = null;
   public boolean isActive = true;
   private int id = -1;

   public void setNumber(int number) {
      this.id = number;
   }

   public int getNumber() {
      return this.id;
   }

   public IVarAbstraction getLHS() {
      return (IVarAbstraction)this.expr.getO1();
   }

   public void setLHS(IVarAbstraction newLHS) {
      this.expr.setO1(newLHS);
   }

   public IVarAbstraction getRHS() {
      return (IVarAbstraction)this.expr.getO2();
   }

   public void setRHS(IVarAbstraction newRHS) {
      this.expr.setO2(newRHS);
   }
}
