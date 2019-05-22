package org.apache.tools.ant.input;

public class InputRequest {
   private String prompt;
   private String input;
   private String defaultValue;

   public InputRequest(String prompt) {
      if (prompt == null) {
         throw new IllegalArgumentException("prompt must not be null");
      } else {
         this.prompt = prompt;
      }
   }

   public String getPrompt() {
      return this.prompt;
   }

   public void setInput(String input) {
      this.input = input;
   }

   public boolean isInputValid() {
      return true;
   }

   public String getInput() {
      return this.input;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String d) {
      this.defaultValue = d;
   }
}
