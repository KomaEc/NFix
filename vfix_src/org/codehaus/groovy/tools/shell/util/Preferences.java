package org.codehaus.groovy.tools.shell.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import org.codehaus.groovy.tools.shell.IO;

public class Preferences {
   private static final java.util.prefs.Preferences STORE = java.util.prefs.Preferences.userRoot().node("/org/codehaus/groovy/tools/shell");
   public static IO.Verbosity verbosity;

   public static boolean getShowLastResult() {
      return STORE.getBoolean("show-last-result", true);
   }

   public static boolean getSanitizeStackTrace() {
      return STORE.getBoolean("sanitize-stack-trace", true);
   }

   public static String getEditor() {
      return STORE.get("editor", System.getenv("EDITOR"));
   }

   public static String getParserFlavor() {
      return STORE.get("parser-flavor", "rigid");
   }

   public static String[] keys() throws BackingStoreException {
      return STORE.keys();
   }

   public static String get(String name, String defaultValue) {
      return STORE.get(name, defaultValue);
   }

   public static String get(String name) {
      return get(name, (String)null);
   }

   public static void put(String name, String value) {
      STORE.put(name, value);
   }

   public static void clear() throws BackingStoreException {
      STORE.clear();
   }

   public static void addChangeListener(PreferenceChangeListener listener) {
      STORE.addPreferenceChangeListener(listener);
   }

   static {
      String tmp = STORE.get("verbosity", IO.Verbosity.INFO.name);

      try {
         verbosity = IO.Verbosity.forName(tmp);
      } catch (IllegalArgumentException var2) {
         verbosity = IO.Verbosity.INFO;
         STORE.remove("verbosity");
      }

      addChangeListener(new PreferenceChangeListener() {
         public void preferenceChange(PreferenceChangeEvent event) {
            if (event.getKey().equals("verbosity")) {
               String name = event.getNewValue();
               if (name == null) {
                  name = IO.Verbosity.INFO.name;
               }

               try {
                  Preferences.verbosity = IO.Verbosity.forName(name);
               } catch (Exception var4) {
                  event.getNode().put(event.getKey(), Preferences.verbosity.name);
               }
            }

         }
      });
   }
}
