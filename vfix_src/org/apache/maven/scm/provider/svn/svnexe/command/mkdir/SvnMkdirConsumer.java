package org.apache.maven.scm.provider.svn.svnexe.command.mkdir;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnMkdirConsumer implements StreamConsumer {
   private ScmLogger logger;
   private static final String COMMITTED_REVISION_TOKEN = "Committed revision";
   private int revision;
   private List<ScmFile> createdDirs = new ArrayList();

   public SvnMkdirConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (!StringUtils.isBlank(line)) {
         String statusString = line.substring(0, 1);
         String file;
         if (line.startsWith("Committed revision")) {
            file = line.substring("Committed revision".length() + 1, line.length() - 1);
            this.revision = Integer.parseInt(file);
         } else if (statusString.equals("A")) {
            file = line.substring(3);
            ScmFileStatus status = ScmFileStatus.ADDED;
            this.createdDirs.add(new ScmFile(file, status));
         } else {
            if (this.logger.isInfoEnabled()) {
               this.logger.info("Unknown line: '" + line + "'");
            }

         }
      }
   }

   public int getRevision() {
      return this.revision;
   }

   public List<ScmFile> getCreatedDirs() {
      return this.createdDirs;
   }
}
