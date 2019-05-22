package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ButtonGroupFactory extends BeanFactory {
   public static final String DELEGATE_PROPERTY_BUTTON_GROUP = (String)"_delegateProperty:buttonGroup";
   public static final String DEFAULT_DELEGATE_PROPERTY_BUTTON_GROUP = (String)"buttonGroup";
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204967L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204967 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BeanFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$ButtonGroup;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ButtonGroupFactory;

   public ButtonGroupFactory() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{$get$$class$javax$swing$ButtonGroup(), Boolean.TRUE};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$swing$factory$BeanFactory());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Class)var10001[0]);
         break;
      case 1:
         super((Class)var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      CallSite var10000 = var5[0];
      Object var10001 = var5[1].callGroovyObjectGetProperty(builder);
      String var10002 = DELEGATE_PROPERTY_BUTTON_GROUP;
      Object var10003 = var5[2].call(attributes, (Object)"buttonGroupProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_BUTTON_GROUP;
      }

      Object var6 = var10003;
      var10000.call(var10001, var10002, var6);
      return (Object)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$swing$factory$BeanFactory(), this, "newInstance", new Object[]{builder, name, value, attributes}), $get$$class$java$lang$Object());
   }

   public static Object buttonGroupAttributeDelegate(Object builder, Object node, Object attributes) {
      CallSite[] var3 = $getCallSiteArray();
      Object var10000 = var3[3].callSafe(var3[4].callGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_BUTTON_GROUP);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = DEFAULT_DELEGATE_PROPERTY_BUTTON_GROUP;
      }

      Object buttonGroupAttr = var10000;
      if (!DefaultTypeTransformation.booleanUnbox(var3[5].call(attributes, buttonGroupAttr))) {
         return null;
      } else {
         Object o = var3[6].call(attributes, buttonGroupAttr);
         if (DefaultTypeTransformation.booleanUnbox(o instanceof ButtonGroup && node instanceof AbstractButton ? Boolean.TRUE : Boolean.FALSE)) {
            ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$ButtonGroupFactory(), var3[7].callGetProperty(node), "group");
            return var3[8].call(attributes, buttonGroupAttr);
         } else {
            return null;
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ButtonGroupFactory()) {
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
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$ButtonGroupFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ButtonGroupFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ButtonGroupFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$this$dist$get$3(String var1) {
      return super.this$dist$get$3(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public boolean super$3$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$3$this$dist$set$3(String var1, Object var2) {
      super.this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$3$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$3$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Class super$3$getBeanClass() {
      return super.getBeanClass();
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$3$newInstance(FactoryBuilderSupport var1, Object var2, Object var3, Map var4) {
      return super.newInstance(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "putAt";
      var0[1] = "context";
      var0[2] = "remove";
      var0[3] = "getAt";
      var0[4] = "context";
      var0[5] = "containsKey";
      var0[6] = "get";
      var0[7] = "model";
      var0[8] = "remove";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[9];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ButtonGroupFactory(), var0);
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BeanFactory() {
      Class var10000 = $class$groovy$swing$factory$BeanFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BeanFactory = class$("groovy.swing.factory.BeanFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$ButtonGroup() {
      Class var10000 = $class$javax$swing$ButtonGroup;
      if (var10000 == null) {
         var10000 = $class$javax$swing$ButtonGroup = class$("javax.swing.ButtonGroup");
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
   private static Class $get$$class$groovy$swing$factory$ButtonGroupFactory() {
      Class var10000 = $class$groovy$swing$factory$ButtonGroupFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ButtonGroupFactory = class$("groovy.swing.factory.ButtonGroupFactory");
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
