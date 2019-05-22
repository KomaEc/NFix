package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.ResourceUtils;

public class ModifiedSelector extends BaseExtendSelector implements BuildListener, ResourceSelector {
   private ModifiedSelector.CacheName cacheName = null;
   private String cacheClass;
   private ModifiedSelector.AlgorithmName algoName = null;
   private String algorithmClass;
   private ModifiedSelector.ComparatorName compName = null;
   private String comparatorClass;
   private boolean update = true;
   private boolean selectDirectories = true;
   private boolean selectResourcesWithoutInputStream = true;
   private boolean delayUpdate = true;
   private Comparator comparator = null;
   private Algorithm algorithm = null;
   private Cache cache = null;
   private int modified = 0;
   private boolean isConfigured = false;
   private Vector configParameter = new Vector();
   private Vector specialParameter = new Vector();
   private ClassLoader myClassLoader = null;
   private Path classpath = null;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$selectors$modifiedselector$Algorithm;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$selectors$modifiedselector$Cache;
   // $FF: synthetic field
   static Class class$java$util$Comparator;

   public void verifySettings() {
      this.configure();
      if (this.cache == null) {
         this.setError("Cache must be set.");
      } else if (this.algorithm == null) {
         this.setError("Algorithm must be set.");
      } else if (!this.cache.isValid()) {
         this.setError("Cache must be proper configured.");
      } else if (!this.algorithm.isValid()) {
         this.setError("Algorithm must be proper configured.");
      }

   }

   public void configure() {
      if (!this.isConfigured) {
         this.isConfigured = true;
         Project p = this.getProject();
         String filename = "cache.properties";
         File cachefile = null;
         if (p != null) {
            cachefile = new File(p.getBaseDir(), filename);
            this.getProject().addBuildListener(this);
         } else {
            cachefile = new File(filename);
            this.setDelayUpdate(false);
         }

         Cache defaultCache = new PropertiesfileCache(cachefile);
         Algorithm defaultAlgorithm = new DigestAlgorithm();
         Comparator defaultComparator = new EqualComparator();
         this.update = true;
         this.selectDirectories = true;
         Iterator itSpecial = this.configParameter.iterator();

         Parameter par;
         while(itSpecial.hasNext()) {
            par = (Parameter)itSpecial.next();
            if (par.getName().indexOf(".") > 0) {
               this.specialParameter.add(par);
            } else {
               this.useParameter(par);
            }
         }

         this.configParameter = new Vector();
         if (this.algoName != null) {
            if ("hashvalue".equals(this.algoName.getValue())) {
               this.algorithm = new HashvalueAlgorithm();
            } else if ("digest".equals(this.algoName.getValue())) {
               this.algorithm = new DigestAlgorithm();
            } else if ("checksum".equals(this.algoName.getValue())) {
               this.algorithm = new ChecksumAlgorithm();
            }
         } else if (this.algorithmClass != null) {
            this.algorithm = (Algorithm)this.loadClass(this.algorithmClass, "is not an Algorithm.", class$org$apache$tools$ant$types$selectors$modifiedselector$Algorithm == null ? (class$org$apache$tools$ant$types$selectors$modifiedselector$Algorithm = class$("org.apache.tools.ant.types.selectors.modifiedselector.Algorithm")) : class$org$apache$tools$ant$types$selectors$modifiedselector$Algorithm);
         } else {
            this.algorithm = defaultAlgorithm;
         }

         if (this.cacheName != null) {
            if ("propertyfile".equals(this.cacheName.getValue())) {
               this.cache = new PropertiesfileCache();
            }
         } else if (this.cacheClass != null) {
            this.cache = (Cache)this.loadClass(this.cacheClass, "is not a Cache.", class$org$apache$tools$ant$types$selectors$modifiedselector$Cache == null ? (class$org$apache$tools$ant$types$selectors$modifiedselector$Cache = class$("org.apache.tools.ant.types.selectors.modifiedselector.Cache")) : class$org$apache$tools$ant$types$selectors$modifiedselector$Cache);
         } else {
            this.cache = defaultCache;
         }

         if (this.compName != null) {
            if ("equal".equals(this.compName.getValue())) {
               this.comparator = new EqualComparator();
            } else if ("rule".equals(this.compName.getValue())) {
               throw new BuildException("RuleBasedCollator not yet supported.");
            }
         } else if (this.comparatorClass != null) {
            this.comparator = (Comparator)this.loadClass(this.comparatorClass, "is not a Comparator.", class$java$util$Comparator == null ? (class$java$util$Comparator = class$("java.util.Comparator")) : class$java$util$Comparator);
         } else {
            this.comparator = defaultComparator;
         }

         itSpecial = this.specialParameter.iterator();

         while(itSpecial.hasNext()) {
            par = (Parameter)itSpecial.next();
            this.useParameter(par);
         }

         this.specialParameter = new Vector();
      }
   }

   protected Object loadClass(String classname, String msg, Class type) {
      try {
         ClassLoader cl = this.getClassLoader();
         Class clazz = null;
         if (cl != null) {
            clazz = cl.loadClass(classname);
         } else {
            clazz = Class.forName(classname);
         }

         Object rv = clazz.newInstance();
         if (!type.isInstance(rv)) {
            throw new BuildException("Specified class (" + classname + ") " + msg);
         } else {
            return rv;
         }
      } catch (ClassNotFoundException var7) {
         throw new BuildException("Specified class (" + classname + ") not found.");
      } catch (Exception var8) {
         throw new BuildException(var8);
      }
   }

   public boolean isSelected(Resource resource) {
      File tmpFile;
      if (resource.isFilesystemOnly()) {
         FileResource fileResource = (FileResource)resource;
         tmpFile = fileResource.getFile();
         String filename = fileResource.getName();
         File basedir = fileResource.getBaseDir();
         return this.isSelected(basedir, filename, tmpFile);
      } else {
         try {
            FileUtils fu = FileUtils.getFileUtils();
            tmpFile = fu.createTempFile("modified-", ".tmp", (File)null);
            Resource tmpResource = new FileResource(tmpFile);
            ResourceUtils.copyResource(resource, tmpResource);
            boolean isSelected = this.isSelected(tmpFile.getParentFile(), tmpFile.getName(), resource.toLongString());
            tmpFile.delete();
            return isSelected;
         } catch (UnsupportedOperationException var6) {
            this.log("The resource '" + resource.getName() + "' does not provide an InputStream, so it is not checked. " + "Akkording to 'selres' attribute value it is " + (this.selectResourcesWithoutInputStream ? "" : " not") + "selected.", 2);
            return this.selectResourcesWithoutInputStream;
         } catch (Exception var7) {
            throw new BuildException(var7);
         }
      }
   }

   public boolean isSelected(File basedir, String filename, File file) {
      return this.isSelected(basedir, filename, file.getAbsolutePath());
   }

   private boolean isSelected(File basedir, String filename, String cacheKey) {
      this.validate();
      File f = new File(basedir, filename);
      if (f.isDirectory()) {
         return this.selectDirectories;
      } else {
         String cachedValue = String.valueOf(this.cache.get(f.getAbsolutePath()));
         String newValue = this.algorithm.getValue(f);
         boolean rv = this.comparator.compare(cachedValue, newValue) != 0;
         if (this.update && rv) {
            this.cache.put(f.getAbsolutePath(), newValue);
            this.setModified(this.getModified() + 1);
            if (!this.getDelayUpdate()) {
               this.saveCache();
            }
         }

         return rv;
      }
   }

   protected void saveCache() {
      if (this.getModified() > 1) {
         this.cache.save();
         this.setModified(0);
      }

   }

   public void setAlgorithmClass(String classname) {
      this.algorithmClass = classname;
   }

   public void setComparatorClass(String classname) {
      this.comparatorClass = classname;
   }

   public void setCacheClass(String classname) {
      this.cacheClass = classname;
   }

   public void setUpdate(boolean update) {
      this.update = update;
   }

   public void setSeldirs(boolean seldirs) {
      this.selectDirectories = seldirs;
   }

   public void setSelres(boolean newValue) {
      this.selectResourcesWithoutInputStream = newValue;
   }

   public int getModified() {
      return this.modified;
   }

   public void setModified(int modified) {
      this.modified = modified;
   }

   public boolean getDelayUpdate() {
      return this.delayUpdate;
   }

   public void setDelayUpdate(boolean delayUpdate) {
      this.delayUpdate = delayUpdate;
   }

   public void addClasspath(Path path) {
      if (this.classpath != null) {
         throw new BuildException("<classpath> can be set only once.");
      } else {
         this.classpath = path;
      }
   }

   public ClassLoader getClassLoader() {
      if (this.myClassLoader == null) {
         this.myClassLoader = (ClassLoader)(this.classpath == null ? this.getClass().getClassLoader() : this.getProject().createClassLoader(this.classpath));
      }

      return this.myClassLoader;
   }

   public void setClassLoader(ClassLoader loader) {
      this.myClassLoader = loader;
   }

   public void addParam(String key, Object value) {
      Parameter par = new Parameter();
      par.setName(key);
      par.setValue(String.valueOf(value));
      this.configParameter.add(par);
   }

   public void addParam(Parameter parameter) {
      this.configParameter.add(parameter);
   }

   public void setParameters(Parameter[] parameters) {
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            this.configParameter.add(parameters[i]);
         }
      }

   }

   public void useParameter(Parameter parameter) {
      String key = parameter.getName();
      String value = parameter.getValue();
      if ("cache".equals(key)) {
         ModifiedSelector.CacheName cn = new ModifiedSelector.CacheName();
         cn.setValue(value);
         this.setCache(cn);
      } else if ("algorithm".equals(key)) {
         ModifiedSelector.AlgorithmName an = new ModifiedSelector.AlgorithmName();
         an.setValue(value);
         this.setAlgorithm(an);
      } else if ("comparator".equals(key)) {
         ModifiedSelector.ComparatorName cn = new ModifiedSelector.ComparatorName();
         cn.setValue(value);
         this.setComparator(cn);
      } else {
         boolean sdValue;
         if ("update".equals(key)) {
            sdValue = "true".equalsIgnoreCase(value);
            this.setUpdate(sdValue);
         } else if ("delayupdate".equals(key)) {
            sdValue = "true".equalsIgnoreCase(value);
            this.setDelayUpdate(sdValue);
         } else if ("seldirs".equals(key)) {
            sdValue = "true".equalsIgnoreCase(value);
            this.setSeldirs(sdValue);
         } else {
            String name;
            if (key.startsWith("cache.")) {
               name = key.substring(6);
               this.tryToSetAParameter(this.cache, name, value);
            } else if (key.startsWith("algorithm.")) {
               name = key.substring(10);
               this.tryToSetAParameter(this.algorithm, name, value);
            } else if (key.startsWith("comparator.")) {
               name = key.substring(11);
               this.tryToSetAParameter(this.comparator, name, value);
            } else {
               this.setError("Invalid parameter " + key);
            }
         }
      }

   }

   protected void tryToSetAParameter(Object obj, String name, String value) {
      Project prj = this.getProject() != null ? this.getProject() : new Project();
      IntrospectionHelper iHelper = IntrospectionHelper.getHelper(prj, obj.getClass());

      try {
         iHelper.setAttribute(prj, obj, name, value);
      } catch (BuildException var7) {
      }

   }

   public String toString() {
      StringBuffer buf = new StringBuffer("{modifiedselector");
      buf.append(" update=").append(this.update);
      buf.append(" seldirs=").append(this.selectDirectories);
      buf.append(" cache=").append(this.cache);
      buf.append(" algorithm=").append(this.algorithm);
      buf.append(" comparator=").append(this.comparator);
      buf.append("}");
      return buf.toString();
   }

   public void buildFinished(BuildEvent event) {
      if (this.getDelayUpdate()) {
         this.saveCache();
      }

   }

   public void targetFinished(BuildEvent event) {
      if (this.getDelayUpdate()) {
         this.saveCache();
      }

   }

   public void taskFinished(BuildEvent event) {
      if (this.getDelayUpdate()) {
         this.saveCache();
      }

   }

   public void buildStarted(BuildEvent event) {
   }

   public void targetStarted(BuildEvent event) {
   }

   public void taskStarted(BuildEvent event) {
   }

   public void messageLogged(BuildEvent event) {
   }

   public Cache getCache() {
      return this.cache;
   }

   public void setCache(ModifiedSelector.CacheName name) {
      this.cacheName = name;
   }

   public Algorithm getAlgorithm() {
      return this.algorithm;
   }

   public void setAlgorithm(ModifiedSelector.AlgorithmName name) {
      this.algoName = name;
   }

   public Comparator getComparator() {
      return this.comparator;
   }

   public void setComparator(ModifiedSelector.ComparatorName name) {
      this.compName = name;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class ComparatorName extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"equal", "rule"};
      }
   }

   public static class AlgorithmName extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"hashvalue", "digest", "checksum"};
      }
   }

   public static class CacheName extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"propertyfile"};
      }
   }
}
