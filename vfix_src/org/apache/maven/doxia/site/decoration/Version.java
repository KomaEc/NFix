package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class Version implements Serializable {
   private String position;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Version)) {
         return false;
      } else {
         boolean var10000;
         boolean result;
         label24: {
            label23: {
               Version that = (Version)other;
               result = true;
               if (result) {
                  if (this.getPosition() == null) {
                     if (that.getPosition() == null) {
                        break label23;
                     }
                  } else if (this.getPosition().equals(that.getPosition())) {
                     break label23;
                  }
               }

               var10000 = false;
               break label24;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public String getPosition() {
      return this.position;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.position != null ? this.position.hashCode() : 0);
      return result;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("position = '");
      buf.append(this.getPosition());
      buf.append("'");
      return buf.toString();
   }
}
