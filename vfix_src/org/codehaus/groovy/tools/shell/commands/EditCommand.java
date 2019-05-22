package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.BufferManager;
import org.codehaus.groovy.tools.shell.CommandSupport;
import org.codehaus.groovy.tools.shell.Shell;

public class EditCommand extends CommandSupport {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204797L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204797 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$EditCommand;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;

   public EditCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "edit", "\\e"};
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

   private String getEditorCommand() {
      CallSite[] var1 = $getCallSiteArray();
      Object editor = var1[0].callGetProperty($get$$class$org$codehaus$groovy$tools$shell$util$Preferences());
      var1[1].call(var1[2].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{editor}, new String[]{"Using editor: ", ""})));
      if (!DefaultTypeTransformation.booleanUnbox(editor)) {
         var1[3].callCurrent(this, (Object)"Unable to determine which editor to use; check $EDITOR");
      }

      return (String)ScriptBytecodeAdapter.castToType(editor, $get$$class$java$lang$String());
   }

   public Object execute(List args) {
      CallSite[] var2 = $getCallSiteArray();
      var2[4].callCurrent(this, (Object)args);
      Object file = var2[5].call($get$$class$java$io$File(), "groovysh-buffer", ".groovy");
      var2[6].call(file);

      Throwable var10000;
      label69: {
         boolean var10001;
         Object var6;
         try {
            var2[7].call(file, var2[8].call(var2[9].callGroovyObjectGetProperty(this), var2[10].callGroovyObjectGetProperty(this)));
            Object cmd = new GStringImpl(new Object[]{var2[11].callGroovyObjectGetProperty(this), file}, new String[]{"", " ", ""});
            var2[12].call(var2[13].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{cmd}, new String[]{"Executing: ", ""})));
            Object p = var2[14].call(cmd);
            var2[15].call(var2[16].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{p}, new String[]{"Waiting for process: ", ""})));
            var2[17].call(p);
            var2[18].call(var2[19].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{var2[20].callGetProperty(file)}, new String[]{"Editor contents: ", ""})));
            var6 = (Object)ScriptBytecodeAdapter.castToType(var2[21].call(file, (Object)(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(var3[1].callGroovyObjectGetProperty(this), itx.get());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1()) {
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
                  var0[0] = "leftShift";
                  var0[1] = "shell";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1(), var0);
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
               private static Class $get$$class$java$lang$Object() {
                  Class var10000 = $class$java$lang$Object;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$Object = class$("java.lang.Object");
                  }

                  return var10000;
               }

               // $FF: synthetic method
               private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$commands$EditCommand$_execute_closure1 = class$("org.codehaus.groovy.tools.shell.commands.EditCommand$_execute_closure1");
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
            })), $get$$class$java$lang$Object());
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label69;
         }

         var2[22].call(file);

         label61:
         try {
            return var6;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            break label61;
         }
      }

      Throwable var13 = var10000;
      var2[24].call(file);
      throw var13;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public String this$3$getEditorCommand() {
      return this.getEditorCommand();
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
      var0[0] = "editor";
      var0[1] = "debug";
      var0[2] = "log";
      var0[3] = "fail";
      var0[4] = "assertNoArguments";
      var0[5] = "createTempFile";
      var0[6] = "deleteOnExit";
      var0[7] = "write";
      var0[8] = "join";
      var0[9] = "buffer";
      var0[10] = "NEWLINE";
      var0[11] = "editorCommand";
      var0[12] = "debug";
      var0[13] = "log";
      var0[14] = "execute";
      var0[15] = "debug";
      var0[16] = "log";
      var0[17] = "waitFor";
      var0[18] = "debug";
      var0[19] = "log";
      var0[20] = "text";
      var0[21] = "eachLine";
      var0[22] = "delete";
      var0[23] = "delete";
      var0[24] = "delete";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[25];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Preferences() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences = class$("org.codehaus.groovy.tools.shell.util.Preferences");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$EditCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$EditCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$EditCommand = class$("org.codehaus.groovy.tools.shell.commands.EditCommand");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
