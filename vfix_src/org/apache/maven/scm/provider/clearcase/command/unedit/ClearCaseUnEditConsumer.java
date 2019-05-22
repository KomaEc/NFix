package org.apache.maven.scm.provider.clearcase.command.unedit;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClearCaseUnEditConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> unEditFiles = new ArrayList();

   public ClearCaseUnEditConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (line.indexOf("Checkout cancelled") > -1) {
         int beginIndex = line.indexOf(34);
         if (beginIndex != -1) {
            String fileName = line.substring(beginIndex + 1, line.indexOf(34, beginIndex + 1));
            this.unEditFiles.add(new ScmFile(fileName, ScmFileStatus.UNKNOWN));
         }
      }

   }

   public List<ScmFile> getUnEditFiles() {
      return this.unEditFiles;
   }
}
