package soot.jimple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.MethodSource;
import soot.PackManager;
import soot.SootMethod;
import soot.jimple.parser.JimpleAST;
import soot.options.Options;

public class JimpleMethodSource implements MethodSource {
   private static final Logger logger = LoggerFactory.getLogger(JimpleMethodSource.class);
   JimpleAST mJimpleAST;

   public JimpleMethodSource(JimpleAST aJimpleAST) {
      this.mJimpleAST = aJimpleAST;
   }

   public Body getBody(SootMethod m, String phaseName) {
      JimpleBody jb = (JimpleBody)this.mJimpleAST.getBody(m);
      if (jb == null) {
         throw new RuntimeException("Could not load body for method " + m.getSignature());
      } else {
         if (Options.v().verbose()) {
            logger.debug("[" + m.getName() + "] Retrieving JimpleBody from AST...");
         }

         PackManager.v().getPack("jb").apply(jb);
         return jb;
      }
   }
}
