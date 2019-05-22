package groovy.ui.view;

import groovy.lang.Binding;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Defaults extends Script {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)128;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205375L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205375 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicToolBar;
   // $FF: synthetic field
   private static Class $class$groovy$ui$text$GroovyFilter;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleConstants;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicStatusBar;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicMenuBar;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$Defaults;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$ui$view$BasicContentPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$text$StyleContext;
   // $FF: synthetic field
   private static Class $class$java$awt$Color;

   public Defaults() {
      CallSite[] var1 = $getCallSiteArray();
   }

   public Defaults(Binding context) {
      CallSite[] var2 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$Script(), this, "setBinding", new Object[]{context});
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      var1[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), $get$$class$groovy$ui$view$Defaults(), args);
   }

   public Object run() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectProperty($get$$class$groovy$ui$view$BasicMenuBar(), $get$$class$groovy$ui$view$Defaults(), this, "menuBarClass");
      ScriptBytecodeAdapter.setGroovyObjectProperty($get$$class$groovy$ui$view$BasicContentPane(), $get$$class$groovy$ui$view$Defaults(), this, "contentPaneClass");
      ScriptBytecodeAdapter.setGroovyObjectProperty($get$$class$groovy$ui$view$BasicToolBar(), $get$$class$groovy$ui$view$Defaults(), this, "toolBarClass");
      ScriptBytecodeAdapter.setGroovyObjectProperty($get$$class$groovy$ui$view$BasicStatusBar(), $get$$class$groovy$ui$view$Defaults(), this, "statusBarClass");
      Map var10000 = ScriptBytecodeAdapter.createMap(new Object[]{"regular", ScriptBytecodeAdapter.createMap(new Object[]{var1[1].callGetProperty($get$$class$javax$swing$text$StyleConstants()), "Monospaced"}), "prompt", ScriptBytecodeAdapter.createMap(new Object[]{var1[2].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[3].callConstructor($get$$class$java$awt$Color(), $const$0, $const$1, $const$0)}), "command", ScriptBytecodeAdapter.createMap(new Object[]{var1[4].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[5].callGetProperty($get$$class$java$awt$Color())}), "stacktrace", ScriptBytecodeAdapter.createMap(new Object[]{var1[6].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[7].call(var1[8].callGetProperty($get$$class$java$awt$Color()))}), "hyperlink", ScriptBytecodeAdapter.createMap(new Object[]{var1[9].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[10].callGetProperty($get$$class$java$awt$Color()), var1[11].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), "output", ScriptBytecodeAdapter.createMap(new Object[0]), "result", ScriptBytecodeAdapter.createMap(new Object[]{var1[12].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[13].callGetProperty($get$$class$java$awt$Color()), var1[14].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[15].callGetProperty($get$$class$java$awt$Color())}), var1[16].callGetProperty($get$$class$javax$swing$text$StyleContext()), ScriptBytecodeAdapter.createMap(new Object[]{var1[17].callGetProperty($get$$class$javax$swing$text$StyleConstants()), "Monospaced"}), var1[18].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[19].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[20].call(var1[21].call(var1[22].callGetProperty($get$$class$java$awt$Color()))), var1[23].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), var1[24].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[25].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[26].call(var1[27].call(var1[28].callGetProperty($get$$class$java$awt$Color())))}), var1[29].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[30].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[31].call(var1[32].call(var1[33].callGetProperty($get$$class$java$awt$Color())))}), var1[34].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[35].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[36].call(var1[37].callGetProperty($get$$class$java$awt$Color()))}), var1[38].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[39].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[40].call(var1[41].callGetProperty($get$$class$java$awt$Color()))}), var1[42].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[43].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE}), var1[44].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[0]), var1[45].callGetProperty($get$$class$groovy$ui$text$GroovyFilter()), ScriptBytecodeAdapter.createMap(new Object[]{var1[46].callGetProperty($get$$class$javax$swing$text$StyleConstants()), Boolean.TRUE, var1[47].callGetProperty($get$$class$javax$swing$text$StyleConstants()), var1[48].call(var1[49].call(var1[50].callGetProperty($get$$class$java$awt$Color())))})});
      ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$groovy$ui$view$Defaults(), this, "styles");
      return var10000;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$view$Defaults()) {
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
      Class var10000 = $get$$class$groovy$ui$view$Defaults();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$view$Defaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$view$Defaults(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "FontFamily";
      var0[2] = "Foreground";
      var0[3] = "<$constructor$>";
      var0[4] = "Foreground";
      var0[5] = "BLUE";
      var0[6] = "Foreground";
      var0[7] = "darker";
      var0[8] = "RED";
      var0[9] = "Foreground";
      var0[10] = "BLUE";
      var0[11] = "Underline";
      var0[12] = "Foreground";
      var0[13] = "BLUE";
      var0[14] = "Background";
      var0[15] = "YELLOW";
      var0[16] = "DEFAULT_STYLE";
      var0[17] = "FontFamily";
      var0[18] = "COMMENT";
      var0[19] = "Foreground";
      var0[20] = "darker";
      var0[21] = "darker";
      var0[22] = "LIGHT_GRAY";
      var0[23] = "Italic";
      var0[24] = "QUOTES";
      var0[25] = "Foreground";
      var0[26] = "darker";
      var0[27] = "darker";
      var0[28] = "MAGENTA";
      var0[29] = "SINGLE_QUOTES";
      var0[30] = "Foreground";
      var0[31] = "darker";
      var0[32] = "darker";
      var0[33] = "GREEN";
      var0[34] = "SLASHY_QUOTES";
      var0[35] = "Foreground";
      var0[36] = "darker";
      var0[37] = "ORANGE";
      var0[38] = "DIGIT";
      var0[39] = "Foreground";
      var0[40] = "darker";
      var0[41] = "RED";
      var0[42] = "OPERATION";
      var0[43] = "Bold";
      var0[44] = "IDENT";
      var0[45] = "RESERVED_WORD";
      var0[46] = "Bold";
      var0[47] = "Foreground";
      var0[48] = "darker";
      var0[49] = "darker";
      var0[50] = "BLUE";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[51];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$view$Defaults(), var0);
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
   private static Class $get$$class$groovy$ui$view$BasicToolBar() {
      Class var10000 = $class$groovy$ui$view$BasicToolBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicToolBar = class$("groovy.ui.view.BasicToolBar");
      }

      return var10000;
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
   private static Class $get$$class$groovy$ui$view$BasicStatusBar() {
      Class var10000 = $class$groovy$ui$view$BasicStatusBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicStatusBar = class$("groovy.ui.view.BasicStatusBar");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$view$BasicMenuBar() {
      Class var10000 = $class$groovy$ui$view$BasicMenuBar;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicMenuBar = class$("groovy.ui.view.BasicMenuBar");
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
   private static Class $get$$class$groovy$ui$view$BasicContentPane() {
      Class var10000 = $class$groovy$ui$view$BasicContentPane;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$view$BasicContentPane = class$("groovy.ui.view.BasicContentPane");
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
