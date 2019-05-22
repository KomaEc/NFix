package org.apache.commons.digester.substitution;

import org.apache.commons.digester.Substitutor;
import org.xml.sax.Attributes;

public class VariableSubstitutor extends Substitutor {
   private VariableExpander attributesExpander;
   private VariableAttributes variableAttributes;
   private VariableExpander bodyTextExpander;

   public VariableSubstitutor(VariableExpander expander) {
      this(expander, expander);
   }

   public VariableSubstitutor(VariableExpander attributesExpander, VariableExpander bodyTextExpander) {
      this.attributesExpander = attributesExpander;
      this.bodyTextExpander = bodyTextExpander;
      this.variableAttributes = new VariableAttributes();
   }

   public Attributes substitute(Attributes attributes) {
      Attributes results = attributes;
      if (this.attributesExpander != null) {
         this.variableAttributes.init(attributes, this.attributesExpander);
         results = this.variableAttributes;
      }

      return (Attributes)results;
   }

   public String substitute(String bodyText) {
      String result = bodyText;
      if (this.bodyTextExpander != null) {
         result = this.bodyTextExpander.expand(bodyText);
      }

      return result;
   }
}
