package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.coverage.CoverageResult;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.util.CommunicationThread;
import java.net.ServerSocket;
import java.util.List;

public class CoverageCommunicationThread extends CommunicationThread {
   public CoverageCommunicationThread(ServerSocket socket, CoverageOptions arguments, List<String> tus, SideEffect1<CoverageResult> handler) {
      super(socket, new SendData(arguments, tus), new Receive(handler));
   }
}
