package org.apache.maven.scm.provider.perforce.command.remove;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceRemoveConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   private static final String FILE_BEGIN_TOKEN = "//";
   private static final Pattern REVISION_PATTERN = Pattern.compile("^([^#]+)#\\d+ - (.*)");
   private List<ScmFile> removals = new ArrayList();
   private boolean error = false;

   public List<ScmFile> getRemovals() {
      return this.removals;
   }

   public void consumeLine(String line) {
      if (!line.startsWith("... ")) {
         if (!line.startsWith("//")) {
            this.error(line);
         }

         Matcher matcher = REVISION_PATTERN.matcher(line);
         if (!matcher.matches()) {
            this.error(line);
         }

         this.removals.add(new ScmFile(matcher.group(1), ScmFileStatus.DELETED));
      }
   }

   private void error(String line) {
      this.error = true;
      this.output.println(line);
   }

   public boolean isSuccess() {
      return !this.error;
   }
}
