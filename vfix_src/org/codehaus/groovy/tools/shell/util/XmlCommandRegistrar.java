package org.codehaus.groovy.tools.shell.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.Command;
import org.codehaus.groovy.tools.shell.Shell;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class XmlCommandRegistrar implements GroovyObject {
   private final Logger log;
   private final Shell shell;
   private final ClassLoader classLoader;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204100L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204100 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Shell;
   // $FF: synthetic field
   private static Class $class$java$lang$ClassLoader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar;

   public XmlCommandRegistrar(Shell shell, ClassLoader classLoader) {
      CallSite[] var3 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var3[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var3[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var4 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var4.record(shell, 8))) {
            var4.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert shell", var4), (Object)null);
         }
      } catch (Throwable var11) {
         var4.clear();
         throw var11;
      }

      ValueRecorder var5 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var5.record(classLoader, 8))) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert classLoader", var5), (Object)null);
         }
      } catch (Throwable var10) {
         var5.clear();
         throw var10;
      }

      this.shell = (Shell)ScriptBytecodeAdapter.castToType(shell, $get$$class$org$codehaus$groovy$tools$shell$Shell());
      this.classLoader = (ClassLoader)ScriptBytecodeAdapter.castToType(classLoader, $get$$class$java$lang$ClassLoader());
   }

   public void register(URL url) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(url, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert url", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[2].callGetProperty(this.log))) {
         var2[3].call(this.log, (Object)(new GStringImpl(new Object[]{url}, new String[]{"Registering commands from: ", ""})));
      }

      var2[4].call(url, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1;
         // $FF: synthetic field
         private static Class $class$groovy$util$XmlParser;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object reader) {
            Object readerx = new Reference(reader);
            CallSite[] var3 = $getCallSiteArray();
            Object doc = var3[0].call(var3[1].callConstructor($get$$class$groovy$util$XmlParser()), readerx.get());
            return var3[2].call(var3[3].callGetProperty(doc), (Object)(new GeneratedClosure(this, this.getThisObject()) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2;
               // $FF: synthetic field
               private static Class $class$java$lang$String;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$Command;
               // $FF: synthetic field
               private static Class $class$java$lang$Class;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object element) {
                  Object elementx = new Reference(element);
                  CallSite[] var3 = $getCallSiteArray();
                  String classname = (String)ScriptBytecodeAdapter.castToType(var3[0].call(elementx.get()), $get$$class$java$lang$String());
                  Class type = (Class)ScriptBytecodeAdapter.castToType(var3[1].call(var3[2].callGroovyObjectGetProperty(this), (Object)classname), $get$$class$java$lang$Class());
                  Command command = new Reference((Command)ScriptBytecodeAdapter.castToType(var3[3].call(type, (Object)var3[4].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$Command()));
                  if (DefaultTypeTransformation.booleanUnbox(var3[5].callGetProperty(var3[6].callGroovyObjectGetProperty(this)))) {
                     var3[7].call(var3[8].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var3[9].callGetProperty(command.get()), command.get()}, new String[]{"Created command '", "': ", ""})));
                  }

                  return var3[10].call(var3[11].callGroovyObjectGetProperty(this), command.get());
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2()) {
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
                  var0[0] = "text";
                  var0[1] = "loadClass";
                  var0[2] = "classLoader";
                  var0[3] = "newInstance";
                  var0[4] = "shell";
                  var0[5] = "debugEnabled";
                  var0[6] = "log";
                  var0[7] = "debug";
                  var0[8] = "log";
                  var0[9] = "name";
                  var0[10] = "leftShift";
                  var0[11] = "shell";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[12];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1_closure2 = class$("org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar$_register_closure1_closure2");
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$Command() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$Command;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$Command = class$("org.codehaus.groovy.tools.shell.Command");
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
            }));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1()) {
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
            var0[0] = "parse";
            var0[1] = "<$constructor$>";
            var0[2] = "each";
            var0[3] = "command";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar$_register_closure1 = class$("org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar$_register_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$util$XmlParser() {
            Class var10000 = $class$groovy$util$XmlParser;
            if (var10000 == null) {
               var10000 = $class$groovy$util$XmlParser = class$("groovy.util.XmlParser");
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
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "create";
      var0[1] = "class";
      var0[2] = "debugEnabled";
      var0[3] = "debug";
      var0[4] = "withReader";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[5];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Logger() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger = class$("org.codehaus.groovy.tools.shell.util.Logger");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Shell() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Shell;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Shell = class$("org.codehaus.groovy.tools.shell.Shell");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$ClassLoader() {
      Class var10000 = $class$java$lang$ClassLoader;
      if (var10000 == null) {
         var10000 = $class$java$lang$ClassLoader = class$("java.lang.ClassLoader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar = class$("org.codehaus.groovy.tools.shell.util.XmlCommandRegistrar");
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
