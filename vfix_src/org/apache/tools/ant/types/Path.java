package org.apache.tools.ant.types;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PathTokenizer;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResourceIterator;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public class Path extends DataType implements Cloneable, ResourceCollection {
   public static Path systemClasspath = new Path((Project)null, System.getProperty("java.class.path"));
   public static Path systemBootClasspath = new Path((Project)null, System.getProperty("sun.boot.class.path"));
   private static final Iterator EMPTY_ITERATOR;
   private Union union;

   public Path(Project p, String path) {
      this(p);
      this.createPathElement().setPath(path);
   }

   public Path(Project project) {
      this.union = null;
      this.setProject(project);
   }

   public void setLocation(File location) throws BuildException {
      this.checkAttributesAllowed();
      this.createPathElement().setLocation(location);
   }

   public void setPath(String path) throws BuildException {
      this.checkAttributesAllowed();
      this.createPathElement().setPath(path);
   }

   public void setRefid(Reference r) throws BuildException {
      if (this.union != null) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   public Path.PathElement createPathElement() throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         Path.PathElement pe = new Path.PathElement();
         this.add((ResourceCollection)pe);
         return pe;
      }
   }

   public void addFileset(FileSet fs) throws BuildException {
      if (fs.getProject() == null) {
         fs.setProject(this.getProject());
      }

      this.add((ResourceCollection)fs);
   }

   public void addFilelist(FileList fl) throws BuildException {
      if (fl.getProject() == null) {
         fl.setProject(this.getProject());
      }

      this.add((ResourceCollection)fl);
   }

   public void addDirset(DirSet dset) throws BuildException {
      if (dset.getProject() == null) {
         dset.setProject(this.getProject());
      }

      this.add((ResourceCollection)dset);
   }

   public void add(Path path) throws BuildException {
      if (path == this) {
         throw this.circularReference();
      } else {
         if (path.getProject() == null) {
            path.setProject(this.getProject());
         }

         this.add((ResourceCollection)path);
      }
   }

   public void add(ResourceCollection c) {
      this.checkChildrenAllowed();
      if (c != null) {
         if (this.union == null) {
            this.union = new Union();
            this.union.setProject(this.getProject());
            this.union.setCache(false);
         }

         this.union.add(c);
         this.setChecked(false);
      }
   }

   public Path createPath() throws BuildException {
      Path p = new Path(this.getProject());
      this.add(p);
      return p;
   }

   public void append(Path other) {
      if (other != null) {
         this.add(other);
      }
   }

   public void addExisting(Path source) {
      this.addExisting(source, false);
   }

   public void addExisting(Path source, boolean tryUserDir) {
      String[] list = source.list();
      File userDir = tryUserDir ? new File(System.getProperty("user.dir")) : null;

      for(int i = 0; i < list.length; ++i) {
         File f = resolveFile(this.getProject(), list[i]);
         if (tryUserDir && !f.exists()) {
            f = new File(userDir, list[i]);
         }

         if (f.exists()) {
            this.setLocation(f);
         } else {
            this.log("dropping " + f + " from path as it doesn't exist", 3);
         }
      }

   }

   public String[] list() {
      if (this.isReference()) {
         return ((Path)this.getCheckedRef()).list();
      } else {
         return this.assertFilesystemOnly(this.union) == null ? new String[0] : this.union.list();
      }
   }

   public String toString() {
      return this.isReference() ? this.getCheckedRef().toString() : (this.union == null ? "" : this.union.toString());
   }

   public static String[] translatePath(Project project, String source) {
      Vector result = new Vector();
      if (source == null) {
         return new String[0];
      } else {
         PathTokenizer tok = new PathTokenizer(source);

         for(StringBuffer element = new StringBuffer(); tok.hasMoreTokens(); element = new StringBuffer()) {
            String pathElement = tok.nextToken();

            try {
               element.append(resolveFile(project, pathElement).getPath());
            } catch (BuildException var7) {
               project.log("Dropping path element " + pathElement + " as it is not valid relative to the project", 3);
            }

            for(int i = 0; i < element.length(); ++i) {
               translateFileSep(element, i);
            }

            result.addElement(element.toString());
         }

         String[] res = new String[result.size()];
         result.copyInto(res);
         return res;
      }
   }

   public static String translateFile(String source) {
      if (source == null) {
         return "";
      } else {
         StringBuffer result = new StringBuffer(source);

         for(int i = 0; i < result.length(); ++i) {
            translateFileSep(result, i);
         }

         return result.toString();
      }
   }

   protected static boolean translateFileSep(StringBuffer buffer, int pos) {
      if (buffer.charAt(pos) != '/' && buffer.charAt(pos) != '\\') {
         return false;
      } else {
         buffer.setCharAt(pos, File.separatorChar);
         return true;
      }
   }

   public synchronized int size() {
      if (this.isReference()) {
         return ((Path)this.getCheckedRef()).size();
      } else {
         this.dieOnCircularReference();
         return this.union == null ? 0 : this.assertFilesystemOnly(this.union).size();
      }
   }

   public Object clone() {
      try {
         Path result = (Path)super.clone();
         result.union = this.union == null ? this.union : (Union)this.union.clone();
         return result;
      } catch (CloneNotSupportedException var2) {
         throw new BuildException(var2);
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            if (this.union != null) {
               stk.push(this.union);
               invokeCircularReferenceCheck(this.union, stk, p);
               stk.pop();
            }

            this.setChecked(true);
         }

      }
   }

   private static File resolveFile(Project project, String relativeName) {
      return FileUtils.getFileUtils().resolveFile(project == null ? null : project.getBaseDir(), relativeName);
   }

   public Path concatSystemClasspath() {
      return this.concatSystemClasspath("last");
   }

   public Path concatSystemClasspath(String defValue) {
      return this.concatSpecialPath(defValue, systemClasspath);
   }

   public Path concatSystemBootClasspath(String defValue) {
      return this.concatSpecialPath(defValue, systemBootClasspath);
   }

   private Path concatSpecialPath(String defValue, Path p) {
      Path result = new Path(this.getProject());
      String order = defValue;
      if (this.getProject() != null) {
         String o = this.getProject().getProperty("build.sysclasspath");
         if (o != null) {
            order = o;
         }
      }

      if (order.equals("only")) {
         result.addExisting(p, true);
      } else if (order.equals("first")) {
         result.addExisting(p, true);
         result.addExisting(this);
      } else if (order.equals("ignore")) {
         result.addExisting(this);
      } else {
         if (!order.equals("last")) {
            this.log("invalid value for build.sysclasspath: " + order, 1);
         }

         result.addExisting(this);
         result.addExisting(p, true);
      }

      return result;
   }

   public void addJavaRuntime() {
      if (JavaEnvUtils.isKaffe()) {
         File kaffeShare = new File(System.getProperty("java.home") + File.separator + "share" + File.separator + "kaffe");
         if (kaffeShare.isDirectory()) {
            FileSet kaffeJarFiles = new FileSet();
            kaffeJarFiles.setDir(kaffeShare);
            kaffeJarFiles.setIncludes("*.jar");
            this.addFileset(kaffeJarFiles);
         }
      } else if ("GNU libgcj".equals(System.getProperty("java.vm.name"))) {
         this.addExisting(systemBootClasspath);
      }

      if (System.getProperty("java.vendor").toLowerCase(Locale.US).indexOf("microsoft") >= 0) {
         FileSet msZipFiles = new FileSet();
         msZipFiles.setDir(new File(System.getProperty("java.home") + File.separator + "Packages"));
         msZipFiles.setIncludes("*.ZIP");
         this.addFileset(msZipFiles);
      } else {
         this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + "lib" + File.separator + "rt.jar"));
         this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + "jre" + File.separator + "lib" + File.separator + "rt.jar"));
         String[] secJars = new String[]{"jce", "jsse"};

         for(int i = 0; i < secJars.length; ++i) {
            this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + "lib" + File.separator + secJars[i] + ".jar"));
            this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + secJars[i] + ".jar"));
         }

         String[] ibmJars = new String[]{"core", "graphics", "security", "server", "xml"};

         for(int i = 0; i < ibmJars.length; ++i) {
            this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + "lib" + File.separator + ibmJars[i] + ".jar"));
         }

         this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + "classes.jar"));
         this.addExisting(new Path((Project)null, System.getProperty("java.home") + File.separator + ".." + File.separator + "Classes" + File.separator + "ui.jar"));
      }

   }

   public void addExtdirs(Path extdirs) {
      if (extdirs == null) {
         String extProp = System.getProperty("java.ext.dirs");
         if (extProp == null) {
            return;
         }

         extdirs = new Path(this.getProject(), extProp);
      }

      String[] dirs = extdirs.list();

      for(int i = 0; i < dirs.length; ++i) {
         File dir = resolveFile(this.getProject(), dirs[i]);
         if (dir.exists() && dir.isDirectory()) {
            FileSet fs = new FileSet();
            fs.setDir(dir);
            fs.setIncludes("*");
            this.addFileset(fs);
         }
      }

   }

   public final synchronized Iterator iterator() {
      if (this.isReference()) {
         return ((Path)this.getCheckedRef()).iterator();
      } else {
         this.dieOnCircularReference();
         return this.union == null ? EMPTY_ITERATOR : this.assertFilesystemOnly(this.union).iterator();
      }
   }

   public synchronized boolean isFilesystemOnly() {
      if (this.isReference()) {
         return ((Path)this.getCheckedRef()).isFilesystemOnly();
      } else {
         this.dieOnCircularReference();
         this.assertFilesystemOnly(this.union);
         return true;
      }
   }

   protected ResourceCollection assertFilesystemOnly(ResourceCollection rc) {
      if (rc != null && !rc.isFilesystemOnly()) {
         throw new BuildException(this.getDataTypeName() + " allows only filesystem resources.");
      } else {
         return rc;
      }
   }

   static {
      EMPTY_ITERATOR = Collections.EMPTY_SET.iterator();
   }

   public class PathElement implements ResourceCollection {
      private String[] parts;

      public void setLocation(File loc) {
         this.parts = new String[]{Path.translateFile(loc.getAbsolutePath())};
      }

      public void setPath(String path) {
         this.parts = Path.translatePath(Path.this.getProject(), path);
      }

      public String[] getParts() {
         return this.parts;
      }

      public Iterator iterator() {
         return new FileResourceIterator((File)null, this.parts);
      }

      public boolean isFilesystemOnly() {
         return true;
      }

      public int size() {
         return this.parts == null ? 0 : this.parts.length;
      }
   }
}
