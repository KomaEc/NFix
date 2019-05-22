package org.apache.velocity.texen.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.velocity.texen.Generator;

public class PropertiesUtil {
   public Properties load(String propertiesFile) {
      Properties properties = null;
      String templatePath = Generator.getInstance().getTemplatePath();

      try {
         if (templatePath != null) {
            properties = this.loadFromTemplatePath(propertiesFile);
         } else {
            properties = this.loadFromClassPath(propertiesFile);
         }

         return properties;
      } catch (RuntimeException var5) {
         throw var5;
      } catch (Exception var6) {
         throw new RuntimeException("Could not load properties: " + var6.getMessage());
      }
   }

   protected Properties loadFromTemplatePath(String propertiesFile) throws Exception {
      Properties properties = new Properties();
      String templatePath = Generator.getInstance().getTemplatePath();
      StringTokenizer st = new StringTokenizer(templatePath, ",");
      if (st.hasMoreTokens()) {
         String templateDir = st.nextToken();
         FileInputStream stream = null;

         try {
            String fullPath = propertiesFile;
            if (!propertiesFile.startsWith(templateDir)) {
               fullPath = templateDir + "/" + propertiesFile;
            }

            stream = new FileInputStream(fullPath);
            properties.load(stream);
         } finally {
            if (stream != null) {
               stream.close();
            }

         }
      }

      return properties;
   }

   protected Properties loadFromClassPath(String propertiesName) throws Exception {
      Properties properties = new Properties();
      ClassLoader classLoader = this.getClass().getClassLoader();
      InputStream inputStream = null;

      try {
         String propertiesFile = propertiesName.startsWith("$generator") ? propertiesName.substring("$generator.templatePath/".length()) : propertiesName;
         inputStream = classLoader.getResourceAsStream(propertiesFile);
         properties.load(inputStream);
      } finally {
         if (inputStream != null) {
            inputStream.close();
         }

      }

      return properties;
   }
}
