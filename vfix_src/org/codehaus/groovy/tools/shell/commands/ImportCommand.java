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
import org.codehaus.groovy.tools.shell.CommandSupport;
import org.codehaus.groovy.tools.shell.Shell;

public class ImportCommand extends CommandSupport {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204806L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204806 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$ImportCommand;

   public ImportCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "import", "\\i"};
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

   protected List createCompletors() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor(), (Object)var1[1].callGetProperty(var1[2].callGroovyObjectGetProperty(var1[3].callGroovyObjectGetProperty(this)))), null}), $get$$class$java$util$List());
   }

   public Object execute(List param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "<$constructor$>";
      var0[1] = "classLoader";
      var0[2] = "interp";
      var0[3] = "shell";
      var0[4] = "isEmpty";
      var0[5] = "fail";
      var0[6] = "plus";
      var0[7] = "join";
      var0[8] = "leftShift";
      var0[9] = "parseClass";
      var0[10] = "classLoader";
      var0[11] = "join";
      var0[12] = "NEWLINE";
      var0[13] = "remove";
      var0[14] = "imports";
      var0[15] = "getAt";
      var0[16] = "debug";
      var0[17] = "log";
      var0[18] = "debug";
      var0[19] = "log";
      var0[20] = "getAt";
      var0[21] = "leftShift";
      var0[22] = "imports";
      var0[23] = "getAt";
      var0[24] = "remove";
      var0[25] = "classCache";
      var0[26] = "classLoader";
      var0[27] = "name";
      var0[28] = "getAt";
      var0[29] = "message";
      var0[30] = "debug";
      var0[31] = "log";
      var0[32] = "fail";
      var0[33] = "remove";
      var0[34] = "classCache";
      var0[35] = "classLoader";
      var0[36] = "name";
      var0[37] = "remove";
      var0[38] = "classCache";
      var0[39] = "classLoader";
      var0[40] = "name";
      var0[41] = "remove";
      var0[42] = "classCache";
      var0[43] = "classLoader";
      var0[44] = "name";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[45];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor = class$("org.codehaus.groovy.tools.shell.commands.ImportCommandCompletor");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$ImportCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$ImportCommand = class$("org.codehaus.groovy.tools.shell.commands.ImportCommand");
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
