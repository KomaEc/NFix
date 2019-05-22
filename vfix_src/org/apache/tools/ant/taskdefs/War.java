package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.zip.ZipOutputStream;

public class War extends Jar {
   private File deploymentDescriptor;
   private boolean needxmlfile = true;
   private File addedWebXmlFile;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final String XML_DESCRIPTOR_PATH = "WEB-INF/web.xml";
   private static final String XML_DESCRIPTOR_PATH_LC;

   public War() {
      this.archiveType = "war";
      this.emptyBehavior = "create";
   }

   /** @deprecated */
   public void setWarfile(File warFile) {
      this.setDestFile(warFile);
   }

   public void setWebxml(File descr) {
      this.deploymentDescriptor = descr;
      if (!this.deploymentDescriptor.exists()) {
         throw new BuildException("Deployment descriptor: " + this.deploymentDescriptor + " does not exist.");
      } else {
         ZipFileSet fs = new ZipFileSet();
         fs.setFile(this.deploymentDescriptor);
         fs.setFullpath("WEB-INF/web.xml");
         super.addFileset(fs);
      }
   }

   public void setNeedxmlfile(boolean needxmlfile) {
      this.needxmlfile = needxmlfile;
   }

   public void addLib(ZipFileSet fs) {
      fs.setPrefix("WEB-INF/lib/");
      super.addFileset(fs);
   }

   public void addClasses(ZipFileSet fs) {
      fs.setPrefix("WEB-INF/classes/");
      super.addFileset(fs);
   }

   public void addWebinf(ZipFileSet fs) {
      fs.setPrefix("WEB-INF/");
      super.addFileset(fs);
   }

   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
      super.initZipOutputStream(zOut);
   }

   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
      String vPathLowerCase = vPath.toLowerCase(Locale.ENGLISH);
      boolean addFile = true;
      if (XML_DESCRIPTOR_PATH_LC.equals(vPathLowerCase)) {
         if (this.addedWebXmlFile != null) {
            addFile = false;
            if (!FILE_UTILS.fileNameEquals(this.addedWebXmlFile, file)) {
               this.log("Warning: selected " + this.archiveType + " files include a second " + "WEB-INF/web.xml" + " which will be ignored.\n" + "The duplicate entry is at " + file + '\n' + "The file that will be used is " + this.addedWebXmlFile, 1);
            }
         } else {
            this.addedWebXmlFile = file;
            addFile = true;
            this.deploymentDescriptor = file;
         }
      }

      if (addFile) {
         super.zipFile(file, zOut, vPath, mode);
      }

   }

   protected void cleanUp() {
      if (this.addedWebXmlFile == null && this.deploymentDescriptor == null && this.needxmlfile && !this.isInUpdateMode()) {
         throw new BuildException("No WEB-INF/web.xml file was added.\nIf this is your intent, set needxml='false' ");
      } else {
         this.addedWebXmlFile = null;
         super.cleanUp();
      }
   }

   static {
      XML_DESCRIPTOR_PATH_LC = "WEB-INF/web.xml".toLowerCase(Locale.ENGLISH);
   }
}
