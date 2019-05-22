package org.codehaus.groovy.ast.builder;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstBuilder implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524203695L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524203695 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstBuilder;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalStateException;
   // $FF: synthetic field
   private static Class $class$java$lang$IllegalArgumentException;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilePhase;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$ast$builder$AstStringCompiler;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;

   public AstBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public List<ASTNode> buildFromCode(CompilePhase phase, boolean statementsOnly, Closure block) {
      CallSite[] var4 = $getCallSiteArray();
      throw (Throwable)var4[0].callConstructor($get$$class$java$lang$IllegalStateException(), (Object)"AstBuilder.build(CompilePhase, boolean, Closure):List<ASTNode> should never be called at runtime.\nAre you sure you are using it correctly?\n");
   }

   public List<ASTNode> buildFromString(CompilePhase phase, boolean statementsOnly, String source) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(source) && !ScriptBytecodeAdapter.compareEqual("", var4[1].call(source)) ? Boolean.FALSE : Boolean.TRUE)) {
         throw (Throwable)var4[2].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"A source must be specified");
      } else {
         return (List)ScriptBytecodeAdapter.castToType(var4[3].call(var4[4].callConstructor($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler()), source, phase, DefaultTypeTransformation.box(statementsOnly)), $get$$class$java$util$List());
      }
   }

   private List<ASTNode> buildFromBlock(CompilePhase phase, boolean statementsOnly, String source) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(source) && !ScriptBytecodeAdapter.compareEqual("", var4[5].call(source)) ? Boolean.FALSE : Boolean.TRUE)) {
         throw (Throwable)var4[6].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"A source must be specified");
      } else {
         Object labelledSource = var4[7].call(new GStringImpl(new Object[]{var4[8].call($get$$class$java$lang$System())}, new String[]{"__synthesized__label__", "__:"}), (Object)source);
         List result = (List)ScriptBytecodeAdapter.castToType(var4[9].call(var4[10].callConstructor($get$$class$org$codehaus$groovy$ast$builder$AstStringCompiler()), labelledSource, phase, DefaultTypeTransformation.box(statementsOnly)), $get$$class$java$util$List());
         return (List)ScriptBytecodeAdapter.castToType(var4[11].call(result, (Object)(new GeneratedClosure(this, this) {
            // $FF: synthetic field
            private static final Integer $const$0 = (Integer)0;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$stmt$BlockStatement;
            // $FF: synthetic field
            private static Class $class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1;

            public {
               CallSite[] var3 = $getCallSiteArray();
            }

            public Object doCall(Object node) {
               Object nodex = new Reference(node);
               CallSite[] var3 = $getCallSiteArray();
               return nodex.get() instanceof BlockStatement ? var3[0].call(var3[1].callGetProperty((BlockStatement)ScriptBytecodeAdapter.castToType(nodex.get(), $get$$class$org$codehaus$groovy$ast$stmt$BlockStatement())), (Object)$const$0) : nodex.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1()) {
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
               var0[0] = "getAt";
               var0[1] = "statements";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1(), var0);
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
            private static Class $get$$class$org$codehaus$groovy$ast$stmt$BlockStatement() {
               Class var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$stmt$BlockStatement = class$("org.codehaus.groovy.ast.stmt.BlockStatement");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1() {
               Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1;
               if (var10000 == null) {
                  var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilder$_buildFromBlock_closure1 = class$("org.codehaus.groovy.ast.builder.AstBuilder$_buildFromBlock_closure1");
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
         })), $get$$class$java$util$List());
      }
   }

   public List<ASTNode> buildFromSpec(Closure specification) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareEqual(specification, (Object)null)) {
         throw (Throwable)var2[12].callConstructor($get$$class$java$lang$IllegalArgumentException(), (Object)"Null: specification");
      } else {
         Object properties = var2[13].callConstructor($get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler(), (Object)specification);
         return (List)ScriptBytecodeAdapter.castToType(var2[14].callGetProperty(properties), $get$$class$java$util$List());
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$ast$builder$AstBuilder()) {
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
      Class var10000 = $get$$class$org$codehaus$groovy$ast$builder$AstBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$ast$builder$AstBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$ast$builder$AstBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public List<ASTNode> buildFromCode(CompilePhase phase, Closure block) {
      CallSite[] var3 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var3[15].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(phase, $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createGroovyObjectWrapper(block, $get$$class$groovy$lang$Closure())), $get$$class$java$util$List());
   }

   public List<ASTNode> buildFromCode(Closure block) {
      CallSite[] var2 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var2[16].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((CompilePhase)ScriptBytecodeAdapter.castToType(var2[17].callGetProperty($get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createGroovyObjectWrapper(block, $get$$class$groovy$lang$Closure())), $get$$class$java$util$List());
   }

   public List<ASTNode> buildFromString(CompilePhase phase, String source) {
      CallSite[] var3 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var3[18].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(phase, $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(source, $get$$class$java$lang$String())), $get$$class$java$util$List());
   }

   public List<ASTNode> buildFromString(String source) {
      CallSite[] var2 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var2[19].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((CompilePhase)ScriptBytecodeAdapter.castToType(var2[20].callGetProperty($get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(source, $get$$class$java$lang$String())), $get$$class$java$util$List());
   }

   private List<ASTNode> buildFromBlock(CompilePhase phase, String source) {
      CallSite[] var3 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var3[21].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(phase, $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(source, $get$$class$java$lang$String())), $get$$class$java$util$List());
   }

   private List<ASTNode> buildFromBlock(String source) {
      CallSite[] var2 = $getCallSiteArray();
      return (List)ScriptBytecodeAdapter.castToType(var2[22].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper((CompilePhase)ScriptBytecodeAdapter.castToType(var2[23].callGetProperty($get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), $get$$class$org$codehaus$groovy$control$CompilePhase()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(source, $get$$class$java$lang$String())), $get$$class$java$util$List());
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
   public List this$2$buildFromBlock(CompilePhase var1, boolean var2, String var3) {
      return this.buildFromBlock(var1, var2, var3);
   }

   // $FF: synthetic method
   public List this$2$buildFromBlock(CompilePhase var1, String var2) {
      return this.buildFromBlock(var1, var2);
   }

   // $FF: synthetic method
   public List this$2$buildFromBlock(String var1) {
      return this.buildFromBlock(var1);
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
      var0[1] = "trim";
      var0[2] = "<$constructor$>";
      var0[3] = "compile";
      var0[4] = "<$constructor$>";
      var0[5] = "trim";
      var0[6] = "<$constructor$>";
      var0[7] = "plus";
      var0[8] = "currentTimeMillis";
      var0[9] = "compile";
      var0[10] = "<$constructor$>";
      var0[11] = "collect";
      var0[12] = "<$constructor$>";
      var0[13] = "<$constructor$>";
      var0[14] = "expression";
      var0[15] = "buildFromCode";
      var0[16] = "buildFromCode";
      var0[17] = "CLASS_GENERATION";
      var0[18] = "buildFromString";
      var0[19] = "buildFromString";
      var0[20] = "CLASS_GENERATION";
      var0[21] = "buildFromBlock";
      var0[22] = "buildFromBlock";
      var0[23] = "CLASS_GENERATION";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[24];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$ast$builder$AstBuilder(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstBuilder() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilder;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstBuilder = class$("org.codehaus.groovy.ast.builder.AstBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler() {
      Class var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$ast$builder$AstSpecificationCompiler = class$("org.codehaus.groovy.ast.builder.AstSpecificationCompiler");
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
   private static Class $get$$class$java$lang$IllegalStateException() {
      Class var10000 = $class$java$lang$IllegalStateException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalStateException = class$("java.lang.IllegalStateException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$IllegalArgumentException() {
      Class var10000 = $class$java$lang$IllegalArgumentException;
      if (var10000 == null) {
         var10000 = $class$java$lang$IllegalArgumentException = class$("java.lang.IllegalArgumentException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$org$codehaus$groovy$control$CompilePhase() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilePhase;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilePhase = class$("org.codehaus.groovy.control.CompilePhase");
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
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
