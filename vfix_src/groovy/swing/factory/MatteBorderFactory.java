package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MatteBorderFactory extends SwingBorderFactory implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205018L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205018 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$BorderFactory;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$MatteBorderFactory;

   public MatteBorderFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(var5[0].call(attributes, (Object)"parent"), $get$$class$groovy$swing$factory$MatteBorderFactory(), var5[1].callGroovyObjectGetProperty(builder), "applyBorderToParent");
      Object matte = null;
      Object border = null;
      if (DefaultTypeTransformation.booleanUnbox(var5[2].call(attributes, (Object)"icon"))) {
         matte = var5[3].call(attributes, (Object)"icon");
      } else if (DefaultTypeTransformation.booleanUnbox(var5[4].call(attributes, (Object)"color"))) {
         matte = var5[5].call(attributes, (Object)"color");
      } else {
         if (!ScriptBytecodeAdapter.compareNotEqual(value, (Object)null)) {
            throw (Throwable)var5[6].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " must have a matte defined, either as a value argument or as a color: or icon: attribute"})));
         }

         matte = value;
      }

      if (DefaultTypeTransformation.booleanUnbox(var5[7].call(attributes, (Object)"size"))) {
         border = var5[8].call(attributes, (Object)"size");
         border = ScriptBytecodeAdapter.createList(new Object[]{border, border, border, border});
      } else if (DefaultTypeTransformation.booleanUnbox(var5[9].call(attributes, (Object)"top"))) {
         Object top = var5[10].call(attributes, (Object)"top");
         Object left = var5[11].call(attributes, (Object)"left");
         Object bottom = var5[12].call(attributes, (Object)"bottom");
         Object right = var5[13].call(attributes, (Object)"right");
         if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!ScriptBytecodeAdapter.compareEqual(top, (Object)null) && !ScriptBytecodeAdapter.compareEqual(left, (Object)null) ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(bottom, (Object)null) ? Boolean.FALSE : Boolean.TRUE) && !ScriptBytecodeAdapter.compareEqual(right, (Object)null) ? Boolean.FALSE : Boolean.TRUE)) {
            throw (Throwable)var5[14].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"In ", " if one of top:, left:, bottom: or right: is specified all must be specified"})));
         }

         border = ScriptBytecodeAdapter.createList(new Object[]{top, left, bottom, right});
      } else if (ScriptBytecodeAdapter.compareNotEqual(value, (Object)null)) {
         if (ScriptBytecodeAdapter.compareEqual(matte, value)) {
            throw (Throwable)var5[15].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"In ", " some attributes are required in addition to the value argument"})));
         }

         if (value instanceof Integer) {
            border = ScriptBytecodeAdapter.createList(new Object[]{value, value, value, value});
         } else {
            border = value;
         }
      }

      if (DefaultTypeTransformation.booleanUnbox(attributes)) {
         throw (Throwable)var5[16].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " only supports the attributes [ icon: | color:]  [ size: | ( top: left: bottom: right: ) }"})));
      } else {
         CallSite var10000 = var5[17];
         Class var10001 = $get$$class$javax$swing$BorderFactory();
         Object[] var10002 = new Object[]{matte};
         Object[] var10003 = new Object[]{border};
         int[] var12 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
         return (Object)ScriptBytecodeAdapter.castToType(var10000.call(var10001, (Object[])ScriptBytecodeAdapter.despreadList(var10002, var10003, var12)), $get$$class$java$lang$Object());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$MatteBorderFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$MatteBorderFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$MatteBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$MatteBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void super$3$setParent(FactoryBuilderSupport var1, Object var2, Object var3) {
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
   public boolean super$3$onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3) {
      return super.onHandleNodeAttributes(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
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
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "remove";
      var0[1] = "context";
      var0[2] = "containsKey";
      var0[3] = "remove";
      var0[4] = "containsKey";
      var0[5] = "remove";
      var0[6] = "<$constructor$>";
      var0[7] = "containsKey";
      var0[8] = "remove";
      var0[9] = "containsKey";
      var0[10] = "remove";
      var0[11] = "remove";
      var0[12] = "remove";
      var0[13] = "remove";
      var0[14] = "<$constructor$>";
      var0[15] = "<$constructor$>";
      var0[16] = "<$constructor$>";
      var0[17] = "createMatteBorder";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$MatteBorderFactory(), var0);
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
   private static Class $get$$class$javax$swing$BorderFactory() {
      Class var10000 = $class$javax$swing$BorderFactory;
      if (var10000 == null) {
         var10000 = $class$javax$swing$BorderFactory = class$("javax.swing.BorderFactory");
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
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
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
   private static Class $get$$class$groovy$swing$factory$MatteBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$MatteBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$MatteBorderFactory = class$("groovy.swing.factory.MatteBorderFactory");
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
