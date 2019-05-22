package org.apache.maven.scm;

import java.io.Serializable;

public class CommandParameter implements Serializable {
   private static final long serialVersionUID = -3391190831054016735L;
   public static final CommandParameter BINARY = new CommandParameter("binary");
   public static final CommandParameter RECURSIVE = new CommandParameter("recursive");
   public static final CommandParameter MESSAGE = new CommandParameter("message");
   public static final CommandParameter BRANCH_NAME = new CommandParameter("branchName");
   public static final CommandParameter START_DATE = new CommandParameter("startDate");
   public static final CommandParameter END_DATE = new CommandParameter("endDate");
   public static final CommandParameter NUM_DAYS = new CommandParameter("numDays");
   public static final CommandParameter LIMIT = new CommandParameter("limit");
   public static final CommandParameter BRANCH = new CommandParameter("branch");
   public static final CommandParameter START_SCM_VERSION = new CommandParameter("startScmVersion");
   public static final CommandParameter END_SCM_VERSION = new CommandParameter("endScmVersion");
   public static final CommandParameter CHANGELOG_DATE_PATTERN = new CommandParameter("changelogDatePattern");
   public static final CommandParameter SCM_VERSION = new CommandParameter("scmVersion");
   public static final CommandParameter TAG_NAME = new CommandParameter("tagName");
   public static final CommandParameter FILE = new CommandParameter("file");
   public static final CommandParameter FILES = new CommandParameter("files");
   public static final CommandParameter OUTPUT_FILE = new CommandParameter("outputFile");
   public static final CommandParameter OUTPUT_DIRECTORY = new CommandParameter("outputDirectory");
   public static final CommandParameter RUN_CHANGELOG_WITH_UPDATE = new CommandParameter("run_changelog_with_update");
   public static final CommandParameter SCM_TAG_PARAMETERS = new CommandParameter("ScmTagParameters");
   public static final CommandParameter SCM_BRANCH_PARAMETERS = new CommandParameter("ScmBranchParameters");
   public static final CommandParameter SCM_MKDIR_CREATE_IN_LOCAL = new CommandParameter("createInLocal");
   public static final CommandParameter SCM_SHORT_REVISION_LENGTH = new CommandParameter("shortRevisionLength");
   public static final CommandParameter FORCE_ADD = new CommandParameter("forceAdd");
   public static final CommandParameter IGNORE_WHITESPACE = new CommandParameter("ignoreWhitespace");
   private String name;

   private CommandParameter(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return this.name;
   }
}
