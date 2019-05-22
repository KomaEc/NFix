package org.apache.commons.httpclient;

public interface HttpConnectionManager {
   HttpConnection getConnection(HostConfiguration var1);

   HttpConnection getConnection(HostConfiguration var1, long var2) throws HttpException;

   void releaseConnection(HttpConnection var1);
}
