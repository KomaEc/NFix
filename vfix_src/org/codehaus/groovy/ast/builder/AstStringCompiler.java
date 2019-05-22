package org.codehaus.groovy.ast.builder;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.PackageScope;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

@PackageScope
public class AstStringCompiler implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203889L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203889 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilerConfiguration;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstStringCompiler;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyCodeSource;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilationUnit;

   public AstStringCompiler() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public List<ASTNode> compile(String script, CompilePhase compilePhase, boolean statementsOnly) {
      Boolean statementsOnly = new Reference(DefaultTypeTransformation.box(statementsOnly));
      CallSite[] var5 = $getCallSiteArray();
      Object scriptClassName = new Reference(var5[0].call("script", (Object)var5[1].call($get$$class$java$lang$System())));
      GroovyClassLoader classLoader = var5[2].callConstructor($get$$class$groovy$lang$GroovyClassLoader());
      GroovyCodeSource codeSource = var5[3].callConstructor($get$$class$groovy$lang$GroovyCodeSource(), script, var5[4].call(scriptClassName.get(), (Object)".groovy"), "/groovy/script");
      CompilationUnit cu = var5[5].callConstructor($get$$class$org$codehaus$groovy$control$CompilationUnit(), var5[6].callGetProperty($get$$class$org$codehaus$groovy$control$CompilerConfiguration()), var5[7].callGetProperty(codeSource), classLoader);
      var5[8].call(cu, var5[9].call(codeSource), script);
      var5[10].call(cu, var5[11].call(compilePhase));
      return (List)ScriptBytecodeAdapter.castToType(var5[12].call(var5[13].callGetProperty(var5[14].callGetProperty(cu)), ScriptBytecodeAdapter.createList(new Object[0]), new GeneratedClosure(this, this, scriptClassName, statementsOnly) {
         private Reference<T> scriptClassName;
         private Reference<T> statementsOnly;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1;
         // $FF: synthetic field
         private static Class $class$java$lang$Boolean;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.scriptClassName = (Reference)scriptClassName;
            this.statementsOnly = (Reference)statementsOnly;
         }

         public Object doCall(List acc, ModuleNode node) {
            List accx = new Reference(acc);
            ModuleNode nodex = new Reference(node);
            CallSite[] var5 = $getCallSiteArray();
            if (DefaultTypeTransformation.booleanUnbox(var5[0].callGetProperty(nodex.get()))) {
               var5[1].call(accx.get(), var5[2].callGetProperty(nodex.get()));
            }

            CallSite var10000 = var5[3];
            Object var10001 = var5[4].callGetProperty(nodex.get());
            Object var10005 = this.getThisObject();
            Reference scriptClassName = this.scriptClassName;
            Reference statementsOnly = this.statementsOnly;
            var10000.callSafe(var10001, (Object)(new GeneratedClosure(this, var10005, scriptClassName, statementsOnly, accx) {
               private Reference<T> scriptClassName;
               private Reference<T> statementsOnly;
               private Reference<T> acc;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$java$lang$Boolean;
               // $FF: synthetic field
               private static Class $class$java$util$List;

               public {
                  Reference scriptClassName = new Reference(scriptClassNamex);
                  Reference statementsOnly = new Reference(statementsOnlyx);
                  CallSite[] var8 = $getCallSiteArray();
                  this.scriptClassName = (Reference)((Reference)scriptClassName.get());
                  this.statementsOnly = (Reference)((Reference)statementsOnly.get());
                  this.acc = (Reference)acc;
               }

               public Object doCall(Object it) {
                  Object itx = new Reference(it);
                  CallSite[] var3 = $getCallSiteArray();
                  return !DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(var3[0].callGetProperty(itx.get()), this.scriptClassName.get()) && DefaultTypeTransformation.booleanUnbox(this.statementsOnly.get()) ? Boolean.TRUE : Boolean.FALSE) ? var3[1].call(this.acc.get(), itx.get()) : null;
               }

               public Object getScriptClassName() {
                  CallSite[] var1 = $getCallSiteArray();
                  return this.scriptClassName.get();
               }

               public boolean getStatementsOnly() {
                  CallSite[] var1 = $getCallSiteArray();
                  return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(this.statementsOnly.get(), $get$$class$java$lang$Boolean()));
               }

               public List getAcc() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (List)ScriptBytecodeAdapter.castToType(this.acc.get(), $get$$class$java$util$List());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[2].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2()) {
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
                  var0[0] = "name";
                  var0[1] = "leftShift";
                  var0[2] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[3];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2(), var0);
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
               private static Class $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2() {
                  Class var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2;
                  if (var10000 == null) {
                     var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1_closure2 = class$("org.codehaus.groovy.ast.builder.AstStringCompiler$_compile_closure1_closure2");
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
               private static Class $get$$class$java$lang$Boolean() {
                  Class var10000 = $class$java$lang$Boolean;
                  if (var10000 == null) {
                     var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
            return accx.get();
         }

         public Object call(List acc, ModuleNode node) {
            List accx = new Reference(acc);
            ModuleNode nodex = new Reference(node);
            CallSite[] var5 = $getCallSiteArray();
            return var5[5].callCurrent(this, accx.get(), nodex.get());
         }

         public Object getScriptClassName() {
            CallSite[] var1 = $getCallSiteArray();
            return this.scriptClassName.get();
         }

         public boolean getStatementsOnly() {
            CallSite[] var1 = $getCallSiteArray();
            return DefaultTypeTransformation.booleanUnbox((Boolean)ScriptBytecodeAdapter.castToType(this.statementsOnly.get(), $get$$class$java$lang$Boolean()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1()) {
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
            var0[0] = "statementBlock";
            var0[1] = "add";
            var0[2] = "statementBlock";
            var0[3] = "each";
            var0[4] = "classes";
            var0[5] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[6];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1(), var0);
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
         private static Class $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1() {
            Class var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1;
            if (var10000 == null) {
               var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler$_compile_closure1 = class$("org.codehaus.groovy.ast.builder.AstStringCompiler$_compile_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$java$lang$Boolean() {
            Class var10000 = $class$java$lang$Boolean;
            if (var10000 == null) {
               var10000 = $class$java$lang$Boolean = class$("java.lang.Boolean");
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
      }), $get$$class$java$util$List());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
      var0[0] = "plus";
      var0[1] = "currentTimeMillis";
      var0[2] = "<$constructor$>";
      var0[3] = "<$constructor$>";
      var0[4] = "plus";
      var0[5] = "<$constructor$>";
      var0[6] = "DEFAULT";
      var0[7] = "codeSource";
      var0[8] = "addSource";
      var0[9] = "getName";
      var0[10] = "compile";
      var0[11] = "getPhaseNumber";
      var0[12] = "inject";
      var0[13] = "modules";
      var0[14] = "ast";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[15];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$control$CompilerConfiguration() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration = class$("org.codehaus.groovy.control.CompilerConfiguration");
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
   private static Class $get$$class$groovy$lang$GroovyClassLoader() {
      Class var10000 = $class$groovy$lang$GroovyClassLoader;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyClassLoader = class$("groovy.lang.GroovyClassLoader");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstStringCompiler = class$("org.codehaus.groovy.ast.builder.AstStringCompiler");
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
   private static Class $get$$class$groovy$lang$GroovyCodeSource() {
      Class var10000 = $class$groovy$lang$GroovyCodeSource;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyCodeSource = class$("groovy.lang.GroovyCodeSource");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$control$CompilationUnit() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilationUnit;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilationUnit = class$("org.codehaus.groovy.control.CompilationUnit");
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
