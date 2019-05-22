package org.codehaus.plexus.configuration.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.util.IOUtil;

public class FileConfigurationResourceHandler extends AbstractConfigurationResourceHandler {
   public String getId() {
      return "file-configuration-resource";
   }

   public PlexusConfiguration[] handleRequest(Map parameters) throws ConfigurationResourceNotFoundException, ConfigurationProcessingException {
      File f = new File(this.getSource(parameters));
      if (!f.exists()) {
         throw new ConfigurationResourceNotFoundException("The specified resource " + f + " cannot be found.");
      } else {
         FileReader configurationReader = null;

         PlexusConfiguration[] var4;
         try {
            configurationReader = new FileReader(f);
            var4 = new PlexusConfiguration[]{PlexusTools.buildConfiguration(f.getAbsolutePath(), configurationReader)};
         } catch (PlexusConfigurationException var9) {
            throw new ConfigurationProcessingException(var9);
         } catch (FileNotFoundException var10) {
            throw new ConfigurationProcessingException(var10);
         } finally {
            IOUtil.close((Reader)configurationReader);
         }

         return var4;
      }
   }
}
