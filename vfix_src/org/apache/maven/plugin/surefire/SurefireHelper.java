package org.apache.maven.plugin.surefire;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.surefire.suite.RunResult;

public final class SurefireHelper {
   private SurefireHelper() {
      throw new IllegalAccessError("Utility class");
   }

   public static void reportExecution(SurefireReportParameters reportParameters, RunResult result, Log log) throws MojoFailureException, MojoExecutionException {
      boolean timeoutOrOtherFailure = result.isFailureOrTimeout();
      if (!timeoutOrOtherFailure) {
         if (result.getCompletedCount() == 0) {
            if (reportParameters.getFailIfNoTests() != null && reportParameters.getFailIfNoTests()) {
               throw new MojoFailureException("No tests were executed!  (Set -DfailIfNoTests=false to ignore this error.)");
            }

            return;
         }

         if (result.isErrorFree()) {
            return;
         }
      }

      String msg = timeoutOrOtherFailure ? "There was a timeout or other error in the fork" : "There are test failures.\n\nPlease refer to " + reportParameters.getReportsDirectory() + " for the individual test results.";
      if (reportParameters.isTestFailureIgnore()) {
         log.error((CharSequence)msg);
      } else if (result.isFailure()) {
         throw new MojoExecutionException(msg);
      } else {
         throw new MojoFailureException(msg);
      }
   }
}
