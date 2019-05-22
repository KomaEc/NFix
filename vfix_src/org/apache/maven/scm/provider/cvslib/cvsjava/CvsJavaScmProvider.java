package org.apache.maven.scm.provider.cvslib.cvsjava;

import org.apache.maven.scm.command.Command;
import org.apache.maven.scm.provider.cvslib.AbstractCvsScmProvider;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.add.CvsJavaAddCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.blame.CvsJavaBlameCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.branch.CvsJavaBranchCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.changelog.CvsJavaChangeLogCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.checkin.CvsJavaCheckInCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.checkout.CvsJavaCheckOutCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.diff.CvsJavaDiffCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.export.CvsJavaExportCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.list.CvsJavaListCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.login.CvsJavaLoginCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.mkdir.CvsJavaMkdirCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.remove.CvsJavaRemoveCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.status.CvsJavaStatusCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.tag.CvsJavaTagCommand;
import org.apache.maven.scm.provider.cvslib.cvsjava.command.update.CvsJavaUpdateCommand;

public class CvsJavaScmProvider extends AbstractCvsScmProvider {
   protected Command getAddCommand() {
      return new CvsJavaAddCommand();
   }

   protected Command getBranchCommand() {
      return new CvsJavaBranchCommand();
   }

   protected Command getBlameCommand() {
      return new CvsJavaBlameCommand();
   }

   protected Command getChangeLogCommand() {
      return new CvsJavaChangeLogCommand();
   }

   protected Command getCheckInCommand() {
      return new CvsJavaCheckInCommand();
   }

   protected Command getCheckOutCommand() {
      return new CvsJavaCheckOutCommand();
   }

   protected Command getDiffCommand() {
      return new CvsJavaDiffCommand();
   }

   protected Command getExportCommand() {
      return new CvsJavaExportCommand();
   }

   protected Command getListCommand() {
      return new CvsJavaListCommand();
   }

   protected Command getLoginCommand() {
      return new CvsJavaLoginCommand();
   }

   protected Command getRemoveCommand() {
      return new CvsJavaRemoveCommand();
   }

   protected Command getStatusCommand() {
      return new CvsJavaStatusCommand();
   }

   protected Command getTagCommand() {
      return new CvsJavaTagCommand();
   }

   protected Command getUpdateCommand() {
      return new CvsJavaUpdateCommand();
   }

   protected Command getMkdirCommand() {
      return new CvsJavaMkdirCommand();
   }
}
