package org.apache.velocity.anakia;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.util.StringUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.xml.sax.SAXParseException;

public class AnakiaTask extends MatchingTask {
   SAXBuilder builder = new SAXBuilder();
   private File destDir = null;
   File baseDir = null;
   private String style = null;
   private long styleSheetLastModified = 0L;
   private String projectAttribute = null;
   private File projectFile = null;
   private long projectFileLastModified = 0L;
   private boolean lastModifiedCheck = true;
   private String extension = ".html";
   private String templatePath = null;
   private File velocityPropertiesFile = null;
   private VelocityEngine ve = new VelocityEngine();
   private List contexts = new LinkedList();

   public AnakiaTask() {
      this.builder.setFactory(new AnakiaJDOMFactory());
   }

   public void setBasedir(File dir) {
      this.baseDir = dir;
   }

   public void setDestdir(File dir) {
      this.destDir = dir;
   }

   public void setExtension(String extension) {
      this.extension = extension;
   }

   public void setStyle(String style) {
      this.style = style;
   }

   public void setProjectFile(String projectAttribute) {
      this.projectAttribute = projectAttribute;
   }

   public void setTemplatePath(File templatePath) {
      try {
         this.templatePath = templatePath.getCanonicalPath();
      } catch (IOException var3) {
         throw new BuildException(var3);
      }
   }

   public void setVelocityPropertiesFile(File velocityPropertiesFile) {
      this.velocityPropertiesFile = velocityPropertiesFile;
   }

   public void setLastModifiedCheck(String lastmod) {
      if (lastmod.equalsIgnoreCase("false") || lastmod.equalsIgnoreCase("no") || lastmod.equalsIgnoreCase("off")) {
         this.lastModifiedCheck = false;
      }

   }

   public void execute() throws BuildException {
      if (this.baseDir == null) {
         this.baseDir = this.project.resolveFile(".");
      }

      if (this.destDir == null) {
         String msg = "destdir attribute must be set!";
         throw new BuildException(msg);
      } else if (this.style == null) {
         throw new BuildException("style attribute must be set!");
      } else {
         if (this.velocityPropertiesFile == null) {
            this.velocityPropertiesFile = new File("velocity.properties");
         }

         if (!this.velocityPropertiesFile.exists() && this.templatePath == null) {
            throw new BuildException("No template path and could not locate velocity.properties file: " + this.velocityPropertiesFile.getAbsolutePath());
         } else {
            this.log("Transforming into: " + this.destDir.getAbsolutePath(), 2);
            if (this.projectAttribute != null && this.projectAttribute.length() > 0) {
               this.projectFile = new File(this.baseDir, this.projectAttribute);
               if (this.projectFile.exists()) {
                  this.projectFileLastModified = this.projectFile.lastModified();
               } else {
                  this.log("Project file is defined, but could not be located: " + this.projectFile.getAbsolutePath(), 2);
                  this.projectFile = null;
               }
            }

            Document projectDocument = null;

            try {
               if (this.velocityPropertiesFile.exists()) {
                  String file = this.velocityPropertiesFile.getAbsolutePath();
                  ExtendedProperties config = new ExtendedProperties(file);
                  this.ve.setExtendedProperties(config);
               }

               if (this.templatePath != null && this.templatePath.length() > 0) {
                  this.ve.setProperty("file.resource.loader.path", this.templatePath);
               }

               this.ve.init();
               this.styleSheetLastModified = this.ve.getTemplate(this.style).getLastModified();
               if (this.projectFile != null) {
                  projectDocument = this.builder.build(this.projectFile);
               }
            } catch (Exception var6) {
               this.log("Error: " + var6.toString(), 2);
               throw new BuildException(var6);
            }

            DirectoryScanner scanner = this.getDirectoryScanner(this.baseDir);
            String[] list = scanner.getIncludedFiles();

            for(int i = 0; i < list.length; ++i) {
               this.process(list[i], projectDocument);
            }

         }
      }
   }

   private void process(String xmlFile, Document projectDocument) throws BuildException {
      File outFile = null;
      File inFile = null;
      BufferedWriter writer = null;

      try {
         inFile = new File(this.baseDir, xmlFile);
         outFile = new File(this.destDir, xmlFile.substring(0, xmlFile.lastIndexOf(46)) + this.extension);
         if (!this.lastModifiedCheck || inFile.lastModified() > outFile.lastModified() || this.styleSheetLastModified > outFile.lastModified() || this.projectFileLastModified > outFile.lastModified() || this.userContextsModifed(outFile.lastModified())) {
            this.ensureDirectoryFor(outFile);
            this.log("Input:  " + xmlFile, 2);
            Document root = this.builder.build(inFile);
            VelocityContext context = new VelocityContext();
            String encoding = (String)this.ve.getProperty("output.encoding");
            if (encoding == null || encoding.length() == 0 || encoding.equals("8859-1") || encoding.equals("8859_1")) {
               encoding = "ISO-8859-1";
            }

            Format f = Format.getRawFormat();
            f.setEncoding(encoding);
            OutputWrapper ow = new OutputWrapper(f);
            context.put("root", root.getRootElement());
            context.put("xmlout", ow);
            context.put("relativePath", this.getRelativePath(xmlFile));
            context.put("treeWalk", new TreeWalker());
            context.put("xpath", new XPathTool());
            context.put("escape", new Escape());
            context.put("date", new Date());
            if (projectDocument != null) {
               context.put("project", projectDocument.getRootElement());
            }

            Iterator iter = this.contexts.iterator();

            while(iter.hasNext()) {
               AnakiaTask.Context subContext = (AnakiaTask.Context)iter.next();
               if (subContext == null) {
                  throw new BuildException("Found an undefined SubContext!");
               }

               if (subContext.getContextDocument() == null) {
                  throw new BuildException("Could not build a subContext for " + subContext.getName());
               }

               context.put(subContext.getName(), subContext.getContextDocument().getRootElement());
            }

            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), encoding));
            Template template = this.ve.getTemplate(this.style);
            template.merge(context, writer);
            this.log("Output: " + outFile, 2);
         }
      } catch (JDOMException var29) {
         outFile.delete();
         if (var29.getCause() != null) {
            Throwable rootCause = var29.getCause();
            if (rootCause instanceof SAXParseException) {
               System.out.println("");
               System.out.println("Error: " + rootCause.getMessage());
               System.out.println("       Line: " + ((SAXParseException)rootCause).getLineNumber() + " Column: " + ((SAXParseException)rootCause).getColumnNumber());
               System.out.println("");
            } else {
               rootCause.printStackTrace();
            }
         } else {
            var29.printStackTrace();
         }
      } catch (Throwable var30) {
         if (outFile != null) {
            outFile.delete();
         }

         var30.printStackTrace();
      } finally {
         if (writer != null) {
            try {
               writer.flush();
            } catch (IOException var28) {
            }

            try {
               writer.close();
            } catch (IOException var27) {
            }
         }

      }

   }

   private String getRelativePath(String file) {
      if (file != null && file.length() != 0) {
         StringTokenizer st = new StringTokenizer(file, "/\\");
         int slashCount = st.countTokens() - 1;
         StringBuffer sb = new StringBuffer();

         for(int i = 0; i < slashCount; ++i) {
            sb.append("../");
         }

         return sb.toString().length() > 0 ? StringUtils.chop(sb.toString(), 1) : ".";
      } else {
         return "";
      }
   }

   private void ensureDirectoryFor(File targetFile) throws BuildException {
      File directory = new File(targetFile.getParent());
      if (!directory.exists() && !directory.mkdirs()) {
         throw new BuildException("Unable to create directory: " + directory.getAbsolutePath());
      }
   }

   private boolean userContextsModifed(long lastModified) {
      Iterator iter = this.contexts.iterator();

      AnakiaTask.Context ctx;
      do {
         if (!iter.hasNext()) {
            return false;
         }

         ctx = (AnakiaTask.Context)iter.next();
      } while(ctx.getLastModified() <= lastModified);

      return true;
   }

   public AnakiaTask.Context createContext() {
      AnakiaTask.Context context = new AnakiaTask.Context();
      this.contexts.add(context);
      return context;
   }

   public class Context {
      private String name;
      private Document contextDoc = null;
      private String file;

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         if (!name.equals("relativePath") && !name.equals("treeWalk") && !name.equals("xpath") && !name.equals("escape") && !name.equals("date") && !name.equals("project")) {
            this.name = name;
         } else {
            throw new IllegalArgumentException("Context name '" + name + "' is reserved by Anakia");
         }
      }

      public void setFile(String file) {
         this.file = file;
      }

      public long getLastModified() {
         return (new File(AnakiaTask.this.baseDir, this.file)).lastModified();
      }

      public Document getContextDocument() {
         if (this.contextDoc == null) {
            File contextFile = new File(AnakiaTask.this.baseDir, this.file);

            try {
               this.contextDoc = AnakiaTask.this.builder.build(contextFile);
            } catch (Exception var3) {
               throw new BuildException(var3);
            }
         }

         return this.contextDoc;
      }
   }
}
