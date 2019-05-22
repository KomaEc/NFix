package org.codehaus.groovy.tools.ast;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyResourceLoader;
import groovy.lang.MetaClass;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Manifest;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.transform.ASTTransformation;

private class TestHarnessClassLoader extends GroovyClassLoader implements GroovyObject {
   private ASTTransformation transform;
   private CompilePhase phase;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205708L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205708 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$transform$ASTTransformation;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyClassLoader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilePhase;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$control$CompilationUnit;
   // $FF: synthetic field
   private static Class $class$org$codehaus$groovy$tools$ast$TestHarnessOperation;

   public TestHarnessClassLoader(ASTTransformation transform, CompilePhase phase) {
      CallSite[] var3 = $getCallSiteArray();
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.transform = (ASTTransformation)ScriptBytecodeAdapter.castToType(transform, $get$$class$org$codehaus$groovy$transform$ASTTransformation());
      this.phase = (CompilePhase)ScriptBytecodeAdapter.castToType(phase, $get$$class$org$codehaus$groovy$control$CompilePhase());
   }

   protected CompilationUnit createCompilationUnit(CompilerConfiguration config, CodeSource codeSource) {
      CallSite[] var3 = $getCallSiteArray();
      CompilationUnit cu = (CompilationUnit)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$lang$GroovyClassLoader(), this, "createCompilationUnit", new Object[]{config, codeSource}), $get$$class$org$codehaus$groovy$control$CompilationUnit());
      var3[0].call(cu, var3[1].callConstructor($get$$class$org$codehaus$groovy$tools$ast$TestHarnessOperation(), (Object)this.transform), var3[2].call(this.phase));
      return (CompilationUnit)ScriptBytecodeAdapter.castToType(cu, $get$$class$org$codehaus$groovy$control$CompilationUnit());
   }

   // $FF: synthetic method
   public Object this$dist$invoke$6(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$6(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$6(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader()) {
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
   public Class super$5$parseClass(InputStream var1) {
      return super.parseClass(var1);
   }

   // $FF: synthetic method
   public InputStream super$2$getResourceAsStream(String var1) {
      return super.getResourceAsStream(var1);
   }

   // $FF: synthetic method
   public void super$5$removeClassCacheEntry(String var1) {
      super.removeClassCacheEntry(var1);
   }

   // $FF: synthetic method
   public void super$5$setShouldRecompile(Boolean var1) {
      super.setShouldRecompile(var1);
   }

   // $FF: synthetic method
   public Class super$3$defineClass(String var1, byte[] var2, int var3, int var4, CodeSource var5) {
      return super.defineClass(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public Class super$5$parseClass(GroovyCodeSource var1) {
      return super.parseClass(var1);
   }

   // $FF: synthetic method
   public Class super$2$defineClass(String var1, byte[] var2, int var3, int var4) {
      return super.defineClass(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Class super$3$defineClass(String var1, ByteBuffer var2, CodeSource var3) {
      return super.defineClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$setPackageAssertionStatus(String var1, boolean var2) {
      super.setPackageAssertionStatus(var1, var2);
   }

   // $FF: synthetic method
   public String super$5$generateScriptName() {
      return super.generateScriptName();
   }

   // $FF: synthetic method
   public Class super$5$parseClass(String var1, String var2) {
      return super.parseClass(var1, var2);
   }

   // $FF: synthetic method
   public void super$5$addClasspath(String var1) {
      super.addClasspath(var1);
   }

   // $FF: synthetic method
   public Package super$2$definePackage(String var1, String var2, String var3, String var4, String var5, String var6, String var7, URL var8) {
      return super.definePackage(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   // $FF: synthetic method
   public Class super$5$defineClass(ClassNode var1, String var2) {
      return super.defineClass(var1, var2);
   }

   // $FF: synthetic method
   public void super$5$addURL(URL var1) {
      super.addURL(var1);
   }

   // $FF: synthetic method
   public Class super$5$loadClass(String var1, boolean var2, boolean var3) {
      return super.loadClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$5$loadClass(String var1, boolean var2) {
      return super.loadClass(var1, var2);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$2$setSigners(Class var1, Object[] var2) {
      super.setSigners(var1, var2);
   }

   // $FF: synthetic method
   public Class super$5$parseClass(File var1) {
      return super.parseClass(var1);
   }

   // $FF: synthetic method
   public CompilationUnit super$5$createCompilationUnit(CompilerConfiguration var1, CodeSource var2) {
      return super.createCompilationUnit(var1, var2);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public Class super$2$findLoadedClass(String var1) {
      return super.findLoadedClass(var1);
   }

   // $FF: synthetic method
   public void super$4$close() {
      super.close();
   }

   // $FF: synthetic method
   public Package super$4$definePackage(String var1, Manifest var2, URL var3) {
      return super.definePackage(var1, var2, var3);
   }

   // $FF: synthetic method
   public boolean super$5$isRecompilable(Class var1) {
      return super.isRecompilable(var1);
   }

   // $FF: synthetic method
   public Class super$5$parseClass(InputStream var1, String var2) {
      return super.parseClass(var1, var2);
   }

   // $FF: synthetic method
   public Package[] super$2$getPackages() {
      return super.getPackages();
   }

   // $FF: synthetic method
   public void super$5$expandClassPath(List var1, String var2, String var3, boolean var4) {
      super.expandClassPath(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public Class super$5$parseClass(GroovyCodeSource var1, boolean var2) {
      return super.parseClass(var1, var2);
   }

   // $FF: synthetic method
   public Class super$5$defineClass(ClassNode var1, String var2, String var3) {
      return super.defineClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$2$loadClass(String var1) {
      return super.loadClass(var1);
   }

   // $FF: synthetic method
   public Enumeration super$2$getResources(String var1) {
      return super.getResources(var1);
   }

   // $FF: synthetic method
   public Class super$5$recompile(URL var1, String var2, Class var3) {
      return super.recompile(var1, var2, var3);
   }

   // $FF: synthetic method
   public PermissionCollection super$5$getPermissions(CodeSource var1) {
      return super.getPermissions(var1);
   }

   // $FF: synthetic method
   public void super$2$setClassAssertionStatus(String var1, boolean var2) {
      super.setClassAssertionStatus(var1, var2);
   }

   // $FF: synthetic method
   public void super$5$setResourceLoader(GroovyResourceLoader var1) {
      super.setResourceLoader(var1);
   }

   // $FF: synthetic method
   public void super$2$resolveClass(Class var1) {
      super.resolveClass(var1);
   }

   // $FF: synthetic method
   public String[] super$5$getClassPath() {
      return super.getClassPath();
   }

   // $FF: synthetic method
   public URL super$2$getResource(String var1) {
      return super.getResource(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public GroovyClassLoader.ClassCollector super$5$createCollector(CompilationUnit var1, SourceUnit var2) {
      return super.createCollector(var1, var2);
   }

   // $FF: synthetic method
   public Enumeration super$4$findResources(String var1) {
      return super.findResources(var1);
   }

   // $FF: synthetic method
   public Class super$2$defineClass(String var1, ByteBuffer var2, ProtectionDomain var3) {
      return super.defineClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$5$defineClass(String var1, byte[] var2, ProtectionDomain var3) {
      return super.defineClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public Class super$4$findClass(String var1) {
      return super.findClass(var1);
   }

   // $FF: synthetic method
   public URL[] super$4$getURLs() {
      return super.getURLs();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$5$clearCache() {
      super.clearCache();
   }

   // $FF: synthetic method
   public Class super$5$parseClass(String var1) {
      return super.parseClass(var1);
   }

   // $FF: synthetic method
   public Class super$2$defineClass(String var1, byte[] var2, int var3, int var4, ProtectionDomain var5) {
      return super.defineClass(var1, var2, var3, var4, var5);
   }

   // $FF: synthetic method
   public void super$2$setDefaultAssertionStatus(boolean var1) {
      super.setDefaultAssertionStatus(var1);
   }

   // $FF: synthetic method
   public void super$5$setClassCacheEntry(Class var1) {
      super.setClassCacheEntry(var1);
   }

   // $FF: synthetic method
   public ClassLoader super$2$getParent() {
      return super.getParent();
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public Class super$5$defineClass(String var1, byte[] var2) {
      return super.defineClass(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$5$isSourceNewer(URL var1, Class var2) {
      return super.isSourceNewer(var1, var2);
   }

   // $FF: synthetic method
   public void super$2$clearAssertionStatus() {
      super.clearAssertionStatus();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Class super$2$defineClass(byte[] var1, int var2, int var3) {
      return super.defineClass(var1, var2, var3);
   }

   // $FF: synthetic method
   public Package super$2$getPackage(String var1) {
      return super.getPackage(var1);
   }

   // $FF: synthetic method
   public Boolean super$5$isShouldRecompile() {
      return super.isShouldRecompile();
   }

   // $FF: synthetic method
   public Class[] super$5$getLoadedClasses() {
      return super.getLoadedClasses();
   }

   // $FF: synthetic method
   public Object super$2$getClassLoadingLock(String var1) {
      return super.getClassLoadingLock(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public String super$2$findLibrary(String var1) {
      return super.findLibrary(var1);
   }

   // $FF: synthetic method
   public GroovyResourceLoader super$5$getResourceLoader() {
      return super.getResourceLoader();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Class super$2$findSystemClass(String var1) {
      return super.findSystemClass(var1);
   }

   // $FF: synthetic method
   public long super$5$getTimeStamp(Class var1) {
      return super.getTimeStamp(var1);
   }

   // $FF: synthetic method
   public Class super$5$loadClass(String var1, boolean var2, boolean var3, boolean var4) {
      return super.loadClass(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public Class super$5$getClassCacheEntry(String var1) {
      return super.getClassCacheEntry(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public URL super$4$findResource(String var1) {
      return super.findResource(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "addPhaseOperation";
      var0[1] = "<$constructor$>";
      var0[2] = "getPhaseNumber";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[3];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader(), var0);
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
   private static Class $get$$class$org$codehaus$groovy$transform$ASTTransformation() {
      Class var10000 = $class$org$codehaus$groovy$transform$ASTTransformation;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$transform$ASTTransformation = class$("org.codehaus.groovy.transform.ASTTransformation");
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
   private static Class $get$$class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader() {
      Class var10000 = $class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$ast$TestHarnessClassLoader = class$("org.codehaus.groovy.tools.ast.TestHarnessClassLoader");
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
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
   private static Class $get$$class$org$codehaus$groovy$tools$ast$TestHarnessOperation() {
      Class var10000 = $class$org$codehaus$groovy$tools$ast$TestHarnessOperation;
      if (var10000 == null) {
         var10000 = $class$org$codehaus$groovy$tools$ast$TestHarnessOperation = class$("org.codehaus.groovy.tools.ast.TestHarnessOperation");
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
