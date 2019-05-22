package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class MacOSXDefaults extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205385L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205385 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$ui$text$GroovyFilter;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleConstants;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$MacOSXMenuBar;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$Defaults;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$MacOSXDefaults;
   // $FF: synthetic field
   private static Class $class$java$awt$Color;

   public MacOSXDefaults() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public MacOSXDefaults(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$MacOSXDefaults(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      var1[1].callCurrent(this, (Object)$get$$class$groovy$ui$view$Defaults());
      var1[2].call($get$$class$java$lang$System(), "apple.laf.useScreenMenuBar", "true");
      var1[3].call($get$$class$java$lang$System(), "com.apple.mrj.application.apple.menu.about.name", "GroovyConsole");
      ScriptBytecodeAdapter.setGroovyObjectProperty(ScriptBytecodeAdapter.createMap(new Object[]{"regular", ScriptBytecodeAdapter.createMap(new Object[]{var1[4].callGetProperty($get$$class$javax$swing$text$StyleConstants()), "Monaco"}), "prompt", ScriptBytecodeAdapter.createMap(new Object[]{var1[5].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[6].callGetProperty($get$$class$java$awt$Color())}), "command", ScriptBytecodeAdapter.createMap(new Object[]{var1[7].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[8].callGetProperty($get$$class$java$awt$Color())}), "stacktrace", ScriptBytecodeAdapter.createMap(new Object[]{var1[9].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[10].call(var1[11].callGetProperty($get$$class$java$awt$Color()))}), "hyperlink", ScriptBytecodeAdapter.createMap(new Object[]{var1[12].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[13].callGetProperty($get$$class$java$awt$Color()), var1[14].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), "output", ScriptBytecodeAdapter.createMap(new Object[0]), "result", ScriptBytecodeAdapter.createMap(new Object[]{var1[15].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[16].callGetProperty($get$$class$java$awt$Color()), var1[17].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[18].callGetProperty($get$$class$java$awt$Color())}), var1[19].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[20].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[21].call(var1[22].call(var1[23].callGetProperty($get$$class$java$awt$Color()))), var1[24].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), var1[25].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[26].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[27].call(var1[28].call(var1[29].callGetProperty($get$$class$java$awt$Color())))}), var1[30].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[31].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[32].call(var1[33].call(var1[34].callGetProperty($get$$class$java$awt$Color())))}), var1[35].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[36].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[37].call(var1[38].callGetProperty($get$$class$java$awt$Color()))}), var1[39].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[40].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[41].call(var1[42].callGetProperty($get$$class$java$awt$Color()))}), var1[43].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[44].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), var1[45].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[0]), var1[46].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[47].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE, var1[48].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[49].call(var1[50].call(var1[51].callGetProperty($get$$class$java$awt$Color())))})}), $get$$class$groovy$ui$view$MacOSXDefaults(), this, "styles");
      Class var10000 = $get$$class$groovy$ui$view$MacOSXMenuBar();
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$MacOSXDefaults(), this, "menuBarClass");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$MacOSXDefaults()) {
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
      Class var10000 = $get$$class$groovy$ui$view$MacOSXDefaults();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$MacOSXDefaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$MacOSXDefaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[2] = "setProperty";
      var0[3] = "setProperty";
      var0[4] = "FontFamily";
      var0[5] = "Foreground";
      var0[6] = "LIGHT_GRAY";
      var0[7] = "Foreground";
      var0[8] = "GRAY";
      var0[9] = "Foreground";
      var0[10] = "darker";
      var0[11] = "RED";
      var0[12] = "Foreground";
      var0[13] = "BLUE";
      var0[14] = "Underline";
      var0[15] = "Foreground";
      var0[16] = "WHITE";
      var0[17] = "Background";
      var0[18] = "BLACK";
      var0[19] = "COMMENT";
      var0[20] = "Foreground";
      var0[21] = "darker";
      var0[22] = "darker";
      var0[23] = "LIGHT_GRAY";
      var0[24] = "Italic";
      var0[25] = "QUOTES";
      var0[26] = "Foreground";
      var0[27] = "darker";
      var0[28] = "darker";
      var0[29] = "MAGENTA";
      var0[30] = "SINGLE_QUOTES";
      var0[31] = "Foreground";
      var0[32] = "darker";
      var0[33] = "darker";
      var0[34] = "GREEN";
      var0[35] = "SLASHY_QUOTES";
      var0[36] = "Foreground";
      var0[37] = "darker";
      var0[38] = "ORANGE";
      var0[39] = "DIGIT";
      var0[40] = "Foreground";
      var0[41] = "darker";
      var0[42] = "RED";
      var0[43] = "OPERATION";
      var0[44] = "Bold";
      var0[45] = "IDENT";
      var0[46] = "RESERVED_WORD";
      var0[47] = "Bold";
      var0[48] = "Foreground";
      var0[49] = "darker";
      var0[50] = "darker";
      var0[51] = "BLUE";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[52];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$MacOSXDefaults(), var0);
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
   private static Class $get$$class$groovy$ui$text$GroovyFilter() {
      Class var10000 = $class$groovy$ui$text$GroovyFilter;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$text$GroovyFilter = class$("groovy.ui.text.GroovyFilter");
      }

      return var10000;
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
   private static Class $get$$class$groovy$ui$view$MacOSXMenuBar() {
      Class var10000 = $class$groovy$ui$view$MacOSXMenuBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$MacOSXMenuBar = class$("groovy.ui.view.MacOSXMenuBar");
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$MacOSXDefaults() {
      Class var10000 = $class$groovy$ui$view$MacOSXDefaults;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$MacOSXDefaults = class$("groovy.ui.view.MacOSXDefaults");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Color() {
      Class var10000 = $class$java$awt$Color;
      if (var10000 == null) {
         var10000 = $class$java$awt$Color = class$("java.awt.Color");
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
