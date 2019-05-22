package groovy.lang;

import java.net.MalformedURLException;
import java.net.URL;

public interface GroovyResourceLoader {
   URL loadGroovySource(String var1) throws MalformedURLException;
}
