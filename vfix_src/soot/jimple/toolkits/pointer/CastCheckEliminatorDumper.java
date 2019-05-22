package soot.jimple.toolkits.pointer;

import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.toolkits.graph.BriefUnitGraph;

public class CastCheckEliminatorDumper extends BodyTransformer {
   public CastCheckEliminatorDumper(Singletons.Global g) {
   }

   public static CastCheckEliminatorDumper v() {
      return G.v().soot_jimple_toolkits_pointer_CastCheckEliminatorDumper();
   }

   public String getDefaultOptions() {
      return "";
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      new CastCheckEliminator(new BriefUnitGraph(b));
   }
}
