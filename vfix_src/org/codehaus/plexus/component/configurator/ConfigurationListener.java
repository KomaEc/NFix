package org.codehaus.plexus.component.configurator;

public interface ConfigurationListener {
   void notifyFieldChangeUsingSetter(String var1, Object var2, Object var3);

   void notifyFieldChangeUsingReflection(String var1, Object var2, Object var3);
}
