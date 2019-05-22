package soot.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EscapedReader extends FilterReader {
   private static final Logger logger = LoggerFactory.getLogger(EscapedReader.class);
   private StringBuffer mini = new StringBuffer();
   boolean nextF;
   int nextch = 0;

   public EscapedReader(Reader fos) {
      super(fos);
   }

   public int read() throws IOException {
      if (this.nextF) {
         this.nextF = false;
         return this.nextch;
      } else {
         int ch = super.read();
         if (ch != 92) {
            return ch;
         } else {
            this.mini = new StringBuffer();
            ch = super.read();
            if (ch != 117) {
               this.nextF = true;
               this.nextch = ch;
               return 92;
            } else {
               this.mini.append("\\u");

               while(this.mini.length() < 6) {
                  ch = super.read();
                  this.mini.append((char)ch);
               }

               ch = Integer.parseInt(this.mini.substring(2).toString(), 16);
               return ch;
            }
         }
      }
   }
}
