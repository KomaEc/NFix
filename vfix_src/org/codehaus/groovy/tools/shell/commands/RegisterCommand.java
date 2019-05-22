package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.BufferManager;
import org.codehaus.groovy.tools.shell.Command;
import org.codehaus.groovy.tools.shell.CommandSupport;
import org.codehaus.groovy.tools.shell.Shell;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class RegisterCommand extends CommandSupport {
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
   private static final Integer $const$3 = (Integer)3;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204815L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204815 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$RegisterCommand;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Command;
   // $FF: synthetic field
   private static Class $class$java$lang$Class;

   public RegisterCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "register", "\\rc"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$CommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   public Object execute(List args) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var3.record(args, 8), (Object)null);
         var3.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 13);
         if (var10000) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert args != null", var3), (Object)null);
         }
      } catch (Throwable var9) {
         var3.clear();
         throw var9;
      }

      if (ScriptBytecodeAdapter.compareLessThan(var2[0].call(args), $const$0)) {
         var2[1].callCurrent(this, (Object)"Command 'register' requires at least 1 arguments");
      }

      String classname = (String)ScriptBytecodeAdapter.castToType(var2[2].call(args, (Object)$const$1), $get$$class$java$lang$String());
      Class type = (Class)ScriptBytecodeAdapter.castToType(var2[3].call(var2[4].callCurrent(this), (Object)classname), $get$$class$java$lang$Class());
      Command command = (Command)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$tools$shell$Command());
      if (ScriptBytecodeAdapter.compareEqual(var2[5].call(args), $const$0)) {
         command = (Command)ScriptBytecodeAdapter.castToType(var2[6].call(type, (Object)var2[7].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$Command());
      } else if (ScriptBytecodeAdapter.compareEqual(var2[8].call(args), $const$2)) {
         command = (Command)ScriptBytecodeAdapter.castToType(var2[9].call(type, var2[10].callGroovyObjectGetProperty(this), var2[11].call(args, (Object)$const$0), (Object)null), $get$$class$org$codehaus$groovy$tools$shell$Command());
      } else if (ScriptBytecodeAdapter.compareEqual(var2[12].call(args), $const$3)) {
         command = (Command)ScriptBytecodeAdapter.castToType(var2[13].call(type, var2[14].callGroovyObjectGetProperty(this), var2[15].call(args, (Object)$const$0), var2[16].call(args, (Object)$const$2)), $get$$class$org$codehaus$groovy$tools$shell$Command());
      }

      Object oldcommand = var2[17].call(var2[18].callGroovyObjectGetProperty(this), var2[19].callGetProperty(command));
      if (DefaultTypeTransformation.booleanUnbox(oldcommand)) {
         var2[20].callCurrent(this, (Object)(new GStringImpl(new Object[]{var2[21].callGetProperty(command)}, new String[]{"Can not rebind command: ", ""})));
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[22].callGetProperty(var2[23].callGroovyObjectGetProperty(this)))) {
         var2[24].call(var2[25].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[26].callGetProperty(command), command}, new String[]{"Created command '", "': ", ""})));
      }

      command = (Command)ScriptBytecodeAdapter.castToType(var2[27].call(var2[28].callGroovyObjectGetProperty(this), (Object)command), $get$$class$org$codehaus$groovy$tools$shell$Command());
      return DefaultTypeTransformation.booleanUnbox(var2[29].callGroovyObjectGetProperty(var2[30].callGroovyObjectGetProperty(this))) ? (Object)ScriptBytecodeAdapter.castToType(var2[31].call(var2[32].callGetProperty(var2[33].callGroovyObjectGetProperty(var2[34].callGroovyObjectGetProperty(this))), (Object)command), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public BufferManager super$2$getBuffers() {
      return super.getBuffers();
   }

   // $FF: synthetic method
   public String super$2$getHelp() {
      return super.getHelp();
   }

   // $FF: synthetic method
   public History super$2$getHistory() {
      return super.getHistory();
   }

   // $FF: synthetic method
   public List super$2$createCompletors() {
      return super.createCompletors();
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
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Map super$2$getVariables() {
      return super.getVariables();
   }

   // $FF: synthetic method
   public List super$2$getBuffer() {
      return super.getBuffer();
   }

   // $FF: synthetic method
   public String super$2$getDescription() {
      return super.getDescription();
   }

   // $FF: synthetic method
   public String super$2$getShortcut() {
      return super.getShortcut();
   }

   // $FF: synthetic method
   public Object super$2$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Object super$2$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public String super$2$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$2$fail(String var1, Throwable var2) {
      super.fail(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$fail(String var1) {
      super.fail(var1);
   }

   // $FF: synthetic method
   public void super$2$assertNoArguments(List var1) {
      super.assertNoArguments(var1);
   }

   // $FF: synthetic method
   public void super$2$alias(String var1, String var2) {
      super.alias(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$2$setHidden(boolean var1) {
      super.setHidden(var1);
   }

   // $FF: synthetic method
   public Completor super$2$getCompletor() {
      return super.getCompletor();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$get$2(String var1) {
      return super.this$dist$get$2(var1);
   }

   // $FF: synthetic method
   public String super$2$getUsage() {
      return super.getUsage();
   }

   // $FF: synthetic method
   public GroovyClassLoader super$2$getClassLoader() {
      return super.getClassLoader();
   }

   // $FF: synthetic method
   public boolean super$2$isHidden() {
      return super.isHidden();
   }

   // $FF: synthetic method
   public boolean super$2$getHidden() {
      return super.getHidden();
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public Binding super$2$getBinding() {
      return super.getBinding();
   }

   // $FF: synthetic method
   public List super$2$getImports() {
      return super.getImports();
   }

   // $FF: synthetic method
   public List super$2$getAliases() {
      return super.getAliases();
   }

   // $FF: synthetic method
   public MetaClass super$2$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "size";
      var0[1] = "fail";
      var0[2] = "get";
      var0[3] = "loadClass";
      var0[4] = "getClassLoader";
      var0[5] = "size";
      var0[6] = "newInstance";
      var0[7] = "shell";
      var0[8] = "size";
      var0[9] = "newInstance";
      var0[10] = "shell";
      var0[11] = "get";
      var0[12] = "size";
      var0[13] = "newInstance";
      var0[14] = "shell";
      var0[15] = "get";
      var0[16] = "get";
      var0[17] = "getAt";
      var0[18] = "registry";
      var0[19] = "name";
      var0[20] = "fail";
      var0[21] = "name";
      var0[22] = "debugEnabled";
      var0[23] = "log";
      var0[24] = "debug";
      var0[25] = "log";
      var0[26] = "name";
      var0[27] = "leftShift";
      var0[28] = "shell";
      var0[29] = "runner";
      var0[30] = "shell";
      var0[31] = "leftShift";
      var0[32] = "completor";
      var0[33] = "runner";
      var0[34] = "shell";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[35];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandSupport() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport = class$("org.codehaus.groovy.tools.shell.CommandSupport");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$RegisterCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$RegisterCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$RegisterCommand = class$("org.codehaus.groovy.tools.shell.commands.RegisterCommand");
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
}
