package groovy.jmx.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxAttributeInfoManager implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202782L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202782 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$DescriptorSupport;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxAttributeInfoManager;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$ModelMBeanAttributeInfo;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaProperty;

   public JmxAttributeInfoManager() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static List<ModelMBeanAttributeInfo> getAttributeInfosFromMap(Map metaMap) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(metaMap)) {
         return (List)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$List());
      } else {
         Object attribs = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         var1[0].call(metaMap, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), $get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), attribs) {
            private Reference<T> attribs;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxAttributeInfoManager;
            // $FF: synthetic field
            private static Class $class$javax$management$modelmbean$ModelMBeanAttributeInfo;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.attribs = (Reference)attribs;
            }

            public Object doCall(Object attribName, Object map) {
               Object attribNamex = new Reference(attribName);
               Object mapx = new Reference(map);
               CallSite[] var5 = $getCallSiteArray();
               ScriptBytecodeAdapter.setProperty(attribNamex.get(), $get$$class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1(), mapx.get(), "name");
               ModelMBeanAttributeInfo info = (ModelMBeanAttributeInfo)ScriptBytecodeAdapter.castToType(var5[0].callStatic($get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), mapx.get()), $get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo());
               return var5[1].call(this.attribs.get(), (Object)info);
            }

            public Object call(Object attribName, Object map) {
               Object attribNamex = new Reference(attribName);
               Object mapx = new Reference(map);
               CallSite[] var5 = $getCallSiteArray();
               return var5[2].callCurrent(this, attribNamex.get(), mapx.get());
            }

            public Object getAttribs() {
               CallSite[] var1 = $getCallSiteArray();
               return this.attribs.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1()) {
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
               var0[0] = "getAttributeInfoFromMap";
               var0[1] = "leftShift";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxAttributeInfoManager() {
               Class var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager = class$("groovy.jmx.builder.JmxAttributeInfoManager");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo() {
               Class var10000 = $class$javax$management$modelmbean$ModelMBeanAttributeInfo;
               if (var10000 == null) {
                  var10000 = $class$javax$management$modelmbean$ModelMBeanAttributeInfo = class$("javax.management.modelmbean.ModelMBeanAttributeInfo");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1 = class$("groovy.jmx.builder.JmxAttributeInfoManager$_getAttributeInfosFromMap_closure1");
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
         return (List)ScriptBytecodeAdapter.castToType(attribs.get(), $get$$class$java$util$List());
      }
   }

   public static ModelMBeanAttributeInfo getAttributeInfoFromMap(Map map) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(map)) {
         return (ModelMBeanAttributeInfo)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo());
      } else {
         MetaProperty prop = (MetaProperty)ScriptBytecodeAdapter.castToType(var1[1].call(map, (Object)"property"), $get$$class$groovy$lang$MetaProperty());
         if (!DefaultTypeTransformation.booleanUnbox(prop)) {
            throw (Throwable)var1[2].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable generate ModelMBeanAttributeInfo, missing property object.");
         } else {
            DescriptorSupport desc = var1[3].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport());
            var1[4].call(desc, var1[5].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[6].call(map, (Object)var1[7].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[8].call(desc, var1[9].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[10].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            Object var10000 = var1[11].call(map, (Object)var1[12].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = Boolean.TRUE;
            }

            Boolean isReadable = (Boolean)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$Boolean());
            var10000 = var1[13].call(map, (Object)var1[14].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = Boolean.FALSE;
            }

            Boolean isWritable = (Boolean)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$Boolean());
            var1[15].call(desc, var1[16].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), isReadable);
            var1[17].call(desc, var1[18].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), isWritable);
            if (DefaultTypeTransformation.booleanUnbox(isReadable)) {
               var1[19].call(desc, var1[20].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[21].call(map, (Object)var1[22].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            }

            if (DefaultTypeTransformation.booleanUnbox(isWritable)) {
               var1[23].call(desc, var1[24].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[25].call(map, (Object)var1[26].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            }

            var1[27].call(desc, "default", var1[28].call(map, (Object)"defaultValue"));
            var1[29].call(desc, var1[30].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[31].call(map, (Object)var1[32].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            ModelMBeanAttributeInfo attrib = var1[33].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo(), (Object[])ArrayUtil.createArray(ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[34].call(desc, var1[35].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), var1[36].call(var1[37].callGetProperty(prop)), ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[38].call(desc, var1[39].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.castToType(var1[40].call(desc, var1[41].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$Boolean()), Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.castToType(var1[42].call(desc, var1[43].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$Boolean()), Boolean.TYPE), var1[44].callGetProperty(prop) instanceof Boolean ? Boolean.TRUE : Boolean.FALSE));
            var1[45].call(attrib, desc);
            return (ModelMBeanAttributeInfo)ScriptBytecodeAdapter.castToType(attrib, $get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxAttributeInfoManager()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxAttributeInfoManager();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "each";
      var0[1] = "remove";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "setField";
      var0[5] = "DESC_KEY_NAME";
      var0[6] = "remove";
      var0[7] = "DESC_KEY_NAME";
      var0[8] = "setField";
      var0[9] = "DESC_KEY_TYPE";
      var0[10] = "DESC_VAL_TYPE_ATTRIB";
      var0[11] = "remove";
      var0[12] = "DESC_KEY_READABLE";
      var0[13] = "remove";
      var0[14] = "DESC_KEY_WRITABLE";
      var0[15] = "setField";
      var0[16] = "DESC_KEY_READABLE";
      var0[17] = "setField";
      var0[18] = "DESC_KEY_WRITABLE";
      var0[19] = "setField";
      var0[20] = "DESC_KEY_GETMETHOD";
      var0[21] = "remove";
      var0[22] = "DESC_KEY_GETMETHOD";
      var0[23] = "setField";
      var0[24] = "DESC_KEY_SETMETHOD";
      var0[25] = "remove";
      var0[26] = "DESC_KEY_SETMETHOD";
      var0[27] = "setField";
      var0[28] = "remove";
      var0[29] = "setField";
      var0[30] = "DESC_KEY_DISPLAY_NAME";
      var0[31] = "remove";
      var0[32] = "DESC_KEY_DISPLAY_NAME";
      var0[33] = "<$constructor$>";
      var0[34] = "getFieldValue";
      var0[35] = "DESC_KEY_NAME";
      var0[36] = "getName";
      var0[37] = "type";
      var0[38] = "getFieldValue";
      var0[39] = "DESC_KEY_DISPLAY_NAME";
      var0[40] = "getFieldValue";
      var0[41] = "DESC_KEY_READABLE";
      var0[42] = "getFieldValue";
      var0[43] = "DESC_KEY_WRITABLE";
      var0[44] = "type";
      var0[45] = "setDescriptor";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[46];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxAttributeInfoManager(), var0);
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$modelmbean$ModelMBeanAttributeInfo() {
      Class var10000 = $class$javax$management$modelmbean$ModelMBeanAttributeInfo;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$ModelMBeanAttributeInfo = class$("javax.management.modelmbean.ModelMBeanAttributeInfo");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
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
   private static Class $get$$class$groovy$jmx$builder$JmxBuilderTools() {
      Class var10000 = $class$groovy$jmx$builder$JmxBuilderTools;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxBuilderTools = class$("groovy.jmx.builder.JmxBuilderTools");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
