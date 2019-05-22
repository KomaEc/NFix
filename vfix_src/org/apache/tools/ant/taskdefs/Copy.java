package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSet;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.FlatFileNameMapper;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.ResourceUtils;
import org.apache.tools.ant.util.SourceFileScanner;

public class Copy extends Task {
   static final File NULL_FILE_PLACEHOLDER = new File("/NULL_FILE");
   static final String LINE_SEPARATOR = System.getProperty("line.separator");
   protected File file = null;
   protected File destFile = null;
   protected File destDir = null;
   protected Vector rcs = new Vector();
   private boolean enableMultipleMappings = false;
   protected boolean filtering = false;
   protected boolean preserveLastModified = false;
   protected boolean forceOverwrite = false;
   protected boolean flatten = false;
   protected int verbosity = 3;
   protected boolean includeEmpty = true;
   protected boolean failonerror = true;
   protected Hashtable fileCopyMap = new Hashtable();
   protected Hashtable dirCopyMap = new Hashtable();
   protected Hashtable completeDirMap = new Hashtable();
   protected Mapper mapperElement = null;
   protected FileUtils fileUtils = FileUtils.getFileUtils();
   private Vector filterChains = new Vector();
   private Vector filterSets = new Vector();
   private String inputEncoding = null;
   private String outputEncoding = null;
   private long granularity = 0L;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$Copy;
   // $FF: synthetic field
   static Class class$java$io$IOException;

   public Copy() {
      this.granularity = this.fileUtils.getFileTimestampGranularity();
   }

   protected FileUtils getFileUtils() {
      return this.fileUtils;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public void setTofile(File destFile) {
      this.destFile = destFile;
   }

   public void setTodir(File destDir) {
      this.destDir = destDir;
   }

   public FilterChain createFilterChain() {
      FilterChain filterChain = new FilterChain();
      this.filterChains.addElement(filterChain);
      return filterChain;
   }

   public FilterSet createFilterSet() {
      FilterSet filterSet = new FilterSet();
      this.filterSets.addElement(filterSet);
      return filterSet;
   }

   /** @deprecated */
   public void setPreserveLastModified(String preserve) {
      this.setPreserveLastModified(Project.toBoolean(preserve));
   }

   public void setPreserveLastModified(boolean preserve) {
      this.preserveLastModified = preserve;
   }

   public boolean getPreserveLastModified() {
      return this.preserveLastModified;
   }

   protected Vector getFilterSets() {
      return this.filterSets;
   }

   protected Vector getFilterChains() {
      return this.filterChains;
   }

   public void setFiltering(boolean filtering) {
      this.filtering = filtering;
   }

   public void setOverwrite(boolean overwrite) {
      this.forceOverwrite = overwrite;
   }

   public void setFlatten(boolean flatten) {
      this.flatten = flatten;
   }

   public void setVerbose(boolean verbose) {
      this.verbosity = verbose ? 2 : 3;
   }

   public void setIncludeEmptyDirs(boolean includeEmpty) {
      this.includeEmpty = includeEmpty;
   }

   public void setEnableMultipleMappings(boolean enableMultipleMappings) {
      this.enableMultipleMappings = enableMultipleMappings;
   }

   public boolean isEnableMultipleMapping() {
      return this.enableMultipleMappings;
   }

   public void setFailOnError(boolean failonerror) {
      this.failonerror = failonerror;
   }

   public void addFileset(FileSet set) {
      this.add((ResourceCollection)set);
   }

   public void add(ResourceCollection res) {
      this.rcs.add(res);
   }

   public Mapper createMapper() throws BuildException {
      if (this.mapperElement != null) {
         throw new BuildException("Cannot define more than one mapper", this.getLocation());
      } else {
         this.mapperElement = new Mapper(this.getProject());
         return this.mapperElement;
      }
   }

   public void add(FileNameMapper fileNameMapper) {
      this.createMapper().add(fileNameMapper);
   }

   public void setEncoding(String encoding) {
      this.inputEncoding = encoding;
      if (this.outputEncoding == null) {
         this.outputEncoding = encoding;
      }

   }

   public String getEncoding() {
      return this.inputEncoding;
   }

   public void setOutputEncoding(String encoding) {
      this.outputEncoding = encoding;
   }

   public String getOutputEncoding() {
      return this.outputEncoding;
   }

   public void setGranularity(long granularity) {
      this.granularity = granularity;
   }

   public void execute() throws BuildException {
      File savedFile = this.file;
      File savedDestFile = this.destFile;
      File savedDestDir = this.destDir;
      ResourceCollection savedRc = null;
      if (this.file == null && this.destFile != null && this.rcs.size() == 1) {
         savedRc = (ResourceCollection)this.rcs.elementAt(0);
      }

      this.validateAttributes();

      try {
         if (this.file != null) {
            if (this.file.exists()) {
               if (this.destFile == null) {
                  this.destFile = new File(this.destDir, this.file.getName());
               }

               if (!this.forceOverwrite && this.destFile.exists() && this.file.lastModified() - this.granularity <= this.destFile.lastModified()) {
                  this.log(this.file + " omitted as " + this.destFile + " is up to date.", 3);
               } else {
                  this.fileCopyMap.put(this.file.getAbsolutePath(), new String[]{this.destFile.getAbsolutePath()});
               }
            } else {
               String message = "Warning: Could not find file " + this.file.getAbsolutePath() + " to copy.";
               if (this.failonerror) {
                  throw new BuildException(message);
               }

               this.log(message, 0);
            }
         }

         HashMap filesByBasedir = new HashMap();
         HashMap dirsByBasedir = new HashMap();
         HashSet baseDirs = new HashSet();
         ArrayList nonFileResources = new ArrayList();

         String[] srcFiles;
         label353:
         for(int i = 0; i < this.rcs.size(); ++i) {
            ResourceCollection rc = (ResourceCollection)this.rcs.elementAt(i);
            Resource r;
            File baseDir;
            if (rc instanceof FileSet && rc.isFilesystemOnly()) {
               FileSet fs = (FileSet)rc;
               r = null;

               DirectoryScanner ds;
               try {
                  ds = fs.getDirectoryScanner(this.getProject());
               } catch (BuildException var22) {
                  if (!this.failonerror && this.getMessage(var22).endsWith(" not found.")) {
                     this.log("Warning: " + this.getMessage(var22), 0);
                     continue;
                  }

                  throw var22;
               }

               baseDir = fs.getDir(this.getProject());
               srcFiles = ds.getIncludedFiles();
               String[] srcDirs = ds.getIncludedDirectories();
               if (!this.flatten && this.mapperElement == null && ds.isEverythingIncluded() && !fs.hasPatterns()) {
                  this.completeDirMap.put(baseDir, this.destDir);
               }

               add(baseDir, (String[])srcFiles, filesByBasedir);
               add(baseDir, (String[])srcDirs, dirsByBasedir);
               baseDirs.add(baseDir);
            } else {
               if (!rc.isFilesystemOnly() && !this.supportsNonFileResources()) {
                  throw new BuildException("Only FileSystem resources are supported.");
               }

               Iterator resources = rc.iterator();

               while(true) {
                  while(true) {
                     do {
                        if (!resources.hasNext()) {
                           continue label353;
                        }

                        r = (Resource)resources.next();
                     } while(!r.isExists());

                     baseDir = NULL_FILE_PLACEHOLDER;
                     String name = r.getName();
                     if (r instanceof FileResource) {
                        FileResource fr = (FileResource)r;
                        baseDir = getKeyFile(fr.getBaseDir());
                        if (fr.getBaseDir() == null) {
                           name = fr.getFile().getAbsolutePath();
                        }
                     }

                     if (!r.isDirectory() && !(r instanceof FileResource)) {
                        nonFileResources.add(r);
                     } else {
                        add(baseDir, (String)name, r.isDirectory() ? dirsByBasedir : filesByBasedir);
                        baseDirs.add(baseDir);
                     }
                  }
               }
            }
         }

         File f;
         String[] srcFiles;
         for(Iterator iter = baseDirs.iterator(); iter.hasNext(); this.scan(f == NULL_FILE_PLACEHOLDER ? null : f, this.destDir, srcFiles, srcFiles)) {
            f = (File)iter.next();
            List files = (List)filesByBasedir.get(f);
            List dirs = (List)dirsByBasedir.get(f);
            srcFiles = new String[0];
            if (files != null) {
               srcFiles = (String[])((String[])files.toArray(srcFiles));
            }

            srcFiles = new String[0];
            if (dirs != null) {
               srcFiles = (String[])((String[])dirs.toArray(srcFiles));
            }
         }

         try {
            this.doFileOperations();
         } catch (BuildException var23) {
            if (this.failonerror) {
               throw var23;
            }

            this.log("Warning: " + this.getMessage(var23), 0);
         }

         if (nonFileResources.size() > 0) {
            Resource[] nonFiles = (Resource[])((Resource[])nonFileResources.toArray(new Resource[nonFileResources.size()]));
            Map map = this.scan(nonFiles, this.destDir);

            try {
               this.doResourceOperations(map);
            } catch (BuildException var21) {
               if (this.failonerror) {
                  throw var21;
               }

               this.log("Warning: " + this.getMessage(var21), 0);
            }
         }
      } finally {
         this.file = savedFile;
         this.destFile = savedDestFile;
         this.destDir = savedDestDir;
         if (savedRc != null) {
            this.rcs.insertElementAt(savedRc, 0);
         }

         this.fileCopyMap.clear();
         this.dirCopyMap.clear();
         this.completeDirMap.clear();
      }

   }

   protected void validateAttributes() throws BuildException {
      if (this.file == null && this.rcs.size() == 0) {
         throw new BuildException("Specify at least one source--a file or a resource collection.");
      } else if (this.destFile != null && this.destDir != null) {
         throw new BuildException("Only one of tofile and todir may be set.");
      } else if (this.destFile == null && this.destDir == null) {
         throw new BuildException("One of tofile or todir must be set.");
      } else if (this.file != null && this.file.isDirectory()) {
         throw new BuildException("Use a resource collection to copy directories.");
      } else {
         if (this.destFile != null && this.rcs.size() > 0) {
            if (this.rcs.size() > 1) {
               throw new BuildException("Cannot concatenate multiple files into a single file.");
            }

            ResourceCollection rc = (ResourceCollection)this.rcs.elementAt(0);
            if (!rc.isFilesystemOnly()) {
               throw new BuildException("Only FileSystem resources are supported when concatenating files.");
            }

            if (rc.size() == 0) {
               throw new BuildException("Cannot perform operation from directory to file.");
            }

            if (rc.size() != 1) {
               throw new BuildException("Cannot concatenate multiple files into a single file.");
            }

            FileResource r = (FileResource)rc.iterator().next();
            if (this.file != null) {
               throw new BuildException("Cannot concatenate multiple files into a single file.");
            }

            this.file = r.getFile();
            this.rcs.removeElementAt(0);
         }

         if (this.destFile != null) {
            this.destDir = this.destFile.getParentFile();
         }

      }
   }

   protected void scan(File fromDir, File toDir, String[] files, String[] dirs) {
      FileNameMapper mapper = this.getMapper();
      this.buildMap(fromDir, toDir, files, mapper, this.fileCopyMap);
      if (this.includeEmpty) {
         this.buildMap(fromDir, toDir, dirs, mapper, this.dirCopyMap);
      }

   }

   protected Map scan(Resource[] fromResources, File toDir) {
      return this.buildMap(fromResources, toDir, this.getMapper());
   }

   protected void buildMap(File fromDir, File toDir, String[] names, FileNameMapper mapper, Hashtable map) {
      String[] toCopy = null;
      if (this.forceOverwrite) {
         Vector v = new Vector();

         for(int i = 0; i < names.length; ++i) {
            if (mapper.mapFileName(names[i]) != null) {
               v.addElement(names[i]);
            }
         }

         toCopy = new String[v.size()];
         v.copyInto(toCopy);
      } else {
         SourceFileScanner ds = new SourceFileScanner(this);
         toCopy = ds.restrict(names, fromDir, toDir, mapper, this.granularity);
      }

      for(int i = 0; i < toCopy.length; ++i) {
         File src = new File(fromDir, toCopy[i]);
         String[] mappedFiles = mapper.mapFileName(toCopy[i]);
         if (!this.enableMultipleMappings) {
            map.put(src.getAbsolutePath(), new String[]{(new File(toDir, mappedFiles[0])).getAbsolutePath()});
         } else {
            for(int k = 0; k < mappedFiles.length; ++k) {
               mappedFiles[k] = (new File(toDir, mappedFiles[k])).getAbsolutePath();
            }

            map.put(src.getAbsolutePath(), mappedFiles);
         }
      }

   }

   protected Map buildMap(Resource[] fromResources, final File toDir, FileNameMapper mapper) {
      HashMap map = new HashMap();
      Resource[] toCopy = null;
      if (this.forceOverwrite) {
         Vector v = new Vector();

         for(int i = 0; i < fromResources.length; ++i) {
            if (mapper.mapFileName(fromResources[i].getName()) != null) {
               v.addElement(fromResources[i]);
            }
         }

         toCopy = new Resource[v.size()];
         v.copyInto(toCopy);
      } else {
         toCopy = ResourceUtils.selectOutOfDateSources(this, (Resource[])fromResources, mapper, new ResourceFactory() {
            public Resource getResource(String name) {
               return new FileResource(toDir, name);
            }
         }, this.granularity);
      }

      for(int i = 0; i < toCopy.length; ++i) {
         String[] mappedFiles = mapper.mapFileName(toCopy[i].getName());
         if (!this.enableMultipleMappings) {
            map.put(toCopy[i], new String[]{(new File(toDir, mappedFiles[0])).getAbsolutePath()});
         } else {
            for(int k = 0; k < mappedFiles.length; ++k) {
               mappedFiles[k] = (new File(toDir, mappedFiles[k])).getAbsolutePath();
            }

            map.put(toCopy[i], mappedFiles);
         }
      }

      return map;
   }

   protected void doFileOperations() {
      Enumeration e;
      String[] dirs;
      int i;
      if (this.fileCopyMap.size() > 0) {
         this.log("Copying " + this.fileCopyMap.size() + " file" + (this.fileCopyMap.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
         e = this.fileCopyMap.keys();

         while(e.hasMoreElements()) {
            String fromFile = (String)e.nextElement();
            dirs = (String[])((String[])this.fileCopyMap.get(fromFile));

            for(i = 0; i < dirs.length; ++i) {
               String toFile = dirs[i];
               if (fromFile.equals(toFile)) {
                  this.log("Skipping self-copy of " + fromFile, this.verbosity);
               } else {
                  try {
                     this.log("Copying " + fromFile + " to " + toFile, this.verbosity);
                     FilterSetCollection executionFilters = new FilterSetCollection();
                     if (this.filtering) {
                        executionFilters.addFilterSet(this.getProject().getGlobalFilterSet());
                     }

                     Enumeration filterEnum = this.filterSets.elements();

                     while(filterEnum.hasMoreElements()) {
                        executionFilters.addFilterSet((FilterSet)filterEnum.nextElement());
                     }

                     this.fileUtils.copyFile(fromFile, toFile, executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, this.inputEncoding, this.outputEncoding, this.getProject());
                  } catch (IOException var9) {
                     String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + this.getDueTo(var9);
                     File targetFile = new File(toFile);
                     if (targetFile.exists() && !targetFile.delete()) {
                        msg = msg + " and I couldn't delete the corrupt " + toFile;
                     }

                     if (this.failonerror) {
                        throw new BuildException(msg, var9, this.getLocation());
                     }

                     this.log(msg, 0);
                  }
               }
            }
         }
      }

      if (this.includeEmpty) {
         e = this.dirCopyMap.elements();
         int createCount = 0;

         while(e.hasMoreElements()) {
            dirs = (String[])((String[])e.nextElement());

            for(i = 0; i < dirs.length; ++i) {
               File d = new File(dirs[i]);
               if (!d.exists()) {
                  if (!d.mkdirs()) {
                     this.log("Unable to create directory " + d.getAbsolutePath(), 0);
                  } else {
                     ++createCount;
                  }
               }
            }
         }

         if (createCount > 0) {
            this.log("Copied " + this.dirCopyMap.size() + " empty director" + (this.dirCopyMap.size() == 1 ? "y" : "ies") + " to " + createCount + " empty director" + (createCount == 1 ? "y" : "ies") + " under " + this.destDir.getAbsolutePath());
         }
      }

   }

   protected void doResourceOperations(Map map) {
      if (map.size() > 0) {
         this.log("Copying " + map.size() + " resource" + (map.size() == 1 ? "" : "s") + " to " + this.destDir.getAbsolutePath());
         Iterator iter = map.keySet().iterator();

         while(iter.hasNext()) {
            Resource fromResource = (Resource)iter.next();
            String[] toFiles = (String[])((String[])map.get(fromResource));

            for(int i = 0; i < toFiles.length; ++i) {
               String toFile = toFiles[i];

               try {
                  this.log("Copying " + fromResource + " to " + toFile, this.verbosity);
                  FilterSetCollection executionFilters = new FilterSetCollection();
                  if (this.filtering) {
                     executionFilters.addFilterSet(this.getProject().getGlobalFilterSet());
                  }

                  Enumeration filterEnum = this.filterSets.elements();

                  while(filterEnum.hasMoreElements()) {
                     executionFilters.addFilterSet((FilterSet)filterEnum.nextElement());
                  }

                  ResourceUtils.copyResource(fromResource, new FileResource(this.destDir, toFile), executionFilters, this.filterChains, this.forceOverwrite, this.preserveLastModified, this.inputEncoding, this.outputEncoding, this.getProject());
               } catch (IOException var10) {
                  String msg = "Failed to copy " + fromResource + " to " + toFile + " due to " + this.getDueTo(var10);
                  File targetFile = new File(toFile);
                  if (targetFile.exists() && !targetFile.delete()) {
                     msg = msg + " and I couldn't delete the corrupt " + toFile;
                  }

                  if (this.failonerror) {
                     throw new BuildException(msg, var10, this.getLocation());
                  }

                  this.log(msg, 0);
               }
            }
         }
      }

   }

   protected boolean supportsNonFileResources() {
      return this.getClass().equals(class$org$apache$tools$ant$taskdefs$Copy == null ? (class$org$apache$tools$ant$taskdefs$Copy = class$("org.apache.tools.ant.taskdefs.Copy")) : class$org$apache$tools$ant$taskdefs$Copy);
   }

   private static void add(File baseDir, String[] names, Map m) {
      if (names != null) {
         baseDir = getKeyFile(baseDir);
         List l = (List)m.get(baseDir);
         if (l == null) {
            l = new ArrayList(names.length);
            m.put(baseDir, l);
         }

         ((List)l).addAll(Arrays.asList(names));
      }

   }

   private static void add(File baseDir, String name, Map m) {
      if (name != null) {
         add(baseDir, new String[]{name}, m);
      }

   }

   private static File getKeyFile(File f) {
      return f == null ? NULL_FILE_PLACEHOLDER : f;
   }

   private FileNameMapper getMapper() {
      FileNameMapper mapper = null;
      if (this.mapperElement != null) {
         mapper = this.mapperElement.getImplementation();
      } else if (this.flatten) {
         mapper = new FlatFileNameMapper();
      } else {
         mapper = new IdentityMapper();
      }

      return (FileNameMapper)mapper;
   }

   private String getMessage(Exception ex) {
      return ex.getMessage() == null ? ex.toString() : ex.getMessage();
   }

   private String getDueTo(Exception ex) {
      boolean baseIOException = ex.getClass() == (class$java$io$IOException == null ? (class$java$io$IOException = class$("java.io.IOException")) : class$java$io$IOException);
      StringBuffer message = new StringBuffer();
      if (!baseIOException || ex.getMessage() == null) {
         message.append(ex.getClass().getName());
      }

      if (ex.getMessage() != null) {
         if (!baseIOException) {
            message.append(" ");
         }

         message.append(ex.getMessage());
      }

      if (ex.getClass().getName().indexOf("MalformedInput") != -1) {
         message.append(LINE_SEPARATOR);
         message.append("This is normally due to the input file containing invalid");
         message.append(LINE_SEPARATOR);
         message.append("bytes for the character encoding used : ");
         message.append(this.inputEncoding == null ? this.fileUtils.getDefaultEncoding() : this.inputEncoding);
         message.append(LINE_SEPARATOR);
      }

      return message.toString();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
