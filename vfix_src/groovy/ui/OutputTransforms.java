package groovy.ui;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class OutputTransforms implements GroovyObject {
   private static Object $localTransforms;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203533L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203533 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$groovy$ui$OutputTransforms$ObjectHolder_localTransforms;
   // $FF: synthetic field
   private static Class $class$groovy$ui$OutputTransforms;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyShell;

   public OutputTransforms() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static Object loadOutputTransforms() {
      CallSite[] var0 = $getCallSiteArray();
      Object transforms = ScriptBytecodeAdapter.createList(new Object[0]);
      Object userHome = var0[0].callConstructor($get$$class$java$io$File(), (Object)var0[1].call($get$$class$java$lang$System(), (Object)"user.home"));
      Object groovyDir = var0[2].callConstructor($get$$class$java$io$File(), userHome, ".groovy");
      Object userTransforms = var0[3].callConstructor($get$$class$java$io$File(), groovyDir, "OutputTransforms.groovy");
      if (DefaultTypeTransformation.booleanUnbox(var0[4].call(userTransforms))) {
         GroovyShell shell = var0[5].callConstructor($get$$class$groovy$lang$GroovyShell());
         var0[6].call(shell, "transforms", transforms);
         var0[7].call(shell, userTransforms);
      }

      var0[8].call(transforms, (Object)(new GeneratedClosure($get$$class$groovy$ui$OutputTransforms(), $get$$class$groovy$ui$OutputTransforms()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(itx.get() instanceof Component && !(itx.get() instanceof Window) ? Boolean.TRUE : Boolean.FALSE) && ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(itx.get()), (Object)null) ? Boolean.TRUE : Boolean.FALSE) ? itx.get() : null;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1()) {
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
            var0[0] = "parent";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1(), var0);
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
         private static Class $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1() {
            Class var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure1 = class$("groovy.ui.OutputTransforms$_loadOutputTransforms_closure1");
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
      var0[9].call(transforms, (Object)(new GeneratedClosure($get$$class$groovy$ui$OutputTransforms(), $get$$class$groovy$ui$OutputTransforms()) {
         // $FF: synthetic field
         private static final Integer $const$0 = (Integer)0;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$javax$swing$ImageIcon;
         // $FF: synthetic field
         private static Class $class$java$lang$Integer;
         // $FF: synthetic field
         private static Class $class$java$awt$Dimension;
         // $FF: synthetic field
         private static Class $class$java$awt$Graphics2D;
         // $FF: synthetic field
         private static Class $class$java$awt$image$BufferedImage;
         // $FF: synthetic field
         private static Class $class$java$awt$Transparency;
         // $FF: synthetic field
         private static Class $class$java$awt$GraphicsConfiguration;
         // $FF: synthetic field
         private static Class $class$java$awt$GraphicsEnvironment;
         // $FF: synthetic field
         private static Class $class$java$awt$GraphicsDevice;
         // $FF: synthetic field
         private static Class $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            if (itx.get() instanceof JComponent) {
               Dimension d = new Reference((Dimension)ScriptBytecodeAdapter.castToType(var3[0].callGetProperty(itx.get()), $get$$class$java$awt$Dimension()));
               if (ScriptBytecodeAdapter.compareEqual(var3[1].callGetProperty(d.get()), $const$0)) {
                  d.set((Dimension)ScriptBytecodeAdapter.castToType(var3[2].callGetProperty(itx.get()), $get$$class$java$awt$Dimension()));
                  ScriptBytecodeAdapter.setProperty(d.get(), $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2(), itx.get(), "size");
               }

               GraphicsEnvironment ge = (GraphicsEnvironment)ScriptBytecodeAdapter.castToType(var3[3].callGetProperty($get$$class$java$awt$GraphicsEnvironment()), $get$$class$java$awt$GraphicsEnvironment());
               GraphicsDevice gs = (GraphicsDevice)ScriptBytecodeAdapter.castToType(var3[4].callGetProperty(ge), $get$$class$java$awt$GraphicsDevice());
               GraphicsConfiguration gc = (GraphicsConfiguration)ScriptBytecodeAdapter.castToType(var3[5].callGetProperty(gs), $get$$class$java$awt$GraphicsConfiguration());
               BufferedImage image = (BufferedImage)ScriptBytecodeAdapter.castToType(var3[6].call(gc, ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var3[7].callGetProperty(d.get()), $get$$class$java$lang$Integer()), Integer.TYPE), ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.asType(var3[8].callGetProperty(d.get()), $get$$class$java$lang$Integer()), Integer.TYPE), var3[9].callGetProperty($get$$class$java$awt$Transparency())), $get$$class$java$awt$image$BufferedImage());
               Graphics2D g2 = (Graphics2D)ScriptBytecodeAdapter.castToType(var3[10].call(image), $get$$class$java$awt$Graphics2D());
               var3[11].call(itx.get(), (Object)g2);
               var3[12].call(g2);
               return var3[13].callConstructor($get$$class$javax$swing$ImageIcon(), (Object)image);
            } else {
               return null;
            }
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2()) {
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
            var0[0] = "size";
            var0[1] = "width";
            var0[2] = "preferredSize";
            var0[3] = "localGraphicsEnvironment";
            var0[4] = "defaultScreenDevice";
            var0[5] = "defaultConfiguration";
            var0[6] = "createCompatibleImage";
            var0[7] = "width";
            var0[8] = "height";
            var0[9] = "TRANSLUCENT";
            var0[10] = "createGraphics";
            var0[11] = "print";
            var0[12] = "dispose";
            var0[13] = "<$constructor$>";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[14];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2(), var0);
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
         private static Class $get$$class$javax$swing$ImageIcon() {
            Class var10000 = $class$javax$swing$ImageIcon;
            if (var10000 == null) {
               var10000 = $class$javax$swing$ImageIcon = class$("javax.swing.ImageIcon");
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
         private static Class $get$$class$java$awt$Dimension() {
            Class var10000 = $class$java$awt$Dimension;
            if (var10000 == null) {
               var10000 = $class$java$awt$Dimension = class$("java.awt.Dimension");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$Graphics2D() {
            Class var10000 = $class$java$awt$Graphics2D;
            if (var10000 == null) {
               var10000 = $class$java$awt$Graphics2D = class$("java.awt.Graphics2D");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$image$BufferedImage() {
            Class var10000 = $class$java$awt$image$BufferedImage;
            if (var10000 == null) {
               var10000 = $class$java$awt$image$BufferedImage = class$("java.awt.image.BufferedImage");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$Transparency() {
            Class var10000 = $class$java$awt$Transparency;
            if (var10000 == null) {
               var10000 = $class$java$awt$Transparency = class$("java.awt.Transparency");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$GraphicsConfiguration() {
            Class var10000 = $class$java$awt$GraphicsConfiguration;
            if (var10000 == null) {
               var10000 = $class$java$awt$GraphicsConfiguration = class$("java.awt.GraphicsConfiguration");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$GraphicsEnvironment() {
            Class var10000 = $class$java$awt$GraphicsEnvironment;
            if (var10000 == null) {
               var10000 = $class$java$awt$GraphicsEnvironment = class$("java.awt.GraphicsEnvironment");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$awt$GraphicsDevice() {
            Class var10000 = $class$java$awt$GraphicsDevice;
            if (var10000 == null) {
               var10000 = $class$java$awt$GraphicsDevice = class$("java.awt.GraphicsDevice");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2() {
            Class var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure2 = class$("groovy.ui.OutputTransforms$_loadOutputTransforms_closure2");
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
      var0[10].call(transforms, (Object)(new GeneratedClosure($get$$class$groovy$ui$OutputTransforms(), $get$$class$groovy$ui$OutputTransforms()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return itx.get() instanceof Icon ? itx.get() : null;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3()) {
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
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[0];
            return new CallSiteArray($get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3(), var0);
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
         private static Class $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3() {
            Class var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure3 = class$("groovy.ui.OutputTransforms$_loadOutputTransforms_closure3");
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
      var0[11].call(transforms, (Object)(new GeneratedClosure($get$$class$groovy$ui$OutputTransforms(), $get$$class$groovy$ui$OutputTransforms()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$javax$swing$ImageIcon;
         // $FF: synthetic field
         private static Class $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return itx.get() instanceof Image ? var3[0].callConstructor($get$$class$javax$swing$ImageIcon(), (Object)itx.get()) : null;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4()) {
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
            var0[0] = "<$constructor$>";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4(), var0);
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
         private static Class $get$$class$javax$swing$ImageIcon() {
            Class var10000 = $class$javax$swing$ImageIcon;
            if (var10000 == null) {
               var10000 = $class$javax$swing$ImageIcon = class$("javax.swing.ImageIcon");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4() {
            Class var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure4 = class$("groovy.ui.OutputTransforms$_loadOutputTransforms_closure4");
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
      var0[12].call(transforms, (Object)(new GeneratedClosure($get$$class$groovy$ui$OutputTransforms(), $get$$class$groovy$ui$OutputTransforms()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
         // $FF: synthetic field
         private static Class $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.compareNotEqual(itx.get(), (Object)null) ? new GStringImpl(new Object[]{var3[0].call($get$$class$org$codehaus$groovy$runtime$InvokerHelper(), (Object)itx.get())}, new String[]{"", ""}) : null;
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5()) {
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
            var0[0] = "inspect";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[1];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$runtime$InvokerHelper() {
            Class var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$runtime$InvokerHelper = class$("org.codehaus.groovy.runtime.InvokerHelper");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5() {
            Class var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5;
            if (var10000 == null) {
               var10000 = $class$groovy$ui$OutputTransforms$_loadOutputTransforms_closure5 = class$("groovy.ui.OutputTransforms$_loadOutputTransforms_closure5");
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
      return transforms;
   }

   public static Object transformResult(Object base, Object transforms) {
      CallSite[] var2 = $getCallSiteArray();
      Closure c = null;
      Object var4 = var2[13].call(transforms);

      Object result;
      do {
         if (!((Iterator)var4).hasNext()) {
            return base;
         }

         c = ((Iterator)var4).next();
         result = var2[14].call(c, (Object)ScriptBytecodeAdapter.createPojoWrapper(base, $get$$class$java$lang$Object()));
      } while(!ScriptBytecodeAdapter.compareNotEqual(result, (Object)null));

      return result;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$ui$OutputTransforms()) {
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
      Class var10000 = $get$$class$groovy$ui$OutputTransforms();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$ui$OutputTransforms(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$ui$OutputTransforms(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public static Object getLocalTransforms() {
      CallSite[] var0 = $getCallSiteArray();
      return var0[15].callGetProperty($get$$class$groovy$ui$OutputTransforms$ObjectHolder_localTransforms());
   }

   public static Object transformResult(Object base) {
      CallSite[] var1 = $getCallSiteArray();
      return var1[16].callStatic($get$$class$groovy$ui$OutputTransforms(), ScriptBytecodeAdapter.createPojoWrapper(base, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createPojoWrapper(var1[17].callGetProperty($get$$class$groovy$ui$OutputTransforms()), $get$$class$java$lang$Object()));
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

   public static Object get$localTransforms() {
      return $localTransforms;
   }

   public static void set$localTransforms(Object var0) {
      $localTransforms = var0;
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
      var0[0] = "<$constructor$>";
      var0[1] = "getProperty";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "exists";
      var0[5] = "<$constructor$>";
      var0[6] = "setVariable";
      var0[7] = "evaluate";
      var0[8] = "leftShift";
      var0[9] = "leftShift";
      var0[10] = "leftShift";
      var0[11] = "leftShift";
      var0[12] = "leftShift";
      var0[13] = "iterator";
      var0[14] = "call";
      var0[15] = "INSTANCE";
      var0[16] = "transformResult";
      var0[17] = "localTransforms";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[18];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$ui$OutputTransforms(), var0);
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
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
   private static Class $get$$class$groovy$ui$OutputTransforms$ObjectHolder_localTransforms() {
      Class var10000 = $class$groovy$ui$OutputTransforms$ObjectHolder_localTransforms;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$OutputTransforms$ObjectHolder_localTransforms = class$("groovy.ui.OutputTransforms$ObjectHolder_localTransforms");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$ui$OutputTransforms() {
      Class var10000 = $class$groovy$ui$OutputTransforms;
      if (var10000 == null) {
         var10000 = $class$groovy$ui$OutputTransforms = class$("groovy.ui.OutputTransforms");
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
   private static Class $get$$class$java$io$File() {
      Class var10000 = $class$java$io$File;
      if (var10000 == null) {
         var10000 = $class$java$io$File = class$("java.io.File");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyShell() {
      Class var10000 = $class$groovy$lang$GroovyShell;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyShell = class$("groovy.lang.GroovyShell");
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
