package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

public class Ear extends Jar {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File deploymentDescriptor;
   private boolean descriptorAdded;

   public Ear() {
      this.archiveType = "ear";
      this.emptyBehavior = "create";
   }

   /** @deprecated */
   public void setEarfile(File earFile) {
      this.setDestFile(earFile);
   }

   public void setAppxml(File descr) {
      this.deploymentDescriptor = descr;
      if (!this.deploymentDescriptor.exists()) {
         throw new BuildException("Deployment descriptor: " + this.deploymentDescriptor + " does not exist.");
      } else {
         ZipFileSet fs = new ZipFileSet();
         fs.setFile(this.deploymentDescriptor);
         fs.setFullpath("META-INF/application.xml");
         super.addFileset(fs);
      }
   }

   public void addArchives(ZipFileSet fs) {
      fs.setPrefix("/");
      super.addFileset(fs);
   }

   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
      if (this.deploymentDescriptor == null && !this.isInUpdateMode()) {
         throw new BuildException("appxml attribute is required", this.getLocation());
      } else {
         super.initZipOutputStream(zOut);
      }
   }

   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
      if (vPath.equalsIgnoreCase("META-INF/application.xml")) {
         if (this.deploymentDescriptor != null && FILE_UTILS.fileNameEquals(this.deploymentDescriptor, file) && !this.descriptorAdded) {
            super.zipFile(file, zOut, vPath, mode);
            this.descriptorAdded = true;
         } else {
            this.log("Warning: selected " + this.archiveType + " files include a META-INF/application.xml which will" + " be ignored (please use appxml attribute to " + this.archiveType + " task)", 1);
         }
      } else {
         super.zipFile(file, zOut, vPath, mode);
      }

   }

   protected void cleanUp() {
      this.descriptorAdded = false;
      super.cleanUp();
   }
}
