package org.apache.tools.ant.taskdefs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.zip.CRC32;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.FileScanner;
import org.apache.tools.ant.types.ArchiveFileSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ZipFileSet;
import org.apache.tools.ant.types.ZipScanner;
import org.apache.tools.ant.types.resources.ArchiveResource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.MergingMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipExtraField;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class Zip extends MatchingTask {
   protected File zipFile;
   private ZipScanner zs;
   private File baseDir;
   protected Hashtable entries = new Hashtable();
   private Vector groupfilesets = new Vector();
   private Vector filesetsFromGroupfilesets = new Vector();
   protected String duplicate = "add";
   private boolean doCompress = true;
   private boolean doUpdate = false;
   private boolean savedDoUpdate = false;
   private boolean doFilesonly = false;
   protected String archiveType = "zip";
   private static final long EMPTY_CRC = (new CRC32()).getValue();
   protected String emptyBehavior = "skip";
   private Vector resources = new Vector();
   protected Hashtable addedDirs = new Hashtable();
   private Vector addedFiles = new Vector();
   protected boolean doubleFilePass = false;
   protected boolean skipWriting = false;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private boolean addingNewFiles = false;
   private String encoding;
   private boolean keepCompression = false;
   private boolean roundUp = true;
   private String comment = "";
   private int level = -1;

   /** @deprecated */
   public void setZipfile(File zipFile) {
      this.setDestFile(zipFile);
   }

   /** @deprecated */
   public void setFile(File file) {
      this.setDestFile(file);
   }

   public void setDestFile(File destFile) {
      this.zipFile = destFile;
   }

   public File getDestFile() {
      return this.zipFile;
   }

   public void setBasedir(File baseDir) {
      this.baseDir = baseDir;
   }

   public void setCompress(boolean c) {
      this.doCompress = c;
   }

   public boolean isCompress() {
      return this.doCompress;
   }

   public void setFilesonly(boolean f) {
      this.doFilesonly = f;
   }

   public void setUpdate(boolean c) {
      this.doUpdate = c;
      this.savedDoUpdate = c;
   }

   public boolean isInUpdateMode() {
      return this.doUpdate;
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void addZipfileset(ZipFileSet set) {
      this.add(set);
   }

   public void add(ResourceCollection a) {
      this.resources.add(a);
   }

   public void addZipGroupFileset(FileSet set) {
      this.groupfilesets.addElement(set);
   }

   public void setDuplicate(Zip.Duplicate df) {
      this.duplicate = df.getValue();
   }

   public void setWhenempty(Zip.WhenEmpty we) {
      this.emptyBehavior = we.getValue();
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setKeepCompression(boolean keep) {
      this.keepCompression = keep;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getComment() {
      return this.comment;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getLevel() {
      return this.level;
   }

   public void setRoundUp(boolean r) {
      this.roundUp = r;
   }

   public void execute() throws BuildException {
      if (this.doubleFilePass) {
         this.skipWriting = true;
         this.executeMain();
         this.skipWriting = false;
         this.executeMain();
      } else {
         this.executeMain();
      }

   }

   public void executeMain() throws BuildException {
      if (this.baseDir == null && this.resources.size() == 0 && this.groupfilesets.size() == 0 && "zip".equals(this.archiveType)) {
         throw new BuildException("basedir attribute must be set, or at least one resource collection must be given!");
      } else if (this.zipFile == null) {
         throw new BuildException("You must specify the " + this.archiveType + " file to create!");
      } else if (this.zipFile.exists() && !this.zipFile.isFile()) {
         throw new BuildException(this.zipFile + " is not a file.");
      } else if (this.zipFile.exists() && !this.zipFile.canWrite()) {
         throw new BuildException(this.zipFile + " is read-only.");
      } else {
         File renamedFile = null;
         this.addingNewFiles = true;
         if (this.doUpdate && !this.zipFile.exists()) {
            this.doUpdate = false;
            this.log("ignoring update attribute as " + this.archiveType + " doesn't exist.", 4);
         }

         FileSet fs;
         for(int i = 0; i < this.groupfilesets.size(); ++i) {
            this.log("Processing groupfileset ", 3);
            fs = (FileSet)this.groupfilesets.elementAt(i);
            FileScanner scanner = fs.getDirectoryScanner(this.getProject());
            String[] files = scanner.getIncludedFiles();
            File basedir = scanner.getBasedir();

            for(int j = 0; j < files.length; ++j) {
               this.log("Adding file " + files[j] + " to fileset", 3);
               ZipFileSet zf = new ZipFileSet();
               zf.setProject(this.getProject());
               zf.setSrc(new File(basedir, files[j]));
               this.add(zf);
               this.filesetsFromGroupfilesets.addElement(zf);
            }
         }

         Vector vfss = new Vector();
         if (this.baseDir != null) {
            fs = (FileSet)this.getImplicitFileSet().clone();
            fs.setDir(this.baseDir);
            vfss.addElement(fs);
         }

         for(int i = 0; i < this.resources.size(); ++i) {
            ResourceCollection rc = (ResourceCollection)this.resources.elementAt(i);
            vfss.addElement(rc);
         }

         ResourceCollection[] fss = new ResourceCollection[vfss.size()];
         vfss.copyInto(fss);
         boolean success = false;

         try {
            Zip.ArchiveState state = this.getResourcesToAdd(fss, this.zipFile, false);
            if (state.isOutOfDate()) {
               if (!this.zipFile.exists() && state.isWithoutAnyResources()) {
                  this.createEmptyZip(this.zipFile);
               } else {
                  Resource[][] addThem = state.getResourcesToAdd();
                  if (this.doUpdate) {
                     renamedFile = FILE_UTILS.createTempFile("zip", ".tmp", this.zipFile.getParentFile());
                     renamedFile.deleteOnExit();

                     try {
                        FILE_UTILS.rename(this.zipFile, renamedFile);
                     } catch (SecurityException var36) {
                        throw new BuildException("Not allowed to rename old file (" + this.zipFile.getAbsolutePath() + ") to temporary file");
                     } catch (IOException var37) {
                        throw new BuildException("Unable to rename old file (" + this.zipFile.getAbsolutePath() + ") to temporary file");
                     }
                  }

                  String action = this.doUpdate ? "Updating " : "Building ";
                  this.log(action + this.archiveType + ": " + this.zipFile.getAbsolutePath());
                  ZipOutputStream zOut = null;

                  try {
                     if (!this.skipWriting) {
                        zOut = new ZipOutputStream(this.zipFile);
                        zOut.setEncoding(this.encoding);
                        zOut.setMethod(this.doCompress ? 8 : 0);
                        zOut.setLevel(this.level);
                     }

                     this.initZipOutputStream(zOut);

                     for(int i = 0; i < fss.length; ++i) {
                        if (addThem[i].length != 0) {
                           this.addResources(fss[i], addThem[i], zOut);
                        }
                     }

                     if (this.doUpdate) {
                        this.addingNewFiles = false;
                        ZipFileSet oldFiles = new ZipFileSet();
                        oldFiles.setProject(this.getProject());
                        oldFiles.setSrc(renamedFile);
                        oldFiles.setDefaultexcludes(false);

                        for(int i = 0; i < this.addedFiles.size(); ++i) {
                           PatternSet.NameEntry ne = oldFiles.createExclude();
                           ne.setName((String)this.addedFiles.elementAt(i));
                        }

                        DirectoryScanner ds = oldFiles.getDirectoryScanner(this.getProject());
                        ((ZipScanner)ds).setEncoding(this.encoding);
                        String[] f = ds.getIncludedFiles();
                        Resource[] r = new Resource[f.length];

                        for(int i = 0; i < f.length; ++i) {
                           r[i] = ds.getResource(f[i]);
                        }

                        if (!this.doFilesonly) {
                           String[] d = ds.getIncludedDirectories();
                           Resource[] dr = new Resource[d.length];

                           for(int i = 0; i < d.length; ++i) {
                              dr[i] = ds.getResource(d[i]);
                           }

                           Resource[] tmp = r;
                           r = new Resource[r.length + dr.length];
                           System.arraycopy(dr, 0, r, 0, dr.length);
                           System.arraycopy(tmp, 0, r, dr.length, tmp.length);
                        }

                        this.addResources((FileSet)oldFiles, r, zOut);
                     }

                     if (zOut != null) {
                        zOut.setComment(this.comment);
                     }

                     this.finalizeZipOutputStream(zOut);
                     if (this.doUpdate && !renamedFile.delete()) {
                        this.log("Warning: unable to delete temporary file " + renamedFile.getName(), 1);
                     }

                     success = true;
                  } finally {
                     try {
                        if (zOut != null) {
                           zOut.close();
                        }
                     } catch (IOException var38) {
                        if (success) {
                           throw var38;
                        }
                     }

                  }
               }
            }
         } catch (IOException var40) {
            String msg = "Problem creating " + this.archiveType + ": " + var40.getMessage();
            if ((!this.doUpdate || renamedFile != null) && !this.zipFile.delete()) {
               msg = msg + " (and the archive is probably corrupt but I could not delete it)";
            }

            if (this.doUpdate && renamedFile != null) {
               try {
                  FILE_UTILS.rename(renamedFile, this.zipFile);
               } catch (IOException var35) {
                  msg = msg + " (and I couldn't rename the temporary file " + renamedFile.getName() + " back)";
               }
            }

            throw new BuildException(msg, var40, this.getLocation());
         } finally {
            this.cleanUp();
         }
      }
   }

   protected final boolean isAddingNewFiles() {
      return this.addingNewFiles;
   }

   protected final void addResources(FileSet fileset, Resource[] resources, ZipOutputStream zOut) throws IOException {
      String prefix = "";
      String fullpath = "";
      int dirMode = 16877;
      int fileMode = 33188;
      ArchiveFileSet zfs = null;
      if (fileset instanceof ArchiveFileSet) {
         zfs = (ArchiveFileSet)fileset;
         prefix = zfs.getPrefix(this.getProject());
         fullpath = zfs.getFullpath(this.getProject());
         dirMode = zfs.getDirMode(this.getProject());
         fileMode = zfs.getFileMode(this.getProject());
      }

      if (prefix.length() > 0 && fullpath.length() > 0) {
         throw new BuildException("Both prefix and fullpath attributes must not be set on the same fileset.");
      } else if (resources.length != 1 && fullpath.length() > 0) {
         throw new BuildException("fullpath attribute may only be specified for filesets that specify a single file.");
      } else {
         if (prefix.length() > 0) {
            if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
               prefix = prefix + "/";
            }

            this.addParentDirs((File)null, prefix, zOut, "", dirMode);
         }

         ZipFile zf = null;

         try {
            boolean dealingWithFiles = false;
            File base = null;
            if (zfs != null && zfs.getSrc(this.getProject()) != null) {
               if (zfs instanceof ZipFileSet) {
                  zf = new ZipFile(zfs.getSrc(this.getProject()), this.encoding);
               }
            } else {
               dealingWithFiles = true;
               base = fileset.getDir(this.getProject());
            }

            for(int i = 0; i < resources.length; ++i) {
               String name = null;
               if (fullpath.length() > 0) {
                  name = fullpath;
               } else {
                  name = resources[i].getName();
               }

               name = name.replace(File.separatorChar, '/');
               if (!"".equals(name)) {
                  if (resources[i].isDirectory() && !name.endsWith("/")) {
                     name = name + "/";
                  }

                  if (!this.doFilesonly && !dealingWithFiles && resources[i].isDirectory() && !zfs.hasDirModeBeenSet()) {
                     int nextToLastSlash = name.lastIndexOf("/", name.length() - 2);
                     if (nextToLastSlash != -1) {
                        this.addParentDirs(base, name.substring(0, nextToLastSlash + 1), zOut, prefix, dirMode);
                     }

                     if (zf != null) {
                        ZipEntry ze = zf.getEntry(resources[i].getName());
                        this.addParentDirs(base, name, zOut, prefix, ze.getUnixMode());
                     } else {
                        ArchiveResource tr = (ArchiveResource)resources[i];
                        this.addParentDirs(base, name, zOut, prefix, tr.getMode());
                     }
                  } else {
                     this.addParentDirs(base, name, zOut, prefix, dirMode);
                  }

                  if (!resources[i].isDirectory() && dealingWithFiles) {
                     File f = FILE_UTILS.resolveFile(base, resources[i].getName());
                     this.zipFile(f, zOut, prefix + name, fileMode);
                  } else if (!resources[i].isDirectory()) {
                     if (zf != null) {
                        ZipEntry ze = zf.getEntry(resources[i].getName());
                        if (ze != null) {
                           boolean oldCompress = this.doCompress;
                           if (this.keepCompression) {
                              this.doCompress = ze.getMethod() == 8;
                           }

                           try {
                              this.zipFile(zf.getInputStream(ze), zOut, prefix + name, ze.getTime(), zfs.getSrc(this.getProject()), zfs.hasFileModeBeenSet() ? fileMode : ze.getUnixMode());
                           } finally {
                              this.doCompress = oldCompress;
                           }
                        }
                     } else {
                        ArchiveResource tr = (ArchiveResource)resources[i];
                        InputStream is = null;

                        try {
                           is = tr.getInputStream();
                           this.zipFile(is, zOut, prefix + name, resources[i].getLastModified(), zfs.getSrc(this.getProject()), zfs.hasFileModeBeenSet() ? fileMode : tr.getMode());
                        } finally {
                           FileUtils.close(is);
                        }
                     }
                  }
               }
            }
         } finally {
            if (zf != null) {
               zf.close();
            }

         }

      }
   }

   protected final void addResources(ResourceCollection rc, Resource[] resources, ZipOutputStream zOut) throws IOException {
      if (rc instanceof FileSet) {
         this.addResources((FileSet)rc, resources, zOut);
      } else {
         for(int i = 0; i < resources.length; ++i) {
            String name = resources[i].getName().replace(File.separatorChar, '/');
            if (!"".equals(name) && (!resources[i].isDirectory() || !this.doFilesonly)) {
               File base = null;
               if (resources[i] instanceof FileResource) {
                  base = ((FileResource)resources[i]).getBaseDir();
               }

               if (resources[i].isDirectory() && !name.endsWith("/")) {
                  name = name + "/";
               }

               this.addParentDirs(base, name, zOut, "", 16877);
               if (!resources[i].isDirectory()) {
                  if (resources[i] instanceof FileResource) {
                     File f = ((FileResource)resources[i]).getFile();
                     this.zipFile(f, zOut, name, 33188);
                  } else {
                     InputStream is = null;

                     try {
                        is = resources[i].getInputStream();
                        this.zipFile(is, zOut, name, resources[i].getLastModified(), (File)null, 33188);
                     } finally {
                        FileUtils.close(is);
                     }
                  }
               }
            }
         }

      }
   }

   protected void initZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
   }

   protected void finalizeZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
   }

   protected boolean createEmptyZip(File zipFile) throws BuildException {
      this.log("Note: creating empty " + this.archiveType + " archive " + zipFile, 2);
      FileOutputStream os = null;

      try {
         os = new FileOutputStream(zipFile);
         byte[] empty = new byte[22];
         empty[0] = 80;
         empty[1] = 75;
         empty[2] = 5;
         empty[3] = 6;
         os.write(empty);
      } catch (IOException var11) {
         throw new BuildException("Could not create empty ZIP archive (" + var11.getMessage() + ")", var11, this.getLocation());
      } finally {
         if (os != null) {
            try {
               os.close();
            } catch (IOException var10) {
            }
         }

      }

      return true;
   }

   private synchronized ZipScanner getZipScanner() {
      if (this.zs == null) {
         this.zs = new ZipScanner();
         this.zs.setEncoding(this.encoding);
         this.zs.setSrc(this.zipFile);
      }

      return this.zs;
   }

   protected Zip.ArchiveState getResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
      ArrayList filesets = new ArrayList();
      ArrayList rest = new ArrayList();

      for(int i = 0; i < rcs.length; ++i) {
         if (rcs[i] instanceof FileSet) {
            filesets.add(rcs[i]);
         } else {
            rest.add(rcs[i]);
         }
      }

      ResourceCollection[] rc = (ResourceCollection[])((ResourceCollection[])rest.toArray(new ResourceCollection[rest.size()]));
      Zip.ArchiveState as = this.getNonFileSetResourcesToAdd(rc, zipFile, needsUpdate);
      FileSet[] fs = (FileSet[])((FileSet[])filesets.toArray(new FileSet[filesets.size()]));
      Zip.ArchiveState as2 = this.getResourcesToAdd(fs, zipFile, as.isOutOfDate());
      if (!as.isOutOfDate() && as2.isOutOfDate()) {
         as = this.getNonFileSetResourcesToAdd(rc, zipFile, true);
      }

      Resource[][] toAdd = new Resource[rcs.length][];
      int fsIndex = 0;
      int restIndex = 0;

      for(int i = 0; i < rcs.length; ++i) {
         if (rcs[i] instanceof FileSet) {
            toAdd[i] = as2.getResourcesToAdd()[fsIndex++];
         } else {
            toAdd[i] = as.getResourcesToAdd()[restIndex++];
         }
      }

      return new Zip.ArchiveState(as2.isOutOfDate(), toAdd);
   }

   protected Zip.ArchiveState getResourcesToAdd(FileSet[] filesets, File zipFile, boolean needsUpdate) throws BuildException {
      Resource[][] initialResources = this.grabResources(filesets);
      if (isEmpty(initialResources)) {
         if (needsUpdate && this.doUpdate) {
            return new Zip.ArchiveState(true, initialResources);
         } else {
            if (this.emptyBehavior.equals("skip")) {
               if (this.doUpdate) {
                  this.log(this.archiveType + " archive " + zipFile + " not updated because no new files were included.", 3);
               } else {
                  this.log("Warning: skipping " + this.archiveType + " archive " + zipFile + " because no files were included.", 1);
               }
            } else {
               if (this.emptyBehavior.equals("fail")) {
                  throw new BuildException("Cannot create " + this.archiveType + " archive " + zipFile + ": no files were included.", this.getLocation());
               }

               if (!zipFile.exists()) {
                  needsUpdate = true;
               }
            }

            return new Zip.ArchiveState(needsUpdate, initialResources);
         }
      } else if (!zipFile.exists()) {
         return new Zip.ArchiveState(true, initialResources);
      } else if (needsUpdate && !this.doUpdate) {
         return new Zip.ArchiveState(true, initialResources);
      } else {
         Resource[][] newerResources = new Resource[filesets.length][];

         int i;
         for(i = 0; i < filesets.length; ++i) {
            if (!(this.fileset instanceof ZipFileSet) || ((ZipFileSet)this.fileset).getSrc(this.getProject()) == null) {
               File base = filesets[i].getDir(this.getProject());

               for(int j = 0; j < initialResources[i].length; ++j) {
                  File resourceAsFile = FILE_UTILS.resolveFile(base, initialResources[i][j].getName());
                  if (resourceAsFile.equals(zipFile)) {
                     throw new BuildException("A zip file cannot include itself", this.getLocation());
                  }
               }
            }
         }

         for(i = 0; i < filesets.length; ++i) {
            if (initialResources[i].length == 0) {
               newerResources[i] = new Resource[0];
            } else {
               FileNameMapper myMapper = new IdentityMapper();
               if (filesets[i] instanceof ZipFileSet) {
                  ZipFileSet zfs = (ZipFileSet)filesets[i];
                  if (zfs.getFullpath(this.getProject()) != null && !zfs.getFullpath(this.getProject()).equals("")) {
                     MergingMapper fm = new MergingMapper();
                     fm.setTo(zfs.getFullpath(this.getProject()));
                     myMapper = fm;
                  } else if (zfs.getPrefix(this.getProject()) != null && !zfs.getPrefix(this.getProject()).equals("")) {
                     GlobPatternMapper gm = new GlobPatternMapper();
                     gm.setFrom("*");
                     String prefix = zfs.getPrefix(this.getProject());
                     if (!prefix.endsWith("/") && !prefix.endsWith("\\")) {
                        prefix = prefix + "/";
                     }

                     gm.setTo(prefix + "*");
                     myMapper = gm;
                  }
               }

               Resource[] resources = initialResources[i];
               if (this.doFilesonly) {
                  resources = this.selectFileResources(resources);
               }

               newerResources[i] = ResourceUtils.selectOutOfDateSources(this, resources, (FileNameMapper)myMapper, this.getZipScanner());
               needsUpdate = needsUpdate || newerResources[i].length > 0;
               if (needsUpdate && !this.doUpdate) {
                  break;
               }
            }
         }

         if (needsUpdate && !this.doUpdate) {
            return new Zip.ArchiveState(true, initialResources);
         } else {
            return new Zip.ArchiveState(needsUpdate, newerResources);
         }
      }
   }

   protected Zip.ArchiveState getNonFileSetResourcesToAdd(ResourceCollection[] rcs, File zipFile, boolean needsUpdate) throws BuildException {
      Resource[][] initialResources = this.grabNonFileSetResources(rcs);
      if (isEmpty(initialResources)) {
         return new Zip.ArchiveState(needsUpdate, initialResources);
      } else if (!zipFile.exists()) {
         return new Zip.ArchiveState(true, initialResources);
      } else if (needsUpdate && !this.doUpdate) {
         return new Zip.ArchiveState(true, initialResources);
      } else {
         Resource[][] newerResources = new Resource[rcs.length][];

         for(int i = 0; i < rcs.length; ++i) {
            if (initialResources[i].length == 0) {
               newerResources[i] = new Resource[0];
            } else {
               for(int j = 0; j < initialResources[i].length; ++j) {
                  if (initialResources[i][j] instanceof FileResource && zipFile.equals(((FileResource)initialResources[i][j]).getFile())) {
                     throw new BuildException("A zip file cannot include itself", this.getLocation());
                  }
               }

               Resource[] rs = initialResources[i];
               if (this.doFilesonly) {
                  rs = this.selectFileResources(rs);
               }

               newerResources[i] = ResourceUtils.selectOutOfDateSources(this, rs, new IdentityMapper(), this.getZipScanner());
               needsUpdate = needsUpdate || newerResources[i].length > 0;
               if (needsUpdate && !this.doUpdate) {
                  break;
               }
            }
         }

         return needsUpdate && !this.doUpdate ? new Zip.ArchiveState(true, initialResources) : new Zip.ArchiveState(needsUpdate, newerResources);
      }
   }

   protected Resource[][] grabResources(FileSet[] filesets) {
      Resource[][] result = new Resource[filesets.length][];

      for(int i = 0; i < filesets.length; ++i) {
         boolean skipEmptyNames = true;
         if (filesets[i] instanceof ZipFileSet) {
            ZipFileSet zfs = (ZipFileSet)filesets[i];
            skipEmptyNames = zfs.getPrefix(this.getProject()).equals("") && zfs.getFullpath(this.getProject()).equals("");
         }

         DirectoryScanner rs = filesets[i].getDirectoryScanner(this.getProject());
         if (rs instanceof ZipScanner) {
            ((ZipScanner)rs).setEncoding(this.encoding);
         }

         Vector resources = new Vector();
         String[] files;
         int j;
         if (!this.doFilesonly) {
            files = rs.getIncludedDirectories();

            for(j = 0; j < files.length; ++j) {
               if (!"".equals(files[j]) || !skipEmptyNames) {
                  resources.addElement(rs.getResource(files[j]));
               }
            }
         }

         files = rs.getIncludedFiles();

         for(j = 0; j < files.length; ++j) {
            if (!"".equals(files[j]) || !skipEmptyNames) {
               resources.addElement(rs.getResource(files[j]));
            }
         }

         result[i] = new Resource[resources.size()];
         resources.copyInto(result[i]);
      }

      return result;
   }

   protected Resource[][] grabNonFileSetResources(ResourceCollection[] rcs) {
      Resource[][] result = new Resource[rcs.length][];

      for(int i = 0; i < rcs.length; ++i) {
         Iterator iter = rcs[i].iterator();
         ArrayList rs = new ArrayList();
         int var6 = 0;

         while(iter.hasNext()) {
            Resource r = (Resource)iter.next();
            if (r.isExists()) {
               if (r.isDirectory()) {
                  rs.add(var6++, r);
               } else {
                  rs.add(r);
               }
            }
         }

         result[i] = (Resource[])((Resource[])rs.toArray(new Resource[rs.size()]));
      }

      return result;
   }

   protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode) throws IOException {
      this.zipDir(dir, zOut, vPath, mode, (ZipExtraField[])null);
   }

   protected void zipDir(File dir, ZipOutputStream zOut, String vPath, int mode, ZipExtraField[] extra) throws IOException {
      if (this.doFilesonly) {
         this.log("skipping directory " + vPath + " for file-only archive", 3);
      } else if (this.addedDirs.get(vPath) == null) {
         this.log("adding directory " + vPath, 3);
         this.addedDirs.put(vPath, vPath);
         if (!this.skipWriting) {
            ZipEntry ze = new ZipEntry(vPath);
            if (dir != null && dir.exists()) {
               ze.setTime(dir.lastModified() + (long)(this.roundUp ? 1999 : 0));
            } else {
               ze.setTime(System.currentTimeMillis() + (long)(this.roundUp ? 1999 : 0));
            }

            ze.setSize(0L);
            ze.setMethod(0);
            ze.setCrc(EMPTY_CRC);
            ze.setUnixMode(mode);
            if (extra != null) {
               ze.setExtraFields(extra);
            }

            zOut.putNextEntry(ze);
         }

      }
   }

   protected void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode) throws IOException {
      if (this.entries.contains(vPath)) {
         if (this.duplicate.equals("preserve")) {
            this.log(vPath + " already added, skipping", 2);
            return;
         }

         if (this.duplicate.equals("fail")) {
            throw new BuildException("Duplicate file " + vPath + " was found and the duplicate " + "attribute is 'fail'.");
         }

         this.log("duplicate file " + vPath + " found, adding.", 3);
      } else {
         this.log("adding entry " + vPath, 3);
      }

      this.entries.put(vPath, vPath);
      if (!this.skipWriting) {
         ZipEntry ze = new ZipEntry(vPath);
         ze.setTime(lastModified);
         ze.setMethod(this.doCompress ? 8 : 0);
         if (!zOut.isSeekable() && !this.doCompress) {
            long size = 0L;
            CRC32 cal = new CRC32();
            if (!((InputStream)in).markSupported()) {
               ByteArrayOutputStream bos = new ByteArrayOutputStream();
               byte[] buffer = new byte[8192];
               int count = 0;

               do {
                  size += (long)count;
                  cal.update(buffer, 0, count);
                  bos.write(buffer, 0, count);
                  count = ((InputStream)in).read(buffer, 0, buffer.length);
               } while(count != -1);

               in = new ByteArrayInputStream(bos.toByteArray());
            } else {
               ((InputStream)in).mark(Integer.MAX_VALUE);
               byte[] buffer = new byte[8192];
               int count = 0;

               do {
                  size += (long)count;
                  cal.update(buffer, 0, count);
                  count = ((InputStream)in).read(buffer, 0, buffer.length);
               } while(count != -1);

               ((InputStream)in).reset();
            }

            ze.setSize(size);
            ze.setCrc(cal.getValue());
         }

         ze.setUnixMode(mode);
         zOut.putNextEntry(ze);
         byte[] buffer = new byte[8192];
         int count = 0;

         do {
            if (count != 0) {
               zOut.write(buffer, 0, count);
            }

            count = ((InputStream)in).read(buffer, 0, buffer.length);
         } while(count != -1);
      }

      this.addedFiles.addElement(vPath);
   }

   protected void zipFile(File file, ZipOutputStream zOut, String vPath, int mode) throws IOException {
      if (file.equals(this.zipFile)) {
         throw new BuildException("A zip file cannot include itself", this.getLocation());
      } else {
         FileInputStream fIn = new FileInputStream(file);

         try {
            this.zipFile(fIn, zOut, vPath, file.lastModified() + (long)(this.roundUp ? 1999 : 0), (File)null, mode);
         } finally {
            fIn.close();
         }

      }
   }

   protected final void addParentDirs(File baseDir, String entry, ZipOutputStream zOut, String prefix, int dirMode) throws IOException {
      if (!this.doFilesonly) {
         Stack directories = new Stack();
         int slashPos = entry.length();

         String dir;
         while((slashPos = entry.lastIndexOf(47, slashPos - 1)) != -1) {
            dir = entry.substring(0, slashPos + 1);
            if (this.addedDirs.get(prefix + dir) != null) {
               break;
            }

            directories.push(dir);
         }

         File f;
         for(; !directories.isEmpty(); this.zipDir(f, zOut, prefix + dir, dirMode)) {
            dir = (String)directories.pop();
            f = null;
            if (baseDir != null) {
               f = new File(baseDir, dir);
            } else {
               f = new File(dir);
            }
         }
      }

   }

   protected void cleanUp() {
      this.addedDirs.clear();
      this.addedFiles.removeAllElements();
      this.entries.clear();
      this.addingNewFiles = false;
      this.doUpdate = this.savedDoUpdate;
      Enumeration e = this.filesetsFromGroupfilesets.elements();

      while(e.hasMoreElements()) {
         ZipFileSet zf = (ZipFileSet)e.nextElement();
         this.resources.removeElement(zf);
      }

      this.filesetsFromGroupfilesets.removeAllElements();
   }

   public void reset() {
      this.resources.removeAllElements();
      this.zipFile = null;
      this.baseDir = null;
      this.groupfilesets.removeAllElements();
      this.duplicate = "add";
      this.archiveType = "zip";
      this.doCompress = true;
      this.emptyBehavior = "skip";
      this.doUpdate = false;
      this.doFilesonly = false;
      this.encoding = null;
   }

   protected static final boolean isEmpty(Resource[][] r) {
      for(int i = 0; i < r.length; ++i) {
         if (r[i].length > 0) {
            return false;
         }
      }

      return true;
   }

   protected Resource[] selectFileResources(Resource[] orig) {
      if (orig.length == 0) {
         return orig;
      } else {
         Vector v = new Vector(orig.length);

         for(int i = 0; i < orig.length; ++i) {
            if (!orig[i].isDirectory()) {
               v.addElement(orig[i]);
            } else {
               this.log("Ignoring directory " + orig[i].getName() + " as only files will be added.", 3);
            }
         }

         if (v.size() != orig.length) {
            Resource[] r = new Resource[v.size()];
            v.copyInto(r);
            return r;
         } else {
            return orig;
         }
      }
   }

   public static class ArchiveState {
      private boolean outOfDate;
      private Resource[][] resourcesToAdd;

      ArchiveState(boolean state, Resource[][] r) {
         this.outOfDate = state;
         this.resourcesToAdd = r;
      }

      public boolean isOutOfDate() {
         return this.outOfDate;
      }

      public Resource[][] getResourcesToAdd() {
         return this.resourcesToAdd;
      }

      public boolean isWithoutAnyResources() {
         if (this.resourcesToAdd == null) {
            return true;
         } else {
            for(int counter = 0; counter < this.resourcesToAdd.length; ++counter) {
               if (this.resourcesToAdd[counter] != null && this.resourcesToAdd[counter].length > 0) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static class Duplicate extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"add", "preserve", "fail"};
      }
   }

   public static class WhenEmpty extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"fail", "skip", "create"};
      }
   }
}
