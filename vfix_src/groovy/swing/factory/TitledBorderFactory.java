package groovy.swing.factory;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.util.FactoryBuilderSupport;
import java.awt.Color;
import java.awt.Font;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class TitledBorderFactory extends SwingBorderFactory {
   private static final Map positions = (Map)ScriptBytecodeAdapter.createMap(new Object[]{"default", $getCallSiteArray()[18].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "aboveTop", $getCallSiteArray()[19].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "top", $getCallSiteArray()[20].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "belowTop", $getCallSiteArray()[21].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "aboveBottom", $getCallSiteArray()[22].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "bottom", $getCallSiteArray()[23].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "belowBottom", $getCallSiteArray()[24].callGetProperty($get$$class$javax$swing$border$TitledBorder())});
   private static final Map justifications = (Map)ScriptBytecodeAdapter.createMap(new Object[]{"default", $getCallSiteArray()[25].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "left", $getCallSiteArray()[26].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "center", $getCallSiteArray()[27].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "right", $getCallSiteArray()[28].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "leading", $getCallSiteArray()[29].callGetProperty($get$$class$javax$swing$border$TitledBorder()), "trailing", $getCallSiteArray()[30].callGetProperty($get$$class$javax$swing$border$TitledBorder())});
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205050L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205050 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$awt$Font;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TitledBorderFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$Border;
   // $FF: synthetic field
   private static Class $class$java$awt$Color;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$TitledBorder;

   public TitledBorderFactory() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      CallSite[] var5 = $getCallSiteArray();
      ScriptBytecodeAdapter.setProperty(var5[0].call(attributes, (Object)"parent"), $get$$class$groovy$swing$factory$TitledBorderFactory(), var5[1].callGroovyObjectGetProperty(builder), "applyBorderToParent");
      String title = (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      if (DefaultTypeTransformation.booleanUnbox(value)) {
         title = (String)ScriptBytecodeAdapter.asType(value, $get$$class$java$lang$String());
         if (DefaultTypeTransformation.booleanUnbox(var5[2].call(attributes, (Object)"title"))) {
            throw (Throwable)var5[3].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{name}, new String[]{"", " cannot have both a value attribute and an attribute title:"})));
         }
      } else {
         title = (String)ScriptBytecodeAdapter.asType(var5[4].call(attributes, (Object)"title"), $get$$class$java$lang$String());
      }

      TitledBorder border = var5[5].callConstructor($get$$class$javax$swing$border$TitledBorder(), (Object)title);
      Object position = var5[6].call(attributes, (Object)"position");
      Object var10000 = var5[7].call(positions, (Object)position);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = position;
      }

      position = var10000;
      if (position instanceof Integer) {
         var5[8].call(border, position);
      }

      Object justification = var5[9].call(attributes, (Object)"justification");
      var10000 = var5[10].call(justifications, (Object)justification);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = justification;
      }

      justification = var10000;
      if (justification instanceof Integer) {
         var5[11].call(border, justification);
      }

      Border otherBorder = (Border)ScriptBytecodeAdapter.castToType(var5[12].call(attributes, (Object)"border"), $get$$class$javax$swing$border$Border());
      if (ScriptBytecodeAdapter.compareNotEqual(otherBorder, (Object)null)) {
         var5[13].call(border, (Object)otherBorder);
      }

      Color color = (Color)ScriptBytecodeAdapter.castToType(var5[14].call(attributes, (Object)"color"), $get$$class$java$awt$Color());
      if (DefaultTypeTransformation.booleanUnbox(color)) {
         var5[15].call(border, (Object)color);
      }

      Font font = (Font)ScriptBytecodeAdapter.castToType(var5[16].call(attributes, (Object)"font"), $get$$class$java$awt$Font());
      if (DefaultTypeTransformation.booleanUnbox(font)) {
         var5[17].call(border, (Object)font);
      }

      return (Object)ScriptBytecodeAdapter.castToType(border, $get$$class$java$lang$Object());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$factory$TitledBorderFactory()) {
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
      Class var10000 = $get$$class$groovy$swing$factory$TitledBorderFactory();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$factory$TitledBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$factory$TitledBorderFactory(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public static final Map getPositions() {
      return positions;
   }

   public static final Map getJustifications() {
      return justifications;
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
      var0[3] = "<$constructor$>";
      var0[4] = "remove";
      var0[5] = "<$constructor$>";
      var0[6] = "remove";
      var0[7] = "getAt";
      var0[8] = "setTitlePosition";
      var0[9] = "remove";
      var0[10] = "getAt";
      var0[11] = "setTitleJustification";
      var0[12] = "remove";
      var0[13] = "setBorder";
      var0[14] = "remove";
      var0[15] = "setTitleColor";
      var0[16] = "remove";
      var0[17] = "setTitleFont";
      var0[18] = "DEFAULT_POSITION";
      var0[19] = "ABOVE_TOP";
      var0[20] = "TOP";
      var0[21] = "BELOW_TOP";
      var0[22] = "ABOVE_BOTTOM";
      var0[23] = "BOTTOM";
      var0[24] = "BELOW_BOTTOM";
      var0[25] = "DEFAULT_JUSTIFICATION";
      var0[26] = "LEFT";
      var0[27] = "CENTER";
      var0[28] = "RIGHT";
      var0[29] = "LEADING";
      var0[30] = "TRAILING";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[31];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$factory$TitledBorderFactory(), var0);
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
   private static Class $get$$class$java$awt$Font() {
      Class var10000 = $class$java$awt$Font;
      if (var10000 == null) {
         var10000 = $class$java$awt$Font = class$("java.awt.Font");
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
   private static Class $get$$class$groovy$swing$factory$TitledBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$TitledBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TitledBorderFactory = class$("groovy.swing.factory.TitledBorderFactory");
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
   private static Class $get$$class$javax$swing$border$Border() {
      Class var10000 = $class$javax$swing$border$Border;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$Border = class$("javax.swing.border.Border");
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
   private static Class $get$$class$javax$swing$border$TitledBorder() {
      Class var10000 = $class$javax$swing$border$TitledBorder;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$TitledBorder = class$("javax.swing.border.TitledBorder");
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
