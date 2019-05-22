package org.apache.maven.scm.provider.vss.commands.update;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.vss.repository.VssScmProviderRepository;
import org.apache.maven.scm.util.AbstractConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class VssUpdateConsumer extends AbstractConsumer implements StreamConsumer {
   private static final int GET_UNKNOWN = 0;
   private static final int GET_FILE = 1;
   private static final int REPLACE_FILE = 2;
   private static final int GET_FILE_PATH = 3;
   private static final int IS_WRITABLE_COPY = 4;
   private static final int SET_WORKING_FOLDER = 5;
   private static final String START_FILE_PATH = "$/";
   private static final String START_GETTING = "Getting";
   private static final String START_REPLACING = "Replacing local copy of ";
   private static final String START_WRITABLE_COPY = "A writable ";
   private static final String CONTAINS_SET_DEFAULT_WORKING_FOLDER = "as the default folder for project";
   private String currentPath = "";
   private List<ScmFile> updatedFiles = new ArrayList();
   private VssScmProviderRepository repo;

   public VssUpdateConsumer(VssScmProviderRepository repo, ScmLogger logger) {
      super(logger);
      this.repo = repo;
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(line);
      }

      switch(this.getLineStatus(line)) {
      case 1:
         this.processGetFile(line);
         break;
      case 2:
         this.processReplaceFile(line);
         break;
      case 3:
         this.processGetFilePath(line);
         break;
      case 4:
         this.processWritableFile(line);
      case 5:
      }

   }

   private void processGetFile(String line) {
      String[] fileLine = line.split(" ");
      this.updatedFiles.add(new ScmFile(this.currentPath + "/" + fileLine[1], ScmFileStatus.UPDATED));
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info(fileLine[0] + ": " + this.currentPath + "/" + fileLine[1]);
      }

   }

   private void processReplaceFile(String line) {
      this.updatedFiles.add(new ScmFile(this.currentPath + "/" + line.substring("Replacing local copy of ".length()), ScmFileStatus.UPDATED));
      if (this.getLogger().isInfoEnabled()) {
         this.getLogger().info("Replacing local copy of " + this.currentPath + "/" + line.substring("Replacing local copy of ".length()));
      }

   }

   private void processGetFilePath(String line) {
      this.currentPath = line.substring(("$" + this.repo.getProject()).length(), line.length() - 1);
   }

   private void processWritableFile(String line) {
   }

   private int getLineStatus(String line) {
      int argument = 0;
      if (line.startsWith("$/")) {
         argument = 3;
      } else if (line.startsWith("Getting")) {
         argument = 1;
      } else if (line.startsWith("Replacing local copy of ")) {
         argument = 2;
      } else if (line.startsWith("A writable ")) {
         argument = 4;
      } else if (line.indexOf("as the default folder for project") != -1) {
         argument = 5;
      }

      return argument;
   }

   public List<ScmFile> getUpdatedFiles() {
      return this.updatedFiles;
   }
}
