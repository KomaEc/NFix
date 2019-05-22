package groovy.jmx.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.GroovyMBean;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxBuilderTools implements GroovyObject {
   private static String DEFAULT_DOMAIN = (String)"jmx.builder";
   private static String DEFAULT_NAME_TYPE = (String)"ExportedObject";
   private static String NODE_NAME_ATTRIBUTES = (String)"attributes";
   private static String NODE_NAME_ATTRIBS = (String)"attribs";
   private static String NODE_NAME_CONSTRUCTORS = (String)"constructors";
   private static String NODE_NAME_CTORS = (String)"ctors";
   private static String NODE_NAME_OPERATIONS = (String)"operations";
   private static String NODE_NAME_OPS = (String)"ops";
   private static String ATTRIB_KEY_DESCRIPTION = (String)"description";
   private static String ATTRIB_KEY_DESC = (String)"desc";
   private static String ATTRIB_KEY_TYPE = (String)"type";
   private static String ATTRIB_KEY_DEFAULT = (String)"defaultValue";
   private static String JMX_KEY = (String)"jmx";
   private static String DESC_KEY = (String)"descriptor";
   private static String DESC_KEY_MBEAN_RESOURCE = (String)"resource";
   private static String DESC_KEY_MBEAN_RESOURCE_TYPE = (String)"ObjectReference";
   private static String DESC_KEY_MBEAN_ATTRIBS = (String)"attributes";
   private static String DESC_KEY_MBEAN_OPS = (String)"operations";
   private static String DESC_KEY_MBEAN_CTORS = (String)"constructors";
   private static String DESC_KEY_MBEAN_NOTES = (String)"notifications";
   private static String DESC_KEY_NAME = (String)"name";
   private static String DESC_KEY_JMX_NAME = (String)"jmxName";
   private static String DESC_KEY_DISPLAY_NAME = (String)"displayName";
   private static String DESC_KEY_TYPE = (String)"descriptorType";
   private static String DESC_KEY_GETMETHOD = (String)"getMethod";
   private static String DESC_KEY_SETMETHOD = (String)"setMethod";
   private static String DESC_KEY_EVENT_TYPE = (String)"eventType";
   private static String DESC_KEY_EVENT_NAME = (String)"eventName";
   private static String DESC_KEY_EVENT_SOURCE = (String)"eventSource";
   private static String DESC_KEY_EVENT_MESSAGE = (String)"messageText";
   private static String DESC_VAL_TYPE_ATTRIB = (String)"attribute";
   private static String DESC_VAL_TYPE_GETTER = (String)"getter";
   private static String DESC_VAL_TYPE_SETTER = (String)"setter";
   private static String DESC_VAL_TYPE_OP = (String)"operation";
   private static String DESC_VAL_TYPE_NOTIFICATION = (String)"notification";
   private static String DESC_VAL_TYPE_CTOR = (String)"constructor";
   private static String DESC_VAL_TYPE_MBEAN = (String)"mbean";
   private static String DESC_KEY_ROLE = (String)"role";
   private static String DESC_KEY_READABLE = (String)"readable";
   private static String DESC_KEY_WRITABLE = (String)"writable";
   private static String DESC_KEY_SIGNATURE = (String)"signature";
   private static String EVENT_KEY_CONTEXTS = (String)"eventContexts";
   private static String EVENT_KEY_CALLBACK = (String)"eventCallback";
   private static String EVENT_KEY_CALLBACK_RESULT = (String)"eventCallbackResult";
   private static String EVENT_KEY_METHOD = (String)"eventMethod";
   private static String EVENT_KEY_METHOD_RESULT = (String)"eventMethodResult";
   private static String EVENT_KEY_ISATTRIB = (String)"eventIsAttrib";
   private static String EVENT_KEY_NAME = (String)"eventName";
   private static String EVENT_KEY_MESSAGE = (String)"eventMessage";
   private static String EVENT_KEY_TYPE = (String)"eventType";
   private static String EVENT_KEY_NODE_TYPE = (String)"eventNodeType";
   private static String EVENT_VAL_NODETYPE_BROADCASTER = (String)"broadcaster";
   private static String EVENT_VAL_NODETYPE_LISTENER = (String)"listener";
   private static String EVENT_KEY_TARGETS = (String)"eventListeners";
   private static Map PRIMITIVE_TYPES = (Map)ScriptBytecodeAdapter.createMap(new Object[]{"char", $getCallSiteArray()[83].callGetProperty($get$$class$java$lang$Integer()), "byte", $getCallSiteArray()[84].callGetProperty($get$$class$java$lang$Byte()), "short", $getCallSiteArray()[85].callGetProperty($get$$class$java$lang$Short()), "int", $getCallSiteArray()[86].callGetProperty($get$$class$java$lang$Integer()), "long", $getCallSiteArray()[87].callGetProperty($get$$class$java$lang$Long()), "float", $getCallSiteArray()[88].callGetProperty($get$$class$java$lang$Float()), "double", $getCallSiteArray()[89].callGetProperty($get$$class$java$lang$Double()), "boolean", $getCallSiteArray()[90].callGetProperty($get$$class$java$lang$Boolean())});
   private static Map TYPE_MAP;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202801L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202801 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Long;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServerFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Character;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderModelMBean;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanInfoManager;
   // $FF: synthetic field
   private static Class $class$java$math$BigDecimal;
   // $FF: synthetic field
   private static Class $class$javax$management$DynamicMBean;
   // $FF: synthetic field
   private static Class $class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class $class$groovy$util$GroovyMBean;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServerConnection;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class array$$class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$java$lang$Short;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Double;
   // $FF: synthetic field
   private static Class $class$java$lang$Byte;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$util$Date;
   // $FF: synthetic field
   private static Class $class$java$lang$management$ManagementFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Float;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;
   // $FF: synthetic field
   private static Class $class$java$math$BigInteger;

   public JmxBuilderTools() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static String capitalize(String value) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(value)) {
         return (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      } else {
         return ScriptBytecodeAdapter.compareEqual(var1[0].call(value), $const$0) ? (String)ScriptBytecodeAdapter.castToType(var1[1].call(value), $get$$class$java$lang$String()) : (String)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareGreaterThan(var1[2].call(value), $const$0) ? var1[3].call(var1[4].call(var1[5].call(value, (Object)$const$1)), var1[6].call(value, (Object)ScriptBytecodeAdapter.createRange($const$0, $const$2, true))) : var1[7].call(value), $get$$class$java$lang$String());
      }
   }

   public static String uncapitalize(String value) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(value)) {
         return (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      } else {
         return ScriptBytecodeAdapter.compareEqual(var1[8].call(value), $const$0) ? (String)ScriptBytecodeAdapter.castToType(var1[9].call(value), $get$$class$java$lang$String()) : (String)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareGreaterThan(var1[10].call(value), $const$0) ? var1[11].call(var1[12].call(var1[13].call(value, (Object)$const$1)), var1[14].call(value, (Object)ScriptBytecodeAdapter.createRange($const$0, $const$2, true))) : var1[15].call(value), $get$$class$java$lang$String());
      }
   }

   public static ObjectName getDefaultObjectName(Object param0) {
      // $FF: Couldn't be decompiled
   }

   public static MBeanServerConnection getMBeanServer() {
      CallSite[] var0 = $getCallSiteArray();
      Object servers = var0[22].call($get$$class$javax$management$MBeanServerFactory(), (Object)null);
      Object server = ScriptBytecodeAdapter.compareGreaterThan(var0[23].call(servers), $const$1) ? var0[24].call(servers, (Object)$const$1) : var0[25].call($get$$class$java$lang$management$ManagementFactory());
      return (MBeanServerConnection)ScriptBytecodeAdapter.castToType(server, $get$$class$javax$management$MBeanServerConnection());
   }

   public static Class[] getSignatureFromParamInfo(Object params) {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(params, (Object)null) && !ScriptBytecodeAdapter.compareEqual(var1[26].call(params), $const$1) ? Boolean.FALSE : Boolean.TRUE)) {
         return (Class[])ScriptBytecodeAdapter.castToType((Object)null, $get$array$$class$java$lang$Class());
      } else {
         Object[] result = new Reference(new Object[DefaultTypeTransformation.intUnbox(var1[27].call(params))]);
         var1[28].call(params, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxBuilderTools(), $get$$class$groovy$jmx$builder$JmxBuilderTools(), result) {
            private Reference<T> result;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1;
            // $FF: synthetic field
            private static Class $class$java$lang$Class;
            // $FF: synthetic field
            private static Class array$$class$java$lang$Object;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.result = (Reference)result;
            }

            public Object doCall(Object param, Object i) {
               Object paramx = new Reference(param);
               Object ix = new Reference(i);
               CallSite[] var5 = $getCallSiteArray();
               Object var10000 = var5[0].call(var5[1].callGroovyObjectGetProperty(this), var5[2].call(paramx.get()));
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = var5[3].call($get$$class$java$lang$Class(), (Object)var5[4].call(paramx.get()));
                  if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                     var10000 = null;
                  }
               }

               Object type = var10000;
               return var5[5].call(this.result.get(), ix.get(), type);
            }

            public Object call(Object param, Object i) {
               Object paramx = new Reference(param);
               Object ix = new Reference(i);
               CallSite[] var5 = $getCallSiteArray();
               return var5[6].callCurrent(this, paramx.get(), ix.get());
            }

            public Object[] getResult() {
               CallSite[] var1 = $getCallSiteArray();
               return (Object[])ScriptBytecodeAdapter.castToType(this.result.get(), $get$array$$class$java$lang$Object());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1()) {
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
               var0[0] = "get";
               var0[1] = "TYPE_MAP";
               var0[2] = "getType";
               var0[3] = "forName";
               var0[4] = "getType";
               var0[5] = "putAt";
               var0[6] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[7];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxBuilderTools$_getSignatureFromParamInfo_closure1 = class$("groovy.jmx.builder.JmxBuilderTools$_getSignatureFromParamInfo_closure1");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$Class() {
               Class var10000 = $class$java$lang$Class;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Class = class$("java.lang.Class");
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
         return (Class[])ScriptBytecodeAdapter.castToType(result.get(), $get$array$$class$java$lang$Class());
      }
   }

   public static String getNormalizedType(String type) {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var1[29].callStatic($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)type))) {
         return (String)ScriptBytecodeAdapter.castToType(var1[30].callGetProperty(var1[31].call(PRIMITIVE_TYPES, (Object)type)), $get$$class$java$lang$String());
      } else {
         Object var10000 = var1[32].callGetPropertySafe(var1[33].call(TYPE_MAP, (Object)type));
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var1[34].callGetPropertySafe(var1[35].call($get$$class$java$lang$Class(), (Object)type));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = null;
            }
         }

         return (String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String());
      }
   }

   private static boolean typeIsPrimitive(String typeName) {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(var1[36].call(PRIMITIVE_TYPES, (Object)typeName), $get$$class$java$lang$Boolean()));
   }

   public static boolean isClassMBean(Class cls) {
      CallSite[] var1 = $getCallSiteArray();
      Boolean result = Boolean.FALSE;
      if (ScriptBytecodeAdapter.compareEqual(cls, (Object)null)) {
         result = Boolean.FALSE;
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[37].call($get$$class$javax$management$DynamicMBean(), (Object)cls))) {
         result = Boolean.TRUE;
      }

      Object face = null;
      Object var4 = var1[38].call(var1[39].call(cls));

      while(((Iterator)var4).hasNext()) {
         face = ((Iterator)var4).next();
         String name = (String)ScriptBytecodeAdapter.castToType(var1[40].call(face), $get$$class$java$lang$String());
         if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var1[41].call(name, (Object)"MBean")) && !DefaultTypeTransformation.booleanUnbox(var1[42].call(name, (Object)"MXBean")) ? Boolean.FALSE : Boolean.TRUE)) {
            result = Boolean.TRUE;
            break;
         }
      }

      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Boolean()));
   }

   public static GroovyMBean registerMBeanFromMap(String regPolicy, Map metaMap) {
      CallSite[] var2 = $getCallSiteArray();
      Object info = var2[43].call($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), (Object)metaMap);
      Object mbean = null;
      if (DefaultTypeTransformation.booleanUnbox(var2[44].callGetProperty(metaMap))) {
         mbean = var2[45].callGetProperty(metaMap);
      } else {
         mbean = var2[46].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderModelMBean(), (Object)info);
         var2[47].call(mbean, var2[48].callGetProperty(metaMap));
         var2[49].call(mbean, var2[50].callGetProperty(metaMap));
         var2[51].call(mbean, var2[52].callGetProperty(metaMap));
         if (DefaultTypeTransformation.booleanUnbox(var2[53].callGetProperty(metaMap))) {
            var2[54].call(mbean, var2[55].callGetProperty(metaMap), var2[56].callGetProperty(metaMap));
         }
      }

      Object gbean = null;
      if (ScriptBytecodeAdapter.isCase(regPolicy, "replace")) {
         if (DefaultTypeTransformation.booleanUnbox(var2[57].call(var2[58].callGetProperty(metaMap), var2[59].callGetProperty(metaMap)))) {
            var2[60].call(var2[61].callGetProperty(metaMap), var2[62].callGetProperty(metaMap));
         }

         var2[63].call(var2[64].callGetProperty(metaMap), mbean, var2[65].callGetProperty(metaMap));
         gbean = var2[66].callConstructor($get$$class$groovy$util$GroovyMBean(), var2[67].callGetProperty(metaMap), var2[68].callGetProperty(metaMap));
      } else {
         if (ScriptBytecodeAdapter.isCase(regPolicy, "ignore")) {
            if (DefaultTypeTransformation.booleanUnbox(var2[69].call(var2[70].callGetProperty(metaMap), var2[71].callGetProperty(metaMap)))) {
               return (GroovyMBean)ScriptBytecodeAdapter.castToType(gbean, $get$$class$groovy$util$GroovyMBean());
            }
         } else if (ScriptBytecodeAdapter.isCase(regPolicy, "error")) {
         }

         if (DefaultTypeTransformation.booleanUnbox(var2[72].call(var2[73].callGetProperty(metaMap), var2[74].callGetProperty(metaMap)))) {
            throw (Throwable)var2[75].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{var2[76].callGetProperty(metaMap)}, new String[]{"A Bean with name ", " is already registered on the server."})));
         }

         var2[77].call(var2[78].callGetProperty(metaMap), mbean, var2[79].callGetProperty(metaMap));
         gbean = var2[80].callConstructor($get$$class$groovy$util$GroovyMBean(), var2[81].callGetProperty(metaMap), var2[82].callGetProperty(metaMap));
      }

      return (GroovyMBean)ScriptBytecodeAdapter.castToType(gbean, $get$$class$groovy$util$GroovyMBean());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBuilderTools()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxBuilderTools();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxBuilderTools(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxBuilderTools(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   static {
      TYPE_MAP = (Map)ScriptBytecodeAdapter.createMap(new Object[]{"object", $get$$class$java$lang$Object(), "Object", $get$$class$java$lang$Object(), "java.lang.Object", $get$$class$java$lang$Object(), "string", $get$$class$java$lang$String(), "String", $get$$class$java$lang$String(), "java.lang.String", $get$$class$java$lang$String(), "char", Character.TYPE, "character", $get$$class$java$lang$Character(), "Character", $get$$class$java$lang$Character(), "java.lang.Character", $get$$class$java$lang$Character(), "byte", Byte.TYPE, "Byte", $get$$class$java$lang$Byte(), "java.lang.Byte", $get$$class$java$lang$Byte(), "short", Short.TYPE, "Short", $get$$class$java$lang$Short(), "java.lang.Short", $get$$class$java$lang$Short(), "int", Integer.TYPE, "integer", $get$$class$java$lang$Integer(), "Integer", $get$$class$java$lang$Integer(), "java.lang.Integer", $get$$class$java$lang$Integer(), "long", Long.TYPE, "Long", $get$$class$java$lang$Long(), "java.lang.Long", $get$$class$java$lang$Long(), "float", Float.TYPE, "Float", $get$$class$java$lang$Float(), "java.lang.Float", $get$$class$java$lang$Float(), "double", Double.TYPE, "Double", $get$$class$java$lang$Double(), "java.lang.Double", $get$$class$java$lang$Double(), "boolean", Boolean.TYPE, "Boolean", $get$$class$java$lang$Boolean(), "java.lang.Boolean", $get$$class$java$lang$Boolean(), "bigDec", $get$$class$java$math$BigDecimal(), "bigDecimal", $get$$class$java$math$BigDecimal(), "BigDecimal", $get$$class$java$math$BigDecimal(), "java.math.BigDecimal", $get$$class$java$math$BigDecimal(), "bigInt", $get$$class$java$math$BigInteger(), "bigInteger", $get$$class$java$math$BigInteger(), "BigInteger", $get$$class$java$math$BigInteger(), "java.math.BigInteger", $get$$class$java$math$BigInteger(), "date", $get$$class$java$util$Date(), "java.util.Date", $get$$class$java$util$Date()});
   }

   public static String getDEFAULT_DOMAIN() {
      return DEFAULT_DOMAIN;
   }

   public static void setDEFAULT_DOMAIN(String var0) {
      DEFAULT_DOMAIN = var0;
   }

   public static String getDEFAULT_NAME_TYPE() {
      return DEFAULT_NAME_TYPE;
   }

   public static void setDEFAULT_NAME_TYPE(String var0) {
      DEFAULT_NAME_TYPE = var0;
   }

   public static String getNODE_NAME_ATTRIBUTES() {
      return NODE_NAME_ATTRIBUTES;
   }

   public static void setNODE_NAME_ATTRIBUTES(String var0) {
      NODE_NAME_ATTRIBUTES = var0;
   }

   public static String getNODE_NAME_ATTRIBS() {
      return NODE_NAME_ATTRIBS;
   }

   public static void setNODE_NAME_ATTRIBS(String var0) {
      NODE_NAME_ATTRIBS = var0;
   }

   public static String getNODE_NAME_CONSTRUCTORS() {
      return NODE_NAME_CONSTRUCTORS;
   }

   public static void setNODE_NAME_CONSTRUCTORS(String var0) {
      NODE_NAME_CONSTRUCTORS = var0;
   }

   public static String getNODE_NAME_CTORS() {
      return NODE_NAME_CTORS;
   }

   public static void setNODE_NAME_CTORS(String var0) {
      NODE_NAME_CTORS = var0;
   }

   public static String getNODE_NAME_OPERATIONS() {
      return NODE_NAME_OPERATIONS;
   }

   public static void setNODE_NAME_OPERATIONS(String var0) {
      NODE_NAME_OPERATIONS = var0;
   }

   public static String getNODE_NAME_OPS() {
      return NODE_NAME_OPS;
   }

   public static void setNODE_NAME_OPS(String var0) {
      NODE_NAME_OPS = var0;
   }

   public static String getATTRIB_KEY_DESCRIPTION() {
      return ATTRIB_KEY_DESCRIPTION;
   }

   public static void setATTRIB_KEY_DESCRIPTION(String var0) {
      ATTRIB_KEY_DESCRIPTION = var0;
   }

   public static String getATTRIB_KEY_DESC() {
      return ATTRIB_KEY_DESC;
   }

   public static void setATTRIB_KEY_DESC(String var0) {
      ATTRIB_KEY_DESC = var0;
   }

   public static String getATTRIB_KEY_TYPE() {
      return ATTRIB_KEY_TYPE;
   }

   public static void setATTRIB_KEY_TYPE(String var0) {
      ATTRIB_KEY_TYPE = var0;
   }

   public static String getATTRIB_KEY_DEFAULT() {
      return ATTRIB_KEY_DEFAULT;
   }

   public static void setATTRIB_KEY_DEFAULT(String var0) {
      ATTRIB_KEY_DEFAULT = var0;
   }

   public static String getJMX_KEY() {
      return JMX_KEY;
   }

   public static void setJMX_KEY(String var0) {
      JMX_KEY = var0;
   }

   public static String getDESC_KEY() {
      return DESC_KEY;
   }

   public static void setDESC_KEY(String var0) {
      DESC_KEY = var0;
   }

   public static String getDESC_KEY_MBEAN_RESOURCE() {
      return DESC_KEY_MBEAN_RESOURCE;
   }

   public static void setDESC_KEY_MBEAN_RESOURCE(String var0) {
      DESC_KEY_MBEAN_RESOURCE = var0;
   }

   public static String getDESC_KEY_MBEAN_RESOURCE_TYPE() {
      return DESC_KEY_MBEAN_RESOURCE_TYPE;
   }

   public static void setDESC_KEY_MBEAN_RESOURCE_TYPE(String var0) {
      DESC_KEY_MBEAN_RESOURCE_TYPE = var0;
   }

   public static String getDESC_KEY_MBEAN_ATTRIBS() {
      return DESC_KEY_MBEAN_ATTRIBS;
   }

   public static void setDESC_KEY_MBEAN_ATTRIBS(String var0) {
      DESC_KEY_MBEAN_ATTRIBS = var0;
   }

   public static String getDESC_KEY_MBEAN_OPS() {
      return DESC_KEY_MBEAN_OPS;
   }

   public static void setDESC_KEY_MBEAN_OPS(String var0) {
      DESC_KEY_MBEAN_OPS = var0;
   }

   public static String getDESC_KEY_MBEAN_CTORS() {
      return DESC_KEY_MBEAN_CTORS;
   }

   public static void setDESC_KEY_MBEAN_CTORS(String var0) {
      DESC_KEY_MBEAN_CTORS = var0;
   }

   public static String getDESC_KEY_MBEAN_NOTES() {
      return DESC_KEY_MBEAN_NOTES;
   }

   public static void setDESC_KEY_MBEAN_NOTES(String var0) {
      DESC_KEY_MBEAN_NOTES = var0;
   }

   public static String getDESC_KEY_NAME() {
      return DESC_KEY_NAME;
   }

   public static void setDESC_KEY_NAME(String var0) {
      DESC_KEY_NAME = var0;
   }

   public static String getDESC_KEY_JMX_NAME() {
      return DESC_KEY_JMX_NAME;
   }

   public static void setDESC_KEY_JMX_NAME(String var0) {
      DESC_KEY_JMX_NAME = var0;
   }

   public static String getDESC_KEY_DISPLAY_NAME() {
      return DESC_KEY_DISPLAY_NAME;
   }

   public static void setDESC_KEY_DISPLAY_NAME(String var0) {
      DESC_KEY_DISPLAY_NAME = var0;
   }

   public static String getDESC_KEY_TYPE() {
      return DESC_KEY_TYPE;
   }

   public static void setDESC_KEY_TYPE(String var0) {
      DESC_KEY_TYPE = var0;
   }

   public static String getDESC_KEY_GETMETHOD() {
      return DESC_KEY_GETMETHOD;
   }

   public static void setDESC_KEY_GETMETHOD(String var0) {
      DESC_KEY_GETMETHOD = var0;
   }

   public static String getDESC_KEY_SETMETHOD() {
      return DESC_KEY_SETMETHOD;
   }

   public static void setDESC_KEY_SETMETHOD(String var0) {
      DESC_KEY_SETMETHOD = var0;
   }

   public static String getDESC_KEY_EVENT_TYPE() {
      return DESC_KEY_EVENT_TYPE;
   }

   public static void setDESC_KEY_EVENT_TYPE(String var0) {
      DESC_KEY_EVENT_TYPE = var0;
   }

   public static String getDESC_KEY_EVENT_NAME() {
      return DESC_KEY_EVENT_NAME;
   }

   public static void setDESC_KEY_EVENT_NAME(String var0) {
      DESC_KEY_EVENT_NAME = var0;
   }

   public static String getDESC_KEY_EVENT_SOURCE() {
      return DESC_KEY_EVENT_SOURCE;
   }

   public static void setDESC_KEY_EVENT_SOURCE(String var0) {
      DESC_KEY_EVENT_SOURCE = var0;
   }

   public static String getDESC_KEY_EVENT_MESSAGE() {
      return DESC_KEY_EVENT_MESSAGE;
   }

   public static void setDESC_KEY_EVENT_MESSAGE(String var0) {
      DESC_KEY_EVENT_MESSAGE = var0;
   }

   public static String getDESC_VAL_TYPE_ATTRIB() {
      return DESC_VAL_TYPE_ATTRIB;
   }

   public static void setDESC_VAL_TYPE_ATTRIB(String var0) {
      DESC_VAL_TYPE_ATTRIB = var0;
   }

   public static String getDESC_VAL_TYPE_GETTER() {
      return DESC_VAL_TYPE_GETTER;
   }

   public static void setDESC_VAL_TYPE_GETTER(String var0) {
      DESC_VAL_TYPE_GETTER = var0;
   }

   public static String getDESC_VAL_TYPE_SETTER() {
      return DESC_VAL_TYPE_SETTER;
   }

   public static void setDESC_VAL_TYPE_SETTER(String var0) {
      DESC_VAL_TYPE_SETTER = var0;
   }

   public static String getDESC_VAL_TYPE_OP() {
      return DESC_VAL_TYPE_OP;
   }

   public static void setDESC_VAL_TYPE_OP(String var0) {
      DESC_VAL_TYPE_OP = var0;
   }

   public static String getDESC_VAL_TYPE_NOTIFICATION() {
      return DESC_VAL_TYPE_NOTIFICATION;
   }

   public static void setDESC_VAL_TYPE_NOTIFICATION(String var0) {
      DESC_VAL_TYPE_NOTIFICATION = var0;
   }

   public static String getDESC_VAL_TYPE_CTOR() {
      return DESC_VAL_TYPE_CTOR;
   }

   public static void setDESC_VAL_TYPE_CTOR(String var0) {
      DESC_VAL_TYPE_CTOR = var0;
   }

   public static String getDESC_VAL_TYPE_MBEAN() {
      return DESC_VAL_TYPE_MBEAN;
   }

   public static void setDESC_VAL_TYPE_MBEAN(String var0) {
      DESC_VAL_TYPE_MBEAN = var0;
   }

   public static String getDESC_KEY_ROLE() {
      return DESC_KEY_ROLE;
   }

   public static void setDESC_KEY_ROLE(String var0) {
      DESC_KEY_ROLE = var0;
   }

   public static String getDESC_KEY_READABLE() {
      return DESC_KEY_READABLE;
   }

   public static void setDESC_KEY_READABLE(String var0) {
      DESC_KEY_READABLE = var0;
   }

   public static String getDESC_KEY_WRITABLE() {
      return DESC_KEY_WRITABLE;
   }

   public static void setDESC_KEY_WRITABLE(String var0) {
      DESC_KEY_WRITABLE = var0;
   }

   public static String getDESC_KEY_SIGNATURE() {
      return DESC_KEY_SIGNATURE;
   }

   public static void setDESC_KEY_SIGNATURE(String var0) {
      DESC_KEY_SIGNATURE = var0;
   }

   public static String getEVENT_KEY_CONTEXTS() {
      return EVENT_KEY_CONTEXTS;
   }

   public static void setEVENT_KEY_CONTEXTS(String var0) {
      EVENT_KEY_CONTEXTS = var0;
   }

   public static String getEVENT_KEY_CALLBACK() {
      return EVENT_KEY_CALLBACK;
   }

   public static void setEVENT_KEY_CALLBACK(String var0) {
      EVENT_KEY_CALLBACK = var0;
   }

   public static String getEVENT_KEY_CALLBACK_RESULT() {
      return EVENT_KEY_CALLBACK_RESULT;
   }

   public static void setEVENT_KEY_CALLBACK_RESULT(String var0) {
      EVENT_KEY_CALLBACK_RESULT = var0;
   }

   public static String getEVENT_KEY_METHOD() {
      return EVENT_KEY_METHOD;
   }

   public static void setEVENT_KEY_METHOD(String var0) {
      EVENT_KEY_METHOD = var0;
   }

   public static String getEVENT_KEY_METHOD_RESULT() {
      return EVENT_KEY_METHOD_RESULT;
   }

   public static void setEVENT_KEY_METHOD_RESULT(String var0) {
      EVENT_KEY_METHOD_RESULT = var0;
   }

   public static String getEVENT_KEY_ISATTRIB() {
      return EVENT_KEY_ISATTRIB;
   }

   public static void setEVENT_KEY_ISATTRIB(String var0) {
      EVENT_KEY_ISATTRIB = var0;
   }

   public static String getEVENT_KEY_NAME() {
      return EVENT_KEY_NAME;
   }

   public static void setEVENT_KEY_NAME(String var0) {
      EVENT_KEY_NAME = var0;
   }

   public static String getEVENT_KEY_MESSAGE() {
      return EVENT_KEY_MESSAGE;
   }

   public static void setEVENT_KEY_MESSAGE(String var0) {
      EVENT_KEY_MESSAGE = var0;
   }

   public static String getEVENT_KEY_TYPE() {
      return EVENT_KEY_TYPE;
   }

   public static void setEVENT_KEY_TYPE(String var0) {
      EVENT_KEY_TYPE = var0;
   }

   public static String getEVENT_KEY_NODE_TYPE() {
      return EVENT_KEY_NODE_TYPE;
   }

   public static void setEVENT_KEY_NODE_TYPE(String var0) {
      EVENT_KEY_NODE_TYPE = var0;
   }

   public static String getEVENT_VAL_NODETYPE_BROADCASTER() {
      return EVENT_VAL_NODETYPE_BROADCASTER;
   }

   public static void setEVENT_VAL_NODETYPE_BROADCASTER(String var0) {
      EVENT_VAL_NODETYPE_BROADCASTER = var0;
   }

   public static String getEVENT_VAL_NODETYPE_LISTENER() {
      return EVENT_VAL_NODETYPE_LISTENER;
   }

   public static void setEVENT_VAL_NODETYPE_LISTENER(String var0) {
      EVENT_VAL_NODETYPE_LISTENER = var0;
   }

   public static String getEVENT_KEY_TARGETS() {
      return EVENT_KEY_TARGETS;
   }

   public static void setEVENT_KEY_TARGETS(String var0) {
      EVENT_KEY_TARGETS = var0;
   }

   public static Map getPRIMITIVE_TYPES() {
      return PRIMITIVE_TYPES;
   }

   public static void setPRIMITIVE_TYPES(Map var0) {
      PRIMITIVE_TYPES = var0;
   }

   public static Map getTYPE_MAP() {
      return TYPE_MAP;
   }

   public static void setTYPE_MAP(Map var0) {
      TYPE_MAP = var0;
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
      var0[0] = "length";
      var0[1] = "toUpperCase";
      var0[2] = "length";
      var0[3] = "plus";
      var0[4] = "toUpperCase";
      var0[5] = "getAt";
      var0[6] = "getAt";
      var0[7] = "toUpperCase";
      var0[8] = "length";
      var0[9] = "toLowerCase";
      var0[10] = "length";
      var0[11] = "plus";
      var0[12] = "toLowerCase";
      var0[13] = "getAt";
      var0[14] = "getAt";
      var0[15] = "toLowerCase";
      var0[16] = "plus";
      var0[17] = "getName";
      var0[18] = "getClass";
      var0[19] = "hashCode";
      var0[20] = "<$constructor$>";
      var0[21] = "<$constructor$>";
      var0[22] = "findMBeanServer";
      var0[23] = "size";
      var0[24] = "getAt";
      var0[25] = "getPlatformMBeanServer";
      var0[26] = "size";
      var0[27] = "size";
      var0[28] = "eachWithIndex";
      var0[29] = "typeIsPrimitive";
      var0[30] = "name";
      var0[31] = "getAt";
      var0[32] = "name";
      var0[33] = "getAt";
      var0[34] = "name";
      var0[35] = "forName";
      var0[36] = "containsKey";
      var0[37] = "isAssignableFrom";
      var0[38] = "iterator";
      var0[39] = "getInterfaces";
      var0[40] = "getName";
      var0[41] = "endsWith";
      var0[42] = "endsWith";
      var0[43] = "getModelMBeanInfoFromMap";
      var0[44] = "isMBean";
      var0[45] = "target";
      var0[46] = "<$constructor$>";
      var0[47] = "setManagedResource";
      var0[48] = "target";
      var0[49] = "addOperationCallListeners";
      var0[50] = "attributes";
      var0[51] = "addOperationCallListeners";
      var0[52] = "operations";
      var0[53] = "listeners";
      var0[54] = "addEventListeners";
      var0[55] = "server";
      var0[56] = "listeners";
      var0[57] = "isRegistered";
      var0[58] = "server";
      var0[59] = "jmxName";
      var0[60] = "unregisterMBean";
      var0[61] = "server";
      var0[62] = "jmxName";
      var0[63] = "registerMBean";
      var0[64] = "server";
      var0[65] = "jmxName";
      var0[66] = "<$constructor$>";
      var0[67] = "server";
      var0[68] = "jmxName";
      var0[69] = "isRegistered";
      var0[70] = "server";
      var0[71] = "jmxName";
      var0[72] = "isRegistered";
      var0[73] = "server";
      var0[74] = "jmxName";
      var0[75] = "<$constructor$>";
      var0[76] = "jmxName";
      var0[77] = "registerMBean";
      var0[78] = "server";
      var0[79] = "jmxName";
      var0[80] = "<$constructor$>";
      var0[81] = "server";
      var0[82] = "jmxName";
      var0[83] = "TYPE";
      var0[84] = "TYPE";
      var0[85] = "TYPE";
      var0[86] = "TYPE";
      var0[87] = "TYPE";
      var0[88] = "TYPE";
      var0[89] = "TYPE";
      var0[90] = "TYPE";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[91];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBuilderTools(), var0);
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
   private static Class $get$$class$java$lang$Long() {
      Class var10000 = $class$java$lang$Long;
      if (var10000 == null) {
         var10000 = $class$java$lang$Long = class$("java.lang.Long");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$MBeanServerFactory() {
      Class var10000 = $class$javax$management$MBeanServerFactory;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServerFactory = class$("javax.management.MBeanServerFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Character() {
      Class var10000 = $class$java$lang$Character;
      if (var10000 == null) {
         var10000 = $class$java$lang$Character = class$("java.lang.Character");
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
   private static Class $get$$class$groovy$jmx$builder$JmxBuilderModelMBean() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilderModelMBean;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilderModelMBean = class$("groovy.jmx.builder.JmxBuilderModelMBean");
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
   private static Class $get$$class$groovy$jmx$builder$JmxBeanInfoManager() {
      Class var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager = class$("groovy.jmx.builder.JmxBeanInfoManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$math$BigDecimal() {
      Class var10000 = $class$java$math$BigDecimal;
      if (var10000 == null) {
         var10000 = $class$java$math$BigDecimal = class$("java.math.BigDecimal");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$DynamicMBean() {
      Class var10000 = $class$javax$management$DynamicMBean;
      if (var10000 == null) {
         var10000 = $class$javax$management$DynamicMBean = class$("javax.management.DynamicMBean");
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
   private static Class $get$$class$javax$management$MBeanServerConnection() {
      Class var10000 = $class$javax$management$MBeanServerConnection;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServerConnection = class$("javax.management.MBeanServerConnection");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
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
   private static Class $get$$class$java$lang$Short() {
      Class var10000 = $class$java$lang$Short;
      if (var10000 == null) {
         var10000 = $class$java$lang$Short = class$("java.lang.Short");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Integer() {
      Class var10000 = $class$java$lang$Integer;
      if (var10000 == null) {
         var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
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
   private static Class $get$$class$java$lang$Double() {
      Class var10000 = $class$java$lang$Double;
      if (var10000 == null) {
         var10000 = $class$java$lang$Double = class$("java.lang.Double");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Byte() {
      Class var10000 = $class$java$lang$Byte;
      if (var10000 == null) {
         var10000 = $class$java$lang$Byte = class$("java.lang.Byte");
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
   private static Class $get$$class$java$util$Date() {
      Class var10000 = $class$java$util$Date;
      if (var10000 == null) {
         var10000 = $class$java$util$Date = class$("java.util.Date");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$management$ManagementFactory() {
      Class var10000 = $class$java$lang$management$ManagementFactory;
      if (var10000 == null) {
         var10000 = $class$java$lang$management$ManagementFactory = class$("java.lang.management.ManagementFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Float() {
      Class var10000 = $class$java$lang$Float;
      if (var10000 == null) {
         var10000 = $class$java$lang$Float = class$("java.lang.Float");
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
   private static Class $get$$class$java$math$BigInteger() {
      Class var10000 = $class$java$math$BigInteger;
      if (var10000 == null) {
         var10000 = $class$java$math$BigInteger = class$("java.math.BigInteger");
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
