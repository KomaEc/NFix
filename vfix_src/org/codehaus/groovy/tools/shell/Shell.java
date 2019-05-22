package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class Shell implements GroovyObject {
   protected final Logger log;
   private final CommandRegistry registry;
   private final IO io;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)-1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204061L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204061 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandRegistry;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Shell;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Command;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO;

   public Shell(IO io) {
      CallSite[] var2 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var2[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var2[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.registry = (CommandRegistry)ScriptBytecodeAdapter.castToType(var2[2].callConstructor($get$$class$org$codehaus$groovy$tools$shell$CommandRegistry()), $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(io, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert io", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      this.io = (IO)ScriptBytecodeAdapter.castToType(io, $get$$class$org$codehaus$groovy$tools$shell$IO());
   }

   public Shell() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{var1[3].callConstructor($get$$class$org$codehaus$groovy$tools$shell$IO())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$org$codehaus$groovy$tools$shell$Shell());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((IO)var10001[0]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   protected List parseLine(String line) {
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
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      return (List)ScriptBytecodeAdapter.castToType(var2[4].call(var2[5].call(line)), $get$$class$java$util$List());
   }

   public Command findCommand(String line) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(line, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert line", var3), (Object)null);
         }
      } catch (Throwable var13) {
         var3.clear();
         throw var13;
      }

      Object args = var2[6].callCurrent(this, (Object)line);
      ValueRecorder var5 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareGreaterThan(var5.record(var2[7].call(var5.record(args, 8)), 13), $const$0);
         var5.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 20);
         if (var10000) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert args.size() > 0", var5), (Object)null);
         }
      } catch (Throwable var12) {
         var5.clear();
         throw var12;
      }

      Object name = var2[8].call(args, (Object)$const$0);
      Object command = var2[9].call(this.registry, (Object)name);
      return (Command)ScriptBytecodeAdapter.castToType(command, $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   public boolean isExecutable(String line) {
      CallSite[] var2 = $getCallSiteArray();
      return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.compareNotEqual(var2[10].callCurrent(this, (Object)line), (Object)null) ? Boolean.TRUE : Boolean.FALSE, $get$$class$java$lang$Boolean()));
   }

   public Object execute(String line) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(line, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert line", var3), (Object)null);
         }
      } catch (Throwable var8) {
         var3.clear();
         throw var8;
      }

      Object command = var2[11].callCurrent(this, (Object)line);
      Object result = null;
      if (DefaultTypeTransformation.booleanUnbox(command)) {
         Object args = var2[12].callCurrent(this, (Object)line);
         if (ScriptBytecodeAdapter.compareEqual(var2[13].call(args), $const$1)) {
            args = ScriptBytecodeAdapter.createList(new Object[0]);
         } else {
            args = var2[14].call(args, (Object)ScriptBytecodeAdapter.createRange($const$1, $const$2, true));
         }

         var2[15].call(this.log, (Object)(new GStringImpl(new Object[]{var2[16].callGetProperty(command), command, args}, new String[]{"Executing command(", "): ", "; w/args: ", ""})));
         result = var2[17].call(command, args);
         var2[18].call(this.log, (Object)(new GStringImpl(new Object[]{var2[19].call($get$$class$java$lang$String(), (Object)result)}, new String[]{"Result: ", ""})));
      }

      return (Object)ScriptBytecodeAdapter.castToType(result, $get$$class$java$lang$Object());
   }

   public Command register(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      return (Command)ScriptBytecodeAdapter.castToType(var2[20].call(this.registry, (Object)command), $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   public Object leftShift(String line) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[21].callCurrent(this, (Object)line);
   }

   public Object leftShift(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[22].callCurrent(this, (Object)command);
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$Shell()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$Shell();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$Shell(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$Shell(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public final CommandRegistry getRegistry() {
      return this.registry;
   }

   public final IO getIo() {
      return this.io;
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
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "tokenize";
      var0[5] = "trim";
      var0[6] = "parseLine";
      var0[7] = "size";
      var0[8] = "getAt";
      var0[9] = "getAt";
      var0[10] = "findCommand";
      var0[11] = "findCommand";
      var0[12] = "parseLine";
      var0[13] = "size";
      var0[14] = "getAt";
      var0[15] = "debug";
      var0[16] = "name";
      var0[17] = "execute";
      var0[18] = "debug";
      var0[19] = "valueOf";
      var0[20] = "leftShift";
      var0[21] = "execute";
      var0[22] = "register";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[23];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$Shell(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandRegistry;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandRegistry = class$("org.codehaus.groovy.tools.shell.CommandRegistry");
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
   private static Class $get$$class$java$lang$Boolean() {
      Class var10000 = $class$java$lang$Boolean;
      if (var10000 == null) {
         var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Shell() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Shell;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Shell = class$("org.codehaus.groovy.tools.shell.Shell");
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
