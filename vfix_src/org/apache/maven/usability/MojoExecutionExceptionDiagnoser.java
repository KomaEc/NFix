package org.apache.maven.usability;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.usability.diagnostics.DiagnosisUtils;
import org.apache.maven.usability.diagnostics.ErrorDiagnoser;

public class MojoExecutionExceptionDiagnoser implements ErrorDiagnoser {
   public boolean canDiagnose(Throwable error) {
      return DiagnosisUtils.containsInCausality(error, MojoExecutionException.class);
   }

   public String diagnose(Throwable error) {
      MojoExecutionException mee = (MojoExecutionException)DiagnosisUtils.getFromCausality(error, MojoExecutionException.class);
      StringBuffer message = new StringBuffer();
      Object source = mee.getSource();
      if (source != null) {
         message.append(": ").append(mee.getSource()).append("\n");
      }

      message.append(mee.getMessage());
      String longMessage = mee.getLongMessage();
      if (longMessage != null) {
         message.append("\n\n").append(longMessage);
      }

      Throwable directCause = mee.getCause();
      if (directCause != null) {
         message.append("\n");
         String directCauseMessage = directCause.getMessage();
         String meeMessage = mee.getMessage();
         if (directCauseMessage != null && meeMessage != null && meeMessage.indexOf(directCauseMessage) < 0) {
            message.append("\nEmbedded error: ").append(directCauseMessage);
         }

         DiagnosisUtils.appendRootCauseIfPresentAndUnique(directCause, message, false);
      }

      return message.toString();
   }
}
