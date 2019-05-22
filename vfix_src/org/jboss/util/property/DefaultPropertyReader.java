package org.jboss.util.property;

public final class DefaultPropertyReader extends FilePropertyReader {
   public static final String DEFAULT_PROPERTY_NAME = "properties";

   public DefaultPropertyReader(String propertyName) {
      super(getFilenames(propertyName));
   }

   public DefaultPropertyReader() {
      this("properties");
   }

   public static String[] getFilenames(String propertyName) throws PropertyException {
      Object filename = PropertyManager.getProperty(propertyName);
      String[] filenames;
      if (filename != null) {
         filenames = new String[]{String.valueOf(filename)};
      } else {
         filenames = PropertyManager.getArrayProperty(propertyName);
      }

      return filenames;
   }
}
