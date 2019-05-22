package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.DynamicConfigurator;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.XMLCatalog;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

public class XSLTProcess extends MatchingTask implements XSLTLogger {
   private File destDir = null;
   private File baseDir = null;
   private String xslFile = null;
   private Resource xslResource = null;
   private String targetExtension = ".html";
   private String fileNameParameter = null;
   private String fileDirParameter = null;
   private Vector params = new Vector();
   private File inFile = null;
   private File outFile = null;
   private String processor;
   private Path classpath = null;
   private XSLTLiaison liaison;
   private boolean stylesheetLoaded = false;
   private boolean force = false;
   private Vector outputProperties = new Vector();
   private XMLCatalog xmlCatalog = new XMLCatalog();
   private static final String TRAX_LIAISON_CLASS = "org.apache.tools.ant.taskdefs.optional.TraXLiaison";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private boolean performDirectoryScan = true;
   private XSLTProcess.Factory factory = null;
   private boolean reuseLoadedStylesheet = true;
   private AntClassLoader loader = null;
   private Mapper mapperElement = null;
   private Union resources = new Union();
   private boolean useImplicitFileset = true;
   public static final String PROCESSOR_TRAX = "trax";

   public void setScanIncludedDirectories(boolean b) {
      this.performDirectoryScan = b;
   }

   public void setReloadStylesheet(boolean b) {
      this.reuseLoadedStylesheet = !b;
   }

   public void addMapper(Mapper mapper) {
      if (this.mapperElement != null) {
         throw new BuildException("Cannot define more than one mapper", this.getLocation());
      } else {
         this.mapperElement = mapper;
      }
   }

   public void add(ResourceCollection rc) {
      this.resources.add(rc);
   }

   public void addConfiguredStyle(Resources rc) {
      if (rc.size() != 1) {
         throw new BuildException("The style element must be specified with exactly one nested resource.");
      } else {
         this.setXslResource((Resource)rc.iterator().next());
      }
   }

   public void setXslResource(Resource xslResource) {
      this.xslResource = xslResource;
   }

   public void add(FileNameMapper fileNameMapper) throws BuildException {
      Mapper mapper = new Mapper(this.getProject());
      mapper.add(fileNameMapper);
      this.addMapper(mapper);
   }

   public void execute() throws BuildException {
      if ("style".equals(this.getTaskType())) {
         this.log("Warning: the task name <style> is deprecated. Use <xslt> instead.", 1);
      }

      File savedBaseDir = this.baseDir;
      if (this.xslResource == null && this.xslFile == null) {
         throw new BuildException("specify the stylesheet either as a filename in style attribute or as a nested resource", this.getLocation());
      } else if (this.xslResource != null && this.xslFile != null) {
         throw new BuildException("specify the stylesheet either as a filename in style attribute or as a nested resource but not as both", this.getLocation());
      } else if (this.inFile != null && !this.inFile.exists()) {
         throw new BuildException("input file " + this.inFile.toString() + " does not exist", this.getLocation());
      } else {
         try {
            if (this.baseDir == null) {
               this.baseDir = this.getProject().resolveFile(".");
            }

            this.liaison = this.getLiaison();
            if (this.liaison instanceof XSLTLoggerAware) {
               ((XSLTLoggerAware)this.liaison).setLogger(this);
            }

            this.log("Using " + this.liaison.getClass().toString(), 3);
            if (this.xslFile != null) {
               File stylesheet = this.getProject().resolveFile(this.xslFile);
               if (!stylesheet.exists()) {
                  stylesheet = FILE_UTILS.resolveFile(this.baseDir, this.xslFile);
                  if (stylesheet.exists()) {
                     this.log("DEPRECATED - the 'style' attribute should be relative to the project's");
                     this.log("             basedir, not the tasks's basedir.");
                  }
               }

               FileResource fr = new FileResource();
               fr.setProject(this.getProject());
               fr.setFile(stylesheet);
               this.xslResource = fr;
            }

            if (this.inFile != null && this.outFile != null) {
               this.process(this.inFile, this.outFile, this.xslResource);
               return;
            }

            this.checkDest();
            if (!this.useImplicitFileset) {
               if (this.resources.size() == 0) {
                  throw new BuildException("no resources specified");
               }
            } else {
               DirectoryScanner scanner = this.getDirectoryScanner(this.baseDir);
               this.log("Transforming into " + this.destDir, 2);
               String[] list = scanner.getIncludedFiles();

               int j;
               for(j = 0; j < list.length; ++j) {
                  this.process(this.baseDir, list[j], this.destDir, this.xslResource);
               }

               if (this.performDirectoryScan) {
                  String[] dirs = scanner.getIncludedDirectories();

                  for(j = 0; j < dirs.length; ++j) {
                     list = (new File(this.baseDir, dirs[j])).list();

                     for(int i = 0; i < list.length; ++i) {
                        this.process(this.baseDir, dirs[j] + File.separator + list[i], this.destDir, this.xslResource);
                     }
                  }
               }
            }

            this.processResources(this.xslResource);
         } finally {
            if (this.loader != null) {
               this.loader.resetThreadContextLoader();
               this.loader.cleanup();
               this.loader = null;
            }

            this.liaison = null;
            this.stylesheetLoaded = false;
            this.baseDir = savedBaseDir;
         }

      }
   }

   public void setForce(boolean force) {
      this.force = force;
   }

   public void setBasedir(File dir) {
      this.baseDir = dir;
   }

   public void setDestdir(File dir) {
      this.destDir = dir;
   }

   public void setExtension(String name) {
      this.targetExtension = name;
   }

   public void setStyle(String xslFile) {
      this.xslFile = xslFile;
   }

   public void setClasspath(Path classpath) {
      this.createClasspath().append(classpath);
   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void setProcessor(String processor) {
      this.processor = processor;
   }

   public void setUseImplicitFileset(boolean useimplicitfileset) {
      this.useImplicitFileset = useimplicitfileset;
   }

   public void addConfiguredXMLCatalog(XMLCatalog xmlCatalog) {
      this.xmlCatalog.addConfiguredXMLCatalog(xmlCatalog);
   }

   public void setFileNameParameter(String fileNameParameter) {
      this.fileNameParameter = fileNameParameter;
   }

   public void setFileDirParameter(String fileDirParameter) {
      this.fileDirParameter = fileDirParameter;
   }

   private void resolveProcessor(String proc) throws Exception {
      String classname;
      if (proc.equals("trax")) {
         classname = "org.apache.tools.ant.taskdefs.optional.TraXLiaison";
      } else {
         classname = proc;
      }

      Class clazz = this.loadClass(classname);
      this.liaison = (XSLTLiaison)clazz.newInstance();
   }

   private Class loadClass(String classname) throws Exception {
      if (this.classpath == null) {
         return Class.forName(classname);
      } else {
         this.loader = this.getProject().createClassLoader(this.classpath);
         this.loader.setThreadContextLoader();
         Class c = Class.forName(classname, true, this.loader);
         return c;
      }
   }

   public void setOut(File outFile) {
      this.outFile = outFile;
   }

   public void setIn(File inFile) {
      this.inFile = inFile;
   }

   private void checkDest() {
      if (this.destDir == null) {
         String msg = "destdir attributes must be set!";
         throw new BuildException(msg);
      }
   }

   private void processResources(Resource stylesheet) {
      Iterator iter = this.resources.iterator();

      while(iter.hasNext()) {
         Resource r = (Resource)iter.next();
         if (r.isExists()) {
            File base = this.baseDir;
            String name = r.getName();
            if (r instanceof FileResource) {
               FileResource f = (FileResource)r;
               base = f.getBaseDir();
               if (base == null) {
                  name = f.getFile().getAbsolutePath();
               }
            }

            this.process(base, name, this.destDir, stylesheet);
         }
      }

   }

   private void process(File baseDir, String xmlFile, File destDir, Resource stylesheet) throws BuildException {
      File outF = null;
      File inF = null;

      try {
         long styleSheetLastModified = stylesheet.getLastModified();
         inF = new File(baseDir, xmlFile);
         if (inF.isDirectory()) {
            this.log("Skipping " + inF + " it is a directory.", 3);
         } else {
            FileNameMapper mapper = null;
            if (this.mapperElement != null) {
               mapper = this.mapperElement.getImplementation();
            } else {
               mapper = new XSLTProcess.StyleMapper();
            }

            String[] outFileName = ((FileNameMapper)mapper).mapFileName(xmlFile);
            if (outFileName != null && outFileName.length != 0) {
               if (outFileName != null && outFileName.length <= 1) {
                  outF = new File(destDir, outFileName[0]);
                  if (this.force || inF.lastModified() > outF.lastModified() || styleSheetLastModified > outF.lastModified()) {
                     this.ensureDirectoryFor(outF);
                     this.log("Processing " + inF + " to " + outF);
                     this.configureLiaison(stylesheet);
                     this.setLiaisonDynamicFileParameters(this.liaison, inF);
                     this.liaison.transform(inF, outF);
                  }

               } else {
                  this.log("Skipping " + this.inFile + " its mapping is ambiguos.", 3);
               }
            } else {
               this.log("Skipping " + this.inFile + " it cannot get mapped to output.", 3);
            }
         }
      } catch (Exception var12) {
         this.log("Failed to process " + this.inFile, 2);
         if (outF != null) {
            outF.delete();
         }

         throw new BuildException(var12);
      }
   }

   private void process(File inFile, File outFile, Resource stylesheet) throws BuildException {
      try {
         long styleSheetLastModified = stylesheet.getLastModified();
         this.log("In file " + inFile + " time: " + inFile.lastModified(), 4);
         this.log("Out file " + outFile + " time: " + outFile.lastModified(), 4);
         this.log("Style file " + this.xslFile + " time: " + styleSheetLastModified, 4);
         if (!this.force && inFile.lastModified() < outFile.lastModified() && styleSheetLastModified < outFile.lastModified()) {
            this.log("Skipping input file " + inFile + " because it is older than output file " + outFile + " and so is the stylesheet " + stylesheet, 4);
         } else {
            this.ensureDirectoryFor(outFile);
            this.log("Processing " + inFile + " to " + outFile, 2);
            this.configureLiaison(stylesheet);
            this.setLiaisonDynamicFileParameters(this.liaison, inFile);
            this.liaison.transform(inFile, outFile);
         }

      } catch (Exception var7) {
         this.log("Failed to process " + inFile, 2);
         if (outFile != null) {
            outFile.delete();
         }

         throw new BuildException(var7);
      }
   }

   private void ensureDirectoryFor(File targetFile) throws BuildException {
      File directory = targetFile.getParentFile();
      if (!directory.exists() && !directory.mkdirs()) {
         throw new BuildException("Unable to create directory: " + directory.getAbsolutePath());
      }
   }

   public XSLTProcess.Factory getFactory() {
      return this.factory;
   }

   public XMLCatalog getXMLCatalog() {
      this.xmlCatalog.setProject(this.getProject());
      return this.xmlCatalog;
   }

   public Enumeration getOutputProperties() {
      return this.outputProperties.elements();
   }

   protected XSLTLiaison getLiaison() {
      if (this.liaison == null) {
         if (this.processor != null) {
            try {
               this.resolveProcessor(this.processor);
            } catch (Exception var3) {
               throw new BuildException(var3);
            }
         } else {
            try {
               this.resolveProcessor("trax");
            } catch (Throwable var2) {
               var2.printStackTrace();
               throw new BuildException(var2);
            }
         }
      }

      return this.liaison;
   }

   public XSLTProcess.Param createParam() {
      XSLTProcess.Param p = new XSLTProcess.Param();
      this.params.addElement(p);
      return p;
   }

   public XSLTProcess.OutputProperty createOutputProperty() {
      XSLTProcess.OutputProperty p = new XSLTProcess.OutputProperty();
      this.outputProperties.addElement(p);
      return p;
   }

   public void init() throws BuildException {
      super.init();
      this.xmlCatalog.setProject(this.getProject());
   }

   /** @deprecated */
   protected void configureLiaison(File stylesheet) throws BuildException {
      FileResource fr = new FileResource();
      fr.setProject(this.getProject());
      fr.setFile(stylesheet);
      this.configureLiaison((Resource)fr);
   }

   protected void configureLiaison(Resource stylesheet) throws BuildException {
      if (!this.stylesheetLoaded || !this.reuseLoadedStylesheet) {
         this.stylesheetLoaded = true;

         try {
            this.log("Loading stylesheet " + stylesheet, 2);
            if (this.liaison instanceof XSLTLiaison2) {
               ((XSLTLiaison2)this.liaison).configure(this);
            }

            if (this.liaison instanceof XSLTLiaison3) {
               ((XSLTLiaison3)this.liaison).setStylesheet(stylesheet);
            } else {
               if (!(stylesheet instanceof FileResource)) {
                  throw new BuildException(this.liaison.getClass().toString() + " accepts the stylesheet only as a file", this.getLocation());
               }

               this.liaison.setStylesheet(((FileResource)stylesheet).getFile());
            }

            Enumeration e = this.params.elements();

            while(e.hasMoreElements()) {
               XSLTProcess.Param p = (XSLTProcess.Param)e.nextElement();
               if (p.shouldUse()) {
                  this.liaison.addParam(p.getName(), p.getExpression());
               }
            }

         } catch (Exception var4) {
            this.log("Failed to transform using stylesheet " + stylesheet, 2);
            throw new BuildException(var4);
         }
      }
   }

   private void setLiaisonDynamicFileParameters(XSLTLiaison liaison, File inFile) throws Exception {
      if (this.fileNameParameter != null) {
         liaison.addParam(this.fileNameParameter, inFile.getName());
      }

      if (this.fileDirParameter != null) {
         String fileName = FileUtils.getRelativePath(this.baseDir, inFile);
         File file = new File(fileName);
         liaison.addParam(this.fileDirParameter, file.getParent() != null ? file.getParent().replace('\\', '/') : ".");
      }

   }

   public XSLTProcess.Factory createFactory() throws BuildException {
      if (this.factory != null) {
         throw new BuildException("'factory' element must be unique");
      } else {
         this.factory = new XSLTProcess.Factory();
         return this.factory;
      }
   }

   private class StyleMapper implements FileNameMapper {
      private StyleMapper() {
      }

      public void setFrom(String from) {
      }

      public void setTo(String to) {
      }

      public String[] mapFileName(String xmlFile) {
         int dotPos = xmlFile.lastIndexOf(46);
         if (dotPos > 0) {
            xmlFile = xmlFile.substring(0, dotPos);
         }

         return new String[]{xmlFile + XSLTProcess.this.targetExtension};
      }

      // $FF: synthetic method
      StyleMapper(Object x1) {
         this();
      }
   }

   public static class Factory {
      private String name;
      private Vector attributes = new Vector();

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void addAttribute(XSLTProcess.Factory.Attribute attr) {
         this.attributes.addElement(attr);
      }

      public Enumeration getAttributes() {
         return this.attributes.elements();
      }

      public static class Attribute implements DynamicConfigurator {
         private String name;
         private Object value;

         public String getName() {
            return this.name;
         }

         public Object getValue() {
            return this.value;
         }

         public Object createDynamicElement(String name) throws BuildException {
            return null;
         }

         public void setDynamicAttribute(String name, String value) throws BuildException {
            if ("name".equalsIgnoreCase(name)) {
               this.name = value;
            } else {
               if (!"value".equalsIgnoreCase(name)) {
                  throw new BuildException("Unsupported attribute: " + name);
               }

               if ("true".equalsIgnoreCase(value)) {
                  this.value = Boolean.TRUE;
               } else if ("false".equalsIgnoreCase(value)) {
                  this.value = Boolean.FALSE;
               } else {
                  try {
                     this.value = new Integer(value);
                  } catch (NumberFormatException var4) {
                     this.value = value;
                  }
               }
            }

         }
      }
   }

   public static class OutputProperty {
      private String name;
      private String value;

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }
   }

   public static class Param {
      private String name = null;
      private String expression = null;
      private String ifProperty;
      private String unlessProperty;
      private Project project;

      public void setProject(Project project) {
         this.project = project;
      }

      public void setName(String name) {
         this.name = name;
      }

      public void setExpression(String expression) {
         this.expression = expression;
      }

      public String getName() throws BuildException {
         if (this.name == null) {
            throw new BuildException("Name attribute is missing.");
         } else {
            return this.name;
         }
      }

      public String getExpression() throws BuildException {
         if (this.expression == null) {
            throw new BuildException("Expression attribute is missing.");
         } else {
            return this.expression;
         }
      }

      public void setIf(String ifProperty) {
         this.ifProperty = ifProperty;
      }

      public void setUnless(String unlessProperty) {
         this.unlessProperty = unlessProperty;
      }

      public boolean shouldUse() {
         if (this.ifProperty != null && this.project.getProperty(this.ifProperty) == null) {
            return false;
         } else {
            return this.unlessProperty == null || this.project.getProperty(this.unlessProperty) == null;
         }
      }
   }
}
