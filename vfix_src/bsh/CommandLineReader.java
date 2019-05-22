package bsh;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

class CommandLineReader extends FilterReader {
   static final int normal = 0;
   static final int lastCharNL = 1;
   static final int sentSemi = 2;
   int state = 1;

   public CommandLineReader(Reader var1) {
      super(var1);
   }

   public int read() throws IOException {
      if (this.state == 2) {
         this.state = 1;
         return 10;
      } else {
         int var1;
         while((var1 = this.in.read()) == 13) {
         }

         if (var1 == 10) {
            if (this.state == 1) {
               var1 = 59;
               this.state = 2;
            } else {
               this.state = 1;
            }
         } else {
            this.state = 0;
         }

         return var1;
      }
   }

   public int read(char[] var1, int var2, int var3) throws IOException {
      int var4 = this.read();
      if (var4 == -1) {
         return -1;
      } else {
         var1[var2] = (char)var4;
         return 1;
      }
   }

   public static void main(String[] var0) throws Exception {
      CommandLineReader var1 = new CommandLineReader(new InputStreamReader(System.in));

      while(true) {
         System.out.println(var1.read());
      }
   }
}
