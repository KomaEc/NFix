package org.apache.maven.scm.provider.perforce.command.unedit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceUnEditConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   private static final Pattern REVISION_PATTERN = Pattern.compile("^([^#]+)#\\d+ - (.*)");
   private static final int STATE_NORMAL = 1;
   private static final int STATE_ERROR = 2;
   private int currentState = 1;
   private List<ScmFile> edits = new ArrayList();

   public List<ScmFile> getEdits() {
      return this.edits;
   }

   public void consumeLine(String line) {
      Matcher matcher = REVISION_PATTERN.matcher(line);
      if (this.currentState != 2 && matcher.matches()) {
         this.edits.add(new ScmFile(matcher.group(1), ScmFileStatus.UNKNOWN));
      } else {
         this.error(line);
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
