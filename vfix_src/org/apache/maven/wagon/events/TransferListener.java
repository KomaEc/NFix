package org.apache.maven.wagon.events;

public interface TransferListener {
   void transferInitiated(TransferEvent var1);

   void transferStarted(TransferEvent var1);

   void transferProgress(TransferEvent var1, byte[] var2, int var3);

   void transferCompleted(TransferEvent var1);

   void transferError(TransferEvent var1);

   void debug(String var1);
}
