package org.apache.maven.scm.provider.jazz.command.update;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzUpdateConsumer extends AbstractRepositoryConsumer {
   public static final String UPDATE_CMD_ADD_FLAG = "-a-";
   public static final String UPDATE_CMD_CHANGE_FLAG = "--c";
   public static final String UPDATE_CMD_DELETE_FLAG = "-d-";
   public static final String UPDATE_CMD_MOVED_FLAG = "-m-";
   private List<ScmFile> fUpdatedFiles = new ArrayList();

   public JazzUpdateConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(repository, logger);
   }

   public void consumeLine(String line) {
      super.consumeLine(line);
      if (this.containsStatusFlag(line)) {
         this.extractUpdatedFile(line);
      }

   }

   private boolean containsStatusFlag(String line) {
      boolean containsStatusFlag = false;
      if (line.trim().length() > 3) {
         String flag = line.trim().substring(0, 3);
         if ("-a-".equals(flag) || "--c".equals(flag) || "-d-".equals(flag) || "-m-".equals(flag)) {
            containsStatusFlag = true;
         }
      }

      return containsStatusFlag;
   }

   private void extractUpdatedFile(String line) {
      String filePath = "";
      String flag = line.trim().substring(0, 3);
      ScmFileStatus status = ScmFileStatus.UNKNOWN;
      if ("-a-".equals(flag)) {
         status = ScmFileStatus.ADDED;
         filePath = line.trim().substring(4);
      }

      if ("--c".equals(flag)) {
         status = ScmFileStatus.UPDATED;
         filePath = line.trim().substring(4);
      }

      if ("-d-".equals(flag)) {
         status = ScmFileStatus.DELETED;
         filePath = line.trim().substring(4);
      }

      if ("-m-".equals(flag)) {
         status = ScmFileStatus.ADDED;
         String pattern = "^-m-\\s(.*)\\s\\(moved\\sfrom\\s.*$";
         Pattern r = Pattern.compile(pattern);
         Matcher m = r.matcher(line.trim());
         if (m.find()) {
            filePath = m.group(1);
         }
      }

      this.fUpdatedFiles.add(new ScmFile(filePath, status));
   }

   public List<ScmFile> getUpdatedFiles() {
      return this.fUpdatedFiles;
   }
}
