package soot.coffi;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.PackManager;
import soot.PhaseOptions;
import soot.Scene;
import soot.SootMethod;
import soot.Timers;
import soot.jbco.Main;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.options.Options;

public class CoffiMethodSource implements MethodSource {
   private static final Logger logger = LoggerFactory.getLogger(CoffiMethodSource.class);
   public ClassFile coffiClass;
   public method_info coffiMethod;

   CoffiMethodSource(ClassFile coffiClass, method_info coffiMethod) {
      this.coffiClass = coffiClass;
      this.coffiMethod = coffiMethod;
   }

   public Body getBody(SootMethod m, String phaseName) {
      JimpleBody jb = Jimple.v().newBody(m);
      Map options = PhaseOptions.v().getPhaseOptions(phaseName);
      boolean useOriginalNames = PhaseOptions.getBoolean(options, "use-original-names");
      if (useOriginalNames) {
         Util.v().setFaithfulNaming(true);
      }

      if (Options.v().verbose()) {
         logger.debug("[" + m.getName() + "] Constructing JimpleBody from coffi...");
      }

      if (!m.isAbstract() && !m.isNative() && !m.isPhantom()) {
         if (Options.v().time()) {
            Timers.v().conversionTimer.start();
         }

         if (this.coffiMethod.instructions == null) {
            if (Options.v().verbose()) {
               logger.debug("[" + m.getName() + "]     Parsing Coffi instructions...");
            }

            this.coffiClass.parseMethod(this.coffiMethod);
         }

         if (this.coffiMethod.cfg == null) {
            if (Options.v().verbose()) {
               logger.debug("[" + m.getName() + "]     Building Coffi CFG...");
            }

            new CFG(this.coffiMethod);
            if (Main.metrics) {
               return null;
            }
         }

         if (Options.v().verbose()) {
            logger.debug("[" + m.getName() + "]     Producing naive Jimple...");
         }

         boolean oldPhantomValue = Scene.v().getPhantomRefs();
         Scene.v().setPhantomRefs(true);
         this.coffiMethod.cfg.jimplify(this.coffiClass.constant_pool, this.coffiClass.this_class, this.coffiClass.bootstrap_methods_attribute, jb);
         Scene.v().setPhantomRefs(oldPhantomValue);
         if (Options.v().time()) {
            Timers.v().conversionTimer.end();
         }

         this.coffiMethod.instructions = null;
         this.coffiMethod.cfg = null;
         this.coffiMethod.attributes = null;
         this.coffiMethod.code_attr = null;
         this.coffiMethod.jmethod = null;
         this.coffiMethod.instructionList = null;
         this.coffiMethod = null;
         this.coffiClass = null;
         PackManager.v().getPack("jb").apply(jb);
         return jb;
      } else {
         return jb;
      }
   }
}
