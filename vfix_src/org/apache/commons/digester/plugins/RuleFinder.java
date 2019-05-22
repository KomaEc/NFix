package org.apache.commons.digester.plugins;

import java.util.Properties;
import org.apache.commons.digester.Digester;

public abstract class RuleFinder {
   public abstract RuleLoader findLoader(Digester var1, Class var2, Properties var3) throws PluginException;
}
