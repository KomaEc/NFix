package org.apache.maven.wagon.resource;

public class Resource {
   private String name;
   private long lastModified;
   private long contentLength = -1L;

   public Resource() {
   }

   public Resource(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public long getLastModified() {
      return this.lastModified;
   }

   public void setLastModified(long lastModified) {
      this.lastModified = lastModified;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public void setContentLength(long contentLength) {
      this.contentLength = contentLength;
   }

   public String toString() {
      return this.name;
   }

   public String inspect() {
      return this.name + "[len = " + this.contentLength + "; mod = " + this.lastModified + "]";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (int)(this.contentLength ^ this.contentLength >>> 32);
      result = 31 * result + (int)(this.lastModified ^ this.lastModified >>> 32);
      result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
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
         Resource other = (Resource)obj;
         if (this.contentLength != other.contentLength) {
            return false;
         } else if (this.lastModified != other.lastModified) {
            return false;
         } else {
            if (this.name == null) {
               if (other.name != null) {
                  return false;
               }
            } else if (!this.name.equals(other.name)) {
               return false;
            }

            return true;
         }
      }
   }
}
