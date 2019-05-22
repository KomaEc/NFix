package org.apache.maven.doxia.module.fml.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Faqs implements Serializable {
   private String title = "FAQ";
   private boolean toplink = true;
   private List parts;
   private String modelEncoding = "UTF-8";

   public void addPart(Part part) {
      if (!(part instanceof Part)) {
         throw new ClassCastException("Faqs.addParts(part) parameter must be instanceof " + Part.class.getName());
      } else {
         this.getParts().add(part);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Faqs)) {
         return false;
      } else {
         boolean var10000;
         Faqs that;
         boolean result;
         label49: {
            label48: {
               that = (Faqs)other;
               result = true;
               if (result) {
                  if (this.getTitle() == null) {
                     if (that.getTitle() == null) {
                        break label48;
                     }
                  } else if (this.getTitle().equals(that.getTitle())) {
                     break label48;
                  }
               }

               var10000 = false;
               break label49;
            }

            var10000 = true;
         }

         label35: {
            label34: {
               result = var10000;
               result = result && this.toplink == that.toplink;
               if (result) {
                  if (this.getParts() == null) {
                     if (that.getParts() == null) {
                        break label34;
                     }
                  } else if (this.getParts().equals(that.getParts())) {
                     break label34;
                  }
               }

               var10000 = false;
               break label35;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public List getParts() {
      if (this.parts == null) {
         this.parts = new ArrayList();
      }

      return this.parts;
   }

   public String getTitle() {
      return this.title;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.title != null ? this.title.hashCode() : 0);
      result = 37 * result + (this.toplink ? 0 : 1);
      result = 37 * result + (this.parts != null ? this.parts.hashCode() : 0);
      return result;
   }

   public boolean isToplink() {
      return this.toplink;
   }

   public void removePart(Part part) {
      if (!(part instanceof Part)) {
         throw new ClassCastException("Faqs.removeParts(part) parameter must be instanceof " + Part.class.getName());
      } else {
         this.getParts().remove(part);
      }
   }

   public void setParts(List parts) {
      this.parts = parts;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setToplink(boolean toplink) {
      this.toplink = toplink;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("title = '");
      buf.append(this.getTitle());
      buf.append("'");
      buf.append("\n");
      buf.append("toplink = '");
      buf.append(this.isToplink());
      buf.append("'");
      buf.append("\n");
      buf.append("parts = '");
      buf.append(this.getParts());
      buf.append("'");
      return buf.toString();
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }
}
