package com.gzoltar.shaded.org.pitest.classpath;

import com.gzoltar.shaded.org.pitest.functional.Option;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public interface ClassPathRoot {
   URL getResource(String var1) throws MalformedURLException;

   InputStream getData(String var1) throws IOException;

   Collection<String> classNames();

   Option<String> cacheLocation();
}
