package org.apache.maven.plugin.registry;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import org.apache.maven.plugin.registry.io.xpp3.PluginRegistryXpp3Reader;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultPluginRegistryBuilder extends AbstractLogEnabled implements MavenPluginRegistryBuilder, Initializable {
   public static final String userHome = System.getProperty("user.home");
   private String userRegistryPath;
   private String globalRegistryPath;
   private File userRegistryFile;
   private File globalRegistryFile;

   public void initialize() {
      this.userRegistryFile = this.getFile(this.userRegistryPath, "user.home", "org.apache.maven.user-plugin-registry");
      this.getLogger().debug("Building Maven user-level plugin registry from: '" + this.userRegistryFile.getAbsolutePath() + "'");
      if (System.getProperty("maven.home") != null || System.getProperty("org.apache.maven.global-plugin-registry") != null) {
         this.globalRegistryFile = this.getFile(this.globalRegistryPath, "maven.home", "org.apache.maven.global-plugin-registry");
         this.getLogger().debug("Building Maven global-level plugin registry from: '" + this.globalRegistryFile.getAbsolutePath() + "'");
      }

   }

   public PluginRegistry buildPluginRegistry() throws IOException, XmlPullParserException {
      PluginRegistry global = this.readPluginRegistry(this.globalRegistryFile);
      PluginRegistry user = this.readPluginRegistry(this.userRegistryFile);
      if (user == null && global != null) {
         PluginRegistryUtils.recursivelySetSourceLevel(global, "global-level");
         user = global;
      } else {
         PluginRegistryUtils.merge(user, global, "global-level");
      }

      return user;
   }

   private PluginRegistry readPluginRegistry(File registryFile) throws IOException, XmlPullParserException {
      PluginRegistry registry = null;
      if (registryFile != null && registryFile.exists() && registryFile.isFile()) {
         XmlStreamReader reader = null;

         try {
            reader = ReaderFactory.newXmlReader(registryFile);
            PluginRegistryXpp3Reader modelReader = new PluginRegistryXpp3Reader();
            registry = modelReader.read((Reader)reader);
            RuntimeInfo rtInfo = new RuntimeInfo(registry);
            registry.setRuntimeInfo(rtInfo);
            rtInfo.setFile(registryFile);
         } finally {
            IOUtil.close((Reader)reader);
         }
      }

      return registry;
   }

   private File getFile(String pathPattern, String basedirSysProp, String altLocationSysProp) {
      String path = System.getProperty(altLocationSysProp);
      if (StringUtils.isEmpty(path)) {
         String basedir = System.getProperty(basedirSysProp);
         basedir = basedir.replaceAll("\\\\", "/");
         basedir = basedir.replaceAll("\\$", "\\\\\\$");
         path = pathPattern.replaceAll("\\$\\{" + basedirSysProp + "\\}", basedir);
         path = path.replaceAll("\\\\", "/");
         path = path.replaceAll("//", "/");
         return (new File(path)).getAbsoluteFile();
      } else {
         return (new File(path)).getAbsoluteFile();
      }
   }

   public PluginRegistry createUserPluginRegistry() {
      PluginRegistry registry = new PluginRegistry();
      RuntimeInfo rtInfo = new RuntimeInfo(registry);
      registry.setRuntimeInfo(rtInfo);
      rtInfo.setFile(this.userRegistryFile);
      return registry;
   }
}
