package corg.vfix.sa;

import corg.vfix.Main;
import corg.vfix.fl.spectrum.FLSpectrum;
import corg.vfix.sa.plot.VFGPlot;
import corg.vfix.sa.vfg.VFG;
import corg.vfix.sa.vfg.VFGNode;
import corg.vfix.sa.vfg.build.NPELocator;
import corg.vfix.sa.vfg.build.VFGConstructor;
import corg.vfix.sa.vfg.cgst.CongestionCalculator;
import java.util.ArrayList;

public class SAMain {
   public static ArrayList<VFGNode> nodes;
   public static VFG vfg;

   public static void main() throws Exception {
      SootRunner.main();
      NPELocator.locateNPE();
      vfg = VFGConstructor.build();
      CongestionCalculator.main(vfg);
      nodes = CongestionCalculator.getSortedRepairLocations();
      VFGPlot.plot(vfg);
      setLocationStat();
   }

   private static void setLocationStat() {
      Main.bug.numOfVfgNodes = vfg.getNodes().size();
      Main.bug.numOfVfgExecuted = vfg.getExecutedNodes().size();
      Main.bug.numOfTotalExecuted = FLSpectrum.getTotalNumOfExecuted();
   }
}
