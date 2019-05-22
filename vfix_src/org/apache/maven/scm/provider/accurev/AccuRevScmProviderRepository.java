package org.apache.maven.scm.provider.accurev;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.apache.maven.scm.provider.accurev.util.WorkspaceUtils;
import org.codehaus.plexus.util.StringUtils;

public class AccuRevScmProviderRepository extends ScmProviderRepositoryWithHost {
   public static final String DEFAULT_TAG_FORMAT = "%s";
   private AccuRev accurev;
   private String streamName;
   private String projectPath;
   private String tagFormat = "%s";
   private ScmLogger logger;
   private String checkoutRelativePath;
   private boolean shouldUseExportForNonPersistentCheckout = true;

   public AccuRevScmProviderRepository() {
      this.setPersistCheckout(true);
      this.setShouldUseExportForNonPersistentCheckout(true);
   }

   public String getTagFormat() {
      return this.tagFormat;
   }

   public void setTagFormat(String tagFormat) {
      if (tagFormat != null && tagFormat.contains("%s")) {
         this.tagFormat = tagFormat;
      } else {
         throw new IllegalArgumentException("tagFormat must contain '%s' to be replaced");
      }
   }

   public String getStreamName() {
      return this.streamName;
   }

   public void setStreamName(String streamName) {
      this.streamName = streamName;
   }

   public String getProjectPath() {
      return this.projectPath;
   }

   public void setProjectPath(String projectPath) {
      this.projectPath = projectPath;
      this.setCheckoutRelativePath(projectPath);
   }

   public AccuRev getAccuRev() {
      return this.accurev;
   }

   public void setAccuRev(AccuRev accurev) {
      this.accurev = accurev;
   }

   public boolean isWorkSpaceRoot(AccuRevInfo info) {
      String p = this.getProjectPath();
      return p != null && WorkspaceUtils.isSameFile(info.getBasedir(), new File(info.getTop(), p)) || this.isWorkSpaceTop(info);
   }

   public boolean isWorkSpaceTop(AccuRevInfo info) {
      return info.isWorkSpaceTop();
   }

   String tagToStream(String tagName) {
      return String.format(this.getTagFormat(), tagName);
   }

   String streamToTag(String streamName) {
      this.tagFormat = this.getTagFormat();
      String tagPatternString = this.tagToStream("(.*)");
      Pattern tagPattern = Pattern.compile(tagPatternString);
      Matcher tagMatcher = tagPattern.matcher(streamName);
      return tagMatcher.matches() ? tagMatcher.group(1) : streamName;
   }

   public void setLogger(ScmLogger logger) {
      this.logger = logger;
   }

   public String getCheckoutRelativePath() {
      return this.checkoutRelativePath == null ? "" : this.checkoutRelativePath;
   }

   public void setCheckoutRelativePath(String checkoutRelativePath) {
      this.checkoutRelativePath = checkoutRelativePath;
   }

   public String getExportRelativePath() {
      return this.getCheckoutRelativePath();
   }

   public boolean shouldUseExportForNonPersistentCheckout() {
      return this.shouldUseExportForNonPersistentCheckout;
   }

   public void setShouldUseExportForNonPersistentCheckout(boolean shouldUseExportForNonPersistentCheckout) {
      this.shouldUseExportForNonPersistentCheckout = shouldUseExportForNonPersistentCheckout;
   }

   public String getDepotRelativeProjectPath() {
      return "/./" + (this.projectPath == null ? "" : this.projectPath);
   }

   public AccuRevVersion getAccuRevVersion(ScmVersion scmVersion) {
      String tran = null;
      String basisStream = null;
      if (scmVersion == null) {
         basisStream = this.getStreamName();
      } else {
         String name = StringUtils.clean(scmVersion.getName());
         String[] versionComponents = name.split("[/\\\\]", 2);
         basisStream = versionComponents[0];
         if (basisStream.length() == 0) {
            basisStream = this.getStreamName();
         } else {
            basisStream = this.tagToStream(basisStream);
         }

         if (versionComponents.length == 2 && versionComponents[1].length() > 0) {
            tran = versionComponents[1];
         }
      }

      return new AccuRevVersion(basisStream, tran);
   }

   public String getSnapshotName(String tagName) {
      return this.tagToStream(tagName);
   }

   public String getRevision(String streamName, Date date) {
      return this.getRevision(streamName, AccuRev.ACCUREV_TIME_SPEC.format(date == null ? new Date() : date));
   }

   public String getRevision(String stream, long fromTranId) {
      return this.getRevision(stream, Long.toString(fromTranId));
   }

   public String getRevision(String streamName, String transaction) {
      return this.streamToTag(streamName) + "/" + transaction;
   }

   public String getWorkSpaceRevision(String workspace) throws AccuRevException {
      return this.getRevision(workspace, Long.toString(this.getCurrentTransactionId(workspace)));
   }

   public Transaction getDepotTransaction(String stream, String tranSpec) throws AccuRevException {
      if (tranSpec == null) {
         tranSpec = "now";
      }

      List<Transaction> transactions = this.getAccuRev().history(stream, tranSpec, (String)null, 1, true, true);
      if (transactions != null && !transactions.isEmpty()) {
         return (Transaction)transactions.get(0);
      } else {
         this.logger.warn("Unable to find transaction for tranSpec=" + tranSpec);
         return null;
      }
   }

   public String getDepotTransactionId(String stream, String tranSpec) throws AccuRevException {
      Transaction t = this.getDepotTransaction(stream, tranSpec);
      return t == null ? tranSpec : Long.toString(t.getTranId());
   }

   private long getCurrentTransactionId(String workSpaceName) throws AccuRevException {
      AccuRev accuRev = this.getAccuRev();
      Map<String, WorkSpace> workSpaces = accuRev.showWorkSpaces();
      WorkSpace workspace = (WorkSpace)workSpaces.get(workSpaceName);
      if (workspace == null) {
         workSpaces = accuRev.showRefTrees();
         workspace = (WorkSpace)workSpaces.get(workSpaceName);
      }

      if (workspace == null) {
         throw new AccuRevException("Can't find workspace " + workSpaceName);
      } else {
         return workspace.getTransactionId();
      }
   }

   public String toString() {
      StringBuilder buff = new StringBuilder("AccuRevScmProviderRepository");
      buff.append(" user=");
      buff.append(this.getUser());
      buff.append(" pass=");
      buff.append(this.getPassword() == null ? "null" : StringUtils.repeat("*", this.getPassword().length()));
      buff.append(" host=");
      buff.append(this.getHost());
      buff.append(" port=");
      buff.append(this.getPort());
      buff.append(" stream=");
      buff.append(this.getStreamName());
      buff.append(" projectPath=");
      buff.append(this.getProjectPath());
      return buff.toString();
   }

   public static String formatTimeSpec(Date when) {
      return when == null ? "now" : AccuRev.ACCUREV_TIME_SPEC.format(when);
   }
}
