package com.gzoltar.shaded.org.pitest.util;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Timings {
   private final Map<Timings.Stage, TimeSpan> timings = new LinkedHashMap();

   public void registerStart(Timings.Stage stage) {
      this.timings.put(stage, new TimeSpan(System.currentTimeMillis(), 0L));
   }

   public void registerEnd(Timings.Stage stage) {
      long end = System.currentTimeMillis();
      ((TimeSpan)this.timings.get(stage)).setEnd(end);
   }

   public void report(PrintStream ps) {
      long total = 0L;
      Iterator i$ = this.timings.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Timings.Stage, TimeSpan> each = (Entry)i$.next();
         total += ((TimeSpan)each.getValue()).duration();
         ps.println("> " + each.getKey() + " : " + each.getValue());
      }

      ps.println(StringUtil.separatorLine());
      ps.println("> Total  : " + new TimeSpan(0L, total));
      ps.println(StringUtil.separatorLine());
   }

   public static enum Stage {
      BUILD_MUTATION_TESTS("build mutation tests"),
      RUN_MUTATION_TESTS("run mutation analysis"),
      SCAN_CLASS_PATH("scan classpath"),
      COVERAGE("coverage and dependency analysis");

      private final String description;

      private Stage(String desc) {
         this.description = desc;
      }

      public String toString() {
         return this.description;
      }
   }
}
