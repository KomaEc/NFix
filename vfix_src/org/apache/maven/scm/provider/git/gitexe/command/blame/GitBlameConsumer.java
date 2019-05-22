package org.apache.maven.scm.provider.git.gitexe.command.blame;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class GitBlameConsumer extends AbstractConsumer {
   private static final String GIT_COMMITTER_PREFIX = "committer";
   private static final String GIT_COMMITTER = "committer ";
   private static final String GIT_COMMITTER_TIME = "committer-time ";
   private static final String GIT_AUTHOR = "author ";
   private List<BlameLine> lines = new ArrayList();
   private Map<String, BlameLine> commitInfo = new HashMap();
   private boolean expectRevisionLine = true;
   private String revision = null;
   private String author = null;
   private String committer = null;
   private Date time = null;

   public GitBlameConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      if (line != null) {
         if (this.expectRevisionLine) {
            String[] parts = line.split("\\s", 4);
            if (parts.length >= 1) {
               this.revision = parts[0];
               BlameLine oldLine = (BlameLine)this.commitInfo.get(this.revision);
               if (oldLine != null) {
                  this.author = oldLine.getAuthor();
                  this.committer = oldLine.getCommitter();
                  this.time = oldLine.getDate();
               }

               this.expectRevisionLine = false;
            }
         } else {
            if (line.startsWith("author ")) {
               this.author = line.substring("author ".length());
               return;
            }

            if (line.startsWith("committer ")) {
               this.committer = line.substring("committer ".length());
               return;
            }

            if (line.startsWith("committer-time ")) {
               String timeStr = line.substring("committer-time ".length());
               this.time = new Date(Long.parseLong(timeStr) * 1000L);
               return;
            }

            if (line.startsWith("\t")) {
               BlameLine blameLine = new BlameLine(this.time, this.revision, this.author, this.committer);
               this.getLines().add(blameLine);
               this.commitInfo.put(this.revision, blameLine);
               if (this.getLogger().isDebugEnabled()) {
                  DateFormat df = SimpleDateFormat.getDateTimeInstance();
                  this.getLogger().debug(this.author + " " + df.format(this.time));
               }

               this.expectRevisionLine = true;
            }
         }

      }
   }

   public List<BlameLine> getLines() {
      return this.lines;
   }
}
