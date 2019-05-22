package org.apache.maven.doxia.module.fml.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Part implements Serializable {
   private String id;
   private String title;
   private List faqs;
   private String modelEncoding = "UTF-8";

   public void addFaq(Faq faq) {
      if (!(faq instanceof Faq)) {
         throw new ClassCastException("Part.addFaqs(faq) parameter must be instanceof " + Faq.class.getName());
      } else {
         this.getFaqs().add(faq);
      }
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Part)) {
         return false;
      } else {
         boolean var10000;
         Part that;
         boolean result;
         label56: {
            label55: {
               that = (Part)other;
               result = true;
               if (result) {
                  if (this.getId() == null) {
                     if (that.getId() == null) {
                        break label55;
                     }
                  } else if (this.getId().equals(that.getId())) {
                     break label55;
                  }
               }

               var10000 = false;
               break label56;
            }

            var10000 = true;
         }

         label47: {
            label46: {
               result = var10000;
               if (result) {
                  if (this.getTitle() == null) {
                     if (that.getTitle() == null) {
                        break label46;
                     }
                  } else if (this.getTitle().equals(that.getTitle())) {
                     break label46;
                  }
               }

               var10000 = false;
               break label47;
            }

            var10000 = true;
         }

         label38: {
            label37: {
               result = var10000;
               if (result) {
                  if (this.getFaqs() == null) {
                     if (that.getFaqs() == null) {
                        break label37;
                     }
                  } else if (this.getFaqs().equals(that.getFaqs())) {
                     break label37;
                  }
               }

               var10000 = false;
               break label38;
            }

            var10000 = true;
         }

         result = var10000;
         return result;
      }
   }

   public List getFaqs() {
      if (this.faqs == null) {
         this.faqs = new ArrayList();
      }

      return this.faqs;
   }

   public String getId() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.id != null ? this.id.hashCode() : 0);
      result = 37 * result + (this.title != null ? this.title.hashCode() : 0);
      result = 37 * result + (this.faqs != null ? this.faqs.hashCode() : 0);
      return result;
   }

   public void removeFaq(Faq faq) {
      if (!(faq instanceof Faq)) {
         throw new ClassCastException("Part.removeFaqs(faq) parameter must be instanceof " + Faq.class.getName());
      } else {
         this.getFaqs().remove(faq);
      }
   }

   public void setFaqs(List faqs) {
      this.faqs = faqs;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("id = '");
      buf.append(this.getId());
      buf.append("'");
      buf.append("\n");
      buf.append("title = '");
      buf.append(this.getTitle());
      buf.append("'");
      buf.append("\n");
      buf.append("faqs = '");
      buf.append(this.getFaqs());
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
