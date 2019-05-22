package org.apache.maven.doxia.module.fml.model;

import java.io.Serializable;

public class Faq implements Serializable {
   private String id;
   private String question;
   private String answer;
   private String modelEncoding = "UTF-8";

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Faq)) {
         return false;
      } else {
         boolean var10000;
         Faq that;
         boolean result;
         label56: {
            label55: {
               that = (Faq)other;
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
                  if (this.getQuestion() == null) {
                     if (that.getQuestion() == null) {
                        break label46;
                     }
                  } else if (this.getQuestion().equals(that.getQuestion())) {
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
                  if (this.getAnswer() == null) {
                     if (that.getAnswer() == null) {
                        break label37;
                     }
                  } else if (this.getAnswer().equals(that.getAnswer())) {
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

   public String getAnswer() {
      return this.answer;
   }

   public String getId() {
      return this.id;
   }

   public String getQuestion() {
      return this.question;
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.id != null ? this.id.hashCode() : 0);
      result = 37 * result + (this.question != null ? this.question.hashCode() : 0);
      result = 37 * result + (this.answer != null ? this.answer.hashCode() : 0);
      return result;
   }

   public void setAnswer(String answer) {
      this.answer = answer;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setQuestion(String question) {
      this.question = question;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("id = '");
      buf.append(this.getId());
      buf.append("'");
      buf.append("\n");
      buf.append("question = '");
      buf.append(this.getQuestion());
      buf.append("'");
      buf.append("\n");
      buf.append("answer = '");
      buf.append(this.getAnswer());
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
