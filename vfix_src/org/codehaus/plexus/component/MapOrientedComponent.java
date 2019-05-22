package org.codehaus.plexus.component;

import java.util.Map;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.repository.ComponentRequirement;

public interface MapOrientedComponent {
   void addComponentRequirement(ComponentRequirement var1, Object var2) throws ComponentConfigurationException;

   void setComponentConfiguration(Map var1) throws ComponentConfigurationException;
}
