package org.apache.maven.project.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelValidationResult {
   private static final String NEWLINE = System.getProperty("line.separator");
   private List messages = new ArrayList();

   public int getMessageCount() {
      return this.messages.size();
   }

   public String getMessage(int i) {
      return this.messages.get(i).toString();
   }

   public List getMessages() {
      return Collections.unmodifiableList(this.messages);
   }

   public void addMessage(String message) {
      this.messages.add(message);
   }

   public String toString() {
      return this.render("");
   }

   public String render(String indentation) {
      if (this.messages.size() == 0) {
         return indentation + "There were no validation errors.";
      } else {
         StringBuffer message = new StringBuffer();

         for(int i = 0; i < this.messages.size(); ++i) {
            message.append(indentation + "[" + i + "]  " + this.messages.get(i).toString() + NEWLINE);
         }

         return message.toString();
      }
   }
}
