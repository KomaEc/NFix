package com.gzoltar.instrumentation.testing.launch;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TestMessage extends UnicastRemoteObject implements TestIMessage {
   private static final long serialVersionUID = -4945586329145039973L;
   private ExecutionParameters executionData = null;
   private TestResponse testResponse = null;
   private String testClassName = null;
   private String testMethodName = null;

   public TestMessage() throws RemoteException {
   }

   public ExecutionParameters getExecutionData() throws RemoteException {
      return this.executionData;
   }

   public void setExecutionData(ExecutionParameters var1) throws RemoteException {
      this.executionData = var1;
   }

   public TestResponse getResponse() throws RemoteException {
      return this.testResponse;
   }

   public void setResponse(TestResponse var1) throws RemoteException {
      this.testResponse = var1;
   }

   public String getTestClassName() throws RemoteException {
      return this.testClassName;
   }

   public void setTestClassName(String var1) throws RemoteException {
      this.testClassName = var1;
   }

   public String getTestMethodName() throws RemoteException {
      return this.testMethodName;
   }

   public void setTestMethodName(String var1) throws RemoteException {
      this.testMethodName = var1;
   }
}
