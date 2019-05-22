package hidden.org.codehaus.plexus.interpolation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringSearchInterpolator implements Interpolator {
   private Map existingAnswers = new HashMap();
   private List valueSources = new ArrayList();
   private List postProcessors = new ArrayList();
   private boolean reusePatterns = false;
   private boolean cacheAnswers = false;

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

   public String interpolate(String input, String thisPrefixPattern) throws InterpolationException {
      throw new UnsupportedOperationException("Regular expressions are not supported in this Interpolator implementation.");
   }

   public String interpolate(String input, String thisPrefixPattern, RecursionInterceptor recursionInterceptor) throws InterpolationException {
      throw new UnsupportedOperationException("Regular expressions are not supported in this Interpolator implementation.");
   }

   public String interpolate(String input) throws InterpolationException {
      return this.interpolate(input, (RecursionInterceptor)(new SimpleRecursionInterceptor()));
   }

   public String interpolate(String input, RecursionInterceptor recursionInterceptor) throws InterpolationException {
      return this.interpolate(input, (RecursionInterceptor)recursionInterceptor, (Set)(new HashSet()));
   }

   private String interpolate(String input, RecursionInterceptor recursionInterceptor, Set unresolvable) throws InterpolationException {
      StringBuffer result = new StringBuffer(input.length() * 2);
      int startIdx = true;
      int endIdx = -1;

      int startIdx;
      while((startIdx = input.indexOf("${", endIdx + 1)) > -1) {
         result.append(input.substring(endIdx + 1, startIdx));
         endIdx = input.indexOf("}", startIdx + 1);
         if (endIdx < 0) {
            break;
         }

         String wholeExpr = input.substring(startIdx, endIdx + 1);
         String realExpr = wholeExpr.substring(2, wholeExpr.length() - 1);
         boolean resolved = false;
         if (!unresolvable.contains(wholeExpr)) {
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

            if (value == null) {
               unresolvable.add(wholeExpr);
            } else {
               value = this.interpolate(String.valueOf(value), recursionInterceptor, unresolvable);
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

               result.append(String.valueOf(value));
               resolved = true;
            }

            recursionInterceptor.expressionResolutionFinished(realExpr);
         }

         if (!resolved) {
            result.append(wholeExpr);
         }
      }

      if (endIdx == -1 && startIdx > -1) {
         result.append(input.substring(startIdx, input.length()));
      } else if (endIdx < input.length()) {
         result.append(input.substring(endIdx + 1, input.length()));
      }

      return result.toString();
   }

   public List getFeedback() {
      List messages = new ArrayList();
      Iterator it = this.valueSources.iterator();

      while(it.hasNext()) {
         ValueSource vs = (ValueSource)it.next();
         if (vs instanceof FeedbackEnabledValueSource) {
            List feedback = ((FeedbackEnabledValueSource)vs).getFeedback();
            if (feedback != null && !feedback.isEmpty()) {
               messages.addAll(feedback);
            }
         }
      }

      return messages;
   }

   public void clearFeedback() {
      Iterator it = this.valueSources.iterator();

      while(it.hasNext()) {
         ValueSource vs = (ValueSource)it.next();
         if (vs instanceof FeedbackEnabledValueSource) {
            ((FeedbackEnabledValueSource)vs).clearFeedback();
         }
      }

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
