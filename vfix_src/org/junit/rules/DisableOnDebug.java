package org.junit.rules;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DisableOnDebug implements TestRule {
   private final TestRule rule;
   private final boolean debugging;

   public DisableOnDebug(TestRule rule) {
      this(rule, ManagementFactory.getRuntimeMXBean().getInputArguments());
   }

   DisableOnDebug(TestRule rule, List<String> inputArguments) {
      this.rule = rule;
      this.debugging = isDebugging(inputArguments);
   }

   public Statement apply(Statement base, Description description) {
      return this.debugging ? base : this.rule.apply(base, description);
   }

   private static boolean isDebugging(List<String> arguments) {
      Iterator i$ = arguments.iterator();

      String argument;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         argument = (String)i$.next();
         if ("-Xdebug".equals(argument)) {
            return true;
         }
      } while(!argument.startsWith("-agentlib:jdwp"));

      return true;
   }

   public boolean isDebugging() {
      return this.debugging;
   }
}
