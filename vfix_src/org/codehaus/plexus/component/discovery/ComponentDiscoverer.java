package org.codehaus.plexus.component.discovery;

import java.util.List;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.context.Context;

public interface ComponentDiscoverer {
   String ROLE = (null.class$org$codehaus$plexus$component$discovery$ComponentDiscoverer == null ? (null.class$org$codehaus$plexus$component$discovery$ComponentDiscoverer = null.class$("org.codehaus.plexus.component.discovery.ComponentDiscoverer")) : null.class$org$codehaus$plexus$component$discovery$ComponentDiscoverer).getName();

   void setManager(ComponentDiscovererManager var1);

   List findComponents(Context var1, ClassRealm var2) throws PlexusConfigurationException;
}
