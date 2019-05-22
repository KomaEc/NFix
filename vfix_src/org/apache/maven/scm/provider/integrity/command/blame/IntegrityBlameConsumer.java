package org.apache.maven.scm.provider.integrity.command.blame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class IntegrityBlameConsumer implements StreamConsumer {
   private ScmLogger logger;
   private List<BlameLine> blameList;
   private SimpleDateFormat dateFormat;

   public IntegrityBlameConsumer(ScmLogger logger) {
      this.logger = logger;
      this.blameList = new ArrayList();
      this.dateFormat = new SimpleDateFormat("MMM dd, yyyy z");
   }

   public void consumeLine(String line) {
      this.logger.debug(line);
      if (null != line && line.trim().length() > 0) {
         String[] tokens = StringUtils.split(line, "\t");
         if (tokens.length != 3) {
            this.logger.warn("Failed to parse line: " + line);
         } else {
            try {
               this.blameList.add(new BlameLine(this.dateFormat.parse(tokens[0]), tokens[1], tokens[2]));
            } catch (ParseException var4) {
               this.logger.error("Failed to date string: " + tokens[0]);
            }
         }
      }

   }

   public List<BlameLine> getBlameList() {
      return this.blameList;
   }
}
