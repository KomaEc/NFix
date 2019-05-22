package org.apache.tools.ant.types.resources;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;

public abstract class ArchiveResource extends Resource {
   private static final int NULL_ARCHIVE = Resource.getMagicNumber("null archive".getBytes());
   private Resource archive;
   private boolean haveEntry;
   private boolean modeSet;
   private int mode;

   public ArchiveResource() {
      this.haveEntry = false;
      this.modeSet = false;
      this.mode = 0;
   }

   public ArchiveResource(File a) {
      this(a, false);
   }

   public ArchiveResource(File a, boolean withEntry) {
      this.haveEntry = false;
      this.modeSet = false;
      this.mode = 0;
      this.setArchive(a);
      this.haveEntry = withEntry;
   }

   public ArchiveResource(Resource a, boolean withEntry) {
      this.haveEntry = false;
      this.modeSet = false;
      this.mode = 0;
      this.addConfigured(a);
      this.haveEntry = withEntry;
   }

   public void setArchive(File a) {
      this.checkAttributesAllowed();
      this.archive = new FileResource(a);
   }

   public void setMode(int mode) {
      this.checkAttributesAllowed();
      this.mode = mode;
      this.modeSet = true;
   }

   public void addConfigured(ResourceCollection a) {
      this.checkChildrenAllowed();
      if (this.archive != null) {
         throw new BuildException("you must not specify more than one archive");
      } else if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported as archives");
      } else {
         this.archive = (Resource)a.iterator().next();
      }
   }

   public Resource getArchive() {
      return this.isReference() ? ((ArchiveResource)this.getCheckedRef()).getArchive() : this.archive;
   }

   public long getLastModified() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getLastModified();
      } else {
         this.checkEntry();
         return super.getLastModified();
      }
   }

   public long getSize() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getSize();
      } else {
         this.checkEntry();
         return super.getSize();
      }
   }

   public boolean isDirectory() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).isDirectory();
      } else {
         this.checkEntry();
         return super.isDirectory();
      }
   }

   public boolean isExists() {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).isExists();
      } else {
         this.checkEntry();
         return super.isExists();
      }
   }

   public int getMode() {
      if (this.isReference()) {
         return ((ArchiveResource)this.getCheckedRef()).getMode();
      } else {
         this.checkEntry();
         return this.mode;
      }
   }

   public void setRefid(Reference r) {
      if (this.archive == null && !this.modeSet) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   public int compareTo(Object another) {
      return this.equals(another) ? 0 : super.compareTo(another);
   }

   public boolean equals(Object another) {
      if (this == another) {
         return true;
      } else if (this.isReference()) {
         return this.getCheckedRef().equals(another);
      } else if (!another.getClass().equals(this.getClass())) {
         return false;
      } else {
         ArchiveResource r = (ArchiveResource)another;
         return this.getArchive().equals(r.getArchive()) && this.getName().equals(r.getName());
      }
   }

   public int hashCode() {
      return super.hashCode() * (this.getArchive() == null ? NULL_ARCHIVE : this.getArchive().hashCode());
   }

   public String toString() {
      return this.isReference() ? this.getCheckedRef().toString() : this.getArchive().toString() + ':' + this.getName();
   }

   private synchronized void checkEntry() throws BuildException {
      if (!this.haveEntry) {
         String name = this.getName();
         if (name == null) {
            throw new BuildException("entry name not set");
         } else {
            Resource r = this.getArchive();
            if (r == null) {
               throw new BuildException("archive attribute not set");
            } else if (!r.isExists()) {
               throw new BuildException(r.toString() + " does not exist.");
            } else if (r.isDirectory()) {
               throw new BuildException(r + " denotes a directory.");
            } else {
               this.fetchEntry();
               this.haveEntry = true;
            }
         }
      }
   }

   protected abstract void fetchEntry();
}
