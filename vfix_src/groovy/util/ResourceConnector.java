package groovy.util;

import java.net.URLConnection;

public interface ResourceConnector {
   URLConnection getResourceConnection(String var1) throws ResourceException;
}
