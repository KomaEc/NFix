package org.junit.internal.runners.statements;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.Statement;

public class ExpectException extends Statement {
   private final Statement next;
   private final Class<? extends Throwable> expected;

   public ExpectException(Statement next, Class<? extends Throwable> expected) {
      this.next = next;
      this.expected = expected;
   }

   public void evaluate() throws Exception {
      boolean complete = false;

      try {
         this.next.evaluate();
         complete = true;
      } catch (AssumptionViolatedException var4) {
         throw var4;
      } catch (Throwable var5) {
         if (!this.expected.isAssignableFrom(var5.getClass())) {
            String message = "Unexpected exception, expected<" + this.expected.getName() + "> but was<" + var5.getClass().getName() + ">";
            throw new Exception(message, var5);
         }
      }

      if (complete) {
         throw new AssertionError("Expected exception: " + this.expected.getName());
      }
   }
}
