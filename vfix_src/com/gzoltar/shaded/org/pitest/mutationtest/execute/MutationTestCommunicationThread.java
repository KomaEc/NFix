package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.util.CommunicationThread;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.ReceiveStrategy;
import com.gzoltar.shaded.org.pitest.util.SafeDataInputStream;
import com.gzoltar.shaded.org.pitest.util.SafeDataOutputStream;
import java.net.ServerSocket;
import java.util.Map;
import java.util.logging.Logger;

public class MutationTestCommunicationThread extends CommunicationThread {
   private static final Logger LOG = Log.getLogger();
   private final Map<MutationIdentifier, MutationStatusTestPair> idMap;

   public MutationTestCommunicationThread(ServerSocket socket, SlaveArguments arguments, Map<MutationIdentifier, MutationStatusTestPair> idMap) {
      super(socket, new MutationTestCommunicationThread.SendData(arguments), new MutationTestCommunicationThread.Receive(idMap));
      this.idMap = idMap;
   }

   public MutationStatusTestPair getStatus(MutationIdentifier id) {
      return (MutationStatusTestPair)this.idMap.get(id);
   }

   private static class Receive implements ReceiveStrategy {
      private final Map<MutationIdentifier, MutationStatusTestPair> idMap;

      Receive(Map<MutationIdentifier, MutationStatusTestPair> idMap) {
         this.idMap = idMap;
      }

      public void apply(byte control, SafeDataInputStream is) {
         switch(control) {
         case 1:
            this.handleDescribe(is);
            break;
         case 2:
            this.handleReport(is);
         }

      }

      private void handleReport(SafeDataInputStream is) {
         MutationIdentifier mutation = (MutationIdentifier)is.read(MutationIdentifier.class);
         MutationStatusTestPair value = (MutationStatusTestPair)is.read(MutationStatusTestPair.class);
         this.idMap.put(mutation, value);
         MutationTestCommunicationThread.LOG.fine(mutation + " " + value);
      }

      private void handleDescribe(SafeDataInputStream is) {
         MutationIdentifier mutation = (MutationIdentifier)is.read(MutationIdentifier.class);
         this.idMap.put(mutation, new MutationStatusTestPair(1, DetectionStatus.STARTED));
      }
   }

   private static class SendData implements SideEffect1<SafeDataOutputStream> {
      private final SlaveArguments arguments;

      SendData(SlaveArguments arguments) {
         this.arguments = arguments;
      }

      public void apply(SafeDataOutputStream dos) {
         dos.write(this.arguments);
         dos.flush();
      }
   }
}
