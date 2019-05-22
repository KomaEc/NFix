package org.apache.maven.scm.provider.clearcase.command.tag;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseTagConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> taggedFiles = new ArrayList();

   public ClearCaseTagConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      int beginIndexTag = line.indexOf(34);
      if (beginIndexTag != -1) {
         int endIndexTag = line.indexOf(34, beginIndexTag + 1);
         if (endIndexTag != -1) {
            int beginIndex = line.indexOf(34, endIndexTag + 1);
            if (beginIndex != -1) {
               String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
               this.taggedFiles.add(new ScmFile(fileName, ScmFileStatus.TAGGED));
            }
         }
      }

   }

   public List<ScmFile> getTaggedFiles() {
      return this.taggedFiles;
   }
}
