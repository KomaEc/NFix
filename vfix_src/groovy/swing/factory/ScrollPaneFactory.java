package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.util.FactoryBuilderSupport;
import java.awt.Component;
import java.awt.Window;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.JViewport;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ScrollPaneFactory extends BeanFactory {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205031L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205031 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ScrollPaneFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JScrollPane;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BeanFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public ScrollPaneFactory() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{$get$$class$javax$swing$JScrollPane()};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$swing$factory$ScrollPaneFactory());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Class)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public ScrollPaneFactory(Class klass) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{klass, Boolean.FALSE};
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

   public void setChild(FactoryBuilderSupport factory, Object parent, Object child) {
      CallSite[] var4 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(child instanceof Component && !(child instanceof Window) ? Boolean.FALSE : Boolean.TRUE)) {
         if (ScriptBytecodeAdapter.compareNotEqual(var4[0].callSafe(var4[1].call(parent)), (Object)null)) {
            throw (Throwable)var4[2].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"ScrollPane can only have one child component");
         } else {
            if (child instanceof JViewport) {
               var4[3].call(parent, child);
            } else {
               var4[4].call(parent, child);
            }

         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ScrollPaneFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$ScrollPaneFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ScrollPaneFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ScrollPaneFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "getView";
      var0[1] = "getViewport";
      var0[2] = "<$constructor$>";
      var0[3] = "setViewport";
      var0[4] = "setViewportView";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[5];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ScrollPaneFactory(), var0);
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
   private static Class $get$$class$groovy$swing$factory$ScrollPaneFactory() {
      Class var10000 = $class$groovy$swing$factory$ScrollPaneFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ScrollPaneFactory = class$("groovy.swing.factory.ScrollPaneFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JScrollPane() {
      Class var10000 = $class$javax$swing$JScrollPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JScrollPane = class$("javax.swing.JScrollPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
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
