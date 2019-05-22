package org.apache.commons.httpclient.auth;

import org.apache.commons.httpclient.Credentials;

public interface AuthScheme {
   String getSchemeName();

   String getParameter(String var1);

   String getRealm();

   String getID();

   String authenticate(Credentials var1, String var2, String var3) throws AuthenticationException;
}
