package org.apache.maven.wagon;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.maven.wagon.authorization.AuthorizationException;

public interface StreamingWagon extends Wagon {
   void getToStream(String var1, OutputStream var2) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException;

   boolean getIfNewerToStream(String var1, OutputStream var2, long var3) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException;

   void putFromStream(InputStream var1, String var2) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   void putFromStream(InputStream var1, String var2, long var3, long var5) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;
}
