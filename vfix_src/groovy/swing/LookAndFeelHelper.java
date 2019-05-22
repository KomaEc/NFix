package groovy.swing;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import javax.swing.LookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class LookAndFeelHelper implements GroovyObject {
   protected static LookAndFeelHelper instance;
   private Map lafCodeNames;
   private Map extendedAttributes;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202985L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202985 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$LookAndFeel;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$groovy$swing$LookAndFeelHelper;
   // $FF: synthetic field
   private static Class $class$javax$swing$UIManager;
   // $FF: synthetic field
   private static Class $class$groovy$util$FactoryBuilderSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$util$Map;

   private LookAndFeelHelper() {
      CallSite[] var1 = $getCallSiteArray();
      this.lafCodeNames = (Map)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createMap(new Object[]{"metal", "javax.swing.plaf.metal.MetalLookAndFeel", "nimbus", var1[0].callStatic($get$$class$groovy$swing$LookAndFeelHelper()), "mac", var1[1].callStatic($get$$class$groovy$swing$LookAndFeelHelper()), "motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel", "windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "win2k", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel", "gtk", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel", "synth", "javax.swing.plaf.synth.SynthLookAndFeel", "system", var1[2].call($get$$class$javax$swing$UIManager()), "crossPlatform", var1[3].call($get$$class$javax$swing$UIManager()), "plastic", "com.jgoodies.looks.plastic.PlasticLookAndFeel", "plastic3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel", "plasticXP", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel", "substance", var1[4].callStatic($get$$class$groovy$swing$LookAndFeelHelper()), "napkin", "net.sourceforge.napkinlaf.NapkinLookAndFeel"}), $get$$class$java$util$Map());
      this.extendedAttributes = (Map)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createMap(new Object[]{"javax.swing.plaf.metal.MetalLookAndFeel", ScriptBytecodeAdapter.createMap(new Object[]{"theme", new LookAndFeelHelper._closure1(this, this), "boldFonts", new LookAndFeelHelper._closure2(this, this), "noxp", new LookAndFeelHelper._closure3(this, this)}), "org.jvnet.substance.SubstanceLookAndFeel", ScriptBytecodeAdapter.createMap(new Object[]{"theme", new LookAndFeelHelper._closure4(this, this), "skin", new LookAndFeelHelper._closure5(this, this), "watermark", new LookAndFeelHelper._closure6(this, this)})}), $get$$class$java$util$Map());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      var1[5].call($get$$class$javax$swing$UIManager());
   }

   public static LookAndFeelHelper getInstance() {
      CallSite[] var0 = $getCallSiteArray();
      Object var10000 = instance;
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = var0[6].callConstructor($get$$class$groovy$swing$LookAndFeelHelper());
         instance = (LookAndFeelHelper)var10000;
      }

      return (LookAndFeelHelper)ScriptBytecodeAdapter.castToType(var10000, $get$$class$groovy$swing$LookAndFeelHelper());
   }

   public String addLookAndFeelAlias(String alias, String className) {
      CallSite[] var3 = $getCallSiteArray();
      var3[7].call(this.lafCodeNames, alias, className);
      return (String)ScriptBytecodeAdapter.castToType(className, $get$$class$java$lang$String());
   }

   public String addLookAndFeelAttributeHandler(String className, String attr, Closure handler) {
      CallSite[] var4 = $getCallSiteArray();
      Map attrs = (Map)ScriptBytecodeAdapter.castToType(var4[8].call(this.extendedAttributes, (Object)className), $get$$class$java$util$Map());
      if (ScriptBytecodeAdapter.compareEqual(attrs, (Object)null)) {
         attrs = ScriptBytecodeAdapter.createMap(new Object[0]);
         var4[9].call(this.extendedAttributes, className, attrs);
      }

      var4[10].call(attrs, attr, handler);
      return (String)ScriptBytecodeAdapter.castToType(handler, $get$$class$java$lang$String());
   }

   public boolean isLeaf() {
      CallSite[] var1 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(Boolean.TRUE, $get$$class$java$lang$Boolean()));
   }

   public LookAndFeel lookAndFeel(Object value, Map attributes, Closure initClosure) {
      CallSite[] var4 = $getCallSiteArray();
      LookAndFeel lafInstance = new Reference((LookAndFeel)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$LookAndFeel()));
      String lafClassName = new Reference((String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String()));
      if (DefaultTypeTransformation.booleanUnbox(value instanceof Closure && ScriptBytecodeAdapter.compareEqual(initClosure, (Object)null) ? Boolean.TRUE : Boolean.FALSE)) {
         initClosure = (Closure)ScriptBytecodeAdapter.castToType(value, $get$$class$groovy$lang$Closure());
         value = null;
      }

      if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         value = var4[11].call(attributes, (Object)"lookAndFeel");
      }

      if (value instanceof GString) {
         value = (String)ScriptBytecodeAdapter.asType(value, $get$$class$java$lang$String());
      }

      Object var10000;
      if (DefaultTypeTransformation.booleanUnbox(var4[12].call($get$$class$groovy$util$FactoryBuilderSupport(), value, "lookAndFeel", $get$$class$javax$swing$LookAndFeel()))) {
         lafInstance.set((LookAndFeel)ScriptBytecodeAdapter.castToType(value, $get$$class$javax$swing$LookAndFeel()));
         lafClassName.set((String)ScriptBytecodeAdapter.castToType(var4[13].callGetProperty(var4[14].callGetProperty(lafInstance.get())), $get$$class$java$lang$String()));
      } else if (ScriptBytecodeAdapter.compareNotEqual(value, (Object)null)) {
         var10000 = var4[15].call(this.lafCodeNames, (Object)value);
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = value;
         }

         lafClassName.set((String)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$String()));
         lafInstance.set((LookAndFeel)ScriptBytecodeAdapter.castToType(var4[16].call(var4[17].call($get$$class$java$lang$Class(), lafClassName.get(), Boolean.TRUE, var4[18].callGetProperty(var4[19].callCurrent(this)))), $get$$class$javax$swing$LookAndFeel()));
      }

      var10000 = var4[20].call(this.extendedAttributes, (Object)lafClassName.get());
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = ScriptBytecodeAdapter.createMap(new Object[0]);
      }

      Map possibleAttributes = new Reference((Map)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$util$Map()));
      var4[21].call(attributes, (Object)(new GeneratedClosure(this, this, lafInstance, possibleAttributes, lafClassName) {
         private Reference<T> lafInstance;
         private Reference<T> possibleAttributes;
         private Reference<T> lafClassName;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$javax$swing$LookAndFeel;
         // $FF: synthetic field
         private static Class $class$java$lang$RuntimeException;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$util$Map;
         // $FF: synthetic field
         private static Class $class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7;

         public {
            CallSite[] var6 = $getCallSiteArray();
            this.lafInstance = (Reference)lafInstance;
            this.possibleAttributes = (Reference)possibleAttributes;
            this.lafClassName = (Reference)lafClassName;
         }

         public Object doCall(Object param1, Object param2) {
            // $FF: Couldn't be decompiled
         }

         public Object call(Object k, Object v) {
            Object kx = new Reference(k);
            Object vx = new Reference(v);
            CallSite[] var5 = $getCallSiteArray();
            return var5[4].callCurrent(this, kx.get(), vx.get());
         }

         public LookAndFeel getLafInstance() {
            CallSite[] var1 = $getCallSiteArray();
            return (LookAndFeel)ScriptBytecodeAdapter.castToType(this.lafInstance.get(), $get$$class$javax$swing$LookAndFeel());
         }

         public Map getPossibleAttributes() {
            CallSite[] var1 = $getCallSiteArray();
            return (Map)ScriptBytecodeAdapter.castToType(this.possibleAttributes.get(), $get$$class$java$util$Map());
         }

         public String getLafClassName() {
            CallSite[] var1 = $getCallSiteArray();
            return (String)ScriptBytecodeAdapter.castToType(this.lafClassName.get(), $get$$class$java$lang$String());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7()) {
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
            var0[1] = "call";
            var0[2] = "getAt";
            var0[3] = "<$constructor$>";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7(), var0);
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
         private static Class $get$$class$javax$swing$LookAndFeel() {
            Class var10000 = $class$javax$swing$LookAndFeel;
            if (var10000 == null) {
               var10000 = $class$javax$swing$LookAndFeel = class$("javax.swing.LookAndFeel");
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
         private static Class $get$$class$java$util$Map() {
            Class var10000 = $class$java$util$Map;
            if (var10000 == null) {
               var10000 = $class$java$util$Map = class$("java.util.Map");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7() {
            Class var10000 = $class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$LookAndFeelHelper$_lookAndFeel_closure7 = class$("groovy.swing.LookAndFeelHelper$_lookAndFeel_closure7");
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
      if (DefaultTypeTransformation.booleanUnbox(initClosure)) {
         var4[22].call(initClosure, (Object)lafInstance.get());
      }

      var4[23].call($get$$class$javax$swing$UIManager(), (Object)lafInstance.get());
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(lafInstance.get(), $get$$class$javax$swing$LookAndFeel());
   }

   public static String getNimbusLAFName() {
      // $FF: Couldn't be decompiled
   }

   public static String getAquaLAFName() {
      // $FF: Couldn't be decompiled
   }

   public static String getSubstanceLAFName() {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper()) {
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
   public Object this$dist$invoke$2(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$LookAndFeelHelper();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$LookAndFeelHelper(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$LookAndFeelHelper(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void super$1$wait(long var1) {
      super.wait(var1);
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
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "getNimbusLAFName";
      var0[1] = "getAquaLAFName";
      var0[2] = "getSystemLookAndFeelClassName";
      var0[3] = "getCrossPlatformLookAndFeelClassName";
      var0[4] = "getSubstanceLAFName";
      var0[5] = "getInstalledLookAndFeels";
      var0[6] = "<$constructor$>";
      var0[7] = "putAt";
      var0[8] = "getAt";
      var0[9] = "putAt";
      var0[10] = "putAt";
      var0[11] = "remove";
      var0[12] = "checkValueIsTypeNotString";
      var0[13] = "name";
      var0[14] = "class";
      var0[15] = "getAt";
      var0[16] = "newInstance";
      var0[17] = "forName";
      var0[18] = "classLoader";
      var0[19] = "getClass";
      var0[20] = "getAt";
      var0[21] = "each";
      var0[22] = "call";
      var0[23] = "setLookAndFeel";
      var0[24] = "iterator";
      var0[25] = "getName";
      var0[26] = "forName";
      var0[27] = "iterator";
      var0[28] = "getName";
      var0[29] = "forName";
      var0[30] = "iterator";
      var0[31] = "getName";
      var0[32] = "forName";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[33];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper(), var0);
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
   private static Class $get$$class$javax$swing$LookAndFeel() {
      Class var10000 = $class$javax$swing$LookAndFeel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$LookAndFeel = class$("javax.swing.LookAndFeel");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$LookAndFeelHelper() {
      Class var10000 = $class$groovy$swing$LookAndFeelHelper;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$LookAndFeelHelper = class$("groovy.swing.LookAndFeelHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$UIManager() {
      Class var10000 = $class$javax$swing$UIManager;
      if (var10000 == null) {
         var10000 = $class$javax$swing$UIManager = class$("javax.swing.UIManager");
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
   private static Class $get$$class$java$lang$Class() {
      Class var10000 = $class$java$lang$Class;
      if (var10000 == null) {
         var10000 = $class$java$lang$Class = class$("java.lang.Class");
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

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure1;
      // $FF: synthetic field
      private static Class $class$javax$swing$plaf$metal$MetalLookAndFeel;
      // $FF: synthetic field
      private static Class $class$javax$swing$plaf$metal$DefaultMetalTheme;
      // $FF: synthetic field
      private static Class $class$java$lang$String;
      // $FF: synthetic field
      private static Class $class$java$lang$Class;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object theme) {
         Object themex = new Reference(theme);
         CallSite[] var4 = $getCallSiteArray();
         if (!(themex.get() instanceof MetalTheme)) {
            if (ScriptBytecodeAdapter.compareEqual(themex.get(), "ocean")) {
               themex.set(var4[0].call(var4[1].call($get$$class$java$lang$Class(), (Object)"javax.swing.plaf.metal.OceanTheme")));
            } else if (ScriptBytecodeAdapter.compareEqual(themex.get(), "steel")) {
               themex.set(var4[2].callConstructor($get$$class$javax$swing$plaf$metal$DefaultMetalTheme()));
            } else {
               themex.set(var4[3].call(var4[4].call($get$$class$java$lang$Class(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.asType(themex.get(), $get$$class$java$lang$String()), $get$$class$java$lang$String()))));
            }
         }

         Object var10000 = themex.get();
         ScriptBytecodeAdapter.setProperty(var10000, $get$$class$groovy$swing$LookAndFeelHelper$_closure1(), $get$$class$javax$swing$plaf$metal$MetalLookAndFeel(), "currentTheme");
         return var10000;
      }

      public Object call(Object laf, Object themex) {
         Object theme = new Reference(themex);
         CallSite[] var4 = $getCallSiteArray();
         return var4[5].callCurrent(this, laf, theme.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure1()) {
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
         var0[0] = "newInstance";
         var0[1] = "forName";
         var0[2] = "<$constructor$>";
         var0[3] = "newInstance";
         var0[4] = "forName";
         var0[5] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[6];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure1(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure1() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure1;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure1 = class$("groovy.swing.LookAndFeelHelper$_closure1");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$javax$swing$plaf$metal$MetalLookAndFeel() {
         Class var10000 = $class$javax$swing$plaf$metal$MetalLookAndFeel;
         if (var10000 == null) {
            var10000 = $class$javax$swing$plaf$metal$MetalLookAndFeel = class$("javax.swing.plaf.metal.MetalLookAndFeel");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$javax$swing$plaf$metal$DefaultMetalTheme() {
         Class var10000 = $class$javax$swing$plaf$metal$DefaultMetalTheme;
         if (var10000 == null) {
            var10000 = $class$javax$swing$plaf$metal$DefaultMetalTheme = class$("javax.swing.plaf.metal.DefaultMetalTheme");
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
      static Class class$(String var0) {
         try {
            return Class.forName(var0);
         } catch (ClassNotFoundException var2) {
            throw new NoClassDefFoundError(var2.getMessage());
         }
      }
   }

   class _closure2 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure2;
      // $FF: synthetic field
      private static Class $class$java$lang$Boolean;
      // $FF: synthetic field
      private static Class $class$javax$swing$UIManager;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object boldx) {
         Object bold = new Reference(boldx);
         CallSite[] var4 = $getCallSiteArray();
         return var4[0].call($get$$class$javax$swing$UIManager(), "swing.boldMetal", ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.asType(bold.get(), $get$$class$java$lang$Boolean()), $get$$class$java$lang$Boolean()));
      }

      public Object call(Object laf, Object boldx) {
         Object bold = new Reference(boldx);
         CallSite[] var4 = $getCallSiteArray();
         return var4[1].callCurrent(this, laf, bold.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure2()) {
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
         var0[0] = "put";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure2(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure2() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure2;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure2 = class$("groovy.swing.LookAndFeelHelper$_closure2");
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
      private static Class $get$$class$javax$swing$UIManager() {
         Class var10000 = $class$javax$swing$UIManager;
         if (var10000 == null) {
            var10000 = $class$javax$swing$UIManager = class$("javax.swing.UIManager");
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

   class _closure3 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure3;
      // $FF: synthetic field
      private static Class $class$java$lang$Boolean;
      // $FF: synthetic field
      private static Class $class$javax$swing$UIManager;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object xp) {
         CallSite[] var3 = $getCallSiteArray();
         return var3[0].call($get$$class$javax$swing$UIManager(), "swing.noxp", ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.asType(var3[1].callGroovyObjectGetProperty(this), $get$$class$java$lang$Boolean()), $get$$class$java$lang$Boolean()));
      }

      public Object call(Object laf, Object xp) {
         CallSite[] var3 = $getCallSiteArray();
         return var3[2].callCurrent(this, laf, xp);
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure3()) {
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
         var0[0] = "put";
         var0[1] = "bold";
         var0[2] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[3];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure3(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure3() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure3;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure3 = class$("groovy.swing.LookAndFeelHelper$_closure3");
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
      private static Class $get$$class$javax$swing$UIManager() {
         Class var10000 = $class$javax$swing$UIManager;
         if (var10000 == null) {
            var10000 = $class$javax$swing$UIManager = class$("javax.swing.UIManager");
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

   class _closure4 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure4;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object theme) {
         Object lafx = new Reference(laf);
         Object themex = new Reference(theme);
         CallSite[] var5 = $getCallSiteArray();
         return var5[0].call(lafx.get(), themex.get());
      }

      public Object call(Object laf, Object theme) {
         Object lafx = new Reference(laf);
         Object themex = new Reference(theme);
         CallSite[] var5 = $getCallSiteArray();
         return var5[1].callCurrent(this, lafx.get(), themex.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure4()) {
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
         var0[0] = "setCurrentTheme";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure4(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure4() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure4;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure4 = class$("groovy.swing.LookAndFeelHelper$_closure4");
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

   class _closure5 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure5;

      public _closure5(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object skin) {
         Object lafx = new Reference(laf);
         Object skinx = new Reference(skin);
         CallSite[] var5 = $getCallSiteArray();
         return var5[0].call(lafx.get(), skinx.get());
      }

      public Object call(Object laf, Object skin) {
         Object lafx = new Reference(laf);
         Object skinx = new Reference(skin);
         CallSite[] var5 = $getCallSiteArray();
         return var5[1].callCurrent(this, lafx.get(), skinx.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure5()) {
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
         var0[0] = "setSkin";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure5(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure5() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure5;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure5 = class$("groovy.swing.LookAndFeelHelper$_closure5");
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

   class _closure6 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$groovy$swing$LookAndFeelHelper$_closure6;

      public _closure6(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object laf, Object watermark) {
         Object lafx = new Reference(laf);
         Object watermarkx = new Reference(watermark);
         CallSite[] var5 = $getCallSiteArray();
         return var5[0].call(lafx.get(), watermarkx.get());
      }

      public Object call(Object laf, Object watermark) {
         Object lafx = new Reference(laf);
         Object watermarkx = new Reference(watermark);
         CallSite[] var5 = $getCallSiteArray();
         return var5[1].callCurrent(this, lafx.get(), watermarkx.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$groovy$swing$LookAndFeelHelper$_closure6()) {
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
         var0[0] = "setCurrentWatermark";
         var0[1] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[2];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$groovy$swing$LookAndFeelHelper$_closure6(), var0);
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
      private static Class $get$$class$groovy$swing$LookAndFeelHelper$_closure6() {
         Class var10000 = $class$groovy$swing$LookAndFeelHelper$_closure6;
         if (var10000 == null) {
            var10000 = $class$groovy$swing$LookAndFeelHelper$_closure6 = class$("groovy.swing.LookAndFeelHelper$_closure6");
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
}
