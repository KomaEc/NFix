package com.gzoltar.shaded.jline.console.completer;

import com.gzoltar.shaded.jline.console.ConsoleReader;
import com.gzoltar.shaded.jline.console.CursorBuffer;
import com.gzoltar.shaded.jline.internal.Ansi;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class CandidateListCompletionHandler implements CompletionHandler {
   private boolean printSpaceAfterFullCompletion = true;
   private boolean stripAnsi;

   public boolean getPrintSpaceAfterFullCompletion() {
      return this.printSpaceAfterFullCompletion;
   }

   public void setPrintSpaceAfterFullCompletion(boolean printSpaceAfterFullCompletion) {
      this.printSpaceAfterFullCompletion = printSpaceAfterFullCompletion;
   }

   public boolean isStripAnsi() {
      return this.stripAnsi;
   }

   public void setStripAnsi(boolean stripAnsi) {
      this.stripAnsi = stripAnsi;
   }

   public boolean complete(ConsoleReader reader, List<CharSequence> candidates, int pos) throws IOException {
      CursorBuffer buf = reader.getCursorBuffer();
      String value;
      if (candidates.size() == 1) {
         value = Ansi.stripAnsi(((CharSequence)candidates.get(0)).toString());
         if (buf.cursor == buf.buffer.length() && this.printSpaceAfterFullCompletion && !value.endsWith(" ")) {
            value = value + " ";
         }

         if (value.equals(buf.toString())) {
            return false;
         } else {
            setBuffer(reader, value, pos);
            return true;
         }
      } else {
         if (candidates.size() > 1) {
            value = this.getUnambiguousCompletions(candidates);
            setBuffer(reader, value, pos);
         }

         printCandidates(reader, candidates);
         reader.drawLine();
         return true;
      }
   }

   public static void setBuffer(ConsoleReader reader, CharSequence value, int offset) throws IOException {
      while(reader.getCursorBuffer().cursor > offset && reader.backspace()) {
      }

      reader.putString(value);
      reader.setCursorPosition(offset + value.length());
   }

   public static void printCandidates(ConsoleReader reader, Collection<CharSequence> candidates) throws IOException {
      Set<CharSequence> distinct = new HashSet((Collection)candidates);
      if (distinct.size() > reader.getAutoprintThreshold()) {
         reader.println();
         reader.print(CandidateListCompletionHandler.Messages.DISPLAY_CANDIDATES.format(((Collection)candidates).size()));
         reader.flush();
         String noOpt = CandidateListCompletionHandler.Messages.DISPLAY_CANDIDATES_NO.format();
         String yesOpt = CandidateListCompletionHandler.Messages.DISPLAY_CANDIDATES_YES.format();
         char[] allowed = new char[]{yesOpt.charAt(0), noOpt.charAt(0)};

         int c;
         while((c = reader.readCharacter(allowed)) != -1) {
            String tmp = new String(new char[]{(char)c});
            if (noOpt.startsWith(tmp)) {
               reader.println();
               return;
            }

            if (yesOpt.startsWith(tmp)) {
               break;
            }

            reader.beep();
         }
      }

      if (distinct.size() != ((Collection)candidates).size()) {
         Collection<CharSequence> copy = new ArrayList();
         Iterator var8 = ((Collection)candidates).iterator();

         while(var8.hasNext()) {
            CharSequence next = (CharSequence)var8.next();
            if (!copy.contains(next)) {
               copy.add(next);
            }
         }

         candidates = copy;
      }

      reader.println();
      reader.printColumns((Collection)candidates);
   }

   private String getUnambiguousCompletions(List<CharSequence> candidates) {
      if (candidates != null && !candidates.isEmpty()) {
         if (candidates.size() == 1) {
            return ((CharSequence)candidates.get(0)).toString();
         } else {
            String first = null;
            String[] strings = new String[candidates.size() - 1];

            for(int i = 0; i < candidates.size(); ++i) {
               String str = ((CharSequence)candidates.get(i)).toString();
               if (this.stripAnsi) {
                  str = Ansi.stripAnsi(str);
               }

               if (first == null) {
                  first = str;
               } else {
                  strings[i - 1] = str;
               }
            }

            StringBuilder candidate = new StringBuilder();

            for(int i = 0; i < first.length() && this.startsWith(first.substring(0, i + 1), strings); ++i) {
               candidate.append(first.charAt(i));
            }

            return candidate.toString();
         }
      } else {
         return null;
      }
   }

   private boolean startsWith(String starts, String[] candidates) {
      String[] var3 = candidates;
      int var4 = candidates.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String candidate = var3[var5];
         if (!candidate.startsWith(starts)) {
            return false;
         }
      }

      return true;
   }

   private static enum Messages {
      DISPLAY_CANDIDATES,
      DISPLAY_CANDIDATES_YES,
      DISPLAY_CANDIDATES_NO;

      private static final ResourceBundle bundle = ResourceBundle.getBundle(CandidateListCompletionHandler.class.getName(), Locale.getDefault());

      public String format(Object... args) {
         return bundle == null ? "" : String.format(bundle.getString(this.name()), args);
      }
   }
}
