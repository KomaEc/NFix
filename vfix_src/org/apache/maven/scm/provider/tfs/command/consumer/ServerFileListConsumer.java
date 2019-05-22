package org.apache.maven.scm.provider.tfs.command.consumer;

import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.codehaus.plexus.util.StringUtils;

public class ServerFileListConsumer extends FileListConsumer {
   protected ScmFile getScmFile(String filename) {
      if (filename.startsWith("$")) {
         filename = StringUtils.replace(filename, "$", "", -1);
      }

      String path = this.currentDir + "/" + filename;
      return new ScmFile(path, ScmFileStatus.UNKNOWN);
   }
}
