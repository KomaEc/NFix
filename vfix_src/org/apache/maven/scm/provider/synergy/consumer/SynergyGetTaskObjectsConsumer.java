package org.apache.maven.scm.provider.synergy.consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class SynergyGetTaskObjectsConsumer extends AbstractConsumer {
   private List<ChangeFile> entries = new ArrayList();
   public static final String OUTPUT_FORMAT = "%name#####%version#####";

   public List<ChangeFile> getFiles() {
      return this.entries;
   }

   public SynergyGetTaskObjectsConsumer(ScmLogger logger) {
      super(logger);
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Consume: " + line);
      }

      StringTokenizer tokenizer = new StringTokenizer(line.trim(), "#####");
      if (tokenizer.countTokens() == 2) {
         ChangeFile f = new ChangeFile(tokenizer.nextToken());
         f.setRevision(tokenizer.nextToken());
         this.entries.add(f);
      } else if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error("Invalid token count in SynergyGetTaskObjects [" + tokenizer.countTokens() + "]");
      }

   }
}
