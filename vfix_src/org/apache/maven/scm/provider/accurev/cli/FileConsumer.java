package org.apache.maven.scm.provider.accurev.cli;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.cli.StreamConsumer;

class FileConsumer implements StreamConsumer {
   private Pattern filePattern;
   private List<File> matchedFiles;
   public static final Pattern ADD_PATTERN = Pattern.compile("Added and kept element [/\\\\]\\.[/\\\\](\\S+)\\s*");
   public static final Pattern UPDATE_PATTERN = Pattern.compile("Updating element [/\\\\]\\.[/\\\\](\\S+)\\s*|Content.*of \"(.*)\".*");
   public static final Pattern POPULATE_PATTERN = Pattern.compile("Populating element [/\\\\]\\.[/\\\\](\\S+)\\s*");
   public static final Pattern PROMOTE_PATTERN = Pattern.compile("Promoted element [/\\\\]\\.[/\\\\](\\S+)\\s*");
   public static final Pattern STAT_PATTERN = Pattern.compile("[/\\\\]\\.[/\\\\](.*)");
   public static final Pattern DEFUNCT_PATTERN = Pattern.compile("Removing \"(\\S+)\".*");

   public FileConsumer(List<File> matchedFilesAccumulator, Pattern filematcher) {
      this.matchedFiles = matchedFilesAccumulator;
      this.filePattern = filematcher;
   }

   public void consumeLine(String line) {
      Matcher m = this.filePattern.matcher(line);
      if (m.matches()) {
         int i = 1;

         String fileName;
         for(fileName = null; fileName == null && i <= m.groupCount(); fileName = m.group(i++)) {
         }

         if (fileName != null) {
            this.matchedFiles.add(new File(fileName));
         }
      }

   }
}
