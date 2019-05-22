package soot;

import java.util.Map;
import soot.jimple.JimpleBody;
import soot.options.JJOptions;
import soot.options.Options;

public class JavaToJimpleBodyPack extends BodyPack {
   public JavaToJimpleBodyPack() {
      super("jj");
   }

   private void applyPhaseOptions(JimpleBody b, Map<String, String> opts) {
      JJOptions options = new JJOptions(opts);
      if (options.use_original_names()) {
         PhaseOptions.v().setPhaseOptionIfUnset("jj.lns", "only-stack-locals");
      }

      if (Options.v().time()) {
         Timers.v().splitTimer.start();
      }

      PackManager.v().getTransform("jj.ls").apply(b);
      if (Options.v().time()) {
         Timers.v().splitTimer.end();
      }

      PackManager.v().getTransform("jj.a").apply(b);
      PackManager.v().getTransform("jj.ule").apply(b);
      PackManager.v().getTransform("jj.ne").apply(b);
      if (Options.v().time()) {
         Timers.v().assignTimer.start();
      }

      PackManager.v().getTransform("jj.tr").apply(b);
      if (Options.v().time()) {
         Timers.v().assignTimer.end();
      }

      if (options.use_original_names()) {
         PackManager.v().getTransform("jj.ulp").apply(b);
      }

      PackManager.v().getTransform("jj.lns").apply(b);
      PackManager.v().getTransform("jj.cp").apply(b);
      PackManager.v().getTransform("jj.dae").apply(b);
      PackManager.v().getTransform("jj.cp-ule").apply(b);
      PackManager.v().getTransform("jj.lp").apply(b);
      PackManager.v().getTransform("jj.uce").apply(b);
      if (Options.v().time()) {
         Timers var10000 = Timers.v();
         var10000.stmtCount += (long)b.getUnits().size();
      }

   }

   protected void internalApply(Body b) {
      this.applyPhaseOptions((JimpleBody)b, PhaseOptions.v().getPhaseOptions(this.getPhaseName()));
   }
}
