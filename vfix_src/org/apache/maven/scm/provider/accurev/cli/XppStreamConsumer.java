package org.apache.maven.scm.provider.accurev.cli;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public abstract class XppStreamConsumer extends Thread implements StreamConsumer {
   private Writer writer;
   private XmlPullParser parser = new MXParser();
   private volatile boolean complete = false;
   private ScmLogger logger;
   private int lineCount = 0;
   private Reader reader;

   public ScmLogger getLogger() {
      return this.logger;
   }

   public XppStreamConsumer(ScmLogger logger) {
      this.logger = logger;

      try {
         Pipe p = Pipe.open();
         SinkChannel sink = p.sink();
         SourceChannel source = p.source();
         this.writer = Channels.newWriter(sink, Charset.defaultCharset().name());
         this.reader = Channels.newReader(source, Charset.defaultCharset().name());
         this.parser.setInput(this.reader);
      } catch (Exception var5) {
         logger.error("Exception initialising pipe", var5);
      }

   }

   public final void consumeLine(String line) {
      try {
         this.writer.append(line);
         if (this.lineCount == 0) {
            this.start();
         }

         ++this.lineCount;
         this.writer.flush();
      } catch (IOException var3) {
         throw new RuntimeException("error pumping line to pipe", var3);
      }
   }

   public void run() {
      boolean var17 = false;

      label117: {
         try {
            var17 = true;
            this.parse(this.parser);
            var17 = false;
            break label117;
         } catch (Exception var24) {
            this.caughtParseException(var24);
            var17 = false;
         } finally {
            if (var17) {
               synchronized(this) {
                  try {
                     this.reader.close();
                  } catch (IOException var18) {
                     this.getLogger().warn("Error closing pipe reader", var18);
                  }

                  this.complete = true;
                  this.notifyAll();
               }
            }
         }

         synchronized(this) {
            try {
               this.reader.close();
            } catch (IOException var20) {
               this.getLogger().warn("Error closing pipe reader", var20);
            }

            this.complete = true;
            this.notifyAll();
            return;
         }
      }

      synchronized(this) {
         try {
            this.reader.close();
         } catch (IOException var22) {
            this.getLogger().warn("Error closing pipe reader", var22);
         }

         this.complete = true;
         this.notifyAll();
      }

   }

   protected void caughtParseException(Exception e) {
      this.logger.warn("Exception parsing input", e);
   }

   protected void parse(XmlPullParser p) throws XmlPullParserException, IOException {
      List<String> tagPath = new ArrayList();
      int eventType = p.getEventType();
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Event " + eventType);
      }

      while(eventType != 1) {
         int lastIndex = tagPath.size() - 1;
         String tagName;
         switch(eventType) {
         case 0:
            break;
         case 1:
         default:
            this.logger.warn("Unexpected event type " + eventType);
            break;
         case 2:
            tagName = p.getName();
            if (tagName == null) {
               break;
            }

            tagPath.add(tagName);
            int attributeCount = p.getAttributeCount();
            Map<String, String> attributes = new HashMap(Math.max(attributeCount, 0));

            for(int i = 0; i < attributeCount; ++i) {
               attributes.put(p.getAttributeName(i), p.getAttributeValue(i));
            }

            this.startTag(tagPath, attributes);
            break;
         case 3:
            tagName = p.getName();
            if (lastIndex < 0 || !tagName.equals(tagPath.get(lastIndex))) {
               this.logger.warn("Bad tag path: " + Arrays.toString(tagPath.toArray()));
            }

            this.endTag(tagPath);
            tagPath.remove(lastIndex);
            break;
         case 4:
            if (!p.isWhitespace()) {
               String text = p.getText();
               this.text(tagPath, text);
            }
         }

         p.next();
         eventType = p.getEventType();
         if (this.logger.isDebugEnabled()) {
            this.logger.debug("Event " + eventType);
         }
      }

   }

   public void waitComplete() {
      Thread.yield();

      try {
         this.writer.close();
      } catch (IOException var6) {
         this.logger.warn("Exception flushing output", var6);
      }

      while(!this.isComplete()) {
         synchronized(this) {
            try {
               if (!this.isComplete()) {
                  this.wait(1000L);
               }
            } catch (Exception var4) {
               this.logger.warn((Throwable)var4);
            }
         }
      }

   }

   private boolean isComplete() {
      return this.complete || this.lineCount == 0;
   }

   protected void startTag(List<String> tagPath, Map<String, String> attributes) {
      if (this.logger.isDebugEnabled()) {
         String tagName = getTagName(tagPath);
         this.logger.debug("START_TAG: " + tagName + "(" + attributes.size() + ")");
      }

   }

   protected static String getTagName(List<String> tagPath) {
      return tagPath.size() == 0 ? null : (String)tagPath.get(tagPath.size() - 1);
   }

   protected void endTag(List<String> tagPath) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("END_TAG: " + getTagName(tagPath));
      }

   }

   protected void text(List<String> tagPath, String text) {
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("TEXT: " + text);
      }

   }
}
