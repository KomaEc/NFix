package org.apache.tools.ant.util;

public class MergingMapper implements FileNameMapper {
   protected String[] mergedFile = null;

   public void setFrom(String from) {
   }

   public void setTo(String to) {
      this.mergedFile = new String[]{to};
   }

   public String[] mapFileName(String sourceFileName) {
      return this.mergedFile;
   }
}
