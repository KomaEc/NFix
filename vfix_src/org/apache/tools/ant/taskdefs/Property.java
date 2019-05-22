package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class Property extends Task {
   protected String name;
   protected String value;
   protected File file;
   protected URL url;
   protected String resource;
   protected Path classpath;
   protected String env;
   protected Reference ref;
   protected String prefix;
   private Project fallback;
   protected boolean userProperty;

   public Property() {
      this(false);
   }

   protected Property(boolean userProperty) {
      this(userProperty, (Project)null);
   }

   protected Property(boolean userProperty, Project fallback) {
      this.userProperty = userProperty;
      this.fallback = fallback;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setLocation(File location) {
      this.setValue(location.getAbsolutePath());
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public void setUrl(URL url) {
      this.url = url;
   }

   public URL getUrl() {
      return this.url;
   }

   public void setPrefix(String prefix) {
      this.prefix = prefix;
      if (!prefix.endsWith(".")) {
         this.prefix = this.prefix + ".";
      }

   }

   public String getPrefix() {
      return this.prefix;
   }

   public void setRefid(Reference ref) {
      this.ref = ref;
   }

   public Reference getRefid() {
      return this.ref;
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   public String getResource() {
      return this.resource;
   }

   public void setEnvironment(String env) {
      this.env = env;
   }

   public String getEnvironment() {
      return this.env;
   }

   public void setClasspath(Path classpath) {
      if (this.classpath == null) {
         this.classpath = classpath;
      } else {
         this.classpath.append(classpath);
      }

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

   public Path getClasspath() {
      return this.classpath;
   }

   /** @deprecated */
   public void setUserProperty(boolean userProperty) {
      this.log("DEPRECATED: Ignoring request to set user property in Property task.", 1);
   }

   public String toString() {
      return this.value == null ? "" : this.value;
   }

   public void execute() throws BuildException {
      if (this.getProject() == null) {
         throw new IllegalStateException("project has not been set");
      } else {
         if (this.name != null) {
            if (this.value == null && this.ref == null) {
               throw new BuildException("You must specify value, location or refid with the name attribute", this.getLocation());
            }
         } else if (this.url == null && this.file == null && this.resource == null && this.env == null) {
            throw new BuildException("You must specify url, file, resource or environment when not using the name attribute", this.getLocation());
         }

         if (this.url == null && this.file == null && this.resource == null && this.prefix != null) {
            throw new BuildException("Prefix is only valid when loading from a url, file or resource", this.getLocation());
         } else {
            if (this.name != null && this.value != null) {
               this.addProperty(this.name, this.value);
            }

            if (this.file != null) {
               this.loadFile(this.file);
            }

            if (this.url != null) {
               this.loadUrl(this.url);
            }

            if (this.resource != null) {
               this.loadResource(this.resource);
            }

            if (this.env != null) {
               this.loadEnvironment(this.env);
            }

            if (this.name != null && this.ref != null) {
               try {
                  this.addProperty(this.name, this.ref.getReferencedObject(this.getProject()).toString());
               } catch (BuildException var2) {
                  if (this.fallback == null) {
                     throw var2;
                  }

                  this.addProperty(this.name, this.ref.getReferencedObject(this.fallback).toString());
               }
            }

         }
      }
   }

   protected void loadUrl(URL url) throws BuildException {
      Properties props = new Properties();
      this.log("Loading " + url, 3);

      try {
         InputStream is = url.openStream();

         try {
            props.load(is);
         } finally {
            if (is != null) {
               is.close();
            }

         }

         this.addProperties(props);
      } catch (IOException var8) {
         throw new BuildException(var8, this.getLocation());
      }
   }

   protected void loadFile(File file) throws BuildException {
      Properties props = new Properties();
      this.log("Loading " + file.getAbsolutePath(), 3);

      try {
         if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);

            try {
               props.load(fis);
            } finally {
               if (fis != null) {
                  fis.close();
               }

            }

            this.addProperties(props);
         } else {
            this.log("Unable to find property file: " + file.getAbsolutePath(), 3);
         }

      } catch (IOException var8) {
         throw new BuildException(var8, this.getLocation());
      }
   }

   protected void loadResource(String name) {
      Properties props = new Properties();
      this.log("Resource Loading " + name, 3);
      InputStream is = null;

      try {
         ClassLoader cL = null;
         if (this.classpath != null) {
            cL = this.getProject().createClassLoader(this.classpath);
         } else {
            cL = this.getClass().getClassLoader();
         }

         if (cL == null) {
            is = ClassLoader.getSystemResourceAsStream(name);
         } else {
            is = ((ClassLoader)cL).getResourceAsStream(name);
         }

         if (is != null) {
            props.load(is);
            this.addProperties(props);
         } else {
            this.log("Unable to find resource " + name, 1);
         }
      } catch (IOException var12) {
         throw new BuildException(var12, this.getLocation());
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException var11) {
            }
         }

      }

   }

   protected void loadEnvironment(String prefix) {
      Properties props = new Properties();
      if (!prefix.endsWith(".")) {
         prefix = prefix + ".";
      }

      this.log("Loading Environment " + prefix, 3);
      Vector osEnv = Execute.getProcEnvironment();
      Enumeration e = osEnv.elements();

      while(e.hasMoreElements()) {
         String entry = (String)e.nextElement();
         int pos = entry.indexOf(61);
         if (pos == -1) {
            this.log("Ignoring: " + entry, 1);
         } else {
            props.put(prefix + entry.substring(0, pos), entry.substring(pos + 1));
         }
      }

      this.addProperties(props);
   }

   protected void addProperties(Properties props) {
      this.resolveAllProperties(props);

      String propertyName;
      String v;
      for(Enumeration e = props.keys(); e.hasMoreElements(); this.addProperty(propertyName, v)) {
         propertyName = (String)e.nextElement();
         String propertyValue = props.getProperty(propertyName);
         v = this.getProject().replaceProperties(propertyValue);
         if (this.prefix != null) {
            propertyName = this.prefix + propertyName;
         }
      }

   }

   protected void addProperty(String n, String v) {
      if (this.userProperty) {
         if (this.getProject().getUserProperty(n) == null) {
            this.getProject().setInheritedProperty(n, v);
         } else {
            this.log("Override ignored for " + n, 3);
         }
      } else {
         this.getProject().setNewProperty(n, v);
      }

   }

   private void resolveAllProperties(Properties props) throws BuildException {
      Enumeration e = props.keys();

      while(e.hasMoreElements()) {
         String propertyName = (String)e.nextElement();
         Stack referencesSeen = new Stack();
         this.resolve(props, propertyName, referencesSeen);
      }

   }

   private void resolve(Properties props, String name, Stack referencesSeen) throws BuildException {
      if (referencesSeen.contains(name)) {
         throw new BuildException("Property " + name + " was circularly " + "defined.");
      } else {
         String propertyValue = props.getProperty(name);
         Vector fragments = new Vector();
         Vector propertyRefs = new Vector();
         PropertyHelper.getPropertyHelper(this.getProject()).parsePropertyString(propertyValue, fragments, propertyRefs);
         if (propertyRefs.size() != 0) {
            referencesSeen.push(name);
            StringBuffer sb = new StringBuffer();
            Enumeration i = fragments.elements();

            String fragment;
            for(Enumeration j = propertyRefs.elements(); i.hasMoreElements(); sb.append(fragment)) {
               fragment = (String)i.nextElement();
               if (fragment == null) {
                  String propertyName = (String)j.nextElement();
                  fragment = this.getProject().getProperty(propertyName);
                  if (fragment == null) {
                     if (props.containsKey(propertyName)) {
                        this.resolve(props, propertyName, referencesSeen);
                        fragment = props.getProperty(propertyName);
                     } else {
                        fragment = "${" + propertyName + "}";
                     }
                  }
               }
            }

            propertyValue = sb.toString();
            props.put(name, propertyValue);
            referencesSeen.pop();
         }

      }
   }
}
