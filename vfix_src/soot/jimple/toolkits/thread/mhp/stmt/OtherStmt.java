package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public class OtherStmt extends JPegStmt {
   public OtherStmt(String obj, String na, String ca, Unit un, UnitGraph ug, SootMethod sm) {
      this.object = obj;
      this.name = na;
      this.caller = ca;
      this.unit = un;
      this.unitGraph = ug;
      this.sootMethod = sm;
   }
}
