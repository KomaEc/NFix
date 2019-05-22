package difflib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DiffRowGenerator {
   private final boolean showInlineDiffs;
   private final boolean ignoreWhiteSpaces;
   private final boolean ignoreBlankLines;
   private final String InlineOldTag;
   private final String InlineNewTag;
   private final String InlineOldCssClass;
   private final String InlineNewCssClass;
   private final int columnWidth;

   private DiffRowGenerator(DiffRowGenerator.Builder builder) {
      this.showInlineDiffs = builder.showInlineDiffs;
      this.ignoreWhiteSpaces = builder.ignoreWhiteSpaces;
      this.ignoreBlankLines = builder.ignoreBlankLines;
      this.InlineOldTag = builder.InlineOldTag;
      this.InlineNewTag = builder.InlineNewTag;
      this.InlineOldCssClass = builder.InlineOldCssClass;
      this.InlineNewCssClass = builder.InlineNewCssClass;
      this.columnWidth = builder.columnWidth;
   }

   public List<DiffRow> generateDiffRows(List<String> original, List<String> revised) {
      return this.generateDiffRows(original, revised, DiffUtils.diff(original, revised));
   }

   public List<DiffRow> generateDiffRows(List<String> original, List<String> revised, Patch patch) {
      original = StringUtills.normalize(original);
      revised = StringUtills.normalize(revised);
      original = StringUtills.wrapText(original, this.columnWidth);
      StringUtills.wrapText(revised, this.columnWidth);
      List<DiffRow> diffRows = new ArrayList();
      int endPos = 0;
      List<Delta> deltaList = patch.getDeltas();

      for(int i = 0; i < deltaList.size(); ++i) {
         Delta delta = (Delta)deltaList.get(i);
         Chunk orig = delta.getOriginal();
         Chunk rev = delta.getRevised();
         orig.setLines(StringUtills.normalize(orig.getLines()));
         rev.setLines(StringUtills.normalize(rev.getLines()));
         orig.setLines(StringUtills.wrapText(orig.getLines(), this.columnWidth));
         rev.setLines(StringUtills.wrapText(rev.getLines(), this.columnWidth));
         Iterator i$ = original.subList(endPos, orig.getPosition()).iterator();

         String line;
         while(i$.hasNext()) {
            line = (String)i$.next();
            diffRows.add(new DiffRow(DiffRow.Tag.EQUAL, line, line));
         }

         if (delta.getClass().equals(InsertDelta.class)) {
            endPos = orig.last() + 1;
            i$ = rev.getLines().iterator();

            while(i$.hasNext()) {
               line = (String)i$.next();
               diffRows.add(new DiffRow(DiffRow.Tag.INSERT, "", line));
            }
         } else if (delta.getClass().equals(DeleteDelta.class)) {
            endPos = orig.last() + 1;
            i$ = orig.getLines().iterator();

            while(i$.hasNext()) {
               line = (String)i$.next();
               diffRows.add(new DiffRow(DiffRow.Tag.DELETE, line, ""));
            }
         } else {
            if (this.showInlineDiffs) {
               this.addInlineDiffs(delta);
            }

            int j;
            if (orig.size() == rev.size()) {
               for(j = 0; j < orig.size(); ++j) {
                  diffRows.add(new DiffRow(DiffRow.Tag.CHANGE, (String)orig.getLines().get(j), (String)rev.getLines().get(j)));
               }
            } else if (orig.size() > rev.size()) {
               for(j = 0; j < orig.size(); ++j) {
                  diffRows.add(new DiffRow(DiffRow.Tag.CHANGE, (String)orig.getLines().get(j), rev.getLines().size() > j ? (String)rev.getLines().get(j) : ""));
               }
            } else {
               for(j = 0; j < rev.size(); ++j) {
                  diffRows.add(new DiffRow(DiffRow.Tag.CHANGE, orig.getLines().size() > j ? (String)orig.getLines().get(j) : "", (String)rev.getLines().get(j)));
               }
            }

            endPos = orig.last() + 1;
         }
      }

      Iterator i$ = original.subList(endPos, original.size()).iterator();

      while(i$.hasNext()) {
         String line = (String)i$.next();
         diffRows.add(new DiffRow(DiffRow.Tag.EQUAL, line, line));
      }

      return diffRows;
   }

   private void addInlineDiffs(Delta delta) {
      List<String> orig = delta.getOriginal().getLines();
      List<String> rev = delta.getRevised().getLines();
      LinkedList<String> origList = new LinkedList();
      char[] arr$ = join(orig, "\n").toCharArray();
      int len$ = arr$.length;

      int len$;
      for(len$ = 0; len$ < len$; ++len$) {
         Character character = arr$[len$];
         origList.add(character.toString());
      }

      LinkedList<String> revList = new LinkedList();
      char[] arr$ = join(rev, "\n").toCharArray();
      len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Character character = arr$[i$];
         revList.add(character.toString());
      }

      List<Delta> inlineDeltas = DiffUtils.diff(origList, revList).getDeltas();
      if (inlineDeltas.size() < 3) {
         Collections.reverse(inlineDeltas);
         Iterator i$ = inlineDeltas.iterator();

         while(i$.hasNext()) {
            Delta inlineDelta = (Delta)i$.next();
            Chunk inlineOrig = inlineDelta.getOriginal();
            Chunk inlineRev = inlineDelta.getRevised();
            if (inlineDelta.getClass().equals(DeleteDelta.class)) {
               origList = wrapInTag(origList, inlineOrig.getPosition(), inlineOrig.getPosition() + inlineOrig.size() + 1, this.InlineOldTag, this.InlineOldCssClass);
            } else if (inlineDelta.getClass().equals(InsertDelta.class)) {
               revList = wrapInTag(revList, inlineRev.getPosition(), inlineRev.getPosition() + inlineRev.size() + 1, this.InlineNewTag, this.InlineNewCssClass);
            } else if (inlineDelta.getClass().equals(ChangeDelta.class)) {
               origList = wrapInTag(origList, inlineOrig.getPosition(), inlineOrig.getPosition() + inlineOrig.size() + 1, this.InlineOldTag, this.InlineOldCssClass);
               revList = wrapInTag(revList, inlineRev.getPosition(), inlineRev.getPosition() + inlineRev.size() + 1, this.InlineNewTag, this.InlineNewCssClass);
            }
         }

         StringBuilder origResult = new StringBuilder();
         StringBuilder revResult = new StringBuilder();
         Iterator i$ = origList.iterator();

         String character;
         while(i$.hasNext()) {
            character = (String)i$.next();
            origResult.append(character);
         }

         i$ = revList.iterator();

         while(i$.hasNext()) {
            character = (String)i$.next();
            revResult.append(character);
         }

         delta.getOriginal().setLines(Arrays.asList(origResult.toString().split("\n")));
         delta.getRevised().setLines(Arrays.asList(revResult.toString().split("\n")));
      }

   }

   public static LinkedList<String> wrapInTag(LinkedList<String> sequence, int startPosition, int endPosition, String tag, String cssClass) {
      LinkedList<String> result = (LinkedList)sequence.clone();
      StringBuilder tagBuilder = new StringBuilder();
      tagBuilder.append("<");
      tagBuilder.append(tag);
      if (cssClass != null) {
         tagBuilder.append(" class=\"");
         tagBuilder.append(cssClass);
         tagBuilder.append("\"");
      }

      tagBuilder.append(">");
      String startTag = tagBuilder.toString();
      tagBuilder.delete(0, tagBuilder.length());
      tagBuilder.append("</");
      tagBuilder.append(tag);
      tagBuilder.append(">");
      String endTag = tagBuilder.toString();
      result.add(startPosition, startTag);
      result.add(endPosition, endTag);
      return result;
   }

   public static String wrapInTag(String line, String tag, String cssClass) {
      StringBuilder tagBuilder = new StringBuilder();
      tagBuilder.append("<");
      tagBuilder.append(tag);
      if (cssClass != null) {
         tagBuilder.append(" class=\"");
         tagBuilder.append(cssClass);
         tagBuilder.append("\"");
      }

      tagBuilder.append(">");
      String startTag = tagBuilder.toString();
      tagBuilder.delete(0, tagBuilder.length());
      tagBuilder.append("</");
      tagBuilder.append(tag);
      tagBuilder.append(">");
      String endTag = tagBuilder.toString();
      return startTag + line + endTag;
   }

   private static <T> String join(Iterable<T> objs, String delimiter) {
      Iterator<T> iter = objs.iterator();
      if (!iter.hasNext()) {
         return "";
      } else {
         StringBuffer buffer = new StringBuffer(String.valueOf(iter.next()));

         while(iter.hasNext()) {
            buffer.append(delimiter).append(String.valueOf(iter.next()));
         }

         return buffer.toString();
      }
   }

   // $FF: synthetic method
   DiffRowGenerator(DiffRowGenerator.Builder x0, Object x1) {
      this(x0);
   }

   public static class Builder {
      private boolean showInlineDiffs = false;
      private boolean ignoreWhiteSpaces = true;
      private boolean ignoreBlankLines = true;
      private String InlineOldTag = "span";
      private String InlineNewTag = "span";
      private String InlineOldCssClass = "editOldInline";
      private String InlineNewCssClass = "editNewInline";
      private int columnWidth = 80;

      public DiffRowGenerator.Builder showInlineDiffs(boolean val) {
         this.showInlineDiffs = val;
         return this;
      }

      public DiffRowGenerator.Builder ignoreWhiteSpaces(boolean val) {
         this.ignoreWhiteSpaces = val;
         return this;
      }

      public DiffRowGenerator.Builder ignoreBlankLines(boolean val) {
         this.ignoreBlankLines = val;
         return this;
      }

      public DiffRowGenerator.Builder InlineOldTag(String tag) {
         this.InlineOldTag = tag;
         return this;
      }

      public DiffRowGenerator.Builder InlineNewTag(String tag) {
         this.InlineNewTag = tag;
         return this;
      }

      public DiffRowGenerator.Builder InlineOldCssClass(String cssClass) {
         this.InlineOldCssClass = cssClass;
         return this;
      }

      public DiffRowGenerator.Builder InlineNewCssClass(String cssClass) {
         this.InlineNewCssClass = cssClass;
         return this;
      }

      public DiffRowGenerator.Builder columnWidth(int width) {
         if (width > 0) {
            this.columnWidth = width;
         }

         return this;
      }

      public DiffRowGenerator build() {
         return new DiffRowGenerator(this);
      }
   }
}
