package org.codehaus.groovy.tools.shell;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.MultiCompletor;
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

public class CommandsMultiCompletor extends MultiCompletor implements GroovyObject {
   protected final Logger log;
   private List list;
   private boolean dirty;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204777L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204777 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$jline$MultiCompletor;
   // $FF: synthetic field
   private static Class array$$class$jline$Completor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$Logger;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor;

   public CommandsMultiCompletor() {
      CallSite[] var1 = $getCallSiteArray();
      this.log = (Logger)ScriptBytecodeAdapter.castToType((Logger)ScriptBytecodeAdapter.castToType(var1[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Logger(), (Object)var1[1].callGroovyObjectGetProperty(this)), $get$$class$org$codehaus$groovy$tools$shell$util$Logger()), $get$$class$org$codehaus$groovy$tools$shell$util$Logger());
      this.list = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.dirty = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public Object leftShift(Command command) {
      CallSite[] var2 = $getCallSiteArray();
      ValueRecorder var3 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var3.record(command, 8))) {
            var3.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert command", var3), (Object)null);
         }
      } catch (Throwable var6) {
         var3.clear();
         throw var6;
      }

      Object c = var2[2].callGetProperty(command);
      if (DefaultTypeTransformation.booleanUnbox(c)) {
         var2[3].call(this.list, (Object)c);
         var2[4].call(this.log, (Object)(new GStringImpl(new Object[]{var2[5].call(this.list), var2[6].callGetProperty(command)}, new String[]{"Added completor[", "] for command: ", ""})));
         Boolean var10000 = Boolean.TRUE;
         this.dirty = DefaultTypeTransformation.booleanUnbox(var10000);
         return var10000;
      } else {
         return null;
      }
   }

   public void refresh() {
      CallSite[] var1 = $getCallSiteArray();
      var1[7].call(this.log, (Object)"Refreshing the completor list");
      ScriptBytecodeAdapter.setGroovyObjectProperty((Completor[])ScriptBytecodeAdapter.asType(this.list, $get$array$$class$jline$Completor()), $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor(), this, "completors");
      this.dirty = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
   }

   public int complete(String buffer, int pos, List cand) {
      CallSite[] var4 = $getCallSiteArray();
      ValueRecorder var5 = new ValueRecorder();

      try {
         boolean var10000 = ScriptBytecodeAdapter.compareNotEqual(var5.record(buffer, 8), (Object)null);
         var5.record(var10000 ? Boolean.TRUE : Boolean.FALSE, 15);
         if (var10000) {
            var5.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert buffer != null", var5), (Object)null);
         }
      } catch (Throwable var7) {
         var5.clear();
         throw var7;
      }

      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.dirty))) {
         var4[8].callCurrent(this);
      }

      return DefaultTypeTransformation.intUnbox((Integer)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$jline$MultiCompletor(), this, "complete", new Object[]{buffer, DefaultTypeTransformation.box(pos), cand}), $get$$class$java$lang$Integer()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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

   public List getList() {
      return this.list;
   }

   public void setList(List var1) {
      this.list = var1;
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
   public Completor[] super$2$getCompletors() {
      return super.getCompletors();
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
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public int super$2$complete(String var1, int var2, List var3) {
      return super.complete(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$setCompletors(Completor[] var1) {
      super.setCompletors(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "create";
      var0[1] = "class";
      var0[2] = "completor";
      var0[3] = "leftShift";
      var0[4] = "debug";
      var0[5] = "size";
      var0[6] = "name";
      var0[7] = "debug";
      var0[8] = "refresh";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[9];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor(), var0);
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
   private static Class $get$$class$jline$MultiCompletor() {
      Class var10000 = $class$jline$MultiCompletor;
      if (var10000 == null) {
         var10000 = $class$jline$MultiCompletor = class$("com.gzoltar.shaded.jline.MultiCompletor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$array$$class$jline$Completor() {
      Class var10000 = array$$class$jline$Completor;
      if (var10000 == null) {
         var10000 = array$$class$jline$Completor = class$("[Lcom.gzoltar.shaded.jline.Completor;");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$CommandsMultiCompletor = class$("org.codehaus.groovy.tools.shell.CommandsMultiCompletor");
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
