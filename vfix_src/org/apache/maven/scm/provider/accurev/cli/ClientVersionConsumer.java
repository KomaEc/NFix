package org.apache.maven.scm.provider.accurev.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class ClientVersionConsumer implements StreamConsumer {
   private static final Pattern CLIENT_VERSION_PATTERN = Pattern.compile("^AccuRev ([0-9a-z.]+) .*");
   private String clientVersion;

   public void consumeLine(String line) {
      if (this.clientVersion == null) {
         Matcher matcher = CLIENT_VERSION_PATTERN.matcher(line);
         if (matcher.matches()) {
            this.clientVersion = matcher.group(1);
         }
      }

   }

   public String getClientVersion() {
      return this.clientVersion;
   }
}
