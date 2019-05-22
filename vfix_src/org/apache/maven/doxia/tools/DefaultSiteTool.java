package org.apache.maven.doxia.tools;

import com.gzoltar.shaded.org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.doxia.site.decoration.Banner;
import org.apache.maven.doxia.site.decoration.DecorationModel;
import org.apache.maven.doxia.site.decoration.Menu;
import org.apache.maven.doxia.site.decoration.MenuItem;
import org.apache.maven.doxia.site.decoration.Skin;
import org.apache.maven.doxia.site.decoration.inheritance.DecorationModelInheritanceAssembler;
import org.apache.maven.doxia.site.decoration.io.xpp3.DecorationXpp3Reader;
import org.apache.maven.model.Model;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.reporting.MavenReport;
import org.codehaus.plexus.i18n.I18N;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.interpolation.EnvarBasedValueSource;
import org.codehaus.plexus.util.interpolation.MapBasedValueSource;
import org.codehaus.plexus.util.interpolation.ObjectBasedValueSource;
import org.codehaus.plexus.util.interpolation.RegexBasedInterpolator;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class DefaultSiteTool extends AbstractLogEnabled implements SiteTool {
   private ArtifactResolver artifactResolver;
   private ArtifactFactory artifactFactory;
   protected I18N i18n;
   protected DecorationModelInheritanceAssembler assembler;
   protected MavenProjectBuilder mavenProjectBuilder;

   public Artifact getSkinArtifactFromRepository(ArtifactRepository localRepository, List remoteArtifactRepositories, DecorationModel decoration) throws SiteToolException {
      if (localRepository == null) {
         throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
      } else if (remoteArtifactRepositories == null) {
         throw new IllegalArgumentException("The parameter 'remoteArtifactRepositories' can not be null");
      } else if (decoration == null) {
         throw new IllegalArgumentException("The parameter 'decoration' can not be null");
      } else {
         Skin skin = decoration.getSkin();
         if (skin == null) {
            skin = Skin.getDefaultSkin();
         }

         String version = skin.getVersion();

         try {
            if (version == null) {
               version = "RELEASE";
            }

            VersionRange versionSpec = VersionRange.createFromVersionSpec(version);
            Artifact artifact = this.artifactFactory.createDependencyArtifact(skin.getGroupId(), skin.getArtifactId(), versionSpec, "jar", (String)null, (String)null);
            this.artifactResolver.resolve(artifact, remoteArtifactRepositories, localRepository);
            return artifact;
         } catch (InvalidVersionSpecificationException var8) {
            throw new SiteToolException("InvalidVersionSpecificationException: The skin version '" + version + "' is not valid: " + var8.getMessage());
         } catch (ArtifactResolutionException var9) {
            throw new SiteToolException("ArtifactResolutionException: Unable to find skin", var9);
         } catch (ArtifactNotFoundException var10) {
            throw new SiteToolException("ArtifactNotFoundException: The skin does not exist: " + var10.getMessage());
         }
      }
   }

   public Artifact getDefaultSkinArtifact(ArtifactRepository localRepository, List remoteArtifactRepositories) throws SiteToolException {
      return this.getSkinArtifactFromRepository(localRepository, remoteArtifactRepositories, new DecorationModel());
   }

   public String getRelativePath(String to, String from) {
      if (to == null) {
         throw new IllegalArgumentException("The parameter 'to' can not be null");
      } else if (from == null) {
         throw new IllegalArgumentException("The parameter 'from' can not be null");
      } else {
         URL toUrl = null;
         URL fromUrl = null;
         String toPath = to;
         String fromPath = from;

         try {
            toUrl = new URL(to);
         } catch (MalformedURLException var14) {
            try {
               toUrl = (new File(getNormalizedPath(to))).toURL();
            } catch (MalformedURLException var13) {
               this.getLogger().warn("Unable to load a URL for '" + to + "': " + var14.getMessage());
            }
         }

         try {
            fromUrl = new URL(from);
         } catch (MalformedURLException var12) {
            try {
               fromUrl = (new File(getNormalizedPath(from))).toURL();
            } catch (MalformedURLException var11) {
               this.getLogger().warn("Unable to load a URL for '" + from + "': " + var12.getMessage());
            }
         }

         if (toUrl != null && fromUrl != null) {
            if (!toUrl.getProtocol().equalsIgnoreCase(fromUrl.getProtocol()) || !toUrl.getHost().equalsIgnoreCase(fromUrl.getHost()) || toUrl.getPort() != fromUrl.getPort()) {
               return to;
            }

            toPath = toUrl.getFile();
            fromPath = fromUrl.getFile();
         } else if (toUrl != null && fromUrl == null || toUrl == null && fromUrl != null) {
            return to;
         }

         toPath = (new File(toPath)).getPath();
         fromPath = (new File(fromPath)).getPath();
         if (toPath.matches("^\\[a-zA-Z]:")) {
            toPath = toPath.substring(1);
         }

         if (fromPath.matches("^\\[a-zA-Z]:")) {
            fromPath = fromPath.substring(1);
         }

         if (toPath.startsWith(":", 1)) {
            toPath = toPath.substring(0, 1).toLowerCase() + toPath.substring(1);
         }

         if (fromPath.startsWith(":", 1)) {
            fromPath = fromPath.substring(0, 1).toLowerCase() + fromPath.substring(1);
         }

         if (toPath.startsWith(":", 1) && fromPath.startsWith(":", 1) && !toPath.substring(0, 1).equals(fromPath.substring(0, 1))) {
            return to;
         } else if (toPath.startsWith(":", 1) && !fromPath.startsWith(":", 1) || !toPath.startsWith(":", 1) && fromPath.startsWith(":", 1)) {
            return to;
         } else {
            StringTokenizer toTokeniser = new StringTokenizer(toPath, File.separator);
            StringTokenizer fromTokeniser = new StringTokenizer(fromPath, File.separator);

            int count;
            for(count = 0; toTokeniser.hasMoreTokens() && fromTokeniser.hasMoreTokens(); ++count) {
               if (File.separatorChar == '\\') {
                  if (!fromTokeniser.nextToken().equalsIgnoreCase(toTokeniser.nextToken())) {
                     break;
                  }
               } else if (!fromTokeniser.nextToken().equals(toTokeniser.nextToken())) {
                  break;
               }
            }

            toTokeniser = new StringTokenizer(toPath, File.separator);
            fromTokeniser = new StringTokenizer(fromPath, File.separator);

            while(count-- > 0) {
               fromTokeniser.nextToken();
               toTokeniser.nextToken();
            }

            String relativePath = "";

            while(fromTokeniser.hasMoreTokens()) {
               fromTokeniser.nextToken();
               relativePath = relativePath + "..";
               if (fromTokeniser.hasMoreTokens()) {
                  relativePath = relativePath + File.separatorChar;
               }
            }

            if (relativePath.length() != 0 && toTokeniser.hasMoreTokens()) {
               relativePath = relativePath + File.separatorChar;
            }

            while(toTokeniser.hasMoreTokens()) {
               relativePath = relativePath + toTokeniser.nextToken();
               if (toTokeniser.hasMoreTokens()) {
                  relativePath = relativePath + File.separatorChar;
               }
            }

            if (!relativePath.equals(to)) {
               this.getLogger().debug("Mapped url: " + to + " to relative path: " + relativePath);
            }

            return relativePath;
         }
      }
   }

   public File getSiteDescriptorFromBasedir(String siteDirectory, File basedir, Locale locale) {
      if (basedir == null) {
         throw new IllegalArgumentException("The parameter 'basedir' can not be null");
      } else {
         if (siteDirectory == null) {
            siteDirectory = "src/site";
         }

         if (locale == null) {
            locale = new Locale("");
         }

         File siteDir = new File(basedir, siteDirectory);
         File siteDescriptor = new File(siteDir, "site_" + locale.getLanguage() + ".xml");
         if (!siteDescriptor.isFile()) {
            siteDescriptor = new File(siteDir, "site.xml");
         }

         return siteDescriptor;
      }
   }

   public File getSiteDescriptorFromRepository(MavenProject project, ArtifactRepository localRepository, List remoteArtifactRepositories, Locale locale) throws SiteToolException {
      if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (localRepository == null) {
         throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
      } else if (remoteArtifactRepositories == null) {
         throw new IllegalArgumentException("The parameter 'remoteArtifactRepositories' can not be null");
      } else {
         if (locale == null) {
            locale = new Locale("");
         }

         try {
            return this.resolveSiteDescriptor(project, localRepository, remoteArtifactRepositories, locale);
         } catch (ArtifactNotFoundException var6) {
            this.getLogger().debug("ArtifactNotFoundException: Unable to locate site descriptor: " + var6);
            return null;
         } catch (ArtifactResolutionException var7) {
            throw new SiteToolException("ArtifactResolutionException: Unable to locate site descriptor: " + var7.getMessage());
         } catch (IOException var8) {
            throw new SiteToolException("IOException: Unable to locate site descriptor: " + var8.getMessage());
         }
      }
   }

   public DecorationModel getDecorationModel(MavenProject project, List reactorProjects, ArtifactRepository localRepository, List repositories, String siteDirectory, Locale locale, String inputEncoding, String outputEncoding) throws SiteToolException {
      if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (reactorProjects == null) {
         throw new IllegalArgumentException("The parameter 'reactorProjects' can not be null");
      } else if (localRepository == null) {
         throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
      } else if (repositories == null) {
         throw new IllegalArgumentException("The parameter 'repositories' can not be null");
      } else if (inputEncoding == null) {
         throw new IllegalArgumentException("The parameter 'inputEncoding' can not be null");
      } else if (outputEncoding == null) {
         throw new IllegalArgumentException("The parameter 'outputEncoding' can not be null");
      } else {
         if (locale == null) {
            locale = Locale.getDefault();
         }

         Map props = new HashMap();
         props.put("reports", "<menu ref=\"reports\"/>\n");
         props.put("modules", "<menu ref=\"modules\"/>\n");
         DecorationModel decorationModel = this.getDecorationModel(project, reactorProjects, localRepository, repositories, siteDirectory, locale, props, inputEncoding, outputEncoding);
         if (decorationModel == null) {
            String siteDescriptorContent;
            try {
               siteDescriptorContent = IOUtil.toString(this.getClass().getResourceAsStream("/default-site.xml"), "UTF-8");
            } catch (IOException var13) {
               throw new SiteToolException("Error reading default site descriptor: " + var13.getMessage(), var13);
            }

            siteDescriptorContent = this.getInterpolatedSiteDescriptorContent(props, project, siteDescriptorContent, inputEncoding, outputEncoding);
            decorationModel = this.readDecorationModel(siteDescriptorContent);
         }

         MavenProject parentProject = this.getParentProject(project, reactorProjects, localRepository);
         if (parentProject != null) {
            this.populateParentMenu(decorationModel, locale, project, parentProject, true);
         }

         this.populateModulesMenu(project, reactorProjects, localRepository, decorationModel, locale, true);
         if (decorationModel.getBannerLeft() == null) {
            Banner banner = new Banner();
            banner.setName(project.getName());
            decorationModel.setBannerLeft(banner);
         }

         if (project.getUrl() != null) {
            this.assembler.resolvePaths(decorationModel, project.getUrl());
         } else {
            this.getLogger().warn("No URL defined for the project - decoration links will not be resolved");
         }

         return decorationModel;
      }
   }

   public void populateReportsMenu(DecorationModel decorationModel, Locale locale, Map categories) {
      if (decorationModel == null) {
         throw new IllegalArgumentException("The parameter 'decorationModel' can not be null");
      } else if (categories == null) {
         throw new IllegalArgumentException("The parameter 'categories' can not be null");
      } else {
         if (locale == null) {
            locale = Locale.getDefault();
         }

         Menu menu = decorationModel.getMenuRef("reports");
         if (menu != null) {
            if (menu.getName() == null) {
               menu.setName(this.i18n.getString("site-tool", locale, "decorationModel.menu.projectdocumentation"));
            }

            boolean found = false;
            if (menu.getItems().isEmpty()) {
               List categoryReports = (List)categories.get("Project Info");
               MenuItem item;
               if (!isEmptyList(categoryReports)) {
                  item = this.createCategoryMenu(this.i18n.getString("site-tool", locale, "decorationModel.menu.projectinformation"), "/project-info.html", categoryReports, locale);
                  menu.getItems().add(item);
                  found = true;
               }

               categoryReports = (List)categories.get("Project Reports");
               if (!isEmptyList(categoryReports)) {
                  item = this.createCategoryMenu(this.i18n.getString("site-tool", locale, "decorationModel.menu.projectreports"), "/project-reports.html", categoryReports, locale);
                  menu.getItems().add(item);
                  found = true;
               }
            }

            if (!found) {
               decorationModel.removeMenuRef("reports");
            }
         }

      }
   }

   public String getInterpolatedSiteDescriptorContent(Map props, MavenProject project, String siteDescriptorContent, String inputEncoding, String outputEncoding) throws SiteToolException {
      if (props == null) {
         throw new IllegalArgumentException("The parameter 'props' can not be null");
      } else if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (siteDescriptorContent == null) {
         throw new IllegalArgumentException("The parameter 'siteDescriptorContent' can not be null");
      } else if (inputEncoding == null) {
         throw new IllegalArgumentException("The parameter 'inputEncoding' can not be null");
      } else if (outputEncoding == null) {
         throw new IllegalArgumentException("The parameter 'outputEncoding' can not be null");
      } else {
         Map modulesProps = new HashMap();
         modulesProps.put("modules", "<menu ref=\"modules\"/>");
         siteDescriptorContent = StringUtils.interpolate(siteDescriptorContent, modulesProps);
         RegexBasedInterpolator interpolator = new RegexBasedInterpolator();

         try {
            interpolator.addValueSource(new EnvarBasedValueSource());
         } catch (IOException var9) {
            throw new SiteToolException("IOException: cannot interpolate environment properties: " + var9.getMessage(), var9);
         }

         interpolator.addValueSource(new ObjectBasedValueSource(project));
         interpolator.addValueSource(new MapBasedValueSource(project.getProperties()));
         siteDescriptorContent = interpolator.interpolate(siteDescriptorContent, "project");
         props.put("inputEncoding", inputEncoding);
         props.put("outputEncoding", outputEncoding);
         props.put("parentProject", "<menu ref=\"parent\"/>");
         props.put("reports", "<menu ref=\"reports\"/>");
         return StringUtils.interpolate(siteDescriptorContent, props);
      }
   }

   public MavenProject getParentProject(MavenProject project, List reactorProjects, ArtifactRepository localRepository) {
      if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (reactorProjects == null) {
         throw new IllegalArgumentException("The parameter 'reactorProjects' can not be null");
      } else if (localRepository == null) {
         throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
      } else {
         MavenProject parentProject = null;
         MavenProject origParent = project.getParent();
         if (origParent != null) {
            Iterator reactorItr = reactorProjects.iterator();

            while(reactorItr.hasNext()) {
               MavenProject reactorProject = (MavenProject)reactorItr.next();
               if (reactorProject.getGroupId().equals(origParent.getGroupId()) && reactorProject.getArtifactId().equals(origParent.getArtifactId()) && reactorProject.getVersion().equals(origParent.getVersion())) {
                  parentProject = reactorProject;
                  break;
               }
            }

            if (parentProject == null && project.getBasedir() != null) {
               try {
                  File pomFile = new File(project.getBasedir(), project.getModel().getParent().getRelativePath());
                  if (pomFile.isDirectory()) {
                     pomFile = new File(pomFile, "pom.xml");
                  }

                  pomFile = new File(getNormalizedPath(pomFile.getPath()));
                  MavenProject mavenProject = this.mavenProjectBuilder.build(pomFile, localRepository, (ProfileManager)null);
                  if (mavenProject.getGroupId().equals(origParent.getGroupId()) && mavenProject.getArtifactId().equals(origParent.getArtifactId()) && mavenProject.getVersion().equals(origParent.getVersion())) {
                     parentProject = mavenProject;
                  }
               } catch (ProjectBuildingException var10) {
                  this.getLogger().info("Unable to load parent project from a relative path: " + var10.getMessage());
               }
            }

            if (parentProject == null) {
               try {
                  parentProject = this.mavenProjectBuilder.buildFromRepository(project.getParentArtifact(), project.getRemoteArtifactRepositories(), localRepository);
                  this.getLogger().info("Parent project loaded from repository.");
               } catch (ProjectBuildingException var9) {
                  this.getLogger().warn("Unable to load parent project from repository: " + var9.getMessage());
               }
            }

            if (parentProject == null) {
               parentProject = origParent;
            }
         }

         return parentProject;
      }
   }

   public void populateParentMenu(DecorationModel decorationModel, Locale locale, MavenProject project, MavenProject parentProject, boolean keepInheritedRefs) {
      if (decorationModel == null) {
         throw new IllegalArgumentException("The parameter 'decorationModel' can not be null");
      } else if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (parentProject == null) {
         throw new IllegalArgumentException("The parameter 'parentProject' can not be null");
      } else {
         if (locale == null) {
            locale = Locale.getDefault();
         }

         Menu menu = decorationModel.getMenuRef("parent");
         if (menu != null) {
            if (!keepInheritedRefs || !menu.isInheritAsRef()) {
               String parentUrl = parentProject.getUrl();
               if (parentUrl != null) {
                  if (parentUrl.endsWith("/")) {
                     parentUrl = parentUrl + "index.html";
                  } else {
                     parentUrl = parentUrl + "/index.html";
                  }

                  parentUrl = this.getRelativePath(parentUrl, project.getUrl());
                  if (menu.getName() == null) {
                     menu.setName(this.i18n.getString("site-tool", locale, "decorationModel.menu.parentproject"));
                  }

                  MenuItem item = new MenuItem();
                  item.setName(parentProject.getName());
                  item.setHref(parentUrl);
                  menu.addItem(item);
               } else {
                  decorationModel.removeMenuRef("parent");
               }
            }

         }
      }
   }

   /** @deprecated */
   public void populateProjectParentMenu(DecorationModel decorationModel, Locale locale, MavenProject project, MavenProject parentProject, boolean keepInheritedRefs) {
      this.populateParentMenu(decorationModel, locale, project, parentProject, keepInheritedRefs);
   }

   /** @deprecated */
   public void populateModules(MavenProject project, List reactorProjects, ArtifactRepository localRepository, DecorationModel decorationModel, Locale locale, boolean keepInheritedRefs) throws SiteToolException {
      this.populateModulesMenu(project, reactorProjects, localRepository, decorationModel, locale, keepInheritedRefs);
   }

   public void populateModulesMenu(MavenProject project, List reactorProjects, ArtifactRepository localRepository, DecorationModel decorationModel, Locale locale, boolean keepInheritedRefs) throws SiteToolException {
      if (project == null) {
         throw new IllegalArgumentException("The parameter 'project' can not be null");
      } else if (reactorProjects == null) {
         throw new IllegalArgumentException("The parameter 'reactorProjects' can not be null");
      } else if (localRepository == null) {
         throw new IllegalArgumentException("The parameter 'localRepository' can not be null");
      } else if (decorationModel == null) {
         throw new IllegalArgumentException("The parameter 'decorationModel' can not be null");
      } else {
         if (locale == null) {
            locale = Locale.getDefault();
         }

         Menu menu = decorationModel.getMenuRef("modules");
         if (menu != null) {
            if (!keepInheritedRefs || !menu.isInheritAsRef()) {
               if (project.getModules().size() > 0) {
                  if (menu.getName() == null) {
                     menu.setName(this.i18n.getString("site-tool", locale, "decorationModel.menu.projectmodules"));
                  }

                  if (reactorProjects.size() == 1) {
                     this.getLogger().debug("Attempting to load module information from local filesystem");
                     List models = new ArrayList(project.getModules().size());

                     Model model;
                     for(Iterator i = project.getModules().iterator(); i.hasNext(); models.add(model)) {
                        String module = (String)i.next();
                        File f = new File(project.getBasedir(), module + "/pom.xml");
                        if (f.exists()) {
                           try {
                              model = this.mavenProjectBuilder.build(f, localRepository, (ProfileManager)null).getModel();
                           } catch (ProjectBuildingException var15) {
                              throw new SiteToolException("Unable to read local module-POM", var15);
                           }
                        } else {
                           this.getLogger().warn("No filesystem module-POM available");
                           model = new Model();
                           model.setName(module);
                           model.setUrl(module);
                        }
                     }

                     this.populateModulesMenuItemsFromModels(project, models, menu);
                  } else {
                     this.populateModulesMenuItemsFromReactorProjects(project, reactorProjects, menu);
                  }
               } else {
                  decorationModel.removeMenuRef("modules");
               }
            }

         }
      }
   }

   public List getAvailableLocales(String locales) {
      List localesList = new ArrayList();
      if (locales != null) {
         String[] localesArray = StringUtils.split(locales, ",");

         for(int i = 0; i < localesArray.length; ++i) {
            Locale locale = this.codeToLocale(localesArray[i]);
            if (locale != null) {
               if (!Arrays.asList(Locale.getAvailableLocales()).contains(locale)) {
                  if (this.getLogger().isWarnEnabled()) {
                     this.getLogger().warn("The locale parsed defined by '" + locale + "' is not available in this Java Virtual Machine (" + System.getProperty("java.version") + " from " + System.getProperty("java.vendor") + ") - IGNORING");
                  }
               } else if (!locale.getLanguage().equals(DEFAULT_LOCALE.getLanguage()) && !this.i18n.getBundle("site-tool", locale).getLocale().getLanguage().equals(locale.getLanguage())) {
                  StringBuffer sb = new StringBuffer();
                  sb.append("The locale '").append(locale).append("' (");
                  sb.append(locale.getDisplayName(Locale.ENGLISH));
                  sb.append(") is not currently support by Maven - IGNORING. ");
                  sb.append("\n");
                  sb.append("Contribution are welcome and greatly appreciated! ");
                  sb.append("\n");
                  sb.append("If you want to contribute a new translation, please visit ");
                  sb.append("http://maven.apache.org/plugins/maven-site-plugin/i18n.html ");
                  sb.append("for detailed instructions.");
                  if (this.getLogger().isWarnEnabled()) {
                     this.getLogger().warn(sb.toString());
                  }
               } else {
                  ((List)localesList).add(locale);
               }
            }
         }
      }

      if (((List)localesList).isEmpty()) {
         localesList = Collections.singletonList(DEFAULT_LOCALE);
      }

      return (List)localesList;
   }

   public Locale codeToLocale(String localeCode) {
      if (localeCode == null) {
         return null;
      } else if ("default".equalsIgnoreCase(localeCode)) {
         return Locale.getDefault();
      } else {
         String language = "";
         String country = "";
         String variant = "";
         StringTokenizer tokenizer = new StringTokenizer(localeCode, "_");
         int maxTokens = true;
         if (tokenizer.countTokens() > 3) {
            if (this.getLogger().isWarnEnabled()) {
               this.getLogger().warn("Invalid java.util.Locale format for '" + localeCode + "' entry - IGNORING");
            }

            return null;
         } else {
            if (tokenizer.hasMoreTokens()) {
               language = tokenizer.nextToken();
               if (tokenizer.hasMoreTokens()) {
                  country = tokenizer.nextToken();
                  if (tokenizer.hasMoreTokens()) {
                     variant = tokenizer.nextToken();
                  }
               }
            }

            return new Locale(language, country, variant);
         }
      }
   }

   protected static String getNormalizedPath(String path) {
      String normalized = FilenameUtils.normalize(path);
      if (normalized == null) {
         normalized = path;
      }

      return normalized == null ? null : normalized.replace('\\', '/');
   }

   private File resolveSiteDescriptor(MavenProject project, ArtifactRepository localRepository, List repositories, Locale locale) throws IOException, ArtifactResolutionException, ArtifactNotFoundException {
      Artifact artifact = this.artifactFactory.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(), project.getVersion(), "xml", "site_" + locale.getLanguage());
      boolean found = false;

      File result;
      try {
         this.artifactResolver.resolve(artifact, repositories, localRepository);
         result = artifact.getFile();
         if (result.length() > 0L) {
            found = true;
         } else {
            this.getLogger().debug("Skipped site descriptor for locale " + locale.getLanguage());
         }
      } catch (ArtifactNotFoundException var10) {
         this.getLogger().debug("Unable to locate site descriptor for locale " + locale.getLanguage() + ": " + var10);
         result = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));
         result.getParentFile().mkdirs();
         result.createNewFile();
      }

      if (!found) {
         artifact = this.artifactFactory.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(), project.getVersion(), "xml", "site");

         try {
            this.artifactResolver.resolve(artifact, repositories, localRepository);
         } catch (ArtifactNotFoundException var9) {
            result = new File(localRepository.getBasedir(), localRepository.pathOf(artifact));
            result.getParentFile().mkdirs();
            result.createNewFile();
            throw var9;
         }

         result = artifact.getFile();
         if (result.length() == 0L) {
            this.getLogger().debug("Skipped remote site descriptor check");
            result = null;
         }
      }

      return result;
   }

   private DecorationModel getDecorationModel(MavenProject project, List reactorProjects, ArtifactRepository localRepository, List repositories, String siteDirectory, Locale locale, Map origProps, String inputEncoding, String outputEncoding) throws SiteToolException {
      Map props = new HashMap(origProps);
      File siteDescriptor;
      if (project.getBasedir() == null) {
         try {
            siteDescriptor = this.getSiteDescriptorFromRepository(project, localRepository, repositories, locale);
         } catch (SiteToolException var16) {
            throw new SiteToolException("The site descriptor cannot be resolved from the repository: " + var16.getMessage(), var16);
         }
      } else {
         siteDescriptor = this.getSiteDescriptorFromBasedir(siteDirectory, project.getBasedir(), locale);
      }

      String siteDescriptorContent = null;

      try {
         if (siteDescriptor != null && siteDescriptor.exists()) {
            this.getLogger().debug("Reading site descriptor from " + siteDescriptor);
            Reader siteDescriptorReader = ReaderFactory.newXmlReader(siteDescriptor);
            siteDescriptorContent = IOUtil.toString((Reader)siteDescriptorReader);
         }
      } catch (IOException var17) {
         throw new SiteToolException("The site descriptor cannot be read!", var17);
      }

      DecorationModel decoration = null;
      if (siteDescriptorContent != null) {
         siteDescriptorContent = this.getInterpolatedSiteDescriptorContent(props, project, siteDescriptorContent, inputEncoding, outputEncoding);
         decoration = this.readDecorationModel(siteDescriptorContent);
      }

      MavenProject parentProject = this.getParentProject(project, reactorProjects, localRepository);
      if (parentProject != null) {
         this.getLogger().debug("Parent project loaded ...");
         DecorationModel parent = this.getDecorationModel(parentProject, reactorProjects, localRepository, repositories, siteDirectory, locale, props, inputEncoding, outputEncoding);
         if (decoration == null) {
            decoration = parent;
         } else {
            this.assembler.assembleModelInheritance(project.getName(), decoration, parent, project.getUrl(), parentProject.getUrl() == null ? project.getUrl() : parentProject.getUrl());
         }
      }

      if (decoration != null && decoration.getSkin() != null) {
         this.getLogger().debug("Skin used: " + decoration.getSkin());
      }

      return decoration;
   }

   private DecorationModel readDecorationModel(String siteDescriptorContent) throws SiteToolException {
      try {
         DecorationModel decoration = (new DecorationXpp3Reader()).read((Reader)(new StringReader(siteDescriptorContent)));
         return decoration;
      } catch (XmlPullParserException var4) {
         throw new SiteToolException("Error parsing site descriptor", var4);
      } catch (IOException var5) {
         throw new SiteToolException("Error reading site descriptor", var5);
      }
   }

   private void populateModulesMenuItemsFromReactorProjects(MavenProject project, List reactorProjects, Menu menu) {
      Iterator iterator = this.getModuleProjects(project, reactorProjects, 1).iterator();

      while(iterator.hasNext()) {
         MavenProject moduleProject = (MavenProject)iterator.next();
         this.appendMenuItem(project, menu, moduleProject.getName(), moduleProject.getUrl(), moduleProject.getArtifactId());
      }

   }

   private List getModuleProjects(MavenProject project, List reactorProjects, int levels) {
      List moduleProjects = new ArrayList();
      boolean infinite = levels == -1;
      if (reactorProjects != null && (infinite || levels > 0)) {
         Iterator iterator = reactorProjects.iterator();

         while(iterator.hasNext()) {
            MavenProject reactorProject = (MavenProject)iterator.next();
            if (this.isModuleOfProject(project, reactorProject)) {
               moduleProjects.add(reactorProject);
               moduleProjects.addAll(this.getModuleProjects(reactorProject, reactorProjects, infinite ? levels : levels - 1));
            }
         }
      }

      return moduleProjects;
   }

   private boolean isModuleOfProject(MavenProject parentProject, MavenProject potentialModule) {
      boolean result = false;
      List modules = parentProject.getModules();
      if (modules != null && parentProject != potentialModule) {
         File parentBaseDir = parentProject.getBasedir();
         Iterator iterator = modules.iterator();

         while(iterator.hasNext()) {
            String module = (String)iterator.next();
            File moduleBaseDir = new File(parentBaseDir, module);

            try {
               String lhs = potentialModule.getBasedir().getCanonicalPath();
               String rhs = moduleBaseDir.getCanonicalPath();
               if (lhs.equals(rhs)) {
                  result = true;
                  break;
               }
            } catch (IOException var11) {
               this.getLogger().error("Error encountered when trying to resolve canonical module paths: " + var11.getMessage());
            }
         }
      }

      return result;
   }

   private void populateModulesMenuItemsFromModels(MavenProject project, List models, Menu menu) {
      if (models != null && models.size() > 1) {
         Iterator reactorItr = models.iterator();

         while(reactorItr.hasNext()) {
            Model model = (Model)reactorItr.next();
            String reactorUrl = model.getUrl();
            String name = model.getName();
            this.appendMenuItem(project, menu, name, reactorUrl, model.getArtifactId());
         }
      }

   }

   private void appendMenuItem(MavenProject project, Menu menu, String name, String href, String defaultHref) {
      String selectedHref = href;
      if (href == null) {
         selectedHref = defaultHref;
      }

      MenuItem item = new MenuItem();
      item.setName(name);
      String baseUrl = project.getUrl();
      if (baseUrl != null) {
         selectedHref = this.getRelativePath(selectedHref, baseUrl);
      }

      if (selectedHref.endsWith("/")) {
         item.setHref(selectedHref + "index.html");
      } else {
         item.setHref(selectedHref + "/index.html");
      }

      menu.addItem(item);
   }

   private MenuItem createCategoryMenu(String name, String href, List categoryReports, Locale locale) {
      MenuItem item = new MenuItem();
      item.setName(name);
      item.setCollapse(true);
      item.setHref(href);
      Collections.sort(categoryReports, new ReportComparator(locale));
      Iterator k = categoryReports.iterator();

      while(k.hasNext()) {
         MavenReport report = (MavenReport)k.next();
         MenuItem subitem = new MenuItem();
         subitem.setName(report.getName(locale));
         subitem.setHref(report.getOutputName() + ".html");
         item.getItems().add(subitem);
      }

      return item;
   }

   private static boolean isEmptyList(List list) {
      return list == null || list.isEmpty();
   }
}
