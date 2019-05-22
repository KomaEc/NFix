package org.apache.maven.surefire.suite;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.IOUtil;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.PrettyPrintXMLWriter;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.XMLWriter;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.Xpp3Dom;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.Xpp3DomBuilder;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml.Xpp3DomWriter;

public class RunResult {
   private final int completedCount;
   private final int errors;
   private final int failures;
   private final int skipped;
   private final String failure;
   private final boolean timeout;
   public static final int SUCCESS = 0;
   private static final int FAILURE = 255;
   private static final int NO_TESTS = 254;

   public static RunResult timeout(RunResult accumulatedAtTimeout) {
      return errorCode(accumulatedAtTimeout, accumulatedAtTimeout.getFailure(), true);
   }

   public static RunResult failure(RunResult accumulatedAtTimeout, Exception cause) {
      return errorCode(accumulatedAtTimeout, getStackTrace(cause), accumulatedAtTimeout.isTimeout());
   }

   private static RunResult errorCode(RunResult other, String failure, boolean timeout) {
      return new RunResult(other.getCompletedCount(), other.getErrors(), other.getFailures(), other.getSkipped(), failure, timeout);
   }

   public RunResult(int completedCount, int errors, int failures, int skipped) {
      this(completedCount, errors, failures, skipped, (String)null, false);
   }

   public RunResult(int completedCount, int errors, int failures, int skipped, String failure, boolean timeout) {
      this.completedCount = completedCount;
      this.errors = errors;
      this.failures = failures;
      this.skipped = skipped;
      this.failure = failure;
      this.timeout = timeout;
   }

   private static String getStackTrace(Exception e) {
      if (e == null) {
         return null;
      } else {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         PrintWriter pw = new PrintWriter(out);
         e.printStackTrace(pw);
         return new String(out.toByteArray());
      }
   }

   public int getCompletedCount() {
      return this.completedCount;
   }

   public int getErrors() {
      return this.errors;
   }

   public int getFailures() {
      return this.failures;
   }

   public int getSkipped() {
      return this.skipped;
   }

   public Integer getFailsafeCode() {
      if (this.completedCount == 0) {
         return 254;
      } else {
         return !this.isErrorFree() ? 255 : null;
      }
   }

   public boolean isErrorFree() {
      return this.getFailures() == 0 && this.getErrors() == 0;
   }

   public boolean isFailureOrTimeout() {
      return this.timeout || this.isFailure();
   }

   public boolean isFailure() {
      return this.failure != null;
   }

   public String getFailure() {
      return this.failure;
   }

   public boolean isTimeout() {
      return this.timeout;
   }

   public RunResult aggregate(RunResult other) {
      String failureMessage = this.getFailure() != null ? this.getFailure() : other.getFailure();
      boolean timeout = this.isTimeout() || other.isTimeout();
      int completed = this.getCompletedCount() + other.getCompletedCount();
      int fail = this.getFailures() + other.getFailures();
      int ign = this.getSkipped() + other.getSkipped();
      int err = this.getErrors() + other.getErrors();
      return new RunResult(completed, err, fail, ign, failureMessage, timeout);
   }

   public static RunResult noTestsRun() {
      return new RunResult(0, 0, 0, 0);
   }

   private Xpp3Dom create(String node, String value) {
      Xpp3Dom dom = new Xpp3Dom(node);
      dom.setValue(value);
      return dom;
   }

   private Xpp3Dom create(String node, int value) {
      return this.create(node, Integer.toString(value));
   }

   Xpp3Dom asXpp3Dom() {
      Xpp3Dom dom = new Xpp3Dom("failsafe-summary");
      Integer failsafeCode = this.getFailsafeCode();
      if (failsafeCode != null) {
         dom.setAttribute("result", Integer.toString(failsafeCode));
      }

      dom.setAttribute("timeout", Boolean.toString(this.timeout));
      dom.addChild(this.create("completed", this.completedCount));
      dom.addChild(this.create("errors", this.errors));
      dom.addChild(this.create("failures", this.failures));
      dom.addChild(this.create("skipped", this.skipped));
      dom.addChild(this.create("failureMessage", this.failure));
      return dom;
   }

   public static RunResult fromInputStream(InputStream inputStream, String encoding) throws FileNotFoundException {
      Xpp3Dom dom = Xpp3DomBuilder.build(inputStream, encoding);
      boolean timeout = Boolean.parseBoolean(dom.getAttribute("timeout"));
      int completed = Integer.parseInt(dom.getChild("completed").getValue());
      int errors = Integer.parseInt(dom.getChild("errors").getValue());
      int failures = Integer.parseInt(dom.getChild("failures").getValue());
      int skipped = Integer.parseInt(dom.getChild("skipped").getValue());
      String failureMessage1 = dom.getChild("failureMessage").getValue();
      String failureMessage = StringUtils.isEmpty(failureMessage1) ? null : failureMessage1;
      return new RunResult(completed, errors, failures, skipped, failureMessage, timeout);
   }

   public void writeSummary(File summaryFile, boolean inProgress, String encoding) throws IOException {
      if (!summaryFile.getParentFile().isDirectory()) {
         summaryFile.getParentFile().mkdirs();
      }

      FileInputStream fin = null;
      FileWriter writer = null;

      try {
         RunResult mergedSummary = this;
         if (summaryFile.exists() && inProgress) {
            fin = new FileInputStream(summaryFile);
            RunResult runResult = fromInputStream(new BufferedInputStream(fin), encoding);
            mergedSummary = this.aggregate(runResult);
         }

         writer = new FileWriter(summaryFile);
         writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
         PrettyPrintXMLWriter prettyPrintXMLWriter = new PrettyPrintXMLWriter(writer);
         Xpp3DomWriter.write((XMLWriter)prettyPrintXMLWriter, mergedSummary.asXpp3Dom());
      } finally {
         IOUtil.close((InputStream)fin);
         IOUtil.close((Writer)writer);
      }

   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         RunResult runResult = (RunResult)o;
         if (this.completedCount != runResult.completedCount) {
            return false;
         } else if (this.errors != runResult.errors) {
            return false;
         } else if (this.failures != runResult.failures) {
            return false;
         } else if (this.skipped != runResult.skipped) {
            return false;
         } else if (this.timeout != runResult.timeout) {
            return false;
         } else {
            if (this.failure != null) {
               if (!this.failure.equals(runResult.failure)) {
                  return false;
               }
            } else if (runResult.failure != null) {
               return false;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.completedCount;
      result = 31 * result + this.errors;
      result = 31 * result + this.failures;
      result = 31 * result + this.skipped;
      result = 31 * result + (this.failure != null ? this.failure.hashCode() : 0);
      result = 31 * result + (this.timeout ? 1 : 0);
      return result;
   }
}
