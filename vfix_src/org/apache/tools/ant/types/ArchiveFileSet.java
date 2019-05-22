package org.apache.tools.ant.types;

import java.io.File;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;

public abstract class ArchiveFileSet extends FileSet {
   private static final int BASE_OCTAL = 8;
   public static final int DEFAULT_DIR_MODE = 16877;
   public static final int DEFAULT_FILE_MODE = 33188;
   private Resource src = null;
   private String prefix = "";
   private String fullpath = "";
   private boolean hasDir = false;
   private int fileMode = 33188;
   private int dirMode = 16877;
   private boolean fileModeHasBeenSet = false;
   private boolean dirModeHasBeenSet = false;

   public ArchiveFileSet() {
   }

   protected ArchiveFileSet(FileSet fileset) {
      super(fileset);
   }

   protected ArchiveFileSet(ArchiveFileSet fileset) {
      super(fileset);
      this.src = fileset.src;
      this.prefix = fileset.prefix;
      this.fullpath = fileset.fullpath;
      this.hasDir = fileset.hasDir;
      this.fileMode = fileset.fileMode;
      this.dirMode = fileset.dirMode;
      this.fileModeHasBeenSet = fileset.fileModeHasBeenSet;
      this.dirModeHasBeenSet = fileset.dirModeHasBeenSet;
   }

   public void setDir(File dir) throws BuildException {
      this.checkAttributesAllowed();
      if (this.src != null) {
         throw new BuildException("Cannot set both dir and src attributes");
      } else {
         super.setDir(dir);
         this.hasDir = true;
      }
   }

   public void addConfigured(ResourceCollection a) {
      this.checkChildrenAllowed();
      if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported as archives");
      } else {
         this.setSrcResource((Resource)a.iterator().next());
      }
   }

   public void setSrc(File srcFile) {
      this.setSrcResource(new FileResource(srcFile));
   }

   public void setSrcResource(Resource src) {
      this.checkArchiveAttributesAllowed();
      if (this.hasDir) {
         throw new BuildException("Cannot set both dir and src attributes");
      } else {
         this.src = src;
      }
   }

   public File getSrc(Project p) {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(p)).getSrc(p) : this.getSrc();
   }

   public File getSrc() {
      return this.src instanceof FileResource ? ((FileResource)this.src).getFile() : null;
   }

   public void setPrefix(String prefix) {
      this.checkArchiveAttributesAllowed();
      if (!prefix.equals("") && !this.fullpath.equals("")) {
         throw new BuildException("Cannot set both fullpath and prefix attributes");
      } else {
         this.prefix = prefix;
      }
   }

   public String getPrefix(Project p) {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(p)).getPrefix(p) : this.prefix;
   }

   public void setFullpath(String fullpath) {
      this.checkArchiveAttributesAllowed();
      if (!this.prefix.equals("") && !fullpath.equals("")) {
         throw new BuildException("Cannot set both fullpath and prefix attributes");
      } else {
         this.fullpath = fullpath;
      }
   }

   public String getFullpath(Project p) {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(p)).getFullpath(p) : this.fullpath;
   }

   protected abstract ArchiveScanner newArchiveScanner();

   public DirectoryScanner getDirectoryScanner(Project p) {
      if (this.isReference()) {
         return this.getRef(p).getDirectoryScanner(p);
      } else if (this.src == null) {
         return super.getDirectoryScanner(p);
      } else if (!this.src.isExists()) {
         throw new BuildException("the archive doesn't exist");
      } else if (this.src.isDirectory()) {
         throw new BuildException("the archive can't be a directory");
      } else {
         ArchiveScanner as = this.newArchiveScanner();
         as.setSrc(this.src);
         super.setDir(p.getBaseDir());
         this.setupDirectoryScanner(as, p);
         as.init();
         return as;
      }
   }

   public Iterator iterator() {
      if (this.isReference()) {
         return ((ResourceCollection)((ResourceCollection)this.getRef(this.getProject()))).iterator();
      } else if (this.src == null) {
         return super.iterator();
      } else {
         ArchiveScanner as = (ArchiveScanner)this.getDirectoryScanner(this.getProject());
         return as.getResourceFiles();
      }
   }

   public int size() {
      if (this.isReference()) {
         return ((ResourceCollection)((ResourceCollection)this.getRef(this.getProject()))).size();
      } else if (this.src == null) {
         return super.size();
      } else {
         ArchiveScanner as = (ArchiveScanner)this.getDirectoryScanner(this.getProject());
         return as.getIncludedFilesCount();
      }
   }

   public boolean isFilesystemOnly() {
      return this.src == null;
   }

   public void setFileMode(String octalString) {
      this.checkArchiveAttributesAllowed();
      this.integerSetFileMode(Integer.parseInt(octalString, 8));
   }

   public void integerSetFileMode(int mode) {
      this.fileModeHasBeenSet = true;
      this.fileMode = '耀' | mode;
   }

   public int getFileMode(Project p) {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(p)).getFileMode(p) : this.fileMode;
   }

   public boolean hasFileModeBeenSet() {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(this.getProject())).hasFileModeBeenSet() : this.fileModeHasBeenSet;
   }

   public void setDirMode(String octalString) {
      this.checkArchiveAttributesAllowed();
      this.integerSetDirMode(Integer.parseInt(octalString, 8));
   }

   public void integerSetDirMode(int mode) {
      this.dirModeHasBeenSet = true;
      this.dirMode = 16384 | mode;
   }

   public int getDirMode(Project p) {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(p)).getDirMode(p) : this.dirMode;
   }

   public boolean hasDirModeBeenSet() {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(this.getProject())).hasDirModeBeenSet() : this.dirModeHasBeenSet;
   }

   protected void configureFileSet(ArchiveFileSet zfs) {
      zfs.setPrefix(this.prefix);
      zfs.setFullpath(this.fullpath);
      zfs.fileModeHasBeenSet = this.fileModeHasBeenSet;
      zfs.fileMode = this.fileMode;
      zfs.dirModeHasBeenSet = this.dirModeHasBeenSet;
      zfs.dirMode = this.dirMode;
   }

   public Object clone() {
      return this.isReference() ? ((ArchiveFileSet)this.getRef(this.getProject())).clone() : super.clone();
   }

   public String toString() {
      if (this.hasDir && this.getProject() != null) {
         return super.toString();
      } else {
         return this.src != null ? this.src.getName() : null;
      }
   }

   /** @deprecated */
   public String getPrefix() {
      return this.prefix;
   }

   /** @deprecated */
   public String getFullpath() {
      return this.fullpath;
   }

   /** @deprecated */
   public int getFileMode() {
      return this.fileMode;
   }

   /** @deprecated */
   public int getDirMode() {
      return this.dirMode;
   }

   private void checkArchiveAttributesAllowed() {
      if (this.getProject() == null || this.isReference() && this.getRefid().getReferencedObject(this.getProject()) instanceof ArchiveFileSet) {
         this.checkAttributesAllowed();
      }

   }
}
