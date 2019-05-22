package org.apache.maven.doxia.parser;

import java.io.IOException;
import java.io.Reader;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.markup.XmlMarkup;
import org.apache.maven.doxia.sink.Sink;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public abstract class AbstractXmlParser extends AbstractParser implements XmlMarkup {
   public void parse(Reader source, Sink sink) throws ParseException {
      try {
         XmlPullParser parser = new MXParser();
         parser.setInput(source);
         this.parseXml(parser, sink);
      } catch (XmlPullParserException var4) {
         throw new ParseException("Error parsing the model: " + var4.getMessage(), var4);
      } catch (MacroExecutionException var5) {
         throw new ParseException("Macro execution failed: " + var5.getMessage(), var5);
      }
   }

   public final int getType() {
      return 2;
   }

   private void parseXml(XmlPullParser parser, Sink sink) throws XmlPullParserException, MacroExecutionException {
      int eventType = parser.getEventType();

      while(eventType != 1) {
         if (eventType == 2) {
            this.handleStartTag(parser, sink);
         } else if (eventType == 3) {
            this.handleEndTag(parser, sink);
         } else if (eventType == 4) {
            this.handleText(parser, sink);
         } else if (eventType != 5 && eventType != 9 && eventType == 6) {
         }

         try {
            eventType = parser.next();
         } catch (IOException var5) {
            throw new XmlPullParserException("IOException: " + var5.getMessage(), parser, var5);
         }
      }

   }

   protected abstract void handleStartTag(XmlPullParser var1, Sink var2) throws XmlPullParserException, MacroExecutionException;

   protected abstract void handleEndTag(XmlPullParser var1, Sink var2) throws XmlPullParserException, MacroExecutionException;

   protected abstract void handleText(XmlPullParser var1, Sink var2) throws XmlPullParserException;
}
