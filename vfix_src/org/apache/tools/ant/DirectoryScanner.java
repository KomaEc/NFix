package org.apache.tools.ant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceFactory;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.SelectorScanner;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.apache.tools.ant.util.FileUtils;

public class DirectoryScanner implements FileScanner, SelectorScanner, ResourceFactory {
   private static final boolean ON_VMS = Os.isFamily("openvms");
   /** @deprecated */
   protected static final String[] DEFAULTEXCLUDES = new String[]{"**/*~", "**/#*#", "**/.#*", "**/%*%", "**/._*", "**/CVS", "**/CVS/**", "**/.cvsignore", "**/SCCS", "**/SCCS/**", "**/vssver.scc", "**/.svn", "**/.svn/**", "**/.DS_Store"};
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final boolean[] CS_SCAN_ONLY = new boolean[]{true};
   private static final boolean[] CS_THEN_NON_CS = new boolean[]{true, false};
   private static Vector defaultExcludes = new Vector();
   protected File basedir;
   protected String[] includes;
   protected String[] excludes;
   protected FileSelector[] selectors = null;
   protected Vector filesIncluded;
   protected Vector filesNotIncluded;
   protected Vector filesExcluded;
   protected Vector dirsIncluded;
   protected Vector dirsNotIncluded;
   protected Vector dirsExcluded;
   protected Vector filesDeselected;
   protected Vector dirsDeselected;
   protected boolean haveSlowResults = false;
   protected boolean isCaseSensitive = true;
   private boolean followSymlinks = true;
   protected boolean everythingIncluded = true;
   private Map fileListMap = new HashMap();
   private Set scannedDirs = new HashSet();
   private Set includeNonPatterns = new HashSet();
   private Set excludeNonPatterns = new HashSet();
   private String[] includePatterns;
   private String[] excludePatterns;
   private boolean areNonPatternSetsReady = false;
   private boolean scanning = false;
   private Object scanLock = new Object();
   private boolean slowScanning = false;
   private Object slowScanLock = new Object();
   private IllegalStateException illegal = null;

   protected static boolean matchPatternStart(String pattern, String str) {
      return SelectorUtils.matchPatternStart(pattern, str);
   }

   protected static boolean matchPatternStart(String pattern, String str, boolean isCaseSensitive) {
      return SelectorUtils.matchPatternStart(pattern, str, isCaseSensitive);
   }

   protected static boolean matchPath(String pattern, String str) {
      return SelectorUtils.matchPath(pattern, str);
   }

   protected static boolean matchPath(String pattern, String str, boolean isCaseSensitive) {
      return SelectorUtils.matchPath(pattern, str, isCaseSensitive);
   }

   public static boolean match(String pattern, String str) {
      return SelectorUtils.match(pattern, str);
   }

   protected static boolean match(String pattern, String str, boolean isCaseSensitive) {
      return SelectorUtils.match(pattern, str, isCaseSensitive);
   }

   public static String[] getDefaultExcludes() {
      return (String[])((String[])defaultExcludes.toArray(new String[defaultExcludes.size()]));
   }

   public static boolean addDefaultExclude(String s) {
      if (defaultExcludes.indexOf(s) == -1) {
         defaultExcludes.add(s);
         return true;
      } else {
         return false;
      }
   }

   public static boolean removeDefaultExclude(String s) {
      return defaultExcludes.remove(s);
   }

   public static void resetDefaultExcludes() {
      defaultExcludes = new Vector();

      for(int i = 0; i < DEFAULTEXCLUDES.length; ++i) {
         defaultExcludes.add(DEFAULTEXCLUDES[i]);
      }

   }

   public void setBasedir(String basedir) {
      this.setBasedir(basedir == null ? (File)null : new File(basedir.replace('/', File.separatorChar).replace('\\', File.separatorChar)));
   }

   public synchronized void setBasedir(File basedir) {
      this.basedir = basedir;
   }

   public synchronized File getBasedir() {
      return this.basedir;
   }

   public synchronized boolean isCaseSensitive() {
      return this.isCaseSensitive;
   }

   public synchronized void setCaseSensitive(boolean isCaseSensitive) {
      this.isCaseSensitive = isCaseSensitive;
   }

   public synchronized boolean isFollowSymlinks() {
      return this.followSymlinks;
   }

   public synchronized void setFollowSymlinks(boolean followSymlinks) {
      this.followSymlinks = followSymlinks;
   }

   public synchronized void setIncludes(String[] includes) {
      if (includes == null) {
         this.includes = null;
      } else {
         this.includes = new String[includes.length];

         for(int i = 0; i < includes.length; ++i) {
            this.includes[i] = normalizePattern(includes[i]);
         }
      }

   }

   public synchronized void setExcludes(String[] excludes) {
      if (excludes == null) {
         this.excludes = null;
      } else {
         this.excludes = new String[excludes.length];

         for(int i = 0; i < excludes.length; ++i) {
            this.excludes[i] = normalizePattern(excludes[i]);
         }
      }

   }

   public synchronized void addExcludes(String[] excludes) {
      if (excludes != null && excludes.length > 0) {
         if (this.excludes != null && this.excludes.length > 0) {
            String[] tmp = new String[excludes.length + this.excludes.length];
            System.arraycopy(this.excludes, 0, tmp, 0, this.excludes.length);

            for(int i = 0; i < excludes.length; ++i) {
               tmp[this.excludes.length + i] = normalizePattern(excludes[i]);
            }

            this.excludes = tmp;
         } else {
            this.setExcludes(excludes);
         }
      }

   }

   private static String normalizePattern(String p) {
      String pattern = p.replace('/', File.separatorChar).replace('\\', File.separatorChar);
      if (pattern.endsWith(File.separator)) {
         pattern = pattern + "**";
      }

      return pattern;
   }

   public synchronized void setSelectors(FileSelector[] selectors) {
      this.selectors = selectors;
   }

   public synchronized boolean isEverythingIncluded() {
      return this.everythingIncluded;
   }

   public void scan() throws IllegalStateException {
      synchronized(this.scanLock) {
         if (this.scanning) {
            while(this.scanning) {
               try {
                  this.scanLock.wait();
               } catch (InterruptedException var20) {
               }
            }

            if (this.illegal != null) {
               throw this.illegal;
            }

            return;
         }

         this.scanning = true;
      }

      boolean var18 = false;

      label268: {
         try {
            var18 = true;
            synchronized(this) {
               this.illegal = null;
               this.clearResults();
               boolean nullIncludes = this.includes == null;
               this.includes = nullIncludes ? new String[]{"**"} : this.includes;
               boolean nullExcludes = this.excludes == null;
               this.excludes = nullExcludes ? new String[0] : this.excludes;
               if (this.basedir == null) {
                  if (nullIncludes) {
                     var18 = false;
                     break label268;
                  }
               } else {
                  if (!this.basedir.exists()) {
                     this.illegal = new IllegalStateException("basedir " + this.basedir + " does not exist");
                  }

                  if (!this.basedir.isDirectory()) {
                     this.illegal = new IllegalStateException("basedir " + this.basedir + " is not a directory");
                  }

                  if (this.illegal != null) {
                     throw this.illegal;
                  }
               }

               if (this.isIncluded("")) {
                  if (!this.isExcluded("")) {
                     if (this.isSelected("", this.basedir)) {
                        this.dirsIncluded.addElement("");
                     } else {
                        this.dirsDeselected.addElement("");
                     }
                  } else {
                     this.dirsExcluded.addElement("");
                  }
               } else {
                  this.dirsNotIncluded.addElement("");
               }

               this.checkIncludePatterns();
               this.clearCaches();
               this.includes = nullIncludes ? null : this.includes;
               this.excludes = nullExcludes ? null : this.excludes;
               var18 = false;
            }
         } finally {
            if (var18) {
               synchronized(this.scanLock) {
                  this.scanning = false;
                  this.scanLock.notifyAll();
               }
            }
         }

         synchronized(this.scanLock) {
            this.scanning = false;
            this.scanLock.notifyAll();
            return;
         }
      }

      synchronized(this.scanLock) {
         this.scanning = false;
         this.scanLock.notifyAll();
      }
   }

   private void checkIncludePatterns() {
      Map newroots = new HashMap();

      for(int i = 0; i < this.includes.length; ++i) {
         if (FileUtils.isAbsolutePath(this.includes[i])) {
            if (this.basedir != null && !SelectorUtils.matchPatternStart(this.includes[i], this.basedir.getAbsolutePath(), this.isCaseSensitive())) {
               continue;
            }
         } else if (this.basedir == null) {
            continue;
         }

         newroots.put(SelectorUtils.rtrimWildcardTokens(this.includes[i]), this.includes[i]);
      }

      if (newroots.containsKey("") && this.basedir != null) {
         this.scandir(this.basedir, "", true);
      } else {
         Iterator it = newroots.entrySet().iterator();
         File canonBase = null;
         if (this.basedir != null) {
            try {
               canonBase = this.basedir.getCanonicalFile();
            } catch (IOException var9) {
               throw new BuildException(var9);
            }
         }

         while(true) {
            while(true) {
               while(true) {
                  String currentelement;
                  String originalpattern;
                  File myfile;
                  do {
                     do {
                        do {
                           Entry entry;
                           do {
                              if (!it.hasNext()) {
                                 return;
                              }

                              entry = (Entry)it.next();
                              currentelement = (String)entry.getKey();
                           } while(this.basedir == null && !FileUtils.isAbsolutePath(currentelement));

                           originalpattern = (String)entry.getValue();
                           myfile = new File(this.basedir, currentelement);
                           if (myfile.exists()) {
                              try {
                                 String path = this.basedir == null ? myfile.getCanonicalPath() : FILE_UTILS.removeLeadingPath(canonBase, myfile.getCanonicalFile());
                                 if (!path.equals(currentelement) || ON_VMS) {
                                    myfile = this.findFile(this.basedir, currentelement, true);
                                    if (myfile != null && this.basedir != null) {
                                       currentelement = FILE_UTILS.removeLeadingPath(this.basedir, myfile);
                                    }
                                 }
                              } catch (IOException var10) {
                                 throw new BuildException(var10);
                              }
                           }

                           if ((myfile == null || !myfile.exists()) && !this.isCaseSensitive()) {
                              File f = this.findFile(this.basedir, currentelement, false);
                              if (f != null && f.exists()) {
                                 currentelement = this.basedir == null ? f.getAbsolutePath() : FILE_UTILS.removeLeadingPath(this.basedir, f);
                                 myfile = f;
                              }
                           }
                        } while(myfile == null);
                     } while(!myfile.exists());
                  } while(!this.followSymlinks && this.isSymlink(this.basedir, currentelement));

                  if (myfile.isDirectory()) {
                     if (this.isIncluded(currentelement) && currentelement.length() > 0) {
                        this.accountForIncludedDir(currentelement, myfile, true);
                     } else {
                        if (currentelement.length() > 0 && currentelement.charAt(currentelement.length() - 1) != File.separatorChar) {
                           currentelement = currentelement + File.separatorChar;
                        }

                        this.scandir(myfile, currentelement, true);
                     }
                  } else {
                     boolean included = this.isCaseSensitive() ? originalpattern.equals(currentelement) : originalpattern.equalsIgnoreCase(currentelement);
                     if (included) {
                        this.accountForIncludedFile(currentelement, myfile);
                     }
                  }
               }
            }
         }
      }
   }

   protected synchronized void clearResults() {
      this.filesIncluded = new Vector();
      this.filesNotIncluded = new Vector();
      this.filesExcluded = new Vector();
      this.filesDeselected = new Vector();
      this.dirsIncluded = new Vector();
      this.dirsNotIncluded = new Vector();
      this.dirsExcluded = new Vector();
      this.dirsDeselected = new Vector();
      this.everythingIncluded = this.basedir != null;
      this.scannedDirs.clear();
   }

   protected void slowScan() {
      synchronized(this.slowScanLock) {
         if (this.haveSlowResults) {
            return;
         }

         if (this.slowScanning) {
            while(this.slowScanning) {
               try {
                  this.slowScanLock.wait();
               } catch (InterruptedException var19) {
               }
            }

            return;
         }

         this.slowScanning = true;
      }

      boolean var17 = false;

      try {
         var17 = true;
         synchronized(this) {
            boolean nullIncludes = this.includes == null;
            this.includes = nullIncludes ? new String[]{"**"} : this.includes;
            boolean nullExcludes = this.excludes == null;
            this.excludes = nullExcludes ? new String[0] : this.excludes;
            String[] excl = new String[this.dirsExcluded.size()];
            this.dirsExcluded.copyInto(excl);
            String[] notIncl = new String[this.dirsNotIncluded.size()];
            this.dirsNotIncluded.copyInto(notIncl);
            this.processSlowScan(excl);
            this.processSlowScan(notIncl);
            this.clearCaches();
            this.includes = nullIncludes ? null : this.includes;
            this.excludes = nullExcludes ? null : this.excludes;
            var17 = false;
         }
      } finally {
         if (var17) {
            synchronized(this.slowScanLock) {
               this.haveSlowResults = true;
               this.slowScanning = false;
               this.slowScanLock.notifyAll();
            }
         }
      }

      synchronized(this.slowScanLock) {
         this.haveSlowResults = true;
         this.slowScanning = false;
         this.slowScanLock.notifyAll();
      }
   }

   private void processSlowScan(String[] arr) {
      for(int i = 0; i < arr.length; ++i) {
         if (!this.couldHoldIncluded(arr[i])) {
            this.scandir(new File(this.basedir, arr[i]), arr[i] + File.separator, false);
         }
      }

   }

   protected void scandir(File dir, String vpath, boolean fast) {
      if (dir == null) {
         throw new BuildException("dir must not be null.");
      } else if (!dir.exists()) {
         throw new BuildException(dir + " doesn't exist.");
      } else if (!dir.isDirectory()) {
         throw new BuildException(dir + " is not a directory.");
      } else if (!fast || !this.hasBeenScanned(vpath)) {
         String[] newfiles = dir.list();
         if (newfiles == null) {
            throw new BuildException("IO error scanning directory '" + dir.getAbsolutePath() + "'");
         } else {
            if (!this.followSymlinks) {
               Vector noLinks = new Vector();

               for(int i = 0; i < newfiles.length; ++i) {
                  try {
                     if (FILE_UTILS.isSymbolicLink(dir, newfiles[i])) {
                        String name = vpath + newfiles[i];
                        File file = new File(dir, newfiles[i]);
                        (file.isDirectory() ? this.dirsExcluded : this.filesExcluded).addElement(name);
                     } else {
                        noLinks.addElement(newfiles[i]);
                     }
                  } catch (IOException var9) {
                     String msg = "IOException caught while checking for links, couldn't get canonical path!";
                     System.err.println(msg);
                     noLinks.addElement(newfiles[i]);
                  }
               }

               newfiles = (String[])((String[])noLinks.toArray(new String[noLinks.size()]));
            }

            for(int i = 0; i < newfiles.length; ++i) {
               String name = vpath + newfiles[i];
               File file = new File(dir, newfiles[i]);
               if (file.isDirectory()) {
                  if (this.isIncluded(name)) {
                     this.accountForIncludedDir(name, file, fast);
                  } else {
                     this.everythingIncluded = false;
                     this.dirsNotIncluded.addElement(name);
                     if (fast && this.couldHoldIncluded(name)) {
                        this.scandir(file, name + File.separator, fast);
                     }
                  }

                  if (!fast) {
                     this.scandir(file, name + File.separator, fast);
                  }
               } else if (file.isFile()) {
                  if (this.isIncluded(name)) {
                     this.accountForIncludedFile(name, file);
                  } else {
                     this.everythingIncluded = false;
                     this.filesNotIncluded.addElement(name);
                  }
               }
            }

         }
      }
   }

   private void accountForIncludedFile(String name, File file) {
      this.processIncluded(name, file, this.filesIncluded, this.filesExcluded, this.filesDeselected);
   }

   private void accountForIncludedDir(String name, File file, boolean fast) {
      this.processIncluded(name, file, this.dirsIncluded, this.dirsExcluded, this.dirsDeselected);
      if (fast && this.couldHoldIncluded(name) && !this.contentsExcluded(name)) {
         this.scandir(file, name + File.separator, fast);
      }

   }

   private void processIncluded(String name, File file, Vector inc, Vector exc, Vector des) {
      if (!inc.contains(name) && !exc.contains(name) && !des.contains(name)) {
         boolean included = false;
         if (this.isExcluded(name)) {
            exc.add(name);
         } else if (this.isSelected(name, file)) {
            included = true;
            inc.add(name);
         } else {
            des.add(name);
         }

         this.everythingIncluded &= included;
      }
   }

   protected boolean isIncluded(String name) {
      label25: {
         this.ensureNonPatternSetsReady();
         if (this.isCaseSensitive()) {
            if (!this.includeNonPatterns.contains(name)) {
               break label25;
            }
         } else if (!this.includeNonPatterns.contains(name.toUpperCase())) {
            break label25;
         }

         return true;
      }

      for(int i = 0; i < this.includePatterns.length; ++i) {
         if (matchPath(this.includePatterns[i], name, this.isCaseSensitive())) {
            return true;
         }
      }

      return false;
   }

   protected boolean couldHoldIncluded(String name) {
      for(int i = 0; i < this.includes.length; ++i) {
         if (matchPatternStart(this.includes[i], name, this.isCaseSensitive()) && this.isMorePowerfulThanExcludes(name, this.includes[i]) && this.isDeeper(this.includes[i], name)) {
            return true;
         }
      }

      return false;
   }

   private boolean isDeeper(String pattern, String name) {
      Vector p = SelectorUtils.tokenizePath(pattern);
      Vector n = SelectorUtils.tokenizePath(name);
      return p.contains("**") || p.size() > n.size();
   }

   private boolean isMorePowerfulThanExcludes(String name, String includepattern) {
      String soughtexclude = name + File.separator + "**";

      for(int counter = 0; counter < this.excludes.length; ++counter) {
         if (this.excludes[counter].equals(soughtexclude)) {
            return false;
         }
      }

      return true;
   }

   private boolean contentsExcluded(String name) {
      name = name.endsWith(File.separator) ? name : name + File.separator;

      for(int i = 0; i < this.excludes.length; ++i) {
         String e = this.excludes[i];
         if (e.endsWith("**") && SelectorUtils.matchPath(e.substring(0, e.length() - 2), name, this.isCaseSensitive())) {
            return true;
         }
      }

      return false;
   }

   protected boolean isExcluded(String name) {
      label25: {
         this.ensureNonPatternSetsReady();
         if (this.isCaseSensitive()) {
            if (!this.excludeNonPatterns.contains(name)) {
               break label25;
            }
         } else if (!this.excludeNonPatterns.contains(name.toUpperCase())) {
            break label25;
         }

         return true;
      }

      for(int i = 0; i < this.excludePatterns.length; ++i) {
         if (matchPath(this.excludePatterns[i], name, this.isCaseSensitive())) {
            return true;
         }
      }

      return false;
   }

   protected boolean isSelected(String name, File file) {
      if (this.selectors != null) {
         for(int i = 0; i < this.selectors.length; ++i) {
            if (!this.selectors[i].isSelected(this.basedir, name, file)) {
               return false;
            }
         }
      }

      return true;
   }

   public synchronized String[] getIncludedFiles() {
      if (this.filesIncluded == null) {
         throw new IllegalStateException("Must call scan() first");
      } else {
         String[] files = new String[this.filesIncluded.size()];
         this.filesIncluded.copyInto(files);
         Arrays.sort(files);
         return files;
      }
   }

   public synchronized int getIncludedFilesCount() {
      if (this.filesIncluded == null) {
         throw new IllegalStateException("Must call scan() first");
      } else {
         return this.filesIncluded.size();
      }
   }

   public synchronized String[] getNotIncludedFiles() {
      this.slowScan();
      String[] files = new String[this.filesNotIncluded.size()];
      this.filesNotIncluded.copyInto(files);
      return files;
   }

   public synchronized String[] getExcludedFiles() {
      this.slowScan();
      String[] files = new String[this.filesExcluded.size()];
      this.filesExcluded.copyInto(files);
      return files;
   }

   public synchronized String[] getDeselectedFiles() {
      this.slowScan();
      String[] files = new String[this.filesDeselected.size()];
      this.filesDeselected.copyInto(files);
      return files;
   }

   public synchronized String[] getIncludedDirectories() {
      if (this.dirsIncluded == null) {
         throw new IllegalStateException("Must call scan() first");
      } else {
         String[] directories = new String[this.dirsIncluded.size()];
         this.dirsIncluded.copyInto(directories);
         Arrays.sort(directories);
         return directories;
      }
   }

   public synchronized int getIncludedDirsCount() {
      if (this.dirsIncluded == null) {
         throw new IllegalStateException("Must call scan() first");
      } else {
         return this.dirsIncluded.size();
      }
   }

   public synchronized String[] getNotIncludedDirectories() {
      this.slowScan();
      String[] directories = new String[this.dirsNotIncluded.size()];
      this.dirsNotIncluded.copyInto(directories);
      return directories;
   }

   public synchronized String[] getExcludedDirectories() {
      this.slowScan();
      String[] directories = new String[this.dirsExcluded.size()];
      this.dirsExcluded.copyInto(directories);
      return directories;
   }

   public synchronized String[] getDeselectedDirectories() {
      this.slowScan();
      String[] directories = new String[this.dirsDeselected.size()];
      this.dirsDeselected.copyInto(directories);
      return directories;
   }

   public synchronized void addDefaultExcludes() {
      int excludesLength = this.excludes == null ? 0 : this.excludes.length;
      String[] newExcludes = new String[excludesLength + defaultExcludes.size()];
      if (excludesLength > 0) {
         System.arraycopy(this.excludes, 0, newExcludes, 0, excludesLength);
      }

      String[] defaultExcludesTemp = getDefaultExcludes();

      for(int i = 0; i < defaultExcludesTemp.length; ++i) {
         newExcludes[i + excludesLength] = defaultExcludesTemp[i].replace('/', File.separatorChar).replace('\\', File.separatorChar);
      }

      this.excludes = newExcludes;
   }

   public synchronized Resource getResource(String name) {
      return new FileResource(this.basedir, name);
   }

   private String[] list(File file) {
      String[] files = (String[])((String[])this.fileListMap.get(file));
      if (files == null) {
         files = file.list();
         if (files != null) {
            this.fileListMap.put(file, files);
         }
      }

      return files;
   }

   private File findFile(File base, String path, boolean cs) {
      if (FileUtils.isAbsolutePath(path)) {
         if (base == null) {
            String[] s = FILE_UTILS.dissect(path);
            base = new File(s[0]);
            path = s[1];
         } else {
            File f = FILE_UTILS.normalize(path);
            String s = FILE_UTILS.removeLeadingPath(base, f);
            if (s.equals(f.getAbsolutePath())) {
               return null;
            }

            path = s;
         }
      }

      return this.findFile(base, SelectorUtils.tokenizePath(path), cs);
   }

   private File findFile(File base, Vector pathElements, boolean cs) {
      if (pathElements.size() == 0) {
         return base;
      } else {
         String current = (String)pathElements.remove(0);
         if (base == null) {
            return this.findFile(new File(current), pathElements, cs);
         } else if (!base.isDirectory()) {
            return null;
         } else {
            String[] files = this.list(base);
            if (files == null) {
               throw new BuildException("IO error scanning directory " + base.getAbsolutePath());
            } else {
               boolean[] matchCase = cs ? CS_SCAN_ONLY : CS_THEN_NON_CS;

               for(int i = 0; i < matchCase.length; ++i) {
                  for(int j = 0; j < files.length; ++j) {
                     if (matchCase[i]) {
                        if (files[j].equals(current)) {
                           return this.findFile(new File(base, files[j]), pathElements, cs);
                        }
                     } else if (files[j].equalsIgnoreCase(current)) {
                        return this.findFile(new File(base, files[j]), pathElements, cs);
                     }
                  }
               }

               return null;
            }
         }
      }
   }

   private boolean isSymlink(File base, String path) {
      return this.isSymlink(base, SelectorUtils.tokenizePath(path));
   }

   private boolean isSymlink(File base, Vector pathElements) {
      if (pathElements.size() > 0) {
         String current = (String)pathElements.remove(0);

         try {
            return FILE_UTILS.isSymbolicLink(base, current) || this.isSymlink(new File(base, current), pathElements);
         } catch (IOException var6) {
            String msg = "IOException caught while checking for links, couldn't get canonical path!";
            System.err.println(msg);
         }
      }

      return false;
   }

   private boolean hasBeenScanned(String vpath) {
      return !this.scannedDirs.add(vpath);
   }

   Set getScannedDirs() {
      return this.scannedDirs;
   }

   private synchronized void clearCaches() {
      this.fileListMap.clear();
      this.includeNonPatterns.clear();
      this.excludeNonPatterns.clear();
      this.includePatterns = null;
      this.excludePatterns = null;
      this.areNonPatternSetsReady = false;
   }

   private synchronized void ensureNonPatternSetsReady() {
      if (!this.areNonPatternSetsReady) {
         this.includePatterns = this.fillNonPatternSet(this.includeNonPatterns, this.includes);
         this.excludePatterns = this.fillNonPatternSet(this.excludeNonPatterns, this.excludes);
         this.areNonPatternSetsReady = true;
      }

   }

   private String[] fillNonPatternSet(Set set, String[] patterns) {
      ArrayList al = new ArrayList(patterns.length);

      for(int i = 0; i < patterns.length; ++i) {
         if (!SelectorUtils.hasWildcards(patterns[i])) {
            set.add(this.isCaseSensitive() ? patterns[i] : patterns[i].toUpperCase());
         } else {
            al.add(patterns[i]);
         }
      }

      return set.size() == 0 ? patterns : (String[])((String[])al.toArray(new String[al.size()]));
   }

   static {
      resetDefaultExcludes();
   }
}
