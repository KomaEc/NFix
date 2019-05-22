package org.apache.tools.ant.types.resources;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.selectors.AbstractSelectorContainer;
import org.apache.tools.ant.types.selectors.FileSelector;

public class Files extends AbstractSelectorContainer implements Cloneable, ResourceCollection {
   private static final Iterator EMPTY_ITERATOR;
   private PatternSet defaultPatterns = new PatternSet();
   private Vector additionalPatterns = new Vector();
   private Vector selectors = new Vector();
   private boolean useDefaultExcludes = true;
   private boolean caseSensitive = true;
   private boolean followSymlinks = true;
   private DirectoryScanner ds = null;

   public Files() {
   }

   protected Files(Files f) {
      this.defaultPatterns = f.defaultPatterns;
      this.additionalPatterns = f.additionalPatterns;
      this.selectors = f.selectors;
      this.useDefaultExcludes = f.useDefaultExcludes;
      this.caseSensitive = f.caseSensitive;
      this.followSymlinks = f.followSymlinks;
      this.ds = f.ds;
      this.setProject(f.getProject());
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.hasPatterns(this.defaultPatterns)) {
         throw this.tooManyAttributes();
      } else if (!this.additionalPatterns.isEmpty()) {
         throw this.noChildrenAllowed();
      } else if (!this.selectors.isEmpty()) {
         throw this.noChildrenAllowed();
      } else {
         super.setRefid(r);
      }
   }

   public synchronized PatternSet createPatternSet() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         PatternSet patterns = new PatternSet();
         this.additionalPatterns.addElement(patterns);
         this.ds = null;
         return patterns;
      }
   }

   public synchronized PatternSet.NameEntry createInclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.ds = null;
         return this.defaultPatterns.createInclude();
      }
   }

   public synchronized PatternSet.NameEntry createIncludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.ds = null;
         return this.defaultPatterns.createIncludesFile();
      }
   }

   public synchronized PatternSet.NameEntry createExclude() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.ds = null;
         return this.defaultPatterns.createExclude();
      }
   }

   public synchronized PatternSet.NameEntry createExcludesFile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.ds = null;
         return this.defaultPatterns.createExcludesFile();
      }
   }

   public synchronized void setIncludes(String includes) {
      this.checkAttributesAllowed();
      this.defaultPatterns.setIncludes(includes);
      this.ds = null;
   }

   public synchronized void appendIncludes(String[] includes) {
      this.checkAttributesAllowed();
      if (includes != null) {
         for(int i = 0; i < includes.length; ++i) {
            this.defaultPatterns.createInclude().setName(includes[i]);
         }

         this.ds = null;
      }

   }

   public synchronized void setExcludes(String excludes) {
      this.checkAttributesAllowed();
      this.defaultPatterns.setExcludes(excludes);
      this.ds = null;
   }

   public synchronized void appendExcludes(String[] excludes) {
      this.checkAttributesAllowed();
      if (excludes != null) {
         for(int i = 0; i < excludes.length; ++i) {
            this.defaultPatterns.createExclude().setName(excludes[i]);
         }

         this.ds = null;
      }

   }

   public synchronized void setIncludesfile(File incl) throws BuildException {
      this.checkAttributesAllowed();
      this.defaultPatterns.setIncludesfile(incl);
      this.ds = null;
   }

   public synchronized void setExcludesfile(File excl) throws BuildException {
      this.checkAttributesAllowed();
      this.defaultPatterns.setExcludesfile(excl);
      this.ds = null;
   }

   public synchronized void setDefaultexcludes(boolean useDefaultExcludes) {
      this.checkAttributesAllowed();
      this.useDefaultExcludes = useDefaultExcludes;
      this.ds = null;
   }

   public synchronized boolean getDefaultexcludes() {
      return this.isReference() ? this.getRef().getDefaultexcludes() : this.useDefaultExcludes;
   }

   public synchronized void setCaseSensitive(boolean caseSensitive) {
      this.checkAttributesAllowed();
      this.caseSensitive = caseSensitive;
      this.ds = null;
   }

   public synchronized boolean isCaseSensitive() {
      return this.isReference() ? this.getRef().isCaseSensitive() : this.caseSensitive;
   }

   public synchronized void setFollowSymlinks(boolean followSymlinks) {
      this.checkAttributesAllowed();
      this.followSymlinks = followSymlinks;
      this.ds = null;
   }

   public synchronized boolean isFollowSymlinks() {
      return this.isReference() ? this.getRef().isFollowSymlinks() : this.followSymlinks;
   }

   public synchronized Iterator iterator() {
      if (this.isReference()) {
         return this.getRef().iterator();
      } else {
         this.ensureDirectoryScannerSetup();
         this.ds.scan();
         int fct = this.ds.getIncludedFilesCount();
         int dct = this.ds.getIncludedDirsCount();
         if (fct + dct == 0) {
            return EMPTY_ITERATOR;
         } else {
            FileResourceIterator result = new FileResourceIterator();
            if (fct > 0) {
               result.addFiles(this.ds.getIncludedFiles());
            }

            if (dct > 0) {
               result.addFiles(this.ds.getIncludedDirectories());
            }

            return result;
         }
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return this.getRef().size();
      } else {
         this.ensureDirectoryScannerSetup();
         this.ds.scan();
         return this.ds.getIncludedFilesCount() + this.ds.getIncludedDirsCount();
      }
   }

   public synchronized boolean hasPatterns() {
      if (this.isReference()) {
         return this.getRef().hasPatterns();
      } else if (this.hasPatterns(this.defaultPatterns)) {
         return true;
      } else {
         Iterator i = this.additionalPatterns.iterator();

         do {
            if (!i.hasNext()) {
               return false;
            }
         } while(!this.hasPatterns((PatternSet)i.next()));

         return true;
      }
   }

   public synchronized void appendSelector(FileSelector selector) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         super.appendSelector(selector);
         this.ds = null;
      }
   }

   public String toString() {
      if (this.isReference()) {
         return this.getRef().toString();
      } else {
         Iterator i = this.iterator();
         if (!i.hasNext()) {
            return "";
         } else {
            StringBuffer sb;
            for(sb = new StringBuffer(); i.hasNext(); sb.append(i.next())) {
               if (sb.length() > 0) {
                  sb.append(File.pathSeparatorChar);
               }
            }

            return sb.toString();
         }
      }
   }

   public synchronized Object clone() {
      if (this.isReference()) {
         return this.getRef().clone();
      } else {
         try {
            Files f = (Files)super.clone();
            f.defaultPatterns = (PatternSet)this.defaultPatterns.clone();
            f.additionalPatterns = new Vector(this.additionalPatterns.size());
            Iterator iter = this.additionalPatterns.iterator();

            while(iter.hasNext()) {
               PatternSet ps = (PatternSet)iter.next();
               f.additionalPatterns.add(ps.clone());
            }

            f.selectors = new Vector(this.selectors);
            return f;
         } catch (CloneNotSupportedException var4) {
            throw new BuildException(var4);
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
         return this.getRef().mergePatterns(p);
      } else {
         PatternSet ps = new PatternSet();
         ps.append(this.defaultPatterns, p);
         int count = this.additionalPatterns.size();

         for(int i = 0; i < count; ++i) {
            Object o = this.additionalPatterns.elementAt(i);
            ps.append((PatternSet)o, p);
         }

         return ps;
      }
   }

   public boolean isFilesystemOnly() {
      return true;
   }

   protected Files getRef() {
      return (Files)this.getCheckedRef();
   }

   private synchronized void ensureDirectoryScannerSetup() {
      if (this.ds == null) {
         this.ds = new DirectoryScanner();
         PatternSet ps = this.mergePatterns(this.getProject());
         this.ds.setIncludes(ps.getIncludePatterns(this.getProject()));
         this.ds.setExcludes(ps.getExcludePatterns(this.getProject()));
         this.ds.setSelectors(this.getSelectors(this.getProject()));
         if (this.useDefaultExcludes) {
            this.ds.addDefaultExcludes();
         }

         this.ds.setCaseSensitive(this.caseSensitive);
         this.ds.setFollowSymlinks(this.followSymlinks);
      }

   }

   private boolean hasPatterns(PatternSet ps) {
      return ps.getIncludePatterns(this.getProject()).length > 0 || ps.getExcludePatterns(this.getProject()).length > 0;
   }

   static {
      EMPTY_ITERATOR = Collections.EMPTY_SET.iterator();
   }
}
