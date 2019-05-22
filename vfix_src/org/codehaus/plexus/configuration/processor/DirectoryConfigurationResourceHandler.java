package org.codehaus.plexus.configuration.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.util.FileUtils;

public class DirectoryConfigurationResourceHandler extends AbstractConfigurationResourceHandler {
   public String getId() {
      return "directory-configuration-resource";
   }

   public PlexusConfiguration[] handleRequest(Map parameters) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException {
      File f = new File(this.getSource(parameters));
      if (!f.exists()) {
         throw new ConfigurationResourceNotFoundException("The specified resource " + f + " cannot be found.");
      } else if (!f.isDirectory()) {
         throw new ConfigurationResourceNotFoundException("The specified resource " + f + " is not a directory.");
      } else {
         String includes = (String)parameters.get("includes");
         if (includes == null) {
            includes = "**/*.xml";
         }

         String excludes = (String)parameters.get("excludes");

         try {
            List files = FileUtils.getFiles(f, includes, excludes);
            PlexusConfiguration[] configurations = new PlexusConfiguration[files.size()];

            for(int i = 0; i < configurations.length; ++i) {
               File configurationFile = (File)files.get(i);
               PlexusConfiguration configuration = PlexusTools.buildConfiguration(configurationFile.getAbsolutePath(), new FileReader(configurationFile));
               configurations[i] = configuration;
            }

            return configurations;
         } catch (FileNotFoundException var10) {
            throw new ConfigurationProcessingException(var10);
         } catch (IOException var11) {
            throw new ConfigurationProcessingException(var11);
         } catch (PlexusConfigurationException var12) {
            throw new ConfigurationProcessingException(var12);
         }
      }
   }
}
