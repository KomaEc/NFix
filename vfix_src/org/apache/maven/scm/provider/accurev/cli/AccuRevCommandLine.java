package org.apache.maven.scm.provider.accurev.cli;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.AccuRev;
import org.apache.maven.scm.provider.accurev.AccuRevException;
import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.apache.maven.scm.provider.accurev.AccuRevStat;
import org.apache.maven.scm.provider.accurev.AccuRevVersion;
import org.apache.maven.scm.provider.accurev.CategorisedElements;
import org.apache.maven.scm.provider.accurev.FileDifference;
import org.apache.maven.scm.provider.accurev.Stream;
import org.apache.maven.scm.provider.accurev.Transaction;
import org.apache.maven.scm.provider.accurev.WorkSpace;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class AccuRevCommandLine implements AccuRev {
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final File CURRENT_DIR = new File(".");
   private ScmLogger logger;
   private Commandline cl;
   private StringBuilder commandLines;
   private StringBuilder errorOutput;
   private StreamConsumer systemErr;
   private String[] hostArgs;
   private String[] authArgs;
   private String executable;
   private long executableModTime;
   private String clientVersion;

   public AccuRevCommandLine() {
      this.cl = new Commandline();
      this.commandLines = new StringBuilder();
      this.errorOutput = new StringBuilder();
      this.hostArgs = EMPTY_STRING_ARRAY;
      this.authArgs = EMPTY_STRING_ARRAY;
      this.executable = "accurev";
      this.reset();
   }

   public AccuRevCommandLine(String host, int port) {
      this();
      this.setServer(host, port);
   }

   public void setServer(String host, int port) {
      if (host != null) {
         this.hostArgs = new String[]{"-H", host + ":" + port};
      } else {
         this.hostArgs = EMPTY_STRING_ARRAY;
      }

   }

   public void setExecutable(String accuRevExe) {
      this.executable = accuRevExe;
      this.reset();
   }

   private boolean executeCommandLine(File basedir, String[] args, Iterable<File> elements, Pattern matchPattern, List<File> matchedFiles) throws AccuRevException {
      FileConsumer stdoutConsumer = new FileConsumer(matchedFiles, matchPattern);
      return this.executeCommandLine((File)basedir, (String[])args, (Iterable)elements, stdoutConsumer);
   }

   private boolean executeCommandLine(File basedir, String[] args, Iterable<File> elements, StreamConsumer stdoutConsumer) throws AccuRevException {
      this.setWorkingDirectory(basedir);
      this.setCommandLineArgs(args);
      String path;
      if (elements != null) {
         for(Iterator i$ = elements.iterator(); i$.hasNext(); this.cl.createArg().setValue(path)) {
            File file = (File)i$.next();
            path = file.getPath();
            if ("\\.".equals(path)) {
               path = "\\.\\";
            }
         }
      }

      return this.executeCommandLine((InputStream)null, stdoutConsumer) == 0;
   }

   private void setCommandLineArgs(String[] args) {
      this.cl.clearArgs();
      if (args.length > 0) {
         this.cl.createArg().setValue(args[0]);
         this.cl.addArguments(this.hostArgs);
         this.cl.addArguments(this.authArgs);
      }

      for(int i = 1; i < args.length; ++i) {
         this.cl.createArg().setValue(args[i]);
      }

   }

   private boolean executeCommandLine(String[] args) throws AccuRevException {
      return this.executeCommandLine(args, (InputStream)null, (StreamConsumer)null) == 0;
   }

   private int executeCommandLine(String[] args, InputStream stdin, StreamConsumer stdout) throws AccuRevException {
      this.setCommandLineArgs(args);
      return this.executeCommandLine(stdin, stdout);
   }

   private int executeCommandLine(InputStream stdin, StreamConsumer stdout) throws AccuRevException {
      this.commandLines.append(this.cl.toString());
      this.commandLines.append(';');
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug(this.cl.toString());
      }

      try {
         int result = this.executeCommandLine(this.cl, stdin, new CommandOutputConsumer(this.getLogger(), stdout), this.systemErr);
         if (result != 0) {
            this.getLogger().debug("Non zero result - " + result);
         }

         return result;
      } catch (CommandLineException var4) {
         throw new AccuRevException("Error executing command " + this.cl.toString(), var4);
      }
   }

   protected int executeCommandLine(Commandline cl, InputStream stdin, CommandOutputConsumer stdout, StreamConsumer stderr) throws CommandLineException {
      int result = CommandLineUtils.executeCommandLine(cl, stdin, stdout, stderr);
      stdout.waitComplete();
      return result;
   }

   protected Commandline getCommandline() {
      return this.cl;
   }

   public void reset() {
      this.cl = new Commandline();
      this.commandLines = new StringBuilder();
      this.errorOutput = new StringBuilder();
      this.systemErr = new ErrorConsumer(this.getLogger(), this.errorOutput);
      this.cl.getShell().setQuotedArgumentsEnabled(true);
      this.cl.setExecutable(this.executable);

      try {
         this.cl.addSystemEnvironment();
      } catch (Exception var2) {
         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("Unable to obtain system environment", var2);
         } else {
            this.getLogger().warn("Unable to obtain system environment");
         }
      }

   }

   public boolean mkws(String basisStream, String workspaceName, File basedir) throws AccuRevException {
      this.setWorkingDirectory(basedir);
      String[] mkws = new String[]{"mkws", "-b", basisStream, "-w", workspaceName, "-l", basedir.getAbsolutePath()};
      return this.executeCommandLine(mkws);
   }

   public List<File> update(File baseDir, String transactionId) throws AccuRevException {
      if (transactionId == null) {
         transactionId = "highest";
      }

      String[] update = new String[]{"update", "-t", transactionId};
      this.setWorkingDirectory(baseDir);
      List<File> updatedFiles = new ArrayList();
      int ret = this.executeCommandLine(update, (InputStream)null, new FileConsumer(updatedFiles, FileConsumer.UPDATE_PATTERN));
      return ret == 0 ? updatedFiles : null;
   }

   public List<File> add(File basedir, List<File> elements, String message) throws AccuRevException {
      if (StringUtils.isBlank(message)) {
         message = "initial version (maven-scm)";
      }

      boolean recursive = false;
      if (elements != null && !elements.isEmpty()) {
         if (elements.size() == 1 && elements.toArray()[0].equals(CURRENT_DIR)) {
            recursive = true;
         }
      } else {
         elements = Collections.singletonList(CURRENT_DIR);
         recursive = true;
      }

      List<File> addedFiles = new ArrayList();
      return this.executeCommandLine(basedir, new String[]{"add", "-c", message, recursive ? "-R" : null}, elements, FileConsumer.ADD_PATTERN, addedFiles) ? addedFiles : null;
   }

   public List<File> defunct(File basedir, List<File> files, String message) throws AccuRevException {
      if (StringUtils.isBlank(message)) {
         message = "removed (maven-scm)";
      }

      if (files == null || files.isEmpty()) {
         files = Collections.singletonList(CURRENT_DIR);
      }

      ArrayList<File> defunctFiles = new ArrayList();
      return this.executeCommandLine(basedir, new String[]{"defunct", "-c", message}, files, FileConsumer.DEFUNCT_PATTERN, defunctFiles) ? defunctFiles : null;
   }

   public List<File> promote(File basedir, List<File> files, String message) throws AccuRevException {
      if (StringUtils.isBlank(message)) {
         message = "promote (maven-scm)";
      }

      List<File> promotedFiles = new ArrayList();
      return this.executeCommandLine(basedir, new String[]{"promote", "-K", "-c", message}, files, FileConsumer.PROMOTE_PATTERN, promotedFiles) ? promotedFiles : null;
   }

   public String getCommandLines() {
      return this.commandLines.toString();
   }

   public String getErrorOutput() {
      return this.errorOutput.toString();
   }

   public void setLogger(ScmLogger logger) {
      this.logger = logger;
   }

   public ScmLogger getLogger() {
      return this.logger;
   }

   public boolean mkdepot(String depotName) throws AccuRevException {
      String[] mkdepot = new String[]{"mkdepot", "-p", depotName};
      return this.executeCommandLine(mkdepot);
   }

   public boolean mkstream(String backingStream, String newStreamName) throws AccuRevException {
      String[] mkstream = new String[]{"mkstream", "-b", backingStream, "-s", newStreamName};
      return this.executeCommandLine(mkstream);
   }

   public boolean promoteStream(String subStream, String commitMessage, List<File> promotedFiles) throws AccuRevException {
      String[] promote = new String[]{"promote", "-s", subStream, "-d"};
      return this.executeCommandLine(promote);
   }

   public List<File> promoteAll(File baseDir, String commitMessage) throws AccuRevException {
      this.setWorkingDirectory(baseDir);
      String[] promote = new String[]{"promote", "-p", "-K", "-c", commitMessage};
      List<File> promotedFiles = new ArrayList();
      int ret = this.executeCommandLine(promote, (InputStream)null, new FileConsumer(promotedFiles, FileConsumer.PROMOTE_PATTERN));
      return ret == 0 ? promotedFiles : null;
   }

   public AccuRevInfo info(File basedir) throws AccuRevException {
      this.setWorkingDirectory(basedir);
      String[] info = new String[]{"info"};
      AccuRevInfo result = new AccuRevInfo(basedir);
      this.executeCommandLine(info, (InputStream)null, new InfoConsumer(result));
      return result;
   }

   private void setWorkingDirectory(File basedir) {
      if (basedir == null) {
         this.cl.setWorkingDirectory(".");
      }

      this.cl.setWorkingDirectory(basedir);
   }

   public boolean reactivate(String workSpaceName) throws AccuRevException {
      String[] reactivate = new String[]{"reactivate", "wspace", workSpaceName};
      return this.executeCommandLine(reactivate, (InputStream)null, new CommandOutputConsumer(this.getLogger(), (StreamConsumer)null)) == 0;
   }

   public boolean rmws(String workSpaceName) throws AccuRevException {
      String[] rmws = new String[]{"rmws", "-s", workSpaceName};
      return this.executeCommandLine(rmws);
   }

   public String stat(File element) throws AccuRevException {
      String[] stat = new String[]{"stat", "-fx", element.getAbsolutePath()};
      StatConsumer statConsumer = new StatConsumer(this.getLogger());
      this.executeCommandLine(stat, (InputStream)null, statConsumer);
      return statConsumer.getStatus();
   }

   public boolean chws(File basedir, String workSpaceName, String newBasisStream) throws AccuRevException {
      this.setWorkingDirectory(basedir);
      return this.executeCommandLine(new String[]{"chws", "-s", workSpaceName, "-b", newBasisStream, "-l", "."});
   }

   public boolean mksnap(String snapShotName, String basisStream) throws AccuRevException {
      return this.executeCommandLine(new String[]{"mksnap", "-s", snapShotName, "-b", basisStream, "-t", "now"});
   }

   public List<File> statTag(String streamName) throws AccuRevException {
      List<File> taggedFiles = new ArrayList();
      String[] stat = new String[]{"stat", "-a", "-ffl", "-s", streamName};
      return this.executeCommandLine((File)null, stat, (Iterable)null, FileConsumer.STAT_PATTERN, taggedFiles) ? taggedFiles : null;
   }

   public List<File> stat(File basedir, Collection<File> elements, AccuRevStat statType) throws AccuRevException {
      boolean recursive = false;
      if (elements != null && !((Collection)elements).isEmpty()) {
         if (((Collection)elements).size() == 1 && ((Collection)elements).toArray()[0].equals(CURRENT_DIR)) {
            recursive = true;
         }
      } else {
         elements = Collections.singletonList(CURRENT_DIR);
         recursive = true;
      }

      String[] args = new String[]{"stat", "-ffr", statType.getStatArg(), recursive ? "-R" : null};
      List<File> matchingElements = new ArrayList();
      boolean ret = this.executeCommandLine(basedir, args, (Iterable)elements, statType.getMatchPattern(), matchingElements);
      return ret ? matchingElements : null;
   }

   public List<File> pop(File basedir, Collection<File> elements) throws AccuRevException {
      if (elements == null || ((Collection)elements).isEmpty()) {
         elements = Collections.singletonList(CURRENT_DIR);
      }

      String[] popws = new String[]{"pop", "-R"};
      List<File> poppedFiles = new ArrayList();
      boolean ret = this.executeCommandLine(basedir, popws, (Iterable)elements, FileConsumer.POPULATE_PATTERN, poppedFiles);
      return ret ? poppedFiles : null;
   }

   public List<File> popExternal(File basedir, String versionSpec, String tranSpec, Collection<File> elements) throws AccuRevException {
      if (elements == null || ((Collection)elements).isEmpty()) {
         elements = Collections.singletonList(new File("/./"));
      }

      if (StringUtils.isBlank(tranSpec)) {
         tranSpec = "now";
      }

      String[] popArgs;
      if (AccuRevVersion.isNow(tranSpec)) {
         popArgs = new String[]{"pop", "-v", versionSpec, "-L", basedir.getAbsolutePath(), "-R"};
      } else {
         popArgs = new String[]{"pop", "-v", versionSpec, "-L", basedir.getAbsolutePath(), "-t", tranSpec, "-R"};
      }

      List<File> poppedFiles = new ArrayList();
      boolean ret = this.executeCommandLine(basedir, popArgs, (Iterable)elements, FileConsumer.POPULATE_PATTERN, poppedFiles);
      return ret ? poppedFiles : null;
   }

   public CategorisedElements statBackingStream(File basedir, Collection<File> elements) throws AccuRevException {
      CategorisedElements catElems = new CategorisedElements();
      if (elements.isEmpty()) {
         return catElems;
      } else {
         String[] args = new String[]{"stat", "-b", "-ffr"};
         boolean ret = this.executeCommandLine((File)basedir, (String[])args, (Iterable)elements, new StatBackingConsumer(catElems.getMemberElements(), catElems.getNonMemberElements()));
         return ret ? catElems : null;
      }
   }

   public List<Transaction> history(String baseStream, String fromTimeSpec, String toTimeSpec, int count, boolean depotHistory, boolean transactionsOnly) throws AccuRevException {
      String timeSpec = fromTimeSpec;
      if (toTimeSpec != null) {
         timeSpec = fromTimeSpec + "-" + toTimeSpec;
      }

      if (count > 0) {
         timeSpec = timeSpec + "." + count;
      }

      String[] hist = new String[]{"hist", transactionsOnly ? "-ftx" : "-fx", depotHistory ? "-p" : "-s", baseStream, "-t", timeSpec};
      ArrayList<Transaction> transactions = new ArrayList();
      HistoryConsumer stdout = new HistoryConsumer(this.getLogger(), transactions);
      return this.executeCommandLine(hist, (InputStream)null, stdout) == 0 ? transactions : null;
   }

   public List<FileDifference> diff(String baseStream, String fromTimeSpec, String toTimeSpec) throws AccuRevException {
      String timeSpec = fromTimeSpec + "-" + toTimeSpec;
      String[] diff = new String[]{"diff", "-fx", "-a", "-i", "-v", baseStream, "-V", baseStream, "-t", timeSpec};
      List<FileDifference> results = new ArrayList();
      DiffConsumer stdout = new DiffConsumer(this.getLogger(), results);
      return this.executeCommandLine(diff, (InputStream)null, stdout) < 2 ? results : null;
   }

   public boolean login(String user, String password) throws AccuRevException {
      this.cl.setWorkingDirectory(".");
      this.authArgs = EMPTY_STRING_ARRAY;
      AuthTokenConsumer stdout = new AuthTokenConsumer();
      String[] login;
      boolean result;
      if (Os.isFamily("windows")) {
         if (StringUtils.isBlank(password)) {
            password = "\"\"";
         }

         login = new String[]{"login", "-A", user, password};
         result = this.executeCommandLine(login, (InputStream)null, stdout) == 0;
      } else {
         login = new String[]{"login", "-A", user};
         password = StringUtils.clean(password) + "\n";
         byte[] bytes = password.getBytes();
         ByteArrayInputStream stdin = new ByteArrayInputStream(bytes);
         result = this.executeCommandLine(login, stdin, stdout) == 0;
      }

      this.authArgs = new String[]{"-A", stdout.getAuthToken()};
      return result;
   }

   public boolean logout() throws AccuRevException {
      String[] logout = new String[]{"logout"};
      return this.executeCommandLine(logout);
   }

   public List<BlameLine> annotate(File basedir, File file) throws AccuRevException {
      String[] annotate = new String[]{"annotate", "-ftud"};
      List<BlameLine> lines = new ArrayList();
      AnnotateConsumer stdout = new AnnotateConsumer(lines, this.getLogger());
      return this.executeCommandLine((File)basedir, (String[])annotate, (Iterable)Collections.singletonList(file), stdout) ? lines : null;
   }

   public Map<String, WorkSpace> showRefTrees() throws AccuRevException {
      String[] show = new String[]{"show", "-fx", "refs"};
      Map<String, WorkSpace> refTrees = new HashMap();
      WorkSpaceConsumer stdout = new WorkSpaceConsumer(this.getLogger(), refTrees);
      return this.executeCommandLine(show, (InputStream)null, stdout) == 0 ? refTrees : null;
   }

   public Map<String, WorkSpace> showWorkSpaces() throws AccuRevException {
      String[] show = new String[]{"show", "-a", "-fx", "wspaces"};
      Map<String, WorkSpace> workSpaces = new HashMap();
      WorkSpaceConsumer stdout = new WorkSpaceConsumer(this.getLogger(), workSpaces);
      return this.executeCommandLine(show, (InputStream)null, stdout) == 0 ? workSpaces : null;
   }

   public Stream showStream(String stream) throws AccuRevException {
      String[] show = new String[]{"show", "-s", stream, "-fx", "streams"};
      List<Stream> streams = new ArrayList();
      StreamsConsumer stdout = new StreamsConsumer(this.getLogger(), streams);
      return this.executeCommandLine(show, (InputStream)null, stdout) == 0 && streams.size() == 1 ? (Stream)streams.get(0) : null;
   }

   public String getExecutable() {
      return this.executable;
   }

   public String getClientVersion() throws AccuRevException {
      long lastModified = (new File(this.getExecutable())).lastModified();
      if (this.clientVersion == null || this.executableModTime != lastModified) {
         this.executableModTime = lastModified;
         ClientVersionConsumer stdout = new ClientVersionConsumer();
         this.executeCommandLine(new String[0], (InputStream)null, stdout);
         this.clientVersion = stdout.getClientVersion();
      }

      return this.clientVersion;
   }

   public boolean syncReplica() throws AccuRevException {
      return this.executeCommandLine(new String[]{"replica", "sync"});
   }
}
