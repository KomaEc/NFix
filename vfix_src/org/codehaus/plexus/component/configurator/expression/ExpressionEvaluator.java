package org.codehaus.plexus.component.configurator.expression;

import java.io.File;

public interface ExpressionEvaluator {
   Object evaluate(String var1) throws ExpressionEvaluationException;

   File alignToBaseDirectory(File var1);
}
