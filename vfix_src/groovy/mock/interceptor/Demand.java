package groovy.mock.interceptor;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.IntRange;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Demand implements GroovyObject {
   private List recorded;
   private Map ignore;
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
   public static Long __timeStamp = (Long)1292524202908L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202908 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$junit$framework$AssertionFailedError;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$CallSpec;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$Demand;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;

   public Demand() {
      CallSite[] var1 = $getCallSiteArray();
      this.recorded = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.ignore = (Map)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object invokeMethod(String methodName, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Object range = ScriptBytecodeAdapter.createRange($const$0, $const$0, true);
      if (var3[0].call(args, (Object)$const$1) instanceof IntRange) {
         range = var3[1].call(args, (Object)$const$1);
         if (DefaultTypeTransformation.booleanUnbox(var3[2].callGetProperty(range))) {
            throw (Throwable)var3[3].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Reverse ranges not supported.");
         }
      } else if (var3[4].call(args, (Object)$const$1) instanceof Integer) {
         range = ScriptBytecodeAdapter.createRange(var3[5].call(args, (Object)$const$1), var3[6].call(args, (Object)$const$1), true);
      }

      return var3[7].call(args, (Object)$const$2) instanceof Closure ? (Object)ScriptBytecodeAdapter.castToType(var3[8].call(this.recorded, (Object)var3[9].callConstructor($get$$class$groovy$mock$interceptor$CallSpec(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"name", methodName, "behavior", var3[10].call(args, (Object)$const$2), "range", range}))), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
   }

   public Object verify(List calls) {
      CallSite[] var2 = $getCallSiteArray();
      Object i = null;
      Object var4 = var2[11].call(ScriptBytecodeAdapter.createRange($const$1, var2[12].call(this.recorded), false));

      Object call;
      Object callCounter;
      do {
         if (!((Iterator)var4).hasNext()) {
            return null;
         }

         i = ((Iterator)var4).next();
         call = var2[13].call(this.recorded, (Object)i);
         callCounter = DefaultTypeTransformation.booleanUnbox(var2[14].call(calls, (Object)i)) ? var2[15].call(calls, (Object)i) : $const$1;
      } while(DefaultTypeTransformation.booleanUnbox(var2[16].call(var2[17].callGetProperty(call), callCounter)));

      Object msg = new GStringImpl(new Object[]{i, var2[18].call(var2[19].callGetProperty(call)), var2[20].callGetProperty(call)}, new String[]{"verify[", "]: expected ", " call(s) to '", "' but was "});
      throw (Throwable)var2[21].callConstructor($get$$class$junit$framework$AssertionFailedError(), (Object)var2[22].call(msg, (Object)(new GStringImpl(new Object[]{callCounter}, new String[]{"called ", " time(s)."}))));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$mock$interceptor$Demand()) {
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
      Class var10000 = $get$$class$groovy$mock$interceptor$Demand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$mock$interceptor$Demand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$mock$interceptor$Demand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public List getRecorded() {
      return this.recorded;
   }

   public void setRecorded(List var1) {
      this.recorded = var1;
   }

   public Map getIgnore() {
      return this.ignore;
   }

   public void setIgnore(Map var1) {
      this.ignore = var1;
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
      var0[0] = "getAt";
      var0[1] = "getAt";
      var0[2] = "reverse";
      var0[3] = "<$constructor$>";
      var0[4] = "getAt";
      var0[5] = "getAt";
      var0[6] = "getAt";
      var0[7] = "getAt";
      var0[8] = "leftShift";
      var0[9] = "<$constructor$>";
      var0[10] = "getAt";
      var0[11] = "iterator";
      var0[12] = "size";
      var0[13] = "getAt";
      var0[14] = "getAt";
      var0[15] = "getAt";
      var0[16] = "contains";
      var0[17] = "range";
      var0[18] = "toString";
      var0[19] = "range";
      var0[20] = "name";
      var0[21] = "<$constructor$>";
      var0[22] = "plus";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[23];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$mock$interceptor$Demand(), var0);
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
   private static Class $get$$class$junit$framework$AssertionFailedError() {
      Class var10000 = $class$junit$framework$AssertionFailedError;
      if (var10000 == null) {
         var10000 = $class$junit$framework$AssertionFailedError = class$("junit.framework.AssertionFailedError");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$mock$interceptor$CallSpec() {
      Class var10000 = $class$groovy$mock$interceptor$CallSpec;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$CallSpec = class$("groovy.mock.interceptor.CallSpec");
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
   private static Class $get$$class$groovy$mock$interceptor$Demand() {
      Class var10000 = $class$groovy$mock$interceptor$Demand;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$Demand = class$("groovy.mock.interceptor.Demand");
      }

      return var10000;
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
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
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
