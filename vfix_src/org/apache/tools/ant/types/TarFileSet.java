package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class TarFileSet extends ArchiveFileSet {
   private boolean userNameSet;
   private boolean groupNameSet;
   private boolean userIdSet;
   private boolean groupIdSet;
   private String userName = "";
   private String groupName = "";
   private int uid;
   private int gid;

   public TarFileSet() {
   }

   protected TarFileSet(FileSet fileset) {
      super(fileset);
   }

   protected TarFileSet(TarFileSet fileset) {
      super((ArchiveFileSet)fileset);
   }

   public void setUserName(String userName) {
      this.checkTarFileSetAttributesAllowed();
      this.userNameSet = true;
      this.userName = userName;
   }

   public String getUserName() {
      return this.isReference() ? ((TarFileSet)this.getCheckedRef()).getUserName() : this.userName;
   }

   public boolean hasUserNameBeenSet() {
      return this.userNameSet;
   }

   public void setUid(int uid) {
      this.checkTarFileSetAttributesAllowed();
      this.userIdSet = true;
      this.uid = uid;
   }

   public int getUid() {
      return this.isReference() ? ((TarFileSet)this.getCheckedRef()).getUid() : this.uid;
   }

   public boolean hasUserIdBeenSet() {
      return this.userIdSet;
   }

   public void setGroup(String groupName) {
      this.checkTarFileSetAttributesAllowed();
      this.groupNameSet = true;
      this.groupName = groupName;
   }

   public String getGroup() {
      return this.isReference() ? ((TarFileSet)this.getCheckedRef()).getGroup() : this.groupName;
   }

   public boolean hasGroupBeenSet() {
      return this.groupNameSet;
   }

   public void setGid(int gid) {
      this.checkTarFileSetAttributesAllowed();
      this.groupIdSet = true;
      this.gid = gid;
   }

   public int getGid() {
      return this.isReference() ? ((TarFileSet)this.getCheckedRef()).getGid() : this.gid;
   }

   public boolean hasGroupIdBeenSet() {
      return this.groupIdSet;
   }

   protected ArchiveScanner newArchiveScanner() {
      TarScanner zs = new TarScanner();
      return zs;
   }

   public void setRefid(Reference r) throws BuildException {
      if (!this.userNameSet && !this.userIdSet && !this.groupNameSet && !this.groupIdSet) {
         super.setRefid(r);
      } else {
         throw this.tooManyAttributes();
      }
   }

   protected AbstractFileSet getRef(Project p) {
      this.dieOnCircularReference(p);
      Object o = this.getRefid().getReferencedObject(p);
      if (o instanceof TarFileSet) {
         return (AbstractFileSet)o;
      } else if (o instanceof FileSet) {
         TarFileSet zfs = new TarFileSet((FileSet)o);
         this.configureFileSet(zfs);
         return zfs;
      } else {
         String msg = this.getRefid().getRefId() + " doesn't denote a tarfileset or a fileset";
         throw new BuildException(msg);
      }
   }

   protected void configureFileSet(ArchiveFileSet zfs) {
      super.configureFileSet(zfs);
      if (zfs instanceof TarFileSet) {
         TarFileSet tfs = (TarFileSet)zfs;
         tfs.setUserName(this.userName);
         tfs.setGroup(this.groupName);
         tfs.setUid(this.uid);
         tfs.setGid(this.gid);
      }

   }

   public Object clone() {
      return this.isReference() ? ((TarFileSet)this.getRef(this.getProject())).clone() : super.clone();
   }

   private void checkTarFileSetAttributesAllowed() {
      if (this.getProject() == null || this.isReference() && this.getRefid().getReferencedObject(this.getProject()) instanceof TarFileSet) {
         this.checkAttributesAllowed();
      }

   }
}
