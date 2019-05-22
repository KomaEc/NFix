package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.ProjectComponent;

public class LineTokenizer extends ProjectComponent implements Tokenizer {
   private String lineEnd = "";
   private int pushed = -2;
   private boolean includeDelims = false;

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
         this.lineEnd = "";
         StringBuffer line = new StringBuffer();

         boolean state;
         for(state = false; ch != -1; ch = in.read()) {
            if (state) {
               state = false;
               if (ch == 10) {
                  this.lineEnd = "\r\n";
               } else {
                  this.pushed = ch;
                  this.lineEnd = "\r";
               }
               break;
            }

            if (ch == 13) {
               state = true;
            } else {
               if (ch == 10) {
                  this.lineEnd = "\n";
                  break;
               }

               line.append((char)ch);
            }
         }

         if (ch == -1 && state) {
            this.lineEnd = "\r";
         }

         if (this.includeDelims) {
            line.append(this.lineEnd);
         }

         return line.toString();
      }
   }

   public String getPostToken() {
      return this.includeDelims ? "" : this.lineEnd;
   }
}
