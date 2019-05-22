package org.apache.maven.scm.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmBranchParameters;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
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
import org.apache.maven.scm.command.list.ListScmResult;
import org.apache.maven.scm.command.mkdir.MkdirScmResult;
import org.apache.maven.scm.command.remove.RemoveScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.command.tag.TagScmResult;
import org.apache.maven.scm.command.unedit.UnEditScmResult;
import org.apache.maven.scm.command.update.UpdateScmResult;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.ScmUrlUtils;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

public abstract class AbstractScmManager implements ScmManager {
   private Map<String, ScmProvider> scmProviders = new HashMap();
   private ScmLogger logger;
   private Map<String, String> userProviderTypes = new HashMap();

   protected void setScmProviders(Map<String, ScmProvider> providers) {
      this.scmProviders = providers;
   }

   /** @deprecated */
   protected void addScmProvider(String providerType, ScmProvider provider) {
      this.setScmProvider(providerType, provider);
   }

   public void setScmProvider(String providerType, ScmProvider provider) {
      this.scmProviders.put(providerType, provider);
   }

   protected abstract ScmLogger getScmLogger();

   public ScmProvider getProviderByUrl(String scmUrl) throws ScmRepositoryException, NoSuchScmProviderException {
      if (scmUrl == null) {
         throw new NullPointerException("The scm url cannot be null.");
      } else {
         String providerType = ScmUrlUtils.getProvider(scmUrl);
         return this.getProviderByType(providerType);
      }
   }

   public void setScmProviderImplementation(String providerType, String providerImplementation) {
      this.userProviderTypes.put(providerType, providerImplementation);
   }

   public ScmProvider getProviderByType(String providerType) throws NoSuchScmProviderException {
      if (this.logger == null) {
         this.logger = this.getScmLogger();
         Iterator i$ = this.scmProviders.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, ScmProvider> entry = (Entry)i$.next();
            ScmProvider p = (ScmProvider)entry.getValue();
            p.addListener(this.logger);
         }
      }

      String usedProviderType = System.getProperty("maven.scm.provider." + providerType + ".implementation");
      if (usedProviderType == null) {
         if (this.userProviderTypes.containsKey(providerType)) {
            usedProviderType = (String)this.userProviderTypes.get(providerType);
         } else {
            usedProviderType = providerType;
         }
      }

      ScmProvider scmProvider = (ScmProvider)this.scmProviders.get(usedProviderType);
      if (scmProvider == null) {
         throw new NoSuchScmProviderException(usedProviderType);
      } else {
         return scmProvider;
      }
   }

   public ScmProvider getProviderByRepository(ScmRepository repository) throws NoSuchScmProviderException {
      return this.getProviderByType(repository.getProvider());
   }

   public ScmRepository makeScmRepository(String scmUrl) throws ScmRepositoryException, NoSuchScmProviderException {
      if (scmUrl == null) {
         throw new NullPointerException("The scm url cannot be null.");
      } else {
         char delimiter = ScmUrlUtils.getDelimiter(scmUrl).charAt(0);
         String providerType = ScmUrlUtils.getProvider(scmUrl);
         ScmProvider provider = this.getProviderByType(providerType);
         String scmSpecificUrl = this.cleanScmUrl(scmUrl.substring(providerType.length() + 5));
         ScmProviderRepository providerRepository = provider.makeProviderScmRepository(scmSpecificUrl, delimiter);
         return new ScmRepository(providerType, providerRepository);
      }
   }

   protected String cleanScmUrl(String scmUrl) {
      if (scmUrl == null) {
         throw new NullPointerException("The scm url cannot be null.");
      } else {
         String pathSeparator = "";
         int indexOfDoubleDot = -1;
         if (scmUrl.indexOf("../") > 1) {
            pathSeparator = "/";
            indexOfDoubleDot = scmUrl.indexOf("../");
         }

         if (scmUrl.indexOf("..\\") > 1) {
            pathSeparator = "\\";
            indexOfDoubleDot = scmUrl.indexOf("..\\");
         }

         if (indexOfDoubleDot > 1) {
            int startOfTextToRemove = scmUrl.substring(0, indexOfDoubleDot - 1).lastIndexOf(pathSeparator);
            String beginUrl = "";
            if (startOfTextToRemove >= 0) {
               beginUrl = scmUrl.substring(0, startOfTextToRemove);
            }

            String endUrl = scmUrl.substring(indexOfDoubleDot + 3);
            scmUrl = beginUrl + pathSeparator + endUrl;
            if (scmUrl.indexOf("../") > 1 || scmUrl.indexOf("..\\") > 1) {
               scmUrl = this.cleanScmUrl(scmUrl);
            }
         }

         return scmUrl;
      }
   }

   public ScmRepository makeProviderScmRepository(String providerType, File path) throws ScmRepositoryException, UnknownRepositoryStructure, NoSuchScmProviderException {
      if (providerType == null) {
         throw new NullPointerException("The provider type cannot be null.");
      } else {
         ScmProvider provider = this.getProviderByType(providerType);
         ScmProviderRepository providerRepository = provider.makeProviderScmRepository(path);
         return new ScmRepository(providerType, providerRepository);
      }
   }

   public List<String> validateScmRepository(String scmUrl) {
      List<String> messages = new ArrayList();
      messages.addAll(ScmUrlUtils.validate(scmUrl));
      String providerType = ScmUrlUtils.getProvider(scmUrl);

      ScmProvider provider;
      try {
         provider = this.getProviderByType(providerType);
      } catch (NoSuchScmProviderException var7) {
         messages.add("No such provider installed '" + providerType + "'.");
         return messages;
      }

      String scmSpecificUrl = this.cleanScmUrl(scmUrl.substring(providerType.length() + 5));
      List<String> providerMessages = provider.validateScmUrl(scmSpecificUrl, ScmUrlUtils.getDelimiter(scmUrl).charAt(0));
      if (providerMessages == null) {
         throw new RuntimeException("The SCM provider cannot return null from validateScmUrl().");
      } else {
         messages.addAll(providerMessages);
         return messages;
      }
   }

   public AddScmResult add(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).add(repository, fileSet);
   }

   public AddScmResult add(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      return this.getProviderByRepository(repository).add(repository, fileSet, message);
   }

   public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName) throws ScmException {
      ScmBranchParameters scmBranchParameters = new ScmBranchParameters("");
      return this.getProviderByRepository(repository).branch(repository, fileSet, branchName, scmBranchParameters);
   }

   public BranchScmResult branch(ScmRepository repository, ScmFileSet fileSet, String branchName, String message) throws ScmException {
      ScmBranchParameters scmBranchParameters = new ScmBranchParameters(message);
      return this.getProviderByRepository(repository).branch(repository, fileSet, branchName, scmBranchParameters);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, ScmBranch branch) throws ScmException {
      return this.getProviderByRepository(repository).changeLog(repository, fileSet, startDate, endDate, numDays, branch);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, Date startDate, Date endDate, int numDays, ScmBranch branch, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).changeLog(repository, fileSet, startDate, endDate, numDays, branch, datePattern);
   }

   public ChangeLogScmResult changeLog(ChangeLogScmRequest scmRequest) throws ScmException {
      return this.getProviderByRepository(scmRequest.getScmRepository()).changeLog(scmRequest);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      return this.getProviderByRepository(repository).changeLog(repository, fileSet, startVersion, endVersion);
   }

   public ChangeLogScmResult changeLog(ScmRepository repository, ScmFileSet fileSet, ScmVersion startRevision, ScmVersion endRevision, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).changeLog(repository, fileSet, startRevision, endRevision, datePattern);
   }

   public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      return this.getProviderByRepository(repository).checkIn(repository, fileSet, message);
   }

   public CheckInScmResult checkIn(ScmRepository repository, ScmFileSet fileSet, ScmVersion revision, String message) throws ScmException {
      return this.getProviderByRepository(repository).checkIn(repository, fileSet, revision, message);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).checkOut(repository, fileSet);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      return this.getProviderByRepository(repository).checkOut(repository, fileSet, version);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, boolean recursive) throws ScmException {
      return this.getProviderByRepository(repository).checkOut(repository, fileSet, recursive);
   }

   public CheckOutScmResult checkOut(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, boolean recursive) throws ScmException {
      return this.getProviderByRepository(repository).checkOut(repository, fileSet, version, recursive);
   }

   public DiffScmResult diff(ScmRepository repository, ScmFileSet fileSet, ScmVersion startVersion, ScmVersion endVersion) throws ScmException {
      return this.getProviderByRepository(repository).diff(repository, fileSet, startVersion, endVersion);
   }

   public EditScmResult edit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).edit(repository, fileSet);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).export(repository, fileSet);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      return this.getProviderByRepository(repository).export(repository, fileSet, version);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, String outputDirectory) throws ScmException {
      return this.getProviderByRepository(repository).export(repository, fileSet, (ScmVersion)null, outputDirectory);
   }

   public ExportScmResult export(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String outputDirectory) throws ScmException {
      return this.getProviderByRepository(repository).export(repository, fileSet, version, outputDirectory);
   }

   public ListScmResult list(ScmRepository repository, ScmFileSet fileSet, boolean recursive, ScmVersion version) throws ScmException {
      return this.getProviderByRepository(repository).list(repository, fileSet, recursive, version);
   }

   public MkdirScmResult mkdir(ScmRepository repository, ScmFileSet fileSet, String message, boolean createInLocal) throws ScmException {
      return this.getProviderByRepository(repository).mkdir(repository, fileSet, message, createInLocal);
   }

   public RemoveScmResult remove(ScmRepository repository, ScmFileSet fileSet, String message) throws ScmException {
      return this.getProviderByRepository(repository).remove(repository, fileSet, message);
   }

   public StatusScmResult status(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).status(repository, fileSet);
   }

   public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName) throws ScmException {
      return this.tag(repository, fileSet, tagName, "");
   }

   public TagScmResult tag(ScmRepository repository, ScmFileSet fileSet, String tagName, String message) throws ScmException {
      ScmTagParameters scmTagParameters = new ScmTagParameters(message);
      return this.getProviderByRepository(repository).tag(repository, fileSet, tagName, scmTagParameters);
   }

   public UnEditScmResult unedit(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).unedit(repository, fileSet);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, version);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, boolean runChangelog) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, runChangelog);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, boolean runChangelog) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, version, runChangelog);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, (ScmVersion)null, datePattern);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, version, datePattern);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, Date lastUpdate) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, (ScmVersion)null, lastUpdate);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, version, lastUpdate);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, Date lastUpdate, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, (ScmVersion)null, lastUpdate, datePattern);
   }

   public UpdateScmResult update(ScmRepository repository, ScmFileSet fileSet, ScmVersion version, Date lastUpdate, String datePattern) throws ScmException {
      return this.getProviderByRepository(repository).update(repository, fileSet, version, lastUpdate, datePattern);
   }

   public BlameScmResult blame(ScmRepository repository, ScmFileSet fileSet, String filename) throws ScmException {
      return this.getProviderByRepository(repository).blame(repository, fileSet, filename);
   }

   public BlameScmResult blame(BlameScmRequest blameScmRequest) throws ScmException {
      return this.getProviderByRepository(blameScmRequest.getScmRepository()).blame(blameScmRequest);
   }
}
