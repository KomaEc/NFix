package soot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhaseOptions {
   private static final Logger logger = LoggerFactory.getLogger(PhaseOptions.class);
   private PackManager pm;
   private final Map<HasPhaseOptions, Map<String, String>> phaseToOptionMap = new HashMap();

   public void setPackManager(PackManager m) {
      this.pm = m;
   }

   PackManager getPM() {
      if (this.pm == null) {
         PackManager.v();
      }

      return this.pm;
   }

   public PhaseOptions(Singletons.Global g) {
   }

   public static PhaseOptions v() {
      return G.v().soot_PhaseOptions();
   }

   public Map<String, String> getPhaseOptions(String phaseName) {
      return this.getPhaseOptions(this.getPM().getPhase(phaseName));
   }

   public Map<String, String> getPhaseOptions(HasPhaseOptions phase) {
      Map<String, String> ret = (Map)this.phaseToOptionMap.get(phase);
      HashMap ret;
      if (ret == null) {
         ret = new HashMap();
      } else {
         ret = new HashMap(ret);
      }

      StringTokenizer st = new StringTokenizer(phase.getDefaultOptions());

      while(st.hasMoreTokens()) {
         String opt = st.nextToken();
         String key = this.getKey(opt);
         String value = this.getValue(opt);
         if (!ret.containsKey(key)) {
            ret.put(key, value);
         }
      }

      return Collections.unmodifiableMap(ret);
   }

   public boolean processPhaseOptions(String phaseName, String option) {
      StringTokenizer st = new StringTokenizer(option, ",");

      do {
         if (!st.hasMoreTokens()) {
            return true;
         }
      } while(this.setPhaseOption(phaseName, st.nextToken()));

      return false;
   }

   public static boolean getBoolean(Map<String, String> options, String name) {
      String val = (String)options.get(name);
      return val != null && val.equals("true");
   }

   public static boolean getBoolean(Map<String, String> options, String name, boolean defaultValue) {
      String val = (String)options.get(name);
      return val == null ? defaultValue : val.equals("true");
   }

   public static String getString(Map<String, String> options, String name) {
      String val = (String)options.get(name);
      return val != null ? val : "";
   }

   public static float getFloat(Map<String, String> options, String name) {
      return options.containsKey(name) ? new Float((String)options.get(name)) : 1.0F;
   }

   public static int getInt(Map<String, String> options, String name) {
      return options.containsKey(name) ? new Integer((String)options.get(name)) : 0;
   }

   private Map<String, String> mapForPhase(String phaseName) {
      HasPhaseOptions phase = this.getPM().getPhase(phaseName);
      return phase == null ? null : this.mapForPhase(phase);
   }

   private Map<String, String> mapForPhase(HasPhaseOptions phase) {
      Map<String, String> optionMap = (Map)this.phaseToOptionMap.get(phase);
      if (optionMap == null) {
         this.phaseToOptionMap.put(phase, optionMap = new HashMap());
      }

      return (Map)optionMap;
   }

   private String getKey(String option) {
      int delimLoc = option.indexOf(":");
      if (delimLoc < 0) {
         return !option.equals("on") && !option.equals("off") ? option : "enabled";
      } else {
         return option.substring(0, delimLoc);
      }
   }

   private String getValue(String option) {
      int delimLoc = option.indexOf(":");
      if (delimLoc < 0) {
         return option.equals("off") ? "false" : "true";
      } else {
         return option.substring(delimLoc + 1);
      }
   }

   private void resetRadioPack(String phaseName) {
      Iterator var2 = this.getPM().allPacks().iterator();

      while(true) {
         Pack p;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               p = (Pack)var2.next();
            } while(!(p instanceof RadioScenePack));
         } while(p.get(phaseName) == null);

         Iterator tIt = p.iterator();

         while(tIt.hasNext()) {
            Transform t = (Transform)tIt.next();
            this.setPhaseOption(t.getPhaseName(), "enabled:false");
         }
      }
   }

   private boolean checkParentEnabled(String phaseName) {
      return true;
   }

   public boolean setPhaseOption(String phaseName, String option) {
      HasPhaseOptions phase = this.getPM().getPhase(phaseName);
      if (phase == null) {
         logger.debug("Option " + option + " given for nonexistent phase " + phaseName);
         return false;
      } else {
         return this.setPhaseOption(phase, option);
      }
   }

   public boolean setPhaseOption(HasPhaseOptions phase, String option) {
      Map<String, String> optionMap = this.mapForPhase(phase);
      if (!this.checkParentEnabled(phase.getPhaseName())) {
         return false;
      } else if (optionMap == null) {
         logger.debug("Option " + option + " given for nonexistent phase " + phase.getPhaseName());
         return false;
      } else {
         String key = this.getKey(option);
         if (key.equals("enabled") && this.getValue(option).equals("true")) {
            this.resetRadioPack(phase.getPhaseName());
         }

         if (this.declaresOption(phase, key)) {
            optionMap.put(key, this.getValue(option));
            return true;
         } else {
            logger.debug("Invalid option " + option + " for phase " + phase.getPhaseName());
            return false;
         }
      }
   }

   private boolean declaresOption(String phaseName, String option) {
      HasPhaseOptions phase = this.getPM().getPhase(phaseName);
      return this.declaresOption(phase, option);
   }

   private boolean declaresOption(HasPhaseOptions phase, String option) {
      String declareds = phase.getDeclaredOptions();
      StringTokenizer st = new StringTokenizer(declareds);

      do {
         if (!st.hasMoreTokens()) {
            return false;
         }
      } while(!st.nextToken().equals(option));

      return true;
   }

   public void setPhaseOptionIfUnset(String phaseName, String option) {
      Map<String, String> optionMap = this.mapForPhase(phaseName);
      if (optionMap == null) {
         throw new RuntimeException("No such phase " + phaseName);
      } else if (!optionMap.containsKey(this.getKey(option))) {
         if (!this.declaresOption(phaseName, this.getKey(option))) {
            throw new RuntimeException("No option " + option + " for phase " + phaseName);
         } else {
            optionMap.put(this.getKey(option), this.getValue(option));
         }
      }
   }
}
