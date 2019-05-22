package org.apache.tools.ant.types;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.DifferentSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SelectorContainer;
import org.apache.tools.ant.types.selectors.SelectorScanner;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.TypeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

public abstract class AbstractFileSet extends DataType implements Cloneable, SelectorContainer {
   private PatternSet defaultPatterns = new PatternSet();
   private Vector additionalPatterns = new Vector();
   private Vector selectors = new Vector();
   private File dir;
   private boolean useDefaultExcludes = true;
   private boolean caseSensitive = true;
   private boolean followSymlinks = true;
   private DirectoryScanner directoryScanner = null;

   public AbstractFileSet() {
   }

   protected AbstractFileSet(AbstractFileSet fileset) {
      this.dir = fileset.dir;
      this.defaultPatterns = fileset.defaultPatterns;
      this.additionalPatterns = fileset.additionalPatterns;
      this.selectors = fileset.selectors;
      this.useDefaultExcludes = fileset.useDefaultExcludes;
      this.caseSensitive = fileset.caseSensitive;
      this.followSymlinks = fileset.followSymlinks;
      this.setProject(fileset.getProject());
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.dir == null && !this.defaultPatterns.hasPatterns(this.getProject())) {
         if (!this.additionalPatterns.isEmpty()) {
            throw this.noChildrenAllowed();
         } else if (!this.selectors.isEmpty()) {
            throw this.noChildrenAllowed();
         } else {
            super.setRefid(r);
         }
      } else {
         throw this.tooManyAttributes();
      }
   }

   public synchronized void setDir(File dir) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.dir = dir;
         this.directoryScanner = null;
      }
   }

   public File getDir() {
      return this.getDir(this.getProject());
   }

   public synchronized File getDir(Project p) {
      return this.isReference() ? this.getRef(p).getDir(p) : this.dir;
   }

   public synchronized PatternSet createPatternSet() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         PatternSet patterns = new PatternSet();
         this.additionalPatterns.addElement(patterns);
         this.directoryScanner = null;
         return patterns;
      }
   }

   public synchronized PatternSet.NameEntry createInclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.directoryScanner = null;
         return this.defaultPatterns.createInclude();
      }
   }

   public synchronized PatternSet.NameEntry createIncludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.directoryScanner = null;
         return this.defaultPatterns.createIncludesFile();
      }
   }

   public synchronized PatternSet.NameEntry createExclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.directoryScanner = null;
         return this.defaultPatterns.createExclude();
      }
   }

   public synchronized PatternSet.NameEntry createExcludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.directoryScanner = null;
         return this.defaultPatterns.createExcludesFile();
      }
   }

   public synchronized void setFile(File file) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.setDir(file.getParentFile());
         this.createInclude().setName(file.getName());
      }
   }

   public synchronized void setIncludes(String includes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.defaultPatterns.setIncludes(includes);
         this.directoryScanner = null;
      }
   }

   public synchronized void appendIncludes(String[] includes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (includes != null) {
            for(int i = 0; i < includes.length; ++i) {
               this.defaultPatterns.createInclude().setName(includes[i]);
            }

            this.directoryScanner = null;
         }

      }
   }

   public synchronized void setExcludes(String excludes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.defaultPatterns.setExcludes(excludes);
         this.directoryScanner = null;
      }
   }

   public synchronized void appendExcludes(String[] excludes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (excludes != null) {
            for(int i = 0; i < excludes.length; ++i) {
               this.defaultPatterns.createExclude().setName(excludes[i]);
            }

            this.directoryScanner = null;
         }

      }
   }

   public synchronized void setIncludesfile(File incl) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.defaultPatterns.setIncludesfile(incl);
         this.directoryScanner = null;
      }
   }

   public synchronized void setExcludesfile(File excl) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.defaultPatterns.setExcludesfile(excl);
         this.directoryScanner = null;
      }
   }

   public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.useDefaultExcludes = useDefaultExcludes;
         this.directoryScanner = null;
      }
   }

   public synchronized boolean getDefaultexcludes() {
      return this.isReference() ? this.getRef(this.getProject()).getDefaultexcludes() : this.useDefaultExcludes;
   }

   public synchronized void setCaseSensitive(boolean caseSensitive) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.caseSensitive = caseSensitive;
         this.directoryScanner = null;
      }
   }

   public synchronized boolean isCaseSensitive() {
      return this.isReference() ? this.getRef(this.getProject()).isCaseSensitive() : this.caseSensitive;
   }

   public synchronized void setFollowSymlinks(boolean followSymlinks) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.followSymlinks = followSymlinks;
         this.directoryScanner = null;
      }
   }

   public synchronized boolean isFollowSymlinks() {
      return this.isReference() ? this.getRef(this.getProject()).isFollowSymlinks() : this.followSymlinks;
   }

   public DirectoryScanner getDirectoryScanner() {
      return this.getDirectoryScanner(this.getProject());
   }

   public DirectoryScanner getDirectoryScanner(Project p) {
      if (this.isReference()) {
         return this.getRef(p).getDirectoryScanner(p);
      } else {
         DirectoryScanner ds = null;
         synchronized(this) {
            if (this.directoryScanner != null && p == this.getProject()) {
               ds = this.directoryScanner;
            } else {
               if (this.dir == null) {
                  throw new BuildException("No directory specified for " + this.getDataTypeName() + ".");
               }

               if (!this.dir.exists()) {
                  throw new BuildException(this.dir.getAbsolutePath() + " not found.");
               }

               if (!this.dir.isDirectory()) {
                  throw new BuildException(this.dir.getAbsolutePath() + " is not a directory.");
               }

               ds = new DirectoryScanner();
               this.setupDirectoryScanner(ds, p);
               ds.setFollowSymlinks(this.followSymlinks);
               this.directoryScanner = p == this.getProject() ? ds : this.directoryScanner;
            }
         }

         ds.scan();
         return ds;
      }
   }

   public void setupDirectoryScanner(FileScanner ds) {
      this.setupDirectoryScanner(ds, this.getProject());
   }

   public synchronized void setupDirectoryScanner(FileScanner ds, Project p) {
      if (this.isReference()) {
         this.getRef(p).setupDirectoryScanner(ds, p);
      } else if (ds == null) {
         throw new IllegalArgumentException("ds cannot be null");
      } else {
         ds.setBasedir(this.dir);
         PatternSet ps = this.mergePatterns(p);
         p.log(this.getDataTypeName() + ": Setup scanner in dir " + this.dir + " with " + ps, 4);
         ds.setIncludes(ps.getIncludePatterns(p));
         ds.setExcludes(ps.getExcludePatterns(p));
         if (ds instanceof SelectorScanner) {
            SelectorScanner ss = (SelectorScanner)ds;
            ss.setSelectors(this.getSelectors(p));
         }

         if (this.useDefaultExcludes) {
            ds.addDefaultExcludes();
         }

         ds.setCaseSensitive(this.caseSensitive);
      }
   }

   protected AbstractFileSet getRef(Project p) {
      return (AbstractFileSet)this.getCheckedRef(p);
   }

   public synchronized boolean hasSelectors() {
      return this.isReference() && this.getProject() != null ? this.getRef(this.getProject()).hasSelectors() : !this.selectors.isEmpty();
   }

   public synchronized boolean hasPatterns() {
      if (this.isReference() && this.getProject() != null) {
         return this.getRef(this.getProject()).hasPatterns();
      } else if (this.defaultPatterns.hasPatterns(this.getProject())) {
         return true;
      } else {
         Enumeration e = this.additionalPatterns.elements();

         PatternSet ps;
         do {
            if (!e.hasMoreElements()) {
               return false;
            }

            ps = (PatternSet)e.nextElement();
         } while(!ps.hasPatterns(this.getProject()));

         return true;
      }
   }

   public synchronized int selectorCount() {
      return this.isReference() && this.getProject() != null ? this.getRef(this.getProject()).selectorCount() : this.selectors.size();
   }

   public synchronized FileSelector[] getSelectors(Project p) {
      return this.isReference() ? this.getRef(p).getSelectors(p) : (FileSelector[])((FileSelector[])this.selectors.toArray(new FileSelector[this.selectors.size()]));
   }

   public synchronized Enumeration selectorElements() {
      return this.isReference() && this.getProject() != null ? this.getRef(this.getProject()).selectorElements() : this.selectors.elements();
   }

   public synchronized void appendSelector(FileSelector selector) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.selectors.addElement(selector);
         this.directoryScanner = null;
      }
   }

   public void addSelector(SelectSelector selector) {
      this.appendSelector(selector);
   }

   public void addAnd(AndSelector selector) {
      this.appendSelector(selector);
   }

   public void addOr(OrSelector selector) {
      this.appendSelector(selector);
   }

   public void addNot(NotSelector selector) {
      this.appendSelector(selector);
   }

   public void addNone(NoneSelector selector) {
      this.appendSelector(selector);
   }

   public void addMajority(MajoritySelector selector) {
      this.appendSelector(selector);
   }

   public void addDate(DateSelector selector) {
      this.appendSelector(selector);
   }

   public void addSize(SizeSelector selector) {
      this.appendSelector(selector);
   }

   public void addDifferent(DifferentSelector selector) {
      this.appendSelector(selector);
   }

   public void addFilename(FilenameSelector selector) {
      this.appendSelector(selector);
   }

   public void addType(TypeSelector selector) {
      this.appendSelector(selector);
   }

   public void addCustom(ExtendSelector selector) {
      this.appendSelector(selector);
   }

   public void addContains(ContainsSelector selector) {
      this.appendSelector(selector);
   }

   public void addPresent(PresentSelector selector) {
      this.appendSelector(selector);
   }

   public void addDepth(DepthSelector selector) {
      this.appendSelector(selector);
   }

   public void addDepend(DependSelector selector) {
      this.appendSelector(selector);
   }

   public void addContainsRegexp(ContainsRegexpSelector selector) {
      this.appendSelector(selector);
   }

   public void addModified(ModifiedSelector selector) {
      this.appendSelector(selector);
   }

   public void add(FileSelector selector) {
      this.appendSelector(selector);
   }

   public String toString() {
      DirectoryScanner ds = this.getDirectoryScanner(this.getProject());
      String[] files = ds.getIncludedFiles();
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < files.length; ++i) {
         if (i > 0) {
            sb.append(';');
         }

         sb.append(files[i]);
      }

      return sb.toString();
   }

   public synchronized Object clone() {
      if (this.isReference()) {
         return this.getRef(this.getProject()).clone();
      } else {
         try {
            AbstractFileSet fs = (AbstractFileSet)super.clone();
            fs.defaultPatterns = (PatternSet)this.defaultPatterns.clone();
            fs.additionalPatterns = new Vector(this.additionalPatterns.size());
            Enumeration e = this.additionalPatterns.elements();

            while(e.hasMoreElements()) {
               fs.additionalPatterns.addElement(((PatternSet)e.nextElement()).clone());
            }

            fs.selectors = new Vector(this.selectors);
            return fs;
         } catch (CloneNotSupportedException var3) {
            throw new BuildException(var3);
         }
      }
   }

   public String[] mergeIncludes(Project p) {
      return this.mergePatterns(p).getIncludePatterns(p);
   }

   public String[] mergeExcludes(Project p) {
      return this.mergePatterns(p).getExcludePatterns(p);
   }

   public synchronized PatternSet mergePatterns(Project p) {
      if (this.isReference()) {
         return this.getRef(p).mergePatterns(p);
      } else {
         PatternSet ps = (PatternSet)this.defaultPatterns.clone();
         int count = this.additionalPatterns.size();

         for(int i = 0; i < count; ++i) {
            Object o = this.additionalPatterns.elementAt(i);
            ps.append((PatternSet)o, p);
         }

         return ps;
      }
   }
}
