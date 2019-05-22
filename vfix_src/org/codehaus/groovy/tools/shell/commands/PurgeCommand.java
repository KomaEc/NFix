package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Closure;
import groovy.lang.MetaClass;
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
import org.codehaus.groovy.tools.shell.ComplexCommandSupport;
import org.codehaus.groovy.tools.shell.Shell;

public class PurgeCommand extends ComplexCommandSupport {
   private Object do_variables;
   private Object do_classes;
   private Object do_imports;
   private Object do_preferences;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205506L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205506 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport;
   // $FF: synthetic field
   private static Class $class$java$lang$String;

   public PurgeCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "purge", "\\p"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.do_variables = new PurgeCommand._closure1(this, this);
         this.do_classes = new PurgeCommand._closure2(this, this);
         this.do_imports = new PurgeCommand._closure3(this, this);
         this.do_preferences = new PurgeCommand._closure4(this, this);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         ScriptBytecodeAdapter.setGroovyObjectProperty(ScriptBytecodeAdapter.createList(new Object[]{"variables", "classes", "imports", "preferences", "all"}), $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand(), this, "functions");
         return;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand()) {
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
   public Object this$dist$invoke$4(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getDo_variables() {
      return this.do_variables;
   }

   public void setDo_variables(Object var1) {
      this.do_variables = var1;
   }

   public Object getDo_classes() {
      return this.do_classes;
   }

   public void setDo_classes(Object var1) {
      this.do_classes = var1;
   }

   public Object getDo_imports() {
      return this.do_imports;
   }

   public void setDo_imports(Object var1) {
      this.do_imports = var1;
   }

   public Object getDo_preferences() {
      return this.do_preferences;
   }

   public void setDo_preferences(Object var1) {
      this.do_preferences = var1;
   }

   // $FF: synthetic method
   public Object super$3$this$dist$get$3(String var1) {
      return super.this$dist$get$3(var1);
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
   public Object super$3$execute(List var1) {
      return super.execute(var1);
   }

   // $FF: synthetic method
   public void super$3$this$dist$set$3(String var1, Object var2) {
      super.this$dist$set$3(var1, var2);
   }

   // $FF: synthetic method
   public History super$2$getHistory() {
      return super.getHistory();
   }

   // $FF: synthetic method
   public List super$3$createCompletors() {
      return super.createCompletors();
   }

   // $FF: synthetic method
   public void super$3$super$2$this$dist$set$2(String var1, Object var2) {
      super.super$2$this$dist$set$2(var1, var2);
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
   public Object super$3$executeFunction(List var1) {
      return super.executeFunction(var1);
   }

   // $FF: synthetic method
   public List super$2$getBuffer() {
      return super.getBuffer();
   }

   // $FF: synthetic method
   public Object super$3$getDo_all() {
      return super.getDo_all();
   }

   // $FF: synthetic method
   public void super$3$setDo_all(Object var1) {
      super.setDo_all(var1);
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
   public void super$2$assertNoArguments(List var1) {
      super.assertNoArguments(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$3$super$2$this$dist$get$2(String var1) {
      return super.super$2$this$dist$get$2(var1);
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
   public Object super$3$this$dist$invoke$3(String var1, Object var2) {
      return super.this$dist$invoke$3(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$this$dist$set$2(String var1, Object var2) {
      super.this$dist$set$2(var1, var2);
   }

   // $FF: synthetic method
   public MetaClass super$3$$getStaticMetaClass() {
      return super.$getStaticMetaClass();
   }

   // $FF: synthetic method
   public Object super$3$super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.super$2$this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   public Closure super$3$loadFunction(String var1) {
      return super.loadFunction(var1);
   }

   // $FF: synthetic method
   public Object super$2$this$dist$invoke$2(String var1, Object var2) {
      return super.this$dist$invoke$2(var1, var2);
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[0];
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand = class$("org.codehaus.groovy.tools.shell.commands.PurgeCommand");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport = class$("org.codehaus.groovy.tools.shell.ComplexCommandSupport");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         if (DefaultTypeTransformation.booleanUnbox(var2[0].call(var2[1].callGroovyObjectGetProperty(this)))) {
            return var2[2].call(var2[3].callGetProperty(var2[4].callGroovyObjectGetProperty(this)), (Object)"No variables defined");
         } else {
            var2[5].call(var2[6].callGroovyObjectGetProperty(this));
            return DefaultTypeTransformation.booleanUnbox(var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this))) ? var2[9].call(var2[10].callGetProperty(var2[11].callGroovyObjectGetProperty(this)), (Object)"Custom variables purged") : null;
         }
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1()) {
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
         var0[0] = "isEmpty";
         var0[1] = "variables";
         var0[2] = "println";
         var0[3] = "out";
         var0[4] = "io";
         var0[5] = "clear";
         var0[6] = "variables";
         var0[7] = "verbose";
         var0[8] = "io";
         var0[9] = "println";
         var0[10] = "out";
         var0[11] = "io";
         var0[12] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[13];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure1 = class$("org.codehaus.groovy.tools.shell.commands.PurgeCommand$_closure1");
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

   class _closure2 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static final Integer $const$0 = (Integer)0;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         if (ScriptBytecodeAdapter.compareEqual(var2[0].call(var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this))), $const$0)) {
            return var2[3].call(var2[4].callGetProperty(var2[5].callGroovyObjectGetProperty(this)), (Object)"No classes have been loaded");
         } else {
            var2[6].call(var2[7].callGroovyObjectGetProperty(this));
            return DefaultTypeTransformation.booleanUnbox(var2[8].callGetProperty(var2[9].callGroovyObjectGetProperty(this))) ? var2[10].call(var2[11].callGetProperty(var2[12].callGroovyObjectGetProperty(this)), (Object)"Loaded classes purged") : null;
         }
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[13].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2()) {
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
         var0[0] = "size";
         var0[1] = "loadedClasses";
         var0[2] = "classLoader";
         var0[3] = "println";
         var0[4] = "out";
         var0[5] = "io";
         var0[6] = "clearCache";
         var0[7] = "classLoader";
         var0[8] = "verbose";
         var0[9] = "io";
         var0[10] = "println";
         var0[11] = "out";
         var0[12] = "io";
         var0[13] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[14];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure2 = class$("org.codehaus.groovy.tools.shell.commands.PurgeCommand$_closure2");
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

   class _closure3 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         if (DefaultTypeTransformation.booleanUnbox(var2[0].call(var2[1].callGroovyObjectGetProperty(this)))) {
            return var2[2].call(var2[3].callGetProperty(var2[4].callGroovyObjectGetProperty(this)), (Object)"No custom imports have been defined");
         } else {
            var2[5].call(var2[6].callGroovyObjectGetProperty(this));
            return DefaultTypeTransformation.booleanUnbox(var2[7].callGetProperty(var2[8].callGroovyObjectGetProperty(this))) ? var2[9].call(var2[10].callGetProperty(var2[11].callGroovyObjectGetProperty(this)), (Object)"Custom imports purged") : null;
         }
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[12].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3()) {
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
         var0[0] = "isEmpty";
         var0[1] = "imports";
         var0[2] = "println";
         var0[3] = "out";
         var0[4] = "io";
         var0[5] = "clear";
         var0[6] = "imports";
         var0[7] = "verbose";
         var0[8] = "io";
         var0[9] = "println";
         var0[10] = "out";
         var0[11] = "io";
         var0[12] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[13];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure3 = class$("org.codehaus.groovy.tools.shell.commands.PurgeCommand$_closure3");
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

   class _closure4 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         var2[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Preferences());
         return DefaultTypeTransformation.booleanUnbox(var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this))) ? var2[3].call(var2[4].callGetProperty(var2[5].callGroovyObjectGetProperty(this)), (Object)"Preferences purged") : null;
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[6].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4()) {
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
         var0[0] = "clear";
         var0[1] = "verbose";
         var0[2] = "io";
         var0[3] = "println";
         var0[4] = "out";
         var0[5] = "io";
         var0[6] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[7];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$util$Preferences() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$util$Preferences = class$("org.codehaus.groovy.tools.shell.util.Preferences");
         }

         return var10000;
      }

      // $FF: synthetic method
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$PurgeCommand$_closure4 = class$("org.codehaus.groovy.tools.shell.commands.PurgeCommand$_closure4");
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
}
