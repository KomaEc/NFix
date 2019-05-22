package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.swing.impl.DefaultAction;
import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ActionFactory extends AbstractFactory implements GroovyObject {
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
   public static Long __timeStamp = (Long)1292524204377L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204377 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$JComponent;
   // $FF: synthetic field
   private static Class $class$javax$swing$Action;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$impl$DefaultAction;
   // $FF: synthetic field
   private static Class $class$java$lang$Character;
   // $FF: synthetic field
   private static Class $class$groovy$util$FactoryBuilderSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$javax$swing$KeyStroke;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ActionFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$StringBuffer;

   public ActionFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public boolean isHandlesNodeChildren() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      CallSite[] var5 = $getCallSiteArray();
      Action action = (Action)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$Action());
      Object action;
      if (DefaultTypeTransformation.booleanUnbox(var5[0].call($get$$class$groovy$util$FactoryBuilderSupport(), value, name, $get$$class$javax$swing$Action()))) {
         action = (Action)ScriptBytecodeAdapter.castToType(value, $get$$class$javax$swing$Action());
      } else if (var5[1].call(attributes, (Object)name) instanceof Action) {
         action = (Action)ScriptBytecodeAdapter.castToType(var5[2].call(attributes, (Object)name), $get$$class$javax$swing$Action());
      } else {
         action = var5[3].callConstructor($get$$class$groovy$swing$impl$DefaultAction());
      }

      return (Object)ScriptBytecodeAdapter.castToType(action, $get$$class$java$lang$Object());
   }

   public boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object action, Map attributes) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var4[4].call(attributes, (Object)"closure") instanceof Closure && action instanceof DefaultAction ? Boolean.TRUE : Boolean.FALSE)) {
         Closure closure = (Closure)ScriptBytecodeAdapter.castToType(var4[5].call(attributes, (Object)"closure"), $get$$class$groovy$lang$Closure());
         var4[6].call((DefaultAction)ScriptBytecodeAdapter.castToType(action, $get$$class$groovy$swing$impl$DefaultAction()), (Object)closure);
      }

      Object accel = var4[7].call(attributes, (Object)"accelerator");
      if (ScriptBytecodeAdapter.compareNotEqual(accel, (Object)null)) {
         KeyStroke stroke = (KeyStroke)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$KeyStroke());
         if (accel instanceof KeyStroke) {
            stroke = (KeyStroke)ScriptBytecodeAdapter.castToType(accel, $get$$class$javax$swing$KeyStroke());
         } else {
            stroke = (KeyStroke)ScriptBytecodeAdapter.castToType(var4[8].call($get$$class$javax$swing$KeyStroke(), (Object)var4[9].call(accel)), $get$$class$javax$swing$KeyStroke());
         }

         var4[10].call(action, var4[11].callGetProperty($get$$class$javax$swing$Action()), stroke);
      }

      Object mnemonic = var4[12].call(attributes, (Object)"mnemonic");
      if (ScriptBytecodeAdapter.compareNotEqual(mnemonic, (Object)null)) {
         if (!(mnemonic instanceof Number)) {
            mnemonic = var4[13].call(var4[14].call(mnemonic), (Object)$const$0);
         }

         var4[15].call(action, var4[16].callGetProperty($get$$class$javax$swing$Action()), ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(mnemonic, $get$$class$java$lang$Integer()), $get$$class$java$lang$Integer()));
      }

      Object entry = null;
      Object var8 = var4[17].call(var4[18].call(attributes));

      while(((Iterator)var8).hasNext()) {
         entry = ((Iterator)var8).next();
         String propertyName = (String)ScriptBytecodeAdapter.castToType(var4[19].call(entry), $get$$class$java$lang$String());

         try {
            var4[20].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), action, propertyName, var4[21].call(entry));
         } catch (MissingPropertyException var13) {
            propertyName = (String)ScriptBytecodeAdapter.castToType(var4[22].callCurrent(this, (Object)propertyName), $get$$class$java$lang$String());
            var4[23].call(action, propertyName, var4[24].call(entry));
         } finally {
            ;
         }
      }

      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public boolean onNodeChildren(FactoryBuilderSupport builder, Object node, Closure childContent) {
      CallSite[] var4 = $getCallSiteArray();
      if (!(node instanceof DefaultAction)) {
         throw (Throwable)var4[25].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{var4[26].callGroovyObjectGetProperty(builder)}, new String[]{"", " only accepts a closure content when the action is generated by the node"})));
      } else if (ScriptBytecodeAdapter.compareNotEqual(var4[27].callGetProperty(node), (Object)null)) {
         throw (Throwable)var4[28].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{var4[29].callGroovyObjectGetProperty(builder)}, new String[]{"", " already has an action set via the closure attribute, child content as action not allowed"})));
      } else {
         ScriptBytecodeAdapter.setProperty(childContent, $get$$class$groovy$swing$factory$ActionFactory(), node, "closure");
         return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.FALSE, $get$$class$java$lang$Boolean()));
      }
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object action) {
      CallSite[] var4 = $getCallSiteArray();

      try {
         var4[30].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), parent, "action", action);
      } catch (RuntimeException var12) {
      } finally {
         ;
      }

      Object keyStroke = var4[31].call(action, (Object)"KeyStroke");
      if (parent instanceof JComponent) {
         JComponent component = (JComponent)ScriptBytecodeAdapter.castToType(parent, $get$$class$javax$swing$JComponent());
         KeyStroke stroke = (KeyStroke)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$KeyStroke());
         if (keyStroke instanceof GString) {
            keyStroke = (String)ScriptBytecodeAdapter.asType(keyStroke, $get$$class$java$lang$String());
         }

         if (keyStroke instanceof String) {
            stroke = (KeyStroke)ScriptBytecodeAdapter.castToType(var4[32].call($get$$class$javax$swing$KeyStroke(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(keyStroke, $get$$class$java$lang$String()), $get$$class$java$lang$String())), $get$$class$javax$swing$KeyStroke());
         } else if (keyStroke instanceof KeyStroke) {
            stroke = (KeyStroke)ScriptBytecodeAdapter.castToType(keyStroke, $get$$class$javax$swing$KeyStroke());
         }

         if (ScriptBytecodeAdapter.compareNotEqual(stroke, (Object)null)) {
            String key = (String)ScriptBytecodeAdapter.castToType(var4[33].call(action), $get$$class$java$lang$String());
            var4[34].call(var4[35].call(component), stroke, key);
            var4[36].call(var4[37].call(component), key, action);
         }
      }

   }

   public String capitalize(String text) {
      CallSite[] var2 = $getCallSiteArray();
      Character ch = (Character)ScriptBytecodeAdapter.castToType(var2[38].call(text, (Object)$const$0), $get$$class$java$lang$Character());
      if (DefaultTypeTransformation.booleanUnbox(var2[39].call($get$$class$java$lang$Character(), (Object)ch))) {
         return (String)ScriptBytecodeAdapter.castToType(text, $get$$class$java$lang$String());
      } else {
         StringBuffer buffer = var2[40].callConstructor($get$$class$java$lang$StringBuffer(), (Object)var2[41].call(text));
         var2[42].call(buffer, var2[43].call($get$$class$java$lang$Character(), (Object)ch));
         var2[44].call(buffer, var2[45].call(text, (Object)$const$1));
         return (String)ScriptBytecodeAdapter.castToType(var2[46].call(buffer), $get$$class$java$lang$String());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$ActionFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$ActionFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$ActionFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$ActionFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "checkValueIsTypeNotString";
      var0[1] = "get";
      var0[2] = "remove";
      var0[3] = "<$constructor$>";
      var0[4] = "get";
      var0[5] = "remove";
      var0[6] = "setClosure";
      var0[7] = "remove";
      var0[8] = "getKeyStroke";
      var0[9] = "toString";
      var0[10] = "putValue";
      var0[11] = "ACCELERATOR_KEY";
      var0[12] = "remove";
      var0[13] = "charAt";
      var0[14] = "toString";
      var0[15] = "putValue";
      var0[16] = "MNEMONIC_KEY";
      var0[17] = "iterator";
      var0[18] = "entrySet";
      var0[19] = "getKey";
      var0[20] = "setProperty";
      var0[21] = "getValue";
      var0[22] = "capitalize";
      var0[23] = "putValue";
      var0[24] = "getValue";
      var0[25] = "<$constructor$>";
      var0[26] = "currentName";
      var0[27] = "closure";
      var0[28] = "<$constructor$>";
      var0[29] = "currentName";
      var0[30] = "setProperty";
      var0[31] = "getValue";
      var0[32] = "getKeyStroke";
      var0[33] = "toString";
      var0[34] = "put";
      var0[35] = "getInputMap";
      var0[36] = "put";
      var0[37] = "getActionMap";
      var0[38] = "charAt";
      var0[39] = "isUpperCase";
      var0[40] = "<$constructor$>";
      var0[41] = "length";
      var0[42] = "append";
      var0[43] = "toUpperCase";
      var0[44] = "append";
      var0[45] = "substring";
      var0[46] = "toString";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[47];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$ActionFactory(), var0);
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
   private static Class $get$$class$javax$swing$JComponent() {
      Class var10000 = $class$javax$swing$JComponent;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JComponent = class$("javax.swing.JComponent");
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
   private static Class $get$$class$groovy$swing$impl$DefaultAction() {
      Class var10000 = $class$groovy$swing$impl$DefaultAction;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$impl$DefaultAction = class$("groovy.swing.impl.DefaultAction");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Character() {
      Class var10000 = $class$java$lang$Character;
      if (var10000 == null) {
         var10000 = $class$java$lang$Character = class$("java.lang.Character");
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
   private static Class $get$$class$javax$swing$KeyStroke() {
      Class var10000 = $class$javax$swing$KeyStroke;
      if (var10000 == null) {
         var10000 = $class$javax$swing$KeyStroke = class$("javax.swing.KeyStroke");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Integer() {
      Class var10000 = $class$java$lang$Integer;
      if (var10000 == null) {
         var10000 = $class$java$lang$Integer = class$("java.lang.Integer");
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
   private static Class $get$$class$groovy$swing$factory$ActionFactory() {
      Class var10000 = $class$groovy$swing$factory$ActionFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ActionFactory = class$("groovy.swing.factory.ActionFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
      Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$StringBuffer() {
      Class var10000 = $class$java$lang$StringBuffer;
      if (var10000 == null) {
         var10000 = $class$java$lang$StringBuffer = class$("java.lang.StringBuffer");
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
