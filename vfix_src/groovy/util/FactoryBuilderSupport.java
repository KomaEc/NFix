package groovy.util;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExceptionNoStack;

public abstract class FactoryBuilderSupport extends Binding {
   public static final String CURRENT_FACTORY = "_CURRENT_FACTORY_";
   public static final String PARENT_FACTORY = "_PARENT_FACTORY_";
   public static final String PARENT_NODE = "_PARENT_NODE_";
   public static final String CURRENT_NODE = "_CURRENT_NODE_";
   public static final String PARENT_CONTEXT = "_PARENT_CONTEXT_";
   public static final String PARENT_NAME = "_PARENT_NAME_";
   public static final String CURRENT_NAME = "_CURRENT_NAME_";
   public static final String OWNER = "owner";
   public static final String PARENT_BUILDER = "_PARENT_BUILDER_";
   public static final String CURRENT_BUILDER = "_CURRENT_BUILDER_";
   public static final String CHILD_BUILDER = "_CHILD_BUILDER_";
   private static final Logger LOG = Logger.getLogger(FactoryBuilderSupport.class.getName());
   private ThreadLocal<LinkedList<Map<String, Object>>> contexts;
   protected LinkedList<Closure> attributeDelegates;
   private List<Closure> disposalClosures;
   private Map<String, Factory> factories;
   private Closure nameMappingClosure;
   private ThreadLocal<FactoryBuilderSupport> localProxyBuilder;
   private FactoryBuilderSupport globalProxyBuilder;
   protected LinkedList<Closure> preInstantiateDelegates;
   protected LinkedList<Closure> postInstantiateDelegates;
   protected LinkedList<Closure> postNodeCompletionDelegates;
   protected Map<String, Closure[]> explicitProperties;
   protected Map<String, Closure> explicitMethods;
   protected Map<String, Set<String>> registrationGroup;
   protected String registrationGroupName;
   protected boolean autoRegistrationRunning;
   protected boolean autoRegistrationComplete;

   public static void checkValueIsNull(Object value, Object name) {
      if (value != null) {
         throw new RuntimeException("'" + name + "' elements do not accept a value argument.");
      }
   }

   public static boolean checkValueIsType(Object value, Object name, Class type) {
      if (value != null) {
         if (type.isAssignableFrom(value.getClass())) {
            return true;
         } else {
            throw new RuntimeException("The value argument of '" + name + "' must be of type " + type.getName());
         }
      } else {
         return false;
      }
   }

   public static boolean checkValueIsTypeNotString(Object value, Object name, Class type) {
      if (value != null) {
         if (type.isAssignableFrom(value.getClass())) {
            return true;
         } else if (value instanceof String) {
            return false;
         } else {
            throw new RuntimeException("The value argument of '" + name + "' must be of type " + type.getName() + " or a String.");
         }
      } else {
         return false;
      }
   }

   public FactoryBuilderSupport() {
      this(false);
   }

   public FactoryBuilderSupport(boolean init) {
      this.contexts = new ThreadLocal();
      this.attributeDelegates = new LinkedList();
      this.disposalClosures = new ArrayList();
      this.factories = new HashMap();
      this.localProxyBuilder = new ThreadLocal();
      this.preInstantiateDelegates = new LinkedList();
      this.postInstantiateDelegates = new LinkedList();
      this.postNodeCompletionDelegates = new LinkedList();
      this.explicitProperties = new HashMap();
      this.explicitMethods = new HashMap();
      this.registrationGroup = new HashMap();
      this.registrationGroupName = "";
      this.autoRegistrationRunning = false;
      this.autoRegistrationComplete = false;
      this.globalProxyBuilder = this;
      this.registrationGroup.put(this.registrationGroupName, new TreeSet());
      if (init) {
         this.autoRegisterNodes();
      }

   }

   private Set<String> getRegistrationGroup(String name) {
      Set<String> group = (Set)this.registrationGroup.get(name);
      if (group == null) {
         group = new TreeSet();
         this.registrationGroup.put(name, group);
      }

      return (Set)group;
   }

   public void autoRegisterNodes() {
      synchronized(this) {
         if (this.autoRegistrationRunning || this.autoRegistrationComplete) {
            return;
         }
      }

      this.autoRegistrationRunning = true;

      try {
         this.callAutoRegisterMethods(this.getClass());
      } finally {
         this.autoRegistrationComplete = true;
         this.autoRegistrationRunning = false;
      }

   }

   private void callAutoRegisterMethods(Class declaredClass) {
      if (declaredClass != null) {
         this.callAutoRegisterMethods(declaredClass.getSuperclass());
         Method[] arr$ = declaredClass.getDeclaredMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (method.getName().startsWith("register") && method.getParameterTypes().length == 0) {
               this.registrationGroupName = method.getName().substring("register".length());
               this.registrationGroup.put(this.registrationGroupName, new TreeSet());

               try {
                  if (Modifier.isPublic(method.getModifiers())) {
                     method.invoke(this);
                  }
               } catch (IllegalAccessException var11) {
                  throw new RuntimeException("Could not init " + this.getClass().getName() + " because of an access error in " + declaredClass.getName() + "." + method.getName(), var11);
               } catch (InvocationTargetException var12) {
                  throw new RuntimeException("Could not init " + this.getClass().getName() + " because of an exception in " + declaredClass.getName() + "." + method.getName(), var12);
               } finally {
                  this.registrationGroupName = "";
               }
            }
         }

      }
   }

   /** @deprecated */
   @Deprecated
   public FactoryBuilderSupport(Closure nameMappingClosure) {
      this.contexts = new ThreadLocal();
      this.attributeDelegates = new LinkedList();
      this.disposalClosures = new ArrayList();
      this.factories = new HashMap();
      this.localProxyBuilder = new ThreadLocal();
      this.preInstantiateDelegates = new LinkedList();
      this.postInstantiateDelegates = new LinkedList();
      this.postNodeCompletionDelegates = new LinkedList();
      this.explicitProperties = new HashMap();
      this.explicitMethods = new HashMap();
      this.registrationGroup = new HashMap();
      this.registrationGroupName = "";
      this.autoRegistrationRunning = false;
      this.autoRegistrationComplete = false;
      this.nameMappingClosure = nameMappingClosure;
   }

   public Object getVariable(String name) {
      return this.getProxyBuilder().doGetVariable(name);
   }

   private Object doGetVariable(String name) {
      return super.getVariable(name);
   }

   public void setVariable(String name, Object value) {
      this.getProxyBuilder().doSetVariable(name, value);
   }

   private void doSetVariable(String name, Object value) {
      super.setVariable(name, value);
   }

   public Map getVariables() {
      return this.getProxyBuilder().doGetVariables();
   }

   private Map doGetVariables() {
      return super.getVariables();
   }

   public Object getProperty(String property) {
      try {
         return this.getProxyBuilder().doGetProperty(property);
      } catch (MissingPropertyException var3) {
         return this.getContext() != null && this.getContext().containsKey(property) ? this.getContext().get(property) : this.getMetaClass().getProperty(this, property);
      }
   }

   private Object doGetProperty(String property) {
      Closure[] accessors = this.resolveExplicitProperty(property);
      if (accessors != null) {
         if (accessors[0] == null) {
            throw new MissingPropertyException(property + " is declared as write only");
         } else {
            return accessors[0].call();
         }
      } else {
         return super.getProperty(property);
      }
   }

   public void setProperty(String property, Object newValue) {
      this.getProxyBuilder().doSetProperty(property, newValue);
   }

   private void doSetProperty(String property, Object newValue) {
      Closure[] accessors = this.resolveExplicitProperty(property);
      if (accessors != null) {
         if (accessors[1] == null) {
            throw new MissingPropertyException(property + " is declared as read only");
         }

         accessors[1].call(newValue);
      } else {
         super.setProperty(property, newValue);
      }

   }

   public Map<String, Factory> getFactories() {
      return Collections.unmodifiableMap(this.getProxyBuilder().factories);
   }

   public Map<String, Closure> getExplicitMethods() {
      return Collections.unmodifiableMap(this.getProxyBuilder().explicitMethods);
   }

   public Map<String, Closure[]> getExplicitProperties() {
      return Collections.unmodifiableMap(this.getProxyBuilder().explicitProperties);
   }

   public Map<String, Factory> getLocalFactories() {
      return Collections.unmodifiableMap(this.factories);
   }

   public Map<String, Closure> getLocalExplicitMethods() {
      return Collections.unmodifiableMap(this.explicitMethods);
   }

   public Map<String, Closure[]> getLocalExplicitProperties() {
      return Collections.unmodifiableMap(this.explicitProperties);
   }

   public Set<String> getRegistrationGroups() {
      return Collections.unmodifiableSet(this.registrationGroup.keySet());
   }

   public Set<String> getRegistrationGroupItems(String group) {
      Set<String> groupSet = (Set)this.registrationGroup.get(group);
      return groupSet != null ? Collections.unmodifiableSet(groupSet) : Collections.emptySet();
   }

   public List<Closure> getAttributeDelegates() {
      return Collections.unmodifiableList(this.attributeDelegates);
   }

   public List<Closure> getPreInstantiateDelegates() {
      return Collections.unmodifiableList(this.preInstantiateDelegates);
   }

   public List<Closure> getPostInstantiateDelegates() {
      return Collections.unmodifiableList(this.postInstantiateDelegates);
   }

   public List<Closure> getPostNodeCompletionDelegates() {
      return Collections.unmodifiableList(this.postNodeCompletionDelegates);
   }

   public Map<String, Object> getContext() {
      LinkedList<Map<String, Object>> contexts = (LinkedList)this.getProxyBuilder().contexts.get();
      return contexts != null && !contexts.isEmpty() ? (Map)contexts.getFirst() : null;
   }

   public Object getCurrent() {
      return this.getContextAttribute("_CURRENT_NODE_");
   }

   public Factory getCurrentFactory() {
      return (Factory)this.getContextAttribute("_CURRENT_FACTORY_");
   }

   public String getCurrentName() {
      return (String)this.getContextAttribute("_CURRENT_NAME_");
   }

   public FactoryBuilderSupport getCurrentBuilder() {
      return (FactoryBuilderSupport)this.getContextAttribute("_CURRENT_BUILDER_");
   }

   public Object getParentNode() {
      return this.getContextAttribute("_PARENT_NODE_");
   }

   public Factory getParentFactory() {
      return (Factory)this.getContextAttribute("_PARENT_FACTORY_");
   }

   public Map getParentContext() {
      return (Map)this.getContextAttribute("_PARENT_CONTEXT_");
   }

   public String getParentName() {
      return (String)this.getContextAttribute("_PARENT_NAME_");
   }

   public FactoryBuilderSupport getChildBuilder() {
      return (FactoryBuilderSupport)this.getContextAttribute("_CHILD_BUILDER_");
   }

   public Object getContextAttribute(String key) {
      Map context = this.getContext();
      return context != null ? context.get(key) : null;
   }

   public Object invokeMethod(String methodName) {
      return this.getProxyBuilder().invokeMethod(methodName, (Object)null);
   }

   public Object invokeMethod(String methodName, Object args) {
      Object name = this.getProxyBuilder().getName(methodName);
      Map previousContext = this.getProxyBuilder().getContext();

      try {
         Object result = this.getProxyBuilder().doInvokeMethod(methodName, name, args);
         return result;
      } catch (RuntimeException var8) {
         if (this.getContexts().contains(previousContext)) {
            for(Map context = this.getProxyBuilder().getContext(); context != null && context != previousContext; context = this.getProxyBuilder().getContext()) {
               this.getProxyBuilder().popContext();
            }
         }

         throw var8;
      }
   }

   public Closure addAttributeDelegate(Closure attrDelegate) {
      this.getProxyBuilder().attributeDelegates.addFirst(attrDelegate);
      return attrDelegate;
   }

   public void removeAttributeDelegate(Closure attrDelegate) {
      this.getProxyBuilder().attributeDelegates.remove(attrDelegate);
   }

   public Closure addPreInstantiateDelegate(Closure delegate) {
      this.getProxyBuilder().preInstantiateDelegates.addFirst(delegate);
      return delegate;
   }

   public void removePreInstantiateDelegate(Closure delegate) {
      this.getProxyBuilder().preInstantiateDelegates.remove(delegate);
   }

   public Closure addPostInstantiateDelegate(Closure delegate) {
      this.getProxyBuilder().postInstantiateDelegates.addFirst(delegate);
      return delegate;
   }

   public void removePostInstantiateDelegate(Closure delegate) {
      this.getProxyBuilder().postInstantiateDelegates.remove(delegate);
   }

   public Closure addPostNodeCompletionDelegate(Closure delegate) {
      this.getProxyBuilder().postNodeCompletionDelegates.addFirst(delegate);
      return delegate;
   }

   public void removePostNodeCompletionDelegate(Closure delegate) {
      this.getProxyBuilder().postNodeCompletionDelegates.remove(delegate);
   }

   public void registerExplicitProperty(String name, Closure getter, Closure setter) {
      this.registerExplicitProperty(name, this.registrationGroupName, getter, setter);
   }

   public void registerExplicitProperty(String name, String groupName, Closure getter, Closure setter) {
      if (getter != null) {
         getter.setDelegate(this);
      }

      if (setter != null) {
         setter.setDelegate(this);
      }

      this.explicitProperties.put(name, new Closure[]{getter, setter});
      String methodNameBase = MetaClassHelper.capitalize(name);
      if (getter != null) {
         this.getRegistrationGroup(groupName).add("get" + methodNameBase);
      }

      if (setter != null) {
         this.getRegistrationGroup(groupName).add("set" + methodNameBase);
      }

   }

   public void registerExplicitMethod(String name, Closure closure) {
      this.registerExplicitMethod(name, this.registrationGroupName, closure);
   }

   public void registerExplicitMethod(String name, String groupName, Closure closure) {
      closure.setDelegate(this);
      this.explicitMethods.put(name, closure);
      this.getRegistrationGroup(groupName).add(name);
   }

   public void registerBeanFactory(String theName, Class beanClass) {
      this.registerBeanFactory(theName, this.registrationGroupName, beanClass);
   }

   public void registerBeanFactory(String theName, String groupName, final Class beanClass) {
      this.getProxyBuilder().registerFactory(theName, new AbstractFactory() {
         public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
            return FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass) ? value : beanClass.newInstance();
         }
      });
      this.getRegistrationGroup(groupName).add(theName);
   }

   public void registerFactory(String name, Factory factory) {
      this.registerFactory(name, this.registrationGroupName, factory);
   }

   public void registerFactory(String name, String groupName, Factory factory) {
      this.getProxyBuilder().factories.put(name, factory);
      this.getRegistrationGroup(groupName).add(name);
      factory.onFactoryRegistration(this, name, groupName);
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      Factory factory = this.getProxyBuilder().resolveFactory(name, attributes, value);
      if (factory == null) {
         LOG.log(Level.WARNING, "Could not find match for name '" + name + "'");
         throw new MissingMethodExceptionNoStack((String)name, Object.class, new Object[]{attributes, value});
      } else {
         this.getProxyBuilder().getContext().put("_CURRENT_FACTORY_", factory);
         this.getProxyBuilder().getContext().put("_CURRENT_NAME_", String.valueOf(name));
         this.getProxyBuilder().preInstantiate(name, attributes, value);

         Object node;
         try {
            node = factory.newInstance(this.getProxyBuilder().getChildBuilder(), name, value, attributes);
            if (node == null) {
               LOG.log(Level.WARNING, "Factory for name '" + name + "' returned null");
               return null;
            }

            if (LOG.isLoggable(Level.FINE)) {
               LOG.fine("For name: " + name + " created node: " + node);
            }
         } catch (Exception var7) {
            throw new RuntimeException("Failed to create component for '" + name + "' reason: " + var7, var7);
         }

         this.getProxyBuilder().postInstantiate(name, attributes, node);
         this.getProxyBuilder().handleNodeAttributes(node, attributes);
         return node;
      }
   }

   protected Factory resolveFactory(Object name, Map attributes, Object value) {
      this.getProxyBuilder().getContext().put("_CHILD_BUILDER_", this.getProxyBuilder());
      return (Factory)this.getProxyBuilder().factories.get(name);
   }

   protected Closure resolveExplicitMethod(String methodName, Object args) {
      return (Closure)this.explicitMethods.get(methodName);
   }

   protected Closure[] resolveExplicitProperty(String propertyName) {
      return (Closure[])this.explicitProperties.get(propertyName);
   }

   private Object doInvokeMethod(String methodName, Object name, Object args) {
      Reference explicitResult = new Reference();
      return this.checkExplicitMethod(methodName, args, explicitResult) ? explicitResult.get() : this.dispathNodeCall(name, args);
   }

   protected boolean checkExplicitMethod(String methodName, Object args, Reference result) {
      Closure explicitMethod = this.resolveExplicitMethod(methodName, args);
      if (explicitMethod != null) {
         if (args instanceof Object[]) {
            result.set(explicitMethod.call((Object[])((Object[])args)));
         } else {
            result.set(explicitMethod.call(args));
         }

         return true;
      } else {
         return false;
      }
   }

   protected Object dispathNodeCall(Object name, Object args) {
      Closure closure = null;
      List list = InvokerHelper.asList(args);
      boolean needToPopContext;
      if (this.getProxyBuilder().getContexts().isEmpty()) {
         this.getProxyBuilder().newContext();
         needToPopContext = true;
      } else {
         needToPopContext = false;
      }

      Object node;
      try {
         Map namedArgs = Collections.EMPTY_MAP;
         if (list.size() > 0 && list.get(0) instanceof LinkedHashMap) {
            namedArgs = (Map)list.get(0);
            list = list.subList(1, list.size());
         }

         if (list.size() > 0 && list.get(list.size() - 1) instanceof Closure) {
            closure = (Closure)list.get(list.size() - 1);
            list = list.subList(0, list.size() - 1);
         }

         Object arg;
         if (list.size() == 0) {
            arg = null;
         } else if (list.size() == 1) {
            arg = list.get(0);
         } else {
            arg = list;
         }

         node = this.getProxyBuilder().createNode(name, namedArgs, arg);
         Object current = this.getProxyBuilder().getCurrent();
         if (current != null) {
            this.getProxyBuilder().setParent(current, node);
         }

         if (closure != null) {
            Factory parentFactory = this.getProxyBuilder().getCurrentFactory();
            if (parentFactory.isLeaf()) {
               throw new RuntimeException("'" + name + "' doesn't support nesting.");
            }

            boolean processContent = true;
            if (parentFactory.isHandlesNodeChildren()) {
               processContent = parentFactory.onNodeChildren(this, node, closure);
            }

            if (processContent) {
               String parentName = this.getProxyBuilder().getCurrentName();
               Map parentContext = this.getProxyBuilder().getContext();
               this.getProxyBuilder().newContext();

               try {
                  this.getProxyBuilder().getContext().put("owner", closure.getOwner());
                  this.getProxyBuilder().getContext().put("_CURRENT_NODE_", node);
                  this.getProxyBuilder().getContext().put("_PARENT_FACTORY_", parentFactory);
                  this.getProxyBuilder().getContext().put("_PARENT_NODE_", current);
                  this.getProxyBuilder().getContext().put("_PARENT_CONTEXT_", parentContext);
                  this.getProxyBuilder().getContext().put("_PARENT_NAME_", parentName);
                  this.getProxyBuilder().getContext().put("_PARENT_BUILDER_", parentContext.get("_CURRENT_BUILDER_"));
                  this.getProxyBuilder().getContext().put("_CURRENT_BUILDER_", parentContext.get("_CHILD_BUILDER_"));
                  this.getProxyBuilder().setClosureDelegate(closure, node);
                  closure.call();
               } finally {
                  this.getProxyBuilder().popContext();
               }
            }
         }

         this.getProxyBuilder().nodeCompleted(current, node);
         node = this.getProxyBuilder().postNodeCompletion(current, node);
      } finally {
         if (needToPopContext) {
            this.getProxyBuilder().popContext();
         }

      }

      return node;
   }

   public Object getName(String methodName) {
      return this.getProxyBuilder().nameMappingClosure != null ? this.getProxyBuilder().nameMappingClosure.call((Object)methodName) : methodName;
   }

   protected FactoryBuilderSupport getProxyBuilder() {
      FactoryBuilderSupport proxy = (FactoryBuilderSupport)this.localProxyBuilder.get();
      return proxy == null ? this.globalProxyBuilder : proxy;
   }

   protected void setProxyBuilder(FactoryBuilderSupport proxyBuilder) {
      this.globalProxyBuilder = proxyBuilder;
   }

   public Closure getNameMappingClosure() {
      return this.nameMappingClosure;
   }

   public void setNameMappingClosure(Closure nameMappingClosure) {
      this.nameMappingClosure = nameMappingClosure;
   }

   protected void handleNodeAttributes(Object node, Map attributes) {
      if (node != null) {
         Closure attrDelegate;
         FactoryBuilderSupport builder;
         for(Iterator i$ = this.getProxyBuilder().attributeDelegates.iterator(); i$.hasNext(); attrDelegate.call(new Object[]{builder, node, attributes})) {
            attrDelegate = (Closure)i$.next();
            builder = this;
            if (attrDelegate.getOwner() instanceof FactoryBuilderSupport) {
               builder = (FactoryBuilderSupport)attrDelegate.getOwner();
            } else if (attrDelegate.getDelegate() instanceof FactoryBuilderSupport) {
               builder = (FactoryBuilderSupport)attrDelegate.getDelegate();
            }
         }

         if (this.getProxyBuilder().getCurrentFactory().onHandleNodeAttributes(this.getProxyBuilder().getChildBuilder(), node, attributes)) {
            this.getProxyBuilder().setNodeAttributes(node, attributes);
         }

      }
   }

   protected void newContext() {
      this.getContexts().addFirst(new HashMap());
   }

   protected void nodeCompleted(Object parent, Object node) {
      this.getProxyBuilder().getCurrentFactory().onNodeCompleted(this.getProxyBuilder().getChildBuilder(), parent, node);
   }

   protected Map<String, Object> popContext() {
      return !this.getProxyBuilder().getContexts().isEmpty() ? (Map)this.getProxyBuilder().getContexts().removeFirst() : null;
   }

   protected void postInstantiate(Object name, Map attributes, Object node) {
      Iterator i$ = this.getProxyBuilder().postInstantiateDelegates.iterator();

      while(i$.hasNext()) {
         Closure postInstantiateDelegate = (Closure)i$.next();
         postInstantiateDelegate.call(new Object[]{this, attributes, node});
      }

   }

   protected Object postNodeCompletion(Object parent, Object node) {
      Iterator i$ = this.getProxyBuilder().postNodeCompletionDelegates.iterator();

      while(i$.hasNext()) {
         Closure postNodeCompletionDelegate = (Closure)i$.next();
         postNodeCompletionDelegate.call(new Object[]{this, parent, node});
      }

      return node;
   }

   protected void preInstantiate(Object name, Map attributes, Object value) {
      Iterator i$ = this.getProxyBuilder().preInstantiateDelegates.iterator();

      while(i$.hasNext()) {
         Closure preInstantiateDelegate = (Closure)i$.next();
         preInstantiateDelegate.call(new Object[]{this, attributes, value});
      }

   }

   protected void reset() {
      this.getProxyBuilder().getContexts().clear();
   }

   protected void setClosureDelegate(Closure closure, Object node) {
      closure.setDelegate(this);
   }

   protected void setNodeAttributes(Object node, Map attributes) {
      Iterator i$ = attributes.entrySet().iterator();

      while(i$.hasNext()) {
         Entry entry = (Entry)i$.next();
         String property = entry.getKey().toString();
         Object value = entry.getValue();
         InvokerHelper.setProperty(node, property, value);
      }

   }

   protected void setParent(Object parent, Object child) {
      this.getProxyBuilder().getCurrentFactory().setParent(this.getProxyBuilder().getChildBuilder(), parent, child);
      Factory parentFactory = this.getProxyBuilder().getParentFactory();
      if (parentFactory != null) {
         parentFactory.setChild(this.getProxyBuilder().getCurrentBuilder(), parent, child);
      }

   }

   protected LinkedList<Map<String, Object>> getContexts() {
      LinkedList<Map<String, Object>> contexts = (LinkedList)this.getProxyBuilder().contexts.get();
      if (contexts == null) {
         contexts = new LinkedList();
         this.getProxyBuilder().contexts.set(contexts);
      }

      return contexts;
   }

   protected Map<String, Object> getContinuationData() {
      Map<String, Object> data = new HashMap();
      data.put("proxyBuilder", this.localProxyBuilder.get());
      data.put("contexts", this.contexts.get());
      return data;
   }

   protected void restoreFromContinuationData(Map<String, Object> data) {
      this.localProxyBuilder.set((FactoryBuilderSupport)data.get("proxyBuilder"));
      this.contexts.set((LinkedList)data.get("contexts"));
   }

   public Object build(Class viewClass) {
      if (Script.class.isAssignableFrom(viewClass)) {
         Script script = InvokerHelper.createScript(viewClass, this);
         return this.build(script);
      } else {
         throw new RuntimeException("Only scripts can be executed via build(Class)");
      }
   }

   public Object build(Script script) {
      MetaClass scriptMetaClass = script.getMetaClass();
      script.setMetaClass(new FactoryInterceptorMetaClass(scriptMetaClass, this));
      script.setBinding(this);
      return script.run();
   }

   public Object build(String script, GroovyClassLoader loader) {
      return this.build(loader.parseClass(script));
   }

   public Object withBuilder(FactoryBuilderSupport builder, Closure closure) {
      if (builder != null && closure != null) {
         Object result = null;
         Object previousContext = this.getProxyBuilder().getContext();
         FactoryBuilderSupport previousProxyBuilder = (FactoryBuilderSupport)this.localProxyBuilder.get();

         try {
            this.localProxyBuilder.set(builder);
            closure.setDelegate(builder);
            result = closure.call();
         } catch (RuntimeException var11) {
            this.localProxyBuilder.set(previousProxyBuilder);
            if (this.getProxyBuilder().getContexts().contains(previousContext)) {
               for(Map context = this.getProxyBuilder().getContext(); context != null && context != previousContext; context = this.getProxyBuilder().getContext()) {
                  this.getProxyBuilder().popContext();
               }
            }

            throw var11;
         } finally {
            this.localProxyBuilder.set(previousProxyBuilder);
         }

         return result;
      } else {
         return null;
      }
   }

   public Object withBuilder(FactoryBuilderSupport builder, String name, Closure closure) {
      if (name == null) {
         return null;
      } else {
         Object result = this.getProxyBuilder().withBuilder(builder, closure);
         return this.getProxyBuilder().invokeMethod(name, new Object[]{result});
      }
   }

   public Object withBuilder(Map attributes, FactoryBuilderSupport builder, String name, Closure closure) {
      if (name == null) {
         return null;
      } else {
         Object result = this.getProxyBuilder().withBuilder(builder, closure);
         return this.getProxyBuilder().invokeMethod(name, new Object[]{attributes, result});
      }
   }

   public void addDisposalClosure(Closure closure) {
      this.disposalClosures.add(closure);
   }

   public void dispose() {
      for(int i = this.disposalClosures.size() - 1; i >= 0; --i) {
         ((Closure)this.disposalClosures.get(i)).call();
      }

   }
}
