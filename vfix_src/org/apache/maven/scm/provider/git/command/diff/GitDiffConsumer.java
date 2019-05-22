package org.apache.maven.scm.provider.git.command.diff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class GitDiffConsumer implements StreamConsumer {
   private static final Pattern DIFF_FILES_PATTERN = Pattern.compile("^diff --git\\sa/(.*)\\sb/(.*)");
   private static final String START_REVISION_TOKEN = "---";
   private static final String END_REVISION_TOKEN = "+++";
   private static final String ADDED_LINE_TOKEN = "+";
   private static final String REMOVED_LINE_TOKEN = "-";
   private static final String UNCHANGED_LINE_TOKEN = " ";
   private static final String CHANGE_SEPARATOR_TOKEN = "@@";
   private static final String NO_NEWLINE_TOKEN = "\\ No newline at end of file";
   private static final String INDEX_LINE_TOKEN = "index ";
   private static final String NEW_FILE_MODE_TOKEN = "new file mode ";
   private static final String DELETED_FILE_MODE_TOKEN = "deleted file mode ";
   private ScmLogger logger;
   private String currentFile;
   private StringBuilder currentDifference;
   private List<ScmFile> changedFiles = new ArrayList();
   private Map<String, CharSequence> differences = new HashMap();
   private StringBuilder patch = new StringBuilder();

   public GitDiffConsumer(ScmLogger logger, File workingDirectory) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      Matcher matcher = DIFF_FILES_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.currentFile = matcher.group(1);
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
         if (line.startsWith("index ")) {
            this.patch.append(line).append("\n");
         } else if (!line.startsWith("new file mode ") && !line.startsWith("deleted file mode ")) {
            if (line.startsWith("---")) {
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
         } else {
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
