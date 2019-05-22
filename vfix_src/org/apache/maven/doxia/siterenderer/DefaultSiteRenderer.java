package org.apache.maven.doxia.siterenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.maven.doxia.Doxia;
import org.apache.maven.doxia.module.site.SiteModule;
import org.apache.maven.doxia.module.site.manager.SiteModuleManager;
import org.apache.maven.doxia.module.site.manager.SiteModuleNotFoundException;
import org.apache.maven.doxia.module.xhtml.decoration.render.RenderingContext;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.parser.manager.ParserNotFoundException;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.siterenderer.sink.SiteRendererSink;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.PathTool;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.velocity.SiteResourceLoader;
import org.codehaus.plexus.velocity.VelocityComponent;

public class DefaultSiteRenderer extends AbstractLogEnabled implements Renderer {
   private VelocityComponent velocity;
   private SiteModuleManager siteModuleManager;
   private Doxia doxia;
   private I18N i18n;
   private static final String RESOURCE_DIR = "org/apache/maven/doxia/siterenderer/resources";
   private static final String DEFAULT_TEMPLATE = "org/apache/maven/doxia/siterenderer/resources/default-site.vm";
   private static final String SKIN_TEMPLATE_LOCATION = "META-INF/maven/site.vm";

   public void render(Collection documents, SiteRenderingContext siteRenderingContext, File outputDirectory) throws RendererException, IOException {
      this.renderModule(documents, siteRenderingContext, outputDirectory);
      Iterator i = siteRenderingContext.getSiteDirectories().iterator();

      while(i.hasNext()) {
         File siteDirectory = (File)i.next();
         this.copyResources(siteRenderingContext, new File(siteDirectory, "resources"), outputDirectory);
      }

   }

   public Map locateDocumentFiles(SiteRenderingContext siteRenderingContext) throws IOException, RendererException {
      Map files = new LinkedHashMap();
      Map moduleExcludes = siteRenderingContext.getModuleExcludes();
      Iterator i = siteRenderingContext.getSiteDirectories().iterator();

      label56:
      while(true) {
         File siteDirectory;
         do {
            if (!i.hasNext()) {
               i = siteRenderingContext.getModules().iterator();

               while(i.hasNext()) {
                  ModuleReference module = (ModuleReference)i.next();

                  try {
                     if (moduleExcludes != null && moduleExcludes.containsKey(module.getParserId())) {
                        this.addModuleFiles(module.getBasedir(), this.siteModuleManager.getSiteModule(module.getParserId()), (String)moduleExcludes.get(module.getParserId()), files);
                     } else {
                        this.addModuleFiles(module.getBasedir(), this.siteModuleManager.getSiteModule(module.getParserId()), (String)null, files);
                     }
                  } catch (SiteModuleNotFoundException var9) {
                     throw new RendererException("Unable to find module: " + var9.getMessage(), var9);
                  }
               }

               return files;
            }

            siteDirectory = (File)i.next();
         } while(!siteDirectory.exists());

         Iterator j = this.siteModuleManager.getSiteModules().iterator();

         while(true) {
            while(true) {
               if (!j.hasNext()) {
                  continue label56;
               }

               SiteModule module = (SiteModule)j.next();
               File moduleBasedir = new File(siteDirectory, module.getSourceDirectory());
               if (moduleExcludes != null && moduleExcludes.containsKey(module.getParserId())) {
                  this.addModuleFiles(moduleBasedir, module, (String)moduleExcludes.get(module.getParserId()), files);
               } else {
                  this.addModuleFiles(moduleBasedir, module, (String)null, files);
               }
            }
         }
      }
   }

   private void addModuleFiles(File moduleBasedir, SiteModule module, String excludes, Map files) throws IOException, RendererException {
      if (moduleBasedir.exists()) {
         List docs = new ArrayList();
         docs.addAll(FileUtils.getFileNames(moduleBasedir, "**/*." + module.getExtension(), excludes, false));
         docs.addAll(FileUtils.getFileNames(moduleBasedir, "**/*." + module.getExtension() + ".vm", excludes, false));
         Iterator k = docs.iterator();

         while(k.hasNext()) {
            String doc = (String)k.next();
            RenderingContext context = new RenderingContext(moduleBasedir, doc, module.getParserId(), module.getExtension());
            if (doc.endsWith(".vm")) {
               context.setAttribute("velocity", "true");
            }

            String key = context.getOutputName();
            if (files.containsKey(key)) {
               DocumentRenderer renderer = (DocumentRenderer)files.get(key);
               RenderingContext originalContext = renderer.getRenderingContext();
               File originalDoc = new File(originalContext.getBasedir(), originalContext.getInputName());
               throw new RendererException("Files '" + doc + "' clashes with existing '" + originalDoc + "'.");
            }

            Iterator iter = files.entrySet().iterator();

            while(iter.hasNext()) {
               Entry entry = (Entry)iter.next();
               if (entry.getKey().toString().toLowerCase().equals(key.toLowerCase())) {
                  DocumentRenderer renderer = (DocumentRenderer)files.get(entry.getKey());
                  RenderingContext originalContext = renderer.getRenderingContext();
                  File originalDoc = new File(originalContext.getBasedir(), originalContext.getInputName());
                  if (Os.isFamily("windows")) {
                     throw new RendererException("Files '" + doc + "' clashes with existing '" + originalDoc + "'.");
                  }

                  this.getLogger().warn("Files '" + doc + "' could clashes with existing '" + originalDoc + "'.");
               }
            }

            files.put(key, new DoxiaDocumentRenderer(context));
         }
      }

   }

   private void renderModule(Collection docs, SiteRenderingContext siteRenderingContext, File outputDirectory) throws IOException, RendererException {
      Iterator i = docs.iterator();

      while(true) {
         while(i.hasNext()) {
            DocumentRenderer docRenderer = (DocumentRenderer)i.next();
            RenderingContext renderingContext = docRenderer.getRenderingContext();
            File outputFile = new File(outputDirectory, docRenderer.getOutputName());
            File inputFile = new File(renderingContext.getBasedir(), renderingContext.getInputName());
            boolean modified = false;
            if (!outputFile.exists() || inputFile.lastModified() > outputFile.lastModified()) {
               modified = true;
            }

            if (!modified && !docRenderer.isOverwrite()) {
               this.getLogger().debug(inputFile + " unchanged, not regenerating...");
            } else {
               if (!outputFile.getParentFile().exists()) {
                  outputFile.getParentFile().mkdirs();
               }

               this.getLogger().debug("Generating " + outputFile);
               OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(outputFile), siteRenderingContext.getOutputEncoding());

               try {
                  docRenderer.renderDocument(writer, this, siteRenderingContext);
               } finally {
                  IOUtil.close((Writer)writer);
               }
            }
         }

         return;
      }
   }

   public void renderDocument(Writer writer, RenderingContext renderingContext, SiteRenderingContext context) throws RendererException, FileNotFoundException, UnsupportedEncodingException {
      SiteRendererSink sink = new SiteRendererSink(renderingContext);
      File doc = new File(renderingContext.getBasedir(), renderingContext.getInputName());

      try {
         Reader reader = null;
         Parser parser = this.doxia.getParser(renderingContext.getParserId());
         if (renderingContext.getAttribute("velocity") != null) {
            String resource = doc.getAbsolutePath();

            try {
               SiteResourceLoader.setResource(resource);
               Context vc = this.createContext(sink, context);
               StringWriter sw = new StringWriter();
               this.velocity.getEngine().mergeTemplate(resource, context.getInputEncoding(), vc, sw);
               reader = new StringReader(sw.toString());
            } catch (Exception var17) {
               if (this.getLogger().isDebugEnabled()) {
                  this.getLogger().error("Error parsing " + resource + " as a velocity template, using as text.", var17);
               } else {
                  this.getLogger().error("Error parsing " + resource + " as a velocity template, using as text.");
               }
            }
         } else {
            switch(parser.getType()) {
            case 0:
            case 1:
            default:
               reader = ReaderFactory.newReader(doc, context.getInputEncoding());
               break;
            case 2:
               reader = ReaderFactory.newXmlReader(doc);
            }
         }

         this.doxia.parse((Reader)reader, renderingContext.getParserId(), sink);
         this.generateDocument(writer, sink, context);
      } catch (ParserNotFoundException var18) {
         throw new RendererException("Error getting a parser for " + doc + ": " + var18.getMessage());
      } catch (ParseException var19) {
         this.getLogger().error("Error parsing " + doc + ": line [" + var19.getLineNumber() + "] " + var19.getMessage(), var19);
      } catch (IOException var20) {
         this.getLogger().error("Error parsing " + doc + " to detect encoding", var20);
      } finally {
         sink.flush();
         sink.close();
      }

   }

   private Context createContext(SiteRendererSink sink, SiteRenderingContext siteRenderingContext) {
      VelocityContext context = new VelocityContext();
      RenderingContext renderingContext = sink.getRenderingContext();
      context.put("relativePath", renderingContext.getRelativePath());
      context.put("authors", sink.getAuthors());
      String title = "";
      if (siteRenderingContext.getDecoration().getName() != null) {
         title = siteRenderingContext.getDecoration().getName();
      } else if (siteRenderingContext.getDefaultWindowTitle() != null) {
         title = siteRenderingContext.getDefaultWindowTitle();
      }

      if (title.length() > 0) {
         title = title + " - ";
      }

      title = title + sink.getTitle();
      context.put("title", title);
      context.put("bodyContent", sink.getBody());
      context.put("decoration", siteRenderingContext.getDecoration());
      context.put("currentDate", new Date());
      Locale locale = siteRenderingContext.getLocale();
      context.put("dateFormat", DateFormat.getDateInstance(2, locale));
      String currentFileName = renderingContext.getOutputName().replace('\\', '/');
      context.put("currentFileName", currentFileName);
      context.put("alignedFileName", PathTool.calculateLink(currentFileName, renderingContext.getRelativePath()));
      context.put("locale", locale);
      Map templateProperties = siteRenderingContext.getTemplateProperties();
      if (templateProperties != null) {
         Iterator i = templateProperties.keySet().iterator();

         while(i.hasNext()) {
            String key = (String)i.next();
            context.put(key, templateProperties.get(key));
         }
      }

      context.put("PathTool", new PathTool());
      context.put("FileUtils", new FileUtils());
      context.put("StringUtils", new StringUtils());
      context.put("i18n", this.i18n);
      return context;
   }

   public void generateDocument(Writer writer, SiteRendererSink sink, SiteRenderingContext siteRenderingContext) throws RendererException {
      Context context = this.createContext(sink, siteRenderingContext);
      this.writeTemplate(writer, context, siteRenderingContext);
   }

   private void writeTemplate(Writer writer, Context context, SiteRenderingContext siteContext) throws RendererException {
      ClassLoader old = null;
      if (siteContext.getTemplateClassLoader() != null) {
         old = Thread.currentThread().getContextClassLoader();
         Thread.currentThread().setContextClassLoader(siteContext.getTemplateClassLoader());
      }

      try {
         this.processTemplate(siteContext.getTemplateName(), context, writer);
      } finally {
         IOUtil.close(writer);
         if (old != null) {
            Thread.currentThread().setContextClassLoader(old);
         }

      }

   }

   private void processTemplate(String templateName, Context context, Writer writer) throws RendererException {
      Template template;
      try {
         template = this.velocity.getEngine().getTemplate(templateName);
      } catch (Exception var7) {
         throw new RendererException("Could not find the template '" + templateName);
      }

      try {
         template.merge(context, writer);
      } catch (Exception var6) {
         throw new RendererException("Error while generating code.", var6);
      }
   }

   public SiteRenderingContext createContextForSkin(File skinFile, Map attributes, DecorationModel decoration, String defaultWindowTitle, Locale locale) throws IOException {
      SiteRenderingContext context = new SiteRenderingContext();
      ZipFile zipFile = new ZipFile(skinFile);

      try {
         if (zipFile.getEntry("META-INF/maven/site.vm") != null) {
            context.setTemplateName("META-INF/maven/site.vm");
            context.setTemplateClassLoader(new URLClassLoader(new URL[]{skinFile.toURL()}));
         } else {
            context.setTemplateName("org/apache/maven/doxia/siterenderer/resources/default-site.vm");
            context.setTemplateClassLoader(this.getClass().getClassLoader());
            context.setUsingDefaultTemplate(true);
         }
      } finally {
         this.closeZipFile(zipFile);
      }

      context.setTemplateProperties(attributes);
      context.setLocale(locale);
      context.setDecoration(decoration);
      context.setDefaultWindowTitle(defaultWindowTitle);
      context.setSkinJarFile(skinFile);
      return context;
   }

   public SiteRenderingContext createContextForTemplate(File templateFile, File skinFile, Map attributes, DecorationModel decoration, String defaultWindowTitle, Locale locale) throws MalformedURLException {
      SiteRenderingContext context = new SiteRenderingContext();
      context.setTemplateName(templateFile.getName());
      context.setTemplateClassLoader(new URLClassLoader(new URL[]{templateFile.getParentFile().toURI().toURL()}));
      context.setTemplateProperties(attributes);
      context.setLocale(locale);
      context.setDecoration(decoration);
      context.setDefaultWindowTitle(defaultWindowTitle);
      context.setSkinJarFile(skinFile);
      return context;
   }

   private void closeZipFile(ZipFile zipFile) {
      try {
         zipFile.close();
      } catch (IOException var3) {
      }

   }

   public void copyResources(SiteRenderingContext siteRenderingContext, File resourcesDirectory, File outputDirectory) throws IOException {
      if (siteRenderingContext.getSkinJarFile() != null) {
         ZipFile file = new ZipFile(siteRenderingContext.getSkinJarFile());

         try {
            Enumeration e = file.entries();

            while(e.hasMoreElements()) {
               ZipEntry entry = (ZipEntry)e.nextElement();
               if (!entry.getName().startsWith("META-INF/")) {
                  File destFile = new File(outputDirectory, entry.getName());
                  if (!entry.isDirectory()) {
                     destFile.getParentFile().mkdirs();
                     this.copyFileFromZip(file, entry, destFile);
                  } else {
                     destFile.mkdirs();
                  }
               }
            }
         } finally {
            file.close();
         }
      }

      if (siteRenderingContext.isUsingDefaultTemplate()) {
         InputStream resourceList = this.getClass().getClassLoader().getResourceAsStream("org/apache/maven/doxia/siterenderer/resources/resources.txt");
         if (resourceList != null) {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(resourceList));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
               InputStream is = this.getClass().getClassLoader().getResourceAsStream("org/apache/maven/doxia/siterenderer/resources/" + line);
               if (is == null) {
                  throw new IOException("The resource " + line + " doesn't exist.");
               }

               File outputFile = new File(outputDirectory, line);
               if (!outputFile.getParentFile().exists()) {
                  outputFile.getParentFile().mkdirs();
               }

               FileOutputStream w = new FileOutputStream(outputFile);
               IOUtil.copy((InputStream)is, (OutputStream)w);
               IOUtil.close(is);
               IOUtil.close((OutputStream)w);
            }
         }
      }

      if (resourcesDirectory != null && resourcesDirectory.exists()) {
         this.copyDirectory(resourcesDirectory, outputDirectory);
      }

      File siteCssFile = new File(outputDirectory, "/css/site.css");
      if (!siteCssFile.exists()) {
         File cssDirectory = new File(outputDirectory, "/css/");
         boolean created = cssDirectory.mkdirs();
         if (created && this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("The directory '" + cssDirectory.getAbsolutePath() + "' did not exist. It was created.");
         }

         if (this.getLogger().isDebugEnabled()) {
            this.getLogger().debug("The file '" + siteCssFile.getAbsolutePath() + "' does not exists. Creating an empty file.");
         }

         FileWriter w = new FileWriter(siteCssFile);
         w.write("/* You can override this file with your own styles */");
         IOUtil.close((Writer)w);
      }

   }

   private void copyFileFromZip(ZipFile file, ZipEntry entry, File destFile) throws IOException {
      FileOutputStream fos = new FileOutputStream(destFile);

      try {
         IOUtil.copy((InputStream)file.getInputStream(entry), (OutputStream)fos);
      } finally {
         IOUtil.close((OutputStream)fos);
      }

   }

   protected void copyDirectory(File source, File destination) throws IOException {
      if (source.exists()) {
         DirectoryScanner scanner = new DirectoryScanner();
         String[] includedResources = new String[]{"**/**"};
         scanner.setIncludes(includedResources);
         scanner.addDefaultExcludes();
         scanner.setBasedir(source);
         scanner.scan();
         List includedFiles = Arrays.asList(scanner.getIncludedFiles());
         Iterator j = includedFiles.iterator();

         while(j.hasNext()) {
            String name = (String)j.next();
            File sourceFile = new File(source, name);
            File destinationFile = new File(destination, name);
            FileUtils.copyFile(sourceFile, destinationFile);
         }
      }

   }
}
