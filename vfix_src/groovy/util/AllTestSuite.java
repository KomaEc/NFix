package groovy.util;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.ScriptTestAdapter;

public class AllTestSuite extends TestSuite {
   public static final String SYSPROP_TEST_DIR = "groovy.test.dir";
   public static final String SYSPROP_TEST_PATTERN = "groovy.test.pattern";
   public static final String SYSPROP_TEST_EXCLUDES_PATTERN = "groovy.test.excludesPattern";
   private static final Logger LOG = Logger.getLogger(AllTestSuite.class.getName());
   private static final ClassLoader JAVA_LOADER = AllTestSuite.class.getClassLoader();
   private static final GroovyClassLoader GROOVY_LOADER;
   private static final String[] EMPTY_ARGS;
   private static IFileNameFinder finder;

   public static Test suite() {
      String basedir = System.getProperty("groovy.test.dir", "./test/");
      String pattern = System.getProperty("groovy.test.pattern", "**/*Test.groovy");
      String excludesPattern = System.getProperty("groovy.test.excludesPattern", "");
      return suite(basedir, pattern);
   }

   public static Test suite(String basedir, String pattern) {
      return suite(basedir, pattern, "");
   }

   public static Test suite(String basedir, String pattern, String excludesPattern) {
      AllTestSuite suite = new AllTestSuite();
      String fileName = "";

      try {
         Collection filenames = excludesPattern.length() > 0 ? finder.getFileNames(basedir, pattern, excludesPattern) : finder.getFileNames(basedir, pattern);
         Iterator iter = filenames.iterator();

         while(iter.hasNext()) {
            fileName = (String)iter.next();
            LOG.finest("trying to load " + fileName);
            suite.loadTest(fileName);
         }

         return suite;
      } catch (CompilationFailedException var7) {
         var7.printStackTrace();
         throw new RuntimeException("CompilationFailedException when loading " + fileName, var7);
      } catch (IOException var8) {
         throw new RuntimeException("IOException when loading " + fileName, var8);
      }
   }

   protected void loadTest(String fileName) throws CompilationFailedException, IOException {
      Class type = this.compile(fileName);
      if (!Test.class.isAssignableFrom(type) && Script.class.isAssignableFrom(type)) {
         this.addTest(new ScriptTestAdapter(type, EMPTY_ARGS));
      } else {
         this.addTestSuite(type);
      }

   }

   protected Class compile(String fileName) throws CompilationFailedException, IOException {
      return GROOVY_LOADER.parseClass(new File(fileName));
   }

   static {
      GROOVY_LOADER = new GroovyClassLoader(JAVA_LOADER);
      EMPTY_ARGS = new String[0];
      finder = null;

      try {
         Class finderClass = Class.forName("groovy.util.FileNameFinder");
         finder = (IFileNameFinder)finderClass.newInstance();
      } catch (Exception var1) {
         throw new RuntimeException("Cannot find and instantiate class FileNameFinder", var1);
      }
   }
}
