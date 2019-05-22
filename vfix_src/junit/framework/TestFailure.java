package junit.framework;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestFailure {
   protected Test fFailedTest;
   protected Throwable fThrownException;

   public TestFailure(Test failedTest, Throwable thrownException) {
      this.fFailedTest = failedTest;
      this.fThrownException = thrownException;
   }

   public Test failedTest() {
      return this.fFailedTest;
   }

   public Throwable thrownException() {
      return this.fThrownException;
   }

   public String toString() {
      return this.fFailedTest + ": " + this.fThrownException.getMessage();
   }

   public String trace() {
      StringWriter stringWriter = new StringWriter();
      PrintWriter writer = new PrintWriter(stringWriter);
      this.thrownException().printStackTrace(writer);
      return stringWriter.toString();
   }

   public String exceptionMessage() {
      return this.thrownException().getMessage();
   }

   public boolean isFailure() {
      return this.thrownException() instanceof AssertionFailedError;
   }
}
