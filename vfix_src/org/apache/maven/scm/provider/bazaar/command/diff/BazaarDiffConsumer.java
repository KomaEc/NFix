package org.apache.maven.scm.provider.bazaar.command.diff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.bazaar.command.BazaarConsumer;

public class BazaarDiffConsumer extends BazaarConsumer {
   private static final String MODIFIED_FILE_TOKEN = "=== modified file ";
   private static final String ADDED_FILE_TOKEN = "=== added file ";
   private static final String DELETED_FILE_TOKEN = "=== deleted file ";
   private static final String NO_NEWLINE_TOKEN = "\\ No newline at end of file";
   private static final String FROM_FILE_TOKEN = "---";
   private static final String TO_FILE_TOKEN = "+++";
   private static final String ADDED_LINE_TOKEN = "+";
   private static final String REMOVED_LINE_TOKEN = "-";
   private static final String UNCHANGED_LINE_TOKEN = " ";
   private static final String RANGE_TOKEN = "@@";
   private ScmLogger logger;
   private File workingDirectory;
   private String currentFile;
   private StringBuilder currentDifference;
   private List<ScmFile> changedFiles = new ArrayList();
   private Map<String, CharSequence> differences = new HashMap();
   private StringBuilder patch = new StringBuilder();

   public BazaarDiffConsumer(ScmLogger logger, File workingDirectory) {
      super(logger);
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public void doConsume(ScmFileStatus status, String line) {
      new String(line);
      this.patch.append(line).append("\n");
      String tmpLine;
      if (line.startsWith("=== modified file ")) {
         tmpLine = line.substring("=== modified file ".length());
         tmpLine = tmpLine.trim();
         status = ScmFileStatus.MODIFIED;
         this.addChangedFile(status, line, tmpLine);
      } else if (line.startsWith("=== added file ")) {
         tmpLine = line.substring("=== added file ".length());
         tmpLine = tmpLine.trim();
         status = ScmFileStatus.ADDED;
         this.addChangedFile(status, line, tmpLine);
      } else if (line.startsWith("=== deleted file ")) {
         tmpLine = line.substring("=== deleted file ".length());
         tmpLine = tmpLine.trim();
         status = ScmFileStatus.DELETED;
         this.addChangedFile(status, line, tmpLine);
      } else if (!line.startsWith("+++") && !line.startsWith("---") && (line.startsWith("+") || line.startsWith("-") || line.startsWith(" ") || line.startsWith("@@") || line.startsWith("\\ No newline at end of file"))) {
         this.currentDifference.append(line).append("\n");
      }

   }

   private void addChangedFile(ScmFileStatus status, String line, String tmpLine) {
      tmpLine = tmpLine.substring(1, tmpLine.length() - 1);
      boolean ok = this.addChangedFile(status, tmpLine);
      if (!ok) {
         int index = tmpLine.indexOf(47);
         if (index > -1) {
            tmpLine = tmpLine.substring(index + 1);
            ok = this.addChangedFile(status, tmpLine);
         }
      }

      if (!ok && this.logger.isWarnEnabled()) {
         this.logger.warn("Could not figure out of line: " + line);
      }

   }

   private boolean addChangedFile(ScmFileStatus status, String tmpLine) {
      File tmpFile = new File(this.workingDirectory, tmpLine);
      if (status.equals(ScmFileStatus.DELETED)) {
         return true;
      } else if (tmpFile.isFile()) {
         this.currentFile = tmpLine;
         this.currentDifference = new StringBuilder();
         this.differences.put(this.currentFile, this.currentDifference);
         this.changedFiles.add(new ScmFile(tmpLine, status));
         return true;
      } else {
         return false;
      }
   }

   public List<ScmFile> getChangedFiles() {
      return this.changedFiles;
   }

   public Map<String, CharSequence> getDifferences() {
      return this.differences;
   }

   public String getPatch() {
      return this.patch.toString();
   }
}
