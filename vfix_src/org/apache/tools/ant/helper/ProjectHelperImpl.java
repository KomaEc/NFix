package org.apache.tools.ant.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.TypeAdapter;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.AttributeList;
import org.xml.sax.DocumentHandler;
import org.xml.sax.HandlerBase;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLReaderAdapter;

public class ProjectHelperImpl extends ProjectHelper {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private Parser parser;
   private Project project;
   private File buildFile;
   private File buildFileParent;
   private Locator locator;
   private Target implicitTarget = new Target();

   public ProjectHelperImpl() {
      this.implicitTarget.setName("");
   }

   public void parse(Project project, Object source) throws BuildException {
      if (!(source instanceof File)) {
         throw new BuildException("Only File source supported by default plugin");
      } else {
         File bFile = (File)source;
         FileInputStream inputStream = null;
         InputSource inputSource = null;
         this.project = project;
         this.buildFile = new File(bFile.getAbsolutePath());
         this.buildFileParent = new File(this.buildFile.getParent());

         try {
            try {
               this.parser = JAXPUtils.getParser();
            } catch (BuildException var18) {
               this.parser = new XMLReaderAdapter(JAXPUtils.getXMLReader());
            }

            String uri = FILE_UTILS.toURI(bFile.getAbsolutePath());
            inputStream = new FileInputStream(bFile);
            inputSource = new InputSource(inputStream);
            inputSource.setSystemId(uri);
            project.log("parsing buildfile " + bFile + " with URI = " + uri, 3);
            HandlerBase hb = new ProjectHelperImpl.RootHandler(this);
            this.parser.setDocumentHandler(hb);
            this.parser.setEntityResolver(hb);
            this.parser.setErrorHandler(hb);
            this.parser.setDTDHandler(hb);
            this.parser.parse(inputSource);
         } catch (SAXParseException var19) {
            Location location = new Location(var19.getSystemId(), var19.getLineNumber(), var19.getColumnNumber());
            Throwable t = var19.getException();
            if (t instanceof BuildException) {
               BuildException be = (BuildException)t;
               if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                  be.setLocation(location);
               }

               throw be;
            }

            throw new BuildException(var19.getMessage(), t, location);
         } catch (SAXException var20) {
            Throwable t = var20.getException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            }

            throw new BuildException(var20.getMessage(), t);
         } catch (FileNotFoundException var21) {
            throw new BuildException(var21);
         } catch (UnsupportedEncodingException var22) {
            throw new BuildException("Encoding of project file is invalid.", var22);
         } catch (IOException var23) {
            throw new BuildException("Error reading project file: " + var23.getMessage(), var23);
         } finally {
            FileUtils.close((InputStream)inputStream);
         }

      }
   }

   private static void handleElement(ProjectHelperImpl helperImpl, DocumentHandler parent, Target target, String elementName, AttributeList attrs) throws SAXParseException {
      if (elementName.equals("description")) {
         new ProjectHelperImpl.DescriptionHandler(helperImpl, parent);
      } else if (helperImpl.project.getDataTypeDefinitions().get(elementName) != null) {
         (new ProjectHelperImpl.DataTypeHandler(helperImpl, parent, target)).init(elementName, attrs);
      } else {
         (new ProjectHelperImpl.TaskHandler(helperImpl, parent, target, (RuntimeConfigurable)null, target)).init(elementName, attrs);
      }

   }

   private void configureId(Object target, AttributeList attr) {
      String id = attr.getValue("id");
      if (id != null) {
         this.project.addReference(id, target);
      }

   }

   static class DataTypeHandler extends ProjectHelperImpl.AbstractHandler {
      private Target target;
      private Object element;
      private RuntimeConfigurable wrapper = null;

      public DataTypeHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Target target) {
         super(helperImpl, parentHandler);
         this.target = target;
      }

      public void init(String propType, AttributeList attrs) throws SAXParseException {
         try {
            this.element = this.helperImpl.project.createDataType(propType);
            if (this.element == null) {
               throw new BuildException("Unknown data type " + propType);
            } else {
               this.wrapper = new RuntimeConfigurable(this.element, propType);
               this.wrapper.setAttributes(attrs);
               this.target.addDataType(this.wrapper);
            }
         } catch (BuildException var4) {
            throw new SAXParseException(var4.getMessage(), this.helperImpl.locator, var4);
         }
      }

      public void characters(char[] buf, int start, int count) {
         this.wrapper.addText(buf, start, count);
      }

      public void startElement(String name, AttributeList attrs) throws SAXParseException {
         (new ProjectHelperImpl.NestedElementHandler(this.helperImpl, this, this.element, this.wrapper, this.target)).init(name, attrs);
      }
   }

   static class NestedElementHandler extends ProjectHelperImpl.AbstractHandler {
      private Object parent;
      private Object child;
      private RuntimeConfigurable parentWrapper;
      private RuntimeConfigurable childWrapper = null;
      private Target target;

      public NestedElementHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Object parent, RuntimeConfigurable parentWrapper, Target target) {
         super(helperImpl, parentHandler);
         if (parent instanceof TypeAdapter) {
            this.parent = ((TypeAdapter)parent).getProxy();
         } else {
            this.parent = parent;
         }

         this.parentWrapper = parentWrapper;
         this.target = target;
      }

      public void init(String propType, AttributeList attrs) throws SAXParseException {
         Class parentClass = this.parent.getClass();
         IntrospectionHelper ih = IntrospectionHelper.getHelper(this.helperImpl.project, parentClass);

         try {
            String elementName = propType.toLowerCase(Locale.US);
            if (this.parent instanceof UnknownElement) {
               UnknownElement uc = new UnknownElement(elementName);
               uc.setProject(this.helperImpl.project);
               ((UnknownElement)this.parent).addChild(uc);
               this.child = uc;
            } else {
               this.child = ih.createElement(this.helperImpl.project, this.parent, elementName);
            }

            this.helperImpl.configureId(this.child, attrs);
            this.childWrapper = new RuntimeConfigurable(this.child, propType);
            this.childWrapper.setAttributes(attrs);
            this.parentWrapper.addChild(this.childWrapper);
         } catch (BuildException var7) {
            throw new SAXParseException(var7.getMessage(), this.helperImpl.locator, var7);
         }
      }

      public void characters(char[] buf, int start, int count) {
         this.childWrapper.addText(buf, start, count);
      }

      public void startElement(String name, AttributeList attrs) throws SAXParseException {
         if (this.child instanceof TaskContainer) {
            (new ProjectHelperImpl.TaskHandler(this.helperImpl, this, (TaskContainer)this.child, this.childWrapper, this.target)).init(name, attrs);
         } else {
            (new ProjectHelperImpl.NestedElementHandler(this.helperImpl, this, this.child, this.childWrapper, this.target)).init(name, attrs);
         }

      }
   }

   static class TaskHandler extends ProjectHelperImpl.AbstractHandler {
      private Target target;
      private TaskContainer container;
      private Task task;
      private RuntimeConfigurable parentWrapper;
      private RuntimeConfigurable wrapper = null;

      public TaskHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, TaskContainer container, RuntimeConfigurable parentWrapper, Target target) {
         super(helperImpl, parentHandler);
         this.container = container;
         this.parentWrapper = parentWrapper;
         this.target = target;
      }

      public void init(String tag, AttributeList attrs) throws SAXParseException {
         try {
            this.task = this.helperImpl.project.createTask(tag);
         } catch (BuildException var4) {
         }

         if (this.task == null) {
            this.task = new UnknownElement(tag);
            this.task.setProject(this.helperImpl.project);
            this.task.setTaskName(tag);
         }

         this.task.setLocation(new Location(this.helperImpl.locator));
         this.helperImpl.configureId(this.task, attrs);
         this.task.setOwningTarget(this.target);
         this.container.addTask(this.task);
         this.task.init();
         this.wrapper = this.task.getRuntimeConfigurableWrapper();
         this.wrapper.setAttributes(attrs);
         if (this.parentWrapper != null) {
            this.parentWrapper.addChild(this.wrapper);
         }

      }

      public void characters(char[] buf, int start, int count) {
         this.wrapper.addText(buf, start, count);
      }

      public void startElement(String name, AttributeList attrs) throws SAXParseException {
         if (this.task instanceof TaskContainer) {
            (new ProjectHelperImpl.TaskHandler(this.helperImpl, this, (TaskContainer)this.task, this.wrapper, this.target)).init(name, attrs);
         } else {
            (new ProjectHelperImpl.NestedElementHandler(this.helperImpl, this, this.task, this.wrapper, this.target)).init(name, attrs);
         }

      }
   }

   static class DescriptionHandler extends ProjectHelperImpl.AbstractHandler {
      public DescriptionHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
         super(helperImpl, parentHandler);
      }

      public void characters(char[] buf, int start, int count) {
         String text = new String(buf, start, count);
         String currentDescription = this.helperImpl.project.getDescription();
         if (currentDescription == null) {
            this.helperImpl.project.setDescription(text);
         } else {
            this.helperImpl.project.setDescription(currentDescription + text);
         }

      }
   }

   static class TargetHandler extends ProjectHelperImpl.AbstractHandler {
      private Target target;

      public TargetHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
         super(helperImpl, parentHandler);
      }

      public void init(String tag, AttributeList attrs) throws SAXParseException {
         String name = null;
         String depends = "";
         String ifCond = null;
         String unlessCond = null;
         String id = null;
         String description = null;

         for(int i = 0; i < attrs.getLength(); ++i) {
            String key = attrs.getName(i);
            String value = attrs.getValue(i);
            if (key.equals("name")) {
               name = value;
               if (value.equals("")) {
                  throw new BuildException("name attribute must not be empty", new Location(this.helperImpl.locator));
               }
            } else if (key.equals("depends")) {
               depends = value;
            } else if (key.equals("if")) {
               ifCond = value;
            } else if (key.equals("unless")) {
               unlessCond = value;
            } else if (key.equals("id")) {
               id = value;
            } else {
               if (!key.equals("description")) {
                  throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl.locator);
               }

               description = value;
            }
         }

         if (name == null) {
            throw new SAXParseException("target element appears without a name attribute", this.helperImpl.locator);
         } else {
            this.target = new Target();
            this.target.addDependency("");
            this.target.setName(name);
            this.target.setIf(ifCond);
            this.target.setUnless(unlessCond);
            this.target.setDescription(description);
            this.helperImpl.project.addTarget(name, this.target);
            if (id != null && !id.equals("")) {
               this.helperImpl.project.addReference(id, this.target);
            }

            if (depends.length() > 0) {
               this.target.setDepends(depends);
            }

         }
      }

      public void startElement(String name, AttributeList attrs) throws SAXParseException {
         ProjectHelperImpl.handleElement(this.helperImpl, this, this.target, name, attrs);
      }
   }

   static class ProjectHandler extends ProjectHelperImpl.AbstractHandler {
      public ProjectHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
         super(helperImpl, parentHandler);
      }

      public void init(String tag, AttributeList attrs) throws SAXParseException {
         String def = null;
         String name = null;
         String id = null;
         String baseDir = null;

         for(int i = 0; i < attrs.getLength(); ++i) {
            String key = attrs.getName(i);
            String value = attrs.getValue(i);
            if (key.equals("default")) {
               def = value;
            } else if (key.equals("name")) {
               name = value;
            } else if (key.equals("id")) {
               id = value;
            } else {
               if (!key.equals("basedir")) {
                  throw new SAXParseException("Unexpected attribute \"" + attrs.getName(i) + "\"", this.helperImpl.locator);
               }

               baseDir = value;
            }
         }

         if (def != null && !def.equals("")) {
            this.helperImpl.project.setDefaultTarget(def);
            if (name != null) {
               this.helperImpl.project.setName(name);
               this.helperImpl.project.addReference(name, this.helperImpl.project);
            }

            if (id != null) {
               this.helperImpl.project.addReference(id, this.helperImpl.project);
            }

            if (this.helperImpl.project.getProperty("basedir") != null) {
               this.helperImpl.project.setBasedir(this.helperImpl.project.getProperty("basedir"));
            } else if (baseDir == null) {
               this.helperImpl.project.setBasedir(this.helperImpl.buildFileParent.getAbsolutePath());
            } else if ((new File(baseDir)).isAbsolute()) {
               this.helperImpl.project.setBasedir(baseDir);
            } else {
               File resolvedBaseDir = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, baseDir);
               this.helperImpl.project.setBaseDir(resolvedBaseDir);
            }

            this.helperImpl.project.addTarget("", this.helperImpl.implicitTarget);
         } else {
            throw new BuildException("The default attribute is required");
         }
      }

      public void startElement(String name, AttributeList attrs) throws SAXParseException {
         if (name.equals("target")) {
            this.handleTarget(name, attrs);
         } else {
            ProjectHelperImpl.handleElement(this.helperImpl, this, this.helperImpl.implicitTarget, name, attrs);
         }

      }

      private void handleTarget(String tag, AttributeList attrs) throws SAXParseException {
         (new ProjectHelperImpl.TargetHandler(this.helperImpl, this)).init(tag, attrs);
      }
   }

   static class RootHandler extends HandlerBase {
      ProjectHelperImpl helperImpl;

      public RootHandler(ProjectHelperImpl helperImpl) {
         this.helperImpl = helperImpl;
      }

      public InputSource resolveEntity(String publicId, String systemId) {
         this.helperImpl.project.log("resolving systemId: " + systemId, 3);
         if (systemId.startsWith("file:")) {
            String path = ProjectHelperImpl.FILE_UTILS.fromURI(systemId);
            File file = new File(path);
            if (!file.isAbsolute()) {
               file = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, path);
               this.helperImpl.project.log("Warning: '" + systemId + "' in " + this.helperImpl.buildFile + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
            }

            try {
               InputSource inputSource = new InputSource(new FileInputStream(file));
               inputSource.setSystemId(ProjectHelperImpl.FILE_UTILS.toURI(file.getAbsolutePath()));
               return inputSource;
            } catch (FileNotFoundException var6) {
               this.helperImpl.project.log(file.getAbsolutePath() + " could not be found", 1);
            }
         }

         return null;
      }

      public void startElement(String tag, AttributeList attrs) throws SAXParseException {
         if (tag.equals("project")) {
            (new ProjectHelperImpl.ProjectHandler(this.helperImpl, this)).init(tag, attrs);
         } else {
            throw new SAXParseException("Config file is not of expected XML type", this.helperImpl.locator);
         }
      }

      public void setDocumentLocator(Locator locator) {
         this.helperImpl.locator = locator;
      }
   }

   static class AbstractHandler extends HandlerBase {
      protected DocumentHandler parentHandler;
      ProjectHelperImpl helperImpl;

      public AbstractHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
         this.parentHandler = parentHandler;
         this.helperImpl = helperImpl;
         helperImpl.parser.setDocumentHandler(this);
      }

      public void startElement(String tag, AttributeList attrs) throws SAXParseException {
         throw new SAXParseException("Unexpected element \"" + tag + "\"", this.helperImpl.locator);
      }

      public void characters(char[] buf, int start, int count) throws SAXParseException {
         String s = (new String(buf, start, count)).trim();
         if (s.length() > 0) {
            throw new SAXParseException("Unexpected text \"" + s + "\"", this.helperImpl.locator);
         }
      }

      public void endElement(String name) throws SAXException {
         this.helperImpl.parser.setDocumentHandler(this.parentHandler);
      }
   }
}
