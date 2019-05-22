package org.apache.maven.scm.provider.synergy.util;

import java.util.Date;

public class SynergyTask {
   private int number;
   private String username;
   private Date modifiedTime;
   private String comment;

   public String getComment() {
      return this.comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public Date getModifiedTime() {
      return this.modifiedTime;
   }

   public void setModifiedTime(Date modifiedTime) {
      this.modifiedTime = modifiedTime;
   }

   public int getNumber() {
      return this.number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }
}
