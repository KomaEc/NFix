package org.apache.maven.scm.provider.jazz.command.checkout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.jazz.command.consumer.AbstractRepositoryConsumer;

public class JazzCheckOutConsumer extends AbstractRepositoryConsumer {
   private static final Pattern DOWNLOAD_PATTERN = Pattern.compile("^Downloading\\s(.*)\\s\\s\\(\\d.*B\\)$");
   protected String fCurrentDir = "";
   private List<ScmFile> fCheckedOutFiles = new ArrayList();

   public JazzCheckOutConsumer(ScmProviderRepository repository, ScmLogger logger) {
      super(repository, logger);
   }

   public void consumeLine(String line) {
      Matcher matcher = DOWNLOAD_PATTERN.matcher(line);
      if (matcher.matches()) {
         this.fCheckedOutFiles.add(new ScmFile(matcher.group(1), ScmFileStatus.CHECKED_OUT));
      }

   }

   public List<ScmFile> getCheckedOutFiles() {
      return this.fCheckedOutFiles;
   }
}
