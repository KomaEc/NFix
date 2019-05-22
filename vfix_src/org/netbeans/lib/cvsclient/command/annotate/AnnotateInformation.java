package org.netbeans.lib.cvsclient.command.annotate;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class AnnotateInformation extends FileInfoContainer {
   private File file;
   private List linesList;
   private Iterator iterator;
   private File tempFile;
   private File tempDir;
   private BufferedOutputStream tempOutStream;

   public AnnotateInformation() {
      this.tempDir = null;
   }

   public AnnotateInformation(File var1) {
      this.tempDir = var1;
   }

   public File getFile() {
      return this.file;
   }

   public void setFile(File var1) {
      this.file = var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(30);
      var1.append("\nFile: " + (this.file != null ? this.file.getAbsolutePath() : "null"));
      return var1.toString();
   }

   public AnnotateLine createAnnotateLine() {
      return new AnnotateLine();
   }

   public void addLine(AnnotateLine var1) {
      this.linesList.add(var1);
   }

   public AnnotateLine getFirstLine() {
      if (this.linesList == null) {
         this.linesList = this.createLinesList();
      }

      this.iterator = this.linesList.iterator();
      return this.getNextLine();
   }

   public AnnotateLine getNextLine() {
      if (this.iterator == null) {
         return null;
      } else {
         return !this.iterator.hasNext() ? null : (AnnotateLine)this.iterator.next();
      }
   }

   protected void addToTempFile(String var1) throws IOException {
      if (this.tempOutStream == null) {
         try {
            this.tempFile = File.createTempFile("ann", ".cvs", this.tempDir);
            this.tempFile.deleteOnExit();
            this.tempOutStream = new BufferedOutputStream(new FileOutputStream(this.tempFile));
         } catch (IOException var3) {
         }
      }

      this.tempOutStream.write(var1.getBytes());
      this.tempOutStream.write(10);
   }

   protected void closeTempFile() throws IOException {
      if (this.tempOutStream != null) {
         try {
            this.tempOutStream.flush();
         } finally {
            this.tempOutStream.close();
         }

      }
   }

   public File getTempFile() {
      return this.tempFile;
   }

   private List createLinesList() {
      LinkedList var1 = new LinkedList();
      BufferedReader var2 = null;
      if (this.tempFile == null) {
         return var1;
      } else {
         try {
            var2 = new BufferedReader(new FileReader(this.tempFile));
            String var3 = var2.readLine();

            for(int var4 = 1; var3 != null; var3 = var2.readLine()) {
               AnnotateLine var5 = AnnotateBuilder.processLine(var3);
               if (var5 != null) {
                  var5.setLineNum(var4);
                  var1.add(var5);
                  ++var4;
               }
            }
         } catch (IOException var15) {
         } finally {
            try {
               if (var2 != null) {
                  var2.close();
               }
            } catch (IOException var14) {
            }

         }

         return var1;
      }
   }
}
