package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class ZipFileSet extends ArchiveFileSet {
   private String encoding = null;

   public ZipFileSet() {
   }

   protected ZipFileSet(FileSet fileset) {
      super(fileset);
   }

   protected ZipFileSet(ZipFileSet fileset) {
      super((ArchiveFileSet)fileset);
      this.encoding = fileset.encoding;
   }

   public void setEncoding(String enc) {
      this.checkZipFileSetAttributesAllowed();
      this.encoding = enc;
   }

   public String getEncoding() {
      if (this.isReference()) {
         AbstractFileSet ref = this.getRef(this.getProject());
         return ref instanceof ZipFileSet ? ((ZipFileSet)ref).getEncoding() : null;
      } else {
         return this.encoding;
      }
   }

   protected ArchiveScanner newArchiveScanner() {
      ZipScanner zs = new ZipScanner();
      zs.setEncoding(this.encoding);
      return zs;
   }

   protected AbstractFileSet getRef(Project p) {
      this.dieOnCircularReference(p);
      Object o = this.getRefid().getReferencedObject(p);
      if (o instanceof ZipFileSet) {
         return (AbstractFileSet)o;
      } else if (o instanceof FileSet) {
         ZipFileSet zfs = new ZipFileSet((FileSet)o);
         this.configureFileSet(zfs);
         return zfs;
      } else {
         String msg = this.getRefid().getRefId() + " doesn't denote a zipfileset or a fileset";
         throw new BuildException(msg);
      }
   }

   public Object clone() {
      return this.isReference() ? ((ZipFileSet)this.getRef(this.getProject())).clone() : super.clone();
   }

   private void checkZipFileSetAttributesAllowed() {
      if (this.getProject() == null || this.isReference() && this.getRefid().getReferencedObject(this.getProject()) instanceof ZipFileSet) {
         this.checkAttributesAllowed();
      }

   }
}
