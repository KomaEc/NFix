package org.codehaus.groovy.ant;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.tools.groovydoc.ClasspathResourceManager;
import org.codehaus.groovy.tools.groovydoc.FileOutputTool;
import org.codehaus.groovy.tools.groovydoc.GroovyDocTool;
import org.codehaus.groovy.tools.groovydoc.LinkArgument;
import org.codehaus.groovy.tools.groovydoc.gstringTemplates.GroovyDocTemplateInfo;

public class Groovydoc extends Task {
   private final LoggingHelper log = new LoggingHelper(this);
   private Path sourcePath;
   private File destDir;
   private List<String> packageNames = new ArrayList();
   private List<String> excludePackageNames = new ArrayList();
   private String windowTitle = "Groovy Documentation";
   private String docTitle = "Groovy Documentation";
   private String footer = "Groovy Documentation";
   private String header = "Groovy Documentation";
   private Boolean privateScope = false;
   private Boolean protectedScope = false;
   private Boolean packageScope = false;
   private Boolean publicScope = false;
   private Boolean author = true;
   private Boolean processScripts = true;
   private Boolean includeMainForScripts = true;
   private boolean useDefaultExcludes = true;
   private boolean includeNoSourcePackages = false;
   private List<DirSet> packageSets = new ArrayList();
   private List<String> sourceFilesToDoc = new ArrayList();
   private List<LinkArgument> links = new ArrayList();
   private File overviewFile;
   private File styleSheetFile;
   private String extensions = ".java:.groovy:.gv:.gvy:.gsh";

   public void setSourcepath(Path src) {
      if (this.sourcePath == null) {
         this.sourcePath = src;
      } else {
         this.sourcePath.append(src);
      }

   }

   public void setDestdir(File dir) {
      this.destDir = dir;
   }

   public void setAuthor(boolean author) {
      this.author = author;
   }

   public void setProcessScripts(boolean processScripts) {
      this.processScripts = processScripts;
   }

   public void setIncludeMainForScripts(boolean includeMainForScripts) {
      this.includeMainForScripts = includeMainForScripts;
   }

   public void setExtensions(String extensions) {
      this.extensions = extensions;
   }

   public void setPackagenames(String packages) {
      StringTokenizer tok = new StringTokenizer(packages, ",");

      while(tok.hasMoreTokens()) {
         String packageName = tok.nextToken();
         this.packageNames.add(packageName);
      }

   }

   public void setUse(boolean b) {
   }

   public void setWindowtitle(String title) {
      this.windowTitle = title;
   }

   public void setDoctitle(String htmlTitle) {
      this.docTitle = htmlTitle;
   }

   public void setOverview(File file) {
      this.overviewFile = file;
   }

   public void setAccess(String access) {
      if ("public".equals(access)) {
         this.publicScope = true;
      } else if ("protected".equals(access)) {
         this.protectedScope = true;
      } else if ("package".equals(access)) {
         this.packageScope = true;
      } else if ("private".equals(access)) {
         this.privateScope = true;
      }

   }

   public void setPrivate(boolean b) {
      this.privateScope = b;
   }

   public void setPublic(boolean b) {
      this.publicScope = b;
   }

   public void setProtected(boolean b) {
      this.protectedScope = b;
   }

   public void setPackage(boolean b) {
      this.packageScope = b;
   }

   public void setFooter(String footer) {
      this.footer = footer;
   }

   public void setHeader(String header) {
      this.header = header;
   }

   public void setStyleSheetFile(File styleSheetFile) {
      this.styleSheetFile = styleSheetFile;
   }

   private void parsePackages(List<String> resultantPackages, Path sourcePath) {
      List<String> addedPackages = new ArrayList();
      List<DirSet> dirSets = new ArrayList(this.packageSets);
      if (this.sourcePath != null) {
         PatternSet ps = new PatternSet();
         Iterator i$;
         String epn;
         String pkg;
         if (this.packageNames.size() > 0) {
            for(i$ = this.packageNames.iterator(); i$.hasNext(); ps.createInclude().setName(pkg)) {
               epn = (String)i$.next();
               pkg = epn.replace('.', '/');
               if (pkg.endsWith("*")) {
                  pkg = pkg + "*";
               }
            }
         } else {
            ps.createInclude().setName("**");
         }

         for(i$ = this.excludePackageNames.iterator(); i$.hasNext(); ps.createExclude().setName(pkg)) {
            epn = (String)i$.next();
            pkg = epn.replace('.', '/');
            if (pkg.endsWith("*")) {
               pkg = pkg + "*";
            }
         }

         String[] pathElements = this.sourcePath.list();
         String[] arr$ = pathElements;
         int len$ = pathElements.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String pathElement = arr$[i$];
            File dir = new File(pathElement);
            if (dir.isDirectory()) {
               DirSet ds = new DirSet();
               ds.setDefaultexcludes(this.useDefaultExcludes);
               ds.setDir(dir);
               ds.createPatternSet().addConfiguredPatternset(ps);
               dirSets.add(ds);
            } else {
               this.log.warn("Skipping " + pathElement + " since it is no directory.");
            }
         }
      }

      Iterator i$ = dirSets.iterator();

      while(i$.hasNext()) {
         DirSet ds = (DirSet)i$.next();
         File baseDir = ds.getDir(this.getProject());
         this.log.debug("scanning " + baseDir + " for packages.");
         DirectoryScanner dsc = ds.getDirectoryScanner(this.getProject());
         String[] dirs = dsc.getIncludedDirectories();
         boolean containsPackages = false;
         String[] arr$ = dirs;
         int len$ = dirs.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String dir = arr$[i$];
            File pd = new File(baseDir, dir);
            String[] files = pd.list(new FilenameFilter() {
               public boolean accept(File dir1, String name) {
                  if (!Groovydoc.this.includeNoSourcePackages && name.equals("package.html")) {
                     return true;
                  } else {
                     StringTokenizer tokenizer = new StringTokenizer(Groovydoc.this.extensions, ":");

                     String ext;
                     do {
                        if (!tokenizer.hasMoreTokens()) {
                           return false;
                        }

                        ext = tokenizer.nextToken();
                     } while(!name.endsWith(ext));

                     return true;
                  }
               }
            });
            Iterator i$ = Arrays.asList(files).iterator();

            while(i$.hasNext()) {
               String filename = (String)i$.next();
               this.sourceFilesToDoc.add(dir + File.separator + filename);
            }

            if (files.length > 0) {
               if ("".equals(dir)) {
                  this.log.warn(baseDir + " contains source files in the default package," + " you must specify them as source files not packages.");
               } else {
                  containsPackages = true;
                  String pn = dir.replace(File.separatorChar, '.');
                  if (!addedPackages.contains(pn)) {
                     addedPackages.add(pn);
                     resultantPackages.add(pn);
                  }
               }
            }
         }

         if (containsPackages) {
            sourcePath.createPathElement().setLocation(baseDir);
         } else {
            this.log.verbose(baseDir + " doesn't contain any packages, dropping it.");
         }
      }

   }

   public void execute() throws BuildException {
      List<String> packagesToDoc = new ArrayList();
      Path sourceDirs = new Path(this.getProject());
      Properties properties = new Properties();
      properties.setProperty("windowTitle", this.windowTitle);
      properties.setProperty("docTitle", this.docTitle);
      properties.setProperty("footer", this.footer);
      properties.setProperty("header", this.header);
      this.checkScopeProperties(properties);
      properties.setProperty("publicScope", this.publicScope.toString());
      properties.setProperty("protectedScope", this.protectedScope.toString());
      properties.setProperty("packageScope", this.packageScope.toString());
      properties.setProperty("privateScope", this.privateScope.toString());
      properties.setProperty("author", this.author.toString());
      properties.setProperty("processScripts", this.processScripts.toString());
      properties.setProperty("includeMainForScripts", this.includeMainForScripts.toString());
      properties.setProperty("overviewFile", this.overviewFile != null ? this.overviewFile.getAbsolutePath() : "");
      if (this.sourcePath != null) {
         sourceDirs.addExisting(this.sourcePath);
      }

      this.parsePackages(packagesToDoc, sourceDirs);
      GroovyDocTool htmlTool = new GroovyDocTool(new ClasspathResourceManager(), this.sourcePath.list(), GroovyDocTemplateInfo.DEFAULT_DOC_TEMPLATES, GroovyDocTemplateInfo.DEFAULT_PACKAGE_TEMPLATES, GroovyDocTemplateInfo.DEFAULT_CLASS_TEMPLATES, this.links, properties);

      try {
         htmlTool.add(this.sourceFilesToDoc);
         FileOutputTool output = new FileOutputTool();
         htmlTool.renderToOutput(output, this.destDir.getCanonicalPath());
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      if (this.styleSheetFile != null) {
         try {
            String css = DefaultGroovyMethods.getText(this.styleSheetFile);
            File outfile = new File(this.destDir, "stylesheet.css");
            DefaultGroovyMethods.setText(outfile, css);
         } catch (IOException var7) {
            System.out.println("Warning: Unable to copy specified stylesheet '" + this.styleSheetFile.getAbsolutePath() + "'. Using default stylesheet instead. Due to: " + var7.getMessage());
         }
      }

   }

   private void checkScopeProperties(Properties properties) {
      int scopeCount = 0;
      if (this.packageScope) {
         ++scopeCount;
      }

      if (this.privateScope) {
         ++scopeCount;
      }

      if (this.protectedScope) {
         ++scopeCount;
      }

      if (this.publicScope) {
         ++scopeCount;
      }

      if (scopeCount == 0) {
         this.protectedScope = true;
      } else if (scopeCount > 1) {
         throw new BuildException("More than one of public, private, package, or protected scopes specified.");
      }

   }

   public LinkArgument createLink() {
      LinkArgument result = new LinkArgument();
      this.links.add(result);
      return result;
   }
}
