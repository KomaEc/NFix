package org.apache.maven.scm.provider.perforce.command.checkout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.perforce.command.AbstractPerforceConsumer;
import org.apache.maven.scm.provider.perforce.command.PerforceVerbMapper;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceCheckOutConsumer extends AbstractPerforceConsumer implements StreamConsumer {
   public static final int STATE_CLIENTSPEC = 0;
   public static final int STATE_NORMAL = 1;
   public static final int STATE_ERROR = 2;
   private int currentState = 0;
   private Pattern fileRegexp = Pattern.compile("([^#]+)#\\d+ - ([a-z]+)");
   private List<ScmFile> checkedout = new ArrayList();
   private String repo = null;
   private String specname = null;

   public PerforceCheckOutConsumer(String clientspec, String repoPath) {
      this.repo = repoPath;
      this.specname = clientspec;
   }

   public void consumeLine(String line) {
      if (this.currentState != 0 || !line.startsWith("Client " + this.specname + " saved.") && !line.startsWith("Client " + this.specname + " not changed.")) {
         if (this.currentState != 1 || line.indexOf("ile(s) up-to-date") == -1) {
            Matcher matcher;
            if (this.currentState != 2 && (matcher = this.fileRegexp.matcher(line)).find()) {
               String location = matcher.group(1);
               if (location.startsWith(this.repo)) {
                  location = location.substring(this.repo.length() + 1);
               }

               ScmFileStatus status = PerforceVerbMapper.toStatus(matcher.group(2));
               if (status != null) {
                  this.checkedout.add(new ScmFile(location, status));
               }

            } else {
               this.error(line);
            }
         }
      } else {
         this.currentState = 1;
      }
   }

   private void error(String line) {
      this.currentState = 2;
      this.output.println(line);
   }

   public boolean isSuccess() {
      return this.currentState == 1;
   }

   public List<ScmFile> getCheckedout() {
      return this.checkedout;
   }
}
