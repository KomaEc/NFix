package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;
import soot.Unit;
import soot.tagkit.AbstractHost;
import soot.toolkits.graph.UnitGraph;

public abstract class JPegStmt extends AbstractHost {
   protected String object;
   protected String name;
   protected String caller;
   protected Unit unit = null;
   protected UnitGraph unitGraph = null;
   protected SootMethod sootMethod = null;

   protected JPegStmt() {
   }

   protected JPegStmt(String obj, String na, String ca) {
      this.object = obj;
      this.name = na;
      this.caller = ca;
   }

   protected JPegStmt(String obj, String na, String ca, SootMethod sm) {
      this.object = obj;
      this.name = na;
      this.caller = ca;
      this.sootMethod = sm;
   }

   protected JPegStmt(String obj, String na, String ca, UnitGraph ug, SootMethod sm) {
      this.object = obj;
      this.name = na;
      this.caller = ca;
      this.unitGraph = ug;
      this.sootMethod = sm;
   }

   protected JPegStmt(String obj, String na, String ca, Unit un, UnitGraph ug, SootMethod sm) {
      this.object = obj;
      this.name = na;
      this.caller = ca;
      this.unit = un;
      this.unitGraph = ug;
      this.sootMethod = sm;
   }

   protected void setUnit(Unit un) {
      this.unit = un;
   }

   protected void setUnitGraph(UnitGraph ug) {
      this.unitGraph = ug;
   }

   public UnitGraph getUnitGraph() {
      if (!this.containUnitGraph()) {
         throw new RuntimeException("This statement does not contain UnitGraph!");
      } else {
         return this.unitGraph;
      }
   }

   public boolean containUnitGraph() {
      return this.unitGraph != null;
   }

   public Unit getUnit() {
      if (!this.containUnit()) {
         throw new RuntimeException("This statement does not contain Unit!");
      } else {
         return this.unit;
      }
   }

   public boolean containUnit() {
      return this.unit != null;
   }

   public String getObject() {
      return this.object;
   }

   protected void setObject(String ob) {
      this.object = ob;
   }

   public String getName() {
      return this.name;
   }

   protected void setName(String na) {
      this.name = na;
   }

   public String getCaller() {
      return this.caller;
   }

   protected void setCaller(String ca) {
      this.caller = ca;
   }

   public SootMethod getMethod() {
      return this.sootMethod;
   }

   public String toString() {
      return this.sootMethod != null ? "(" + this.getObject() + ", " + this.getName() + ", " + this.getCaller() + "," + this.sootMethod + ")" : "(" + this.getObject() + ", " + this.getName() + ", " + this.getCaller() + ")";
   }

   public String testToString() {
      if (this.containUnit()) {
         return this.sootMethod != null ? "(" + this.getObject() + ", " + this.getName() + ", " + this.getCaller() + ", " + this.getUnit() + "," + this.sootMethod + ")" : "(" + this.getObject() + ", " + this.getName() + ", " + this.getCaller() + ", " + this.getUnit() + ")";
      } else {
         return "(" + this.getObject() + ", " + this.getName() + ", " + this.getCaller() + ")";
      }
   }
}
