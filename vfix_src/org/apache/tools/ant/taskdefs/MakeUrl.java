package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.FileUtils;

public class MakeUrl extends Task {
   private String property;
   private File file;
   private String separator = " ";
   private List filesets = new LinkedList();
   private List paths = new LinkedList();
   private boolean validate = true;
   public static final String ERROR_MISSING_FILE = "A source file is missing :";
   public static final String ERROR_NO_PROPERTY = "No property defined";
   public static final String ERROR_NO_FILES = "No files defined";

   public void setProperty(String property) {
      this.property = property;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public void addFileSet(FileSet fileset) {
      this.filesets.add(fileset);
   }

   public void setSeparator(String separator) {
      this.separator = separator;
   }

   public void setValidate(boolean validate) {
      this.validate = validate;
   }

   public void addPath(Path path) {
      this.paths.add(path);
   }

   private String filesetsToURL() {
      if (this.filesets.isEmpty()) {
         return "";
      } else {
         int count = 0;
         StringBuffer urls = new StringBuffer();
         ListIterator list = this.filesets.listIterator();

         while(list.hasNext()) {
            FileSet set = (FileSet)list.next();
            DirectoryScanner scanner = set.getDirectoryScanner(this.getProject());
            String[] files = scanner.getIncludedFiles();

            for(int i = 0; i < files.length; ++i) {
               File f = new File(scanner.getBasedir(), files[i]);
               this.validateFile(f);
               String asUrl = this.toURL(f);
               urls.append(asUrl);
               this.log(asUrl, 4);
               urls.append(this.separator);
               ++count;
            }
         }

         return this.stripTrailingSeparator(urls, count);
      }
   }

   private String stripTrailingSeparator(StringBuffer urls, int count) {
      if (count > 0) {
         urls.delete(urls.length() - this.separator.length(), urls.length());
         return new String(urls);
      } else {
         return "";
      }
   }

   private String pathsToURL() {
      if (this.paths.isEmpty()) {
         return "";
      } else {
         int count = 0;
         StringBuffer urls = new StringBuffer();
         ListIterator list = this.paths.listIterator();

         while(list.hasNext()) {
            Path path = (Path)list.next();
            String[] elements = path.list();

            for(int i = 0; i < elements.length; ++i) {
               File f = new File(elements[i]);
               this.validateFile(f);
               String asUrl = this.toURL(f);
               urls.append(asUrl);
               this.log(asUrl, 4);
               urls.append(this.separator);
               ++count;
            }
         }

         return this.stripTrailingSeparator(urls, count);
      }
   }

   private void validateFile(File fileToCheck) {
      if (this.validate && !fileToCheck.exists()) {
         throw new BuildException("A source file is missing :" + fileToCheck.toString());
      }
   }

   public void execute() throws BuildException {
      this.validate();
      if (this.getProject().getProperty(this.property) == null) {
         String filesetURL = this.filesetsToURL();
         String url;
         if (this.file != null) {
            this.validateFile(this.file);
            url = this.toURL(this.file);
            if (filesetURL.length() > 0) {
               url = url + this.separator + filesetURL;
            }
         } else {
            url = filesetURL;
         }

         String pathURL = this.pathsToURL();
         if (pathURL.length() > 0) {
            if (url.length() > 0) {
               url = url + this.separator + pathURL;
            } else {
               url = pathURL;
            }
         }

         this.log("Setting " + this.property + " to URL " + url, 3);
         this.getProject().setNewProperty(this.property, url);
      }
   }

   private void validate() {
      if (this.property == null) {
         throw new BuildException("No property defined");
      } else if (this.file == null && this.filesets.isEmpty() && this.paths.isEmpty()) {
         throw new BuildException("No files defined");
      }
   }

   private String toURL(File fileToConvert) {
      String url = FileUtils.getFileUtils().toURI(fileToConvert.getAbsolutePath());
      return url;
   }
}
