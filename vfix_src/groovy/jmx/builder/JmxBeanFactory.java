package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import javax.management.MBeanServer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxBeanFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204303L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204303 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanFactory;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServer;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$java$util$Iterator;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;

   public JmxBeanFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeParam, Map nodeAttributes) {
      CallSite[] var5 = $getCallSiteArray();
      JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
      MBeanServer server = (MBeanServer)ScriptBytecodeAdapter.castToType(var5[0].call(fsb), $get$$class$javax$management$MBeanServer());
      Object metaMap = null;
      Object target = null;
      if (DefaultTypeTransformation.booleanUnbox(nodeParam)) {
         target = nodeParam;
         var5[1].callCurrent(this, (Object)nodeParam);
         metaMap = var5[2].call($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), (Object)nodeParam);
      } else if (DefaultTypeTransformation.booleanUnbox(nodeAttributes)) {
         target = var5[3].callGetProperty(nodeAttributes);
         var5[4].callCurrent(this, (Object)target);
         metaMap = var5[5].call($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), target, nodeAttributes);
      }

      Object var10000 = var5[6].callGetProperty(metaMap);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = server;
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxBeanFactory(), metaMap, "server");
      ScriptBytecodeAdapter.setProperty(var5[7].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var5[8].call(target)), $get$$class$groovy$jmx$builder$JmxBeanFactory(), metaMap, "isMBean");
      return (Object)ScriptBytecodeAdapter.castToType(metaMap, $get$$class$java$lang$Object());
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map nodeAttribs) {
      CallSite[] var4 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parentNode, Object thisNode) {
      CallSite[] var4 = $getCallSiteArray();
      JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
      MBeanServer server = (MBeanServer)ScriptBytecodeAdapter.castToType(var4[9].call(fsb), $get$$class$javax$management$MBeanServer());
      Object metaMap = thisNode;
      Object var10000 = var4[10].callGetPropertySafe(var4[11].call(fsb));
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = "replace";
      }

      Object regPolicy = var10000;
      Object registeredBean = var4[12].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), regPolicy, thisNode);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(registeredBean) && ScriptBytecodeAdapter.compareEqual(regPolicy, "replace") ? Boolean.TRUE : Boolean.FALSE)) {
         Iterator i = (Iterator)ScriptBytecodeAdapter.castToType(var4[13].call(parentNode), $get$$class$java$util$Iterator());

         while(DefaultTypeTransformation.booleanUnbox(var4[14].call(i))) {
            Object exportedBean = var4[15].call(i);
            if (DefaultTypeTransformation.booleanUnbox(var4[16].call(var4[17].call(exportedBean), var4[18].callGetProperty(metaMap)))) {
               var4[19].call(i);
            }
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(parentNode, (Object)null) && DefaultTypeTransformation.booleanUnbox(registeredBean) ? Boolean.TRUE : Boolean.FALSE)) {
         var4[20].call(parentNode, registeredBean);
      }

   }

   public boolean isLeaf() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   private Object initMetaMap(Object target) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(target)) {
         throw (Throwable)var2[21].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)var2[22].call(var2[23].call("You must specify a target object to ", (Object)" export as MBean i.e. JmxBuilder.bean(targetInstance), "), (Object)" JmxBuilder.bean([target:instance]), JmxBuilder.beans([instanceList])."));
      } else {
         Object metaMap = ScriptBytecodeAdapter.createMap(new Object[0]);
         ScriptBytecodeAdapter.setProperty(target, $get$$class$groovy$jmx$builder$JmxBeanFactory(), metaMap, "target");
         ScriptBytecodeAdapter.setProperty(var2[24].callGetProperty(var2[25].callGetProperty(target)), $get$$class$groovy$jmx$builder$JmxBeanFactory(), metaMap, "name");
         return metaMap;
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeanFactory()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxBeanFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxBeanFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxBeanFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object this$3$initMetaMap(Object var1) {
      return this.initMetaMap(var1);
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
      var0[0] = "getMBeanServer";
      var0[1] = "initMetaMap";
      var0[2] = "buildObjectMapFrom";
      var0[3] = "target";
      var0[4] = "initMetaMap";
      var0[5] = "buildObjectMapFrom";
      var0[6] = "server";
      var0[7] = "isClassMBean";
      var0[8] = "getClass";
      var0[9] = "getMBeanServer";
      var0[10] = "registrationPolicy";
      var0[11] = "getParentFactory";
      var0[12] = "registerMBeanFromMap";
      var0[13] = "iterator";
      var0[14] = "hasNext";
      var0[15] = "next";
      var0[16] = "equals";
      var0[17] = "name";
      var0[18] = "jmxName";
      var0[19] = "remove";
      var0[20] = "add";
      var0[21] = "<$constructor$>";
      var0[22] = "plus";
      var0[23] = "plus";
      var0[24] = "canonicalName";
      var0[25] = "class";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[26];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeanFactory(), var0);
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
   private static Class $get$$class$groovy$jmx$builder$JmxBeanFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeanFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeanFactory = class$("groovy.jmx.builder.JmxBeanFactory");
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
   private static Class $get$$class$groovy$jmx$builder$JmxBuilderException() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilderException;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilderException = class$("groovy.jmx.builder.JmxBuilderException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Iterator() {
      Class var10000 = $class$java$util$Iterator;
      if (var10000 == null) {
         var10000 = $class$java$util$Iterator = class$("java.util.Iterator");
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
