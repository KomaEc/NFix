package groovy.util;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.codehaus.groovy.runtime.ScriptTestAdapter;

public class GroovyTestSuite extends TestSuite {
   protected static String file = null;
   protected final GroovyClassLoader loader = new GroovyClassLoader(GroovyTestSuite.class.getClassLoader());

   public static void main(String[] args) {
      if (args.length > 0) {
         file = args[0];
      }

      TestRunner.run(suite());
   }

   public static Test suite() {
      GroovyTestSuite suite = new GroovyTestSuite();

      try {
         suite.loadTestSuite();
         return suite;
      } catch (Exception var2) {
         throw new RuntimeException("Could not create the test suite: " + var2, var2);
      }
   }

   public void loadTestSuite() throws Exception {
      String fileName = System.getProperty("test", file);
      if (fileName == null) {
         throw new RuntimeException("No filename given in the 'test' system property so cannot run a Groovy unit test");
      } else {
         System.out.println("Compiling: " + fileName);
         Class type = this.compile(fileName);
         String[] args = new String[0];
         if (!Test.class.isAssignableFrom(type) && Script.class.isAssignableFrom(type)) {
            this.addTest(new ScriptTestAdapter(type, args));
         } else {
            this.addTestSuite(type);
         }

      }
   }

   public Class compile(String fileName) throws Exception {
      return this.loader.parseClass(new File(fileName));
   }
}
