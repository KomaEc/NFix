package org.apache.maven.usability;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.usability.diagnostics.DiagnosisUtils;
import org.apache.maven.usability.diagnostics.ErrorDiagnoser;

public class MojoFailureExceptionDiagnoser implements ErrorDiagnoser {
   public boolean canDiagnose(Throwable error) {
      return DiagnosisUtils.containsInCausality(error, MojoFailureException.class);
   }

   public String diagnose(Throwable error) {
      MojoFailureException mfe = (MojoFailureException)DiagnosisUtils.getFromCausality(error, MojoFailureException.class);
      StringBuffer message = new StringBuffer();
      Object source = mfe.getSource();
      if (source != null) {
         message.append(": ").append(mfe.getSource()).append("\n");
      }

      message.append(mfe.getMessage());
      String longMessage = mfe.getLongMessage();
      if (longMessage != null) {
         message.append("\n\n").append(longMessage);
      }

      return message.toString();
   }
}
