package org.apache.maven.scm.provider.svn;

import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.provider.svn.repository.SvnScmProviderRepository;
import org.codehaus.plexus.util.StringUtils;

public final class SvnTagBranchUtils {
   public static final String[] REVISION_SPECIFIERS = new String[]{"HEAD", "BASE", "COMMITTED", "PREV"};
   public static final String SVN_TRUNK = "trunk";
   public static final String SVN_BRANCHES = "branches";
   public static final String SVN_TAGS = "tags";
   public static final String[] SVN_BASE_DIRS = new String[]{"trunk", "branches", "tags"};

   private SvnTagBranchUtils() {
   }

   static String appendPath(String basePath, String addlPath) {
      basePath = StringUtils.stripEnd(basePath, "/");
      return StringUtils.isEmpty(addlPath) ? basePath : basePath + "/" + StringUtils.stripStart(addlPath, "/");
   }

   public static String getProjectRoot(String repoPath) {
      for(int i = 0; i < SVN_BASE_DIRS.length; ++i) {
         String base = "/" + SVN_BASE_DIRS[i];
         int pos = repoPath.lastIndexOf(base + "/");
         if (repoPath.endsWith(base)) {
            return repoPath.substring(0, repoPath.length() - base.length());
         }

         if (pos >= 0) {
            return repoPath.substring(0, pos);
         }
      }

      return appendPath(repoPath, (String)null);
   }

   public static String resolveTagBase(SvnScmProviderRepository repository) {
      return resolveTagBase(repository.getUrl());
   }

   public static String resolveTagBase(String repositoryUrl) {
      return appendPath(getProjectRoot(repositoryUrl), "tags");
   }

   public static String resolveBranchBase(SvnScmProviderRepository repository) {
      return resolveBranchBase(repository.getUrl());
   }

   public static String resolveBranchBase(String repositoryUrl) {
      return appendPath(getProjectRoot(repositoryUrl), "branches");
   }

   public static String resolveTagUrl(SvnScmProviderRepository repository, ScmTag tag) {
      return resolveUrl(repository.getUrl(), repository.getTagBase(), "tags", tag);
   }

   public static String resolveTagUrl(String repositoryUrl, ScmTag tag) {
      return resolveUrl(repositoryUrl, (String)null, "tags", tag);
   }

   public static String resolveBranchUrl(SvnScmProviderRepository repository, ScmBranch branch) {
      return resolveUrl(repository.getUrl(), repository.getBranchBase(), "branches", branch);
   }

   public static String resolveBranchUrl(String repositoryUrl, ScmBranch branch) {
      return resolveUrl(repositoryUrl, resolveBranchBase(repositoryUrl), "branches", branch);
   }

   private static String addSuffix(String baseString, String suffix) {
      return suffix != null ? baseString + suffix : baseString;
   }

   public static String resolveUrl(String repositoryUrl, String tagBase, String subdir, ScmBranch branchTag) {
      String branchTagName = branchTag.getName();
      String projectRoot = getProjectRoot(repositoryUrl);
      branchTagName = StringUtils.strip(branchTagName, "/");
      if (StringUtils.isEmpty(branchTagName)) {
         return null;
      } else {
         String queryString = null;
         if (repositoryUrl.indexOf(63) >= 0) {
            queryString = repositoryUrl.substring(repositoryUrl.indexOf(63));
            projectRoot = StringUtils.replace(projectRoot, queryString, "");
         }

         if (branchTagName.indexOf("://") >= 0) {
            return branchTagName;
         } else if (StringUtils.isNotEmpty(tagBase) && !tagBase.equals(resolveTagBase(repositoryUrl)) && !tagBase.equals(resolveBranchBase(repositoryUrl))) {
            return appendPath(tagBase, branchTagName);
         } else {
            for(int i = 0; i < SVN_BASE_DIRS.length; ++i) {
               if (branchTagName.startsWith(SVN_BASE_DIRS[i] + "/")) {
                  return addSuffix(appendPath(projectRoot, branchTagName), queryString);
               }
            }

            return addSuffix(appendPath(appendPath(projectRoot, subdir), branchTagName), queryString);
         }
      }
   }

   private static boolean checkRevisionArg(String arg) {
      if (StringUtils.isNumeric(arg) || arg.startsWith("{") && arg.endsWith("}")) {
         return true;
      } else {
         for(int i = 0; i < REVISION_SPECIFIERS.length; ++i) {
            if (REVISION_SPECIFIERS[i].equalsIgnoreCase(arg)) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isRevisionSpecifier(ScmVersion version) {
      if (version == null) {
         return false;
      } else {
         String versionName = version.getName();
         if (StringUtils.isEmpty(versionName)) {
            return false;
         } else if (checkRevisionArg(versionName)) {
            return true;
         } else {
            String[] parts = StringUtils.split(versionName, ":");
            if (parts.length == 2 && StringUtils.isNotEmpty(parts[0]) && StringUtils.isNotEmpty(parts[1])) {
               return checkRevisionArg(parts[0]) && checkRevisionArg(parts[1]);
            } else {
               return false;
            }
         }
      }
   }
}
