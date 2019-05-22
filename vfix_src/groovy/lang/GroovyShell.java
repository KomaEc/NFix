package groovy.lang;

import groovy.security.GroovyCodeSourcePermission;
import groovy.ui.GroovyMain;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.ProcessingUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;

public class GroovyShell extends GroovyObjectSupport {
   public static final String DEFAULT_CODE_BASE = "/groovy/shell";
   /** @deprecated */
   @Deprecated
   public static final String[] EMPTY_ARGS = new String[0];
   private Binding context;
   private int counter;
   private CompilerConfiguration config;
   private GroovyClassLoader loader;

   public static void main(String[] args) {
      GroovyMain.main(args);
   }

   public GroovyShell() {
      this((ClassLoader)null, (Binding)(new Binding()));
   }

   public GroovyShell(Binding binding) {
      this((ClassLoader)null, (Binding)binding);
   }

   public GroovyShell(CompilerConfiguration config) {
      this(new Binding(), config);
   }

   public GroovyShell(Binding binding, CompilerConfiguration config) {
      this((ClassLoader)null, binding, config);
   }

   public GroovyShell(ClassLoader parent, Binding binding) {
      this(parent, binding, CompilerConfiguration.DEFAULT);
   }

   public GroovyShell(ClassLoader parent) {
      this(parent, new Binding(), CompilerConfiguration.DEFAULT);
   }

   public GroovyShell(ClassLoader parent, Binding binding, final CompilerConfiguration config) {
      if (binding == null) {
         throw new IllegalArgumentException("Binding must not be null.");
      } else if (config == null) {
         throw new IllegalArgumentException("Compiler configuration must not be null.");
      } else {
         final ClassLoader parentLoader = parent != null ? parent : GroovyShell.class.getClassLoader();
         this.loader = (GroovyClassLoader)AccessController.doPrivileged(new PrivilegedAction<GroovyClassLoader>() {
            public GroovyClassLoader run() {
               return new GroovyClassLoader(parentLoader, config);
            }
         });
         this.context = binding;
         this.config = config;
      }
   }

   /** @deprecated */
   @Deprecated
   public void initializeBinding() {
      Map map = this.context.getVariables();
      if (map.get("shell") == null) {
         map.put("shell", this);
      }

   }

   public void resetLoadedClasses() {
      this.loader.clearCache();
   }

   public GroovyShell(GroovyShell shell) {
      this((ClassLoader)shell.loader, (Binding)shell.context);
   }

   public Binding getContext() {
      return this.context;
   }

   public GroovyClassLoader getClassLoader() {
      return this.loader;
   }

   public Object getProperty(String property) {
      Object answer = this.getVariable(property);
      if (answer == null) {
         answer = super.getProperty(property);
      }

      return answer;
   }

   public void setProperty(String property, Object newValue) {
      this.setVariable(property, newValue);

      try {
         super.setProperty(property, newValue);
      } catch (GroovyRuntimeException var4) {
      }

   }

   public Object run(File scriptFile, List list) throws CompilationFailedException, IOException {
      String[] args = new String[list.size()];
      return this.run(scriptFile, (String[])((String[])list.toArray(args)));
   }

   public Object run(String scriptText, String fileName, List list) throws CompilationFailedException {
      String[] args = new String[list.size()];
      list.toArray(args);
      return this.run(scriptText, fileName, args);
   }

   public Object run(final File scriptFile, String[] args) throws CompilationFailedException, IOException {
      String scriptName = scriptFile.getName();
      int p = scriptName.lastIndexOf(".");
      if (p++ >= 0 && scriptName.substring(p).equals("java")) {
         throw new CompilationFailedException(0, (ProcessingUnit)null);
      } else {
         final Thread thread = Thread.currentThread();

         class DoSetContext implements PrivilegedAction {
            ClassLoader classLoader;

            public DoSetContext(ClassLoader loader) {
               this.classLoader = loader;
            }

            public Object run() {
               thread.setContextClassLoader(this.classLoader);
               return null;
            }
         }

         AccessController.doPrivileged(new DoSetContext(this.loader));

         Class scriptClass;
         try {
            scriptClass = (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class>() {
               public Class run() throws CompilationFailedException, IOException {
                  return GroovyShell.this.loader.parseClass(scriptFile);
               }
            });
         } catch (PrivilegedActionException var9) {
            Exception e = var9.getException();
            if (e instanceof CompilationFailedException) {
               throw (CompilationFailedException)e;
            }

            if (e instanceof IOException) {
               throw (IOException)e;
            }

            throw (RuntimeException)var9.getException();
         }

         return this.runScriptOrMainOrTestOrRunnable(scriptClass, args);
      }
   }

   private Object runScriptOrMainOrTestOrRunnable(Class scriptClass, String[] args) {
      if (scriptClass == null) {
         return null;
      } else {
         if (Script.class.isAssignableFrom(scriptClass)) {
            Script script = null;

            try {
               script = (Script)scriptClass.newInstance();
            } catch (InstantiationException var5) {
            } catch (IllegalAccessException var6) {
            }

            if (script != null) {
               script.setBinding(this.context);
               script.setProperty("args", args);
               return script.run();
            }
         }

         try {
            scriptClass.getMethod("main", String[].class);
            return InvokerHelper.invokeMethod(scriptClass, "main", new Object[]{args});
         } catch (NoSuchMethodException var7) {
            if (Runnable.class.isAssignableFrom(scriptClass)) {
               return this.runRunnable(scriptClass, args);
            } else if (this.isJUnit3Test(scriptClass)) {
               return this.runJUnit3Test(scriptClass);
            } else if (this.isJUnit3TestSuite(scriptClass)) {
               return this.runJUnit3TestSuite(scriptClass);
            } else if (this.isJUnit4Test(scriptClass)) {
               return this.runJUnit4Test(scriptClass);
            } else if (this.isTestNgTest(scriptClass)) {
               return this.runTestNgTest(scriptClass);
            } else {
               throw new GroovyRuntimeException("This script or class could not be run.\nIt should either: \n- have a main method, \n- be a JUnit test, TestNG test or extend GroovyTestCase, \n- or implement the Runnable interface.");
            }
         }
      }
   }

   private Object runRunnable(Class scriptClass, String[] args) {
      Constructor constructor = null;
      Runnable runnable = null;
      Object reason = null;

      try {
         constructor = scriptClass.getConstructor((new String[0]).getClass());

         try {
            runnable = (Runnable)constructor.newInstance(args);
         } catch (Throwable var11) {
            reason = var11;
         }
      } catch (NoSuchMethodException var12) {
         try {
            constructor = scriptClass.getConstructor();

            try {
               runnable = (Runnable)constructor.newInstance();
            } catch (InvocationTargetException var8) {
               throw new InvokerInvocationException(var8.getTargetException());
            } catch (Throwable var9) {
               reason = var9;
            }
         } catch (NoSuchMethodException var10) {
            reason = var10;
         }
      }

      if (constructor != null && runnable != null) {
         runnable.run();
         return null;
      } else {
         throw new GroovyRuntimeException("This script or class was runnable but could not be run. ", (Throwable)reason);
      }
   }

   private Object runJUnit3Test(Class scriptClass) {
      try {
         Object testSuite = InvokerHelper.invokeConstructorOf((String)"junit.framework.TestSuite", new Object[]{scriptClass});
         return InvokerHelper.invokeStaticMethod((String)"junit.textui.TestRunner", "run", new Object[]{testSuite});
      } catch (ClassNotFoundException var3) {
         throw new GroovyRuntimeException("Failed to run the unit test. JUnit is not on the Classpath.", var3);
      }
   }

   private Object runJUnit3TestSuite(Class scriptClass) {
      try {
         Object testSuite = InvokerHelper.invokeStaticMethod((Class)scriptClass, "suite", new Object[0]);
         return InvokerHelper.invokeStaticMethod((String)"junit.textui.TestRunner", "run", new Object[]{testSuite});
      } catch (ClassNotFoundException var3) {
         throw new GroovyRuntimeException("Failed to run the unit test. JUnit is not on the Classpath.", var3);
      }
   }

   private Object runJUnit4Test(Class scriptClass) {
      try {
         return InvokerHelper.invokeStaticMethod((String)"org.codehaus.groovy.vmplugin.v5.JUnit4Utils", "realRunJUnit4Test", new Object[]{scriptClass, this.loader});
      } catch (ClassNotFoundException var3) {
         throw new GroovyRuntimeException("Failed to run the JUnit 4 test.", var3);
      }
   }

   private Object runTestNgTest(Class scriptClass) {
      try {
         return InvokerHelper.invokeStaticMethod((String)"org.codehaus.groovy.vmplugin.v5.TestNgUtils", "realRunTestNgTest", new Object[]{scriptClass, this.loader});
      } catch (ClassNotFoundException var3) {
         throw new GroovyRuntimeException("Failed to run the TestNG test.", var3);
      }
   }

   private boolean isJUnit3Test(Class scriptClass) {
      boolean isUnitTestCase = false;

      try {
         try {
            Class testCaseClass = this.loader.loadClass("junit.framework.TestCase");
            if (testCaseClass.isAssignableFrom(scriptClass)) {
               isUnitTestCase = true;
            }
         } catch (ClassNotFoundException var4) {
         }
      } catch (Throwable var5) {
      }

      return isUnitTestCase;
   }

   private boolean isJUnit3TestSuite(Class scriptClass) {
      boolean isUnitTestSuite = false;

      try {
         try {
            Class testSuiteClass = this.loader.loadClass("junit.framework.TestSuite");
            if (testSuiteClass.isAssignableFrom(scriptClass)) {
               isUnitTestSuite = true;
            }
         } catch (ClassNotFoundException var4) {
         }
      } catch (Throwable var5) {
      }

      return isUnitTestSuite;
   }

   private boolean isJUnit4Test(Class scriptClass) {
      char version = System.getProperty("java.version").charAt(2);
      if (version < '5') {
         return false;
      } else {
         boolean isTest = false;

         try {
            if (InvokerHelper.invokeStaticMethod((String)"org.codehaus.groovy.vmplugin.v5.JUnit4Utils", "realIsJUnit4Test", new Object[]{scriptClass, this.loader}) == Boolean.TRUE) {
               isTest = true;
            }

            return isTest;
         } catch (ClassNotFoundException var5) {
            throw new GroovyRuntimeException("Failed to invoke the JUnit 4 helper class.", var5);
         }
      }
   }

   private boolean isTestNgTest(Class scriptClass) {
      char version = System.getProperty("java.version").charAt(2);
      if (version < '5') {
         return false;
      } else {
         boolean isTest = false;

         try {
            if (InvokerHelper.invokeStaticMethod((String)"org.codehaus.groovy.vmplugin.v5.TestNgUtils", "realIsTestNgTest", new Object[]{scriptClass, this.loader}) == Boolean.TRUE) {
               isTest = true;
            }

            return isTest;
         } catch (ClassNotFoundException var5) {
            throw new GroovyRuntimeException("Failed to invoke the TestNG helper class.", var5);
         }
      }
   }

   public Object run(final String scriptText, final String fileName, String[] args) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            return new GroovyCodeSource(scriptText, fileName, "/groovy/shell");
         }
      });
      Class scriptClass = this.parseClass(gcs);
      return this.runScriptOrMainOrTestOrRunnable(scriptClass, args);
   }

   public Object run(final Reader in, final String fileName, String[] args) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            return new GroovyCodeSource(in, fileName, "/groovy/shell");
         }
      });
      Class scriptClass = this.parseClass(gcs);
      return this.runScriptOrMainOrTestOrRunnable(scriptClass, args);
   }

   /** @deprecated */
   public Object run(final InputStream in, final String fileName, String[] args) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            try {
               String scriptText = GroovyShell.this.config.getSourceEncoding() != null ? DefaultGroovyMethods.getText(in, GroovyShell.this.config.getSourceEncoding()) : DefaultGroovyMethods.getText(in);
               return new GroovyCodeSource(scriptText, fileName, "/groovy/shell");
            } catch (IOException var2) {
               throw new RuntimeException("Impossible to read the content of the input stream for file named: " + fileName, var2);
            }
         }
      });
      Class scriptClass = this.parseClass(gcs);
      return this.runScriptOrMainOrTestOrRunnable(scriptClass, args);
   }

   public Object getVariable(String name) {
      return this.context.getVariables().get(name);
   }

   public void setVariable(String name, Object value) {
      this.context.setVariable(name, value);
   }

   public Object evaluate(GroovyCodeSource codeSource) throws CompilationFailedException {
      Script script = this.parse(codeSource);
      script.setBinding(this.context);
      return script.run();
   }

   public Object evaluate(String scriptText) throws CompilationFailedException {
      return this.evaluate(scriptText, this.generateScriptName(), "/groovy/shell");
   }

   public Object evaluate(String scriptText, String fileName) throws CompilationFailedException {
      return this.evaluate(scriptText, fileName, "/groovy/shell");
   }

   public Object evaluate(final String scriptText, final String fileName, final String codeBase) throws CompilationFailedException {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
         sm.checkPermission(new GroovyCodeSourcePermission(codeBase));
      }

      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            return new GroovyCodeSource(scriptText, fileName, codeBase);
         }
      });
      return this.evaluate(gcs);
   }

   public Object evaluate(File file) throws CompilationFailedException, IOException {
      return this.evaluate(new GroovyCodeSource(file, this.config.getSourceEncoding()));
   }

   public Object evaluate(Reader in) throws CompilationFailedException {
      return this.evaluate(in, this.generateScriptName());
   }

   public Object evaluate(Reader in, String fileName) throws CompilationFailedException {
      Script script = null;

      Object var4;
      try {
         script = this.parse(in, fileName);
         script.setBinding(this.context);
         var4 = script.run();
      } finally {
         if (script != null) {
            InvokerHelper.removeClass(script.getClass());
         }

      }

      return var4;
   }

   /** @deprecated */
   public Object evaluate(InputStream in) throws CompilationFailedException {
      return this.evaluate(in, this.generateScriptName());
   }

   /** @deprecated */
   public Object evaluate(InputStream in, String fileName) throws CompilationFailedException {
      Script script = null;

      Object var4;
      try {
         script = this.parse(in, fileName);
         script.setBinding(this.context);
         var4 = script.run();
      } finally {
         if (script != null) {
            InvokerHelper.removeClass(script.getClass());
         }

      }

      return var4;
   }

   public Script parse(Reader reader, String fileName) throws CompilationFailedException {
      return this.parse(new GroovyCodeSource(reader, fileName, "/groovy/shell"));
   }

   /** @deprecated */
   public Script parse(final InputStream in, final String fileName) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            try {
               String scriptText = GroovyShell.this.config.getSourceEncoding() != null ? DefaultGroovyMethods.getText(in, GroovyShell.this.config.getSourceEncoding()) : DefaultGroovyMethods.getText(in);
               return new GroovyCodeSource(scriptText, fileName, "/groovy/shell");
            } catch (IOException var2) {
               throw new RuntimeException("Impossible to read the content of the input stream for file named: " + fileName, var2);
            }
         }
      });
      return this.parse(gcs);
   }

   private Class parseClass(GroovyCodeSource codeSource) throws CompilationFailedException {
      return this.loader.parseClass(codeSource, false);
   }

   public Script parse(GroovyCodeSource codeSource) throws CompilationFailedException {
      return InvokerHelper.createScript(this.parseClass(codeSource), this.context);
   }

   public Script parse(File file) throws CompilationFailedException, IOException {
      return this.parse(new GroovyCodeSource(file, this.config.getSourceEncoding()));
   }

   public Script parse(String scriptText) throws CompilationFailedException {
      return this.parse(scriptText, this.generateScriptName());
   }

   public Script parse(final String scriptText, final String fileName) throws CompilationFailedException {
      GroovyCodeSource gcs = (GroovyCodeSource)AccessController.doPrivileged(new PrivilegedAction<GroovyCodeSource>() {
         public GroovyCodeSource run() {
            return new GroovyCodeSource(scriptText, fileName, "/groovy/shell");
         }
      });
      return this.parse(gcs);
   }

   public Script parse(Reader in) throws CompilationFailedException {
      return this.parse(in, this.generateScriptName());
   }

   /** @deprecated */
   public Script parse(InputStream in) throws CompilationFailedException {
      return this.parse(in, this.generateScriptName());
   }

   protected synchronized String generateScriptName() {
      return "Script" + ++this.counter + ".groovy";
   }
}
