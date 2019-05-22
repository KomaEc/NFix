package org.codehaus.groovy.tools.groovydoc;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyPackageDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;

public class GroovyDocWriter {
   private GroovyDocTool tool;
   private OutputTool output;
   private GroovyDocTemplateEngine templateEngine;
   private static final String FS = "/";
   private Properties properties;

   public GroovyDocWriter(GroovyDocTool tool, OutputTool output, GroovyDocTemplateEngine templateEngine, Properties properties) {
      this.tool = tool;
      this.output = output;
      this.templateEngine = templateEngine;
      this.properties = properties;
   }

   public void writeClasses(GroovyRootDoc rootDoc, String destdir) throws Exception {
      GroovyClassDoc[] arr$ = rootDoc.classes();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GroovyClassDoc classDoc = arr$[i$];
         this.writeClassToOutput(classDoc, destdir);
      }

   }

   public void writeClassToOutput(GroovyClassDoc classDoc, String destdir) throws Exception {
      if (classDoc.isPublic() || classDoc.isProtected() && "true".equals(this.properties.getProperty("protectedScope")) || classDoc.isPackagePrivate() && "true".equals(this.properties.getProperty("packageScope")) || "true".equals(this.properties.getProperty("privateScope"))) {
         String destFileName = destdir + "/" + classDoc.getFullPathName() + ".html";
         System.out.println("Generating " + destFileName);
         String renderedSrc = this.templateEngine.applyClassTemplates(classDoc);
         this.output.writeToOutput(destFileName, renderedSrc);
      }

   }

   public void writePackages(GroovyRootDoc rootDoc, String destdir) throws Exception {
      GroovyPackageDoc[] arr$ = rootDoc.specifiedPackages();
      int len$ = arr$.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         GroovyPackageDoc packageDoc = arr$[len$];
         if (!(new File(packageDoc.name())).isAbsolute()) {
            this.output.makeOutputArea(destdir + "/" + packageDoc.name());
            this.writePackageToOutput(packageDoc, destdir);
         }
      }

      StringBuilder sb = new StringBuilder();
      GroovyPackageDoc[] arr$ = rootDoc.specifiedPackages();
      len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         GroovyPackageDoc packageDoc = arr$[i$];
         sb.append(packageDoc.nameWithDots());
         sb.append("\n");
      }

      String destFileName = destdir + "/" + "package-list";
      System.out.println("Generating " + destFileName);
      this.output.writeToOutput(destFileName, sb.toString());
   }

   public void writePackageToOutput(GroovyPackageDoc packageDoc, String destdir) throws Exception {
      Iterator templates = this.templateEngine.packageTemplatesIterator();

      while(templates.hasNext()) {
         String template = (String)templates.next();
         String renderedSrc = this.templateEngine.applyPackageTemplate(template, packageDoc);
         String destFileName = destdir + "/" + packageDoc.name() + "/" + this.tool.getFile(template);
         System.out.println("Generating " + destFileName);
         this.output.writeToOutput(destFileName, renderedSrc);
      }

   }

   public void writeRoot(GroovyRootDoc rootDoc, String destdir) throws Exception {
      this.output.makeOutputArea(destdir);
      this.writeRootDocToOutput(rootDoc, destdir);
   }

   public void writeRootDocToOutput(GroovyRootDoc rootDoc, String destdir) throws Exception {
      Iterator templates = this.templateEngine.docTemplatesIterator();

      while(templates.hasNext()) {
         String template = (String)templates.next();
         String destFileName = destdir + "/" + this.tool.getFile(template);
         System.out.println("Generating " + destFileName);
         if (this.hasBinaryExtension(template)) {
            this.templateEngine.copyBinaryResource(template, destFileName);
         } else {
            String renderedSrc = this.templateEngine.applyRootDocTemplate(template, rootDoc);
            this.output.writeToOutput(destFileName, renderedSrc);
         }
      }

   }

   private boolean hasBinaryExtension(String template) {
      return template.endsWith(".gif") || template.endsWith(".ico");
   }
}
