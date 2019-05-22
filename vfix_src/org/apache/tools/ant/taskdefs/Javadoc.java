package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JavaEnvUtils;

public class Javadoc extends Task {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private Commandline cmd = new Commandline();
   private boolean failOnError = false;
   private Path sourcePath = null;
   private File destDir = null;
   private Vector sourceFiles = new Vector();
   private Vector packageNames = new Vector();
   private Vector excludePackageNames = new Vector(1);
   private boolean author = true;
   private boolean version = true;
   private Javadoc.DocletInfo doclet = null;
   private Path classpath = null;
   private Path bootclasspath = null;
   private String group = null;
   private String packageList = null;
   private Vector links = new Vector();
   private Vector groups = new Vector();
   private Vector tags = new Vector();
   private boolean useDefaultExcludes = true;
   private Javadoc.Html doctitle = null;
   private Javadoc.Html header = null;
   private Javadoc.Html footer = null;
   private Javadoc.Html bottom = null;
   private boolean useExternalFile = false;
   private String source = null;
   private boolean linksource = false;
   private boolean breakiterator = false;
   private String noqualifier;
   private boolean includeNoSourcePackages = false;
   private boolean old = false;
   private String executable = null;
   private Javadoc.ResourceCollectionContainer nestedSourceFiles = new Javadoc.ResourceCollectionContainer();
   private Vector packageSets = new Vector();
   static final String[] SCOPE_ELEMENTS = new String[]{"overview", "packages", "types", "constructors", "methods", "fields"};

   private void addArgIf(boolean b, String arg) {
      if (b) {
         this.cmd.createArgument().setValue(arg);
      }

   }

   private void addArgIfNotEmpty(String key, String value) {
      if (value != null && value.length() != 0) {
         this.cmd.createArgument().setValue(key);
         this.cmd.createArgument().setValue(value);
      } else {
         this.log("Warning: Leaving out empty argument '" + key + "'", 1);
      }

   }

   public void setUseExternalFile(boolean b) {
      this.useExternalFile = b;
   }

   public void setDefaultexcludes(boolean useDefaultExcludes) {
      this.useDefaultExcludes = useDefaultExcludes;
   }

   public void setMaxmemory(String max) {
      this.cmd.createArgument().setValue("-J-Xmx" + max);
   }

   public void setAdditionalparam(String add) {
      this.cmd.createArgument().setLine(add);
   }

   public Commandline.Argument createArg() {
      return this.cmd.createArgument();
   }

   public void setSourcepath(Path src) {
      if (this.sourcePath == null) {
         this.sourcePath = src;
      } else {
         this.sourcePath.append(src);
      }

   }

   public Path createSourcepath() {
      if (this.sourcePath == null) {
         this.sourcePath = new Path(this.getProject());
      }

      return this.sourcePath.createPath();
   }

   public void setSourcepathRef(Reference r) {
      this.createSourcepath().setRefid(r);
   }

   public void setDestdir(File dir) {
      this.destDir = dir;
      this.cmd.createArgument().setValue("-d");
      this.cmd.createArgument().setFile(this.destDir);
   }

   public void setSourcefiles(String src) {
      StringTokenizer tok = new StringTokenizer(src, ",");

      while(tok.hasMoreTokens()) {
         String f = tok.nextToken();
         Javadoc.SourceFile sf = new Javadoc.SourceFile();
         sf.setFile(this.getProject().resolveFile(f.trim()));
         this.addSource(sf);
      }

   }

   public void addSource(Javadoc.SourceFile sf) {
      this.sourceFiles.addElement(sf);
   }

   public void setPackagenames(String packages) {
      StringTokenizer tok = new StringTokenizer(packages, ",");

      while(tok.hasMoreTokens()) {
         String p = tok.nextToken();
         Javadoc.PackageName pn = new Javadoc.PackageName();
         pn.setName(p);
         this.addPackage(pn);
      }

   }

   public void addPackage(Javadoc.PackageName pn) {
      this.packageNames.addElement(pn);
   }

   public void setExcludePackageNames(String packages) {
      StringTokenizer tok = new StringTokenizer(packages, ",");

      while(tok.hasMoreTokens()) {
         String p = tok.nextToken();
         Javadoc.PackageName pn = new Javadoc.PackageName();
         pn.setName(p);
         this.addExcludePackage(pn);
      }

   }

   public void addExcludePackage(Javadoc.PackageName pn) {
      this.excludePackageNames.addElement(pn);
   }

   public void setOverview(File f) {
      this.cmd.createArgument().setValue("-overview");
      this.cmd.createArgument().setFile(f);
   }

   public void setPublic(boolean b) {
      this.addArgIf(b, "-public");
   }

   public void setProtected(boolean b) {
      this.addArgIf(b, "-protected");
   }

   public void setPackage(boolean b) {
      this.addArgIf(b, "-package");
   }

   public void setPrivate(boolean b) {
      this.addArgIf(b, "-private");
   }

   public void setAccess(Javadoc.AccessType at) {
      this.cmd.createArgument().setValue("-" + at.getValue());
   }

   public void setDoclet(String docletName) {
      if (this.doclet == null) {
         this.doclet = new Javadoc.DocletInfo();
         this.doclet.setProject(this.getProject());
      }

      this.doclet.setName(docletName);
   }

   public void setDocletPath(Path docletPath) {
      if (this.doclet == null) {
         this.doclet = new Javadoc.DocletInfo();
         this.doclet.setProject(this.getProject());
      }

      this.doclet.setPath(docletPath);
   }

   public void setDocletPathRef(Reference r) {
      if (this.doclet == null) {
         this.doclet = new Javadoc.DocletInfo();
         this.doclet.setProject(this.getProject());
      }

      this.doclet.createPath().setRefid(r);
   }

   public Javadoc.DocletInfo createDoclet() {
      if (this.doclet == null) {
         this.doclet = new Javadoc.DocletInfo();
      }

      return this.doclet;
   }

   public void addTaglet(Javadoc.ExtensionInfo tagletInfo) {
      this.tags.addElement(tagletInfo);
   }

   public void setOld(boolean b) {
      this.old = b;
   }

   public void setClasspath(Path path) {
      if (this.classpath == null) {
         this.classpath = path;
      } else {
         this.classpath.append(path);
      }

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

   public void setBootclasspath(Path path) {
      if (this.bootclasspath == null) {
         this.bootclasspath = path;
      } else {
         this.bootclasspath.append(path);
      }

   }

   public Path createBootclasspath() {
      if (this.bootclasspath == null) {
         this.bootclasspath = new Path(this.getProject());
      }

      return this.bootclasspath.createPath();
   }

   public void setBootClasspathRef(Reference r) {
      this.createBootclasspath().setRefid(r);
   }

   /** @deprecated */
   public void setExtdirs(String path) {
      this.cmd.createArgument().setValue("-extdirs");
      this.cmd.createArgument().setValue(path);
   }

   public void setExtdirs(Path path) {
      this.cmd.createArgument().setValue("-extdirs");
      this.cmd.createArgument().setPath(path);
   }

   public void setVerbose(boolean b) {
      this.addArgIf(b, "-verbose");
   }

   public void setLocale(String locale) {
      this.cmd.createArgument(true).setValue(locale);
      this.cmd.createArgument(true).setValue("-locale");
   }

   public void setEncoding(String enc) {
      this.cmd.createArgument().setValue("-encoding");
      this.cmd.createArgument().setValue(enc);
   }

   public void setVersion(boolean b) {
      this.version = b;
   }

   public void setUse(boolean b) {
      this.addArgIf(b, "-use");
   }

   public void setAuthor(boolean b) {
      this.author = b;
   }

   public void setSplitindex(boolean b) {
      this.addArgIf(b, "-splitindex");
   }

   public void setWindowtitle(String title) {
      this.addArgIfNotEmpty("-windowtitle", title);
   }

   public void setDoctitle(String doctitle) {
      Javadoc.Html h = new Javadoc.Html();
      h.addText(doctitle);
      this.addDoctitle(h);
   }

   public void addDoctitle(Javadoc.Html text) {
      this.doctitle = text;
   }

   public void setHeader(String header) {
      Javadoc.Html h = new Javadoc.Html();
      h.addText(header);
      this.addHeader(h);
   }

   public void addHeader(Javadoc.Html text) {
      this.header = text;
   }

   public void setFooter(String footer) {
      Javadoc.Html h = new Javadoc.Html();
      h.addText(footer);
      this.addFooter(h);
   }

   public void addFooter(Javadoc.Html text) {
      this.footer = text;
   }

   public void setBottom(String bottom) {
      Javadoc.Html h = new Javadoc.Html();
      h.addText(bottom);
      this.addBottom(h);
   }

   public void addBottom(Javadoc.Html text) {
      this.bottom = text;
   }

   public void setLinkoffline(String src) {
      Javadoc.LinkArgument le = this.createLink();
      le.setOffline(true);
      String linkOfflineError = "The linkoffline attribute must include a URL and a package-list file location separated by a space";
      if (src.trim().length() == 0) {
         throw new BuildException(linkOfflineError);
      } else {
         StringTokenizer tok = new StringTokenizer(src, " ", false);
         le.setHref(tok.nextToken());
         if (!tok.hasMoreTokens()) {
            throw new BuildException(linkOfflineError);
         } else {
            le.setPackagelistLoc(this.getProject().resolveFile(tok.nextToken()));
         }
      }
   }

   public void setGroup(String src) {
      this.group = src;
   }

   public void setLink(String src) {
      this.createLink().setHref(src);
   }

   public void setNodeprecated(boolean b) {
      this.addArgIf(b, "-nodeprecated");
   }

   public void setNodeprecatedlist(boolean b) {
      this.addArgIf(b, "-nodeprecatedlist");
   }

   public void setNotree(boolean b) {
      this.addArgIf(b, "-notree");
   }

   public void setNoindex(boolean b) {
      this.addArgIf(b, "-noindex");
   }

   public void setNohelp(boolean b) {
      this.addArgIf(b, "-nohelp");
   }

   public void setNonavbar(boolean b) {
      this.addArgIf(b, "-nonavbar");
   }

   public void setSerialwarn(boolean b) {
      this.addArgIf(b, "-serialwarn");
   }

   public void setStylesheetfile(File f) {
      this.cmd.createArgument().setValue("-stylesheetfile");
      this.cmd.createArgument().setFile(f);
   }

   public void setHelpfile(File f) {
      this.cmd.createArgument().setValue("-helpfile");
      this.cmd.createArgument().setFile(f);
   }

   public void setDocencoding(String enc) {
      this.cmd.createArgument().setValue("-docencoding");
      this.cmd.createArgument().setValue(enc);
   }

   public void setPackageList(String src) {
      this.packageList = src;
   }

   public Javadoc.LinkArgument createLink() {
      Javadoc.LinkArgument la = new Javadoc.LinkArgument();
      this.links.addElement(la);
      return la;
   }

   public Javadoc.TagArgument createTag() {
      Javadoc.TagArgument ta = new Javadoc.TagArgument();
      this.tags.addElement(ta);
      return ta;
   }

   public Javadoc.GroupArgument createGroup() {
      Javadoc.GroupArgument ga = new Javadoc.GroupArgument();
      this.groups.addElement(ga);
      return ga;
   }

   public void setCharset(String src) {
      this.addArgIfNotEmpty("-charset", src);
   }

   public void setFailonerror(boolean b) {
      this.failOnError = b;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public void setExecutable(String executable) {
      this.executable = executable;
   }

   public void addPackageset(DirSet packageSet) {
      this.packageSets.addElement(packageSet);
   }

   public void addFileset(FileSet fs) {
      this.createSourceFiles().add(fs);
   }

   public Javadoc.ResourceCollectionContainer createSourceFiles() {
      return this.nestedSourceFiles;
   }

   public void setLinksource(boolean b) {
      this.linksource = b;
   }

   public void setBreakiterator(boolean b) {
      this.breakiterator = b;
   }

   public void setNoqualifier(String noqualifier) {
      this.noqualifier = noqualifier;
   }

   public void setIncludeNoSourcePackages(boolean b) {
      this.includeNoSourcePackages = b;
   }

   public void execute() throws BuildException {
      if ("javadoc2".equals(this.getTaskType())) {
         this.log("Warning: the task name <javadoc2> is deprecated. Use <javadoc> instead.", 1);
      }

      boolean javadoc4 = !JavaEnvUtils.isJavaVersion("1.2") && !JavaEnvUtils.isJavaVersion("1.3");
      boolean javadoc5 = javadoc4 && !JavaEnvUtils.isJavaVersion("1.4");
      Vector packagesToDoc = new Vector();
      Path sourceDirs = new Path(this.getProject());
      String msg;
      if (this.packageList != null && this.sourcePath == null) {
         msg = "sourcePath attribute must be set when specifying packagelist.";
         throw new BuildException(msg);
      } else {
         if (this.sourcePath != null) {
            sourceDirs.addExisting(this.sourcePath);
         }

         this.parsePackages(packagesToDoc, sourceDirs);
         if (packagesToDoc.size() != 0 && sourceDirs.size() == 0) {
            msg = "sourcePath attribute must be set when specifying package names.";
            throw new BuildException(msg);
         } else {
            Vector sourceFilesToDoc = (Vector)this.sourceFiles.clone();
            this.addSourceFiles(sourceFilesToDoc);
            if (this.packageList == null && packagesToDoc.size() == 0 && sourceFilesToDoc.size() == 0) {
               throw new BuildException("No source files and no packages have been specified.");
            } else {
               this.log("Generating Javadoc", 2);
               Commandline toExecute = (Commandline)this.cmd.clone();
               if (this.executable != null) {
                  toExecute.setExecutable(this.executable);
               } else {
                  toExecute.setExecutable(JavaEnvUtils.getJdkExecutable("javadoc"));
               }

               if (this.doctitle != null) {
                  toExecute.createArgument().setValue("-doctitle");
                  toExecute.createArgument().setValue(this.expand(this.doctitle.getText()));
               }

               if (this.header != null) {
                  toExecute.createArgument().setValue("-header");
                  toExecute.createArgument().setValue(this.expand(this.header.getText()));
               }

               if (this.footer != null) {
                  toExecute.createArgument().setValue("-footer");
                  toExecute.createArgument().setValue(this.expand(this.footer.getText()));
               }

               if (this.bottom != null) {
                  toExecute.createArgument().setValue("-bottom");
                  toExecute.createArgument().setValue(this.expand(this.bottom.getText()));
               }

               if (this.classpath == null) {
                  this.classpath = (new Path(this.getProject())).concatSystemClasspath("last");
               } else {
                  this.classpath = this.classpath.concatSystemClasspath("ignore");
               }

               if (this.classpath.size() > 0) {
                  toExecute.createArgument().setValue("-classpath");
                  toExecute.createArgument().setPath(this.classpath);
               }

               if (sourceDirs.size() > 0) {
                  toExecute.createArgument().setValue("-sourcepath");
                  toExecute.createArgument().setPath(sourceDirs);
               }

               if (this.version && this.doclet == null) {
                  toExecute.createArgument().setValue("-version");
               }

               if (this.author && this.doclet == null) {
                  toExecute.createArgument().setValue("-author");
               }

               if (this.doclet == null && this.destDir == null) {
                  throw new BuildException("destdir attribute must be set!");
               } else {
                  Path docletPath;
                  Javadoc.DocletParam param;
                  if (this.doclet != null) {
                     if (this.doclet.getName() == null) {
                        throw new BuildException("The doclet name must be specified.", this.getLocation());
                     }

                     toExecute.createArgument().setValue("-doclet");
                     toExecute.createArgument().setValue(this.doclet.getName());
                     if (this.doclet.getPath() != null) {
                        docletPath = this.doclet.getPath().concatSystemClasspath("ignore");
                        if (docletPath.size() != 0) {
                           toExecute.createArgument().setValue("-docletpath");
                           toExecute.createArgument().setPath(docletPath);
                        }
                     }

                     Enumeration e = this.doclet.getParams();

                     while(e.hasMoreElements()) {
                        param = (Javadoc.DocletParam)e.nextElement();
                        if (param.getName() == null) {
                           throw new BuildException("Doclet parameters must have a name");
                        }

                        toExecute.createArgument().setValue(param.getName());
                        if (param.getValue() != null) {
                           toExecute.createArgument().setValue(param.getValue());
                        }
                     }
                  }

                  docletPath = new Path(this.getProject());
                  if (this.bootclasspath != null) {
                     docletPath.append(this.bootclasspath);
                  }

                  docletPath = docletPath.concatSystemBootClasspath("ignore");
                  if (docletPath.size() > 0) {
                     toExecute.createArgument().setValue("-bootclasspath");
                     toExecute.createArgument().setPath(docletPath);
                  }

                  String link;
                  File packageListLocation;
                  String name;
                  Enumeration e;
                  if (this.links.size() != 0) {
                     e = this.links.elements();

                     label1333:
                     while(true) {
                        Javadoc.LinkArgument la;
                        label1331:
                        while(true) {
                           while(true) {
                              if (!e.hasMoreElements()) {
                                 break label1333;
                              }

                              la = (Javadoc.LinkArgument)e.nextElement();
                              if (la.getHref() != null && la.getHref().length() != 0) {
                                 link = null;
                                 if (la.shouldResolveLink()) {
                                    packageListLocation = this.getProject().resolveFile(la.getHref());
                                    if (packageListLocation.exists()) {
                                       try {
                                          link = FILE_UTILS.getFileURL(packageListLocation).toExternalForm();
                                       } catch (MalformedURLException var42) {
                                          this.log("Warning: link location was invalid " + packageListLocation, 1);
                                       }
                                    }
                                 }

                                 if (link != null) {
                                    break label1331;
                                 }

                                 try {
                                    URL base = new URL("file://.");
                                    new URL(base, la.getHref());
                                    link = la.getHref();
                                    break label1331;
                                 } catch (MalformedURLException var48) {
                                    this.log("Link href \"" + la.getHref() + "\" is not a valid url - skipping link", 1);
                                 }
                              } else {
                                 this.log("No href was given for the link - skipping", 3);
                              }
                           }
                        }

                        if (la.isLinkOffline()) {
                           packageListLocation = la.getPackagelistLoc();
                           if (packageListLocation == null) {
                              throw new BuildException("The package list location for link " + la.getHref() + " must be provided " + "because the link is " + "offline");
                           }

                           File packageListFile = new File(packageListLocation, "package-list");
                           if (packageListFile.exists()) {
                              try {
                                 name = FILE_UTILS.getFileURL(packageListLocation).toExternalForm();
                                 toExecute.createArgument().setValue("-linkoffline");
                                 toExecute.createArgument().setValue(link);
                                 toExecute.createArgument().setValue(name);
                              } catch (MalformedURLException var41) {
                                 this.log("Warning: Package list location was invalid " + packageListLocation, 1);
                              }
                           } else {
                              this.log("Warning: No package list was found at " + packageListLocation, 3);
                           }
                        } else {
                           toExecute.createArgument().setValue("-link");
                           toExecute.createArgument().setValue(link);
                        }
                     }
                  }

                  String packageName;
                  String sourceFileName;
                  if (this.group != null) {
                     StringTokenizer tok = new StringTokenizer(this.group, ",", false);

                     while(tok.hasMoreTokens()) {
                        String grp = tok.nextToken().trim();
                        int space = grp.indexOf(" ");
                        if (space > 0) {
                           packageName = grp.substring(0, space);
                           sourceFileName = grp.substring(space + 1);
                           toExecute.createArgument().setValue("-group");
                           toExecute.createArgument().setValue(packageName);
                           toExecute.createArgument().setValue(sourceFileName);
                        }
                     }
                  }

                  if (this.groups.size() != 0) {
                     e = this.groups.elements();

                     while(e.hasMoreElements()) {
                        Javadoc.GroupArgument ga = (Javadoc.GroupArgument)e.nextElement();
                        link = ga.getTitle();
                        packageName = ga.getPackages();
                        if (link == null || packageName == null) {
                           throw new BuildException("The title and packages must be specified for group elements.");
                        }

                        toExecute.createArgument().setValue("-group");
                        toExecute.createArgument().setValue(this.expand(link));
                        toExecute.createArgument().setValue(packageName);
                     }
                  }

                  if (!javadoc4 && this.executable == null) {
                     if (!this.tags.isEmpty()) {
                        this.log("-tag and -taglet options not supported on Javadoc < 1.4", 3);
                     }

                     if (this.source != null) {
                        this.log("-source option not supported on Javadoc < 1.4", 3);
                     }

                     if (this.linksource) {
                        this.log("-linksource option not supported on Javadoc < 1.4", 3);
                     }

                     if (this.breakiterator) {
                        this.log("-breakiterator option not supported on Javadoc < 1.4", 3);
                     }

                     if (this.noqualifier != null) {
                        this.log("-noqualifier option not supported on Javadoc < 1.4", 3);
                     }
                  } else {
                     e = this.tags.elements();

                     label1287:
                     while(true) {
                        while(true) {
                           while(e.hasMoreElements()) {
                              Object element = e.nextElement();
                              if (element instanceof Javadoc.TagArgument) {
                                 Javadoc.TagArgument ta = (Javadoc.TagArgument)element;
                                 packageListLocation = ta.getDir(this.getProject());
                                 if (packageListLocation == null) {
                                    toExecute.createArgument().setValue("-tag");
                                    toExecute.createArgument().setValue(ta.getParameter());
                                 } else {
                                    DirectoryScanner tagDefScanner = ta.getDirectoryScanner(this.getProject());
                                    String[] files = tagDefScanner.getIncludedFiles();

                                    for(int i = 0; i < files.length; ++i) {
                                       File tagDefFile = new File(packageListLocation, files[i]);

                                       try {
                                          BufferedReader in = new BufferedReader(new FileReader(tagDefFile));
                                          String line = null;

                                          while((line = in.readLine()) != null) {
                                             toExecute.createArgument().setValue("-tag");
                                             toExecute.createArgument().setValue(line);
                                          }

                                          in.close();
                                       } catch (IOException var47) {
                                          throw new BuildException("Couldn't read  tag file from " + tagDefFile.getAbsolutePath(), var47);
                                       }
                                    }
                                 }
                              } else {
                                 Javadoc.ExtensionInfo tagletInfo = (Javadoc.ExtensionInfo)element;
                                 toExecute.createArgument().setValue("-taglet");
                                 toExecute.createArgument().setValue(tagletInfo.getName());
                                 if (tagletInfo.getPath() != null) {
                                    Path tagletPath = tagletInfo.getPath().concatSystemClasspath("ignore");
                                    if (tagletPath.size() != 0) {
                                       toExecute.createArgument().setValue("-tagletpath");
                                       toExecute.createArgument().setPath(tagletPath);
                                    }
                                 }
                              }
                           }

                           String sourceArg = this.source != null ? this.source : this.getProject().getProperty("ant.build.javac.source");
                           if (sourceArg != null) {
                              toExecute.createArgument().setValue("-source");
                              toExecute.createArgument().setValue(sourceArg);
                           }

                           if (this.linksource && this.doclet == null) {
                              toExecute.createArgument().setValue("-linksource");
                           }

                           if (this.breakiterator && (this.doclet == null || javadoc5)) {
                              toExecute.createArgument().setValue("-breakiterator");
                           }

                           if (this.noqualifier != null && this.doclet == null) {
                              toExecute.createArgument().setValue("-noqualifier");
                              toExecute.createArgument().setValue(this.noqualifier);
                           }
                           break label1287;
                        }
                     }
                  }

                  if (javadoc4 && this.executable == null) {
                     if (this.old) {
                        this.log("Javadoc 1.4 doesn't support the -1.1 switch anymore", 1);
                     }
                  } else if (this.old) {
                     toExecute.createArgument().setValue("-1.1");
                  }

                  if (this.useExternalFile && javadoc4) {
                     this.writeExternalArgs(toExecute);
                  }

                  File tmpList = null;
                  PrintWriter srcListWriter = null;

                  try {
                     if (this.useExternalFile) {
                        if (tmpList == null) {
                           tmpList = FILE_UTILS.createTempFile("javadoc", "", (File)null);
                           tmpList.deleteOnExit();
                           toExecute.createArgument().setValue("@" + tmpList.getAbsolutePath());
                        }

                        srcListWriter = new PrintWriter(new FileWriter(tmpList.getAbsolutePath(), true));
                     }

                     Enumeration e = packagesToDoc.elements();

                     while(e.hasMoreElements()) {
                        packageName = (String)e.nextElement();
                        if (this.useExternalFile) {
                           srcListWriter.println(packageName);
                        } else {
                           toExecute.createArgument().setValue(packageName);
                        }
                     }

                     e = sourceFilesToDoc.elements();

                     while(e.hasMoreElements()) {
                        Javadoc.SourceFile sf = (Javadoc.SourceFile)e.nextElement();
                        sourceFileName = sf.getFile().getAbsolutePath();
                        if (this.useExternalFile) {
                           if (javadoc4 && sourceFileName.indexOf(" ") > -1) {
                              name = sourceFileName;
                              if (File.separatorChar == '\\') {
                                 name = sourceFileName.replace(File.separatorChar, '/');
                              }

                              srcListWriter.println("\"" + name + "\"");
                           } else {
                              srcListWriter.println(sourceFileName);
                           }
                        } else {
                           toExecute.createArgument().setValue(sourceFileName);
                        }
                     }
                  } catch (IOException var45) {
                     tmpList.delete();
                     throw new BuildException("Error creating temporary file", var45, this.getLocation());
                  } finally {
                     if (srcListWriter != null) {
                        srcListWriter.close();
                     }

                  }

                  if (this.packageList != null) {
                     toExecute.createArgument().setValue("@" + this.packageList);
                  }

                  this.log(toExecute.describeCommand(), 3);
                  this.log("Javadoc execution", 2);
                  Javadoc.JavadocOutputStream out = new Javadoc.JavadocOutputStream(2);
                  Javadoc.JavadocOutputStream err = new Javadoc.JavadocOutputStream(1);
                  Execute exe = new Execute(new PumpStreamHandler(out, err));
                  exe.setAntRun(this.getProject());
                  exe.setWorkingDirectory((File)null);

                  try {
                     exe.setCommandline(toExecute.getCommandline());
                     int ret = exe.execute();
                     if (ret != 0 && this.failOnError) {
                        throw new BuildException("Javadoc returned " + ret, this.getLocation());
                     }
                  } catch (IOException var43) {
                     throw new BuildException("Javadoc failed: " + var43, var43, this.getLocation());
                  } finally {
                     if (tmpList != null) {
                        tmpList.delete();
                        param = null;
                     }

                     out.logFlush();
                     err.logFlush();

                     try {
                        out.close();
                        err.close();
                     } catch (IOException var40) {
                     }

                  }

               }
            }
         }
      }
   }

   private void writeExternalArgs(Commandline toExecute) {
      File optionsTmpFile = null;
      PrintWriter optionsListWriter = null;

      try {
         optionsTmpFile = FILE_UTILS.createTempFile("javadocOptions", "", (File)null);
         optionsTmpFile.deleteOnExit();
         String[] listOpt = toExecute.getArguments();
         toExecute.clearArgs();
         toExecute.createArgument().setValue("@" + optionsTmpFile.getAbsolutePath());
         optionsListWriter = new PrintWriter(new FileWriter(optionsTmpFile.getAbsolutePath(), true));

         for(int i = 0; i < listOpt.length; ++i) {
            String string = listOpt[i];
            if (string.startsWith("-J-")) {
               toExecute.createArgument().setValue(string);
            } else if (string.startsWith("-")) {
               optionsListWriter.print(string);
               optionsListWriter.print(" ");
            } else {
               optionsListWriter.println(this.quoteString(string));
            }
         }

         optionsListWriter.close();
      } catch (IOException var10) {
         if (optionsTmpFile != null) {
            optionsTmpFile.delete();
         }

         throw new BuildException("Error creating or writing temporary file for javadoc options", var10, this.getLocation());
      } finally {
         FileUtils var10000 = FILE_UTILS;
         FileUtils.close((Writer)optionsListWriter);
      }
   }

   private String quoteString(String str) {
      if (str.indexOf(32) == -1 && str.indexOf(39) == -1 && str.indexOf(34) == -1) {
         return str;
      } else {
         return str.indexOf(39) == -1 ? this.quoteString(str, '\'') : this.quoteString(str, '"');
      }
   }

   private String quoteString(String str, char delim) {
      StringBuffer buf = new StringBuffer(str.length() * 2);
      buf.append(delim);
      if (str.indexOf(92) != -1) {
         str = this.replace(str, '\\', "\\\\");
      }

      if (str.indexOf(delim) != -1) {
         str = this.replace(str, delim, "\\" + delim);
      }

      buf.append(str);
      buf.append(delim);
      return buf.toString();
   }

   private String replace(String str, char fromChar, String toString) {
      StringBuffer buf = new StringBuffer(str.length() * 2);

      for(int i = 0; i < str.length(); ++i) {
         char ch = str.charAt(i);
         if (ch == fromChar) {
            buf.append(toString);
         } else {
            buf.append(ch);
         }
      }

      return buf.toString();
   }

   private void addSourceFiles(Vector sf) {
      Iterator e = this.nestedSourceFiles.iterator();

      while(e.hasNext()) {
         ResourceCollection rc = (ResourceCollection)e.next();
         if (!rc.isFilesystemOnly()) {
            throw new BuildException("only file system based resources are supported by javadoc");
         }

         if (rc instanceof FileSet) {
            FileSet fs = (FileSet)rc;
            if (!fs.hasPatterns() && !fs.hasSelectors()) {
               fs = (FileSet)fs.clone();
               fs.createInclude().setName("**/*.java");
               if (this.includeNoSourcePackages) {
                  fs.createInclude().setName("**/package.html");
               }
            }
         }

         Iterator iter = rc.iterator();

         while(iter.hasNext()) {
            sf.addElement(new Javadoc.SourceFile(((FileResource)iter.next()).getFile()));
         }
      }

   }

   private void parsePackages(Vector pn, Path sp) {
      Vector addedPackages = new Vector();
      Vector dirSets = (Vector)this.packageSets.clone();
      if (this.sourcePath != null) {
         PatternSet ps = new PatternSet();
         Enumeration e;
         Javadoc.PackageName p;
         String pkg;
         if (this.packageNames.size() > 0) {
            for(e = this.packageNames.elements(); e.hasMoreElements(); ps.createInclude().setName(pkg)) {
               p = (Javadoc.PackageName)e.nextElement();
               pkg = p.getName().replace('.', '/');
               if (pkg.endsWith("*")) {
                  pkg = pkg + "*";
               }
            }
         } else {
            ps.createInclude().setName("**");
         }

         for(e = this.excludePackageNames.elements(); e.hasMoreElements(); ps.createExclude().setName(pkg)) {
            p = (Javadoc.PackageName)e.nextElement();
            pkg = p.getName().replace('.', '/');
            if (pkg.endsWith("*")) {
               pkg = pkg + "*";
            }
         }

         String[] pathElements = this.sourcePath.list();

         for(int i = 0; i < pathElements.length; ++i) {
            File dir = new File(pathElements[i]);
            if (dir.isDirectory()) {
               DirSet ds = new DirSet();
               ds.setDefaultexcludes(this.useDefaultExcludes);
               ds.setDir(dir);
               ds.createPatternSet().addConfiguredPatternset(ps);
               dirSets.addElement(ds);
            } else {
               this.log("Skipping " + pathElements[i] + " since it is no directory.", 1);
            }
         }
      }

      Enumeration e = dirSets.elements();

      while(e.hasMoreElements()) {
         DirSet ds = (DirSet)e.nextElement();
         File baseDir = ds.getDir(this.getProject());
         this.log("scanning " + baseDir + " for packages.", 4);
         DirectoryScanner dsc = ds.getDirectoryScanner(this.getProject());
         String[] dirs = dsc.getIncludedDirectories();
         boolean containsPackages = false;

         for(int i = 0; i < dirs.length; ++i) {
            File pd = new File(baseDir, dirs[i]);
            String[] files = pd.list(new FilenameFilter() {
               public boolean accept(File dir1, String name) {
                  return name.endsWith(".java") || Javadoc.this.includeNoSourcePackages && name.equals("package.html");
               }
            });
            if (files.length > 0) {
               if ("".equals(dirs[i])) {
                  this.log(baseDir + " contains source files in the default package," + " you must specify them as source files" + " not packages.", 1);
               } else {
                  containsPackages = true;
                  String packageName = dirs[i].replace(File.separatorChar, '.');
                  if (!addedPackages.contains(packageName)) {
                     addedPackages.addElement(packageName);
                     pn.addElement(packageName);
                  }
               }
            }
         }

         if (containsPackages) {
            sp.createPathElement().setLocation(baseDir);
         } else {
            this.log(baseDir + " doesn't contain any packages, dropping it.", 3);
         }
      }

   }

   protected String expand(String content) {
      return this.getProject().replaceProperties(content);
   }

   private class JavadocOutputStream extends LogOutputStream {
      private String queuedLine = null;

      JavadocOutputStream(int level) {
         super((Task)Javadoc.this, level);
      }

      protected void processLine(String line, int messageLevel) {
         if (messageLevel == 2 && line.startsWith("Generating ")) {
            if (this.queuedLine != null) {
               super.processLine(this.queuedLine, 3);
            }

            this.queuedLine = line;
         } else {
            if (this.queuedLine != null) {
               if (line.startsWith("Building ")) {
                  super.processLine(this.queuedLine, 3);
               } else {
                  super.processLine(this.queuedLine, 2);
               }

               this.queuedLine = null;
            }

            super.processLine(line, messageLevel);
         }

      }

      protected void logFlush() {
         if (this.queuedLine != null) {
            super.processLine(this.queuedLine, 3);
            this.queuedLine = null;
         }

      }
   }

   public class GroupArgument {
      private Javadoc.Html title;
      private Vector packages = new Vector();

      public void setTitle(String src) {
         Javadoc.Html h = new Javadoc.Html();
         h.addText(src);
         this.addTitle(h);
      }

      public void addTitle(Javadoc.Html text) {
         this.title = text;
      }

      public String getTitle() {
         return this.title != null ? this.title.getText() : null;
      }

      public void setPackages(String src) {
         StringTokenizer tok = new StringTokenizer(src, ",");

         while(tok.hasMoreTokens()) {
            String p = tok.nextToken();
            Javadoc.PackageName pn = new Javadoc.PackageName();
            pn.setName(p);
            this.addPackage(pn);
         }

      }

      public void addPackage(Javadoc.PackageName pn) {
         this.packages.addElement(pn);
      }

      public String getPackages() {
         StringBuffer p = new StringBuffer();

         for(int i = 0; i < this.packages.size(); ++i) {
            if (i > 0) {
               p.append(":");
            }

            p.append(this.packages.elementAt(i).toString());
         }

         return p.toString();
      }
   }

   public class TagArgument extends FileSet {
      private String name = null;
      private boolean enabled = true;
      private String scope = "a";

      public void setName(String name) {
         this.name = name;
      }

      public void setScope(String verboseScope) throws BuildException {
         verboseScope = verboseScope.toLowerCase(Locale.US);
         boolean[] elements = new boolean[Javadoc.SCOPE_ELEMENTS.length];
         boolean gotAll = false;
         boolean gotNotAll = false;
         StringTokenizer tok = new StringTokenizer(verboseScope, ",");

         while(true) {
            int i;
            while(tok.hasMoreTokens()) {
               String next = tok.nextToken().trim();
               if (next.equals("all")) {
                  if (gotAll) {
                     this.getProject().log("Repeated tag scope element: all", 3);
                  }

                  gotAll = true;
               } else {
                  for(i = 0; i < Javadoc.SCOPE_ELEMENTS.length && !next.equals(Javadoc.SCOPE_ELEMENTS[i]); ++i) {
                  }

                  if (i == Javadoc.SCOPE_ELEMENTS.length) {
                     throw new BuildException("Unrecognised scope element: " + next);
                  }

                  if (elements[i]) {
                     this.getProject().log("Repeated tag scope element: " + next, 3);
                  }

                  elements[i] = true;
                  gotNotAll = true;
               }
            }

            if (gotNotAll && gotAll) {
               throw new BuildException("Mixture of \"all\" and other scope elements in tag parameter.");
            }

            if (!gotNotAll && !gotAll) {
               throw new BuildException("No scope elements specified in tag parameter.");
            }

            if (gotAll) {
               this.scope = "a";
            } else {
               StringBuffer buff = new StringBuffer(elements.length);

               for(i = 0; i < elements.length; ++i) {
                  if (elements[i]) {
                     buff.append(Javadoc.SCOPE_ELEMENTS[i].charAt(0));
                  }
               }

               this.scope = buff.toString();
            }

            return;
         }
      }

      public void setEnabled(boolean enabled) {
         this.enabled = enabled;
      }

      public String getParameter() throws BuildException {
         if (this.name != null && !this.name.equals("")) {
            return this.getDescription() != null ? this.name + ":" + (this.enabled ? "" : "X") + this.scope + ":" + this.getDescription() : this.name + ":" + (this.enabled ? "" : "X") + this.scope + ":" + this.name;
         } else {
            throw new BuildException("No name specified for custom tag.");
         }
      }
   }

   public class LinkArgument {
      private String href;
      private boolean offline = false;
      private File packagelistLoc;
      private boolean resolveLink = false;

      public void setHref(String hr) {
         this.href = hr;
      }

      public String getHref() {
         return this.href;
      }

      public void setPackagelistLoc(File src) {
         this.packagelistLoc = src;
      }

      public File getPackagelistLoc() {
         return this.packagelistLoc;
      }

      public void setOffline(boolean offline) {
         this.offline = offline;
      }

      public boolean isLinkOffline() {
         return this.offline;
      }

      public void setResolveLink(boolean resolve) {
         this.resolveLink = resolve;
      }

      public boolean shouldResolveLink() {
         return this.resolveLink;
      }
   }

   public class ResourceCollectionContainer {
      private ArrayList rcs = new ArrayList();

      public void add(ResourceCollection rc) {
         this.rcs.add(rc);
      }

      private Iterator iterator() {
         return this.rcs.iterator();
      }
   }

   public static class AccessType extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"protected", "public", "package", "private"};
      }
   }

   public static class Html {
      private StringBuffer text = new StringBuffer();

      public void addText(String t) {
         this.text.append(t);
      }

      public String getText() {
         return this.text.substring(0);
      }
   }

   public static class SourceFile {
      private File file;

      public SourceFile() {
      }

      public SourceFile(File file) {
         this.file = file;
      }

      public void setFile(File file) {
         this.file = file;
      }

      public File getFile() {
         return this.file;
      }
   }

   public static class PackageName {
      private String name;

      public void setName(String name) {
         this.name = name.trim();
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.getName();
      }
   }

   public class DocletInfo extends Javadoc.ExtensionInfo {
      private Vector params = new Vector();

      public Javadoc.DocletParam createParam() {
         Javadoc.DocletParam param = Javadoc.this.new DocletParam();
         this.params.addElement(param);
         return param;
      }

      public Enumeration getParams() {
         return this.params.elements();
      }
   }

   public static class ExtensionInfo extends ProjectComponent {
      private String name;
      private Path path;

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public void setPath(Path path) {
         if (this.path == null) {
            this.path = path;
         } else {
            this.path.append(path);
         }

      }

      public Path getPath() {
         return this.path;
      }

      public Path createPath() {
         if (this.path == null) {
            this.path = new Path(this.getProject());
         }

         return this.path.createPath();
      }

      public void setPathRef(Reference r) {
         this.createPath().setRefid(r);
      }
   }

   public class DocletParam {
      private String name;
      private String value;

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }
   }
}
