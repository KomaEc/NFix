package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.File;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.command.annotate.AnnotateCommand;
import org.netbeans.lib.cvsclient.command.annotate.AnnotateInformation;
import org.netbeans.lib.cvsclient.command.annotate.AnnotateLine;
import org.netbeans.lib.cvsclient.commandLine.GetOpt;
import org.netbeans.lib.cvsclient.commandLine.ListenerProvider;
import org.netbeans.lib.cvsclient.event.CVSAdapter;
import org.netbeans.lib.cvsclient.event.CVSListener;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;

public class locbundlecheck extends CVSAdapter implements CommandProvider {
   private PrintStream out;
   private PrintStream err;
   private int realEnd = 0;
   private HashMap originalBundles;
   private HashMap localizedBundles;
   private String local;
   private String workDir;

   public locbundlecheck() {
   }

   public String getName() {
      return "locbundlecheck";
   }

   public String[] getSynonyms() {
      return new String[]{"lbch", "lbcheck"};
   }

   public String getUsage() {
      return ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString("locbundlecheck.usage");
   }

   public void printShortDescription(PrintStream var1) {
      String var2 = ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString("locbundlecheck.shortDescription");
      var1.print(var2);
   }

   public void printLongDescription(PrintStream var1) {
      String var2 = ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString("locbundlecheck.longDescription");
      var1.println(var2);
   }

   public Command createCommand(String[] var1, int var2, GlobalOptions var3, String var4) {
      locbundlecheck.LocBundleAnnotateCommand var5 = new locbundlecheck.LocBundleAnnotateCommand();
      String var6 = var5.getOptString();
      GetOpt var7 = new GetOpt(var1, var6 + "i:");
      boolean var8 = true;
      var7.optIndexSet(var2);
      boolean var9 = false;
      String var10 = null;

      int var16;
      while((var16 = var7.getopt()) != -1) {
         if (var16 == 105) {
            var10 = var7.optArgGet();
            var5.setLocalization(var10);
         } else {
            boolean var11 = var5.setCVSCommand((char)var16, var7.optArgGet());
            if (!var11) {
               var9 = true;
            }
         }
      }

      if (!var9 && var10 != null) {
         int var17 = var7.optIndexGet();
         if (var17 < var1.length) {
            ArrayList var12 = new ArrayList();
            if (var4 == null) {
               var4 = System.getProperty("user.dir");
            }

            var5.setWorkDir(var4);
            File var13 = new File(var4);
            int var14 = var17;

            while(true) {
               if (var14 >= var1.length) {
                  if (var12.size() <= 0) {
                     throw new IllegalArgumentException(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.no_file_spec"));
                  }

                  File[] var18 = new File[var12.size()];
                  var18 = (File[])var12.toArray(var18);
                  var5.setFiles(var18);
                  break;
               }

               File var15 = new File(var13, var1[var14]);
               if (var15.exists() && var15.isDirectory()) {
                  addFilesInDir(var12, var15, var10);
               } else {
                  if (!var15.exists() || !var15.getName().endsWith(".properties")) {
                     throw new IllegalArgumentException();
                  }

                  addFiles(var12, var15, var10);
               }

               ++var14;
            }
         }

         return var5;
      } else {
         throw new IllegalArgumentException(this.getUsage());
      }
   }

   private static void addFiles(Collection var0, File var1, String var2) {
      String var3 = var1.getAbsolutePath();
      String var4 = var3.substring(0, var3.length() - ".properties".length()) + "_" + var2 + ".properties";
      File var5 = new File(var4);
      var0.add(var1);
      if (var5.exists()) {
         var0.add(var5);
      }

   }

   private static void addFilesInDir(Collection var0, File var1, String var2) {
      File[] var3 = var1.listFiles();
      if (var3 != null && var3.length > 0) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].exists() && var3[var4].isDirectory()) {
               addFilesInDir(var0, var3[var4], var2);
            } else if (var3[var4].exists() && "Bundle.properties".equals(var3[var4].getName())) {
               addFiles(var0, var3[var4], var2);
            }
         }
      }

   }

   locbundlecheck(PrintStream var1, PrintStream var2, String var3, String var4) {
      this.out = var1;
      this.err = var2;
      this.originalBundles = new HashMap();
      this.localizedBundles = new HashMap();
      this.local = var3;
      this.workDir = var4;
   }

   public void fileInfoGenerated(FileInfoEvent var1) {
      FileInfoContainer var2 = var1.getInfoContainer();
      if (var2.getFile().getName().indexOf("_" + this.local) >= 0) {
         this.localizedBundles.put(var2.getFile().getAbsolutePath(), var2);
      } else {
         this.originalBundles.put(var2.getFile().getAbsolutePath(), var2);
      }

      if (this.realEnd == 2) {
         this.generateOutput();
      }

   }

   public void commandTerminated(TerminationEvent var1) {
      if (this.realEnd == 0) {
         this.realEnd = 1;
      } else {
         this.realEnd = 2;
      }
   }

   private void generateOutput() {
      Iterator var1 = this.originalBundles.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         int var3 = var2.lastIndexOf(".");
         if (var3 < 0) {
            throw new IllegalStateException(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.illegal_state"));
         }

         String var4 = var2.substring(0, var3) + "_" + this.local + var2.substring(var3);
         AnnotateInformation var5 = (AnnotateInformation)this.originalBundles.get(var2);
         AnnotateInformation var6 = (AnnotateInformation)this.localizedBundles.get(var4);
         if (var6 == null) {
            this.out.println(MessageFormat.format(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.noLocalizedFile"), var2));
         } else {
            this.localizedBundles.remove(var4);
            HashMap var7 = this.createPropMap(var5);
            HashMap var8 = this.createPropMap(var6);
            String var9 = var2;
            if (var2.startsWith(this.workDir)) {
               var9 = var2.substring(this.workDir.length());
               if (var9.startsWith("/") || var9.startsWith("\\")) {
                  var9 = var9.substring(1);
               }
            }

            this.out.println(MessageFormat.format(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.File"), var9));
            Iterator var10 = var7.keySet().iterator();

            while(var10.hasNext()) {
               String var11 = (String)var10.next();
               AnnotateLine var12 = (AnnotateLine)var7.get(var11);
               AnnotateLine var13 = (AnnotateLine)var8.get(var11);
               if (var13 == null) {
                  this.out.println(MessageFormat.format(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.propMissing"), var11));
               } else if (var12.getDate().compareTo(var13.getDate()) > 0) {
                  this.out.println(MessageFormat.format(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.prop_updated"), var11));
               }
            }
         }
      }

      if (this.localizedBundles.size() > 0) {
         Iterator var14 = this.localizedBundles.keySet().iterator();

         while(var14.hasNext()) {
            String var15 = (String)var14.next();
            this.out.println(MessageFormat.format(ResourceBundle.getBundle("org/netbeans/lib/cvsclient/commandLine/command/Bundle").getString("locbundlecheck.prop_removed"), var15));
         }
      }

   }

   private HashMap createPropMap(AnnotateInformation var1) {
      HashMap var2 = new HashMap();
      AnnotateLine var3 = var1.getFirstLine();

      while(var3 != null) {
         String var4 = var3.getContent();
         if (var4.startsWith("#")) {
            var3 = var1.getNextLine();
         } else {
            int var5 = var4.indexOf(61);
            if (var5 > 0) {
               String var6 = var4.substring(0, var5);
               var2.put(var6, var3);
            }

            var3 = var1.getNextLine();
         }
      }

      return var2;
   }

   private static class LocBundleAnnotateCommand extends AnnotateCommand implements ListenerProvider {
      private String loc;
      private String workDir;

      private LocBundleAnnotateCommand() {
      }

      public CVSListener createCVSListener(PrintStream var1, PrintStream var2) {
         return new locbundlecheck(var1, var2, this.loc, this.workDir);
      }

      public void setLocalization(String var1) {
         this.loc = var1;
      }

      public void setWorkDir(String var1) {
         this.workDir = var1;
      }

      // $FF: synthetic method
      LocBundleAnnotateCommand(Object var1) {
         this();
      }
   }
}
