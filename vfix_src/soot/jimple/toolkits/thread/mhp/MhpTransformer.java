package soot.jimple.toolkits.thread.mhp;

import java.util.Map;
import soot.G;
import soot.SceneTransformer;
import soot.Singletons;

public class MhpTransformer extends SceneTransformer {
   MhpTester mhpTester;

   public MhpTransformer(Singletons.Global g) {
   }

   public static MhpTransformer v() {
      return G.v().soot_jimple_toolkits_thread_mhp_MhpTransformer();
   }

   protected void internalTransform(String phaseName, Map options) {
      this.getMhpTester().printMhpSummary();
   }

   public MhpTester getMhpTester() {
      if (this.mhpTester == null) {
         this.mhpTester = new SynchObliviousMhpAnalysis();
      }

      return this.mhpTester;
   }

   public void setMhpTester(MhpTester mhpTester) {
      this.mhpTester = mhpTester;
   }
}
