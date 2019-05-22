package org.apache.maven.scm.provider.starteam.command.tag;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class StarteamTagConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<ScmFile> tags = new ArrayList();

   public StarteamTagConsumer(ScmLogger logger) {
      this.logger = logger;
   }

   public void consumeLine(String line) {
      if (this.logger.isInfoEnabled()) {
         this.logger.info(line);
      }

   }

   public List<ScmFile> getTaggedFiles() {
      return this.tags;
   }
}
