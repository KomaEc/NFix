package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.UnitGraph;

public class MonitorEntryStmt extends JPegStmt {
   public MonitorEntryStmt(String obj, String ca, Unit un, UnitGraph ug, SootMethod sm) {
      this.object = obj;
      this.name = "entry";
      this.caller = ca;
      this.unit = un;
      this.unitGraph = ug;
   }

   public MonitorEntryStmt(String obj, String ca, UnitGraph ug, SootMethod sm) {
      this.object = obj;
      this.name = "entry";
      this.caller = ca;
      this.unitGraph = ug;
      this.sootMethod = sm;
   }
}
