package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import javax.swing.JComponent;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class WindowsDefaults extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205398L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205398 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleConstants;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$Defaults;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$WindowsDefaults;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleContext;

   public WindowsDefaults() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public WindowsDefaults(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$WindowsDefaults(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      var1[1].callCurrent(this, (Object)$get$$class$groovy$ui$view$Defaults());
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.findRegex(var1[2].call(var1[3].callGetProperty($get$$class$java$lang$System()), (Object)"os.version"), "6\\."))) {
         CallSite var10000 = var1[4];
         Object var10001 = var1[5].callGetProperty(var1[6].callGroovyObjectGetProperty(this));
         Object var10002 = var1[7].callGetProperty($get$$class$javax$swing$text$StyleConstants());
         String var2 = "Consolas";
         var10000.call(var10001, var10002, var2);
         var10000 = var1[8];
         var10001 = var1[9].call(var1[10].callGroovyObjectGetProperty(this), var1[11].callGetProperty($get$$class$javax$swing$text$StyleContext()));
         var10002 = var1[12].callGetProperty($get$$class$javax$swing$text$StyleConstants());
         var2 = "Consolas";
         var10000.call(var10001, var10002, var2);
         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.findRegex(var1[13].call(var1[14].callGetProperty($get$$class$java$lang$System()), (Object)"java.version"), "^1\\.5"))) {
            ScriptBytecodeAdapter.setGroovyObjectProperty(var1[15].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), ScriptBytecodeAdapter.createPojoWrapper((Class)ScriptBytecodeAdapter.asType("com.sun.java.swing.SwingUtilities2", $get$$class$java$lang$Class()), $get$$class$java$lang$Class()), "AA_TEXT_PROPERTY_KEY"), $get$$class$groovy$ui$view$WindowsDefaults(), this, "key");
            return var1[16].callCurrent(this, (Object)(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$ui$view$WindowsDefaults$_run_closure1;
               // $FF: synthetic field
               private static Class $class$java$lang$Boolean;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object builder, Object node, Object attributes) {
                  Object nodex = new Reference(node);
                  CallSite[] var5 = $getCallSiteArray();
                  return nodex.get() instanceof JComponent ? var5[0].call(nodex.get(), var5[1].callGroovyObjectGetProperty(this), var5[2].callConstructor($get$$class$java$lang$Boolean(), (Object)Boolean.TRUE)) : null;
               }

               public Object call(Object builder, Object node, Object attributes) {
                  Object nodex = new Reference(node);
                  CallSite[] var5 = $getCallSiteArray();
                  return var5[3].callCurrent(this, builder, nodex.get(), attributes);
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$ui$view$WindowsDefaults$_run_closure1()) {
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
                  var0[0] = "putClientProperty";
                  var0[1] = "key";
                  var0[2] = "<$constructor$>";
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$ui$view$WindowsDefaults$_run_closure1(), var0);
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
               private static Class $get$$class$groovy$ui$view$WindowsDefaults$_run_closure1() {
                  Class var10000 = $class$groovy$ui$view$WindowsDefaults$_run_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$ui$view$WindowsDefaults$_run_closure1 = class$("groovy.ui.view.WindowsDefaults$_run_closure1");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$WindowsDefaults()) {
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
      Class var10000 = $get$$class$groovy$ui$view$WindowsDefaults();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$WindowsDefaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$WindowsDefaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object super$3$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$3$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$3$println() {
      super.println();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$3$print(Object var1) {
      super.print(var1);
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object[] var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public Object super$3$evaluate(String var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Binding super$3$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$3$printf(String var1, Object var2) {
      super.printf(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setBinding(Binding var1) {
      super.setBinding(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$3$run(File var1, String[] var2) {
      super.run(var1, var2);
   }

   // $FF: synthetic method
   public Object super$3$evaluate(File var1) {
      return super.evaluate(var1);
   }

   // $FF: synthetic method
   public void super$3$println(Object var1) {
      super.println(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$3$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "runScript";
      var0[1] = "build";
      var0[2] = "getAt";
      var0[3] = "properties";
      var0[4] = "putAt";
      var0[5] = "regular";
      var0[6] = "styles";
      var0[7] = "FontFamily";
      var0[8] = "putAt";
      var0[9] = "getAt";
      var0[10] = "styles";
      var0[11] = "DEFAULT_STYLE";
      var0[12] = "FontFamily";
      var0[13] = "getAt";
      var0[14] = "properties";
      var0[15] = "getProperty";
      var0[16] = "addAttributeDelegate";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[17];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$WindowsDefaults(), var0);
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
   private static Class $get$$class$javax$swing$text$StyleConstants() {
      Class var10000 = $class$javax$swing$text$StyleConstants;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$StyleConstants = class$("javax.swing.text.StyleConstants");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
   private static Class $get$$class$groovy$ui$view$Defaults() {
      Class var10000 = $class$groovy$ui$view$Defaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$Defaults = class$("groovy.ui.view.Defaults");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$WindowsDefaults() {
      Class var10000 = $class$groovy$ui$view$WindowsDefaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$WindowsDefaults = class$("groovy.ui.view.WindowsDefaults");
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
   private static Class $get$$class$javax$swing$text$StyleContext() {
      Class var10000 = $class$javax$swing$text$StyleContext;
      if (var10000 == null) {
         var10000 = $class$javax$swing$text$StyleContext = class$("javax.swing.text.StyleContext");
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
