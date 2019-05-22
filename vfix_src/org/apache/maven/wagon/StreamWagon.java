package org.apache.maven.wagon;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.apache.maven.wagon.resource.Resource;
import org.codehaus.plexus.util.IOUtil;

public abstract class StreamWagon extends AbstractWagon implements StreamingWagon {
   public abstract void fillInputData(InputData var1) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException;

   public abstract void fillOutputData(OutputData var1) throws TransferFailedException;

   public abstract void closeConnection() throws ConnectionException;

   public void get(String resourceName, File destination) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      this.getIfNewer(resourceName, destination, 0L);
   }

   protected void checkInputStream(InputStream is, Resource resource) throws TransferFailedException {
      if (is == null) {
         TransferFailedException e = new TransferFailedException(this.getRepository().getUrl() + " - Could not open input stream for resource: '" + resource + "'");
         this.fireTransferError(resource, e, 5);
         throw e;
      }
   }

   public boolean getIfNewer(String resourceName, File destination, long timestamp) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      boolean retValue = false;
      Resource resource = new Resource(resourceName);
      this.fireGetInitiated(resource, destination);
      resource.setLastModified(timestamp);
      InputStream is = this.getInputStream(resource);
      if (timestamp != 0L && timestamp >= resource.getLastModified()) {
         IOUtil.close(is);
      } else {
         retValue = true;
         this.checkInputStream(is, resource);
         this.getTransfer(resource, destination, is);
      }

      return retValue;
   }

   protected InputStream getInputStream(Resource resource) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      InputData inputData = new InputData();
      inputData.setResource(resource);

      try {
         this.fillInputData(inputData);
      } catch (TransferFailedException var9) {
         this.fireTransferError(resource, var9, 5);
         this.cleanupGetTransfer(resource);
         throw var9;
      } catch (ResourceDoesNotExistException var10) {
         this.fireTransferError(resource, var10, 5);
         this.cleanupGetTransfer(resource);
         throw var10;
      } catch (AuthorizationException var11) {
         this.fireTransferError(resource, var11, 5);
         this.cleanupGetTransfer(resource);
         throw var11;
      } finally {
         if (inputData.getInputStream() == null) {
            this.cleanupGetTransfer(resource);
         }

      }

      return inputData.getInputStream();
   }

   public void put(File source, String resourceName) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      Resource resource = new Resource(resourceName);
      this.firePutInitiated(resource, source);
      resource.setContentLength(source.length());
      resource.setLastModified(source.lastModified());
      OutputStream os = this.getOutputStream(resource);
      this.checkOutputStream(resource, os);
      this.putTransfer(resource, source, os, true);
   }

   protected void checkOutputStream(Resource resource, OutputStream os) throws TransferFailedException {
      if (os == null) {
         TransferFailedException e = new TransferFailedException(this.getRepository().getUrl() + " - Could not open output stream for resource: '" + resource + "'");
         this.fireTransferError(resource, e, 6);
         throw e;
      }
   }

   protected OutputStream getOutputStream(Resource resource) throws TransferFailedException {
      OutputData outputData = new OutputData();
      outputData.setResource(resource);

      try {
         this.fillOutputData(outputData);
      } catch (TransferFailedException var7) {
         this.fireTransferError(resource, var7, 6);
         throw var7;
      } finally {
         if (outputData.getOutputStream() == null) {
            this.cleanupPutTransfer(resource);
         }

      }

      return outputData.getOutputStream();
   }

   public boolean getIfNewerToStream(String resourceName, OutputStream stream, long timestamp) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException {
      boolean retValue = false;
      Resource resource = new Resource(resourceName);
      this.fireGetInitiated(resource, (File)null);
      InputStream is = this.getInputStream(resource);
      if (timestamp != 0L && timestamp >= resource.getLastModified()) {
         IOUtil.close(is);
      } else {
         retValue = true;
         this.checkInputStream(is, resource);
         this.fireGetStarted(resource, (File)null);
         this.getTransfer(resource, stream, is, true, Integer.MAX_VALUE);
         this.fireGetCompleted(resource, (File)null);
      }

      return retValue;
   }

   public void getToStream(String resourceName, OutputStream stream) throws ResourceDoesNotExistException, TransferFailedException, AuthorizationException {
      this.getIfNewerToStream(resourceName, stream, 0L);
   }

   public void putFromStream(InputStream stream, String destination) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      Resource resource = new Resource(destination);
      this.firePutInitiated(resource, (File)null);
      this.putFromStream(stream, resource);
   }

   public void putFromStream(InputStream stream, String destination, long contentLength, long lastModified) throws TransferFailedException, ResourceDoesNotExistException, AuthorizationException {
      Resource resource = new Resource(destination);
      this.firePutInitiated(resource, (File)null);
      resource.setContentLength(contentLength);
      resource.setLastModified(lastModified);
      this.putFromStream(stream, resource);
   }

   private void putFromStream(InputStream stream, Resource resource) throws TransferFailedException, AuthorizationException, ResourceDoesNotExistException {
      OutputStream os = this.getOutputStream(resource);
      this.checkOutputStream(resource, os);
      this.firePutStarted(resource, (File)null);
      this.putTransfer(resource, stream, os, true);
      this.firePutCompleted(resource, (File)null);
   }
}
