package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Iterator;
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

public class HelpCommand extends CommandSupport {
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
   public static Long __timeStamp = (Long)1292524204802L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204802 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$HelpCommand;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$HelpCommandCompletor;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Command;

   public HelpCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "help", "\\h"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$CommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         var2[0].callCurrent(this, "?", "\\?");
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   protected List createCompletors() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[1].callConstructor($get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommandCompletor(), (Object)var1[2].callGroovyObjectGetProperty(this)), null}), $get$$class$java$util$List());
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
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      if (ScriptBytecodeAdapter.compareGreaterThan(var2[3].call(args), $const$0)) {
         var2[4].callCurrent(this, (Object)var2[5].call(var2[6].callGroovyObjectGetProperty(this), "error.unexpected_args", var2[7].call(args, (Object)" ")));
      }

      return ScriptBytecodeAdapter.compareEqual(var2[8].call(args), $const$0) ? (Object)ScriptBytecodeAdapter.castToType(var2[9].callCurrent(this, (Object)var2[10].call(args, (Object)$const$1)), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType(var2[11].callCurrent(this), $get$$class$java$lang$Object());
   }

   private void help(String name) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(name, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert name", var3), (Object)null);
         }
      } catch (Throwable var6) {
         var3.clear();
         throw var6;
      }

      Command command = (Command)ScriptBytecodeAdapter.castToType(var2[12].call(var2[13].callGroovyObjectGetProperty(this), (Object)name), $get$$class$org$codehaus$groovy$tools$shell$Command());
      if (!DefaultTypeTransformation.booleanUnbox(command)) {
         var2[14].callCurrent(this, (Object)(new GStringImpl(new Object[]{name}, new String[]{"No such command: ", ""})));
      }

      var2[15].call(var2[16].callGetProperty(var2[17].callGroovyObjectGetProperty(this)));
      var2[18].call(var2[19].callGetProperty(var2[20].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{var2[21].callGetProperty(command), var2[22].callGetProperty(command)}, new String[]{"usage: @|bold ", "|@ ", ""})));
      var2[23].call(var2[24].callGetProperty(var2[25].callGroovyObjectGetProperty(this)));
      var2[26].call(var2[27].callGetProperty(var2[28].callGroovyObjectGetProperty(this)), var2[29].callGetProperty(command));
      var2[30].call(var2[31].callGetProperty(var2[32].callGroovyObjectGetProperty(this)));
   }

   private void list() {
      CallSite[] var1 = $getCallSiteArray();
      Integer maxName = $const$1;
      Integer maxShortcut = (Integer)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Integer());
      Object command = null;
      Object var5 = var1[33].call(var1[34].callGroovyObjectGetProperty(this));

      while(((Iterator)var5).hasNext()) {
         command = ((Iterator)var5).next();
         if (!DefaultTypeTransformation.booleanUnbox(var1[35].callGetProperty(command))) {
            if (ScriptBytecodeAdapter.compareGreaterThan(var1[36].call(var1[37].callGetProperty(command)), maxName)) {
               maxName = (Integer)ScriptBytecodeAdapter.castToType(var1[38].call(var1[39].callGetProperty(command)), $get$$class$java$lang$Integer());
            }

            if (ScriptBytecodeAdapter.compareGreaterThan(var1[40].call(var1[41].callGetProperty(command)), maxShortcut)) {
               maxShortcut = (Integer)ScriptBytecodeAdapter.castToType(var1[42].call(var1[43].callGetProperty(command)), $get$$class$java$lang$Integer());
            }
         }
      }

      var1[44].call(var1[45].callGetProperty(var1[46].callGroovyObjectGetProperty(this)));
      var1[47].call(var1[48].callGetProperty(var1[49].callGroovyObjectGetProperty(this)), (Object)"For information about @|green Groovy|@, visit:");
      var1[50].call(var1[51].callGetProperty(var1[52].callGroovyObjectGetProperty(this)), (Object)"    @|cyan http://groovy.codehaus.org|@ ");
      var1[53].call(var1[54].callGetProperty(var1[55].callGroovyObjectGetProperty(this)));
      var1[56].call(var1[57].callGetProperty(var1[58].callGroovyObjectGetProperty(this)), (Object)"Available commands:");
      command = null;
      var5 = var1[59].call(var1[60].callGroovyObjectGetProperty(this));

      while(((Iterator)var5).hasNext()) {
         command = ((Iterator)var5).next();
         if (!DefaultTypeTransformation.booleanUnbox(var1[61].callGetProperty(command))) {
            Object n = var1[62].call(var1[63].callGetProperty(command), maxName, " ");
            Object s = var1[64].call(var1[65].callGetProperty(command), maxShortcut, " ");
            Object d = var1[66].callGetProperty(command);
            var1[67].call(var1[68].callGetProperty(var1[69].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{n, s, d}, new String[]{"  @|bold ", "|@  (@|bold ", "|@) ", ""})));
         }
      }

      var1[70].call(var1[71].callGetProperty(var1[72].callGroovyObjectGetProperty(this)));
      var1[73].call(var1[74].callGetProperty(var1[75].callGroovyObjectGetProperty(this)), (Object)"For help on a specific command type:");
      var1[76].call(var1[77].callGetProperty(var1[78].callGroovyObjectGetProperty(this)), (Object)"    help @|bold command|@ ");
      var1[79].call(var1[80].callGetProperty(var1[81].callGroovyObjectGetProperty(this)));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public void this$3$help(String var1) {
      this.help(var1);
   }

   // $FF: synthetic method
   public void this$3$list() {
      this.list();
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
      var0[0] = "alias";
      var0[1] = "<$constructor$>";
      var0[2] = "registry";
      var0[3] = "size";
      var0[4] = "fail";
      var0[5] = "format";
      var0[6] = "messages";
      var0[7] = "join";
      var0[8] = "size";
      var0[9] = "help";
      var0[10] = "getAt";
      var0[11] = "list";
      var0[12] = "getAt";
      var0[13] = "registry";
      var0[14] = "fail";
      var0[15] = "println";
      var0[16] = "out";
      var0[17] = "io";
      var0[18] = "println";
      var0[19] = "out";
      var0[20] = "io";
      var0[21] = "name";
      var0[22] = "usage";
      var0[23] = "println";
      var0[24] = "out";
      var0[25] = "io";
      var0[26] = "println";
      var0[27] = "out";
      var0[28] = "io";
      var0[29] = "help";
      var0[30] = "println";
      var0[31] = "out";
      var0[32] = "io";
      var0[33] = "iterator";
      var0[34] = "registry";
      var0[35] = "hidden";
      var0[36] = "size";
      var0[37] = "name";
      var0[38] = "size";
      var0[39] = "name";
      var0[40] = "size";
      var0[41] = "shortcut";
      var0[42] = "size";
      var0[43] = "shortcut";
      var0[44] = "println";
      var0[45] = "out";
      var0[46] = "io";
      var0[47] = "println";
      var0[48] = "out";
      var0[49] = "io";
      var0[50] = "println";
      var0[51] = "out";
      var0[52] = "io";
      var0[53] = "println";
      var0[54] = "out";
      var0[55] = "io";
      var0[56] = "println";
      var0[57] = "out";
      var0[58] = "io";
      var0[59] = "iterator";
      var0[60] = "registry";
      var0[61] = "hidden";
      var0[62] = "padRight";
      var0[63] = "name";
      var0[64] = "padRight";
      var0[65] = "shortcut";
      var0[66] = "description";
      var0[67] = "println";
      var0[68] = "out";
      var0[69] = "io";
      var0[70] = "println";
      var0[71] = "out";
      var0[72] = "io";
      var0[73] = "println";
      var0[74] = "out";
      var0[75] = "io";
      var0[76] = "println";
      var0[77] = "out";
      var0[78] = "io";
      var0[79] = "println";
      var0[80] = "out";
      var0[81] = "io";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[82];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HelpCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$HelpCommand = class$("org.codehaus.groovy.tools.shell.commands.HelpCommand");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HelpCommandCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HelpCommandCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$HelpCommandCompletor = class$("org.codehaus.groovy.tools.shell.commands.HelpCommandCompletor");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Command() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Command;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Command = class$("org.codehaus.groovy.tools.shell.Command");
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
