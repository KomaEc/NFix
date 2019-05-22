package org.jboss.util.property;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.jboss.util.NullArgumentException;

public class FilePropertyReader implements PropertyReader {
   protected String[] filenames;

   public FilePropertyReader(String[] filenames) {
      if (filenames == null) {
         throw new NullArgumentException("filenames");
      } else {
         this.filenames = filenames;
      }
   }

   public FilePropertyReader(String filename) {
      this(new String[]{filename});
   }

   protected InputStream getInputStream(String filename) throws IOException {
      File file = new File(filename);
      return new FileInputStream(file);
   }

   protected void loadProperties(Properties props, String filename) throws IOException {
      if (filename == null) {
         throw new NullArgumentException("filename");
      } else if (filename.equals("")) {
         throw new IllegalArgumentException("filename");
      } else {
         InputStream in = new BufferedInputStream(this.getInputStream(filename));
         props.load(in);
         in.close();
      }
   }

   public Map readProperties() throws PropertyException, IOException {
      Properties props = new Properties();

      for(int i = 0; i < this.filenames.length; ++i) {
         this.loadProperties(props, this.filenames[i]);
      }

      return props;
   }
}
