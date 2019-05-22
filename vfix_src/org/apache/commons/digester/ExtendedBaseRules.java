package org.apache.commons.digester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExtendedBaseRules extends RulesBase {
   private int counter = 0;
   private Map order = new HashMap();

   public void add(String pattern, Rule rule) {
      super.add(pattern, rule);
      ++this.counter;
      this.order.put(rule, new Integer(this.counter));
   }

   public List match(String namespace, String pattern) {
      String parentPattern = "";
      int lastIndex = pattern.lastIndexOf(47);
      boolean hasParent = true;
      if (lastIndex == -1) {
         hasParent = false;
      } else {
         parentPattern = pattern.substring(0, lastIndex);
      }

      List universalList = new ArrayList(this.counter);
      List tempList = (List)this.cache.get("!*");
      if (tempList != null) {
         universalList.addAll(tempList);
      }

      tempList = (List)this.cache.get("!" + parentPattern + "/?");
      if (tempList != null) {
         universalList.addAll(tempList);
      }

      boolean ignoreBasicMatches = false;
      List rulesList = (List)this.cache.get(pattern);
      if (rulesList != null) {
         ignoreBasicMatches = true;
      } else if (hasParent) {
         rulesList = (List)this.cache.get(parentPattern + "/?");
         if (rulesList != null) {
            ignoreBasicMatches = true;
         } else {
            rulesList = this.findExactAncesterMatch(pattern);
            if (rulesList != null) {
               ignoreBasicMatches = true;
            }
         }
      }

      String longKey = "";
      int longKeyLength = 0;
      Iterator keys = this.cache.keySet().iterator();

      while(keys.hasNext()) {
         String key = (String)keys.next();
         boolean isUniversal = key.startsWith("!");
         if (isUniversal) {
            key = key.substring(1, key.length());
         }

         boolean wildcardMatchStart = key.startsWith("*/");
         boolean wildcardMatchEnd = key.endsWith("/*");
         if (wildcardMatchStart || isUniversal && wildcardMatchEnd) {
            boolean parentMatched = false;
            boolean basicMatched = false;
            boolean ancesterMatched = false;
            boolean parentMatchEnd = key.endsWith("/?");
            if (parentMatchEnd) {
               parentMatched = this.parentMatch(key, pattern, parentPattern);
            } else if (wildcardMatchEnd) {
               String bodyPattern;
               if (wildcardMatchStart) {
                  bodyPattern = key.substring(2, key.length() - 2);
                  if (pattern.endsWith(bodyPattern)) {
                     ancesterMatched = true;
                  } else {
                     ancesterMatched = pattern.indexOf(bodyPattern + "/") > -1;
                  }
               } else {
                  bodyPattern = key.substring(0, key.length() - 2);
                  if (pattern.startsWith(bodyPattern)) {
                     if (pattern.length() == bodyPattern.length()) {
                        ancesterMatched = true;
                     } else {
                        ancesterMatched = pattern.charAt(bodyPattern.length()) == '/';
                     }
                  } else {
                     ancesterMatched = false;
                  }
               }
            } else {
               basicMatched = this.basicMatch(key, pattern);
            }

            if (parentMatched || basicMatched || ancesterMatched) {
               if (isUniversal) {
                  tempList = (List)this.cache.get("!" + key);
                  if (tempList != null) {
                     universalList.addAll(tempList);
                  }
               } else if (!ignoreBasicMatches) {
                  int keyLength = key.length();
                  if (wildcardMatchStart) {
                     --keyLength;
                  }

                  if (wildcardMatchEnd) {
                     --keyLength;
                  } else if (parentMatchEnd) {
                     --keyLength;
                  }

                  if (keyLength > longKeyLength) {
                     rulesList = (List)this.cache.get(key);
                     longKeyLength = keyLength;
                  }
               }
            }
         }
      }

      if (rulesList == null) {
         rulesList = (List)this.cache.get("*");
      }

      if (rulesList != null) {
         universalList.addAll(rulesList);
      }

      if (namespace != null) {
         Iterator it = universalList.iterator();

         while(it.hasNext()) {
            Rule rule = (Rule)it.next();
            String ns_uri = rule.getNamespaceURI();
            if (ns_uri != null && !ns_uri.equals(namespace)) {
               it.remove();
            }
         }
      }

      Collections.sort(universalList, new Comparator() {
         public int compare(Object o1, Object o2) throws ClassCastException {
            Integer i1 = (Integer)ExtendedBaseRules.this.order.get(o1);
            Integer i2 = (Integer)ExtendedBaseRules.this.order.get(o2);
            if (i1 == null) {
               return i2 == null ? 0 : -1;
            } else {
               return i2 == null ? 1 : i1 - i2;
            }
         }
      });
      return universalList;
   }

   private boolean parentMatch(String key, String pattern, String parentPattern) {
      return parentPattern.endsWith(key.substring(1, key.length() - 2));
   }

   private boolean basicMatch(String key, String pattern) {
      return pattern.equals(key.substring(2)) || pattern.endsWith(key.substring(1));
   }

   private List findExactAncesterMatch(String parentPattern) {
      List matchingRules = null;
      int lastIndex = parentPattern.length();

      while(lastIndex-- > 0) {
         lastIndex = parentPattern.lastIndexOf(47, lastIndex);
         if (lastIndex > 0) {
            matchingRules = (List)this.cache.get(parentPattern.substring(0, lastIndex) + "/*");
            if (matchingRules != null) {
               return matchingRules;
            }
         }
      }

      return null;
   }
}
