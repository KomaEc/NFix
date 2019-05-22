package org.codehaus.plexus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.classworlds.DuplicateRealmException;
import org.codehaus.classworlds.NoSuchRealmException;
import org.codehaus.plexus.component.composition.ComponentComposerManager;
import org.codehaus.plexus.component.composition.CompositionException;
import org.codehaus.plexus.component.composition.UndefinedComponentComposerException;
import org.codehaus.plexus.component.configurator.BasicComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.discovery.ComponentDiscoverer;
import org.codehaus.plexus.component.discovery.ComponentDiscovererManager;
import org.codehaus.plexus.component.discovery.ComponentDiscoveryListener;
import org.codehaus.plexus.component.discovery.DiscoveryListenerDescriptor;
import org.codehaus.plexus.component.discovery.PlexusXmlComponentDiscoverer;
import org.codehaus.plexus.component.factory.ComponentFactory;
import org.codehaus.plexus.component.factory.ComponentFactoryManager;
import org.codehaus.plexus.component.factory.ComponentInstantiationException;
import org.codehaus.plexus.component.factory.UndefinedComponentFactoryException;
import org.codehaus.plexus.component.manager.ComponentManager;
import org.codehaus.plexus.component.manager.ComponentManagerManager;
import org.codehaus.plexus.component.manager.UndefinedComponentManagerException;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRepository;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.component.repository.exception.ComponentRepositoryException;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.configuration.PlexusConfigurationMerger;
import org.codehaus.plexus.configuration.PlexusConfigurationResourceException;
import org.codehaus.plexus.configuration.processor.ConfigurationProcessingException;
import org.codehaus.plexus.configuration.processor.ConfigurationProcessor;
import org.codehaus.plexus.configuration.processor.ConfigurationResourceNotFoundException;
import org.codehaus.plexus.configuration.processor.DirectoryConfigurationResourceHandler;
import org.codehaus.plexus.configuration.processor.FileConfigurationResourceHandler;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.context.ContextMapAdapter;
import org.codehaus.plexus.context.DefaultContext;
import org.codehaus.plexus.lifecycle.LifecycleHandlerManager;
import org.codehaus.plexus.lifecycle.UndefinedLifecycleHandlerException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.codehaus.plexus.util.StringUtils;

public class DefaultPlexusContainer extends AbstractLogEnabled implements PlexusContainer {
   private PlexusContainer parentContainer;
   private LoggerManager loggerManager;
   private DefaultContext context = new DefaultContext();
   protected PlexusConfiguration configuration;
   private Reader configurationReader;
   private ClassWorld classWorld;
   private ClassRealm coreRealm;
   private ClassRealm plexusRealm;
   private String name;
   private ComponentRepository componentRepository;
   private ComponentManagerManager componentManagerManager;
   private LifecycleHandlerManager lifecycleHandlerManager;
   private ComponentDiscovererManager componentDiscovererManager;
   private ComponentFactoryManager componentFactoryManager;
   private ComponentComposerManager componentComposerManager;
   private Map childContainers = new WeakHashMap();
   public static final String BOOTSTRAP_CONFIGURATION = "org/codehaus/plexus/plexus-bootstrap.xml";
   private boolean started = false;
   private boolean initialized = false;
   private final Date creationDate = new Date();
   // $FF: synthetic field
   static Class class$org$codehaus$plexus$PlexusContainer;

   public Date getCreationDate() {
      return this.creationDate;
   }

   public boolean hasChildContainer(String name) {
      return this.childContainers.get(name) != null;
   }

   public void removeChildContainer(String name) {
      this.childContainers.remove(name);
   }

   public PlexusContainer getChildContainer(String name) {
      return (PlexusContainer)this.childContainers.get(name);
   }

   public PlexusContainer createChildContainer(String name, List classpathJars, Map context) throws PlexusContainerException {
      return this.createChildContainer(name, classpathJars, context, Collections.EMPTY_LIST);
   }

   public PlexusContainer createChildContainer(String name, List classpathJars, Map context, List discoveryListeners) throws PlexusContainerException {
      if (this.hasChildContainer(name)) {
         throw new DuplicateChildContainerException(this.getName(), name);
      } else {
         DefaultPlexusContainer child = new DefaultPlexusContainer();
         child.classWorld = this.classWorld;
         ClassRealm childRealm = null;
         String childRealmId = this.getName() + ".child-container[" + name + "]";

         try {
            childRealm = this.classWorld.getRealm(childRealmId);
         } catch (NoSuchRealmException var12) {
            try {
               childRealm = this.classWorld.newRealm(childRealmId);
            } catch (DuplicateRealmException var11) {
               this.getLogger().error("An impossible error has occurred. After getRealm() failed, newRealm() produced duplication error on same id!", var11);
            }
         }

         childRealm.setParent(this.plexusRealm);
         child.coreRealm = childRealm;
         child.plexusRealm = childRealm;
         child.setName(name);
         child.setParentPlexusContainer(this);
         child.setLoggerManager(this.loggerManager);
         Iterator it = context.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            child.addContextValue(entry.getKey(), entry.getValue());
         }

         child.initialize();
         it = classpathJars.iterator();

         while(it.hasNext()) {
            Object next = it.next();
            File jar = (File)next;
            child.addJarResource(jar);
         }

         it = discoveryListeners.iterator();

         while(it.hasNext()) {
            ComponentDiscoveryListener listener = (ComponentDiscoveryListener)it.next();
            child.registerComponentDiscoveryListener(listener);
         }

         child.start();
         this.childContainers.put(name, child);
         return child;
      }
   }

   public Object lookup(String componentKey) throws ComponentLookupException {
      Object component = null;
      ComponentManager componentManager = this.componentManagerManager.findComponentManagerByComponentKey(componentKey);
      if (componentManager == null) {
         ComponentDescriptor descriptor = this.componentRepository.getComponentDescriptor(componentKey);
         if (descriptor == null) {
            if (this.parentContainer != null) {
               return this.parentContainer.lookup(componentKey);
            }

            if (this.getLogger().isDebugEnabled()) {
               this.getLogger().debug("Nonexistent component: " + componentKey);
            }

            String message = "Component descriptor cannot be found in the component repository: " + componentKey + ".";
            throw new ComponentLookupException(message);
         }

         componentManager = this.createComponentManager(descriptor);
      }

      try {
         component = componentManager.getComponent();
      } catch (ComponentInstantiationException var6) {
         throw new ComponentLookupException("Unable to lookup component '" + componentKey + "', it could not be created", var6);
      } catch (ComponentLifecycleException var7) {
         throw new ComponentLookupException("Unable to lookup component '" + componentKey + "', it could not be started", var7);
      }

      this.componentManagerManager.associateComponentWithComponentManager(component, componentManager);
      return component;
   }

   private ComponentManager createComponentManager(ComponentDescriptor descriptor) throws ComponentLookupException {
      String message;
      try {
         ComponentManager componentManager = this.componentManagerManager.createComponentManager(descriptor, this);
         return componentManager;
      } catch (UndefinedComponentManagerException var5) {
         message = "Cannot create component manager for " + descriptor.getComponentKey() + ", so we cannot provide a component instance.";
         throw new ComponentLookupException(message, var5);
      } catch (UndefinedLifecycleHandlerException var6) {
         message = "Cannot create component manager for " + descriptor.getComponentKey() + ", so we cannot provide a component instance.";
         throw new ComponentLookupException(message, var6);
      }
   }

   public Map lookupMap(String role) throws ComponentLookupException {
      Map components = new HashMap();
      Map componentDescriptors = this.getComponentDescriptorMap(role);
      if (componentDescriptors != null) {
         Iterator i = componentDescriptors.keySet().iterator();

         while(i.hasNext()) {
            String roleHint = (String)i.next();
            Object component = this.lookup(role, roleHint);
            components.put(roleHint, component);
         }
      }

      return components;
   }

   public List lookupList(String role) throws ComponentLookupException {
      List components = new ArrayList();
      List componentDescriptors = this.getComponentDescriptorList(role);
      Object component;
      if (componentDescriptors != null) {
         for(Iterator i = componentDescriptors.iterator(); i.hasNext(); components.add(component)) {
            ComponentDescriptor descriptor = (ComponentDescriptor)i.next();
            String roleHint = descriptor.getRoleHint();
            if (roleHint != null) {
               component = this.lookup(role, roleHint);
            } else {
               component = this.lookup(role);
            }
         }
      }

      return components;
   }

   public Object lookup(String role, String roleHint) throws ComponentLookupException {
      return this.lookup(role + roleHint);
   }

   public ComponentDescriptor getComponentDescriptor(String componentKey) {
      ComponentDescriptor result = this.componentRepository.getComponentDescriptor(componentKey);
      if (result == null && this.parentContainer != null) {
         result = this.parentContainer.getComponentDescriptor(componentKey);
      }

      return result;
   }

   public Map getComponentDescriptorMap(String role) {
      Map result = null;
      if (this.parentContainer != null) {
         result = this.parentContainer.getComponentDescriptorMap(role);
      }

      Map componentDescriptors = this.componentRepository.getComponentDescriptorMap(role);
      if (componentDescriptors != null) {
         if (result != null) {
            result.putAll(componentDescriptors);
         } else {
            result = componentDescriptors;
         }
      }

      return result;
   }

   public List getComponentDescriptorList(String role) {
      List result = null;
      Map componentDescriptorsByHint = this.getComponentDescriptorMap(role);
      if (componentDescriptorsByHint != null) {
         result = new ArrayList(componentDescriptorsByHint.values());
      } else {
         ComponentDescriptor unhintedDescriptor = this.getComponentDescriptor(role);
         if (unhintedDescriptor != null) {
            result = Collections.singletonList(unhintedDescriptor);
         } else {
            result = Collections.EMPTY_LIST;
         }
      }

      return (List)result;
   }

   public void addComponentDescriptor(ComponentDescriptor componentDescriptor) throws ComponentRepositoryException {
      this.componentRepository.addComponentDescriptor(componentDescriptor);
   }

   public void release(Object component) throws ComponentLifecycleException {
      if (component != null) {
         ComponentManager componentManager = this.componentManagerManager.findComponentManagerByComponentInstance(component);
         if (componentManager == null) {
            if (this.parentContainer != null) {
               this.parentContainer.release(component);
            } else {
               this.getLogger().warn("Component manager not found for returned component. Ignored. component=" + component);
            }
         } else {
            componentManager.release(component);
            if (componentManager.getConnections() <= 0) {
               this.componentManagerManager.unassociateComponentWithComponentManager(component);
            }
         }

      }
   }

   public void releaseAll(Map components) throws ComponentLifecycleException {
      Iterator i = components.values().iterator();

      while(i.hasNext()) {
         Object component = i.next();
         this.release(component);
      }

   }

   public void releaseAll(List components) throws ComponentLifecycleException {
      Iterator i = components.iterator();

      while(i.hasNext()) {
         Object component = i.next();
         this.release(component);
      }

   }

   public boolean hasComponent(String componentKey) {
      return this.componentRepository.hasComponent(componentKey);
   }

   public boolean hasComponent(String role, String roleHint) {
      return this.componentRepository.hasComponent(role, roleHint);
   }

   public void suspend(Object component) throws ComponentLifecycleException {
      if (component != null) {
         ComponentManager componentManager = this.componentManagerManager.findComponentManagerByComponentInstance(component);
         componentManager.suspend(component);
      }
   }

   public void resume(Object component) throws ComponentLifecycleException {
      if (component != null) {
         ComponentManager componentManager = this.componentManagerManager.findComponentManagerByComponentInstance(component);
         componentManager.resume(component);
      }
   }

   /** @deprecated */
   public ClassRealm getComponentRealm(String id) {
      return this.plexusRealm;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public void initialize() throws PlexusContainerException {
      try {
         this.initializeClassWorlds();
         this.initializeConfiguration();
         this.initializeResources();
         this.initializeCoreComponents();
         this.initializeLoggerManager();
         this.initializeContext();
         this.initializeSystemProperties();
         this.initialized = true;
      } catch (DuplicateRealmException var2) {
         throw new PlexusContainerException("Error initializing classworlds", var2);
      } catch (ConfigurationProcessingException var3) {
         throw new PlexusContainerException("Error processing configuration", var3);
      } catch (ConfigurationResourceNotFoundException var4) {
         throw new PlexusContainerException("Error processing configuration", var4);
      } catch (ComponentConfigurationException var5) {
         throw new PlexusContainerException("Error configuring components", var5);
      } catch (PlexusConfigurationException var6) {
         throw new PlexusContainerException("Error configuring components", var6);
      } catch (ComponentRepositoryException var7) {
         throw new PlexusContainerException("Error initializing components", var7);
      } catch (ContextException var8) {
         throw new PlexusContainerException("Error contextualizing components", var8);
      }
   }

   public void registerComponentDiscoveryListeners() throws ComponentLookupException {
      List listeners = this.componentDiscovererManager.getListenerDescriptors();
      if (listeners != null) {
         Iterator i = listeners.iterator();

         while(i.hasNext()) {
            DiscoveryListenerDescriptor listenerDescriptor = (DiscoveryListenerDescriptor)i.next();
            String role = listenerDescriptor.getRole();
            ComponentDiscoveryListener l = (ComponentDiscoveryListener)this.lookup(role);
            this.componentDiscovererManager.registerComponentDiscoveryListener(l);
         }
      }

   }

   public List discoverComponents(ClassRealm classRealm) throws PlexusConfigurationException, ComponentRepositoryException {
      List discoveredComponentDescriptors = new ArrayList();
      Iterator i = this.componentDiscovererManager.getComponentDiscoverers().iterator();

      label34:
      while(i.hasNext()) {
         ComponentDiscoverer componentDiscoverer = (ComponentDiscoverer)i.next();
         List componentSetDescriptors = componentDiscoverer.findComponents(this.getContext(), classRealm);
         Iterator j = componentSetDescriptors.iterator();

         while(true) {
            ComponentSetDescriptor componentSet;
            List componentDescriptors;
            do {
               if (!j.hasNext()) {
                  continue label34;
               }

               componentSet = (ComponentSetDescriptor)j.next();
               componentDescriptors = componentSet.getComponents();
            } while(componentDescriptors == null);

            Iterator k = componentDescriptors.iterator();

            while(k.hasNext()) {
               ComponentDescriptor componentDescriptor = (ComponentDescriptor)k.next();
               componentDescriptor.setComponentSetDescriptor(componentSet);
               if (this.getComponentDescriptor(componentDescriptor.getComponentKey()) == null) {
                  this.addComponentDescriptor(componentDescriptor);
                  discoveredComponentDescriptors.add(componentDescriptor);
               }
            }
         }
      }

      return discoveredComponentDescriptors;
   }

   public boolean isStarted() {
      return this.started;
   }

   public void start() throws PlexusContainerException {
      try {
         this.registerComponentDiscoveryListeners();
         this.discoverComponents(this.plexusRealm);
         this.loadComponentsOnStart();
         this.started = true;
      } catch (PlexusConfigurationException var2) {
         throw new PlexusContainerException("Error starting container", var2);
      } catch (ComponentLookupException var3) {
         throw new PlexusContainerException("Error starting container", var3);
      } catch (ComponentRepositoryException var4) {
         throw new PlexusContainerException("Error starting container", var4);
      }

      this.configuration = null;
   }

   public void dispose() {
      this.disposeAllComponents();
      if (this.parentContainer != null) {
         this.parentContainer.removeChildContainer(this.getName());
         this.parentContainer = null;
      }

      try {
         this.plexusRealm.setParent((ClassRealm)null);
         this.classWorld.disposeRealm(this.plexusRealm.getId());
      } catch (NoSuchRealmException var2) {
         this.getLogger().debug("Failed to dispose realm for exiting container: " + this.getName(), var2);
      }

      this.started = false;
      this.initialized = true;
   }

   protected void disposeAllComponents() {
      Collection collection = new ArrayList(this.componentManagerManager.getComponentManagers().values());
      Iterator iter = collection.iterator();

      while(iter.hasNext()) {
         try {
            ((ComponentManager)iter.next()).dispose();
         } catch (Exception var4) {
            this.getLogger().error("Error while disposing component manager. Continuing with the rest", var4);
         }
      }

      this.componentManagerManager.getComponentManagers().clear();
   }

   public void setParentPlexusContainer(PlexusContainer parentContainer) {
      this.parentContainer = parentContainer;
   }

   public void addContextValue(Object key, Object value) {
      this.context.put(key, value);
   }

   public void setConfigurationResource(Reader configuration) throws PlexusConfigurationResourceException {
      this.configurationReader = configuration;
   }

   protected void loadComponentsOnStart() throws PlexusConfigurationException, ComponentLookupException {
      PlexusConfiguration[] loadOnStartComponents = this.configuration.getChild("load-on-start").getChildren("component");
      this.getLogger().debug("Found " + loadOnStartComponents.length + " components to load on start");

      for(int i = 0; i < loadOnStartComponents.length; ++i) {
         String role = loadOnStartComponents[i].getChild("role").getValue((String)null);
         String roleHint = loadOnStartComponents[i].getChild("role-hint").getValue();
         if (role == null) {
            throw new PlexusConfigurationException("Missing 'role' element from load-on-start.");
         }

         if (roleHint == null) {
            this.getLogger().info("Loading on start [role]: [" + role + "]");
            this.lookup(role);
         } else if (roleHint.equals("*")) {
            this.getLogger().info("Loading on start all components with [role]: [" + role + "]");
            this.lookupList(role);
         } else {
            this.getLogger().info("Loading on start [role,roleHint]: [" + role + "," + roleHint + "]");
            this.lookup(role, roleHint);
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public ClassWorld getClassWorld() {
      return this.classWorld;
   }

   public void setClassWorld(ClassWorld classWorld) {
      this.classWorld = classWorld;
   }

   public ClassRealm getCoreRealm() {
      return this.coreRealm;
   }

   public void setCoreRealm(ClassRealm coreRealm) {
      this.coreRealm = coreRealm;
   }

   private void initializeClassWorlds() throws DuplicateRealmException {
      if (this.classWorld == null) {
         this.classWorld = new ClassWorld();
      }

      this.initializeName();
      if (this.coreRealm == null) {
         try {
            this.coreRealm = this.classWorld.getRealm("plexus.core");
         } catch (NoSuchRealmException var3) {
            this.coreRealm = this.classWorld.newRealm("plexus.core", Thread.currentThread().getContextClassLoader());
         }
      }

      if (this.plexusRealm == null) {
         try {
            this.plexusRealm = this.coreRealm.getWorld().getRealm("plexus.core.maven");
         } catch (NoSuchRealmException var2) {
            this.plexusRealm = this.coreRealm;
         }

         this.addContextValue("common.classloader", this.plexusRealm.getClassLoader());
         Thread.currentThread().setContextClassLoader(this.plexusRealm.getClassLoader());
      }

   }

   public ClassRealm getContainerRealm() {
      return this.plexusRealm;
   }

   protected void initializeName() {
      if (this.name == null) {
         int i = 0;

         while(true) {
            try {
               this.classWorld.getRealm("plexus.app" + i);
               ++i;
            } catch (NoSuchRealmException var3) {
               this.setName("app" + i);
               return;
            }
         }
      }
   }

   public Context getContext() {
      return this.context;
   }

   private void initializeContext() {
      this.addContextValue("plexus", this);
      this.addContextValue("coreRealm", this.plexusRealm);
   }

   protected void initializeConfiguration() throws ConfigurationProcessingException, ConfigurationResourceNotFoundException, PlexusConfigurationException {
      InputStream is = this.coreRealm.getResourceAsStream("org/codehaus/plexus/plexus-bootstrap.xml");
      if (is == null) {
         throw new IllegalStateException("The internal default plexus-bootstrap.xml is missing. This is highly irregular, your plexus JAR is most likely corrupt.");
      } else {
         PlexusConfiguration systemConfiguration = PlexusTools.buildConfiguration("org/codehaus/plexus/plexus-bootstrap.xml", new InputStreamReader(is));
         this.configuration = systemConfiguration;
         PlexusXmlComponentDiscoverer discoverer = new PlexusXmlComponentDiscoverer();
         PlexusConfiguration plexusConfiguration = discoverer.discoverConfiguration(this.getContext(), this.plexusRealm);
         if (plexusConfiguration != null) {
            this.configuration = PlexusConfigurationMerger.merge(plexusConfiguration, this.configuration);
            this.processConfigurationsDirectory();
         }

         if (this.configurationReader != null) {
            PlexusConfiguration userConfiguration = PlexusTools.buildConfiguration("<User Specified Configuration Reader>", this.getInterpolationConfigurationReader(this.configurationReader));
            this.configuration = PlexusConfigurationMerger.merge(userConfiguration, this.configuration);
            this.processConfigurationsDirectory();
         }

         ConfigurationProcessor p = new ConfigurationProcessor();
         p.addConfigurationResourceHandler(new FileConfigurationResourceHandler());
         p.addConfigurationResourceHandler(new DirectoryConfigurationResourceHandler());
         this.configuration = p.process(this.configuration, Collections.EMPTY_MAP);
      }
   }

   protected Reader getInterpolationConfigurationReader(Reader reader) {
      InterpolationFilterReader interpolationFilterReader = new InterpolationFilterReader(reader, new ContextMapAdapter(this.context));
      return interpolationFilterReader;
   }

   private void processConfigurationsDirectory() throws PlexusConfigurationException {
      String s = this.configuration.getChild("configurations-directory").getValue((String)null);
      if (s != null) {
         PlexusConfiguration componentsConfiguration = this.configuration.getChild("components");
         File configurationsDirectory = new File(s);
         if (configurationsDirectory.exists() && configurationsDirectory.isDirectory()) {
            List componentConfigurationFiles = null;

            try {
               componentConfigurationFiles = FileUtils.getFiles(configurationsDirectory, "**/*.conf", "**/*.xml");
            } catch (IOException var15) {
               throw new PlexusConfigurationException("Unable to locate configuration files", var15);
            }

            Iterator i = componentConfigurationFiles.iterator();

            while(i.hasNext()) {
               File componentConfigurationFile = (File)i.next();
               FileReader reader = null;

               try {
                  reader = new FileReader(componentConfigurationFile);
                  PlexusConfiguration componentConfiguration = PlexusTools.buildConfiguration(componentConfigurationFile.getAbsolutePath(), this.getInterpolationConfigurationReader(reader));
                  componentsConfiguration.addChild(componentConfiguration.getChild("components"));
               } catch (FileNotFoundException var13) {
                  throw new PlexusConfigurationException("File " + componentConfigurationFile + " disappeared before processing", var13);
               } finally {
                  IOUtil.close((Reader)reader);
               }
            }
         }
      }

   }

   private void initializeLoggerManager() throws PlexusContainerException {
      if (this.loggerManager == null) {
         try {
            this.loggerManager = (LoggerManager)this.lookup(LoggerManager.ROLE);
         } catch (ComponentLookupException var2) {
            throw new PlexusContainerException("Unable to locate logger manager", var2);
         }
      }

      this.enableLogging(this.loggerManager.getLoggerForComponent((class$org$codehaus$plexus$PlexusContainer == null ? (class$org$codehaus$plexus$PlexusContainer = class$("org.codehaus.plexus.PlexusContainer")) : class$org$codehaus$plexus$PlexusContainer).getName()));
   }

   private void initializeCoreComponents() throws ComponentConfigurationException, ComponentRepositoryException, ContextException {
      BasicComponentConfigurator configurator = new BasicComponentConfigurator();
      PlexusConfiguration c = this.configuration.getChild("component-repository");
      this.processCoreComponentConfiguration("component-repository", configurator, c);
      this.componentRepository.configure(this.configuration);
      this.componentRepository.setClassRealm(this.plexusRealm);
      this.componentRepository.initialize();
      c = this.configuration.getChild("lifecycle-handler-manager");
      this.processCoreComponentConfiguration("lifecycle-handler-manager", configurator, c);
      this.lifecycleHandlerManager.initialize();
      c = this.configuration.getChild("component-manager-manager");
      this.processCoreComponentConfiguration("component-manager-manager", configurator, c);
      this.componentManagerManager.setLifecycleHandlerManager(this.lifecycleHandlerManager);
      c = this.configuration.getChild("component-discoverer-manager");
      this.processCoreComponentConfiguration("component-discoverer-manager", configurator, c);
      this.componentDiscovererManager.initialize();
      c = this.configuration.getChild("component-factory-manager");
      this.processCoreComponentConfiguration("component-factory-manager", configurator, c);
      if (this.componentFactoryManager instanceof Contextualizable) {
         Context context = this.getContext();
         context.put("plexus", this);
         ((Contextualizable)this.componentFactoryManager).contextualize(this.getContext());
      }

      c = this.configuration.getChild("component-composer-manager");
      this.processCoreComponentConfiguration("component-composer-manager", configurator, c);
   }

   private void processCoreComponentConfiguration(String role, BasicComponentConfigurator configurator, PlexusConfiguration c) throws ComponentConfigurationException {
      String implementation = c.getAttribute("implementation", (String)null);
      if (implementation == null) {
         String msg = "Core component: '" + role + "' + which is needed by plexus to function properly cannot " + "be instantiated. Implementation attribute was not specified in plexus.conf." + "This is highly irregular, your plexus JAR is most likely corrupt.";
         throw new ComponentConfigurationException(msg);
      } else {
         ComponentDescriptor componentDescriptor = new ComponentDescriptor();
         componentDescriptor.setRole(role);
         componentDescriptor.setImplementation(implementation);
         PlexusConfiguration configuration = new XmlPlexusConfiguration("configuration");
         configuration.addChild(c);

         try {
            configurator.configureComponent(this, configuration, this.plexusRealm);
         } catch (ComponentConfigurationException var9) {
            String message = "Error configuring component: " + componentDescriptor.getHumanReadableKey();
            throw new ComponentConfigurationException(message, var9);
         }
      }
   }

   private void initializeSystemProperties() throws PlexusConfigurationException {
      PlexusConfiguration[] systemProperties = this.configuration.getChild("system-properties").getChildren("property");

      for(int i = 0; i < systemProperties.length; ++i) {
         String name = systemProperties[i].getAttribute("name");
         String value = systemProperties[i].getAttribute("value");
         if (name == null) {
            throw new PlexusConfigurationException("Missing 'name' attribute in 'property' tag. ");
         }

         if (value == null) {
            throw new PlexusConfigurationException("Missing 'value' attribute in 'property' tag. ");
         }

         System.getProperties().setProperty(name, value);
         this.getLogger().info("Setting system property: [ " + name + ", " + value + " ]");
      }

   }

   public void initializeResources() throws PlexusConfigurationException {
      PlexusConfiguration[] resourceConfigs = this.configuration.getChild("resources").getChildren();

      for(int i = 0; i < resourceConfigs.length; ++i) {
         try {
            String name = resourceConfigs[i].getName();
            if (name.equals("jar-repository")) {
               this.addJarRepository(new File(resourceConfigs[i].getValue()));
            } else if (name.equals("directory")) {
               File directory = new File(resourceConfigs[i].getValue());
               if (directory.exists() && directory.isDirectory()) {
                  this.plexusRealm.addConstituent(directory.toURL());
               }
            } else {
               this.getLogger().warn("Unknown resource type: " + name);
            }
         } catch (MalformedURLException var5) {
            this.getLogger().error("Error configuring resource: " + resourceConfigs[i].getName() + "=" + resourceConfigs[i].getValue(), var5);
         }
      }

   }

   public void addJarResource(File jar) throws PlexusContainerException {
      try {
         this.plexusRealm.addConstituent(jar.toURL());
         if (this.isStarted()) {
            this.discoverComponents(this.plexusRealm);
         }

      } catch (MalformedURLException var3) {
         throw new PlexusContainerException("Cannot add jar resource: " + jar + " (bad URL)", var3);
      } catch (PlexusConfigurationException var4) {
         throw new PlexusContainerException("Cannot add jar resource: " + jar + " (error discovering new components)", var4);
      } catch (ComponentRepositoryException var5) {
         throw new PlexusContainerException("Cannot add jar resource: " + jar + " (error discovering new components)", var5);
      }
   }

   public void addJarRepository(File repository) {
      if (repository.exists() && repository.isDirectory()) {
         File[] jars = repository.listFiles();

         for(int j = 0; j < jars.length; ++j) {
            if (jars[j].getAbsolutePath().endsWith(".jar")) {
               try {
                  this.addJarResource(jars[j]);
               } catch (PlexusContainerException var5) {
                  this.getLogger().warn("Unable to add JAR: " + jars[j], var5);
               }
            }
         }
      } else {
         this.getLogger().warn("The specified JAR repository doesn't exist or is not a directory: '" + repository.getAbsolutePath() + "'.");
      }

   }

   public Logger getLogger() {
      return super.getLogger();
   }

   public Object createComponentInstance(ComponentDescriptor componentDescriptor) throws ComponentInstantiationException, ComponentLifecycleException {
      String componentFactoryId = componentDescriptor.getComponentFactory();
      ComponentFactory componentFactory = null;
      Object component = null;

      try {
         if (componentFactoryId != null) {
            componentFactory = this.componentFactoryManager.findComponentFactory(componentFactoryId);
         } else {
            componentFactory = this.componentFactoryManager.getDefaultComponentFactory();
         }

         component = componentFactory.newInstance(componentDescriptor, this.plexusRealm, this);
      } catch (UndefinedComponentFactoryException var10) {
         throw new ComponentInstantiationException("Unable to create component as factory '" + componentFactoryId + "' could not be found", var10);
      } finally {
         if (StringUtils.isNotEmpty(componentFactoryId) && !"java".equals(componentFactoryId)) {
            this.release(componentFactory);
         }

      }

      return component;
   }

   public void composeComponent(Object component, ComponentDescriptor componentDescriptor) throws CompositionException, UndefinedComponentComposerException {
      this.componentComposerManager.assembleComponent(component, componentDescriptor, this);
   }

   public void registerComponentDiscoveryListener(ComponentDiscoveryListener listener) {
      this.componentDiscovererManager.registerComponentDiscoveryListener(listener);
   }

   public void removeComponentDiscoveryListener(ComponentDiscoveryListener listener) {
      this.componentDiscovererManager.removeComponentDiscoveryListener(listener);
   }

   public void setLoggerManager(LoggerManager loggerManager) {
      this.loggerManager = loggerManager;
   }

   public LoggerManager getLoggerManager() {
      return this.loggerManager;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
