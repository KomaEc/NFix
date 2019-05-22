package org.apache.maven.scm.provider.integrity.command.checkin;

import java.util.List;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.checkin.AbstractCheckInCommand;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.integrity.Sandbox;
import org.apache.maven.scm.provider.integrity.repository.IntegrityScmProviderRepository;

public class IntegrityCheckInCommand extends AbstractCheckInCommand {
   public CheckInScmResult executeCheckInCommand(ScmProviderRepository repository, ScmFileSet fileSet, String message, ScmVersion scmVersion) throws ScmException {
      this.getLogger().info("Attempting to check-in updates from sandbox " + fileSet.getBasedir().getAbsolutePath());
      IntegrityScmProviderRepository iRepo = (IntegrityScmProviderRepository)repository;
      Sandbox siSandbox = iRepo.getSandbox();
      List<ScmFile> changedFiles = siSandbox.checkInUpdates(message);
      return siSandbox.getOverallCheckInSuccess() ? new CheckInScmResult("si ci/drop", changedFiles) : new CheckInScmResult(changedFiles, new ScmResult("si ci/drop", "There was a problem updating the repository", "", false));
   }
}
