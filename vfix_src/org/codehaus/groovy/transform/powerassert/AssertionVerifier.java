package org.codehaus.groovy.transform.powerassert;

import java.io.FileWriter;
import java.io.IOException;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public abstract class AssertionVerifier {
   public static final String VERIFY_METHOD_NAME = "verify";
   private static final String LOG_FILE = null;

   public static void verify(Object truthValue, String text, ValueRecorder recorder) {
      if (LOG_FILE != null) {
         log(text, recorder);
      }

      if (!DefaultTypeTransformation.castToBoolean(truthValue)) {
         String msg = AssertionRenderer.render(text, recorder);
         Error error = new PowerAssertionError(msg);
         filterStackTrace(error);
         throw error;
      }
   }

   private static void log(String text, ValueRecorder recorder) {
      FileWriter out = null;

      try {
         out = new FileWriter(LOG_FILE, true);
         out.write(AssertionRenderer.render(text, recorder));
         out.write("\n\n");
      } catch (IOException var11) {
         throw new RuntimeException(var11);
      } finally {
         try {
            if (out != null) {
               out.close();
            }
         } catch (IOException var10) {
         }

      }

   }

   private static void filterStackTrace(Throwable error) {
      StackTraceElement[] stackTrace = error.getStackTrace();

      for(int i = 0; i < stackTrace.length; ++i) {
         StackTraceElement elem = stackTrace[i];
         if (!elem.getClassName().startsWith("org.codehaus.groovy.transform.powerassert") && !elem.getClassName().startsWith("org.codehaus.groovy.runtime.callsite")) {
            StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - i];
            System.arraycopy(stackTrace, i, newStackTrace, 0, newStackTrace.length);
            error.setStackTrace(newStackTrace);
            return;
         }
      }

   }
}
