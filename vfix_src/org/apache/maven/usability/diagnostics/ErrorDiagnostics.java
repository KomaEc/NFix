package org.apache.maven.usability.diagnostics;

import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;

public class ErrorDiagnostics extends AbstractLogEnabled implements Contextualizable {
   public static final String ROLE;
   private PlexusContainer container;
   private List errorDiagnosers;

   public void setErrorDiagnosers(List errorDiagnosers) {
      this.errorDiagnosers = errorDiagnosers;
   }

   public String diagnose(Throwable error) {
      List diags = this.errorDiagnosers;
      boolean releaseDiags = false;
      boolean errorProcessed = false;
      String message = null;

      try {
         if (diags == null) {
            releaseDiags = true;

            try {
               diags = this.container.lookupList(ErrorDiagnoser.ROLE);
            } catch (ComponentLookupException var15) {
               this.getLogger().error("Failed to lookup the list of error diagnosers.", var15);
            }
         }

         if (diags != null) {
            Iterator it = diags.iterator();

            while(it.hasNext()) {
               ErrorDiagnoser diagnoser = (ErrorDiagnoser)it.next();
               if (diagnoser.canDiagnose(error)) {
                  errorProcessed = true;
                  message = diagnoser.diagnose(error);
                  break;
               }
            }
         }
      } finally {
         if (releaseDiags && diags != null) {
            try {
               this.container.releaseAll(diags);
            } catch (ComponentLifecycleException var14) {
               this.getLogger().debug("Failed to release error diagnoser list.", var14);
            }
         }

         if (!errorProcessed) {
            message = (new ErrorDiagnostics.PuntErrorDiagnoser()).diagnose(error);
         }

      }

      return message;
   }

   public void contextualize(Context context) throws ContextException {
      this.container = (PlexusContainer)context.get("plexus");
   }

   static {
      ROLE = ErrorDiagnostics.class.getName();
   }

   private static class PuntErrorDiagnoser implements ErrorDiagnoser {
      private PuntErrorDiagnoser() {
      }

      public boolean canDiagnose(Throwable error) {
         return true;
      }

      public String diagnose(Throwable error) {
         StringBuffer message = new StringBuffer();
         message.append(error.getMessage());
         DiagnosisUtils.appendRootCauseIfPresentAndUnique(error, message, false);
         return message.toString();
      }

      // $FF: synthetic method
      PuntErrorDiagnoser(Object x0) {
         this();
      }
   }
}
