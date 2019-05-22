package org.apache.commons.digester;

public abstract class RuleSetBase implements RuleSet {
   protected String namespaceURI = null;

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public abstract void addRuleInstances(Digester var1);
}
