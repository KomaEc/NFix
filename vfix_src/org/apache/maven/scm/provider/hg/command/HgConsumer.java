package org.apache.maven.scm.provider.hg.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.util.AbstractConsumer;

public class HgConsumer extends AbstractConsumer {
   private static final Map<String, ScmFileStatus> IDENTIFIERS = new HashMap();
   private static final Map<String, String> MESSAGES = new HashMap();
   private static final int MAX_STDERR_SIZE = 10;
   private final List<String> stderr = new ArrayList();

   public HgConsumer(ScmLogger logger) {
      super(logger);
   }

   public void doConsume(ScmFileStatus status, String trimmedLine) {
   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(line);
      }

      String trimmedLine = line.trim();
      String statusStr = processInputForKnownIdentifiers(trimmedLine);
      if (statusStr == null) {
         boolean isMessage = this.processInputForKnownMessages(trimmedLine);
         if (isMessage) {
            return;
         }
      } else {
         trimmedLine = trimmedLine.substring(statusStr.length());
         trimmedLine = trimmedLine.trim();
      }

      ScmFileStatus status = statusStr != null ? (ScmFileStatus)IDENTIFIERS.get(statusStr.intern()) : null;
      this.doConsume(status, trimmedLine);
   }

   public String getStdErr() {
      StringBuilder str = new StringBuilder();
      Iterator it = this.stderr.iterator();

      while(it.hasNext()) {
         str.append((String)it.next());
      }

      return str.toString();
   }

   private static String processInputForKnownIdentifiers(String line) {
      Iterator it = IDENTIFIERS.keySet().iterator();

      String id;
      do {
         if (!it.hasNext()) {
            return null;
         }

         id = (String)it.next();
      } while(!line.startsWith(id));

      return id;
   }

   private boolean processInputForKnownMessages(String line) {
      Iterator it = MESSAGES.keySet().iterator();

      String prefix;
      do {
         if (!it.hasNext()) {
            return false;
         }

         prefix = (String)it.next();
      } while(!line.startsWith(prefix));

      this.stderr.add(line);
      if (this.stderr.size() > 10) {
         this.stderr.remove(0);
      }

      String message = line.substring(prefix.length());
      if (((String)MESSAGES.get(prefix)).equals("WARNING")) {
         if (this.getLogger().isWarnEnabled()) {
            this.getLogger().warn(message);
         }
      } else if (this.getLogger().isErrorEnabled()) {
         this.getLogger().error(message);
      }

      return true;
   }

   static {
      IDENTIFIERS.put("adding", ScmFileStatus.ADDED);
      IDENTIFIERS.put("unknown", ScmFileStatus.UNKNOWN);
      IDENTIFIERS.put("modified", ScmFileStatus.MODIFIED);
      IDENTIFIERS.put("removed", ScmFileStatus.DELETED);
      IDENTIFIERS.put("renamed", ScmFileStatus.MODIFIED);
      IDENTIFIERS.put("A", ScmFileStatus.ADDED);
      IDENTIFIERS.put("?", ScmFileStatus.UNKNOWN);
      IDENTIFIERS.put("M", ScmFileStatus.MODIFIED);
      IDENTIFIERS.put("R", ScmFileStatus.DELETED);
      IDENTIFIERS.put("C", ScmFileStatus.CHECKED_IN);
      IDENTIFIERS.put("!", ScmFileStatus.MISSING);
      IDENTIFIERS.put("I", ScmFileStatus.UNKNOWN);
      MESSAGES.put("hg: WARNING:", "WARNING");
      MESSAGES.put("hg: ERROR:", "ERROR");
      MESSAGES.put("'hg' ", "ERROR");
   }
}
