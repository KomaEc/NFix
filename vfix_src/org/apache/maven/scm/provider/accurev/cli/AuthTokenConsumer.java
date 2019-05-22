package org.apache.maven.scm.provider.accurev.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class AuthTokenConsumer implements StreamConsumer {
   private static final Pattern TOKEN_PATTERN = Pattern.compile("(?:Password:|)\\s*([0-9a-f]+).*");
   private String authToken = null;

   public String getAuthToken() {
      return this.authToken;
   }

   public void consumeLine(String line) {
      if (StringUtils.isBlank(this.authToken)) {
         Matcher matcher = TOKEN_PATTERN.matcher(line);
         if (matcher.matches()) {
            this.authToken = matcher.group(1);
         }
      }

   }
}
