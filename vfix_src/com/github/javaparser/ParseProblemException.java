package com.github.javaparser;

import com.github.javaparser.utils.Utils;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ParseProblemException extends RuntimeException {
   private final List<Problem> problems;

   public ParseProblemException(List<Problem> problems) {
      super(createMessage((List)Utils.assertNotNull(problems)));
      this.problems = problems;
   }

   public ParseProblemException(Throwable throwable) {
      this(Collections.singletonList(new Problem(throwable.getMessage(), (TokenRange)null, throwable)));
   }

   private static String createMessage(List<Problem> problems) {
      StringBuilder message = new StringBuilder();
      Iterator var2 = problems.iterator();

      while(var2.hasNext()) {
         Problem problem = (Problem)var2.next();
         message.append(problem.toString()).append(Utils.EOL);
      }

      return message.toString();
   }

   public List<Problem> getProblems() {
      return this.problems;
   }
}
