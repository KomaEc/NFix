package org.apache.tools.ant.taskdefs;

import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.apache.tools.ant.types.EnumeratedAttribute;

public class WaitFor extends ConditionBase {
   private long maxWaitMillis = 180000L;
   private long maxWaitMultiplier = 1L;
   private long checkEveryMillis = 500L;
   private long checkEveryMultiplier = 1L;
   private String timeoutProperty;

   public WaitFor() {
      super("waitfor");
   }

   public void setMaxWait(long time) {
      this.maxWaitMillis = time;
   }

   public void setMaxWaitUnit(WaitFor.Unit unit) {
      this.maxWaitMultiplier = unit.getMultiplier();
   }

   public void setCheckEvery(long time) {
      this.checkEveryMillis = time;
   }

   public void setCheckEveryUnit(WaitFor.Unit unit) {
      this.checkEveryMultiplier = unit.getMultiplier();
   }

   public void setTimeoutProperty(String p) {
      this.timeoutProperty = p;
   }

   public void execute() throws BuildException {
      if (this.countConditions() > 1) {
         throw new BuildException("You must not nest more than one condition into " + this.getTaskName());
      } else if (this.countConditions() < 1) {
         throw new BuildException("You must nest a condition into " + this.getTaskName());
      } else {
         Condition c = (Condition)this.getConditions().nextElement();
         long savedMaxWaitMillis = this.maxWaitMillis;
         long savedCheckEveryMillis = this.checkEveryMillis;

         try {
            this.maxWaitMillis *= this.maxWaitMultiplier;
            this.checkEveryMillis *= this.checkEveryMultiplier;
            long start = System.currentTimeMillis();
            long end = start + this.maxWaitMillis;

            while(System.currentTimeMillis() < end) {
               if (c.eval()) {
                  this.processSuccess();
                  return;
               }

               try {
                  Thread.sleep(this.checkEveryMillis);
               } catch (InterruptedException var14) {
               }
            }

            this.processTimeout();
         } finally {
            this.maxWaitMillis = savedMaxWaitMillis;
            this.checkEveryMillis = savedCheckEveryMillis;
         }
      }
   }

   protected void processSuccess() {
      this.log(this.getTaskName() + ": condition was met", 3);
   }

   protected void processTimeout() {
      this.log(this.getTaskName() + ": timeout", 3);
      if (this.timeoutProperty != null) {
         this.getProject().setNewProperty(this.timeoutProperty, "true");
      }

   }

   public static class Unit extends EnumeratedAttribute {
      public static final String MILLISECOND = "millisecond";
      public static final String SECOND = "second";
      public static final String MINUTE = "minute";
      public static final String HOUR = "hour";
      public static final String DAY = "day";
      public static final String WEEK = "week";
      private static final String[] UNITS = new String[]{"millisecond", "second", "minute", "hour", "day", "week"};
      private Map timeTable = new HashMap();

      public Unit() {
         this.timeTable.put("millisecond", new Long(1L));
         this.timeTable.put("second", new Long(1000L));
         this.timeTable.put("minute", new Long(60000L));
         this.timeTable.put("hour", new Long(3600000L));
         this.timeTable.put("day", new Long(86400000L));
         this.timeTable.put("week", new Long(604800000L));
      }

      public long getMultiplier() {
         String key = this.getValue().toLowerCase();
         Long l = (Long)this.timeTable.get(key);
         return l;
      }

      public String[] getValues() {
         return UNITS;
      }
   }
}
