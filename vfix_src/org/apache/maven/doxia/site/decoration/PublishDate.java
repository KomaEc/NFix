package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class PublishDate implements Serializable {
   private String position;
   private String format;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof PublishDate)) {
         return false;
      } else {
         boolean var10000;
         PublishDate that;
         boolean result;
         label40: {
            label39: {
               that = (PublishDate)other;
               result = true;
               if (result) {
                  if (this.getPosition() == null) {
                     if (that.getPosition() == null) {
                        break label39;
                     }
                  } else if (this.getPosition().equals(that.getPosition())) {
                     break label39;
                  }
               }

               var10000 = false;
               break label40;
            }

            var10000 = true;
         }

         label31: {
            label30: {
               result = var10000;
               if (result) {
                  if (this.getFormat() == null) {
                     if (that.getFormat() == null) {
                        break label30;
                     }
                  } else if (this.getFormat().equals(that.getFormat())) {
                     break label30;
                  }
               }

               var10000 = false;
               break label31;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public String getFormat() {
      return this.format;
   }

   public String getPosition() {
      return this.position;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.position != null ? this.position.hashCode() : 0);
      result = 37 * result + (this.format != null ? this.format.hashCode() : 0);
      return result;
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("position = '");
      buf.append(this.getPosition());
      buf.append("'");
      buf.append("\n");
      buf.append("format = '");
      buf.append(this.getFormat());
      buf.append("'");
      return buf.toString();
   }
}
