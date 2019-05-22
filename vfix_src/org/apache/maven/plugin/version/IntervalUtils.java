package org.apache.maven.plugin.version;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IntervalUtils {
   private static final String PERIOD_PART_PATTERN = "[0-9]+[WwDdHhMm]?";
   private static final Map PART_TYPE_CONTRIBUTIONS;

   private IntervalUtils() {
   }

   public static boolean isExpired(String intervalSpec, Date lastChecked) {
      if ("never".equalsIgnoreCase(intervalSpec)) {
         return false;
      } else if ("always".equalsIgnoreCase(intervalSpec)) {
         return true;
      } else if (intervalSpec != null && intervalSpec.toLowerCase().startsWith("interval:") && intervalSpec.length() > "interval:".length()) {
         String intervalPart = intervalSpec.substring("interval:".length());
         long period = parseInterval(intervalPart);
         Calendar cal = Calendar.getInstance();
         cal.setTimeInMillis(System.currentTimeMillis() - period);
         Date test = cal.getTime();
         return lastChecked == null || test.after(lastChecked);
      } else {
         throw new IllegalArgumentException("Invalid interval specification: '" + intervalSpec + "'");
      }
   }

   public static long parseInterval(String interval) {
      Matcher partMatcher = Pattern.compile("[0-9]+[WwDdHhMm]?").matcher(interval);

      long period;
      String part;
      for(period = 0L; partMatcher.find(); period += getPartPeriod(part)) {
         part = partMatcher.group();
      }

      return period;
   }

   private static long getPartPeriod(String part) {
      char type = part.charAt(part.length() - 1);
      String coefficientPart;
      if (Character.isLetter(type)) {
         coefficientPart = part.substring(0, part.length() - 1);
      } else {
         coefficientPart = part;
         type = 'm';
      }

      int coefficient = Integer.parseInt(coefficientPart);
      Long period = (Long)PART_TYPE_CONTRIBUTIONS.get("" + Character.toLowerCase(type));
      long result = 0L;
      if (period != null) {
         result = (long)coefficient * period;
      }

      return result;
   }

   static {
      Map contributions = new HashMap();
      contributions.put("w", new Long(604800000L));
      contributions.put("d", new Long(86400000L));
      contributions.put("h", new Long(3600000L));
      contributions.put("m", new Long(60000L));
      PART_TYPE_CONTRIBUTIONS = contributions;
   }
}
