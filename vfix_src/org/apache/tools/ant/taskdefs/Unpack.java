package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;

public abstract class Unpack extends Task {
   protected File source;
   protected File dest;
   protected Resource srcResource;

   /** @deprecated */
   public void setSrc(String src) {
      this.log("DEPRECATED - The setSrc(String) method has been deprecated. Use setSrc(File) instead.");
      this.setSrc(this.getProject().resolveFile(src));
   }

   /** @deprecated */
   public void setDest(String dest) {
      this.log("DEPRECATED - The setDest(String) method has been deprecated. Use setDest(File) instead.");
      this.setDest(this.getProject().resolveFile(dest));
   }

   public void setSrc(File src) {
      this.setSrcResource(new FileResource(src));
   }

   public void setSrcResource(Resource src) {
      if (!src.isExists()) {
         throw new BuildException("the archive doesn't exist");
      } else if (src.isDirectory()) {
         throw new BuildException("the archive can't be a directory");
      } else {
         if (src instanceof FileResource) {
            this.source = ((FileResource)src).getFile();
         } else if (!this.supportsNonFileResources()) {
            throw new BuildException("Only FileSystem resources are supported.");
         }

         this.srcResource = src;
      }
   }

   public void addConfigured(ResourceCollection a) {
      if (a.size() != 1) {
         throw new BuildException("only single argument resource collections are supported as archives");
      } else {
         this.setSrcResource((Resource)a.iterator().next());
      }
   }

   public void setDest(File dest) {
      this.dest = dest;
   }

   private void validate() throws BuildException {
      if (this.srcResource == null) {
         throw new BuildException("No Src specified", this.getLocation());
      } else {
         if (this.dest == null) {
            this.dest = new File(this.source.getParent());
         }

         if (this.dest.isDirectory()) {
            String defaultExtension = this.getDefaultExtension();
            this.createDestFile(defaultExtension);
         }

      }
   }

   private void createDestFile(String defaultExtension) {
      String sourceName = this.source.getName();
      int len = sourceName.length();
      if (defaultExtension != null && len > defaultExtension.length() && defaultExtension.equalsIgnoreCase(sourceName.substring(len - defaultExtension.length()))) {
         this.dest = new File(this.dest, sourceName.substring(0, len - defaultExtension.length()));
      } else {
         this.dest = new File(this.dest, sourceName);
      }

   }

   public void execute() throws BuildException {
      File savedDest = this.dest;

      try {
         this.validate();
         this.extract();
      } finally {
         this.dest = savedDest;
      }

   }

   protected abstract String getDefaultExtension();

   protected abstract void extract();

   protected boolean supportsNonFileResources() {
      return false;
   }
}
