package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import java.awt.HeadlessException;
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
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class InspectCommand extends CommandSupport {
   private Object lafInitialized;
   private Object headless;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204809L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204809 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$java$awt$Frame;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$ObjectBrowser;
   // $FF: synthetic field
   private static Class $class$javax$swing$UIManager;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$InspectCommandCompletor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$InspectCommand;

   public InspectCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "inspect", "\\n"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$CommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.lafInitialized = Boolean.FALSE;
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   protected List createCompletors() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommandCompletor(), (Object)var1[1].callGroovyObjectGetProperty(this)), null}), $get$$class$java$util$List());
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
      } catch (Throwable var14) {
         var3.clear();
         throw var14;
      }

      var2[2].call(var2[3].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{args}, new String[]{"Inspecting w/args: ", ""})));
      if (ScriptBytecodeAdapter.compareGreaterThan(var2[4].call(args), $const$0)) {
         var2[5].callCurrent(this, (Object)var2[6].call(var2[7].callGroovyObjectGetProperty(this), "error.unexpected_args", var2[8].call(args, (Object)" ")));
      }

      Object subject = null;
      if (ScriptBytecodeAdapter.compareEqual(var2[9].call(args), $const$0)) {
         subject = var2[10].call(var2[11].callGroovyObjectGetProperty(var2[12].callGroovyObjectGetProperty(this)), var2[13].call(args, (Object)$const$1));
      } else {
         subject = var2[14].call(var2[15].callGroovyObjectGetProperty(var2[16].callGroovyObjectGetProperty(this)), (Object)"_");
      }

      if (!DefaultTypeTransformation.booleanUnbox(subject)) {
         return (Object)ScriptBytecodeAdapter.castToType(var2[17].call(var2[18].callGetProperty(var2[19].callGroovyObjectGetProperty(this)), (Object)"Subject is null; nothing to inspect"), $get$$class$java$lang$Object());
      } else {
         if (!DefaultTypeTransformation.booleanUnbox(this.lafInitialized)) {
            this.lafInitialized = Boolean.TRUE;

            try {
               var2[20].call($get$$class$javax$swing$UIManager(), (Object)var2[21].call($get$$class$javax$swing$UIManager()));
               var2[22].call(var2[23].callConstructor($get$$class$java$awt$Frame()));
               this.headless = Boolean.FALSE;
            } catch (HeadlessException var12) {
               this.headless = Boolean.TRUE;
            } finally {
               ;
            }
         }

         if (DefaultTypeTransformation.booleanUnbox(this.headless)) {
            var2[24].call(var2[25].callGetProperty(var2[26].callGroovyObjectGetProperty(this)), (Object)"@|red ERROR:|@ Running in AWT Headless mode, 'inspect' is not available.");
            return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
         } else {
            if (DefaultTypeTransformation.booleanUnbox(var2[27].callGetProperty(var2[28].callGroovyObjectGetProperty(this)))) {
               var2[29].call(var2[30].callGetProperty(var2[31].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{subject}, new String[]{"Launching object browser to inspect: ", ""})));
            }

            return (Object)ScriptBytecodeAdapter.castToType(var2[32].call($get$$class$groovy$inspect$swingui$ObjectBrowser(), (Object)subject), $get$$class$java$lang$Object());
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getLafInitialized() {
      return this.lafInitialized;
   }

   public void setLafInitialized(Object var1) {
      this.lafInitialized = var1;
   }

   public Object getHeadless() {
      return this.headless;
   }

   public void setHeadless(Object var1) {
      this.headless = var1;
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
      var0[1] = "binding";
      var0[2] = "debug";
      var0[3] = "log";
      var0[4] = "size";
      var0[5] = "fail";
      var0[6] = "format";
      var0[7] = "messages";
      var0[8] = "join";
      var0[9] = "size";
      var0[10] = "getAt";
      var0[11] = "variables";
      var0[12] = "binding";
      var0[13] = "getAt";
      var0[14] = "getAt";
      var0[15] = "variables";
      var0[16] = "binding";
      var0[17] = "println";
      var0[18] = "out";
      var0[19] = "io";
      var0[20] = "setLookAndFeel";
      var0[21] = "getSystemLookAndFeelClassName";
      var0[22] = "dispose";
      var0[23] = "<$constructor$>";
      var0[24] = "println";
      var0[25] = "err";
      var0[26] = "io";
      var0[27] = "verbose";
      var0[28] = "io";
      var0[29] = "println";
      var0[30] = "out";
      var0[31] = "io";
      var0[32] = "inspect";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[33];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand(), var0);
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
   private static Class $get$$class$java$awt$Frame() {
      Class var10000 = $class$java$awt$Frame;
      if (var10000 == null) {
         var10000 = $class$java$awt$Frame = class$("java.awt.Frame");
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
   private static Class $get$$class$groovy$inspect$swingui$ObjectBrowser() {
      Class var10000 = $class$groovy$inspect$swingui$ObjectBrowser;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$ObjectBrowser = class$("groovy.inspect.swingui.ObjectBrowser");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommandCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$InspectCommandCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$InspectCommandCompletor = class$("org.codehaus.groovy.tools.shell.commands.InspectCommandCompletor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$InspectCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$InspectCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$InspectCommand = class$("org.codehaus.groovy.tools.shell.commands.InspectCommand");
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
