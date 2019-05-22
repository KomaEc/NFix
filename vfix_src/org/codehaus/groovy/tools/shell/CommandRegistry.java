package org.codehaus.groovy.tools.shell;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.tools.shell.util.Logger;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class CommandRegistry implements GroovyObject {
   protected final Logger log;
   private final List commands;
   private final Set names;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203950L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203950 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandRegistry;
   // $FF: synthetic field
   private static Class $class$java$util$Set;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$Command;
   // $FF: synthetic field
   private static Class $class$java$util$Iterator;
   // $FF: synthetic field
   private static Class $class$java$util$TreeSet;

   public CommandRegistry() {
      CallSite[] var1 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var1[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)$get$$class$org$codehaus$groovy$tools$shell$CommandRegistry()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.commands = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.names = (Set)ScriptBytecodeAdapter.castToType(var1[1].callConstructor($get$$class$java$util$TreeSet()), $get$$class$java$util$Set());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Command register(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(command, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert command", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[2].call(this.names, (Object)var2[3].callGetProperty(command)))) {
         ScriptBytecodeAdapter.assertFailed("names.contains(command.name)", new GStringImpl(new Object[]{var2[4].callGetProperty(command)}, new String[]{"Duplicate comamnd name: ", ""}));
      }

      var2[5].call(this.names, (Object)var2[6].callGetProperty(command));
      if (DefaultTypeTransformation.booleanUnbox(var2[7].call(this.names, (Object)var2[8].callGetProperty(command)))) {
         ScriptBytecodeAdapter.assertFailed("names.contains(command.shortcut)", new GStringImpl(new Object[]{var2[9].callGetProperty(command)}, new String[]{"Duplicate command shortcut: ", ""}));
      }

      var2[10].call(this.names, (Object)var2[11].callGetProperty(command));
      var2[12].call(this.commands, (Object)command);
      ScriptBytecodeAdapter.setProperty(this, $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry(), command, "registry");
      var2[13].callSafe(var2[14].callGetProperty(command), (Object)(new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return var3[0].call(this.getThisObject(), itx.get());
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1()) {
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
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$CommandRegistry$_register_closure1 = class$("org.codehaus.groovy.tools.shell.CommandRegistry$_register_closure1");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      if (DefaultTypeTransformation.booleanUnbox(var2[15].callGetProperty(this.log))) {
         var2[16].call(this.log, (Object)(new GStringImpl(new Object[]{var2[17].callGetProperty(command)}, new String[]{"Registered command: ", ""})));
      }

      return (Command)ScriptBytecodeAdapter.castToType(command, $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   public Object leftShift(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[18].callCurrent(this, (Object)command);
   }

   public Command find(String name) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(name, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert name", var3), (Object)null);
         }
      } catch (Throwable var7) {
         var3.clear();
         throw var7;
      }

      Object c = null;
      Object var5 = var2[19].call(this.commands);

      do {
         if (!((Iterator)var5).hasNext()) {
            return (Command)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$org$codehaus$groovy$tools$shell$Command());
         }

         c = ((Iterator)var5).next();
      } while(!ScriptBytecodeAdapter.isCase(name, ScriptBytecodeAdapter.createList(new Object[]{var2[20].callGetProperty(c), var2[21].callGetProperty(c)})));

      return (Command)ScriptBytecodeAdapter.castToType(c, $get$$class$org$codehaus$groovy$tools$shell$Command());
   }

   public void remove(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(command, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert command", var3), (Object)null);
         }
      } catch (Throwable var5) {
         var3.clear();
         throw var5;
      }

      var2[22].call(this.commands, (Object)command);
      var2[23].call(this.names, (Object)var2[24].callGetProperty(command));
      var2[25].call(this.names, (Object)var2[26].callGetProperty(command));
      if (DefaultTypeTransformation.booleanUnbox(var2[27].callGetProperty(this.log))) {
         var2[28].call(this.log, (Object)(new GStringImpl(new Object[]{var2[29].callGetProperty(command)}, new String[]{"Removed command: ", ""})));
      }

   }

   public List commands() {
      CallSite[] var1 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(this.commands, $get$$class$java$util$List());
   }

   public Object getProperty(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[30].callCurrent(this, (Object)name);
   }

   public Iterator iterator() {
      CallSite[] var1 = $getCallSiteArray();
      return (Iterator)ScriptBytecodeAdapter.castToType(var1[31].call(var1[32].callCurrent(this)), $get$$class$java$util$Iterator());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$CommandRegistry(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$CommandRegistry(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public final List getCommands() {
      return this.commands;
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
      var0[1] = "<$constructor$>";
      var0[2] = "contains";
      var0[3] = "name";
      var0[4] = "name";
      var0[5] = "leftShift";
      var0[6] = "name";
      var0[7] = "contains";
      var0[8] = "shortcut";
      var0[9] = "shortcut";
      var0[10] = "leftShift";
      var0[11] = "shortcut";
      var0[12] = "leftShift";
      var0[13] = "each";
      var0[14] = "aliases";
      var0[15] = "debugEnabled";
      var0[16] = "debug";
      var0[17] = "name";
      var0[18] = "register";
      var0[19] = "iterator";
      var0[20] = "name";
      var0[21] = "shortcut";
      var0[22] = "remove";
      var0[23] = "remove";
      var0[24] = "name";
      var0[25] = "remove";
      var0[26] = "shortcut";
      var0[27] = "debugEnabled";
      var0[28] = "debug";
      var0[29] = "name";
      var0[30] = "find";
      var0[31] = "iterator";
      var0[32] = "commands";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[33];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandRegistry(), var0);
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
   private static Class $get$$class$java$util$Set() {
      Class var10000 = $class$java$util$Set;
      if (var10000 == null) {
         var10000 = $class$java$util$Set = class$("java.util.Set");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$Command() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$Command;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$Command = class$("org.codehaus.groovy.tools.shell.Command");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Iterator() {
      Class var10000 = $class$java$util$Iterator;
      if (var10000 == null) {
         var10000 = $class$java$util$Iterator = class$("java.util.Iterator");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$TreeSet() {
      Class var10000 = $class$java$util$TreeSet;
      if (var10000 == null) {
         var10000 = $class$java$util$TreeSet = class$("java.util.TreeSet");
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
