package org.apache.maven.doxia.macro.snippet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SnippetReader {
   private static final String EOL = System.getProperty("line.separator");
   private URL source;

   public SnippetReader(URL src) {
      this.source = src;
   }

   public StringBuffer readSnippet(String snippetId) throws IOException {
      List lines = this.readLines(snippetId);
      int minIndent = this.minIndent(lines);
      StringBuffer result = new StringBuffer();
      Iterator iterator = lines.iterator();

      while(iterator.hasNext()) {
         String line = (String)iterator.next();
         result.append(line.substring(minIndent));
         result.append(EOL);
      }

      return result;
   }

   int minIndent(List lines) {
      int minIndent = Integer.MAX_VALUE;

      String line;
      for(Iterator iterator = lines.iterator(); iterator.hasNext(); minIndent = Math.min(minIndent, this.indent(line))) {
         line = (String)iterator.next();
      }

      return minIndent;
   }

   int indent(String line) {
      char[] chars = line.toCharArray();

      int indent;
      for(indent = 0; indent < chars.length && chars[indent] == ' '; ++indent) {
      }

      return indent;
   }

   private List readLines(String snippetId) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(this.source.openStream()));
      ArrayList lines = new ArrayList();

      try {
         boolean capture = false;

         String line;
         while((line = reader.readLine()) != null) {
            if (this.isStart(snippetId, line)) {
               capture = true;
            } else {
               if (this.isEnd(snippetId, line)) {
                  break;
               }

               if (capture) {
                  lines.add(line);
               }
            }
         }
      } finally {
         reader.close();
      }

      return lines;
   }

   protected boolean isStart(String snippetId, String line) {
      return this.isDemarcator(snippetId, "START", line);
   }

   protected boolean isDemarcator(String snippetId, String what, String line) {
      String upper = line.toUpperCase();
      return upper.indexOf(what.toUpperCase()) != -1 && upper.indexOf("SNIPPET") != -1 && line.indexOf(snippetId) != -1;
   }

   protected boolean isEnd(String snippetId, String line) {
      return this.isDemarcator(snippetId, "END", line);
   }
}
