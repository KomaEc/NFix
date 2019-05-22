package org.apache.maven.scm.provider.accurev.command.changelog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.CommandParameter;
import org.apache.maven.scm.CommandParameters;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.command.changelog.ChangeLogSet;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevCapability;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevScmProviderRepository;
import org.apache.maven.scm.provider.accurev.AccuRevVersion;
import org.apache.maven.scm.provider.accurev.FileDifference;
import org.apache.maven.scm.provider.accurev.Stream;
import org.apache.maven.scm.provider.accurev.Transaction;
import org.apache.maven.scm.provider.accurev.command.AbstractAccuRevCommand;
import org.codehaus.plexus.util.StringUtils;

public class AccuRevChangeLogCommand extends AbstractAccuRevCommand {
   public AccuRevChangeLogCommand(ScmLogger logger) {
      super(logger);
   }

   protected ScmResult executeAccurevCommand(AccuRevScmProviderRepository repository, ScmFileSet fileSet, CommandParameters parameters) throws ScmException, AccuRevException {
      ScmBranch branch = (ScmBranch)parameters.getScmVersion(CommandParameter.BRANCH, (ScmVersion)null);
      AccuRevVersion branchVersion = repository.getAccuRevVersion(branch);
      String stream = branchVersion.getBasisStream();
      String fromSpec = branchVersion.getTimeSpec();
      String toSpec = "highest";
      ScmVersion startVersion = parameters.getScmVersion(CommandParameter.START_SCM_VERSION, (ScmVersion)null);
      ScmVersion endVersion = parameters.getScmVersion(CommandParameter.END_SCM_VERSION, (ScmVersion)null);
      if (startVersion != null && StringUtils.isNotEmpty(startVersion.getName())) {
         AccuRevVersion fromVersion = repository.getAccuRevVersion(startVersion);
         AccuRevVersion toVersion = endVersion == null ? new AccuRevVersion(fromVersion.getBasisStream(), "now") : repository.getAccuRevVersion(endVersion);
         if (!StringUtils.equals(fromVersion.getBasisStream(), toVersion.getBasisStream())) {
            throw new AccuRevException("Not able to provide change log between different streams " + fromVersion + "," + toVersion);
         }

         stream = fromVersion.getBasisStream();
         fromSpec = fromVersion.getTimeSpec();
         toSpec = toVersion.getTimeSpec();
      }

      Date startDate = parameters.getDate(CommandParameter.START_DATE, (Date)null);
      Date endDate = parameters.getDate(CommandParameter.END_DATE, (Date)null);
      int numDays = parameters.getInt(CommandParameter.NUM_DAYS, 0);
      if (numDays > 0) {
         if (startDate != null || endDate != null) {
            throw new ScmException("Start or end date cannot be set if num days is set.");
         }

         int day = 86400000;
         startDate = new Date(System.currentTimeMillis() - (long)numDays * (long)day);
         endDate = new Date(System.currentTimeMillis() + (long)day);
      }

      if (endDate != null && startDate == null) {
         throw new ScmException("The end date is set but the start date isn't.");
      } else {
         if (startDate != null) {
            fromSpec = AccuRevScmProviderRepository.formatTimeSpec(startDate);
         } else if (fromSpec == null) {
            fromSpec = "1";
         }

         Transaction fromTransaction = this.getDepotTransaction(repository, stream, fromSpec);
         long fromTranId = 1L;
         if (fromTransaction != null) {
            fromTranId = fromTransaction.getTranId();
            if (startDate == null) {
               startDate = fromTransaction.getWhen();
            }
         }

         if (endDate != null) {
            toSpec = AccuRevScmProviderRepository.formatTimeSpec(endDate);
         } else if (toSpec == null) {
            toSpec = "highest";
         }

         Transaction toTransaction = this.getDepotTransaction(repository, stream, toSpec);
         long toTranId = 1L;
         if (toTransaction != null) {
            toTranId = toTransaction.getTranId();
            if (endDate == null) {
               endDate = toTransaction.getWhen();
            }
         }

         ScmVersion startVersion = new ScmRevision(repository.getRevision(stream, fromTranId));
         ScmVersion endVersion = new ScmRevision(repository.getRevision(stream, toTranId));
         List<Transaction> streamHistory = Collections.emptyList();
         List<Transaction> workspaceHistory = Collections.emptyList();
         List<FileDifference> streamDifferences = Collections.emptyList();
         StringBuilder errorMessage = new StringBuilder();
         AccuRev accurev = repository.getAccuRev();
         Stream changelogStream = accurev.showStream(stream);
         String errorString;
         if (changelogStream == null) {
            errorMessage.append("Unknown accurev stream -").append(stream).append(".");
         } else {
            errorString = "Changelog on stream " + stream + "(" + changelogStream.getStreamType() + ") from " + fromTranId + " (" + startDate + "), to " + toTranId + " (" + endDate + ")";
            if ((startDate == null || !startDate.after(endDate)) && fromTranId < toTranId) {
               this.getLogger().info(errorString);
               Stream diffStream = changelogStream;
               if (changelogStream.isWorkspace()) {
                  workspaceHistory = accurev.history(stream, Long.toString(fromTranId + 1L), Long.toString(toTranId), 0, false, false);
                  if (workspaceHistory == null) {
                     errorMessage.append("history on workspace " + stream + " from " + fromTranId + 1 + " to " + toTranId + " failed.");
                  }

                  stream = changelogStream.getBasis();
                  diffStream = accurev.showStream(stream);
               }

               if (AccuRevCapability.DIFF_BETWEEN_STREAMS.isSupported(accurev.getClientVersion())) {
                  if (startDate.before(diffStream.getStartDate())) {
                     this.getLogger().warn("Skipping diff of " + stream + " due to start date out of range");
                  } else {
                     streamDifferences = accurev.diff(stream, Long.toString(fromTranId), Long.toString(toTranId));
                     if (streamDifferences == null) {
                        errorMessage.append("Diff " + stream + "- " + fromTranId + " to " + toTranId + "failed.");
                     }
                  }
               }

               streamHistory = accurev.history(stream, Long.toString(fromTranId + 1L), Long.toString(toTranId), 0, false, false);
               if (streamHistory == null) {
                  errorMessage.append("history on stream " + stream + " from " + fromTranId + 1 + " to " + toTranId + " failed.");
               }
            } else {
               this.getLogger().warn("Skipping out of range " + errorString);
            }
         }

         errorString = errorMessage.toString();
         if (StringUtils.isBlank(errorString)) {
            ChangeLogSet changeLog = this.getChangeLog(changelogStream, streamDifferences, streamHistory, workspaceHistory, startDate, endDate);
            changeLog.setEndVersion(endVersion);
            changeLog.setStartVersion(startVersion);
            return new ChangeLogScmResult(accurev.getCommandLines(), changeLog);
         } else {
            return new ChangeLogScmResult(accurev.getCommandLines(), "AccuRev errors: " + errorMessage, accurev.getErrorOutput(), false);
         }
      }
   }

   private Transaction getDepotTransaction(AccuRevScmProviderRepository repo, String stream, String tranSpec) throws AccuRevException {
      return repo.getDepotTransaction(stream, tranSpec);
   }

   private ChangeLogSet getChangeLog(Stream stream, List<FileDifference> streamDifferences, List<Transaction> streamHistory, List<Transaction> workspaceHistory, Date startDate, Date endDate) {
      Map<Long, FileDifference> differencesMap = new HashMap();
      Iterator i$ = streamDifferences.iterator();

      while(i$.hasNext()) {
         FileDifference fileDifference = (FileDifference)i$.next();
         differencesMap.put(fileDifference.getElementId(), fileDifference);
      }

      List<Transaction> mergedHistory = new ArrayList(streamHistory);
      String streamPrefix = "/";
      mergedHistory.addAll(workspaceHistory);
      streamPrefix = stream.getId() + "/";
      List<ChangeSet> entries = new ArrayList(streamHistory.size());
      Iterator i$ = mergedHistory.iterator();

      while(true) {
         while(true) {
            Transaction t;
            do {
               do {
                  do {
                     if (!i$.hasNext()) {
                        if (!differencesMap.isEmpty()) {
                           List<ChangeFile> upstreamFiles = new ArrayList();
                           Iterator i$ = differencesMap.values().iterator();

                           while(i$.hasNext()) {
                              FileDifference difference = (FileDifference)i$.next();
                              if (difference.getNewVersionSpec() != null) {
                                 upstreamFiles.add(new ChangeFile(difference.getNewFile().getPath(), difference.getNewVersionSpec()));
                              } else {
                                 upstreamFiles.add(new ChangeFile(difference.getOldFile().getPath(), (String)null));
                              }
                           }

                           entries.add(new ChangeSet(endDate, "Upstream changes", "various", upstreamFiles));
                        }

                        return new ChangeLogSet(entries, startDate, endDate);
                     }

                     t = (Transaction)i$.next();
                  } while(startDate != null && t.getWhen().before(startDate));
               } while(endDate != null && t.getWhen().after(endDate));
            } while("mkstream".equals(t.getTranType()));

            Collection<Transaction.Version> versions = t.getVersions();
            List<ChangeFile> files = new ArrayList(versions.size());
            Iterator i$ = versions.iterator();

            while(true) {
               while(i$.hasNext()) {
                  Transaction.Version v = (Transaction.Version)i$.next();
                  FileDifference difference = (FileDifference)differencesMap.get(v.getElementId());
                  if (difference != null) {
                     String newVersionSpec = difference.getNewVersionSpec();
                     if (newVersionSpec != null && newVersionSpec.equals(v.getRealSpec())) {
                        if (this.getLogger().isDebugEnabled()) {
                           this.getLogger().debug("Removing difference for " + v);
                        }

                        differencesMap.remove(v.getElementId());
                     }
                  }

                  if (v.getRealSpec().startsWith(streamPrefix) && !v.getVirtualSpec().startsWith(streamPrefix)) {
                     if (this.getLogger().isDebugEnabled()) {
                        this.getLogger().debug("Skipping workspace to basis stream promote " + v);
                     }
                  } else {
                     ChangeFile f = new ChangeFile(v.getElementName(), v.getVirtualSpec() + " (" + v.getRealSpec() + ")");
                     files.add(f);
                  }
               }

               if (!versions.isEmpty() && files.isEmpty()) {
                  if (this.getLogger().isDebugEnabled()) {
                     this.getLogger().debug("All versions removed for " + t);
                  }
                  break;
               }

               ChangeSet changeSet = new ChangeSet(t.getWhen(), t.getComment(), t.getAuthor(), files);
               entries.add(changeSet);
               break;
            }
         }
      }
   }

   public ChangeLogScmResult changelog(ScmProviderRepository repo, ScmFileSet testFileSet, CommandParameters params) throws ScmException {
      return (ChangeLogScmResult)this.execute(repo, testFileSet, params);
   }
}
