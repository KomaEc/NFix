package com.gzoltar.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IMessage extends Remote {
   Response getResponse() throws RemoteException;

   void setResponse(Response var1) throws RemoteException;

   Map<String, String> getProperties() throws RemoteException;

   void setProperties(Map<String, String> var1) throws RemoteException;

   String getClassPath() throws RemoteException;

   void setClassPath(String var1) throws RemoteException;

   String getAgentPath() throws RemoteException;

   void setAgentPath(String var1) throws RemoteException;
}
