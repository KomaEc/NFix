package com.gzoltar.client.minimize;

import com.gzoltar.client.Properties;
import com.gzoltar.client.trie.Trie;
import com.gzoltar.client.trie.TrieNode;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.instrumentation.testing.TestResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MinionWrapper {
   private final Trie tr = new Trie();
   private final String inputFile;
   private final String outputFile;

   public MinionWrapper() throws IOException {
      File var1;
      (var1 = File.createTempFile("minion", ".input")).deleteOnExit();
      this.inputFile = var1.getAbsolutePath();
      (var1 = File.createTempFile("minion", ".output")).deleteOnExit();
      this.outputFile = var1.getAbsolutePath();
   }

   public boolean createMinionInputFile(Spectra var1) {
      int var2 = var1.getNumberOfTestResults();
      StringBuilder var3;
      (var3 = new StringBuilder()).append("MINION 3\n\n");
      var3.append("**VARIABLES**\n\n");

      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var3.append("BOOL t" + var4 + "\n");
      }

      var3.append("\n**SEARCH**\n\n");
      var3.append("VARORDER [");

      for(var4 = 0; var4 < var2; ++var4) {
         var3.append("t" + var4);
         if (var4 != var2 - 1) {
            var3.append(",");
         }
      }

      var3.append("]\n");
      var3.append("PRINT ALL\n\n");
      var3.append("**CONSTRAINTS**\n\n");
      Iterator var18 = var1.getComponents().iterator();

      while(var18.hasNext()) {
         ComponentCount var17 = (ComponentCount)var18.next();
         StringBuilder var5;
         (var5 = new StringBuilder()).append("watched-or({");
         int var6 = 0;

         for(Iterator var7 = var1.getTestResults().iterator(); var7.hasNext(); ++var6) {
            if (((TestResult)var7.next()).covers(var17)) {
               if (!var5.toString().equals("watched-or({")) {
                  var5.append(",");
               }

               var5.append("eq(t" + var6 + ",1)");
            }
         }

         var5.append("})");
         if (!var5.toString().equals("watched-or({})")) {
            var3.append(var5 + "\n");
         }
      }

      var3.append("\n**EOF**");
      Logger.getInstance().debug(var3.toString());
      BufferedWriter var19 = null;

      try {
         (var19 = new BufferedWriter(new FileWriter(this.inputFile))).write(var3.toString());
         return true;
      } catch (IOException var15) {
         Logger.getInstance().err("", var15);
      } finally {
         try {
            var19.close();
         } catch (IOException var14) {
            Logger.getInstance().err("", var14);
            return false;
         }
      }

      return false;
   }

   public boolean run() {
      Runtime var1 = Runtime.getRuntime();
      Process var2 = null;

      try {
         String var3;
         if (!(var3 = System.getProperty("os.name").toLowerCase()).contains("linux") && !var3.contains("mac")) {
            if (var3.contains("windows")) {
               String var8 = "";
               if (Properties.MINION_NUM_SOLUTIONS == -1) {
                  var8 = var8 + "-findallsols ";
               } else {
                  var8 = var8 + "-sollimit " + Integer.toString(Properties.MINION_NUM_SOLUTIONS) + " ";
               }

               if (Properties.MINION_TIMEOUT != -1) {
                  var8 = var8 + "-timelimit " + Integer.toString(Properties.MINION_TIMEOUT) + " ";
               }

               String[] var10 = new String[]{"cmd.exe", "/c", "start /min " + Properties.MINION_EXEC + " -noprintsols -noresume " + var8 + this.inputFile + " -solsout " + this.outputFile};
               Logger.getInstance().debug(var10.toString());
               var2 = var1.exec(var10);
               BufferedReader var6 = new BufferedReader(new InputStreamReader(var2.getInputStream()));
               BufferedReader var9 = new BufferedReader(new InputStreamReader(var2.getErrorStream()));

               String var4;
               while((var4 = var6.readLine()) != null) {
                  Logger.getInstance().debug(var4);
               }

               while((var4 = var9.readLine()) != null) {
                  Logger.getInstance().debug(var4);
               }
            }
         } else {
            ArrayList var7;
            (var7 = new ArrayList()).add(Properties.MINION_EXEC);
            var7.add("-noprintsols");
            var7.add("-noresume");
            if (Properties.MINION_NUM_SOLUTIONS == -1) {
               var7.add("-findallsols");
            } else {
               var7.add("-sollimit");
               var7.add(Integer.toString(Properties.MINION_NUM_SOLUTIONS));
            }

            if (Properties.MINION_TIMEOUT != -1) {
               var7.add("-timelimit");
               var7.add(Integer.toString(Properties.MINION_TIMEOUT));
            }

            var7.add(this.inputFile);
            var7.add("-solsout");
            var7.add(this.outputFile);
            Logger.getInstance().debug(var7.toString());
            var2 = var1.exec((String[])var7.toArray(new String[var7.size()]));
         }

         if (var2.waitFor() != 0) {
            throw new InterruptedException("Something went wrong. Please check the input file");
         }
      } catch (InterruptedException | IOException var5) {
         Logger.getInstance().err("", var5);
      }

      return true;
   }

   public List<Set<Integer>> getMinionOutput() {
      ArrayList var1 = new ArrayList();

      try {
         BufferedReader var2 = new BufferedReader(new FileReader(this.outputFile));

         String var3;
         while((var3 = var2.readLine()) != null) {
            String[] var8 = var3.split(" ");
            StringBuilder var4 = new StringBuilder("");
            HashSet var5 = new HashSet();

            for(int var6 = 0; var6 < var8.length; ++var6) {
               if (var8[var6].compareTo("1") == 0) {
                  var5.add(var6);
                  var4.append(var6 + 1 + ",");
               }
            }

            String var9 = (var9 = var4.toString()).substring(0, var9.length() - 1);
            if (this.tr.insert(new TrieNode(var9))) {
               Logger.getInstance().debug(var9 + " considered as a solution");
               var1.add(var5);
            }
         }

         var2.close();
         return var1;
      } catch (IOException var7) {
         Logger.getInstance().err("", var7);
         return null;
      }
   }
}
