package groovy.jmx.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Date;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.NotificationFilter;
import javax.management.ObjectName;
import javax.management.timer.Timer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JmxTimerFactory extends AbstractFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Long $const$1 = (Long)1000L;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)-2;
   // $FF: synthetic field
   private static final Integer $const$4 = (Integer)1000;
   // $FF: synthetic field
   private static final Integer $const$5 = (Integer)60;
   // $FF: synthetic field
   private static final Integer $const$6 = (Integer)24;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204371L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204371 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$management$ObjectName;
   // $FF: synthetic field
   private static Class $class$groovy$util$GroovyMBean;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilderException;
   // $FF: synthetic field
   private static Class $class$javax$management$NotificationFilterSupport;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$javax$management$MBeanServer;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$javax$management$timer$Timer;
   // $FF: synthetic field
   private static Class $class$groovy$jmx$builder$JmxTimerFactory;
   // $FF: synthetic field
   private static Class $class$java$util$Date;
   // $FF: synthetic field
   private static Class $class$javax$management$NotificationFilter;

   public JmxTimerFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object nodeName, Object nodeParam, Map nodeAttribs) {
      CallSite[] var5 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(nodeParam)) {
         throw (Throwable)var5[0].callConstructor($get$$class$groovy$jmx$builder$JmxBuilderException(), (Object)(new GStringImpl(new Object[]{nodeName}, new String[]{"Node '", "' only supports named attributes."})));
      } else {
         JmxBuilder fsb = (JmxBuilder)ScriptBytecodeAdapter.castToType(builder, $get$$class$groovy$jmx$builder$JmxBuilder());
         Timer timer = var5[1].callConstructor($get$$class$javax$management$timer$Timer());
         Object metaMap = ScriptBytecodeAdapter.createMap(new Object[0]);
         ScriptBytecodeAdapter.setProperty(var5[2].call(fsb), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "server");
         ScriptBytecodeAdapter.setProperty(timer, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "timer");
         ScriptBytecodeAdapter.setProperty(var5[3].call(nodeAttribs, (Object)"name"), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "name");
         Object var10000 = var5[4].call(nodeAttribs, (Object)"event");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[5].call(nodeAttribs, (Object)"type");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = "jmx.builder.event";
            }
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "event");
         var10000 = var5[6].call(nodeAttribs, (Object)"message");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[7].call(nodeAttribs, (Object)"msg");
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "message");
         var10000 = var5[8].call(nodeAttribs, (Object)"data");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[9].call(nodeAttribs, (Object)"userData");
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "data");
         var10000 = var5[10].call(nodeAttribs, (Object)"date");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[11].call(nodeAttribs, (Object)"startDate");
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "date");
         var10000 = var5[12].call(nodeAttribs, (Object)"period");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[13].call(nodeAttribs, (Object)"frequency");
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "period");
         var10000 = var5[14].call(nodeAttribs, (Object)"occurs");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var5[15].call(nodeAttribs, (Object)"occurences");
            if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
               var10000 = $const$0;
            }
         }

         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "occurences");
         ScriptBytecodeAdapter.setProperty(var5[16].callCurrent(this, fsb, timer, var5[17].callGetProperty(metaMap)), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "name");
         ScriptBytecodeAdapter.setProperty(var5[18].callCurrent(this, (Object)var5[19].callGetProperty(metaMap)), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "date");
         ScriptBytecodeAdapter.setProperty(var5[20].callCurrent(this, (Object)var5[21].callGetProperty(metaMap)), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "period");
         ScriptBytecodeAdapter.setProperty(var5[22].callCurrent(this, (Object)var5[23].callGetProperty(metaMap)), $get$$class$groovy$jmx$builder$JmxTimerFactory(), metaMap, "listeners");
         Object result = var5[24].callCurrent(this, (Object)metaMap);
         return (Object)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Object());
      }
   }

   private Object getNormalizedName(Object fsb, Object timer, Object name) {
      CallSite[] var4 = $getCallSiteArray();
      Object result = null;
      if (!DefaultTypeTransformation.booleanUnbox(name)) {
         result = var4[25].callCurrent(this, fsb, timer);
      } else if (name instanceof String) {
         result = var4[26].callConstructor($get$$class$javax$management$ObjectName(), (Object)name);
      } else if (name instanceof ObjectName) {
         result = name;
      } else {
         result = var4[27].callCurrent(this, fsb, var4[28].callGroovyObjectGetProperty(this));
      }

      return result;
   }

   private Object getDefaultName(Object fsb, Object timer) {
      CallSite[] var3 = $getCallSiteArray();
      Object name = new GStringImpl(new Object[]{var3[29].call(fsb), var3[30].call(timer)}, new String[]{"", ":type=TimerService,name=Timer@", ""});
      return var3[31].callConstructor($get$$class$javax$management$ObjectName(), (Object)name);
   }

   private Object getNormalizedDate(Object date) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(date)) {
         return var2[32].callConstructor($get$$class$java$util$Date());
      } else if (date instanceof Date) {
         return date;
      } else {
         Object startDate = null;
         if (!ScriptBytecodeAdapter.isCase(date, (Object)null) && ScriptBytecodeAdapter.isCase(date, "now")) {
         }

         startDate = var2[33].callConstructor($get$$class$java$util$Date());
         return startDate;
      }
   }

   private Object getNormalizedPeriod(Object period) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(period)) {
         return $const$1;
      } else if (period instanceof Number) {
         return period;
      } else {
         Object result = $const$1;
         if (period instanceof String) {
            Object multiplier = var2[34].call(period, (Object)$const$2);
            Object value = null;

            try {
               value = var2[35].call(var2[36].call(period, (Object)ScriptBytecodeAdapter.createRange($const$0, $const$3, true)));
            } catch (Exception var10) {
               multiplier = "x";
            } finally {
               ;
            }

            if (ScriptBytecodeAdapter.isCase(multiplier, "s")) {
               result = var2[37].call(value, (Object)$const$4);
            } else if (ScriptBytecodeAdapter.isCase(multiplier, "m")) {
               result = var2[38].call(var2[39].call(value, (Object)$const$5), (Object)$const$4);
            } else if (ScriptBytecodeAdapter.isCase(multiplier, "h")) {
               result = var2[40].call(var2[41].call(var2[42].call(value, (Object)$const$5), (Object)$const$5), (Object)$const$4);
            } else if (ScriptBytecodeAdapter.isCase(multiplier, "d")) {
               result = var2[43].call(var2[44].call(var2[45].call(var2[46].call(value, (Object)$const$6), (Object)$const$5), (Object)$const$5), (Object)$const$4);
            } else {
               result = $const$1;
            }
         }

         return result;
      }
   }

   private Object getNormalizedRecipientList(Object list) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(list)) {
         return null;
      } else {
         Object result = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
         var2[47].call(list, (Object)(new GeneratedClosure(this, this, result) {
            private Reference<T> result;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1;
            // $FF: synthetic field
            private static Class $class$javax$management$ObjectName;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.result = (Reference)result;
            }

            public Object doCall(Object name) {
               Object namex = new Reference(name);
               CallSite[] var3 = $getCallSiteArray();
               Object on = new Reference((Object)null);
               if (namex.get() instanceof String) {
                  on.set(var3[0].callConstructor($get$$class$javax$management$ObjectName(), (Object)namex.get()));
               }

               if (namex.get() instanceof ObjectName) {
                  on.set(namex.get());
               }

               return var3[1].call(this.result.get(), on.get());
            }

            public Object getResult() {
               CallSite[] var1 = $getCallSiteArray();
               return this.result.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1()) {
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
               var0[0] = "<$constructor$>";
               var0[1] = "add";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1(), var0);
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
            private static Class $get$$class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1() {
               Class var10000 = $class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$jmx$builder$JmxTimerFactory$_getNormalizedRecipientList_closure1 = class$("groovy.jmx.builder.JmxTimerFactory$_getNormalizedRecipientList_closure1");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
         return result.get();
      }
   }

   private Object registerTimer(Object metaMap) {
      CallSite[] var2 = $getCallSiteArray();
      Object server = (MBeanServer)ScriptBytecodeAdapter.castToType(var2[48].callGetProperty(metaMap), $get$$class$javax$management$MBeanServer());
      Object timer = var2[49].callGetProperty(metaMap);
      var2[50].call(timer, ArrayUtil.createArray(var2[51].callGetProperty(metaMap), var2[52].callGetProperty(metaMap), var2[53].callGetProperty(metaMap), var2[54].callGetProperty(metaMap), var2[55].callGetProperty(metaMap), var2[56].callGetProperty(metaMap)));
      if (DefaultTypeTransformation.booleanUnbox(var2[57].call(server, (Object)var2[58].callGetProperty(metaMap)))) {
         var2[59].call(server, (Object)var2[60].callGetProperty(metaMap));
      }

      var2[61].call(server, timer, var2[62].callGetProperty(metaMap));
      return var2[63].callConstructor($get$$class$groovy$util$GroovyMBean(), var2[64].callGetProperty(metaMap), var2[65].callGetProperty(metaMap));
   }

   private NotificationFilter getEventFilter(Object type) {
      CallSite[] var2 = $getCallSiteArray();
      Object noteFilter = var2[66].callConstructor($get$$class$javax$management$NotificationFilterSupport());
      var2[67].call(noteFilter, type);
      return (NotificationFilter)ScriptBytecodeAdapter.castToType(noteFilter, $get$$class$javax$management$NotificationFilter());
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
         var4[68].call(parentNode, thisNode);
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$jmx$builder$JmxTimerFactory()) {
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
      Class var10000 = $get$$class$groovy$jmx$builder$JmxTimerFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$jmx$builder$JmxTimerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$jmx$builder$JmxTimerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object this$3$getNormalizedName(Object var1, Object var2, Object var3) {
      return this.getNormalizedName(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object this$3$getDefaultName(Object var1, Object var2) {
      return this.getDefaultName(var1, var2);
   }

   // $FF: synthetic method
   public Object this$3$getNormalizedDate(Object var1) {
      return this.getNormalizedDate(var1);
   }

   // $FF: synthetic method
   public Object this$3$getNormalizedPeriod(Object var1) {
      return this.getNormalizedPeriod(var1);
   }

   // $FF: synthetic method
   public Object this$3$getNormalizedRecipientList(Object var1) {
      return this.getNormalizedRecipientList(var1);
   }

   // $FF: synthetic method
   public Object this$3$registerTimer(Object var1) {
      return this.registerTimer(var1);
   }

   // $FF: synthetic method
   public NotificationFilter this$3$getEventFilter(Object var1) {
      return this.getEventFilter(var1);
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
      var0[1] = "<$constructor$>";
      var0[2] = "getMBeanServer";
      var0[3] = "remove";
      var0[4] = "remove";
      var0[5] = "remove";
      var0[6] = "remove";
      var0[7] = "remove";
      var0[8] = "remove";
      var0[9] = "remove";
      var0[10] = "remove";
      var0[11] = "remove";
      var0[12] = "remove";
      var0[13] = "remove";
      var0[14] = "remove";
      var0[15] = "remove";
      var0[16] = "getNormalizedName";
      var0[17] = "name";
      var0[18] = "getNormalizedDate";
      var0[19] = "date";
      var0[20] = "getNormalizedPeriod";
      var0[21] = "period";
      var0[22] = "getNormalizedRecipientList";
      var0[23] = "listeners";
      var0[24] = "registerTimer";
      var0[25] = "getDefaultName";
      var0[26] = "<$constructor$>";
      var0[27] = "getDefaultName";
      var0[28] = "time";
      var0[29] = "getDefaultJmxNameDomain";
      var0[30] = "hashCode";
      var0[31] = "<$constructor$>";
      var0[32] = "<$constructor$>";
      var0[33] = "<$constructor$>";
      var0[34] = "getAt";
      var0[35] = "toLong";
      var0[36] = "getAt";
      var0[37] = "multiply";
      var0[38] = "multiply";
      var0[39] = "multiply";
      var0[40] = "multiply";
      var0[41] = "multiply";
      var0[42] = "multiply";
      var0[43] = "multiply";
      var0[44] = "multiply";
      var0[45] = "multiply";
      var0[46] = "multiply";
      var0[47] = "each";
      var0[48] = "server";
      var0[49] = "timer";
      var0[50] = "addNotification";
      var0[51] = "event";
      var0[52] = "message";
      var0[53] = "data";
      var0[54] = "date";
      var0[55] = "period";
      var0[56] = "occurences";
      var0[57] = "isRegistered";
      var0[58] = "name";
      var0[59] = "unregisterMBean";
      var0[60] = "name";
      var0[61] = "registerMBean";
      var0[62] = "name";
      var0[63] = "<$constructor$>";
      var0[64] = "server";
      var0[65] = "name";
      var0[66] = "<$constructor$>";
      var0[67] = "enableType";
      var0[68] = "add";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[69];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$jmx$builder$JmxTimerFactory(), var0);
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
   private static Class $get$$class$javax$management$NotificationFilterSupport() {
      Class var10000 = $class$javax$management$NotificationFilterSupport;
      if (var10000 == null) {
         var10000 = $class$javax$management$NotificationFilterSupport = class$("javax.management.NotificationFilterSupport");
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
   private static Class $get$$class$javax$management$MBeanServer() {
      Class var10000 = $class$javax$management$MBeanServer;
      if (var10000 == null) {
         var10000 = $class$javax$management$MBeanServer = class$("javax.management.MBeanServer");
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
   private static Class $get$$class$javax$management$timer$Timer() {
      Class var10000 = $class$javax$management$timer$Timer;
      if (var10000 == null) {
         var10000 = $class$javax$management$timer$Timer = class$("javax.management.timer.Timer");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$jmx$builder$JmxTimerFactory() {
      Class var10000 = $class$groovy$jmx$builder$JmxTimerFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$jmx$builder$JmxTimerFactory = class$("groovy.jmx.builder.JmxTimerFactory");
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
   private static Class $get$$class$javax$management$NotificationFilter() {
      Class var10000 = $class$javax$management$NotificationFilter;
      if (var10000 == null) {
         var10000 = $class$javax$management$NotificationFilter = class$("javax.management.NotificationFilter");
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
