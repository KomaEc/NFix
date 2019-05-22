package org.apache.maven.doxia.module.xhtml.decoration.render;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.util.PathTool;
import org.codehaus.plexus.util.StringUtils;

public class RenderingContext {
   private final File basedir;
   private final String inputName;
   private final String outputName;
   private final String parserId;
   private final String relativePath;
   private final String extension;
   private Map attributes;

   public RenderingContext(File basedir, String document) {
      this(basedir, document, (String)null);
   }

   public RenderingContext(File basedir, String document, String parserId) {
      this(basedir, document, (String)null, (String)null);
   }

   public RenderingContext(File basedir, String document, String parserId, String extension) {
      this.basedir = basedir;
      this.extension = extension;
      if (StringUtils.isNotEmpty(extension)) {
         int startIndexOfExtension = document.indexOf("." + extension);
         String fileNameWithoutExt = document.substring(0, startIndexOfExtension);
         this.outputName = fileNameWithoutExt + ".html";
      } else {
         this.outputName = document.substring(0, document.indexOf(".")).replace('\\', '/') + ".html";
      }

      this.relativePath = PathTool.getRelativePath(basedir.getPath(), (new File(basedir, document)).getPath());
      this.inputName = document;
      this.parserId = parserId;
      this.attributes = new HashMap();
   }

   public File getBasedir() {
      return this.basedir;
   }

   public String getInputName() {
      return this.inputName;
   }

   public String getOutputName() {
      return this.outputName;
   }

   public String getParserId() {
      return this.parserId;
   }

   public String getRelativePath() {
      return this.relativePath;
   }

   public void setAttribute(String key, String value) {
      this.attributes.put(key, value);
   }

   public String getAttribute(String key) {
      return (String)this.attributes.get(key);
   }

   public String getExtension() {
      return this.extension;
   }
}
