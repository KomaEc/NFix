package org.apache.maven.scm.provider.cvslib.command.list;

import java.util.LinkedList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class CvsListConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> entries;

   public CvsListConsumer(ScmLogger logger) {
      this.logger = logger;
      this.entries = new LinkedList();
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      String[] params = line.split("/");
      if (params.length < 2) {
         if (StringUtils.isNotEmpty(line) && this.logger.isWarnEnabled()) {
            this.logger.warn("Unable to parse it as CVS/Entries format: " + line + ".");
         }
      } else {
         this.entries.add(new ScmFile(params[1], ScmFileStatus.UNKNOWN));
      }

   }

   public List<ScmFile> getEntries() {
      return this.entries;
   }
}
