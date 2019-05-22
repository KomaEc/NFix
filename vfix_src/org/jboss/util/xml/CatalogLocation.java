package org.jboss.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.jboss.logging.Logger;
import org.jboss.util.xml.catalog.CatalogManager;
import org.jboss.util.xml.catalog.Resolver;
import org.xml.sax.InputSource;

public class CatalogLocation {
   private static Logger log = Logger.getLogger(CatalogLocation.class);
   private static final String[] catalogFilesNames = new String[]{"META-INF/jax-ws-catalog.xml", "WEB-INF/jax-ws-catalog.xml", "jax-ws-catalog.xml"};
   private final Resolver catologResolver = new Resolver();
   private final URL location;
   private boolean isLastEntityResolved = false;

   public CatalogLocation(URL url) throws IOException {
      this.catologResolver.setCatalogManager(CatalogManager.getStaticManager());
      this.catologResolver.setupReaders();
      this.catologResolver.parseCatalog(url);
      this.location = url;
   }

   public InputSource resolveEntity(String publicId, String systemId) throws MalformedURLException, IOException {
      String resolvedURI = this.catologResolver.resolveSystem(systemId);
      if (resolvedURI == null) {
         resolvedURI = this.catologResolver.resolvePublic(publicId, systemId);
      }

      if (resolvedURI != null) {
         InputSource is = new InputSource();
         is.setPublicId(publicId);
         is.setSystemId(systemId);
         is.setByteStream(this.loadResource(resolvedURI));
         this.isLastEntityResolved = true;
         return is;
      } else {
         this.isLastEntityResolved = false;
         return null;
      }
   }

   public static URL lookupCatalogFiles() throws IOException {
      URL url = null;
      ClassLoader loader = Thread.currentThread().getContextClassLoader();

      for(int i = 0; i < catalogFilesNames.length; ++i) {
         url = loader.getResource(catalogFilesNames[i]);
         if (url != null) {
            break;
         }
      }

      return url;
   }

   public boolean isEntityResolved() {
      return this.isLastEntityResolved;
   }

   private InputStream loadResource(String resolvedURI) throws IOException {
      try {
         URI toLoad = new URI(resolvedURI);
         InputStream inputStream = null;
         if (toLoad != null) {
            try {
               inputStream = new FileInputStream(new File(toLoad));
            } catch (IOException var5) {
               log.error("Failed to open url stream", var5);
               throw var5;
            }
         }

         return inputStream;
      } catch (URISyntaxException var6) {
         log.error("The URI (" + resolvedURI + ") is malfomed");
         throw new IOException("The URI (" + resolvedURI + ") is malfomed");
      }
   }

   public boolean equals(Object other) {
      boolean back = false;
      if (other != null && other instanceof CatalogLocation) {
         CatalogLocation otherC = (CatalogLocation)other;
         back = this.location.equals(otherC.location);
      }

      return back;
   }

   public int hashCode() {
      return this.location.hashCode();
   }

   static {
      System.setProperty("xml.catalog.allowPI", "true");
      System.setProperty("xml.catalog.prefer", "public");
      System.setProperty("xml.catalog.verbosity", "0");
   }
}
