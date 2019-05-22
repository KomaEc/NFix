package org.apache.tools.ant.taskdefs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.LogLevel;
import org.apache.tools.ant.util.FileUtils;

public class Echo extends Task {
   protected String message = "";
   protected File file = null;
   protected boolean append = false;
   private String encoding = "";
   protected int logLevel = 1;

   public void execute() throws BuildException {
      if (this.file == null) {
         this.log(this.message, this.logLevel);
      } else {
         Object out = null;

         try {
            String filename = this.file.getAbsolutePath();
            if (this.encoding != null && this.encoding.length() != 0) {
               out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, this.append), this.encoding));
            } else {
               out = new FileWriter(filename, this.append);
            }

            ((Writer)out).write(this.message, 0, this.message.length());
         } catch (IOException var6) {
            throw new BuildException(var6, this.getLocation());
         } finally {
            FileUtils.close((Writer)out);
         }
      }

   }

   public void setMessage(String msg) {
      this.message = msg;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public void setAppend(boolean append) {
      this.append = append;
   }

   public void addText(String msg) {
      this.message = this.message + this.getProject().replaceProperties(msg);
   }

   public void setLevel(Echo.EchoLevel echoLevel) {
      this.logLevel = echoLevel.getLevel();
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public static class EchoLevel extends LogLevel {
   }
}
