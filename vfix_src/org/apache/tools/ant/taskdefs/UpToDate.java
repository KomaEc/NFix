package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.SourceFileScanner;

public class UpToDate extends Task implements Condition {
   private String property;
   private String value;
   private File sourceFile;
   private File targetFile;
   private Vector sourceFileSets = new Vector();
   private Union sourceResources = new Union();
   protected Mapper mapperElement = null;

   public void setProperty(String property) {
      this.property = property;
   }

   public void setValue(String value) {
      this.value = value;
   }

   private String getValue() {
      return this.value != null ? this.value : "true";
   }

   public void setTargetFile(File file) {
      this.targetFile = file;
   }

   public void setSrcfile(File file) {
      this.sourceFile = file;
   }

   public void addSrcfiles(FileSet fs) {
      this.sourceFileSets.addElement(fs);
   }

   public Union createSrcResources() {
      return this.sourceResources;
   }

   public Mapper createMapper() throws BuildException {
      if (this.mapperElement != null) {
         throw new BuildException("Cannot define more than one mapper", this.getLocation());
      } else {
         this.mapperElement = new Mapper(this.getProject());
         return this.mapperElement;
      }
   }

   public void add(FileNameMapper fileNameMapper) {
      this.createMapper().add(fileNameMapper);
   }

   public boolean eval() {
      if (this.sourceFileSets.size() == 0 && this.sourceResources.size() == 0 && this.sourceFile == null) {
         throw new BuildException("At least one srcfile or a nested <srcfiles> or <srcresources> element must be set.");
      } else if ((this.sourceFileSets.size() > 0 || this.sourceResources.size() > 0) && this.sourceFile != null) {
         throw new BuildException("Cannot specify both the srcfile attribute and a nested <srcfiles> or <srcresources> element.");
      } else if (this.targetFile == null && this.mapperElement == null) {
         throw new BuildException("The targetfile attribute or a nested mapper element must be set.");
      } else if (this.targetFile != null && !this.targetFile.exists()) {
         this.log("The targetfile \"" + this.targetFile.getAbsolutePath() + "\" does not exist.", 3);
         return false;
      } else if (this.sourceFile != null && !this.sourceFile.exists()) {
         throw new BuildException(this.sourceFile.getAbsolutePath() + " not found.");
      } else {
         boolean upToDate = true;
         if (this.sourceFile != null) {
            if (this.mapperElement == null) {
               upToDate = upToDate && this.targetFile.lastModified() >= this.sourceFile.lastModified();
            } else {
               SourceFileScanner sfs = new SourceFileScanner(this);
               upToDate = upToDate && sfs.restrict(new String[]{this.sourceFile.getAbsolutePath()}, (File)null, (File)null, this.mapperElement.getImplementation()).length == 0;
            }
         }

         FileSet fs;
         DirectoryScanner ds;
         for(Enumeration e = this.sourceFileSets.elements(); upToDate && e.hasMoreElements(); upToDate = upToDate && this.scanDir(fs.getDir(this.getProject()), ds.getIncludedFiles())) {
            fs = (FileSet)e.nextElement();
            ds = fs.getDirectoryScanner(this.getProject());
         }

         if (upToDate) {
            Resource[] r = this.sourceResources.listResources();
            upToDate = upToDate && ResourceUtils.selectOutOfDateSources(this, r, this.getMapper(), this.getProject()).length == 0;
         }

         return upToDate;
      }
   }

   public void execute() throws BuildException {
      if (this.property == null) {
         throw new BuildException("property attribute is required.", this.getLocation());
      } else {
         boolean upToDate = this.eval();
         if (upToDate) {
            this.getProject().setNewProperty(this.property, this.getValue());
            if (this.mapperElement == null) {
               this.log("File \"" + this.targetFile.getAbsolutePath() + "\" is up-to-date.", 3);
            } else {
               this.log("All target files are up-to-date.", 3);
            }
         }

      }
   }

   protected boolean scanDir(File srcDir, String[] files) {
      SourceFileScanner sfs = new SourceFileScanner(this);
      FileNameMapper mapper = this.getMapper();
      File dir = srcDir;
      if (this.mapperElement == null) {
         dir = null;
      }

      return sfs.restrict(files, srcDir, dir, mapper).length == 0;
   }

   private FileNameMapper getMapper() {
      FileNameMapper mapper = null;
      if (this.mapperElement == null) {
         MergingMapper mm = new MergingMapper();
         mm.setTo(this.targetFile.getAbsolutePath());
         mapper = mm;
      } else {
         mapper = this.mapperElement.getImplementation();
      }

      return (FileNameMapper)mapper;
   }
}
