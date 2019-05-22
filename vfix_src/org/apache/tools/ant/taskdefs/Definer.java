package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.FileUtils;

public abstract class Definer extends DefBase {
   private static final String ANTLIB_XML = "/antlib.xml";
   private static Definer.ResourceStack resourceStack = new Definer.ResourceStack();
   private String name;
   private String classname;
   private File file;
   private String resource;
   private int format = 0;
   private boolean definerSet = false;
   private int onError = 0;
   private String adapter;
   private String adaptTo;
   private Class adapterClass;
   private Class adaptToClass;

   public void setOnError(Definer.OnError onError) {
      this.onError = onError.getIndex();
   }

   public void setFormat(Definer.Format format) {
      this.format = format.getIndex();
   }

   public String getName() {
      return this.name;
   }

   public File getFile() {
      return this.file;
   }

   public String getResource() {
      return this.resource;
   }

   public void execute() throws BuildException {
      ClassLoader al = this.createLoader();
      String urls;
      if (!this.definerSet) {
         if (this.getURI() == null) {
            throw new BuildException("name, file or resource attribute of " + this.getTaskName() + " is undefined", this.getLocation());
         }

         if (!this.getURI().startsWith("antlib:")) {
            throw new BuildException("Only antlib URIs can be located from the URI alone,not the URI " + this.getURI());
         }

         urls = this.getURI();
         this.setResource(makeResourceFromURI(urls));
      }

      if (this.name != null) {
         if (this.classname == null) {
            throw new BuildException("classname attribute of " + this.getTaskName() + " element " + "is undefined", this.getLocation());
         }

         this.addDefinition(al, this.name, this.classname);
      } else {
         if (this.classname != null) {
            urls = "You must not specify classname together with file or resource.";
            throw new BuildException(urls, this.getLocation());
         }

         urls = null;
         final URL url;
         Enumeration urls;
         if (this.file != null) {
            url = this.fileToURL();
            if (url == null) {
               return;
            }

            urls = new Enumeration() {
               private boolean more = true;

               public boolean hasMoreElements() {
                  return this.more;
               }

               public Object nextElement() throws NoSuchElementException {
                  if (this.more) {
                     this.more = false;
                     return url;
                  } else {
                     throw new NoSuchElementException();
                  }
               }
            };
         } else {
            urls = this.resourceToURLs(al);
         }

         while(urls.hasMoreElements()) {
            url = (URL)urls.nextElement();
            int fmt = this.format;
            if (url.toString().toLowerCase(Locale.US).endsWith(".xml")) {
               fmt = 1;
            }

            if (fmt == 0) {
               this.loadProperties(al, url);
               break;
            }

            if (resourceStack.getStack().get(url) != null) {
               this.log("Warning: Recursive loading of " + url + " ignored" + " at " + this.getLocation() + " originally loaded at " + resourceStack.getStack().get(url), 1);
            } else {
               try {
                  resourceStack.getStack().put(url, this.getLocation());
                  this.loadAntlib(al, url);
               } finally {
                  resourceStack.getStack().remove(url);
               }
            }
         }
      }

   }

   public static String makeResourceFromURI(String uri) {
      String path = uri.substring("antlib:".length());
      String resource;
      if (path.startsWith("//")) {
         resource = path.substring("//".length());
         if (!resource.endsWith(".xml")) {
            resource = resource + "/antlib.xml";
         }
      } else {
         resource = path.replace('.', '/') + "/antlib.xml";
      }

      return resource;
   }

   private URL fileToURL() {
      String message = null;
      if (!this.file.exists()) {
         message = "File " + this.file + " does not exist";
      }

      if (message == null && !this.file.isFile()) {
         message = "File " + this.file + " is not a file";
      }

      try {
         if (message == null) {
            return this.file.toURL();
         }
      } catch (Exception var3) {
         message = "File " + this.file + " cannot use as URL: " + var3.toString();
      }

      switch(this.onError) {
      case 0:
      case 1:
         this.log(message, 1);
         break;
      case 2:
         this.log(message, 3);
         break;
      case 3:
         throw new BuildException(message);
      }

      return null;
   }

   private Enumeration resourceToURLs(ClassLoader classLoader) {
      Enumeration ret;
      try {
         ret = classLoader.getResources(this.resource);
      } catch (IOException var4) {
         throw new BuildException("Could not fetch resources named " + this.resource, var4, this.getLocation());
      }

      if (!ret.hasMoreElements()) {
         String message = "Could not load definitions from resource " + this.resource + ". It could not be found.";
         switch(this.onError) {
         case 0:
         case 1:
            this.log(message, 1);
            break;
         case 2:
            this.log(message, 3);
            break;
         case 3:
            throw new BuildException(message);
         }
      }

      return ret;
   }

   protected void loadProperties(ClassLoader al, URL url) {
      InputStream is = null;

      try {
         is = url.openStream();
         if (is != null) {
            Properties props = new Properties();
            props.load(is);
            Enumeration keys = props.keys();

            while(keys.hasMoreElements()) {
               this.name = (String)keys.nextElement();
               this.classname = props.getProperty(this.name);
               this.addDefinition(al, this.name, this.classname);
            }

            return;
         }

         this.log("Could not load definitions from " + url, 1);
      } catch (IOException var9) {
         throw new BuildException(var9, this.getLocation());
      } finally {
         FileUtils.close(is);
      }

   }

   private void loadAntlib(ClassLoader classLoader, URL url) {
      try {
         Antlib antlib = Antlib.createAntlib(this.getProject(), url, this.getURI());
         antlib.setClassLoader(classLoader);
         antlib.setURI(this.getURI());
         antlib.execute();
      } catch (BuildException var4) {
         throw ProjectHelper.addLocationToBuildException(var4, this.getLocation());
      }
   }

   public void setFile(File file) {
      if (this.definerSet) {
         this.tooManyDefinitions();
      }

      this.definerSet = true;
      this.file = file;
   }

   public void setResource(String res) {
      if (this.definerSet) {
         this.tooManyDefinitions();
      }

      this.definerSet = true;
      this.resource = res;
   }

   public void setAntlib(String antlib) {
      if (this.definerSet) {
         this.tooManyDefinitions();
      }

      if (!antlib.startsWith("antlib:")) {
         throw new BuildException("Invalid antlib attribute - it must start with antlib:");
      } else {
         this.setURI(antlib);
         this.resource = antlib.substring("antlib:".length()).replace('.', '/') + "/antlib.xml";
         this.definerSet = true;
      }
   }

   public void setName(String name) {
      if (this.definerSet) {
         this.tooManyDefinitions();
      }

      this.definerSet = true;
      this.name = name;
   }

   public String getClassname() {
      return this.classname;
   }

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public void setAdapter(String adapter) {
      this.adapter = adapter;
   }

   protected void setAdapterClass(Class adapterClass) {
      this.adapterClass = adapterClass;
   }

   public void setAdaptTo(String adaptTo) {
      this.adaptTo = adaptTo;
   }

   protected void setAdaptToClass(Class adaptToClass) {
      this.adaptToClass = adaptToClass;
   }

   protected void addDefinition(ClassLoader al, String name, String classname) throws BuildException {
      Class cl = null;

      try {
         String msg;
         try {
            name = ProjectHelper.genComponentName(this.getURI(), name);
            if (this.onError != 2) {
               cl = Class.forName(classname, true, al);
            }

            if (this.adapter != null) {
               this.adapterClass = Class.forName(this.adapter, true, al);
            }

            if (this.adaptTo != null) {
               this.adaptToClass = Class.forName(this.adaptTo, true, al);
            }

            AntTypeDefinition def = new AntTypeDefinition();
            def.setName(name);
            def.setClassName(classname);
            def.setClass(cl);
            def.setAdapterClass(this.adapterClass);
            def.setAdaptToClass(this.adaptToClass);
            def.setClassLoader(al);
            if (cl != null) {
               def.checkClass(this.getProject());
            }

            ComponentHelper.getComponentHelper(this.getProject()).addDataTypeDefinition(def);
         } catch (ClassNotFoundException var7) {
            msg = this.getTaskName() + " class " + classname + " cannot be found";
            throw new BuildException(msg, var7, this.getLocation());
         } catch (NoClassDefFoundError var8) {
            msg = this.getTaskName() + " A class needed by class " + classname + " cannot be found: " + var8.getMessage();
            throw new BuildException(msg, var8, this.getLocation());
         }
      } catch (BuildException var9) {
         switch(this.onError) {
         case 0:
         case 3:
            throw var9;
         case 1:
            this.log(var9.getLocation() + "Warning: " + var9.getMessage(), 1);
            break;
         case 2:
         default:
            this.log(var9.getLocation() + var9.getMessage(), 4);
         }
      }

   }

   private void tooManyDefinitions() {
      throw new BuildException("Only one of the attributes name, file and resource can be set", this.getLocation());
   }

   public static class Format extends EnumeratedAttribute {
      public static final int PROPERTIES = 0;
      public static final int XML = 1;

      public String[] getValues() {
         return new String[]{"properties", "xml"};
      }
   }

   public static class OnError extends EnumeratedAttribute {
      public static final int FAIL = 0;
      public static final int REPORT = 1;
      public static final int IGNORE = 2;
      public static final int FAIL_ALL = 3;
      public static final String POLICY_FAIL = "fail";
      public static final String POLICY_REPORT = "report";
      public static final String POLICY_IGNORE = "ignore";
      public static final String POLICY_FAILALL = "failall";

      public OnError() {
      }

      public OnError(String value) {
         this.setValue(value);
      }

      public String[] getValues() {
         return new String[]{"fail", "report", "ignore", "failall"};
      }
   }

   private static class ResourceStack extends ThreadLocal {
      private ResourceStack() {
      }

      public Object initialValue() {
         return new HashMap();
      }

      Map getStack() {
         return (Map)this.get();
      }

      // $FF: synthetic method
      ResourceStack(Object x0) {
         this();
      }
   }
}
