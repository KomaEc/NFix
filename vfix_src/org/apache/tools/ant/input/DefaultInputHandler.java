package org.apache.tools.ant.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;

public class DefaultInputHandler implements InputHandler {
   public void handleInput(InputRequest request) throws BuildException {
      String prompt = this.getPrompt(request);
      BufferedReader r = null;

      try {
         r = new BufferedReader(new InputStreamReader(this.getInputStream()));

         do {
            System.err.println(prompt);
            System.err.flush();

            try {
               String input = r.readLine();
               request.setInput(input);
            } catch (IOException var12) {
               throw new BuildException("Failed to read input from Console.", var12);
            }
         } while(!request.isInputValid());
      } finally {
         if (r != null) {
            try {
               r.close();
            } catch (IOException var11) {
               throw new BuildException("Failed to close input.", var11);
            }
         }

      }

   }

   protected String getPrompt(InputRequest request) {
      String prompt = request.getPrompt();
      String def = request.getDefaultValue();
      if (request instanceof MultipleChoiceInputRequest) {
         StringBuffer sb = new StringBuffer(prompt);
         sb.append(" (");
         Enumeration e = ((MultipleChoiceInputRequest)request).getChoices().elements();

         for(boolean first = true; e.hasMoreElements(); first = false) {
            if (!first) {
               sb.append(", ");
            }

            String next = (String)e.nextElement();
            if (next.equals(def)) {
               sb.append('[');
            }

            sb.append(next);
            if (next.equals(def)) {
               sb.append(']');
            }
         }

         sb.append(")");
         return sb.toString();
      } else {
         return def != null ? prompt + " [" + def + "]" : prompt;
      }
   }

   protected InputStream getInputStream() {
      return System.in;
   }
}
