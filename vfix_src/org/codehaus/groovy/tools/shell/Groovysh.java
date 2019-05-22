package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.jline.History;
import com.gzoltar.shaded.jline.WindowsTerminal;
import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.MessageSource;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class Groovysh extends Shell implements GroovyObject {
   private static final MessageSource messages = (MessageSource)$getCallSiteArray()[9].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$MessageSource(), (Object)$get$$class$org$codehaus$groovy$tools$shell$Groovysh());
   private final BufferManager buffers;
   private final Parser parser;
   private final Interpreter interp;
   private final List imports;
   private InteractiveShellRunner runner;
   private History history;
   private final Closure defaultResultHook;
   private Closure resultHook;
   private final Closure defaultErrorHook;
   private Closure errorHook;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)80;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204736L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204736 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ParseCode;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner;
   // $FF: synthetic field
   private static Class $class$org$fusesource$jansi$Ansi;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Parser;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$AnsiDetector;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$jline$Terminal;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$MessageSource;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Groovysh;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$java$lang$Error;
   // $FF: synthetic field
   private static Class $class$java$lang$Thread;
   // $FF: synthetic field
   private static Class $class$jline$History;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Binding;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalStateException;
   // $FF: synthetic field
   private static Class $class$org$fusesource$jansi$AnsiConsole;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Interpreter;
   // $FF: synthetic field
   private static Class $class$java$lang$Throwable;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$StackTraceUtils;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$org$fusesource$jansi$AnsiRenderer;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$BufferManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Shell;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO;

   public Groovysh(ClassLoader classLoader, Binding binding, IO io, Closure registrar) {
      CallSite[] var5 = $getCallSiteArray();
      Object[] var10000 = new Object[]{io};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$org$codehaus$groovy$tools$shell$Shell());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         break;
      case 1:
         super((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.buffers = (BufferManager)ScriptBytecodeAdapter.castToType(var5[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$BufferManager()), $get$$class$org$codehaus$groovy$tools$shell$BufferManager());
      this.imports = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.defaultResultHook = (Closure)ScriptBytecodeAdapter.castToType(new Groovysh._closure1(this, this), $get$$class$groovy$lang$Closure());
      this.resultHook = (Closure)ScriptBytecodeAdapter.castToType(this.defaultResultHook, $get$$class$groovy$lang$Closure());
      this.defaultErrorHook = (Closure)ScriptBytecodeAdapter.castToType(new Groovysh._closure2(this, this), $get$$class$groovy$lang$Closure());
      this.errorHook = (Closure)ScriptBytecodeAdapter.castToType(this.defaultErrorHook, $get$$class$groovy$lang$Closure());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var6 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var6.record(classLoader, 8))) {
            var6.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert classLoader", var6), (Object)null);
         }
      } catch (Throwable var20) {
         var6.clear();
         throw var20;
      }

      ValueRecorder var7 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var7.record(binding, 8))) {
            var7.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert binding", var7), (Object)null);
         }
      } catch (Throwable var19) {
         var7.clear();
         throw var19;
      }

      ValueRecorder var8 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var8.record(registrar, 8))) {
            var8.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert registrar", var8), (Object)null);
         }
      } catch (Throwable var18) {
         var8.clear();
         throw var18;
      }

      this.parser = (Parser)ScriptBytecodeAdapter.castToType(var5[1].callConstructor($get$$class$org$codehaus$groovy$tools$shell$Parser()), $get$$class$org$codehaus$groovy$tools$shell$Parser());
      this.interp = (Interpreter)ScriptBytecodeAdapter.castToType(var5[2].callConstructor($get$$class$org$codehaus$groovy$tools$shell$Interpreter(), classLoader, binding), $get$$class$org$codehaus$groovy$tools$shell$Interpreter());
      var5[3].call(registrar, (Object)this);
   }

   public Groovysh(ClassLoader classLoader, Binding binding, IO io) {
      CallSite[] var4 = $getCallSiteArray();
      Object[] var10000 = new Object[]{classLoader, binding, io, var4[4].callStatic($get$$class$org$codehaus$groovy$tools$shell$Groovysh())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 5, $get$$class$org$codehaus$groovy$tools$shell$Groovysh());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0], (IO)var10001[1]);
         break;
      case 2:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2], (Closure)var10001[3]);
         break;
      case 4:
         this((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Groovysh(Binding binding, IO io) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{var3[5].callGetProperty(var3[6].call($get$$class$java$lang$Thread())), binding, io};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 5, $get$$class$org$codehaus$groovy$tools$shell$Groovysh());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0], (IO)var10001[1]);
         break;
      case 2:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2], (Closure)var10001[3]);
         break;
      case 4:
         this((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Groovysh(IO io) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{var2[7].callConstructor($get$$class$groovy$lang$Binding()), io};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 5, $get$$class$org$codehaus$groovy$tools$shell$Groovysh());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0], (IO)var10001[1]);
         break;
      case 2:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2], (Closure)var10001[3]);
         break;
      case 4:
         this((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Groovysh() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{var1[8].callConstructor($get$$class$org$codehaus$groovy$tools$shell$IO())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 5, $get$$class$org$codehaus$groovy$tools$shell$Groovysh());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Binding)var10001[0], (IO)var10001[1]);
         break;
      case 2:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2]);
         break;
      case 3:
         this((ClassLoader)var10001[0], (Binding)var10001[1], (IO)var10001[2], (Closure)var10001[3]);
         break;
      case 4:
         this((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   static {
      $getCallSiteArray()[10].call($get$$class$org$fusesource$jansi$AnsiConsole());
      $getCallSiteArray()[11].call($get$$class$org$fusesource$jansi$Ansi(), (Object)$getCallSiteArray()[12].callConstructor($get$$class$org$codehaus$groovy$tools$shell$AnsiDetector()));
   }

   private static Closure createDefaultRegistrar() {
      CallSite[] var0 = $getCallSiteArray();
      return (Closure)ScriptBytecodeAdapter.castToType(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$shell$Groovysh(), $get$$class$org$codehaus$groovy$tools$shell$Groovysh()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object shell) {
            Object shellx = new Reference(shell);
            CallSite[] var3 = $getCallSiteArray();
            Object r = var3[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$XmlCommandRegistrar(), shellx.get(), var3[1].callGroovyObjectGetProperty(this));
            return var3[2].call(r, var3[3].call(var3[4].callCurrent(this), (Object)"commands.xml"));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3()) {
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
            var0[1] = "classLoader";
            var0[2] = "register";
            var0[3] = "getResource";
            var0[4] = "getClass";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_createDefaultRegistrar_closure3 = class$("org.codehaus.groovy.tools.shell.Groovysh$_createDefaultRegistrar_closure3");
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
      }, $get$$class$groovy$lang$Closure());
   }

   public Object execute(String line) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var3.record(line, 8), (Object)null);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 13);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert line != null", var3), (Object)null);
         }
      } catch (Throwable var10) {
         var3.clear();
         throw var10;
      }

      if (ScriptBytecodeAdapter.compareEqual(var2[13].call(var2[14].call(line)), $const$0)) {
         return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
      } else {
         var2[15].callCurrent(this, (Object)line);
         Object result = null;
         if (DefaultTypeTransformation.booleanUnbox(var2[16].callCurrent(this, (Object)line))) {
            result = var2[17].callCurrent(this, (Object)line);
            if (DefaultTypeTransformation.booleanUnbox(result)) {
               ScriptBytecodeAdapter.setGroovyObjectProperty(result, $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this, "lastResult");
            }

            return (Object)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Object());
         } else {
            Object current = ScriptBytecodeAdapter.createList(new Object[0]);
            Object current = var2[18].call(current, (Object)var2[19].call(this.buffers));
            var2[20].call(current, (Object)line);
            Object status = var2[21].call(this.parser, (Object)var2[22].call(this.imports, (Object)current));
            Object var7 = var2[23].callGetProperty(status);
            if (ScriptBytecodeAdapter.isCase(var7, var2[24].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$ParseCode()))) {
               var2[25].call(var2[26].callGroovyObjectGetProperty(this), (Object)"Evaluating buffer...");
               if (DefaultTypeTransformation.booleanUnbox(var2[27].callGetProperty(var2[28].callGroovyObjectGetProperty(this)))) {
                  var2[29].callCurrent(this, (Object)var2[30].callGroovyObjectGetProperty(this));
               }

               Object buff = var2[31].call(var2[32].call(this.imports, (Object)ScriptBytecodeAdapter.createList(new Object[]{"true"})), current);
               ScriptBytecodeAdapter.setGroovyObjectProperty(result = var2[33].call(this.interp, (Object)buff), $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this, "lastResult");
               var2[34].call(this.buffers);
            } else {
               if (!ScriptBytecodeAdapter.isCase(var7, var2[35].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$ParseCode()))) {
                  if (ScriptBytecodeAdapter.isCase(var7, var2[37].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$ParseCode()))) {
                     throw (Throwable)var2[38].callGetProperty(status);
                  }

                  throw (Throwable)var2[39].callConstructor($get$$class$java$lang$Error(), (Object)(new GStringImpl(new Object[]{var2[40].callGetProperty(status)}, new String[]{"Invalid parse status: ", ""})));
               }

               var2[36].call(this.buffers, (Object)current);
            }

            return (Object)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Object());
         }
      }
   }

   protected Object executeCommand(String line) {
      CallSite[] var2 = $getCallSiteArray();
      return (Object)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$tools$shell$Shell(), this, "execute", new Object[]{line}), $get$$class$java$lang$Object());
   }

   private void displayBuffer(List buffer) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(buffer, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert buffer", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      var2[41].call(buffer, (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object line, Object index) {
            Object linex = new Reference(line);
            Object indexx = new Reference(index);
            CallSite[] var5 = $getCallSiteArray();
            Object lineNum = var5[0].callCurrent(this, (Object)indexx.get());
            return var5[1].call(var5[2].callGetProperty(var5[3].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{lineNum, linex.get()}, new String[]{" ", "@|bold >|@ ", ""})));
         }

         public Object call(Object line, Object index) {
            Object linex = new Reference(line);
            Object indexx = new Reference(index);
            CallSite[] var5 = $getCallSiteArray();
            return var5[4].callCurrent(this, linex.get(), indexx.get());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4()) {
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
            var0[0] = "formatLineNumber";
            var0[1] = "println";
            var0[2] = "out";
            var0[3] = "io";
            var0[4] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_displayBuffer_closure4 = class$("org.codehaus.groovy.tools.shell.Groovysh$_displayBuffer_closure4");
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

   private String renderPrompt() {
      CallSite[] var1 = $getCallSiteArray();
      Object lineNum = var1[42].callCurrent(this, (Object)var1[43].call(var1[44].call(this.buffers)));
      return (String)ScriptBytecodeAdapter.castToType(var1[45].call($get$$class$org$fusesource$jansi$AnsiRenderer(), (Object)(new GStringImpl(new Object[]{lineNum}, new String[]{"@|bold groovy:|@", "@|bold >|@ "}))), $get$$class$java$lang$String());
   }

   private String formatLineNumber(int num) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareGreaterThanEqual(var3.record(DefaultTypeTransformation.box(num), 8), $const$0);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 12);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert num >= 0", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      return (String)ScriptBytecodeAdapter.castToType(var2[46].call(var2[47].call(DefaultTypeTransformation.box(num)), $const$1, "0"), $get$$class$java$lang$String());
   }

   public File getUserStateDirectory() {
      CallSite[] var1 = $getCallSiteArray();
      Object userHome = var1[48].callConstructor($get$$class$java$io$File(), (Object)var1[49].call($get$$class$java$lang$System(), (Object)"user.home"));
      Object dir = var1[50].callConstructor($get$$class$java$io$File(), userHome, ".groovy");
      return (File)ScriptBytecodeAdapter.castToType(var1[51].callGetProperty(dir), $get$$class$java$io$File());
   }

   private void loadUserScript(String filename) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(filename, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert filename", var3), (Object)null);
         }
      } catch (Throwable var13) {
         var3.clear();
         throw var13;
      }

      Object file = var2[52].callConstructor($get$$class$java$io$File(), var2[53].callGroovyObjectGetProperty(this), filename);
      if (DefaultTypeTransformation.booleanUnbox(var2[54].call(file))) {
         Object command = var2[55].call(var2[56].callGroovyObjectGetProperty(this), (Object)"load");
         if (DefaultTypeTransformation.booleanUnbox(command)) {
            var2[57].call(var2[58].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{file}, new String[]{"Loading user-script: ", ""})));
            Object previousHook = this.resultHook;
            this.resultHook = (Closure)ScriptBytecodeAdapter.castToType(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object result) {
                  CallSite[] var2 = $getCallSiteArray();
                  return null;
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5()) {
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
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_loadUserScript_closure5 = class$("org.codehaus.groovy.tools.shell.Groovysh$_loadUserScript_closure5");
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
            }, $get$$class$groovy$lang$Closure());

            try {
               var2[59].call(command, var2[60].call(var2[61].call(file)));
            } finally {
               this.resultHook = (Closure)ScriptBytecodeAdapter.castToType((Closure)ScriptBytecodeAdapter.castToType(previousHook, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure());
            }
         } else {
            var2[62].call(var2[63].callGroovyObjectGetProperty(this), (Object)"Unable to load user-script, missing 'load' command");
         }
      }

   }

   private void maybeRecordInput(String line) {
      CallSite[] var2 = $getCallSiteArray();
      Object record = var2[64].call(var2[65].callGroovyObjectGetProperty(this), (Object)"record");
      if (ScriptBytecodeAdapter.compareNotEqual(record, (Object)null)) {
         var2[66].call(record, (Object)line);
      }

   }

   private void maybeRecordResult(Object result) {
      CallSite[] var2 = $getCallSiteArray();
      Object record = var2[67].call(var2[68].callGroovyObjectGetProperty(this), (Object)"record");
      if (ScriptBytecodeAdapter.compareNotEqual(record, (Object)null)) {
         var2[69].call(record, result);
      }

   }

   private void maybeRecordError(Throwable cause) {
      CallSite[] var2 = $getCallSiteArray();
      Object record = var2[70].call(var2[71].callGroovyObjectGetProperty(this), (Object)"record");
      if (ScriptBytecodeAdapter.compareNotEqual(record, (Object)null)) {
         Boolean sanitize = (Boolean)ScriptBytecodeAdapter.castToType(var2[72].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$util$Preferences()), $get$$class$java$lang$Boolean());
         if (DefaultTypeTransformation.booleanUnbox(sanitize)) {
            cause = (Throwable)ScriptBytecodeAdapter.castToType(var2[73].call($get$$class$org$codehaus$groovy$runtime$StackTraceUtils(), (Object)cause), $get$$class$java$lang$Throwable());
         }

         var2[74].call(record, (Object)cause);
      }

   }

   private void setLastResult(Object result) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(this.resultHook, (Object)null)) {
         throw (Throwable)var2[75].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"Result hook is not set");
      } else {
         var2[76].call(this.resultHook, (Object)ScriptBytecodeAdapter.createPojoWrapper(result, $get$$class$java$lang$Object()));
         var2[77].call(var2[78].callGroovyObjectGetProperty(this.interp), "_", result);
         var2[79].callCurrent(this, (Object)result);
      }
   }

   private Object getLastResult() {
      CallSite[] var1 = $getCallSiteArray();
      return (Object)ScriptBytecodeAdapter.castToType(var1[80].call(var1[81].callGroovyObjectGetProperty(this.interp), (Object)"_"), $get$$class$java$lang$Object());
   }

   private void displayError(Throwable cause) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(this.errorHook, (Object)null)) {
         throw (Throwable)var2[82].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"Error hook is not set");
      } else {
         var2[83].call(this.errorHook, (Object)cause);
      }
   }

   public int run(String... args) {
      CallSite[] var2 = $getCallSiteArray();
      String commandLine = (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(args, (Object)null) && ScriptBytecodeAdapter.compareGreaterThan(var2[84].callGetProperty(args), $const$0) ? Boolean.TRUE : Boolean.FALSE)) {
         commandLine = (String)ScriptBytecodeAdapter.castToType(var2[85].call(args, (Object)" "), $get$$class$java$lang$String());
      }

      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(var2[86].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper(commandLine, $get$$class$java$lang$String())), $get$$class$java$lang$Integer()));
   }

   public int run(String commandLine) {
      CallSite[] var2 = $getCallSiteArray();
      Object term = var2[87].callGetProperty($get$$class$jline$Terminal());
      if (DefaultTypeTransformation.booleanUnbox(var2[88].callGetProperty(var2[89].callGroovyObjectGetProperty(this)))) {
         var2[90].call(var2[91].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{term}, new String[]{"Terminal (", ")"})));
         var2[92].call(var2[93].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[94].callGetProperty(term)}, new String[]{"    Supported:  ", ""})));
         var2[95].call(var2[96].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[97].callGetProperty(term), var2[98].callGetProperty(term)}, new String[]{"    ECHO:       ", " (enabled: ", ")"})));
         var2[99].call(var2[100].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[101].callGetProperty(term), var2[102].callGetProperty(term)}, new String[]{"    H x W:      ", " x ", ""})));
         var2[103].call(var2[104].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[105].call(term)}, new String[]{"    ANSI:       ", ""})));
         if (term instanceof WindowsTerminal) {
            var2[106].call(var2[107].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[108].callGetProperty(term)}, new String[]{"    Direct:     ", ""})));
         }
      }

      Object code = null;

      try {
         var2[109].callCurrent(this, (Object)"groovysh.profile");
         if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(commandLine, (Object)null) && ScriptBytecodeAdapter.compareGreaterThan(var2[110].call(var2[111].call(commandLine)), $const$0) ? Boolean.TRUE : Boolean.FALSE)) {
            var2[112].callCurrent(this, (Object)commandLine);
         } else {
            var2[113].callCurrent(this, (Object)"groovysh.rc");
            this.runner = (InteractiveShellRunner)ScriptBytecodeAdapter.castToType(var2[114].callConstructor($get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner(), this, ScriptBytecodeAdapter.createGroovyObjectWrapper(ScriptBytecodeAdapter.getMethodPointer(this, "renderPrompt"), $get$$class$groovy$lang$Closure())), $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner());
            Object var10000 = var2[115].callConstructor($get$$class$jline$History());
            this.history = (History)ScriptBytecodeAdapter.castToType(var10000, $get$$class$jline$History());
            ScriptBytecodeAdapter.setGroovyObjectProperty(var10000, $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this.runner, "history");
            ScriptBytecodeAdapter.setGroovyObjectProperty(var2[116].callConstructor($get$$class$java$io$File(), var2[117].callGroovyObjectGetProperty(this), "groovysh.history"), $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this.runner, "historyFile");
            ScriptBytecodeAdapter.setGroovyObjectProperty(ScriptBytecodeAdapter.getMethodPointer(this, "displayError"), $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this.runner, "errorHandler");
            if (!DefaultTypeTransformation.booleanUnbox(var2[118].callGetProperty(var2[119].callGroovyObjectGetProperty(this)))) {
               Object width = var2[120].callGetProperty(term);
               if (ScriptBytecodeAdapter.compareLessThan(width, $const$2)) {
                  width = $const$3;
               }

               var2[121].call(var2[122].callGetProperty(var2[123].callGroovyObjectGetProperty(this)), var2[124].call(messages, "startup_banner.0", var2[125].callGetProperty($get$$class$org$codehaus$groovy$runtime$InvokerHelper()), var2[126].call(var2[127].callGetProperty($get$$class$java$lang$System()), (Object)"java.version")));
               var2[128].call(var2[129].callGetProperty(var2[130].callGroovyObjectGetProperty(this)), var2[131].call(messages, (Object)"startup_banner.1"));
               var2[132].call(var2[133].callGetProperty(var2[134].callGroovyObjectGetProperty(this)), var2[135].call("-", (Object)var2[136].call(width, (Object)$const$2)));
            }

            var2[137].call(this.runner);
         }

         code = $const$0;
      } catch (ExitNotification var16) {
         var2[138].call(var2[139].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[140].callGroovyObjectGetProperty(var16)}, new String[]{"Exiting w/code: ", ""})));
         code = var2[141].callGroovyObjectGetProperty(var16);
      } catch (Throwable var17) {
         var2[142].call(var2[143].callGetProperty(var2[144].callGroovyObjectGetProperty(this)), var2[145].call(messages, "info.fatal", var17));
         var2[146].call(var17, (Object)var2[147].callGetProperty(var2[148].callGroovyObjectGetProperty(this)));
         code = $const$2;
      } finally {
         ;
      }

      ValueRecorder var6 = new ValueRecorder();

      try {
         boolean var19 = ScriptBytecodeAdapter.compareNotEqual(var6.record(code, 8), (Object)null);
         var6.record(var19 ? Boolean.TRUE : Boolean.FALSE, 13);
         if (var19) {
            var6.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert code != null // This should never happen", var6), (Object)null);
         }
      } catch (Throwable var15) {
         var6.clear();
         throw var15;
      }

      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(code, $get$$class$java$lang$Integer()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$Groovysh();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$Groovysh(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public final BufferManager getBuffers() {
      return this.buffers;
   }

   public final Parser getParser() {
      return this.parser;
   }

   public final Interpreter getInterp() {
      return this.interp;
   }

   public final List getImports() {
      return this.imports;
   }

   public InteractiveShellRunner getRunner() {
      return this.runner;
   }

   public void setRunner(InteractiveShellRunner var1) {
      this.runner = var1;
   }

   public History getHistory() {
      return this.history;
   }

   public void setHistory(History var1) {
      this.history = var1;
   }

   public final Closure getDefaultResultHook() {
      return this.defaultResultHook;
   }

   public Closure getResultHook() {
      return this.resultHook;
   }

   public void setResultHook(Closure var1) {
      this.resultHook = var1;
   }

   public final Closure getDefaultErrorHook() {
      return this.defaultErrorHook;
   }

   public Closure getErrorHook() {
      return this.errorHook;
   }

   public void setErrorHook(Closure var1) {
      this.errorHook = var1;
   }

   // $FF: synthetic method
   public void this$3$displayBuffer(List var1) {
      this.displayBuffer(var1);
   }

   // $FF: synthetic method
   public String this$3$renderPrompt() {
      return this.renderPrompt();
   }

   // $FF: synthetic method
   public String this$3$formatLineNumber(int var1) {
      return this.formatLineNumber(var1);
   }

   // $FF: synthetic method
   public void this$3$loadUserScript(String var1) {
      this.loadUserScript(var1);
   }

   // $FF: synthetic method
   public void this$3$maybeRecordInput(String var1) {
      this.maybeRecordInput(var1);
   }

   // $FF: synthetic method
   public void this$3$maybeRecordResult(Object var1) {
      this.maybeRecordResult(var1);
   }

   // $FF: synthetic method
   public void this$3$maybeRecordError(Throwable var1) {
      this.maybeRecordError(var1);
   }

   // $FF: synthetic method
   public void this$3$setLastResult(Object var1) {
      this.setLastResult(var1);
   }

   // $FF: synthetic method
   public Object this$3$getLastResult() {
      return this.getLastResult();
   }

   // $FF: synthetic method
   public void this$3$displayError(Throwable var1) {
      this.displayError(var1);
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$2$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public CommandRegistry super$2$getRegistry() {
      return super.getRegistry();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public List super$2$parseLine(String var1) {
      return super.parseLine(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isExecutable(String var1) {
      return super.isExecutable(var1);
   }

   // $FF: synthetic method
   public Object super$2$this$dist$get$2(String var1) {
      return super.this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public Object super$2$leftShift(String var1) {
      return super.leftShift(var1);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public Command super$2$findCommand(String var1) {
      return super.findCommand(var1);
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
   public IO super$2$getIo() {
      return super.getIo();
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public Object super$2$leftShift(Command var1) {
      return super.leftShift(var1);
   }

   // $FF: synthetic method
   public MetaClass super$2$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Command super$2$register(Command var1) {
      return super.register(var1);
   }

   // $FF: synthetic method
   public Object super$2$execute(String var1) {
      return super.execute(var1);
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   public Object super$2$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
      var0[3] = "call";
      var0[4] = "createDefaultRegistrar";
      var0[5] = "contextClassLoader";
      var0[6] = "currentThread";
      var0[7] = "<$constructor$>";
      var0[8] = "<$constructor$>";
      var0[9] = "<$constructor$>";
      var0[10] = "systemInstall";
      var0[11] = "setDetector";
      var0[12] = "<$constructor$>";
      var0[13] = "size";
      var0[14] = "trim";
      var0[15] = "maybeRecordInput";
      var0[16] = "isExecutable";
      var0[17] = "executeCommand";
      var0[18] = "plus";
      var0[19] = "current";
      var0[20] = "leftShift";
      var0[21] = "parse";
      var0[22] = "plus";
      var0[23] = "code";
      var0[24] = "COMPLETE";
      var0[25] = "debug";
      var0[26] = "log";
      var0[27] = "verbose";
      var0[28] = "io";
      var0[29] = "displayBuffer";
      var0[30] = "buffer";
      var0[31] = "plus";
      var0[32] = "plus";
      var0[33] = "evaluate";
      var0[34] = "clearSelected";
      var0[35] = "INCOMPLETE";
      var0[36] = "updateSelected";
      var0[37] = "ERROR";
      var0[38] = "cause";
      var0[39] = "<$constructor$>";
      var0[40] = "code";
      var0[41] = "eachWithIndex";
      var0[42] = "formatLineNumber";
      var0[43] = "size";
      var0[44] = "current";
      var0[45] = "render";
      var0[46] = "padLeft";
      var0[47] = "toString";
      var0[48] = "<$constructor$>";
      var0[49] = "getProperty";
      var0[50] = "<$constructor$>";
      var0[51] = "canonicalFile";
      var0[52] = "<$constructor$>";
      var0[53] = "userStateDirectory";
      var0[54] = "exists";
      var0[55] = "getAt";
      var0[56] = "registry";
      var0[57] = "debug";
      var0[58] = "log";
      var0[59] = "load";
      var0[60] = "toURL";
      var0[61] = "toURI";
      var0[62] = "error";
      var0[63] = "log";
      var0[64] = "getAt";
      var0[65] = "registry";
      var0[66] = "recordInput";
      var0[67] = "getAt";
      var0[68] = "registry";
      var0[69] = "recordResult";
      var0[70] = "getAt";
      var0[71] = "registry";
      var0[72] = "sanitizeStackTrace";
      var0[73] = "deepSanitize";
      var0[74] = "recordError";
      var0[75] = "<$constructor$>";
      var0[76] = "call";
      var0[77] = "putAt";
      var0[78] = "context";
      var0[79] = "maybeRecordResult";
      var0[80] = "getAt";
      var0[81] = "context";
      var0[82] = "<$constructor$>";
      var0[83] = "call";
      var0[84] = "length";
      var0[85] = "join";
      var0[86] = "run";
      var0[87] = "terminal";
      var0[88] = "debug";
      var0[89] = "log";
      var0[90] = "debug";
      var0[91] = "log";
      var0[92] = "debug";
      var0[93] = "log";
      var0[94] = "supported";
      var0[95] = "debug";
      var0[96] = "log";
      var0[97] = "echo";
      var0[98] = "echoEnabled";
      var0[99] = "debug";
      var0[100] = "log";
      var0[101] = "terminalHeight";
      var0[102] = "terminalWidth";
      var0[103] = "debug";
      var0[104] = "log";
      var0[105] = "isANSISupported";
      var0[106] = "debug";
      var0[107] = "log";
      var0[108] = "directConsole";
      var0[109] = "loadUserScript";
      var0[110] = "size";
      var0[111] = "trim";
      var0[112] = "execute";
      var0[113] = "loadUserScript";
      var0[114] = "<$constructor$>";
      var0[115] = "<$constructor$>";
      var0[116] = "<$constructor$>";
      var0[117] = "userStateDirectory";
      var0[118] = "quiet";
      var0[119] = "io";
      var0[120] = "terminalWidth";
      var0[121] = "println";
      var0[122] = "out";
      var0[123] = "io";
      var0[124] = "format";
      var0[125] = "version";
      var0[126] = "getAt";
      var0[127] = "properties";
      var0[128] = "println";
      var0[129] = "out";
      var0[130] = "io";
      var0[131] = "getAt";
      var0[132] = "println";
      var0[133] = "out";
      var0[134] = "io";
      var0[135] = "multiply";
      var0[136] = "minus";
      var0[137] = "run";
      var0[138] = "debug";
      var0[139] = "log";
      var0[140] = "code";
      var0[141] = "code";
      var0[142] = "println";
      var0[143] = "err";
      var0[144] = "io";
      var0[145] = "format";
      var0[146] = "printStackTrace";
      var0[147] = "err";
      var0[148] = "io";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[149];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ParseCode() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ParseCode;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ParseCode = class$("org.codehaus.groovy.tools.shell.ParseCode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$InteractiveShellRunner() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$InteractiveShellRunner = class$("org.codehaus.groovy.tools.shell.InteractiveShellRunner");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$fusesource$jansi$Ansi() {
      Class var10000 = $class$org$fusesource$jansi$Ansi;
      if (var10000 == null) {
         var10000 = $class$org$fusesource$jansi$Ansi = class$("com.gzoltar.shaded.org.fusesource.jansi.Ansi");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Parser() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Parser;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Parser = class$("org.codehaus.groovy.tools.shell.Parser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$AnsiDetector() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$AnsiDetector;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$AnsiDetector = class$("org.codehaus.groovy.tools.shell.AnsiDetector");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
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
   private static Class $get$$class$jline$Terminal() {
      Class var10000 = $class$jline$Terminal;
      if (var10000 == null) {
         var10000 = $class$jline$Terminal = class$("com.gzoltar.shaded.jline.Terminal");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$MessageSource() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$MessageSource;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$MessageSource = class$("org.codehaus.groovy.tools.shell.util.MessageSource");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh = class$("org.codehaus.groovy.tools.shell.Groovysh");
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
   private static Class $get$$class$java$lang$Error() {
      Class var10000 = $class$java$lang$Error;
      if (var10000 == null) {
         var10000 = $class$java$lang$Error = class$("java.lang.Error");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Thread() {
      Class var10000 = $class$java$lang$Thread;
      if (var10000 == null) {
         var10000 = $class$java$lang$Thread = class$("java.lang.Thread");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$jline$History() {
      Class var10000 = $class$jline$History;
      if (var10000 == null) {
         var10000 = $class$jline$History = class$("com.gzoltar.shaded.jline.History");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Binding() {
      Class var10000 = $class$groovy$lang$Binding;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Binding = class$("groovy.lang.Binding");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$IllegalStateException() {
      Class var10000 = $class$java$lang$IllegalStateException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalStateException = class$("java.lang.IllegalStateException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$fusesource$jansi$AnsiConsole() {
      Class var10000 = $class$org$fusesource$jansi$AnsiConsole;
      if (var10000 == null) {
         var10000 = $class$org$fusesource$jansi$AnsiConsole = class$("com.gzoltar.shaded.org.fusesource.jansi.AnsiConsole");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Interpreter() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter = class$("org.codehaus.groovy.tools.shell.Interpreter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Throwable() {
      Class var10000 = $class$java$lang$Throwable;
      if (var10000 == null) {
         var10000 = $class$java$lang$Throwable = class$("java.lang.Throwable");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$runtime$StackTraceUtils() {
      Class var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils = class$("org.codehaus.groovy.runtime.StackTraceUtils");
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
   private static Class $get$$class$org$fusesource$jansi$AnsiRenderer() {
      Class var10000 = $class$org$fusesource$jansi$AnsiRenderer;
      if (var10000 == null) {
         var10000 = $class$org$fusesource$jansi$AnsiRenderer = class$("com.gzoltar.shaded.org.fusesource.jansi.AnsiRenderer");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Preferences() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences = class$("org.codehaus.groovy.tools.shell.util.Preferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$BufferManager() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$BufferManager;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$BufferManager = class$("org.codehaus.groovy.tools.shell.BufferManager");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$IO() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$IO;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$IO = class$("org.codehaus.groovy.tools.shell.IO");
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
      private static Class $class$java$lang$Boolean;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
      // $FF: synthetic field
      private static Class $class$java$lang$String;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$Groovysh$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object result) {
         Object resultx = new Reference(result);
         CallSite[] var3 = $getCallSiteArray();
         Boolean showLastResult = (Boolean)ScriptBytecodeAdapter.castToType(!DefaultTypeTransformation.booleanUnbox(var3[0].callGetProperty(var3[1].callGroovyObjectGetProperty(this))) && DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var3[2].callGetProperty(var3[3].callGroovyObjectGetProperty(this))) && !DefaultTypeTransformation.booleanUnbox(var3[4].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$util$Preferences())) ? Boolean.FALSE : Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean());
         return DefaultTypeTransformation.booleanUnbox(showLastResult) ? var3[5].call(var3[6].callGetProperty(var3[7].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var3[8].call($get$$class$java$lang$String(), (Object)resultx.get())}, new String[]{"@|bold ===>|@ ", ""}))) : null;
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure1()) {
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
         var0[0] = "quiet";
         var0[1] = "io";
         var0[2] = "verbose";
         var0[3] = "io";
         var0[4] = "showLastResult";
         var0[5] = "println";
         var0[6] = "out";
         var0[7] = "io";
         var0[8] = "valueOf";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[9];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure1(), var0);
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
      private static Class $get$$class$java$lang$Boolean() {
         Class var10000 = $class$java$lang$Boolean;
         if (var10000 == null) {
            var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Preferences() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences = class$("org.codehaus.groovy.tools.shell.util.Preferences");
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_closure1 = class$("org.codehaus.groovy.tools.shell.Groovysh$_closure1");
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
      private static final Integer $const$0 = (Integer)-1;
      // $FF: synthetic field
      private static final Integer $const$1 = (Integer)0;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$Groovysh$_closure2;
      // $FF: synthetic field
      private static Class $class$java$lang$Boolean;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$Interpreter;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
      // $FF: synthetic field
      private static Class $class$java$lang$Throwable;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$runtime$StackTraceUtils;
      // $FF: synthetic field
      private static Class $class$java$lang$StringBuffer;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Throwable cause) {
         Throwable causex = new Reference(cause);
         CallSite[] var3 = $getCallSiteArray();
         ValueRecorder var4 = new ValueRecorder();

         try {
            boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var4.record(causex.get(), 8), (Object)null);
            var4.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 14);
            if (var10000) {
               var4.clear();
            } else {
               ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert cause != null", var4), (Object)null);
            }
         } catch (Throwable var11) {
            var4.clear();
            throw var11;
         }

         var3[0].call(var3[1].callGetProperty(var3[2].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var3[3].callGetProperty(var3[4].callGetProperty(causex.get()))}, new String[]{"@|bold,red ERROR|@ ", ":"})));
         var3[5].call(var3[6].callGetProperty(var3[7].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var3[8].callGetProperty(causex.get())}, new String[]{"@|bold,red ", "|@"})));
         var3[9].callCurrent(this, (Object)causex.get());
         if (DefaultTypeTransformation.booleanUnbox(var3[10].callGetProperty(var3[11].callGroovyObjectGetProperty(this)))) {
            return var3[12].call(var3[13].callGroovyObjectGetProperty(this), causex.get());
         } else {
            Boolean sanitize = (Boolean)ScriptBytecodeAdapter.castToType(var3[14].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$util$Preferences()), $get$$class$java$lang$Boolean());
            if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var3[15].callGetProperty(var3[16].callGroovyObjectGetProperty(this))) && DefaultTypeTransformation.booleanUnbox(sanitize) ? Boolean.TRUE : Boolean.FALSE)) {
               causex.set((Throwable)ScriptBytecodeAdapter.castToType(var3[17].call($get$$class$org$codehaus$groovy$runtime$StackTraceUtils(), (Object)causex.get()), $get$$class$java$lang$Throwable()));
            }

            Object trace = new Reference(var3[18].callGetProperty(causex.get()));
            Object buff = new Reference(var3[19].callConstructor($get$$class$java$lang$StringBuffer()));
            Object e = new Reference((Object)null);
            Object var9 = var3[20].call(trace.get());

            while(((Iterator)var9).hasNext()) {
               e.set(((Iterator)var9).next());
               var3[21].call(buff.get(), (Object)(new GStringImpl(new Object[]{var3[22].callGetProperty(e.get()), var3[23].callGetProperty(e.get())}, new String[]{"        @|bold at|@ ", ".", " (@|bold "})));
               var3[24].call(buff.get(), DefaultTypeTransformation.booleanUnbox(var3[25].callGetProperty(e.get())) ? "Native Method" : (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareNotEqual(var3[26].callGetProperty(e.get()), (Object)null) && ScriptBytecodeAdapter.compareNotEqual(var3[27].callGetProperty(e.get()), $const$0) ? Boolean.TRUE : Boolean.FALSE) ? new GStringImpl(new Object[]{var3[28].callGetProperty(e.get()), var3[29].callGetProperty(e.get())}, new String[]{"", ":", ""}) : (ScriptBytecodeAdapter.compareNotEqual(var3[30].callGetProperty(e.get()), (Object)null) ? var3[31].callGetProperty(e.get()) : "Unknown Source")));
               var3[32].call(buff.get(), (Object)"|@)");
               var3[33].call(var3[34].callGetProperty(var3[35].callGroovyObjectGetProperty(this)), buff.get());
               var3[36].call(buff.get(), (Object)$const$1);
               if (DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[37].callGetProperty(e.get()), var3[38].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$Interpreter())) && ScriptBytecodeAdapter.compareEqual(var3[39].callGetProperty(e.get()), "run") ? Boolean.TRUE : Boolean.FALSE)) {
                  var3[40].call(var3[41].callGetProperty(var3[42].callGroovyObjectGetProperty(this)), (Object)"        @|bold ...|@");
                  break;
               }
            }

            return null;
         }
      }

      public Object call(Throwable cause) {
         Throwable causex = new Reference(cause);
         CallSite[] var3 = $getCallSiteArray();
         return var3[43].callCurrent(this, (Object)causex.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure2()) {
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
         var0[0] = "println";
         var0[1] = "err";
         var0[2] = "io";
         var0[3] = "name";
         var0[4] = "class";
         var0[5] = "println";
         var0[6] = "err";
         var0[7] = "io";
         var0[8] = "message";
         var0[9] = "maybeRecordError";
         var0[10] = "debug";
         var0[11] = "log";
         var0[12] = "debug";
         var0[13] = "log";
         var0[14] = "sanitizeStackTrace";
         var0[15] = "verbose";
         var0[16] = "io";
         var0[17] = "deepSanitize";
         var0[18] = "stackTrace";
         var0[19] = "<$constructor$>";
         var0[20] = "iterator";
         var0[21] = "leftShift";
         var0[22] = "className";
         var0[23] = "methodName";
         var0[24] = "leftShift";
         var0[25] = "nativeMethod";
         var0[26] = "fileName";
         var0[27] = "lineNumber";
         var0[28] = "fileName";
         var0[29] = "lineNumber";
         var0[30] = "fileName";
         var0[31] = "fileName";
         var0[32] = "leftShift";
         var0[33] = "println";
         var0[34] = "err";
         var0[35] = "io";
         var0[36] = "setLength";
         var0[37] = "className";
         var0[38] = "SCRIPT_FILENAME";
         var0[39] = "methodName";
         var0[40] = "println";
         var0[41] = "err";
         var0[42] = "io";
         var0[43] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[44];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure2(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$Groovysh$_closure2() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_closure2;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$Groovysh$_closure2 = class$("org.codehaus.groovy.tools.shell.Groovysh$_closure2");
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$Interpreter() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$Interpreter = class$("org.codehaus.groovy.tools.shell.Interpreter");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Preferences() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences = class$("org.codehaus.groovy.tools.shell.util.Preferences");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$java$lang$Throwable() {
         Class var10000 = $class$java$lang$Throwable;
         if (var10000 == null) {
            var10000 = $class$java$lang$Throwable = class$("java.lang.Throwable");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$codehaus$groovy$runtime$StackTraceUtils() {
         Class var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$runtime$StackTraceUtils = class$("org.codehaus.groovy.runtime.StackTraceUtils");
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
}
