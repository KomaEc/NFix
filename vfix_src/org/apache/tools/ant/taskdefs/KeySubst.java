package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/** @deprecated */
public class KeySubst extends Task {
   private File source = null;
   private File dest = null;
   private String sep = "*";
   private Hashtable replacements = new Hashtable();

   public void execute() throws BuildException {
      this.log("!! KeySubst is deprecated. Use Filter + Copy instead. !!");
      this.log("Performing Substitutions");
      if (this.source != null && this.dest != null) {
         BufferedReader br = null;
         BufferedWriter bw = null;

         try {
            br = new BufferedReader(new FileReader(this.source));
            this.dest.delete();
            bw = new BufferedWriter(new FileWriter(this.dest));
            String line = null;
            String newline = null;

            for(line = br.readLine(); line != null; line = br.readLine()) {
               if (line.length() == 0) {
                  bw.newLine();
               } else {
                  newline = replace(line, this.replacements);
                  bw.write(newline);
                  bw.newLine();
               }
            }

            bw.flush();
         } catch (IOException var17) {
            var17.printStackTrace();
         } finally {
            if (bw != null) {
               try {
                  bw.close();
               } catch (IOException var16) {
               }
            }

            if (br != null) {
               try {
                  br.close();
               } catch (IOException var15) {
               }
            }

         }

      } else {
         this.log("Source and destinations must not be null");
      }
   }

   public void setSrc(File s) {
      this.source = s;
   }

   public void setDest(File dest) {
      this.dest = dest;
   }

   public void setSep(String sep) {
      this.sep = sep;
   }

   public void setKeys(String keys) {
      if (keys != null && keys.length() > 0) {
         StringTokenizer tok = new StringTokenizer(keys, this.sep, false);

         while(tok.hasMoreTokens()) {
            String token = tok.nextToken().trim();
            StringTokenizer itok = new StringTokenizer(token, "=", false);
            String name = itok.nextToken();
            String value = itok.nextToken();
            this.replacements.put(name, value);
         }
      }

   }

   public static void main(String[] args) {
      try {
         Hashtable hash = new Hashtable();
         hash.put("VERSION", "1.0.3");
         hash.put("b", "ffff");
         System.out.println(replace("$f ${VERSION} f ${b} jj $", hash));
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static String replace(String origString, Hashtable keys) throws BuildException {
      StringBuffer finalString = new StringBuffer();
      int index = false;
      int i = 0;

      int index;
      for(String key = null; (index = origString.indexOf("${", i)) > -1; i = index + 3 + key.length()) {
         key = origString.substring(index + 2, origString.indexOf("}", index + 3));
         finalString.append(origString.substring(i, index));
         if (keys.containsKey(key)) {
            finalString.append(keys.get(key));
         } else {
            finalString.append("${");
            finalString.append(key);
            finalString.append("}");
         }
      }

      finalString.append(origString.substring(i));
      return finalString.toString();
   }
}
