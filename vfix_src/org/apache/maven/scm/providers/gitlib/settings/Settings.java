package org.apache.maven.scm.providers.gitlib.settings;

import java.io.Serializable;

public class Settings implements Serializable {
   private String revParseDateFormat = "yyyy-MM-dd HH:mm:ss";
   private String traceGitCommand = "";
   private boolean commitNoVerify = false;
   private String modelEncoding = "UTF-8";

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public String getRevParseDateFormat() {
      return this.revParseDateFormat;
   }

   public String getTraceGitCommand() {
      return this.traceGitCommand;
   }

   public boolean isCommitNoVerify() {
      return this.commitNoVerify;
   }

   public void setCommitNoVerify(boolean commitNoVerify) {
      this.commitNoVerify = commitNoVerify;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setRevParseDateFormat(String revParseDateFormat) {
      this.revParseDateFormat = revParseDateFormat;
   }

   public void setTraceGitCommand(String traceGitCommand) {
      this.traceGitCommand = traceGitCommand;
   }
}
