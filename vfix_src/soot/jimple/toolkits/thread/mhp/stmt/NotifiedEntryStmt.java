package soot.jimple.toolkits.thread.mhp.stmt;

import soot.SootMethod;

public class NotifiedEntryStmt extends JPegStmt {
   public NotifiedEntryStmt(String obj, String ca, SootMethod sm) {
      this.object = obj;
      this.name = "notified-entry";
      this.caller = ca;
      this.sootMethod = sm;
   }
}
