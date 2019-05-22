package org.codehaus.plexus;

import java.io.File;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.composition.CompositionException;
import org.codehaus.plexus.component.composition.UndefinedComponentComposerException;
import org.codehaus.plexus.component.discovery.ComponentDiscoveryListener;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.component.repository.exception.ComponentRepositoryException;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;

public interface PlexusContainer {
   String ROLE = (null.class$org$codehaus$plexus$PlexusContainer == null ? (null.class$org$codehaus$plexus$PlexusContainer = null.class$("org.codehaus.plexus.PlexusContainer")) : null.class$org$codehaus$plexus$PlexusContainer).getName();

   Date getCreationDate();

   boolean hasChildContainer(String var1);

   void removeChildContainer(String var1);

   PlexusContainer getChildContainer(String var1);

   PlexusContainer createChildContainer(String var1, List var2, Map var3) throws PlexusContainerException;

   PlexusContainer createChildContainer(String var1, List var2, Map var3, List var4) throws PlexusContainerException;

   Object lookup(String var1) throws ComponentLookupException;

   Object lookup(String var1, String var2) throws ComponentLookupException;

   Map lookupMap(String var1) throws ComponentLookupException;

   List lookupList(String var1) throws ComponentLookupException;

   ComponentDescriptor getComponentDescriptor(String var1);

   Map getComponentDescriptorMap(String var1);

   List getComponentDescriptorList(String var1);

   void addComponentDescriptor(ComponentDescriptor var1) throws ComponentRepositoryException;

   void release(Object var1) throws ComponentLifecycleException;

   void releaseAll(Map var1) throws ComponentLifecycleException;

   void releaseAll(List var1) throws ComponentLifecycleException;

   boolean hasComponent(String var1);

   boolean hasComponent(String var1, String var2);

   void suspend(Object var1) throws ComponentLifecycleException;

   void resume(Object var1) throws ComponentLifecycleException;

   void initialize() throws PlexusContainerException;

   boolean isInitialized();

   void start() throws PlexusContainerException;

   boolean isStarted();

   void dispose();

   Context getContext();

   void setParentPlexusContainer(PlexusContainer var1);

   void addContextValue(Object var1, Object var2);

   void setConfigurationResource(Reader var1) throws PlexusConfigurationResourceException;

   Logger getLogger();

   Object createComponentInstance(ComponentDescriptor var1) throws ComponentInstantiationException, ComponentLifecycleException;

   void composeComponent(Object var1, ComponentDescriptor var2) throws CompositionException, UndefinedComponentComposerException;

   void registerComponentDiscoveryListener(ComponentDiscoveryListener var1);

   void removeComponentDiscoveryListener(ComponentDiscoveryListener var1);

   void addJarRepository(File var1);

   void addJarResource(File var1) throws PlexusContainerException;

   ClassRealm getContainerRealm();

   /** @deprecated */
   ClassRealm getComponentRealm(String var1);

   void setLoggerManager(LoggerManager var1);

   LoggerManager getLoggerManager();
}
