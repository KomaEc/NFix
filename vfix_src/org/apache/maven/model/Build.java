package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Build extends BuildBase implements Serializable {
   private String sourceDirectory;
   private String scriptSourceDirectory;
   private String testSourceDirectory;
   private String outputDirectory;
   private String testOutputDirectory;
   private List<Extension> extensions;

   public void addExtension(Extension extension) {
      if (!(extension instanceof Extension)) {
         throw new ClassCastException("Build.addExtensions(extension) parameter must be instanceof " + Extension.class.getName());
      } else {
         this.getExtensions().add(extension);
      }
   }

   public List<Extension> getExtensions() {
      if (this.extensions == null) {
         this.extensions = new ArrayList();
      }

      return this.extensions;
   }

   public String getOutputDirectory() {
      return this.outputDirectory;
   }

   public String getScriptSourceDirectory() {
      return this.scriptSourceDirectory;
   }

   public String getSourceDirectory() {
      return this.sourceDirectory;
   }

   public String getTestOutputDirectory() {
      return this.testOutputDirectory;
   }

   public String getTestSourceDirectory() {
      return this.testSourceDirectory;
   }

   public void removeExtension(Extension extension) {
      if (!(extension instanceof Extension)) {
         throw new ClassCastException("Build.removeExtensions(extension) parameter must be instanceof " + Extension.class.getName());
      } else {
         this.getExtensions().remove(extension);
      }
   }

   public void setExtensions(List<Extension> extensions) {
      this.extensions = extensions;
   }

   public void setOutputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
   }

   public void setScriptSourceDirectory(String scriptSourceDirectory) {
      this.scriptSourceDirectory = scriptSourceDirectory;
   }

   public void setSourceDirectory(String sourceDirectory) {
      this.sourceDirectory = sourceDirectory;
   }

   public void setTestOutputDirectory(String testOutputDirectory) {
      this.testOutputDirectory = testOutputDirectory;
   }

   public void setTestSourceDirectory(String testSourceDirectory) {
      this.testSourceDirectory = testSourceDirectory;
   }
}
