package org.apache.maven.scm.provider.clearcase.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ResourceBundle;
import org.apache.maven.scm.providers.clearcase.settings.Settings;
import org.apache.maven.scm.providers.clearcase.settings.io.xpp3.ClearcaseXpp3Reader;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public final class ClearCaseUtil {
   protected static final String CLEARCASE_SETTINGS_FILENAME = "clearcase-settings.xml";
   public static final File DEFAULT_SETTINGS_DIRECTORY = new File(System.getProperty("user.home"), ".scm");
   private static File settingsDirectory;
   private static final String RESOURCE_FILENAME = "org.apache.maven.scm.provider.clearcase.command.clearcase";
   private static final ResourceBundle RESOURCE_BUNDLE;
   private static Settings settings;

   private ClearCaseUtil() {
   }

   public static String getLocalizedResource(String key) {
      return RESOURCE_BUNDLE.getString(key);
   }

   public static Settings getSettings() {
      if (settings == null) {
         settings = readSettings();
      }

      return settings;
   }

   public static Settings readSettings() {
      File settingsFile = new File(settingsDirectory, "clearcase-settings.xml");
      if (!settingsFile.exists()) {
         File scmGlobalDir = new File(System.getProperty("maven.home"), "conf");
         settingsFile = new File(scmGlobalDir, "clearcase-settings.xml");
      }

      if (settingsFile.exists()) {
         ClearcaseXpp3Reader reader = new ClearcaseXpp3Reader();

         try {
            return reader.read((Reader)ReaderFactory.newXmlReader(settingsFile));
         } catch (FileNotFoundException var4) {
         } catch (IOException var5) {
         } catch (XmlPullParserException var6) {
            String message = settingsFile.getAbsolutePath() + " isn't well formed. SKIPPED." + var6.getMessage();
            System.out.println(message);
         }
      }

      return new Settings();
   }

   public static void setSettingsDirectory(File directory) {
      settingsDirectory = directory;
      settings = readSettings();
   }

   static {
      settingsDirectory = DEFAULT_SETTINGS_DIRECTORY;
      RESOURCE_BUNDLE = ResourceBundle.getBundle("org.apache.maven.scm.provider.clearcase.command.clearcase");
   }
}
