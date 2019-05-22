package org.apache.maven.scm.provider.hg.command;

public final class HgCommandConstants {
   public static final String EXEC = "hg";
   public static final String INIT_CMD = "init";
   public static final String ADD_CMD = "add";
   public static final String STATUS_CMD = "status";
   public static final String REMOVE_CMD = "remove";
   public static final String CLONE_CMD = "clone";
   public static final String BRANCH_CMD = "branch";
   public static final String COMMIT_CMD = "commit";
   public static final String UPDATE_CMD = "update";
   public static final String PULL_CMD = "pull";
   public static final String LOG_CMD = "log";
   public static final String DIFF_CMD = "diff";
   public static final String PUSH_CMD = "push";
   public static final String REVNO_CMD = "id";
   public static final String TAG_CMD = "tag";
   public static final String INVENTORY_CMD = "locate";
   public static final String OUTGOING_CMD = "outgoing";
   public static final String BRANCH_NAME_CMD = "branch";
   public static final String NO_RECURSE_OPTION = "";
   public static final String MESSAGE_OPTION = "--message";
   public static final String REVISION_OPTION = "-r";
   public static final String DATE_OPTION = "--date";
   public static final String VERBOSE_OPTION = "--verbose";
   public static final String NO_MERGES_OPTION = "--no-merges";
   public static final String VERSION = "version";
   public static final String CHECK = "check";
   public static final String ALL_OPTION = "-A";
   public static final String NEW_BRANCH_OPTION = "--new-branch";
   public static final String CLEAN_OPTION = "-c";
   public static final String TEMPLATE_OPTION = "--template";
   public static final String LIMIT_OPTION = "--limit";
   public static final String TEMPLATE_FORMAT = "changeset:   {rev}:{node|short}\nbranch:      {branch}\nuser:        {author}\ndate:        {date|isodatesec}\ntag:         {tags}\nfiles:       {files}\ndescription:\n{desc}\n";

   private HgCommandConstants() {
   }
}
