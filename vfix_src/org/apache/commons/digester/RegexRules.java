package org.apache.commons.digester;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RegexRules extends AbstractRulesImpl {
   private ArrayList registeredRules = new ArrayList();
   private RegexMatcher matcher;

   public RegexRules(RegexMatcher matcher) {
      this.setRegexMatcher(matcher);
   }

   public RegexMatcher getRegexMatcher() {
      return this.matcher;
   }

   public void setRegexMatcher(RegexMatcher matcher) {
      if (matcher == null) {
         throw new IllegalArgumentException("RegexMatcher must not be null.");
      } else {
         this.matcher = matcher;
      }
   }

   protected void registerRule(String pattern, Rule rule) {
      this.registeredRules.add(new RegexRules.RegisteredRule(pattern, rule));
   }

   public void clear() {
      this.registeredRules.clear();
   }

   public List match(String namespaceURI, String pattern) {
      ArrayList rules = new ArrayList(this.registeredRules.size());
      Iterator it = this.registeredRules.iterator();

      while(it.hasNext()) {
         RegexRules.RegisteredRule next = (RegexRules.RegisteredRule)it.next();
         if (this.matcher.match(pattern, next.pattern)) {
            rules.add(next.rule);
         }
      }

      return rules;
   }

   public List rules() {
      ArrayList rules = new ArrayList(this.registeredRules.size());
      Iterator it = this.registeredRules.iterator();

      while(it.hasNext()) {
         rules.add(((RegexRules.RegisteredRule)it.next()).rule);
      }

      return rules;
   }

   private class RegisteredRule {
      String pattern;
      Rule rule;

      RegisteredRule(String pattern, Rule rule) {
         this.pattern = pattern;
         this.rule = rule;
      }
   }
}
