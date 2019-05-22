package org.apache.maven.scm.provider.svn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.maven.scm.providers.svn.settings.Settings;
import org.apache.maven.scm.providers.svn.settings.io.xpp3.SvnXpp3Reader;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class SvnUtil {
   protected static final String SVN_SETTINGS_FILENAME = "svn-settings.xml";
   public static final File DEFAULT_SETTINGS_DIRECTORY = new File(System.getProperty("user.home"), ".scm");
   private static File settingsDirectory;
   private static Settings settings;

   private SvnUtil() {
   }

   public static Settings getSettings() {
      if (settings == null) {
         settings = readSettings();
      }

      return settings;
   }

   public static Settings readSettings() {
      File settingsFile = getSettingsFile();
      if (settingsFile.exists()) {
         SvnXpp3Reader reader = new SvnXpp3Reader();

         try {
            return reader.read((Reader)ReaderFactory.newXmlReader(settingsFile));
         } catch (FileNotFoundException var4) {
         } catch (IOException var5) {
         } catch (XmlPullParserException var6) {
            String message = settingsFile.getAbsolutePath() + " isn't well formed. SKIPPED." + var6.getMessage();
            System.err.println(message);
         }
      }

      return new Settings();
   }

   public static void setSettingsDirectory(File directory) {
      settingsDirectory = directory;
      settings = readSettings();
   }

   public static File getSettingsFile() {
      return new File(settingsDirectory, "svn-settings.xml");
   }

   static {
      settingsDirectory = DEFAULT_SETTINGS_DIRECTORY;
   }
}
