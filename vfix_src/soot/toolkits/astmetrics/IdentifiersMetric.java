package soot.toolkits.astmetrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polyglot.ast.ClassDecl;
import polyglot.ast.FieldDecl;
import polyglot.ast.Formal;
import polyglot.ast.LocalDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.visit.NodeVisitor;
import soot.options.Options;

public class IdentifiersMetric extends ASTMetric {
   private static final Logger logger = LoggerFactory.getLogger(IdentifiersMetric.class);
   double nameComplexity = 0.0D;
   double nameCount = 0.0D;
   int dictionarySize = 0;
   ArrayList<String> dictionary;
   HashMap<String, Double> names;

   public IdentifiersMetric(Node astNode) {
      super(astNode);
      this.initializeDictionary();
   }

   private void initializeDictionary() {
      BufferedReader br = null;
      this.dictionary = new ArrayList();
      this.names = new HashMap();
      InputStream is = ClassLoader.getSystemResourceAsStream("mydict.txt");
      String line;
      if (is != null) {
         br = new BufferedReader(new InputStreamReader(is));

         try {
            while((line = br.readLine()) != null) {
               this.addWord(line);
            }
         } catch (IOException var8) {
            logger.debug("" + var8.getMessage());
         }
      }

      is = ClassLoader.getSystemResourceAsStream("soot/toolkits/astmetrics/dict.txt");
      if (is != null) {
         br = new BufferedReader(new InputStreamReader(is));

         try {
            while((line = br.readLine()) != null) {
               this.addWord(line.trim().toLowerCase());
            }
         } catch (IOException var7) {
            logger.debug("" + var7.getMessage());
         }
      }

      if ((this.dictionarySize = this.dictionary.size()) == 0) {
         logger.debug("Error reading in dictionary file(s)");
      } else if (Options.v().verbose()) {
         logger.debug("Read " + this.dictionarySize + " words in from dictionary file(s)");
      }

      try {
         is.close();
      } catch (IOException var6) {
         logger.debug("" + var6.getMessage());
      }

      try {
         if (br != null) {
            br.close();
         }
      } catch (IOException var5) {
         logger.debug("" + var5.getMessage());
      }

   }

   private void addWord(String word) {
      if (this.dictionarySize != 0 && word.compareTo((String)this.dictionary.get(this.dictionarySize - 1)) <= 0) {
         int i;
         for(i = 0; i < this.dictionarySize && word.compareTo((String)this.dictionary.get(i)) > 0; ++i) {
         }

         if (word.compareTo((String)this.dictionary.get(i)) == 0) {
            return;
         }

         this.dictionary.add(i, word);
      } else {
         this.dictionary.add(word);
      }

      ++this.dictionarySize;
   }

   public void reset() {
      this.nameComplexity = 0.0D;
      this.nameCount = 0.0D;
   }

   public void addMetrics(ClassData data) {
      data.addMetric(new MetricData("NameComplexity", new Double(this.nameComplexity)));
      data.addMetric(new MetricData("NameCount", new Double(this.nameCount)));
   }

   public NodeVisitor enter(Node parent, Node n) {
      double multiplier = 1.0D;
      String name = null;
      if (n instanceof ClassDecl) {
         name = ((ClassDecl)n).name();
         multiplier = 3.0D;
         ++this.nameCount;
      } else if (n instanceof MethodDecl) {
         name = ((MethodDecl)n).name();
         multiplier = 4.0D;
         ++this.nameCount;
      } else if (n instanceof FieldDecl) {
         name = ((FieldDecl)n).name();
         multiplier = 2.0D;
         ++this.nameCount;
      } else if (n instanceof Formal) {
         name = ((Formal)n).name();
         multiplier = 1.5D;
         ++this.nameCount;
      } else if (n instanceof LocalDecl) {
         name = ((LocalDecl)n).name();
         ++this.nameCount;
      }

      if (name != null) {
         this.nameComplexity += multiplier * this.computeNameComplexity(name);
      }

      return this.enter(n);
   }

   private double computeNameComplexity(String name) {
      if (this.names.containsKey(name)) {
         return (Double)this.names.get(name);
      } else {
         ArrayList<String> strings = new ArrayList();
         String tmp = "";

         for(int i = 0; i < name.length(); ++i) {
            char c = name.charAt(i);
            if (c > '@' && c < '[' || c > '`' && c < '{') {
               tmp = tmp + c;
            } else if (tmp.length() > 0) {
               strings.add(tmp);
               tmp = "";
            }
         }

         if (tmp.length() > 0) {
            strings.add(tmp);
         }

         ArrayList<String> tokens = new ArrayList();

         for(int i = 0; i < strings.size(); ++i) {
            tmp = (String)strings.get(i);

            while(tmp.length() > 0) {
               int caps = this.countCaps(tmp);
               int idx;
               if (caps == 0) {
                  idx = this.findCap(tmp);
                  if (idx <= 0) {
                     tokens.add(tmp.substring(0, tmp.length()));
                     break;
                  }

                  tokens.add(tmp.substring(0, idx));
                  tmp = tmp.substring(idx, tmp.length());
               } else if (caps == 1) {
                  idx = this.findCap(tmp.substring(1)) + 1;
                  if (idx <= 0) {
                     tokens.add(tmp.substring(0, tmp.length()));
                     break;
                  }

                  tokens.add(tmp.substring(0, idx));
                  tmp = tmp.substring(idx, tmp.length());
               } else {
                  if (caps >= tmp.length()) {
                     tokens.add(tmp.substring(0, caps).toLowerCase());
                     break;
                  }

                  tokens.add(tmp.substring(0, caps - 1).toLowerCase());
                  tmp = tmp.substring(caps);
               }
            }
         }

         double words = 0.0D;
         double complexity = 0.0D;

         for(int i = 0; i < tokens.size(); ++i) {
            if (this.dictionary.contains(tokens.get(i))) {
               ++words;
            }
         }

         if (words > 0.0D) {
            complexity = (double)tokens.size() / words;
         }

         this.names.put(name, new Double(complexity + this.computeCharComplexity(name)));
         return complexity;
      }
   }

   private double computeCharComplexity(String name) {
      int count = 0;
      int index = 0;
      int last = 0;

      int lng;
      for(lng = name.length(); index < lng; ++index) {
         char c = name.charAt(index);
         if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z')) {
            ++last;
         } else {
            if (last > 1) {
               count += last;
            }

            last = 0;
         }
      }

      double complexity = (double)(lng - count);
      if (complexity > 0.0D) {
         return (double)lng / complexity;
      } else {
         return (double)lng;
      }
   }

   private int countCaps(String name) {
      int caps;
      for(caps = 0; caps < name.length(); ++caps) {
         char c = name.charAt(caps);
         if (c <= '@' || c >= '[') {
            break;
         }
      }

      return caps;
   }

   private int findCap(String name) {
      for(int idx = 0; idx < name.length(); ++idx) {
         char c = name.charAt(idx);
         if (c > '@' && c < '[') {
            return idx;
         }
      }

      return -1;
   }
}
