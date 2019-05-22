package org.apache.commons.digester;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RulesBase implements Rules {
   protected HashMap cache = new HashMap();
   protected Digester digester = null;
   protected String namespaceURI = null;
   protected ArrayList rules = new ArrayList();

   public Digester getDigester() {
      return this.digester;
   }

   public void setDigester(Digester digester) {
      this.digester = digester;
      Iterator items = this.rules.iterator();

      while(items.hasNext()) {
         Rule item = (Rule)items.next();
         item.setDigester(digester);
      }

   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setNamespaceURI(String namespaceURI) {
      this.namespaceURI = namespaceURI;
   }

   public void add(String pattern, Rule rule) {
      int patternLength = pattern.length();
      if (patternLength > 1 && pattern.endsWith("/")) {
         pattern = pattern.substring(0, patternLength - 1);
      }

      List list = (List)this.cache.get(pattern);
      if (list == null) {
         list = new ArrayList();
         this.cache.put(pattern, list);
      }

      ((List)list).add(rule);
      this.rules.add(rule);
      if (this.digester != null) {
         rule.setDigester(this.digester);
      }

      if (this.namespaceURI != null) {
         rule.setNamespaceURI(this.namespaceURI);
      }

   }

   public void clear() {
      this.cache.clear();
      this.rules.clear();
   }

   /** @deprecated */
   public List match(String pattern) {
      return this.match((String)null, pattern);
   }

   public List match(String namespaceURI, String pattern) {
      List rulesList = this.lookup(namespaceURI, pattern);
      if (rulesList == null || ((List)rulesList).size() < 1) {
         String longKey = "";
         Iterator keys = this.cache.keySet().iterator();

         label32:
         while(true) {
            String key;
            do {
               do {
                  if (!keys.hasNext()) {
                     break label32;
                  }

                  key = (String)keys.next();
               } while(!key.startsWith("*/"));
            } while(!pattern.equals(key.substring(2)) && !pattern.endsWith(key.substring(1)));

            if (key.length() > longKey.length()) {
               rulesList = this.lookup(namespaceURI, key);
               longKey = key;
            }
         }
      }

      if (rulesList == null) {
         rulesList = new ArrayList();
      }

      return (List)rulesList;
   }

   public List rules() {
      return this.rules;
   }

   protected List lookup(String namespaceURI, String pattern) {
      List list = (List)this.cache.get(pattern);
      if (list == null) {
         return null;
      } else if (namespaceURI != null && namespaceURI.length() != 0) {
         ArrayList results = new ArrayList();
         Iterator items = list.iterator();

         while(true) {
            Rule item;
            do {
               if (!items.hasNext()) {
                  return results;
               }

               item = (Rule)items.next();
            } while(!namespaceURI.equals(item.getNamespaceURI()) && item.getNamespaceURI() != null);

            results.add(item);
         }
      } else {
         return list;
      }
   }
}
