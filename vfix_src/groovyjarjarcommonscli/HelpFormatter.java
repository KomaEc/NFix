package groovyjarjarcommonscli;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HelpFormatter {
   public static final int DEFAULT_WIDTH = 74;
   public static final int DEFAULT_LEFT_PAD = 1;
   public static final int DEFAULT_DESC_PAD = 3;
   public static final String DEFAULT_SYNTAX_PREFIX = "usage: ";
   public static final String DEFAULT_OPT_PREFIX = "-";
   public static final String DEFAULT_LONG_OPT_PREFIX = "--";
   public static final String DEFAULT_ARG_NAME = "arg";
   /** @deprecated */
   public int defaultWidth = 74;
   /** @deprecated */
   public int defaultLeftPad = 1;
   /** @deprecated */
   public int defaultDescPad = 3;
   /** @deprecated */
   public String defaultSyntaxPrefix = "usage: ";
   /** @deprecated */
   public String defaultNewLine = System.getProperty("line.separator");
   /** @deprecated */
   public String defaultOptPrefix = "-";
   /** @deprecated */
   public String defaultLongOptPrefix = "--";
   /** @deprecated */
   public String defaultArgName = "arg";
   protected Comparator optionComparator = new HelpFormatter.OptionComparator();

   public void setWidth(int width) {
      this.defaultWidth = width;
   }

   public int getWidth() {
      return this.defaultWidth;
   }

   public void setLeftPadding(int padding) {
      this.defaultLeftPad = padding;
   }

   public int getLeftPadding() {
      return this.defaultLeftPad;
   }

   public void setDescPadding(int padding) {
      this.defaultDescPad = padding;
   }

   public int getDescPadding() {
      return this.defaultDescPad;
   }

   public void setSyntaxPrefix(String prefix) {
      this.defaultSyntaxPrefix = prefix;
   }

   public String getSyntaxPrefix() {
      return this.defaultSyntaxPrefix;
   }

   public void setNewLine(String newline) {
      this.defaultNewLine = newline;
   }

   public String getNewLine() {
      return this.defaultNewLine;
   }

   public void setOptPrefix(String prefix) {
      this.defaultOptPrefix = prefix;
   }

   public String getOptPrefix() {
      return this.defaultOptPrefix;
   }

   public void setLongOptPrefix(String prefix) {
      this.defaultLongOptPrefix = prefix;
   }

   public String getLongOptPrefix() {
      return this.defaultLongOptPrefix;
   }

   public void setArgName(String name) {
      this.defaultArgName = name;
   }

   public String getArgName() {
      return this.defaultArgName;
   }

   public Comparator getOptionComparator() {
      return this.optionComparator;
   }

   public void setOptionComparator(Comparator comparator) {
      if (comparator == null) {
         this.optionComparator = new HelpFormatter.OptionComparator();
      } else {
         this.optionComparator = comparator;
      }

   }

   public void printHelp(String cmdLineSyntax, Options options) {
      this.printHelp(this.defaultWidth, cmdLineSyntax, (String)null, options, (String)null, false);
   }

   public void printHelp(String cmdLineSyntax, Options options, boolean autoUsage) {
      this.printHelp(this.defaultWidth, cmdLineSyntax, (String)null, options, (String)null, autoUsage);
   }

   public void printHelp(String cmdLineSyntax, String header, Options options, String footer) {
      this.printHelp(cmdLineSyntax, header, options, footer, false);
   }

   public void printHelp(String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
      this.printHelp(this.defaultWidth, cmdLineSyntax, header, options, footer, autoUsage);
   }

   public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer) {
      this.printHelp(width, cmdLineSyntax, header, options, footer, false);
   }

   public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
      PrintWriter pw = new PrintWriter(System.out);
      this.printHelp(pw, width, cmdLineSyntax, header, options, this.defaultLeftPad, this.defaultDescPad, footer, autoUsage);
      pw.flush();
   }

   public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer) {
      this.printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, false);
   }

   public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer, boolean autoUsage) {
      if (cmdLineSyntax != null && cmdLineSyntax.length() != 0) {
         if (autoUsage) {
            this.printUsage(pw, width, cmdLineSyntax, options);
         } else {
            this.printUsage(pw, width, cmdLineSyntax);
         }

         if (header != null && header.trim().length() > 0) {
            this.printWrapped(pw, width, header);
         }

         this.printOptions(pw, width, options, leftPad, descPad);
         if (footer != null && footer.trim().length() > 0) {
            this.printWrapped(pw, width, footer);
         }

      } else {
         throw new IllegalArgumentException("cmdLineSyntax not provided");
      }
   }

   public void printUsage(PrintWriter pw, int width, String app, Options options) {
      StringBuffer buff = (new StringBuffer(this.defaultSyntaxPrefix)).append(app).append(" ");
      Collection processedGroups = new ArrayList();
      List optList = new ArrayList(options.getOptions());
      Collections.sort(optList, this.getOptionComparator());
      Iterator i = optList.iterator();

      while(i.hasNext()) {
         Option option = (Option)i.next();
         OptionGroup group = options.getOptionGroup(option);
         if (group != null) {
            if (!processedGroups.contains(group)) {
               processedGroups.add(group);
               this.appendOptionGroup(buff, group);
            }
         } else {
            appendOption(buff, option, option.isRequired());
         }

         if (i.hasNext()) {
            buff.append(" ");
         }
      }

      this.printWrapped(pw, width, buff.toString().indexOf(32) + 1, buff.toString());
   }

   private void appendOptionGroup(StringBuffer buff, OptionGroup group) {
      if (!group.isRequired()) {
         buff.append("[");
      }

      List optList = new ArrayList(group.getOptions());
      Collections.sort(optList, this.getOptionComparator());
      Iterator i = optList.iterator();

      while(i.hasNext()) {
         appendOption(buff, (Option)i.next(), true);
         if (i.hasNext()) {
            buff.append(" | ");
         }
      }

      if (!group.isRequired()) {
         buff.append("]");
      }

   }

   private static void appendOption(StringBuffer buff, Option option, boolean required) {
      if (!required) {
         buff.append("[");
      }

      if (option.getOpt() != null) {
         buff.append("-").append(option.getOpt());
      } else {
         buff.append("--").append(option.getLongOpt());
      }

      if (option.hasArg() && option.hasArgName()) {
         buff.append(" <").append(option.getArgName()).append(">");
      }

      if (!required) {
         buff.append("]");
      }

   }

   public void printUsage(PrintWriter pw, int width, String cmdLineSyntax) {
      int argPos = cmdLineSyntax.indexOf(32) + 1;
      this.printWrapped(pw, width, this.defaultSyntaxPrefix.length() + argPos, this.defaultSyntaxPrefix + cmdLineSyntax);
   }

   public void printOptions(PrintWriter pw, int width, Options options, int leftPad, int descPad) {
      StringBuffer sb = new StringBuffer();
      this.renderOptions(sb, width, options, leftPad, descPad);
      pw.println(sb.toString());
   }

   public void printWrapped(PrintWriter pw, int width, String text) {
      this.printWrapped(pw, width, 0, text);
   }

   public void printWrapped(PrintWriter pw, int width, int nextLineTabStop, String text) {
      StringBuffer sb = new StringBuffer(text.length());
      this.renderWrappedText(sb, width, nextLineTabStop, text);
      pw.println(sb.toString());
   }

   protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
      String lpad = this.createPadding(leftPad);
      String dpad = this.createPadding(descPad);
      int max = 0;
      List prefixList = new ArrayList();
      List optList = options.helpOptions();
      Collections.sort(optList, this.getOptionComparator());

      StringBuffer optBuf;
      for(Iterator i = optList.iterator(); i.hasNext(); max = optBuf.length() > max ? optBuf.length() : max) {
         Option option = (Option)i.next();
         optBuf = new StringBuffer(8);
         if (option.getOpt() == null) {
            optBuf.append(lpad).append("   " + this.defaultLongOptPrefix).append(option.getLongOpt());
         } else {
            optBuf.append(lpad).append(this.defaultOptPrefix).append(option.getOpt());
            if (option.hasLongOpt()) {
               optBuf.append(',').append(this.defaultLongOptPrefix).append(option.getLongOpt());
            }
         }

         if (option.hasArg()) {
            if (option.hasArgName()) {
               optBuf.append(" <").append(option.getArgName()).append(">");
            } else {
               optBuf.append(' ');
            }
         }

         prefixList.add(optBuf);
      }

      int x = 0;
      Iterator i = optList.iterator();

      while(i.hasNext()) {
         Option option = (Option)i.next();
         optBuf = new StringBuffer(prefixList.get(x++).toString());
         if (optBuf.length() < max) {
            optBuf.append(this.createPadding(max - optBuf.length()));
         }

         optBuf.append(dpad);
         int nextLineTabStop = max + descPad;
         if (option.getDescription() != null) {
            optBuf.append(option.getDescription());
         }

         this.renderWrappedText(sb, width, nextLineTabStop, optBuf.toString());
         if (i.hasNext()) {
            sb.append(this.defaultNewLine);
         }
      }

      return sb;
   }

   protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
      int pos = this.findWrapPos(text, width, 0);
      if (pos == -1) {
         sb.append(this.rtrim(text));
         return sb;
      } else {
         sb.append(this.rtrim(text.substring(0, pos))).append(this.defaultNewLine);
         if (nextLineTabStop >= width) {
            nextLineTabStop = 1;
         }

         String padding = this.createPadding(nextLineTabStop);

         while(true) {
            text = padding + text.substring(pos).trim();
            pos = this.findWrapPos(text, width, 0);
            if (pos == -1) {
               sb.append(text);
               return sb;
            }

            if (text.length() > width && pos == nextLineTabStop - 1) {
               pos = width;
            }

            sb.append(this.rtrim(text.substring(0, pos))).append(this.defaultNewLine);
         }
      }
   }

   protected int findWrapPos(String text, int width, int startPos) {
      int pos = true;
      int pos;
      if (((pos = text.indexOf(10, startPos)) == -1 || pos > width) && ((pos = text.indexOf(9, startPos)) == -1 || pos > width)) {
         if (startPos + width >= text.length()) {
            return -1;
         } else {
            char c;
            for(pos = startPos + width; pos >= startPos && (c = text.charAt(pos)) != ' ' && c != '\n' && c != '\r'; --pos) {
            }

            if (pos > startPos) {
               return pos;
            } else {
               for(pos = startPos + width; pos <= text.length() && (c = text.charAt(pos)) != ' ' && c != '\n' && c != '\r'; ++pos) {
               }

               return pos == text.length() ? -1 : pos;
            }
         }
      } else {
         return pos + 1;
      }
   }

   protected String createPadding(int len) {
      StringBuffer sb = new StringBuffer(len);

      for(int i = 0; i < len; ++i) {
         sb.append(' ');
      }

      return sb.toString();
   }

   protected String rtrim(String s) {
      if (s != null && s.length() != 0) {
         int pos;
         for(pos = s.length(); pos > 0 && Character.isWhitespace(s.charAt(pos - 1)); --pos) {
         }

         return s.substring(0, pos);
      } else {
         return s;
      }
   }

   private static class OptionComparator implements Comparator {
      private OptionComparator() {
      }

      public int compare(Object o1, Object o2) {
         Option opt1 = (Option)o1;
         Option opt2 = (Option)o2;
         return opt1.getKey().compareToIgnoreCase(opt2.getKey());
      }

      // $FF: synthetic method
      OptionComparator(Object x0) {
         this();
      }
   }
}
