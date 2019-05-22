package org.codehaus.groovy.tools.shell.commands;

import com.gzoltar.shaded.jline.Completor;
import com.gzoltar.shaded.jline.History;
import groovy.lang.Closure;
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
import org.codehaus.groovy.tools.shell.ComplexCommandSupport;
import org.codehaus.groovy.tools.shell.Shell;

public class HistoryCommand extends ComplexCommandSupport {
   private Object do_show;
   private Object do_clear;
   private Object do_flush;
   private Object do_recall;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205477L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205477 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$ComplexCommandSupport;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$shell$util$SimpleCompletor;

   public HistoryCommand(Shell shell) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{shell, "history", "\\H"};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 1, $get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super((Shell)var10001[0], (String)var10001[1], (String)var10001[2]);
         this.do_show = new HistoryCommand._closure1(this, this);
         this.do_clear = new HistoryCommand._closure2(this, this);
         this.do_flush = new HistoryCommand._closure3(this, this);
         this.do_recall = new HistoryCommand._closure4(this, this);
         this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
         ScriptBytecodeAdapter.setGroovyObjectProperty(ScriptBytecodeAdapter.createList(new Object[]{"show", "clear", "flush", "recall"}), $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand(), this, "functions");
         ScriptBytecodeAdapter.setGroovyObjectProperty("show", $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand(), this, "defaultFunction");
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
         private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            CallSite[] var2 = $getCallSiteArray();
            Object list = new Reference(ScriptBytecodeAdapter.createList(new Object[0]));
            var2[0].call(var2[1].callGroovyObjectGetProperty(this), (Object)(new GeneratedClosure(this, this.getThisObject(), list) {
               private Reference<T> list;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7;

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
                  if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7()) {
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
                  return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7() {
                  Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5_closure7 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_createCompletors_closure5_closure7");
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
            if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5()) {
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
            var0[0] = "each";
            var0[1] = "functions";
            var0[2] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[3];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5() {
            Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_createCompletors_closure5 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_createCompletors_closure5");
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
      };
      return (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[]{var1[0].callConstructor($get$$class$org$codehaus$groovy$tools$shell$util$SimpleCompletor(), (Object)loader), null}), $get$$class$java$util$List());
   }

   public Object execute(List args) {
      CallSite[] var2 = $getCallSiteArray();
      if (!DefaultTypeTransformation.booleanUnbox(var2[1].callGroovyObjectGetProperty(this))) {
         var2[2].callCurrent(this, (Object)"Shell does not appear to be interactive; Can not query history");
      }

      ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$org$codehaus$groovy$tools$shell$ComplexCommandSupport(), this, "execute", new Object[]{args});
      return (Object)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$Object());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$4(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$4(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object getDo_show() {
      return this.do_show;
   }

   public void setDo_show(Object var1) {
      this.do_show = var1;
   }

   public Object getDo_clear() {
      return this.do_clear;
   }

   public void setDo_clear(Object var1) {
      this.do_clear = var1;
   }

   public Object getDo_flush() {
      return this.do_flush;
   }

   public void setDo_flush(Object var1) {
      this.do_flush = var1;
   }

   public Object getDo_recall() {
      return this.do_recall;
   }

   public void setDo_recall(Object var1) {
      this.do_recall = var1;
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
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "<$constructor$>";
      var0[1] = "history";
      var0[2] = "fail";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand(), var0);
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
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
   private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand() {
      Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand");
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

   class _closure1 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;

      public _closure1(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         return var2[0].call(var2[1].callGetProperty(var2[2].callGroovyObjectGetProperty(this)), (Object)(new GeneratedClosure(this, this.getThisObject()) {
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)3;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object item, Object i) {
               Object itemx = new Reference(item);
               Object ix = new Reference(i);
               CallSite[] var5 = $getCallSiteArray();
               ix.set(var5[0].call(var5[1].call(ix.get()), $const$0, " "));
               return var5[2].call(var5[3].callGetProperty(var5[4].callGroovyObjectGetProperty(this)), (Object)(new GStringImpl(new Object[]{ix.get(), itemx.get()}, new String[]{" @|bold ", "|@  ", ""})));
            }

            public Object call(Object item, Object i) {
               Object itemx = new Reference(item);
               Object ix = new Reference(i);
               CallSite[] var5 = $getCallSiteArray();
               return var5[5].callCurrent(this, itemx.get(), ix.get());
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6()) {
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
               var0[0] = "padLeft";
               var0[1] = "toString";
               var0[2] = "println";
               var0[3] = "out";
               var0[4] = "io";
               var0[5] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[6];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6() {
               Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1_closure6 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_closure1_closure6");
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
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1()) {
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
         var0[0] = "eachWithIndex";
         var0[1] = "historyList";
         var0[2] = "history";
         var0[3] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[4];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure1 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_closure1");
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
   }

   class _closure2 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;

      public _closure2(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         var2[0].call(var2[1].callGroovyObjectGetProperty(this));
         return DefaultTypeTransformation.booleanUnbox(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this))) ? var2[4].call(var2[5].callGetProperty(var2[6].callGroovyObjectGetProperty(this)), (Object)"History cleared") : null;
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2()) {
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
         var0[1] = "history";
         var0[2] = "verbose";
         var0[3] = "io";
         var0[4] = "println";
         var0[5] = "out";
         var0[6] = "io";
         var0[7] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[8];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure2 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_closure2");
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
   }

   class _closure3 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3;
      // $FF: synthetic field
      private static Class $class$java$lang$Object;

      public _closure3(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object it) {
         CallSite[] var2 = $getCallSiteArray();
         var2[0].call(var2[1].callGroovyObjectGetProperty(this));
         return DefaultTypeTransformation.booleanUnbox(var2[2].callGetProperty(var2[3].callGroovyObjectGetProperty(this))) ? var2[4].call(var2[5].callGetProperty(var2[6].callGroovyObjectGetProperty(this)), (Object)"History flushed") : null;
      }

      public Object doCall() {
         CallSite[] var1 = $getCallSiteArray();
         return var1[7].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3()) {
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
         var0[0] = "flushBuffer";
         var0[1] = "history";
         var0[2] = "verbose";
         var0[3] = "io";
         var0[4] = "println";
         var0[5] = "out";
         var0[6] = "io";
         var0[7] = "doCall";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[8];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure3 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_closure3");
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
   }

   class _closure4 extends Closure implements GeneratedClosure {
      // $FF: synthetic field
      private static final Integer $const$0 = (Integer)1;
      // $FF: synthetic field
      private static final Integer $const$1 = (Integer)0;
      // $FF: synthetic field
      private static ClassInfo $staticClassInfo;
      // $FF: synthetic field
      private static SoftReference $callSiteArray;
      // $FF: synthetic field
      private static Class $class$java$lang$Integer;
      // $FF: synthetic field
      private static Class $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4;

      public _closure4(Object _outerInstance, Object _thisObject) {
         CallSite[] var3 = $getCallSiteArray();
         super(_outerInstance, _thisObject);
      }

      public Object doCall(Object args) {
         Object argsx = new Reference(args);
         CallSite[] var3 = $getCallSiteArray();
         Object line = new Reference((Object)null);
         if (ScriptBytecodeAdapter.compareNotEqual(var3[0].call(argsx.get()), $const$0)) {
            var3[1].callCurrent(this, (Object)"History recall requires a single history identifer");
         }

         Reference id = new Reference(var3[2].call(argsx.get(), (Object)$const$1));

         try {
            id.set(var3[3].call($get$$class$java$lang$Integer(), (Object)id.get()));
            line.set(var3[4].call(var3[5].callGetProperty(var3[6].callGroovyObjectGetProperty(this)), id.get()));
         } catch (Exception var9) {
            Exception e = new Reference(var9);
            var3[7].callCurrent(this, new GStringImpl(new Object[]{id.get()}, new String[]{"Invalid history identifier: ", ""}), e.get());
         } finally {
            ;
         }

         var3[8].call(var3[9].callGroovyObjectGetProperty(this), (Object)(new GStringImpl(new Object[]{id.get(), line.get()}, new String[]{"Recalling history item #", ": ", ""})));
         return var3[10].call(var3[11].callGroovyObjectGetProperty(this), line.get());
      }

      // $FF: synthetic method
      protected MetaClass $getStaticMetaClass() {
         if (this.getClass() == $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4()) {
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
         var0[1] = "fail";
         var0[2] = "getAt";
         var0[3] = "parseInt";
         var0[4] = "getAt";
         var0[5] = "historyList";
         var0[6] = "history";
         var0[7] = "fail";
         var0[8] = "debug";
         var0[9] = "log";
         var0[10] = "execute";
         var0[11] = "shell";
      }

      // $FF: synthetic method
      private static CallSiteArray $createCallSiteArray() {
         String[] var0 = new String[12];
         $createCallSiteArray_1(var0);
         return new CallSiteArray($get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4(), var0);
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
      private static Class $get$$class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4() {
         Class var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4;
         if (var10000 == null) {
            var10000 = $class$org$codehaus$groovy$tools$shell$commands$HistoryCommand$_closure4 = class$("org.codehaus.groovy.tools.shell.commands.HistoryCommand$_closure4");
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
