package org.apache.tools.ant;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.tools.ant.taskdefs.condition.Os;

public class PathTokenizer {
   private StringTokenizer tokenizer;
   private String lookahead = null;
   private boolean onNetWare = Os.isFamily("netware");
   private boolean dosStyleFilesystem;

   public PathTokenizer(String path) {
      if (this.onNetWare) {
         this.tokenizer = new StringTokenizer(path, ":;", true);
      } else {
         this.tokenizer = new StringTokenizer(path, ":;", false);
      }

      this.dosStyleFilesystem = File.pathSeparatorChar == ';';
   }

   public boolean hasMoreTokens() {
      return this.lookahead != null ? true : this.tokenizer.hasMoreTokens();
   }

   public String nextToken() throws NoSuchElementException {
      String token = null;
      if (this.lookahead != null) {
         token = this.lookahead;
         this.lookahead = null;
      } else {
         token = this.tokenizer.nextToken().trim();
      }

      String nextToken;
      if (!this.onNetWare) {
         if (token.length() == 1 && Character.isLetter(token.charAt(0)) && this.dosStyleFilesystem && this.tokenizer.hasMoreTokens()) {
            nextToken = this.tokenizer.nextToken().trim();
            if (!nextToken.startsWith("\\") && !nextToken.startsWith("/")) {
               this.lookahead = nextToken;
            } else {
               token = token + ":" + nextToken;
            }
         }
      } else {
         if (token.equals(File.pathSeparator) || token.equals(":")) {
            token = this.tokenizer.nextToken().trim();
         }

         if (this.tokenizer.hasMoreTokens()) {
            nextToken = this.tokenizer.nextToken().trim();
            if (!nextToken.equals(File.pathSeparator)) {
               if (nextToken.equals(":")) {
                  if (!token.startsWith("/") && !token.startsWith("\\") && !token.startsWith(".") && !token.startsWith("..")) {
                     String oneMore = this.tokenizer.nextToken().trim();
                     if (!oneMore.equals(File.pathSeparator)) {
                        token = token + ":" + oneMore;
                     } else {
                        token = token + ":";
                        this.lookahead = oneMore;
                     }
                  }
               } else {
                  this.lookahead = nextToken;
               }
            }
         }
      }

      return token;
   }
}
