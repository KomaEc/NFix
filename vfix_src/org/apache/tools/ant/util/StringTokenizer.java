package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;

public class StringTokenizer extends ProjectComponent implements Tokenizer {
   private String intraString = "";
   private int pushed = -2;
   private char[] delims = null;
   private boolean delimsAreTokens = false;
   private boolean suppressDelims = false;
   private boolean includeDelims = false;

   public void setDelims(String delims) {
      this.delims = StringUtils.resolveBackSlash(delims).toCharArray();
   }

   public void setDelimsAreTokens(boolean delimsAreTokens) {
      this.delimsAreTokens = delimsAreTokens;
   }

   public void setSuppressDelims(boolean suppressDelims) {
      this.suppressDelims = suppressDelims;
   }

   public void setIncludeDelims(boolean includeDelims) {
      this.includeDelims = includeDelims;
   }

   public String getToken(Reader in) throws IOException {
      int ch = true;
      int ch;
      if (this.pushed != -2) {
         ch = this.pushed;
         this.pushed = -2;
      } else {
         ch = in.read();
      }

      if (ch == -1) {
         return null;
      } else {
         boolean inToken = true;
         this.intraString = "";
         StringBuffer word = new StringBuffer();

         StringBuffer padding;
         for(padding = new StringBuffer(); ch != -1; ch = in.read()) {
            char c = (char)ch;
            boolean isDelim = this.isDelim(c);
            if (inToken) {
               if (isDelim) {
                  if (this.delimsAreTokens) {
                     if (word.length() == 0) {
                        word.append(c);
                     } else {
                        this.pushed = ch;
                     }
                     break;
                  }

                  padding.append(c);
                  inToken = false;
               } else {
                  word.append(c);
               }
            } else {
               if (!isDelim) {
                  this.pushed = ch;
                  break;
               }

               padding.append(c);
            }
         }

         this.intraString = padding.toString();
         if (this.includeDelims) {
            word.append(this.intraString);
         }

         return word.toString();
      }
   }

   public String getPostToken() {
      return !this.suppressDelims && !this.includeDelims ? this.intraString : "";
   }

   private boolean isDelim(char ch) {
      if (this.delims == null) {
         return Character.isWhitespace(ch);
      } else {
         for(int i = 0; i < this.delims.length; ++i) {
            if (this.delims[i] == ch) {
               return true;
            }
         }

         return false;
      }
   }
}
