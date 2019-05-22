package org.apache.maven.scm;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;

public class ScmFileSet implements Serializable {
   private static final long serialVersionUID = -5978597349974797556L;
   private static final String DELIMITER = ",";
   private static final String DEFAULT_EXCLUDES;
   private final File basedir;
   private String includes;
   private String excludes;
   private final List<File> files;

   public ScmFileSet(File basedir) {
      this(basedir, (List)(new ArrayList(0)));
   }

   public ScmFileSet(File basedir, File file) {
      this(basedir, new File[]{file});
   }

   public ScmFileSet(File basedir, String includes, String excludes) throws IOException {
      this.basedir = basedir;
      if (excludes != null && excludes.length() > 0) {
         excludes = excludes + "," + DEFAULT_EXCLUDES;
      } else {
         excludes = DEFAULT_EXCLUDES;
      }

      List<File> fileList = FileUtils.getFiles(basedir, includes, excludes, false);
      this.files = fileList;
      this.includes = includes;
      this.excludes = excludes;
   }

   public ScmFileSet(File basedir, String includes) throws IOException {
      this(basedir, includes, (String)null);
   }

   /** @deprecated */
   public ScmFileSet(File basedir, File[] files) {
      this(basedir, Arrays.asList(files));
   }

   public ScmFileSet(File basedir, List<File> files) {
      if (basedir == null) {
         throw new NullPointerException("basedir must not be null");
      } else if (files == null) {
         throw new NullPointerException("files must not be null");
      } else {
         this.basedir = basedir;
         this.files = files;
      }
   }

   public File getBasedir() {
      return this.basedir;
   }

   /** @deprecated */
   public File[] getFiles() {
      return (File[])this.files.toArray(new File[this.files.size()]);
   }

   public List<File> getFileList() {
      return this.files;
   }

   public String getIncludes() {
      return this.includes;
   }

   public String getExcludes() {
      return this.excludes;
   }

   public String toString() {
      return "basedir = " + this.basedir + "; files = " + this.files;
   }

   static {
      DEFAULT_EXCLUDES = StringUtils.join((Object[])DirectoryScanner.DEFAULTEXCLUDES, ",");
   }
}
