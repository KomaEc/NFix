package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxBeansFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204306L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204306 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServer;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeansFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;

   public JmxBeansFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeParam, Map nodeAttribs) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(nodeParam) && nodeParam instanceof List ? Boolean.FALSE : Boolean.TRUE)) {
         throw (Throwable)var5[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", "' requires a list of object to be exported."})));
      } else {
         JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
         MBeanServer server = new Reference((MBeanServer)ScriptBytecodeAdapter.castToType(var5[1].call(fsb), $get$$class$javax$management$MBeanServer()));
         Object metaMaps = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         Object map = null;
         var5[2].call(nodeParam, (Object)(new GeneratedClosure(this, this, server, metaMaps) {
            private Reference<T> server;
            private Reference<T> metaMaps;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$javax$management$MBeanServer;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBuilderTools;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.server = (Reference)server;
               this.metaMaps = (Reference)metaMaps;
            }

            public Object doCall(Object target) {
               Object targetx = new Reference(target);
               CallSite[] var3 = $getCallSiteArray();
               Object metaMap = var3[0].call($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), (Object)targetx.get());
               Object var10000 = var3[1].callGetProperty(metaMap);
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = this.server.get();
               }

               ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1(), metaMap, "server");
               ScriptBytecodeAdapter.setProperty(var3[2].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var3[3].call(targetx.get())), $get$$class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1(), metaMap, "isMBean");
               return var3[4].call(this.metaMaps.get(), metaMap);
            }

            public MBeanServer getServer() {
               CallSite[] var1 = $getCallSiteArray();
               return (MBeanServer)ScriptBytecodeAdapter.castToType(this.server.get(), $get$$class$javax$management$MBeanServer());
            }

            public Object getMetaMaps() {
               CallSite[] var1 = $getCallSiteArray();
               return this.metaMaps.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1()) {
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
               var0[0] = "buildObjectMapFrom";
               var0[1] = "server";
               var0[2] = "isClassMBean";
               var0[3] = "getClass";
               var0[4] = "add";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxBuilderTools() {
               Class var10000 = $class$groovy$jmx$builder$JmxBuilderTools;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxBuilderTools = class$("groovy.jmx.builder.JmxBuilderTools");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxBeansFactory$_newInstance_closure1 = class$("groovy.jmx.builder.JmxBeansFactory$_newInstance_closure1");
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
         return (Object)ScriptBytecodeAdapter.castToType(metaMaps.get(), $get$$class$java$lang$Object());
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
      Object parentNode = new Reference(parentNode);
      CallSite[] var5 = $getCallSiteArray();
      JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
      MBeanServer server = (MBeanServer)ScriptBytecodeAdapter.castToType(var5[3].call(fsb), $get$$class$javax$management$MBeanServer());
      Object var10000 = var5[4].callGetPropertySafe(var5[5].call(fsb));
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = "replace";
      }

      Object regPolicy = new Reference(var10000);
      var5[6].call(thisNode, (Object)(new GeneratedClosure(this, this, parentNode, regPolicy) {
         private Reference<T> parentNode;
         private Reference<T> regPolicy;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$java$util$Iterator;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxBuilderTools;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.parentNode = (Reference)parentNode;
            this.regPolicy = (Reference)regPolicy;
         }

         public Object doCall(Object metaMap) {
            Object metaMapx = new Reference(metaMap);
            CallSite[] var3 = $getCallSiteArray();
            Object registeredBean = new Reference(var3[0].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), this.regPolicy.get(), metaMapx.get()));
            if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(registeredBean.get()) && ScriptBytecodeAdapter.compareEqual(this.regPolicy.get(), "replace") ? Boolean.TRUE : Boolean.FALSE)) {
               Reference i = new Reference((Iterator)ScriptBytecodeAdapter.castToType(var3[1].call(this.parentNode.get()), $get$$class$java$util$Iterator()));

               while(DefaultTypeTransformation.booleanUnbox(var3[2].call(i.get()))) {
                  Object exportedBean = var3[3].call(i.get());
                  if (DefaultTypeTransformation.booleanUnbox(var3[4].call(var3[5].call(exportedBean), var3[6].callGetProperty(metaMapx.get())))) {
                     var3[7].call(i.get());
                  }
               }
            }

            return DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(this.parentNode.get(), (Object)null) && DefaultTypeTransformation.booleanUnbox(registeredBean.get()) ? Boolean.TRUE : Boolean.FALSE) ? var3[8].call(this.parentNode.get(), registeredBean.get()) : null;
         }

         public Object getParentNode() {
            CallSite[] var1 = $getCallSiteArray();
            return (Object)ScriptBytecodeAdapter.castToType(this.parentNode.get(), $get$$class$java$lang$Object());
         }

         public Object getRegPolicy() {
            CallSite[] var1 = $getCallSiteArray();
            return this.regPolicy.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2()) {
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
            var0[0] = "registerMBeanFromMap";
            var0[1] = "iterator";
            var0[2] = "hasNext";
            var0[3] = "next";
            var0[4] = "equals";
            var0[5] = "name";
            var0[6] = "jmxName";
            var0[7] = "remove";
            var0[8] = "add";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[9];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2(), var0);
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
         private static Class $get$$class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2() {
            Class var10000 = $class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$jmx$builder$JmxBeansFactory$_onNodeCompleted_closure2 = class$("groovy.jmx.builder.JmxBeansFactory$_onNodeCompleted_closure2");
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
         private static Class $get$$class$java$util$Iterator() {
            Class var10000 = $class$java$util$Iterator;
            if (var10000 == null) {
               var10000 = $class$java$util$Iterator = class$("java.util.Iterator");
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
      }));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeansFactory()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxBeansFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxBeansFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxBeansFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "each";
      var0[3] = "getMBeanServer";
      var0[4] = "registrationPolicy";
      var0[5] = "getParentFactory";
      var0[6] = "each";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[7];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeansFactory(), var0);
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
   private static Class $get$$class$javax$management$MBeanServer() {
      Class var10000 = $class$javax$management$MBeanServer;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServer = class$("javax.management.MBeanServer");
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
