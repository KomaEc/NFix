package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.NotificationFilterSupport;
import javax.management.ObjectName;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxEmitterFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204314L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204314 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxEmitterFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class $class$groovy$util$GroovyMBean;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServer;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$javax$management$NotificationFilterSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxEventEmitter;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;

   public JmxEmitterFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeParam, Map nodeAttribs) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(nodeParam)) {
         throw (Throwable)var5[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", "' only supports named attributes."})));
      } else {
         JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
         Object server = new Reference((MBeanServer)ScriptBytecodeAdapter.castToType(var5[1].call(fsb), $get$$class$javax$management$MBeanServer()));
         Object emitter = new Reference(var5[2].callConstructor($get$$class$groovy$jmx$builder$JmxEventEmitter()));
         Object name = new Reference(var5[3].callCurrent(this, fsb, emitter.get(), var5[4].call(nodeAttribs, (Object)"name")));
         Object var10000 = var5[5].call(nodeAttribs, (Object)"event");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[6].call(nodeAttribs, (Object)"type");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = "jmx.builder.event.emitter";
            }
         }

         Object event = var10000;
         var10000 = var5[7].call(nodeAttribs, (Object)"listeners");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[8].call(nodeAttribs, (Object)"recipients");
         }

         Object listeners = var10000;
         NotificationFilterSupport filter = new Reference((NotificationFilterSupport)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$NotificationFilterSupport()));
         if (DefaultTypeTransformation.booleanUnbox(event)) {
            filter.set(var5[9].callConstructor($get$$class$javax$management$NotificationFilterSupport()));
            var5[10].call(filter.get(), event);
            var5[11].call(emitter.get(), event);
         }

         if (DefaultTypeTransformation.booleanUnbox(var5[12].call(server.get(), name.get()))) {
            var5[13].call(server.get(), name.get());
         }

         var5[14].call(server.get(), emitter.get(), name.get());
         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(listeners) && !(listeners instanceof List) ? Boolean.TRUE : Boolean.FALSE)) {
            throw (Throwable)var5[15].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Listeners must be provided as a list [listner1,...,listenerN]");
         } else {
            var5[16].call(listeners, (Object)(new GeneratedClosure(this, this, name, server, emitter, filter) {
               private Reference<T> name;
               private Reference<T> server;
               private Reference<T> emitter;
               private Reference<T> filter;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$javax$management$ObjectName;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1;
               // $FF: synthetic field
               private static Class $class$javax$management$NotificationFilterSupport;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxBuilderException;

               public {
                  CallSite[] var7 = $getCallSiteArray();
                  this.name = (Reference)name;
                  this.server = (Reference)server;
                  this.emitter = (Reference)emitter;
                  this.filter = (Reference)filter;
               }

               public Object doCall(Object param1) {
                  // $FF: Couldn't be decompiled
               }

               public Object getName() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.name.get();
               }

               public Object getServer() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.server.get();
               }

               public Object getEmitter() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.emitter.get();
               }

               public NotificationFilterSupport getFilter() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (NotificationFilterSupport)ScriptBytecodeAdapter.castToType(this.filter.get(), $get$$class$javax$management$NotificationFilterSupport());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1()) {
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
                  var0[1] = "addNotificationListener";
                  var0[2] = "addNotificationListener";
                  var0[3] = "<$constructor$>";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1(), var0);
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
               private static Class $get$$class$javax$management$ObjectName() {
                  Class var10000 = $class$javax$management$ObjectName;
                  if (var10000 == null) {
                     var10000 = $class$javax$management$ObjectName = class$("javax.management.ObjectName");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1() {
                  Class var10000 = $class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxEmitterFactory$_newInstance_closure1 = class$("groovy.jmx.builder.JmxEmitterFactory$_newInstance_closure1");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$javax$management$NotificationFilterSupport() {
                  Class var10000 = $class$javax$management$NotificationFilterSupport;
                  if (var10000 == null) {
                     var10000 = $class$javax$management$NotificationFilterSupport = class$("javax.management.NotificationFilterSupport");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
            return (Object)ScriptBytecodeAdapter.castToType(var5[17].callConstructor($get$$class$groovy$util$GroovyMBean(), var5[18].call(fsb), name.get()), $get$$class$java$lang$Object());
         }
      }
   }

   private ObjectName getObjectName(Object fsb, Object emitter, Object name) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(name) && name instanceof ObjectName ? Boolean.TRUE : Boolean.FALSE)) {
         return (ObjectName)ScriptBytecodeAdapter.castToType(name, $get$$class$javax$management$ObjectName());
      } else if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(name) && name instanceof String ? Boolean.TRUE : Boolean.FALSE)) {
         return (ObjectName)ScriptBytecodeAdapter.castToType(var4[19].callConstructor($get$$class$javax$management$ObjectName(), (Object)name), $get$$class$javax$management$ObjectName());
      } else {
         return !DefaultTypeTransformation.booleanUnbox(name) ? (ObjectName)ScriptBytecodeAdapter.castToType(var4[20].callConstructor($get$$class$javax$management$ObjectName(), (Object)(new GStringImpl(new Object[]{var4[21].call(fsb), var4[22].call(emitter)}, new String[]{"", ":type=Emitter,name=Emitter@", ""}))), $get$$class$javax$management$ObjectName()) : (ObjectName)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$ObjectName());
      }
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map nodeAttribs) {
      CallSite[] var4 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public boolean isLeaf() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parentNode, Object thisNode) {
      CallSite[] var4 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareNotEqual(parentNode, (Object)null)) {
         var4[23].call(parentNode, thisNode);
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxEmitterFactory()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxEmitterFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxEmitterFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxEmitterFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public ObjectName this$3$getObjectName(Object var1, Object var2, Object var3) {
      return this.getObjectName(var1, var2, var3);
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
      var0[1] = "getMBeanServer";
      var0[2] = "<$constructor$>";
      var0[3] = "getObjectName";
      var0[4] = "remove";
      var0[5] = "remove";
      var0[6] = "remove";
      var0[7] = "remove";
      var0[8] = "remove";
      var0[9] = "<$constructor$>";
      var0[10] = "enableType";
      var0[11] = "setEvent";
      var0[12] = "isRegistered";
      var0[13] = "unregisterMBean";
      var0[14] = "registerMBean";
      var0[15] = "<$constructor$>";
      var0[16] = "each";
      var0[17] = "<$constructor$>";
      var0[18] = "getMBeanServer";
      var0[19] = "<$constructor$>";
      var0[20] = "<$constructor$>";
      var0[21] = "getDefaultJmxNameDomain";
      var0[22] = "hashCode";
      var0[23] = "add";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[24];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxEmitterFactory(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
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
   private static Class $get$$class$javax$management$ObjectName() {
      Class var10000 = $class$javax$management$ObjectName;
      if (var10000 == null) {
         var10000 = $class$javax$management$ObjectName = class$("javax.management.ObjectName");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$GroovyMBean() {
      Class var10000 = $class$groovy$util$GroovyMBean;
      if (var10000 == null) {
         var10000 = $class$groovy$util$GroovyMBean = class$("groovy.util.GroovyMBean");
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$NotificationFilterSupport() {
      Class var10000 = $class$javax$management$NotificationFilterSupport;
      if (var10000 == null) {
         var10000 = $class$javax$management$NotificationFilterSupport = class$("javax.management.NotificationFilterSupport");
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
   private static Class $get$$class$groovy$jmx$builder$JmxEventEmitter() {
      Class var10000 = $class$groovy$jmx$builder$JmxEventEmitter;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxEventEmitter = class$("groovy.jmx.builder.JmxEventEmitter");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
