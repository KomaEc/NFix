package groovy.swing.binding;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.binding.TriggerBinding;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JListProperties implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203082L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203082 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$3;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$2;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$1;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$javax$swing$JList;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$4;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$5;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$6;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$7;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;

   public JListProperties() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static Map<String, TriggerBinding> getSyntheticProperties() {
      CallSite[] var0 = $getCallSiteArray();
      Map result = var0[0].callConstructor($get$$class$java$util$HashMap());
      var0[1].call(result, var0[2].call(var0[3].call($get$$class$javax$swing$JList()), (Object)"#selectedValue"), var0[4].callConstructor($get$$class$groovy$swing$binding$JListProperties$1(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[5].call(result, var0[6].call(var0[7].call($get$$class$javax$swing$JList()), (Object)"#selectedElement"), var0[8].callConstructor($get$$class$groovy$swing$binding$JListProperties$2(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[9].call(result, var0[10].call(var0[11].call($get$$class$javax$swing$JList()), (Object)"#selectedValues"), var0[12].callConstructor($get$$class$groovy$swing$binding$JListProperties$3(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[13].call(result, var0[14].call(var0[15].call($get$$class$javax$swing$JList()), (Object)"#selectedElements"), var0[16].callConstructor($get$$class$groovy$swing$binding$JListProperties$4(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[17].call(result, var0[18].call(var0[19].call($get$$class$javax$swing$JList()), (Object)"#selectedIndex"), var0[20].callConstructor($get$$class$groovy$swing$binding$JListProperties$5(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[21].call(result, var0[22].call(var0[23].call($get$$class$javax$swing$JList()), (Object)"#selectedIndices"), var0[24].callConstructor($get$$class$groovy$swing$binding$JListProperties$6(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      var0[25].call(result, var0[26].call(var0[27].call($get$$class$javax$swing$JList()), (Object)"#elements"), var0[28].callConstructor($get$$class$groovy$swing$binding$JListProperties$7(), (Object)$get$$class$groovy$swing$binding$JListProperties()));
      return (Map)ScriptBytecodeAdapter.castToType(result, $get$$class$java$util$Map());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$JListProperties()) {
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
      Class var10000 = $get$$class$groovy$swing$binding$JListProperties();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$JListProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$JListProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "<$constructor$>";
      var0[1] = "put";
      var0[2] = "plus";
      var0[3] = "getName";
      var0[4] = "<$constructor$>";
      var0[5] = "put";
      var0[6] = "plus";
      var0[7] = "getName";
      var0[8] = "<$constructor$>";
      var0[9] = "put";
      var0[10] = "plus";
      var0[11] = "getName";
      var0[12] = "<$constructor$>";
      var0[13] = "put";
      var0[14] = "plus";
      var0[15] = "getName";
      var0[16] = "<$constructor$>";
      var0[17] = "put";
      var0[18] = "plus";
      var0[19] = "getName";
      var0[20] = "<$constructor$>";
      var0[21] = "put";
      var0[22] = "plus";
      var0[23] = "getName";
      var0[24] = "<$constructor$>";
      var0[25] = "put";
      var0[26] = "plus";
      var0[27] = "getName";
      var0[28] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[29];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$JListProperties(), var0);
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
   private static Class $get$$class$groovy$swing$binding$JListProperties() {
      Class var10000 = $class$groovy$swing$binding$JListProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties = class$("groovy.swing.binding.JListProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$3() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$3;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$3 = class$("groovy.swing.binding.JListProperties$3");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$2() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$2;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$2 = class$("groovy.swing.binding.JListProperties$2");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$1() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$1;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$1 = class$("groovy.swing.binding.JListProperties$1");
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
   private static Class $get$$class$javax$swing$JList() {
      Class var10000 = $class$javax$swing$JList;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JList = class$("javax.swing.JList");
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
   private static Class $get$$class$groovy$swing$binding$JListProperties$4() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$4;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$4 = class$("groovy.swing.binding.JListProperties$4");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$5() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$5;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$5 = class$("groovy.swing.binding.JListProperties$5");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$6() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$6;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$6 = class$("groovy.swing.binding.JListProperties$6");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListProperties$7() {
      Class var10000 = $class$groovy$swing$binding$JListProperties$7;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties$7 = class$("groovy.swing.binding.JListProperties$7");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
