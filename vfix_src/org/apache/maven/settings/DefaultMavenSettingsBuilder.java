package org.apache.maven.settings;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.settings.io.xpp3.SettingsXpp3Reader;
import org.codehaus.plexus.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultMavenSettingsBuilder extends AbstractLogEnabled implements MavenSettingsBuilder, Initializable {
   public static final String userHome = System.getProperty("user.home");
   private String userSettingsPath;
   private String globalSettingsPath;
   private File userSettingsFile;
   private File globalSettingsFile;
   private Settings loadedSettings;

   public void initialize() {
      this.userSettingsFile = this.getFile(this.userSettingsPath, "user.home", "org.apache.maven.user-settings");
      this.globalSettingsFile = this.getFile(this.globalSettingsPath, "maven.home", "org.apache.maven.global-settings");
      this.getLogger().debug("Building Maven global-level settings from: '" + this.globalSettingsFile.getAbsolutePath() + "'");
      this.getLogger().debug("Building Maven user-level settings from: '" + this.userSettingsFile.getAbsolutePath() + "'");
   }

   private Settings readSettings(File settingsFile) throws IOException, XmlPullParserException {
      Settings settings = null;
      if (settingsFile != null && settingsFile.exists() && settingsFile.isFile()) {
         XmlStreamReader reader = null;

         try {
            reader = ReaderFactory.newXmlReader(settingsFile);
            StringWriter sWriter = new StringWriter();
            IOUtil.copy((Reader)reader, (Writer)sWriter);
            String rawInput = sWriter.toString();

            try {
               RegexBasedInterpolator interpolator = new RegexBasedInterpolator();
               interpolator.addValueSource(new EnvarBasedValueSource());
               rawInput = interpolator.interpolate(rawInput, "settings");
            } catch (Exception var12) {
               this.getLogger().warn("Failed to initialize environment variable resolver. Skipping environment substitution in settings.");
               this.getLogger().debug("Failed to initialize envar resolver. Skipping resolution.", var12);
            }

            StringReader sReader = new StringReader(rawInput);
            SettingsXpp3Reader modelReader = new SettingsXpp3Reader();
            settings = modelReader.read((Reader)sReader, true);
            RuntimeInfo rtInfo = new RuntimeInfo(settings);
            rtInfo.setFile(settingsFile);
            settings.setRuntimeInfo(rtInfo);
         } finally {
            IOUtil.close((Reader)reader);
         }
      }

      return settings;
   }

   public Settings buildSettings() throws IOException, XmlPullParserException {
      return this.buildSettings(this.userSettingsFile);
   }

   public Settings buildSettings(boolean useCachedSettings) throws IOException, XmlPullParserException {
      return this.buildSettings(this.userSettingsFile, useCachedSettings);
   }

   public Settings buildSettings(File userSettingsFile) throws IOException, XmlPullParserException {
      return this.buildSettings(userSettingsFile, true);
   }

   public Settings buildSettings(File userSettingsFile, boolean useCachedSettings) throws IOException, XmlPullParserException {
      if (!useCachedSettings || this.loadedSettings == null) {
         Settings globalSettings = this.readSettings(this.globalSettingsFile);
         Settings userSettings = this.readSettings(userSettingsFile);
         if (globalSettings == null) {
            globalSettings = new Settings();
         }

         if (userSettings == null) {
            userSettings = new Settings();
            userSettings.setRuntimeInfo(new RuntimeInfo(userSettings));
         }

         SettingsUtils.merge(userSettings, globalSettings, "global-level");
         this.activateDefaultProfiles(userSettings);
         this.setLocalRepository(userSettings);
         this.loadedSettings = userSettings;
      }

      return this.loadedSettings;
   }

   private void activateDefaultProfiles(Settings settings) {
      List activeProfiles = settings.getActiveProfiles();
      Iterator profiles = settings.getProfiles().iterator();

      while(profiles.hasNext()) {
         Profile profile = (Profile)profiles.next();
         if (profile.getActivation() != null && profile.getActivation().isActiveByDefault() && !activeProfiles.contains(profile.getId())) {
            settings.addActiveProfile(profile.getId());
         }
      }

   }

   private void setLocalRepository(Settings userSettings) {
      String localRepository = System.getProperty("maven.repo.local");
      if (localRepository == null || localRepository.length() < 1) {
         localRepository = userSettings.getLocalRepository();
      }

      File file;
      if (localRepository == null || localRepository.length() < 1) {
         file = new File(userHome, ".m2");
         if (!file.exists() && !file.mkdirs()) {
         }

         localRepository = (new File(file, "repository")).getAbsolutePath();
      }

      file = new File(localRepository);
      if (!file.isAbsolute() && file.getPath().startsWith(File.separator)) {
         localRepository = file.getAbsolutePath();
      }

      userSettings.setLocalRepository(localRepository);
   }

   private File getFile(String pathPattern, String basedirSysProp, String altLocationSysProp) {
      String path = System.getProperty(altLocationSysProp);
      if (StringUtils.isEmpty(path)) {
         String basedir = System.getProperty(basedirSysProp);
         if (basedir == null) {
            basedir = System.getProperty("user.dir");
         }

         basedir = basedir.replaceAll("\\\\", "/");
         basedir = basedir.replaceAll("\\$", "\\\\\\$");
         path = pathPattern.replaceAll("\\$\\{" + basedirSysProp + "\\}", basedir);
         path = path.replaceAll("\\\\", "/");
         return (new File(path)).getAbsoluteFile();
      } else {
         return (new File(path)).getAbsoluteFile();
      }
   }
}
