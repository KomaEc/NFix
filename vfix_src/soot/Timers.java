package soot;

import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;

public class Timers {
   private static final Logger logger = LoggerFactory.getLogger(Timers.class);
   public int totalFlowNodes;
   public int totalFlowComputations;
   public Timer copiesTimer = new Timer("copies");
   public Timer defsTimer = new Timer("defs");
   public Timer usesTimer = new Timer("uses");
   public Timer liveTimer = new Timer("live");
   public Timer splitTimer = new Timer("split");
   public Timer packTimer = new Timer("pack");
   public Timer cleanup1Timer = new Timer("cleanup1");
   public Timer cleanup2Timer = new Timer("cleanup2");
   public Timer conversionTimer = new Timer("conversion");
   public Timer cleanupAlgorithmTimer = new Timer("cleanupAlgorithm");
   public Timer graphTimer = new Timer("graphTimer");
   public Timer assignTimer = new Timer("assignTimer");
   public Timer resolveTimer = new Timer("resolveTimer");
   public Timer totalTimer = new Timer("totalTimer");
   public Timer splitPhase1Timer = new Timer("splitPhase1");
   public Timer splitPhase2Timer = new Timer("splitPhase2");
   public Timer usePhase1Timer = new Timer("usePhase1");
   public Timer usePhase2Timer = new Timer("usePhase2");
   public Timer usePhase3Timer = new Timer("usePhase3");
   public Timer defsSetupTimer = new Timer("defsSetup");
   public Timer defsAnalysisTimer = new Timer("defsAnalysis");
   public Timer defsPostTimer = new Timer("defsPost");
   public Timer liveSetupTimer = new Timer("liveSetup");
   public Timer liveAnalysisTimer = new Timer("liveAnalysis");
   public Timer livePostTimer = new Timer("livePost");
   public Timer aggregationTimer = new Timer("aggregation");
   public Timer grimpAggregationTimer = new Timer("grimpAggregation");
   public Timer deadCodeTimer = new Timer("deadCode");
   public Timer propagatorTimer = new Timer("propagator");
   public Timer buildJasminTimer = new Timer("buildjasmin");
   public Timer assembleJasminTimer = new Timer("assembling jasmin");
   public Timer resolverTimer = new Timer("resolver");
   public int conversionLocalCount;
   public int cleanup1LocalCount;
   public int splitLocalCount;
   public int assignLocalCount;
   public int packLocalCount;
   public int cleanup2LocalCount;
   public int conversionStmtCount;
   public int cleanup1StmtCount;
   public int splitStmtCount;
   public int assignStmtCount;
   public int packStmtCount;
   public int cleanup2StmtCount;
   public long stmtCount;
   public Timer fieldTimer = new Timer();
   public Timer methodTimer = new Timer();
   public Timer attributeTimer = new Timer();
   public Timer locatorTimer = new Timer();
   public Timer readTimer = new Timer();
   public Timer orderComputation = new Timer("orderComputation");

   public Timers(Singletons.Global g) {
   }

   public static Timers v() {
      return G.v().soot_Timers();
   }

   public void printProfilingInformation() {
      long totalTime = this.totalTimer.getTime();
      logger.debug("Time measurements");
      logger.debug("      Building graphs: " + this.toTimeString(this.graphTimer, totalTime));
      logger.debug("  Computing LocalDefs: " + this.toTimeString(this.defsTimer, totalTime));
      logger.debug("  Computing LocalUses: " + this.toTimeString(this.usesTimer, totalTime));
      logger.debug("     Cleaning up code: " + this.toTimeString(this.cleanupAlgorithmTimer, totalTime));
      logger.debug("Computing LocalCopies: " + this.toTimeString(this.copiesTimer, totalTime));
      logger.debug(" Computing LiveLocals: " + this.toTimeString(this.liveTimer, totalTime));
      logger.debug("Coading coffi structs: " + this.toTimeString(this.resolveTimer, totalTime));
      logger.debug("       Resolving classfiles: " + this.toTimeString(this.resolverTimer, totalTime));
      logger.debug(" Bytecode -> jimple (naive): " + this.toTimeString(this.conversionTimer, totalTime));
      logger.debug("        Splitting variables: " + this.toTimeString(this.splitTimer, totalTime));
      logger.debug("            Assigning types: " + this.toTimeString(this.assignTimer, totalTime));
      logger.debug("  Propagating copies & csts: " + this.toTimeString(this.propagatorTimer, totalTime));
      logger.debug("      Eliminating dead code: " + this.toTimeString(this.deadCodeTimer, totalTime));
      logger.debug("                Aggregation: " + this.toTimeString(this.aggregationTimer, totalTime));
      logger.debug("            Coloring locals: " + this.toTimeString(this.packTimer, totalTime));
      logger.debug("     Generating jasmin code: " + this.toTimeString(this.buildJasminTimer, totalTime));
      logger.debug("          .jasmin -> .class: " + this.toTimeString(this.assembleJasminTimer, totalTime));
      float timeInSecs = (float)totalTime / 1000.0F;
      logger.debug("totalTime:" + this.toTimeString(this.totalTimer, totalTime));
      if (Options.v().subtract_gc()) {
         logger.debug("Garbage collection was subtracted from these numbers.");
         logger.debug("           forcedGC:" + this.toTimeString(G.v().Timer_forcedGarbageCollectionTimer, totalTime));
      }

      logger.debug("stmtCount: " + this.stmtCount + "(" + this.toFormattedString((double)((float)this.stmtCount / timeInSecs)) + " stmt/s)");
      logger.debug("totalFlowNodes: " + this.totalFlowNodes + " totalFlowComputations: " + this.totalFlowComputations + " avg: " + this.truncatedOf((double)this.totalFlowComputations / (double)this.totalFlowNodes, 2));
   }

   private String toTimeString(Timer timer, long totalTime) {
      DecimalFormat format = new DecimalFormat("00.0");
      DecimalFormat percFormat = new DecimalFormat("00.0");
      long time = timer.getTime();
      String timeString = format.format((double)time / 1000.0D);
      return timeString + "s (" + percFormat.format((double)time * 100.0D / (double)totalTime) + "%)";
   }

   private String toFormattedString(double value) {
      return this.paddedLeftOf((new Double(this.truncatedOf(value, 2))).toString(), 5);
   }

   public double truncatedOf(double d, int numDigits) {
      double multiplier = 1.0D;

      for(int i = 0; i < numDigits; ++i) {
         multiplier *= 10.0D;
      }

      return (double)((long)(d * multiplier)) / multiplier;
   }

   public String paddedLeftOf(String s, int length) {
      if (s.length() >= length) {
         return s;
      } else {
         int diff = length - s.length();
         char[] padding = new char[diff];

         for(int i = 0; i < diff; ++i) {
            padding[i] = ' ';
         }

         return new String(padding) + s;
      }
   }
}
