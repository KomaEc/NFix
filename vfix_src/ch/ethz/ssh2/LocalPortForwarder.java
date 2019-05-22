package ch.ethz.ssh2;

import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.channel.LocalAcceptThread;
import java.io.IOException;

public class LocalPortForwarder {
   ChannelManager cm;
   int local_port;
   String host_to_connect;
   int port_to_connect;
   LocalAcceptThread lat;

   LocalPortForwarder(ChannelManager cm, int local_port, String host_to_connect, int port_to_connect) throws IOException {
      this.cm = cm;
      this.local_port = local_port;
      this.host_to_connect = host_to_connect;
      this.port_to_connect = port_to_connect;
      this.lat = new LocalAcceptThread(cm, local_port, host_to_connect, port_to_connect);
      this.lat.setDaemon(true);
      this.lat.start();
   }

   public void close() throws IOException {
      this.lat.stopWorking();
   }
}
