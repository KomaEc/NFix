package org.apache.tools.ant.util;

public class IdentityMapper implements FileNameMapper {
   public void setFrom(String from) {
   }

   public void setTo(String to) {
   }

   public String[] mapFileName(String sourceFileName) {
      return new String[]{sourceFileName};
   }
}
