package org.apache.maven.scm.provider.git.gitexe.command.status;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitStatusConsumer implements StreamConsumer {
   private static final Pattern ADDED_PATTERN = Pattern.compile("^A[ M]* (.*)$");
   private static final Pattern MODIFIED_PATTERN = Pattern.compile("^ *M[ M]* (.*)$");
   private static final Pattern DELETED_PATTERN = Pattern.compile("^ *D * (.*)$");
   private static final Pattern RENAMED_PATTERN = Pattern.compile("^R  (.*) -> (.*)$");
   private ScmLogger logger;
   private File workingDirectory;
   private List<ScmFile> changedFiles;
   private URI relativeRepositoryPath;

   public GitStatusConsumer(ScmLogger logger, File workingDirectory) {
      this.changedFiles = new ArrayList();
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public GitStatusConsumer(ScmLogger logger, File workingDirectory, URI relativeRepositoryPath) {
      this(logger, workingDirectory);
      this.relativeRepositoryPath = relativeRepositoryPath;
   }

   public void consumeLine(String line) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug(line);
      }

      if (!StringUtils.isEmpty(line)) {
         ScmFileStatus status = null;
         List<String> files = new ArrayList();
         Matcher matcher;
         if ((matcher = ADDED_PATTERN.matcher(line)).find()) {
            status = ScmFileStatus.ADDED;
            files.add(resolvePath(matcher.group(1), this.relativeRepositoryPath));
         } else if ((matcher = MODIFIED_PATTERN.matcher(line)).find()) {
            status = ScmFileStatus.MODIFIED;
            files.add(resolvePath(matcher.group(1), this.relativeRepositoryPath));
         } else if ((matcher = DELETED_PATTERN.matcher(line)).find()) {
            status = ScmFileStatus.DELETED;
            files.add(resolvePath(matcher.group(1), this.relativeRepositoryPath));
         } else {
            if (!(matcher = RENAMED_PATTERN.matcher(line)).find()) {
               this.logger.warn("Ignoring unrecognized line: " + line);
               return;
            }

            status = ScmFileStatus.RENAMED;
            files.add(resolvePath(matcher.group(1), this.relativeRepositoryPath));
            files.add(resolvePath(matcher.group(2), this.relativeRepositoryPath));
            this.logger.debug("RENAMED status for line '" + line + "' files added '" + matcher.group(1) + "' '" + matcher.group(2));
         }

         if (!files.isEmpty() && status != null) {
            String newFilePath;
            if (this.workingDirectory != null) {
               if (status == ScmFileStatus.RENAMED) {
                  String oldFilePath = (String)files.get(0);
                  newFilePath = (String)files.get(1);
                  if (this.isFile(oldFilePath)) {
                     this.logger.debug("file '" + oldFilePath + "' is a file");
                     return;
                  }

                  this.logger.debug("file '" + oldFilePath + "' not a file");
                  if (!this.isFile(newFilePath)) {
                     this.logger.debug("file '" + newFilePath + "' not a file");
                     return;
                  }

                  this.logger.debug("file '" + newFilePath + "' is a file");
               } else if (status == ScmFileStatus.DELETED) {
                  if (this.isFile((String)files.get(0))) {
                     return;
                  }
               } else if (!this.isFile((String)files.get(0))) {
                  return;
               }
            }

            Iterator i$ = files.iterator();

            while(i$.hasNext()) {
               newFilePath = (String)i$.next();
               this.changedFiles.add(new ScmFile(newFilePath, status));
            }
         }

      }
   }

   private boolean isFile(String file) {
      File targetFile;
      if (this.relativeRepositoryPath == null) {
         targetFile = new File(this.workingDirectory, file);
      } else {
         targetFile = new File(this.relativeRepositoryPath.getPath(), file);
      }

      return targetFile.isFile();
   }

   protected static String resolvePath(String fileEntry, URI path) {
      return path != null ? resolveURI(fileEntry, path).getPath() : fileEntry;
   }

   public static URI resolveURI(String fileEntry, URI path) {
      String str = fileEntry.replace(" ", "%20");
      return path.relativize(URI.create(str));
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }
}
