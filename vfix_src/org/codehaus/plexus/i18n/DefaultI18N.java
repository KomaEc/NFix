package org.codehaus.plexus.i18n;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.codehaus.plexus.util.StringUtils;

public class DefaultI18N extends AbstractLogEnabled implements I18N, Initializable {
   private static final Object[] NO_ARGS = new Object[0];
   private HashMap bundles;
   private String[] bundleNames;
   private String defaultBundleName;
   private Locale defaultLocale = Locale.getDefault();
   private String defaultLanguage = Locale.getDefault().getLanguage();
   private String defaultCountry = Locale.getDefault().getCountry();
   private boolean devMode;

   public String getDefaultLanguage() {
      return this.defaultLanguage;
   }

   public String getDefaultCountry() {
      return this.defaultCountry;
   }

   public String getDefaultBundleName() {
      return this.defaultBundleName;
   }

   public String[] getBundleNames() {
      return (String[])((String[])this.bundleNames.clone());
   }

   public ResourceBundle getBundle() {
      return this.getBundle(this.getDefaultBundleName(), (Locale)null);
   }

   public ResourceBundle getBundle(String bundleName) {
      return this.getBundle(bundleName, (Locale)null);
   }

   public ResourceBundle getBundle(String bundleName, String languageHeader) {
      return this.getBundle(bundleName, this.getLocale(languageHeader));
   }

   public ResourceBundle getBundle(String bundleName, Locale locale) {
      bundleName = bundleName == null ? this.getDefaultBundleName() : bundleName.trim();
      if (this.devMode) {
         try {
            Class klass = ResourceBundle.getBundle(bundleName).getClass().getSuperclass();
            Field field = klass.getDeclaredField("cacheList");
            field.setAccessible(true);
            Object cache = field.get((Object)null);
            cache.getClass().getDeclaredMethod("clear", (Class[])null).invoke(cache, (Object[])null);
            field.setAccessible(false);
         } catch (Exception var6) {
         }
      }

      if (locale == null) {
         locale = this.getLocale((String)null);
      }

      HashMap bundlesByLocale = (HashMap)this.bundles.get(bundleName);
      ResourceBundle rb;
      if (bundlesByLocale != null) {
         rb = (ResourceBundle)bundlesByLocale.get(locale);
         if (rb == null) {
            rb = this.cacheBundle(bundleName, locale);
         }
      } else {
         rb = this.cacheBundle(bundleName, locale);
      }

      return rb;
   }

   public Locale getLocale(String header) {
      if (!StringUtils.isEmpty(header)) {
         I18NTokenizer tok = new I18NTokenizer(header);
         if (tok.hasNext()) {
            return (Locale)tok.next();
         }
      }

      return this.defaultLocale;
   }

   public String getString(String key) {
      return this.getString(key, (Locale)null);
   }

   public String getString(String key, Locale locale) {
      return this.getString(this.getDefaultBundleName(), locale, key);
   }

   public String getString(String bundleName, Locale locale, String key) {
      if (locale == null) {
         locale = this.getLocale((String)null);
      }

      ResourceBundle rb = this.getBundle(bundleName, locale);
      String value = this.getStringOrNull(rb, key);
      String name;
      if (value == null && this.bundleNames.length > 0) {
         for(int i = 0; i < this.bundleNames.length; ++i) {
            name = this.bundleNames[i];
            if (!name.equals(bundleName)) {
               rb = this.getBundle(name, locale);
               value = this.getStringOrNull(rb, key);
               if (value != null) {
                  locale = rb.getLocale();
                  break;
               }
            }
         }
      }

      if (value == null) {
         name = locale.toString();
         String mesg = "Noticed missing resource: bundleName=" + bundleName + ", locale=" + name + ", key=" + key;
         this.getLogger().debug(mesg);
         value = key;
      }

      return value;
   }

   public String format(String key, Object arg1) {
      return this.format(this.defaultBundleName, this.defaultLocale, key, new Object[]{arg1});
   }

   public String format(String key, Object arg1, Object arg2) {
      return this.format(this.defaultBundleName, this.defaultLocale, key, new Object[]{arg1, arg2});
   }

   public String format(String bundleName, Locale locale, String key, Object arg1) {
      return this.format(bundleName, locale, key, new Object[]{arg1});
   }

   public String format(String bundleName, Locale locale, String key, Object arg1, Object arg2) {
      return this.format(bundleName, locale, key, new Object[]{arg1, arg2});
   }

   public String format(String bundleName, Locale locale, String key, Object[] args) {
      if (locale == null) {
         locale = this.getLocale((String)null);
      }

      String value = this.getString(bundleName, locale, key);
      if (args == null) {
         args = NO_ARGS;
      }

      MessageFormat messageFormat = new MessageFormat("");
      messageFormat.setLocale(locale);
      messageFormat.applyPattern(value);
      return messageFormat.format(args);
   }

   public void initialize() throws InitializationException {
      this.bundles = new HashMap();
      this.defaultLocale = new Locale(this.defaultLanguage, this.defaultCountry);
      this.initializeBundleNames();
      if ("true".equals(System.getProperty("PLEXUS_DEV_MODE"))) {
         this.devMode = true;
      }

   }

   protected void initializeBundleNames() {
      if (this.defaultBundleName != null && this.defaultBundleName.length() > 0) {
         if (this.bundleNames != null && this.bundleNames.length > 0) {
            String[] array = new String[this.bundleNames.length + 1];
            array[0] = this.defaultBundleName;
            System.arraycopy(this.bundleNames, 0, array, 1, this.bundleNames.length);
            this.bundleNames = array;
         } else {
            this.bundleNames = new String[]{this.defaultBundleName};
         }
      }

      if (this.bundleNames == null) {
         this.bundleNames = new String[0];
      }

   }

   private synchronized ResourceBundle cacheBundle(String bundleName, Locale locale) throws MissingResourceException {
      HashMap bundlesByLocale = (HashMap)this.bundles.get(bundleName);
      ResourceBundle rb = bundlesByLocale == null ? null : (ResourceBundle)bundlesByLocale.get(locale);
      if (rb == null) {
         bundlesByLocale = bundlesByLocale == null ? new HashMap(3) : new HashMap(bundlesByLocale);

         try {
            rb = ResourceBundle.getBundle(bundleName, locale);
         } catch (MissingResourceException var6) {
            rb = this.findBundleByLocale(bundleName, locale, bundlesByLocale);
            if (rb == null) {
               throw (MissingResourceException)var6.fillInStackTrace();
            }
         }

         if (rb != null) {
            bundlesByLocale.put(rb.getLocale(), rb);
            HashMap bundlesByName = new HashMap(this.bundles);
            bundlesByName.put(bundleName, bundlesByLocale);
            this.bundles = bundlesByName;
         }
      }

      return rb;
   }

   private ResourceBundle findBundleByLocale(String bundleName, Locale locale, Map bundlesByLocale) {
      ResourceBundle rb = null;
      Locale withDefaultLanguage;
      if (!StringUtils.isNotEmpty(locale.getCountry()) && this.defaultLanguage.equals(locale.getLanguage())) {
         withDefaultLanguage = new Locale(locale.getLanguage(), this.defaultCountry);
         rb = (ResourceBundle)bundlesByLocale.get(withDefaultLanguage);
         if (rb == null) {
            rb = this.getBundleIgnoreException(bundleName, withDefaultLanguage);
         }
      } else if (!StringUtils.isNotEmpty(locale.getLanguage()) && this.defaultCountry.equals(locale.getCountry())) {
         withDefaultLanguage = new Locale(this.defaultLanguage, locale.getCountry());
         rb = (ResourceBundle)bundlesByLocale.get(withDefaultLanguage);
         if (rb == null) {
            rb = this.getBundleIgnoreException(bundleName, withDefaultLanguage);
         }
      }

      if (rb == null && !this.defaultLocale.equals(locale)) {
         rb = this.getBundleIgnoreException(bundleName, this.defaultLocale);
      }

      return rb;
   }

   private ResourceBundle getBundleIgnoreException(String bundleName, Locale locale) {
      try {
         return ResourceBundle.getBundle(bundleName, locale);
      } catch (MissingResourceException var4) {
         return null;
      }
   }

   protected final String getStringOrNull(ResourceBundle rb, String key) {
      if (rb != null) {
         try {
            return rb.getString(key);
         } catch (MissingResourceException var4) {
         }
      }

      return null;
   }
}
