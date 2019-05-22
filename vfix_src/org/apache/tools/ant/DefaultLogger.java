package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.StringUtils;

public class DefaultLogger implements BuildLogger {
   public static final int LEFT_COLUMN_SIZE = 12;
   protected PrintStream out;
   protected PrintStream err;
   protected int msgOutputLevel = 0;
   private long startTime = System.currentTimeMillis();
   protected static final String lSep;
   protected boolean emacsMode = false;

   public void setMessageOutputLevel(int level) {
      this.msgOutputLevel = level;
   }

   public void setOutputPrintStream(PrintStream output) {
      this.out = new PrintStream(output, true);
   }

   public void setErrorPrintStream(PrintStream err) {
      this.err = new PrintStream(err, true);
   }

   public void setEmacsMode(boolean emacsMode) {
      this.emacsMode = emacsMode;
   }

   public void buildStarted(BuildEvent event) {
      this.startTime = System.currentTimeMillis();
   }

   public void buildFinished(BuildEvent event) {
      Throwable error = event.getException();
      StringBuffer message = new StringBuffer();
      if (error == null) {
         message.append(StringUtils.LINE_SEP);
         message.append(this.getBuildSuccessfulMessage());
      } else {
         message.append(StringUtils.LINE_SEP);
         message.append(this.getBuildFailedMessage());
         message.append(StringUtils.LINE_SEP);
         if (3 > this.msgOutputLevel && error instanceof BuildException) {
            message.append(error.toString()).append(lSep);
         } else {
            message.append(StringUtils.getStackTrace(error));
         }
      }

      message.append(StringUtils.LINE_SEP);
      message.append("Total time: ");
      message.append(formatTime(System.currentTimeMillis() - this.startTime));
      String msg = message.toString();
      if (error == null) {
         this.printMessage(msg, this.out, 3);
      } else {
         this.printMessage(msg, this.err, 0);
      }

      this.log(msg);
   }

   protected String getBuildFailedMessage() {
      return "BUILD FAILED";
   }

   protected String getBuildSuccessfulMessage() {
      return "BUILD SUCCESSFUL";
   }

   public void targetStarted(BuildEvent event) {
      if (2 <= this.msgOutputLevel && !event.getTarget().getName().equals("")) {
         String msg = StringUtils.LINE_SEP + event.getTarget().getName() + ":";
         this.printMessage(msg, this.out, event.getPriority());
         this.log(msg);
      }

   }

   public void targetFinished(BuildEvent event) {
   }

   public void taskStarted(BuildEvent event) {
   }

   public void taskFinished(BuildEvent event) {
   }

   public void messageLogged(BuildEvent event) {
      int priority = event.getPriority();
      if (priority <= this.msgOutputLevel) {
         StringBuffer message = new StringBuffer();
         String label;
         if (event.getTask() != null && !this.emacsMode) {
            String name = event.getTask().getTaskName();
            label = "[" + name + "] ";
            int size = 12 - label.length();
            StringBuffer tmp = new StringBuffer();

            for(int i = 0; i < size; ++i) {
               tmp.append(" ");
            }

            tmp.append(label);
            label = tmp.toString();

            try {
               BufferedReader r = new BufferedReader(new StringReader(event.getMessage()));
               String line = r.readLine();
               boolean first = true;

               do {
                  if (first) {
                     if (line == null) {
                        message.append(label);
                        break;
                     }
                  } else {
                     message.append(StringUtils.LINE_SEP);
                  }

                  first = false;
                  message.append(label).append(line);
                  line = r.readLine();
               } while(line != null);
            } catch (IOException var11) {
               message.append(label).append(event.getMessage());
            }
         } else {
            message.append(event.getMessage());
         }

         Throwable ex = event.getException();
         if (4 <= this.msgOutputLevel && ex != null) {
            message.append(StringUtils.getStackTrace(ex));
         }

         label = message.toString();
         if (priority != 0) {
            this.printMessage(label, this.out, priority);
         } else {
            this.printMessage(label, this.err, priority);
         }

         this.log(label);
      }

   }

   protected static String formatTime(long millis) {
      return DateUtils.formatElapsedTime(millis);
   }

   protected void printMessage(String message, PrintStream stream, int priority) {
      stream.println(message);
   }

   protected void log(String message) {
   }

   static {
      lSep = StringUtils.LINE_SEP;
   }
}
