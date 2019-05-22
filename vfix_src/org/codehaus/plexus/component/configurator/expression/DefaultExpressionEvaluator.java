package org.codehaus.plexus.component.configurator.expression;

import java.io.File;

public class DefaultExpressionEvaluator implements ExpressionEvaluator {
   public Object evaluate(String expression) {
      return expression;
   }

   public File alignToBaseDirectory(File file) {
      return file;
   }
}
