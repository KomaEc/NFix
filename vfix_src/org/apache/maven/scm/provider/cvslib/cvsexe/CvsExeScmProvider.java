package org.apache.maven.scm.provider.cvslib.cvsexe;

import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.provider.cvslib.AbstractCvsScmProvider;
import org.apache.maven.scm.provider.cvslib.command.login.CvsLoginCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.add.CvsExeAddCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.blame.CvsExeBlameCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.branch.CvsExeBranchCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.changelog.CvsExeChangeLogCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.checkin.CvsExeCheckInCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.checkout.CvsExeCheckOutCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.diff.CvsExeDiffCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.export.CvsExeExportCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.list.CvsExeListCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.mkdir.CvsExeMkdirCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.remove.CvsExeRemoveCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.status.CvsExeStatusCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.tag.CvsExeTagCommand;
import org.apache.maven.scm.provider.cvslib.cvsexe.command.update.CvsExeUpdateCommand;
import org.apache.maven.scm.provider.cvslib.repository.CvsScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public class CvsExeScmProvider extends AbstractCvsScmProvider {
   public static final String TRANSPORT_SSERVER = "sserver";

   protected Command getAddCommand() {
      return new CvsExeAddCommand();
   }

   protected Command getBranchCommand() {
      return new CvsExeBranchCommand();
   }

   protected Command getBlameCommand() {
      return new CvsExeBlameCommand();
   }

   protected Command getChangeLogCommand() {
      return new CvsExeChangeLogCommand();
   }

   protected Command getCheckInCommand() {
      return new CvsExeCheckInCommand();
   }

   protected Command getCheckOutCommand() {
      return new CvsExeCheckOutCommand();
   }

   protected Command getDiffCommand() {
      return new CvsExeDiffCommand();
   }

   protected Command getExportCommand() {
      return new CvsExeExportCommand();
   }

   protected Command getListCommand() {
      return new CvsExeListCommand();
   }

   protected Command getLoginCommand() {
      return new CvsLoginCommand();
   }

   protected Command getRemoveCommand() {
      return new CvsExeRemoveCommand();
   }

   protected Command getStatusCommand() {
      return new CvsExeStatusCommand();
   }

   protected Command getTagCommand() {
      return new CvsExeTagCommand();
   }

   protected Command getUpdateCommand() {
      return new CvsExeUpdateCommand();
   }

   protected Command getMkdirCommand() {
      return new CvsExeMkdirCommand();
   }

   protected AbstractCvsScmProvider.ScmUrlParserResult parseScmUrl(String scmSpecificUrl, char delimiter) {
      AbstractCvsScmProvider.ScmUrlParserResult result = super.parseScmUrl(scmSpecificUrl, delimiter);
      if (result.getMessages().isEmpty()) {
         return result;
      } else {
         result.resetMessages();
         String[] tokens = StringUtils.split(scmSpecificUrl, Character.toString(delimiter));
         String transport = tokens[0];
         if (!transport.equalsIgnoreCase("sserver")) {
            result.getMessages().add("Unknown transport: " + transport);
            return result;
         } else if (tokens.length >= 4 && (tokens.length <= 5 || !transport.equalsIgnoreCase("sserver"))) {
            String cvsroot;
            if (tokens.length == 4) {
               cvsroot = ":" + transport + ":" + tokens[1] + ":" + tokens[2];
            } else {
               cvsroot = ":" + transport + ":" + tokens[1] + ":" + tokens[2] + ":" + tokens[3];
            }

            String user = null;
            String password = null;
            String host = null;
            String path = null;
            String module = null;
            int port = -1;
            if (transport.equalsIgnoreCase("sserver")) {
               String userhost = tokens[1];
               int index = userhost.indexOf(64);
               if (index == -1) {
                  user = "";
                  host = userhost;
               } else {
                  user = userhost.substring(0, index);
                  host = userhost.substring(index + 1);
               }

               if (tokens.length == 4) {
                  path = tokens[2];
                  module = tokens[3];
               } else {
                  try {
                     port = Integer.valueOf(tokens[2]);
                     path = tokens[3];
                     module = tokens[4];
                  } catch (Exception var16) {
                     result.getMessages().add("Your scm url is invalid, could not get port value.");
                     return result;
                  }
               }

               cvsroot = ":" + transport + ":" + host + ":";
               if (port != -1) {
                  cvsroot = cvsroot + port;
               }

               cvsroot = cvsroot + path;
            }

            if (port == -1) {
               result.setRepository(new CvsScmProviderRepository(cvsroot, transport, user, (String)password, host, path, module));
            } else {
               result.setRepository(new CvsScmProviderRepository(cvsroot, transport, user, (String)password, host, port, path, module));
            }

            return result;
         } else {
            result.getMessages().add("The connection string contains too few tokens.");
            return result;
         }
      }
   }
}
