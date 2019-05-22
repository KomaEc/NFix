package org.apache.maven.scm.provider.perforce.command.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceEditConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   private static final Pattern PATTERN = Pattern.compile("^([^#]+)#\\d+ - (.*)");
   private static final String FILE_BEGIN_TOKEN = "//";
   private List<ScmFile> edits = new ArrayList();
   private boolean errors = false;
   private StringBuilder errorMessage = new StringBuilder();

   public List<ScmFile> getEdits() {
      return this.edits;
   }

   public void consumeLine(String line) {
      if (!line.startsWith("... ")) {
         if (!line.startsWith("//")) {
            this.error(line);
         }

         Matcher matcher = PATTERN.matcher(line);
         if (!matcher.matches()) {
            this.error(line);
         }

         this.edits.add(new ScmFile(matcher.group(1), ScmFileStatus.EDITED));
      }
   }

   private void error(String line) {
      this.errors = true;
      this.output.println(line);
      if (this.errorMessage.length() > 0) {
         this.errorMessage.append(System.getProperty("line.separator"));
      }

      this.errorMessage.append(line);
   }

   public boolean isSuccess() {
      return !this.errors;
   }

   public String getErrorMessage() {
      return this.errorMessage.toString();
   }
}
