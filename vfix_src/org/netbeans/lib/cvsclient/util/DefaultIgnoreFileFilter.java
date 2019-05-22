package org.netbeans.lib.cvsclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class DefaultIgnoreFileFilter implements IgnoreFileFilter {
   private final List patterns = new LinkedList();
   private final List localPatterns = new LinkedList();
   private boolean processGlobalPatterns = true;
   private boolean processLocalPatterns = false;
   private File lastDirectory = null;

   public DefaultIgnoreFileFilter() {
   }

   public DefaultIgnoreFileFilter(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = var2.next().toString();
         SimpleStringPattern var4 = new SimpleStringPattern(var3);
         this.addPattern((StringPattern)var4);
      }

   }

   public void addPattern(StringPattern var1) {
      if (var1.toString().equals("!")) {
         this.clearPatterns();
      } else {
         this.patterns.add(var1);
      }

   }

   public void addPattern(String var1) {
      if (var1.equals("!")) {
         this.clearPatterns();
      } else {
         this.patterns.add(new SimpleStringPattern(var1));
      }

   }

   public void clearPatterns() {
      this.patterns.clear();
   }

   public boolean shouldBeIgnored(File var1, String var2) {
      if (this.lastDirectory != var1) {
         this.lastDirectory = var1;
         this.processGlobalPatterns = true;
         this.processLocalPatterns = false;
         this.localPatterns.clear();
         String var3 = var1.getPath() + File.separator + ".cvsignore";
         File var4 = new File(var3);
         if (var4.exists()) {
            try {
               List var5 = parseCvsIgnoreFile(var4);
               Iterator var6 = var5.iterator();

               while(var6.hasNext()) {
                  String var7 = var6.next().toString();
                  if (var7.equals("!")) {
                     this.processGlobalPatterns = false;
                     this.localPatterns.clear();
                  } else {
                     this.localPatterns.add(new SimpleStringPattern(var7));
                  }
               }
            } catch (IOException var8) {
            }
         }

         this.processLocalPatterns = this.localPatterns.size() > 0;
      }

      Iterator var9;
      StringPattern var10;
      if (this.processGlobalPatterns) {
         var9 = this.patterns.iterator();

         while(var9.hasNext()) {
            var10 = (StringPattern)var9.next();
            if (var10.doesMatch(var2)) {
               return true;
            }
         }
      }

      if (this.processLocalPatterns) {
         var9 = this.localPatterns.iterator();

         while(var9.hasNext()) {
            var10 = (StringPattern)var9.next();
            if (var10.doesMatch(var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public static List parseCvsIgnoreFile(File var0) throws IOException, FileNotFoundException {
      BufferedReader var1 = null;
      LinkedList var2 = new LinkedList();

      try {
         var1 = new BufferedReader(new FileReader(var0));

         String var3;
         while((var3 = var1.readLine()) != null) {
            StringTokenizer var4 = new StringTokenizer(var3, " ", false);

            while(var4.hasMoreTokens()) {
               String var5 = var4.nextToken();
               var2.add(var5);
            }
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }

      }

      return var2;
   }
}
