package org.apache.maven.scm.provider.perforce.command.status;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceStatusConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   static final int STATE_FILES = 1;
   static final int STATE_ERROR = 2;
   private int currentState = 1;
   private List<String> depotfiles = new ArrayList();

   public void consumeLine(String line) {
      if (line.indexOf("not opened") == -1) {
         switch(this.currentState) {
         case 1:
            if (line.startsWith("//")) {
               this.depotfiles.add(line.trim());
            }
            break;
         default:
            this.error(line);
         }

      }
   }

   private void error(String line) {
      this.currentState = 2;
      this.output.println(line);
   }

   public boolean isSuccess() {
      return this.currentState != 2;
   }

   public List<String> getDepotfiles() {
      return this.depotfiles;
   }
}
