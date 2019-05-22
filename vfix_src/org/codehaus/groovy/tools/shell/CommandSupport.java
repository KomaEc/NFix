package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
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
import org.codehaus.groovy.tools.shell.util.Logger;
import org.codehaus.groovy.tools.shell.util.MessageSource;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public abstract class CommandSupport implements Command, GroovyObject {
   protected static final String NEWLINE = (String)((String)ScriptBytecodeAdapter.castToType($getCallSiteArray()[33].call($getCallSiteArray()[34].callGetProperty($get$$class$java$lang$System()), (Object)"line.separator"), $get$$class$java$lang$String()));
   protected final Logger log;
   protected final MessageSource messages;
   private final String name;
   private final String shortcut;
   protected final Shell shell;
   protected final IO io;
   protected CommandRegistry registry;
   private final List aliases;
   private boolean hidden;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203957L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203957 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$jline$History;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Binding;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$jline$Completor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandException;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$jline$NullCompletor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandAlias;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$MessageSource;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$jline$ArgumentCompletor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$BufferManager;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Shell;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$IO;

   protected CommandSupport(Shell shell, String name, String shortcut) {
      CallSite[] var4 = $getCallSiteArray();
      this.messages = (MessageSource)ScriptBytecodeAdapter.castToType(var4[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$MessageSource(), var4[1].callGroovyObjectGetProperty(this), $get$$class$org$codehaus$groovy$tools$shell$CommandSupport()), $get$$class$org$codehaus$groovy$tools$shell$util$MessageSource());
      this.aliases = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.hidden = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      ValueRecorder var5 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var5.record(shell, 8))) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert shell", var5), (Object)null);
         }
      } catch (Throwable var19) {
         var5.clear();
         throw var19;
      }

      ValueRecorder var6 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var6.record(name, 8))) {
            var6.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert name", var6), (Object)null);
         }
      } catch (Throwable var18) {
         var6.clear();
         throw var18;
      }

      ValueRecorder var7 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var7.record(shortcut, 8))) {
            var7.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert shortcut", var7), (Object)null);
         }
      } catch (Throwable var17) {
         var7.clear();
         throw var17;
      }

      this.log = (Logger)ScriptBytecodeAdapter.castToType(var4[2].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), var4[3].callGroovyObjectGetProperty(this), name), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.shell = (Shell)ScriptBytecodeAdapter.castToType(shell, $get$$class$org$codehaus$groovy$tools$shell$Shell());
      this.io = (IO)ScriptBytecodeAdapter.castToType(var4[4].callGroovyObjectGetProperty(shell), $get$$class$org$codehaus$groovy$tools$shell$IO());
      this.name = (String)ScriptBytecodeAdapter.castToType(name, $get$$class$java$lang$String());
      this.shortcut = (String)ScriptBytecodeAdapter.castToType(shortcut, $get$$class$java$lang$String());
   }

   public String getDescription() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var1[5].call(this.messages, (Object)"command.description"), $get$$class$java$lang$String());
   }

   public String getUsage() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var1[6].call(this.messages, (Object)"command.usage"), $get$$class$java$lang$String());
   }

   public String getHelp() {
      CallSite[] var1 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var1[7].call(this.messages, (Object)"command.help"), $get$$class$java$lang$String());
   }

   protected List createCompletors() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$util$List());
   }

   public Completor getCompletor() {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.hidden))) {
         return (Completor)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$jline$Completor());
      } else {
         Object list = new Reference(ScriptBytecodeAdapter.createList(new Object[]{var1[8].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor(), this.name, this.shortcut)}));
         Object completors = var1[9].callCurrent(this);
         if (DefaultTypeTransformation.booleanUnbox(completors)) {
            var1[10].call(completors, (Object)(new GeneratedClosure(this, this, list) {
               private Reference<T> list;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$jline$NullCompletor;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.list = (Reference)list;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return DefaultTypeTransformation.booleanUnbox(itx.get()) ? var3[0].call(this.list.get(), itx.get()) : var3[1].call(this.list.get(), var3[2].callConstructor($get$$class$jline$NullCompletor()));
               }

               public Object getList() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.list.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1()) {
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
                  var0[1] = "leftShift";
                  var0[2] = "<$constructor$>";
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport$_getCompletor_closure1 = class$("org.codehaus.groovy.tools.shell.CommandSupport$_getCompletor_closure1");
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
               private static Class $get$$class$jline$NullCompletor() {
                  Class var10000 = $class$jline$NullCompletor;
                  if (var10000 == null) {
                     var10000 = $class$jline$NullCompletor = class$("com.gzoltar.shaded.jline.NullCompletor");
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
         } else {
            var1[11].call(list.get(), var1[12].callConstructor($get$$class$jline$NullCompletor()));
         }

         return (Completor)ScriptBytecodeAdapter.castToType(var1[13].callConstructor($get$$class$jline$ArgumentCompletor(), (Object)list.get()), $get$$class$jline$Completor());
      }
   }

   protected void alias(String name, String shortcut) {
      CallSite[] var3 = $getCallSiteArray();
      var3[14].call(this.aliases, (Object)var3[15].callConstructor($get$$class$org$codehaus$groovy$tools$shell$CommandAlias(), this.shell, name, shortcut, this.name));
   }

   protected void fail(String msg) {
      CallSite[] var2 = $getCallSiteArray();
      throw (Throwable)var2[16].callConstructor($get$$class$org$codehaus$groovy$tools$shell$CommandException(), this, msg);
   }

   protected void fail(String msg, Throwable cause) {
      CallSite[] var3 = $getCallSiteArray();
      throw (Throwable)var3[17].callConstructor($get$$class$org$codehaus$groovy$tools$shell$CommandException(), this, msg, cause);
   }

   protected void assertNoArguments(List args) {
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

      if (ScriptBytecodeAdapter.compareGreaterThan(var2[18].call(args), $const$0)) {
         var2[19].callCurrent(this, (Object)var2[20].call(this.messages, "error.unexpected_args", var2[21].call(args, (Object)" ")));
      }

   }

   protected BufferManager getBuffers() {
      CallSite[] var1 = $getCallSiteArray();
      return (BufferManager)ScriptBytecodeAdapter.castToType(var1[22].callGroovyObjectGetProperty(this.shell), $get$$class$org$codehaus$groovy$tools$shell$BufferManager());
   }

   protected List getBuffer() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var1[23].call(var1[24].callGroovyObjectGetProperty(this.shell)), $get$$class$java$util$List());
   }

   protected List getImports() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var1[25].callGroovyObjectGetProperty(this.shell), $get$$class$java$util$List());
   }

   protected Binding getBinding() {
      CallSite[] var1 = $getCallSiteArray();
      return (Binding)ScriptBytecodeAdapter.castToType(var1[26].callGetProperty(var1[27].callGroovyObjectGetProperty(this.shell)), $get$$class$groovy$lang$Binding());
   }

   protected Map getVariables() {
      CallSite[] var1 = $getCallSiteArray();
      return (Map)ScriptBytecodeAdapter.castToType(var1[28].callGroovyObjectGetProperty(var1[29].callGroovyObjectGetProperty(this)), $get$$class$java$util$Map());
   }

   protected History getHistory() {
      CallSite[] var1 = $getCallSiteArray();
      return (History)ScriptBytecodeAdapter.castToType(var1[30].callGroovyObjectGetProperty(this.shell), $get$$class$jline$History());
   }

   protected GroovyClassLoader getClassLoader() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyClassLoader)ScriptBytecodeAdapter.castToType(var1[31].callGetProperty(var1[32].callGroovyObjectGetProperty(this.shell)), $get$$class$groovy$lang$GroovyClassLoader());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandSupport()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$CommandSupport();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$CommandSupport(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$CommandSupport(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public final String getName() {
      return this.name;
   }

   public final String getShortcut() {
      return this.shortcut;
   }

   public final List getAliases() {
      return this.aliases;
   }

   public boolean getHidden() {
      return this.hidden;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public void setHidden(boolean var1) {
      this.hidden = var1;
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
      var0[1] = "class";
      var0[2] = "create";
      var0[3] = "class";
      var0[4] = "io";
      var0[5] = "getAt";
      var0[6] = "getAt";
      var0[7] = "getAt";
      var0[8] = "<$constructor$>";
      var0[9] = "createCompletors";
      var0[10] = "each";
      var0[11] = "leftShift";
      var0[12] = "<$constructor$>";
      var0[13] = "<$constructor$>";
      var0[14] = "leftShift";
      var0[15] = "<$constructor$>";
      var0[16] = "<$constructor$>";
      var0[17] = "<$constructor$>";
      var0[18] = "size";
      var0[19] = "fail";
      var0[20] = "format";
      var0[21] = "join";
      var0[22] = "buffers";
      var0[23] = "current";
      var0[24] = "buffers";
      var0[25] = "imports";
      var0[26] = "context";
      var0[27] = "interp";
      var0[28] = "variables";
      var0[29] = "binding";
      var0[30] = "history";
      var0[31] = "classLoader";
      var0[32] = "interp";
      var0[33] = "getAt";
      var0[34] = "properties";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[35];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandSupport(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandSupport() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandSupport = class$("org.codehaus.groovy.tools.shell.CommandSupport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$jline$Completor() {
      Class var10000 = $class$jline$Completor;
      if (var10000 == null) {
         var10000 = $class$jline$Completor = class$("com.gzoltar.shaded.jline.Completor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandException() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandException;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandException = class$("org.codehaus.groovy.tools.shell.CommandException");
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
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$jline$NullCompletor() {
      Class var10000 = $class$jline$NullCompletor;
      if (var10000 == null) {
         var10000 = $class$jline$NullCompletor = class$("com.gzoltar.shaded.jline.NullCompletor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandAlias() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandAlias;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandAlias = class$("org.codehaus.groovy.tools.shell.CommandAlias");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
   private static Class $get$$class$jline$ArgumentCompletor() {
      Class var10000 = $class$jline$ArgumentCompletor;
      if (var10000 == null) {
         var10000 = $class$jline$ArgumentCompletor = class$("com.gzoltar.shaded.jline.ArgumentCompletor");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor = class$("org.codehaus.groovy.tools.shell.util.SimpleCompletor");
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
