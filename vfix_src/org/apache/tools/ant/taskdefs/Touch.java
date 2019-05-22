package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Touchable;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;

public class Touch extends Task {
   private static final Touch.DateFormatFactory DEFAULT_DF_FACTORY = new Touch.DateFormatFactory() {
      public DateFormat getPrimaryFormat() {
         return DateFormat.getDateTimeInstance(3, 3, Locale.US);
      }

      public DateFormat getFallbackFormat() {
         return DateFormat.getDateTimeInstance(3, 2, Locale.US);
      }
   };
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File file;
   private long millis = -1L;
   private String dateTime;
   private Vector filesets = new Vector();
   private Union resources = new Union();
   private boolean dateTimeConfigured;
   private boolean mkdirs;
   private boolean verbose = true;
   private FileNameMapper fileNameMapper = null;
   private Touch.DateFormatFactory dfFactory;

   public Touch() {
      this.dfFactory = DEFAULT_DF_FACTORY;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public void setMillis(long millis) {
      this.millis = millis;
   }

   public void setDatetime(String dateTime) {
      if (this.dateTime != null) {
         this.log("Resetting datetime attribute to " + dateTime, 3);
      }

      this.dateTime = dateTime;
      this.dateTimeConfigured = false;
   }

   public void setMkdirs(boolean mkdirs) {
      this.mkdirs = mkdirs;
   }

   public void setVerbose(boolean verbose) {
      this.verbose = verbose;
   }

   public void setPattern(final String pattern) {
      this.dfFactory = new Touch.DateFormatFactory() {
         public DateFormat getPrimaryFormat() {
            return new SimpleDateFormat(pattern);
         }

         public DateFormat getFallbackFormat() {
            return null;
         }
      };
   }

   public void addConfiguredMapper(Mapper mapper) {
      this.add(mapper.getImplementation());
   }

   public void add(FileNameMapper fileNameMapper) throws BuildException {
      if (this.fileNameMapper != null) {
         throw new BuildException("Only one mapper may be added to the " + this.getTaskName() + " task.");
      } else {
         this.fileNameMapper = fileNameMapper;
      }
   }

   public void addFileset(FileSet set) {
      this.filesets.add(set);
      this.add((ResourceCollection)set);
   }

   public void addFilelist(FileList list) {
      this.add((ResourceCollection)list);
   }

   public void add(ResourceCollection rc) {
      this.resources.add(rc);
   }

   protected synchronized void checkConfiguration() throws BuildException {
      if (this.file == null && this.resources.size() == 0) {
         throw new BuildException("Specify at least one source--a file or resource collection.");
      } else if (this.file != null && this.file.exists() && this.file.isDirectory()) {
         throw new BuildException("Use a resource collection to touch directories.");
      } else {
         if (this.dateTime != null && !this.dateTimeConfigured) {
            long workmillis = this.millis;
            DateFormat df = this.dfFactory.getPrimaryFormat();
            ParseException pe = null;

            try {
               workmillis = df.parse(this.dateTime).getTime();
            } catch (ParseException var8) {
               df = this.dfFactory.getFallbackFormat();
               if (df == null) {
                  pe = var8;
               } else {
                  try {
                     workmillis = df.parse(this.dateTime).getTime();
                  } catch (ParseException var7) {
                     pe = var7;
                  }
               }
            }

            if (pe != null) {
               throw new BuildException(pe.getMessage(), pe, this.getLocation());
            }

            if (workmillis < 0L) {
               throw new BuildException("Date of " + this.dateTime + " results in negative " + "milliseconds value " + "relative to epoch " + "(January 1, 1970, " + "00:00:00 GMT).");
            }

            this.log("Setting millis to " + workmillis + " from datetime attribute", this.millis < 0L ? 4 : 3);
            this.setMillis(workmillis);
            this.dateTimeConfigured = true;
         }

      }
   }

   public void execute() throws BuildException {
      this.checkConfiguration();
      this.touch();
   }

   protected void touch() throws BuildException {
      long defaultTimestamp = this.getTimestamp();
      if (this.file != null) {
         this.touch((Resource)(new FileResource(this.file.getParentFile(), this.file.getName())), defaultTimestamp);
      }

      Iterator iter = this.resources.iterator();

      while(iter.hasNext()) {
         Resource r = (Resource)iter.next();
         if (!(r instanceof Touchable)) {
            throw new BuildException("Can't touch " + r);
         }

         this.touch(r, defaultTimestamp);
      }

      for(int i = 0; i < this.filesets.size(); ++i) {
         FileSet fs = (FileSet)this.filesets.elementAt(i);
         DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
         File fromDir = fs.getDir(this.getProject());
         String[] srcDirs = ds.getIncludedDirectories();

         for(int j = 0; j < srcDirs.length; ++j) {
            this.touch((Resource)(new FileResource(fromDir, srcDirs[j])), defaultTimestamp);
         }
      }

   }

   /** @deprecated */
   protected void touch(File file) {
      this.touch(file, this.getTimestamp());
   }

   private long getTimestamp() {
      return this.millis < 0L ? System.currentTimeMillis() : this.millis;
   }

   private void touch(Resource r, long defaultTimestamp) {
      if (this.fileNameMapper == null) {
         if (r instanceof FileResource) {
            this.touch(((FileResource)r).getFile(), defaultTimestamp);
         } else {
            ((Touchable)r).touch(defaultTimestamp);
         }
      } else {
         String[] mapped = this.fileNameMapper.mapFileName(r.getName());
         if (mapped != null && mapped.length > 0) {
            long modTime = r.isExists() ? r.getLastModified() : defaultTimestamp;

            for(int i = 0; i < mapped.length; ++i) {
               this.touch(this.getProject().resolveFile(mapped[i]), modTime);
            }
         }
      }

   }

   private void touch(File file, long modTime) {
      if (!file.exists()) {
         this.log("Creating " + file, this.verbose ? 2 : 3);

         try {
            FILE_UTILS.createNewFile(file, this.mkdirs);
         } catch (IOException var5) {
            throw new BuildException("Could not create " + file, var5, this.getLocation());
         }
      }

      if (!file.canWrite()) {
         throw new BuildException("Can not change modification date of read-only file " + file);
      } else {
         FILE_UTILS.setFileLastModified(file, modTime);
      }
   }

   private interface DateFormatFactory {
      DateFormat getPrimaryFormat();

      DateFormat getFallbackFormat();
   }
}
