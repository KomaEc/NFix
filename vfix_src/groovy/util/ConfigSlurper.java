package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ConfigSlurper implements GroovyObject {
   private static final Object ENV_METHOD = "environments";
   private static final Object ENV_SETTINGS = "__env_settings__";
   private GroovyClassLoader classLoader;
   private String environment;
   private Object envMode;
   private Map bindingVars;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203603L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203603 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovySystem;
   // $FF: synthetic field
   private static Class $class$groovy$util$ConfigObject;
   // $FF: synthetic field
   private static Class $class$groovy$util$ConfigBinding;
   // $FF: synthetic field
   private static Class $class$groovy$util$ConfigSlurper;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$util$LinkedList;

   public ConfigSlurper() {
      CallSite[] var1 = $getCallSiteArray();
      this.classLoader = (GroovyClassLoader)ScriptBytecodeAdapter.castToType(var1[0].callConstructor($get$$class$groovy$lang$GroovyClassLoader()), $get$$class$groovy$lang$GroovyClassLoader());
      this.envMode = Boolean.FALSE;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public ConfigSlurper(String env) {
      CallSite[] var2 = $getCallSiteArray();
      this.classLoader = (GroovyClassLoader)ScriptBytecodeAdapter.castToType(var2[1].callConstructor($get$$class$groovy$lang$GroovyClassLoader()), $get$$class$groovy$lang$GroovyClassLoader());
      this.envMode = Boolean.FALSE;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.environment = (String)ScriptBytecodeAdapter.castToType(env, $get$$class$java$lang$String());
   }

   public void setBinding(Map vars) {
      CallSite[] var2 = $getCallSiteArray();
      this.bindingVars = (Map)ScriptBytecodeAdapter.castToType(vars, $get$$class$java$util$Map());
   }

   public ConfigObject parse(Properties properties) {
      CallSite[] var2 = $getCallSiteArray();
      ConfigObject config = var2[2].callConstructor($get$$class$groovy$util$ConfigObject());
      Object key = null;
      Object var5 = var2[3].call(var2[4].call(properties));

      while(((Iterator)var5).hasNext()) {
         key = ((Iterator)var5).next();
         Object tokens = var2[5].call(key, (Object)"\\.");
         Object current = config;
         Object currentToken = null;
         Object last = new Reference((Object)null);
         Object lastToken = null;
         Object foundBase = Boolean.FALSE;
         Object flattened = null;
         Object var13 = var2[6].call(tokens);

         while(((Iterator)var13).hasNext()) {
            flattened = ((Iterator)var13).next();
            if (DefaultTypeTransformation.booleanUnbox(foundBase)) {
               lastToken = var2[7].call(lastToken, var2[8].call(".", (Object)flattened));
               current = last.get();
            } else {
               last.set(current);
               lastToken = flattened;
               current = ScriptBytecodeAdapter.getProperty($get$$class$groovy$util$ConfigSlurper(), current, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{flattened}, new String[]{"", ""}), $get$$class$java$lang$String()));
               if (!(current instanceof ConfigObject)) {
                  foundBase = Boolean.TRUE;
               }
            }
         }

         if (current instanceof ConfigObject) {
            CallSite var10000;
            Object var10001;
            if (DefaultTypeTransformation.booleanUnbox(var2[9].call(last.get(), lastToken))) {
               flattened = var2[10].call(last.get());
               var2[11].call(last.get());
               var2[12].call(flattened, (Object)(new GeneratedClosure(this, this, last) {
                  private Reference<T> last;
                  // $FF: synthetic field
                  private static ClassInfo $staticClassInfo;
                  // $FF: synthetic field
                  private static SoftReference $callSiteArray;
                  // $FF: synthetic field
                  private static Class $class$groovy$util$ConfigSlurper$_parse_closure1;

                  public {
                     CallSite[] var4 = $getCallSiteArray();
                     this.last = (Reference)last;
                  }

                  public Object doCall(Object k2, Object v2) {
                     Object k2x = new Reference(k2);
                     Object v2x = new Reference(v2);
                     CallSite[] var5 = $getCallSiteArray();
                     CallSite var10000 = var5[0];
                     Object var10001 = this.last.get();
                     Object var10002 = k2x.get();
                     Object var6 = v2x.get();
                     var10000.call(var10001, var10002, var6);
                     return var6;
                  }

                  public Object call(Object k2, Object v2) {
                     Object k2x = new Reference(k2);
                     Object v2x = new Reference(v2);
                     CallSite[] var5 = $getCallSiteArray();
                     return var5[1].callCurrent(this, k2x.get(), v2x.get());
                  }

                  public Object getLast() {
                     CallSite[] var1 = $getCallSiteArray();
                     return this.last.get();
                  }

                  // $FF: synthetic method
                  protected MetaClass $getStaticMetaClass() {
                     if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure1()) {
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
                     var0[0] = "putAt";
                     var0[1] = "doCall";
                  }

                  // $FF: synthetic method
                  private static CallSiteArray $createCallSiteArray() {
                     String[] var0 = new String[2];
                     $createCallSiteArray_1(var0);
                     return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure1(), var0);
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
                  private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure1() {
                     Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure1;
                     if (var10000 == null) {
                        var10000 = $class$groovy$util$ConfigSlurper$_parse_closure1 = class$("groovy.util.ConfigSlurper$_parse_closure1");
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
               var10000 = var2[13];
               var10001 = last.get();
               var13 = var2[14].call(properties, (Object)key);
               var10000.call(var10001, lastToken, var13);
            } else {
               var10000 = var2[15];
               var10001 = last.get();
               flattened = var2[16].call(properties, (Object)key);
               var10000.call(var10001, lastToken, flattened);
            }
         }
      }

      return (ConfigObject)ScriptBytecodeAdapter.castToType(config, $get$$class$groovy$util$ConfigObject());
   }

   public ConfigObject parse(String script) {
      CallSite[] var2 = $getCallSiteArray();
      return (ConfigObject)ScriptBytecodeAdapter.castToType(var2[17].callCurrent(this, (Object)var2[18].call(this.classLoader, (Object)script)), $get$$class$groovy$util$ConfigObject());
   }

   public ConfigObject parse(Class scriptClass) {
      CallSite[] var2 = $getCallSiteArray();
      return (ConfigObject)ScriptBytecodeAdapter.castToType(var2[19].callCurrent(this, (Object)var2[20].call(scriptClass)), $get$$class$groovy$util$ConfigObject());
   }

   public ConfigObject parse(Script script) {
      CallSite[] var2 = $getCallSiteArray();
      return (ConfigObject)ScriptBytecodeAdapter.castToType(var2[21].callCurrent(this, script, (Object)null), $get$$class$groovy$util$ConfigObject());
   }

   public ConfigObject parse(URL scriptLocation) {
      CallSite[] var2 = $getCallSiteArray();
      return (ConfigObject)ScriptBytecodeAdapter.castToType(var2[22].callCurrent(this, var2[23].call(var2[24].call(this.classLoader, (Object)var2[25].callGetProperty(scriptLocation))), scriptLocation), $get$$class$groovy$util$ConfigObject());
   }

   public ConfigObject parse(Script script, URL location) {
      CallSite[] var3 = $getCallSiteArray();
      Object config = new Reference(DefaultTypeTransformation.booleanUnbox(location) ? var3[26].callConstructor($get$$class$groovy$util$ConfigObject(), (Object)location) : var3[27].callConstructor($get$$class$groovy$util$ConfigObject()));
      var3[28].call(var3[29].callGetProperty($get$$class$groovy$lang$GroovySystem()), var3[30].callGroovyObjectGetProperty(script));
      Object mc = new Reference(var3[31].callGetProperty(var3[32].callGroovyObjectGetProperty(script)));
      Object prefix = new Reference("");
      LinkedList stack = new Reference(var3[33].callConstructor($get$$class$java$util$LinkedList()));
      var3[34].call(stack.get(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"config", config.get(), "scope", ScriptBytecodeAdapter.createMap(new Object[0])}));
      Object pushStack = new Reference(new GeneratedClosure(this, this, stack) {
         private Reference<T> stack;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$util$LinkedList;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigSlurper$_parse_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.stack = (Reference)stack;
         }

         public Object doCall(Object co) {
            Object cox = new Reference(co);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(this.stack.get(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"config", cox.get(), "scope", var3[1].call(var3[2].callGetProperty(var3[3].callGetProperty(this.stack.get())))}));
         }

         public LinkedList getStack() {
            CallSite[] var1 = $getCallSiteArray();
            return (LinkedList)ScriptBytecodeAdapter.castToType(this.stack.get(), $get$$class$java$util$LinkedList());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure2()) {
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
            var0[1] = "clone";
            var0[2] = "scope";
            var0[3] = "last";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure2(), var0);
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
         private static Class $get$$class$java$util$LinkedList() {
            Class var10000 = $class$java$util$LinkedList;
            if (var10000 == null) {
               var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure2() {
            Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigSlurper$_parse_closure2 = class$("groovy.util.ConfigSlurper$_parse_closure2");
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
      });
      Object assignName = new Reference(new GeneratedClosure(this, this, stack) {
         private Reference<T> stack;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigSlurper$_parse_closure3;
         // $FF: synthetic field
         private static Class $class$java$util$LinkedList;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.stack = (Reference)stack;
         }

         public Object doCall(Object name, Object co) {
            Object namex = new Reference(name);
            Object cox = new Reference(co);
            CallSite[] var5 = $getCallSiteArray();
            Object current = var5[0].callGetProperty(this.stack.get());
            CallSite var10000 = var5[1];
            Object var10001 = var5[2].callGetProperty(current);
            Object var10002 = namex.get();
            Object var7 = cox.get();
            var10000.call(var10001, var10002, var7);
            var10000 = var5[3];
            var10001 = var5[4].callGetProperty(current);
            var10002 = namex.get();
            var7 = cox.get();
            var10000.call(var10001, var10002, var7);
            return var7;
         }

         public Object call(Object name, Object co) {
            Object namex = new Reference(name);
            Object cox = new Reference(co);
            CallSite[] var5 = $getCallSiteArray();
            return var5[5].callCurrent(this, namex.get(), cox.get());
         }

         public LinkedList getStack() {
            CallSite[] var1 = $getCallSiteArray();
            return (LinkedList)ScriptBytecodeAdapter.castToType(this.stack.get(), $get$$class$java$util$LinkedList());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure3()) {
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
            var0[0] = "last";
            var0[1] = "putAt";
            var0[2] = "config";
            var0[3] = "putAt";
            var0[4] = "scope";
            var0[5] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure3(), var0);
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
         private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure3() {
            Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigSlurper$_parse_closure3 = class$("groovy.util.ConfigSlurper$_parse_closure3");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$LinkedList() {
            Class var10000 = $class$java$util$LinkedList;
            if (var10000 == null) {
               var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
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
      });
      Object getPropertyClosure = new GeneratedClosure(this, this, assignName, stack) {
         private Reference<T> assignName;
         private Reference<T> stack;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigSlurper$_parse_closure4;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigObject;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
         // $FF: synthetic field
         private static Class $class$java$util$LinkedList;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.assignName = (Reference)assignName;
            this.stack = (Reference)stack;
         }

         public Object doCall(String name) {
            String namex = new Reference(name);
            CallSite[] var3 = $getCallSiteArray();
            Object current = new Reference(var3[0].callGetProperty(this.stack.get()));
            Object result = new Reference((Object)null);
            if (DefaultTypeTransformation.booleanUnbox(var3[1].call(var3[2].callGetProperty(current.get()), namex.get()))) {
               result.set(var3[3].call(var3[4].callGetProperty(current.get()), namex.get()));
            } else if (DefaultTypeTransformation.booleanUnbox(var3[5].call(var3[6].callGetProperty(current.get()), namex.get()))) {
               result.set(var3[7].call(var3[8].callGetProperty(current.get()), namex.get()));
            } else {
               try {
                  result.set(var3[9].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), this.getThisObject(), namex.get()));
               } catch (GroovyRuntimeException var9) {
                  result.set(var3[10].callConstructor($get$$class$groovy$util$ConfigObject()));
                  var3[11].call(this.assignName.get(), namex.get(), result.get());
               } finally {
                  ;
               }
            }

            return result.get();
         }

         public Object call(String name) {
            String namex = new Reference(name);
            CallSite[] var3 = $getCallSiteArray();
            return var3[12].callCurrent(this, (Object)namex.get());
         }

         public Object getAssignName() {
            CallSite[] var1 = $getCallSiteArray();
            return this.assignName.get();
         }

         public LinkedList getStack() {
            CallSite[] var1 = $getCallSiteArray();
            return (LinkedList)ScriptBytecodeAdapter.castToType(this.stack.get(), $get$$class$java$util$LinkedList());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure4()) {
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
            var0[0] = "last";
            var0[1] = "get";
            var0[2] = "config";
            var0[3] = "get";
            var0[4] = "config";
            var0[5] = "getAt";
            var0[6] = "scope";
            var0[7] = "getAt";
            var0[8] = "scope";
            var0[9] = "getProperty";
            var0[10] = "<$constructor$>";
            var0[11] = "call";
            var0[12] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[13];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure4(), var0);
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
         private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure4() {
            Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigSlurper$_parse_closure4 = class$("groovy.util.ConfigSlurper$_parse_closure4");
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
         private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
            Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$LinkedList() {
            Class var10000 = $class$java$util$LinkedList;
            if (var10000 == null) {
               var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
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
      };
      ScriptBytecodeAdapter.setProperty(getPropertyClosure, $get$$class$groovy$util$ConfigSlurper(), mc.get(), "getProperty");
      ScriptBytecodeAdapter.setProperty(new GeneratedClosure(this, this, mc, prefix, assignName, config, stack, pushStack) {
         private Reference<T> mc;
         private Reference<T> prefix;
         private Reference<T> assignName;
         private Reference<T> config;
         private Reference<T> stack;
         private Reference<T> pushStack;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)1;
         // $FF: synthetic field
         private static final Integer $const$1 = (Integer)0;
         // $FF: synthetic field
         private static final Integer $const$2 = (Integer)2;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigObject;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigSlurper$_parse_closure5;
         // $FF: synthetic field
         private static Class $class$groovy$lang$MissingMethodException;
         // $FF: synthetic field
         private static Class $class$groovy$lang$MetaMethod;
         // $FF: synthetic field
         private static Class $class$java$util$LinkedList;

         public {
            CallSite[] var9 = $getCallSiteArray();
            this.mc = (Reference)mc;
            this.prefix = (Reference)prefix;
            this.assignName = (Reference)assignName;
            this.config = (Reference)config;
            this.stack = (Reference)stack;
            this.pushStack = (Reference)pushStack;
         }

         public Object doCall(String name, Object args) {
            String namex = new Reference(name);
            Object argsx = new Reference(args);
            CallSite[] var5 = $getCallSiteArray();
            Object result = new Reference((Object)null);
            Reference cox;
            if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var5[0].callGetProperty(argsx.get()), $const$0) && var5[1].call(argsx.get(), (Object)$const$1) instanceof Closure ? Boolean.TRUE : Boolean.FALSE)) {
               if (ScriptBytecodeAdapter.compareEqual(namex.get(), var5[2].callGroovyObjectGetProperty(this))) {
                  try {
                     ScriptBytecodeAdapter.setGroovyObjectProperty(Boolean.TRUE, $get$$class$groovy$util$ConfigSlurper$_parse_closure5(), this, "envMode");
                     var5[3].call(var5[4].call(argsx.get(), (Object)$const$1));
                  } finally {
                     ScriptBytecodeAdapter.setGroovyObjectProperty(Boolean.FALSE, $get$$class$groovy$util$ConfigSlurper$_parse_closure5(), this, "envMode");
                  }
               } else if (DefaultTypeTransformation.booleanUnbox(var5[5].callGroovyObjectGetProperty(this))) {
                  if (ScriptBytecodeAdapter.compareEqual(namex.get(), var5[6].callGroovyObjectGetProperty(this))) {
                     Object co = var5[7].callConstructor($get$$class$groovy$util$ConfigObject());
                     var5[8].call(this.config.get(), var5[9].callGroovyObjectGetProperty(this), co);
                     var5[10].call(this.pushStack.get(), co);

                     try {
                        ScriptBytecodeAdapter.setGroovyObjectProperty(Boolean.FALSE, $get$$class$groovy$util$ConfigSlurper$_parse_closure5(), this, "envMode");
                        var5[11].call(var5[12].call(argsx.get(), (Object)$const$1));
                     } finally {
                        ScriptBytecodeAdapter.setGroovyObjectProperty(Boolean.TRUE, $get$$class$groovy$util$ConfigSlurper$_parse_closure5(), this, "envMode");
                     }

                     var5[13].call(this.stack.get());
                  }
               } else {
                  cox = new Reference((Object)null);
                  if (var5[14].call(var5[15].callGetProperty(var5[16].callGetProperty(this.stack.get())), namex.get()) instanceof ConfigObject) {
                     cox.set(var5[17].call(var5[18].callGetProperty(var5[19].callGetProperty(this.stack.get())), namex.get()));
                  } else {
                     cox.set(var5[20].callConstructor($get$$class$groovy$util$ConfigObject()));
                  }

                  var5[21].call(this.assignName.get(), namex.get(), cox.get());
                  var5[22].call(this.pushStack.get(), cox.get());
                  var5[23].call(var5[24].call(argsx.get(), (Object)$const$1));
                  var5[25].call(this.stack.get());
               }
            } else if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var5[26].callGetProperty(argsx.get()), $const$2) && var5[27].call(argsx.get(), (Object)$const$0) instanceof Closure ? Boolean.TRUE : Boolean.FALSE)) {
               try {
                  this.prefix.set(var5[28].call(namex.get(), (Object)"."));
                  var5[29].call(this.assignName.get(), namex.get(), var5[30].call(argsx.get(), (Object)$const$1));
                  var5[31].call(var5[32].call(argsx.get(), (Object)$const$0));
               } finally {
                  this.prefix.set("");
               }
            } else {
               cox = new Reference((MetaMethod)ScriptBytecodeAdapter.castToType(var5[33].call(this.mc.get(), namex.get(), argsx.get()), $get$$class$groovy$lang$MetaMethod()));
               if (!DefaultTypeTransformation.booleanUnbox(cox.get())) {
                  throw (Throwable)var5[36].callConstructor($get$$class$groovy$lang$MissingMethodException(), namex.get(), var5[37].callCurrent(this), argsx.get());
               }

               result.set(var5[34].call(cox.get(), var5[35].callGroovyObjectGetProperty(this), argsx.get()));
            }

            return result.get();
         }

         public Object call(String name, Object args) {
            String namex = new Reference(name);
            Object argsx = new Reference(args);
            CallSite[] var5 = $getCallSiteArray();
            return var5[38].callCurrent(this, namex.get(), argsx.get());
         }

         public Object getMc() {
            CallSite[] var1 = $getCallSiteArray();
            return this.mc.get();
         }

         public Object getPrefix() {
            CallSite[] var1 = $getCallSiteArray();
            return this.prefix.get();
         }

         public Object getAssignName() {
            CallSite[] var1 = $getCallSiteArray();
            return this.assignName.get();
         }

         public Object getConfig() {
            CallSite[] var1 = $getCallSiteArray();
            return this.config.get();
         }

         public LinkedList getStack() {
            CallSite[] var1 = $getCallSiteArray();
            return (LinkedList)ScriptBytecodeAdapter.castToType(this.stack.get(), $get$$class$java$util$LinkedList());
         }

         public Object getPushStack() {
            CallSite[] var1 = $getCallSiteArray();
            return this.pushStack.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure5()) {
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
            var0[0] = "length";
            var0[1] = "getAt";
            var0[2] = "ENV_METHOD";
            var0[3] = "call";
            var0[4] = "getAt";
            var0[5] = "envMode";
            var0[6] = "environment";
            var0[7] = "<$constructor$>";
            var0[8] = "putAt";
            var0[9] = "ENV_SETTINGS";
            var0[10] = "call";
            var0[11] = "call";
            var0[12] = "getAt";
            var0[13] = "pop";
            var0[14] = "get";
            var0[15] = "config";
            var0[16] = "last";
            var0[17] = "get";
            var0[18] = "config";
            var0[19] = "last";
            var0[20] = "<$constructor$>";
            var0[21] = "call";
            var0[22] = "call";
            var0[23] = "call";
            var0[24] = "getAt";
            var0[25] = "pop";
            var0[26] = "length";
            var0[27] = "getAt";
            var0[28] = "plus";
            var0[29] = "call";
            var0[30] = "getAt";
            var0[31] = "call";
            var0[32] = "getAt";
            var0[33] = "getMetaMethod";
            var0[34] = "invoke";
            var0[35] = "delegate";
            var0[36] = "<$constructor$>";
            var0[37] = "getClass";
            var0[38] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[39];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure5(), var0);
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
         private static Class $get$$class$groovy$util$ConfigObject() {
            Class var10000 = $class$groovy$util$ConfigObject;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigObject = class$("groovy.util.ConfigObject");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure5() {
            Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigSlurper$_parse_closure5 = class$("groovy.util.ConfigSlurper$_parse_closure5");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$lang$MissingMethodException() {
            Class var10000 = $class$groovy$lang$MissingMethodException;
            if (var10000 == null) {
               var10000 = $class$groovy$lang$MissingMethodException = class$("groovy.lang.MissingMethodException");
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
         private static Class $get$$class$java$util$LinkedList() {
            Class var10000 = $class$java$util$LinkedList;
            if (var10000 == null) {
               var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
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
      }, $get$$class$groovy$util$ConfigSlurper(), mc.get(), "invokeMethod");
      ScriptBytecodeAdapter.setGroovyObjectProperty(mc.get(), $get$$class$groovy$util$ConfigSlurper(), script, "metaClass");
      Object setProperty = new GeneratedClosure(this, this, prefix, assignName) {
         private Reference<T> prefix;
         private Reference<T> assignName;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$ConfigSlurper$_parse_closure6;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.prefix = (Reference)prefix;
            this.assignName = (Reference)assignName;
         }

         public Object doCall(String name, Object value) {
            String namex = new Reference(name);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[0].call(this.assignName.get(), var5[1].call(this.prefix.get(), namex.get()), valuex.get());
         }

         public Object call(String name, Object value) {
            String namex = new Reference(name);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[2].callCurrent(this, namex.get(), valuex.get());
         }

         public Object getPrefix() {
            CallSite[] var1 = $getCallSiteArray();
            return this.prefix.get();
         }

         public Object getAssignName() {
            CallSite[] var1 = $getCallSiteArray();
            return this.assignName.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$ConfigSlurper$_parse_closure6()) {
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
            var0[0] = "call";
            var0[1] = "plus";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$ConfigSlurper$_parse_closure6(), var0);
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
         private static Class $get$$class$groovy$util$ConfigSlurper$_parse_closure6() {
            Class var10000 = $class$groovy$util$ConfigSlurper$_parse_closure6;
            if (var10000 == null) {
               var10000 = $class$groovy$util$ConfigSlurper$_parse_closure6 = class$("groovy.util.ConfigSlurper$_parse_closure6");
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
      };
      Object binding = var3[35].callConstructor($get$$class$groovy$util$ConfigBinding(), (Object)setProperty);
      if (DefaultTypeTransformation.booleanUnbox(this.bindingVars)) {
         var3[36].call(var3[37].call(binding), (Object)this.bindingVars);
      }

      ScriptBytecodeAdapter.setGroovyObjectProperty(binding, $get$$class$groovy$util$ConfigSlurper(), script, "binding");
      var3[38].call(script);
      Object envSettings = var3[39].call(config.get(), ENV_SETTINGS);
      if (DefaultTypeTransformation.booleanUnbox(envSettings)) {
         var3[40].call(config.get(), envSettings);
      }

      return (ConfigObject)ScriptBytecodeAdapter.castToType(config.get(), $get$$class$groovy$util$ConfigObject());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$ConfigSlurper()) {
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
      Class var10000 = $get$$class$groovy$util$ConfigSlurper();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$ConfigSlurper(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$ConfigSlurper(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public static final Object getENV_SETTINGS() {
      return ENV_SETTINGS;
   }

   public GroovyClassLoader getClassLoader() {
      return this.classLoader;
   }

   public void setClassLoader(GroovyClassLoader var1) {
      this.classLoader = var1;
   }

   public String getEnvironment() {
      return this.environment;
   }

   public void setEnvironment(String var1) {
      this.environment = var1;
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
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
      var0[3] = "iterator";
      var0[4] = "keySet";
      var0[5] = "split";
      var0[6] = "iterator";
      var0[7] = "plus";
      var0[8] = "plus";
      var0[9] = "getAt";
      var0[10] = "flatten";
      var0[11] = "clear";
      var0[12] = "each";
      var0[13] = "putAt";
      var0[14] = "get";
      var0[15] = "putAt";
      var0[16] = "get";
      var0[17] = "parse";
      var0[18] = "parseClass";
      var0[19] = "parse";
      var0[20] = "newInstance";
      var0[21] = "parse";
      var0[22] = "parse";
      var0[23] = "newInstance";
      var0[24] = "parseClass";
      var0[25] = "text";
      var0[26] = "<$constructor$>";
      var0[27] = "<$constructor$>";
      var0[28] = "removeMetaClass";
      var0[29] = "metaClassRegistry";
      var0[30] = "class";
      var0[31] = "metaClass";
      var0[32] = "class";
      var0[33] = "<$constructor$>";
      var0[34] = "leftShift";
      var0[35] = "<$constructor$>";
      var0[36] = "putAll";
      var0[37] = "getVariables";
      var0[38] = "run";
      var0[39] = "remove";
      var0[40] = "merge";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[41];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$ConfigSlurper(), var0);
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
   private static Class $get$$class$groovy$lang$GroovySystem() {
      Class var10000 = $class$groovy$lang$GroovySystem;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovySystem = class$("groovy.lang.GroovySystem");
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
   private static Class $get$$class$groovy$util$ConfigBinding() {
      Class var10000 = $class$groovy$util$ConfigBinding;
      if (var10000 == null) {
         var10000 = $class$groovy$util$ConfigBinding = class$("groovy.util.ConfigBinding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$ConfigSlurper() {
      Class var10000 = $class$groovy$util$ConfigSlurper;
      if (var10000 == null) {
         var10000 = $class$groovy$util$ConfigSlurper = class$("groovy.util.ConfigSlurper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
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
   private static Class $get$$class$java$util$LinkedList() {
      Class var10000 = $class$java$util$LinkedList;
      if (var10000 == null) {
         var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
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
