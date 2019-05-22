package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.FileUtils;

public class ManifestTask extends Task {
   private Manifest nestedManifest = new Manifest();
   private File manifestFile;
   private ManifestTask.Mode mode = new ManifestTask.Mode();
   private String encoding;

   public ManifestTask() {
      this.mode.setValue("replace");
   }

   public void addConfiguredSection(Manifest.Section section) throws ManifestException {
      this.nestedManifest.addConfiguredSection(section);
   }

   public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
      this.nestedManifest.addConfiguredAttribute(attribute);
   }

   public void setFile(File f) {
      this.manifestFile = f;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public void setMode(ManifestTask.Mode m) {
      this.mode = m;
   }

   public void execute() throws BuildException {
      if (this.manifestFile == null) {
         throw new BuildException("the file attribute is required");
      } else {
         Manifest toWrite = Manifest.getDefaultManifest();
         Manifest current = null;
         BuildException error = null;
         if (this.manifestFile.exists()) {
            FileInputStream fis = null;
            InputStreamReader isr = null;

            try {
               fis = new FileInputStream(this.manifestFile);
               if (this.encoding == null) {
                  isr = new InputStreamReader(fis, "UTF-8");
               } else {
                  isr = new InputStreamReader(fis, this.encoding);
               }

               current = new Manifest(isr);
            } catch (ManifestException var22) {
               error = new BuildException("Existing manifest " + this.manifestFile + " is invalid", var22, this.getLocation());
            } catch (IOException var23) {
               error = new BuildException("Failed to read " + this.manifestFile, var23, this.getLocation());
            } finally {
               FileUtils.close((Reader)isr);
            }
         }

         Enumeration e = this.nestedManifest.getWarnings();

         while(e.hasMoreElements()) {
            this.log("Manifest warning: " + (String)e.nextElement(), 1);
         }

         try {
            if (this.mode.getValue().equals("update") && this.manifestFile.exists()) {
               if (current != null) {
                  toWrite.merge(current);
               } else if (error != null) {
                  throw error;
               }
            }

            toWrite.merge(this.nestedManifest);
         } catch (ManifestException var26) {
            throw new BuildException("Manifest is invalid", var26, this.getLocation());
         }

         if (toWrite.equals(current)) {
            this.log("Manifest has not changed, do not recreate", 3);
         } else {
            PrintWriter w = null;

            try {
               FileOutputStream fos = new FileOutputStream(this.manifestFile);
               OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
               w = new PrintWriter(osw);
               toWrite.write(w);
            } catch (IOException var21) {
               throw new BuildException("Failed to write " + this.manifestFile, var21, this.getLocation());
            } finally {
               if (w != null) {
                  w.close();
               }

            }

         }
      }
   }

   public static class Mode extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"update", "replace"};
      }
   }
}
