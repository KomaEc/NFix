package org.apache.maven.scm.provider.svn.svnexe.command.list;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnListConsumer implements StreamConsumer {
   private List<ScmFile> files = new ArrayList();

   public void consumeLine(String line) {
      this.files.add(new ScmFile(line, ScmFileStatus.CHECKED_IN));
   }

   public List<ScmFile> getFiles() {
      return this.files;
   }
}
