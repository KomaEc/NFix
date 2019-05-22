package org.apache.maven.scm.provider.clearcase.command.update;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.clearcase.util.ClearCaseUtil;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseUpdateConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> updatedFiles = new ArrayList();

   public ClearCaseUpdateConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (line.indexOf(ClearCaseUtil.getLocalizedResource("loading")) > -1) {
         int beginIndex = line.indexOf(34);
         if (beginIndex != -1) {
            String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
            this.updatedFiles.add(new ScmFile(fileName, ScmFileStatus.UPDATED));
         }
      }

   }

   public List<ScmFile> getUpdatedFiles() {
      return this.updatedFiles;
   }
}
