package soot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RadioScenePack extends ScenePack {
   private static final Logger logger = LoggerFactory.getLogger(RadioScenePack.class);

   public RadioScenePack(String name) {
      super(name);
   }

   protected void internalApply() {
      LinkedList<Transform> enableds = new LinkedList();
      Iterator tIt = this.iterator();

      Transform t;
      while(tIt.hasNext()) {
         t = (Transform)tIt.next();
         Map<String, String> opts = PhaseOptions.v().getPhaseOptions((HasPhaseOptions)t);
         if (PhaseOptions.getBoolean(opts, "enabled")) {
            enableds.add(t);
         }
      }

      if (enableds.size() == 0) {
         logger.debug("Exactly one phase in the pack " + this.getPhaseName() + " must be enabled. Currently, none of them are.");
         throw new CompilationDeathException(0);
      } else if (enableds.size() > 1) {
         logger.debug("Only one phase in the pack " + this.getPhaseName() + " may be enabled. The following are enabled currently: ");
         tIt = enableds.iterator();

         while(tIt.hasNext()) {
            t = (Transform)tIt.next();
            logger.debug("  " + t.getPhaseName());
         }

         throw new CompilationDeathException(0);
      } else {
         tIt = enableds.iterator();

         while(tIt.hasNext()) {
            t = (Transform)tIt.next();
            t.apply();
         }

      }
   }

   public void add(Transform t) {
      super.add(t);
      this.checkEnabled(t);
   }

   public void insertAfter(Transform t, String phaseName) {
      super.insertAfter(t, phaseName);
      this.checkEnabled(t);
   }

   public void insertBefore(Transform t, String phaseName) {
      super.insertBefore(t, phaseName);
      this.checkEnabled(t);
   }

   private void checkEnabled(Transform t) {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions((HasPhaseOptions)t);
      if (PhaseOptions.getBoolean(options, "enabled")) {
         PhaseOptions.v().setPhaseOption((HasPhaseOptions)t, "enabled:true");
      }

   }
}
