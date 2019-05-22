package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.util.FactoryBuilderSupport;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.RootPaneContainer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GridBagFactory extends LayoutFactory {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205670L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205670 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Number;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$GridBagFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$RootPaneContainer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$LayoutFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$GridBagConstraints;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$awt$Container;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$awt$Insets;
   // $FF: synthetic field
   private static Class $class$java$awt$GridBagLayout;

   public GridBagFactory() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{$get$$class$java$awt$GridBagLayout(), Boolean.TRUE};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$swing$factory$LayoutFactory());
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

   public void addLayoutProperties(Object context) {
      CallSite[] var2 = $getCallSiteArray();
      var2[0].callCurrent(this, context, $get$$class$java$awt$GridBagConstraints());
   }

   public static void processGridBagConstraintsAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
      CallSite[] var3 = $getCallSiteArray();
      if (node instanceof Component) {
         Object var10000 = var3[1].callSafe(var3[2].callGroovyObjectGetPropertySafe(builder), var3[3].callGetProperty($get$$class$groovy$swing$factory$LayoutFactory()));
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var3[4].callGetProperty($get$$class$groovy$swing$factory$LayoutFactory());
         }

         Object constraintsAttr = var10000;
         if (!DefaultTypeTransformation.booleanUnbox(var3[5].call(attributes, (Object)constraintsAttr))) {
            if (!DefaultTypeTransformation.booleanUnbox(var3[6].call(var3[7].call(builder), (Object)"constraints"))) {
               Object parent = var3[8].call(builder);
               if (parent instanceof RootPaneContainer) {
                  if (!(var3[9].call(var3[10].call((RootPaneContainer)ScriptBytecodeAdapter.castToType(parent, $get$$class$javax$swing$RootPaneContainer()))) instanceof GridBagLayout)) {
                     return;
                  }
               } else {
                  if (!(parent instanceof Container)) {
                     return;
                  }

                  if (!(var3[11].call((Container)ScriptBytecodeAdapter.castToType(parent, $get$$class$java$awt$Container())) instanceof GridBagLayout)) {
                     return;
                  }
               }

               Boolean anyAttrs = Boolean.FALSE;
               GridBagConstraints gbc = var3[12].callConstructor($get$$class$java$awt$GridBagConstraints());
               Object o = null;
               o = var3[13].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "gridx", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "gridx");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[14].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "gridy", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "gridy");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[15].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "gridwidth", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "gridwidth");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[16].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "gridheight", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "gridheight");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[17].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "weightx", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "weightx");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[18].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "weighty", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "weighty");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[19].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "anchor", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "anchor");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[20].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "fill", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "fill");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[21].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "insets", $get$$class$java$lang$Object());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty((Insets)ScriptBytecodeAdapter.asType(o, $get$$class$java$awt$Insets()), $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "insets");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[22].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "ipadx", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "ipadx");
                  anyAttrs = Boolean.TRUE;
               }

               o = var3[23].callStatic($get$$class$groovy$swing$factory$GridBagFactory(), attributes, "ipady", $get$$class$java$lang$Number());
               if (ScriptBytecodeAdapter.compareNotEqual(o, (Object)null)) {
                  ScriptBytecodeAdapter.setProperty(o, $get$$class$groovy$swing$factory$GridBagFactory(), gbc, "ipady");
                  anyAttrs = Boolean.TRUE;
               }

               if (DefaultTypeTransformation.booleanUnbox(anyAttrs)) {
                  var3[24].call(var3[25].call(builder), "constraints", gbc);
               }

            }
         }
      }
   }

   public static Object extractAttribute(Map attrs, String name, Class type) {
      CallSite[] var3 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(var3[26].call(attrs, (Object)name))) {
         return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
      } else {
         Object o = var3[27].call(attrs, (Object)name);
         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(o, (Object)null) && DefaultTypeTransformation.booleanUnbox(var3[28].call(type, (Object)type)) ? Boolean.TRUE : Boolean.FALSE)) {
            var3[29].call(attrs, (Object)name);
            return (Object)ScriptBytecodeAdapter.castToType(o, $get$$class$java$lang$Object());
         } else {
            return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$GridBagFactory()) {
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
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$GridBagFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$GridBagFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$GridBagFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object super$4$super$3$this$dist$get$3(String var1) {
      return super.super$3$this$dist$get$3(var1);
   }

   // $FF: synthetic method
   public void super$4$setContextProps(Object var1) {
      super.setContextProps(var1);
   }

   // $FF: synthetic method
   public void super$4$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$4$this$dist$get$4(String var1) {
      return super.this$dist$get$4(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$4$this$dist$set$4(String var1, Object var2) {
      super.this$dist$set$4(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$getContextProps() {
      return super.getContextProps();
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
   public Class super$3$getBeanClass() {
      return super.getBeanClass();
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$4$super$3$this$dist$set$3(String var1, Object var2) {
      super.super$3$this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$4$super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.super$3$this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$newInstance(FactoryBuilderSupport var1, Object var2, Object var3, Map var4) {
      return super.newInstance(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Object super$4$this$dist$invoke$4(String var1, Object var2) {
      return super.this$dist$invoke$4(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$addLayoutProperties(Object var1, Class var2) {
      super.addLayoutProperties(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$addLayoutProperties(Object var1) {
      super.addLayoutProperties(var1);
   }

   // $FF: synthetic method
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$4$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "addLayoutProperties";
      var0[1] = "getAt";
      var0[2] = "context";
      var0[3] = "DELEGATE_PROPERTY_CONSTRAINT";
      var0[4] = "DEFAULT_DELEGATE_PROPERTY_CONSTRAINT";
      var0[5] = "containsKey";
      var0[6] = "containsKey";
      var0[7] = "getContext";
      var0[8] = "getCurrent";
      var0[9] = "getLayout";
      var0[10] = "getContentPane";
      var0[11] = "getLayout";
      var0[12] = "<$constructor$>";
      var0[13] = "extractAttribute";
      var0[14] = "extractAttribute";
      var0[15] = "extractAttribute";
      var0[16] = "extractAttribute";
      var0[17] = "extractAttribute";
      var0[18] = "extractAttribute";
      var0[19] = "extractAttribute";
      var0[20] = "extractAttribute";
      var0[21] = "extractAttribute";
      var0[22] = "extractAttribute";
      var0[23] = "extractAttribute";
      var0[24] = "put";
      var0[25] = "getContext";
      var0[26] = "containsKey";
      var0[27] = "get";
      var0[28] = "isAssignableFrom";
      var0[29] = "remove";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[30];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$GridBagFactory(), var0);
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
   private static Class $get$$class$java$lang$Number() {
      Class var10000 = $class$java$lang$Number;
      if (var10000 == null) {
         var10000 = $class$java$lang$Number = class$("java.lang.Number");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$GridBagFactory() {
      Class var10000 = $class$groovy$swing$factory$GridBagFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$GridBagFactory = class$("groovy.swing.factory.GridBagFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$RootPaneContainer() {
      Class var10000 = $class$javax$swing$RootPaneContainer;
      if (var10000 == null) {
         var10000 = $class$javax$swing$RootPaneContainer = class$("javax.swing.RootPaneContainer");
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
   private static Class $get$$class$groovy$swing$factory$LayoutFactory() {
      Class var10000 = $class$groovy$swing$factory$LayoutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$LayoutFactory = class$("groovy.swing.factory.LayoutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GridBagConstraints() {
      Class var10000 = $class$java$awt$GridBagConstraints;
      if (var10000 == null) {
         var10000 = $class$java$awt$GridBagConstraints = class$("java.awt.GridBagConstraints");
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
   private static Class $get$$class$java$awt$Container() {
      Class var10000 = $class$java$awt$Container;
      if (var10000 == null) {
         var10000 = $class$java$awt$Container = class$("java.awt.Container");
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
   private static Class $get$$class$java$awt$Insets() {
      Class var10000 = $class$java$awt$Insets;
      if (var10000 == null) {
         var10000 = $class$java$awt$Insets = class$("java.awt.Insets");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GridBagLayout() {
      Class var10000 = $class$java$awt$GridBagLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$GridBagLayout = class$("java.awt.GridBagLayout");
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
