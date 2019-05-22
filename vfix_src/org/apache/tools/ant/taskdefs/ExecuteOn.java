package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.AbstractFileSet;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.SourceFileScanner;

public class ExecuteOn extends ExecTask {
   protected Vector filesets = new Vector();
   private Union resources = null;
   private boolean relative = false;
   private boolean parallel = false;
   private boolean forwardSlash = false;
   protected String type = "file";
   protected Commandline.Marker srcFilePos = null;
   private boolean skipEmpty = false;
   protected Commandline.Marker targetFilePos = null;
   protected Mapper mapperElement = null;
   protected FileNameMapper mapper = null;
   protected File destDir = null;
   private int maxParallel = -1;
   private boolean addSourceFile = true;
   private boolean verbose = false;
   private boolean ignoreMissing = true;
   private boolean force = false;
   protected boolean srcIsFirst = true;

   public void addFileset(FileSet set) {
      this.filesets.addElement(set);
   }

   public void addDirset(DirSet set) {
      this.filesets.addElement(set);
   }

   public void addFilelist(FileList list) {
      this.add((ResourceCollection)list);
   }

   public void add(ResourceCollection rc) {
      if (this.resources == null) {
         this.resources = new Union();
      }

      this.resources.add(rc);
   }

   public void setRelative(boolean relative) {
      this.relative = relative;
   }

   public void setParallel(boolean parallel) {
      this.parallel = parallel;
   }

   public void setType(ExecuteOn.FileDirBoth type) {
      this.type = type.getValue();
   }

   public void setSkipEmptyFilesets(boolean skip) {
      this.skipEmpty = skip;
   }

   public void setDest(File destDir) {
      this.destDir = destDir;
   }

   public void setForwardslash(boolean forwardSlash) {
      this.forwardSlash = forwardSlash;
   }

   public void setMaxParallel(int max) {
      this.maxParallel = max;
   }

   public void setAddsourcefile(boolean b) {
      this.addSourceFile = b;
   }

   public void setVerbose(boolean b) {
      this.verbose = b;
   }

   public void setIgnoremissing(boolean b) {
      this.ignoreMissing = b;
   }

   public void setForce(boolean b) {
      this.force = b;
   }

   public Commandline.Marker createSrcfile() {
      if (this.srcFilePos != null) {
         throw new BuildException(this.getTaskType() + " doesn't support multiple " + "srcfile elements.", this.getLocation());
      } else {
         this.srcFilePos = this.cmdl.createMarker();
         return this.srcFilePos;
      }
   }

   public Commandline.Marker createTargetfile() {
      if (this.targetFilePos != null) {
         throw new BuildException(this.getTaskType() + " doesn't support multiple " + "targetfile elements.", this.getLocation());
      } else {
         this.targetFilePos = this.cmdl.createMarker();
         this.srcIsFirst = this.srcFilePos != null;
         return this.targetFilePos;
      }
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

   protected void checkConfiguration() {
      if ("execon".equals(this.getTaskName())) {
         this.log("!! execon is deprecated. Use apply instead. !!");
      }

      super.checkConfiguration();
      if (this.filesets.size() == 0 && this.resources == null) {
         throw new BuildException("no resources specified", this.getLocation());
      } else if (this.targetFilePos != null && this.mapperElement == null) {
         throw new BuildException("targetfile specified without mapper", this.getLocation());
      } else if (this.destDir != null && this.mapperElement == null) {
         throw new BuildException("dest specified without mapper", this.getLocation());
      } else {
         if (this.mapperElement != null) {
            this.mapper = this.mapperElement.getImplementation();
         }

      }
   }

   protected ExecuteStreamHandler createHandler() throws BuildException {
      return (ExecuteStreamHandler)(this.redirectorElement == null ? super.createHandler() : new PumpStreamHandler());
   }

   protected void setupRedirector() {
      super.setupRedirector();
      this.redirector.setAppendProperties(true);
   }

   protected void runExec(Execute exe) throws BuildException {
      int totalFiles = 0;
      int totalDirs = 0;
      boolean haveExecuted = false;

      try {
         Vector fileNames = new Vector();
         Vector baseDirs = new Vector();

         for(int i = 0; i < this.filesets.size(); ++i) {
            String currentType = this.type;
            AbstractFileSet fs = (AbstractFileSet)this.filesets.elementAt(i);
            if (fs instanceof DirSet && !"dir".equals(this.type)) {
               this.log("Found a nested dirset but type is " + this.type + ". " + "Temporarily switching to type=\"dir\" on the" + " assumption that you really did mean" + " <dirset> not <fileset>.", 4);
               currentType = "dir";
            }

            File base = fs.getDir(this.getProject());
            DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
            String[] s;
            int j;
            if (!"dir".equals(currentType)) {
               s = this.getFiles(base, ds);

               for(j = 0; j < s.length; ++j) {
                  ++totalFiles;
                  fileNames.addElement(s[j]);
                  baseDirs.addElement(base);
               }
            }

            if (!"file".equals(currentType)) {
               s = this.getDirs(base, ds);

               for(j = 0; j < s.length; ++j) {
                  ++totalDirs;
                  fileNames.addElement(s[j]);
                  baseDirs.addElement(base);
               }
            }

            if (fileNames.size() == 0 && this.skipEmpty) {
               int includedCount = (!"dir".equals(currentType) ? ds.getIncludedFilesCount() : 0) + (!"file".equals(currentType) ? ds.getIncludedDirsCount() : 0);
               this.log("Skipping fileset for directory " + base + ". It is " + (includedCount > 0 ? "up to date." : "empty."), 2);
            } else if (!this.parallel) {
               s = new String[fileNames.size()];
               fileNames.copyInto(s);

               for(j = 0; j < s.length; ++j) {
                  String[] command = this.getCommandline(s[j], base);
                  this.log(Commandline.describeCommand(command), 3);
                  exe.setCommandline(command);
                  if (this.redirectorElement != null) {
                     this.setupRedirector();
                     this.redirectorElement.configure(this.redirector, s[j]);
                  }

                  if (this.redirectorElement != null || haveExecuted) {
                     exe.setStreamHandler(this.redirector.createHandler());
                  }

                  this.runExecute(exe);
                  haveExecuted = true;
               }

               fileNames.removeAllElements();
               baseDirs.removeAllElements();
            }
         }

         if (this.resources != null) {
            Iterator iter = this.resources.iterator();

            label402:
            while(true) {
               File base;
               String name;
               do {
                  while(true) {
                     Resource res;
                     do {
                        do {
                           if (!iter.hasNext()) {
                              break label402;
                           }

                           res = (Resource)iter.next();
                        } while(!res.isExists() && this.ignoreMissing);

                        base = null;
                        name = res.getName();
                        if (res instanceof FileResource) {
                           FileResource fr = (FileResource)res;
                           base = fr.getBaseDir();
                           if (base == null) {
                              name = fr.getFile().getAbsolutePath();
                           }
                        }
                     } while(this.restrict(new String[]{name}, base).length == 0);

                     if ((!res.isDirectory() || !res.isExists()) && !"dir".equals(this.type)) {
                        ++totalFiles;
                        break;
                     }

                     if (res.isDirectory() && !"file".equals(this.type)) {
                        ++totalDirs;
                        break;
                     }
                  }

                  baseDirs.add(base);
                  fileNames.add(name);
               } while(this.parallel);

               String[] command = this.getCommandline(name, base);
               this.log(Commandline.describeCommand(command), 3);
               exe.setCommandline(command);
               if (this.redirectorElement != null) {
                  this.setupRedirector();
                  this.redirectorElement.configure(this.redirector, name);
               }

               if (this.redirectorElement != null || haveExecuted) {
                  exe.setStreamHandler(this.redirector.createHandler());
               }

               this.runExecute(exe);
               haveExecuted = true;
               fileNames.removeAllElements();
               baseDirs.removeAllElements();
            }
         }

         if (this.parallel && (fileNames.size() > 0 || !this.skipEmpty)) {
            this.runParallel(exe, fileNames, baseDirs);
            haveExecuted = true;
         }

         if (haveExecuted) {
            this.log("Applied " + this.cmdl.getExecutable() + " to " + totalFiles + " file" + (totalFiles != 1 ? "s" : "") + " and " + totalDirs + " director" + (totalDirs != 1 ? "ies" : "y") + ".", this.verbose ? 2 : 3);
         }
      } catch (IOException var18) {
         throw new BuildException("Execute failed: " + var18, var18, this.getLocation());
      } finally {
         this.logFlush();
         this.redirector.setAppendProperties(false);
         this.redirector.setProperties();
      }

   }

   protected String[] getCommandline(String[] srcFiles, File[] baseDirs) {
      char fileSeparator = File.separatorChar;
      Vector targets = new Vector();
      String[] result;
      int srcIndex;
      if (this.targetFilePos != null) {
         Hashtable addedFiles = new Hashtable();

         for(int i = 0; i < srcFiles.length; ++i) {
            result = this.mapper.mapFileName(srcFiles[i]);
            if (result != null) {
               for(srcIndex = 0; srcIndex < result.length; ++srcIndex) {
                  String name = null;
                  if (!this.relative) {
                     name = (new File(this.destDir, result[srcIndex])).getAbsolutePath();
                  } else {
                     name = result[srcIndex];
                  }

                  if (this.forwardSlash && fileSeparator != '/') {
                     name = name.replace(fileSeparator, '/');
                  }

                  if (!addedFiles.contains(name)) {
                     targets.addElement(name);
                     addedFiles.put(name, name);
                  }
               }
            }
         }
      }

      String[] targetFiles = new String[targets.size()];
      targets.copyInto(targetFiles);
      if (!this.addSourceFile) {
         srcFiles = new String[0];
      }

      String[] orig = this.cmdl.getCommandline();
      result = new String[orig.length + srcFiles.length + targetFiles.length];
      srcIndex = orig.length;
      if (this.srcFilePos != null) {
         srcIndex = this.srcFilePos.getPosition();
      }

      int i;
      if (this.targetFilePos != null) {
         i = this.targetFilePos.getPosition();
         if (srcIndex >= i && (srcIndex != i || !this.srcIsFirst)) {
            System.arraycopy(orig, 0, result, 0, i);
            System.arraycopy(targetFiles, 0, result, i, targetFiles.length);
            System.arraycopy(orig, i, result, i + targetFiles.length, srcIndex - i);
            System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length + targetFiles.length, orig.length - srcIndex);
            srcIndex += targetFiles.length;
         } else {
            System.arraycopy(orig, 0, result, 0, srcIndex);
            System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, i - srcIndex);
            System.arraycopy(targetFiles, 0, result, i + srcFiles.length, targetFiles.length);
            System.arraycopy(orig, i, result, i + srcFiles.length + targetFiles.length, orig.length - i);
         }
      } else {
         System.arraycopy(orig, 0, result, 0, srcIndex);
         System.arraycopy(orig, srcIndex, result, srcIndex + srcFiles.length, orig.length - srcIndex);
      }

      for(i = 0; i < srcFiles.length; ++i) {
         if (!this.relative) {
            result[srcIndex + i] = (new File(baseDirs[i], srcFiles[i])).getAbsolutePath();
         } else {
            result[srcIndex + i] = srcFiles[i];
         }

         if (this.forwardSlash && fileSeparator != '/') {
            result[srcIndex + i] = result[srcIndex + i].replace(fileSeparator, '/');
         }
      }

      return result;
   }

   protected String[] getCommandline(String srcFile, File baseDir) {
      return this.getCommandline(new String[]{srcFile}, new File[]{baseDir});
   }

   protected String[] getFiles(File baseDir, DirectoryScanner ds) {
      return this.restrict(ds.getIncludedFiles(), baseDir);
   }

   protected String[] getDirs(File baseDir, DirectoryScanner ds) {
      return this.restrict(ds.getIncludedDirectories(), baseDir);
   }

   protected String[] getFilesAndDirs(FileList list) {
      return this.restrict(list.getFiles(this.getProject()), list.getDir(this.getProject()));
   }

   private String[] restrict(String[] s, File baseDir) {
      return this.mapper != null && !this.force ? (new SourceFileScanner(this)).restrict(s, baseDir, this.destDir, this.mapper) : s;
   }

   protected void runParallel(Execute exe, Vector fileNames, Vector baseDirs) throws IOException, BuildException {
      String[] s = new String[fileNames.size()];
      fileNames.copyInto(s);
      File[] b = new File[baseDirs.size()];
      baseDirs.copyInto(b);
      if (this.maxParallel > 0 && s.length != 0) {
         int stillToDo = fileNames.size();

         int currentAmount;
         for(int currentOffset = 0; stillToDo > 0; currentOffset += currentAmount) {
            currentAmount = Math.min(stillToDo, this.maxParallel);
            String[] cs = new String[currentAmount];
            System.arraycopy(s, currentOffset, cs, 0, currentAmount);
            File[] cb = new File[currentAmount];
            System.arraycopy(b, currentOffset, cb, 0, currentAmount);
            String[] command = this.getCommandline(cs, cb);
            this.log(Commandline.describeCommand(command), 3);
            exe.setCommandline(command);
            if (this.redirectorElement != null) {
               this.setupRedirector();
               this.redirectorElement.configure(this.redirector, (String)null);
            }

            if (this.redirectorElement != null || currentOffset > 0) {
               exe.setStreamHandler(this.redirector.createHandler());
            }

            this.runExecute(exe);
            stillToDo -= currentAmount;
         }
      } else {
         String[] command = this.getCommandline(s, b);
         this.log(Commandline.describeCommand(command), 3);
         exe.setCommandline(command);
         this.runExecute(exe);
      }

   }

   public static class FileDirBoth extends EnumeratedAttribute {
      public static final String FILE = "file";
      public static final String DIR = "dir";

      public String[] getValues() {
         return new String[]{"file", "dir", "both"};
      }
   }
}
