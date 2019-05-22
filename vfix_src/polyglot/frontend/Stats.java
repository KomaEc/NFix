package polyglot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import polyglot.main.Report;

public class Stats {
   protected ExtensionInfo ext;
   protected Map passTimes = new HashMap();
   protected List keys = new ArrayList(20);

   public Stats(ExtensionInfo ext) {
      this.ext = ext;
   }

   public void resetPassTimes(Object key) {
      this.passTimes.remove(key);
   }

   public long passTime(Object key, boolean inclusive) {
      Stats.Times t = (Stats.Times)this.passTimes.get(key);
      if (t == null) {
         return 0L;
      } else {
         return inclusive ? t.inclusive : t.exclusive;
      }
   }

   public void accumPassTimes(Object key, long in, long ex) {
      Stats.Times t = (Stats.Times)this.passTimes.get(key);
      if (t == null) {
         this.keys.add(key);
         t = new Stats.Times();
         this.passTimes.put(key, t);
      }

      t.inclusive += in;
      t.exclusive += ex;
   }

   public void report() {
      if (Report.should_report((String)"time", 1)) {
         Report.report(1, "\nStatistics for " + this.ext.compilerName() + " (" + this.ext.getClass().getName() + ")");
         Report.report(1, "Pass Inclusive Exclusive");
         Report.report(1, "---- --------- ---------");
         Iterator i = this.keys.iterator();

         while(i.hasNext()) {
            Object key = i.next();
            Stats.Times t = (Stats.Times)this.passTimes.get(key);
            Report.report(1, key.toString() + " " + t.inclusive + " " + t.exclusive);
         }
      }

   }

   protected static class Times {
      long inclusive;
      long exclusive;
   }
}
