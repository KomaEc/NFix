package org.apache.tools.ant.taskdefs;

import java.util.Hashtable;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.SubBuildListener;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.LogLevel;

public class Recorder extends Task implements SubBuildListener {
   private String filename = null;
   private Boolean append = null;
   private Boolean start = null;
   private int loglevel = -1;
   private boolean emacsMode = false;
   private static Hashtable recorderEntries = new Hashtable();

   public void init() {
      this.getProject().addBuildListener(this);
   }

   public void setName(String fname) {
      this.filename = fname;
   }

   public void setAction(Recorder.ActionChoices action) {
      if (action.getValue().equalsIgnoreCase("start")) {
         this.start = Boolean.TRUE;
      } else {
         this.start = Boolean.FALSE;
      }

   }

   public void setAppend(boolean append) {
      this.append = append ? Boolean.TRUE : Boolean.FALSE;
   }

   public void setEmacsMode(boolean emacsMode) {
      this.emacsMode = emacsMode;
   }

   public void setLoglevel(Recorder.VerbosityLevelChoices level) {
      this.loglevel = level.getLevel();
   }

   public void execute() throws BuildException {
      if (this.filename == null) {
         throw new BuildException("No filename specified");
      } else {
         this.getProject().log("setting a recorder for name " + this.filename, 4);
         RecorderEntry recorder = this.getRecorder(this.filename, this.getProject());
         recorder.setMessageOutputLevel(this.loglevel);
         recorder.setEmacsMode(this.emacsMode);
         if (this.start != null) {
            if (this.start) {
               recorder.reopenFile();
               recorder.setRecordState(this.start);
            } else {
               recorder.setRecordState(this.start);
               recorder.closeFile();
            }
         }

      }
   }

   protected RecorderEntry getRecorder(String name, Project proj) throws BuildException {
      Object o = recorderEntries.get(name);
      RecorderEntry entry;
      if (o == null) {
         entry = new RecorderEntry(name);
         if (this.append == null) {
            entry.openFile(false);
         } else {
            entry.openFile(this.append);
         }

         entry.setProject(proj);
         recorderEntries.put(name, entry);
      } else {
         entry = (RecorderEntry)o;
      }

      return entry;
   }

   public void buildStarted(BuildEvent event) {
   }

   public void subBuildStarted(BuildEvent event) {
   }

   public void targetStarted(BuildEvent event) {
   }

   public void targetFinished(BuildEvent event) {
   }

   public void taskStarted(BuildEvent event) {
   }

   public void taskFinished(BuildEvent event) {
   }

   public void messageLogged(BuildEvent event) {
   }

   public void buildFinished(BuildEvent event) {
      this.cleanup();
   }

   public void subBuildFinished(BuildEvent event) {
      if (event.getProject() == this.getProject()) {
         this.cleanup();
      }

   }

   private void cleanup() {
      recorderEntries.clear();
      this.getProject().removeBuildListener(this);
   }

   public static class VerbosityLevelChoices extends LogLevel {
   }

   public static class ActionChoices extends EnumeratedAttribute {
      private static final String[] VALUES = new String[]{"start", "stop"};

      public String[] getValues() {
         return VALUES;
      }
   }
}
