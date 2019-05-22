package org.apache.maven.doxia.macro.toc;

import java.io.StringReader;
import java.util.Iterator;
import org.apache.maven.doxia.index.IndexEntry;
import org.apache.maven.doxia.index.IndexingSink;
import org.apache.maven.doxia.macro.AbstractMacro;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;

public class TocMacro extends AbstractMacro {
   private int section;
   private int fromDepth;
   private int toDepth;
   private static final int DEFAULT_DEPTH = 5;

   public void execute(Sink sink, MacroRequest request) throws MacroExecutionException {
      String source = (String)request.getParameter("sourceContent");
      Parser parser = (Parser)request.getParameter("parser");
      this.section = getInt(request, "section", 0);
      if (this.section != 0) {
         this.fromDepth = getInt(request, "fromDepth", 0);
         this.toDepth = getInt(request, "toDepth", 5);
      } else {
         this.fromDepth = 0;
         this.toDepth = 5;
      }

      IndexEntry index = new IndexEntry("index");
      IndexingSink tocSink = new IndexingSink(index);

      try {
         parser.parse(new StringReader(source), tocSink);
         if (index.getChildEntries().size() > 0) {
            if (this.fromDepth < this.section || this.section == 0) {
               sink.list();
            }

            int i = 1;

            for(Iterator it = index.getChildEntries().iterator(); it.hasNext(); ++i) {
               IndexEntry sectionIndex = (IndexEntry)it.next();
               if (i == this.section || this.section == 0) {
                  this.writeSubSectionN(sink, sectionIndex, i);
               }
            }

            if (this.fromDepth < this.section || this.section == 0) {
               sink.list_();
            }
         }

      } catch (ParseException var10) {
         throw new MacroExecutionException("ParseException: " + var10.getMessage(), var10);
      }
   }

   private void writeSubSectionN(Sink sink, IndexEntry sectionIndex, int n) {
      if (this.fromDepth < n) {
         sink.listItem();
         sink.link("#" + HtmlTools.encodeId(sectionIndex.getId()));
         sink.text(sectionIndex.getTitle());
         sink.link_();
      }

      if (this.toDepth >= n && sectionIndex.getChildEntries().size() > 0) {
         if (this.fromDepth < n + 1) {
            sink.list();
         }

         Iterator it = sectionIndex.getChildEntries().iterator();

         while(it.hasNext()) {
            IndexEntry subsectionIndex = (IndexEntry)it.next();
            if (n == 5) {
               sink.listItem();
               sink.link("#" + HtmlTools.encodeId(subsectionIndex.getId()));
               sink.text(subsectionIndex.getTitle());
               sink.link_();
               sink.listItem_();
            } else {
               this.writeSubSectionN(sink, subsectionIndex, n + 1);
            }
         }

         if (this.fromDepth < n + 1) {
            sink.list_();
         }
      }

      if (this.fromDepth < n) {
         sink.listItem_();
      }

   }

   private static int getInt(MacroRequest request, String parameter, int defaultValue) throws MacroExecutionException {
      String value = (String)request.getParameter(parameter);
      if (StringUtils.isEmpty(value)) {
         return defaultValue;
      } else {
         int i;
         try {
            i = Integer.parseInt(value);
         } catch (NumberFormatException var6) {
            return defaultValue;
         }

         if (i < 0) {
            throw new MacroExecutionException("The " + parameter + "=" + i + " should be positive.");
         } else {
            return i;
         }
      }
   }
}
