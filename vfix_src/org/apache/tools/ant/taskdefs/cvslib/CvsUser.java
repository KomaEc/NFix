package org.apache.tools.ant.taskdefs.cvslib;

import org.apache.tools.ant.BuildException;

public class CvsUser {
   private String userID;
   private String displayName;

   public void setDisplayname(String displayName) {
      this.displayName = displayName;
   }

   public void setUserid(String userID) {
      this.userID = userID;
   }

   public String getUserID() {
      return this.userID;
   }

   public String getDisplayname() {
      return this.displayName;
   }

   public void validate() throws BuildException {
      String message;
      if (null == this.userID) {
         message = "Username attribute must be set.";
         throw new BuildException("Username attribute must be set.");
      } else if (null == this.displayName) {
         message = "Displayname attribute must be set for userID " + this.userID;
         throw new BuildException(message);
      }
   }
}
