package org.codehaus.plexus.interpolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.interpolation.util.StringUtils;

public class RegexBasedInterpolator implements Interpolator {
   private String startRegex;
   private String endRegex;
   private Map existingAnswers;
   private List valueSources;
   private List postProcessors;
   private boolean reusePatterns;
   private boolean cacheAnswers;
   public static final String DEFAULT_REGEXP = "\\$\\{(.+?)\\}";
   private Map compiledPatterns;

   public RegexBasedInterpolator() {
      this.existingAnswers = new HashMap();
      this.valueSources = new ArrayList();
      this.postProcessors = new ArrayList();
      this.reusePatterns = false;
      this.cacheAnswers = false;
      this.compiledPatterns = new WeakHashMap();
      this.compiledPatterns.put("\\$\\{(.+?)\\}", Pattern.compile("\\$\\{(.+?)\\}"));
   }

   public RegexBasedInterpolator(boolean reusePatterns) {
      this();
      this.reusePatterns = reusePatterns;
   }

   public RegexBasedInterpolator(String startRegex, String endRegex) {
      this();
      this.startRegex = startRegex;
      this.endRegex = endRegex;
   }

   public RegexBasedInterpolator(List valueSources) {
      this();
      this.valueSources.addAll(valueSources);
   }

   public RegexBasedInterpolator(String startRegex, String endRegex, List valueSources) {
      this();
      this.startRegex = startRegex;
      this.endRegex = endRegex;
      this.valueSources.addAll(valueSources);
   }

   public void addValueSource(ValueSource valueSource) {
      this.valueSources.add(valueSource);
   }

   public void removeValuesSource(ValueSource valueSource) {
      this.valueSources.remove(valueSource);
   }

   public void addPostProcessor(InterpolationPostProcessor postProcessor) {
      this.postProcessors.add(postProcessor);
   }

   public void removePostProcessor(InterpolationPostProcessor postProcessor) {
      this.postProcessors.remove(postProcessor);
   }

   public String interpolate(String input, String thisPrefixPattern, RecursionInterceptor recursionInterceptor) throws InterpolationException {
      if (input == null) {
         return "";
      } else {
         if (recursionInterceptor == null) {
            recursionInterceptor = new SimpleRecursionInterceptor();
         }

         if (thisPrefixPattern != null && thisPrefixPattern.length() == 0) {
            thisPrefixPattern = null;
         }

         int realExprGroup = 2;
         Pattern expressionPattern = null;
         if (this.startRegex == null && this.endRegex == null) {
            if (thisPrefixPattern != null) {
               expressionPattern = this.getPattern("\\$\\{(" + thisPrefixPattern + ")?(.+?)\\}");
            } else {
               expressionPattern = this.getPattern("\\$\\{(.+?)\\}");
               realExprGroup = 1;
            }
         } else if (thisPrefixPattern == null) {
            expressionPattern = this.getPattern(this.startRegex + this.endRegex);
            realExprGroup = 1;
         } else {
            expressionPattern = this.getPattern(this.startRegex + thisPrefixPattern + this.endRegex);
         }

         String var6;
         try {
            var6 = this.interpolate(input, (RecursionInterceptor)recursionInterceptor, expressionPattern, realExprGroup);
         } finally {
            if (!this.cacheAnswers) {
               this.clearAnswers();
            }

         }

         return var6;
      }
   }

   private Pattern getPattern(String regExp) {
      if (!this.reusePatterns) {
         return Pattern.compile(regExp);
      } else {
         Pattern pattern = null;
         synchronized(this) {
            pattern = (Pattern)this.compiledPatterns.get(regExp);
            if (pattern != null) {
               return pattern;
            } else {
               pattern = Pattern.compile(regExp);
               this.compiledPatterns.put(regExp, pattern);
               return pattern;
            }
         }
      }
   }

   private String interpolate(String input, RecursionInterceptor recursionInterceptor, Pattern expressionPattern, int realExprGroup) throws InterpolationException {
      if (input == null) {
         return "";
      } else {
         String result = input;

         String realExpr;
         for(Matcher matcher = expressionPattern.matcher(input); matcher.find(); recursionInterceptor.expressionResolutionFinished(realExpr)) {
            String wholeExpr = matcher.group(0);
            realExpr = matcher.group(realExprGroup);
            if (realExpr.startsWith(".")) {
               realExpr = realExpr.substring(1);
            }

            if (recursionInterceptor.hasRecursiveExpression(realExpr)) {
               throw new InterpolationException("Detected the following recursive expression cycle: " + recursionInterceptor.getExpressionCycle(realExpr), wholeExpr);
            }

            recursionInterceptor.expressionResolutionStarted(realExpr);
            Object value = this.existingAnswers.get(realExpr);

            Iterator it;
            ValueSource vs;
            for(it = this.valueSources.iterator(); it.hasNext() && value == null; value = vs.getValue(realExpr)) {
               vs = (ValueSource)it.next();
            }

            if (value != null) {
               value = this.interpolate(String.valueOf(value), recursionInterceptor, expressionPattern, realExprGroup);
               if (this.postProcessors != null && !this.postProcessors.isEmpty()) {
                  it = this.postProcessors.iterator();

                  while(it.hasNext()) {
                     InterpolationPostProcessor postProcessor = (InterpolationPostProcessor)it.next();
                     Object newVal = postProcessor.execute(realExpr, value);
                     if (newVal != null) {
                        value = newVal;
                        break;
                     }
                  }
               }

               result = StringUtils.replace(result, wholeExpr, String.valueOf(value));
               matcher.reset(result);
            }
         }

         return result;
      }
   }

   public List getFeedback() {
      List messages = new ArrayList();
      Iterator it = this.valueSources.iterator();

      while(it.hasNext()) {
         ValueSource vs = (ValueSource)it.next();
         List feedback = vs.getFeedback();
         if (feedback != null && !feedback.isEmpty()) {
            messages.addAll(feedback);
         }
      }

      return messages;
   }

   public void clearFeedback() {
      Iterator it = this.valueSources.iterator();

      while(it.hasNext()) {
         ValueSource vs = (ValueSource)it.next();
         vs.clearFeedback();
      }

   }

   public String interpolate(String input, String thisPrefixPattern) throws InterpolationException {
      return this.interpolate(input, thisPrefixPattern, (RecursionInterceptor)null);
   }

   public String interpolate(String input) throws InterpolationException {
      return this.interpolate(input, (String)null, (RecursionInterceptor)null);
   }

   public String interpolate(String input, RecursionInterceptor recursionInterceptor) throws InterpolationException {
      return this.interpolate(input, (String)null, recursionInterceptor);
   }

   public boolean isReusePatterns() {
      return this.reusePatterns;
   }

   public void setReusePatterns(boolean reusePatterns) {
      this.reusePatterns = reusePatterns;
   }

   public boolean isCacheAnswers() {
      return this.cacheAnswers;
   }

   public void setCacheAnswers(boolean cacheAnswers) {
      this.cacheAnswers = cacheAnswers;
   }

   public void clearAnswers() {
      this.existingAnswers.clear();
   }
}
