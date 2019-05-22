package org.apache.maven.doxia.site.decoration;

import java.io.Serializable;

public class Banner implements Serializable {
   private String name;
   private String src;
   private String alt;
   private String href;

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Banner)) {
         return false;
      } else {
         boolean var10000;
         Banner that;
         boolean result;
         label73: {
            label72: {
               that = (Banner)other;
               result = true;
               if (result) {
                  if (this.getName() == null) {
                     if (that.getName() == null) {
                        break label72;
                     }
                  } else if (this.getName().equals(that.getName())) {
                     break label72;
                  }
               }

               var10000 = false;
               break label73;
            }

            var10000 = true;
         }

         label64: {
            label63: {
               result = var10000;
               if (result) {
                  if (this.getSrc() == null) {
                     if (that.getSrc() == null) {
                        break label63;
                     }
                  } else if (this.getSrc().equals(that.getSrc())) {
                     break label63;
                  }
               }

               var10000 = false;
               break label64;
            }

            var10000 = true;
         }

         label55: {
            label54: {
               result = var10000;
               if (result) {
                  if (this.getAlt() == null) {
                     if (that.getAlt() == null) {
                        break label54;
                     }
                  } else if (this.getAlt().equals(that.getAlt())) {
                     break label54;
                  }
               }

               var10000 = false;
               break label55;
            }

            var10000 = true;
         }

         label83: {
            result = var10000;
            if (result) {
               label82: {
                  if (this.getHref() == null) {
                     if (that.getHref() != null) {
                        break label82;
                     }
                  } else if (!this.getHref().equals(that.getHref())) {
                     break label82;
                  }

                  var10000 = true;
                  break label83;
               }
            }

            var10000 = false;
         }

         result = var10000;
         return result;
      }
   }

   public String getAlt() {
      return this.alt;
   }

   public String getHref() {
      return this.href;
   }

   public String getName() {
      return this.name;
   }

   public String getSrc() {
      return this.src;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 37 * result + (this.src != null ? this.src.hashCode() : 0);
      result = 37 * result + (this.alt != null ? this.alt.hashCode() : 0);
      result = 37 * result + (this.href != null ? this.href.hashCode() : 0);
      return result;
   }

   public void setAlt(String alt) {
      this.alt = alt;
   }

   public void setHref(String href) {
      this.href = href;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSrc(String src) {
      this.src = src;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("name = '");
      buf.append(this.getName());
      buf.append("'");
      buf.append("\n");
      buf.append("src = '");
      buf.append(this.getSrc());
      buf.append("'");
      buf.append("\n");
      buf.append("alt = '");
      buf.append(this.getAlt());
      buf.append("'");
      buf.append("\n");
      buf.append("href = '");
      buf.append(this.getHref());
      buf.append("'");
      return buf.toString();
   }
}
