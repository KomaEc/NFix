package org.netbeans.lib.cvsclient.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.file.FileUtils;

public class StandardAdminHandler implements AdminHandler {
   private static final Object ksEntries = new Object();
   private static Runnable t9yBeforeRename;

   public void updateAdminData(String var1, String var2, Entry var3, GlobalOptions var4) throws IOException {
      File var5 = new File(var1, "CVS");
      var5.mkdirs();
      File var6 = new File(var5, "Root");
      if (!var6.exists()) {
         BufferedWriter var7 = new BufferedWriter(new FileWriter(var6));

         try {
            var7.write(var4.getCVSRoot());
         } finally {
            var7.close();
         }
      }

      File var24 = new File(var5, "Repository");
      if (!var24.exists()) {
         BufferedWriter var8 = new BufferedWriter(new FileWriter(var24));

         try {
            if (var3 != null && !var3.isDirectory()) {
               int var9 = var3.getName().length();
               var2 = var2.substring(0, var2.length() - var9);
            }

            if (var2.endsWith("/")) {
               var2 = var2.substring(0, var2.length() - 1);
            }

            if (var2.length() == 0) {
               var2 = ".";
            }

            var8.write(var2);
         } finally {
            var8.close();
         }
      }

      File var25 = new File(var5, "Entries");
      if (var25.createNewFile()) {
         this.addDirectoryToParentEntriesFile(var5);
         BufferedWriter var26 = new BufferedWriter(new FileWriter(var25));

         try {
            var26.write("D");
         } finally {
            var26.close();
         }
      }

      if (var3 != null) {
         this.updateEntriesFile(var25, var3);
      }

   }

   public boolean exists(File var1) {
      return var1.exists();
   }

   private void addDirectoryToParentEntriesFile(File var1) throws IOException {
      synchronized(ksEntries) {
         File var3 = seekEntries(var1.getParentFile().getParentFile());
         if (var3 != null) {
            File var4 = var3.getParentFile();
            File var5 = new File(var4, "Entries.Backup");
            var5.createNewFile();
            BufferedReader var6 = null;
            BufferedWriter var7 = null;

            try {
               var6 = new BufferedReader(new FileReader(var3));
               var7 = new BufferedWriter(new FileWriter(var5));
               boolean var8 = false;
               Entry var9 = new Entry();
               var9.setName(var1.getParentFile().getName());
               var9.setDirectory(true);

               label316:
               while(true) {
                  String var10;
                  do {
                     if ((var10 = var6.readLine()) == null) {
                        if (!var8) {
                           var7.write(var9.toString());
                           var7.newLine();
                        }
                        break label316;
                     }
                  } while(var10.trim().equals("D"));

                  Entry var11 = new Entry(var10);
                  if (var11.getName() != null && var11.getName().equals(var9.getName())) {
                     var7.write(var9.toString());
                     var8 = true;
                  } else {
                     var7.write(var10);
                  }

                  var7.newLine();
               }
            } finally {
               try {
                  if (var7 != null) {
                     var7.close();
                  }
               } finally {
                  if (var6 != null) {
                     var6.close();
                  }

               }

            }

            if (t9yBeforeRename != null) {
               t9yBeforeRename.run();
            }

            FileUtils.renameFile(var5, var3);
         }

      }
   }

   private void updateEntriesFile(File var1, Entry var2) throws IOException {
      synchronized(ksEntries) {
         File var4 = var1.getParentFile();
         File var5 = new File(var4, "Entries.Backup");
         var5.createNewFile();
         BufferedReader var6 = null;
         BufferedWriter var7 = null;

         try {
            var6 = new BufferedReader(new FileReader(var1));
            var7 = new BufferedWriter(new FileWriter(var5));
            boolean var8 = false;

            while(true) {
               String var9;
               if ((var9 = var6.readLine()) == null) {
                  if (!var8) {
                     var7.write(var2.toString());
                     var7.newLine();
                  }
                  break;
               }

               Entry var10 = new Entry(var9);
               if (var10.getName() != null && var10.getName().equals(var2.getName())) {
                  var7.write(var2.toString());
                  var8 = true;
               } else {
                  var7.write(var9);
               }

               var7.newLine();
            }
         } finally {
            try {
               if (var7 != null) {
                  var7.close();
               }
            } finally {
               if (var6 != null) {
                  var6.close();
               }

            }

         }

         if (t9yBeforeRename != null) {
            t9yBeforeRename.run();
         }

         FileUtils.renameFile(var5, var1);
      }
   }

   public Entry getEntry(File var1) throws IOException {
      File var2 = seekEntries(var1.getParentFile());
      if (var2 == null) {
         return null;
      } else {
         this.processEntriesDotLog(new File(var1.getParent(), "CVS"));
         BufferedReader var3 = null;
         Entry var4 = null;
         boolean var5 = false;

         try {
            var3 = new BufferedReader(new FileReader(var2));

            String var6;
            while(!var5 && (var6 = var3.readLine()) != null) {
               var4 = new Entry(var6);
               if (var4.getName() != null) {
                  var5 = var4.getName().equals(var1.getName());
               }
            }
         } finally {
            if (var3 != null) {
               var3.close();
            }

         }

         return !var5 ? null : var4;
      }
   }

   public Entry[] getEntriesAsArray(File var1) throws IOException {
      LinkedList var2 = new LinkedList();
      File var3 = seekEntries(var1);
      if (var3 == null) {
         return new Entry[0];
      } else {
         this.processEntriesDotLog(new File(var1, "CVS"));
         BufferedReader var4 = null;
         Entry var5 = null;

         try {
            var4 = new BufferedReader(new FileReader(var3));

            String var6;
            while((var6 = var4.readLine()) != null) {
               var5 = new Entry(var6);
               if (var5.getName() != null) {
                  var2.add(var5);
               }
            }
         } finally {
            if (var4 != null) {
               var4.close();
            }

         }

         Entry[] var10 = new Entry[var2.size()];
         var10 = (Entry[])var2.toArray(var10);
         return var10;
      }
   }

   public Iterator getEntries(File var1) throws IOException {
      LinkedList var2 = new LinkedList();
      File var3 = seekEntries(var1);
      if (var3 == null) {
         return var2.iterator();
      } else {
         this.processEntriesDotLog(new File(var1, "CVS"));
         BufferedReader var4 = null;
         Entry var5 = null;

         try {
            var4 = new BufferedReader(new FileReader(var3));

            String var6;
            while((var6 = var4.readLine()) != null) {
               var5 = new Entry(var6);
               if (var5.getName() != null) {
                  var2.add(var5);
               }
            }
         } finally {
            if (var4 != null) {
               var4.close();
            }

         }

         return var2.iterator();
      }
   }

   public void setEntry(File var1, Entry var2) throws IOException {
      String var3 = var1.getParent();
      File var4 = seekEntries(var3);
      if (var4 == null) {
         var4 = new File(var3, "CVS/Entries");
      }

      this.processEntriesDotLog(new File(var3, "CVS"));
      this.updateEntriesFile(var4, var2);
   }

   public void removeEntry(File var1) throws IOException {
      synchronized(ksEntries) {
         File var3 = seekEntries(var1.getParent());
         if (var3 != null) {
            this.processEntriesDotLog(new File(var1.getParent(), "CVS"));
            File var4 = var1.getParentFile();
            File var5 = new File(var4, "Entries.Backup");
            var5.createNewFile();
            BufferedReader var6 = null;
            BufferedWriter var7 = null;

            try {
               var6 = new BufferedReader(new FileReader(var3));
               var7 = new BufferedWriter(new FileWriter(var5));
               boolean var8 = false;

               label340:
               while(true) {
                  Entry var10;
                  do {
                     do {
                        String var9;
                        if ((var9 = var6.readLine()) == null) {
                           if (!var8) {
                              var7.write("D");
                              var7.newLine();
                           }
                           break label340;
                        }

                        var10 = new Entry(var9);
                     } while(var10.getName() == null);
                  } while(var10.getName().equals(var1.getName()));

                  var7.write(var10.toString());
                  var7.newLine();
                  var8 = var8 || var10.isDirectory();
               }
            } finally {
               try {
                  if (var7 != null) {
                     var7.close();
                  }
               } finally {
                  if (var6 != null) {
                     var6.close();
                  }

               }

            }

            if (t9yBeforeRename != null) {
               t9yBeforeRename.run();
            }

            FileUtils.renameFile(var5, var3);
         }
      }
   }

   public String getRepositoryForDirectory(String var1, String var2) throws IOException {
      File var3 = null;
      String var4 = "";

      for(File var5 = new File(var1); var5 != null && var5.getName().length() != 0 && var5.exists(); var5 = var5.getParentFile()) {
         var3 = new File(var5, "CVS/Repository");
         if (var3.exists()) {
            BufferedReader var6 = null;
            String var7 = null;

            try {
               var6 = new BufferedReader(new FileReader(var3));
               var7 = var6.readLine();
            } finally {
               if (var6 != null) {
                  var6.close();
               }

            }

            if (var7 == null) {
               var7 = "";
            }

            var7 = var7 + var4;
            if (var7.startsWith("/")) {
               return var7;
            }

            if (var7.startsWith("./")) {
               var7 = var7.substring(2);
            }

            return var2 + '/' + var7;
         }

         var4 = '/' + var5.getName() + var4;
      }

      throw new FileNotFoundException("Repository file not found for directory " + var1);
   }

   private void processEntriesDotLog(File var1) throws IOException {
      synchronized(ksEntries) {
         File var3 = new File(var1, "Entries.Log");
         if (var3.exists()) {
            BufferedReader var4 = new BufferedReader(new FileReader(var3));
            LinkedList var5 = new LinkedList();
            HashSet var6 = new HashSet();

            String var7;
            try {
               while((var7 = var4.readLine()) != null) {
                  Entry var8;
                  if (var7.startsWith("A ")) {
                     var8 = new Entry(var7.substring(2));
                     var5.add(var8);
                  } else if (var7.startsWith("R ")) {
                     var8 = new Entry(var7.substring(2));
                     var6.add(var8.getName());
                  }
               }
            } finally {
               var4.close();
            }

            if (var5.size() > 0 || var6.size() > 0) {
               File var41 = new File(var1, "Entries.Backup");
               BufferedWriter var9 = new BufferedWriter(new FileWriter(var41));
               File var10 = new File(var1, "Entries");
               var4 = new BufferedReader(new FileReader(var10));

               try {
                  int var11 = 0;

                  while((var7 = var4.readLine()) != null) {
                     if (!var7.trim().equals("D")) {
                        Entry var12 = new Entry(var7);
                        if (var12.isDirectory()) {
                           ++var11;
                        }

                        if (!var6.contains(var12.getName())) {
                           var9.write(var12.toString());
                           var9.newLine();
                           if (var12.isDirectory()) {
                              --var11;
                           }
                        }
                     }
                  }

                  Iterator var42 = var5.iterator();

                  while(var42.hasNext()) {
                     Entry var13 = (Entry)var42.next();
                     if (var13.isDirectory()) {
                        ++var11;
                     }

                     var9.write(var13.toString());
                     var9.newLine();
                  }

                  if (var11 == 0) {
                     var9.write("D");
                     var9.newLine();
                  }
               } finally {
                  try {
                     var4.close();
                  } finally {
                     var9.close();
                  }
               }

               if (t9yBeforeRename != null) {
                  t9yBeforeRename.run();
               }

               FileUtils.renameFile(var41, var10);
            }

            var3.delete();
         }
      }
   }

   public Set getAllFiles(File var1) throws IOException {
      TreeSet var2 = new TreeSet();
      BufferedReader var3 = null;

      try {
         File var4 = seekEntries(var1);
         if (var4 != null) {
            TreeSet var11 = var2;
            return var11;
         }

         var3 = new BufferedReader(new FileReader(var4));

         String var5;
         while((var5 = var3.readLine()) != null) {
            Entry var6 = new Entry(var5);
            if (var6.getName() != null) {
               File var7 = new File(var1, var6.getName());
               if (var7.isFile()) {
                  var2.add(var7);
               }
            }
         }
      } finally {
         if (var3 != null) {
            var3.close();
         }

      }

      return var2;
   }

   public String getStickyTagForDirectory(File var1) {
      BufferedReader var2 = null;
      File var3 = new File(var1, "CVS/Tag");

      try {
         var2 = new BufferedReader(new FileReader(var3));
         String var4 = var2.readLine();
         String var5 = var4;
         return var5;
      } catch (IOException var15) {
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var14) {
            }
         }

      }

      return null;
   }

   private static File seekEntries(File var0) {
      synchronized(ksEntries) {
         File var2 = new File(var0, "CVS/Entries");
         if (var2.exists()) {
            return var2;
         } else {
            File var3 = new File(var0, "CVS/Entries.Backup");
            if (var3.exists()) {
               File var10000;
               try {
                  if (t9yBeforeRename != null) {
                     t9yBeforeRename.run();
                  }

                  FileUtils.renameFile(var3, var2);
                  var10000 = var2;
               } catch (IOException var6) {
                  return null;
               }

               return var10000;
            } else {
               return null;
            }
         }
      }
   }

   private static File seekEntries(String var0) {
      return seekEntries(new File(var0));
   }

   static void t9yBeforeRenameSync(Runnable var0) {
      t9yBeforeRename = var0;
   }
}
