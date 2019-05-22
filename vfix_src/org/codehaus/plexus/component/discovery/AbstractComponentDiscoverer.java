package org.codehaus.plexus.component.discovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextMapAdapter;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

public abstract class AbstractComponentDiscoverer implements ComponentDiscoverer {
   private ComponentDiscovererManager manager;

   protected abstract String getComponentDescriptorLocation();

   protected abstract ComponentSetDescriptor createComponentDescriptors(Reader var1, String var2) throws PlexusConfigurationException;

   public void setManager(ComponentDiscovererManager manager) {
      this.manager = manager;
   }

   public List findComponents(Context context, ClassRealm classRealm) throws PlexusConfigurationException {
      ArrayList componentSetDescriptors = new ArrayList();

      Enumeration resources;
      try {
         resources = classRealm.findResources(this.getComponentDescriptorLocation());
      } catch (IOException var18) {
         throw new PlexusConfigurationException("Unable to retrieve resources for: " + this.getComponentDescriptorLocation() + " in class realm: " + classRealm.getId());
      }

      Enumeration e = resources;

      while(e.hasMoreElements()) {
         URL url = (URL)e.nextElement();
         InputStreamReader reader = null;

         try {
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.connect();
            reader = new InputStreamReader(conn.getInputStream());
            InterpolationFilterReader input = new InterpolationFilterReader(reader, new ContextMapAdapter(context));
            ComponentSetDescriptor componentSetDescriptor = this.createComponentDescriptors(input, url.toString());
            componentSetDescriptors.add(componentSetDescriptor);
            ComponentDiscoveryEvent event = new ComponentDiscoveryEvent(componentSetDescriptor);
            this.manager.fireComponentDiscoveryEvent(event);
         } catch (IOException var16) {
            throw new PlexusConfigurationException("Error reading configuration " + url, var16);
         } finally {
            IOUtil.close((Reader)reader);
         }
      }

      return componentSetDescriptors;
   }
}
