package org.apache.maven.scm.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.NoSuchCommandScmException;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmBranchParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTagParameters;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.add.AddScmResult;
import org.apache.maven.scm.command.blame.BlameScmRequest;
import org.apache.maven.scm.command.blame.BlameScmResult;
import org.apache.maven.scm.command.branch.BranchScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.checkin.CheckInScmResult;
import org.apache.maven.scm.command.checkout.CheckOutScmResult;
import org.apache.maven.scm.command.diff.DiffScmResult;
import org.apache.maven.scm.command.edit.EditScmResult;
import org.apache.maven.scm.command.export.ExportScmResult;
import org.apache.maven.scm.command.info.InfoScmResult;
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.login.LoginScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remoteinfo.RemoteInfoScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.log.ScmLogDispatcher;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractScmProvider implements ScmProvider {
   private ScmLogDispatcher logDispatcher = new ScmLogDispatcher();

   public String getScmSpecificFilename() {
      return null;
   }

   public String sanitizeTagName(String tag) {
      return tag;
   }

   public boolean validateTagName(String tag) {
      return true;
   }

   public List<String> validateScmUrl(String scmSpecificUrl, char delimiter) {
      ArrayList messages = new ArrayList();

      try {
         this.makeProviderScmRepository(scmSpecificUrl, delimiter);
      } catch (ScmRepositoryException var5) {
         messages.add(var5.getMessage());
      }

      return messages;
   }

   public boolean requiresEditMode() {
      return false;
   }

   public AddScmResult add(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.add(repository, fileSet, (String)null);
   }

   public AddScmResult add(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.MESSAGE, message == null ? "" : message);
      parameters.setString(CommandParameter.BINARY, "false");
      return this.add(repository.getProviderRepository(), fileSet, parameters);
   }

   public AddScmResult add(ScmRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      this.login(repository, fileSet);
      parameters.setString(CommandParameter.BINARY, "false");
      return this.add(repository.getProviderRepository(), fileSet, parameters);
   }

   public AddScmResult add(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("add");
   }

   public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName) throws ScmException {
      return this.branch(repository, fileSet, branchName, new ScmBranchParameters());
   }

   public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName, String message) throws ScmException {
      ScmBranchParameters scmBranchParameters = new ScmBranchParameters();
      if (StringUtils.isNotEmpty(message)) {
         scmBranchParameters.setMessage(message);
      }

      return this.branch(repository, fileSet, branchName, scmBranchParameters);
   }

   public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName, ScmBranchParameters scmBranchParameters) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.BRANCH_NAME, branchName);
      parameters.setScmBranchParameters(CommandParameter.SCM_BRANCH_PARAMETERS, scmBranchParameters);
      return this.branch(repository.getProviderRepository(), fileSet, parameters);
   }

   protected BranchScmResult branch(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("branch");
   }

   /** @deprecated */
   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, String branch) throws ScmException {
      return this.changeLog(repository, fileSet, startDate, endDate, numDays, (String)branch, (String)null);
   }

   /** @deprecated */
   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, String branch, String datePattern) throws ScmException {
      ScmBranch scmBranch = null;
      if (StringUtils.isNotEmpty(branch)) {
         scmBranch = new ScmBranch(branch);
      }

      return this.changeLog(repository, fileSet, startDate, endDate, numDays, (ScmBranch)scmBranch, (String)null);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, ScmBranch branch) throws ScmException {
      return this.changeLog(repository, fileSet, startDate, endDate, numDays, (ScmBranch)branch, (String)null);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, ScmBranch branch, String datePattern) throws ScmException {
      ChangeLogScmRequest request = new ChangeLogScmRequest(repository, fileSet);
      request.setDateRange(startDate, endDate);
      request.setNumDays(numDays);
      request.setScmBranch(branch);
      request.setDatePattern(datePattern);
      return this.changeLog(request);
   }

   public ChangeLogScmResult changeLog(ChangeLogScmRequest request) throws ScmException {
      ScmRepository scmRepository = request.getScmRepository();
      ScmFileSet scmFileSet = request.getScmFileSet();
      this.login(scmRepository, scmFileSet);
      return this.changelog(scmRepository.getProviderRepository(), scmFileSet, request.getCommandParameters());
   }

   /** @deprecated */
   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, String startTag, String endTag) throws ScmException {
      return this.changeLog(repository, fileSet, (String)startTag, (String)endTag, (String)null);
   }

   /** @deprecated */
   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, String startTag, String endTag, String datePattern) throws ScmException {
      ScmVersion startRevision = null;
      ScmVersion endRevision = null;
      if (StringUtils.isNotEmpty(startTag)) {
         startRevision = new ScmRevision(startTag);
      }

      if (StringUtils.isNotEmpty(endTag)) {
         endRevision = new ScmRevision(endTag);
      }

      return this.changeLog(repository, fileSet, (ScmVersion)startRevision, (ScmVersion)endRevision, (String)null);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      return this.changeLog(repository, fileSet, (ScmVersion)startVersion, (ScmVersion)endVersion, (String)null);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion, String datePattern) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.START_SCM_VERSION, startVersion);
      parameters.setScmVersion(CommandParameter.END_SCM_VERSION, endVersion);
      parameters.setString(CommandParameter.CHANGELOG_DATE_PATTERN, datePattern);
      return this.changelog(repository.getProviderRepository(), fileSet, parameters);
   }

   protected ChangeLogScmResult changelog(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("changelog");
   }

   /** @deprecated */
   public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, String tag, String message) throws ScmException {
      ScmVersion scmVersion = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmVersion = new ScmBranch(tag);
      }

      return this.checkIn(repository, fileSet, (ScmVersion)scmVersion, message);
   }

   public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      return this.checkIn(repository, fileSet, (ScmVersion)null, message);
   }

   public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, String message) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      parameters.setString(CommandParameter.MESSAGE, message);
      return this.checkin(repository.getProviderRepository(), fileSet, parameters);
   }

   protected CheckInScmResult checkin(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("checkin");
   }

   /** @deprecated */
   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, String tag) throws ScmException {
      return this.checkOut(repository, fileSet, tag, true);
   }

   /** @deprecated */
   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, String tag, boolean recursive) throws ScmException {
      ScmVersion scmVersion = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmVersion = new ScmBranch(tag);
      }

      return this.checkOut(repository, fileSet, (ScmVersion)scmVersion, recursive);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.checkOut(repository, fileSet, (ScmVersion)null, true);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      return this.checkOut(repository, fileSet, scmVersion, true);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, boolean recursive) throws ScmException {
      return this.checkOut(repository, fileSet, (ScmVersion)null, recursive);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, boolean recursive) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      parameters.setString(CommandParameter.RECURSIVE, Boolean.toString(recursive));
      return this.checkout(repository.getProviderRepository(), fileSet, parameters);
   }

   protected CheckOutScmResult checkout(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("checkout");
   }

   /** @deprecated */
   public DiffScmResult diff(ScmRepository repository, ScmFileSet fileSet, String startRevision, String endRevision) throws ScmException {
      ScmVersion startVersion = null;
      ScmVersion endVersion = null;
      if (StringUtils.isNotEmpty(startRevision)) {
         startVersion = new ScmRevision(startRevision);
      }

      if (StringUtils.isNotEmpty(endRevision)) {
         endVersion = new ScmRevision(endRevision);
      }

      return this.diff(repository, fileSet, (ScmVersion)startVersion, (ScmVersion)endVersion);
   }

   public DiffScmResult diff(ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.START_SCM_VERSION, startVersion);
      parameters.setScmVersion(CommandParameter.END_SCM_VERSION, endVersion);
      return this.diff(repository.getProviderRepository(), fileSet, parameters);
   }

   protected DiffScmResult diff(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("diff");
   }

   public EditScmResult edit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      return this.edit(repository.getProviderRepository(), fileSet, parameters);
   }

   protected EditScmResult edit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn("Provider " + this.getScmType() + " does not support edit operation.");
      }

      return new EditScmResult("", (String)null, (String)null, true);
   }

   /** @deprecated */
   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, String tag) throws ScmException {
      return this.export(repository, fileSet, (String)tag, (String)null);
   }

   /** @deprecated */
   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, String tag, String outputDirectory) throws ScmException {
      ScmVersion scmVersion = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmVersion = new ScmRevision(tag);
      }

      return this.export(repository, fileSet, (ScmVersion)scmVersion, outputDirectory);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.export(repository, fileSet, (ScmVersion)((ScmVersion)null), (String)null);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      return this.export(repository, fileSet, (ScmVersion)scmVersion, (String)null);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, String outputDirectory) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      parameters.setString(CommandParameter.OUTPUT_DIRECTORY, outputDirectory);
      return this.export(repository.getProviderRepository(), fileSet, parameters);
   }

   protected ExportScmResult export(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("export");
   }

   public ListScmResult list(ScmRepository repository, ScmFileSet fileSet, boolean recursive, String tag) throws ScmException {
      ScmVersion scmVersion = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmVersion = new ScmRevision(tag);
      }

      return this.list(repository, fileSet, recursive, (ScmVersion)scmVersion);
   }

   public ListScmResult list(ScmRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion scmVersion) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.RECURSIVE, Boolean.toString(recursive));
      if (scmVersion != null) {
         parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      }

      return this.list(repository.getProviderRepository(), fileSet, parameters);
   }

   protected ListScmResult list(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("list");
   }

   public MkdirScmResult mkdir(ScmRepository repository, ScmFileSet fileSet, String message, boolean createInLocal) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      if (message == null) {
         message = "";
         if (!createInLocal) {
            this.getLogger().warn("Commit message is empty!");
         }
      }

      parameters.setString(CommandParameter.MESSAGE, message);
      parameters.setString(CommandParameter.SCM_MKDIR_CREATE_IN_LOCAL, Boolean.toString(createInLocal));
      return this.mkdir(repository.getProviderRepository(), fileSet, parameters);
   }

   protected MkdirScmResult mkdir(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("mkdir");
   }

   private void login(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      LoginScmResult result = this.login(repository.getProviderRepository(), fileSet, new CommandParameters());
      if (!result.isSuccess()) {
         throw new ScmException("Can't login.\n" + result.getCommandOutput());
      }
   }

   protected LoginScmResult login(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return new LoginScmResult((String)null, (String)null, (String)null, true);
   }

   public RemoveScmResult remove(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.MESSAGE, message == null ? "" : message);
      return this.remove(repository.getProviderRepository(), fileSet, parameters);
   }

   protected RemoveScmResult remove(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("remove");
   }

   public StatusScmResult status(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      return this.status(repository.getProviderRepository(), fileSet, parameters);
   }

   protected StatusScmResult status(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("status");
   }

   public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName) throws ScmException {
      return this.tag(repository, fileSet, tagName, new ScmTagParameters());
   }

   public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName, String message) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.TAG_NAME, tagName);
      if (StringUtils.isNotEmpty(message)) {
         parameters.setString(CommandParameter.MESSAGE, message);
      }

      ScmTagParameters scmTagParameters = new ScmTagParameters(message);
      parameters.setScmTagParameters(CommandParameter.SCM_TAG_PARAMETERS, scmTagParameters);
      return this.tag(repository.getProviderRepository(), fileSet, parameters);
   }

   public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName, ScmTagParameters scmTagParameters) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.TAG_NAME, tagName);
      parameters.setScmTagParameters(CommandParameter.SCM_TAG_PARAMETERS, scmTagParameters);
      return this.tag(repository.getProviderRepository(), fileSet, parameters);
   }

   protected TagScmResult tag(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("tag");
   }

   public UnEditScmResult unedit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      return this.unedit(repository.getProviderRepository(), fileSet, parameters);
   }

   protected UnEditScmResult unedit(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      if (this.getLogger().isWarnEnabled()) {
         this.getLogger().warn("Provider " + this.getScmType() + " does not support unedit operation.");
      }

      return new UnEditScmResult("", (String)null, (String)null, true);
   }

   /** @deprecated */
   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag) throws ScmException {
      return this.update(repository, fileSet, tag, true);
   }

   /** @deprecated */
   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag, boolean runChangelog) throws ScmException {
      return this.update(repository, fileSet, tag, "", runChangelog);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.update(repository, fileSet, (ScmVersion)null, true);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion) throws ScmException {
      return this.update(repository, fileSet, scmVersion, true);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, boolean runChangelog) throws ScmException {
      return this.update(repository, fileSet, (ScmVersion)null, "", runChangelog);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, boolean runChangelog) throws ScmException {
      return this.update(repository, fileSet, scmVersion, "", runChangelog);
   }

   /** @deprecated */
   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag, String datePattern) throws ScmException {
      return this.update(repository, fileSet, tag, datePattern, true);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, String datePattern) throws ScmException {
      return this.update(repository, fileSet, scmVersion, datePattern, true);
   }

   /** @deprecated */
   private UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag, String datePattern, boolean runChangelog) throws ScmException {
      ScmBranch scmBranch = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmBranch = new ScmBranch(tag);
      }

      return this.update(repository, fileSet, (ScmVersion)scmBranch, datePattern, runChangelog);
   }

   private UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, String datePattern, boolean runChangelog) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      parameters.setString(CommandParameter.CHANGELOG_DATE_PATTERN, datePattern);
      parameters.setString(CommandParameter.RUN_CHANGELOG_WITH_UPDATE, String.valueOf(runChangelog));
      return this.update(repository.getProviderRepository(), fileSet, parameters);
   }

   /** @deprecated */
   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag, Date lastUpdate) throws ScmException {
      return this.update(repository, fileSet, (String)tag, lastUpdate, (String)null);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, Date lastUpdate) throws ScmException {
      return this.update(repository, fileSet, (ScmVersion)scmVersion, lastUpdate, (String)null);
   }

   /** @deprecated */
   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String tag, Date lastUpdate, String datePattern) throws ScmException {
      ScmBranch scmBranch = null;
      if (StringUtils.isNotEmpty(tag)) {
         scmBranch = new ScmBranch(tag);
      }

      return this.update(repository, fileSet, (ScmVersion)scmBranch, lastUpdate, datePattern);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion scmVersion, Date lastUpdate, String datePattern) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setScmVersion(CommandParameter.SCM_VERSION, scmVersion);
      if (lastUpdate != null) {
         parameters.setDate(CommandParameter.START_DATE, lastUpdate);
      }

      parameters.setString(CommandParameter.CHANGELOG_DATE_PATTERN, datePattern);
      parameters.setString(CommandParameter.RUN_CHANGELOG_WITH_UPDATE, "true");
      return this.update(repository.getProviderRepository(), fileSet, parameters);
   }

   protected UpdateScmResult update(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("update");
   }

   public BlameScmResult blame(ScmRepository repository, ScmFileSet fileSet, String filename) throws ScmException {
      this.login(repository, fileSet);
      CommandParameters parameters = new CommandParameters();
      parameters.setString(CommandParameter.FILE, filename);
      return this.blame(repository.getProviderRepository(), fileSet, parameters);
   }

   protected BlameScmResult blame(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      throw new NoSuchCommandScmException("blame");
   }

   public BlameScmResult blame(BlameScmRequest blameScmRequest) throws ScmException {
      return this.blame(blameScmRequest.getScmRepository().getProviderRepository(), blameScmRequest.getScmFileSet(), blameScmRequest.getCommandParameters());
   }

   public InfoScmResult info(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return null;
   }

   public RemoteInfoScmResult remoteInfo(ScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException {
      return null;
   }

   public void addListener(ScmLogger logger) {
      this.logDispatcher.addListener(logger);
   }

   public ScmLogger getLogger() {
      return this.logDispatcher;
   }

   public ScmProviderRepository makeProviderScmRepository(File path) throws ScmRepositoryException, UnknownRepositoryStructure {
      throw new UnknownRepositoryStructure();
   }
}
