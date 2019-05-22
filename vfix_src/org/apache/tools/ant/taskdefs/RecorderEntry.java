package org.apache.tools.ant.taskdefs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.util.StringUtils;

public class RecorderEntry implements BuildLogger, SubBuildListener {
   private String filename = null;
   private boolean record = true;
   private int loglevel = 2;
   private PrintStream out = null;
   private long targetStartTime = 0L;
   private boolean emacsMode = false;
   private Project project;

   protected RecorderEntry(String name) {
      this.targetStartTime = System.currentTimeMillis();
      this.filename = name;
   }

   public String getFilename() {
      return this.filename;
   }

   public void setRecordState(Boolean state) {
      if (state != null) {
         this.flush();
         this.record = state;
      }

   }

   public void buildStarted(BuildEvent event) {
      this.log("> BUILD STARTED", 4);
   }

   public void buildFinished(BuildEvent event) {
      this.log("< BUILD FINISHED", 4);
      if (this.record && this.out != null) {
         Throwable error = event.getException();
         if (error == null) {
            this.out.println(StringUtils.LINE_SEP + "BUILD SUCCESSFUL");
         } else {
            this.out.println(StringUtils.LINE_SEP + "BUILD FAILED" + StringUtils.LINE_SEP);
            error.printStackTrace(this.out);
         }
      }

      this.cleanup();
   }

   public void subBuildFinished(BuildEvent event) {
      if (event.getProject() == this.project) {
         this.cleanup();
      }

   }

   public void subBuildStarted(BuildEvent event) {
   }

   public void targetStarted(BuildEvent event) {
      this.log(">> TARGET STARTED -- " + event.getTarget(), 4);
      this.log(StringUtils.LINE_SEP + event.getTarget().getName() + ":", 2);
      this.targetStartTime = System.currentTimeMillis();
   }

   public void targetFinished(BuildEvent event) {
      this.log("<< TARGET FINISHED -- " + event.getTarget(), 4);
      String time = formatTime(System.currentTimeMillis() - this.targetStartTime);
      this.log(event.getTarget() + ":  duration " + time, 3);
      this.flush();
   }

   public void taskStarted(BuildEvent event) {
      this.log(">>> TASK STARTED -- " + event.getTask(), 4);
   }

   public void taskFinished(BuildEvent event) {
      this.log("<<< TASK FINISHED -- " + event.getTask(), 4);
      this.flush();
   }

   public void messageLogged(BuildEvent event) {
      this.log("--- MESSAGE LOGGED", 4);
      StringBuffer buf = new StringBuffer();
      if (event.getTask() != null) {
         String name = event.getTask().getTaskName();
         if (!this.emacsMode) {
            String label = "[" + name + "] ";
            int size = 12 - label.length();

            for(int i = 0; i < size; ++i) {
               buf.append(" ");
            }

            buf.append(label);
         }
      }

      buf.append(event.getMessage());
      this.log(buf.toString(), event.getPriority());
   }

   private void log(String mesg, int level) {
      if (this.record && level <= this.loglevel && this.out != null) {
         this.out.println(mesg);
      }

   }

   private void flush() {
      if (this.record && this.out != null) {
         this.out.flush();
      }

   }

   public void setMessageOutputLevel(int level) {
      if (level >= 0 && level <= 4) {
         this.loglevel = level;
      }

   }

   public void setOutputPrintStream(PrintStream output) {
      this.closeFile();
      this.out = output;
   }

   public void setEmacsMode(boolean emacsMode) {
      this.emacsMode = emacsMode;
   }

   public void setErrorPrintStream(PrintStream err) {
      this.setOutputPrintStream(err);
   }

   private static String formatTime(long millis) {
      long seconds = millis / 1000L;
      long minutes = seconds / 60L;
      return minutes > 0L ? Long.toString(minutes) + " minute" + (minutes == 1L ? " " : "s ") + Long.toString(seconds % 60L) + " second" + (seconds % 60L == 1L ? "" : "s") : Long.toString(seconds) + " second" + (seconds % 60L == 1L ? "" : "s");
   }

   public void setProject(Project project) {
      this.project = project;
      if (project != null) {
         project.addBuildListener(this);
      }

   }

   public void cleanup() {
      this.closeFile();
      if (this.project != null) {
         this.project.removeBuildListener(this);
      }

      this.project = null;
   }

   void openFile(boolean append) throws BuildException {
      this.openFileImpl(append);
   }

   void closeFile() {
      if (this.out != null) {
         this.out.close();
         this.out = null;
      }

   }

   void reopenFile() throws BuildException {
      this.openFileImpl(true);
   }

   private void openFileImpl(boolean append) throws BuildException {
      if (this.out == null) {
         try {
            this.out = new PrintStream(new FileOutputStream(this.filename, append));
         } catch (IOException var3) {
            throw new BuildException("Problems opening file using a recorder entry", var3);
         }
      }

   }
}
