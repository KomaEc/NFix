package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.util.FactoryBuilderSupport;
import java.awt.Container;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.RootPaneContainer;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class LayoutFactory extends BeanFactory {
   private Object contextProps;
   public static final String DELEGATE_PROPERTY_CONSTRAINT = (String)"_delegateProperty:Constrinat";
   public static final String DEFAULT_DELEGATE_PROPERTY_CONSTRAINT = (String)"constraints";
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205011L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205011 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$RootPaneContainer;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$LayoutFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$awt$Container;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BeanFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public LayoutFactory(Class klass) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{klass};
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

   public LayoutFactory(Class klass, boolean leaf) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{klass, DefaultTypeTransformation.box(leaf)};
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
      String var10002 = DELEGATE_PROPERTY_CONSTRAINT;
      Object var10003 = var5[2].call(attributes, (Object)"constraintsProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_CONSTRAINT;
      }

      Object o = var10003;
      var10000.call(var10001, var10002, o);
      o = ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$swing$factory$BeanFactory(), this, "newInstance", new Object[]{builder, name, value, attributes});
      var5[3].callCurrent(this, (Object)var5[4].call(builder));
      return (Object)ScriptBytecodeAdapter.castToType(o, $get$$class$java$lang$Object());
   }

   public void addLayoutProperties(Object context, Class layoutClass) {
      Class layoutClass = new Reference(layoutClass);
      CallSite[] var4 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(this.contextProps, (Object)null)) {
         this.contextProps = ScriptBytecodeAdapter.createMap(new Object[0]);
         var4[5].call(var4[6].callGetProperty(layoutClass.get()), (Object)(new GeneratedClosure(this, this, layoutClass) {
            private Reference<T> layoutClass;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$java$lang$String;
            // $FF: synthetic field
            private static Class $class$java$lang$Class;
            // $FF: synthetic field
            private static Class $class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.layoutClass = (Reference)layoutClass;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               Object name = new Reference(var3[0].callGetProperty(itx.get()));
               return ScriptBytecodeAdapter.compareEqual(var3[1].call(name.get()), name.get()) ? var3[2].call(var3[3].callGroovyObjectGetProperty(this), name.get(), ScriptBytecodeAdapter.getProperty($get$$class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1(), this.layoutClass.get(), (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name.get()}, new String[]{"", ""}), $get$$class$java$lang$String()))) : null;
            }

            public Class getLayoutClass() {
               CallSite[] var1 = $getCallSiteArray();
               return (Class)ScriptBytecodeAdapter.castToType(this.layoutClass.get(), $get$$class$java$lang$Class());
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[4].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1()) {
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
               var0[0] = "name";
               var0[1] = "toUpperCase";
               var0[2] = "put";
               var0[3] = "contextProps";
               var0[4] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[5];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1(), var0);
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
            private static Class $get$$class$java$lang$String() {
               Class var10000 = $class$java$lang$String;
               if (var10000 == null) {
                  var10000 = $class$java$lang$String = class$("java.lang.String");
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
            private static Class $get$$class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1() {
               Class var10000 = $class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$swing$factory$LayoutFactory$_addLayoutProperties_closure1 = class$("groovy.swing.factory.LayoutFactory$_addLayoutProperties_closure1");
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
      }

      var4[7].call(context, this.contextProps);
   }

   public void addLayoutProperties(Object context) {
      CallSite[] var2 = $getCallSiteArray();
      var2[8].callCurrent(this, context, var2[9].callGroovyObjectGetProperty(this));
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      CallSite[] var4 = $getCallSiteArray();
      if (parent instanceof Container) {
         ScriptBytecodeAdapter.setProperty(child, $get$$class$groovy$swing$factory$LayoutFactory(), var4[10].callStatic($get$$class$groovy$swing$factory$LayoutFactory(), parent), "layout");
      }

   }

   public static Container getLayoutTarget(Container parent) {
      CallSite[] var1 = $getCallSiteArray();
      if (parent instanceof RootPaneContainer) {
         RootPaneContainer rpc = (RootPaneContainer)ScriptBytecodeAdapter.castToType(parent, $get$$class$javax$swing$RootPaneContainer());
         parent = (Container)ScriptBytecodeAdapter.castToType(var1[11].call(rpc), $get$$class$java$awt$Container());
      }

      return (Container)ScriptBytecodeAdapter.castToType(parent, $get$$class$java$awt$Container());
   }

   public static Object constraintsAttributeDelegate(Object builder, Object node, Object attributes) {
      CallSite[] var3 = $getCallSiteArray();
      Object var10000 = var3[12].callSafe(var3[13].callGetPropertySafe(builder), (Object)DELEGATE_PROPERTY_CONSTRAINT);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = DEFAULT_DELEGATE_PROPERTY_CONSTRAINT;
      }

      Object constraintsAttr = var10000;
      if (DefaultTypeTransformation.booleanUnbox(var3[14].call(attributes, constraintsAttr))) {
         var10000 = var3[15].call(attributes, constraintsAttr);
         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$swing$factory$LayoutFactory(), var3[16].callGetProperty(builder), "constraints");
         return var10000;
      } else {
         return null;
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$LayoutFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$LayoutFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$LayoutFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$LayoutFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getContextProps() {
      return this.contextProps;
   }

   public void setContextProps(Object var1) {
      this.contextProps = var1;
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
      var0[3] = "addLayoutProperties";
      var0[4] = "getContext";
      var0[5] = "each";
      var0[6] = "fields";
      var0[7] = "putAll";
      var0[8] = "addLayoutProperties";
      var0[9] = "beanClass";
      var0[10] = "getLayoutTarget";
      var0[11] = "getContentPane";
      var0[12] = "getAt";
      var0[13] = "context";
      var0[14] = "containsKey";
      var0[15] = "remove";
      var0[16] = "context";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[17];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$LayoutFactory(), var0);
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
   private static Class $get$$class$javax$swing$RootPaneContainer() {
      Class var10000 = $class$javax$swing$RootPaneContainer;
      if (var10000 == null) {
         var10000 = $class$javax$swing$RootPaneContainer = class$("javax.swing.RootPaneContainer");
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
   private static Class $get$$class$java$awt$Container() {
      Class var10000 = $class$java$awt$Container;
      if (var10000 == null) {
         var10000 = $class$java$awt$Container = class$("java.awt.Container");
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
}
