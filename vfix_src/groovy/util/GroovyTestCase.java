package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class GroovyTestCase extends TestCase {
   protected static Logger log = Logger.getLogger(GroovyTestCase.class.getName());
   private static int counter;
   private static final int MAX_NESTED_EXCEPTIONS = 10;
   public static final String TEST_SCRIPT_NAME_PREFIX = "TestScript";
   private boolean useAgileDoxNaming = false;
   private static final ThreadLocal notYetImplementedFlag = new ThreadLocal();

   public String getName() {
      return this.useAgileDoxNaming ? super.getName().substring(4).replaceAll("([A-Z])", " $1").toLowerCase() : super.getName();
   }

   public String getMethodName() {
      return super.getName();
   }

   protected void assertArrayEquals(Object[] expected, Object[] value) {
      String message = "expected array: " + InvokerHelper.toString(expected) + " value array: " + InvokerHelper.toString(value);
      assertNotNull(message + ": expected should not be null", expected);
      assertNotNull(message + ": value should not be null", value);
      assertEquals(message, expected.length, value.length);
      int i = 0;

      for(int size = expected.length; i < size; ++i) {
         assertEquals("value[" + i + "] when " + message, expected[i], value[i]);
      }

   }

   protected void assertLength(int length, char[] array) {
      assertEquals(length, array.length);
   }

   protected void assertLength(int length, int[] array) {
      assertEquals(length, array.length);
   }

   protected void assertLength(int length, Object[] array) {
      assertEquals(length, array.length);
   }

   protected void assertContains(char expected, char[] array) {
      for(int i = 0; i < array.length; ++i) {
         if (array[i] == expected) {
            return;
         }
      }

      StringBuffer message = new StringBuffer();
      message.append(expected).append(" not in {");

      for(int i = 0; i < array.length; ++i) {
         message.append("'").append(array[i]).append("'");
         if (i < array.length - 1) {
            message.append(", ");
         }
      }

      message.append(" }");
      fail(message.toString());
   }

   protected void assertContains(int expected, int[] array) {
      int[] arr$ = array;
      int i = array.length;

      for(int i$ = 0; i$ < i; ++i$) {
         int anInt = arr$[i$];
         if (anInt == expected) {
            return;
         }
      }

      StringBuffer message = new StringBuffer();
      message.append(expected).append(" not in {");

      for(i = 0; i < array.length; ++i) {
         message.append("'").append(array[i]).append("'");
         if (i < array.length - 1) {
            message.append(", ");
         }
      }

      message.append(" }");
      fail(message.toString());
   }

   protected void assertToString(Object value, String expected) {
      Object console = InvokerHelper.invokeMethod(value, "toString", (Object)null);
      assertEquals("toString() on value: " + value, expected, console);
   }

   protected void assertInspect(Object value, String expected) {
      Object console = InvokerHelper.invokeMethod(value, "inspect", (Object)null);
      assertEquals("inspect() on value: " + value, expected, console);
   }

   protected void assertScript(String script) throws Exception {
      GroovyShell shell = new GroovyShell();
      shell.evaluate(script, this.getTestClassName());
   }

   protected String getTestClassName() {
      return "TestScript" + this.getMethodName() + counter++ + ".groovy";
   }

   protected String shouldFail(Closure code) {
      boolean failed = false;
      String result = null;

      try {
         code.call();
      } catch (GroovyRuntimeException var5) {
         failed = true;
         result = ScriptBytecodeAdapter.unwrap(var5).getMessage();
      } catch (Throwable var6) {
         failed = true;
         result = var6.getMessage();
      }

      assertTrue("Closure " + code + " should have failed", failed);
      return result;
   }

   protected String shouldFail(Class clazz, Closure code) {
      Throwable th = null;

      try {
         code.call();
      } catch (GroovyRuntimeException var5) {
         th = ScriptBytecodeAdapter.unwrap(var5);
      } catch (Throwable var6) {
         th = var6;
      }

      if (th == null) {
         fail("Closure " + code + " should have failed with an exception of type " + clazz.getName());
      } else if (!clazz.isInstance(th)) {
         fail("Closure " + code + " should have failed with an exception of type " + clazz.getName() + ", instead got Exception " + th);
      }

      return th.getMessage();
   }

   protected String shouldFailWithCause(Class clazz, Closure code) {
      Throwable th = null;
      Throwable orig = null;
      int level = 0;

      try {
         code.call();
      } catch (GroovyRuntimeException var7) {
         orig = ScriptBytecodeAdapter.unwrap(var7);
         th = orig.getCause();
      } catch (Throwable var8) {
         orig = var8;
         th = var8.getCause();
      }

      while(th != null && !clazz.isInstance(th) && th != th.getCause() && level < 10) {
         th = th.getCause();
         ++level;
      }

      if (orig == null) {
         fail("Closure " + code + " should have failed with an exception caused by type " + clazz.getName());
      } else if (th == null || !clazz.isInstance(th)) {
         fail("Closure " + code + " should have failed with an exception caused by type " + clazz.getName() + ", instead found these Exceptions:\n" + this.buildExceptionList(orig));
      }

      return th.getMessage();
   }

   private String buildExceptionList(Throwable th) {
      StringBuilder sb = new StringBuilder();

      for(int level = 0; th != null; ++level) {
         if (level > 1) {
            for(int i = 0; i < level - 1; ++i) {
               sb.append("   ");
            }
         }

         if (level > 0) {
            sb.append("-> ");
         }

         if (level > 10) {
            sb.append("...");
            break;
         }

         sb.append(th.getClass().getName()).append(": ").append(th.getMessage()).append("\n");
         if (th == th.getCause()) {
            break;
         }

         th = th.getCause();
      }

      return sb.toString();
   }

   protected String fixEOLs(String value) {
      return value.replaceAll("(\\r\\n?)|\n", "\n");
   }

   public static boolean notYetImplemented(TestCase caller) {
      if (notYetImplementedFlag.get() != null) {
         return false;
      } else {
         notYetImplementedFlag.set(Boolean.TRUE);
         Method testMethod = findRunningJUnitTestMethod(caller.getClass());

         try {
            log.info("Running " + testMethod.getName() + " as not yet implemented");
            testMethod.invoke(caller, (Object[])(new Class[0]));
            fail(testMethod.getName() + " is marked as not yet implemented but passes unexpectedly");
         } catch (Exception var6) {
            log.info(testMethod.getName() + " fails which is expected as it is not yet implemented");
         } finally {
            notYetImplementedFlag.set((Object)null);
         }

         return true;
      }
   }

   public boolean notYetImplemented() {
      return notYetImplemented(this);
   }

   private static Method findRunningJUnitTestMethod(Class caller) {
      Class[] args = new Class[0];
      Throwable t = new Exception();

      for(int i = t.getStackTrace().length - 1; i >= 0; --i) {
         StackTraceElement element = t.getStackTrace()[i];
         if (element.getClassName().equals(caller.getName())) {
            try {
               Method m = caller.getMethod(element.getMethodName(), args);
               if (isPublicTestMethod(m)) {
                  return m;
               }
            } catch (Exception var6) {
            }
         }
      }

      throw new RuntimeException("No JUnit test case method found in call stack");
   }

   private static boolean isPublicTestMethod(Method method) {
      String name = method.getName();
      Class[] parameters = method.getParameterTypes();
      Class returnType = method.getReturnType();
      return parameters.length == 0 && name.startsWith("test") && returnType.equals(Void.TYPE) && Modifier.isPublic(method.getModifiers());
   }

   public static void assertEquals(String message, Object expected, Object actual) {
      if (expected != null || actual != null) {
         if (expected == null || !DefaultTypeTransformation.compareEqual(expected, actual)) {
            failNotEquals(message, expected, actual);
         }
      }
   }

   public static void assertEquals(Object expected, Object actual) {
      assertEquals((String)null, expected, actual);
   }

   public static void assertEquals(String expected, String actual) {
      assertEquals((String)null, expected, actual);
   }
}
