package org.apache.maven.scm.provider.git.gitexe.command.remove;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitRemoveConsumer implements StreamConsumer {
   private static final Pattern REMOVED_PATTERN = Pattern.compile("^rm\\s'(.*)'");
   private ScmLogger logger;
   private List<ScmFile> removedFiles = new ArrayList();

   public GitRemoveConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (line.length() > 2) {
         Matcher matcher = REMOVED_PATTERN.matcher(line);
         if (matcher.matches()) {
            String file = matcher.group(1);
            this.removedFiles.add(new ScmFile(file, ScmFileStatus.DELETED));
         } else {
            if (this.logger.isInfoEnabled()) {
               this.logger.info("could not parse line: " + line);
            }

         }
      }
   }

   public List<ScmFile> getRemovedFiles() {
      return this.removedFiles;
   }
}
