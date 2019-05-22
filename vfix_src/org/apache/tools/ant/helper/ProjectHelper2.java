package org.apache.tools.ant.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.RuntimeConfigurable;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ProjectHelper2 extends ProjectHelper {
   private static ProjectHelper2.AntHandler elementHandler = new ProjectHelper2.ElementHandler();
   private static ProjectHelper2.AntHandler targetHandler = new ProjectHelper2.TargetHandler();
   private static ProjectHelper2.AntHandler mainHandler = new ProjectHelper2.MainHandler();
   private static ProjectHelper2.AntHandler projectHandler = new ProjectHelper2.ProjectHandler();
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

   public UnknownElement parseUnknownElement(Project project, URL source) throws BuildException {
      Target dummyTarget = new Target();
      dummyTarget.setProject(project);
      AntXMLContext context = new AntXMLContext(project);
      context.addTarget(dummyTarget);
      context.setImplicitTarget(dummyTarget);
      this.parse(context.getProject(), source, new ProjectHelper2.RootHandler(context, elementHandler));
      Task[] tasks = dummyTarget.getTasks();
      if (tasks.length != 1) {
         throw new BuildException("No tasks defined");
      } else {
         return (UnknownElement)tasks[0];
      }
   }

   public void parse(Project project, Object source) throws BuildException {
      this.getImportStack().addElement(source);
      AntXMLContext context = null;
      context = (AntXMLContext)project.getReference("ant.parsing.context");
      if (context == null) {
         context = new AntXMLContext(project);
         project.addReference("ant.parsing.context", context);
         project.addReference("ant.targets", context.getTargets());
      }

      if (this.getImportStack().size() > 1) {
         context.setIgnoreProjectTag(true);
         Target currentTarget = context.getCurrentTarget();
         Target currentImplicit = context.getImplicitTarget();
         Map currentTargets = context.getCurrentTargets();

         try {
            Target newCurrent = new Target();
            newCurrent.setProject(project);
            newCurrent.setName("");
            context.setCurrentTarget(newCurrent);
            context.setCurrentTargets(new HashMap());
            context.setImplicitTarget(newCurrent);
            this.parse(project, source, new ProjectHelper2.RootHandler(context, mainHandler));
            newCurrent.execute();
         } finally {
            context.setCurrentTarget(currentTarget);
            context.setImplicitTarget(currentImplicit);
            context.setCurrentTargets(currentTargets);
         }
      } else {
         context.setCurrentTargets(new HashMap());
         this.parse(project, source, new ProjectHelper2.RootHandler(context, mainHandler));
         context.getImplicitTarget().execute();
      }

   }

   public void parse(Project project, Object source, ProjectHelper2.RootHandler handler) throws BuildException {
      AntXMLContext context = handler.context;
      File buildFile = null;
      URL url = null;
      String buildFileName = null;
      if (source instanceof File) {
         buildFile = (File)source;
         buildFile = FILE_UTILS.normalize(buildFile.getAbsolutePath());
         context.setBuildFile(buildFile);
         buildFileName = buildFile.toString();
      } else {
         if (!(source instanceof URL)) {
            throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
         }

         url = (URL)source;
         buildFileName = url.toString();
      }

      InputStream inputStream = null;
      InputSource inputSource = null;

      try {
         Object t;
         try {
            XMLReader parser = JAXPUtils.getNamespaceXMLReader();
            t = null;
            String uri;
            if (buildFile != null) {
               uri = FILE_UTILS.toURI(buildFile.getAbsolutePath());
               inputStream = new FileInputStream(buildFile);
            } else {
               inputStream = url.openStream();
               uri = url.toString();
            }

            inputSource = new InputSource((InputStream)inputStream);
            if (uri != null) {
               inputSource.setSystemId(uri);
            }

            project.log("parsing buildfile " + buildFileName + " with URI = " + uri, 3);
            parser.setContentHandler(handler);
            parser.setEntityResolver(handler);
            parser.setErrorHandler(handler);
            parser.setDTDHandler(handler);
            parser.parse(inputSource);
         } catch (SAXParseException var21) {
            Location location = new Location(var21.getSystemId(), var21.getLineNumber(), var21.getColumnNumber());
            Throwable t = var21.getException();
            if (t instanceof BuildException) {
               BuildException be = (BuildException)t;
               if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                  be.setLocation(location);
               }

               throw be;
            }

            if (t == null) {
               t = var21;
            }

            throw new BuildException(var21.getMessage(), (Throwable)t, location);
         } catch (SAXException var22) {
            t = var22.getException();
            if (t instanceof BuildException) {
               throw (BuildException)t;
            }

            if (t == null) {
               t = var22;
            }

            throw new BuildException(var22.getMessage(), (Throwable)t);
         } catch (FileNotFoundException var23) {
            throw new BuildException(var23);
         } catch (UnsupportedEncodingException var24) {
            throw new BuildException("Encoding of project file " + buildFileName + " is invalid.", var24);
         } catch (IOException var25) {
            throw new BuildException("Error reading project file " + buildFileName + ": " + var25.getMessage(), var25);
         }
      } finally {
         FileUtils.close((InputStream)inputStream);
      }

   }

   protected static ProjectHelper2.AntHandler getMainHandler() {
      return mainHandler;
   }

   protected static void setMainHandler(ProjectHelper2.AntHandler handler) {
      mainHandler = handler;
   }

   protected static ProjectHelper2.AntHandler getProjectHandler() {
      return projectHandler;
   }

   protected static void setProjectHandler(ProjectHelper2.AntHandler handler) {
      projectHandler = handler;
   }

   protected static ProjectHelper2.AntHandler getTargetHandler() {
      return targetHandler;
   }

   protected static void setTargetHandler(ProjectHelper2.AntHandler handler) {
      targetHandler = handler;
   }

   protected static ProjectHelper2.AntHandler getElementHandler() {
      return elementHandler;
   }

   protected static void setElementHandler(ProjectHelper2.AntHandler handler) {
      elementHandler = handler;
   }

   public static class ElementHandler extends ProjectHelper2.AntHandler {
      public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         RuntimeConfigurable parentWrapper = context.currentWrapper();
         Object parent = null;
         if (parentWrapper != null) {
            parent = parentWrapper.getProxy();
         }

         UnknownElement task = new UnknownElement(tag);
         task.setProject(context.getProject());
         task.setNamespace(uri);
         task.setQName(qname);
         task.setTaskType(ProjectHelper.genComponentName(task.getNamespace(), tag));
         task.setTaskName(qname);
         Location location = new Location(context.getLocator().getSystemId(), context.getLocator().getLineNumber(), context.getLocator().getColumnNumber());
         task.setLocation(location);
         task.setOwningTarget(context.getCurrentTarget());
         if (parent != null) {
            ((UnknownElement)parent).addChild(task);
         } else {
            context.getCurrentTarget().addTask(task);
         }

         context.configureId(task, attrs);
         RuntimeConfigurable wrapper = new RuntimeConfigurable(task, task.getTaskName());

         for(int i = 0; i < attrs.getLength(); ++i) {
            String name = attrs.getLocalName(i);
            String attrUri = attrs.getURI(i);
            if (attrUri != null && !attrUri.equals("") && !attrUri.equals(uri)) {
               name = attrUri + ":" + attrs.getQName(i);
            }

            String value = attrs.getValue(i);
            if ("ant-type".equals(name) || "antlib:org.apache.tools.ant".equals(attrUri) && "ant-type".equals(attrs.getLocalName(i))) {
               name = "ant-type";
               int index = value.indexOf(":");
               if (index != -1) {
                  String prefix = value.substring(0, index);
                  String mappedUri = context.getPrefixMapping(prefix);
                  if (mappedUri == null) {
                     throw new BuildException("Unable to find XML NS prefix " + prefix);
                  }

                  value = ProjectHelper.genComponentName(mappedUri, value.substring(index + 1));
               }
            }

            wrapper.setAttribute(name, value);
         }

         if (parentWrapper != null) {
            parentWrapper.addChild(wrapper);
         }

         context.pushWrapper(wrapper);
      }

      public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
         RuntimeConfigurable wrapper = context.currentWrapper();
         wrapper.addText(buf, start, count);
      }

      public ProjectHelper2.AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         return ProjectHelper2.elementHandler;
      }

      public void onEndElement(String uri, String tag, AntXMLContext context) {
         context.popWrapper();
      }
   }

   public static class TargetHandler extends ProjectHelper2.AntHandler {
      public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         String name = null;
         String depends = "";
         Project project = context.getProject();
         Target target = new Target();
         target.setProject(project);
         target.setLocation(new Location(context.getLocator()));
         context.addTarget(target);

         String key;
         for(int i = 0; i < attrs.getLength(); ++i) {
            String attrUri = attrs.getURI(i);
            if (attrUri == null || attrUri.equals("") || attrUri.equals(uri)) {
               key = attrs.getLocalName(i);
               String value = attrs.getValue(i);
               if (key.equals("name")) {
                  name = value;
                  if ("".equals(value)) {
                     throw new BuildException("name attribute must not be empty");
                  }
               } else if (key.equals("depends")) {
                  depends = value;
               } else if (key.equals("if")) {
                  target.setIf(value);
               } else if (key.equals("unless")) {
                  target.setUnless(value);
               } else if (key.equals("id")) {
                  if (value != null && !value.equals("")) {
                     context.getProject().addReference(value, target);
                  }
               } else {
                  if (!key.equals("description")) {
                     throw new SAXParseException("Unexpected attribute \"" + key + "\"", context.getLocator());
                  }

                  target.setDescription(value);
               }
            }
         }

         if (name == null) {
            throw new SAXParseException("target element appears without a name attribute", context.getLocator());
         } else if (context.getCurrentTargets().get(name) != null) {
            throw new BuildException("Duplicate target '" + name + "'", target.getLocation());
         } else {
            Hashtable projectTargets = project.getTargets();
            boolean usedTarget = false;
            if (projectTargets.containsKey(name)) {
               project.log("Already defined in main or a previous import, ignore " + name, 3);
            } else {
               target.setName(name);
               context.getCurrentTargets().put(name, target);
               project.addOrReplaceTarget(name, target);
               usedTarget = true;
            }

            if (depends.length() > 0) {
               target.setDepends(depends);
            }

            if (context.isIgnoringProjectTag() && context.getCurrentProjectName() != null && context.getCurrentProjectName().length() != 0) {
               key = context.getCurrentProjectName() + "." + name;
               Target newTarget = usedTarget ? new Target(target) : target;
               newTarget.setName(key);
               context.getCurrentTargets().put(key, newTarget);
               project.addOrReplaceTarget(key, newTarget);
            }

         }
      }

      public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         return ProjectHelper2.elementHandler;
      }

      public void onEndElement(String uri, String tag, AntXMLContext context) {
         context.setCurrentTarget(context.getImplicitTarget());
      }
   }

   public static class ProjectHandler extends ProjectHelper2.AntHandler {
      public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         String baseDir = null;
         boolean nameAttributeSet = false;
         Project project = context.getProject();
         context.getImplicitTarget().setLocation(new Location(context.getLocator()));

         String dup;
         for(int i = 0; i < attrs.getLength(); ++i) {
            dup = attrs.getURI(i);
            if (dup == null || dup.equals("") || dup.equals(uri)) {
               String key = attrs.getLocalName(i);
               String value = attrs.getValue(i);
               if (key.equals("default")) {
                  if (value != null && !value.equals("") && !context.isIgnoringProjectTag()) {
                     project.setDefault(value);
                  }
               } else if (key.equals("name")) {
                  if (value != null) {
                     context.setCurrentProjectName(value);
                     nameAttributeSet = true;
                     if (!context.isIgnoringProjectTag()) {
                        project.setName(value);
                        project.addReference(value, project);
                     }
                  }
               } else if (key.equals("id")) {
                  if (value != null && !context.isIgnoringProjectTag()) {
                     project.addReference(value, project);
                  }
               } else {
                  if (!key.equals("basedir")) {
                     throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context.getLocator());
                  }

                  if (!context.isIgnoringProjectTag()) {
                     baseDir = value;
                  }
               }
            }
         }

         String antFileProp = "ant.file." + context.getCurrentProjectName();
         dup = project.getProperty(antFileProp);
         if (dup != null && nameAttributeSet) {
            File dupFile = new File(dup);
            if (context.isIgnoringProjectTag() && !dupFile.equals(context.getBuildFile())) {
               project.log("Duplicated project name in import. Project " + context.getCurrentProjectName() + " defined first in " + dup + " and again in " + context.getBuildFile(), 1);
            }
         }

         if (context.getBuildFile() != null && nameAttributeSet) {
            project.setUserProperty("ant.file." + context.getCurrentProjectName(), context.getBuildFile().toString());
         }

         if (!context.isIgnoringProjectTag()) {
            if (project.getProperty("basedir") != null) {
               project.setBasedir(project.getProperty("basedir"));
            } else if (baseDir == null) {
               project.setBasedir(context.getBuildFileParent().getAbsolutePath());
            } else if ((new File(baseDir)).isAbsolute()) {
               project.setBasedir(baseDir);
            } else {
               project.setBaseDir(ProjectHelper2.FILE_UTILS.resolveFile(context.getBuildFileParent(), baseDir));
            }

            project.addTarget("", context.getImplicitTarget());
            context.setCurrentTarget(context.getImplicitTarget());
         }
      }

      public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         return !name.equals("target") || !uri.equals("") && !uri.equals("antlib:org.apache.tools.ant") ? ProjectHelper2.elementHandler : ProjectHelper2.targetHandler;
      }
   }

   public static class MainHandler extends ProjectHelper2.AntHandler {
      public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         if (!name.equals("project") || !uri.equals("") && !uri.equals("antlib:org.apache.tools.ant")) {
            if (name.equals(qname)) {
               throw new SAXParseException("Unexpected element \"{" + uri + "}" + name + "\" {" + "antlib:org.apache.tools.ant" + "}" + name, context.getLocator());
            } else {
               throw new SAXParseException("Unexpected element \"" + qname + "\" " + name, context.getLocator());
            }
         } else {
            return ProjectHelper2.projectHandler;
         }
      }
   }

   public static class RootHandler extends DefaultHandler {
      private Stack antHandlers = new Stack();
      private ProjectHelper2.AntHandler currentHandler = null;
      private AntXMLContext context;

      public RootHandler(AntXMLContext context, ProjectHelper2.AntHandler rootHandler) {
         this.currentHandler = rootHandler;
         this.antHandlers.push(this.currentHandler);
         this.context = context;
      }

      public ProjectHelper2.AntHandler getCurrentAntHandler() {
         return this.currentHandler;
      }

      public InputSource resolveEntity(String publicId, String systemId) {
         this.context.getProject().log("resolving systemId: " + systemId, 3);
         if (systemId.startsWith("file:")) {
            String path = ProjectHelper2.FILE_UTILS.fromURI(systemId);
            File file = new File(path);
            if (!file.isAbsolute()) {
               file = ProjectHelper2.FILE_UTILS.resolveFile(this.context.getBuildFileParent(), path);
               this.context.getProject().log("Warning: '" + systemId + "' in " + this.context.getBuildFile() + " should be expressed simply as '" + path.replace('\\', '/') + "' for compliance with other XML tools", 1);
            }

            this.context.getProject().log("file=" + file, 4);

            try {
               InputSource inputSource = new InputSource(new FileInputStream(file));
               inputSource.setSystemId(ProjectHelper2.FILE_UTILS.toURI(file.getAbsolutePath()));
               return inputSource;
            } catch (FileNotFoundException var6) {
               this.context.getProject().log(file.getAbsolutePath() + " could not be found", 1);
            }
         }

         this.context.getProject().log("could not resolve systemId", 4);
         return null;
      }

      public void startElement(String uri, String tag, String qname, Attributes attrs) throws SAXParseException {
         ProjectHelper2.AntHandler next = this.currentHandler.onStartChild(uri, tag, qname, attrs, this.context);
         this.antHandlers.push(this.currentHandler);
         this.currentHandler = next;
         this.currentHandler.onStartElement(uri, tag, qname, attrs, this.context);
      }

      public void setDocumentLocator(Locator locator) {
         this.context.setLocator(locator);
      }

      public void endElement(String uri, String name, String qName) throws SAXException {
         this.currentHandler.onEndElement(uri, name, this.context);
         ProjectHelper2.AntHandler prev = (ProjectHelper2.AntHandler)this.antHandlers.pop();
         this.currentHandler = prev;
         if (this.currentHandler != null) {
            this.currentHandler.onEndChild(uri, name, qName, this.context);
         }

      }

      public void characters(char[] buf, int start, int count) throws SAXParseException {
         this.currentHandler.characters(buf, start, count, this.context);
      }

      public void startPrefixMapping(String prefix, String uri) {
         this.context.startPrefixMapping(prefix, uri);
      }

      public void endPrefixMapping(String prefix) {
         this.context.endPrefixMapping(prefix);
      }
   }

   public static class AntHandler {
      public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
      }

      public ProjectHelper2.AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
         throw new SAXParseException("Unexpected element \"" + qname + " \"", context.getLocator());
      }

      public void onEndChild(String uri, String tag, String qname, AntXMLContext context) throws SAXParseException {
      }

      public void onEndElement(String uri, String tag, AntXMLContext context) {
      }

      public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
         String s = (new String(buf, start, count)).trim();
         if (s.length() > 0) {
            throw new SAXParseException("Unexpected text \"" + s + "\"", context.getLocator());
         }
      }

      protected void checkNamespace(String uri) {
      }
   }
}
