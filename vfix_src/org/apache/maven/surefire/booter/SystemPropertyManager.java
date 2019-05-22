package org.apache.maven.surefire.booter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemPropertyManager {
   public static PropertiesWrapper loadProperties(InputStream inStream) throws IOException {
      Properties p = new Properties();

      try {
         p.load(inStream);
      } finally {
         close(inStream);
      }

      return new PropertiesWrapper(p);
   }

   private static PropertiesWrapper loadProperties(File file) throws IOException {
      return loadProperties((InputStream)(new FileInputStream(file)));
   }

   public static void setSystemProperties(File file) throws IOException {
      PropertiesWrapper p = loadProperties(file);
      p.setAsSystemProperties();
   }

   public static File writePropertiesFile(Properties properties, File tempDirectory, String name, boolean keepForkFiles) throws IOException {
      File file = File.createTempFile(name, "tmp", tempDirectory);
      if (!keepForkFiles) {
         file.deleteOnExit();
      }

      writePropertiesFile(file, name, properties);
      return file;
   }

   public static void writePropertiesFile(File file, String name, Properties properties) throws IOException {
      FileOutputStream out = new FileOutputStream(file);

      try {
         properties.store(out, name);
      } finally {
         out.close();
      }

   }

   public static void close(InputStream inputStream) {
      if (inputStream != null) {
         try {
            inputStream.close();
         } catch (IOException var2) {
         }

      }
   }
}
