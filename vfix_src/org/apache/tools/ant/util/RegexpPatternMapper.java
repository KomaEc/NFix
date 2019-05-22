package org.apache.tools.ant.util;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;

public class RegexpPatternMapper implements FileNameMapper {
   protected RegexpMatcher reg = null;
   protected char[] to = null;
   protected StringBuffer result = new StringBuffer();
   private boolean handleDirSep = false;
   private int regexpOptions = 0;

   public RegexpPatternMapper() throws BuildException {
      this.reg = (new RegexpMatcherFactory()).newRegexpMatcher();
   }

   public void setHandleDirSep(boolean handleDirSep) {
      this.handleDirSep = handleDirSep;
   }

   public void setCaseSensitive(boolean caseSensitive) {
      if (!caseSensitive) {
         this.regexpOptions = 256;
      } else {
         this.regexpOptions = 0;
      }

   }

   public void setFrom(String from) throws BuildException {
      try {
         this.reg.setPattern(from);
      } catch (NoClassDefFoundError var3) {
         throw new BuildException("Cannot load regular expression matcher", var3);
      }
   }

   public void setTo(String to) {
      this.to = to.toCharArray();
   }

   public String[] mapFileName(String sourceFileName) {
      if (this.handleDirSep && sourceFileName.indexOf("\\") != -1) {
         sourceFileName = sourceFileName.replace('\\', '/');
      }

      return this.reg != null && this.to != null && this.reg.matches(sourceFileName, this.regexpOptions) ? new String[]{this.replaceReferences(sourceFileName)} : null;
   }

   protected String replaceReferences(String source) {
      Vector v = this.reg.getGroups(source, this.regexpOptions);
      this.result.setLength(0);

      for(int i = 0; i < this.to.length; ++i) {
         if (this.to[i] == '\\') {
            ++i;
            if (i < this.to.length) {
               int value = Character.digit(this.to[i], 10);
               if (value > -1) {
                  this.result.append((String)v.elementAt(value));
               } else {
                  this.result.append(this.to[i]);
               }
            } else {
               this.result.append('\\');
            }
         } else {
            this.result.append(this.to[i]);
         }
      }

      return this.result.substring(0);
   }
}
