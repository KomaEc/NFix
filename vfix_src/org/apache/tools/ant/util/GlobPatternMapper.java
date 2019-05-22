package org.apache.tools.ant.util;

public class GlobPatternMapper implements FileNameMapper {
   protected String fromPrefix = null;
   protected String fromPostfix = null;
   protected int prefixLength;
   protected int postfixLength;
   protected String toPrefix = null;
   protected String toPostfix = null;
   private boolean handleDirSep = false;
   private boolean caseSensitive = true;

   public void setHandleDirSep(boolean handleDirSep) {
      this.handleDirSep = handleDirSep;
   }

   public void setCaseSensitive(boolean caseSensitive) {
      this.caseSensitive = caseSensitive;
   }

   public void setFrom(String from) {
      int index = from.lastIndexOf("*");
      if (index == -1) {
         this.fromPrefix = from;
         this.fromPostfix = "";
      } else {
         this.fromPrefix = from.substring(0, index);
         this.fromPostfix = from.substring(index + 1);
      }

      this.prefixLength = this.fromPrefix.length();
      this.postfixLength = this.fromPostfix.length();
   }

   public void setTo(String to) {
      int index = to.lastIndexOf("*");
      if (index == -1) {
         this.toPrefix = to;
         this.toPostfix = "";
      } else {
         this.toPrefix = to.substring(0, index);
         this.toPostfix = to.substring(index + 1);
      }

   }

   public String[] mapFileName(String sourceFileName) {
      return this.fromPrefix != null && this.modifyName(sourceFileName).startsWith(this.modifyName(this.fromPrefix)) && this.modifyName(sourceFileName).endsWith(this.modifyName(this.fromPostfix)) ? new String[]{this.toPrefix + this.extractVariablePart(sourceFileName) + this.toPostfix} : null;
   }

   protected String extractVariablePart(String name) {
      return name.substring(this.prefixLength, name.length() - this.postfixLength);
   }

   private String modifyName(String name) {
      if (!this.caseSensitive) {
         name = name.toLowerCase();
      }

      if (this.handleDirSep && name.indexOf(92) != -1) {
         name = name.replace('\\', '/');
      }

      return name;
   }
}
