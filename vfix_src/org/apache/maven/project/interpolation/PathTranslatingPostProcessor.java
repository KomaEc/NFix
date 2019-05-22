package org.apache.maven.project.interpolation;

import java.io.File;
import java.util.List;
import org.apache.maven.project.path.PathTranslator;
import org.codehaus.plexus.interpolation.InterpolationPostProcessor;
import org.codehaus.plexus.interpolation.util.ValueSourceUtils;

public class PathTranslatingPostProcessor implements InterpolationPostProcessor {
   private final List<String> unprefixedPathKeys;
   private final File projectDir;
   private final PathTranslator pathTranslator;
   private final List<String> expressionPrefixes;

   public PathTranslatingPostProcessor(List<String> expressionPrefixes, List<String> unprefixedPathKeys, File projectDir, PathTranslator pathTranslator) {
      this.expressionPrefixes = expressionPrefixes;
      this.unprefixedPathKeys = unprefixedPathKeys;
      this.projectDir = projectDir;
      this.pathTranslator = pathTranslator;
   }

   public Object execute(String expression, Object value) {
      expression = ValueSourceUtils.trimPrefix(expression, this.expressionPrefixes, true);
      return this.projectDir != null && value != null && this.unprefixedPathKeys.contains(expression) ? this.pathTranslator.alignToBaseDirectory(String.valueOf(value), this.projectDir) : value;
   }
}
