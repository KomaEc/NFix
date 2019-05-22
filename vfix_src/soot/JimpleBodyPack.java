package soot;

import java.util.Map;
import soot.jimple.JimpleBody;
import soot.options.JBOptions;
import soot.options.Options;

public class JimpleBodyPack extends BodyPack {
   public JimpleBodyPack() {
      super("jb");
   }

   private void applyPhaseOptions(JimpleBody b, Map<String, String> opts) {
      JBOptions options = new JBOptions(opts);
      if (options.use_original_names()) {
         PhaseOptions.v().setPhaseOptionIfUnset("jb.lns", "only-stack-locals");
      }

      if (Options.v().time()) {
         Timers.v().splitTimer.start();
      }

      PackManager.v().getTransform("jb.tt").apply(b);
      PackManager.v().getTransform("jb.dtr").apply(b);
      PackManager.v().getTransform("jb.uce").apply(b);
      PackManager.v().getTransform("jb.ls").apply(b);
      if (Options.v().time()) {
         Timers.v().splitTimer.end();
      }

      PackManager.v().getTransform("jb.a").apply(b);
      PackManager.v().getTransform("jb.ule").apply(b);
      if (Options.v().time()) {
         Timers.v().assignTimer.start();
      }

      PackManager.v().getTransform("jb.tr").apply(b);
      if (Options.v().time()) {
         Timers.v().assignTimer.end();
      }

      if (options.use_original_names()) {
         PackManager.v().getTransform("jb.ulp").apply(b);
      }

      PackManager.v().getTransform("jb.lns").apply(b);
      PackManager.v().getTransform("jb.cp").apply(b);
      PackManager.v().getTransform("jb.dae").apply(b);
      PackManager.v().getTransform("jb.cp-ule").apply(b);
      PackManager.v().getTransform("jb.lp").apply(b);
      PackManager.v().getTransform("jb.ne").apply(b);
      PackManager.v().getTransform("jb.uce").apply(b);
      if (PhaseOptions.getBoolean(opts, "stabilize-local-names")) {
         PhaseOptions.v().setPhaseOption("jb.lns", "sort-locals:true");
         PackManager.v().getTransform("jb.lns").apply(b);
      }

      if (Options.v().time()) {
         Timers var10000 = Timers.v();
         var10000.stmtCount += (long)b.getUnits().size();
      }

   }

   protected void internalApply(Body b) {
      this.applyPhaseOptions((JimpleBody)b, PhaseOptions.v().getPhaseOptions(this.getPhaseName()));
   }
}
