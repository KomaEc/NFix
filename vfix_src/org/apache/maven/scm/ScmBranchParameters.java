package org.apache.maven.scm;

import java.io.Serializable;

public class ScmBranchParameters implements Serializable {
   private static final long serialVersionUID = 7241536408630608707L;
   private String message;
   private boolean remoteBranching = false;
   private String scmRevision;

   public ScmBranchParameters() {
      this.remoteBranching = false;
   }

   public ScmBranchParameters(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getScmRevision() {
      return this.scmRevision;
   }

   public void setScmRevision(String scmRevision) {
      this.scmRevision = scmRevision;
   }

   public boolean isRemoteBranching() {
      return this.remoteBranching;
   }

   public void setRemoteBranching(boolean remoteBranching) {
      this.remoteBranching = remoteBranching;
   }

   public String toString() {
      return "[" + this.scmRevision + "] " + this.message;
   }
}
