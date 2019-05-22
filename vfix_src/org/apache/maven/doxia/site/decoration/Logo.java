package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class Logo extends LinkItem implements Serializable {
   private String img;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Logo)) {
         return false;
      } else {
         boolean var10000;
         boolean result;
         label33: {
            label32: {
               Logo that = (Logo)other;
               result = true;
               if (result) {
                  if (this.getImg() == null) {
                     if (that.getImg() == null) {
                        break label32;
                     }
                  } else if (this.getImg().equals(that.getImg())) {
                     break label32;
                  }
               }

               var10000 = false;
               break label33;
            }

            var10000 = true;
         }

         result = var10000;
         result = result && super.equals(other);
         return result;
      }
   }

   public String getImg() {
      return this.img;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.img != null ? this.img.hashCode() : 0);
      result = 37 * result + super.hashCode();
      return result;
   }

   public void setImg(String img) {
      this.img = img;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("img = '");
      buf.append(this.getImg());
      buf.append("'");
      buf.append("\n");
      buf.append(super.toString());
      return buf.toString();
   }
}
