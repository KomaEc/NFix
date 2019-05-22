package org.apache.tools.ant.util;

import java.io.File;

public class FlatFileNameMapper implements FileNameMapper {
   public void setFrom(String from) {
   }

   public void setTo(String to) {
   }

   public String[] mapFileName(String sourceFileName) {
      return new String[]{(new File(sourceFileName)).getName()};
   }
}
