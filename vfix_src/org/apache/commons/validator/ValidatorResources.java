package org.apache.commons.validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ValidatorResources implements Serializable {
   private static final String[] registrations = new String[]{"-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.0//EN", "/org/apache/commons/validator/resources/validator_1_0.dtd", "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.0.1//EN", "/org/apache/commons/validator/resources/validator_1_0_1.dtd", "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1//EN", "/org/apache/commons/validator/resources/validator_1_1.dtd", "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1.3//EN", "/org/apache/commons/validator/resources/validator_1_1_3.dtd", "-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.2.0//EN", "/org/apache/commons/validator/resources/validator_1_2_0.dtd"};
   private static final Log log;
   /** @deprecated */
   protected FastHashMap hFormSets;
   /** @deprecated */
   protected FastHashMap hConstants;
   /** @deprecated */
   protected FastHashMap hActions;
   protected static Locale defaultLocale;
   protected FormSet defaultFormSet;
   private static final String argsPattern = "form-validation/formset/form/field/arg";
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$ValidatorResources;

   public ValidatorResources() {
      this.hFormSets = new FastHashMap();
      this.hConstants = new FastHashMap();
      this.hActions = new FastHashMap();
   }

   public ValidatorResources(InputStream in) throws IOException, SAXException {
      this(new InputStream[]{in});
   }

   public ValidatorResources(InputStream[] streams) throws IOException, SAXException {
      this.hFormSets = new FastHashMap();
      this.hConstants = new FastHashMap();
      this.hActions = new FastHashMap();
      Digester digester = this.initDigester();

      for(int i = 0; i < streams.length; ++i) {
         digester.push(this);
         digester.parse(streams[i]);
      }

      this.process();
   }

   public ValidatorResources(String uri) throws IOException, SAXException {
      this(new String[]{uri});
   }

   public ValidatorResources(String[] uris) throws IOException, SAXException {
      this.hFormSets = new FastHashMap();
      this.hConstants = new FastHashMap();
      this.hActions = new FastHashMap();
      Digester digester = this.initDigester();

      for(int i = 0; i < uris.length; ++i) {
         digester.push(this);
         digester.parse(uris[i]);
      }

      this.process();
   }

   private Digester initDigester() {
      URL rulesUrl = this.getClass().getResource("digester-rules.xml");
      Digester digester = DigesterLoader.createDigester(rulesUrl);
      digester.setNamespaceAware(true);
      digester.setValidating(true);
      digester.setUseContextClassLoader(true);
      this.addOldArgRules(digester);

      for(int i = 0; i < registrations.length; i += 2) {
         URL url = this.getClass().getResource(registrations[i + 1]);
         if (url != null) {
            digester.register(registrations[i], url.toString());
         }
      }

      return digester;
   }

   private void addOldArgRules(Digester digester) {
      Rule rule = new Rule() {
         public void begin(String namespace, String name, Attributes attributes) throws Exception {
            Arg arg = new Arg();
            arg.setKey(attributes.getValue("key"));
            arg.setName(attributes.getValue("name"));

            try {
               arg.setPosition(Integer.parseInt(name.substring(3)));
            } catch (Exception var6) {
               ValidatorResources.log.error("Error parsing Arg position: " + name + " " + arg + " " + var6);
            }

            ((Field)this.getDigester().peek(0)).addArg(arg);
         }
      };
      digester.addRule("form-validation/formset/form/field/arg0", rule);
      digester.addRule("form-validation/formset/form/field/arg1", rule);
      digester.addRule("form-validation/formset/form/field/arg2", rule);
      digester.addRule("form-validation/formset/form/field/arg3", rule);
   }

   public void addFormSet(FormSet fs) {
      String key = this.buildKey(fs);
      if (key.length() == 0) {
         if (log.isWarnEnabled() && this.defaultFormSet != null) {
            log.warn("Overriding default FormSet definition.");
         }

         this.defaultFormSet = fs;
      } else {
         FormSet formset = (FormSet)this.hFormSets.get(key);
         if (formset == null) {
            if (log.isDebugEnabled()) {
               log.debug("Adding FormSet '" + fs.toString() + "'.");
            }
         } else if (log.isWarnEnabled()) {
            log.warn("Overriding FormSet definition. Duplicate for locale: " + key);
         }

         this.hFormSets.put(key, fs);
      }

   }

   public void addConstant(String name, String value) {
      if (log.isDebugEnabled()) {
         log.debug("Adding Global Constant: " + name + "," + value);
      }

      this.hConstants.put(name, value);
   }

   public void addValidatorAction(ValidatorAction va) {
      va.init();
      this.hActions.put(va.getName(), va);
      if (log.isDebugEnabled()) {
         log.debug("Add ValidatorAction: " + va.getName() + "," + va.getClassname());
      }

   }

   public ValidatorAction getValidatorAction(String key) {
      return (ValidatorAction)this.hActions.get(key);
   }

   public Map getValidatorActions() {
      return Collections.unmodifiableMap(this.hActions);
   }

   protected String buildKey(FormSet fs) {
      return this.buildLocale(fs.getLanguage(), fs.getCountry(), fs.getVariant());
   }

   private String buildLocale(String lang, String country, String variant) {
      String key = lang != null && lang.length() > 0 ? lang : "";
      key = key + (country != null && country.length() > 0 ? "_" + country : "");
      key = key + (variant != null && variant.length() > 0 ? "_" + variant : "");
      return key;
   }

   public Form getForm(Locale locale, String formKey) {
      return this.getForm(locale.getLanguage(), locale.getCountry(), locale.getVariant(), formKey);
   }

   public Form getForm(String language, String country, String variant, String formKey) {
      Form form = null;
      String key = this.buildLocale(language, country, variant);
      if (key.length() > 0) {
         FormSet formSet = (FormSet)this.hFormSets.get(key);
         if (formSet != null) {
            form = formSet.getForm(formKey);
         }
      }

      String localeKey = key;
      FormSet formSet;
      if (form == null) {
         key = this.buildLocale(language, country, (String)null);
         if (key.length() > 0) {
            formSet = (FormSet)this.hFormSets.get(key);
            if (formSet != null) {
               form = formSet.getForm(formKey);
            }
         }
      }

      if (form == null) {
         key = this.buildLocale(language, (String)null, (String)null);
         if (key.length() > 0) {
            formSet = (FormSet)this.hFormSets.get(key);
            if (formSet != null) {
               form = formSet.getForm(formKey);
            }
         }
      }

      if (form == null) {
         form = this.defaultFormSet.getForm(formKey);
         key = "default";
      }

      if (form == null) {
         if (log.isWarnEnabled()) {
            log.warn("Form '" + formKey + "' not found for locale '" + key + "'");
         }
      } else if (log.isDebugEnabled()) {
         log.debug("Form '" + formKey + "' found in formset '" + key + "' for locale '" + localeKey + "'");
      }

      return form;
   }

   public void process() {
      this.hFormSets.setFast(true);
      this.hConstants.setFast(true);
      this.hActions.setFast(true);
      this.processForms();
   }

   private void processForms() {
      if (this.defaultFormSet == null) {
         this.defaultFormSet = new FormSet();
      }

      this.defaultFormSet.process(this.hConstants);
      Iterator i = this.hFormSets.keySet().iterator();

      FormSet fs;
      while(i.hasNext()) {
         String key = (String)i.next();
         fs = (FormSet)this.hFormSets.get(key);
         fs.merge(this.getParent(fs));
      }

      Iterator i = this.hFormSets.values().iterator();

      while(i.hasNext()) {
         fs = (FormSet)i.next();
         if (!fs.isProcessed()) {
            fs.process(this.hConstants);
         }
      }

   }

   private FormSet getParent(FormSet fs) {
      FormSet parent = null;
      if (fs.getType() == 2) {
         parent = this.defaultFormSet;
      } else if (fs.getType() == 3) {
         parent = (FormSet)this.hFormSets.get(this.buildLocale(fs.getLanguage(), (String)null, (String)null));
         if (parent == null) {
            parent = this.defaultFormSet;
         }
      } else if (fs.getType() == 4) {
         parent = (FormSet)this.hFormSets.get(this.buildLocale(fs.getLanguage(), fs.getCountry(), (String)null));
         if (parent == null) {
            parent = (FormSet)this.hFormSets.get(this.buildLocale(fs.getLanguage(), (String)null, (String)null));
            if (parent == null) {
               parent = this.defaultFormSet;
            }
         }
      }

      return parent;
   }

   FormSet getFormSet(String language, String country, String variant) {
      String key = this.buildLocale(language, country, variant);
      return key.length() == 0 ? this.defaultFormSet : (FormSet)this.hFormSets.get(key);
   }

   protected Map getFormSets() {
      return this.hFormSets;
   }

   protected Map getConstants() {
      return this.hConstants;
   }

   protected Map getActions() {
      return this.hActions;
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
      defaultLocale = Locale.getDefault();
   }
}
