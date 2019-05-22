package org.apache.maven.scm.provider.accurev.cli;

import org.apache.maven.scm.provider.accurev.AccuRevInfo;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class InfoConsumer implements StreamConsumer {
   private AccuRevInfo info;

   public InfoConsumer(AccuRevInfo info) {
      this.info = info;
   }

   public void consumeLine(String line) {
      String[] tokens = line.split("\\s*:\\s*", 2);
      if (tokens[0].equals("Principal")) {
         this.info.setUser(tokens[1]);
      } else if (tokens[0].equals("Basis")) {
         this.info.setBasis(tokens[1]);
      } else if (tokens[0].startsWith("Workspace")) {
         this.info.setWorkSpace(tokens[1]);
      } else if (tokens[0].equals("Top")) {
         this.info.setTop(tokens[1]);
      } else if (tokens[0].equals("Server name")) {
         this.info.setServer(tokens[1]);
      } else if (tokens[0].equals("Port")) {
         this.info.setPort(Integer.parseInt(tokens[1]));
      }

   }
}
