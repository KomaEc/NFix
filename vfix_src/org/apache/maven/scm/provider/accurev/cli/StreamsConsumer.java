package org.apache.maven.scm.provider.accurev.cli;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.accurev.Stream;

public class StreamsConsumer extends XppStreamConsumer {
   private List<Stream> streams;

   public StreamsConsumer(ScmLogger logger, List<Stream> streams) {
      super(logger);
      this.streams = streams;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      String tagName = getTagName(tagPath);
      if ("stream".equals(tagName)) {
         String name = (String)attributes.get("name");
         long streamId = Long.parseLong((String)attributes.get("streamNumber"));
         String basis = (String)attributes.get("basis");
         String basisStreamNumber = (String)attributes.get("basisStreamNumber");
         long basisStreamId = basisStreamNumber == null ? 0L : Long.parseLong(basisStreamNumber);
         String depot = (String)attributes.get("depotName");
         Date startTime = new Date(Long.parseLong((String)attributes.get("startTime")) * 1000L);
         String streamType = (String)attributes.get("type");
         this.streams.add(new Stream(name, streamId, basis, basisStreamId, depot, startTime, streamType));
      }

   }
}
