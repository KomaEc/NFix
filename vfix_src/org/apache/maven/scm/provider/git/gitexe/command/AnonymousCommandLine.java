package org.apache.maven.scm.provider.git.gitexe.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.cli.Commandline;

public class AnonymousCommandLine extends Commandline {
   public static final String PASSWORD_PLACE_HOLDER = "********";
   private Pattern passwordPattern = Pattern.compile("^.*:(.*)@.*$");

   public String toString() {
      String output = super.toString();
      Matcher passwordMatcher = this.passwordPattern.matcher(output);
      if (passwordMatcher.find()) {
         String clearPassword = passwordMatcher.group(1);
         output = output.replace(clearPassword, "********");
      }

      return output;
   }
}
