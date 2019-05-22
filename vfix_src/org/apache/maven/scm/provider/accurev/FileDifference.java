package org.apache.maven.scm.provider.accurev;

import java.io.File;

public class FileDifference {
   private String oldVersionSpec = null;
   private File oldFile = null;
   private String newVersionSpec = null;
   private File newFile = null;
   private long elementId = -1L;

   public FileDifference(long elementId, String newPath, String newVersion, String oldPath, String oldVersion) {
      this.setElementId(elementId);
      this.setNewVersion(newPath, newVersion);
      this.setOldVersion(oldPath, oldVersion);
   }

   public FileDifference() {
   }

   public String getOldVersionSpec() {
      return this.oldVersionSpec;
   }

   public File getOldFile() {
      return this.oldFile;
   }

   public String getNewVersionSpec() {
      return this.newVersionSpec;
   }

   public File getNewFile() {
      return this.newFile;
   }

   public long getElementId() {
      return this.elementId;
   }

   public void setElementId(long elementId) {
      this.elementId = elementId;
   }

   public void setNewVersion(String path, String version) {
      this.newFile = this.oldFile != null && this.oldFile.getPath().equals(path) ? this.oldFile : (path == null ? null : new File(path));
      this.newVersionSpec = version;
   }

   public void setOldVersion(String path, String version) {
      this.oldFile = this.newFile != null && this.newFile.getPath().equals(path) ? this.newFile : (path == null ? null : new File(path));
      this.oldVersionSpec = version;
   }

   public String toString() {
      return "FileDifference [elementId=" + this.elementId + ", newFile=" + this.newFile + ", newVersionSpec=" + this.newVersionSpec + ", oldFile=" + this.oldFile + ", oldVersionSpec=" + this.oldVersionSpec + "]";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (int)(this.elementId ^ this.elementId >>> 32);
      result = 31 * result + (this.newFile == null ? 0 : this.newFile.hashCode());
      result = 31 * result + (this.newVersionSpec == null ? 0 : this.newVersionSpec.hashCode());
      result = 31 * result + (this.oldFile == null ? 0 : this.oldFile.hashCode());
      result = 31 * result + (this.oldVersionSpec == null ? 0 : this.oldVersionSpec.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         FileDifference other = (FileDifference)obj;
         if (this.elementId != other.elementId) {
            return false;
         } else {
            if (this.newFile == null) {
               if (other.newFile != null) {
                  return false;
               }
            } else if (!this.newFile.equals(other.newFile)) {
               return false;
            }

            if (this.newVersionSpec == null) {
               if (other.newVersionSpec != null) {
                  return false;
               }
            } else if (!this.newVersionSpec.equals(other.newVersionSpec)) {
               return false;
            }

            if (this.oldFile == null) {
               if (other.oldFile != null) {
                  return false;
               }
            } else if (!this.oldFile.equals(other.oldFile)) {
               return false;
            }

            if (this.oldVersionSpec == null) {
               if (other.oldVersionSpec != null) {
                  return false;
               }
            } else if (!this.oldVersionSpec.equals(other.oldVersionSpec)) {
               return false;
            }

            return true;
         }
      }
   }
}
