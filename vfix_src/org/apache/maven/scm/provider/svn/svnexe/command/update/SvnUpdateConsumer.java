package org.apache.maven.scm.provider.svn.svnexe.command.update;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.svn.svnexe.command.AbstractFileCheckingConsumer;

public class SvnUpdateConsumer extends AbstractFileCheckingConsumer {
   private static final String UPDATED_TO_REVISION_TOKEN = "Updated to revision";
   private static final String AT_REVISION_TOKEN = "At revision";
   private static final String EXPORTED_REVISION_TOKEN = "Exported revision";
   private static final String RESTORED_TOKEN = "Restored";
   private List<ChangeSet> changeSets = new ArrayList();

   public SvnUpdateConsumer(ScmLogger logger, File workingDirectory) {
      super(logger, workingDirectory);
   }

   protected void parseLine(String line) {
      line = line.trim();
      String statusString = line.substring(0, 1);
      String file = line.substring(3).trim();
      if (file.startsWith(this.workingDirectory.getAbsolutePath())) {
         if (file.length() == this.workingDirectory.getAbsolutePath().length()) {
            file = ".";
         } else {
            file = file.substring(this.workingDirectory.getAbsolutePath().length() + 1);
         }
      }

      String revisionString;
      if (line.startsWith("Updated to revision")) {
         revisionString = line.substring("Updated to revision".length() + 1, line.length() - 1);
         this.revision = this.parseInt(revisionString);
      } else if (line.startsWith("At revision")) {
         revisionString = line.substring("At revision".length() + 1, line.length() - 1);
         this.revision = this.parseInt(revisionString);
      } else if (line.startsWith("Exported revision")) {
         revisionString = line.substring("Exported revision".length() + 1, line.length() - 1);
         this.revision = this.parseInt(revisionString);
      } else if (!line.startsWith("Restored")) {
         ScmFileStatus status;
         if (statusString.equals("A")) {
            status = ScmFileStatus.ADDED;
         } else if (!statusString.equals("U") && !statusString.equals("M")) {
            if (!statusString.equals("D")) {
               return;
            }

            status = ScmFileStatus.DELETED;
         } else {
            status = ScmFileStatus.UPDATED;
         }

         this.addFile(new ScmFile(file, status));
         List<ChangeFile> changeFiles = Arrays.asList(new ChangeFile(line, Integer.valueOf(this.revision).toString()));
         ChangeSet changeSet = new ChangeSet((Date)null, (String)null, (String)null, changeFiles);
         this.changeSets.add(changeSet);
      }
   }

   public List<ScmFile> getUpdatedFiles() {
      return this.getFiles();
   }

   public List<ChangeSet> getChangeSets() {
      return this.changeSets;
   }

   public void setChangeSets(List<ChangeSet> changeSets) {
      this.changeSets = changeSets;
   }
}
