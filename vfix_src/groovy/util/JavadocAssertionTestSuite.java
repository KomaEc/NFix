package groovy.util;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Enumeration;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.transform.powerassert.AssertionRenderer;
import org.codehaus.groovy.transform.powerassert.ValueRecorder;

public class JavadocAssertionTestSuite extends TestSuite implements GroovyObject {
   public static final String SYSPROP_SRC_DIR = (String)"javadocAssertion.src.dir";
   public static final String SYSPROP_SRC_PATTERN = (String)"javadocAssertion.src.pattern";
   public static final String SYSPROP_SRC_EXCLUDES_PATTERN = (String)"javadocAssertion.src.excludesPattern";
   private static JavadocAssertionTestBuilder testBuilder = (JavadocAssertionTestBuilder)$getCallSiteArray()[26].callConstructor($get$$class$groovy$util$JavadocAssertionTestBuilder());
   private static IFileNameFinder finder = (IFileNameFinder)$getCallSiteArray()[27].callConstructor($get$$class$groovy$util$FileNameFinder());
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)3;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static final Integer $const$2 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$3 = (Integer)2;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204589L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204589 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$groovy$util$FileNameFinder;
   // $FF: synthetic field
   private static Class $class$junit$framework$Test;
   // $FF: synthetic field
   private static Class $class$groovy$util$JavadocAssertionTestBuilder;
   // $FF: synthetic field
   private static Class $class$java$lang$System;
   // $FF: synthetic field
   private static Class $class$junit$textui$TestRunner;
   // $FF: synthetic field
   private static Class $class$java$util$Collection;
   // $FF: synthetic field
   private static Class $class$groovy$util$JavadocAssertionTestSuite;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$io$File;

   public JavadocAssertionTestSuite() {
      CallSite[] var1 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
   }

   public static Test suite() {
      CallSite[] var0 = $getCallSiteArray();
      String basedir = (String)ScriptBytecodeAdapter.castToType(var0[0].call($get$$class$java$lang$System(), SYSPROP_SRC_DIR, "./src/"), $get$$class$java$lang$String());
      return (Test)ScriptBytecodeAdapter.castToType(var0[1].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), (Object)basedir), $get$$class$junit$framework$Test());
   }

   public static Test suite(String basedir) {
      CallSite[] var1 = $getCallSiteArray();
      String includePattern = (String)ScriptBytecodeAdapter.castToType(var1[2].call($get$$class$java$lang$System(), SYSPROP_SRC_PATTERN, "**/*.java,**/*.groovy"), $get$$class$java$lang$String());
      return (Test)ScriptBytecodeAdapter.castToType(var1[3].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), basedir, includePattern), $get$$class$junit$framework$Test());
   }

   public static Test suite(String basedir, String includePattern) {
      CallSite[] var2 = $getCallSiteArray();
      String excludePattern = (String)ScriptBytecodeAdapter.castToType(var2[4].call($get$$class$java$lang$System(), SYSPROP_SRC_EXCLUDES_PATTERN, ""), $get$$class$java$lang$String());
      return (Test)ScriptBytecodeAdapter.castToType(var2[5].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), basedir, includePattern, excludePattern), $get$$class$junit$framework$Test());
   }

   public static Test suite(String basedir, String includePattern, String excludePattern) {
      CallSite[] var3 = $getCallSiteArray();
      ValueRecorder var4 = new ValueRecorder();

      try {
         if (DefaultTypeTransformation.booleanUnbox(var4.record(var3[6].call(var4.record(var3[7].callConstructor($get$$class$java$io$File(), (Object)var4.record(basedir, 17)), 8)), 26))) {
            var4.clear();
         } else {
            ScriptBytecodeAdapter.assertFailed(AssertionRenderer.render("assert new File(basedir).exists()", var4), (Object)null);
         }
      } catch (Throwable var8) {
         var4.clear();
         throw var8;
      }

      TestSuite suite = new Reference(var3[8].callConstructor($get$$class$groovy$util$JavadocAssertionTestSuite()));
      Collection filenames = (Collection)ScriptBytecodeAdapter.castToType(var3[9].call(finder, (Object)ScriptBytecodeAdapter.createMap(new Object[]{"dir", basedir, "includes", includePattern, "excludes", excludePattern})), $get$$class$java$util$Collection());
      var3[10].call(filenames, (Object)(new GeneratedClosure($get$$class$groovy$util$JavadocAssertionTestSuite(), $get$$class$groovy$util$JavadocAssertionTestSuite(), suite) {
         private Reference<T> suite;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$util$JavadocAssertionTestSuite$_suite_closure1;
         // $FF: synthetic field
         private static Class $class$junit$framework$TestSuite;
         // $FF: synthetic field
         private static Class $class$java$lang$String;
         // $FF: synthetic field
         private static Class $class$java$lang$Class;
         // $FF: synthetic field
         private static Class $class$java$io$File;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.suite = (Reference)suite;
         }

         public Object doCall(Object filename) {
            Object filenamex = new Reference(filename);
            CallSite[] var3 = $getCallSiteArray();
            String code = (String)ScriptBytecodeAdapter.castToType(var3[0].callGetProperty(var3[1].callConstructor($get$$class$java$io$File(), (Object)filenamex.get())), $get$$class$java$lang$String());
            Class test = new Reference((Class)ScriptBytecodeAdapter.castToType(var3[2].call(var3[3].callGroovyObjectGetProperty(this), filenamex.get(), code), $get$$class$java$lang$Class()));
            return ScriptBytecodeAdapter.compareNotEqual(test.get(), (Object)null) ? var3[4].call(this.suite.get(), test.get()) : null;
         }

         public TestSuite getSuite() {
            CallSite[] var1 = $getCallSiteArray();
            return (TestSuite)ScriptBytecodeAdapter.castToType(this.suite.get(), $get$$class$junit$framework$TestSuite());
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestSuite$_suite_closure1()) {
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
            var0[0] = "text";
            var0[1] = "<$constructor$>";
            var0[2] = "buildTest";
            var0[3] = "testBuilder";
            var0[4] = "addTestSuite";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[5];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestSuite$_suite_closure1(), var0);
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
         private static Class $get$$class$groovy$util$JavadocAssertionTestSuite$_suite_closure1() {
            Class var10000 = $class$groovy$util$JavadocAssertionTestSuite$_suite_closure1;
            if (var10000 == null) {
               var10000 = $class$groovy$util$JavadocAssertionTestSuite$_suite_closure1 = class$("groovy.util.JavadocAssertionTestSuite$_suite_closure1");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$junit$framework$TestSuite() {
            Class var10000 = $class$junit$framework$TestSuite;
            if (var10000 == null) {
               var10000 = $class$junit$framework$TestSuite = class$("junit.framework.TestSuite");
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
         private static Class $get$$class$java$lang$Class() {
            Class var10000 = $class$java$lang$Class;
            if (var10000 == null) {
               var10000 = $class$java$lang$Class = class$("java.lang.Class");
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
         static Class class$(String var0) {
            try {
               return Class.forName(var0);
            } catch (ClassNotFoundException var2) {
               throw new NoClassDefFoundError(var2.getMessage());
            }
         }
      }));
      return (Test)ScriptBytecodeAdapter.castToType(suite.get(), $get$$class$junit$framework$Test());
   }

   public static void main(String... args) {
      CallSite[] var1 = $getCallSiteArray();
      Object var2 = var1[11].callGetProperty(args);
      if (ScriptBytecodeAdapter.isCase(var2, $const$0)) {
         var1[12].call($get$$class$junit$textui$TestRunner(), (Object)var1[13].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), var1[14].call(args, (Object)$const$1), var1[15].call(args, (Object)$const$2), var1[16].call(args, (Object)$const$3)));
      } else if (ScriptBytecodeAdapter.isCase(var2, $const$3)) {
         var1[17].call($get$$class$junit$textui$TestRunner(), (Object)var1[18].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), var1[19].call(args, (Object)$const$1), var1[20].call(args, (Object)$const$2)));
      } else if (ScriptBytecodeAdapter.isCase(var2, $const$2)) {
         var1[21].call($get$$class$junit$textui$TestRunner(), (Object)var1[22].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite(), var1[23].call(args, (Object)$const$1)));
      } else {
         var1[24].call($get$$class$junit$textui$TestRunner(), (Object)var1[25].callStatic($get$$class$groovy$util$JavadocAssertionTestSuite()));
      }

   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$util$JavadocAssertionTestSuite()) {
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
      Class var10000 = $get$$class$groovy$util$JavadocAssertionTestSuite();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$util$JavadocAssertionTestSuite(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$util$JavadocAssertionTestSuite(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public String super$2$getName() {
      return super.getName();
   }

   // $FF: synthetic method
   public void super$2$run(TestResult var1) {
      super.run(var1);
   }

   // $FF: synthetic method
   public String super$2$toString() {
      return super.toString();
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
   public Enumeration super$2$tests() {
      return super.tests();
   }

   // $FF: synthetic method
   public void super$2$runTest(Test var1, TestResult var2) {
      super.runTest(var1, var2);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public Test super$2$testAt(int var1) {
      return super.testAt(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$2$addTestSuite(Class var1) {
      super.addTestSuite(var1);
   }

   // $FF: synthetic method
   public void super$2$setName(String var1) {
      super.setName(var1);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$2$addTest(Test var1) {
      super.addTest(var1);
   }

   // $FF: synthetic method
   public int super$2$testCount() {
      return super.testCount();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public int super$2$countTestCases() {
      return super.countTestCases();
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
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "getProperty";
      var0[1] = "suite";
      var0[2] = "getProperty";
      var0[3] = "suite";
      var0[4] = "getProperty";
      var0[5] = "suite";
      var0[6] = "exists";
      var0[7] = "<$constructor$>";
      var0[8] = "<$constructor$>";
      var0[9] = "getFileNames";
      var0[10] = "each";
      var0[11] = "length";
      var0[12] = "run";
      var0[13] = "suite";
      var0[14] = "getAt";
      var0[15] = "getAt";
      var0[16] = "getAt";
      var0[17] = "run";
      var0[18] = "suite";
      var0[19] = "getAt";
      var0[20] = "getAt";
      var0[21] = "run";
      var0[22] = "suite";
      var0[23] = "getAt";
      var0[24] = "run";
      var0[25] = "suite";
      var0[26] = "<$constructor$>";
      var0[27] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[28];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$util$JavadocAssertionTestSuite(), var0);
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
   private static Class $get$$class$groovy$util$FileNameFinder() {
      Class var10000 = $class$groovy$util$FileNameFinder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$FileNameFinder = class$("groovy.util.FileNameFinder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$junit$framework$Test() {
      Class var10000 = $class$junit$framework$Test;
      if (var10000 == null) {
         var10000 = $class$junit$framework$Test = class$("junit.framework.Test");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$JavadocAssertionTestBuilder() {
      Class var10000 = $class$groovy$util$JavadocAssertionTestBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$util$JavadocAssertionTestBuilder = class$("groovy.util.JavadocAssertionTestBuilder");
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
   private static Class $get$$class$junit$textui$TestRunner() {
      Class var10000 = $class$junit$textui$TestRunner;
      if (var10000 == null) {
         var10000 = $class$junit$textui$TestRunner = class$("junit.textui.TestRunner");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Collection() {
      Class var10000 = $class$java$util$Collection;
      if (var10000 == null) {
         var10000 = $class$java$util$Collection = class$("java.util.Collection");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$JavadocAssertionTestSuite() {
      Class var10000 = $class$groovy$util$JavadocAssertionTestSuite;
      if (var10000 == null) {
         var10000 = $class$groovy$util$JavadocAssertionTestSuite = class$("groovy.util.JavadocAssertionTestSuite");
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
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
