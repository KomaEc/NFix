package org.apache.maven.doxia.module.fml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import org.apache.maven.doxia.module.fml.model.Faq;
import org.apache.maven.doxia.module.fml.model.Faqs;
import org.apache.maven.doxia.module.fml.model.Part;
import org.apache.maven.doxia.parser.ParseException;
import org.apache.maven.doxia.parser.Parser;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class FmlParser implements Parser {
   public void parse(Reader reader, Sink sink) throws ParseException {
      Faqs faqs;
      try {
         XmlPullParser parser = new MXParser();
         parser.setInput(reader);
         faqs = this.parseFml(parser, sink);
      } catch (XmlPullParserException var7) {
         throw new ParseException("Error parsing the model: " + var7.getMessage(), var7);
      } catch (IOException var8) {
         throw new ParseException("Error reading the model: " + var8.getMessage(), var8);
      }

      try {
         this.createSink(faqs, sink);
      } catch (XmlPullParserException var5) {
         throw new ParseException("Error creating sink: " + var5.getMessage(), var5);
      } catch (IOException var6) {
         throw new ParseException("Error writing to sink: " + var6.getMessage(), var6);
      }
   }

   public int getType() {
      return 2;
   }

   public Faqs parseFml(XmlPullParser parser, Sink sink) throws IOException, XmlPullParserException {
      Faqs faqs = new Faqs();
      Part currentPart = null;
      Faq currentFaq = null;
      boolean inQuestion = false;
      boolean inAnswer = false;
      StringBuffer buffer = null;

      for(int eventType = parser.getEventType(); eventType != 1; eventType = parser.nextToken()) {
         if (eventType != 2) {
            if (eventType == 3) {
               if (!parser.getName().equals("faqs")) {
                  if (parser.getName().equals("part")) {
                     faqs.addPart(currentPart);
                     currentPart = null;
                  } else if (parser.getName().equals("faq")) {
                     currentPart.addFaq(currentFaq);
                     currentFaq = null;
                  }
               }

               if (parser.getName().equals("question")) {
                  currentFaq.setQuestion(buffer.toString());
                  inQuestion = false;
               } else if (parser.getName().equals("answer")) {
                  currentFaq.setAnswer(buffer.toString());
                  inAnswer = false;
               } else if (inQuestion || inAnswer) {
                  if (buffer.charAt(buffer.length() - 1) == ' ') {
                     buffer.deleteCharAt(buffer.length() - 1);
                  }

                  buffer.append("</");
                  buffer.append(parser.getName());
                  buffer.append(">");
               }
            } else if (eventType == 5) {
               if (buffer != null && parser.getText() != null) {
                  buffer.append("<![CDATA[");
                  buffer.append(parser.getText());
                  buffer.append("]]>");
               }
            } else if (eventType == 4) {
               if (buffer != null && parser.getText() != null) {
                  buffer.append(parser.getText());
               }
            } else if (eventType == 6 && buffer != null && parser.getText() != null) {
               buffer.append(HtmlTools.escapeHTML(parser.getText()));
            }
         } else {
            if (parser.getName().equals("faqs")) {
               String title = parser.getAttributeValue((String)null, "title");
               if (title != null) {
                  faqs.setTitle(title);
               }

               String toplink = parser.getAttributeValue((String)null, "toplink");
               if (toplink != null) {
                  if (toplink.equalsIgnoreCase("true")) {
                     faqs.setToplink(true);
                  } else {
                     faqs.setToplink(false);
                  }
               }
            } else if (parser.getName().equals("part")) {
               currentPart = new Part();
               currentPart.setId(parser.getAttributeValue((String)null, "id"));
            } else if (parser.getName().equals("title")) {
               currentPart.setTitle(parser.nextText().trim());
            } else if (parser.getName().equals("faq")) {
               currentFaq = new Faq();
               currentFaq.setId(parser.getAttributeValue((String)null, "id"));
            }

            if (parser.getName().equals("question")) {
               buffer = new StringBuffer();
               inQuestion = true;
            } else if (parser.getName().equals("answer")) {
               buffer = new StringBuffer();
               inAnswer = true;
            } else if (inQuestion || inAnswer) {
               buffer.append("<");
               buffer.append(parser.getName());
               int count = parser.getAttributeCount();

               for(int i = 0; i < count; ++i) {
                  buffer.append(" ");
                  buffer.append(parser.getAttributeName(i));
                  buffer.append("=");
                  buffer.append("\"");
                  buffer.append(HtmlTools.escapeHTML(parser.getAttributeValue(i)));
                  buffer.append("\"");
               }

               buffer.append(">");
            }
         }
      }

      return faqs;
   }

   private void createSink(Faqs faqs, Sink sink) throws IOException, XmlPullParserException {
      sink.head();
      sink.title();
      sink.text(faqs.getTitle());
      sink.title_();
      sink.head_();
      sink.body();
      sink.section1();
      sink.sectionTitle1();
      sink.anchor("top");
      sink.text(faqs.getTitle());
      sink.anchor_();
      sink.sectionTitle1_();
      Iterator partIterator = faqs.getParts().iterator();

      Part part;
      Iterator faqIterator;
      Faq faq;
      while(partIterator.hasNext()) {
         part = (Part)partIterator.next();
         if (StringUtils.isNotEmpty(part.getTitle())) {
            sink.paragraph();
            sink.bold();
            sink.text(part.getTitle());
            sink.bold_();
            sink.paragraph_();
         }

         sink.numberedList(0);
         faqIterator = part.getFaqs().iterator();

         while(faqIterator.hasNext()) {
            faq = (Faq)faqIterator.next();
            sink.numberedListItem();
            sink.link("#" + HtmlTools.encodeId(faq.getId()));
            sink.rawText(faq.getQuestion());
            sink.link_();
            sink.numberedListItem_();
         }

         sink.numberedList_();
      }

      sink.section1_();
      partIterator = faqs.getParts().iterator();

      while(partIterator.hasNext()) {
         part = (Part)partIterator.next();
         if (StringUtils.isNotEmpty(part.getTitle())) {
            sink.section1();
            sink.sectionTitle1();
            sink.text(part.getTitle());
            sink.sectionTitle1_();
         }

         sink.definitionList();

         for(faqIterator = part.getFaqs().iterator(); faqIterator.hasNext(); sink.definition_()) {
            faq = (Faq)faqIterator.next();
            sink.definedTerm();
            sink.anchor(faq.getId());
            sink.rawText(faq.getQuestion());
            sink.anchor_();
            sink.definedTerm_();
            sink.definition();
            sink.paragraph();
            this.writeAnswer(sink, faq.getAnswer());
            sink.paragraph_();
            if (faqs.isToplink()) {
               this.writeTopLink(sink);
            }

            if (faqIterator.hasNext()) {
               sink.horizontalRule();
            }
         }

         sink.definitionList_();
         if (StringUtils.isNotEmpty(part.getTitle())) {
            sink.section1_();
         }
      }

      sink.body_();
   }

   private void writeAnswer(Sink sink, String answer) throws IOException, XmlPullParserException {
      int startSource = answer.indexOf("<source>");
      if (startSource != -1) {
         this.writeAnswerWithSource(sink, answer);
      } else {
         sink.rawText(answer);
      }

   }

   private void writeTopLink(Sink sink) {
      sink.rawText("<table border=\"0\">");
      sink.rawText("<tr><td align=\"right\">");
      sink.link("#top");
      sink.text("[top]");
      sink.link_();
      sink.rawText("</td></tr>");
      sink.rawText("</table>");
   }

   private void writeAnswerWithSource(Sink sink, String answer) throws IOException, XmlPullParserException {
      XmlPullParser parser = new MXParser();
      parser.setInput(new StringReader("<answer>" + answer + "</answer>"));
      int countSource = 0;

      for(int eventType = parser.getEventType(); eventType != 1; eventType = parser.nextToken()) {
         if (eventType != 2) {
            if (eventType == 3) {
               if (parser.getName().equals("source") && countSource == 1) {
                  --countSource;
                  sink.verbatim_();
               } else if (parser.getName().equals("source")) {
                  sink.rawText(HtmlTools.escapeHTML("</" + parser.getName() + ">"));
                  --countSource;
               } else if (!parser.getName().equals("answer")) {
                  if (countSource > 0) {
                     sink.rawText(HtmlTools.escapeHTML("</" + parser.getName() + ">"));
                  } else {
                     sink.rawText("</" + parser.getName() + ">");
                  }
               }
            } else if (eventType == 5) {
               sink.rawText(HtmlTools.escapeHTML(parser.getText()));
            } else if (eventType == 4) {
               sink.rawText(HtmlTools.escapeHTML(parser.getText()));
            } else if (eventType == 6) {
               sink.rawText(HtmlTools.escapeHTML(parser.getText()));
            }
         } else if (parser.getName().equals("source") && countSource == 0) {
            sink.verbatim(true);
            ++countSource;
         } else if (parser.getName().equals("source")) {
            sink.rawText(HtmlTools.escapeHTML("<" + parser.getName() + ">"));
            ++countSource;
         } else if (!parser.getName().equals("answer")) {
            if (countSource > 0) {
               sink.rawText(HtmlTools.escapeHTML("<" + parser.getName() + ">"));
            } else {
               StringBuffer buffer = new StringBuffer();
               buffer.append("<" + parser.getName());
               int count = parser.getAttributeCount();

               for(int i = 0; i < count; ++i) {
                  buffer.append(" ");
                  buffer.append(parser.getAttributeName(i));
                  buffer.append("=");
                  buffer.append("\"");
                  buffer.append(HtmlTools.escapeHTML(parser.getAttributeValue(i)));
                  buffer.append("\"");
               }

               buffer.append(">");
               sink.rawText(buffer.toString());
            }
         }
      }

   }
}
