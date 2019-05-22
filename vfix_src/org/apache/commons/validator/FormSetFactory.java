package org.apache.commons.validator;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

public class FormSetFactory extends AbstractObjectCreationFactory {
   private static final Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$ValidatorResources;

   public Object createObject(Attributes attributes) throws Exception {
      ValidatorResources resources = (ValidatorResources)this.digester.peek(0);
      String language = attributes.getValue("language");
      String country = attributes.getValue("country");
      String variant = attributes.getValue("variant");
      return this.createFormSet(resources, language, country, variant);
   }

   private FormSet createFormSet(ValidatorResources resources, String language, String country, String variant) throws Exception {
      FormSet formSet = resources.getFormSet(language, country, variant);
      if (formSet != null) {
         if (log.isDebugEnabled()) {
            log.debug("FormSet[" + formSet.displayKey() + "] found - merging.");
         }

         return formSet;
      } else {
         formSet = new FormSet();
         formSet.setLanguage(language);
         formSet.setCountry(country);
         formSet.setVariant(variant);
         resources.addFormSet(formSet);
         if (log.isDebugEnabled()) {
            log.debug("FormSet[" + formSet.displayKey() + "] created.");
         }

         return formSet;
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
      log = LogFactory.getLog(class$org$apache$commons$validator$ValidatorResources == null ? (class$org$apache$commons$validator$ValidatorResources = class$("org.apache.commons.validator.ValidatorResources")) : class$org$apache$commons$validator$ValidatorResources);
   }
}
