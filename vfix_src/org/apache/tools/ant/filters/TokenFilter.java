package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Substitution;
import org.apache.tools.ant.util.LineTokenizer;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.Tokenizer;
import org.apache.tools.ant.util.regexp.Regexp;

public class TokenFilter extends BaseFilterReader implements ChainableReader {
   private Vector filters = new Vector();
   private Tokenizer tokenizer = null;
   private String delimOutput = null;
   private String line = null;
   private int linePos = 0;

   public TokenFilter() {
   }

   public TokenFilter(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (this.tokenizer == null) {
         this.tokenizer = new LineTokenizer();
      }

      while(this.line == null || this.line.length() == 0) {
         this.line = this.tokenizer.getToken(this.in);
         if (this.line == null) {
            return -1;
         }

         Enumeration e = this.filters.elements();

         while(e.hasMoreElements()) {
            TokenFilter.Filter filter = (TokenFilter.Filter)e.nextElement();
            this.line = filter.filter(this.line);
            if (this.line == null) {
               break;
            }
         }

         this.linePos = 0;
         if (this.line != null && this.tokenizer.getPostToken().length() != 0) {
            if (this.delimOutput != null) {
               this.line = this.line + this.delimOutput;
            } else {
               this.line = this.line + this.tokenizer.getPostToken();
            }
         }
      }

      int ch = this.line.charAt(this.linePos);
      ++this.linePos;
      if (this.linePos == this.line.length()) {
         this.line = null;
      }

      return ch;
   }

   public final Reader chain(Reader reader) {
      TokenFilter newFilter = new TokenFilter(reader);
      newFilter.filters = this.filters;
      newFilter.tokenizer = this.tokenizer;
      newFilter.delimOutput = this.delimOutput;
      newFilter.setProject(this.getProject());
      return newFilter;
   }

   public void setDelimOutput(String delimOutput) {
      this.delimOutput = resolveBackSlash(delimOutput);
   }

   public void addLineTokenizer(LineTokenizer tokenizer) {
      this.add((Tokenizer)tokenizer);
   }

   public void addStringTokenizer(TokenFilter.StringTokenizer tokenizer) {
      this.add((Tokenizer)tokenizer);
   }

   public void addFileTokenizer(TokenFilter.FileTokenizer tokenizer) {
      this.add((Tokenizer)tokenizer);
   }

   public void add(Tokenizer tokenizer) {
      if (this.tokenizer != null) {
         throw new BuildException("Only one tokenizer allowed");
      } else {
         this.tokenizer = tokenizer;
      }
   }

   public void addReplaceString(TokenFilter.ReplaceString filter) {
      this.filters.addElement(filter);
   }

   public void addContainsString(TokenFilter.ContainsString filter) {
      this.filters.addElement(filter);
   }

   public void addReplaceRegex(TokenFilter.ReplaceRegex filter) {
      this.filters.addElement(filter);
   }

   public void addContainsRegex(TokenFilter.ContainsRegex filter) {
      this.filters.addElement(filter);
   }

   public void addTrim(TokenFilter.Trim filter) {
      this.filters.addElement(filter);
   }

   public void addIgnoreBlank(TokenFilter.IgnoreBlank filter) {
      this.filters.addElement(filter);
   }

   public void addDeleteCharacters(TokenFilter.DeleteCharacters filter) {
      this.filters.addElement(filter);
   }

   public void add(TokenFilter.Filter filter) {
      this.filters.addElement(filter);
   }

   public static String resolveBackSlash(String input) {
      return StringUtils.resolveBackSlash(input);
   }

   public static int convertRegexOptions(String flags) {
      if (flags == null) {
         return 0;
      } else {
         int options = 0;
         if (flags.indexOf(103) != -1) {
            options |= 16;
         }

         if (flags.indexOf(105) != -1) {
            options |= 256;
         }

         if (flags.indexOf(109) != -1) {
            options |= 4096;
         }

         if (flags.indexOf(115) != -1) {
            options |= 65536;
         }

         return options;
      }
   }

   public static class DeleteCharacters extends ProjectComponent implements TokenFilter.Filter, ChainableReader {
      private String deleteChars = "";

      public void setChars(String deleteChars) {
         this.deleteChars = TokenFilter.resolveBackSlash(deleteChars);
      }

      public String filter(String string) {
         StringBuffer output = new StringBuffer(string.length());

         for(int i = 0; i < string.length(); ++i) {
            char ch = string.charAt(i);
            if (!this.isDeleteCharacter(ch)) {
               output.append(ch);
            }
         }

         return output.toString();
      }

      public Reader chain(Reader reader) {
         return new BaseFilterReader(reader) {
            public int read() throws IOException {
               int c;
               do {
                  c = this.in.read();
                  if (c == -1) {
                     return c;
                  }
               } while(DeleteCharacters.this.isDeleteCharacter((char)c));

               return c;
            }
         };
      }

      private boolean isDeleteCharacter(char c) {
         for(int d = 0; d < this.deleteChars.length(); ++d) {
            if (this.deleteChars.charAt(d) == c) {
               return true;
            }
         }

         return false;
      }
   }

   public static class IgnoreBlank extends TokenFilter.ChainableReaderFilter {
      public String filter(String line) {
         return line.trim().length() == 0 ? null : line;
      }
   }

   public static class Trim extends TokenFilter.ChainableReaderFilter {
      public String filter(String line) {
         return line.trim();
      }
   }

   public static class ContainsRegex extends TokenFilter.ChainableReaderFilter {
      private String from;
      private String to;
      private RegularExpression regularExpression;
      private Substitution substitution;
      private boolean initialized = false;
      private String flags = "";
      private int options;
      private Regexp regexp;

      public void setPattern(String from) {
         this.from = from;
      }

      public void setReplace(String to) {
         this.to = to;
      }

      public void setFlags(String flags) {
         this.flags = flags;
      }

      private void initialize() {
         if (!this.initialized) {
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
               throw new BuildException("Missing from in containsregex");
            } else {
               this.regularExpression = new RegularExpression();
               this.regularExpression.setPattern(this.from);
               this.regexp = this.regularExpression.getRegexp(this.getProject());
               if (this.to != null) {
                  this.substitution = new Substitution();
                  this.substitution.setExpression(this.to);
               }
            }
         }
      }

      public String filter(String string) {
         this.initialize();
         if (!this.regexp.matches(string, this.options)) {
            return null;
         } else {
            return this.substitution == null ? string : this.regexp.substitute(string, this.substitution.getExpression(this.getProject()), this.options);
         }
      }
   }

   public static class ReplaceRegex extends TokenFilter.ChainableReaderFilter {
      private String from;
      private String to;
      private RegularExpression regularExpression;
      private Substitution substitution;
      private boolean initialized = false;
      private String flags = "";
      private int options;
      private Regexp regexp;

      public void setPattern(String from) {
         this.from = from;
      }

      public void setReplace(String to) {
         this.to = to;
      }

      public void setFlags(String flags) {
         this.flags = flags;
      }

      private void initialize() {
         if (!this.initialized) {
            this.options = TokenFilter.convertRegexOptions(this.flags);
            if (this.from == null) {
               throw new BuildException("Missing pattern in replaceregex");
            } else {
               this.regularExpression = new RegularExpression();
               this.regularExpression.setPattern(this.from);
               this.regexp = this.regularExpression.getRegexp(this.getProject());
               if (this.to == null) {
                  this.to = "";
               }

               this.substitution = new Substitution();
               this.substitution.setExpression(this.to);
            }
         }
      }

      public String filter(String line) {
         this.initialize();
         return !this.regexp.matches(line, this.options) ? line : this.regexp.substitute(line, this.substitution.getExpression(this.getProject()), this.options);
      }
   }

   public static class ContainsString extends ProjectComponent implements TokenFilter.Filter {
      private String contains;

      public void setContains(String contains) {
         this.contains = contains;
      }

      public String filter(String string) {
         if (this.contains == null) {
            throw new BuildException("Missing contains in containsstring");
         } else {
            return string.indexOf(this.contains) > -1 ? string : null;
         }
      }
   }

   public static class ReplaceString extends TokenFilter.ChainableReaderFilter {
      private String from;
      private String to;

      public void setFrom(String from) {
         this.from = from;
      }

      public void setTo(String to) {
         this.to = to;
      }

      public String filter(String line) {
         if (this.from == null) {
            throw new BuildException("Missing from in stringreplace");
         } else {
            StringBuffer ret = new StringBuffer();
            int start = 0;

            for(int found = line.indexOf(this.from); found >= 0; found = line.indexOf(this.from, start)) {
               if (found > start) {
                  ret.append(line.substring(start, found));
               }

               if (this.to != null) {
                  ret.append(this.to);
               }

               start = found + this.from.length();
            }

            if (line.length() > start) {
               ret.append(line.substring(start, line.length()));
            }

            return ret.toString();
         }
      }
   }

   public abstract static class ChainableReaderFilter extends ProjectComponent implements ChainableReader, TokenFilter.Filter {
      private boolean byLine = true;

      public void setByLine(boolean byLine) {
         this.byLine = byLine;
      }

      public Reader chain(Reader reader) {
         TokenFilter tokenFilter = new TokenFilter(reader);
         if (!this.byLine) {
            tokenFilter.add((Tokenizer)(new TokenFilter.FileTokenizer()));
         }

         tokenFilter.add((TokenFilter.Filter)this);
         return tokenFilter;
      }
   }

   public static class StringTokenizer extends org.apache.tools.ant.util.StringTokenizer {
   }

   public static class FileTokenizer extends org.apache.tools.ant.util.FileTokenizer {
   }

   public interface Filter {
      String filter(String var1);
   }
}
