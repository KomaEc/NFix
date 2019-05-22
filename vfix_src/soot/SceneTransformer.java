package soot;

import java.util.HashMap;
import java.util.Map;

public abstract class SceneTransformer extends Transformer {
   public final void transform(String phaseName, Map<String, String> options) {
      if (PhaseOptions.getBoolean(options, "enabled")) {
         this.internalTransform(phaseName, options);
      }
   }

   public final void transform(String phaseName) {
      HashMap<String, String> dummyOptions = new HashMap();
      dummyOptions.put("enabled", "true");
      this.transform(phaseName, dummyOptions);
   }

   public final void transform() {
      this.transform("");
   }

   protected abstract void internalTransform(String var1, Map<String, String> var2);
}
