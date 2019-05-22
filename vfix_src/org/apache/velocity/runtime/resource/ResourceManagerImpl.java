package org.apache.velocity.runtime.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.runtime.resource.loader.ResourceLoaderFactory;
import org.apache.velocity.util.ClassUtils;

public class ResourceManagerImpl implements ResourceManager {
   public static final int RESOURCE_TEMPLATE = 1;
   public static final int RESOURCE_CONTENT = 2;
   private static final String RESOURCE_LOADER_IDENTIFIER = "_RESOURCE_LOADER_IDENTIFIER_";
   protected ResourceCache globalCache = null;
   protected final List resourceLoaders = new ArrayList();
   private final List sourceInitializerList = new ArrayList();
   private boolean isInit = false;
   private boolean logWhenFound = true;
   protected RuntimeServices rsvc = null;
   protected Log log = null;
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$resource$ResourceCache;

   public synchronized void initialize(RuntimeServices rsvc) throws Exception {
      if (this.isInit) {
         this.log.warn("Re-initialization of ResourceLoader attempted!");
      } else {
         ResourceLoader resourceLoader = null;
         this.rsvc = rsvc;
         this.log = rsvc.getLog();
         this.log.debug("Default ResourceManager initializing. (" + this.getClass() + ")");
         this.assembleResourceLoaderInitializers();
         Iterator it = this.sourceInitializerList.iterator();

         while(true) {
            ExtendedProperties configuration;
            while(true) {
               if (!it.hasNext()) {
                  this.logWhenFound = rsvc.getBoolean("resource.manager.logwhenfound", true);
                  String cacheClassName = rsvc.getString("resource.manager.cache.class");
                  Object cacheObject = null;
                  if (StringUtils.isNotEmpty(cacheClassName)) {
                     try {
                        cacheObject = ClassUtils.getNewInstance(cacheClassName);
                     } catch (ClassNotFoundException var7) {
                        this.log.error("The specified class for ResourceCache (" + cacheClassName + ") does not exist or is not accessible to the current classloader.");
                        cacheObject = null;
                     }

                     if (!(cacheObject instanceof ResourceCache)) {
                        this.log.error("The specified class for ResourceCache (" + cacheClassName + ") does not implement " + (class$org$apache$velocity$runtime$resource$ResourceCache == null ? (class$org$apache$velocity$runtime$resource$ResourceCache = class$("org.apache.velocity.runtime.resource.ResourceCache")) : class$org$apache$velocity$runtime$resource$ResourceCache).getName() + " ResourceManager. Using default ResourceCache implementation.");
                        cacheObject = null;
                     }
                  }

                  if (cacheObject == null) {
                     cacheObject = new ResourceCacheImpl();
                  }

                  this.globalCache = (ResourceCache)cacheObject;
                  this.globalCache.initialize(rsvc);
                  this.log.trace("Default ResourceManager initialization complete.");
                  return;
               }

               configuration = (ExtendedProperties)it.next();
               String loaderClass = org.apache.velocity.util.StringUtils.nullTrim(configuration.getString("class"));
               ResourceLoader loaderInstance = (ResourceLoader)configuration.get("instance");
               if (loaderInstance != null) {
                  resourceLoader = loaderInstance;
                  break;
               }

               if (loaderClass != null) {
                  resourceLoader = ResourceLoaderFactory.getLoader(rsvc, loaderClass);
                  break;
               }

               this.log.error("Unable to find '" + configuration.getString("_RESOURCE_LOADER_IDENTIFIER_") + ".resource.loader.class' specification in configuration." + " This is a critical value.  Please adjust configuration.");
            }

            resourceLoader.commonInit(rsvc, configuration);
            resourceLoader.init(configuration);
            this.resourceLoaders.add(resourceLoader);
         }
      }
   }

   private void assembleResourceLoaderInitializers() {
      Vector resourceLoaderNames = this.rsvc.getConfiguration().getVector("resource.loader");
      org.apache.velocity.util.StringUtils.trimStrings(resourceLoaderNames);
      Iterator it = resourceLoaderNames.iterator();

      while(it.hasNext()) {
         String loaderName = (String)it.next();
         StringBuffer loaderID = new StringBuffer(loaderName);
         loaderID.append(".").append("resource.loader");
         ExtendedProperties loaderConfiguration = this.rsvc.getConfiguration().subset(loaderID.toString());
         if (loaderConfiguration == null) {
            this.log.warn("ResourceManager : No configuration information for resource loader named '" + loaderName + "'. Skipping.");
         } else {
            loaderConfiguration.setProperty("_RESOURCE_LOADER_IDENTIFIER_", loaderName);
            this.sourceInitializerList.add(loaderConfiguration);
         }
      }

   }

   public synchronized Resource getResource(String resourceName, int resourceType, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      String resourceKey = resourceType + resourceName;
      Resource resource = this.globalCache.get(resourceKey);
      if (resource != null) {
         try {
            this.refreshResource(resource, encoding);
         } catch (ResourceNotFoundException var11) {
            this.globalCache.remove(resourceKey);
            return this.getResource(resourceName, resourceType, encoding);
         } catch (ParseErrorException var12) {
            this.log.error("ResourceManager.getResource() exception", var12);
            throw var12;
         } catch (RuntimeException var13) {
            throw var13;
         } catch (Exception var14) {
            this.log.error("ResourceManager.getResource() exception", var14);
            throw var14;
         }
      } else {
         try {
            resource = this.loadResource(resourceName, resourceType, encoding);
            if (resource.getResourceLoader().isCachingOn()) {
               this.globalCache.put(resourceKey, resource);
            }
         } catch (ResourceNotFoundException var7) {
            this.log.error("ResourceManager : unable to find resource '" + resourceName + "' in any resource loader.");
            throw var7;
         } catch (ParseErrorException var8) {
            this.log.error("ResourceManager.getResource() parse exception", var8);
            throw var8;
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Exception var10) {
            this.log.error("ResourceManager.getResource() exception new", var10);
            throw var10;
         }
      }

      return resource;
   }

   protected Resource loadResource(String resourceName, int resourceType, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      Resource resource = ResourceFactory.getResource(resourceName, resourceType);
      resource.setRuntimeServices(this.rsvc);
      resource.setName(resourceName);
      resource.setEncoding(encoding);
      long howOldItWas = 0L;
      Iterator it = this.resourceLoaders.iterator();

      while(it.hasNext()) {
         ResourceLoader resourceLoader = (ResourceLoader)it.next();
         resource.setResourceLoader(resourceLoader);

         try {
            if (resource.process()) {
               if (this.logWhenFound && this.log.isDebugEnabled()) {
                  this.log.debug("ResourceManager : found " + resourceName + " with loader " + resourceLoader.getClassName());
               }

               howOldItWas = resourceLoader.getLastModified(resource);
               break;
            }
         } catch (ResourceNotFoundException var10) {
         }
      }

      if (resource.getData() == null) {
         throw new ResourceNotFoundException("Unable to find resource '" + resourceName + "'");
      } else {
         resource.setLastModified(howOldItWas);
         resource.setModificationCheckInterval(resource.getResourceLoader().getModificationCheckInterval());
         resource.touch();
         return resource;
      }
   }

   protected void refreshResource(Resource resource, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
      if (resource.requiresChecking()) {
         resource.touch();
         if (resource.isSourceModified()) {
            if (!StringUtils.equals(resource.getEncoding(), encoding)) {
               this.log.warn("Declared encoding for template '" + resource.getName() + "' is different on reload. Old = '" + resource.getEncoding() + "' New = '" + encoding);
               resource.setEncoding(encoding);
            }

            long howOldItWas = resource.getResourceLoader().getLastModified(resource);
            resource.process();
            resource.setLastModified(howOldItWas);
         }
      }

   }

   /** @deprecated */
   public Resource getResource(String resourceName, int resourceType) throws ResourceNotFoundException, ParseErrorException, Exception {
      return this.getResource(resourceName, resourceType, "ISO-8859-1");
   }

   public String getLoaderNameForResource(String resourceName) {
      Iterator it = this.resourceLoaders.iterator();

      while(true) {
         if (it.hasNext()) {
            ResourceLoader resourceLoader = (ResourceLoader)it.next();
            InputStream is = null;

            String var5;
            try {
               is = resourceLoader.getResourceStream(resourceName);
               if (is == null) {
                  continue;
               }

               var5 = resourceLoader.getClass().toString();
            } catch (ResourceNotFoundException var16) {
               continue;
            } finally {
               if (is != null) {
                  try {
                     is.close();
                  } catch (IOException var15) {
                  }
               }

            }

            return var5;
         }

         return null;
      }
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
