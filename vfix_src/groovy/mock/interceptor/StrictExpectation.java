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

public class StrictExpectation implements GroovyObject {
   private Demand fDemand;
   private int fCallSpecIdx;
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
   public static Long __timeStamp = (Long)1292524202973L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202973 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$StrictExpectation;
   // $FF: synthetic field
   private static Class $class$junit$framework$AssertionFailedError;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$mock$interceptor$Demand;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;

   public StrictExpectation(Demand demand) {
      CallSite[] var2 = $getCallSiteArray();
      this.fDemand = (Demand)ScriptBytecodeAdapter.castToType((Demand)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$mock$interceptor$Demand()), $get$$class$groovy$mock$interceptor$Demand());
      this.fCallSpecIdx = DefaultTypeTransformation.intUnbox($const$0);
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
         private static Class $class$groovy$mock$interceptor$StrictExpectation$_match_closure1;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
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
            if (this.getClass() == $get$$class$groovy$mock$interceptor$StrictExpectation$_match_closure1()) {
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
            return new CallSiteArray($get$$class$groovy$mock$interceptor$StrictExpectation$_match_closure1(), var0);
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
         private static Class $get$$class$groovy$mock$interceptor$StrictExpectation$_match_closure1() {
            Class var10000 = $class$groovy$mock$interceptor$StrictExpectation$_match_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$mock$interceptor$StrictExpectation$_match_closure1 = class$("groovy.mock.interceptor.StrictExpectation$_match_closure1");
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
         CallSite var10000;
         if (!DefaultTypeTransformation.booleanUnbox(var3[5].call(this.fCalls, (Object)DefaultTypeTransformation.box(this.fCallSpecIdx)))) {
            var10000 = var3[6];
            List var10001 = this.fCalls;
            Object var10002 = DefaultTypeTransformation.box(this.fCallSpecIdx);
            Integer var5 = $const$0;
            var10000.call(var10001, var10002, var5);
         }

         if (ScriptBytecodeAdapter.compareGreaterThanEqual(DefaultTypeTransformation.box(this.fCallSpecIdx), var3[7].call(var3[8].callGroovyObjectGetProperty(this.fDemand)))) {
            throw (Throwable)var3[9].callConstructor($get$$class$junit$framework$AssertionFailedError(), (Object)(new GStringImpl(new Object[]{name.get()}, new String[]{"No more calls to '", "' expected at this point. End of demands."})));
         } else {
            Object call = var3[10].call(var3[11].callGroovyObjectGetProperty(this.fDemand), DefaultTypeTransformation.box(this.fCallSpecIdx));
            Object result;
            Object var7;
            if (ScriptBytecodeAdapter.compareNotEqual(name.get(), var3[12].callGetProperty(call))) {
               result = var3[13].call(var3[14].callGetProperty(var3[15].callGetProperty(call)), var3[16].call(this.fCalls, (Object)DefaultTypeTransformation.box(this.fCallSpecIdx)));
               if (ScriptBytecodeAdapter.compareGreaterThan(result, $const$0)) {
                  throw (Throwable)var3[17].callConstructor($get$$class$junit$framework$AssertionFailedError(), (Object)var3[18].call(new GStringImpl(new Object[]{name.get()}, new String[]{"No call to '", "' expected at this point. "}), (Object)(new GStringImpl(new Object[]{result, var3[19].callGetProperty(call)}, new String[]{"Still ", " call(s) to '", "' expected."}))));
               } else {
                  var7 = DefaultTypeTransformation.box(this.fCallSpecIdx);
                  this.fCallSpecIdx = DefaultTypeTransformation.intUnbox(var3[20].call(DefaultTypeTransformation.box(this.fCallSpecIdx)));
                  return (Closure)ScriptBytecodeAdapter.castToType(var3[21].callCurrent(this, (Object)name.get()), $get$$class$groovy$lang$Closure());
               }
            } else {
               var10000 = var3[22];
               CallSite var9 = var3[23];
               CallSite var10 = var3[24];
               List var10003 = this.fCalls;
               Object var10005 = DefaultTypeTransformation.box(this.fCallSpecIdx);
               result = var9.call(var10.call(var10003, (Object)var10005), (Object)$const$1);
               var10000.call(var10003, var10005, result);
               result = var3[25].callGetProperty(call);
               if (ScriptBytecodeAdapter.compareGreaterThanEqual(var3[26].call(this.fCalls, (Object)DefaultTypeTransformation.box(this.fCallSpecIdx)), var3[27].callGetProperty(var3[28].callGetProperty(call)))) {
                  var7 = DefaultTypeTransformation.box(this.fCallSpecIdx);
                  this.fCallSpecIdx = DefaultTypeTransformation.intUnbox(var3[29].call(DefaultTypeTransformation.box(this.fCallSpecIdx)));
               }

               return (Closure)ScriptBytecodeAdapter.castToType(result, $get$$class$groovy$lang$Closure());
            }
         }
      }
   }

   public void verify() {
      CallSite[] var1 = $getCallSiteArray();
      var1[30].call(this.fDemand, (Object)this.fCalls);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$mock$interceptor$StrictExpectation()) {
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
      Class var10000 = $get$$class$groovy$mock$interceptor$StrictExpectation();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$mock$interceptor$StrictExpectation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$mock$interceptor$StrictExpectation(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public int getfCallSpecIdx() {
      return this.fCallSpecIdx;
   }

   public void setfCallSpecIdx(int var1) {
      this.fCallSpecIdx = var1;
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
      var0[5] = "getAt";
      var0[6] = "putAt";
      var0[7] = "size";
      var0[8] = "recorded";
      var0[9] = "<$constructor$>";
      var0[10] = "getAt";
      var0[11] = "recorded";
      var0[12] = "name";
      var0[13] = "minus";
      var0[14] = "from";
      var0[15] = "range";
      var0[16] = "getAt";
      var0[17] = "<$constructor$>";
      var0[18] = "plus";
      var0[19] = "name";
      var0[20] = "next";
      var0[21] = "match";
      var0[22] = "putAt";
      var0[23] = "plus";
      var0[24] = "getAt";
      var0[25] = "behavior";
      var0[26] = "getAt";
      var0[27] = "to";
      var0[28] = "range";
      var0[29] = "next";
      var0[30] = "verify";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[31];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$mock$interceptor$StrictExpectation(), var0);
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
   private static Class $get$$class$groovy$mock$interceptor$StrictExpectation() {
      Class var10000 = $class$groovy$mock$interceptor$StrictExpectation;
      if (var10000 == null) {
         var10000 = $class$groovy$mock$interceptor$StrictExpectation = class$("groovy.mock.interceptor.StrictExpectation");
      }

      return var10000;
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
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
