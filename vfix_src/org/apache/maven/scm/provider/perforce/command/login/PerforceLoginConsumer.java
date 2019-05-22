package org.apache.maven.scm.provider.perforce.command.login;

import java.util.regex.Pattern;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceLoginConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   private static final Pattern LOGIN_PATTERN = Pattern.compile("^User [^ ]+ logged in.$");
   public static final int STATE_LOGIN = 1;
   public static final int STATE_ERROR = 2;
   private int currentState = 1;

   public void consumeLine(String line) {
      if (!line.startsWith("Enter password:")) {
         if (this.currentState == 2 || !LOGIN_PATTERN.matcher(line).matches()) {
            this.error(line);
         }
      }
   }

   private void error(String line) {
      this.currentState = 2;
      this.output.println(line);
   }

   public boolean isSuccess() {
      return this.currentState == 1;
   }
}
