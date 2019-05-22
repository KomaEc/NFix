package org.apache.tools.ant.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.JAXPUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLCatalog extends DataType implements Cloneable, EntityResolver, URIResolver {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private Vector elements = new Vector();
   private Path classpath;
   private Path catalogPath;
   public static final String APACHE_RESOLVER = "org.apache.tools.ant.types.resolver.ApacheCatalogResolver";
   public static final String CATALOG_RESOLVER = "org.apache.xml.resolver.tools.CatalogResolver";
   private XMLCatalog.CatalogResolver catalogResolver = null;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$XMLCatalog;
   // $FF: synthetic field
   static Class class$java$lang$String;

   public XMLCatalog() {
      this.setChecked(false);
   }

   private Vector getElements() {
      return this.getRef().elements;
   }

   private Path getClasspath() {
      return this.getRef().classpath;
   }

   public Path createClasspath() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         if (this.classpath == null) {
            this.classpath = new Path(this.getProject());
         }

         this.setChecked(false);
         return this.classpath.createPath();
      }
   }

   public void setClasspath(Path classpath) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (this.classpath == null) {
            this.classpath = classpath;
         } else {
            this.classpath.append(classpath);
         }

         this.setChecked(false);
      }
   }

   public void setClasspathRef(Reference r) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createClasspath().setRefid(r);
         this.setChecked(false);
      }
   }

   public Path createCatalogPath() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         if (this.catalogPath == null) {
            this.catalogPath = new Path(this.getProject());
         }

         this.setChecked(false);
         return this.catalogPath.createPath();
      }
   }

   public void setCatalogPathRef(Reference r) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.createCatalogPath().setRefid(r);
         this.setChecked(false);
      }
   }

   public Path getCatalogPath() {
      return this.getRef().catalogPath;
   }

   public void addDTD(ResourceLocation dtd) throws BuildException {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.getElements().addElement(dtd);
         this.setChecked(false);
      }
   }

   public void addEntity(ResourceLocation entity) throws BuildException {
      this.addDTD(entity);
   }

   public void addConfiguredXMLCatalog(XMLCatalog catalog) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         Vector newElements = catalog.getElements();
         Vector ourElements = this.getElements();
         Enumeration e = newElements.elements();

         while(e.hasMoreElements()) {
            ourElements.addElement(e.nextElement());
         }

         Path nestedClasspath = catalog.getClasspath();
         this.createClasspath().append(nestedClasspath);
         Path nestedCatalogPath = catalog.getCatalogPath();
         this.createCatalogPath().append(nestedCatalogPath);
         this.setChecked(false);
      }
   }

   public void setRefid(Reference r) throws BuildException {
      if (!this.elements.isEmpty()) {
         throw this.tooManyAttributes();
      } else {
         super.setRefid(r);
      }
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      if (this.isReference()) {
         return this.getRef().resolveEntity(publicId, systemId);
      } else {
         this.dieOnCircularReference();
         this.log("resolveEntity: '" + publicId + "': '" + systemId + "'", 4);
         InputSource inputSource = this.getCatalogResolver().resolveEntity(publicId, systemId);
         if (inputSource == null) {
            this.log("No matching catalog entry found, parser will use: '" + systemId + "'", 4);
         }

         return inputSource;
      }
   }

   public Source resolve(String href, String base) throws TransformerException {
      if (this.isReference()) {
         return this.getRef().resolve(href, base);
      } else {
         this.dieOnCircularReference();
         SAXSource source = null;
         String uri = this.removeFragment(href);
         this.log("resolve: '" + uri + "' with base: '" + base + "'", 4);
         source = (SAXSource)this.getCatalogResolver().resolve(uri, base);
         if (source == null) {
            this.log("No matching catalog entry found, parser will use: '" + href + "'", 4);
            source = new SAXSource();
            URL baseURL = null;

            try {
               if (base == null) {
                  baseURL = FILE_UTILS.getFileURL(this.getProject().getBaseDir());
               } else {
                  baseURL = new URL(base);
               }

               URL url = uri.length() == 0 ? baseURL : new URL(baseURL, uri);
               source.setInputSource(new InputSource(url.toString()));
            } catch (MalformedURLException var7) {
               source.setInputSource(new InputSource(uri));
            }
         }

         this.setEntityResolver(source);
         return source;
      }
   }

   private XMLCatalog getRef() {
      return !this.isReference() ? this : (XMLCatalog)this.getCheckedRef(class$org$apache$tools$ant$types$XMLCatalog == null ? (class$org$apache$tools$ant$types$XMLCatalog = class$("org.apache.tools.ant.types.XMLCatalog")) : class$org$apache$tools$ant$types$XMLCatalog, "xmlcatalog");
   }

   private XMLCatalog.CatalogResolver getCatalogResolver() {
      if (this.catalogResolver == null) {
         AntClassLoader loader = null;
         loader = this.getProject().createClassLoader(Path.systemClasspath);

         try {
            Class clazz = Class.forName("org.apache.tools.ant.types.resolver.ApacheCatalogResolver", true, loader);
            ClassLoader apacheResolverLoader = clazz.getClassLoader();
            Class baseResolverClass = Class.forName("org.apache.xml.resolver.tools.CatalogResolver", true, apacheResolverLoader);
            ClassLoader baseResolverLoader = baseResolverClass.getClassLoader();
            clazz = Class.forName("org.apache.tools.ant.types.resolver.ApacheCatalogResolver", true, baseResolverLoader);
            Object obj = clazz.newInstance();
            this.catalogResolver = new XMLCatalog.ExternalResolver(clazz, obj);
         } catch (Throwable var7) {
            this.catalogResolver = new XMLCatalog.InternalResolver();
            if (this.getCatalogPath() != null && this.getCatalogPath().list().length != 0) {
               this.log("Warning: XML resolver not found; external catalogs will be ignored", 1);
            }

            this.log("Failed to load Apache resolver: " + var7, 4);
         }
      }

      return this.catalogResolver;
   }

   private void setEntityResolver(SAXSource source) throws TransformerException {
      XMLReader reader = source.getXMLReader();
      if (reader == null) {
         SAXParserFactory spFactory = SAXParserFactory.newInstance();
         spFactory.setNamespaceAware(true);

         try {
            reader = spFactory.newSAXParser().getXMLReader();
         } catch (ParserConfigurationException var5) {
            throw new TransformerException(var5);
         } catch (SAXException var6) {
            throw new TransformerException(var6);
         }
      }

      reader.setEntityResolver(this);
      source.setXMLReader(reader);
   }

   private ResourceLocation findMatchingEntry(String publicId) {
      Enumeration e = this.getElements().elements();
      ResourceLocation element = null;

      while(e.hasMoreElements()) {
         Object o = e.nextElement();
         if (o instanceof ResourceLocation) {
            element = (ResourceLocation)o;
            if (element.getPublicId().equals(publicId)) {
               return element;
            }
         }
      }

      return null;
   }

   private String removeFragment(String uri) {
      String result = uri;
      int hashPos = uri.indexOf("#");
      if (hashPos >= 0) {
         result = uri.substring(0, hashPos);
      }

      return result;
   }

   private InputSource filesystemLookup(ResourceLocation matchingEntry) {
      String uri = matchingEntry.getLocation();
      uri = uri.replace(File.separatorChar, '/');
      URL baseURL = null;
      if (matchingEntry.getBase() != null) {
         baseURL = matchingEntry.getBase();
      } else {
         try {
            baseURL = FILE_UTILS.getFileURL(this.getProject().getBaseDir());
         } catch (MalformedURLException var11) {
            throw new BuildException("Project basedir cannot be converted to a URL");
         }
      }

      InputSource source = null;
      URL url = null;

      File resFile;
      try {
         url = new URL(baseURL, uri);
      } catch (MalformedURLException var12) {
         resFile = new File(uri);
         if (resFile.exists() && resFile.canRead()) {
            this.log("uri : '" + uri + "' matches a readable file", 4);

            try {
               url = FILE_UTILS.getFileURL(resFile);
            } catch (MalformedURLException var10) {
               throw new BuildException("could not find an URL for :" + resFile.getAbsolutePath());
            }
         } else {
            this.log("uri : '" + uri + "' does not match a readable file", 4);
         }
      }

      if (url != null && url.getProtocol().equals("file")) {
         String fileName = FILE_UTILS.fromURI(url.toString());
         if (fileName != null) {
            this.log("fileName " + fileName, 4);
            resFile = new File(fileName);
            if (resFile.exists() && resFile.canRead()) {
               try {
                  source = new InputSource(new FileInputStream(resFile));
                  String sysid = JAXPUtils.getSystemId(resFile);
                  source.setSystemId(sysid);
                  this.log("catalog entry matched a readable file: '" + sysid + "'", 4);
               } catch (IOException var9) {
               }
            }
         }
      }

      return source;
   }

   private InputSource classpathLookup(ResourceLocation matchingEntry) {
      InputSource source = null;
      AntClassLoader loader = null;
      Path cp = this.classpath;
      if (cp != null) {
         cp = this.classpath.concatSystemClasspath("ignore");
      } else {
         cp = (new Path(this.getProject())).concatSystemClasspath("last");
      }

      loader = this.getProject().createClassLoader(cp);
      InputStream is = loader.getResourceAsStream(matchingEntry.getLocation());
      if (is != null) {
         source = new InputSource(is);
         URL entryURL = loader.getResource(matchingEntry.getLocation());
         String sysid = entryURL.toExternalForm();
         source.setSystemId(sysid);
         this.log("catalog entry matched a resource in the classpath: '" + sysid + "'", 4);
      }

      return source;
   }

   private InputSource urlLookup(ResourceLocation matchingEntry) {
      String uri = matchingEntry.getLocation();
      URL baseURL = null;
      if (matchingEntry.getBase() != null) {
         baseURL = matchingEntry.getBase();
      } else {
         try {
            baseURL = FILE_UTILS.getFileURL(this.getProject().getBaseDir());
         } catch (MalformedURLException var10) {
            throw new BuildException("Project basedir cannot be converted to a URL");
         }
      }

      InputSource source = null;
      URL url = null;

      try {
         url = new URL(baseURL, uri);
      } catch (MalformedURLException var9) {
      }

      if (url != null) {
         try {
            InputStream is = url.openStream();
            if (is != null) {
               source = new InputSource(is);
               String sysid = url.toExternalForm();
               source.setSystemId(sysid);
               this.log("catalog entry matched as a URL: '" + sysid + "'", 4);
            }
         } catch (IOException var8) {
         }
      }

      return source;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   private class ExternalResolver implements XMLCatalog.CatalogResolver {
      private Method setXMLCatalog = null;
      private Method parseCatalog = null;
      private Method resolveEntity = null;
      private Method resolve = null;
      private Object resolverImpl = null;
      private boolean externalCatalogsProcessed = false;

      public ExternalResolver(Class resolverImplClass, Object resolverImpl) {
         this.resolverImpl = resolverImpl;

         try {
            this.setXMLCatalog = resolverImplClass.getMethod("setXMLCatalog", XMLCatalog.class$org$apache$tools$ant$types$XMLCatalog == null ? (XMLCatalog.class$org$apache$tools$ant$types$XMLCatalog = XMLCatalog.class$("org.apache.tools.ant.types.XMLCatalog")) : XMLCatalog.class$org$apache$tools$ant$types$XMLCatalog);
            this.parseCatalog = resolverImplClass.getMethod("parseCatalog", XMLCatalog.class$java$lang$String == null ? (XMLCatalog.class$java$lang$String = XMLCatalog.class$("java.lang.String")) : XMLCatalog.class$java$lang$String);
            this.resolveEntity = resolverImplClass.getMethod("resolveEntity", XMLCatalog.class$java$lang$String == null ? (XMLCatalog.class$java$lang$String = XMLCatalog.class$("java.lang.String")) : XMLCatalog.class$java$lang$String, XMLCatalog.class$java$lang$String == null ? (XMLCatalog.class$java$lang$String = XMLCatalog.class$("java.lang.String")) : XMLCatalog.class$java$lang$String);
            this.resolve = resolverImplClass.getMethod("resolve", XMLCatalog.class$java$lang$String == null ? (XMLCatalog.class$java$lang$String = XMLCatalog.class$("java.lang.String")) : XMLCatalog.class$java$lang$String, XMLCatalog.class$java$lang$String == null ? (XMLCatalog.class$java$lang$String = XMLCatalog.class$("java.lang.String")) : XMLCatalog.class$java$lang$String);
         } catch (NoSuchMethodException var5) {
            throw new BuildException(var5);
         }

         XMLCatalog.this.log("Apache resolver library found, xml-commons resolver will be used", 3);
      }

      public InputSource resolveEntity(String publicId, String systemId) {
         InputSource result = null;
         this.processExternalCatalogs();
         ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
         if (matchingEntry != null) {
            XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
            result = XMLCatalog.this.filesystemLookup(matchingEntry);
            if (result == null) {
               result = XMLCatalog.this.classpathLookup(matchingEntry);
            }

            if (result == null) {
               try {
                  result = (InputSource)this.resolveEntity.invoke(this.resolverImpl, publicId, systemId);
               } catch (Exception var7) {
                  throw new BuildException(var7);
               }
            }
         } else {
            try {
               result = (InputSource)this.resolveEntity.invoke(this.resolverImpl, publicId, systemId);
            } catch (Exception var6) {
               throw new BuildException(var6);
            }
         }

         return result;
      }

      public Source resolve(String href, String base) throws TransformerException {
         SAXSource result = null;
         InputSource source = null;
         this.processExternalCatalogs();
         ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
         if (matchingEntry != null) {
            XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
            ResourceLocation entryCopy = matchingEntry;
            if (base != null) {
               try {
                  URL baseURL = new URL(base);
                  entryCopy = new ResourceLocation();
                  entryCopy.setBase(baseURL);
               } catch (MalformedURLException var10) {
               }
            }

            entryCopy.setPublicId(matchingEntry.getPublicId());
            entryCopy.setLocation(matchingEntry.getLocation());
            source = XMLCatalog.this.filesystemLookup(entryCopy);
            if (source == null) {
               source = XMLCatalog.this.classpathLookup(entryCopy);
            }

            if (source != null) {
               result = new SAXSource(source);
            } else {
               try {
                  result = (SAXSource)this.resolve.invoke(this.resolverImpl, href, base);
               } catch (Exception var9) {
                  throw new BuildException(var9);
               }
            }
         } else {
            try {
               result = (SAXSource)this.resolve.invoke(this.resolverImpl, href, base);
            } catch (Exception var8) {
               throw new BuildException(var8);
            }
         }

         return result;
      }

      private void processExternalCatalogs() {
         if (!this.externalCatalogsProcessed) {
            try {
               this.setXMLCatalog.invoke(this.resolverImpl, XMLCatalog.this);
            } catch (Exception var7) {
               throw new BuildException(var7);
            }

            Path catPath = XMLCatalog.this.getCatalogPath();
            if (catPath != null) {
               XMLCatalog.this.log("Using catalogpath '" + XMLCatalog.this.getCatalogPath() + "'", 4);
               String[] catPathList = XMLCatalog.this.getCatalogPath().list();

               for(int i = 0; i < catPathList.length; ++i) {
                  File catFile = new File(catPathList[i]);
                  XMLCatalog.this.log("Parsing " + catFile, 4);

                  try {
                     this.parseCatalog.invoke(this.resolverImpl, catFile.getPath());
                  } catch (Exception var6) {
                     throw new BuildException(var6);
                  }
               }
            }
         }

         this.externalCatalogsProcessed = true;
      }
   }

   private class InternalResolver implements XMLCatalog.CatalogResolver {
      public InternalResolver() {
         XMLCatalog.this.log("Apache resolver library not found, internal resolver will be used", 3);
      }

      public InputSource resolveEntity(String publicId, String systemId) {
         InputSource result = null;
         ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
         if (matchingEntry != null) {
            XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
            result = XMLCatalog.this.filesystemLookup(matchingEntry);
            if (result == null) {
               result = XMLCatalog.this.classpathLookup(matchingEntry);
            }

            if (result == null) {
               result = XMLCatalog.this.urlLookup(matchingEntry);
            }
         }

         return result;
      }

      public Source resolve(String href, String base) throws TransformerException {
         SAXSource result = null;
         InputSource source = null;
         ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
         if (matchingEntry != null) {
            XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry.getPublicId() + "' location: '" + matchingEntry.getLocation() + "'", 4);
            ResourceLocation entryCopy = matchingEntry;
            if (base != null) {
               try {
                  URL baseURL = new URL(base);
                  entryCopy = new ResourceLocation();
                  entryCopy.setBase(baseURL);
               } catch (MalformedURLException var8) {
               }
            }

            entryCopy.setPublicId(matchingEntry.getPublicId());
            entryCopy.setLocation(matchingEntry.getLocation());
            source = XMLCatalog.this.filesystemLookup(entryCopy);
            if (source == null) {
               source = XMLCatalog.this.classpathLookup(entryCopy);
            }

            if (source == null) {
               source = XMLCatalog.this.urlLookup(entryCopy);
            }

            if (source != null) {
               result = new SAXSource(source);
            }
         }

         return result;
      }
   }

   private interface CatalogResolver extends URIResolver, EntityResolver {
      InputSource resolveEntity(String var1, String var2);

      Source resolve(String var1, String var2) throws TransformerException;
   }
}
