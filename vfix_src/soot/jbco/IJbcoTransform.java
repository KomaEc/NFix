package soot.jbco;

import java.io.PrintStream;
import soot.G;

public interface IJbcoTransform {
   /** @deprecated */
   @Deprecated
   PrintStream out = G.v().out;
   /** @deprecated */
   @Deprecated
   boolean output = G.v().soot_options_Options().verbose() || Main.jbcoVerbose;
   /** @deprecated */
   @Deprecated
   boolean debug = Main.jbcoDebug;

   String getName();

   String[] getDependencies();

   void outputSummary();

   default boolean isVerbose() {
      return G.v().soot_options_Options().verbose() || Main.jbcoVerbose;
   }

   default boolean isDebugEnabled() {
      return Main.jbcoDebug;
   }
}
