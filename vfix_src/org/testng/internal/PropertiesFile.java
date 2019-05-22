package org.testng.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {
   private Properties m_properties = new Properties();

   public PropertiesFile(String fileName) throws IOException {
      FileInputStream fis = null;

      try {
         fis = new FileInputStream(new File(fileName));
         this.m_properties.load(fis);
      } finally {
         if (fis != null) {
            fis.close();
         }

      }

   }

   public Properties getProperties() {
      return this.m_properties;
   }
}
