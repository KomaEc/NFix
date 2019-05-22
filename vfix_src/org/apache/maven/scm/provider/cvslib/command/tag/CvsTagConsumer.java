package org.apache.maven.scm.provider.cvslib.command.tag;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class CvsTagConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> files = new ArrayList();

   public CvsTagConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (line.length() < 3) {
         if (StringUtils.isNotEmpty(line) && this.logger.isWarnEnabled()) {
            this.logger.warn("Unable to parse output from command: line length must be bigger than 3. (" + line + ").");
         }

      } else {
         String status = line.substring(0, 2);
         String file = line.substring(2);
         if (status.equals("T ")) {
            this.files.add(new ScmFile(file, ScmFileStatus.TAGGED));
         } else if (this.logger.isWarnEnabled()) {
            this.logger.warn("Unknown status: '" + status + "'.");
         }

      }
   }

   public List<ScmFile> getTaggedFiles() {
      return this.files;
   }
}
