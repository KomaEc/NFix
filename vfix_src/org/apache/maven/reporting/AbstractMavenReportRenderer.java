package org.apache.maven.reporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.validator.EmailValidator;
import org.apache.commons.validator.UrlValidator;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.util.HtmlTools;
import org.codehaus.plexus.util.StringUtils;

public abstract class AbstractMavenReportRenderer implements MavenReportRenderer {
   protected Sink sink;
   private int section;

   public AbstractMavenReportRenderer(Sink sink) {
      this.sink = sink;
   }

   public void render() {
      this.sink.head();
      this.sink.title();
      this.text(this.getTitle());
      this.sink.title_();
      this.sink.head_();
      this.sink.body();
      this.renderBody();
      this.sink.body_();
      this.sink.flush();
      this.sink.close();
   }

   protected void startSection(String name) {
      ++this.section;
      this.sink.anchor(HtmlTools.encodeId(name));
      this.sink.anchor_();
      switch(this.section) {
      case 1:
         this.sink.section1();
         this.sink.sectionTitle1();
         break;
      case 2:
         this.sink.section2();
         this.sink.sectionTitle2();
         break;
      case 3:
         this.sink.section3();
         this.sink.sectionTitle3();
         break;
      case 4:
         this.sink.section4();
         this.sink.sectionTitle4();
         break;
      case 5:
         this.sink.section5();
         this.sink.sectionTitle5();
      }

      this.text(name);
      switch(this.section) {
      case 1:
         this.sink.sectionTitle1_();
         break;
      case 2:
         this.sink.sectionTitle2_();
         break;
      case 3:
         this.sink.sectionTitle3_();
         break;
      case 4:
         this.sink.sectionTitle4_();
         break;
      case 5:
         this.sink.sectionTitle5_();
      }

   }

   protected void endSection() {
      switch(this.section) {
      case 1:
         this.sink.section1_();
         break;
      case 2:
         this.sink.section2_();
         break;
      case 3:
         this.sink.section3_();
         break;
      case 4:
         this.sink.section4_();
         break;
      case 5:
         this.sink.section5_();
      }

      --this.section;
      if (this.section < 0) {
         throw new IllegalStateException("Too many closing sections");
      }
   }

   protected void startTable() {
      this.sink.table();
   }

   protected void endTable() {
      this.sink.table_();
   }

   protected void tableHeaderCell(String text) {
      this.sink.tableHeaderCell();
      this.text(text);
      this.sink.tableHeaderCell_();
   }

   protected void tableCell(String text) {
      this.tableCell(text, false);
   }

   protected void tableCell(String text, boolean asHtml) {
      this.sink.tableCell();
      if (asHtml) {
         this.sink.rawText(text);
      } else {
         this.linkPatternedText(text);
      }

      this.sink.tableCell_();
   }

   protected void tableRow(String[] content) {
      this.sink.tableRow();
      if (content != null) {
         for(int i = 0; i < content.length; ++i) {
            this.tableCell(content[i]);
         }
      }

      this.sink.tableRow_();
   }

   protected void tableHeader(String[] content) {
      this.sink.tableRow();
      if (content != null) {
         for(int i = 0; i < content.length; ++i) {
            this.tableHeaderCell(content[i]);
         }
      }

      this.sink.tableRow_();
   }

   protected void tableCaption(String caption) {
      this.sink.tableCaption();
      this.text(caption);
      this.sink.tableCaption_();
   }

   protected void paragraph(String paragraph) {
      this.sink.paragraph();
      this.text(paragraph);
      this.sink.paragraph_();
   }

   protected void link(String href, String name) {
      this.sink.link(href);
      this.text(name);
      this.sink.link_();
   }

   protected void text(String text) {
      if (StringUtils.isEmpty(text)) {
         this.sink.text("-");
      } else {
         this.sink.text(text);
      }

   }

   protected void verbatimText(String text) {
      this.sink.verbatim(true);
      this.text(text);
      this.sink.verbatim_();
   }

   protected void verbatimLink(String text, String href) {
      if (StringUtils.isEmpty(href)) {
         this.verbatimText(text);
      } else {
         this.sink.verbatim(true);
         this.link(href, text);
         this.sink.verbatim_();
      }

   }

   protected void javaScript(String jsCode) {
      this.sink.rawText("<script type=\"text/javascript\">\n" + jsCode + "</script>");
   }

   public void linkPatternedText(String text) {
      if (StringUtils.isEmpty(text)) {
         this.text(text);
      } else {
         List segments = applyPattern(text);
         if (segments == null) {
            this.text(text);
         } else {
            Iterator it = segments.iterator();

            while(it.hasNext()) {
               String name = (String)it.next();
               String href = (String)it.next();
               if (href == null) {
                  this.text(name);
               } else if (getValidHref(href) != null) {
                  this.link(getValidHref(href), name);
               } else {
                  this.text(href);
               }
            }
         }
      }

   }

   protected static String createLinkPatternedText(String text, String href) {
      if (text == null) {
         return text;
      } else if (href == null) {
         return text;
      } else {
         StringBuffer sb = new StringBuffer();
         sb.append("{").append(text).append(", ").append(href).append("}");
         return sb.toString();
      }
   }

   protected static String propertiesToString(Properties props) {
      StringBuffer sb = new StringBuffer();
      if (props != null && !props.isEmpty()) {
         Iterator i = props.keySet().iterator();

         while(i.hasNext()) {
            String key = (String)i.next();
            sb.append(key).append("=").append(props.get(key));
            if (i.hasNext()) {
               sb.append(", ");
            }
         }

         return sb.toString();
      } else {
         return sb.toString();
      }
   }

   private static String getValidHref(String href) {
      if (StringUtils.isEmpty(href)) {
         return null;
      } else {
         href = href.trim();
         String[] schemes = new String[]{"http", "https"};
         UrlValidator urlValidator = new UrlValidator(schemes);
         if (EmailValidator.getInstance().isValid(href) || href.indexOf("?") != -1 && EmailValidator.getInstance().isValid(href.substring(0, href.indexOf("?")))) {
            return "mailto:" + href;
         } else if (href.toLowerCase().startsWith("mailto:")) {
            return href;
         } else if (urlValidator.isValid(href)) {
            return href;
         } else {
            String hrefTmp;
            if (!href.endsWith("/")) {
               hrefTmp = href + "/index.html";
            } else {
               hrefTmp = href + "index.html";
            }

            if (urlValidator.isValid(hrefTmp)) {
               return href;
            } else if (href.startsWith("./")) {
               return href.length() > 2 ? href.substring(2, href.length()) : ".";
            } else {
               return null;
            }
         }
      }
   }

   private static List applyPattern(String text) {
      if (StringUtils.isEmpty(text)) {
         return null;
      } else {
         List segments = new ArrayList();
         int braceStack;
         if (text.indexOf("${") != -1) {
            int lastComma = text.lastIndexOf(",");
            braceStack = text.lastIndexOf("}");
            if (lastComma != -1 && braceStack != -1) {
               segments.add(text.substring(lastComma + 1, braceStack).trim());
               segments.add((Object)null);
            } else {
               segments.add(text);
               segments.add((Object)null);
            }

            return segments;
         } else {
            boolean inQuote = false;
            braceStack = 0;
            int lastOffset = 0;

            for(int i = 0; i < text.length(); ++i) {
               char ch = text.charAt(i);
               if (ch == '\'' && !inQuote && braceStack == 0) {
                  if (i + 1 < text.length() && text.charAt(i + 1) == '\'') {
                     ++i;
                     segments.add(text.substring(lastOffset, i));
                     segments.add((Object)null);
                     lastOffset = i + 1;
                  } else {
                     inQuote = true;
                  }
               } else {
                  switch(ch) {
                  case '\'':
                     inQuote = false;
                     break;
                  case '{':
                     if (!inQuote) {
                        if (braceStack == 0) {
                           if (i != 0) {
                              segments.add(text.substring(lastOffset, i));
                              segments.add((Object)null);
                           }

                           lastOffset = i + 1;
                        }

                        ++braceStack;
                     }
                     break;
                  case '}':
                     if (!inQuote) {
                        --braceStack;
                        if (braceStack == 0) {
                           String subString = text.substring(lastOffset, i);
                           lastOffset = i + 1;
                           int lastComma = subString.lastIndexOf(",");
                           if (lastComma != -1) {
                              segments.add(subString.substring(0, lastComma).trim());
                              segments.add(subString.substring(lastComma + 1).trim());
                           } else {
                              segments.add(subString);
                              segments.add((Object)null);
                           }
                        }
                     }
                  }
               }
            }

            if (!StringUtils.isEmpty(text.substring(lastOffset, text.length()))) {
               segments.add(text.substring(lastOffset, text.length()));
               segments.add((Object)null);
            }

            if (braceStack != 0) {
               throw new IllegalArgumentException("Unmatched braces in the pattern.");
            } else {
               if (inQuote) {
               }

               return Collections.unmodifiableList(segments);
            }
         }
      }
   }

   public abstract String getTitle();

   protected abstract void renderBody();
}
