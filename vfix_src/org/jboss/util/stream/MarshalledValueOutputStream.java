package org.jboss.util.stream;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteStub;

public class MarshalledValueOutputStream extends ObjectOutputStream {
   public MarshalledValueOutputStream(OutputStream os) throws IOException {
      super(os);
      this.enableReplaceObject(true);
   }

   protected void annotateClass(Class<?> cl) throws IOException {
      super.annotateClass(cl);
   }

   protected void annotateProxyClass(Class<?> cl) throws IOException {
      super.annotateProxyClass(cl);
   }

   protected Object replaceObject(Object obj) throws IOException {
      if (obj instanceof Remote && !(obj instanceof RemoteStub)) {
         Remote remote = (Remote)obj;

         try {
            obj = RemoteObject.toStub(remote);
         } catch (IOException var4) {
         }
      }

      return obj;
   }
}
