package soot.JastAddJ;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class Unicode extends FilterReader {
   private static final int SIZE = 1024;
   private char[] buffer;
   private int pos;
   private int length;
   private int lookahead;
   private int numConsecutiveBackSlash;

   public Unicode(Reader in) {
      super(in);
      this.buffer = new char[1024];
      this.pos = 0;
      this.length = 0;
      this.lookahead = -1;
      this.numConsecutiveBackSlash = 0;

      try {
         this.next();
      } catch (IOException var3) {
      }

   }

   public Unicode(InputStream in) {
      this((Reader)(new InputStreamReader(in)));
   }

   private void refill() throws IOException {
      if (this.pos >= this.length) {
         this.pos = 0;
         int i = this.in.read(this.buffer, 0, 1024);
         this.length = i != -1 ? i : 0;
      }

   }

   private int next() throws IOException {
      int c = this.lookahead;
      this.refill();
      this.lookahead = this.pos >= this.length ? -1 : this.buffer[this.pos++];
      return c;
   }

   public int read() throws IOException {
      int current = this.next();
      if (current != 92) {
         this.numConsecutiveBackSlash = 0;
         return current;
      } else {
         boolean isEven = (this.numConsecutiveBackSlash & 1) == 0;
         if (isEven && this.lookahead == 117) {
            this.numConsecutiveBackSlash = 0;

            while(this.lookahead == 117) {
               this.next();
            }

            int result = 0;

            for(int i = 0; i < 4; ++i) {
               int c = this.next();
               int value = Character.digit((char)c, 16);
               if (value == -1) {
                  throw new Error("Invalid Unicode Escape");
               }

               result <<= 4;
               result += value;
            }

            return result;
         } else {
            ++this.numConsecutiveBackSlash;
            return current;
         }
      }
   }

   public int read(char[] cbuf, int off, int len) throws IOException {
      if (!this.ready()) {
         return -1;
      } else {
         len += off;

         for(int i = off; i < len; ++i) {
            while(this.pos < this.length && i < len - 1 && this.lookahead != 92) {
               if (this.lookahead < 0) {
                  return i - off;
               }

               cbuf[i++] = (char)this.lookahead;
               this.lookahead = this.buffer[this.pos++];
               this.numConsecutiveBackSlash = 0;
            }

            int c = this.read();
            if (c < 0) {
               return i - off;
            }

            cbuf[i] = (char)c;
         }

         return len - off;
      }
   }

   public boolean ready() throws IOException {
      return this.pos < this.length || super.ready();
   }
}
