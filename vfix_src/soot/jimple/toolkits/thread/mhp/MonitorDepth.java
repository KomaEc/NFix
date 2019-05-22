package soot.jimple.toolkits.thread.mhp;

public class MonitorDepth {
   private String objName;
   private int depth;

   MonitorDepth(String s, int d) {
      this.objName = s;
      this.depth = d;
   }

   protected String getObjName() {
      return this.objName;
   }

   protected void SetObjName(String s) {
      this.objName = s;
   }

   protected int getDepth() {
      return this.depth;
   }

   protected void setDepth(int d) {
      this.depth = d;
   }

   protected void decreaseDepth() {
      if (this.depth > 0) {
         --this.depth;
      } else {
         throw new RuntimeException("Error! You can not decrease a monitor depth of " + this.depth);
      }
   }

   protected void increaseDepth() {
      ++this.depth;
   }
}
