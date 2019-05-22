package org.jboss.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jboss.logging.Logger;
import org.jboss.util.NotImplementedException;
import org.jboss.util.StringPropertyReplacer;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JBossEntityResolver implements EntityResolver, LSResourceResolver {
   private static final Logger log = Logger.getLogger(JBossEntityResolver.class);
   private static Map entities = new ConcurrentHashMap();
   private static boolean warnOnNonFileURLs;
   private boolean replaceSystemProperties = true;
   private Map localEntities;
   private ThreadLocal<Boolean> entityResolved = new ThreadLocal();

   public static Map getEntityMap() {
      return Collections.unmodifiableMap(entities);
   }

   public static boolean isWarnOnNonFileURLs() {
      return warnOnNonFileURLs;
   }

   public static void setWarnOnNonFileURLs(boolean warnOnNonFileURLs) {
      JBossEntityResolver.warnOnNonFileURLs = warnOnNonFileURLs;
   }

   public static void registerEntity(String id, String dtdFileName) {
      entities.put(id, dtdFileName);
   }

   public boolean isReplaceSystemProperties() {
      return this.replaceSystemProperties;
   }

   public void setReplaceSystemProperties(boolean replaceSystemProperties) {
      this.replaceSystemProperties = replaceSystemProperties;
   }

   public synchronized void registerLocalEntity(String id, String dtdOrSchema) {
      if (this.localEntities == null) {
         this.localEntities = new ConcurrentHashMap();
      }

      this.localEntities.put(id, dtdOrSchema);
   }

   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
      this.entityResolved.set(Boolean.FALSE);
      if (publicId == null && systemId == null) {
         return null;
      } else {
         boolean trace = log.isTraceEnabled();
         boolean resolvePublicIdFirst = true;
         if (publicId != null && systemId != null) {
            String registeredSystemId = null;
            if (this.localEntities != null) {
               registeredSystemId = (String)this.localEntities.get(publicId);
            }

            if (registeredSystemId == null) {
               registeredSystemId = (String)entities.get(publicId);
            }

            if (registeredSystemId != null && !registeredSystemId.equals(systemId)) {
               resolvePublicIdFirst = false;
               if (trace) {
                  log.trace("systemId argument '" + systemId + "' for publicId '" + publicId + "' is different from the registered systemId '" + registeredSystemId + "', resolution will be based on the argument");
               }
            }
         }

         InputSource inputSource = null;
         if (resolvePublicIdFirst) {
            inputSource = this.resolvePublicID(publicId, trace);
         }

         if (inputSource == null) {
            inputSource = this.resolveSystemID(systemId, trace);
         }

         if (inputSource == null) {
            inputSource = this.resolveClasspathName(systemId, trace);
         }

         if (inputSource == null) {
            inputSource = this.resolveSystemIDasURL(systemId, trace);
         }

         this.entityResolved.set(new Boolean(inputSource != null));
         if (inputSource == null) {
            log.debug("Cannot resolve [publicID=" + publicId + ",systemID=" + systemId + "]");
         }

         return inputSource;
      }
   }

   public boolean isEntityResolved() {
      Boolean value = (Boolean)this.entityResolved.get();
      return value != null ? value : false;
   }

   protected InputSource resolvePublicID(String publicId, boolean trace) {
      if (publicId == null) {
         return null;
      } else {
         if (trace) {
            log.trace("resolvePublicID, publicId=" + publicId);
         }

         InputSource inputSource = null;
         String filename = null;
         if (this.localEntities != null) {
            filename = (String)this.localEntities.get(publicId);
         }

         if (filename == null) {
            filename = (String)entities.get(publicId);
         }

         if (filename != null) {
            if (trace) {
               log.trace("Found entity from publicId=" + publicId + " fileName=" + filename);
            }

            InputStream ins = this.loadClasspathResource(filename, trace);
            if (ins != null) {
               inputSource = new InputSource(ins);
               inputSource.setPublicId(publicId);
            } else {
               log.trace("Cannot load publicId from classpath resource: " + filename);
               inputSource = this.resolveSystemIDasURL(filename, trace);
               if (inputSource == null) {
                  log.warn("Cannot load publicId from resource: " + filename);
               }
            }
         }

         return inputSource;
      }
   }

   protected InputSource resolveSystemID(String systemId, boolean trace) {
      if (systemId == null) {
         return null;
      } else {
         if (trace) {
            log.trace("resolveSystemID, systemId=" + systemId);
         }

         InputSource inputSource = null;
         String filename = null;
         if (this.localEntities != null) {
            filename = (String)this.localEntities.get(systemId);
         }

         if (filename == null) {
            filename = (String)entities.get(systemId);
         }

         if (filename != null) {
            if (trace) {
               log.trace("Found entity systemId=" + systemId + " fileName=" + filename);
            }

            InputStream ins = this.loadClasspathResource(filename, trace);
            if (ins != null) {
               inputSource = new InputSource(ins);
               inputSource.setSystemId(systemId);
            } else {
               log.warn("Cannot load systemId from resource: " + filename);
            }
         }

         return inputSource;
      }
   }

   protected InputSource resolveSystemIDasURL(String systemId, boolean trace) {
      if (systemId == null) {
         return null;
      } else {
         if (trace) {
            log.trace("resolveSystemIDasURL, systemId=" + systemId);
         }

         InputSource inputSource = null;

         try {
            if (trace) {
               log.trace("Trying to resolve systemId as a URL");
            }

            if (this.isReplaceSystemProperties()) {
               systemId = StringPropertyReplacer.replaceProperties(systemId);
            }

            URL url = new URL(systemId);
            if (warnOnNonFileURLs && !url.getProtocol().equalsIgnoreCase("file") && !url.getProtocol().equalsIgnoreCase("vfszip")) {
               log.warn("Trying to resolve systemId as a non-file URL: " + systemId);
            }

            InputStream ins = url.openStream();
            if (ins != null) {
               inputSource = new InputSource(ins);
               inputSource.setSystemId(systemId);
            } else {
               log.warn("Cannot load systemId as URL: " + systemId);
            }

            if (trace) {
               log.trace("Resolved systemId as a URL");
            }
         } catch (MalformedURLException var6) {
            if (trace) {
               log.trace("SystemId is not a url: " + systemId, var6);
            }
         } catch (IOException var7) {
            if (trace) {
               log.trace("Failed to obtain URL.InputStream from systemId: " + systemId, var7);
            }
         }

         return inputSource;
      }
   }

   protected InputSource resolveClasspathName(String systemId, boolean trace) {
      if (systemId == null) {
         return null;
      } else {
         if (trace) {
            log.trace("resolveClasspathName, systemId=" + systemId);
         }

         String filename = systemId;

         try {
            URI url = new URI(systemId);
            String path = url.getPath();
            if (path == null) {
               path = url.getSchemeSpecificPart();
            }

            int slash = path.lastIndexOf(47);
            if (slash >= 0) {
               filename = path.substring(slash + 1);
            } else {
               filename = path;
            }

            if (filename.length() == 0) {
               return null;
            }

            if (trace) {
               log.trace("Mapped systemId to filename: " + filename);
            }
         } catch (URISyntaxException var7) {
            if (trace) {
               log.trace("systemId: is not a URI, using systemId as resource", var7);
            }
         }

         InputStream is = this.loadClasspathResource(filename, trace);
         InputSource inputSource = null;
         if (is != null) {
            inputSource = new InputSource(is);
            inputSource.setSystemId(systemId);
         }

         return inputSource;
      }
   }

   protected InputStream loadClasspathResource(String resource, boolean trace) {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      URL url = loader.getResource(resource);
      if (url == null) {
         if (resource.endsWith(".dtd")) {
            resource = "dtd/" + resource;
         } else if (resource.endsWith(".xsd")) {
            resource = "schema/" + resource;
         }

         url = loader.getResource(resource);
      }

      InputStream inputStream = null;
      if (url != null) {
         if (trace) {
            log.trace(resource + " maps to URL: " + url);
         }

         try {
            inputStream = url.openStream();
         } catch (IOException var7) {
            log.debug("Failed to open url stream", var7);
         }
      }

      return inputStream;
   }

   public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
      InputSource inputSource = null;

      try {
         inputSource = this.resolveEntity(publicId, systemId);
      } catch (Exception var8) {
         log.debug("Failed to resolve resource", var8);
      }

      LSInput result = null;
      if (inputSource != null) {
         result = new JBossEntityResolver.LSInputImpl(publicId, systemId, baseURI, inputSource);
      }

      return result;
   }

   static {
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            JBossEntityResolver.warnOnNonFileURLs = new Boolean(System.getProperty("org.jboss.resolver.warning", "false"));
            return null;
         }
      });
      registerEntity("-//W3C//DTD/XMLSCHEMA 200102//EN", "XMLSchema.dtd");
      registerEntity("http://www.w3.org/2001/XMLSchema.dtd", "XMLSchema.dtd");
      registerEntity("datatypes", "datatypes.dtd");
      registerEntity("http://www.w3.org/XML/1998/namespace", "xml.xsd");
      registerEntity("http://www.w3.org/2001/xml.xsd", "xml.xsd");
      registerEntity("http://www.w3.org/2005/05/xmlmime", "xml-media-types.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/j2ee_1_4.xsd", "j2ee_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/javaee_5.xsd", "javaee_5.xsd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-common_5_1.xsd", "jboss-common_5_1.xsd");
      registerEntity("http://schemas.xmlsoap.org/soap/encoding/", "soap-encoding_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_client_1_1.xsd", "j2ee_web_services_client_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_web_services_1_1.xsd", "j2ee_web_services_1_1.xsd");
      registerEntity("http://www.ibm.com/webservices/xsd/j2ee_jaxrpc_mapping_1_1.xsd", "j2ee_jaxrpc_mapping_1_1.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/javaee_web_services_client_1_2.xsd", "javaee_web_services_client_1_2.xsd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "ejb-jar_1_1.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "ejb-jar_2_0.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd", "ejb-jar_2_1.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd", "ejb-jar_3_0.xsd");
      registerEntity("-//JBoss//DTD JBOSS//EN", "jboss.dtd");
      registerEntity("-//JBoss//DTD JBOSS 2.4//EN", "jboss_2_4.dtd");
      registerEntity("-//JBoss//DTD JBOSS 3.0//EN", "jboss_3_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 3.2//EN", "jboss_3_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS 4.0//EN", "jboss_4_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 4.2//EN", "jboss_4_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS 5.0//EN", "jboss_5_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS 5.1.EAP//EN", "jboss_5_1_eap.dtd");
      registerEntity("-//JBoss//DTD JBOSS 6.0//EN", "jboss_6_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.0//EN", "jbosscmp-jdbc_3_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 3.2//EN", "jbosscmp-jdbc_3_2.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.0//EN", "jbosscmp-jdbc_4_0.dtd");
      registerEntity("-//JBoss//DTD JBOSSCMP-JDBC 4.2//EN", "jbosscmp-jdbc_4_2.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss_5_0.xsd", "jboss_5_0.xsd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss_5_1.xsd", "jboss_5_1.xsd");
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.2//EN", "application_1_2.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN", "application_1_3.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD J2EE Application Client 1.3//EN", "application-client_1_3.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/application_1_4.xsd", "application_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/application_5.xsd", "application_5.xsd");
      registerEntity("-//JBoss//DTD J2EE Application 1.3//EN", "jboss-app_3_0.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 1.3V2//EN", "jboss-app_3_2.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 1.4//EN", "jboss-app_4_0.dtd");
      registerEntity("-//JBoss//DTD J2EE Application 4.2//EN", "jboss-app_4_2.dtd");
      registerEntity("-//JBoss//DTD Java EE Application 5.0//EN", "jboss-app_5_0.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Connector 1.0//EN", "connector_1_0.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/connector_1_5.xsd", "connector_1_5.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/connector_1_6.xsd", "connector_1_6.xsd");
      registerEntity("-//JBoss//DTD JBOSS JCA Config 1.0//EN", "jboss-ds_1_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS JCA Config 1.5//EN", "jboss-ds_1_5.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-ds_5_0.xsd", "jboss-ds_5_0.xsd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-ra_1_0.xsd", "jboss-ra_1_0.xsd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", "web-app_2_2.dtd");
      registerEntity("-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN", "web-app_2_3.dtd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd", "web-app_2_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/web-app_2_5.xsd", "web-app_2_5.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd", "web-app_3_0.xsd");
      registerEntity("-//JBoss//DTD Web Application 2.2//EN", "jboss-web.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.3//EN", "jboss-web_3_0.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.3V2//EN", "jboss-web_3_2.dtd");
      registerEntity("-//JBoss//DTD Web Application 2.4//EN", "jboss-web_4_0.dtd");
      registerEntity("-//JBoss//DTD Web Application 4.2//EN", "jboss-web_4_2.dtd");
      registerEntity("-//JBoss//DTD Web Application 5.0//EN", "jboss-web_5_0.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-web_5_1.xsd", "jboss-web_5_1.xsd");
      registerEntity("http://java.sun.com/xml/ns/j2ee/application-client_1_4.xsd", "application-client_1_4.xsd");
      registerEntity("http://java.sun.com/xml/ns/javaee/application-client_5.xsd", "application-client_5.xsd");
      registerEntity("-//JBoss//DTD Application Client 3.2//EN", "jboss-client_3_2.dtd");
      registerEntity("-//JBoss//DTD Application Client 4.0//EN", "jboss-client_4_0.dtd");
      registerEntity("-//JBoss//DTD Application Client 4.2//EN", "jboss-client_4_2.dtd");
      registerEntity("-//JBoss//DTD Application Client 5.0//EN", "jboss-client_5_0.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/jboss-client_5_1.xsd", "jboss-client_5_1.xsd");
      registerEntity("-//JBoss//DTD Web Service Reference 4.0//EN", "service-ref_4_0.dtd");
      registerEntity("-//JBoss//DTD Web Service Reference 4.2//EN", "service-ref_4_2.dtd");
      registerEntity("-//JBoss//DTD Web Service Reference 5.0//EN", "service-ref_5_0.dtd");
      registerEntity("-//JBoss//DTD MBean Service 3.2//EN", "jboss-service_3_2.dtd");
      registerEntity("-//JBoss//DTD MBean Service 4.0//EN", "jboss-service_4_0.dtd");
      registerEntity("-//JBoss//DTD MBean Service 4.2//EN", "jboss-service_4_2.dtd");
      registerEntity("-//JBoss//DTD MBean Service 5.0//EN", "jboss-service_5_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.0//EN", "jboss_xmbean_1_0.dtd");
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.1//EN", "jboss_xmbean_1_1.dtd");
      registerEntity("-//JBoss//DTD JBOSS XMBEAN 1.2//EN", "jboss_xmbean_1_2.dtd");
      registerEntity("-//JBoss//DTD JBOSS Security Config 3.0//EN", "security_config.dtd");
      registerEntity("http://www.jboss.org/j2ee/schema/security-config_4_0.xsd", "security-config_4_0.xsd");
      registerEntity("urn:jboss:aop-deployer", "aop-deployer_1_1.xsd");
      registerEntity("urn:jboss:aop-beans:1.0", "aop-beans_1_0.xsd");
      registerEntity("urn:jboss:bean-deployer", "bean-deployer_1_0.xsd");
      registerEntity("urn:jboss:bean-deployer:2.0", "bean-deployer_2_0.xsd");
      registerEntity("urn:jboss:javabean:1.0", "javabean_1_0.xsd");
      registerEntity("urn:jboss:javabean:2.0", "javabean_2_0.xsd");
      registerEntity("urn:jboss:spring-beans:2.0", "mc-spring-beans_2_0.xsd");
      registerEntity("urn:jboss:policy:1.0", "policy_1_0.xsd");
      registerEntity("urn:jboss:osgi-beans:1.0", "osgi-beans_1_0.xsd");
      registerEntity("urn:jboss:seam-components:1.0", "seam-components_1_0.xsd");
      registerEntity("urn:jboss:security-config:4.1", "security-config_4_1.xsd");
      registerEntity("urn:jboss:security-config:5.0", "security-config_5_0.xsd");
      registerEntity("urn:jboss:jndi-binding-service:1.0", "jndi-binding-service_1_0.xsd");
      registerEntity("urn:jboss:user-roles:1.0", "user-roles_1_0.xsd");
   }

   private static class LSInputImpl implements LSInput {
      private final String systemID;
      private final String publicID;
      private final String baseURI;
      private final InputSource inputSource;

      public LSInputImpl(String publicID, String systemID, String baseURI, InputSource inputSource) {
         this.inputSource = inputSource;
         this.systemID = systemID;
         this.publicID = publicID;
         this.baseURI = baseURI;
      }

      public Reader getCharacterStream() {
         return this.inputSource.getCharacterStream();
      }

      public void setCharacterStream(Reader characterStream) {
         throw new NotImplementedException();
      }

      public InputStream getByteStream() {
         return this.inputSource.getByteStream();
      }

      public void setByteStream(InputStream byteStream) {
         throw new NotImplementedException();
      }

      public String getStringData() {
         return null;
      }

      public void setStringData(String stringData) {
         throw new NotImplementedException();
      }

      public String getSystemId() {
         return this.systemID;
      }

      public void setSystemId(String systemId) {
         throw new NotImplementedException();
      }

      public String getPublicId() {
         return this.publicID;
      }

      public void setPublicId(String publicId) {
         throw new NotImplementedException();
      }

      public String getBaseURI() {
         return this.baseURI;
      }

      public void setBaseURI(String baseURI) {
         throw new NotImplementedException();
      }

      public String getEncoding() {
         return null;
      }

      public void setEncoding(String encoding) {
         throw new NotImplementedException();
      }

      public boolean getCertifiedText() {
         return false;
      }

      public void setCertifiedText(boolean certifiedText) {
         throw new NotImplementedException();
      }
   }
}
