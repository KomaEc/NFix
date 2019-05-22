package com.gzoltar.shaded.org.pitest.mutationtest.config;

import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class DirectoryResultOutputStrategy implements ResultOutputStrategy {
   private final File reportDir;

   public DirectoryResultOutputStrategy(String baseDir, ReportDirCreationStrategy dirCreationStrategy) {
      this.reportDir = dirCreationStrategy.createReportDir(baseDir);
   }

   public Writer createWriterForFile(String file) {
      try {
         int fileSepIndex = file.lastIndexOf(File.separatorChar);
         if (fileSepIndex > 0) {
            String directory = this.reportDir.getAbsolutePath() + File.separatorChar + file.substring(0, fileSepIndex);
            File directoryFile = new File(directory);
            if (!directoryFile.exists()) {
               directoryFile.mkdirs();
            }
         }

         return new BufferedWriter(new FileWriter(this.reportDir.getAbsolutePath() + File.separatorChar + file));
      } catch (IOException var5) {
         throw Unchecked.translateCheckedException(var5);
      }
   }
}
