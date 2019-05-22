package org.apache.maven.scm.provider.integrity.command.add;

import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.add.AbstractAddCommand;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityAddCommand extends AbstractAddCommand {
   public AddScmResult executeAddCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, boolean binary) throws ScmException {
      this.getLogger().info("Attempting to add new files from directory " + fileSet.getBasedir().getAbsolutePath());
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      Sandbox siSandbox = iRepo.getSandbox();
      String excludes = Sandbox.formatFilePatterns(fileSet.getExcludes());
      String includes = Sandbox.formatFilePatterns(fileSet.getIncludes());
      String msg = null != message && message.length() != 0 ? message : System.getProperty("message");
      List<ScmFile> addedFiles = siSandbox.addNonMembers(excludes, includes, msg);
      return siSandbox.getOverallAddSuccess() ? new AddScmResult("si add", addedFiles) : new AddScmResult(addedFiles, new ScmResult("si add", "There was a problem adding files to the repository", "", false));
   }
}
