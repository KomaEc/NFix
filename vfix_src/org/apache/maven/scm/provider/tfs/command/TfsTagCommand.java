package org.apache.maven.scm.provider.tfs.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.tfs.TfsScmProviderRepository;
import org.apache.maven.scm.provider.tfs.command.consumer.ErrorStreamConsumer;
import org.codehaus.plexus.util.cli.CommandLineUtils;

public class TfsTagCommand extends AbstractTagCommand {
   protected ScmResult executeTagCommand(ScmProviderRepository r, ScmFileSet f, String tag, String message) throws ScmException {
      return this.executeTagCommand(r, f, tag, new ScmTagParameters(message));
   }

   protected ScmResult executeTagCommand(ScmProviderRepository r, ScmFileSet f, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      TfsCommand command = this.createCommand(r, f, tag, scmTagParameters);
      CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
      ErrorStreamConsumer err = new ErrorStreamConsumer();
      int status = command.execute(out, err);
      if (status == 0 && !err.hasBeenFed()) {
         List<ScmFile> files = new ArrayList(f.getFileList().size());
         Iterator i$ = f.getFileList().iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            files.add(new ScmFile(file.getPath(), ScmFileStatus.TAGGED));
         }

         return new TagScmResult(command.getCommandString(), files);
      } else {
         return new TagScmResult(command.getCommandString(), "Error code for TFS label command - " + status, err.getOutput(), false);
      }
   }

   public TfsCommand createCommand(ScmProviderRepository r, ScmFileSet f, String tag, ScmTagParameters scmTagParameters) {
      TfsScmProviderRepository tfsRepo = (TfsScmProviderRepository)r;
      String url = tfsRepo.getServerPath();
      TfsCommand command = new TfsCommand("label", r, f, this.getLogger());
      command.addArgument(tag);
      command.addArgument(url);
      command.addArgument("-recursive");
      command.addArgument("-child:replace");
      String message = scmTagParameters.getMessage();
      if (message != null && !message.equals("")) {
         command.addArgument("-comment:" + message);
      }

      return command;
   }
}
