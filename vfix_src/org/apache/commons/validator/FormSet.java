package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FormSet implements Serializable {
   private static final Log log;
   private boolean processed = false;
   private String language = null;
   private String country = null;
   private String variant = null;
   private Map forms = new HashMap();
   private Map constants = new HashMap();
   protected static final int GLOBAL_FORMSET = 1;
   protected static final int LANGUAGE_FORMSET = 2;
   protected static final int COUNTRY_FORMSET = 3;
   protected static final int VARIANT_FORMSET = 4;
   private boolean merged;
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$FormSet;

   protected boolean isMerged() {
      return this.merged;
   }

   protected int getType() {
      if (this.getVariant() != null) {
         if (this.getLanguage() != null && this.getCountry() != null) {
            return 4;
         } else {
            throw new NullPointerException("When variant is specified, country and language must be specified.");
         }
      } else if (this.getCountry() != null) {
         if (this.getLanguage() == null) {
            throw new NullPointerException("When country is specified, language must be specified.");
         } else {
            return 3;
         }
      } else {
         return this.getLanguage() != null ? 2 : 1;
      }
   }

   protected void merge(FormSet depends) {
      if (depends != null) {
         Map pForms = this.getForms();
         Map dForms = depends.getForms();
         Iterator it = dForms.keySet().iterator();

         while(it.hasNext()) {
            Object key = it.next();
            Form pForm = (Form)pForms.get(key);
            if (pForm != null) {
               pForm.merge((Form)dForms.get(key));
            } else {
               this.addForm((Form)dForms.get(key));
            }
         }
      }

      this.merged = true;
   }

   public boolean isProcessed() {
      return this.processed;
   }

   public String getLanguage() {
      return this.language;
   }

   public void setLanguage(String language) {
      this.language = language;
   }

   public String getCountry() {
      return this.country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getVariant() {
      return this.variant;
   }

   public void setVariant(String variant) {
      this.variant = variant;
   }

   public void addConstant(String name, String value) {
      if (this.constants.containsKey(name)) {
         log.error("Constant '" + name + "' already exists in FormSet[" + this.displayKey() + "] - ignoring.");
      } else {
         this.constants.put(name, value);
      }

   }

   public void addForm(Form f) {
      String formName = f.getName();
      if (this.forms.containsKey(formName)) {
         log.error("Form '" + formName + "' already exists in FormSet[" + this.displayKey() + "] - ignoring.");
      } else {
         this.forms.put(f.getName(), f);
      }

   }

   public Form getForm(String formName) {
      return (Form)this.forms.get(formName);
   }

   public Map getForms() {
      return Collections.unmodifiableMap(this.forms);
   }

   synchronized void process(Map globalConstants) {
      Iterator i = this.forms.values().iterator();

      while(i.hasNext()) {
         Form f = (Form)i.next();
         f.process(globalConstants, this.constants, this.forms);
      }

      this.processed = true;
   }

   public String displayKey() {
      StringBuffer results = new StringBuffer();
      if (this.language != null && this.language.length() > 0) {
         results.append("language=");
         results.append(this.language);
      }

      if (this.country != null && this.country.length() > 0) {
         if (results.length() > 0) {
            results.append(", ");
         }

         results.append("country=");
         results.append(this.country);
      }

      if (this.variant != null && this.variant.length() > 0) {
         if (results.length() > 0) {
            results.append(", ");
         }

         results.append("variant=");
         results.append(this.variant);
      }

      if (results.length() == 0) {
         results.append("default");
      }

      return results.toString();
   }

   public String toString() {
      StringBuffer results = new StringBuffer();
      results.append("FormSet: language=");
      results.append(this.language);
      results.append("  country=");
      results.append(this.country);
      results.append("  variant=");
      results.append(this.variant);
      results.append("\n");
      Iterator i = this.getForms().values().iterator();

      while(i.hasNext()) {
         results.append("   ");
         results.append(i.next());
         results.append("\n");
      }

      return results.toString();
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
      log = LogFactory.getLog(class$org$apache$commons$validator$FormSet == null ? (class$org$apache$commons$validator$FormSet = class$("org.apache.commons.validator.FormSet")) : class$org$apache$commons$validator$FormSet);
   }
}
