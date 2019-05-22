package org.apache.tools.ant.types.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

public class TarResource extends ArchiveResource {
   private String userName = "";
   private String groupName = "";
   private int uid;
   private int gid;

   public TarResource() {
   }

   public TarResource(File a, TarEntry e) {
      super(a, true);
      this.setEntry(e);
   }

   public TarResource(Resource a, TarEntry e) {
      super(a, true);
      this.setEntry(e);
   }

   public InputStream getInputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getInputStream();
      } else {
         Resource archive = this.getArchive();
         TarInputStream i = new TarInputStream(archive.getInputStream());
         TarEntry te = null;

         do {
            if ((te = i.getNextEntry()) == null) {
               FileUtils.close((InputStream)i);
               throw new BuildException("no entry " + this.getName() + " in " + this.getArchive());
            }
         } while(!te.getName().equals(this.getName()));

         return i;
      }
   }

   public OutputStream getOutputStream() throws IOException {
      if (this.isReference()) {
         return ((Resource)this.getCheckedRef()).getOutputStream();
      } else {
         throw new UnsupportedOperationException("Use the tar task for tar output.");
      }
   }

   public String getUserName() {
      return this.isReference() ? ((TarResource)this.getCheckedRef()).getUserName() : this.userName;
   }

   public String getGroup() {
      return this.isReference() ? ((TarResource)this.getCheckedRef()).getGroup() : this.groupName;
   }

   public int getUid() {
      return this.isReference() ? ((TarResource)this.getCheckedRef()).getUid() : this.uid;
   }

   public int getGid() {
      return this.isReference() ? ((TarResource)this.getCheckedRef()).getGid() : this.uid;
   }

   protected void fetchEntry() {
      Resource archive = this.getArchive();
      TarInputStream i = null;

      label76: {
         try {
            i = new TarInputStream(archive.getInputStream());
            TarEntry te = null;

            do {
               if ((te = i.getNextEntry()) == null) {
                  break label76;
               }
            } while(!te.getName().equals(this.getName()));

            this.setEntry(te);
         } catch (IOException var7) {
            this.log(var7.getMessage(), 4);
            throw new BuildException(var7);
         } finally {
            if (i != null) {
               FileUtils.close((InputStream)i);
            }

         }

         return;
      }

      this.setEntry((TarEntry)null);
   }

   private void setEntry(TarEntry e) {
      if (e == null) {
         this.setExists(false);
      } else {
         this.setName(e.getName());
         this.setExists(true);
         this.setLastModified(e.getModTime().getTime());
         this.setDirectory(e.isDirectory());
         this.setSize(e.getSize());
         this.setMode(e.getMode());
         this.userName = e.getUserName();
         this.groupName = e.getGroupName();
         this.uid = e.getUserId();
         this.gid = e.getGroupId();
      }
   }
}
