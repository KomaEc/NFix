package org.apache.maven.scm.provider.perforce.command.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceTagConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   private static final Pattern LABEL_PATTERN = Pattern.compile("^Label ([^ ]+) saved.$");
   private static final Pattern SYNC_PATTERN = Pattern.compile("^([^#]+)#\\d+ - (.*)");
   public static final int STATE_CREATE = 1;
   public static final int STATE_SYNC = 2;
   public static final int STATE_ERROR = 3;
   private int currentState = 1;
   private List<ScmFile> tagged = new ArrayList();

   public List<ScmFile> getTagged() {
      return this.tagged;
   }

   public void consumeLine(String line) {
      switch(this.currentState) {
      case 1:
         if (!LABEL_PATTERN.matcher(line).matches()) {
            this.error(line);
         } else {
            this.currentState = 2;
         }
         break;
      case 2:
         Matcher matcher = SYNC_PATTERN.matcher(line);
         if (!matcher.matches()) {
            this.error(line);
         } else {
            this.tagged.add(new ScmFile(matcher.group(1), ScmFileStatus.TAGGED));
         }
         break;
      default:
         this.error(line);
      }

   }

   private void error(String line) {
      this.currentState = 3;
      this.output.println(line);
   }

   public boolean isSuccess() {
      return this.currentState == 2;
   }
}
