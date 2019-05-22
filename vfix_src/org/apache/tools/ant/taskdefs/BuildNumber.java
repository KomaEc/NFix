package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

public class BuildNumber extends Task {
   private static final String DEFAULT_PROPERTY_NAME = "build.number";
   private static final String DEFAULT_FILENAME = "build.number";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File myFile;

   public void setFile(File file) {
      this.myFile = file;
   }

   public void execute() throws BuildException {
      File savedFile = this.myFile;
      this.validate();
      Properties properties = this.loadProperties();
      int buildNumber = this.getBuildNumber(properties);
      properties.put("build.number", String.valueOf(buildNumber + 1));
      FileOutputStream output = null;

      try {
         output = new FileOutputStream(this.myFile);
         String header = "Build Number for ANT. Do not edit!";
         properties.store(output, "Build Number for ANT. Do not edit!");
      } catch (IOException var14) {
         String message = "Error while writing " + this.myFile;
         throw new BuildException(message, var14);
      } finally {
         if (null != output) {
            try {
               output.close();
            } catch (IOException var13) {
               this.log("error closing output stream " + var13, 0);
            }
         }

         this.myFile = savedFile;
      }

      this.getProject().setNewProperty("build.number", String.valueOf(buildNumber));
   }

   private int getBuildNumber(Properties properties) throws BuildException {
      String buildNumber = properties.getProperty("build.number", "0").trim();

      try {
         return Integer.parseInt(buildNumber);
      } catch (NumberFormatException var5) {
         String message = this.myFile + " contains a non integer build number: " + buildNumber;
         throw new BuildException(message, var5);
      }
   }

   private Properties loadProperties() throws BuildException {
      FileInputStream input = null;

      Properties var3;
      try {
         Properties properties = new Properties();
         input = new FileInputStream(this.myFile);
         properties.load(input);
         var3 = properties;
      } catch (IOException var12) {
         throw new BuildException(var12);
      } finally {
         if (null != input) {
            try {
               input.close();
            } catch (IOException var11) {
               this.log("error closing input stream " + var11, 0);
            }
         }

      }

      return var3;
   }

   private void validate() throws BuildException {
      if (null == this.myFile) {
         this.myFile = FILE_UTILS.resolveFile(this.getProject().getBaseDir(), "build.number");
      }

      if (!this.myFile.exists()) {
         try {
            FILE_UTILS.createNewFile(this.myFile);
         } catch (IOException var3) {
            String message = this.myFile + " doesn't exist and new file can't be created.";
            throw new BuildException(message, var3);
         }
      }

      String message;
      if (!this.myFile.canRead()) {
         message = "Unable to read from " + this.myFile + ".";
         throw new BuildException(message);
      } else if (!this.myFile.canWrite()) {
         message = "Unable to write to " + this.myFile + ".";
         throw new BuildException(message);
      }
   }
}
