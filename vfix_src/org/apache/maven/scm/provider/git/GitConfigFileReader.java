package org.apache.maven.scm.provider.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

public class GitConfigFileReader {
   private File configDirectory;

   public File getConfigDirectory() {
      if (this.configDirectory == null) {
         File confDir = new File(".git");
         if (confDir.exists()) {
            this.configDirectory = confDir;
         } else {
            confDir = new File(".");
            if (confDir.exists()) {
               this.configDirectory = confDir;
            }
         }
      }

      return this.configDirectory;
   }

   public void setConfigDirectory(File configDirectory) {
      this.configDirectory = configDirectory;
   }

   public String getProperty(String group, String propertyName) {
      List<String> lines = this.getConfLines();
      boolean inGroup = false;
      Iterator i = lines.iterator();

      while(i.hasNext()) {
         String line = ((String)i.next()).trim();
         if (!inGroup) {
            if (("[" + group + "]").equals(line)) {
               inGroup = true;
            }
         } else {
            if (line.startsWith("[") && line.endsWith("]")) {
               return null;
            }

            if (!line.startsWith("#") && line.indexOf(61) >= 0) {
               String property = line.substring(0, line.indexOf(61)).trim();
               if (property.equals(propertyName)) {
                  String value = line.substring(line.indexOf(61) + 1);
                  return value.trim();
               }
            }
         }
      }

      return null;
   }

   private List<String> getConfLines() {
      List<String> lines = new ArrayList();
      BufferedReader reader = null;

      try {
         if (this.getConfigDirectory().exists()) {
            reader = new BufferedReader(new FileReader(new File(this.getConfigDirectory(), "config")));

            String line;
            while((line = reader.readLine()) != null) {
               if (!line.startsWith("#") && StringUtils.isNotEmpty(line)) {
                  lines.add(line);
               }
            }
         }
      } catch (IOException var7) {
         lines.clear();
      } finally {
         IOUtil.close((Reader)reader);
         reader = null;
      }

      return lines;
   }
}
