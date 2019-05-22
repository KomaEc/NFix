package groovy.mock.interceptor;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class LooseExpectation implements GroovyObject {
   private Demand fDemand;
   private List fCalls;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202917L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202917 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$junit$framework$AssertionFailedError;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$LooseExpectation;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$Demand;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;

   public LooseExpectation(Demand demand) {
      CallSite[] var2 = $getCallSiteArray();
      this.fDemand = (Demand)ScriptBytecodeAdapter.castToType((Demand)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$mock$interceptor$Demand()), $get$$class$groovy$mock$interceptor$Demand());
      this.fCalls = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.fDemand = (Demand)ScriptBytecodeAdapter.castToType(demand, $get$$class$groovy$mock$interceptor$Demand());
   }

   public Closure match(String name) {
      String name = new Reference(name);
      CallSite[] var3 = $getCallSiteArray();
      Object filter = var3[0].call(var3[1].call(var3[2].callGroovyObjectGetProperty(this.fDemand)), (Object)(new GeneratedClosure(this, this, name) {
         private Reference<T> name;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$mock$interceptor$LooseExpectation$_match_closure1;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$runtime$DefaultGroovyMethods;
         // $FF: synthetic field
         private static Class $class$java$lang$String;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.name = (Reference)name;
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call($get$$class$org$codehaus$groovy$runtime$DefaultGroovyMethods(), ScriptBytecodeAdapter.createList(new Object[]{this.name.get()}), itx.get());
         }

         public String getName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.name.get(), $get$$class$java$lang$String());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$mock$interceptor$LooseExpectation$_match_closure1()) {
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
            var0[0] = "grep";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$mock$interceptor$LooseExpectation$_match_closure1(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$mock$interceptor$LooseExpectation$_match_closure1() {
            Class var10000 = $class$groovy$mock$interceptor$LooseExpectation$_match_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$mock$interceptor$LooseExpectation$_match_closure1 = class$("groovy.mock.interceptor.LooseExpectation$_match_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$org$codehaus$groovy$runtime$DefaultGroovyMethods() {
            Class var10000 = $class$org$codehaus$groovy$runtime$DefaultGroovyMethods;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$runtime$DefaultGroovyMethods = class$("org.codehaus.groovy.runtime.DefaultGroovyMethods");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      if (DefaultTypeTransformation.booleanUnbox(filter)) {
         return (Closure)ScriptBytecodeAdapter.castToType(var3[3].call(var3[4].callGroovyObjectGetProperty(this.fDemand), filter), $get$$class$groovy$lang$Closure());
      } else {
         Object callIndex;
         for(callIndex = $const$0; !DefaultTypeTransformation.booleanUnbox(var3[5].callCurrent(this, name.get(), callIndex)); callIndex = var3[6].call(callIndex)) {
         }

         CallSite var10000 = var3[7];
         CallSite var10001 = var3[8];
         CallSite var10002 = var3[9];
         List var10004 = this.fCalls;
         Object var7 = var10001.call(var10002.call(var10004, (Object)callIndex), (Object)$const$1);
         var10000.call(var10004, callIndex, var7);
         return (Closure)ScriptBytecodeAdapter.castToType(var3[10].callGetProperty(var3[11].call(var3[12].callGroovyObjectGetProperty(this.fDemand), callIndex)), $get$$class$groovy$lang$Closure());
      }
   }

   public boolean isEligible(String name, int i) {
      CallSite[] var3 = $getCallSiteArray();
      Object calls = var3[13].callGroovyObjectGetProperty(this.fDemand);
      if (ScriptBytecodeAdapter.compareGreaterThanEqual(DefaultTypeTransformation.box(i), var3[14].call(calls))) {
         throw (Throwable)var3[15].callConstructor($get$$class$junit$framework$AssertionFailedError(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"No more calls to '", "' expected at this point. End of demands."})));
      } else if (ScriptBytecodeAdapter.compareNotEqual(var3[16].callGetProperty(var3[17].call(calls, DefaultTypeTransformation.box(i))), name)) {
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      } else {
         if (ScriptBytecodeAdapter.compareEqual((Object)null, var3[18].call(this.fCalls, (Object)DefaultTypeTransformation.box(i)))) {
            CallSite var10000 = var3[19];
            List var10001 = this.fCalls;
            Object var10002 = DefaultTypeTransformation.box(i);
            Integer var5 = $const$0;
            var10000.call(var10001, var10002, var5);
         }

         return ScriptBytecodeAdapter.compareGreaterThanEqual(var3[20].call(this.fCalls, (Object)DefaultTypeTransformation.box(i)), var3[21].callGetProperty(var3[22].callGetProperty(var3[23].call(calls, DefaultTypeTransformation.box(i))))) ? DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean())) : DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
      }
   }

   public void verify() {
      CallSite[] var1 = $getCallSiteArray();
      var1[24].call(this.fDemand, (Object)this.fCalls);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$mock$interceptor$LooseExpectation()) {
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
      Class var10000 = $get$$class$groovy$mock$interceptor$LooseExpectation();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$mock$interceptor$LooseExpectation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$mock$interceptor$LooseExpectation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public Demand getfDemand() {
      return this.fDemand;
   }

   public void setfDemand(Demand var1) {
      this.fDemand = var1;
   }

   public List getfCalls() {
      return this.fCalls;
   }

   public void setfCalls(List var1) {
      this.fCalls = var1;
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
      var0[0] = "find";
      var0[1] = "keySet";
      var0[2] = "ignore";
      var0[3] = "get";
      var0[4] = "ignore";
      var0[5] = "isEligible";
      var0[6] = "next";
      var0[7] = "putAt";
      var0[8] = "plus";
      var0[9] = "getAt";
      var0[10] = "behavior";
      var0[11] = "getAt";
      var0[12] = "recorded";
      var0[13] = "recorded";
      var0[14] = "size";
      var0[15] = "<$constructor$>";
      var0[16] = "name";
      var0[17] = "getAt";
      var0[18] = "getAt";
      var0[19] = "putAt";
      var0[20] = "getAt";
      var0[21] = "to";
      var0[22] = "range";
      var0[23] = "getAt";
      var0[24] = "verify";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[25];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$mock$interceptor$LooseExpectation(), var0);
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
   private static Class $get$$class$groovy$mock$interceptor$LooseExpectation() {
      Class var10000 = $class$groovy$mock$interceptor$LooseExpectation;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$LooseExpectation = class$("groovy.mock.interceptor.LooseExpectation");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
