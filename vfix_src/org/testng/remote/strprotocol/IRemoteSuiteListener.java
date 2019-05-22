package org.testng.remote.strprotocol;

public interface IRemoteSuiteListener {
   void onInitialization(GenericMessage var1);

   void onStart(SuiteMessage var1);

   void onFinish(SuiteMessage var1);
}
