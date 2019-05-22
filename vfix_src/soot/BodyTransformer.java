package soot;

import java.util.Collections;
import java.util.Map;

public abstract class BodyTransformer extends Transformer {
   private Map<String, String> enabledOnlyMap = Collections.singletonMap("enabled", "true");

   public final void transform(Body b, String phaseName, Map<String, String> options) {
      if (PhaseOptions.getBoolean(options, "enabled")) {
         this.internalTransform(b, phaseName, options);
      }
   }

   public final void transform(Body b, String phaseName) {
      this.internalTransform(b, phaseName, this.enabledOnlyMap);
   }

   public final void transform(Body b) {
      this.transform(b, "");
   }

   protected abstract void internalTransform(Body var1, String var2, Map<String, String> var3);
}
