package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.ArchiveFileSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.ArchiveResource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.TarResource;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.SourceFileScanner;
import org.apache.tools.bzip2.CBZip2OutputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;

public class Tar extends MatchingTask {
   /** @deprecated */
   public static final String WARN = "warn";
   /** @deprecated */
   public static final String FAIL = "fail";
   /** @deprecated */
   public static final String TRUNCATE = "truncate";
   /** @deprecated */
   public static final String GNU = "gnu";
   /** @deprecated */
   public static final String OMIT = "omit";
   File tarFile;
   File baseDir;
   private Tar.TarLongFileMode longFileMode = new Tar.TarLongFileMode();
   Vector filesets = new Vector();
   private Vector resourceCollections = new Vector();
   Vector fileSetFiles = new Vector();
   private boolean longWarningGiven = false;
   private Tar.TarCompressionMethod compression = new Tar.TarCompressionMethod();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$Tar;

   public Tar.TarFileSet createTarFileSet() {
      Tar.TarFileSet fs = new Tar.TarFileSet();
      fs.setProject(this.getProject());
      this.filesets.addElement(fs);
      return fs;
   }

   public void add(ResourceCollection res) {
      this.resourceCollections.add(res);
   }

   /** @deprecated */
   public void setTarfile(File tarFile) {
      this.tarFile = tarFile;
   }

   public void setDestFile(File destFile) {
      this.tarFile = destFile;
   }

   public void setBasedir(File baseDir) {
      this.baseDir = baseDir;
   }

   /** @deprecated */
   public void setLongfile(String mode) {
      this.log("DEPRECATED - The setLongfile(String) method has been deprecated. Use setLongfile(Tar.TarLongFileMode) instead.");
      this.longFileMode = new Tar.TarLongFileMode();
      this.longFileMode.setValue(mode);
   }

   public void setLongfile(Tar.TarLongFileMode mode) {
      this.longFileMode = mode;
   }

   public void setCompression(Tar.TarCompressionMethod mode) {
      this.compression = mode;
   }

   public void execute() throws BuildException {
      if (this.tarFile == null) {
         throw new BuildException("tarfile attribute must be set!", this.getLocation());
      } else if (this.tarFile.exists() && this.tarFile.isDirectory()) {
         throw new BuildException("tarfile is a directory!", this.getLocation());
      } else if (this.tarFile.exists() && !this.tarFile.canWrite()) {
         throw new BuildException("Can not write to the specified tarfile!", this.getLocation());
      } else {
         Vector savedFileSets = (Vector)this.filesets.clone();

         try {
            if (this.baseDir != null) {
               if (!this.baseDir.exists()) {
                  throw new BuildException("basedir does not exist!", this.getLocation());
               }

               Tar.TarFileSet mainFileSet = new Tar.TarFileSet(this.fileset);
               mainFileSet.setDir(this.baseDir);
               this.filesets.addElement(mainFileSet);
            }

            if (this.filesets.size() == 0 && this.resourceCollections.size() == 0) {
               throw new BuildException("You must supply either a basedir attribute or some nested resource collections.", this.getLocation());
            }

            boolean upToDate = true;

            Enumeration e;
            for(e = this.filesets.elements(); e.hasMoreElements(); upToDate &= this.check((Tar.TarFileSet)e.nextElement())) {
            }

            for(e = this.resourceCollections.elements(); e.hasMoreElements(); upToDate &= this.check((ResourceCollection)e.nextElement())) {
            }

            if (upToDate) {
               this.log("Nothing to do: " + this.tarFile.getAbsolutePath() + " is up to date.", 2);
               return;
            }

            this.log("Building tar: " + this.tarFile.getAbsolutePath(), 2);
            TarOutputStream tOut = null;

            try {
               tOut = new TarOutputStream(this.compression.compress(new BufferedOutputStream(new FileOutputStream(this.tarFile))));
               tOut.setDebug(true);
               if (this.longFileMode.isTruncateMode()) {
                  tOut.setLongFileMode(1);
               } else if (!this.longFileMode.isFailMode() && !this.longFileMode.isOmitMode()) {
                  tOut.setLongFileMode(2);
               } else {
                  tOut.setLongFileMode(0);
               }

               this.longWarningGiven = false;
               Enumeration e = this.filesets.elements();

               while(e.hasMoreElements()) {
                  this.tar((Tar.TarFileSet)e.nextElement(), tOut);
               }

               e = this.resourceCollections.elements();

               while(e.hasMoreElements()) {
                  this.tar((ResourceCollection)e.nextElement(), tOut);
               }
            } catch (IOException var14) {
               String msg = "Problem creating TAR: " + var14.getMessage();
               throw new BuildException(msg, var14, this.getLocation());
            } finally {
               FileUtils.close((OutputStream)tOut);
            }
         } finally {
            this.filesets = savedFileSets;
         }

      }
   }

   protected void tarFile(File file, TarOutputStream tOut, String vPath, Tar.TarFileSet tarFileSet) throws IOException {
      if (!file.equals(this.tarFile)) {
         this.tarResource(new FileResource(file), tOut, vPath, tarFileSet);
      }
   }

   protected void tarResource(Resource r, TarOutputStream tOut, String vPath, Tar.TarFileSet tarFileSet) throws IOException {
      if (r.isExists()) {
         if (tarFileSet != null) {
            String fullpath = tarFileSet.getFullpath(this.getProject());
            if (fullpath.length() > 0) {
               vPath = fullpath;
            } else {
               if (vPath.length() <= 0) {
                  return;
               }

               String prefix = tarFileSet.getPrefix(this.getProject());
               if (prefix.length() > 0 && !prefix.endsWith("/")) {
                  prefix = prefix + "/";
               }

               vPath = prefix + vPath;
            }

            if (vPath.startsWith("/") && !tarFileSet.getPreserveLeadingSlashes()) {
               int l = vPath.length();
               if (l <= 1) {
                  return;
               }

               vPath = vPath.substring(1, l);
            }
         }

         if (r.isDirectory() && !vPath.endsWith("/")) {
            vPath = vPath + "/";
         }

         if (vPath.length() >= 100) {
            if (this.longFileMode.isOmitMode()) {
               this.log("Omitting: " + vPath, 2);
               return;
            }

            if (this.longFileMode.isWarnMode()) {
               this.log("Entry: " + vPath + " longer than " + 100 + " characters.", 1);
               if (!this.longWarningGiven) {
                  this.log("Resulting tar file can only be processed successfully by GNU compatible tar commands", 1);
                  this.longWarningGiven = true;
               }
            } else if (this.longFileMode.isFailMode()) {
               throw new BuildException("Entry: " + vPath + " longer than " + 100 + "characters.", this.getLocation());
            }
         }

         TarEntry te = new TarEntry(vPath);
         te.setModTime(r.getLastModified());
         if (r instanceof ArchiveResource) {
            ArchiveResource ar = (ArchiveResource)r;
            te.setMode(ar.getMode());
            if (r instanceof TarResource) {
               TarResource tr = (TarResource)r;
               te.setUserName(tr.getUserName());
               te.setUserId(tr.getUid());
               te.setGroupName(tr.getGroup());
               te.setGroupId(tr.getGid());
            }
         }

         if (!r.isDirectory()) {
            if ((long)r.size() > 8589934591L) {
               throw new BuildException("Resource: " + r + " larger than " + 8589934591L + " bytes.");
            }

            te.setSize(r.getSize());
            if (tarFileSet != null && tarFileSet.hasFileModeBeenSet()) {
               te.setMode(tarFileSet.getMode());
            }
         } else if (tarFileSet != null && tarFileSet.hasDirModeBeenSet()) {
            te.setMode(tarFileSet.getDirMode(this.getProject()));
         }

         if (tarFileSet != null) {
            if (tarFileSet.hasUserNameBeenSet()) {
               te.setUserName(tarFileSet.getUserName());
            }

            if (tarFileSet.hasGroupBeenSet()) {
               te.setGroupName(tarFileSet.getGroup());
            }

            if (tarFileSet.hasUserIdBeenSet()) {
               te.setUserId(tarFileSet.getUid());
            }

            if (tarFileSet.hasGroupIdBeenSet()) {
               te.setGroupId(tarFileSet.getGid());
            }
         }

         InputStream in = null;

         try {
            tOut.putNextEntry(te);
            if (!r.isDirectory()) {
               in = r.getInputStream();
               byte[] buffer = new byte[8192];
               int count = 0;

               do {
                  tOut.write(buffer, 0, count);
                  count = in.read(buffer, 0, buffer.length);
               } while(count != -1);
            }

            tOut.closeEntry();
         } finally {
            FileUtils.close(in);
         }

      }
   }

   /** @deprecated */
   protected boolean archiveIsUpToDate(String[] files) {
      return this.archiveIsUpToDate(files, this.baseDir);
   }

   protected boolean archiveIsUpToDate(String[] files, File dir) {
      SourceFileScanner sfs = new SourceFileScanner(this);
      MergingMapper mm = new MergingMapper();
      mm.setTo(this.tarFile.getAbsolutePath());
      return sfs.restrict(files, dir, (File)null, mm).length == 0;
   }

   protected boolean archiveIsUpToDate(Resource r) {
      return SelectorUtils.isOutOfDate(new FileResource(this.tarFile), r, FileUtils.getFileUtils().getFileTimestampGranularity());
   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$Tar == null ? (class$org$apache$tools$ant$taskdefs$Tar = class$("org.apache.tools.ant.taskdefs.Tar")) : class$org$apache$tools$ant$taskdefs$Tar);
   }

   protected boolean check(ResourceCollection rc) {
      boolean upToDate = true;
      if (isFileFileSet(rc)) {
         FileSet fs = (FileSet)rc;
         upToDate = this.check(fs.getDir(this.getProject()), getFileNames(fs));
      } else {
         if (!rc.isFilesystemOnly() && !this.supportsNonFileResources()) {
            throw new BuildException("only filesystem resources are supported");
         }

         Resource r;
         if (rc.isFilesystemOnly()) {
            HashSet basedirs = new HashSet();
            HashMap basedirToFilesMap = new HashMap();

            Iterator iter;
            FileResource r;
            Vector files;
            for(iter = rc.iterator(); iter.hasNext(); files.add(r.getName())) {
               r = (FileResource)iter.next();
               File base = r.getBaseDir();
               if (base == null) {
                  base = Copy.NULL_FILE_PLACEHOLDER;
               }

               basedirs.add(base);
               files = (Vector)basedirToFilesMap.get(base);
               if (files == null) {
                  files = new Vector();
                  basedirToFilesMap.put(base, new Vector());
               }
            }

            File base;
            String[] files;
            for(iter = basedirs.iterator(); iter.hasNext(); upToDate &= this.check(base == Copy.NULL_FILE_PLACEHOLDER ? null : base, files)) {
               base = (File)iter.next();
               Vector f = (Vector)basedirToFilesMap.get(base);
               files = (String[])((String[])f.toArray(new String[f.size()]));
            }
         } else {
            for(Iterator iter = rc.iterator(); upToDate && iter.hasNext(); upToDate &= this.archiveIsUpToDate(r)) {
               r = (Resource)iter.next();
            }
         }
      }

      return upToDate;
   }

   protected boolean check(File basedir, String[] files) {
      boolean upToDate = true;
      if (!this.archiveIsUpToDate(files, basedir)) {
         upToDate = false;
      }

      for(int i = 0; i < files.length; ++i) {
         if (this.tarFile.equals(new File(basedir, files[i]))) {
            throw new BuildException("A tar file cannot include itself", this.getLocation());
         }
      }

      return upToDate;
   }

   protected void tar(ResourceCollection rc, TarOutputStream tOut) throws IOException {
      ArchiveFileSet afs = null;
      if (rc instanceof ArchiveFileSet) {
         afs = (ArchiveFileSet)rc;
      }

      if (afs != null && afs.size() > 1 && afs.getFullpath(this.getProject()).length() > 0) {
         throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
      } else {
         Tar.TarFileSet tfs = this.asTarFileSet(afs);
         if (isFileFileSet(rc)) {
            FileSet fs = (FileSet)rc;
            String[] files = getFileNames(fs);

            for(int i = 0; i < files.length; ++i) {
               File f = new File(fs.getDir(this.getProject()), files[i]);
               String name = files[i].replace(File.separatorChar, '/');
               this.tarFile(f, tOut, name, tfs);
            }
         } else {
            Iterator iter;
            File f;
            if (rc.isFilesystemOnly()) {
               for(iter = rc.iterator(); iter.hasNext(); this.tarFile(f, tOut, f.getName(), tfs)) {
                  FileResource r = (FileResource)iter.next();
                  f = r.getFile();
                  if (f == null) {
                     f = new File(r.getBaseDir(), r.getName());
                  }
               }
            } else {
               iter = rc.iterator();

               while(iter.hasNext()) {
                  Resource r = (Resource)iter.next();
                  this.tarResource(r, tOut, r.getName(), tfs);
               }
            }
         }

      }
   }

   protected static final boolean isFileFileSet(ResourceCollection rc) {
      return rc instanceof FileSet && rc.isFilesystemOnly();
   }

   protected static final String[] getFileNames(FileSet fs) {
      DirectoryScanner ds = fs.getDirectoryScanner(fs.getProject());
      String[] directories = ds.getIncludedDirectories();
      String[] filesPerSe = ds.getIncludedFiles();
      String[] files = new String[directories.length + filesPerSe.length];
      System.arraycopy(directories, 0, files, 0, directories.length);
      System.arraycopy(filesPerSe, 0, files, directories.length, filesPerSe.length);
      return files;
   }

   protected Tar.TarFileSet asTarFileSet(ArchiveFileSet archiveFileSet) {
      Tar.TarFileSet tfs = null;
      if (archiveFileSet != null && archiveFileSet instanceof Tar.TarFileSet) {
         tfs = (Tar.TarFileSet)archiveFileSet;
      } else {
         tfs = new Tar.TarFileSet();
         tfs.setProject(this.getProject());
         if (archiveFileSet != null) {
            tfs.setPrefix(archiveFileSet.getPrefix(this.getProject()));
            tfs.setFullpath(archiveFileSet.getFullpath(this.getProject()));
            if (archiveFileSet.hasFileModeBeenSet()) {
               tfs.integerSetFileMode(archiveFileSet.getFileMode(this.getProject()));
            }

            if (archiveFileSet.hasDirModeBeenSet()) {
               tfs.integerSetDirMode(archiveFileSet.getDirMode(this.getProject()));
            }

            if (archiveFileSet instanceof org.apache.tools.ant.types.TarFileSet) {
               org.apache.tools.ant.types.TarFileSet t = (org.apache.tools.ant.types.TarFileSet)archiveFileSet;
               if (t.hasUserNameBeenSet()) {
                  tfs.setUserName(t.getUserName());
               }

               if (t.hasGroupBeenSet()) {
                  tfs.setGroup(t.getGroup());
               }

               if (t.hasUserIdBeenSet()) {
                  tfs.setUid(t.getUid());
               }

               if (t.hasGroupIdBeenSet()) {
                  tfs.setGid(t.getGid());
               }
            }
         }
      }

      return tfs;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static final class TarCompressionMethod extends EnumeratedAttribute {
      private static final String NONE = "none";
      private static final String GZIP = "gzip";
      private static final String BZIP2 = "bzip2";

      public TarCompressionMethod() {
         this.setValue("none");
      }

      public String[] getValues() {
         return new String[]{"none", "gzip", "bzip2"};
      }

      private OutputStream compress(OutputStream ostream) throws IOException {
         String v = this.getValue();
         if ("gzip".equals(v)) {
            return new GZIPOutputStream(ostream);
         } else if ("bzip2".equals(v)) {
            ostream.write(66);
            ostream.write(90);
            return new CBZip2OutputStream(ostream);
         } else {
            return ostream;
         }
      }
   }

   public static class TarLongFileMode extends EnumeratedAttribute {
      public static final String WARN = "warn";
      public static final String FAIL = "fail";
      public static final String TRUNCATE = "truncate";
      public static final String GNU = "gnu";
      public static final String OMIT = "omit";
      private final String[] validModes = new String[]{"warn", "fail", "truncate", "gnu", "omit"};

      public TarLongFileMode() {
         this.setValue("warn");
      }

      public String[] getValues() {
         return this.validModes;
      }

      public boolean isTruncateMode() {
         return "truncate".equalsIgnoreCase(this.getValue());
      }

      public boolean isWarnMode() {
         return "warn".equalsIgnoreCase(this.getValue());
      }

      public boolean isGnuMode() {
         return "gnu".equalsIgnoreCase(this.getValue());
      }

      public boolean isFailMode() {
         return "fail".equalsIgnoreCase(this.getValue());
      }

      public boolean isOmitMode() {
         return "omit".equalsIgnoreCase(this.getValue());
      }
   }

   public static class TarFileSet extends org.apache.tools.ant.types.TarFileSet {
      private String[] files = null;
      private boolean preserveLeadingSlashes = false;

      public TarFileSet(FileSet fileset) {
         super(fileset);
      }

      public TarFileSet() {
      }

      public String[] getFiles(Project p) {
         if (this.files == null) {
            this.files = Tar.getFileNames(this);
         }

         return this.files;
      }

      public void setMode(String octalString) {
         this.setFileMode(octalString);
      }

      public int getMode() {
         return this.getFileMode(this.getProject());
      }

      public void setPreserveLeadingSlashes(boolean b) {
         this.preserveLeadingSlashes = b;
      }

      public boolean getPreserveLeadingSlashes() {
         return this.preserveLeadingSlashes;
      }
   }
}
