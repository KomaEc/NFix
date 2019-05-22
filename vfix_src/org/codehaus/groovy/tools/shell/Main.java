package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
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

public class Main implements GroovyObject {
   private static final MessageSource messages = (MessageSource)$getCallSiteArray()[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$MessageSource(), (Object)$get$$class$org$codehaus$groovy$tools$shell$Main());
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)2;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204013L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204013 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$SecurityManager;
   // $FF: synthetic field
   private static Class $class$org$fusesource$jansi$Ansi;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Main;
   // $FF: synthetic field
   private static Class $class$org$fusesource$jansi$AnsiConsole;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$AnsiDetector;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO$Verbosity;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class array$$class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$MessageSource;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$HelpFormatter;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$NoExitSecurityManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Groovysh;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$runtime$InvokerHelper;
   // $FF: synthetic field
   private static Class $class$groovy$util$CliBuilder;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO;

   public Main() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   static {
      $getCallSiteArray()[1].call($get$$class$org$fusesource$jansi$AnsiConsole());
      $getCallSiteArray()[2].call($get$$class$org$fusesource$jansi$Ansi(), (Object)$getCallSiteArray()[3].callConstructor($get$$class$org$codehaus$groovy$tools$shell$AnsiDetector()));
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      IO io = new Reference(var1[4].callConstructor($get$$class$org$codehaus$groovy$tools$shell$IO()));
      ScriptBytecodeAdapter.setProperty(io.get(), $get$$class$org$codehaus$groovy$tools$shell$Main(), $get$$class$org$codehaus$groovy$tools$shell$util$Logger(), "io");
      Object cli = var1[5].callConstructor($get$$class$groovy$util$CliBuilder(), (Object)ScriptBytecodeAdapter.createMap(new Object[]{"usage", "groovysh [options] [...]", "formatter", var1[6].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter()), "writer", var1[7].callGetProperty(io.get())}));
      var1[8].call(cli, var1[9].call(messages, (Object)"cli.option.classpath.description"));
      var1[10].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "classpath"}), var1[11].call(messages, (Object)"cli.option.cp.description"));
      var1[12].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "help"}), var1[13].call(messages, (Object)"cli.option.help.description"));
      var1[14].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "version"}), var1[15].call(messages, (Object)"cli.option.version.description"));
      var1[16].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "verbose"}), var1[17].call(messages, (Object)"cli.option.verbose.description"));
      var1[18].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "quiet"}), var1[19].call(messages, (Object)"cli.option.quiet.description"));
      var1[20].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "debug"}), var1[21].call(messages, (Object)"cli.option.debug.description"));
      var1[22].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "color", "args", $const$0, "argName", "FLAG", "optionalArg", Boolean.TRUE}), var1[23].call(messages, (Object)"cli.option.color.description"));
      var1[24].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "define", "args", $const$0, "argName", "NAME=VALUE"}), var1[25].call(messages, (Object)"cli.option.define.description"));
      var1[26].call(cli, ScriptBytecodeAdapter.createMap(new Object[]{"longOpt", "terminal", "args", $const$0, "argName", "TYPE"}), var1[27].call(messages, (Object)"cli.option.terminal.description"));
      Object options = var1[28].call(cli, (Object)args);
      if (DefaultTypeTransformation.booleanUnbox(var1[29].callGetProperty(options))) {
         var1[30].call(cli);
         var1[31].call($get$$class$java$lang$System(), (Object)$const$1);
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[32].callGetProperty(options))) {
         var1[33].call(var1[34].callGetProperty(io.get()), var1[35].call(messages, "cli.info.version", var1[36].callGetProperty($get$$class$org$codehaus$groovy$runtime$InvokerHelper())));
         var1[37].call($get$$class$java$lang$System(), (Object)$const$1);
      }

      Object value;
      if (DefaultTypeTransformation.booleanUnbox(var1[38].call(options, (Object)"T"))) {
         value = var1[39].call(options, (Object)"T");
         var1[40].callStatic($get$$class$org$codehaus$groovy$tools$shell$Main(), value);
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[41].call(options, (Object)"D"))) {
         value = var1[42].call(options, (Object)"D");
         var1[43].call(value, (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$shell$Main(), $get$$class$org$codehaus$groovy$tools$shell$Main()) {
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$Main$_main_closure1;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$Main;
            // $FF: synthetic field
            private static Class $class$java$lang$String;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].callStatic($get$$class$org$codehaus$groovy$tools$shell$Main(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.asType(itx.get(), $get$$class$java$lang$String()), $get$$class$java$lang$String()));
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure1()) {
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
               var0[0] = "setSystemProperty";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure1(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure1() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$Main$_main_closure1;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$Main$_main_closure1 = class$("org.codehaus.groovy.tools.shell.Main$_main_closure1");
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
            private static Class $get$$class$org$codehaus$groovy$tools$shell$Main() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$Main;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$Main = class$("org.codehaus.groovy.tools.shell.Main");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[44].callGetProperty(options))) {
         ScriptBytecodeAdapter.setProperty(var1[45].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$shell$Main(), io.get(), "verbosity");
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[46].callGetProperty(options))) {
         ScriptBytecodeAdapter.setProperty(var1[47].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$shell$Main(), io.get(), "verbosity");
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[48].callGetProperty(options))) {
         ScriptBytecodeAdapter.setProperty(var1[49].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity()), $get$$class$org$codehaus$groovy$tools$shell$Main(), io.get(), "verbosity");
      }

      if (DefaultTypeTransformation.booleanUnbox(var1[50].call(options, (Object)"C"))) {
         value = var1[51].call(options, (Object)"C");
         var1[52].callStatic($get$$class$org$codehaus$groovy$tools$shell$Main(), value);
      }

      Object code = new Reference((Object)null);
      var1[53].callStatic($get$$class$org$codehaus$groovy$tools$shell$Main(), (Object)(new GeneratedClosure($get$$class$org$codehaus$groovy$tools$shell$Main(), $get$$class$org$codehaus$groovy$tools$shell$Main(), code, io) {
         private Reference<T> code;
         private Reference<T> io;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$Main$_main_closure2;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$IO;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.code = (Reference)code;
            this.io = (Reference)io;
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            if (ScriptBytecodeAdapter.compareEqual(this.code.get(), (Object)null)) {
               var2[0].call(var2[1].callGetProperty(this.io.get()));
               var2[2].call(var2[3].callGetProperty(this.io.get()), (Object)"@|red WARNING:|@ Abnormal JVM shutdown detected");
            }

            return var2[4].call(this.io.get());
         }

         public Object getCode() {
            CallSite[] var1 = $getCallSiteArray();
            return this.code.get();
         }

         public IO getIo() {
            CallSite[] var1 = $getCallSiteArray();
            return (IO)ScriptBytecodeAdapter.castToType(this.io.get(), $get$$class$org$codehaus$groovy$tools$shell$IO());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[5].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure2()) {
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
            var0[2] = "println";
            var0[3] = "err";
            var0[4] = "flush";
            var0[5] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure2(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$Main$_main_closure2() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$Main$_main_closure2;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$Main$_main_closure2 = class$("org.codehaus.groovy.tools.shell.Main$_main_closure2");
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
      }));
      Groovysh shell = var1[54].callConstructor($get$$class$org$codehaus$groovy$tools$shell$Groovysh(), (Object)io.get());
      SecurityManager psm = (SecurityManager)ScriptBytecodeAdapter.castToType(var1[55].call($get$$class$java$lang$System()), $get$$class$java$lang$SecurityManager());
      var1[56].call($get$$class$java$lang$System(), (Object)var1[57].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$NoExitSecurityManager()));
      boolean var10 = false;

      try {
         var10 = true;
         code.set(var1[58].call(shell, (Object)ScriptBytecodeAdapter.createPojoWrapper((String[])ScriptBytecodeAdapter.asType(var1[59].call(options), $get$array$$class$java$lang$String()), $get$array$$class$java$lang$String())));
         var10 = false;
      } finally {
         if (var10) {
            var1[61].call($get$$class$java$lang$System(), (Object)psm);
         }
      }

      var1[60].call($get$$class$java$lang$System(), (Object)psm);
      var1[62].call($get$$class$java$lang$System(), (Object)code.get());
   }

   public static void setTerminalType(String type) {
      CallSite[] var1 = $getCallSiteArray();
      ValueRecorder var2 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var2.record(type, 8), (Object)null);
         var2.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 13);
         if (var10000) {
            var2.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert type != null", var2), (Object)null);
         }
      } catch (Throwable var5) {
         var2.clear();
         throw var5;
      }

      type = (String)ScriptBytecodeAdapter.castToType(var1[63].call(type), $get$$class$java$lang$String());
      if (ScriptBytecodeAdapter.isCase(type, "auto")) {
         type = (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      } else if (ScriptBytecodeAdapter.isCase(type, "unix")) {
         type = "com.gzoltar.shaded.jline.UnixTerminal";
      } else if (!ScriptBytecodeAdapter.isCase(type, "win") && !ScriptBytecodeAdapter.isCase(type, "windows")) {
         if (ScriptBytecodeAdapter.isCase(type, "false") || ScriptBytecodeAdapter.isCase(type, "off") || ScriptBytecodeAdapter.isCase(type, "none")) {
            type = "com.gzoltar.shaded.jline.UnsupportedTerminal";
            ScriptBytecodeAdapter.setProperty(Boolean.FALSE, $get$$class$org$codehaus$groovy$tools$shell$Main(), $get$$class$org$fusesource$jansi$Ansi(), "enabled");
         }
      } else {
         type = "com.gzoltar.shaded.jline.WindowsTerminal";
      }

      if (ScriptBytecodeAdapter.compareNotEqual(type, (Object)null)) {
         var1[64].call($get$$class$java$lang$System(), "com.gzoltar.shaded.jline.terminal", type);
      }

   }

   public static void setColor(Object value) {
      CallSite[] var1 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(value, (Object)null)) {
         value = Boolean.TRUE;
      } else {
         value = var1[65].call(var1[66].call($get$$class$java$lang$Boolean(), (Object)value));
      }

      ScriptBytecodeAdapter.setProperty(value, $get$$class$org$codehaus$groovy$tools$shell$Main(), $get$$class$org$fusesource$jansi$Ansi(), "enabled");
   }

   public static void setSystemProperty(String nameValue) {
      CallSite[] var1 = $getCallSiteArray();
      String name = (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      String value = (String)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$String());
      if (ScriptBytecodeAdapter.compareGreaterThan(var1[67].call(nameValue, (Object)"="), $const$1)) {
         Object tmp = var1[68].call(nameValue, "=", $const$2);
         name = (String)ScriptBytecodeAdapter.castToType(var1[69].call(tmp, (Object)$const$1), $get$$class$java$lang$String());
         value = (String)ScriptBytecodeAdapter.castToType(var1[70].call(tmp, (Object)$const$0), $get$$class$java$lang$String());
      } else {
         name = nameValue;
         value = (String)ScriptBytecodeAdapter.castToType(var1[71].call(var1[72].callGetProperty($get$$class$java$lang$Boolean())), $get$$class$java$lang$String());
      }

      var1[73].call($get$$class$java$lang$System(), name, value);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Main()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$Main();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$Main(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$Main(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "<$constructor$>";
      var0[1] = "systemInstall";
      var0[2] = "setDetector";
      var0[3] = "<$constructor$>";
      var0[4] = "<$constructor$>";
      var0[5] = "<$constructor$>";
      var0[6] = "<$constructor$>";
      var0[7] = "out";
      var0[8] = "classpath";
      var0[9] = "getAt";
      var0[10] = "cp";
      var0[11] = "getAt";
      var0[12] = "h";
      var0[13] = "getAt";
      var0[14] = "V";
      var0[15] = "getAt";
      var0[16] = "v";
      var0[17] = "getAt";
      var0[18] = "q";
      var0[19] = "getAt";
      var0[20] = "d";
      var0[21] = "getAt";
      var0[22] = "C";
      var0[23] = "getAt";
      var0[24] = "D";
      var0[25] = "getAt";
      var0[26] = "T";
      var0[27] = "getAt";
      var0[28] = "parse";
      var0[29] = "h";
      var0[30] = "usage";
      var0[31] = "exit";
      var0[32] = "V";
      var0[33] = "println";
      var0[34] = "out";
      var0[35] = "format";
      var0[36] = "version";
      var0[37] = "exit";
      var0[38] = "hasOption";
      var0[39] = "getOptionValue";
      var0[40] = "setTerminalType";
      var0[41] = "hasOption";
      var0[42] = "getOptionValues";
      var0[43] = "each";
      var0[44] = "v";
      var0[45] = "VERBOSE";
      var0[46] = "d";
      var0[47] = "DEBUG";
      var0[48] = "q";
      var0[49] = "QUIET";
      var0[50] = "hasOption";
      var0[51] = "getOptionValue";
      var0[52] = "setColor";
      var0[53] = "addShutdownHook";
      var0[54] = "<$constructor$>";
      var0[55] = "getSecurityManager";
      var0[56] = "setSecurityManager";
      var0[57] = "<$constructor$>";
      var0[58] = "run";
      var0[59] = "arguments";
      var0[60] = "setSecurityManager";
      var0[61] = "setSecurityManager";
      var0[62] = "exit";
      var0[63] = "toLowerCase";
      var0[64] = "setProperty";
      var0[65] = "booleanValue";
      var0[66] = "valueOf";
      var0[67] = "indexOf";
      var0[68] = "split";
      var0[69] = "getAt";
      var0[70] = "getAt";
      var0[71] = "toString";
      var0[72] = "TRUE";
      var0[73] = "setProperty";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[74];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Main(), var0);
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
   private static Class $get$$class$java$lang$SecurityManager() {
      Class var10000 = $class$java$lang$SecurityManager;
      if (var10000 == null) {
         var10000 = $class$java$lang$SecurityManager = class$("java.lang.SecurityManager");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Main() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Main;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Main = class$("org.codehaus.groovy.tools.shell.Main");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$AnsiDetector() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$AnsiDetector;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$AnsiDetector = class$("org.codehaus.groovy.tools.shell.AnsiDetector");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$IO$Verbosity() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$IO$Verbosity;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$IO$Verbosity = class$("org.codehaus.groovy.tools.shell.IO$Verbosity");
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
   private static Class $get$array$$class$java$lang$String() {
      Class var10000 = array$$class$java$lang$String;
      if (var10000 == null) {
         var10000 = array$$class$java$lang$String = class$("[Ljava.lang.String;");
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
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$HelpFormatter() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$HelpFormatter = class$("org.codehaus.groovy.tools.shell.util.HelpFormatter");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$NoExitSecurityManager() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$NoExitSecurityManager;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$NoExitSecurityManager = class$("org.codehaus.groovy.tools.shell.util.NoExitSecurityManager");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Logger() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Logger = class$("org.codehaus.groovy.tools.shell.util.Logger");
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
   private static Class $get$$class$groovy$util$CliBuilder() {
      Class var10000 = $class$groovy$util$CliBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$CliBuilder = class$("groovy.util.CliBuilder");
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
}
