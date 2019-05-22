package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import groovy.util.Factory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServerConnection;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxBuilder extends FactoryBuilderSupport {
   private MBeanServerConnection server;
   private String defaultNameDomain;
   private String defaultNameType;
   private String mode;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205579L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205579 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxListenerFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxEmitterFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeansFactory;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServerConnection;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$util$FactoryBuilderSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanExportFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxServerConnectorFactory;
   // $FF: synthetic field
   private static Class $class$groovy$util$Factory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxTimerFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;

   public JmxBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.defaultNameDomain = (String)ScriptBytecodeAdapter.castToType((String)ScriptBytecodeAdapter.castToType(var1[0].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), $get$$class$java$lang$String()), $get$$class$java$lang$String());
      this.defaultNameType = (String)ScriptBytecodeAdapter.castToType((String)ScriptBytecodeAdapter.castToType(var1[1].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), $get$$class$java$lang$String()), $get$$class$java$lang$String());
      this.mode = (String)ScriptBytecodeAdapter.castToType("markup", $get$$class$java$lang$String());
      var1[2].callCurrent(this);
   }

   public JmxBuilder(MBeanServerConnection svrConnection) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[0];
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$jmx$builder$JmxBuilder());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((MBeanServerConnection)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.server = (MBeanServerConnection)ScriptBytecodeAdapter.castToType(svrConnection, $get$$class$javax$management$MBeanServerConnection());
   }

   protected void registerFactories() {
      CallSite[] var1 = $getCallSiteArray();
      var1[3].callCurrent(this, "export", var1[4].callConstructor($get$$class$groovy$jmx$builder$JmxBeanExportFactory()));
      var1[5].callCurrent(this, "bean", var1[6].callConstructor($get$$class$groovy$jmx$builder$JmxBeanFactory()));
      var1[7].callCurrent(this, "beans", var1[8].callConstructor($get$$class$groovy$jmx$builder$JmxBeansFactory()));
      var1[9].callCurrent(this, "timer", var1[10].callConstructor($get$$class$groovy$jmx$builder$JmxTimerFactory()));
      var1[11].callCurrent(this, "listener", var1[12].callConstructor($get$$class$groovy$jmx$builder$JmxListenerFactory()));
      var1[13].callCurrent(this, "emitter", var1[14].callConstructor($get$$class$groovy$jmx$builder$JmxEmitterFactory()));
      JmxServerConnectorFactory svrFactory = var1[15].callConstructor($get$$class$groovy$jmx$builder$JmxServerConnectorFactory());
      var1[16].callCurrent(this, "server", svrFactory);
      var1[17].callCurrent(this, "connectorServer", svrFactory);
      var1[18].callCurrent(this, "serverConnector", svrFactory);
      Object newClientFactory = new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxClientConnectorFactory;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            return var2[0].callConstructor($get$$class$groovy$jmx$builder$JmxClientConnectorFactory());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1()) {
               return ScriptBytecodeAdapter.initMetaClass(this);
            } else {
               ClassInfo var1 = $staticClassInfo;
               if (var1 == null) {
                  $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
               }

               return var1.getMetaClass();
            }
         }

         // $FF: synthetic method
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "<$constructor$>";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1(), var0);
         }

         // $FF: synthetic method
         private static CallSite[] $getCallSiteArray() {
            CallSiteArray var0;
            if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
               var0 = $createCallSiteArray();
               $callSiteArray = new SoftReference(var0);
            }

            return var0.array;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1() {
            Class var10000 = $class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$jmx$builder$JmxBuilder$_registerFactories_closure1 = class$("groovy.jmx.builder.JmxBuilder$_registerFactories_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$jmx$builder$JmxClientConnectorFactory() {
            Class var10000 = $class$groovy$jmx$builder$JmxClientConnectorFactory;
            if (var10000 == null) {
               var10000 = $class$groovy$jmx$builder$JmxClientConnectorFactory = class$("groovy.jmx.builder.JmxClientConnectorFactory");
            }

            return var10000;
         }

         // $FF: synthetic method
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      };
      var1[19].callCurrent(this, "client", var1[20].call(newClientFactory));
      var1[21].callCurrent(this, "connector", var1[22].call(newClientFactory));
      var1[23].callCurrent(this, "clientConnector", var1[24].call(newClientFactory));
      var1[25].callCurrent(this, "connectorClient", var1[26].call(newClientFactory));
   }

   public MBeanServerConnection getMBeanServer() {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(this.server)) {
         this.server = (MBeanServerConnection)ScriptBytecodeAdapter.castToType((MBeanServerConnection)ScriptBytecodeAdapter.castToType(var1[27].call($get$$class$groovy$jmx$builder$JmxBuilderTools()), $get$$class$javax$management$MBeanServerConnection()), $get$$class$javax$management$MBeanServerConnection());
      }

      return (MBeanServerConnection)ScriptBytecodeAdapter.castToType(this.server, $get$$class$javax$management$MBeanServerConnection());
   }

   public void setDefaultJmxNameDomain(String domain) {
      CallSite[] var2 = $getCallSiteArray();
      this.defaultNameDomain = (String)ScriptBytecodeAdapter.castToType(domain, $get$$class$java$lang$String());
   }

   public String getDefaultJmxNameDomain() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(this.defaultNameDomain, $get$$class$java$lang$String());
   }

   public void setDefaultJmxNameType(String type) {
      CallSite[] var2 = $getCallSiteArray();
      this.defaultNameType = (String)ScriptBytecodeAdapter.castToType(type, $get$$class$java$lang$String());
   }

   public String getDefaultJmxNameType() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(this.defaultNameType, $get$$class$java$lang$String());
   }

   public void setMBeanServer(MBeanServerConnection svr) {
      CallSite[] var2 = $getCallSiteArray();
      this.server = (MBeanServerConnection)ScriptBytecodeAdapter.castToType(svr, $get$$class$javax$management$MBeanServerConnection());
   }

   public void setMode(String mode) {
      CallSite[] var2 = $getCallSiteArray();
      this.mode = (String)ScriptBytecodeAdapter.castToType(mode, $get$$class$java$lang$String());
   }

   public String getMode() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(this.mode, $get$$class$java$lang$String());
   }

   protected Factory resolveFactory(Object name, Map attributes, Object value) {
      CallSite[] var4 = $getCallSiteArray();
      Factory factory = (Factory)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$util$FactoryBuilderSupport(), this, "resolveFactory", new Object[]{name, attributes, value}), $get$$class$groovy$util$Factory());
      if (!DefaultTypeTransformation.booleanUnbox(factory)) {
         factory = (Factory)ScriptBytecodeAdapter.castToType(var4[28].callGetPropertySafe(var4[29].callCurrent(this)), $get$$class$groovy$util$Factory());
      }

      return (Factory)ScriptBytecodeAdapter.castToType(factory, $get$$class$groovy$util$Factory());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBuilder()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$jmx$builder$JmxBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public void super$4$registerExplicitMethod(String var1, Closure var2) {
      super.registerExplicitMethod(var1, var2);
   }

   // $FF: synthetic method
   public Map super$4$popContext() {
      return super.popContext();
   }

   // $FF: synthetic method
   public Object super$4$getCurrent() {
      return super.getCurrent();
   }

   // $FF: synthetic method
   public Object super$4$getName(String var1) {
      return super.getName(var1);
   }

   // $FF: synthetic method
   public Object super$4$dispathNodeCall(Object var1, Object var2) {
      return super.dispathNodeCall(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$getParentName() {
      return super.getParentName();
   }

   // $FF: synthetic method
   public Map super$4$getExplicitMethods() {
      return super.getExplicitMethods();
   }

   // $FF: synthetic method
   public Closure super$4$addPostInstantiateDelegate(Closure var1) {
      return super.addPostInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public void super$4$preInstantiate(Object var1, Map var2, Object var3) {
      super.preInstantiate(var1, var2, var3);
   }

   // $FF: synthetic method
   public Map super$4$getContext() {
      return super.getContext();
   }

   // $FF: synthetic method
   public Factory super$4$getParentFactory() {
      return super.getParentFactory();
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(FactoryBuilderSupport var1, Closure var2) {
      return super.withBuilder(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$getParentNode() {
      return super.getParentNode();
   }

   // $FF: synthetic method
   public void super$4$autoRegisterNodes() {
      super.autoRegisterNodes();
   }

   // $FF: synthetic method
   public Object super$4$build(Script var1) {
      return super.build(var1);
   }

   // $FF: synthetic method
   public void super$4$postInstantiate(Object var1, Map var2, Object var3) {
      super.postInstantiate(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Closure[] super$4$resolveExplicitProperty(String var1) {
      return super.resolveExplicitProperty(var1);
   }

   // $FF: synthetic method
   public void super$4$removePostInstantiateDelegate(Closure var1) {
      super.removePostInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public Set super$4$getRegistrationGroups() {
      return super.getRegistrationGroups();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Map super$4$getFactories() {
      return super.getFactories();
   }

   // $FF: synthetic method
   public List super$4$getPreInstantiateDelegates() {
      return super.getPreInstantiateDelegates();
   }

   // $FF: synthetic method
   public Closure super$4$getNameMappingClosure() {
      return super.getNameMappingClosure();
   }

   // $FF: synthetic method
   public Object super$4$build(String var1, GroovyClassLoader var2) {
      return super.build(var1, var2);
   }

   // $FF: synthetic method
   public Closure super$4$addAttributeDelegate(Closure var1) {
      return super.addAttributeDelegate(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$4$setNameMappingClosure(Closure var1) {
      super.setNameMappingClosure(var1);
   }

   // $FF: synthetic method
   public Map super$4$getLocalExplicitMethods() {
      return super.getLocalExplicitMethods();
   }

   // $FF: synthetic method
   public Object super$4$invokeMethod(String var1) {
      return super.invokeMethod(var1);
   }

   // $FF: synthetic method
   public Map super$4$getLocalExplicitProperties() {
      return super.getLocalExplicitProperties();
   }

   // $FF: synthetic method
   public void super$4$removeAttributeDelegate(Closure var1) {
      super.removeAttributeDelegate(var1);
   }

   // $FF: synthetic method
   public Object super$4$postNodeCompletion(Object var1, Object var2) {
      return super.postNodeCompletion(var1, var2);
   }

   // $FF: synthetic method
   public Map super$4$getExplicitProperties() {
      return super.getExplicitProperties();
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getCurrentBuilder() {
      return super.getCurrentBuilder();
   }

   // $FF: synthetic method
   public Set super$4$getRegistrationGroupItems(String var1) {
      return super.getRegistrationGroupItems(var1);
   }

   // $FF: synthetic method
   public void super$4$registerExplicitProperty(String var1, String var2, Closure var3, Closure var4) {
      super.registerExplicitProperty(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public List super$4$getPostInstantiateDelegates() {
      return super.getPostInstantiateDelegates();
   }

   // $FF: synthetic method
   public LinkedList super$4$getContexts() {
      return super.getContexts();
   }

   // $FF: synthetic method
   public void super$4$addDisposalClosure(Closure var1) {
      super.addDisposalClosure(var1);
   }

   // $FF: synthetic method
   public void super$4$handleNodeAttributes(Object var1, Map var2) {
      super.handleNodeAttributes(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$getCurrentName() {
      return super.getCurrentName();
   }

   // $FF: synthetic method
   public void super$4$setParent(Object var1, Object var2) {
      super.setParent(var1, var2);
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getChildBuilder() {
      return super.getChildBuilder();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public Map super$4$getParentContext() {
      return super.getParentContext();
   }

   // $FF: synthetic method
   public void super$4$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public List super$4$getAttributeDelegates() {
      return super.getAttributeDelegates();
   }

   // $FF: synthetic method
   public Map super$4$getContinuationData() {
      return super.getContinuationData();
   }

   // $FF: synthetic method
   public Closure super$4$addPostNodeCompletionDelegate(Closure var1) {
      return super.addPostNodeCompletionDelegate(var1);
   }

   // $FF: synthetic method
   public Object super$4$build(Class var1) {
      return super.build(var1);
   }

   // $FF: synthetic method
   public void super$4$registerFactory(String var1, String var2, Factory var3) {
      super.registerFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public Closure super$4$addPreInstantiateDelegate(Closure var1) {
      return super.addPreInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$4$setClosureDelegate(Closure var1, Object var2) {
      super.setClosureDelegate(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$setVariable(String var1, Object var2) {
      super.setVariable(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$getContextAttribute(String var1) {
      return super.getContextAttribute(var1);
   }

   // $FF: synthetic method
   public Map super$4$getVariables() {
      return super.getVariables();
   }

   // $FF: synthetic method
   public void super$4$restoreFromContinuationData(Map var1) {
      super.restoreFromContinuationData(var1);
   }

   // $FF: synthetic method
   public Object super$4$createNode(Object var1, Map var2, Object var3) {
      return super.createNode(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$newContext() {
      super.newContext();
   }

   // $FF: synthetic method
   public void super$4$setNodeAttributes(Object var1, Map var2) {
      super.setNodeAttributes(var1, var2);
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getProxyBuilder() {
      return super.getProxyBuilder();
   }

   // $FF: synthetic method
   public Object super$4$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(FactoryBuilderSupport var1, String var2, Closure var3) {
      return super.withBuilder(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$4$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$4$nodeCompleted(Object var1, Object var2) {
      super.nodeCompleted(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(Map var1, FactoryBuilderSupport var2, String var3, Closure var4) {
      return super.withBuilder(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$4$registerBeanFactory(String var1, Class var2) {
      super.registerBeanFactory(var1, var2);
   }

   // $FF: synthetic method
   public List super$4$getPostNodeCompletionDelegates() {
      return super.getPostNodeCompletionDelegates();
   }

   // $FF: synthetic method
   public void super$4$registerExplicitProperty(String var1, Closure var2, Closure var3) {
      super.registerExplicitProperty(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$registerFactory(String var1, Factory var2) {
      super.registerFactory(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$4$checkExplicitMethod(String var1, Object var2, Reference var3) {
      return super.checkExplicitMethod(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Object super$4$getVariable(String var1) {
      return super.getVariable(var1);
   }

   // $FF: synthetic method
   public void super$4$dispose() {
      super.dispose();
   }

   // $FF: synthetic method
   public void super$4$removePreInstantiateDelegate(Closure var1) {
      super.removePreInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public Factory super$4$resolveFactory(Object var1, Map var2, Object var3) {
      return super.resolveFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public Factory super$4$getCurrentFactory() {
      return super.getCurrentFactory();
   }

   // $FF: synthetic method
   public Map super$4$getLocalFactories() {
      return super.getLocalFactories();
   }

   // $FF: synthetic method
   public void super$4$removePostNodeCompletionDelegate(Closure var1) {
      super.removePostNodeCompletionDelegate(var1);
   }

   // $FF: synthetic method
   public Closure super$4$resolveExplicitMethod(String var1, Object var2) {
      return super.resolveExplicitMethod(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$4$registerBeanFactory(String var1, String var2, Class var3) {
      super.registerBeanFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$setProxyBuilder(FactoryBuilderSupport var1) {
      super.setProxyBuilder(var1);
   }

   // $FF: synthetic method
   public void super$4$registerExplicitMethod(String var1, String var2, Closure var3) {
      super.registerExplicitMethod(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$reset() {
      super.reset();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "DEFAULT_DOMAIN";
      var0[1] = "DEFAULT_NAME_TYPE";
      var0[2] = "registerFactories";
      var0[3] = "registerFactory";
      var0[4] = "<$constructor$>";
      var0[5] = "registerFactory";
      var0[6] = "<$constructor$>";
      var0[7] = "registerFactory";
      var0[8] = "<$constructor$>";
      var0[9] = "registerFactory";
      var0[10] = "<$constructor$>";
      var0[11] = "registerFactory";
      var0[12] = "<$constructor$>";
      var0[13] = "registerFactory";
      var0[14] = "<$constructor$>";
      var0[15] = "<$constructor$>";
      var0[16] = "registerFactory";
      var0[17] = "registerFactory";
      var0[18] = "registerFactory";
      var0[19] = "registerFactory";
      var0[20] = "call";
      var0[21] = "registerFactory";
      var0[22] = "call";
      var0[23] = "registerFactory";
      var0[24] = "call";
      var0[25] = "registerFactory";
      var0[26] = "call";
      var0[27] = "getMBeanServer";
      var0[28] = "childFactory";
      var0[29] = "getParentFactory";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[30];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBuilder(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxListenerFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxListenerFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxListenerFactory = class$("groovy.jmx.builder.JmxListenerFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxEmitterFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxEmitterFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxEmitterFactory = class$("groovy.jmx.builder.JmxEmitterFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxBeanFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeanFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeanFactory = class$("groovy.jmx.builder.JmxBeanFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxBeansFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeansFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeansFactory = class$("groovy.jmx.builder.JmxBeansFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$MBeanServerConnection() {
      Class var10000 = $class$javax$management$MBeanServerConnection;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServerConnection = class$("javax.management.MBeanServerConnection");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$FactoryBuilderSupport() {
      Class var10000 = $class$groovy$util$FactoryBuilderSupport;
      if (var10000 == null) {
         var10000 = $class$groovy$util$FactoryBuilderSupport = class$("groovy.util.FactoryBuilderSupport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxBuilder() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilder = class$("groovy.jmx.builder.JmxBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxBeanExportFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeanExportFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeanExportFactory = class$("groovy.jmx.builder.JmxBeanExportFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxServerConnectorFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory = class$("groovy.jmx.builder.JmxServerConnectorFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$Factory() {
      Class var10000 = $class$groovy$util$Factory;
      if (var10000 == null) {
         var10000 = $class$groovy$util$Factory = class$("groovy.util.Factory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxTimerFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxTimerFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxTimerFactory = class$("groovy.jmx.builder.JmxTimerFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxBuilderTools() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilderTools;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilderTools = class$("groovy.jmx.builder.JmxBuilderTools");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
