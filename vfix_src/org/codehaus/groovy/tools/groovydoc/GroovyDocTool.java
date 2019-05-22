package org.codehaus.groovy.tools.groovydoc;

import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;

public class GroovyDocTool {
   private final GroovyRootDocBuilder rootDocBuilder;
   private final GroovyDocTemplateEngine templateEngine;
   private Properties properties;

   public GroovyDocTool(String[] sourcepaths) {
      this((ResourceManager)null, sourcepaths, (String)null);
   }

   public GroovyDocTool(ResourceManager resourceManager, String[] sourcepaths, String classTemplate) {
      this(resourceManager, sourcepaths, new String[0], new String[0], new String[]{classTemplate}, new ArrayList(), new Properties());
   }

   public GroovyDocTool(ResourceManager resourceManager, String[] sourcepaths, String[] docTemplates, String[] packageTemplates, String[] classTemplates, List<LinkArgument> links, Properties properties) {
      this.rootDocBuilder = new GroovyRootDocBuilder(this, sourcepaths, links, properties);
      this.properties = properties;
      if (resourceManager == null) {
         this.templateEngine = null;
      } else {
         this.templateEngine = new GroovyDocTemplateEngine(this, resourceManager, docTemplates, packageTemplates, classTemplates, properties);
      }

   }

   public void add(List<String> filenames) throws RecognitionException, TokenStreamException, IOException {
      if (this.templateEngine != null) {
         System.out.println("Loading source files for " + filenames);
      }

      this.rootDocBuilder.buildTree(filenames);
   }

   public GroovyRootDoc getRootDoc() {
      return this.rootDocBuilder.getRootDoc();
   }

   public void renderToOutput(OutputTool output, String destdir) throws Exception {
      if ("true".equals(this.properties.getProperty("privateScope"))) {
         this.properties.setProperty("packageScope", "true");
      }

      if ("true".equals(this.properties.getProperty("packageScope"))) {
         this.properties.setProperty("protectedScope", "true");
      }

      if ("true".equals(this.properties.getProperty("protectedScope"))) {
         this.properties.setProperty("publicScope", "true");
      }

      if (this.templateEngine != null) {
         GroovyDocWriter writer = new GroovyDocWriter(this, output, this.templateEngine, this.properties);
         GroovyRootDoc rootDoc = this.rootDocBuilder.getRootDoc();
         writer.writeRoot(rootDoc, destdir);
         writer.writePackages(rootDoc, destdir);
         writer.writeClasses(rootDoc, destdir);
      } else {
         throw new UnsupportedOperationException("No template engine was found");
      }
   }

   String getPath(String filename) {
      String path = (new File(filename)).getParent();
      if (path == null || path.length() == 1) {
         path = "DefaultPackage";
      }

      return path;
   }

   String getFile(String filename) {
      return (new File(filename)).getName();
   }
}
