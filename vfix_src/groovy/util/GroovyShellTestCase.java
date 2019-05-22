package groovy.util;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MetaClass;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Map;
import junit.framework.TestResult;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GroovyShellTestCase extends GroovyTestCase implements GroovyObject {
   protected GroovyShell shell;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205702L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205702 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Binding;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Script;
   // $FF: synthetic field
   private static Class $class$groovy$util$GroovyTestCase;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$HashMap;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$groovy$util$GroovyShellTestCase;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyShell;

   public GroovyShellTestCase() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   protected void setUp() {
      CallSite[] var1 = $getCallSiteArray();
      ScriptBytecodeAdapter.invokeMethodOnSuper0($get$$class$groovy$util$GroovyTestCase(), this, "setUp");
      this.shell = (GroovyShell)ScriptBytecodeAdapter.castToType((GroovyShell)ScriptBytecodeAdapter.castToType(var1[0].callCurrent(this), $get$$class$groovy$lang$GroovyShell()), $get$$class$groovy$lang$GroovyShell());
   }

   protected void tearDown() {
      CallSite[] var1 = $getCallSiteArray();
      this.shell = (GroovyShell)ScriptBytecodeAdapter.castToType((GroovyShell)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$GroovyShell()), $get$$class$groovy$lang$GroovyShell());
      ScriptBytecodeAdapter.invokeMethodOnSuper0($get$$class$groovy$util$GroovyTestCase(), this, "tearDown");
   }

   protected GroovyShell createNewShell() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyShell)ScriptBytecodeAdapter.castToType(var1[1].callConstructor($get$$class$groovy$lang$GroovyShell()), $get$$class$groovy$lang$GroovyShell());
   }

   protected Object withBinding(Map map, Closure closure) {
      CallSite[] var3 = $getCallSiteArray();
      Binding binding = (Binding)ScriptBytecodeAdapter.castToType(var3[2].callGroovyObjectGetProperty(this.shell), $get$$class$groovy$lang$Binding());
      Map bmap = (Map)ScriptBytecodeAdapter.castToType(var3[3].callGroovyObjectGetProperty(binding), $get$$class$java$util$Map());

      Throwable var10000;
      label69: {
         Object vars;
         Object var7;
         boolean var10001;
         try {
            vars = var3[4].callConstructor($get$$class$java$util$HashMap(), (Object)bmap);
            var3[5].call(bmap, (Object)map);
            var7 = var3[6].call(closure);
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label69;
         }

         var3[7].call(bmap);
         var3[8].call(bmap, (Object)vars);

         label61:
         try {
            return var7;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label61;
         }
      }

      Throwable var14 = var10000;
      var3[12].call(bmap);
      var3[13].call(bmap, (Object)var3[14].callGroovyObjectGetProperty(this));
      throw var14;
   }

   protected Object withBinding(Map map, String script) {
      CallSite[] var3 = $getCallSiteArray();
      Binding binding = (Binding)ScriptBytecodeAdapter.castToType(var3[15].callGroovyObjectGetProperty(this.shell), $get$$class$groovy$lang$Binding());
      Map bmap = (Map)ScriptBytecodeAdapter.castToType(var3[16].callGroovyObjectGetProperty(binding), $get$$class$java$util$Map());

      Throwable var10000;
      label69: {
         Object vars;
         Object var7;
         boolean var10001;
         try {
            vars = var3[17].callConstructor($get$$class$java$util$HashMap(), (Object)bmap);
            var3[18].call(bmap, (Object)map);
            var7 = var3[19].callCurrent(this, (Object)script);
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label69;
         }

         var3[20].call(bmap);
         var3[21].call(bmap, (Object)vars);

         label61:
         try {
            return var7;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label61;
         }
      }

      Throwable var14 = var10000;
      var3[25].call(bmap);
      var3[26].call(bmap, (Object)var3[27].callGroovyObjectGetProperty(this));
      throw var14;
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$GroovyShellTestCase()) {
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
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$util$GroovyShellTestCase();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$GroovyShellTestCase(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$GroovyShellTestCase(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public Object run(File param0, List param1) throws CompilationFailedException, IOException {
      CallSite[] var3 = $getCallSiteArray();
      return var3[28].call(this.shell, param0, param1);
   }

   public Object run(String param0, String param1, List param2) throws CompilationFailedException {
      CallSite[] var4 = $getCallSiteArray();
      return var4[29].call(this.shell, param0, param1, param2);
   }

   public Object run(File param0, String... param1) throws CompilationFailedException, IOException {
      CallSite[] var3 = $getCallSiteArray();
      return var3[30].call(this.shell, param0, param1);
   }

   public Object run(String param0, String param1, String... param2) throws CompilationFailedException {
      CallSite[] var4 = $getCallSiteArray();
      return var4[31].call(this.shell, param0, param1, param2);
   }

   public Object run(Reader param0, String param1, String... param2) throws CompilationFailedException {
      CallSite[] var4 = $getCallSiteArray();
      return var4[32].call(this.shell, param0, param1, param2);
   }

   public Object run(InputStream param0, String param1, String... param2) throws CompilationFailedException {
      CallSite[] var4 = $getCallSiteArray();
      return var4[33].call(this.shell, param0, param1, param2);
   }

   public GroovyClassLoader getClassLoader() {
      CallSite[] var1 = $getCallSiteArray();
      return (GroovyClassLoader)ScriptBytecodeAdapter.castToType(var1[34].call(this.shell), $get$$class$groovy$lang$GroovyClassLoader());
   }

   public Binding getContext() {
      CallSite[] var1 = $getCallSiteArray();
      return (Binding)ScriptBytecodeAdapter.castToType(var1[35].call(this.shell), $get$$class$groovy$lang$Binding());
   }

   public Script parse(Reader param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var3[36].call(this.shell, param0, param1), $get$$class$groovy$lang$Script());
   }

   public Script parse(InputStream param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var3[37].call(this.shell, param0, param1), $get$$class$groovy$lang$Script());
   }

   public Script parse(GroovyCodeSource param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var2[38].call(this.shell, (Object)param0), $get$$class$groovy$lang$Script());
   }

   public Script parse(File param0) throws CompilationFailedException, IOException {
      CallSite[] var2 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var2[39].call(this.shell, (Object)param0), $get$$class$groovy$lang$Script());
   }

   public Script parse(String param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var2[40].call(this.shell, (Object)param0), $get$$class$groovy$lang$Script());
   }

   public Script parse(String param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var3[41].call(this.shell, param0, param1), $get$$class$groovy$lang$Script());
   }

   public Script parse(Reader param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var2[42].call(this.shell, (Object)param0), $get$$class$groovy$lang$Script());
   }

   public Script parse(InputStream param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return (Script)ScriptBytecodeAdapter.castToType(var2[43].call(this.shell, (Object)param0), $get$$class$groovy$lang$Script());
   }

   public Object getVariable(String param0) {
      CallSite[] var2 = $getCallSiteArray();
      return var2[44].call(this.shell, (Object)param0);
   }

   public Object evaluate(GroovyCodeSource param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return var2[45].call(this.shell, (Object)param0);
   }

   public Object evaluate(String param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return var2[46].call(this.shell, (Object)param0);
   }

   public Object evaluate(String param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return var3[47].call(this.shell, param0, param1);
   }

   public Object evaluate(String param0, String param1, String param2) throws CompilationFailedException {
      CallSite[] var4 = $getCallSiteArray();
      return var4[48].call(this.shell, param0, param1, param2);
   }

   public Object evaluate(File param0) throws CompilationFailedException, IOException {
      CallSite[] var2 = $getCallSiteArray();
      return var2[49].call(this.shell, (Object)param0);
   }

   public Object evaluate(Reader param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return var2[50].call(this.shell, (Object)param0);
   }

   public Object evaluate(Reader param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return var3[51].call(this.shell, param0, param1);
   }

   public Object evaluate(InputStream param0) throws CompilationFailedException {
      CallSite[] var2 = $getCallSiteArray();
      return var2[52].call(this.shell, (Object)param0);
   }

   public Object evaluate(InputStream param0, String param1) throws CompilationFailedException {
      CallSite[] var3 = $getCallSiteArray();
      return var3[53].call(this.shell, param0, param1);
   }

   public void setVariable(String param0, Object param1) {
      CallSite[] var3 = $getCallSiteArray();
      var3[54].call(this.shell, param0, param1);
   }

   public void resetLoadedClasses() {
      CallSite[] var1 = $getCallSiteArray();
      var1[55].call(this.shell);
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
   public String super$3$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public String super$4$shouldFailWithCause(Class var1, Closure var2) {
      return super.shouldFailWithCause(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$tearDown() {
      super.tearDown();
   }

   // $FF: synthetic method
   public void super$4$assertScript(String var1) {
      super.assertScript(var1);
   }

   // $FF: synthetic method
   public String super$4$shouldFail(Class var1, Closure var2) {
      return super.shouldFail(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$fixEOLs(String var1) {
      return super.fixEOLs(var1);
   }

   // $FF: synthetic method
   public void super$3$runTest() {
      super.runTest();
   }

   // $FF: synthetic method
   public void super$4$assertInspect(Object var1, String var2) {
      super.assertInspect(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public String super$4$getTestClassName() {
      return super.getTestClassName();
   }

   // $FF: synthetic method
   public String super$4$shouldFail(Closure var1) {
      return super.shouldFail(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$4$assertContains(int var1, int[] var2) {
      super.assertContains(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$setName(String var1) {
      super.setName(var1);
   }

   // $FF: synthetic method
   public int super$3$countTestCases() {
      return super.countTestCases();
   }

   // $FF: synthetic method
   public TestResult super$3$run() {
      return super.run();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public String super$4$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$3$run(TestResult var1) {
      super.run(var1);
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
   public void super$4$assertContains(char var1, char[] var2) {
      super.assertContains(var1, var2);
   }

   // $FF: synthetic method
   public TestResult super$3$createResult() {
      return super.createResult();
   }

   // $FF: synthetic method
   public String super$4$getMethodName() {
      return super.getMethodName();
   }

   // $FF: synthetic method
   public void super$3$setUp() {
      super.setUp();
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, Object[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, int[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$4$notYetImplemented() {
      return super.notYetImplemented();
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$4$assertToString(Object var1, String var2) {
      super.assertToString(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$4$assertLength(int var1, char[] var2) {
      super.assertLength(var1, var2);
   }

   // $FF: synthetic method
   public void super$3$runBare() {
      super.runBare();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$4$assertArrayEquals(Object[] var1, Object[] var2) {
      super.assertArrayEquals(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "createNewShell";
      var0[1] = "<$constructor$>";
      var0[2] = "context";
      var0[3] = "variables";
      var0[4] = "<$constructor$>";
      var0[5] = "putAll";
      var0[6] = "call";
      var0[7] = "clear";
      var0[8] = "putAll";
      var0[9] = "clear";
      var0[10] = "putAll";
      var0[11] = "vars";
      var0[12] = "clear";
      var0[13] = "putAll";
      var0[14] = "vars";
      var0[15] = "context";
      var0[16] = "variables";
      var0[17] = "<$constructor$>";
      var0[18] = "putAll";
      var0[19] = "evaluate";
      var0[20] = "clear";
      var0[21] = "putAll";
      var0[22] = "clear";
      var0[23] = "putAll";
      var0[24] = "vars";
      var0[25] = "clear";
      var0[26] = "putAll";
      var0[27] = "vars";
      var0[28] = "run";
      var0[29] = "run";
      var0[30] = "run";
      var0[31] = "run";
      var0[32] = "run";
      var0[33] = "run";
      var0[34] = "getClassLoader";
      var0[35] = "getContext";
      var0[36] = "parse";
      var0[37] = "parse";
      var0[38] = "parse";
      var0[39] = "parse";
      var0[40] = "parse";
      var0[41] = "parse";
      var0[42] = "parse";
      var0[43] = "parse";
      var0[44] = "getVariable";
      var0[45] = "evaluate";
      var0[46] = "evaluate";
      var0[47] = "evaluate";
      var0[48] = "evaluate";
      var0[49] = "evaluate";
      var0[50] = "evaluate";
      var0[51] = "evaluate";
      var0[52] = "evaluate";
      var0[53] = "evaluate";
      var0[54] = "setVariable";
      var0[55] = "resetLoadedClasses";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[56];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$GroovyShellTestCase(), var0);
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
   private static Class $get$$class$groovy$lang$Binding() {
      Class var10000 = $class$groovy$lang$Binding;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Binding = class$("groovy.lang.Binding");
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
   private static Class $get$$class$groovy$lang$Script() {
      Class var10000 = $class$groovy$lang$Script;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Script = class$("groovy.lang.Script");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$GroovyTestCase() {
      Class var10000 = $class$groovy$util$GroovyTestCase;
      if (var10000 == null) {
         var10000 = $class$groovy$util$GroovyTestCase = class$("groovy.util.GroovyTestCase");
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
   private static Class $get$$class$java$util$HashMap() {
      Class var10000 = $class$java$util$HashMap;
      if (var10000 == null) {
         var10000 = $class$java$util$HashMap = class$("java.util.HashMap");
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
   private static Class $get$$class$groovy$util$GroovyShellTestCase() {
      Class var10000 = $class$groovy$util$GroovyShellTestCase;
      if (var10000 == null) {
         var10000 = $class$groovy$util$GroovyShellTestCase = class$("groovy.util.GroovyShellTestCase");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$GroovyShell() {
      Class var10000 = $class$groovy$lang$GroovyShell;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyShell = class$("groovy.lang.GroovyShell");
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
