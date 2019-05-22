package groovy.ui.text;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.DocumentFilter.FilterBypass;

public class StructuredSyntaxDocumentFilter extends DocumentFilter {
   public static final String TAB_REPLACEMENT = "    ";
   private static final StructuredSyntaxDocumentFilter.MLComparator ML_COMPARATOR = new StructuredSyntaxDocumentFilter.MLComparator();
   protected StructuredSyntaxDocumentFilter.LexerNode lexer = new StructuredSyntaxDocumentFilter.LexerNode(true);
   protected DefaultStyledDocument styledDocument;
   private Segment segment = new Segment();
   private CharBuffer buffer;
   protected SortedSet mlTextRunSet;

   private static void checkRegexp(String regexp) {
      String checking = regexp.replaceAll("\\\\\\(", "X").replaceAll("\\(\\?", "X");
      int checked = checking.indexOf(40);
      if (checked > -1) {
         String msg = "Only non-capturing groups allowed:\r\n" + regexp + "\r\n";

         for(int i = 0; i < checked; ++i) {
            msg = msg + " ";
         }

         msg = msg + "^";
         throw new IllegalArgumentException(msg);
      }
   }

   public StructuredSyntaxDocumentFilter(DefaultStyledDocument document) {
      this.mlTextRunSet = new TreeSet(ML_COMPARATOR);
      this.styledDocument = document;
   }

   private int calcBeginParse(int offset) {
      StructuredSyntaxDocumentFilter.MultiLineRun mlr = this.getMultiLineRun(offset);
      if (mlr != null) {
         offset = mlr.start();
      } else {
         offset = this.styledDocument.getParagraphElement(offset).getStartOffset();
         mlr = this.getMultiLineRun(offset);
         offset = mlr == null ? offset : mlr.end() + 1;
      }

      return offset;
   }

   private int calcEndParse(int offset) {
      StructuredSyntaxDocumentFilter.MultiLineRun mlr = this.getMultiLineRun(offset);
      if (mlr != null) {
         offset = mlr.end();
      } else {
         offset = this.styledDocument.getParagraphElement(offset).getEndOffset();
         mlr = this.getMultiLineRun(offset);
         offset = mlr == null ? offset : mlr.end();
      }

      return offset;
   }

   public StructuredSyntaxDocumentFilter.LexerNode createLexerNode() {
      return new StructuredSyntaxDocumentFilter.LexerNode(false);
   }

   private StructuredSyntaxDocumentFilter.MultiLineRun getMultiLineRun(int offset) {
      StructuredSyntaxDocumentFilter.MultiLineRun ml = null;
      if (offset > 0) {
         Integer os = offset;
         SortedSet set = this.mlTextRunSet.headSet(os);
         if (!set.isEmpty()) {
            ml = (StructuredSyntaxDocumentFilter.MultiLineRun)set.last();
            ml = ml.end() >= offset ? ml : null;
         }
      }

      return ml;
   }

   public StructuredSyntaxDocumentFilter.LexerNode getRootNode() {
      return this.lexer;
   }

   public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
      text = this.replaceMetaCharacters(text);
      fb.insertString(offset, text, attrs);
      this.parseDocument(offset, text.length());
   }

   protected void parseDocument(int offset, int length) throws BadLocationException {
      this.styledDocument.getText(0, this.styledDocument.getLength(), this.segment);
      this.buffer = CharBuffer.wrap(this.segment.array).asReadOnlyBuffer();
      if (!this.lexer.isInitialized()) {
         this.lexer.initialize();
         offset = 0;
         length = this.styledDocument.getLength();
      } else {
         int end = offset + length;
         offset = this.calcBeginParse(offset);
         length = this.calcEndParse(end) - offset;
         SortedSet set = this.mlTextRunSet.subSet(offset, offset + length);
         if (set != null) {
            set.clear();
         }
      }

      this.lexer.parse(this.buffer, offset, length);
   }

   public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
      if (offset == 0 && length != fb.getDocument().getLength()) {
         fb.replace(0, length, "\n", this.lexer.defaultStyle);
         this.parseDocument(offset, 2);
         fb.remove(offset, 1);
      } else {
         fb.remove(offset, length);
         if (offset + 1 < fb.getDocument().getLength()) {
            this.parseDocument(offset, 1);
         } else if (offset - 1 > 0) {
            this.parseDocument(offset - 1, 1);
         } else {
            this.mlTextRunSet.clear();
         }
      }

   }

   public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
      text = this.replaceMetaCharacters(text);
      fb.replace(offset, length, text, attrs);
      this.parseDocument(offset, text.length());
   }

   private String replaceMetaCharacters(String string) {
      string = string.replaceAll("\\t", "    ");
      return string;
   }

   private static class MLComparator implements Comparator {
      private MLComparator() {
      }

      public int compare(Object obj, Object obj1) {
         return this.valueOf(obj) - this.valueOf(obj1);
      }

      private int valueOf(Object obj) {
         return obj instanceof Integer ? (Integer)obj : (obj instanceof StructuredSyntaxDocumentFilter.MultiLineRun ? ((StructuredSyntaxDocumentFilter.MultiLineRun)obj).start() : ((Position)obj).getOffset());
      }

      // $FF: synthetic method
      MLComparator(Object x0) {
         this();
      }
   }

   protected class MultiLineRun {
      private Position start;
      private Position end;
      private int delimeterSize;

      public MultiLineRun(int start, int end) throws BadLocationException {
         this(start, end, 2);
      }

      public MultiLineRun(int start, int end, int delimeterSize) throws BadLocationException {
         String msg;
         if (start > end) {
            msg = "Start offset is after end: ";
            throw new BadLocationException(msg, start);
         } else if (delimeterSize < 1) {
            msg = "Delimiters be at least size 1: " + delimeterSize;
            throw new IllegalArgumentException(msg);
         } else {
            this.start = StructuredSyntaxDocumentFilter.this.styledDocument.createPosition(start);
            this.end = StructuredSyntaxDocumentFilter.this.styledDocument.createPosition(end);
            this.delimeterSize = delimeterSize;
         }
      }

      public int getDelimeterSize() {
         return this.delimeterSize;
      }

      public int end() {
         return this.end.getOffset();
      }

      public int length() {
         return this.end.getOffset() - this.start.getOffset();
      }

      public int start() {
         return this.start.getOffset();
      }

      public String toString() {
         return this.start.toString() + " " + this.end.toString();
      }
   }

   public final class LexerNode {
      private Style defaultStyle;
      private Map styleMap = new LinkedHashMap();
      private Map children = new HashMap();
      private Matcher matcher;
      private List groupList = new ArrayList();
      private boolean initialized;
      private CharBuffer lastBuffer;

      LexerNode(boolean isParent) {
         StyleContext sc = StyleContext.getDefaultStyleContext();
         this.defaultStyle = sc.getStyle("default");
      }

      private String buildRegexp(String[] regexps) {
         String regexp = "";

         for(int i = 0; i < regexps.length; ++i) {
            regexp = regexp + "|" + regexps[i];
         }

         return regexp.substring(1);
      }

      public Style getDefaultStyle() {
         return this.defaultStyle;
      }

      private void initialize() {
         this.matcher = null;
         this.groupList.clear();
         this.groupList.add((Object)null);
         Iterator iter = this.styleMap.keySet().iterator();
         String regexp = "";

         while(iter.hasNext()) {
            String nextRegexp = (String)iter.next();
            regexp = regexp + "|(" + nextRegexp + ")";
            this.groupList.add(Pattern.compile(nextRegexp).pattern());
         }

         if (!regexp.equals("")) {
            this.matcher = Pattern.compile(regexp.substring(1)).matcher("");
            iter = this.children.values().iterator();

            while(iter.hasNext()) {
               ((StructuredSyntaxDocumentFilter.LexerNode)iter.next()).initialize();
            }
         }

         this.initialized = true;
      }

      public boolean isInitialized() {
         return this.initialized;
      }

      public void parse(CharBuffer buffer, int offset, int length) throws BadLocationException {
         int checkPoint = offset + length;
         if (this.lastBuffer != buffer) {
            this.matcher.reset(buffer);
            this.lastBuffer = buffer;
         }

         int matchEnd = offset;

         for(Style style = null; matchEnd < checkPoint && this.matcher.find(offset); offset = matchEnd) {
            int groupNum = 0;

            do {
               ++groupNum;
            } while((offset = this.matcher.start(groupNum)) == -1);

            if (offset != matchEnd) {
               offset = offset > checkPoint ? checkPoint : offset;
               StructuredSyntaxDocumentFilter.this.styledDocument.setCharacterAttributes(matchEnd, offset - matchEnd, this.defaultStyle, true);
               if (offset >= checkPoint) {
                  return;
               }
            }

            matchEnd = this.matcher.end(groupNum);
            style = (Style)this.styleMap.get((String)this.groupList.get(groupNum));
            StructuredSyntaxDocumentFilter.this.styledDocument.setCharacterAttributes(offset, matchEnd - offset, style, true);
            if (StructuredSyntaxDocumentFilter.this.styledDocument.getParagraphElement(offset).getStartOffset() != StructuredSyntaxDocumentFilter.this.styledDocument.getParagraphElement(matchEnd).getStartOffset()) {
               StructuredSyntaxDocumentFilter.MultiLineRun mlr = StructuredSyntaxDocumentFilter.this.new MultiLineRun(offset, matchEnd);
               StructuredSyntaxDocumentFilter.this.mlTextRunSet.add(mlr);
            }

            StructuredSyntaxDocumentFilter.LexerNode node = (StructuredSyntaxDocumentFilter.LexerNode)this.children.get(this.groupList.get(groupNum));
            if (node != null) {
               node.parse(buffer, offset, matchEnd - offset);
            }
         }

         if (matchEnd < checkPoint) {
            StructuredSyntaxDocumentFilter.this.styledDocument.setCharacterAttributes(matchEnd, checkPoint - matchEnd, this.defaultStyle, true);
         }

      }

      public void putChild(String regexp, StructuredSyntaxDocumentFilter.LexerNode node) {
         node.defaultStyle = (Style)this.styleMap.get(regexp);
         this.children.put(Pattern.compile(regexp).pattern(), node);
         this.initialized = false;
      }

      public void putChild(String[] regexps, StructuredSyntaxDocumentFilter.LexerNode node) {
         this.putChild(this.buildRegexp(regexps), node);
      }

      public void putStyle(String regexp, Style style) {
         StructuredSyntaxDocumentFilter.checkRegexp(regexp);
         this.styleMap.put(regexp, style);
         this.initialized = false;
      }

      public void putStyle(String[] regexps, Style style) {
         this.putStyle(this.buildRegexp(regexps), style);
      }

      public void removeChild(String regexp) {
         this.children.remove(regexp);
      }

      public void removeStyle(String regexp) {
         this.styleMap.remove(regexp);
         this.children.remove(regexp);
      }

      public void removeStyle(String[] regexps) {
         this.removeStyle(this.buildRegexp(regexps));
      }

      public void setDefaultStyle(Style style) {
         this.defaultStyle = style;
      }
   }
}
