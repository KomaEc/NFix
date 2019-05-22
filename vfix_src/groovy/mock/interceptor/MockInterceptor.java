package groovy.mock.interceptor;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.PropertyAccessInterceptor;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MockInterceptor implements PropertyAccessInterceptor, GroovyObject {
   private Object expectation;
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
   public static Long __timeStamp = (Long)1292524202969L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202969 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalStateException;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$MockInterceptor;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$MockProxyMetaClass;
   // $FF: synthetic field
   private static Class array$$class$java$lang$Object;

   public MockInterceptor() {
      CallSite[] var1 = $getCallSiteArray();
      this.expectation = null;
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object beforeInvoke(Object object, String methodName, Object... arguments) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(this.expectation)) {
         throw (Throwable)var4[0].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"Property 'expectation' must be set before use.");
      } else {
         Object result = var4[1].call(this.expectation, (Object)methodName);
         if (ScriptBytecodeAdapter.compareEqual(result, var4[2].callGetProperty($get$$class$groovy$mock$interceptor$MockProxyMetaClass()))) {
            return result;
         } else {
            CallSite var10000 = var4[3];
            Object[] var10002 = new Object[0];
            Object[] var10003 = new Object[]{arguments};
            int[] var6 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
            return var10000.call(result, ScriptBytecodeAdapter.despreadList(var10002, var10003, var6));
         }
      }
   }

   public Object beforeGet(Object object, String property) {
      CallSite[] var3 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(this.expectation)) {
         throw (Throwable)var3[4].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"Property 'expectation' must be set before use.");
      } else {
         String name = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{var3[5].call(var3[6].call(property, (Object)$const$0)), var3[7].call(property, (Object)ScriptBytecodeAdapter.createRange($const$1, $const$2, true))}, new String[]{"get", "", ""}), $get$$class$java$lang$String());
         Object result = var3[8].call(this.expectation, (Object)name);
         return ScriptBytecodeAdapter.compareEqual(result, var3[9].callGetProperty($get$$class$groovy$mock$interceptor$MockProxyMetaClass())) ? result : var3[10].call(result);
      }
   }

   public void beforeSet(Object object, String property, Object newValue) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(this.expectation)) {
         throw (Throwable)var4[11].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"Property 'expectation' must be set before use.");
      } else {
         String name = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{var4[12].call(var4[13].call(property, (Object)$const$0)), var4[14].call(property, (Object)ScriptBytecodeAdapter.createRange($const$1, $const$2, true))}, new String[]{"set", "", ""}), $get$$class$java$lang$String());
         Object result = var4[15].call(this.expectation, (Object)name);
         if (ScriptBytecodeAdapter.compareNotEqual(result, var4[16].callGetProperty($get$$class$groovy$mock$interceptor$MockProxyMetaClass()))) {
            var4[17].call(result, newValue);
            result = null;
         }

         if (object instanceof Object[]) {
            var4[18].call((Object[])ScriptBytecodeAdapter.castToType(object, $get$array$$class$java$lang$Object()), $const$0, result);
         }

      }
   }

   public Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
      CallSite[] var5 = $getCallSiteArray();
      return null;
   }

   public boolean doInvoke() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$mock$interceptor$MockInterceptor()) {
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
      Class var10000 = $get$$class$groovy$mock$interceptor$MockInterceptor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$mock$interceptor$MockInterceptor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$mock$interceptor$MockInterceptor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public Object getExpectation() {
      return this.expectation;
   }

   public void setExpectation(Object var1) {
      this.expectation = var1;
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
      var0[1] = "match";
      var0[2] = "FALL_THROUGH_MARKER";
      var0[3] = "call";
      var0[4] = "<$constructor$>";
      var0[5] = "toUpperCase";
      var0[6] = "getAt";
      var0[7] = "getAt";
      var0[8] = "match";
      var0[9] = "FALL_THROUGH_MARKER";
      var0[10] = "call";
      var0[11] = "<$constructor$>";
      var0[12] = "toUpperCase";
      var0[13] = "getAt";
      var0[14] = "getAt";
      var0[15] = "match";
      var0[16] = "FALL_THROUGH_MARKER";
      var0[17] = "call";
      var0[18] = "putAt";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[19];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$mock$interceptor$MockInterceptor(), var0);
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$IllegalStateException() {
      Class var10000 = $class$java$lang$IllegalStateException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalStateException = class$("java.lang.IllegalStateException");
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
   private static Class $get$$class$groovy$mock$interceptor$MockProxyMetaClass() {
      Class var10000 = $class$groovy$mock$interceptor$MockProxyMetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$MockProxyMetaClass = class$("groovy.mock.interceptor.MockProxyMetaClass");
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
}
