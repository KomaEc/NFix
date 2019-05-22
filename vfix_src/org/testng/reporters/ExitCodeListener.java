package org.testng.reporters;

import org.testng.TestNG;

public class ExitCodeListener extends TestNG.ExitCodeListener {
   public ExitCodeListener() {
   }

   public ExitCodeListener(TestNG runner) {
      super(runner);
   }
}
