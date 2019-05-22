package org.apache.maven.scm.provider.svn.command.diff;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SvnDiffConsumer implements StreamConsumer {
   private static final String INDEX_TOKEN = "Index: ";
   private static final String FILE_SEPARATOR_TOKEN = "===";
   private static final String START_REVISION_TOKEN = "---";
   private static final String END_REVISION_TOKEN = "+++";
   private static final String ADDED_LINE_TOKEN = "+";
   private static final String REMOVED_LINE_TOKEN = "-";
   private static final String UNCHANGED_LINE_TOKEN = " ";
   private static final String CHANGE_SEPARATOR_TOKEN = "@@";
   private static final String NO_NEWLINE_TOKEN = "\\ No newline at end of file";
   private ScmLogger logger;
   private String currentFile;
   private StringBuilder currentDifference;
   private List<ScmFile> changedFiles = new ArrayList();
   private Map<String, CharSequence> differences = new HashMap();
   private StringBuilder patch = new StringBuilder();

   public SvnDiffConsumer(ScmLogger logger, File workingDirectory) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (line.startsWith("Index: ")) {
         this.currentFile = line.substring("Index: ".length());
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
