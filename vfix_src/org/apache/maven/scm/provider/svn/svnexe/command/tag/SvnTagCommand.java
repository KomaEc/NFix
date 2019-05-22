package org.apache.maven.scm.provider.svn.svnexe.command.tag;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.command.tag.AbstractTagCommand;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.svn.SvnCommandUtils;
import org.apache.maven.scm.provider.svn.SvnTagBranchUtils;
import org.apache.maven.scm.provider.svn.command.SvnCommand;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.apache.maven.scm.provider.svn.svnexe.command.SvnCommandLineUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

public class SvnTagCommand extends AbstractTagCommand implements SvnCommand {
   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, String message) throws ScmException {
      ScmTagParameters scmTagParameters = new ScmTagParameters(message);
      scmTagParameters.setRemoteTagging(false);
      return this.executeTagCommand(repo, fileSet, tag, scmTagParameters);
   }

   public ScmResult executeTagCommand(ScmProviderRepository repo, ScmFileSet fileSet, String tag, ScmTagParameters scmTagParameters) throws ScmException {
      if (scmTagParameters == null) {
         this.getLogger().debug("SvnTagCommand :: scmTagParameters is null create an empty one");
         scmTagParameters = new ScmTagParameters();
         scmTagParameters.setRemoteTagging(false);
      } else {
         this.getLogger().debug("SvnTagCommand :: scmTagParameters.remoteTagging : " + scmTagParameters.isRemoteTagging());
      }

      if (tag != null && !StringUtils.isEmpty(tag.trim())) {
         if (!fileSet.getFileList().isEmpty()) {
            throw new ScmException("This provider doesn't support tagging subsets of a directory");
         } else {
            SvnScmProviderRepository repository = (SvnScmProviderRepository)repo;
            File messageFile = FileUtils.createTempFile("maven-scm-", ".commit", (File)null);

            try {
               FileUtils.fileWrite(messageFile.getAbsolutePath(), scmTagParameters == null ? "" : scmTagParameters.getMessage());
            } catch (IOException var25) {
               return new TagScmResult((String)null, "Error while making a temporary file for the commit message: " + var25.getMessage(), (String)null, false);
            }

            Commandline cl = createCommandLine(repository, fileSet.getBasedir(), tag, messageFile, scmTagParameters);
            CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
            if (this.getLogger().isInfoEnabled()) {
               this.getLogger().info("Executing: " + SvnCommandLineUtils.cryptPassword(cl));
               this.getLogger().info("Working directory: " + cl.getWorkingDirectory().getAbsolutePath());
            }

            int exitCode;
            try {
               exitCode = SvnCommandLineUtils.execute(cl, stdout, stderr, this.getLogger());
            } catch (CommandLineException var23) {
               throw new ScmException("Error while executing command.", var23);
            } finally {
               try {
                  FileUtils.forceDelete(messageFile);
               } catch (IOException var21) {
               }

            }

            if (exitCode != 0) {
               return new TagScmResult(cl.toString(), "The svn tag command failed.", stderr.getOutput(), false);
            } else {
               List<ScmFile> fileList = new ArrayList();
               List files = null;

               try {
                  List list;
                  if (StringUtils.isNotEmpty(fileSet.getExcludes())) {
                     list = FileUtils.getFiles(fileSet.getBasedir(), StringUtils.isEmpty(fileSet.getIncludes()) ? "**" : fileSet.getIncludes(), fileSet.getExcludes() + ",**/.svn/**", false);
                     files = list;
                  } else {
                     list = FileUtils.getFiles(fileSet.getBasedir(), StringUtils.isEmpty(fileSet.getIncludes()) ? "**" : fileSet.getIncludes(), "**/.svn/**", false);
                     files = list;
                  }
               } catch (IOException var22) {
                  throw new ScmException("Error while executing command.", var22);
               }

               Iterator i = files.iterator();

               while(i.hasNext()) {
                  File f = (File)i.next();
                  fileList.add(new ScmFile(f.getPath(), ScmFileStatus.TAGGED));
               }

               return new TagScmResult(cl.toString(), fileList);
            }
         }
      } else {
         throw new ScmException("tag must be specified");
      }
   }

   /** @deprecated */
   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, String tag, File messageFile) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("copy");
      cl.createArg().setValue("--file");
      cl.createArg().setValue(messageFile.getAbsolutePath());
      cl.createArg().setValue(".");
      String tagUrl = SvnTagBranchUtils.resolveTagUrl(repository, new ScmTag(tag));
      cl.createArg().setValue(SvnCommandUtils.fixUrl(tagUrl, repository.getUser()));
      return cl;
   }

   public static Commandline createCommandLine(SvnScmProviderRepository repository, File workingDirectory, String tag, File messageFile, ScmTagParameters scmTagParameters) {
      Commandline cl = SvnCommandLineUtils.getBaseSvnCommandLine(workingDirectory, repository);
      cl.createArg().setValue("copy");
      cl.createArg().setValue("--file");
      cl.createArg().setValue(messageFile.getAbsolutePath());
      cl.createArg().setValue("--parents");
      if (scmTagParameters != null && scmTagParameters.getScmRevision() != null) {
         cl.createArg().setValue("--revision");
         cl.createArg().setValue(scmTagParameters.getScmRevision());
      }

      if (scmTagParameters != null && scmTagParameters.isRemoteTagging()) {
         cl.createArg().setValue(SvnCommandUtils.fixUrl(repository.getUrl(), repository.getUser()));
      } else {
         cl.createArg().setValue(".");
      }

      String tagUrl = SvnTagBranchUtils.resolveTagUrl(repository, new ScmTag(tag));
      cl.createArg().setValue(SvnCommandUtils.fixUrl(tagUrl, repository.getUser()));
      return cl;
   }
}
