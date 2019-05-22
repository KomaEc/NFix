package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.lang.Reference;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.awt.Component;
import java.awt.Window;
import java.lang.ref.SoftReference;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.JButton;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public abstract class RootPaneContainerFactory extends AbstractFactory implements GroovyObject {
   public static final String DELEGATE_PROPERTY_DEFAULT_BUTTON = (String)"_delegateProperty:defaultButton";
   public static final String DEFAULT_DELEGATE_PROPERTY_DEFAULT_BUTTON = (String)"defaultButton";
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204502L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204502 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RootPaneContainerFactory;

   public RootPaneContainerFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(child instanceof Component && !(child instanceof Window) ? Boolean.FALSE : Boolean.TRUE)) {
         try {
            Object constraints = var4[0].callGetProperty(var4[1].callGroovyObjectGetProperty(builder));
            if (ScriptBytecodeAdapter.compareNotEqual(constraints, (Object)null)) {
               var4[2].call(var4[3].callGetProperty(parent), child, constraints);
               var4[4].call(var4[5].callGroovyObjectGetProperty(builder), (Object)"constraints");
            } else {
               var4[6].call(var4[7].callGetProperty(parent), child);
            }
         } catch (MissingPropertyException var8) {
            var4[8].call(var4[9].callGetProperty(parent), child);
         } finally {
            ;
         }

      }
   }

   public void handleRootPaneTasks(FactoryBuilderSupport builder, Window container, Map attributes) {
      FactoryBuilderSupport builder = new Reference(builder);
      Window container = new Reference(container);
      CallSite[] var6 = $getCallSiteArray();
      CallSite var10000 = var6[10];
      Object var10001 = var6[11].callGroovyObjectGetProperty(builder.get());
      String var10002 = DELEGATE_PROPERTY_DEFAULT_BUTTON;
      Object var10003 = var6[12].call(attributes, (Object)"defaultButtonProperty");
      if (!DefaultTypeTransformation.booleanUnbox(var10003)) {
         var10003 = DEFAULT_DELEGATE_PROPERTY_DEFAULT_BUTTON;
      }

      Object var7 = var10003;
      var10000.call(var10001, var10002, var7);
      ScriptBytecodeAdapter.setProperty(var6[13].call(builder.get(), (Object)(new GeneratedClosure(this, this, container, builder) {
         private Reference<T> container;
         private Reference<T> builder;
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)-1;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$awt$Window;
         // $FF: synthetic field
         private static Class $class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1;
         // $FF: synthetic field
         private static Class $class$java$util$ListIterator;
         // $FF: synthetic field
         private static Class $class$groovy$util$FactoryBuilderSupport;
         // $FF: synthetic field
         private static Class $class$java$util$Map;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.container = (Reference)container;
            this.builder = (Reference)builder;
         }

         public Object doCall(Object myBuilder, Object node, Object myAttributes) {
            Object nodex = new Reference(node);
            Object myAttributesx = new Reference(myAttributes);
            CallSite[] var6 = $getCallSiteArray();
            if (!DefaultTypeTransformation.booleanUnbox(nodex.get() instanceof JButton && ScriptBytecodeAdapter.compareEqual(var6[0].call(var6[1].callGroovyObjectGetProperty(this.builder.get()), (Object)$const$0), this.container.get()) ? Boolean.TRUE : Boolean.FALSE)) {
               return null;
            } else {
               ListIterator li = new Reference((ListIterator)ScriptBytecodeAdapter.castToType(var6[2].call(var6[3].callGroovyObjectGetProperty(this.builder.get())), $get$$class$java$util$ListIterator()));
               Reference context = new Reference((Map)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$Map()));

               while(DefaultTypeTransformation.booleanUnbox(var6[4].call(li.get()))) {
                  context.set((Map)ScriptBytecodeAdapter.castToType(var6[5].call(li.get()), $get$$class$java$util$Map()));
               }

               while(DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(context.get()) && ScriptBytecodeAdapter.compareNotEqual(var6[6].call(context.get(), var6[7].callGetProperty($get$$class$groovy$util$FactoryBuilderSupport())), this.container.get()) ? Boolean.TRUE : Boolean.FALSE)) {
                  context.set((Map)ScriptBytecodeAdapter.castToType(var6[8].call(li.get()), $get$$class$java$util$Map()));
               }

               Object var10000 = var6[9].call(context.get(), var6[10].callGroovyObjectGetProperty(this));
               if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
                  var10000 = var6[11].callGroovyObjectGetProperty(this);
               }

               Object defaultButtonProperty = var10000;
               Object defaultButton = var6[12].call(myAttributesx.get(), defaultButtonProperty);
               if (DefaultTypeTransformation.booleanUnbox(defaultButton)) {
                  var10000 = nodex.get();
                  ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1(), var6[13].callGetProperty(this.container.get()), "defaultButton");
                  return var10000;
               } else {
                  return null;
               }
            }
         }

         public Object call(Object myBuilder, Object node, Object myAttributes) {
            Object nodex = new Reference(node);
            Object myAttributesx = new Reference(myAttributes);
            CallSite[] var6 = $getCallSiteArray();
            return var6[14].callCurrent(this, myBuilder, nodex.get(), myAttributesx.get());
         }

         public Window getContainer() {
            CallSite[] var1 = $getCallSiteArray();
            return (Window)ScriptBytecodeAdapter.castToType(this.container.get(), $get$$class$java$awt$Window());
         }

         public FactoryBuilderSupport getBuilder() {
            CallSite[] var1 = $getCallSiteArray();
            return (FactoryBuilderSupport)ScriptBytecodeAdapter.castToType(this.builder.get(), $get$$class$groovy$util$FactoryBuilderSupport());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1()) {
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
            var0[0] = "getAt";
            var0[1] = "containingWindows";
            var0[2] = "listIterator";
            var0[3] = "contexts";
            var0[4] = "hasNext";
            var0[5] = "next";
            var0[6] = "getAt";
            var0[7] = "CURRENT_NODE";
            var0[8] = "previous";
            var0[9] = "getAt";
            var0[10] = "DELEGATE_PROPERTY_DEFAULT_BUTTON";
            var0[11] = "DEFAULT_DELEGATE_PROPERTY_DEFAULT_BUTTON";
            var0[12] = "remove";
            var0[13] = "rootPane";
            var0[14] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[15];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1(), var0);
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
         private static Class $get$$class$java$awt$Window() {
            Class var10000 = $class$java$awt$Window;
            if (var10000 == null) {
               var10000 = $class$java$awt$Window = class$("java.awt.Window");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1() {
            Class var10000 = $class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$factory$RootPaneContainerFactory$_handleRootPaneTasks_closure1 = class$("groovy.swing.factory.RootPaneContainerFactory$_handleRootPaneTasks_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$util$ListIterator() {
            Class var10000 = $class$java$util$ListIterator;
            if (var10000 == null) {
               var10000 = $class$java$util$ListIterator = class$("java.util.ListIterator");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$FactoryBuilderSupport() {
            Class var10000 = $class$groovy$util$FactoryBuilderSupport;
            if (var10000 == null) {
               var10000 = $class$groovy$util$FactoryBuilderSupport = class$("groovy.util.FactoryBuilderSupport");
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
      })), $get$$class$groovy$swing$factory$RootPaneContainerFactory(), var6[14].callGroovyObjectGetProperty(builder.get()), "defaultButtonDelegate");
      var6[15].call(var6[16].callGroovyObjectGetProperty(builder.get()), container.get());
      ScriptBytecodeAdapter.setProperty(var6[17].call(attributes, (Object)"pack"), $get$$class$groovy$swing$factory$RootPaneContainerFactory(), var6[18].callGroovyObjectGetProperty(builder.get()), "pack");
      ScriptBytecodeAdapter.setProperty(var6[19].call(attributes, (Object)"show"), $get$$class$groovy$swing$factory$RootPaneContainerFactory(), var6[20].callGroovyObjectGetProperty(builder.get()), "show");
      var6[21].call(builder.get(), (Object)ScriptBytecodeAdapter.getMethodPointer(container.get(), "dispose"));
   }

   public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
      CallSite[] var4 = $getCallSiteArray();
      if (node instanceof Window) {
         Object containingWindows = var4[22].callGroovyObjectGetProperty(builder);
         if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var4[23].callGetProperty(containingWindows)) && ScriptBytecodeAdapter.compareEqual(var4[24].callGetProperty(containingWindows), node) ? Boolean.TRUE : Boolean.FALSE)) {
            var4[25].call(containingWindows);
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(var4[26].callGetProperty(var4[27].callGroovyObjectGetProperty(builder)))) {
         var4[28].call(node);
      }

      if (DefaultTypeTransformation.booleanUnbox(var4[29].callGetProperty(var4[30].callGroovyObjectGetProperty(builder)))) {
         ScriptBytecodeAdapter.setProperty(Boolean.TRUE, $get$$class$groovy$swing$factory$RootPaneContainerFactory(), node, "visible");
      }

      var4[31].call(builder, (Object)var4[32].callGetProperty(var4[33].callGroovyObjectGetProperty(builder)));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$RootPaneContainerFactory()) {
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
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$factory$RootPaneContainerFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$RootPaneContainerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$RootPaneContainerFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

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
   public boolean super$2$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$2$setChild(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setChild(var1, var2, var3);
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
   public void super$2$onNodeCompleted(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.onNodeCompleted(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$onFactoryRegistration(FactoryBuilderSupport var1, String var2, String var3) {
      super.onFactoryRegistration(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3) {
      return super.onNodeChildren(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isHandlesNodeChildren() {
      return super.isHandlesNodeChildren();
   }

   // $FF: synthetic method
   public void super$2$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
      super.setParent(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$2$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "constraints";
      var0[1] = "context";
      var0[2] = "add";
      var0[3] = "contentPane";
      var0[4] = "remove";
      var0[5] = "context";
      var0[6] = "add";
      var0[7] = "contentPane";
      var0[8] = "add";
      var0[9] = "contentPane";
      var0[10] = "putAt";
      var0[11] = "context";
      var0[12] = "remove";
      var0[13] = "addAttributeDelegate";
      var0[14] = "context";
      var0[15] = "add";
      var0[16] = "containingWindows";
      var0[17] = "remove";
      var0[18] = "context";
      var0[19] = "remove";
      var0[20] = "context";
      var0[21] = "addDisposalClosure";
      var0[22] = "containingWindows";
      var0[23] = "empty";
      var0[24] = "last";
      var0[25] = "removeLast";
      var0[26] = "pack";
      var0[27] = "context";
      var0[28] = "pack";
      var0[29] = "show";
      var0[30] = "context";
      var0[31] = "removeAttributeDelegate";
      var0[32] = "defaultButtonDelegate";
      var0[33] = "context";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[34];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$RootPaneContainerFactory(), var0);
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$RootPaneContainerFactory() {
      Class var10000 = $class$groovy$swing$factory$RootPaneContainerFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RootPaneContainerFactory = class$("groovy.swing.factory.RootPaneContainerFactory");
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
