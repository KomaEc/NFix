package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Sort;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.comparators.Date;
import org.apache.tools.ant.types.resources.comparators.ResourceComparator;
import org.apache.tools.ant.types.resources.comparators.Reverse;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;

public class DependSet extends MatchingTask {
   private static final ResourceSelector NOT_EXISTS = new Not(new Exists());
   private static final ResourceComparator DATE_ASC = new Date();
   private static final ResourceComparator DATE_DESC;
   private Union sources = null;
   private Path targets = null;

   public synchronized Union createSources() {
      this.sources = this.sources == null ? new Union() : this.sources;
      return this.sources;
   }

   public void addSrcfileset(FileSet fs) {
      this.createSources().add(fs);
   }

   public void addSrcfilelist(FileList fl) {
      this.createSources().add(fl);
   }

   public synchronized Path createTargets() {
      this.targets = this.targets == null ? new Path(this.getProject()) : this.targets;
      return this.targets;
   }

   public void addTargetfileset(FileSet fs) {
      this.createTargets().add((ResourceCollection)(new DependSet.HideMissingBasedir(fs)));
   }

   public void addTargetfilelist(FileList fl) {
      this.createTargets().add((ResourceCollection)fl);
   }

   public void execute() throws BuildException {
      if (this.sources == null) {
         throw new BuildException("At least one set of source resources must be specified");
      } else if (this.targets == null) {
         throw new BuildException("At least one set of target files must be specified");
      } else {
         if (this.sources.size() > 0 && this.targets.size() > 0 && !this.uptodate(this.sources, this.targets)) {
            this.log("Deleting all target files.", 3);
            Delete delete = new Delete();
            delete.bindToOwner(this);
            delete.add((ResourceCollection)this.targets);
            delete.perform();
         }

      }
   }

   private boolean uptodate(ResourceCollection src, ResourceCollection target) {
      org.apache.tools.ant.types.resources.selectors.Date datesel = new org.apache.tools.ant.types.resources.selectors.Date();
      datesel.setMillis(System.currentTimeMillis());
      datesel.setWhen(TimeComparison.AFTER);
      this.logFuture(this.targets, datesel);
      int neTargets = (new DependSet.NonExistent(this.targets)).size();
      if (neTargets > 0) {
         this.log(neTargets + " nonexistent targets", 3);
         return false;
      } else {
         FileResource oldestTarget = (FileResource)((FileResource)(new DependSet.Oldest(this.targets)).iterator().next());
         this.log(oldestTarget + " is oldest target file", 3);
         this.logFuture(this.sources, datesel);
         int neSources = (new DependSet.NonExistent(this.sources)).size();
         if (neSources > 0) {
            this.log(neSources + " nonexistent sources", 3);
            return false;
         } else {
            Resource newestSource = (Resource)((Resource)(new DependSet.Newest(this.sources)).iterator().next());
            this.log(newestSource.toLongString() + " is newest source", 3);
            return oldestTarget.getLastModified() >= newestSource.getLastModified();
         }
      }
   }

   private void logFuture(ResourceCollection rc, ResourceSelector rsel) {
      Restrict r = new Restrict();
      r.add(rsel);
      r.add(rc);
      Iterator i = r.iterator();

      while(i.hasNext()) {
         this.log("Warning: " + i.next() + " modified in the future.", 1);
      }

   }

   static {
      DATE_DESC = new Reverse(DATE_ASC);
   }

   private static class HideMissingBasedir implements ResourceCollection {
      private FileSet fs;

      private HideMissingBasedir(FileSet fs) {
         this.fs = fs;
      }

      public Iterator iterator() {
         return this.basedirExists() ? this.fs.iterator() : Resources.EMPTY_ITERATOR;
      }

      public int size() {
         return this.basedirExists() ? this.fs.size() : 0;
      }

      public boolean isFilesystemOnly() {
         return true;
      }

      private boolean basedirExists() {
         File basedir = this.fs.getDir();
         return basedir == null || basedir.exists();
      }

      // $FF: synthetic method
      HideMissingBasedir(FileSet x0, Object x1) {
         this(x0);
      }
   }

   private static class Newest extends DependSet.Xest {
      private Newest(ResourceCollection rc) {
         super(rc, DependSet.DATE_DESC, null);
      }

      // $FF: synthetic method
      Newest(ResourceCollection x0, Object x1) {
         this(x0);
      }
   }

   private static class Oldest extends DependSet.Xest {
      private Oldest(ResourceCollection rc) {
         super(rc, DependSet.DATE_ASC, null);
      }

      // $FF: synthetic method
      Oldest(ResourceCollection x0, Object x1) {
         this(x0);
      }
   }

   private static class Xest extends Sort {
      private Xest(ResourceCollection rc, ResourceComparator c) {
         super.add(c);
         super.add(rc);
      }

      // $FF: synthetic method
      Xest(ResourceCollection x0, ResourceComparator x1, Object x2) {
         this(x0, x1);
      }
   }

   private static class NonExistent extends Restrict {
      private NonExistent(ResourceCollection rc) {
         super.add(rc);
         super.add(DependSet.NOT_EXISTS);
      }

      // $FF: synthetic method
      NonExistent(ResourceCollection x0, Object x1) {
         this(x0);
      }
   }
}
