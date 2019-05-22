package org.codehaus.plexus.interpolation;

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
   private boolean cacheAnswers = false;
   public static final String DEFAULT_START_EXPR = "${";
   public static final String DEFAULT_END_EXPR = "}";
   private String startExpr;
   private String endExpr;
   private String escapeString;

   public StringSearchInterpolator() {
      this.startExpr = "${";
      this.endExpr = "}";
   }

   public StringSearchInterpolator(String startExpr, String endExpr) {
      this.startExpr = startExpr;
      this.endExpr = endExpr;
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
      String var3;
      try {
         var3 = this.interpolate(input, (RecursionInterceptor)recursionInterceptor, (Set)(new HashSet()));
      } finally {
         if (!this.cacheAnswers) {
            this.existingAnswers.clear();
         }

      }

      return var3;
   }

   private String interpolate(String input, RecursionInterceptor recursionInterceptor, Set unresolvable) throws InterpolationException {
      if (input == null) {
         return "";
      } else {
         StringBuffer result = new StringBuffer(input.length() * 2);
         int startIdx = true;
         int endIdx = -1;

         int startIdx;
         while((startIdx = input.indexOf(this.startExpr, endIdx + 1)) > -1) {
            result.append(input.substring(endIdx + 1, startIdx));
            endIdx = input.indexOf(this.endExpr, startIdx + 1);
            if (endIdx < 0) {
               break;
            }

            String wholeExpr = input.substring(startIdx, endIdx + this.endExpr.length());
            String realExpr = wholeExpr.substring(this.startExpr.length(), wholeExpr.length() - this.endExpr.length());
            if (startIdx >= 0 && this.escapeString != null && this.escapeString.length() > 0) {
               int startEscapeIdx = startIdx == 0 ? 0 : startIdx - this.escapeString.length();
               if (startEscapeIdx >= 0) {
                  String escape = input.substring(startEscapeIdx, startIdx);
                  if (escape != null && this.escapeString.equals(escape)) {
                     result.append(wholeExpr);
                     result.replace(startEscapeIdx, startEscapeIdx + this.escapeString.length(), "");
                     continue;
                  }
               }
            }

            boolean resolved = false;
            if (!unresolvable.contains(wholeExpr)) {
               if (realExpr.startsWith(".")) {
                  realExpr = realExpr.substring(1);
               }

               if (recursionInterceptor.hasRecursiveExpression(realExpr)) {
                  throw new InterpolationCycleException(recursionInterceptor, realExpr, wholeExpr);
               }

               recursionInterceptor.expressionResolutionStarted(realExpr);
               Object value = this.existingAnswers.get(realExpr);
               Object bestAnswer = null;
               Iterator it = this.valueSources.iterator();

               while(it.hasNext() && value == null) {
                  ValueSource vs = (ValueSource)it.next();
                  value = vs.getValue(realExpr);
                  if (value != null && value.toString().indexOf(wholeExpr) > -1) {
                     bestAnswer = value;
                     value = null;
                  }
               }

               if (value == null && bestAnswer != null) {
                  throw new InterpolationCycleException(recursionInterceptor, realExpr, wholeExpr);
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

            if (endIdx > -1) {
               endIdx += this.endExpr.length() - 1;
            }
         }

         if (endIdx == -1 && startIdx > -1) {
            result.append(input.substring(startIdx, input.length()));
         } else if (endIdx < input.length()) {
            result.append(input.substring(endIdx + 1, input.length()));
         }

         return result.toString();
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

   public boolean isCacheAnswers() {
      return this.cacheAnswers;
   }

   public void setCacheAnswers(boolean cacheAnswers) {
      this.cacheAnswers = cacheAnswers;
   }

   public void clearAnswers() {
      this.existingAnswers.clear();
   }

   public String getEscapeString() {
      return this.escapeString;
   }

   public void setEscapeString(String escapeString) {
      this.escapeString = escapeString;
   }
}
