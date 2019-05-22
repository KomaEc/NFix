package org.jboss.net.ssl;

import javax.net.ssl.SSLSocketFactory;

public interface SSLSocketFactoryBuilder {
   SSLSocketFactory getSocketFactory() throws Exception;
}
