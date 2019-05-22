package groovy.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Writable;
import java.io.Writer;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ConfigObject extends LinkedHashMap implements Writable, GroovyObject {
   private static final Object KEYWORDS = $getCallSiteArray()[87].call($get$$class$org$codehaus$groovy$syntax$Types());
   private static final Object TAB_CHARACTER = "\t";
   private URL configFile;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205694L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205694 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$syntax$Types;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$util$ConfigObject;
   // $FF: synthetic field
   private static Class $class$java$io$Writer;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$io$BufferedWriter;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$util$Properties;
   // $FF: synthetic field
   private static Class $class$java$net$URL;

   public ConfigObject(URL file) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.configFile = (URL)ScriptBytecodeAdapter.castToType(file, $get$$class$java$net$URL());
   }

   public ConfigObject() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Writer writeTo(Writer outArg) {
      CallSite[] var2 = $getCallSiteArray();
      Object out = null;
      boolean var6 = false;

      try {
         var6 = true;
         out = var2[0].callConstructor($get$$class$java$io$BufferedWriter(), (Object)outArg);
         var2[1].callCurrent(this, (Object[])ArrayUtil.createArray("", this, out, $const$0, Boolean.FALSE));
         var6 = false;
      } finally {
         if (var6) {
            var2[3].call(out);
         }
      }

      var2[2].call(out);
      return (Writer)ScriptBytecodeAdapter.castToType(outArg, $get$$class$java$io$Writer());
   }

   public Object getProperty(String name) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(name, "configFile")) {
         return this.configFile;
      } else if (!DefaultTypeTransformation.booleanUnbox(var2[4].callCurrent(this, (Object)name))) {
         ConfigObject prop = var2[5].callConstructor($get$$class$groovy$util$ConfigObject(), (Object)this.configFile);
         var2[6].callCurrent(this, name, prop);
         return prop;
      } else {
         return var2[7].callCurrent(this, (Object)name);
      }
   }

   public Map flatten() {
      CallSite[] var1 = $getCallSiteArray();
      return (Map)ScriptBytecodeAdapter.castToType(var1[8].callCurrent(this, (Object)null), $get$$class$java$util$Map());
   }

   public Map flatten(Map target) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(target, (Object)null)) {
         target = var2[9].callConstructor($get$$class$groovy$util$ConfigObject());
      }

      var2[10].callCurrent(this, "", target, this);
      return (Map)ScriptBytecodeAdapter.castToType(target, $get$$class$java$util$Map());
   }

   public Map merge(ConfigObject other) {
      CallSite[] var2 = $getCallSiteArray();
      return (Map)ScriptBytecodeAdapter.castToType(var2[11].callCurrent(this, this, other), $get$$class$java$util$Map());
   }

   public Properties toProperties() {
      CallSite[] var1 = $getCallSiteArray();
      Object props = var1[12].callConstructor($get$$class$java$util$Properties());
      var1[13].callCurrent(this, (Object)props);
      props = var1[14].callCurrent(this, (Object)props);
      return (Properties)ScriptBytecodeAdapter.castToType(props, $get$$class$java$util$Properties());
   }

   public Properties toProperties(String prefix) {
      CallSite[] var2 = $getCallSiteArray();
      Object props = var2[15].callConstructor($get$$class$java$util$Properties());
      var2[16].callCurrent(this, new GStringImpl(new Object[]{prefix}, new String[]{"", "."}), props, this);
      props = var2[17].callCurrent(this, (Object)props);
      return (Properties)ScriptBytecodeAdapter.castToType(props, $get$$class$java$util$Properties());
   }

   private Object merge(Map config, Map other) {
      CallSite[] var3 = $getCallSiteArray();
      Object entry = null;
      Object var5 = var3[18].call(other);

      while(true) {
         while(((Iterator)var5).hasNext()) {
            entry = ((Iterator)var5).next();
            Object configEntry = var3[19].call(config, (Object)var3[20].callGetProperty(entry));
            CallSite var10000;
            Object var10002;
            Object var7;
            if (ScriptBytecodeAdapter.compareEqual(configEntry, (Object)null)) {
               var10000 = var3[21];
               var10002 = var3[22].callGetProperty(entry);
               var7 = var3[23].callGetProperty(entry);
               var10000.call(config, var10002, var7);
            } else if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(configEntry instanceof Map && ScriptBytecodeAdapter.compareGreaterThan(var3[24].call(configEntry), $const$0) ? Boolean.TRUE : Boolean.FALSE) && var3[25].callGetProperty(entry) instanceof Map ? Boolean.TRUE : Boolean.FALSE)) {
               var3[26].callCurrent(this, configEntry, var3[27].callGetProperty(entry));
            } else {
               var10000 = var3[28];
               var10002 = var3[29].callGetProperty(entry);
               var7 = var3[30].callGetProperty(entry);
               var10000.call(config, var10002, var7);
            }
         }

         return config;
      }
   }

   private Object writeConfig(String prefix, ConfigObject map, Object out, Integer tab, boolean apply) {
      CallSite[] var6 = $getCallSiteArray();
      Object space = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(apply)) ? var6[31].call(TAB_CHARACTER, (Object)tab) : "";
      Object key = null;
      Object var9 = var6[32].call(var6[33].call(map));

      while(true) {
         while(true) {
            while(true) {
               Object value;
               label81:
               do {
                  while(((Iterator)var9).hasNext()) {
                     key = ((Iterator)var9).next();
                     value = var6[34].call(map, (Object)key);
                     if (value instanceof ConfigObject) {
                        continue label81;
                     }

                     var6[59].callCurrent(this, (Object[])ArrayUtil.createArray(key, space, prefix, value, out));
                  }

                  return null;
               } while(DefaultTypeTransformation.booleanUnbox(var6[35].call(value)));

               Object dotsInKeys = var6[36].call(value, (Object)(new GeneratedClosure(this, this) {
                  // $FF: synthetic field
                  private static final Integer $const$0 = (Integer)-1;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$groovy$util$ConfigObject$_writeConfig_closure1;

                  public {
                     CallSite[] var3 = $getCallSiteArray();
                  }

                  public Object doCall(Object entry) {
                     Object entryx = new Reference(entry);
                     CallSite[] var3 = $getCallSiteArray();
                     return ScriptBytecodeAdapter.compareGreaterThan(var3[0].call(var3[1].callGetProperty(entryx.get()), (Object)"."), $const$0) ? Boolean.TRUE : Boolean.FALSE;
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$util$ConfigObject$_writeConfig_closure1()) {
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
                     var0[0] = "indexOf";
                     var0[1] = "key";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[2];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$util$ConfigObject$_writeConfig_closure1(), var0);
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
                  private static Class $get$$class$groovy$util$ConfigObject$_writeConfig_closure1() {
                     Class var10000 = $class$groovy$util$ConfigObject$_writeConfig_closure1;
                     if (var10000 == null) {
                        var10000 = $class$groovy$util$ConfigObject$_writeConfig_closure1 = class$("groovy.util.ConfigObject$_writeConfig_closure1");
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
               Object configSize = var6[37].call(value);
               Object firstKey = var6[38].call(var6[39].call(var6[40].call(value)));
               Object firstValue = var6[41].call(var6[42].call(var6[43].call(value)));
               Object firstSize = null;
               if (firstValue instanceof ConfigObject) {
                  firstSize = var6[44].call(firstValue);
               } else {
                  firstSize = $const$1;
               }

               if (DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(configSize, $const$1) && !DefaultTypeTransformation.booleanUnbox(dotsInKeys) ? Boolean.FALSE : Boolean.TRUE)) {
                  if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(firstSize, $const$1) && firstValue instanceof ConfigObject ? Boolean.TRUE : Boolean.FALSE)) {
                     key = DefaultTypeTransformation.booleanUnbox(var6[45].call(KEYWORDS, key)) ? var6[46].call(key) : key;
                     Object writePrefix = new GStringImpl(new Object[]{prefix, key, firstKey}, new String[]{"", "", ".", "."});
                     var6[47].callCurrent(this, (Object[])ArrayUtil.createArray(writePrefix, firstValue, out, tab, Boolean.TRUE));
                  } else if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(dotsInKeys) && firstValue instanceof ConfigObject ? Boolean.TRUE : Boolean.FALSE)) {
                     var6[48].callCurrent(this, (Object[])ArrayUtil.createArray(key, space, tab, value, out));
                  } else {
                     Object j = null;
                     Object var17 = var6[49].call(var6[50].call(value));

                     while(((Iterator)var17).hasNext()) {
                        j = ((Iterator)var17).next();
                        Object v2 = var6[51].call(value, j);
                        Object k2 = ScriptBytecodeAdapter.compareGreaterThan(var6[52].call(j, (Object)"."), $const$2) ? var6[53].call(j) : j;
                        if (v2 instanceof ConfigObject) {
                           key = DefaultTypeTransformation.booleanUnbox(var6[54].call(KEYWORDS, key)) ? var6[55].call(key) : key;
                           var6[56].callCurrent(this, (Object[])ArrayUtil.createArray(new GStringImpl(new Object[]{prefix, key}, new String[]{"", "", ""}), v2, out, tab, Boolean.FALSE));
                        } else {
                           var6[57].callCurrent(this, (Object[])ArrayUtil.createArray(new GStringImpl(new Object[]{key, k2}, new String[]{"", ".", ""}), space, prefix, v2, out));
                        }
                     }
                  }
               } else {
                  var6[58].callCurrent(this, (Object[])ArrayUtil.createArray(key, space, tab, value, out));
               }
            }
         }
      }
   }

   private Object writeValue(Object key, Object space, Object prefix, Object value, Object out) {
      CallSite[] var6 = $getCallSiteArray();
      key = ScriptBytecodeAdapter.compareGreaterThan(var6[60].call(key, (Object)"."), $const$2) ? var6[61].call(key) : key;
      Boolean isKeyword = (Boolean)ScriptBytecodeAdapter.castToType(var6[62].call(KEYWORDS, key), $get$$class$java$lang$Boolean());
      key = DefaultTypeTransformation.booleanUnbox(isKeyword) ? var6[63].call(key) : key;
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(prefix) && DefaultTypeTransformation.booleanUnbox(isKeyword) ? Boolean.TRUE : Boolean.FALSE)) {
         prefix = "this.";
      }

      var6[64].call(out, (Object)(new GStringImpl(new Object[]{space, prefix, key, var6[65].call(value)}, new String[]{"", "", "", "=", ""})));
      return var6[66].call(out);
   }

   private Object writeNode(Object key, Object space, Object tab, Object value, Object out) {
      CallSite[] var6 = $getCallSiteArray();
      key = DefaultTypeTransformation.booleanUnbox(var6[67].call(KEYWORDS, key)) ? var6[68].call(key) : key;
      var6[69].call(out, (Object)(new GStringImpl(new Object[]{space, key}, new String[]{"", "", " {"})));
      var6[70].call(out);
      var6[71].callCurrent(this, (Object[])ArrayUtil.createArray("", value, out, var6[72].call(tab, (Object)$const$1), Boolean.TRUE));
      Object last = new GStringImpl(new Object[]{space}, new String[]{"", "}"});
      var6[73].call(out, (Object)last);
      return var6[74].call(out);
   }

   private Object convertValuesToString(Object props) {
      CallSite[] var2 = $getCallSiteArray();
      Object newProps = ScriptBytecodeAdapter.createMap(new Object[0]);
      Object e = null;
      Object var5 = var2[75].call(props);

      while(((Iterator)var5).hasNext()) {
         e = ((Iterator)var5).next();
         CallSite var10000 = var2[76];
         Object var10002 = var2[77].callGetProperty(e);
         Object var6 = var2[78].callSafe(var2[79].callGetProperty(e));
         var10000.call(newProps, var10002, var6);
      }

      return newProps;
   }

   private Object populate(Object suffix, Object config, Object map) {
      CallSite[] var4 = $getCallSiteArray();
      Object key = null;
      Object var6 = var4[80].call(var4[81].call(map));

      while(((Iterator)var6).hasNext()) {
         key = ((Iterator)var6).next();
         Object value = var4[82].call(map, key);
         if (value instanceof Map) {
            var4[83].callCurrent(this, var4[84].call(suffix, (Object)(new GStringImpl(new Object[]{key}, new String[]{"", "."}))), config, value);
         } else {
            try {
               var4[85].call(config, var4[86].call(suffix, key), value);
            } catch (NullPointerException var11) {
            } finally {
               ;
            }
         }
      }

      return null;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$ConfigObject()) {
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
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$util$ConfigObject();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$ConfigObject(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$ConfigObject(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public static final Object getKEYWORDS() {
      return KEYWORDS;
   }

   public static final Object getTAB_CHARACTER() {
      return TAB_CHARACTER;
   }

   public URL getConfigFile() {
      return this.configFile;
   }

   public void setConfigFile(URL var1) {
      this.configFile = var1;
   }

   // $FF: synthetic method
   public Object this$5$merge(Map var1, Map var2) {
      return this.merge(var1, var2);
   }

   // $FF: synthetic method
   public Object this$5$writeConfig(String var1, ConfigObject var2, Object var3, Integer var4, boolean var5) {
      return this.writeConfig(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Object this$5$writeValue(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return this.writeValue(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Object this$5$writeNode(Object var1, Object var2, Object var3, Object var4, Object var5) {
      return this.writeNode(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Object this$5$convertValuesToString(Object var1) {
      return this.convertValuesToString(var1);
   }

   // $FF: synthetic method
   public Object this$5$populate(Object var1, Object var2, Object var3) {
      return this.populate(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$putAll(Map var1) {
      super.putAll(var1);
   }

   // $FF: synthetic method
   public void super$4$clear() {
      super.clear();
   }

   // $FF: synthetic method
   public Object super$4$get(Object var1) {
      return super.get(var1);
   }

   // $FF: synthetic method
   public String super$2$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public Set super$3$keySet() {
      return super.keySet();
   }

   // $FF: synthetic method
   public int super$3$size() {
      return super.size();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public boolean super$3$isEmpty() {
      return super.isEmpty();
   }

   // $FF: synthetic method
   public int super$2$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public boolean super$4$removeEldestEntry(Entry var1) {
      return super.removeEldestEntry(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Collection super$3$values() {
      return super.values();
   }

   // $FF: synthetic method
   public boolean super$3$containsKey(Object var1) {
      return super.containsKey(var1);
   }

   // $FF: synthetic method
   public Object super$3$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Set super$3$entrySet() {
      return super.entrySet();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Object super$3$remove(Object var1) {
      return super.remove(var1);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public Object super$3$put(Object var1, Object var2) {
      return super.put(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$2$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public boolean super$4$containsValue(Object var1) {
      return super.containsValue(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "writeConfig";
      var0[2] = "flush";
      var0[3] = "flush";
      var0[4] = "containsKey";
      var0[5] = "<$constructor$>";
      var0[6] = "put";
      var0[7] = "get";
      var0[8] = "flatten";
      var0[9] = "<$constructor$>";
      var0[10] = "populate";
      var0[11] = "merge";
      var0[12] = "<$constructor$>";
      var0[13] = "flatten";
      var0[14] = "convertValuesToString";
      var0[15] = "<$constructor$>";
      var0[16] = "populate";
      var0[17] = "convertValuesToString";
      var0[18] = "iterator";
      var0[19] = "getAt";
      var0[20] = "key";
      var0[21] = "putAt";
      var0[22] = "key";
      var0[23] = "value";
      var0[24] = "size";
      var0[25] = "value";
      var0[26] = "merge";
      var0[27] = "value";
      var0[28] = "putAt";
      var0[29] = "key";
      var0[30] = "value";
      var0[31] = "multiply";
      var0[32] = "iterator";
      var0[33] = "keySet";
      var0[34] = "get";
      var0[35] = "isEmpty";
      var0[36] = "find";
      var0[37] = "size";
      var0[38] = "next";
      var0[39] = "iterator";
      var0[40] = "keySet";
      var0[41] = "next";
      var0[42] = "iterator";
      var0[43] = "values";
      var0[44] = "size";
      var0[45] = "contains";
      var0[46] = "inspect";
      var0[47] = "writeConfig";
      var0[48] = "writeNode";
      var0[49] = "iterator";
      var0[50] = "keySet";
      var0[51] = "get";
      var0[52] = "indexOf";
      var0[53] = "inspect";
      var0[54] = "contains";
      var0[55] = "inspect";
      var0[56] = "writeConfig";
      var0[57] = "writeValue";
      var0[58] = "writeNode";
      var0[59] = "writeValue";
      var0[60] = "indexOf";
      var0[61] = "inspect";
      var0[62] = "contains";
      var0[63] = "inspect";
      var0[64] = "leftShift";
      var0[65] = "inspect";
      var0[66] = "newLine";
      var0[67] = "contains";
      var0[68] = "inspect";
      var0[69] = "leftShift";
      var0[70] = "newLine";
      var0[71] = "writeConfig";
      var0[72] = "plus";
      var0[73] = "leftShift";
      var0[74] = "newLine";
      var0[75] = "iterator";
      var0[76] = "putAt";
      var0[77] = "key";
      var0[78] = "toString";
      var0[79] = "value";
      var0[80] = "iterator";
      var0[81] = "keySet";
      var0[82] = "get";
      var0[83] = "populate";
      var0[84] = "plus";
      var0[85] = "putAt";
      var0[86] = "plus";
      var0[87] = "getKeywords";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[88];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$ConfigObject(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$syntax$Types() {
      Class var10000 = $class$org$codehaus$groovy$syntax$Types;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$syntax$Types = class$("org.codehaus.groovy.syntax.Types");
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
   private static Class $get$$class$groovy$util$ConfigObject() {
      Class var10000 = $class$groovy$util$ConfigObject;
      if (var10000 == null) {
         var10000 = $class$groovy$util$ConfigObject = class$("groovy.util.ConfigObject");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$io$Writer() {
      Class var10000 = $class$java$io$Writer;
      if (var10000 == null) {
         var10000 = $class$java$io$Writer = class$("java.io.Writer");
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
   private static Class $get$$class$java$io$BufferedWriter() {
      Class var10000 = $class$java$io$BufferedWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$BufferedWriter = class$("java.io.BufferedWriter");
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
   private static Class $get$$class$java$util$Properties() {
      Class var10000 = $class$java$util$Properties;
      if (var10000 == null) {
         var10000 = $class$java$util$Properties = class$("java.util.Properties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$net$URL() {
      Class var10000 = $class$java$net$URL;
      if (var10000 == null) {
         var10000 = $class$java$net$URL = class$("java.net.URL");
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
