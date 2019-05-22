package org.apache.maven.plugin.surefire.runorder;

public class Priority {
   private final String className;
   int priority;
   int totalRuntime = 0;
   int minSuccessRate = Integer.MAX_VALUE;

   public Priority(String className) {
      this.className = className;
   }

   public static Priority newTestClassPriority(String className) {
      Priority priority1 = new Priority(className);
      priority1.setPriority(0);
      priority1.minSuccessRate = 0;
      return priority1;
   }

   public void addItem(RunEntryStatistics itemStat) {
      this.totalRuntime += itemStat.getRunTime();
      this.minSuccessRate = Math.min(this.minSuccessRate, itemStat.getSuccessfulBuilds());
   }

   public int getTotalRuntime() {
      return this.totalRuntime;
   }

   public int getMinSuccessRate() {
      return this.minSuccessRate;
   }

   public String getClassName() {
      return this.className;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }
}
