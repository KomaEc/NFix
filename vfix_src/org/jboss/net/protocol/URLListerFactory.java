package org.jboss.net.protocol;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class URLListerFactory {
   private static HashMap defaultClasses = new HashMap();
   private HashMap classes;

   public URLListerFactory() {
      this.classes = (HashMap)defaultClasses.clone();
   }

   public URLLister createURLLister(URL url) throws MalformedURLException {
      return this.createURLLister(url.getProtocol());
   }

   public URLLister createURLLister(String protocol) throws MalformedURLException {
      try {
         String className = (String)this.classes.get(protocol);
         if (className == null) {
            throw new MalformedURLException("No lister class defined for protocol " + protocol);
         } else {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            return (URLLister)clazz.newInstance();
         }
      } catch (ClassNotFoundException var4) {
         throw new MalformedURLException(var4.getMessage());
      } catch (InstantiationException var5) {
         throw new MalformedURLException(var5.getMessage());
      } catch (IllegalAccessException var6) {
         throw new MalformedURLException(var6.getMessage());
      }
   }

   public void registerListener(String protocol, String className) {
      this.classes.put(protocol, className);
   }

   static {
      defaultClasses.put("file", "org.jboss.net.protocol.file.FileURLLister");
      defaultClasses.put("http", "org.jboss.net.protocol.http.DavURLLister");
      defaultClasses.put("https", "org.jboss.net.protocol.http.DavURLLister");
   }
}
