package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class OtherClassLoaderClassPathRoot implements ClassPathRoot {
   private final ClassLoader loader;

   public OtherClassLoaderClassPathRoot(ClassLoader loader) {
      this.loader = loader;
   }

   public Collection<String> classNames() {
      throw new UnsupportedOperationException();
   }

   public InputStream getData(String name) throws IOException {
      return this.loader.getResourceAsStream(name.replace(".", "/") + ".class");
   }

   public URL getResource(String name) throws MalformedURLException {
      return this.loader.getResource(name);
   }

   public Option<String> cacheLocation() {
      return Option.none();
   }
}
