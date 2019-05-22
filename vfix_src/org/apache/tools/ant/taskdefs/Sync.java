package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.NoneSelector;

public class Sync extends Task {
   private Sync.MyCopy myCopy;
   private Sync.SyncTarget syncTarget;

   public void init() throws BuildException {
      this.myCopy = new Sync.MyCopy();
      this.configureTask(this.myCopy);
      this.myCopy.setFiltering(false);
      this.myCopy.setIncludeEmptyDirs(false);
      this.myCopy.setPreserveLastModified(true);
   }

   private void configureTask(Task helper) {
      helper.setProject(this.getProject());
      helper.setTaskName(this.getTaskName());
      helper.setOwningTarget(this.getOwningTarget());
      helper.init();
   }

   public void execute() throws BuildException {
      File toDir = this.myCopy.getToDir();
      Set allFiles = this.myCopy.nonOrphans;
      boolean noRemovalNecessary = !toDir.exists() || toDir.list().length < 1;
      this.log("PASS#1: Copying files to " + toDir, 4);
      this.myCopy.execute();
      if (noRemovalNecessary) {
         this.log("NO removing necessary in " + toDir, 4);
      } else {
         this.log("PASS#2: Removing orphan files from " + toDir, 4);
         int[] removedFileCount = this.removeOrphanFiles(allFiles, toDir);
         this.logRemovedCount(removedFileCount[0], "dangling director", "y", "ies");
         this.logRemovedCount(removedFileCount[1], "dangling file", "", "s");
         if (!this.myCopy.getIncludeEmptyDirs()) {
            this.log("PASS#3: Removing empty directories from " + toDir, 4);
            int removedDirCount = this.removeEmptyDirectories(toDir, false);
            this.logRemovedCount(removedDirCount, "empty director", "y", "ies");
         }

      }
   }

   private void logRemovedCount(int count, String prefix, String singularSuffix, String pluralSuffix) {
      File toDir = this.myCopy.getToDir();
      String what = prefix == null ? "" : prefix;
      what = what + (count < 2 ? singularSuffix : pluralSuffix);
      if (count > 0) {
         this.log("Removed " + count + " " + what + " from " + toDir, 2);
      } else {
         this.log("NO " + what + " to remove from " + toDir, 3);
      }

   }

   private int[] removeOrphanFiles(Set nonOrphans, File toDir) {
      int[] removedCount = new int[]{0, 0};
      String[] excls = (String[])((String[])nonOrphans.toArray(new String[nonOrphans.size() + 1]));
      excls[nonOrphans.size()] = "";
      DirectoryScanner ds = null;
      if (this.syncTarget != null) {
         FileSet fs = new FileSet();
         fs.setDir(toDir);
         fs.setCaseSensitive(this.syncTarget.isCaseSensitive());
         fs.setFollowSymlinks(this.syncTarget.isFollowSymlinks());
         PatternSet ps = this.syncTarget.mergePatterns(this.getProject());
         fs.appendExcludes(ps.getIncludePatterns(this.getProject()));
         fs.appendIncludes(ps.getExcludePatterns(this.getProject()));
         fs.setDefaultexcludes(!this.syncTarget.getDefaultexcludes());
         FileSelector[] s = this.syncTarget.getSelectors(this.getProject());
         if (s.length > 0) {
            NoneSelector ns = new NoneSelector();

            for(int i = 0; i < s.length; ++i) {
               ns.appendSelector(s[i]);
            }

            fs.appendSelector(ns);
         }

         ds = fs.getDirectoryScanner(this.getProject());
      } else {
         ds = new DirectoryScanner();
         ds.setBasedir(toDir);
      }

      ds.addExcludes(excls);
      ds.scan();
      String[] files = ds.getIncludedFiles();

      int var10002;
      for(int i = 0; i < files.length; ++i) {
         File f = new File(toDir, files[i]);
         this.log("Removing orphan file: " + f, 4);
         f.delete();
         var10002 = removedCount[1]++;
      }

      String[] dirs = ds.getIncludedDirectories();

      for(int i = dirs.length - 1; i >= 0; --i) {
         File f = new File(toDir, dirs[i]);
         if (f.list().length < 1) {
            this.log("Removing orphan directory: " + f, 4);
            f.delete();
            var10002 = removedCount[0]++;
         }
      }

      return removedCount;
   }

   private int removeEmptyDirectories(File dir, boolean removeIfEmpty) {
      int removedCount = 0;
      if (dir.isDirectory()) {
         File[] children = dir.listFiles();

         for(int i = 0; i < children.length; ++i) {
            File file = children[i];
            if (file.isDirectory()) {
               removedCount += this.removeEmptyDirectories(file, true);
            }
         }

         if (children.length > 0) {
            children = dir.listFiles();
         }

         if (children.length < 1 && removeIfEmpty) {
            this.log("Removing empty directory: " + dir, 4);
            dir.delete();
            ++removedCount;
         }
      }

      return removedCount;
   }

   public void setTodir(File destDir) {
      this.myCopy.setTodir(destDir);
   }

   public void setVerbose(boolean verbose) {
      this.myCopy.setVerbose(verbose);
   }

   public void setOverwrite(boolean overwrite) {
      this.myCopy.setOverwrite(overwrite);
   }

   public void setIncludeEmptyDirs(boolean includeEmpty) {
      this.myCopy.setIncludeEmptyDirs(includeEmpty);
   }

   public void setFailOnError(boolean failonerror) {
      this.myCopy.setFailOnError(failonerror);
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void add(ResourceCollection rc) {
      this.myCopy.add(rc);
   }

   public void setGranularity(long granularity) {
      this.myCopy.setGranularity(granularity);
   }

   public void addPreserveInTarget(Sync.SyncTarget s) {
      if (this.syncTarget != null) {
         throw new BuildException("you must not specify multiple preserveintarget elements.");
      } else {
         this.syncTarget = s;
      }
   }

   private static void assertTrue(String message, boolean condition) {
      if (!condition) {
         throw new BuildException("Assertion Error: " + message);
      }
   }

   public static class SyncTarget extends AbstractFileSet {
      public void setDir(File dir) throws BuildException {
         throw new BuildException("preserveintarget doesn't support the dir attribute");
      }
   }

   public static class MyCopy extends Copy {
      private Set nonOrphans = new HashSet();

      protected void scan(File fromDir, File toDir, String[] files, String[] dirs) {
         Sync.assertTrue("No mapper", this.mapperElement == null);
         super.scan(fromDir, toDir, files, dirs);

         int i;
         for(i = 0; i < files.length; ++i) {
            this.nonOrphans.add(files[i]);
         }

         for(i = 0; i < dirs.length; ++i) {
            this.nonOrphans.add(dirs[i]);
         }

      }

      protected Map scan(Resource[] resources, File toDir) {
         Sync.assertTrue("No mapper", this.mapperElement == null);
         Map m = super.scan(resources, toDir);
         Iterator iter = m.keySet().iterator();

         while(iter.hasNext()) {
            this.nonOrphans.add(((Resource)iter.next()).getName());
         }

         return m;
      }

      public File getToDir() {
         return this.destDir;
      }

      public boolean getIncludeEmptyDirs() {
         return this.includeEmpty;
      }

      protected boolean supportsNonFileResources() {
         return true;
      }
   }
}
