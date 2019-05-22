package org.apache.maven.scm.provider.hg.command.diff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

public class HgDiffConsumer extends HgConsumer {
   private static final String INDEX_TOKEN = "diff -r ";
   private static final String FILE_SEPARATOR_TOKEN = "===";
   private static final String START_REVISION_TOKEN = "---";
   private static final String END_REVISION_TOKEN = "+++";
   private static final String ADDED_LINE_TOKEN = "+";
   private static final String REMOVED_LINE_TOKEN = "-";
   private static final String UNCHANGED_LINE_TOKEN = " ";
   private static final String CHANGE_SEPARATOR_TOKEN = "@@";
   private static final String NO_NEWLINE_TOKEN = "\\ No newline at end of file";
   private static final int HASH_ID_LEN = 12;
   private ScmLogger logger;
   private String currentFile;
   private StringBuilder currentDifference;
   private List<ScmFile> changedFiles = new ArrayList();
   private Map<String, CharSequence> differences = new HashMap();
   private StringBuilder patch = new StringBuilder();
   private File workingDirectory;

   public HgDiffConsumer(ScmLogger logger, File workingDirectory) {
      super(logger);
      this.logger = logger;
      this.workingDirectory = workingDirectory;
   }

   public void consumeLine(String line) {
      if (line.startsWith("diff -r ")) {
         this.currentFile = line.substring("diff -r ".length() + 12 + 1);
         this.changedFiles.add(new ScmFile(this.currentFile, ScmFileStatus.MODIFIED));
         this.currentDifference = new StringBuilder();
         this.differences.put(this.currentFile, this.currentDifference);
         this.patch.append(line).append("\n");
      } else if (this.currentFile == null) {
         if (this.logger.isWarnEnabled()) {
            this.logger.warn("Unparseable line: '" + line + "'");
         }

         this.patch.append(line).append("\n");
      } else {
         if (line.startsWith("===")) {
            this.patch.append(line).append("\n");
         } else if (line.startsWith("---")) {
            this.patch.append(line).append("\n");
         } else if (line.startsWith("+++")) {
            this.patch.append(line).append("\n");
         } else if (!line.startsWith("+") && !line.startsWith("-") && !line.startsWith(" ") && !line.startsWith("@@") && !line.equals("\\ No newline at end of file")) {
            if (this.logger.isWarnEnabled()) {
               this.logger.warn("Unparseable line: '" + line + "'");
            }

            this.patch.append(line).append("\n");
            this.currentFile = null;
            this.currentDifference = null;
         } else {
            this.currentDifference.append(line).append("\n");
            this.patch.append(line).append("\n");
         }

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
