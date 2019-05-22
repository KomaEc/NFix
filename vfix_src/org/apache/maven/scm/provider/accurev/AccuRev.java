package org.apache.maven.scm.provider.accurev;

import java.io.File;
import java.text.DateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.command.blame.BlameLine;
import org.apache.maven.scm.util.ThreadSafeDateFormat;

public interface AccuRev {
   String DEFAULT_ACCUREV_EXECUTABLE = "accurev";
   int DEFAULT_PORT = 5050;
   String ACCUREV_TIME_FORMAT_STRING = "yyyy/MM/dd HH:mm:ss";
   DateFormat ACCUREV_TIME_SPEC = new ThreadSafeDateFormat("yyyy/MM/dd HH:mm:ss");
   String DEFAULT_REMOVE_MESSAGE = "removed (maven-scm)";
   String DEFAULT_ADD_MESSAGE = "initial version (maven-scm)";
   String DEFAULT_PROMOTE_MESSAGE = "promote (maven-scm)";

   void reset();

   List<File> popExternal(File var1, String var2, String var3, Collection<File> var4) throws AccuRevException;

   List<File> pop(File var1, Collection<File> var2) throws AccuRevException;

   boolean mkws(String var1, String var2, File var3) throws AccuRevException;

   List<File> update(File var1, String var2) throws AccuRevException;

   AccuRevInfo info(File var1) throws AccuRevException;

   boolean rmws(String var1) throws AccuRevException;

   boolean reactivate(String var1) throws AccuRevException;

   String getCommandLines();

   String getErrorOutput();

   List<File> add(File var1, List<File> var2, String var3) throws AccuRevException;

   List<File> defunct(File var1, List<File> var2, String var3) throws AccuRevException;

   List<File> promoteAll(File var1, String var2) throws AccuRevException;

   List<File> promote(File var1, List<File> var2, String var3) throws AccuRevException;

   boolean chws(File var1, String var2, String var3) throws AccuRevException;

   boolean mksnap(String var1, String var2) throws AccuRevException;

   List<File> statTag(String var1) throws AccuRevException;

   CategorisedElements statBackingStream(File var1, Collection<File> var2) throws AccuRevException;

   List<File> stat(File var1, Collection<File> var2, AccuRevStat var3) throws AccuRevException;

   String stat(File var1) throws AccuRevException;

   List<Transaction> history(String var1, String var2, String var3, int var4, boolean var5, boolean var6) throws AccuRevException;

   List<FileDifference> diff(String var1, String var2, String var3) throws AccuRevException;

   List<BlameLine> annotate(File var1, File var2) throws AccuRevException;

   boolean login(String var1, String var2) throws AccuRevException;

   Map<String, WorkSpace> showWorkSpaces() throws AccuRevException;

   Map<String, WorkSpace> showRefTrees() throws AccuRevException;

   Stream showStream(String var1) throws AccuRevException;

   String getExecutable();

   String getClientVersion() throws AccuRevException;

   boolean syncReplica() throws AccuRevException;
}
