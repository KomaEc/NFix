package org.apache.maven.scm;

import java.io.Serializable;

public class ScmTagParameters implements Serializable {
   private static final long serialVersionUID = 7241536408630606807L;
   private String message;
   private boolean remoteTagging = false;
   private String scmRevision;

   public ScmTagParameters() {
      this.remoteTagging = false;
   }

   public ScmTagParameters(String message) {
      this.message = message;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public boolean isRemoteTagging() {
      return this.remoteTagging;
   }

   public void setRemoteTagging(boolean remoteTagging) {
      this.remoteTagging = remoteTagging;
   }

   public String getScmRevision() {
      return this.scmRevision;
   }

   public void setScmRevision(String scmRevision) {
      this.scmRevision = scmRevision;
   }

   public String toString() {
      return "[" + this.scmRevision + "] " + this.message;
   }
}
