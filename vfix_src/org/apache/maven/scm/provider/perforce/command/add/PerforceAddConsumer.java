package org.apache.maven.scm.provider.perforce.command.add;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class PerforceAddConsumer implements StreamConsumer {
   private static final Pattern PATTERN = Pattern.compile("^([^#]+)#(\\d+) - (.*)");
   private static final String FILE_BEGIN_TOKEN = "//";
   private List<ScmFile> additions = new ArrayList();

   public List<ScmFile> getAdditions() {
      return this.additions;
   }

   public void consumeLine(String line) {
      if (!line.startsWith("... ")) {
         if (!line.startsWith("//")) {
            throw new IllegalStateException("Unknown error: " + line);
         } else {
            Matcher matcher = PATTERN.matcher(line);
            if (!matcher.find()) {
               throw new IllegalStateException("Unknown input: " + line);
            } else {
               this.additions.add(new ScmFile(matcher.group(1), ScmFileStatus.ADDED));
            }
         }
      }
   }
}
