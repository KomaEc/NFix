package groovy.inspect.swingui;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class AstNodeToScriptAdapter implements GroovyObject {
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)2;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524202701L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524202701 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$java$io$StringWriter;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstNodeToScriptAdapter;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;
   // $FF: synthetic field
   private static Class $class$java$lang$ClassLoader;
   // $FF: synthetic field
   private static Class $class$java$lang$Integer;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilerConfiguration;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$AstNodeToScriptVisitor;
   // $FF: synthetic field
   private static Class $class$java$lang$Boolean;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilePhase;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyCodeSource;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilationUnit;

   public AstNodeToScriptAdapter() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(args) && !ScriptBytecodeAdapter.compareLessThan(var1[0].callGetProperty(args), $const$0) ? Boolean.FALSE : Boolean.TRUE)) {
         var1[1].callStatic($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), (Object)"\nUsage: java groovy.inspect.swingui.AstNodeToScriptAdapter [filename] [compilephase]\nwhere [filename] is a Groovy script\nand [compilephase] is a valid Integer based org.codehaus.groovy.control.CompilePhase");
      } else {
         Object file = var1[2].callConstructor($get$$class$java$io$File(), (Object)ScriptBytecodeAdapter.createPojoWrapper((String)ScriptBytecodeAdapter.castToType(var1[3].call(args, (Object)$const$1), $get$$class$java$lang$String()), $get$$class$java$lang$String()));
         Object phase = var1[4].call($get$$class$org$codehaus$groovy$control$CompilePhase(), (Object)var1[5].call(args, (Object)$const$2));
         if (!DefaultTypeTransformation.booleanUnbox(var1[6].call(file))) {
            var1[7].callStatic($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), (Object)(new GStringImpl(new Object[]{var1[8].call(args, (Object)$const$1)}, new String[]{"File ", " cannot be found."})));
         } else if (ScriptBytecodeAdapter.compareEqual(phase, (Object)null)) {
            var1[9].callStatic($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), (Object)(new GStringImpl(new Object[]{var1[10].call(args, (Object)$const$2)}, new String[]{"Compile phase ", " cannot be mapped to a org.codehaus.groovy.control.CompilePhase."})));
         } else {
            var1[11].callStatic($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), var1[12].call(var1[13].callConstructor($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter()), var1[14].callGetProperty(file), var1[15].call(phase)));
         }
      }

   }

   public String compileToScript(String script, int compilePhase, ClassLoader classLoader, boolean showScriptFreeForm, boolean showScriptClass) {
      CallSite[] var6 = $getCallSiteArray();
      Object writer = new Reference(var6[16].callConstructor($get$$class$java$io$StringWriter()));
      Object var10000 = classLoader;
      if (!DefaultTypeTransformation.booleanUnbox(classLoader)) {
         var10000 = var6[17].callConstructor($get$$class$groovy$lang$GroovyClassLoader(), (Object)var6[18].callGetProperty(var6[19].callCurrent(this)));
      }

      classLoader = (ClassLoader)ScriptBytecodeAdapter.castToType(var10000, $get$$class$java$lang$ClassLoader());
      Object scriptName = var6[20].call(var6[21].call("script", (Object)var6[22].call($get$$class$java$lang$System())), (Object)".groovy");
      GroovyCodeSource codeSource = var6[23].callConstructor($get$$class$groovy$lang$GroovyCodeSource(), script, scriptName, "/groovy/script");
      CompilationUnit cu = var6[24].callConstructor($get$$class$org$codehaus$groovy$control$CompilationUnit(), var6[25].callGetProperty($get$$class$org$codehaus$groovy$control$CompilerConfiguration()), var6[26].callGetProperty(codeSource), classLoader);
      var6[27].call(cu, var6[28].callConstructor($get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor(), writer.get(), DefaultTypeTransformation.box(showScriptFreeForm), DefaultTypeTransformation.box(showScriptClass)), DefaultTypeTransformation.box(compilePhase));
      var6[29].call(cu, var6[30].call(codeSource), script);

      try {
         var6[31].call(cu, DefaultTypeTransformation.box(compilePhase));
      } catch (CompilationFailedException var15) {
         var6[32].call(writer.get(), (Object)"Unable to produce AST for this phase due to earlier compilation error:");
         var6[33].call(var6[34].callGetProperty(var15), (Object)(new GeneratedClosure(this, this, writer) {
            private Reference<T> writer;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Object;
            // $FF: synthetic field
            private static Class $class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1;

            public {
               CallSite[] var4 = $getCallSiteArray();
               this.writer = (Reference)writer;
            }

            public Object doCall(Object it) {
               Object itx = new Reference(it);
               CallSite[] var3 = $getCallSiteArray();
               return var3[0].call(this.writer.get(), itx.get());
            }

            public Object getWriter() {
               CallSite[] var1 = $getCallSiteArray();
               return this.writer.get();
            }

            public Object doCall() {
               CallSite[] var1 = $getCallSiteArray();
               return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1()) {
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
               var0[0] = "println";
               var0[1] = "doCall";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[2];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1(), var0);
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
            private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1() {
               Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1;
               if (var10000 == null) {
                  var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter$_compileToScript_closure1 = class$("groovy.inspect.swingui.AstNodeToScriptAdapter$_compileToScript_closure1");
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
         var6[35].call(writer.get(), (Object)"Fix the above error(s) and then press Refresh");
      } catch (Throwable var16) {
         var6[36].call(writer.get(), (Object)"Unable to produce AST for this phase due to an error:");
         var6[37].call(writer.get(), var6[38].call(var16));
         var6[39].call(writer.get(), (Object)"Fix the above error(s) and then press Refresh");
      } finally {
         ;
      }

      return (String)ScriptBytecodeAdapter.castToType(var6[40].call(writer.get()), $get$$class$java$lang$String());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter()) {
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
      Class var10000 = $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$2(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$2(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public String compileToScript(String script, int compilePhase, ClassLoader classLoader, boolean showScriptFreeForm) {
      CallSite[] var5 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var5[41].callCurrent(this, (Object[])ArrayUtil.createArray(ScriptBytecodeAdapter.createPojoWrapper(script, $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.box(compilePhase), $get$$class$java$lang$Integer()), Integer.TYPE), ScriptBytecodeAdapter.createPojoWrapper(classLoader, $get$$class$java$lang$ClassLoader()), ScriptBytecodeAdapter.createPojoWrapper((Boolean)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.box(showScriptFreeForm), $get$$class$java$lang$Boolean()), Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE))), $get$$class$java$lang$String());
   }

   public String compileToScript(String script, int compilePhase, ClassLoader classLoader) {
      CallSite[] var4 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var4[42].callCurrent(this, (Object[])ArrayUtil.createArray(ScriptBytecodeAdapter.createPojoWrapper(script, $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.box(compilePhase), $get$$class$java$lang$Integer()), Integer.TYPE), ScriptBytecodeAdapter.createPojoWrapper(classLoader, $get$$class$java$lang$ClassLoader()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE))), $get$$class$java$lang$String());
   }

   public String compileToScript(String script, int compilePhase) {
      CallSite[] var3 = $getCallSiteArray();
      return (String)ScriptBytecodeAdapter.castToType(var3[43].callCurrent(this, (Object[])ArrayUtil.createArray(ScriptBytecodeAdapter.createPojoWrapper(script, $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper((Integer)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.box(compilePhase), $get$$class$java$lang$Integer()), Integer.TYPE), ScriptBytecodeAdapter.createPojoWrapper((ClassLoader)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$java$lang$ClassLoader()), $get$$class$java$lang$ClassLoader()), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE), ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE))), $get$$class$java$lang$String());
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
      var0[0] = "length";
      var0[1] = "println";
      var0[2] = "<$constructor$>";
      var0[3] = "getAt";
      var0[4] = "fromPhaseNumber";
      var0[5] = "getAt";
      var0[6] = "exists";
      var0[7] = "println";
      var0[8] = "getAt";
      var0[9] = "println";
      var0[10] = "getAt";
      var0[11] = "println";
      var0[12] = "compileToScript";
      var0[13] = "<$constructor$>";
      var0[14] = "text";
      var0[15] = "getPhaseNumber";
      var0[16] = "<$constructor$>";
      var0[17] = "<$constructor$>";
      var0[18] = "classLoader";
      var0[19] = "getClass";
      var0[20] = "plus";
      var0[21] = "plus";
      var0[22] = "currentTimeMillis";
      var0[23] = "<$constructor$>";
      var0[24] = "<$constructor$>";
      var0[25] = "DEFAULT";
      var0[26] = "codeSource";
      var0[27] = "addPhaseOperation";
      var0[28] = "<$constructor$>";
      var0[29] = "addSource";
      var0[30] = "getName";
      var0[31] = "compile";
      var0[32] = "println";
      var0[33] = "eachLine";
      var0[34] = "message";
      var0[35] = "println";
      var0[36] = "println";
      var0[37] = "println";
      var0[38] = "getMessage";
      var0[39] = "println";
      var0[40] = "toString";
      var0[41] = "compileToScript";
      var0[42] = "compileToScript";
      var0[43] = "compileToScript";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[44];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter(), var0);
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
   private static Class $get$$class$java$io$StringWriter() {
      Class var10000 = $class$java$io$StringWriter;
      if (var10000 == null) {
         var10000 = $class$java$io$StringWriter = class$("java.io.StringWriter");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptAdapter() {
      Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstNodeToScriptAdapter = class$("groovy.inspect.swingui.AstNodeToScriptAdapter");
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
   private static Class $get$$class$java$lang$ClassLoader() {
      Class var10000 = $class$java$lang$ClassLoader;
      if (var10000 == null) {
         var10000 = $class$java$lang$ClassLoader = class$("java.lang.ClassLoader");
      }

      return var10000;
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
   private static Class $get$$class$org$codehaus$groovy$control$CompilerConfiguration() {
      Class var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$control$CompilerConfiguration = class$("org.codehaus.groovy.control.CompilerConfiguration");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$AstNodeToScriptVisitor() {
      Class var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$AstNodeToScriptVisitor = class$("groovy.inspect.swingui.AstNodeToScriptVisitor");
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
   private static Class $get$$class$java$lang$System() {
      Class var10000 = $class$java$lang$System;
      if (var10000 == null) {
         var10000 = $class$java$lang$System = class$("java.lang.System");
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
