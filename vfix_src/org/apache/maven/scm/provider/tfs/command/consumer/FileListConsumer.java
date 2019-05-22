package org.apache.maven.scm.provider.tfs.command.consumer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class FileListConsumer implements StreamConsumer {
   private boolean fed = false;
   protected String currentDir = "";
   private List<ScmFile> files = new ArrayList();

   public void consumeLine(String line) {
      this.fed = true;
      if (line.endsWith(":")) {
         this.currentDir = line.substring(0, line.lastIndexOf(58));
         ScmFile scmFile = new ScmFile(this.currentDir, ScmFileStatus.CHECKED_OUT);
         if (!this.files.contains(scmFile)) {
            this.files.add(scmFile);
         }
      } else if (line.trim().equals("")) {
         this.currentDir = "";
      } else if (!this.currentDir.equals("") && line.indexOf(32) >= 0) {
         String filename = line.split(" ")[1];
         this.files.add(this.getScmFile(filename));
      } else {
         this.files.add(this.getScmFile(line));
      }

   }

   protected ScmFile getScmFile(String filename) {
      return new ScmFile((new File(this.currentDir, filename)).getAbsolutePath(), ScmFileStatus.CHECKED_OUT);
   }

   public List<ScmFile> getFiles() {
      return this.files;
   }

   public boolean hasBeenFed() {
      return this.fed;
   }
}
