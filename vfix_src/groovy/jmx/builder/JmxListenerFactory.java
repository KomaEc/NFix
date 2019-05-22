package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.NotificationFilterSupport;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxListenerFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204359L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204359 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxListenerFactory;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxEventListener;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$javax$management$NotificationFilterSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;

   public JmxListenerFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeParam, Map nodeAttribs) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(nodeParam)) {
         throw (Throwable)var5[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", "' only supports named attributes."})));
      } else {
         JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
         Object server = var5[1].call(fsb);
         Object map = var5[2].call($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), (Object)nodeAttribs);
         Object broadcaster = var5[3].call(map, (Object)"from");

         try {
            Object eventType = (String)ScriptBytecodeAdapter.castToType(var5[4].call(map, (Object)"event"), $get$$class$java$lang$String());
            if (!DefaultTypeTransformation.booleanUnbox(var5[5].call(server, broadcaster))) {
               throw (Throwable)var5[6].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{var5[7].call(broadcaster)}, new String[]{"MBean '", "' is not registered in server."})));
            }

            if (DefaultTypeTransformation.booleanUnbox(eventType)) {
               NotificationFilterSupport filter = var5[8].callConstructor($get$$class$javax$management$NotificationFilterSupport());
               var5[9].call(filter, (Object)eventType);
               var5[10].call(server, broadcaster, var5[11].call($get$$class$groovy$jmx$builder$JmxEventListener()), filter, map);
            } else {
               var5[12].call(server, broadcaster, var5[13].call($get$$class$groovy$jmx$builder$JmxEventListener()), (Object)null, map);
            }
         } catch (InstanceNotFoundException var14) {
            throw (Throwable)var5[14].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)var14);
         } finally {
            ;
         }

         return (Object)ScriptBytecodeAdapter.castToType(map, $get$$class$java$lang$Object());
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
         var4[15].call(parentNode, thisNode);
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxListenerFactory()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxListenerFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxListenerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxListenerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "createListenerMap";
      var0[3] = "get";
      var0[4] = "get";
      var0[5] = "isRegistered";
      var0[6] = "<$constructor$>";
      var0[7] = "toString";
      var0[8] = "<$constructor$>";
      var0[9] = "enableType";
      var0[10] = "addNotificationListener";
      var0[11] = "getListner";
      var0[12] = "addNotificationListener";
      var0[13] = "getListner";
      var0[14] = "<$constructor$>";
      var0[15] = "add";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[16];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxListenerFactory(), var0);
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
   private static Class $get$$class$groovy$jmx$builder$JmxEventListener() {
      Class var10000 = $class$groovy$jmx$builder$JmxEventListener;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxEventListener = class$("groovy.jmx.builder.JmxEventListener");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder() {
      Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder = class$("groovy.jmx.builder.JmxMetaMapBuilder");
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
