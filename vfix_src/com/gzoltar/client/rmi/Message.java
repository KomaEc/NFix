package com.gzoltar.client.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class Message extends UnicastRemoteObject implements IMessage {
   private static final long serialVersionUID = -8644611894788764468L;
   private Map<String, String> properties = null;
   private String classPath = null;
   private Response response = null;
   private String agentPath = null;

   public Message() throws RemoteException {
   }

   public Response getResponse() throws RemoteException {
      return this.response;
   }

   public void setResponse(Response var1) throws RemoteException {
      this.response = var1;
   }

   public Map<String, String> getProperties() throws RemoteException {
      return this.properties;
   }

   public void setProperties(Map<String, String> var1) throws RemoteException {
      this.properties = var1;
   }

   public String getClassPath() throws RemoteException {
      return this.classPath;
   }

   public void setClassPath(String var1) throws RemoteException {
      this.classPath = var1;
   }

   public String getAgentPath() throws RemoteException {
      return this.agentPath;
   }

   public void setAgentPath(String var1) throws RemoteException {
      this.agentPath = var1;
   }
}
