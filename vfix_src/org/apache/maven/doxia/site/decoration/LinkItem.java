package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class LinkItem implements Serializable {
   private String name;
   private String href;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof LinkItem)) {
         return false;
      } else {
         boolean var10000;
         LinkItem that;
         boolean result;
         label40: {
            label39: {
               that = (LinkItem)other;
               result = true;
               if (result) {
                  if (this.getName() == null) {
                     if (that.getName() == null) {
                        break label39;
                     }
                  } else if (this.getName().equals(that.getName())) {
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
                  if (this.getHref() == null) {
                     if (that.getHref() == null) {
                        break label30;
                     }
                  } else if (this.getHref().equals(that.getHref())) {
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

   public String getHref() {
      return this.href;
   }

   public String getName() {
      return this.name;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 37 * result + (this.href != null ? this.href.hashCode() : 0);
      return result;
   }

   public void setHref(String href) {
      this.href = href;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("name = '");
      buf.append(this.getName());
      buf.append("'");
      buf.append("\n");
      buf.append("href = '");
      buf.append(this.getHref());
      buf.append("'");
      return buf.toString();
   }
}
