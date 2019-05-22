package soot.jbco.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import javax.swing.JScrollBar;

public class RunnerThread implements Runnable {
   public boolean stopRun = false;
   private JBCOViewer viewer = null;
   private String[] cmdarray = null;
   private String wdir = null;

   public RunnerThread(String[] argv, JBCOViewer jv, String workingdir) {
      this.cmdarray = argv;
      this.viewer = jv;
      this.wdir = workingdir;
   }

   public void run() {
      synchronized(this.viewer.newFileMenuItem) {
         this.viewer.newFileMenuItem.setEnabled(false);
      }

      synchronized(this.viewer.openFileMenuItem) {
         this.viewer.openFileMenuItem.setEnabled(true);
      }

      try {
         File f = null;
         if (this.wdir != null) {
            f = new File(this.wdir);
            if (!f.exists() || !f.isDirectory()) {
               throw new Exception(f + " does not appear to be a proper working directory.");
            }
         }

         Process p = Runtime.getRuntime().exec(this.cmdarray, (String[])null, f);
         BufferedReader br_in = new BufferedReader(new InputStreamReader(p.getInputStream()));
         BufferedReader br_er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
         String line_in = "";

         try {
            while((line_in = br_in.readLine()) != null || (line_in = br_er.readLine()) != null) {
               if (this.stopRun) {
                  p.destroy();
                  synchronized(this.viewer.TextAreaOutput) {
                     this.viewer.TextAreaOutput.append("\n\n*** Execution STOPPED ***");
                     this.viewer.TextAreaOutput.setCaretPosition(this.viewer.TextAreaOutput.getDocument().getLength());
                     break;
                  }
               }

               synchronized(this.viewer.TextAreaOutput) {
                  boolean autoScroll = false;
                  JScrollBar vbar = this.viewer.jScrollPane1.getVerticalScrollBar();
                  synchronized(vbar) {
                     autoScroll = vbar.getValue() + vbar.getVisibleAmount() == vbar.getMaximum();
                  }

                  this.viewer.TextAreaOutput.append("\n" + line_in);
                  if (autoScroll) {
                     this.viewer.TextAreaOutput.setCaretPosition(this.viewer.TextAreaOutput.getDocument().getLength());
                  }
               }
            }
         } catch (Exception var35) {
            throw var35;
         } finally {
            br_in.close();
            br_er.close();
         }
      } catch (Exception var37) {
         Exception exc = var37;
         synchronized(this.viewer.TextAreaOutput) {
            this.viewer.TextAreaOutput.append("\n\n" + exc.toString());
            this.viewer.TextAreaOutput.setCaretPosition(this.viewer.TextAreaOutput.getDocument().getLength());
         }
      }

      synchronized(this.viewer.newFileMenuItem) {
         this.viewer.newFileMenuItem.setEnabled(true);
      }

      synchronized(this.viewer.openFileMenuItem) {
         this.viewer.openFileMenuItem.setEnabled(false);
      }
   }
}
