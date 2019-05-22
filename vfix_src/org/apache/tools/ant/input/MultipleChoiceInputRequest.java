package org.apache.tools.ant.input;

import java.util.Vector;

public class MultipleChoiceInputRequest extends InputRequest {
   private Vector choices = new Vector();

   public MultipleChoiceInputRequest(String prompt, Vector choices) {
      super(prompt);
      if (choices == null) {
         throw new IllegalArgumentException("choices must not be null");
      } else {
         this.choices = choices;
      }
   }

   public Vector getChoices() {
      return this.choices;
   }

   public boolean isInputValid() {
      return this.choices.contains(this.getInput()) || "".equals(this.getInput()) && this.getDefaultValue() != null;
   }
}
