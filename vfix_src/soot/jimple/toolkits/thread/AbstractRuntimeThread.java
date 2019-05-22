package soot.jimple.toolkits.thread;

import java.util.ArrayList;
import java.util.List;
import soot.SootMethod;
import soot.jimple.Stmt;

public class AbstractRuntimeThread {
   Stmt startStmt = null;
   SootMethod startStmtMethod = null;
   Stmt joinStmt;
   List<Object> methods = new ArrayList();
   List<Object> runMethods = new ArrayList();
   boolean runsMany = false;
   boolean runsOnce = false;
   boolean runsOneAtATime = false;
   boolean startStmtHasMultipleReachingObjects = false;
   boolean startStmtMayBeRunMultipleTimes = false;
   boolean startMethodIsReentrant = false;
   boolean startMethodMayHappenInParallel = false;
   boolean isMainThread = false;

   public void setStartStmt(Stmt startStmt) {
      this.startStmt = startStmt;
   }

   public void setJoinStmt(Stmt joinStmt) {
      this.joinStmt = joinStmt;
   }

   public void setStartStmtMethod(SootMethod startStmtMethod) {
      this.startStmtMethod = startStmtMethod;
   }

   public SootMethod getStartStmtMethod() {
      return this.startStmtMethod;
   }

   public boolean containsMethod(Object method) {
      return this.methods.contains(method);
   }

   public void addMethod(Object method) {
      this.methods.add(method);
   }

   public void addRunMethod(Object method) {
      this.runMethods.add(method);
   }

   public List<Object> getRunMethods() {
      return this.runMethods;
   }

   public int methodCount() {
      return this.methods.size();
   }

   public Object getMethod(int methodNum) {
      return this.methods.get(methodNum);
   }

   public void setStartStmtHasMultipleReachingObjects() {
      this.startStmtHasMultipleReachingObjects = true;
   }

   public void setStartStmtMayBeRunMultipleTimes() {
      this.startStmtMayBeRunMultipleTimes = true;
   }

   public void setStartMethodIsReentrant() {
      this.startMethodIsReentrant = true;
   }

   public void setStartMethodMayHappenInParallel() {
      this.startMethodMayHappenInParallel = true;
   }

   public void setRunsMany() {
      this.runsMany = true;
      this.runsOnce = false;
      this.runsOneAtATime = false;
   }

   public void setRunsOnce() {
      this.runsMany = false;
      this.runsOnce = true;
      this.runsOneAtATime = false;
   }

   public void setRunsOneAtATime() {
      this.runsMany = false;
      this.runsOnce = false;
      this.runsOneAtATime = true;
   }

   public void setIsMainThread() {
      this.isMainThread = true;
   }

   public String toString() {
      String ret = (this.isMainThread ? "Main Thread" : "User Thread") + " (" + (this.runsMany ? "Multi,  " : (this.runsOnce ? "Single, " : (this.runsOneAtATime ? "At-Once," : "ERROR")));
      if (this.startStmtHasMultipleReachingObjects) {
         ret = ret + "MRO,";
         if (this.startMethodIsReentrant) {
            ret = ret + "SMR";
         } else if (this.startMethodMayHappenInParallel) {
            ret = ret + "MSP";
         } else if (this.startStmtMayBeRunMultipleTimes) {
            ret = ret + "RMT";
         } else {
            ret = ret + "ROT";
         }
      } else if (this.isMainThread) {
         ret = ret + "---,---";
      } else {
         ret = ret + "SRO,---";
      }

      ret = ret + "): ";
      if (!this.isMainThread) {
         ret = ret + "Started in " + this.startStmtMethod + " by " + this.startStmt + "\n";
      } else {
         ret = ret + "\n";
      }

      if (this.joinStmt != null) {
         ret = ret + "                               Joined  in " + this.startStmtMethod + " by " + this.joinStmt + "\n";
      }

      ret = ret + this.methods.toString();
      return ret;
   }
}
