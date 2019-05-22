package org.codehaus.groovy.antlr;

import java.util.ArrayList;
import java.util.List;

public class SourceBuffer {
   private final List lines = new ArrayList();
   private StringBuffer current = new StringBuffer();

   public SourceBuffer() {
      this.lines.add(this.current);
   }

   public String getSnippet(LineColumn start, LineColumn end) {
      if (start != null && end != null) {
         if (start.equals(end)) {
            return null;
         } else if (this.lines.size() == 1 && this.current.length() == 0) {
            return null;
         } else {
            int startLine = start.getLine();
            int startColumn = start.getColumn();
            int endLine = end.getLine();
            int endColumn = end.getColumn();
            if (startLine < 1) {
               startLine = 1;
            }

            if (endLine < 1) {
               endLine = 1;
            }

            if (startColumn < 1) {
               startColumn = 1;
            }

            if (endColumn < 1) {
               endColumn = 1;
            }

            if (startLine > this.lines.size()) {
               startLine = this.lines.size();
            }

            if (endLine > this.lines.size()) {
               endLine = this.lines.size();
            }

            StringBuffer snippet = new StringBuffer();

            for(int i = startLine - 1; i < endLine; ++i) {
               String line = ((StringBuffer)this.lines.get(i)).toString();
               if (startLine == endLine) {
                  if (startColumn > line.length()) {
                     startColumn = line.length();
                  }

                  if (startColumn < 1) {
                     startColumn = 1;
                  }

                  if (endColumn > line.length()) {
                     endColumn = line.length() + 1;
                  }

                  if (endColumn < 1) {
                     endColumn = 1;
                  }

                  line = line.substring(startColumn - 1, endColumn - 1);
               } else {
                  if (i == startLine - 1 && startColumn - 1 < line.length()) {
                     line = line.substring(startColumn - 1);
                  }

                  if (i == endLine - 1 && endColumn - 1 < line.length()) {
                     line = line.substring(0, endColumn - 1);
                  }
               }

               snippet.append(line);
            }

            return snippet.toString();
         }
      } else {
         return null;
      }
   }

   public void write(int c) {
      if (c != -1) {
         this.current.append((char)c);
      }

      if (c == 10) {
         this.current = new StringBuffer();
         this.lines.add(this.current);
      }

   }
}
