package org.testng.mustache;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.collections.Lists;

public class Mustache {
   public String run(String template, Map<String, Object> m) throws IOException {
      return this.run(template, new Model(m));
   }

   String run(String template, Model model) throws IOException {
      int lineNumber = 0;
      List<BaseChunk> chunks = Lists.newArrayList();
      int ti = 0;

      while(ti < template.length()) {
         if (template.charAt(ti) == '\n') {
            ++lineNumber;
         }

         if (template.charAt(ti) == '{' && ti + 1 < template.length() && template.charAt(ti + 1) == '{') {
            int index = ti + 2;
            int start = index;

            boolean foundEnd;
            for(foundEnd = false; index < template.length() && !foundEnd; foundEnd = template.charAt(index) == '}' && index + 1 < template.length() && template.charAt(index + 1) == '}') {
               ++index;
            }

            if (!foundEnd) {
               throw new RuntimeException("Unclosed variable at line " + lineNumber);
            }

            String variable = template.substring(start, index);
            this.p("Found variable:" + variable);
            if (!variable.startsWith("#")) {
               chunks.add(new VariableChunk(model, variable));
               ti += variable.length() + 4;
            } else {
               String conditionalVariable = variable.substring(1);
               Value value = model.resolveValue(conditionalVariable);
               int endIndex = this.findClosingIndex(template, ti, conditionalVariable);
               Object v = value.get();
               if (v != null) {
                  String subTemplate;
                  if (v instanceof Iterable) {
                     Iterable it = (Iterable)v;
                     subTemplate = template.substring(ti + variable.length() + 4, endIndex);
                     Iterator i$ = it.iterator();

                     while(i$.hasNext()) {
                        Object o = i$.next();
                        model.push(conditionalVariable, o);
                        String r = (new Mustache()).run(subTemplate, model);
                        model.popSubModel();
                        chunks.add(new StringChunk(model, r));
                     }
                  } else {
                     String subTemplate = template.substring(ti + variable.length() + 4, endIndex);
                     model.push(conditionalVariable, v);
                     subTemplate = (new Mustache()).run(subTemplate, model);
                     model.popSubModel();
                     chunks.add(new StringChunk(model, subTemplate));
                  }
               }

               ti = endIndex + variable.length() + 4;
            }
         } else {
            chunks.add(new StringChunk(model, "" + template.charAt(ti)));
            ++ti;
         }
      }

      this.p("******************** Final composition, chunks:");
      StringBuilder result = new StringBuilder();
      this.p("*** Template:" + template);
      Iterator i$ = chunks.iterator();

      BaseChunk bc;
      while(i$.hasNext()) {
         bc = (BaseChunk)i$.next();
         this.p("***  " + bc);
      }

      i$ = chunks.iterator();

      while(i$.hasNext()) {
         bc = (BaseChunk)i$.next();
         String c = bc.compose();
         result.append(c);
      }

      this.p("*** Final result:" + result);
      return result.toString();
   }

   private int findClosingIndex(String template, int ti, String conditionalVariable) {
      int result = template.lastIndexOf("{{/" + conditionalVariable);
      return result;
   }

   private void p(String string) {
   }

   public static void main(String[] args) throws IOException {
   }
}
