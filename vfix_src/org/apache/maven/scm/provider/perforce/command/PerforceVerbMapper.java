package org.apache.maven.scm.provider.perforce.command;

import java.util.HashMap;
import java.util.Map;
import org.apache.maven.scm.ScmFileStatus;

public class PerforceVerbMapper {
   private static final Map<String, ScmFileStatus> VERB = new HashMap();

   public static ScmFileStatus toStatus(String verb) {
      ScmFileStatus stat = (ScmFileStatus)VERB.get(verb);
      if (stat == null) {
         System.err.println("No such verb: " + verb);
         return ScmFileStatus.UNKNOWN;
      } else {
         if (stat == ScmFileStatus.UNKNOWN) {
            stat = null;
         }

         return stat;
      }
   }

   static {
      VERB.put("add", ScmFileStatus.ADDED);
      VERB.put("added", ScmFileStatus.ADDED);
      VERB.put("delete", ScmFileStatus.DELETED);
      VERB.put("deleted", ScmFileStatus.DELETED);
      VERB.put("edit", ScmFileStatus.MODIFIED);
      VERB.put("edited", ScmFileStatus.MODIFIED);
      VERB.put("updating", ScmFileStatus.UPDATED);
      VERB.put("updated", ScmFileStatus.UPDATED);
      VERB.put("refreshing", ScmFileStatus.UNKNOWN);
   }
}
