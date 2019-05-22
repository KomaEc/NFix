package org.apache.velocity.runtime.resource.loader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.util.StringResource;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.util.ClassUtils;

public class StringResourceLoader extends ResourceLoader {
   public static final String REPOSITORY_CLASS = "repository.class";
   public static final String REPOSITORY_CLASS_DEFAULT;
   public static final String REPOSITORY_ENCODING = "repository.encoding";
   public static final String REPOSITORY_ENCODING_DEFAULT = "UTF-8";
   // $FF: synthetic field
   static Class class$org$apache$velocity$runtime$resource$util$StringResourceRepositoryImpl;

   public static StringResourceRepository getRepository() {
      return StringResourceLoader.RepositoryFactory.getRepository();
   }

   public void init(ExtendedProperties configuration) {
      this.log.info("StringResourceLoader : initialization starting.");
      String repositoryClass = configuration.getString("repository.class", REPOSITORY_CLASS_DEFAULT);
      String encoding = configuration.getString("repository.encoding", "UTF-8");
      StringResourceLoader.RepositoryFactory.setRepositoryClass(repositoryClass);
      StringResourceLoader.RepositoryFactory.setEncoding(encoding);
      StringResourceLoader.RepositoryFactory.init(this.log);
      this.log.info("StringResourceLoader : initialization complete.");
   }

   public InputStream getResourceStream(String name) throws ResourceNotFoundException {
      if (StringUtils.isEmpty(name)) {
         throw new ResourceNotFoundException("No template name provided");
      } else {
         StringResource resource = getRepository().getStringResource(name);
         if (resource == null) {
            throw new ResourceNotFoundException("Could not locate resource '" + name + "'");
         } else {
            Object var3 = null;

            try {
               byte[] byteArray = resource.getBody().getBytes(resource.getEncoding());
               return new ByteArrayInputStream(byteArray);
            } catch (UnsupportedEncodingException var5) {
               throw new VelocityException("Could not convert String using encoding " + resource.getEncoding(), var5);
            }
         }
      }
   }

   public boolean isSourceModified(Resource resource) {
      StringResource original = null;
      boolean result = true;
      original = getRepository().getStringResource(resource.getName());
      if (original != null) {
         result = original.getLastModified() != resource.getLastModified();
      }

      return result;
   }

   public long getLastModified(Resource resource) {
      StringResource original = null;
      original = getRepository().getStringResource(resource.getName());
      return original != null ? original.getLastModified() : 0L;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      REPOSITORY_CLASS_DEFAULT = (class$org$apache$velocity$runtime$resource$util$StringResourceRepositoryImpl == null ? (class$org$apache$velocity$runtime$resource$util$StringResourceRepositoryImpl = class$("org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl")) : class$org$apache$velocity$runtime$resource$util$StringResourceRepositoryImpl).getName();
   }

   private static final class RepositoryFactory {
      private static boolean isInitialized = false;
      private static StringResourceRepository repository = null;

      public static void setRepositoryClass(String className) {
         if (isInitialized) {
            throw new IllegalStateException("The RepositoryFactory has already been initialized!");
         } else {
            try {
               repository = (StringResourceRepository)ClassUtils.getNewInstance(className);
            } catch (ClassNotFoundException var2) {
               throw new VelocityException("Could not find '" + className + "'", var2);
            } catch (IllegalAccessException var3) {
               throw new VelocityException("Could not access '" + className + "'", var3);
            } catch (InstantiationException var4) {
               throw new VelocityException("Could not instantiante '" + className + "'", var4);
            }
         }
      }

      public static void setEncoding(String encoding) {
         if (repository == null) {
            throw new IllegalStateException("The Repository class has not yet been set!");
         } else {
            repository.setEncoding(encoding);
         }
      }

      public static synchronized void init(Log log) throws VelocityException {
         if (isInitialized) {
            throw new IllegalStateException("Attempted to re-initialize Factory!");
         } else {
            if (log.isInfoEnabled()) {
               log.info("Using " + repository.getClass().getName() + " as repository implementation");
               log.info("Current repository encoding is " + repository.getEncoding());
            }

            isInitialized = true;
         }
      }

      public static StringResourceRepository getRepository() {
         if (!isInitialized) {
            throw new IllegalStateException("RepositoryFactory was not properly set up");
         } else {
            return repository;
         }
      }
   }
}
