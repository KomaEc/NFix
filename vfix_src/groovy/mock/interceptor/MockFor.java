package groovy.mock.interceptor;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MockFor implements GroovyObject {
   private MockProxyMetaClass proxy;
   private Demand demand;
   private Ignore ignore;
   private Object expect;
   private Map instanceExpectations;
   private Class clazz;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202953L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202953 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$java$lang$reflect$Modifier;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$MockInterceptor;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$MockProxyMetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$MockFor;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$StrictExpectation;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$Ignore;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$util$regex$Pattern;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$Demand;
   // $FF: synthetic field
   private static Class $class$java$util$ArrayList;
   // $FF: synthetic field
   private static Class $class$groovy$util$ProxyGenerator;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyObject;

   public MockFor(Class clazz, boolean interceptConstruction) {
      CallSite[] var3 = $getCallSiteArray();
      this.instanceExpectations = (Map)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(interceptConstruction)) && !DefaultTypeTransformation.booleanUnbox(var3[0].call($get$$class$groovy$lang$GroovyObject(), (Object)clazz)) ? Boolean.TRUE : Boolean.FALSE)) {
         throw (Throwable)var3[1].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)var3[2].call("MockFor with constructor interception enabled is only allowed for Groovy objects but found: ", (Object)var3[3].callGetProperty(clazz)));
      } else {
         this.clazz = (Class)ScriptBytecodeAdapter.castToType(clazz, $get$$class$java$lang$Class());
         this.proxy = (MockProxyMetaClass)ScriptBytecodeAdapter.castToType((MockProxyMetaClass)ScriptBytecodeAdapter.castToType(var3[4].call($get$$class$groovy$mock$interceptor$MockProxyMetaClass(), clazz, DefaultTypeTransformation.box(interceptConstruction)), $get$$class$groovy$mock$interceptor$MockProxyMetaClass()), $get$$class$groovy$mock$interceptor$MockProxyMetaClass());
         this.demand = (Demand)ScriptBytecodeAdapter.castToType(var3[5].callConstructor($get$$class$groovy$mock$interceptor$Demand()), $get$$class$groovy$mock$interceptor$Demand());
         this.ignore = (Ignore)ScriptBytecodeAdapter.castToType(var3[6].callConstructor($get$$class$groovy$mock$interceptor$Ignore(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"parent", this})), $get$$class$groovy$mock$interceptor$Ignore());
         this.expect = var3[7].callConstructor($get$$class$groovy$mock$interceptor$StrictExpectation(), (Object)this.demand);
         ScriptBytecodeAdapter.setProperty(var3[8].callConstructor($get$$class$groovy$mock$interceptor$MockInterceptor(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"expectation", this.expect})), $get$$class$groovy$mock$interceptor$MockFor(), this.proxy, "interceptor");
      }
   }

   public MockFor(Class clazz) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createPojoWrapper(clazz, $get$$class$java$lang$Class()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.FALSE, Boolean.TYPE)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$mock$interceptor$MockFor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this((Class)var10001[0]);
         break;
      case 1:
         this((Class)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public void use(Closure closure) {
      CallSite[] var2 = $getCallSiteArray();
      var2[9].call(this.proxy, (Object)closure);
      var2[10].call(this.expect);
   }

   public void use(GroovyObject obj, Closure closure) {
      CallSite[] var3 = $getCallSiteArray();
      var3[11].call(this.proxy, obj, closure);
      var3[12].call(this.expect);
   }

   public void verify(GroovyObject obj) {
      CallSite[] var2 = $getCallSiteArray();
      var2[13].call(var2[14].call(this.instanceExpectations, (Object)obj));
   }

   public Object ignore(Object filter, Closure filterBehavior) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[15].callGetProperty(this.clazz), "java.lang.String") && filter instanceof String ? Boolean.TRUE : Boolean.FALSE)) {
         filter = var3[16].call($get$$class$java$util$regex$Pattern(), (Object)filter);
      }

      CallSite var10000 = var3[17];
      Object var10001 = var3[18].callGroovyObjectGetProperty(this.demand);
      Object var10003 = filterBehavior;
      if (!DefaultTypeTransformation.booleanUnbox(filterBehavior)) {
         var10003 = var3[19].callGetProperty($get$$class$groovy$mock$interceptor$MockProxyMetaClass());
      }

      return var10000.call(var10001, filter, var10003);
   }

   public GroovyObject proxyInstance(Object args) {
      CallSite[] var2 = $getCallSiteArray();
      return (GroovyObject)ScriptBytecodeAdapter.castToType(var2[20].callCurrent(this, args, Boolean.FALSE), $get$$class$groovy$lang$GroovyObject());
   }

   public GroovyObject proxyDelegateInstance(Object args) {
      CallSite[] var2 = $getCallSiteArray();
      return (GroovyObject)ScriptBytecodeAdapter.castToType(var2[21].callCurrent(this, args, Boolean.TRUE), $get$$class$groovy$lang$GroovyObject());
   }

   public GroovyObject makeProxyInstance(Object args, boolean isDelegate) {
      CallSite[] var3 = $getCallSiteArray();
      Object instance = var3[22].callStatic($get$$class$groovy$mock$interceptor$MockFor(), this.clazz, args);
      Object thisproxy = var3[23].call($get$$class$groovy$mock$interceptor$MockProxyMetaClass(), (Object)(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(isDelegate)) ? var3[24].call(instance) : this.clazz));
      Object thisdemand = var3[25].callConstructor($get$$class$groovy$mock$interceptor$Demand(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"recorded", var3[26].callConstructor($get$$class$java$util$ArrayList(), (Object)var3[27].callGroovyObjectGetProperty(this.demand)), "ignore", var3[28].callConstructor($get$$class$java$util$HashMap(), (Object)var3[29].callGroovyObjectGetProperty(this.demand))}));
      Object thisexpect = var3[30].callConstructor($get$$class$groovy$mock$interceptor$StrictExpectation(), (Object)thisdemand);
      ScriptBytecodeAdapter.setProperty(var3[31].callConstructor($get$$class$groovy$mock$interceptor$MockInterceptor(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"expectation", thisexpect})), $get$$class$groovy$mock$interceptor$MockFor(), thisproxy, "interceptor");
      ScriptBytecodeAdapter.setProperty(thisproxy, $get$$class$groovy$mock$interceptor$MockFor(), instance, "metaClass");
      Object wrapped = instance;
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(isDelegate)) && DefaultTypeTransformation.booleanUnbox(var3[32].call(this.clazz)) ? Boolean.TRUE : Boolean.FALSE)) {
         wrapped = var3[33].call(var3[34].callGetProperty($get$$class$groovy$util$ProxyGenerator()), ScriptBytecodeAdapter.createList(new Object[]{this.clazz}), instance);
      }

      var3[35].call(this.instanceExpectations, wrapped, thisexpect);
      return (GroovyObject)ScriptBytecodeAdapter.castToType(wrapped, $get$$class$groovy$lang$GroovyObject());
   }

   public static GroovyObject getInstance(Class clazz, Object args) {
      CallSite[] var2 = $getCallSiteArray();
      GroovyObject instance = (GroovyObject)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$GroovyObject());
      if (DefaultTypeTransformation.booleanUnbox(var2[36].call(clazz))) {
         instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[37].call(var2[38].callGetProperty($get$$class$groovy$util$ProxyGenerator()), (Object)clazz), $get$$class$groovy$lang$GroovyObject());
      } else if (DefaultTypeTransformation.booleanUnbox(var2[39].call($get$$class$java$lang$reflect$Modifier(), (Object)var2[40].callGetProperty(clazz)))) {
         instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[41].call(var2[42].callGetProperty($get$$class$groovy$util$ProxyGenerator()), clazz, args), $get$$class$groovy$lang$GroovyObject());
      } else if (ScriptBytecodeAdapter.compareNotEqual(args, (Object)null)) {
         if (DefaultTypeTransformation.booleanUnbox(var2[43].call($get$$class$groovy$lang$GroovyObject(), (Object)clazz))) {
            instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[44].call(clazz, (Object)args), $get$$class$groovy$lang$GroovyObject());
         } else {
            instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[45].call(var2[46].callGetProperty($get$$class$groovy$util$ProxyGenerator()), var2[47].call(clazz, (Object)args)), $get$$class$groovy$lang$GroovyObject());
         }
      } else if (DefaultTypeTransformation.booleanUnbox(var2[48].call($get$$class$groovy$lang$GroovyObject(), (Object)clazz))) {
         instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[49].call(clazz), $get$$class$groovy$lang$GroovyObject());
      } else {
         instance = (GroovyObject)ScriptBytecodeAdapter.castToType(var2[50].call(var2[51].callGetProperty($get$$class$groovy$util$ProxyGenerator()), var2[52].call(clazz)), $get$$class$groovy$lang$GroovyObject());
      }

      return (GroovyObject)ScriptBytecodeAdapter.castToType(instance, $get$$class$groovy$lang$GroovyObject());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$mock$interceptor$MockFor()) {
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
      Class var10000 = $get$$class$groovy$mock$interceptor$MockFor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$mock$interceptor$MockFor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$mock$interceptor$MockFor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object ignore(Object filter) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[53].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(filter, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()));
   }

   public GroovyObject proxyInstance() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyObject)ScriptBytecodeAdapter.castToType(var1[54].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object())), $get$$class$groovy$lang$GroovyObject());
   }

   public GroovyObject proxyDelegateInstance() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyObject)ScriptBytecodeAdapter.castToType(var1[55].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object())), $get$$class$groovy$lang$GroovyObject());
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

   public MockProxyMetaClass getProxy() {
      return this.proxy;
   }

   public void setProxy(MockProxyMetaClass var1) {
      this.proxy = var1;
   }

   public Demand getDemand() {
      return this.demand;
   }

   public void setDemand(Demand var1) {
      this.demand = var1;
   }

   public Ignore getIgnore() {
      return this.ignore;
   }

   public void setIgnore(Ignore var1) {
      this.ignore = var1;
   }

   public Object getExpect() {
      return this.expect;
   }

   public void setExpect(Object var1) {
      this.expect = var1;
   }

   public Map getInstanceExpectations() {
      return this.instanceExpectations;
   }

   public void setInstanceExpectations(Map var1) {
      this.instanceExpectations = var1;
   }

   public Class getClazz() {
      return this.clazz;
   }

   public void setClazz(Class var1) {
      this.clazz = var1;
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
      var0[0] = "isAssignableFrom";
      var0[1] = "<$constructor$>";
      var0[2] = "plus";
      var0[3] = "name";
      var0[4] = "make";
      var0[5] = "<$constructor$>";
      var0[6] = "<$constructor$>";
      var0[7] = "<$constructor$>";
      var0[8] = "<$constructor$>";
      var0[9] = "use";
      var0[10] = "verify";
      var0[11] = "use";
      var0[12] = "verify";
      var0[13] = "verify";
      var0[14] = "getAt";
      var0[15] = "name";
      var0[16] = "compile";
      var0[17] = "put";
      var0[18] = "ignore";
      var0[19] = "FALL_THROUGH_MARKER";
      var0[20] = "makeProxyInstance";
      var0[21] = "makeProxyInstance";
      var0[22] = "getInstance";
      var0[23] = "make";
      var0[24] = "getClass";
      var0[25] = "<$constructor$>";
      var0[26] = "<$constructor$>";
      var0[27] = "recorded";
      var0[28] = "<$constructor$>";
      var0[29] = "ignore";
      var0[30] = "<$constructor$>";
      var0[31] = "<$constructor$>";
      var0[32] = "isInterface";
      var0[33] = "instantiateDelegate";
      var0[34] = "INSTANCE";
      var0[35] = "putAt";
      var0[36] = "isInterface";
      var0[37] = "instantiateAggregateFromInterface";
      var0[38] = "INSTANCE";
      var0[39] = "isAbstract";
      var0[40] = "modifiers";
      var0[41] = "instantiateAggregateFromBaseClass";
      var0[42] = "INSTANCE";
      var0[43] = "isAssignableFrom";
      var0[44] = "newInstance";
      var0[45] = "instantiateDelegate";
      var0[46] = "INSTANCE";
      var0[47] = "newInstance";
      var0[48] = "isAssignableFrom";
      var0[49] = "newInstance";
      var0[50] = "instantiateDelegate";
      var0[51] = "INSTANCE";
      var0[52] = "newInstance";
      var0[53] = "ignore";
      var0[54] = "proxyInstance";
      var0[55] = "proxyDelegateInstance";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[56];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$mock$interceptor$MockFor(), var0);
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
   private static Class $get$$class$java$lang$IllegalArgumentException() {
      Class var10000 = $class$java$lang$IllegalArgumentException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalArgumentException = class$("java.lang.IllegalArgumentException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$reflect$Modifier() {
      Class var10000 = $class$java$lang$reflect$Modifier;
      if (var10000 == null) {
         var10000 = $class$java$lang$reflect$Modifier = class$("java.lang.reflect.Modifier");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$MockInterceptor() {
      Class var10000 = $class$groovy$mock$interceptor$MockInterceptor;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$MockInterceptor = class$("groovy.mock.interceptor.MockInterceptor");
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
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$MockProxyMetaClass() {
      Class var10000 = $class$groovy$mock$interceptor$MockProxyMetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$MockProxyMetaClass = class$("groovy.mock.interceptor.MockProxyMetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$MockFor() {
      Class var10000 = $class$groovy$mock$interceptor$MockFor;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$MockFor = class$("groovy.mock.interceptor.MockFor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$StrictExpectation() {
      Class var10000 = $class$groovy$mock$interceptor$StrictExpectation;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$StrictExpectation = class$("groovy.mock.interceptor.StrictExpectation");
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
   private static Class $get$$class$groovy$mock$interceptor$Ignore() {
      Class var10000 = $class$groovy$mock$interceptor$Ignore;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$Ignore = class$("groovy.mock.interceptor.Ignore");
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
   private static Class $get$$class$java$util$regex$Pattern() {
      Class var10000 = $class$java$util$regex$Pattern;
      if (var10000 == null) {
         var10000 = $class$java$util$regex$Pattern = class$("java.util.regex.Pattern");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$Demand() {
      Class var10000 = $class$groovy$mock$interceptor$Demand;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$Demand = class$("groovy.mock.interceptor.Demand");
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
   private static Class $get$$class$groovy$util$ProxyGenerator() {
      Class var10000 = $class$groovy$util$ProxyGenerator;
      if (var10000 == null) {
         var10000 = $class$groovy$util$ProxyGenerator = class$("groovy.util.ProxyGenerator");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$HashMap() {
      Class var10000 = $class$java$util$HashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyObject() {
      Class var10000 = $class$groovy$lang$GroovyObject;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyObject = class$("groovy.lang.GroovyObject");
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
