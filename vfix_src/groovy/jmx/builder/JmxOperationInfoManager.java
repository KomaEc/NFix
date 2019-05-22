package groovy.jmx.builder;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxOperationInfoManager implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202884L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202884 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$DescriptorSupport;
   // $FF: synthetic field
   private static Class array$$class$javax$management$MBeanParameterInfo;
   // $FF: synthetic field
   private static Class $class$java$lang$Void;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxOperationInfoManager;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$ModelMBeanConstructorInfo;
   // $FF: synthetic field
   private static Class $class$javax$management$modelmbean$ModelMBeanOperationInfo;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanParameterInfo;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaMethod;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderTools;

   public JmxOperationInfoManager() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static List<ModelMBeanConstructorInfo> getConstructorInfosFromMap(Map metaMap) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(metaMap)) {
         return (List)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$List());
      } else {
         Object ctors = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         var1[0].call(metaMap, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), $get$$class$groovy$jmx$builder$JmxOperationInfoManager(), ctors) {
            private Reference<T> ctors;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxOperationInfoManager;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1;
            // $FF: synthetic field
            private static Class $class$javax$management$modelmbean$ModelMBeanConstructorInfo;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.ctors = (Reference)ctors;
            }

            public Object doCall(Object ctorName, Object map) {
               Object mapx = new Reference(map);
               CallSite[] var4 = $getCallSiteArray();
               ModelMBeanConstructorInfo info = (ModelMBeanConstructorInfo)ScriptBytecodeAdapter.castToType(var4[0].callStatic($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), mapx.get()), $get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo());
               return var4[1].call(this.ctors.get(), (Object)info);
            }

            public Object call(Object ctorName, Object mapx) {
               Object map = new Reference(mapx);
               CallSite[] var4 = $getCallSiteArray();
               return var4[2].callCurrent(this, ctorName, map.get());
            }

            public Object getCtors() {
               CallSite[] var1 = $getCallSiteArray();
               return this.ctors.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1()) {
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
               var0[0] = "getConstructorInfoFromMap";
               var0[1] = "leftShift";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager() {
               Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager = class$("groovy.jmx.builder.JmxOperationInfoManager");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfosFromMap_closure1 = class$("groovy.jmx.builder.JmxOperationInfoManager$_getConstructorInfosFromMap_closure1");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo() {
               Class var10000 = $class$javax$management$modelmbean$ModelMBeanConstructorInfo;
               if (var10000 == null) {
                  var10000 = $class$javax$management$modelmbean$ModelMBeanConstructorInfo = class$("javax.management.modelmbean.ModelMBeanConstructorInfo");
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
         return (List)ScriptBytecodeAdapter.castToType(ctors.get(), $get$$class$java$util$List());
      }
   }

   public static ModelMBeanConstructorInfo getConstructorInfoFromMap(Map map) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(map)) {
         return (ModelMBeanConstructorInfo)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo());
      } else {
         Object ctor = var1[1].call(map, (Object)"constructor");
         if (!DefaultTypeTransformation.booleanUnbox(ctor)) {
            throw (Throwable)var1[2].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable generate ModelMBeanConstructorInfo, missing constructor reference.");
         } else {
            MBeanParameterInfo[] params = (MBeanParameterInfo[])ScriptBytecodeAdapter.castToType(var1[3].callStatic($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), var1[4].call(map, (Object)"params")), $get$array$$class$javax$management$MBeanParameterInfo());
            Descriptor desc = new Reference(var1[5].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport()));
            var1[6].call(desc.get(), var1[7].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[8].call(map, (Object)var1[9].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[10].call(desc.get(), var1[11].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[12].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            var1[13].call(desc.get(), var1[14].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[15].call(map, (Object)var1[16].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[17].call(desc.get(), var1[18].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[19].call(map, (Object)var1[20].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[21].call(map, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), $get$$class$groovy$jmx$builder$JmxOperationInfoManager(), desc) {
               private Reference<T> desc;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$javax$management$Descriptor;
               // $FF: synthetic field
               private static Class $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.desc = (Reference)desc;
               }

               public Object doCall(Object key, Object value) {
                  Object keyx = new Reference(key);
                  Object valuex = new Reference(value);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[0].call(this.desc.get(), keyx.get(), valuex.get());
               }

               public Object call(Object key, Object value) {
                  Object keyx = new Reference(key);
                  Object valuex = new Reference(value);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[1].callCurrent(this, keyx.get(), valuex.get());
               }

               public Descriptor getDesc() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Descriptor)ScriptBytecodeAdapter.castToType(this.desc.get(), $get$$class$javax$management$Descriptor());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2()) {
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
                  var0[0] = "setField";
                  var0[1] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[2];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2(), var0);
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
               private static Class $get$$class$javax$management$Descriptor() {
                  Class var10000 = $class$javax$management$Descriptor;
                  if (var10000 == null) {
                     var10000 = $class$javax$management$Descriptor = class$("javax.management.Descriptor");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2() {
                  Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2;
                  if (var10000 == null) {
                     var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getConstructorInfoFromMap_closure2 = class$("groovy.jmx.builder.JmxOperationInfoManager$_getConstructorInfoFromMap_closure2");
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
            ModelMBeanConstructorInfo info = var1[22].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo(), var1[23].callGetProperty(ctor), ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[24].call(desc.get(), var1[25].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), params, desc.get());
            return (ModelMBeanConstructorInfo)ScriptBytecodeAdapter.castToType(info, $get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo());
         }
      }
   }

   public static List<ModelMBeanOperationInfo> getOperationInfosFromMap(Map metaMap) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(metaMap)) {
         return (List)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$List());
      } else {
         Object ops = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         var1[26].call(metaMap, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), $get$$class$groovy$jmx$builder$JmxOperationInfoManager(), ops) {
            private Reference<T> ops;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxOperationInfoManager;
            // $FF: synthetic field
            private static Class $class$javax$management$modelmbean$ModelMBeanOperationInfo;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.ops = (Reference)ops;
            }

            public Object doCall(Object opNames, Object map) {
               Object mapx = new Reference(map);
               CallSite[] var4 = $getCallSiteArray();
               ModelMBeanOperationInfo info = (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType(var4[0].callStatic($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), mapx.get()), $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
               return var4[1].call(this.ops.get(), (Object)info);
            }

            public Object call(Object opNames, Object mapx) {
               Object map = new Reference(mapx);
               CallSite[] var4 = $getCallSiteArray();
               return var4[2].callCurrent(this, opNames, map.get());
            }

            public Object getOps() {
               CallSite[] var1 = $getCallSiteArray();
               return this.ops.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3()) {
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
               var0[0] = "getOperationInfoFromMap";
               var0[1] = "leftShift";
               var0[2] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[3];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3() {
               Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_getOperationInfosFromMap_closure3 = class$("groovy.jmx.builder.JmxOperationInfoManager$_getOperationInfosFromMap_closure3");
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
            private static Class $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo() {
               Class var10000 = $class$javax$management$modelmbean$ModelMBeanOperationInfo;
               if (var10000 == null) {
                  var10000 = $class$javax$management$modelmbean$ModelMBeanOperationInfo = class$("javax.management.modelmbean.ModelMBeanOperationInfo");
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
         return (List)ScriptBytecodeAdapter.castToType(ops.get(), $get$$class$java$util$List());
      }
   }

   public static ModelMBeanOperationInfo getOperationInfoFromMap(Map map) {
      CallSite[] var1 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(map)) {
         return (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
      } else {
         MetaMethod method = (MetaMethod)ScriptBytecodeAdapter.castToType(var1[27].call(map, (Object)"method"), $get$$class$groovy$lang$MetaMethod());
         if (!DefaultTypeTransformation.booleanUnbox(method)) {
            throw (Throwable)var1[28].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)"Unable to generate ModelMBeanOperationInfo, missing method reference.");
         } else {
            MBeanParameterInfo[] params = (MBeanParameterInfo[])ScriptBytecodeAdapter.castToType(var1[29].callStatic($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), var1[30].call(map, (Object)"params")), $get$array$$class$javax$management$MBeanParameterInfo());
            Descriptor desc = var1[31].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport());
            var1[32].call(desc, var1[33].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[34].call(map, (Object)var1[35].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[36].call(desc, var1[37].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[38].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
            var1[39].call(desc, var1[40].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[41].call(map, (Object)var1[42].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            var1[43].call(desc, var1[44].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[45].call(map, (Object)var1[46].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())));
            ModelMBeanOperationInfo info = var1[47].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo(), (Object[])ArrayUtil.createArray(var1[48].callGetProperty(method), ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[49].call(desc, var1[50].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), params, var1[51].callGetProperty(var1[52].callGetProperty(method)), var1[53].callGetProperty($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo()), desc));
            return (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType(info, $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
         }
      }
   }

   public static List<MBeanParameterInfo> buildParamInfosFromMaps(Map metaMap) {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(metaMap) && !ScriptBytecodeAdapter.compareEqual(var1[54].call(metaMap), $const$0) ? Boolean.FALSE : Boolean.TRUE)) {
         return (List)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$List());
      } else {
         List result = new Reference(var1[55].callConstructor($get$$class$java$util$ArrayList(), (Object)var1[56].call(metaMap)));
         var1[57].call(metaMap, (Object)(new GeneratedClosure($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), $get$$class$groovy$jmx$builder$JmxOperationInfoManager(), result) {
            private Reference<T> result;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4;
            // $FF: synthetic field
            private static Class $class$java$util$List;
            // $FF: synthetic field
            private static Class $class$java$lang$String;
            // $FF: synthetic field
            private static Class $class$javax$management$MBeanParameterInfo;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxBuilderTools;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.result = (Reference)result;
            }

            public Object doCall(Object paramType, Object param) {
               Object paramx = new Reference(param);
               CallSite[] var4 = $getCallSiteArray();
               String type = (String)ScriptBytecodeAdapter.castToType(var4[0].callGetProperty(paramx.get()) instanceof String ? var4[1].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var4[2].callGetProperty(paramx.get())) : var4[3].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var4[4].callGetProperty(var4[5].callGetProperty(paramx.get()))), $get$$class$java$lang$String());
               MBeanParameterInfo info = var4[6].callConstructor($get$$class$javax$management$MBeanParameterInfo(), var4[7].callGetProperty(paramx.get()), type, var4[8].callGetProperty(paramx.get()));
               return var4[9].call(this.result.get(), info);
            }

            public Object call(Object paramType, Object paramx) {
               Object param = new Reference(paramx);
               CallSite[] var4 = $getCallSiteArray();
               return var4[10].callCurrent(this, paramType, param.get());
            }

            public List<MBeanParameterInfo> getResult() {
               CallSite[] var1 = $getCallSiteArray();
               return (List)ScriptBytecodeAdapter.castToType(this.result.get(), $get$$class$java$util$List());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4()) {
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
               var0[0] = "type";
               var0[1] = "getNormalizedType";
               var0[2] = "type";
               var0[3] = "getNormalizedType";
               var0[4] = "name";
               var0[5] = "type";
               var0[6] = "<$constructor$>";
               var0[7] = "name";
               var0[8] = "displayName";
               var0[9] = "leftShift";
               var0[10] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[11];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4() {
               Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager$_buildParamInfosFromMaps_closure4 = class$("groovy.jmx.builder.JmxOperationInfoManager$_buildParamInfosFromMaps_closure4");
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
            private static Class $get$$class$javax$management$MBeanParameterInfo() {
               Class var10000 = $class$javax$management$MBeanParameterInfo;
               if (var10000 == null) {
                  var10000 = $class$javax$management$MBeanParameterInfo = class$("javax.management.MBeanParameterInfo");
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
         return (List)ScriptBytecodeAdapter.castToType(result.get(), $get$$class$java$util$List());
      }
   }

   public static ModelMBeanOperationInfo createGetterOperationInfoFromProperty(MetaProperty prop) {
      CallSite[] var1 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(prop, (Object)null)) {
         return (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
      } else {
         Descriptor desc = var1[58].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport());
         String opType = (String)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(var1[59].call(var1[60].call(var1[61].callGetProperty(prop)), (Object)"Boolean")) ? "is" : "get", $get$$class$java$lang$String());
         String name = (String)ScriptBytecodeAdapter.castToType(var1[62].call(opType, (Object)var1[63].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var1[64].callGetProperty(prop))), $get$$class$java$lang$String());
         var1[65].call(desc, var1[66].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), name);
         var1[67].call(desc, var1[68].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[69].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
         var1[70].call(desc, var1[71].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[72].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
         var1[73].call(desc, var1[74].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[75].call(new GStringImpl(new Object[]{var1[76].callGetProperty(prop)}, new String[]{"Getter for attribute ", ""})));
         ModelMBeanOperationInfo op = var1[77].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo(), (Object[])ArrayUtil.createArray(name, ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[78].call(desc, var1[79].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), (Object)null, var1[80].call(var1[81].callGetProperty(prop)), var1[82].callGetProperty($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo()), desc));
         return (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType(op, $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
      }
   }

   public static ModelMBeanOperationInfo createSetterOperationInfoFromProperty(MetaProperty prop) {
      CallSite[] var1 = $getCallSiteArray();
      Descriptor desc = var1[83].callConstructor($get$$class$javax$management$modelmbean$DescriptorSupport());
      String name = (String)ScriptBytecodeAdapter.castToType(var1[84].call("set", (Object)var1[85].call($get$$class$groovy$jmx$builder$JmxBuilderTools(), (Object)var1[86].callGetProperty(prop))), $get$$class$java$lang$String());
      var1[87].call(desc, var1[88].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), name);
      var1[89].call(desc, var1[90].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[91].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
      var1[92].call(desc, var1[93].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[94].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()));
      var1[95].call(desc, var1[96].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools()), var1[97].call(new GStringImpl(new Object[]{var1[98].callGetProperty(prop)}, new String[]{"Getter for attribute ", ""})));
      MBeanParameterInfo[] params = (MBeanParameterInfo[])ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[99].callConstructor($get$$class$javax$management$MBeanParameterInfo(), var1[100].call(new GStringImpl(new Object[]{var1[101].call(prop)}, new String[]{"", ""})), var1[102].callGetProperty(var1[103].callGetProperty(prop)), "Parameter for setter")}), $get$array$$class$javax$management$MBeanParameterInfo());
      ModelMBeanOperationInfo op = var1[104].callConstructor($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo(), (Object[])ArrayUtil.createArray(name, ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[105].call(desc, var1[106].callGetProperty($get$$class$groovy$jmx$builder$JmxBuilderTools())), $get$$class$java$lang$String()), $get$$class$java$lang$String()), params, var1[107].callGetProperty(var1[108].callGetProperty($get$$class$java$lang$Void())), var1[109].callGetProperty($get$$class$javax$management$modelmbean$ModelMBeanOperationInfo()), desc));
      return (ModelMBeanOperationInfo)ScriptBytecodeAdapter.castToType(op, $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxOperationInfoManager()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxOperationInfoManager();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxOperationInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[3] = "buildParamInfosFromMaps";
      var0[4] = "remove";
      var0[5] = "<$constructor$>";
      var0[6] = "setField";
      var0[7] = "DESC_KEY_NAME";
      var0[8] = "remove";
      var0[9] = "DESC_KEY_NAME";
      var0[10] = "setField";
      var0[11] = "DESC_KEY_TYPE";
      var0[12] = "DESC_VAL_TYPE_OP";
      var0[13] = "setField";
      var0[14] = "DESC_KEY_ROLE";
      var0[15] = "remove";
      var0[16] = "DESC_KEY_ROLE";
      var0[17] = "setField";
      var0[18] = "DESC_KEY_DISPLAY_NAME";
      var0[19] = "remove";
      var0[20] = "DESC_KEY_DISPLAY_NAME";
      var0[21] = "each";
      var0[22] = "<$constructor$>";
      var0[23] = "name";
      var0[24] = "getFieldValue";
      var0[25] = "DESC_KEY_DISPLAY_NAME";
      var0[26] = "each";
      var0[27] = "remove";
      var0[28] = "<$constructor$>";
      var0[29] = "buildParamInfosFromMaps";
      var0[30] = "remove";
      var0[31] = "<$constructor$>";
      var0[32] = "setField";
      var0[33] = "DESC_KEY_NAME";
      var0[34] = "remove";
      var0[35] = "DESC_KEY_NAME";
      var0[36] = "setField";
      var0[37] = "DESC_KEY_TYPE";
      var0[38] = "DESC_VAL_TYPE_OP";
      var0[39] = "setField";
      var0[40] = "DESC_KEY_ROLE";
      var0[41] = "remove";
      var0[42] = "DESC_KEY_ROLE";
      var0[43] = "setField";
      var0[44] = "DESC_KEY_DISPLAY_NAME";
      var0[45] = "remove";
      var0[46] = "DESC_KEY_DISPLAY_NAME";
      var0[47] = "<$constructor$>";
      var0[48] = "name";
      var0[49] = "getFieldValue";
      var0[50] = "DESC_KEY_DISPLAY_NAME";
      var0[51] = "name";
      var0[52] = "returnType";
      var0[53] = "ACTION";
      var0[54] = "size";
      var0[55] = "<$constructor$>";
      var0[56] = "size";
      var0[57] = "each";
      var0[58] = "<$constructor$>";
      var0[59] = "contains";
      var0[60] = "getName";
      var0[61] = "type";
      var0[62] = "plus";
      var0[63] = "capitalize";
      var0[64] = "name";
      var0[65] = "setField";
      var0[66] = "DESC_KEY_NAME";
      var0[67] = "setField";
      var0[68] = "DESC_KEY_TYPE";
      var0[69] = "DESC_VAL_TYPE_OP";
      var0[70] = "setField";
      var0[71] = "DESC_KEY_ROLE";
      var0[72] = "DESC_VAL_TYPE_GETTER";
      var0[73] = "setField";
      var0[74] = "DESC_KEY_DISPLAY_NAME";
      var0[75] = "toString";
      var0[76] = "name";
      var0[77] = "<$constructor$>";
      var0[78] = "getFieldValue";
      var0[79] = "DESC_KEY_DISPLAY_NAME";
      var0[80] = "getName";
      var0[81] = "type";
      var0[82] = "INFO";
      var0[83] = "<$constructor$>";
      var0[84] = "plus";
      var0[85] = "capitalize";
      var0[86] = "name";
      var0[87] = "setField";
      var0[88] = "DESC_KEY_NAME";
      var0[89] = "setField";
      var0[90] = "DESC_KEY_TYPE";
      var0[91] = "DESC_VAL_TYPE_OP";
      var0[92] = "setField";
      var0[93] = "DESC_KEY_ROLE";
      var0[94] = "DESC_VAL_TYPE_SETTER";
      var0[95] = "setField";
      var0[96] = "DESC_KEY_DISPLAY_NAME";
      var0[97] = "toString";
      var0[98] = "name";
      var0[99] = "<$constructor$>";
      var0[100] = "toString";
      var0[101] = "getName";
      var0[102] = "name";
      var0[103] = "type";
      var0[104] = "<$constructor$>";
      var0[105] = "getFieldValue";
      var0[106] = "DESC_KEY_DISPLAY_NAME";
      var0[107] = "name";
      var0[108] = "TYPE";
      var0[109] = "INFO";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[110];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxOperationInfoManager(), var0);
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
   private static Class $get$array$$class$javax$management$MBeanParameterInfo() {
      Class var10000 = array$$class$javax$management$MBeanParameterInfo;
      if (var10000 == null) {
         var10000 = array$$class$javax$management$MBeanParameterInfo = class$("[Ljavax.management.MBeanParameterInfo;");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Void() {
      Class var10000 = $class$java$lang$Void;
      if (var10000 == null) {
         var10000 = $class$java$lang$Void = class$("java.lang.Void");
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
   private static Class $get$$class$groovy$jmx$builder$JmxOperationInfoManager() {
      Class var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxOperationInfoManager = class$("groovy.jmx.builder.JmxOperationInfoManager");
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
   private static Class $get$$class$javax$management$modelmbean$ModelMBeanConstructorInfo() {
      Class var10000 = $class$javax$management$modelmbean$ModelMBeanConstructorInfo;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$ModelMBeanConstructorInfo = class$("javax.management.modelmbean.ModelMBeanConstructorInfo");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$modelmbean$ModelMBeanOperationInfo() {
      Class var10000 = $class$javax$management$modelmbean$ModelMBeanOperationInfo;
      if (var10000 == null) {
         var10000 = $class$javax$management$modelmbean$ModelMBeanOperationInfo = class$("javax.management.modelmbean.ModelMBeanOperationInfo");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$management$MBeanParameterInfo() {
      Class var10000 = $class$javax$management$MBeanParameterInfo;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanParameterInfo = class$("javax.management.MBeanParameterInfo");
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
   private static Class $get$$class$java$util$ArrayList() {
      Class var10000 = $class$java$util$ArrayList;
      if (var10000 == null) {
         var10000 = $class$java$util$ArrayList = class$("java.util.ArrayList");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaMethod() {
      Class var10000 = $class$groovy$lang$MetaMethod;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaMethod = class$("groovy.lang.MetaMethod");
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
