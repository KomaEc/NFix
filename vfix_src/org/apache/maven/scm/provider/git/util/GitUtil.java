package org.apache.maven.scm.provider.git.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import org.apache.maven.scm.providers.gitlib.settings.Settings;
import org.apache.maven.scm.providers.gitlib.settings.io.xpp3.GitXpp3Reader;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class GitUtil {
   protected static final String GIT_SETTINGS_FILENAME = "git-settings.xml";
   public static final File DEFAULT_SETTINGS_DIRECTORY = new File(System.getProperty("user.home"), ".scm");
   private static File settingsDirectory;
   private static Settings settings;

   private GitUtil() {
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
         GitXpp3Reader reader = new GitXpp3Reader();

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
      return new File(settingsDirectory, "git-settings.xml");
   }

   static {
      settingsDirectory = DEFAULT_SETTINGS_DIRECTORY;
   }
}
