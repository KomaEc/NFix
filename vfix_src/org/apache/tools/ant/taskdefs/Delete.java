package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.FileResourceIterator;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Sort;
import org.apache.tools.ant.types.resources.comparators.FileSystem;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.apache.tools.ant.types.resources.comparators.Reverse;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public class Delete extends MatchingTask {
   private static final int DELETE_RETRY_SLEEP_MILLIS = 10;
   private static final ResourceComparator REVERSE_FILESYSTEM = new Reverse(new FileSystem());
   private static final ResourceSelector EXISTS = new Exists();
   protected File file = null;
   protected File dir = null;
   protected Vector filesets = new Vector();
   protected boolean usedMatchingTask = false;
   protected boolean includeEmpty = false;
   private int verbosity = 3;
   private boolean quiet = false;
   private boolean failonerror = true;
   private boolean deleteOnExit = false;
   private Resources rcs = null;

   public void setFile(File file) {
      this.file = file;
   }

   public void setDir(File dir) {
      this.dir = dir;
      this.getImplicitFileSet().setDir(dir);
   }

   public void setVerbose(boolean verbose) {
      if (verbose) {
         this.verbosity = 2;
      } else {
         this.verbosity = 3;
      }

   }

   public void setQuiet(boolean quiet) {
      this.quiet = quiet;
      if (quiet) {
         this.failonerror = false;
      }

   }

   public void setFailOnError(boolean failonerror) {
      this.failonerror = failonerror;
   }

   public void setDeleteOnExit(boolean deleteOnExit) {
      this.deleteOnExit = deleteOnExit;
   }

   public void setIncludeEmptyDirs(boolean includeEmpty) {
      this.includeEmpty = includeEmpty;
   }

   public void addFileset(FileSet set) {
      this.filesets.addElement(set);
   }

   public void add(ResourceCollection rc) {
      if (rc != null) {
         this.rcs = this.rcs == null ? new Resources() : this.rcs;
         this.rcs.add(rc);
      }
   }

   public PatternSet.NameEntry createInclude() {
      this.usedMatchingTask = true;
      return super.createInclude();
   }

   public PatternSet.NameEntry createIncludesFile() {
      this.usedMatchingTask = true;
      return super.createIncludesFile();
   }

   public PatternSet.NameEntry createExclude() {
      this.usedMatchingTask = true;
      return super.createExclude();
   }

   public PatternSet.NameEntry createExcludesFile() {
      this.usedMatchingTask = true;
      return super.createExcludesFile();
   }

   public PatternSet createPatternSet() {
      this.usedMatchingTask = true;
      return super.createPatternSet();
   }

   public void setIncludes(String includes) {
      this.usedMatchingTask = true;
      super.setIncludes(includes);
   }

   public void setExcludes(String excludes) {
      this.usedMatchingTask = true;
      super.setExcludes(excludes);
   }

   public void setDefaultexcludes(boolean useDefaultExcludes) {
      this.usedMatchingTask = true;
      super.setDefaultexcludes(useDefaultExcludes);
   }

   public void setIncludesfile(File includesfile) {
      this.usedMatchingTask = true;
      super.setIncludesfile(includesfile);
   }

   public void setExcludesfile(File excludesfile) {
      this.usedMatchingTask = true;
      super.setExcludesfile(excludesfile);
   }

   public void setCaseSensitive(boolean isCaseSensitive) {
      this.usedMatchingTask = true;
      super.setCaseSensitive(isCaseSensitive);
   }

   public void setFollowSymlinks(boolean followSymlinks) {
      this.usedMatchingTask = true;
      super.setFollowSymlinks(followSymlinks);
   }

   public void addSelector(SelectSelector selector) {
      this.usedMatchingTask = true;
      super.addSelector(selector);
   }

   public void addAnd(AndSelector selector) {
      this.usedMatchingTask = true;
      super.addAnd(selector);
   }

   public void addOr(OrSelector selector) {
      this.usedMatchingTask = true;
      super.addOr(selector);
   }

   public void addNot(NotSelector selector) {
      this.usedMatchingTask = true;
      super.addNot(selector);
   }

   public void addNone(NoneSelector selector) {
      this.usedMatchingTask = true;
      super.addNone(selector);
   }

   public void addMajority(MajoritySelector selector) {
      this.usedMatchingTask = true;
      super.addMajority(selector);
   }

   public void addDate(DateSelector selector) {
      this.usedMatchingTask = true;
      super.addDate(selector);
   }

   public void addSize(SizeSelector selector) {
      this.usedMatchingTask = true;
      super.addSize(selector);
   }

   public void addFilename(FilenameSelector selector) {
      this.usedMatchingTask = true;
      super.addFilename(selector);
   }

   public void addCustom(ExtendSelector selector) {
      this.usedMatchingTask = true;
      super.addCustom(selector);
   }

   public void addContains(ContainsSelector selector) {
      this.usedMatchingTask = true;
      super.addContains(selector);
   }

   public void addPresent(PresentSelector selector) {
      this.usedMatchingTask = true;
      super.addPresent(selector);
   }

   public void addDepth(DepthSelector selector) {
      this.usedMatchingTask = true;
      super.addDepth(selector);
   }

   public void addDepend(DependSelector selector) {
      this.usedMatchingTask = true;
      super.addDepend(selector);
   }

   public void addContainsRegexp(ContainsRegexpSelector selector) {
      this.usedMatchingTask = true;
      super.addContainsRegexp(selector);
   }

   public void addModified(ModifiedSelector selector) {
      this.usedMatchingTask = true;
      super.addModified(selector);
   }

   public void add(FileSelector selector) {
      this.usedMatchingTask = true;
      super.add(selector);
   }

   public void execute() throws BuildException {
      if (this.usedMatchingTask) {
         this.log("DEPRECATED - Use of the implicit FileSet is deprecated.  Use a nested fileset element instead.", this.quiet ? 3 : this.verbosity);
      }

      if (this.file == null && this.dir == null && this.filesets.size() == 0 && this.rcs == null) {
         throw new BuildException("At least one of the file or dir attributes, or a nested resource collection, must be set.");
      } else if (this.quiet && this.failonerror) {
         throw new BuildException("quiet and failonerror cannot both be set to true", this.getLocation());
      } else {
         if (this.file != null) {
            if (this.file.exists()) {
               if (this.file.isDirectory()) {
                  this.log("Directory " + this.file.getAbsolutePath() + " cannot be removed using the file attribute.  " + "Use dir instead.", this.quiet ? 3 : this.verbosity);
               } else {
                  this.log("Deleting: " + this.file.getAbsolutePath());
                  if (!this.delete(this.file)) {
                     this.handle("Unable to delete file " + this.file.getAbsolutePath());
                  }
               }
            } else {
               this.log("Could not find file " + this.file.getAbsolutePath() + " to delete.", this.quiet ? 3 : this.verbosity);
            }
         }

         if (this.dir != null && this.dir.exists() && this.dir.isDirectory() && !this.usedMatchingTask) {
            if (this.verbosity == 3) {
               this.log("Deleting directory " + this.dir.getAbsolutePath());
            }

            this.removeDir(this.dir);
         }

         Resources resourcesToDelete = new Resources();
         resourcesToDelete.setProject(this.getProject());
         Resources filesetDirs = new Resources();
         filesetDirs.setProject(this.getProject());
         FileSet implicit = null;
         if (this.usedMatchingTask && this.dir != null && this.dir.isDirectory()) {
            implicit = this.getImplicitFileSet();
            implicit.setProject(this.getProject());
            this.filesets.add(implicit);
         }

         int i = 0;

         for(int size = this.filesets.size(); i < size; ++i) {
            FileSet fs = (FileSet)this.filesets.get(i);
            if (fs.getProject() == null) {
               this.log("Deleting fileset with no project specified; assuming executing project", 3);
               fs = (FileSet)fs.clone();
               fs.setProject(this.getProject());
            }

            if (!fs.getDir().isDirectory()) {
               this.handle("Directory does not exist:" + fs.getDir());
            } else {
               resourcesToDelete.add(fs);
               if (this.includeEmpty) {
                  filesetDirs.add(new Delete.ReverseDirs(fs.getDir(), fs.getDirectoryScanner().getIncludedDirectories()));
               }
            }
         }

         resourcesToDelete.add(filesetDirs);
         if (this.rcs != null) {
            Restrict exists = new Restrict();
            exists.add(EXISTS);
            exists.add((ResourceCollection)this.rcs);
            Sort s = new Sort();
            s.add(REVERSE_FILESYSTEM);
            s.add(exists);
            resourcesToDelete.add(s);
         }

         try {
            if (resourcesToDelete.isFilesystemOnly()) {
               Iterator iter = resourcesToDelete.iterator();

               while(true) {
                  FileResource r;
                  do {
                     do {
                        if (!iter.hasNext()) {
                           return;
                        }

                        r = (FileResource)iter.next();
                     } while(!r.isExists());
                  } while(r.isDirectory() && r.getFile().list().length != 0);

                  this.log("Deleting " + r, this.verbosity);
                  if (!this.delete(r.getFile()) && this.failonerror) {
                     this.handle("Unable to delete " + (r.isDirectory() ? "directory " : "file ") + r);
                  }
               }
            } else {
               this.handle(this.getTaskName() + " handles only filesystem resources");
            }
         } catch (Exception var10) {
            this.handle(var10);
         } finally {
            if (implicit != null) {
               this.filesets.remove(implicit);
            }

         }

      }
   }

   private void handle(String msg) {
      this.handle((Exception)(new BuildException(msg)));
   }

   private void handle(Exception e) {
      if (this.failonerror) {
         throw e instanceof BuildException ? (BuildException)e : new BuildException(e);
      } else {
         this.log(e, this.quiet ? 3 : this.verbosity);
      }
   }

   private boolean delete(File f) {
      if (!f.delete()) {
         if (Os.isFamily("windows")) {
            System.gc();
         }

         try {
            Thread.sleep(10L);
         } catch (InterruptedException var3) {
         }

         if (!f.delete()) {
            if (this.deleteOnExit) {
               int level = this.quiet ? 3 : 2;
               this.log("Failed to delete " + f + ", calling deleteOnExit." + " This attempts to delete the file when the Ant jvm" + " has exited and might not succeed.", level);
               f.deleteOnExit();
               return true;
            }

            return false;
         }
      }

      return true;
   }

   protected void removeDir(File d) {
      String[] list = d.list();
      if (list == null) {
         list = new String[0];
      }

      for(int i = 0; i < list.length; ++i) {
         String s = list[i];
         File f = new File(d, s);
         if (f.isDirectory()) {
            this.removeDir(f);
         } else {
            this.log("Deleting " + f.getAbsolutePath(), this.quiet ? 3 : this.verbosity);
            if (!this.delete(f)) {
               this.handle("Unable to delete file " + f.getAbsolutePath());
            }
         }
      }

      this.log("Deleting directory " + d.getAbsolutePath(), this.verbosity);
      if (!this.delete(d)) {
         this.handle("Unable to delete directory " + this.dir.getAbsolutePath());
      }

   }

   protected void removeFiles(File d, String[] files, String[] dirs) {
      int dirCount;
      if (files.length > 0) {
         this.log("Deleting " + files.length + " files from " + d.getAbsolutePath(), this.quiet ? 3 : this.verbosity);

         for(dirCount = 0; dirCount < files.length; ++dirCount) {
            File f = new File(d, files[dirCount]);
            this.log("Deleting " + f.getAbsolutePath(), this.quiet ? 3 : this.verbosity);
            if (!this.delete(f)) {
               this.handle("Unable to delete file " + f.getAbsolutePath());
            }
         }
      }

      if (dirs.length > 0 && this.includeEmpty) {
         dirCount = 0;

         for(int j = dirs.length - 1; j >= 0; --j) {
            File currDir = new File(d, dirs[j]);
            String[] dirFiles = currDir.list();
            if (dirFiles == null || dirFiles.length == 0) {
               this.log("Deleting " + currDir.getAbsolutePath(), this.quiet ? 3 : this.verbosity);
               if (!this.delete(currDir)) {
                  this.handle("Unable to delete directory " + currDir.getAbsolutePath());
               } else {
                  ++dirCount;
               }
            }
         }

         if (dirCount > 0) {
            this.log("Deleted " + dirCount + " director" + (dirCount == 1 ? "y" : "ies") + " form " + d.getAbsolutePath(), this.quiet ? 3 : this.verbosity);
         }
      }

   }

   private static class ReverseDirs implements ResourceCollection {
      static final Comparator REVERSE = new Comparator() {
         public int compare(Object foo, Object bar) {
            return ((Comparable)foo).compareTo(bar) * -1;
         }
      };
      private File basedir;
      private String[] dirs;

      ReverseDirs(File basedir, String[] dirs) {
         this.basedir = basedir;
         this.dirs = dirs;
         Arrays.sort(this.dirs, REVERSE);
      }

      public Iterator iterator() {
         return new FileResourceIterator(this.basedir, this.dirs);
      }

      public boolean isFilesystemOnly() {
         return true;
      }

      public int size() {
         return this.dirs.length;
      }
   }
}
