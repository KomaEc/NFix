package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import javax.swing.Action;
import javax.swing.Icon;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class RichActionWidgetFactory extends AbstractFactory implements GroovyObject {
   private static final Class[] ACTION_ARGS = (Class[])((Class[])ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{$get$$class$javax$swing$Action()}), $get$array$$class$java$lang$Class()));
   private static final Class[] ICON_ARGS = (Class[])((Class[])ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{$get$$class$javax$swing$Icon()}), $get$array$$class$java$lang$Class()));
   private static final Class[] STRING_ARGS = (Class[])((Class[])ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{$get$$class$java$lang$String()}), $get$array$$class$java$lang$Class()));
   private final Constructor actionCtor;
   private final Constructor iconCtor;
   private final Constructor stringCtor;
   private final Class klass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204498L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204498 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RichActionWidgetFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$reflect$Constructor;
   // $FF: synthetic field
   private static Class $class$javax$swing$Action;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Logger;
   // $FF: synthetic field
   private static Class $class$javax$swing$Icon;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class array$$class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Level;

   public RichActionWidgetFactory(Class klass) {
      CallSite[] var2 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());

      try {
         this.actionCtor = (Constructor)ScriptBytecodeAdapter.castToType((Constructor)ScriptBytecodeAdapter.castToType(var2[0].call(klass, (Object)ACTION_ARGS), $get$$class$java$lang$reflect$Constructor()), $get$$class$java$lang$reflect$Constructor());
         this.iconCtor = (Constructor)ScriptBytecodeAdapter.castToType((Constructor)ScriptBytecodeAdapter.castToType(var2[1].call(klass, (Object)ICON_ARGS), $get$$class$java$lang$reflect$Constructor()), $get$$class$java$lang$reflect$Constructor());
         this.stringCtor = (Constructor)ScriptBytecodeAdapter.castToType((Constructor)ScriptBytecodeAdapter.castToType(var2[2].call(klass, (Object)STRING_ARGS), $get$$class$java$lang$reflect$Constructor()), $get$$class$java$lang$reflect$Constructor());
         this.klass = (Class)ScriptBytecodeAdapter.castToType(klass, $get$$class$java$lang$Class());
      } catch (NoSuchMethodException var7) {
         var2[3].call(var2[4].call($get$$class$java$util$logging$Logger(), (Object)"global"), var2[5].callGetProperty($get$$class$java$util$logging$Level()), (Object)null, var7);
      } catch (SecurityException var8) {
         var2[6].call(var2[7].call($get$$class$java$util$logging$Logger(), (Object)"global"), var2[8].callGetProperty($get$$class$java$util$logging$Level()), (Object)null, var8);
      } finally {
         ;
      }

   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      CallSite[] var5 = $getCallSiteArray();

      Object var6;
      try {
         if (value instanceof GString) {
            value = (String)ScriptBytecodeAdapter.asType(value, $get$$class$java$lang$String());
         }

         if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
            var6 = (Object)ScriptBytecodeAdapter.castToType(var5[9].call(this.klass), $get$$class$java$lang$Object());
            return var6;
         }

         if (value instanceof Action) {
            var6 = (Object)ScriptBytecodeAdapter.castToType(var5[10].call(this.actionCtor, (Object)value), $get$$class$java$lang$Object());
            return var6;
         }

         if (value instanceof Icon) {
            var6 = (Object)ScriptBytecodeAdapter.castToType(var5[11].call(this.iconCtor, (Object)value), $get$$class$java$lang$Object());
            return var6;
         }

         if (value instanceof String) {
            var6 = (Object)ScriptBytecodeAdapter.castToType(var5[12].call(this.stringCtor, (Object)value), $get$$class$java$lang$Object());
            return var6;
         }

         if (!DefaultTypeTransformation.booleanUnbox(var5[13].call(this.klass, (Object)var5[14].call(value)))) {
            throw (Throwable)var5[15].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name, var5[16].callGetProperty(this.klass)}, new String[]{"", " can only have a value argument of type javax.swing.Action, javax.swing.Icon, java.lang.String, or ", ""})));
         }

         var6 = (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
      } catch (IllegalArgumentException var10) {
         throw (Throwable)var5[17].callConstructor($get$$class$java$lang$RuntimeException(), new GStringImpl(new Object[]{name, var10}, new String[]{"Failed to create component for '", "' reason: ", ""}), var10);
      } catch (InvocationTargetException var11) {
         throw (Throwable)var5[18].callConstructor($get$$class$java$lang$RuntimeException(), new GStringImpl(new Object[]{name, var11}, new String[]{"Failed to create component for '", "' reason: ", ""}), var11);
      } finally {
         ;
      }

      return var6;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$RichActionWidgetFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$RichActionWidgetFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$RichActionWidgetFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$RichActionWidgetFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public static final Class[] getACTION_ARGS() {
      return ACTION_ARGS;
   }

   public static final Class[] getICON_ARGS() {
      return ICON_ARGS;
   }

   public static final Class[] getSTRING_ARGS() {
      return STRING_ARGS;
   }

   public final Constructor getActionCtor() {
      return this.actionCtor;
   }

   public final Constructor getIconCtor() {
      return this.iconCtor;
   }

   public final Constructor getStringCtor() {
      return this.stringCtor;
   }

   public final Class getKlass() {
      return this.klass;
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
      var0[0] = "getConstructor";
      var0[1] = "getConstructor";
      var0[2] = "getConstructor";
      var0[3] = "log";
      var0[4] = "getLogger";
      var0[5] = "INFO";
      var0[6] = "log";
      var0[7] = "getLogger";
      var0[8] = "SEVERE";
      var0[9] = "newInstance";
      var0[10] = "newInstance";
      var0[11] = "newInstance";
      var0[12] = "newInstance";
      var0[13] = "isAssignableFrom";
      var0[14] = "getClass";
      var0[15] = "<$constructor$>";
      var0[16] = "name";
      var0[17] = "<$constructor$>";
      var0[18] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[19];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$RichActionWidgetFactory(), var0);
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
   private static Class $get$$class$groovy$swing$factory$RichActionWidgetFactory() {
      Class var10000 = $class$groovy$swing$factory$RichActionWidgetFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RichActionWidgetFactory = class$("groovy.swing.factory.RichActionWidgetFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$reflect$Constructor() {
      Class var10000 = $class$java$lang$reflect$Constructor;
      if (var10000 == null) {
         var10000 = $class$java$lang$reflect$Constructor = class$("java.lang.reflect.Constructor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$Action() {
      Class var10000 = $class$javax$swing$Action;
      if (var10000 == null) {
         var10000 = $class$javax$swing$Action = class$("javax.swing.Action");
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$Logger() {
      Class var10000 = $class$java$util$logging$Logger;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Logger = class$("java.util.logging.Logger");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$Icon() {
      Class var10000 = $class$javax$swing$Icon;
      if (var10000 == null) {
         var10000 = $class$javax$swing$Icon = class$("javax.swing.Icon");
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
   private static Class $get$array$$class$java$lang$Class() {
      Class var10000 = array$$class$java$lang$Class;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$Class = class$("[Ljava.lang.Class;");
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
   private static Class $get$$class$java$util$logging$Level() {
      Class var10000 = $class$java$util$logging$Level;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Level = class$("java.util.logging.Level");
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
