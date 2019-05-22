package org.apache.tools.ant.taskdefs;

import java.io.File;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public class Available extends Task implements Condition {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private String property;
   private String classname;
   private String filename;
   private File file;
   private Path filepath;
   private String resource;
   private Available.FileDir type;
   private Path classpath;
   private AntClassLoader loader;
   private String value = "true";
   private boolean isTask = false;
   private boolean ignoreSystemclasses = false;
   private boolean searchParents = false;

   public void setSearchParents(boolean searchParents) {
      this.searchParents = searchParents;
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

   public void setFilepath(Path filepath) {
      this.createFilepath().append(filepath);
   }

   public Path createFilepath() {
      if (this.filepath == null) {
         this.filepath = new Path(this.getProject());
      }

      return this.filepath.createPath();
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void setClassname(String classname) {
      if (!"".equals(classname)) {
         this.classname = classname;
      }

   }

   public void setFile(File file) {
      this.file = file;
      this.filename = FILE_UTILS.removeLeadingPath(this.getProject().getBaseDir(), file);
   }

   public void setResource(String resource) {
      this.resource = resource;
   }

   /** @deprecated */
   public void setType(String type) {
      this.log("DEPRECATED - The setType(String) method has been deprecated. Use setType(Available.FileDir) instead.", 1);
      this.type = new Available.FileDir();
      this.type.setValue(type);
   }

   public void setType(Available.FileDir type) {
      this.type = type;
   }

   public void setIgnoresystemclasses(boolean ignore) {
      this.ignoreSystemclasses = ignore;
   }

   public void execute() throws BuildException {
      if (this.property == null) {
         throw new BuildException("property attribute is required", this.getLocation());
      } else {
         this.isTask = true;

         try {
            if (this.eval()) {
               String oldvalue = this.getProject().getProperty(this.property);
               if (null != oldvalue && !oldvalue.equals(this.value)) {
                  this.log("DEPRECATED - <available> used to override an existing property." + StringUtils.LINE_SEP + "  Build file should not reuse the same property" + " name for different values.", 1);
               }

               this.getProject().setProperty(this.property, this.value);
            }
         } finally {
            this.isTask = false;
         }

      }
   }

   public boolean eval() throws BuildException {
      boolean var3;
      try {
         if (this.classname == null && this.file == null && this.resource == null) {
            throw new BuildException("At least one of (classname|file|resource) is required", this.getLocation());
         }

         if (this.type != null && this.file == null) {
            throw new BuildException("The type attribute is only valid when specifying the file attribute.", this.getLocation());
         }

         if (this.classpath != null) {
            this.classpath.setProject(this.getProject());
            this.loader = this.getProject().createClassLoader(this.classpath);
         }

         String appendix = "";
         if (this.isTask) {
            appendix = " to set property " + this.property;
         } else {
            this.setTaskName("available");
         }

         boolean var7;
         if (this.classname != null && !this.checkClass(this.classname)) {
            this.log("Unable to load class " + this.classname + appendix, 3);
            var7 = false;
            return var7;
         }

         if (this.file == null || this.checkFile()) {
            if (this.resource == null || this.checkResource(this.resource)) {
               return true;
            }

            this.log("Unable to load resource " + this.resource + appendix, 3);
            var7 = false;
            return var7;
         }

         StringBuffer buf = new StringBuffer("Unable to find ");
         if (this.type != null) {
            buf.append(this.type).append(' ');
         }

         buf.append(this.filename).append(appendix);
         this.log(buf.toString(), 3);
         var3 = false;
      } finally {
         if (this.loader != null) {
            this.loader.cleanup();
            this.loader = null;
         }

         if (!this.isTask) {
            this.setTaskName((String)null);
         }

      }

      return var3;
   }

   private boolean checkFile() {
      if (this.filepath == null) {
         return this.checkFile(this.file, this.filename);
      } else {
         String[] paths = this.filepath.list();

         for(int i = 0; i < paths.length; ++i) {
            this.log("Searching " + paths[i], 4);
            File path = new File(paths[i]);
            if (path.exists() && this.filename.equals(paths[i])) {
               if (this.type == null) {
                  this.log("Found: " + path, 3);
                  return true;
               }

               if (this.type.isDir() && path.isDirectory()) {
                  this.log("Found directory: " + path, 3);
                  return true;
               }

               if (this.type.isFile() && path.isFile()) {
                  this.log("Found file: " + path, 3);
                  return true;
               }

               return false;
            }

            File parent = path.getParentFile();
            if (parent != null && parent.exists() && this.filename.equals(parent.getAbsolutePath())) {
               if (this.type == null) {
                  this.log("Found: " + parent, 3);
                  return true;
               }

               if (this.type.isDir()) {
                  this.log("Found directory: " + parent, 3);
                  return true;
               }

               return false;
            }

            if (path.exists() && path.isDirectory() && this.checkFile(new File(path, this.filename), this.filename + " in " + path)) {
               return true;
            }

            while(this.searchParents && parent != null && parent.exists()) {
               if (this.checkFile(new File(parent, this.filename), this.filename + " in " + parent)) {
                  return true;
               }

               parent = parent.getParentFile();
            }
         }

         return false;
      }
   }

   private boolean checkFile(File f, String text) {
      if (this.type != null) {
         if (this.type.isDir()) {
            if (f.isDirectory()) {
               this.log("Found directory: " + text, 3);
            }

            return f.isDirectory();
         }

         if (this.type.isFile()) {
            if (f.isFile()) {
               this.log("Found file: " + text, 3);
            }

            return f.isFile();
         }
      }

      if (f.exists()) {
         this.log("Found: " + text, 3);
      }

      return f.exists();
   }

   private boolean checkResource(String resource) {
      if (this.loader != null) {
         return this.loader.getResourceAsStream(resource) != null;
      } else {
         ClassLoader cL = this.getClass().getClassLoader();
         if (cL != null) {
            return cL.getResourceAsStream(resource) != null;
         } else {
            return ClassLoader.getSystemResourceAsStream(resource) != null;
         }
      }
   }

   private boolean checkClass(String classname) {
      try {
         if (this.ignoreSystemclasses) {
            this.loader = this.getProject().createClassLoader(this.classpath);
            this.loader.setParentFirst(false);
            this.loader.addJavaLibraries();
            if (this.loader == null) {
               return false;
            }

            try {
               this.loader.findClass(classname);
            } catch (SecurityException var3) {
               return true;
            }
         } else if (this.loader != null) {
            this.loader.loadClass(classname);
         } else {
            ClassLoader l = this.getClass().getClassLoader();
            if (l != null) {
               Class.forName(classname, true, l);
            } else {
               Class.forName(classname);
            }
         }

         return true;
      } catch (ClassNotFoundException var4) {
         this.log("class \"" + classname + "\" was not found", 4);
         return false;
      } catch (NoClassDefFoundError var5) {
         this.log("Could not load dependent class \"" + var5.getMessage() + "\" for class \"" + classname + "\"", 4);
         return false;
      }
   }

   public static class FileDir extends EnumeratedAttribute {
      private static final String[] VALUES = new String[]{"file", "dir"};

      public String[] getValues() {
         return VALUES;
      }

      public boolean isDir() {
         return "dir".equalsIgnoreCase(this.getValue());
      }

      public boolean isFile() {
         return "file".equalsIgnoreCase(this.getValue());
      }
   }
}
