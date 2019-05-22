package org.netbeans.lib.cvsclient.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.cvsclient.request.GlobalOptionRequest;
import org.netbeans.lib.cvsclient.request.SetRequest;

public class GlobalOptions implements Cloneable {
   private List variables = new ArrayList();
   private boolean doNoChanges;
   private boolean checkedOutFilesReadOnly;
   private String cvsRoot;
   private boolean useGzip = true;
   private int compressionLevel = 0;
   private boolean noHistoryLogging;
   private boolean moderatelyQuiet;
   private boolean veryQuiet;
   private boolean traceExecution;
   private boolean showHelp;
   private boolean showVersion;
   private boolean ignoreCvsrc;
   private File tempDir;
   private String editor;
   private File[] exclusions;

   public void setExclusions(File[] var1) {
      this.exclusions = var1;
   }

   public File[] getExclusions() {
      return this.exclusions;
   }

   public boolean isExcluded(File var1) {
      if (this.exclusions != null) {
         for(int var2 = 0; var2 < this.exclusions.length; ++var2) {
            if (isParentOrEqual(this.exclusions[var2], var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private static boolean isParentOrEqual(File var0, File var1) {
      while(var1 != null) {
         if (var1.equals(var0)) {
            return true;
         }

         var1 = var1.getParentFile();
      }

      return false;
   }

   public List createRequestList() {
      LinkedList var1 = new LinkedList();
      if (this.variables.size() > 0) {
         Iterator var2 = this.variables.iterator();

         while(var2.hasNext()) {
            String var3 = var2.next().toString();
            var1.add(new SetRequest(var3));
         }
      }

      if (this.isNoHistoryLogging()) {
         var1.add(new GlobalOptionRequest("-l"));
      }

      if (this.isDoNoChanges()) {
         var1.add(new GlobalOptionRequest("-n"));
      }

      if (this.isModeratelyQuiet()) {
         var1.add(new GlobalOptionRequest("-q"));
      }

      if (this.isVeryQuiet()) {
         var1.add(new GlobalOptionRequest("-Q"));
      }

      if (this.isTraceExecution()) {
         var1.add(new GlobalOptionRequest("-t"));
      }

      return var1;
   }

   public String getOptString() {
      return "Hvnfd:lqQtrws:z:T:e:";
   }

   public boolean setCVSCommand(char var1, String var2) {
      if (var1 == 'n') {
         this.setDoNoChanges(true);
      } else if (var1 == 'd') {
         this.setCVSRoot(var2);
      } else if (var1 == 'l') {
         this.setNoHistoryLogging(true);
      } else if (var1 == 'q') {
         this.setModeratelyQuiet(true);
      } else if (var1 == 'Q') {
         this.setVeryQuiet(true);
      } else if (var1 == 't') {
         this.setTraceExecution(true);
      } else if (var1 == 't') {
         this.setTraceExecution(true);
      } else if (var1 == 'r') {
         this.setCheckedOutFilesReadOnly(true);
      } else if (var1 == 'w') {
         this.setCheckedOutFilesReadOnly(false);
      } else if (var1 == 's') {
         this.setCvsVariable(var2);
      } else if (var1 == 'z') {
         try {
            this.setCompressionLevel(Integer.parseInt(var2));
         } catch (NumberFormatException var4) {
         }
      } else if (var1 == 'H') {
         this.setShowHelp(true);
      } else if (var1 == 'v') {
         this.setShowVersion(true);
      } else if (var1 == 'f') {
         this.setIgnoreCvsrc(true);
      } else if (var1 == 'T') {
         this.setTempDir(new File(var2));
      } else {
         if (var1 != 'e') {
            return false;
         }

         this.setEditor(var2);
      }

      return true;
   }

   public void resetCVSCommand() {
      this.setCheckedOutFilesReadOnly(false);
      this.setDoNoChanges(false);
      this.setModeratelyQuiet(false);
      this.setNoHistoryLogging(false);
      this.setTraceExecution(false);
      this.setUseGzip(true);
      this.setCompressionLevel(0);
      this.setVeryQuiet(false);
      this.setShowHelp(false);
      this.setShowVersion(false);
      this.setIgnoreCvsrc(false);
      this.setTempDir((File)null);
      this.setEditor((String)null);
      this.setCVSRoot("");
      this.clearCvsVariables();
   }

   public String getCVSCommand() {
      StringBuffer var1 = new StringBuffer();
      if (this.isDoNoChanges()) {
         var1.append("-n ");
      }

      if (this.isNoHistoryLogging()) {
         var1.append("-l ");
      }

      if (this.isModeratelyQuiet()) {
         var1.append("-q ");
      }

      if (this.isVeryQuiet()) {
         var1.append("-Q ");
      }

      if (this.isTraceExecution()) {
         var1.append("-t ");
      }

      if (this.isCheckedOutFilesReadOnly()) {
         var1.append("-r ");
      }

      if (this.variables.size() > 0) {
         Iterator var2 = this.variables.iterator();

         while(var2.hasNext()) {
            String var3 = var2.next().toString();
            var1.append("-s " + var3 + " ");
         }
      }

      if (this.compressionLevel != 0) {
         var1.append("-z ");
         var1.append(Integer.toString(this.compressionLevel));
         var1.append(" ");
      }

      if (this.isIgnoreCvsrc()) {
         var1.append("-f ");
      }

      if (this.tempDir != null) {
         var1.append("-T ");
         var1.append(this.tempDir.getAbsolutePath());
         var1.append(" ");
      }

      if (this.editor != null) {
         var1.append("-e ");
         var1.append(this.editor);
         var1.append(" ");
      }

      return var1.toString();
   }

   public void setCvsVariable(String var1) {
      this.variables.add(var1);
   }

   public void clearCvsVariables() {
      this.variables.clear();
   }

   public void setCvsVariables(String[] var1) {
      this.clearCvsVariables();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];
         this.variables.add(var3);
      }

   }

   public String[] getCvsVariables() {
      String[] var1 = new String[this.variables.size()];
      var1 = (String[])this.variables.toArray(var1);
      return var1;
   }

   public void setDoNoChanges(boolean var1) {
      this.doNoChanges = var1;
   }

   public boolean isDoNoChanges() {
      return this.doNoChanges;
   }

   public boolean isCheckedOutFilesReadOnly() {
      return this.checkedOutFilesReadOnly;
   }

   public void setCheckedOutFilesReadOnly(boolean var1) {
      this.checkedOutFilesReadOnly = var1;
   }

   public String getCVSRoot() {
      return this.cvsRoot;
   }

   public void setCVSRoot(String var1) {
      this.cvsRoot = var1;
   }

   public void setUseGzip(boolean var1) {
      this.useGzip = var1;
   }

   public boolean isUseGzip() {
      return this.useGzip;
   }

   public int getCompressionLevel() {
      return this.compressionLevel;
   }

   public void setCompressionLevel(int var1) {
      this.compressionLevel = var1;
   }

   public boolean isNoHistoryLogging() {
      return this.noHistoryLogging;
   }

   public void setNoHistoryLogging(boolean var1) {
      this.noHistoryLogging = var1;
   }

   public boolean isModeratelyQuiet() {
      return this.moderatelyQuiet;
   }

   public void setModeratelyQuiet(boolean var1) {
      this.moderatelyQuiet = var1;
   }

   public boolean isVeryQuiet() {
      return this.veryQuiet;
   }

   public void setVeryQuiet(boolean var1) {
      this.veryQuiet = var1;
   }

   public boolean isTraceExecution() {
      return this.traceExecution;
   }

   public void setTraceExecution(boolean var1) {
      this.traceExecution = var1;
   }

   public boolean isShowHelp() {
      return this.showHelp;
   }

   public void setShowHelp(boolean var1) {
      this.showHelp = var1;
   }

   public boolean isShowVersion() {
      return this.showVersion;
   }

   public void setShowVersion(boolean var1) {
      this.showVersion = var1;
   }

   public boolean isIgnoreCvsrc() {
      return this.ignoreCvsrc;
   }

   public void setIgnoreCvsrc(boolean var1) {
      this.ignoreCvsrc = var1;
   }

   public File getTempDir() {
      return this.tempDir;
   }

   public void setTempDir(File var1) {
      this.tempDir = var1;
   }

   public String getEditor() {
      return this.editor;
   }

   public void setEditor(String var1) {
      this.editor = var1;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
