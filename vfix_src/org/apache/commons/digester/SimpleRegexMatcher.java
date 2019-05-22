package org.apache.commons.digester;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleRegexMatcher extends RegexMatcher {
   private static final Log baseLog;
   private Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$digester$SimpleRegexMatcher;

   public SimpleRegexMatcher() {
      this.log = baseLog;
   }

   public Log getLog() {
      return this.log;
   }

   public void setLog(Log log) {
      this.log = log;
   }

   public boolean match(String basePattern, String regexPattern) {
      return basePattern != null && regexPattern != null ? this.match(basePattern, regexPattern, 0, 0) : false;
   }

   private boolean match(String basePattern, String regexPattern, int baseAt, int regexAt) {
      if (this.log.isTraceEnabled()) {
         this.log.trace("Base: " + basePattern);
         this.log.trace("Regex: " + regexPattern);
         this.log.trace("Base@" + baseAt);
         this.log.trace("Regex@" + regexAt);
      }

      if (regexAt >= regexPattern.length()) {
         return baseAt >= basePattern.length();
      } else if (baseAt >= basePattern.length()) {
         return false;
      } else {
         char regexCurrent = regexPattern.charAt(regexAt);
         switch(regexCurrent) {
         case '*':
            ++regexAt;
            if (regexAt >= regexPattern.length()) {
               return true;
            } else {
               char nextRegex = regexPattern.charAt(regexAt);
               if (this.log.isTraceEnabled()) {
                  this.log.trace("Searching for next '" + nextRegex + "' char");
               }

               for(int nextMatch = basePattern.indexOf(nextRegex, baseAt); nextMatch != -1; nextMatch = basePattern.indexOf(nextRegex, nextMatch + 1)) {
                  if (this.log.isTraceEnabled()) {
                     this.log.trace("Trying '*' match@" + nextMatch);
                  }

                  if (this.match(basePattern, regexPattern, nextMatch, regexAt)) {
                     return true;
                  }
               }

               this.log.trace("No matches found.");
               return false;
            }
         case '?':
            ++baseAt;
            ++regexAt;
            return this.match(basePattern, regexPattern, baseAt, regexAt);
         default:
            if (this.log.isTraceEnabled()) {
               this.log.trace("Camparing " + regexCurrent + " to " + basePattern.charAt(baseAt));
            }

            if (regexCurrent == basePattern.charAt(baseAt)) {
               ++baseAt;
               ++regexAt;
               return this.match(basePattern, regexPattern, baseAt, regexAt);
            } else {
               return false;
            }
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      baseLog = LogFactory.getLog(class$org$apache$commons$digester$SimpleRegexMatcher == null ? (class$org$apache$commons$digester$SimpleRegexMatcher = class$("org.apache.commons.digester.SimpleRegexMatcher")) : class$org$apache$commons$digester$SimpleRegexMatcher);
   }
}
