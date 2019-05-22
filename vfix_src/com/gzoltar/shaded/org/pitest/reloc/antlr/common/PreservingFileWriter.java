package com.gzoltar.shaded.org.pitest.reloc.antlr.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PreservingFileWriter extends FileWriter {
   protected File target_file;
   protected File tmp_file;

   public PreservingFileWriter(String var1) throws IOException {
      super(var1 + ".com.gzoltar.shaded.org.pitest.reloc.antlr.common.tmp");
      this.target_file = new File(var1);
      String var2 = this.target_file.getParent();
      if (var2 != null) {
         File var3 = new File(var2);
         if (!var3.exists()) {
            throw new IOException("destination directory of '" + var1 + "' doesn't exist");
         }

         if (!var3.canWrite()) {
            throw new IOException("destination directory of '" + var1 + "' isn't writeable");
         }
      }

      if (this.target_file.exists() && !this.target_file.canWrite()) {
         throw new IOException("cannot write to '" + var1 + "'");
      } else {
         this.tmp_file = new File(var1 + ".com.gzoltar.shaded.org.pitest.reloc.antlr.common.tmp");
      }
   }

   public void close() throws IOException {
      BufferedReader var1 = null;
      BufferedWriter var2 = null;

      try {
         super.close();
         char[] var3 = new char[1024];
         if (this.target_file.length() == this.tmp_file.length()) {
            char[] var4 = new char[1024];
            var1 = new BufferedReader(new FileReader(this.tmp_file));
            BufferedReader var5 = new BufferedReader(new FileReader(this.target_file));
            boolean var6 = true;

            label202:
            while(true) {
               if (var6) {
                  int var7 = var1.read(var3, 0, 1024);
                  int var8 = var5.read(var4, 0, 1024);
                  if (var7 != var8) {
                     var6 = false;
                  } else if (var7 != -1) {
                     int var9 = 0;

                     while(true) {
                        if (var9 >= var7) {
                           continue label202;
                        }

                        if (var3[var9] != var4[var9]) {
                           var6 = false;
                           continue label202;
                        }

                        ++var9;
                     }
                  }
               }

               var1.close();
               var5.close();
               var5 = null;
               var1 = null;
               if (var6) {
                  return;
               }
               break;
            }
         }

         var1 = new BufferedReader(new FileReader(this.tmp_file));
         var2 = new BufferedWriter(new FileWriter(this.target_file));

         while(true) {
            int var10 = var1.read(var3, 0, 1024);
            if (var10 == -1) {
               return;
            }

            var2.write(var3, 0, var10);
         }
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var22) {
            }
         }

         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var21) {
            }
         }

         if (this.tmp_file != null && this.tmp_file.exists()) {
            this.tmp_file.delete();
            this.tmp_file = null;
         }

      }
   }
}
