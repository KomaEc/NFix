package soot;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.util.PhaseDumper;

public class Transform implements HasPhaseOptions {
   private static final Logger logger = LoggerFactory.getLogger(Transform.class);
   private final boolean DEBUG;
   final String phaseName;
   final Transformer t;
   private String declaredOpts;
   private String defaultOpts;

   public Transform(String phaseName, Transformer t) {
      this.DEBUG = Options.v().dump_body().contains(phaseName);
      this.phaseName = phaseName;
      this.t = t;
   }

   public String getPhaseName() {
      return this.phaseName;
   }

   public Transformer getTransformer() {
      return this.t;
   }

   public String getDeclaredOptions() {
      return this.declaredOpts != null ? this.declaredOpts : Options.getDeclaredOptionsForPhase(this.phaseName);
   }

   public String getDefaultOptions() {
      return this.defaultOpts != null ? this.defaultOpts : Options.getDefaultOptionsForPhase(this.phaseName);
   }

   public void setDeclaredOptions(String options) {
      this.declaredOpts = options;
   }

   public void setDefaultOptions(String options) {
      this.defaultOpts = options;
   }

   public void apply() {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions(this.phaseName);
      if (PhaseOptions.getBoolean(options, "enabled") && Options.v().verbose()) {
         logger.debug("Applying phase " + this.phaseName + " to the scene.");
      }

      if (this.DEBUG) {
         PhaseDumper.v().dumpBefore(this.getPhaseName());
      }

      ((SceneTransformer)this.t).transform(this.phaseName, options);
      if (this.DEBUG) {
         PhaseDumper.v().dumpAfter(this.getPhaseName());
      }

   }

   public void apply(Body b) {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions(this.phaseName);
      if (PhaseOptions.getBoolean(options, "enabled") && Options.v().verbose()) {
         logger.debug("Applying phase " + this.phaseName + " to " + b.getMethod() + ".");
      }

      if (this.DEBUG) {
         PhaseDumper.v().dumpBefore(b, this.getPhaseName());
      }

      ((BodyTransformer)this.t).transform(b, this.phaseName, options);
      if (this.DEBUG) {
         PhaseDumper.v().dumpAfter(b, this.getPhaseName());
      }

   }

   public String toString() {
      return this.phaseName;
   }
}
