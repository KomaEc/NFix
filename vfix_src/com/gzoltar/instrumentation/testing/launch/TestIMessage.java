package com.gzoltar.instrumentation.testing.launch;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestIMessage extends Remote {
   ExecutionParameters getExecutionData() throws RemoteException;

   void setExecutionData(ExecutionParameters var1) throws RemoteException;

   TestResponse getResponse() throws RemoteException;

   void setResponse(TestResponse var1) throws RemoteException;

   String getTestClassName() throws RemoteException;

   void setTestClassName(String var1) throws RemoteException;

   String getTestMethodName() throws RemoteException;

   void setTestMethodName(String var1) throws RemoteException;
}
