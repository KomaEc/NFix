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
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class SetCommand extends CommandSupport {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204840L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204840 = (Long)0L;
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
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$SetCommand;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;

   public SetCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "set", "\\="};
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
      Object loader = new GeneratedClosure(this, this) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object list = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
            Object keys = var2[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Preferences());
            var2[1].call(keys, (Object)(new GeneratedClosure(this, this.getThisObject(), list) {
               private Reference<T> list;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3;

               public {
                  CallSite[] var4 = $getCallSiteArray();
                  this.list = (Reference)list;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return var3[0].call(this.list.get(), itx.get());
               }

               public Object getList() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.list.get();
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3()) {
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
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1_closure3 = class$("org.codehaus.groovy.tools.shell.commands.SetCommand$_createCompletors_closure1_closure3");
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
            return list.get();
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1()) {
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
            var0[0] = "keys";
            var0[1] = "each";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_createCompletors_closure1 = class$("org.codehaus.groovy.tools.shell.commands.SetCommand$_createCompletors_closure1");
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
      };
      return (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor(), (Object)loader), null}), $get$$class$java$util$List());
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
      } catch (Throwable var7) {
         var3.clear();
         throw var7;
      }

      Object name;
      if (ScriptBytecodeAdapter.compareEqual(var2[1].call(args), $const$0)) {
         name = var2[2].call($get$$class$org$codehaus$groovy$tools$shell$util$Preferences());
         if (ScriptBytecodeAdapter.compareEqual(var2[3].call(name), $const$0)) {
            var2[4].call(var2[5].callGetProperty(var2[6].callGroovyObjectGetProperty(this)), (Object)"No preferences are set");
            return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
         } else {
            var2[7].call(var2[8].callGetProperty(var2[9].callGroovyObjectGetProperty(this)), (Object)"Preferences:");
            var2[10].call(name, (Object)(new GeneratedClosure(this, this) {
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$util$Preferences;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2;

               public {
                  CallSite[] var3 = $getCallSiteArray();
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  Object value = var3[0].call($get$$class$org$codehaus$groovy$tools$shell$util$Preferences(), itx.get(), (Object)null);
                  return var3[1].callCurrent(this, (Object)(new GStringImpl(new Object[]{itx.get(), value}, new String[]{"    ", "=", ""})));
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2()) {
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
                  var0[0] = "get";
                  var0[1] = "println";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand$_execute_closure2 = class$("org.codehaus.groovy.tools.shell.commands.SetCommand$_execute_closure2");
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
            return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
         }
      } else {
         if (ScriptBytecodeAdapter.compareGreaterThan(var2[11].call(args), $const$1)) {
            var2[12].callCurrent(this, (Object)(new GStringImpl(new Object[]{var2[13].callGroovyObjectGetProperty(this)}, new String[]{"Command '", "' requires arguments: <name> [<value>]"})));
         }

         name = var2[14].call(args, (Object)$const$0);
         Object value = null;
         if (ScriptBytecodeAdapter.compareEqual(var2[15].call(args), $const$2)) {
            value = Boolean.TRUE;
         } else {
            value = var2[16].call(args, (Object)$const$2);
         }

         var2[17].call(var2[18].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{name, value}, new String[]{"Setting preference: ", "=", ""})));
         return (Object)ScriptBytecodeAdapter.castToType(var2[19].call($get$$class$org$codehaus$groovy$tools$shell$util$Preferences(), name, var2[20].call($get$$class$java$lang$String(), (Object)value)), $get$$class$java$lang$Object());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[1] = "size";
      var0[2] = "keys";
      var0[3] = "size";
      var0[4] = "println";
      var0[5] = "out";
      var0[6] = "io";
      var0[7] = "println";
      var0[8] = "out";
      var0[9] = "io";
      var0[10] = "each";
      var0[11] = "size";
      var0[12] = "fail";
      var0[13] = "name";
      var0[14] = "getAt";
      var0[15] = "size";
      var0[16] = "getAt";
      var0[17] = "debug";
      var0[18] = "log";
      var0[19] = "put";
      var0[20] = "valueOf";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[21];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand(), var0);
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
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$SetCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$SetCommand = class$("org.codehaus.groovy.tools.shell.commands.SetCommand");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
