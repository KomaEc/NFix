package groovy.jmx.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.ObjectName;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxMetaMapBuilder implements GroovyObject {
   private static Object ATTRIB_EXCEPTION_LIST = ScriptBytecodeAdapter.createList(new Object[]{"class", "descriptor", "jmx", "metaClass"});
   private static Object OPS_EXCEPTION_LIST = ScriptBytecodeAdapter.createList(new Object[]{"clone", "equals", "finalize", "getClass", "getProperty", "hashCode", "invokeMethod", "notify", "notifyAll", "setProperty", "toString", "wait"});
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202835L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202835 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanInfoManager;
   // $FF: synthetic field
   private static Class $class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;

   public JmxMetaMapBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static Map buildObjectMapFrom(Object object) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(object)) {
         throw (Throwable)var1[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable to create MBean, missing target object.");
      } else {
         Object map = null;
         Object var10000 = var1[1].call(var1[2].callGetProperty(object), (Object)"descriptor");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var1[3].call(var1[4].callGetProperty(object), (Object)"jmx");
         }

         Object metaProp = var10000;
         if (DefaultTypeTransformation.booleanUnbox(metaProp)) {
            Object descriptor = var1[5].call(var1[6].callGetProperty(object), var1[7].call(object), var1[8].callGetPropertySafe(metaProp));
            if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var1[9].call(descriptor), $const$0) && DefaultTypeTransformation.booleanUnbox(var1[10].callGetProperty(descriptor)) ? Boolean.TRUE : Boolean.FALSE)) {
               map = ScriptBytecodeAdapter.createMap(new Object[]{"target", object, "name", var1[11].callGetProperty(var1[12].call(object)), "jmxName", var1[13].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), descriptor), "displayName", var1[14].call(new GStringImpl(new Object[]{var1[15].callGetProperty(var1[16].callGetProperty(object))}, new String[]{"JMX Managed Object ", ""})), "attributes", var1[17].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "constructors", var1[18].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "operations", var1[19].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object)});
            } else {
               Object[] var5 = new Object[]{"target", object, "name", var1[20].callGetProperty(var1[21].call(object)), "displayName", null, null, null, null, null, null, null, null, null, null, null};
               Object var10003 = var1[22].callGetProperty(descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
                  var10003 = var1[23].callGetProperty(descriptor);
               }

               var5[5] = var10003;
               var5[6] = "attributes";
               CallSite var6 = var1[24];
               Class var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
               Object var10006 = var1[25].callGetProperty(descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
                  var10006 = var1[26].callGetProperty(descriptor);
               }

               var5[7] = var6.callStatic(var10004, object, var10006);
               var5[8] = "constructors";
               var6 = var1[27];
               var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
               var10006 = var1[28].callGetProperty(descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
                  var10006 = var1[29].callGetProperty(descriptor);
               }

               var5[9] = var6.callStatic(var10004, object, var10006);
               var5[10] = "operations";
               var6 = var1[30];
               var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
               var10006 = var1[31].callGetProperty(descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
                  var10006 = var1[32].callGetProperty(descriptor);
               }

               var5[11] = var6.callStatic(var10004, object, var10006);
               var5[12] = "listeners";
               var5[13] = var1[33].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var1[34].callGetProperty(descriptor));
               var5[14] = "mbeanServer";
               var10003 = var1[35].callGetProperty(descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
                  var10003 = var1[36].callGetProperty(descriptor);
               }

               var5[15] = var10003;
               map = ScriptBytecodeAdapter.createMap(var5);
               var10000 = var1[37].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), descriptor);
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = var1[38].call($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), var1[39].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[40].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), object);
               }

               ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "jmxName");
            }
         } else {
            map = ScriptBytecodeAdapter.createMap(new Object[]{"target", object, "name", var1[41].callGetProperty(var1[42].call(object)), "jmxName", var1[43].call($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), var1[44].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[45].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), object), "displayName", var1[46].call(new GStringImpl(new Object[]{var1[47].callGetProperty(var1[48].callGetProperty(object))}, new String[]{"JMX Managed Object ", ""})), "attributes", var1[49].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "constructors", var1[50].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "operations", var1[51].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object)});
         }

         return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
      }
   }

   public static Map buildObjectMapFrom(Object object, Object descriptor) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(object)) {
         throw (Throwable)var2[52].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable to create MBean, missing target object.");
      } else {
         Object map = null;
         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var2[53].call(descriptor), $const$1) && DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var2[54].callGetProperty(descriptor)) && DefaultTypeTransformation.booleanUnbox(var2[55].callGetProperty(descriptor)) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE)) {
            map = ScriptBytecodeAdapter.createMap(new Object[]{"target", object, "jmxName", var2[56].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), descriptor), "name", var2[57].callGetProperty(var2[58].call(object)), "displayName", var2[59].call(new GStringImpl(new Object[]{var2[60].callGetProperty(var2[61].callGetProperty(object))}, new String[]{"JMX Managed Object ", ""})), "attributes", var2[62].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "constructors", var2[63].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object), "operations", var2[64].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object)});
         } else {
            Object[] var10000 = new Object[]{"target", object, "name", var2[65].callGetProperty(var2[66].call(object)), "displayName", null, null, null, null, null, null, null, null, null, null, null};
            Object var10003 = var2[67].callGetProperty(descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
               var10003 = var2[68].callGetProperty(descriptor);
            }

            var10000[5] = var10003;
            var10000[6] = "attributes";
            CallSite var5 = var2[69];
            Class var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
            Object var10006 = var2[70].callGetProperty(descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
               var10006 = var2[71].callGetProperty(descriptor);
            }

            var10000[7] = var5.callStatic(var10004, object, var10006);
            var10000[8] = "constructors";
            var5 = var2[72];
            var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
            var10006 = var2[73].callGetProperty(descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
               var10006 = var2[74].callGetProperty(descriptor);
            }

            var10000[9] = var5.callStatic(var10004, object, var10006);
            var10000[10] = "operations";
            var5 = var2[75];
            var10004 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
            var10006 = var2[76].callGetProperty(descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var10006)) {
               var10006 = var2[77].callGetProperty(descriptor);
            }

            var10000[11] = var5.callStatic(var10004, object, var10006);
            var10000[12] = "listeners";
            var10000[13] = var2[78].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var2[79].callGetProperty(descriptor));
            var10000[14] = "mbeanServer";
            var10003 = var2[80].callGetProperty(descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
               var10003 = var2[81].callGetProperty(descriptor);
            }

            var10000[15] = var10003;
            map = ScriptBytecodeAdapter.createMap(var10000);
            Object var4 = var2[82].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), descriptor);
            if (!DefaultTypeTransformation.booleanUnbox(var4)) {
               var4 = var2[83].call($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), var2[84].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var2[85].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), object);
            }

            ScriptBytecodeAdapter.setProperty(var4, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "jmxName");
         }

         return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
      }
   }

   private static ObjectName getObjectName(Object map) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(map)) {
         return (ObjectName)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$ObjectName());
      } else {
         Object jmxName = null;
         if (var1[86].callGetProperty(map) instanceof String) {
            jmxName = var1[87].callConstructor($get$$class$javax$management$ObjectName(), (Object)var1[88].callGetProperty(map));
         } else if (var1[89].callGetProperty(map) instanceof ObjectName) {
            jmxName = var1[90].callGetProperty(map);
         }

         return (ObjectName)ScriptBytecodeAdapter.castToType(jmxName, $get$$class$javax$management$ObjectName());
      }
   }

   public static Map buildAttributeMapFrom(Object object) {
      CallSite[] var1 = $getCallSiteArray();
      Object properties = var1[91].call(var1[92].callGetProperty(object));
      Object attribs = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      var1[93].call(properties, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), attribs) {
         private Reference<T> attribs;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxBuilderTools;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.attribs = (Reference)attribs;
         }

         public Object doCall(MetaProperty prop) {
            MetaProperty propx = new Reference(prop);
            CallSite[] var3 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(var3[0].call(var3[1].callGroovyObjectGetProperty(this), var3[2].callGetProperty(propx.get())))) {
               return null;
            } else {
               Object attrib = ScriptBytecodeAdapter.createMap(new Object[0]);
               Object getterPrefix = DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(var3[3].callGetProperty(var3[4].callGetProperty(propx.get())), "java.lang.Boolean") && !ScriptBytecodeAdapter.compareEqual(var3[5].callGetProperty(var3[6].callGetProperty(propx.get())), "boolean") ? Boolean.FALSE : Boolean.TRUE) ? "is" : "get";
               Object name = var3[7].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var3[8].callGetProperty(propx.get()));
               ScriptBytecodeAdapter.setProperty(name, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "name");
               ScriptBytecodeAdapter.setProperty(var3[9].call(new GStringImpl(new Object[]{var3[10].callGetProperty(propx.get())}, new String[]{"Property ", ""})), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "displayName");
               ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "readable");
               ScriptBytecodeAdapter.setProperty(var3[11].call(getterPrefix, (Object)name), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "getMethod");
               ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "writable");
               ScriptBytecodeAdapter.setProperty(var3[12].callGetProperty(var3[13].callGetProperty(propx.get())), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "type");
               ScriptBytecodeAdapter.setProperty(propx.get(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), attrib, "property");
               return var3[14].call(this.attribs.get(), name, attrib);
            }
         }

         public Object call(MetaProperty prop) {
            MetaProperty propx = new Reference(prop);
            CallSite[] var3 = $getCallSiteArray();
            return var3[15].callCurrent(this, (Object)propx.get());
         }

         public Object getAttribs() {
            CallSite[] var1 = $getCallSiteArray();
            return this.attribs.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1()) {
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
            var0[0] = "contains";
            var0[1] = "ATTRIB_EXCEPTION_LIST";
            var0[2] = "name";
            var0[3] = "name";
            var0[4] = "type";
            var0[5] = "name";
            var0[6] = "type";
            var0[7] = "capitalize";
            var0[8] = "name";
            var0[9] = "toString";
            var0[10] = "name";
            var0[11] = "plus";
            var0[12] = "name";
            var0[13] = "type";
            var0[14] = "put";
            var0[15] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[16];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1(), var0);
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
         private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1() {
            Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure1 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildAttributeMapFrom_closure1");
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
      return (Map)ScriptBytecodeAdapter.castToType(attribs.get(), $get$$class$java$util$Map());
   }

   public static Map buildAttributeMapFrom(Object object, Object descCollection) {
      Object object = new Reference(object);
      CallSite[] var3 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (DefaultTypeTransformation.booleanUnbox(descCollection instanceof String && DefaultTypeTransformation.booleanUnbox(var3[94].call(descCollection, (Object)"*")) ? Boolean.TRUE : Boolean.FALSE)) {
         map.set(var3[95].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object.get()));
      }

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
         var3[96].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, object) {
            private Reference<T> map;
            private Reference<T> object;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
            // $FF: synthetic field
            private static Class $class$groovy$lang$MetaProperty;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBuilderTools;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.map = (Reference)map;
               this.object = (Reference)object;
            }

            public Object doCall(Object attrib) {
               Object attribx = new Reference(attrib);
               CallSite[] var3 = $getCallSiteArray();
               MetaProperty prop = new Reference((MetaProperty)ScriptBytecodeAdapter.castToType(var3[0].call(var3[1].callGetProperty(this.object.get()), var3[2].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)attribx.get())), $get$$class$groovy$lang$MetaProperty()));
               return DefaultTypeTransformation.booleanUnbox(prop.get()) ? var3[3].call(this.map.get(), var3[4].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)attribx.get()), var3[5].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), prop.get(), attribx.get(), "*")) : null;
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            public Object getObject() {
               CallSite[] var1 = $getCallSiteArray();
               return this.object.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2()) {
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
               var0[0] = "getMetaProperty";
               var0[1] = "metaClass";
               var0[2] = "uncapitalize";
               var0[3] = "put";
               var0[4] = "capitalize";
               var0[5] = "createAttributeMap";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure2 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildAttributeMapFrom_closure2");
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
            private static Class $get$$class$groovy$lang$MetaProperty() {
               Class var10000 = $class$groovy$lang$MetaProperty;
               if (var10000 == null) {
                  var10000 = $class$groovy$lang$MetaProperty = class$("groovy.lang.MetaProperty");
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

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
         var3[97].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, object) {
            private Reference<T> map;
            private Reference<T> object;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
            // $FF: synthetic field
            private static Class $class$groovy$lang$MetaProperty;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBuilderTools;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.map = (Reference)map;
               this.object = (Reference)object;
            }

            public Object doCall(Object attrib, Object attrDescriptor) {
               Object attribx = new Reference(attrib);
               Object attrDescriptorx = new Reference(attrDescriptor);
               CallSite[] var5 = $getCallSiteArray();
               MetaProperty prop = new Reference((MetaProperty)ScriptBytecodeAdapter.castToType(var5[0].call(var5[1].callGetProperty(this.object.get()), var5[2].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)attribx.get())), $get$$class$groovy$lang$MetaProperty()));
               return DefaultTypeTransformation.booleanUnbox(prop.get()) ? var5[3].call(this.map.get(), var5[4].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)attribx.get()), var5[5].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), prop.get(), attribx.get(), attrDescriptorx.get())) : null;
            }

            public Object call(Object attrib, Object attrDescriptor) {
               Object attribx = new Reference(attrib);
               Object attrDescriptorx = new Reference(attrDescriptor);
               CallSite[] var5 = $getCallSiteArray();
               return var5[6].callCurrent(this, attribx.get(), attrDescriptorx.get());
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            public Object getObject() {
               CallSite[] var1 = $getCallSiteArray();
               return this.object.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3()) {
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
               var0[0] = "getMetaProperty";
               var0[1] = "metaClass";
               var0[2] = "uncapitalize";
               var0[3] = "put";
               var0[4] = "capitalize";
               var0[5] = "createAttributeMap";
               var0[6] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[7];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildAttributeMapFrom_closure3 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildAttributeMapFrom_closure3");
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
            private static Class $get$$class$groovy$lang$MetaProperty() {
               Class var10000 = $class$groovy$lang$MetaProperty;
               if (var10000 == null) {
                  var10000 = $class$groovy$lang$MetaProperty = class$("groovy.lang.MetaProperty");
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

      return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
   }

   private static Map createAttributeMap(Object prop, Object attribName, Object descriptor) {
      CallSite[] var3 = $getCallSiteArray();
      Object desc = descriptor instanceof Map ? descriptor : ScriptBytecodeAdapter.createMap(new Object[0]);
      Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
      Object name = var3[98].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)attribName);
      Object getterPrefix = DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(var3[99].callGetProperty(var3[100].callGetProperty(prop)), "java.lang.Boolean") && !ScriptBytecodeAdapter.compareEqual(var3[101].callGetProperty(var3[102].callGetProperty(prop)), "boolean") ? Boolean.FALSE : Boolean.TRUE) ? "is" : "get";
      ScriptBytecodeAdapter.setProperty(name, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "name");
      Object var10000 = var3[103].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[104].callGetProperty(desc);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var3[105].call(new GStringImpl(new Object[]{name}, new String[]{"Property ", ""}));
         }
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "displayName");
      ScriptBytecodeAdapter.setProperty(var3[106].callGetProperty(var3[107].callGetProperty(prop)), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "type");
      ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.compareNotEqual(var3[108].callGetProperty(desc), (Object)null) ? var3[109].callGetProperty(desc) : Boolean.TRUE, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "readable");
      if (DefaultTypeTransformation.booleanUnbox(var3[110].callGetProperty(map))) {
         ScriptBytecodeAdapter.setProperty(var3[111].call(getterPrefix, (Object)name), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "getMethod");
      }

      ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.compareNotEqual(var3[112].callGetProperty(desc), (Object)null) ? var3[113].callGetProperty(desc) : Boolean.FALSE, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "writable");
      if (DefaultTypeTransformation.booleanUnbox(var3[114].callGetProperty(map))) {
         ScriptBytecodeAdapter.setProperty(var3[115].call("set", (Object)name), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "setMethod");
      }

      var10000 = var3[116].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[117].callGetProperty(desc);
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "defaultValue");
      ScriptBytecodeAdapter.setProperty(prop, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "property");
      var10000 = var3[118].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[119].callGetProperty(desc);
      }

      Object listener = var10000;
      if (DefaultTypeTransformation.booleanUnbox(listener)) {
         ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "methodListener");
         ScriptBytecodeAdapter.setProperty(listener, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[120].callGetProperty(map), "callback");
         ScriptBytecodeAdapter.setProperty(var3[121].call("set", (Object)name), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[122].callGetProperty(map), "target");
         ScriptBytecodeAdapter.setProperty("attributeChangeListener", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[123].callGetProperty(map), "type");
         ScriptBytecodeAdapter.setProperty(name, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[124].callGetProperty(map), "attribute");
      }

      return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
   }

   public static Map buildConstructorMapFrom(Object object) {
      CallSite[] var1 = $getCallSiteArray();
      Object methods = var1[125].call(var1[126].call(object));
      Object ctors = ScriptBytecodeAdapter.createMap(new Object[0]);
      Object cntr = $const$2;
      Constructor ctor = null;
      Object var6 = var1[127].call(methods);

      while(((Iterator)var6).hasNext()) {
         ctor = ((Iterator)var6).next();
         Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
         ScriptBytecodeAdapter.setProperty(var1[128].callGetProperty(ctor), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "name");
         ScriptBytecodeAdapter.setProperty(var1[129].call(new GStringImpl(new Object[]{var1[130].call(var1[131].call(object))}, new String[]{"Constructor for class ", ""})), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "displayName");
         ScriptBytecodeAdapter.setProperty("constructor", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "role");
         ScriptBytecodeAdapter.setProperty(ctor, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "constructor");
         var1[132].call(map, "params", var1[133].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), ctor));
         CallSite var10000 = var1[134];
         CallSite var10002 = var1[135];
         Object var10003 = var1[136].call(var1[137].call(var1[138].callGetProperty(ctor), (Object)ScriptBytecodeAdapter.createRange(var1[139].call(var1[140].call(var1[141].callGetProperty(ctor), (Object)"."), (Object)$const$0), $const$3, true)), (Object)"@");
         Object var8 = cntr;
         cntr = var1[142].call(cntr);
         var10000.call(ctors, var10002.call(var10003, var8), map);
      }

      return (Map)ScriptBytecodeAdapter.castToType(ctors, $get$$class$java$util$Map());
   }

   public static Map buildConstructorMapFrom(Object object, Object descCollection) {
      Object object = new Reference(object);
      CallSite[] var3 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof String ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(var3[143].call(descCollection, (Object)"*")) ? Boolean.TRUE : Boolean.FALSE)) {
         map.set(var3[144].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object.get()));
      }

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
         var3[145].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, object) {
            private Reference<T> map;
            private Reference<T> object;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$reflect$Constructor;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
            // $FF: synthetic field
            private static Class array$$class$java$lang$Class;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.map = (Reference)map;
               this.object = (Reference)object;
            }

            public Object doCall(Object ctorKey, Object descriptor) {
               Object ctorKeyx = new Reference(ctorKey);
               Object descriptorx = new Reference(descriptor);
               CallSite[] var5 = $getCallSiteArray();
               Object params = new Reference((Object)null);
               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && DefaultTypeTransformation.booleanUnbox(descriptorx.get() instanceof List && ScriptBytecodeAdapter.compareEqual(var5[0].call(descriptorx.get()), $const$0) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE)) {
                  params.set((Object)null);
               }

               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && DefaultTypeTransformation.booleanUnbox(descriptorx.get() instanceof List && ScriptBytecodeAdapter.compareGreaterThan(var5[1].call(descriptorx.get()), $const$0) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE)) {
                  params.set(ScriptBytecodeAdapter.createList(new Object[0]));
                  var5[2].call(descriptorx.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), params) {
                     private Reference<T> params;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12;
                     // $FF: synthetic field
                     private static Class $class$groovy$jmx$builder$JmxBuilderTools;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.params = (Reference)params;
                     }

                     public Object doCall(Object param) {
                        Object paramx = new Reference(param);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].call(this.params.get(), var3[1].call(var3[2].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var3[3].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)paramx.get())));
                     }

                     public Object getParams() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.params.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12()) {
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
                        var0[0] = "leftShift";
                        var0[1] = "getAt";
                        var0[2] = "TYPE_MAP";
                        var0[3] = "getNormalizedType";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[4];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12(), var0);
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
                     private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12() {
                        Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12;
                        if (var10000 == null) {
                           var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure12");
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

               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && descriptorx.get() instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
                  Object paramTypes = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
                  if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var5[3].callGetProperty(descriptorx.get())) && var5[4].callGetProperty(descriptorx.get()) instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
                     paramTypes.set(var5[5].call(var5[6].call(var5[7].callGetProperty(descriptorx.get()))));
                  } else if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var5[8].callGetProperty(descriptorx.get())) && var5[9].callGetProperty(descriptorx.get()) instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
                     paramTypes.set(var5[10].callGetProperty(descriptorx.get()));
                  }

                  params.set(ScriptBytecodeAdapter.createList(new Object[0]));
                  var5[11].call(paramTypes.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), params) {
                     private Reference<T> params;
                     // $FF: synthetic field
                     private static ClassInfo $staticClassInfo;
                     // $FF: synthetic field
                     private static SoftReference $callSiteArray;
                     // $FF: synthetic field
                     private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13;
                     // $FF: synthetic field
                     private static Class $class$groovy$jmx$builder$JmxBuilderTools;

                     public {
                        CallSite[] var4 = $getCallSiteArray();
                        this.params = (Reference)params;
                     }

                     public Object doCall(Object p) {
                        Object px = new Reference(p);
                        CallSite[] var3 = $getCallSiteArray();
                        return var3[0].call(this.params.get(), var3[1].call(var3[2].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var3[3].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)px.get())));
                     }

                     public Object getParams() {
                        CallSite[] var1 = $getCallSiteArray();
                        return this.params.get();
                     }

                     // $FF: synthetic method
                     protected MetaClass $getStaticMetaClass() {
                        if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13()) {
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
                        var0[0] = "leftShift";
                        var0[1] = "getAt";
                        var0[2] = "TYPE_MAP";
                        var0[3] = "getNormalizedType";
                     }

                     // $FF: synthetic method
                     private static CallSiteArray $createCallSiteArray() {
                        String[] var0 = new String[4];
                        $createCallSiteArray_1(var0);
                        return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13(), var0);
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
                     private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13() {
                        Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13;
                        if (var10000 == null) {
                           var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildConstructorMapFrom_closure4_closure13");
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

               Constructor ctor = (Constructor)ScriptBytecodeAdapter.castToType(var5[12].call(var5[13].callGetProperty(this.object.get()), (Object)(ScriptBytecodeAdapter.compareNotEqual(params.get(), (Object)null) ? (Class[])ScriptBytecodeAdapter.castToType(params.get(), $get$array$$class$java$lang$Class()) : null)), $get$$class$java$lang$reflect$Constructor());
               return var5[14].call(this.map.get(), ctorKeyx.get(), var5[15].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), ctor, descriptorx.get()));
            }

            public Object call(Object ctorKey, Object descriptor) {
               Object ctorKeyx = new Reference(ctorKey);
               Object descriptorx = new Reference(descriptor);
               CallSite[] var5 = $getCallSiteArray();
               return var5[16].callCurrent(this, ctorKeyx.get(), descriptorx.get());
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            public Object getObject() {
               CallSite[] var1 = $getCallSiteArray();
               return this.object.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4()) {
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
               var0[0] = "size";
               var0[1] = "size";
               var0[2] = "each";
               var0[3] = "params";
               var0[4] = "params";
               var0[5] = "toList";
               var0[6] = "keySet";
               var0[7] = "params";
               var0[8] = "params";
               var0[9] = "params";
               var0[10] = "params";
               var0[11] = "each";
               var0[12] = "getDeclaredConstructor";
               var0[13] = "class";
               var0[14] = "put";
               var0[15] = "createConstructorMap";
               var0[16] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[17];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4(), var0);
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
            private static Class $get$$class$java$lang$reflect$Constructor() {
               Class var10000 = $class$java$lang$reflect$Constructor;
               if (var10000 == null) {
                  var10000 = $class$java$lang$reflect$Constructor = class$("java.lang.reflect.Constructor");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildConstructorMapFrom_closure4 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildConstructorMapFrom_closure4");
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
            private static Class $get$array$$class$java$lang$Class() {
               Class var10000 = array$$class$java$lang$Class;
               if (var10000 == null) {
                  var10000 = array$$class$java$lang$Class = class$("[Ljava.lang.Class;");
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

      return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
   }

   private static Map createConstructorMap(Object ctor, Object descriptor) {
      CallSite[] var2 = $getCallSiteArray();
      Object desc = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptor) && descriptor instanceof Map ? Boolean.TRUE : Boolean.FALSE) ? descriptor : ScriptBytecodeAdapter.createMap(new Object[0]);
      Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
      ScriptBytecodeAdapter.setProperty(var2[146].callGetProperty(ctor), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "name");
      Object var10000 = var2[147].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var2[148].callGetProperty(desc);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = "Class constructor";
         }
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "displayName");
      ScriptBytecodeAdapter.setProperty("constructor", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "role");
      ScriptBytecodeAdapter.setProperty(ctor, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "constructor");
      if (DefaultTypeTransformation.booleanUnbox(var2[149].callGetProperty(desc))) {
         var2[150].call(map, "params", var2[151].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), ctor, var2[152].callGetProperty(desc)));
      } else {
         var2[153].call(map, "params", var2[154].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), ctor));
      }

      return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
   }

   public static Map buildOperationMapFrom(Object object) {
      Object object = new Reference(object);
      CallSite[] var2 = $getCallSiteArray();
      Object methods = var2[155].call(var2[156].callGetProperty(object.get()));
      Object ops = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      Object declaredMethods = new Reference(ScriptBytecodeAdapter.getPropertySpreadSafe($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var2[157].call(var2[158].call(object.get())), "name"));
      var2[159].call(methods, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), declaredMethods, object, ops) {
         private Reference<T> declaredMethods;
         private Reference<T> object;
         private Reference<T> ops;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)3;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)-1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$groovy$lang$MetaProperty;
         // $FF: synthetic field
         private static Class $class$groovy$jmx$builder$JmxBuilderTools;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.declaredMethods = (Reference)declaredMethods;
            this.object = (Reference)object;
            this.ops = (Reference)ops;
         }

         public Object doCall(Object method) {
            Object methodx = new Reference(method);
            CallSite[] var3 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var3[0].call(this.declaredMethods.get(), var3[1].callGetProperty(methodx.get()))) && !DefaultTypeTransformation.booleanUnbox(var3[2].call(var3[3].callGroovyObjectGetProperty(this), var3[4].callGetProperty(methodx.get()))) ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(var3[5].call(var3[6].callGroovyObjectGetProperty(this), var3[7].callGetProperty(methodx.get()))) ? Boolean.FALSE : Boolean.TRUE)) {
               return null;
            } else {
               String mName = new Reference((String)ScriptBytecodeAdapter.castToType(var3[8].callGetProperty(methodx.get()), $get$$class$java$lang$String()));
               MetaProperty prop = (MetaProperty)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var3[9].call(mName.get(), (Object)"get")) && !DefaultTypeTransformation.booleanUnbox(var3[10].call(mName.get(), (Object)"set")) ? Boolean.FALSE : Boolean.TRUE) ? var3[11].call(var3[12].callGetProperty(this.object.get()), var3[13].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var3[14].call(mName.get(), (Object)ScriptBytecodeAdapter.createRange($const$0, $const$1, true)))) : null, $get$$class$groovy$lang$MetaProperty());
               if (!DefaultTypeTransformation.booleanUnbox(prop)) {
                  Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
                  ScriptBytecodeAdapter.setProperty(mName.get(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5(), map, "name");
                  ScriptBytecodeAdapter.setProperty(var3[15].call(new GStringImpl(new Object[]{var3[16].callGetProperty(methodx.get()), var3[17].call(var3[18].call(this.object.get()))}, new String[]{"Method ", " for class ", ""})), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5(), map, "displayName");
                  ScriptBytecodeAdapter.setProperty("operation", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5(), map, "role");
                  ScriptBytecodeAdapter.setProperty(methodx.get(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5(), map, "method");
                  var3[19].call(map, "params", var3[20].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), methodx.get()));
                  return var3[21].call(this.ops.get(), mName.get(), map);
               } else {
                  return null;
               }
            }
         }

         public Object getDeclaredMethods() {
            CallSite[] var1 = $getCallSiteArray();
            return this.declaredMethods.get();
         }

         public Object getObject() {
            CallSite[] var1 = $getCallSiteArray();
            return this.object.get();
         }

         public Object getOps() {
            CallSite[] var1 = $getCallSiteArray();
            return this.ops.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5()) {
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
            var0[0] = "contains";
            var0[1] = "name";
            var0[2] = "contains";
            var0[3] = "OPS_EXCEPTION_LIST";
            var0[4] = "name";
            var0[5] = "contains";
            var0[6] = "OPS_EXCEPTION_LIST";
            var0[7] = "name";
            var0[8] = "name";
            var0[9] = "startsWith";
            var0[10] = "startsWith";
            var0[11] = "getMetaProperty";
            var0[12] = "metaClass";
            var0[13] = "uncapitalize";
            var0[14] = "getAt";
            var0[15] = "toString";
            var0[16] = "name";
            var0[17] = "getName";
            var0[18] = "getClass";
            var0[19] = "put";
            var0[20] = "buildParameterMapFrom";
            var0[21] = "put";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[22];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5(), var0);
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
         private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5() {
            Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure5 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildOperationMapFrom_closure5");
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
         private static Class $get$$class$groovy$lang$MetaProperty() {
            Class var10000 = $class$groovy$lang$MetaProperty;
            if (var10000 == null) {
               var10000 = $class$groovy$lang$MetaProperty = class$("groovy.lang.MetaProperty");
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
      return (Map)ScriptBytecodeAdapter.castToType(ops.get(), $get$$class$java$util$Map());
   }

   public static Map buildOperationMapFrom(Object object, Object descCollection) {
      Object object = new Reference(object);
      CallSite[] var3 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof String ? Boolean.TRUE : Boolean.FALSE) && DefaultTypeTransformation.booleanUnbox(var3[160].call(descCollection, (Object)"*")) ? Boolean.TRUE : Boolean.FALSE)) {
         map.set(var3[161].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), object.get()));
      }

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
         var3[162].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, object) {
            private Reference<T> map;
            private Reference<T> object;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.map = (Reference)map;
               this.object = (Reference)object;
            }

            public Object doCall(Object opName) {
               Object opNamex = new Reference(opName);
               CallSite[] var3 = $getCallSiteArray();
               Object method = new Reference((Object)null);
               Object m = new Reference((Object)null);
               Object var6 = var3[0].call(var3[1].call(var3[2].callGetProperty(this.object.get())));

               while(((Iterator)var6).hasNext()) {
                  m.set(((Iterator)var6).next());
                  if (DefaultTypeTransformation.booleanUnbox(var3[3].call(var3[4].callGetProperty(m.get()), opNamex.get()))) {
                     method.set(m.get());
                     break;
                  }
               }

               return DefaultTypeTransformation.booleanUnbox(method.get()) ? var3[5].call(this.map.get(), opNamex.get(), var3[6].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.object.get(), method.get(), "*")) : null;
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            public Object getObject() {
               CallSite[] var1 = $getCallSiteArray();
               return this.object.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6()) {
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
               var0[0] = "iterator";
               var0[1] = "getMethods";
               var0[2] = "metaClass";
               var0[3] = "equals";
               var0[4] = "name";
               var0[5] = "put";
               var0[6] = "createOperationMap";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[7];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure6 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildOperationMapFrom_closure6");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
      }

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
         var3[163].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, object) {
            private Reference<T> map;
            private Reference<T> object;
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
            // $FF: synthetic field
            private static Class array$$class$java$lang$Object;

            public {
               CallSite[] var5 = $getCallSiteArray();
               this.map = (Reference)map;
               this.object = (Reference)object;
            }

            public Object doCall(Object opName, Object descriptor) {
               Object opNamex = new Reference(opName);
               Object descriptorx = new Reference(descriptor);
               CallSite[] var5 = $getCallSiteArray();
               Object params = new Reference((Object)null);
               Object method = new Reference((Object)null);
               if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && DefaultTypeTransformation.booleanUnbox(descriptorx.get() instanceof String && DefaultTypeTransformation.booleanUnbox(var5[0].call(descriptorx.get(), (Object)"*")) ? Boolean.TRUE : Boolean.FALSE) ? Boolean.TRUE : Boolean.FALSE)) {
                  method.set(var5[1].call(var5[2].call(var5[3].callGetProperty(this.object.get()), this.object.get(), opNamex.get()), (Object)$const$0));
               } else {
                  if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && descriptorx.get() instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
                     params.set(descriptorx.get());
                  }

                  if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptorx.get()) && descriptorx.get() instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
                     if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var5[4].callGetProperty(descriptorx.get())) && var5[5].callGetProperty(descriptorx.get()) instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
                        params.set(var5[6].call(var5[7].call(var5[8].callGetPropertySafe(descriptorx.get()))));
                     }

                     if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(var5[9].callGetProperty(descriptorx.get())) && var5[10].callGetProperty(descriptorx.get()) instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
                        params.set(var5[11].callGetProperty(descriptorx.get()));
                     }
                  }

                  Object var10000;
                  if (DefaultTypeTransformation.booleanUnbox(params.get())) {
                     Object paramTypes = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
                     var5[12].callSafe(params.get(), (Object)(new GeneratedClosure(this, this.getThisObject(), paramTypes) {
                        private Reference<T> paramTypes;
                        // $FF: synthetic field
                        private static ClassInfo $staticClassInfo;
                        // $FF: synthetic field
                        private static SoftReference $callSiteArray;
                        // $FF: synthetic field
                        private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14;
                        // $FF: synthetic field
                        private static Class $class$groovy$jmx$builder$JmxBuilderTools;

                        public {
                           CallSite[] var4 = $getCallSiteArray();
                           this.paramTypes = (Reference)paramTypes;
                        }

                        public Object doCall(Object key) {
                           Object keyx = new Reference(key);
                           CallSite[] var3 = $getCallSiteArray();
                           return var3[0].call(this.paramTypes.get(), var3[1].call(var3[2].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var3[3].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)keyx.get())));
                        }

                        public Object getParamTypes() {
                           CallSite[] var1 = $getCallSiteArray();
                           return this.paramTypes.get();
                        }

                        // $FF: synthetic method
                        protected MetaClass $getStaticMetaClass() {
                           if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14()) {
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
                           var0[0] = "leftShift";
                           var0[1] = "getAt";
                           var0[2] = "TYPE_MAP";
                           var0[3] = "getNormalizedType";
                        }

                        // $FF: synthetic method
                        private static CallSiteArray $createCallSiteArray() {
                           String[] var0 = new String[4];
                           $createCallSiteArray_1(var0);
                           return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14(), var0);
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
                        private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14() {
                           Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14;
                           if (var10000 == null) {
                              var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildOperationMapFrom_closure7_closure14");
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
                     var10000 = paramTypes.get();
                     if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                        var10000 = null;
                     }

                     params.set(var10000);
                  }

                  Object signature = ScriptBytecodeAdapter.compareNotEqual(params.get(), (Object)null) ? (Object[])ScriptBytecodeAdapter.castToType(params.get(), $get$array$$class$java$lang$Object()) : null;
                  Object methods = var5[13].call(var5[14].callGetProperty(this.object.get()), this.object.get(), opNamex.get(), signature);
                  var10000 = var5[15].call(methods, (Object)$const$0);
                  if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                     var10000 = null;
                  }

                  method.set(var10000);
               }

               return DefaultTypeTransformation.booleanUnbox(method.get()) ? var5[16].call(this.map.get(), opNamex.get(), var5[17].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.object.get(), method.get(), descriptorx.get())) : null;
            }

            public Object call(Object opName, Object descriptor) {
               Object opNamex = new Reference(opName);
               Object descriptorx = new Reference(descriptor);
               CallSite[] var5 = $getCallSiteArray();
               return var5[18].callCurrent(this, opNamex.get(), descriptorx.get());
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            public Object getObject() {
               CallSite[] var1 = $getCallSiteArray();
               return this.object.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7()) {
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
               var0[0] = "equals";
               var0[1] = "getAt";
               var0[2] = "respondsTo";
               var0[3] = "metaClass";
               var0[4] = "params";
               var0[5] = "params";
               var0[6] = "toList";
               var0[7] = "keySet";
               var0[8] = "params";
               var0[9] = "params";
               var0[10] = "params";
               var0[11] = "params";
               var0[12] = "each";
               var0[13] = "respondsTo";
               var0[14] = "metaClass";
               var0[15] = "getAt";
               var0[16] = "put";
               var0[17] = "createOperationMap";
               var0[18] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[19];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildOperationMapFrom_closure7 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildOperationMapFrom_closure7");
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
            private static Class $get$array$$class$java$lang$Object() {
               Class var10000 = array$$class$java$lang$Object;
               if (var10000 == null) {
                  var10000 = array$$class$java$lang$Object = class$("[Ljava.lang.Object;");
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

      return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
   }

   private static Map createOperationMap(Object object, Object method, Object descriptor) {
      CallSite[] var3 = $getCallSiteArray();
      Object desc = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descriptor) && descriptor instanceof Map ? Boolean.TRUE : Boolean.FALSE) ? descriptor : ScriptBytecodeAdapter.createMap(new Object[0]);
      Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
      ScriptBytecodeAdapter.setProperty(var3[164].callGetProperty(method), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "name");
      Object var10000 = var3[165].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[166].callGetProperty(desc);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var3[167].call(new GStringImpl(new Object[]{var3[168].callGetProperty(method), var3[169].call(var3[170].call(object))}, new String[]{"Method ", " for class ", ""}));
         }
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "displayName");
      ScriptBytecodeAdapter.setProperty("operation", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "role");
      ScriptBytecodeAdapter.setProperty(method, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "method");
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareGreaterThan(var3[171].call(desc), $const$2) && DefaultTypeTransformation.booleanUnbox(var3[172].callGetProperty(desc)) ? Boolean.TRUE : Boolean.FALSE)) {
         var3[173].call(map, "params", var3[174].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), method, var3[175].callGetProperty(desc)));
      } else {
         var3[176].call(map, "params", var3[177].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), method));
      }

      var10000 = var3[178].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[179].callGetProperty(desc);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var3[180].callGetProperty(desc);
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = var3[181].callGetProperty(desc);
            }
         }
      }

      Object listener = var10000;
      if (DefaultTypeTransformation.booleanUnbox(listener)) {
         ScriptBytecodeAdapter.setProperty(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "methodListener");
         ScriptBytecodeAdapter.setProperty(listener, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[182].callGetProperty(map), "callback");
         ScriptBytecodeAdapter.setProperty(var3[183].callGetProperty(method), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[184].callGetProperty(map), "target");
         ScriptBytecodeAdapter.setProperty("operationCallListener", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var3[185].callGetProperty(map), "type");
      }

      return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
   }

   public static Map buildParameterMapFrom(Object method) {
      Object method = new Reference(method);
      CallSite[] var2 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (!DefaultTypeTransformation.booleanUnbox(method.get())) {
         return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
      } else {
         Object params = var2[186].call(method.get());
         if (DefaultTypeTransformation.booleanUnbox(params)) {
            var2[187].call(params, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, method) {
               private Reference<T> map;
               private Reference<T> method;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.map = (Reference)map;
                  this.method = (Reference)method;
               }

               public Object doCall(Object param) {
                  Object paramx = new Reference(param);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(this.map.get(), var3[1].callGetProperty(paramx.get()), var3[2].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.method.get(), paramx.get(), "*"));
               }

               public Object getMap() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.map.get();
               }

               public Object getMethod() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.method.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8()) {
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
                  var0[1] = "name";
                  var0[2] = "createParameterMap";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8(), var0);
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
               private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8() {
                  Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure8 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildParameterMapFrom_closure8");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
         }

         return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
      }
   }

   public static Map buildParameterMapFrom(Object method, Object descCollection) {
      Object method = new Reference(method);
      CallSite[] var3 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (!DefaultTypeTransformation.booleanUnbox(method.get())) {
         return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
      } else {
         if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
            var3[188].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, method) {
               private Reference<T> map;
               private Reference<T> method;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxBuilderTools;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.map = (Reference)map;
                  this.method = (Reference)method;
               }

               public Object doCall(Object param, Object paramMap) {
                  Object paramx = new Reference(param);
                  Object paramMapx = new Reference(paramMap);
                  CallSite[] var5 = $getCallSiteArray();
                  Object type = new Reference(var5[0].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.method.get(), var5[1].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)paramx.get())));
                  return DefaultTypeTransformation.booleanUnbox(type.get()) ? var5[2].call(this.map.get(), var5[3].callGetProperty(type.get()), var5[4].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.method.get(), type.get(), paramMapx.get())) : null;
               }

               public Object call(Object param, Object paramMap) {
                  Object paramx = new Reference(param);
                  Object paramMapx = new Reference(paramMap);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[5].callCurrent(this, paramx.get(), paramMapx.get());
               }

               public Object getMap() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.map.get();
               }

               public Object getMethod() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.method.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9()) {
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
                  var0[0] = "getParamTypeByName";
                  var0[1] = "getNormalizedType";
                  var0[2] = "put";
                  var0[3] = "name";
                  var0[4] = "createParameterMap";
                  var0[5] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[6];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9(), var0);
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
               private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder() {
                  Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder = class$("groovy.jmx.builder.JmxMetaMapBuilder");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9() {
                  Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure9 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildParameterMapFrom_closure9");
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
         } else if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof List ? Boolean.TRUE : Boolean.FALSE)) {
            var3[189].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, method) {
               private Reference<T> map;
               private Reference<T> method;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxBuilderTools;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.map = (Reference)map;
                  this.method = (Reference)method;
               }

               public Object doCall(Object param) {
                  Object paramx = new Reference(param);
                  CallSite[] var3 = $getCallSiteArray();
                  Object type = new Reference(var3[0].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.method.get(), var3[1].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)paramx.get())));
                  return DefaultTypeTransformation.booleanUnbox(type.get()) ? var3[2].call(this.map.get(), var3[3].callGetProperty(type.get()), var3[4].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this.method.get(), type.get(), "*")) : null;
               }

               public Object getMap() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.map.get();
               }

               public Object getMethod() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.method.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10()) {
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
                  var0[0] = "getParamTypeByName";
                  var0[1] = "getNormalizedType";
                  var0[2] = "put";
                  var0[3] = "name";
                  var0[4] = "createParameterMap";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[5];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10(), var0);
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
               private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder() {
                  Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder = class$("groovy.jmx.builder.JmxMetaMapBuilder");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10() {
                  Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildParameterMapFrom_closure10 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildParameterMapFrom_closure10");
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

         return (Map)ScriptBytecodeAdapter.castToType(map.get(), $get$$class$java$util$Map());
      }
   }

   private static Map createParameterMap(Object method, Object type, Object descriptor) {
      CallSite[] var3 = $getCallSiteArray();
      Object desc = descriptor instanceof Map ? descriptor : ScriptBytecodeAdapter.createMap(new Object[0]);
      Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
      Object var10000 = var3[190].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[191].callGetProperty(type);
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "name");
      var10000 = var3[192].callGetProperty(desc);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var3[193].callGetProperty(desc);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var3[194].call(new GStringImpl(new Object[]{var3[195].callGetProperty(type), var3[196].callGetProperty(method)}, new String[]{"Parameter ", " for ", ""}));
         }
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "displayName");
      ScriptBytecodeAdapter.setProperty(type, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "type");
      ScriptBytecodeAdapter.setProperty(method, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "method");
      return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
   }

   private static Object getParamTypeByName(Object method, Object typeName) {
      CallSite[] var2 = $getCallSiteArray();
      Object type = null;
      Object var4 = var2[197].call(var2[198].call(method));

      do {
         if (!((Iterator)var4).hasNext()) {
            return null;
         }

         type = ((Iterator)var4).next();
      } while(!DefaultTypeTransformation.booleanUnbox(var2[199].call(var2[200].callGetProperty(type), typeName)));

      return type;
   }

   public static Object buildListenerMapFrom(Object descCollection) {
      CallSite[] var1 = $getCallSiteArray();
      Object map = new Reference(ScriptBytecodeAdapter.createMap(new Object[0]));
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(descCollection) && descCollection instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
         var1[201].call(descCollection, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map) {
            private Reference<T> map;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxMetaMapBuilder;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.map = (Reference)map;
            }

            public Object doCall(Object name, Object listenerMap) {
               Object namex = new Reference(name);
               Object listenerMapx = new Reference(listenerMap);
               CallSite[] var5 = $getCallSiteArray();
               return var5[0].call(this.map.get(), namex.get(), var5[1].callStatic($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), listenerMapx.get()));
            }

            public Object call(Object name, Object listenerMap) {
               Object namex = new Reference(name);
               Object listenerMapx = new Reference(listenerMap);
               CallSite[] var5 = $getCallSiteArray();
               return var5[2].callCurrent(this, namex.get(), listenerMapx.get());
            }

            public Object getMap() {
               CallSite[] var1 = $getCallSiteArray();
               return this.map.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11()) {
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
               var0[1] = "createListenerMap";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11() {
               Class var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxMetaMapBuilder$_buildListenerMapFrom_closure11 = class$("groovy.jmx.builder.JmxMetaMapBuilder$_buildListenerMapFrom_closure11");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
      }

      return map.get();
   }

   public static Map createListenerMap(Object descriptor) {
      CallSite[] var1 = $getCallSiteArray();
      Object map = ScriptBytecodeAdapter.createMap(new Object[0]);
      ScriptBytecodeAdapter.setProperty("eventListener", $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "type");
      ScriptBytecodeAdapter.setProperty(var1[202].callGetProperty(descriptor), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "event");
      Object var10000 = var1[203].callGetProperty(descriptor);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var1[204].callGetProperty(descriptor);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var1[205].callGetProperty(descriptor);
         }
      }

      ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "from");
      if (!DefaultTypeTransformation.booleanUnbox(var1[206].callGetProperty(map))) {
         throw (Throwable)var1[207].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Missing event source: specify source ObjectName (i.e. from:'...').");
      } else {
         try {
            ScriptBytecodeAdapter.setProperty(var1[208].callGetProperty(map) instanceof String ? var1[209].callConstructor($get$$class$javax$management$ObjectName(), (Object)var1[210].callGetProperty(map)) : var1[211].callGetProperty(map), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "from");
         } catch (Exception var6) {
            throw (Throwable)var1[212].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)var6);
         } finally {
            ;
         }

         ScriptBytecodeAdapter.setProperty(var1[213].callGetProperty(descriptor), $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), map, "callback");
         return (Map)ScriptBytecodeAdapter.castToType(map, $get$$class$java$util$Map());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxMetaMapBuilder()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$jmx$builder$JmxMetaMapBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void super$1$wait(long var1) {
      super.wait(var1);
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
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "getMetaProperty";
      var0[2] = "metaClass";
      var0[3] = "getMetaProperty";
      var0[4] = "metaClass";
      var0[5] = "getProperty";
      var0[6] = "metaClass";
      var0[7] = "getClass";
      var0[8] = "name";
      var0[9] = "size";
      var0[10] = "name";
      var0[11] = "name";
      var0[12] = "getClass";
      var0[13] = "getObjectName";
      var0[14] = "toString";
      var0[15] = "canonicalName";
      var0[16] = "class";
      var0[17] = "buildAttributeMapFrom";
      var0[18] = "buildConstructorMapFrom";
      var0[19] = "buildOperationMapFrom";
      var0[20] = "name";
      var0[21] = "getClass";
      var0[22] = "desc";
      var0[23] = "desc";
      var0[24] = "buildAttributeMapFrom";
      var0[25] = "attributes";
      var0[26] = "attribs";
      var0[27] = "buildConstructorMapFrom";
      var0[28] = "constructors";
      var0[29] = "ctors";
      var0[30] = "buildOperationMapFrom";
      var0[31] = "operations";
      var0[32] = "ops";
      var0[33] = "buildListenerMapFrom";
      var0[34] = "listeners";
      var0[35] = "server";
      var0[36] = "mbeanServer";
      var0[37] = "getObjectName";
      var0[38] = "buildDefaultObjectName";
      var0[39] = "DEFAULT_DOMAIN";
      var0[40] = "DEFAULT_NAME_TYPE";
      var0[41] = "name";
      var0[42] = "getClass";
      var0[43] = "buildDefaultObjectName";
      var0[44] = "DEFAULT_DOMAIN";
      var0[45] = "DEFAULT_NAME_TYPE";
      var0[46] = "toString";
      var0[47] = "canonicalName";
      var0[48] = "class";
      var0[49] = "buildAttributeMapFrom";
      var0[50] = "buildConstructorMapFrom";
      var0[51] = "buildOperationMapFrom";
      var0[52] = "<$constructor$>";
      var0[53] = "size";
      var0[54] = "name";
      var0[55] = "target";
      var0[56] = "getObjectName";
      var0[57] = "name";
      var0[58] = "getClass";
      var0[59] = "toString";
      var0[60] = "canonicalName";
      var0[61] = "class";
      var0[62] = "buildAttributeMapFrom";
      var0[63] = "buildConstructorMapFrom";
      var0[64] = "buildOperationMapFrom";
      var0[65] = "name";
      var0[66] = "getClass";
      var0[67] = "desc";
      var0[68] = "desc";
      var0[69] = "buildAttributeMapFrom";
      var0[70] = "attributes";
      var0[71] = "attribs";
      var0[72] = "buildConstructorMapFrom";
      var0[73] = "constructors";
      var0[74] = "ctors";
      var0[75] = "buildOperationMapFrom";
      var0[76] = "operations";
      var0[77] = "ops";
      var0[78] = "buildListenerMapFrom";
      var0[79] = "listeners";
      var0[80] = "server";
      var0[81] = "mbeanServer";
      var0[82] = "getObjectName";
      var0[83] = "buildDefaultObjectName";
      var0[84] = "DEFAULT_DOMAIN";
      var0[85] = "DEFAULT_NAME_TYPE";
      var0[86] = "name";
      var0[87] = "<$constructor$>";
      var0[88] = "name";
      var0[89] = "name";
      var0[90] = "name";
      var0[91] = "getProperties";
      var0[92] = "metaClass";
      var0[93] = "each";
      var0[94] = "equals";
      var0[95] = "buildAttributeMapFrom";
      var0[96] = "each";
      var0[97] = "each";
      var0[98] = "capitalize";
      var0[99] = "name";
      var0[100] = "type";
      var0[101] = "name";
      var0[102] = "type";
      var0[103] = "desc";
      var0[104] = "description";
      var0[105] = "toString";
      var0[106] = "name";
      var0[107] = "type";
      var0[108] = "readable";
      var0[109] = "readable";
      var0[110] = "readable";
      var0[111] = "plus";
      var0[112] = "writable";
      var0[113] = "writable";
      var0[114] = "writable";
      var0[115] = "plus";
      var0[116] = "defaultValue";
      var0[117] = "default";
      var0[118] = "onChange";
      var0[119] = "onChanged";
      var0[120] = "methodListener";
      var0[121] = "plus";
      var0[122] = "methodListener";
      var0[123] = "methodListener";
      var0[124] = "methodListener";
      var0[125] = "getDeclaredConstructors";
      var0[126] = "getClass";
      var0[127] = "iterator";
      var0[128] = "name";
      var0[129] = "toString";
      var0[130] = "getName";
      var0[131] = "getClass";
      var0[132] = "put";
      var0[133] = "buildParameterMapFrom";
      var0[134] = "put";
      var0[135] = "plus";
      var0[136] = "plus";
      var0[137] = "getAt";
      var0[138] = "name";
      var0[139] = "plus";
      var0[140] = "lastIndexOf";
      var0[141] = "name";
      var0[142] = "next";
      var0[143] = "equals";
      var0[144] = "buildConstructorMapFrom";
      var0[145] = "each";
      var0[146] = "name";
      var0[147] = "description";
      var0[148] = "desc";
      var0[149] = "params";
      var0[150] = "put";
      var0[151] = "buildParameterMapFrom";
      var0[152] = "params";
      var0[153] = "put";
      var0[154] = "buildParameterMapFrom";
      var0[155] = "getMethods";
      var0[156] = "metaClass";
      var0[157] = "getDeclaredMethods";
      var0[158] = "getClass";
      var0[159] = "each";
      var0[160] = "equals";
      var0[161] = "buildOperationMapFrom";
      var0[162] = "each";
      var0[163] = "each";
      var0[164] = "name";
      var0[165] = "description";
      var0[166] = "desc";
      var0[167] = "toString";
      var0[168] = "name";
      var0[169] = "getName";
      var0[170] = "getClass";
      var0[171] = "size";
      var0[172] = "params";
      var0[173] = "put";
      var0[174] = "buildParameterMapFrom";
      var0[175] = "params";
      var0[176] = "put";
      var0[177] = "buildParameterMapFrom";
      var0[178] = "onInvoke";
      var0[179] = "onInvoked";
      var0[180] = "onCall";
      var0[181] = "onCalled";
      var0[182] = "methodListener";
      var0[183] = "name";
      var0[184] = "methodListener";
      var0[185] = "methodListener";
      var0[186] = "getParameterTypes";
      var0[187] = "each";
      var0[188] = "each";
      var0[189] = "each";
      var0[190] = "name";
      var0[191] = "name";
      var0[192] = "description";
      var0[193] = "desc";
      var0[194] = "toString";
      var0[195] = "name";
      var0[196] = "name";
      var0[197] = "iterator";
      var0[198] = "getParameterTypes";
      var0[199] = "equals";
      var0[200] = "name";
      var0[201] = "each";
      var0[202] = "event";
      var0[203] = "from";
      var0[204] = "source";
      var0[205] = "broadcaster";
      var0[206] = "from";
      var0[207] = "<$constructor$>";
      var0[208] = "from";
      var0[209] = "<$constructor$>";
      var0[210] = "from";
      var0[211] = "from";
      var0[212] = "<$constructor$>";
      var0[213] = "call";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[214];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxMetaMapBuilder(), var0);
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
   private static Class $get$$class$groovy$jmx$builder$JmxBeanInfoManager() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager = class$("groovy.jmx.builder.JmxBeanInfoManager");
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
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
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
