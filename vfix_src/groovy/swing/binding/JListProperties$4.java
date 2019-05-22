package groovy.swing.binding;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.binding.FullBinding;
import org.codehaus.groovy.binding.PropertyBinding;
import org.codehaus.groovy.binding.SourceBinding;
import org.codehaus.groovy.binding.TargetBinding;
import org.codehaus.groovy.binding.TriggerBinding;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class JListProperties$4 implements TriggerBinding, GroovyObject {
   // $FF: synthetic field
   public Class<T> this$0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$PropertyBinding;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListProperties$4;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$binding$JListSelectedElementBinding;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$binding$FullBinding;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;

   // $FF: synthetic method
   JListProperties$4(Class<T> p0) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[0];
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$java$lang$Object());
      if ((var10002 & 1) != 0) {
         Object[] var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         this.this$0 = (Class)ScriptBytecodeAdapter.castToType(p0, $get$$class$java$lang$Class());
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public FullBinding createBinding(SourceBinding source, TargetBinding target) {
      SourceBinding source = new Reference(source);
      TargetBinding target = new Reference(target);
      CallSite[] var5 = $getCallSiteArray();
      return (FullBinding)ScriptBytecodeAdapter.castToType(var5[0].callConstructor($get$$class$groovy$swing$binding$JListSelectedElementBinding(), ScriptBytecodeAdapter.createPojoWrapper((PropertyBinding)ScriptBytecodeAdapter.castToType(source.get(), $get$$class$org$codehaus$groovy$binding$PropertyBinding()), $get$$class$org$codehaus$groovy$binding$PropertyBinding()), target.get(), "selectedElements"), $get$$class$org$codehaus$groovy$binding$FullBinding());
   }

   // $FF: synthetic method
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$binding$JListProperties$4();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$binding$JListProperties$4(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$binding$JListProperties$4(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object methodMissing(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$binding$JListProperties$4();
      Class var10001 = $get$$class$groovy$swing$binding$JListProperties();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodN(var10000, var10001, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void propertyMissing(String name, Object val) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setField(val, $get$$class$groovy$swing$binding$JListProperties$4(), $get$$class$groovy$swing$binding$JListProperties(), (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object propertyMissing(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getField($get$$class$groovy$swing$binding$JListProperties$4(), $get$$class$groovy$swing$binding$JListProperties(), (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$binding$JListProperties$4()) {
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
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[1];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$binding$JListProperties$4(), var0);
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
   private static Class $get$$class$groovy$swing$binding$JListProperties() {
      Class var10000 = $class$groovy$swing$binding$JListProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListProperties = class$("groovy.swing.binding.JListProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$binding$PropertyBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$PropertyBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$PropertyBinding = class$("org.codehaus.groovy.binding.PropertyBinding");
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$binding$JListSelectedElementBinding() {
      Class var10000 = $class$groovy$swing$binding$JListSelectedElementBinding;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$binding$JListSelectedElementBinding = class$("groovy.swing.binding.JListSelectedElementBinding");
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
   private static Class $get$$class$org$codehaus$groovy$binding$FullBinding() {
      Class var10000 = $class$org$codehaus$groovy$binding$FullBinding;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$binding$FullBinding = class$("org.codehaus.groovy.binding.FullBinding");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
