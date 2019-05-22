package soot;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.options.Options;
import soot.toolkits.graph.interaction.InteractionHandler;

public class BodyPack extends Pack {
   private static final Logger logger = LoggerFactory.getLogger(BodyPack.class);

   public BodyPack(String name) {
      super(name);
   }

   protected void internalApply(Body b) {
      Iterator tIt = this.iterator();

      while(tIt.hasNext()) {
         Transform t = (Transform)tIt.next();
         if (Options.v().interactive_mode()) {
            InteractionHandler.v().handleNewAnalysis(t, b);
         }

         t.apply(b);
         if (Options.v().interactive_mode()) {
            InteractionHandler.v().handleTransformDone(t, b);
         }
      }

   }
}
