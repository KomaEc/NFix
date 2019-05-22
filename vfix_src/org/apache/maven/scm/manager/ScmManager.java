package org.apache.maven.scm.manager;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
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
import org.apache.maven.scm.provider.ScmProvider;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.scm.repository.UnknownRepositoryStructure;

public interface ScmManager {
   String ROLE = ScmManager.class.getName();

   ScmRepository makeScmRepository(String var1) throws ScmRepositoryException, NoSuchScmProviderException;

   ScmRepository makeProviderScmRepository(String var1, File var2) throws ScmRepositoryException, UnknownRepositoryStructure, NoSuchScmProviderException;

   List<String> validateScmRepository(String var1);

   ScmProvider getProviderByUrl(String var1) throws ScmRepositoryException, NoSuchScmProviderException;

   ScmProvider getProviderByType(String var1) throws NoSuchScmProviderException;

   ScmProvider getProviderByRepository(ScmRepository var1) throws NoSuchScmProviderException;

   void setScmProvider(String var1, ScmProvider var2);

   void setScmProviderImplementation(String var1, String var2);

   AddScmResult add(ScmRepository var1, ScmFileSet var2) throws ScmException;

   AddScmResult add(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   BranchScmResult branch(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   BranchScmResult branch(ScmRepository var1, ScmFileSet var2, String var3, String var4) throws ScmException;

   /** @deprecated */
   @Deprecated
   ChangeLogScmResult changeLog(ScmRepository var1, ScmFileSet var2, Date var3, Date var4, int var5, ScmBranch var6) throws ScmException;

   /** @deprecated */
   @Deprecated
   ChangeLogScmResult changeLog(ScmRepository var1, ScmFileSet var2, Date var3, Date var4, int var5, ScmBranch var6, String var7) throws ScmException;

   ChangeLogScmResult changeLog(ChangeLogScmRequest var1) throws ScmException;

   /** @deprecated */
   @Deprecated
   ChangeLogScmResult changeLog(ScmRepository var1, ScmFileSet var2, ScmVersion var3, ScmVersion var4) throws ScmException;

   /** @deprecated */
   @Deprecated
   ChangeLogScmResult changeLog(ScmRepository var1, ScmFileSet var2, ScmVersion var3, ScmVersion var4, String var5) throws ScmException;

   CheckInScmResult checkIn(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   CheckInScmResult checkIn(ScmRepository var1, ScmFileSet var2, ScmVersion var3, String var4) throws ScmException;

   CheckOutScmResult checkOut(ScmRepository var1, ScmFileSet var2) throws ScmException;

   CheckOutScmResult checkOut(ScmRepository var1, ScmFileSet var2, ScmVersion var3) throws ScmException;

   CheckOutScmResult checkOut(ScmRepository var1, ScmFileSet var2, boolean var3) throws ScmException;

   CheckOutScmResult checkOut(ScmRepository var1, ScmFileSet var2, ScmVersion var3, boolean var4) throws ScmException;

   DiffScmResult diff(ScmRepository var1, ScmFileSet var2, ScmVersion var3, ScmVersion var4) throws ScmException;

   EditScmResult edit(ScmRepository var1, ScmFileSet var2) throws ScmException;

   ExportScmResult export(ScmRepository var1, ScmFileSet var2) throws ScmException;

   ExportScmResult export(ScmRepository var1, ScmFileSet var2, ScmVersion var3) throws ScmException;

   ExportScmResult export(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   ExportScmResult export(ScmRepository var1, ScmFileSet var2, ScmVersion var3, String var4) throws ScmException;

   ListScmResult list(ScmRepository var1, ScmFileSet var2, boolean var3, ScmVersion var4) throws ScmException;

   MkdirScmResult mkdir(ScmRepository var1, ScmFileSet var2, String var3, boolean var4) throws ScmException;

   RemoveScmResult remove(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   StatusScmResult status(ScmRepository var1, ScmFileSet var2) throws ScmException;

   TagScmResult tag(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   TagScmResult tag(ScmRepository var1, ScmFileSet var2, String var3, String var4) throws ScmException;

   UnEditScmResult unedit(ScmRepository var1, ScmFileSet var2) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, ScmVersion var3) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, boolean var3) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, ScmVersion var3, boolean var4) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, ScmVersion var3, String var4) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, Date var3) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, ScmVersion var3, Date var4) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, Date var3, String var4) throws ScmException;

   UpdateScmResult update(ScmRepository var1, ScmFileSet var2, ScmVersion var3, Date var4, String var5) throws ScmException;

   BlameScmResult blame(ScmRepository var1, ScmFileSet var2, String var3) throws ScmException;

   BlameScmResult blame(BlameScmRequest var1) throws ScmException;
}
