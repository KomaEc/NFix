package org.apache.maven.doxia.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import org.codehaus.plexus.util.IOUtil;

public class LineBreaker {
   public static final int DEFAULT_MAX_LINE_LENGTH = 78;
   private static final String EOL = System.getProperty("line.separator");
   private Writer destination;
   private BufferedWriter writer;
   private int maxLineLength;
   private int lineLength;
   private StringBuffer word;

   public LineBreaker(Writer out) {
      this(out, 78);
   }

   public LineBreaker(Writer out, int max) {
      this.lineLength = 0;
      this.word = new StringBuffer(1024);
      if (max <= 0) {
         throw new IllegalArgumentException("maxLineLength <= 0");
      } else {
         this.destination = out;
         this.maxLineLength = max;
         this.writer = new BufferedWriter(out);
      }
   }

   public Writer getDestination() {
      return this.destination;
   }

   public void write(String text) throws IOException {
      this.write(text, false);
   }

   public void write(String text, boolean preserveSpace) {
      int length = text.length();

      try {
         for(int i = 0; i < length; ++i) {
            char c = text.charAt(i);
            String os = System.getProperty("os.name").toLowerCase();
            switch(c) {
            case '\r':
               if (os.indexOf("windows") != -1) {
                  break;
               }
            case '\n':
               this.writeWord();
               this.writer.write(EOL);
               this.lineLength = 0;
               break;
            case ' ':
               if (preserveSpace) {
                  this.word.append(c);
               } else {
                  this.writeWord();
               }
               break;
            default:
               this.word.append(c);
            }
         }
      } catch (Exception var7) {
      }

   }

   public void flush() {
      try {
         this.writeWord();
         this.writer.flush();
      } catch (IOException var2) {
      }

   }

   private void writeWord() throws IOException {
      int length = this.word.length();
      if (length > 0) {
         if (this.lineLength > 0) {
            if (this.lineLength + 1 + length > this.maxLineLength) {
               this.writer.write(EOL);
               this.lineLength = 0;
            } else {
               this.writer.write(32);
               ++this.lineLength;
            }
         }

         this.writer.write(this.word.toString());
         this.word.setLength(0);
         this.lineLength += length;
      }

   }

   public void close() {
      IOUtil.close((Writer)this.writer);
   }
}
