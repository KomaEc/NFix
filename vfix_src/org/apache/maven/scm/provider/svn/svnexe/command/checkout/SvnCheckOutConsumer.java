package org.apache.maven.scm.provider.svn.svnexe.command.checkout;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.svn.svnexe.command.AbstractFileCheckingConsumer;

public class SvnCheckOutConsumer extends AbstractFileCheckingConsumer {
   private static final String CHECKED_OUT_REVISION_TOKEN = "Checked out revision";
   private List<ScmFile> files = new ArrayList();

   public SvnCheckOutConsumer(ScmLogger logger, File workingDirectory) {
      super(logger, workingDirectory);
   }

   protected void parseLine(String line) {
      String statusString = line.substring(0, 1);
      String file = line.substring(3).trim();
      if (file.startsWith(this.getWorkingDirectory().getAbsolutePath())) {
         file = StringUtils.substring(file, this.getWorkingDirectory().getAbsolutePath().length() + 1);
      }

      if (line.startsWith("Checked out revision")) {
         String revisionString = line.substring("Checked out revision".length() + 1, line.length() - 1);
         this.revision = this.parseInt(revisionString);
      } else {
         ScmFileStatus status;
         if (statusString.equals("A")) {
            status = ScmFileStatus.ADDED;
         } else {
            if (!statusString.equals("U")) {
               return;
            }

            status = ScmFileStatus.UPDATED;
         }

         this.addFile(new ScmFile(file, status));
      }
   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.getFiles();
   }

   protected void addFile(ScmFile file) {
      this.files.add(file);
   }

   protected List<ScmFile> getFiles() {
      List<ScmFile> onlyFiles = new ArrayList();
      Iterator i$ = this.files.iterator();

      while(true) {
         ScmFile file;
         do {
            if (!i$.hasNext()) {
               return onlyFiles;
            }

            file = (ScmFile)i$.next();
         } while(!file.getStatus().equals(ScmFileStatus.DELETED) && !(new File(this.getWorkingDirectory(), file.getPath())).isFile() && !file.getStatus().equals(ScmFileStatus.DELETED) && !(new File(this.getWorkingDirectory().getParent(), file.getPath())).isFile());

         onlyFiles.add(file);
      }
   }
}
