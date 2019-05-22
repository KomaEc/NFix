package org.apache.maven.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.path.PathTranslator;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.introspection.ReflectionValueExtractor;

public class PluginParameterExpressionEvaluator implements ExpressionEvaluator {
   private static final Map BANNED_EXPRESSIONS;
   private static final Map DEPRECATED_EXPRESSIONS;
   private final PathTranslator pathTranslator;
   private final MavenSession context;
   private final Logger logger;
   private final MojoExecution mojoExecution;
   private final MavenProject project;
   private final String basedir;
   private final Properties properties;

   public PluginParameterExpressionEvaluator(MavenSession context, MojoExecution mojoExecution, PathTranslator pathTranslator, Logger logger, MavenProject project, Properties properties) {
      this.context = context;
      this.mojoExecution = mojoExecution;
      this.pathTranslator = pathTranslator;
      this.logger = logger;
      this.project = project;
      this.properties = properties;
      String basedir = null;
      if (project != null) {
         File projectFile = project.getFile();
         if (projectFile != null) {
            basedir = projectFile.getParentFile().getAbsolutePath();
         }
      }

      if (basedir == null) {
         basedir = System.getProperty("user.dir");
      }

      this.basedir = basedir;
   }

   public Object evaluate(String expr) throws ExpressionEvaluationException {
      Object value = null;
      if (expr == null) {
         return null;
      } else {
         String expression = this.stripTokens(expr);
         int pathSeparator;
         String pathExpression;
         if (!expression.equals(expr)) {
            MojoDescriptor mojoDescriptor = this.mojoExecution.getMojoDescriptor();
            if (BANNED_EXPRESSIONS.containsKey(expression)) {
               throw new ExpressionEvaluationException("The parameter expression: '" + expression + "' used in mojo: '" + mojoDescriptor.getGoal() + "' is banned. Use '" + BANNED_EXPRESSIONS.get(expression) + "' instead.");
            } else {
               if (DEPRECATED_EXPRESSIONS.containsKey(expression)) {
                  this.logger.warn("The parameter expression: '" + expression + "' used in mojo: '" + mojoDescriptor.getGoal() + "' has been deprecated. Use '" + DEPRECATED_EXPRESSIONS.get(expression) + "' instead.");
               }

               if ("localRepository".equals(expression)) {
                  value = this.context.getLocalRepository();
               } else if ("session".equals(expression)) {
                  value = this.context;
               } else if ("reactorProjects".equals(expression)) {
                  value = this.context.getSortedProjects();
               } else if ("reports".equals(expression)) {
                  value = this.mojoExecution.getReports();
               } else if ("mojoExecution".equals(expression)) {
                  value = this.mojoExecution;
               } else if ("project".equals(expression)) {
                  value = this.project;
               } else if ("executedProject".equals(expression)) {
                  value = this.project.getExecutionProject();
               } else if (expression.startsWith("project")) {
                  try {
                     pathSeparator = expression.indexOf("/");
                     if (pathSeparator > 0) {
                        pathExpression = expression.substring(0, pathSeparator);
                        value = ReflectionValueExtractor.evaluate(pathExpression, this.project);
                        value = value + expression.substring(pathSeparator);
                     } else {
                        value = ReflectionValueExtractor.evaluate(expression.substring(1), this.project);
                     }
                  } catch (Exception var10) {
                     throw new ExpressionEvaluationException("Error evaluating plugin parameter expression: " + expression, var10);
                  }
               } else if (expression.startsWith("plugin")) {
                  try {
                     pathSeparator = expression.indexOf("/");
                     PluginDescriptor pluginDescriptor = mojoDescriptor.getPluginDescriptor();
                     if (pathSeparator > 0) {
                        String pathExpression = expression.substring(1, pathSeparator);
                        value = ReflectionValueExtractor.evaluate(pathExpression, pluginDescriptor);
                        value = value + expression.substring(pathSeparator);
                     } else {
                        value = ReflectionValueExtractor.evaluate(expression.substring(1), pluginDescriptor);
                     }
                  } catch (Exception var9) {
                     throw new ExpressionEvaluationException("Error evaluating plugin parameter expression: " + expression, var9);
                  }
               } else if ("settings".equals(expression)) {
                  value = this.context.getSettings();
               } else if (expression.startsWith("settings")) {
                  try {
                     pathSeparator = expression.indexOf("/");
                     if (pathSeparator > 0) {
                        pathExpression = expression.substring(1, pathSeparator);
                        value = ReflectionValueExtractor.evaluate(pathExpression, this.context.getSettings());
                        value = value + expression.substring(pathSeparator);
                     } else {
                        value = ReflectionValueExtractor.evaluate(expression.substring(1), this.context.getSettings());
                     }
                  } catch (Exception var8) {
                     throw new ExpressionEvaluationException("Error evaluating plugin parameter expression: " + expression, var8);
                  }
               } else if ("basedir".equals(expression)) {
                  value = this.basedir;
               } else if (expression.startsWith("basedir")) {
                  pathSeparator = expression.indexOf("/");
                  if (pathSeparator > 0) {
                     value = this.basedir + expression.substring(pathSeparator);
                  } else {
                     this.logger.error("Got expression '" + expression + "' that was not recognised");
                  }
               }

               if (value == null) {
                  if (this.project != null && this.project.getProperties() != null) {
                     value = this.project.getProperties().getProperty(expression);
                  }

                  if (value == null && this.properties != null) {
                     value = this.properties.getProperty(expression);
                  }
               }

               if (value instanceof String) {
                  String val = (String)value;
                  int exprStartDelimiter = val.indexOf("${");
                  if (exprStartDelimiter >= 0) {
                     if (exprStartDelimiter > 0) {
                        value = val.substring(0, exprStartDelimiter) + this.evaluate(val.substring(exprStartDelimiter));
                     } else {
                        value = this.evaluate(val.substring(exprStartDelimiter));
                     }
                  }
               }

               return value;
            }
         } else {
            int index = expr.indexOf("${");
            if (index >= 0) {
               pathSeparator = expr.indexOf("}", index);
               if (pathSeparator >= 0) {
                  pathExpression = expr.substring(0, index);
                  if (index > 0 && expr.charAt(index - 1) == '$') {
                     pathExpression = pathExpression + expr.substring(index + 1, pathSeparator + 1);
                  } else {
                     pathExpression = pathExpression + this.evaluate(expr.substring(index, pathSeparator + 1));
                  }

                  pathExpression = pathExpression + this.evaluate(expr.substring(pathSeparator + 1));
                  return pathExpression;
               }
            }

            return expression.indexOf("$$") > -1 ? expression.replaceAll("\\$\\$", "\\$") : expression;
         }
      }
   }

   private String stripTokens(String expr) {
      if (expr.startsWith("${") && expr.indexOf("}") == expr.length() - 1) {
         expr = expr.substring(2, expr.length() - 1);
      }

      return expr;
   }

   public File alignToBaseDirectory(File file) {
      File basedir;
      if (this.project != null && this.project.getFile() != null) {
         basedir = this.project.getFile().getParentFile();
      } else {
         basedir = (new File(".")).getAbsoluteFile().getParentFile();
      }

      return new File(this.pathTranslator.alignToBaseDirectory(file.getPath(), basedir));
   }

   static {
      Map deprecated = new HashMap();
      deprecated.put("project.build.resources", "project.resources");
      deprecated.put("project.build.testResources", "project.testResources");
      DEPRECATED_EXPRESSIONS = deprecated;
      Map banned = new HashMap();
      BANNED_EXPRESSIONS = banned;
   }
}
