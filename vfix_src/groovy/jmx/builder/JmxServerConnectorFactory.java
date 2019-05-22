package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxServerConnectorFactory extends AbstractFactory implements GroovyObject {
   private static List SUPPORTED_PROTOCOLS = (List)ScriptBytecodeAdapter.createList(new Object[]{"rmi", "jrmp", "iiop", "jmxmp"});
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204362L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204362 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$management$remote$rmi$RMIConnectorServer;
   // $FF: synthetic field
   private static Class $class$javax$management$remote$JMXServiceURL;
   // $FF: synthetic field
   private static Class $class$javax$rmi$ssl$SslRMIClientSocketFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$javax$management$remote$JMXConnectorServer;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxServerConnectorFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServer;
   // $FF: synthetic field
   private static Class $class$javax$management$remote$JMXConnectorServerFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$javax$rmi$ssl$SslRMIServerSocketFactory;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;

   public JmxServerConnectorFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeArgs, Map nodeAttribs) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(nodeArgs)) {
         throw (Throwable)var5[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", "' only supports named attributes."})));
      } else {
         JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
         Object var10000 = var5[1].callSafe(nodeAttribs, (Object)"protocol");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[2].callSafe(nodeAttribs, (Object)"transport");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = "rmi";
            }
         }

         Object protocol = var10000;
         Object port = var5[3].callSafe(nodeAttribs, (Object)"port");
         var10000 = var5[4].callSafe(nodeAttribs, (Object)"host");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[5].callSafe(nodeAttribs, (Object)"address");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = "localhost";
            }
         }

         Object host = var10000;
         Object url = var5[6].callSafe(nodeAttribs, (Object)"url");
         var10000 = var5[7].callSafe(nodeAttribs, (Object)"properties");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[8].callSafe(nodeAttribs, (Object)"props");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var5[9].callSafe(nodeAttribs, (Object)"env");
            }
         }

         Object props = var10000;
         Object env = var5[10].callCurrent(this, protocol, port, props);
         var5[11].call(nodeAttribs);
         if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(port) && !DefaultTypeTransformation.booleanUnbox(url) ? Boolean.TRUE : Boolean.FALSE)) {
            throw (Throwable)var5[12].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", " requires attribute 'port' to specify server's port number."})));
         } else if (!DefaultTypeTransformation.booleanUnbox(var5[13].call(SUPPORTED_PROTOCOLS, (Object)protocol))) {
            throw (Throwable)var5[14].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)var5[15].call(new GStringImpl(new Object[]{protocol}, new String[]{"Connector protocol '", " is not supported at this time. "}), (Object)(new GStringImpl(new Object[]{SUPPORTED_PROTOCOLS}, new String[]{"Supported protocols are ", "."}))));
         } else {
            MBeanServer server = (MBeanServer)ScriptBytecodeAdapter.castToType(var5[16].call(fsb), $get$$class$javax$management$MBeanServer());
            JMXServiceURL serviceUrl = (JMXServiceURL)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(url) ? var5[17].callConstructor($get$$class$javax$management$remote$JMXServiceURL(), (Object)url) : var5[18].callCurrent(this, protocol, host, port), $get$$class$javax$management$remote$JMXServiceURL());
            JMXConnectorServer connector = (JMXConnectorServer)ScriptBytecodeAdapter.castToType(var5[19].call($get$$class$javax$management$remote$JMXConnectorServerFactory(), serviceUrl, env, server), $get$$class$javax$management$remote$JMXConnectorServer());
            return (Object)ScriptBytecodeAdapter.castToType(connector, $get$$class$java$lang$Object());
         }
      }
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map nodeAttribs) {
      CallSite[] var4 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   public boolean isLeaf() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parentNode, Object thisNode) {
      CallSite[] var4 = $getCallSiteArray();
   }

   private Map confiConnectorProperties(String protocol, int port, Map props) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(props)) {
         return (Map)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$Map());
      } else {
         HashMap env = new Reference(var4[20].callConstructor($get$$class$java$util$HashMap()));
         Object var10000 = var4[21].call(props, (Object)"com.sun.management.jmxremote.authenticate");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var4[22].call(props, (Object)"authenticate");
         }

         Object auth = var10000;
         var4[23].call(env.get(), "com.sun.management.jmxremote.authenticate", auth);
         var10000 = var4[24].call(props, (Object)"com.sun.management.jmxremote.password.file");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var4[25].call(props, (Object)"passwordFile");
         }

         Object pFile = var10000;
         var4[26].call(env.get(), "com.sun.management.jmxremote.password.file", pFile);
         var10000 = var4[27].call(props, (Object)"com.sun.management.jmxremote.access.file");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var4[28].call(props, (Object)"accessFile");
         }

         Object aFile = var10000;
         var4[29].call(env.get(), "com.sun.management.jmxremote.access.file", aFile);
         var10000 = var4[30].call(props, (Object)"com.sun.management.jmxremote. ssl");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var4[31].call(props, (Object)"sslEnabled");
         }

         Object ssl = var10000;
         var4[32].call(env.get(), "com.sun.management.jmxremote.ssl", ssl);
         if (ScriptBytecodeAdapter.compareEqual(protocol, "rmi") && DefaultTypeTransformation.booleanUnbox(ssl)) {
            var10000 = var4[33].call(props, (Object)var4[34].callGetProperty($get$$class$javax$management$remote$rmi$RMIConnectorServer()));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var4[35].callConstructor($get$$class$javax$rmi$ssl$SslRMIClientSocketFactory());
            }

            Object csf = var10000;
            var10000 = var4[36].call(props, (Object)var4[37].callGetProperty($get$$class$javax$management$remote$rmi$RMIConnectorServer()));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var4[38].callConstructor($get$$class$javax$rmi$ssl$SslRMIServerSocketFactory());
            }

            Object ssf = var10000;
            var4[39].call(env.get(), var4[40].callGetProperty($get$$class$javax$management$remote$rmi$RMIConnectorServer()), csf);
            var4[41].call(env.get(), var4[42].callGetProperty($get$$class$javax$management$remote$rmi$RMIConnectorServer()), ssf);
         }

         var4[43].call(props, (Object)(new GeneratedClosure(this, this, env) {
            private Reference<T> env;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1;
            // $FF: synthetic field
            private static Class $class$java$util$HashMap;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.env = (Reference)env;
            }

            public Object doCall(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[0].call(this.env.get(), keyx.get(), valuex.get());
            }

            public Object call(Object key, Object value) {
               Object keyx = new Reference(key);
               Object valuex = new Reference(value);
               CallSite[] var5 = $getCallSiteArray();
               return var5[1].callCurrent(this, keyx.get(), valuex.get());
            }

            public HashMap<String, Object> getEnv() {
               CallSite[] var1 = $getCallSiteArray();
               return (HashMap)ScriptBytecodeAdapter.castToType(this.env.get(), $get$$class$java$util$HashMap());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1()) {
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
               var0[0] = "put";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory$_confiConnectorProperties_closure1 = class$("groovy.jmx.builder.JmxServerConnectorFactory$_confiConnectorProperties_closure1");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$util$HashMap() {
               Class var10000 = $class$java$util$HashMap;
               if (var10000 == null) {
                  var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
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
         }));
         return (Map)ScriptBytecodeAdapter.castToType(var4[44].call(props), $get$$class$java$util$Map());
      }
   }

   private JMXServiceURL generateServiceUrl(Object protocol, Object host, Object port) {
      CallSite[] var4 = $getCallSiteArray();
      String url = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{protocol, protocol, host, port}, new String[]{"service:jmx:", ":///jndi/", "://", ":", "/jmxrmi"}), $get$$class$java$lang$String());
      return (JMXServiceURL)ScriptBytecodeAdapter.castToType(var4[45].callConstructor($get$$class$javax$management$remote$JMXServiceURL(), (Object)url), $get$$class$javax$management$remote$JMXServiceURL());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxServerConnectorFactory()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$jmx$builder$JmxServerConnectorFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxServerConnectorFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxServerConnectorFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   // $FF: synthetic method
   public Map this$3$confiConnectorProperties(String var1, int var2, Map var3) {
      return this.confiConnectorProperties(var1, var2, var3);
   }

   // $FF: synthetic method
   public JMXServiceURL this$3$generateServiceUrl(Object var1, Object var2, Object var3) {
      return this.generateServiceUrl(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$2$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$onNodeCompleted(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.onNodeCompleted(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$onFactoryRegistration(FactoryBuilderSupport var1, String var2, String var3) {
      super.onFactoryRegistration(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isHandlesNodeChildren() {
      return super.isHandlesNodeChildren();
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "remove";
      var0[2] = "remove";
      var0[3] = "remove";
      var0[4] = "remove";
      var0[5] = "remove";
      var0[6] = "remove";
      var0[7] = "remove";
      var0[8] = "remove";
      var0[9] = "remove";
      var0[10] = "confiConnectorProperties";
      var0[11] = "clear";
      var0[12] = "<$constructor$>";
      var0[13] = "contains";
      var0[14] = "<$constructor$>";
      var0[15] = "plus";
      var0[16] = "getMBeanServer";
      var0[17] = "<$constructor$>";
      var0[18] = "generateServiceUrl";
      var0[19] = "newJMXConnectorServer";
      var0[20] = "<$constructor$>";
      var0[21] = "remove";
      var0[22] = "remove";
      var0[23] = "put";
      var0[24] = "remove";
      var0[25] = "remove";
      var0[26] = "put";
      var0[27] = "remove";
      var0[28] = "remove";
      var0[29] = "put";
      var0[30] = "remove";
      var0[31] = "remove";
      var0[32] = "put";
      var0[33] = "remove";
      var0[34] = "RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE";
      var0[35] = "<$constructor$>";
      var0[36] = "remove";
      var0[37] = "RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE";
      var0[38] = "<$constructor$>";
      var0[39] = "put";
      var0[40] = "RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE";
      var0[41] = "put";
      var0[42] = "RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE";
      var0[43] = "each";
      var0[44] = "clear";
      var0[45] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[46];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxServerConnectorFactory(), var0);
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
   private static Class $get$$class$javax$management$remote$rmi$RMIConnectorServer() {
      Class var10000 = $class$javax$management$remote$rmi$RMIConnectorServer;
      if (var10000 == null) {
         var10000 = $class$javax$management$remote$rmi$RMIConnectorServer = class$("javax.management.remote.rmi.RMIConnectorServer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$remote$JMXServiceURL() {
      Class var10000 = $class$javax$management$remote$JMXServiceURL;
      if (var10000 == null) {
         var10000 = $class$javax$management$remote$JMXServiceURL = class$("javax.management.remote.JMXServiceURL");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$rmi$ssl$SslRMIClientSocketFactory() {
      Class var10000 = $class$javax$rmi$ssl$SslRMIClientSocketFactory;
      if (var10000 == null) {
         var10000 = $class$javax$rmi$ssl$SslRMIClientSocketFactory = class$("javax.rmi.ssl.SslRMIClientSocketFactory");
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
   private static Class $get$$class$groovy$jmx$builder$JmxBuilderException() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilderException;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilderException = class$("groovy.jmx.builder.JmxBuilderException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$remote$JMXConnectorServer() {
      Class var10000 = $class$javax$management$remote$JMXConnectorServer;
      if (var10000 == null) {
         var10000 = $class$javax$management$remote$JMXConnectorServer = class$("javax.management.remote.JMXConnectorServer");
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
   private static Class $get$$class$groovy$jmx$builder$JmxServerConnectorFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxServerConnectorFactory = class$("groovy.jmx.builder.JmxServerConnectorFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
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
   private static Class $get$$class$javax$management$MBeanServer() {
      Class var10000 = $class$javax$management$MBeanServer;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServer = class$("javax.management.MBeanServer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$remote$JMXConnectorServerFactory() {
      Class var10000 = $class$javax$management$remote$JMXConnectorServerFactory;
      if (var10000 == null) {
         var10000 = $class$javax$management$remote$JMXConnectorServerFactory = class$("javax.management.remote.JMXConnectorServerFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$rmi$ssl$SslRMIServerSocketFactory() {
      Class var10000 = $class$javax$rmi$ssl$SslRMIServerSocketFactory;
      if (var10000 == null) {
         var10000 = $class$javax$rmi$ssl$SslRMIServerSocketFactory = class$("javax.rmi.ssl.SslRMIServerSocketFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$HashMap() {
      Class var10000 = $class$java$util$HashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
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
