package soot.plugins;

import soot.Transformer;
import soot.plugins.model.PhasePluginDescription;

public interface SootPhasePlugin {
   String ENABLED_BY_DEFAULT = "enabled:true";

   String[] getDeclaredOptions();

   String[] getDefaultOptions();

   Transformer getTransformer();

   void setDescription(PhasePluginDescription var1);
}
