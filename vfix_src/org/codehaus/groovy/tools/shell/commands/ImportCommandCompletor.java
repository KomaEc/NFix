package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.ArgumentCompletor;
import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.ArgumentCompletor.ArgumentDelimiter;
import groovy.lang.GroovyClassLoader;
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

public class ImportCommandCompletor extends ArgumentCompletor implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204805L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204805 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$jline$ArgumentCompletor;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;
   // $FF: synthetic field
   private static Class $class$jline$NullCompletor;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$ClassNameCompletor;

   public ImportCommandCompletor(GroovyClassLoader classLoader) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createList(new Object[]{var2[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$ClassNameCompletor(), (Object)classLoader), var2[1].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor(), (Object)"as"), var2[2].callConstructor($get$$class$jline$NullCompletor())})};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 5, $get$$class$jline$ArgumentCompletor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((List)var10001[0]);
         break;
      case 1:
         super((Completor)var10001[0]);
         break;
      case 2:
         super((Completor)var10001[0], (ArgumentDelimiter)var10001[1]);
         break;
      case 3:
         super((Completor[])var10001[0]);
         break;
      case 4:
         super((Completor[])var10001[0], (ArgumentDelimiter)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public boolean super$2$getStrict() {
      return super.getStrict();
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
   public void super$2$setStrict(boolean var1) {
      super.setStrict(var1);
   }

   // $FF: synthetic method
   public int super$2$complete(String var1, int var2, List var3) {
      return super.complete(var1, var2, var3);
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
      var0[0] = "<$constructor$>";
      var0[1] = "<$constructor$>";
      var0[2] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$ImportCommandCompletor(), var0);
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
   private static Class $get$$class$jline$ArgumentCompletor() {
      Class var10000 = $class$jline$ArgumentCompletor;
      if (var10000 == null) {
         var10000 = $class$jline$ArgumentCompletor = class$("com.gzoltar.shaded.jline.ArgumentCompletor");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor = class$("org.codehaus.groovy.tools.shell.util.SimpleCompletor");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$util$ClassNameCompletor() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$util$ClassNameCompletor;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$util$ClassNameCompletor = class$("org.codehaus.groovy.tools.shell.util.ClassNameCompletor");
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
