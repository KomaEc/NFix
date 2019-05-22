package org.apache.maven.model;

import java.io.Serializable;

public class Resource extends FileSet implements Serializable {
   private String targetPath;
   private boolean filtering = false;
   private String mergeId;
   private static int mergeIdCounter = 0;

   public String getMergeId() {
      return this.mergeId;
   }

   public String getTargetPath() {
      return this.targetPath;
   }

   public boolean isFiltering() {
      return this.filtering;
   }

   public void setFiltering(boolean filtering) {
      this.filtering = filtering;
   }

   public void setMergeId(String mergeId) {
      this.mergeId = mergeId;
   }

   public void setTargetPath(String targetPath) {
      this.targetPath = targetPath;
   }

   public void initMergeId() {
      if (this.getMergeId() == null) {
         this.setMergeId("resource-" + mergeIdCounter++);
      }

   }

   public String toString() {
      return "Resource {targetPath: " + this.getTargetPath() + ", filtering: " + this.isFiltering() + ", " + super.toString() + "}";
   }
}
