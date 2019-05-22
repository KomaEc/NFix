package difflib;

import difflib.myers.MyersDiff;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiffUtils {
   private static DiffAlgorithm defaultDiffAlgorithm = new MyersDiff();
   private static Pattern unifiedDiffChunkRe = Pattern.compile("^@@\\s+-(?:(\\d+)(?:,(\\d+))?)\\s+\\+(?:(\\d+)(?:,(\\d+))?)\\s+@@$");

   public static Patch diff(List<?> original, List<?> revised) {
      return diff(original, revised, defaultDiffAlgorithm);
   }

   public static Patch diff(List<?> original, List<?> revised, DiffAlgorithm algorithm) {
      return algorithm.diff(original, revised);
   }

   public static List<?> patch(List<?> original, Patch patch) throws PatchFailedException {
      return patch.applyTo(original);
   }

   public static List<?> unpatch(List<?> revised, Patch patch) {
      return patch.restore(revised);
   }

   public static Patch parseUnifiedDiff(List<String> diff) {
      boolean inPrelude = true;
      List<Object[]> rawChunk = new ArrayList();
      Patch patch = new Patch();
      int old_ln = 0;
      int new_ln = 0;
      Iterator i$ = diff.iterator();

      while(true) {
         String tag;
         String rest;
         while(i$.hasNext()) {
            String line = (String)i$.next();
            if (inPrelude) {
               if (line.startsWith("+++")) {
                  inPrelude = false;
               }
            } else {
               Matcher m = unifiedDiffChunkRe.matcher(line);
               if (!m.find()) {
                  if (line.length() > 0) {
                     tag = line.substring(0, 1);
                     rest = line.substring(1);
                     if (tag.equals(" ") || tag.equals("+") || tag.equals("-")) {
                        rawChunk.add(new Object[]{tag, rest});
                     }
                  } else {
                     rawChunk.add(new Object[]{" ", ""});
                  }
               } else {
                  if (rawChunk.size() != 0) {
                     List<String> oldChunkLines = new ArrayList();
                     List<String> newChunkLines = new ArrayList();
                     Iterator i$ = rawChunk.iterator();

                     label103:
                     while(true) {
                        do {
                           if (!i$.hasNext()) {
                              patch.addDelta(new ChangeDelta(new Chunk(old_ln - 1, oldChunkLines), new Chunk(new_ln - 1, newChunkLines)));
                              rawChunk.clear();
                              break label103;
                           }

                           Object[] raw_line = (Object[])i$.next();
                           tag = (String)raw_line[0];
                           rest = (String)raw_line[1];
                           if (tag.equals(" ") || tag.equals("-")) {
                              oldChunkLines.add(rest);
                           }
                        } while(!tag.equals(" ") && !tag.equals("+"));

                        newChunkLines.add(rest);
                     }
                  }

                  old_ln = m.group(1) == null ? 1 : Integer.parseInt(m.group(1));
                  new_ln = m.group(3) == null ? 1 : Integer.parseInt(m.group(3));
                  if (old_ln == 0) {
                     ++old_ln;
                  }

                  if (new_ln == 0) {
                     ++new_ln;
                  }
               }
            }
         }

         if (rawChunk.size() != 0) {
            List<String> oldChunkLines = new ArrayList();
            List<String> newChunkLines = new ArrayList();
            Iterator i$ = rawChunk.iterator();

            while(true) {
               do {
                  if (!i$.hasNext()) {
                     patch.addDelta(new ChangeDelta(new Chunk(old_ln - 1, oldChunkLines), new Chunk(new_ln - 1, newChunkLines)));
                     rawChunk.clear();
                     return patch;
                  }

                  Object[] raw_line = (Object[])i$.next();
                  tag = (String)raw_line[0];
                  rest = (String)raw_line[1];
                  if (tag.equals(" ") || tag.equals("-")) {
                     oldChunkLines.add(rest);
                  }
               } while(!tag.equals(" ") && !tag.equals("+"));

               newChunkLines.add(rest);
            }
         }

         return patch;
      }
   }

   public static List<String> generateUnifiedDiff(String original, String revised, List<String> originalLines, Patch patch, int contextSize) {
      if (patch.getDeltas().isEmpty()) {
         return new ArrayList();
      } else {
         List<String> ret = new ArrayList();
         ret.add("--- " + original);
         ret.add("+++ " + revised);
         List<Delta> patchDeltas = new ArrayList(patch.getDeltas());
         List<Delta> deltas = new ArrayList();
         Delta delta = (Delta)patchDeltas.get(0);
         deltas.add(delta);
         if (patchDeltas.size() > 1) {
            for(int i = 1; i < patchDeltas.size(); ++i) {
               int position = delta.getOriginal().getPosition();
               Delta nextDelta = (Delta)patchDeltas.get(i);
               if (position + delta.getOriginal().size() + contextSize >= nextDelta.getOriginal().getPosition() - contextSize) {
                  deltas.add(nextDelta);
               } else {
                  List<String> curBlock = processDeltas(originalLines, deltas, contextSize);
                  ret.addAll(curBlock);
                  deltas.clear();
                  deltas.add(nextDelta);
               }

               delta = nextDelta;
            }
         }

         List<String> curBlock = processDeltas(originalLines, deltas, contextSize);
         ret.addAll(curBlock);
         return ret;
      }
   }

   private static List<String> processDeltas(List<String> origLines, List<Delta> deltas, int contextSize) {
      List<String> buffer = new ArrayList();
      int origTotal = 0;
      int revTotal = 0;
      Delta curDelta = (Delta)deltas.get(0);
      int origStart = curDelta.getOriginal().getPosition() + 1 - contextSize;
      if (origStart < 1) {
         origStart = 1;
      }

      int revStart = curDelta.getRevised().getPosition() + 1 - contextSize;
      if (revStart < 1) {
         revStart = 1;
      }

      int contextStart = curDelta.getOriginal().getPosition() - contextSize;
      if (contextStart < 0) {
         contextStart = 0;
      }

      int line;
      for(line = contextStart; line < curDelta.getOriginal().getPosition(); ++line) {
         buffer.add(" " + (String)origLines.get(line));
         ++origTotal;
         ++revTotal;
      }

      buffer.addAll(getDeltaText(curDelta));
      origTotal += curDelta.getOriginal().getLines().size();
      revTotal += curDelta.getRevised().getLines().size();

      for(int deltaIndex = 1; deltaIndex < deltas.size(); ++deltaIndex) {
         Delta nextDelta = (Delta)deltas.get(deltaIndex);
         int intermediateStart = curDelta.getOriginal().getPosition() + curDelta.getOriginal().getLines().size();

         for(line = intermediateStart; line < nextDelta.getOriginal().getPosition(); ++line) {
            buffer.add(" " + (String)origLines.get(line));
            ++origTotal;
            ++revTotal;
         }

         buffer.addAll(getDeltaText(nextDelta));
         origTotal += nextDelta.getOriginal().getLines().size();
         revTotal += nextDelta.getRevised().getLines().size();
         curDelta = nextDelta;
      }

      contextStart = curDelta.getOriginal().getPosition() + curDelta.getOriginal().getLines().size();

      for(line = contextStart; line < contextStart + contextSize && line < origLines.size(); ++line) {
         buffer.add(" " + (String)origLines.get(line));
         ++origTotal;
         ++revTotal;
      }

      StringBuffer header = new StringBuffer();
      header.append("@@ -");
      header.append(origStart);
      header.append(",");
      header.append(origTotal);
      header.append(" +");
      header.append(revStart);
      header.append(",");
      header.append(revTotal);
      header.append(" @@");
      buffer.add(0, header.toString());
      return buffer;
   }

   private static List<String> getDeltaText(Delta delta) {
      List<String> buffer = new ArrayList();
      Iterator i$ = delta.getOriginal().getLines().iterator();

      Object line;
      while(i$.hasNext()) {
         line = i$.next();
         buffer.add("-" + line);
      }

      i$ = delta.getRevised().getLines().iterator();

      while(i$.hasNext()) {
         line = i$.next();
         buffer.add("+" + line);
      }

      return buffer;
   }
}
