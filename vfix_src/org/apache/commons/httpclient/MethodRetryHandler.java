package org.apache.commons.httpclient;

public interface MethodRetryHandler {
   boolean retryMethod(HttpMethod var1, HttpConnection var2, HttpRecoverableException var3, int var4, boolean var5);
}
