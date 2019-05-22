package groovy.jmx.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.management.ObjectName;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxBeanInfoManager implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202792L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202792 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$DescriptorSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxAttributeInfoManager;
   // $FF: synthetic field
   private static Class array$$class$javax$management$modelmbean$ModelMBeanOperationInfo;
   // $FF: synthetic field
   private static Class $class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class array$$class$javax$management$modelmbean$ModelMBeanAttributeInfo;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxOperationInfoManager;
   // $FF: synthetic field
   private static Class array$$class$javax$management$modelmbean$ModelMBeanConstructorInfo;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class array$$class$javax$management$modelmbean$ModelMBeanNotificationInfo;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$ModelMBeanInfoSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBeanInfoManager;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$ModelMBeanInfo;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;

   public JmxBeanInfoManager() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static ObjectName buildDefaultObjectName(String defaultDomain, String defaultType, Object object) {
      CallSite[] var3 = $getCallSiteArray();
      Object name = new GStringImpl(new Object[]{defaultDomain, defaultType, var3[0].callGetProperty(var3[1].callGetProperty(object)), var3[2].call(object)}, new String[]{"", ":type=", ",name=", "@", ""});
      return (ObjectName)ScriptBytecodeAdapter.castToType(var3[3].callConstructor($get$$class$javax$management$ObjectName(), (Object)name), $get$$class$javax$management$ObjectName());
   }

   public static ModelMBeanInfo getModelMBeanInfoFromMap(Map map) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(map)) {
         throw (Throwable)var1[4].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable to create default ModelMBeanInfo, missing meta map.");
      } else {
         Object object = new Reference(var1[5].callGetProperty(map));
         if (!DefaultTypeTransformation.booleanUnbox(object.get())) {
            throw (Throwable)var1[6].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable to create default ModelMBeanInfo, missing target object.");
         } else {
            Object attributes = var1[7].call($get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), (Object)var1[8].callGetProperty(map));
            Object var10000 = var1[9].call($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), (Object)var1[10].callGetProperty(map));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = ScriptBytecodeAdapter.createList(new Object[0]);
            }

            Object operations = new Reference(var10000);
            var1[11].call(attributes, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), $get$$class$groovy$jmx$builder$JmxBeanInfoManager(), operations, object) {
               private Reference<T> operations;
               private Reference<T> object;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxOperationInfoManager;
               // $FF: synthetic field
               private static Class $class$groovy$lang$MetaProperty;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxBuilderTools;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.operations = (Reference)operations;
                  this.object = (Reference)object;
               }

               public Object doCall(Object info) {
                  Object infox = new Reference(info);
                  CallSite[] var3 = $getCallSiteArray();
                  MetaProperty prop = new Reference((MetaProperty)ScriptBytecodeAdapter.castToType(var3[0].call(var3[1].callGetProperty(this.object.get()), var3[2].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var3[3].callGetProperty(infox.get()))), $get$$class$groovy$lang$MetaProperty()));
                  if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(prop.get()) && DefaultTypeTransformation.booleanUnbox(var3[4].call(infox.get())) ? Boolean.TRUE : Boolean.FALSE)) {
                     var3[5].call(this.operations.get(), var3[6].call($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), (Object)prop.get()));
                  }

                  return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(prop.get()) && DefaultTypeTransformation.booleanUnbox(var3[7].call(infox.get())) ? Boolean.TRUE : Boolean.FALSE) ? var3[8].call(this.operations.get(), var3[9].call($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), (Object)prop.get())) : null;
               }

               public Object getOperations() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.operations.get();
               }

               public Object getObject() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.object.get();
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1()) {
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
                  var0[3] = "name";
                  var0[4] = "isReadable";
                  var0[5] = "leftShift";
                  var0[6] = "createGetterOperationInfoFromProperty";
                  var0[7] = "isWritable";
                  var0[8] = "leftShift";
                  var0[9] = "createSetterOperationInfoFromProperty";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[10];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1(), var0);
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
               private static Class $get$$class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1() {
                  Class var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1 = class$("groovy.jmx.builder.JmxBeanInfoManager$_getModelMBeanInfoFromMap_closure1");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager() {
                  Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager = class$("groovy.jmx.builder.JmxOperationInfoManager");
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
            ModelMBeanAttributeInfo[] attribs = (ModelMBeanAttributeInfo[])ScriptBytecodeAdapter.castToType(attributes, $get$array$$class$javax$management$modelmbean$ModelMBeanAttributeInfo());
            ModelMBeanConstructorInfo[] ctors = (ModelMBeanConstructorInfo[])ScriptBytecodeAdapter.castToType(var1[12].call($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), (Object)var1[13].callGetProperty(map)), $get$array$$class$javax$management$modelmbean$ModelMBeanConstructorInfo());
            ModelMBeanOperationInfo[] ops = (ModelMBeanOperationInfo[])ScriptBytecodeAdapter.castToType(operations.get(), $get$array$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
            ModelMBeanNotificationInfo[] notes = (ModelMBeanNotificationInfo[])ScriptBytecodeAdapter.castToType((Object)null, $get$array$$class$javax$management$modelmbean$ModelMBeanNotificationInfo());
            DescriptorSupport desc = var1[14].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport());
            var1[15].call(desc, var1[16].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[17].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            var1[18].call(desc, var1[19].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[20].callGetProperty(map));
            var1[21].call(desc, var1[22].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[23].callGetProperty(map));
            return (ModelMBeanInfo)ScriptBytecodeAdapter.castToType(var1[24].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanInfoSupport(), (Object[])ArrayUtil.createArray(ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[25].callGetProperty(var1[26].call(object.get())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[27].call(desc, var1[28].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), attribs, ctors, ops, notes, desc)), $get$$class$javax$management$modelmbean$ModelMBeanInfo());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxBeanInfoManager()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxBeanInfoManager();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxBeanInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "canonicalName";
      var0[1] = "class";
      var0[2] = "hashCode";
      var0[3] = "<$constructor$>";
      var0[4] = "<$constructor$>";
      var0[5] = "target";
      var0[6] = "<$constructor$>";
      var0[7] = "getAttributeInfosFromMap";
      var0[8] = "attributes";
      var0[9] = "getOperationInfosFromMap";
      var0[10] = "operations";
      var0[11] = "each";
      var0[12] = "getConstructorInfosFromMap";
      var0[13] = "constructors";
      var0[14] = "<$constructor$>";
      var0[15] = "setField";
      var0[16] = "DESC_KEY_TYPE";
      var0[17] = "DESC_VAL_TYPE_MBEAN";
      var0[18] = "setField";
      var0[19] = "DESC_KEY_DISPLAY_NAME";
      var0[20] = "displayName";
      var0[21] = "setField";
      var0[22] = "DESC_KEY_NAME";
      var0[23] = "name";
      var0[24] = "<$constructor$>";
      var0[25] = "name";
      var0[26] = "getClass";
      var0[27] = "getFieldValue";
      var0[28] = "DESC_KEY_DISPLAY_NAME";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[29];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxBeanInfoManager(), var0);
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
   private static Class $get$$class$javax$management$modelmbean$DescriptorSupport() {
      Class var10000 = $class$javax$management$modelmbean$DescriptorSupport;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$DescriptorSupport = class$("javax.management.modelmbean.DescriptorSupport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxAttributeInfoManager() {
      Class var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager = class$("groovy.jmx.builder.JmxAttributeInfoManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$javax$management$modelmbean$ModelMBeanOperationInfo() {
      Class var10000 = array$$class$javax$management$modelmbean$ModelMBeanOperationInfo;
      if (var10000 == null) {
         var10000 = array$$class$javax$management$modelmbean$ModelMBeanOperationInfo = class$("[Ljavax.management.modelmbean.ModelMBeanOperationInfo;");
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
   private static Class $get$array$$class$javax$management$modelmbean$ModelMBeanAttributeInfo() {
      Class var10000 = array$$class$javax$management$modelmbean$ModelMBeanAttributeInfo;
      if (var10000 == null) {
         var10000 = array$$class$javax$management$modelmbean$ModelMBeanAttributeInfo = class$("[Ljavax.management.modelmbean.ModelMBeanAttributeInfo;");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager() {
      Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager = class$("groovy.jmx.builder.JmxOperationInfoManager");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$javax$management$modelmbean$ModelMBeanConstructorInfo() {
      Class var10000 = array$$class$javax$management$modelmbean$ModelMBeanConstructorInfo;
      if (var10000 == null) {
         var10000 = array$$class$javax$management$modelmbean$ModelMBeanConstructorInfo = class$("[Ljavax.management.modelmbean.ModelMBeanConstructorInfo;");
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
   private static Class $get$array$$class$javax$management$modelmbean$ModelMBeanNotificationInfo() {
      Class var10000 = array$$class$javax$management$modelmbean$ModelMBeanNotificationInfo;
      if (var10000 == null) {
         var10000 = array$$class$javax$management$modelmbean$ModelMBeanNotificationInfo = class$("[Ljavax.management.modelmbean.ModelMBeanNotificationInfo;");
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
   private static Class $get$$class$javax$management$modelmbean$ModelMBeanInfoSupport() {
      Class var10000 = $class$javax$management$modelmbean$ModelMBeanInfoSupport;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$ModelMBeanInfoSupport = class$("javax.management.modelmbean.ModelMBeanInfoSupport");
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
   private static Class $get$$class$javax$management$modelmbean$ModelMBeanInfo() {
      Class var10000 = $class$javax$management$modelmbean$ModelMBeanInfo;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$ModelMBeanInfo = class$("javax.management.modelmbean.ModelMBeanInfo");
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
