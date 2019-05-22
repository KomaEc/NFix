package soot.jimple.toolkits.annotation.purity;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.PurityOptions;

public class PurityAnalysis extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(PurityAnalysis.class);
   Singletons.Global g;

   public PurityAnalysis(Singletons.Global g) {
      this.g = g;
   }

   public static PurityAnalysis v() {
      return G.v().soot_jimple_toolkits_annotation_purity_PurityAnalysis();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      PurityOptions opts = new PurityOptions(options);
      logger.debug("[AM] Analysing purity");
      CallGraph cg = Scene.v().getCallGraph();
      new PurityInterproceduralAnalysis(cg, Scene.v().getEntryPoints().iterator(), opts);
   }
}
