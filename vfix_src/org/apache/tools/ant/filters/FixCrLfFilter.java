package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.types.EnumeratedAttribute;

public final class FixCrLfFilter extends BaseParamFilterReader implements ChainableReader {
   private static final char CTRLZ = '\u001a';
   private int tabLength = 8;
   private FixCrLfFilter.CrLf eol;
   private FixCrLfFilter.AddAsisRemove ctrlz;
   private FixCrLfFilter.AddAsisRemove tabs;
   private boolean javafiles = false;
   private boolean fixlast = true;
   private boolean initialized = false;

   public FixCrLfFilter() {
      this.tabs = FixCrLfFilter.AddAsisRemove.ASIS;
      if (Os.isFamily("mac") && !Os.isFamily("unix")) {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.REMOVE;
         this.setEol(FixCrLfFilter.CrLf.MAC);
      } else if (Os.isFamily("dos")) {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.ASIS;
         this.setEol(FixCrLfFilter.CrLf.DOS);
      } else {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.REMOVE;
         this.setEol(FixCrLfFilter.CrLf.UNIX);
      }

   }

   public FixCrLfFilter(Reader in) throws IOException {
      super(in);
      this.tabs = FixCrLfFilter.AddAsisRemove.ASIS;
      if (Os.isFamily("mac") && !Os.isFamily("unix")) {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.REMOVE;
         this.setEol(FixCrLfFilter.CrLf.MAC);
      } else if (Os.isFamily("dos")) {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.ASIS;
         this.setEol(FixCrLfFilter.CrLf.DOS);
      } else {
         this.ctrlz = FixCrLfFilter.AddAsisRemove.REMOVE;
         this.setEol(FixCrLfFilter.CrLf.UNIX);
      }

   }

   public Reader chain(Reader rdr) {
      try {
         FixCrLfFilter newFilter = new FixCrLfFilter(rdr);
         newFilter.setJavafiles(this.getJavafiles());
         newFilter.setEol(this.getEol());
         newFilter.setTab(this.getTab());
         newFilter.setTablength(this.getTablength());
         newFilter.setEof(this.getEof());
         newFilter.setFixlast(this.getFixlast());
         newFilter.initInternalFilters();
         return newFilter;
      } catch (IOException var3) {
         throw new BuildException(var3);
      }
   }

   public FixCrLfFilter.AddAsisRemove getEof() {
      return this.ctrlz.newInstance();
   }

   public FixCrLfFilter.CrLf getEol() {
      return this.eol.newInstance();
   }

   public boolean getFixlast() {
      return this.fixlast;
   }

   public boolean getJavafiles() {
      return this.javafiles;
   }

   public FixCrLfFilter.AddAsisRemove getTab() {
      return this.tabs.newInstance();
   }

   public int getTablength() {
      return this.tabLength;
   }

   private static String calculateEolString(FixCrLfFilter.CrLf eol) {
      if (eol == FixCrLfFilter.CrLf.ASIS) {
         return System.getProperty("line.separator");
      } else if (eol != FixCrLfFilter.CrLf.CR && eol != FixCrLfFilter.CrLf.MAC) {
         return eol != FixCrLfFilter.CrLf.CRLF && eol != FixCrLfFilter.CrLf.DOS ? "\n" : "\r\n";
      } else {
         return "\r";
      }
   }

   private void initInternalFilters() {
      this.in = (Reader)(this.ctrlz == FixCrLfFilter.AddAsisRemove.REMOVE ? new FixCrLfFilter.RemoveEofFilter(this.in) : this.in);
      this.in = new FixCrLfFilter.NormalizeEolFilter(this.in, calculateEolString(this.eol), this.getFixlast());
      if (this.tabs != FixCrLfFilter.AddAsisRemove.ASIS) {
         if (this.getJavafiles()) {
            this.in = new FixCrLfFilter.MaskJavaTabLiteralsFilter(this.in);
         }

         this.in = (Reader)(this.tabs == FixCrLfFilter.AddAsisRemove.ADD ? new FixCrLfFilter.AddTabFilter(this.in, this.getTablength()) : new FixCrLfFilter.RemoveTabFilter(this.in, this.getTablength()));
      }

      this.in = (Reader)(this.ctrlz == FixCrLfFilter.AddAsisRemove.ADD ? new FixCrLfFilter.AddEofFilter(this.in) : this.in);
      this.initialized = true;
   }

   public synchronized int read() throws IOException {
      if (!this.initialized) {
         this.initInternalFilters();
      }

      return this.in.read();
   }

   public void setEof(FixCrLfFilter.AddAsisRemove attr) {
      this.ctrlz = attr.resolve();
   }

   public void setEol(FixCrLfFilter.CrLf attr) {
      this.eol = attr.resolve();
   }

   public void setFixlast(boolean fixlast) {
      this.fixlast = fixlast;
   }

   public void setJavafiles(boolean javafiles) {
      this.javafiles = javafiles;
   }

   public void setTab(FixCrLfFilter.AddAsisRemove attr) {
      this.tabs = attr.resolve();
   }

   public void setTablength(int tabLength) throws IOException {
      if (tabLength >= 2 && tabLength <= 80) {
         this.tabLength = tabLength;
      } else {
         throw new IOException("tablength must be between 2 and 80");
      }
   }

   public static class CrLf extends EnumeratedAttribute {
      private static final FixCrLfFilter.CrLf ASIS = newInstance("asis");
      private static final FixCrLfFilter.CrLf CR = newInstance("cr");
      private static final FixCrLfFilter.CrLf CRLF = newInstance("crlf");
      private static final FixCrLfFilter.CrLf DOS = newInstance("dos");
      private static final FixCrLfFilter.CrLf LF = newInstance("lf");
      private static final FixCrLfFilter.CrLf MAC = newInstance("mac");
      private static final FixCrLfFilter.CrLf UNIX = newInstance("unix");

      public String[] getValues() {
         return new String[]{"asis", "cr", "lf", "crlf", "mac", "unix", "dos"};
      }

      public boolean equals(Object other) {
         return other instanceof FixCrLfFilter.CrLf && this.getIndex() == ((FixCrLfFilter.CrLf)other).getIndex();
      }

      public int hashCode() {
         return this.getIndex();
      }

      FixCrLfFilter.CrLf resolve() {
         if (this.equals(ASIS)) {
            return ASIS;
         } else if (!this.equals(CR) && !this.equals(MAC)) {
            if (!this.equals(CRLF) && !this.equals(DOS)) {
               if (!this.equals(LF) && !this.equals(UNIX)) {
                  throw new IllegalStateException("No replacement for " + this);
               } else {
                  return LF;
               }
            } else {
               return CRLF;
            }
         } else {
            return CR;
         }
      }

      private FixCrLfFilter.CrLf newInstance() {
         return newInstance(this.getValue());
      }

      public static FixCrLfFilter.CrLf newInstance(String value) {
         FixCrLfFilter.CrLf c = new FixCrLfFilter.CrLf();
         c.setValue(value);
         return c;
      }
   }

   public static class AddAsisRemove extends EnumeratedAttribute {
      private static final FixCrLfFilter.AddAsisRemove ASIS = newInstance("asis");
      private static final FixCrLfFilter.AddAsisRemove ADD = newInstance("add");
      private static final FixCrLfFilter.AddAsisRemove REMOVE = newInstance("remove");

      public String[] getValues() {
         return new String[]{"add", "asis", "remove"};
      }

      public boolean equals(Object other) {
         return other instanceof FixCrLfFilter.AddAsisRemove && this.getIndex() == ((FixCrLfFilter.AddAsisRemove)other).getIndex();
      }

      public int hashCode() {
         return this.getIndex();
      }

      FixCrLfFilter.AddAsisRemove resolve() throws IllegalStateException {
         if (this.equals(ASIS)) {
            return ASIS;
         } else if (this.equals(ADD)) {
            return ADD;
         } else if (this.equals(REMOVE)) {
            return REMOVE;
         } else {
            throw new IllegalStateException("No replacement for " + this);
         }
      }

      private FixCrLfFilter.AddAsisRemove newInstance() {
         return newInstance(this.getValue());
      }

      public static FixCrLfFilter.AddAsisRemove newInstance(String value) {
         FixCrLfFilter.AddAsisRemove a = new FixCrLfFilter.AddAsisRemove();
         a.setValue(value);
         return a;
      }
   }

   private static class RemoveTabFilter extends FixCrLfFilter.SimpleFilterReader {
      private int columnNumber = 0;
      private int tabLength = 0;

      public RemoveTabFilter(Reader in, int tabLength) {
         super(in);
         this.tabLength = tabLength;
      }

      public int read() throws IOException {
         int c = super.read();
         switch(c) {
         case 9:
            int width = this.tabLength - this.columnNumber % this.tabLength;
            if (!this.editsBlocked()) {
               while(width > 1) {
                  this.push(' ');
                  --width;
               }

               c = 32;
            }

            this.columnNumber += width;
            break;
         case 10:
         case 13:
            this.columnNumber = 0;
            break;
         case 11:
         case 12:
         default:
            ++this.columnNumber;
         }

         return c;
      }
   }

   private static class AddTabFilter extends FixCrLfFilter.SimpleFilterReader {
      private int columnNumber = 0;
      private int tabLength = 0;

      public AddTabFilter(Reader in, int tabLength) {
         super(in);
         this.tabLength = tabLength;
      }

      public int read() throws IOException {
         int c = super.read();
         switch(c) {
         case 9:
            this.columnNumber = (this.columnNumber + this.tabLength - 1) / this.tabLength * this.tabLength;
            break;
         case 10:
         case 13:
            this.columnNumber = 0;
            break;
         case 32:
            ++this.columnNumber;
            if (!this.editsBlocked()) {
               int colNextTab = (this.columnNumber + this.tabLength - 1) / this.tabLength * this.tabLength;
               int countSpaces = 1;
               int numTabs = 0;

               label34:
               while((c = super.read()) != -1) {
                  switch(c) {
                  case 9:
                     this.columnNumber = colNextTab;
                     ++numTabs;
                     countSpaces = 0;
                     colNextTab += this.tabLength;
                     break;
                  case 32:
                     if (++this.columnNumber == colNextTab) {
                        ++numTabs;
                        countSpaces = 0;
                        colNextTab += this.tabLength;
                     } else {
                        ++countSpaces;
                     }
                     break;
                  default:
                     this.push(c);
                     break label34;
                  }
               }

               while(countSpaces-- > 0) {
                  this.push(' ');
                  --this.columnNumber;
               }

               while(numTabs-- > 0) {
                  this.push('\t');
                  this.columnNumber -= this.tabLength;
               }

               c = super.read();
               switch(c) {
               case 9:
                  this.columnNumber += this.tabLength;
                  break;
               case 32:
                  ++this.columnNumber;
               }
            }
            break;
         default:
            ++this.columnNumber;
         }

         return c;
      }
   }

   private static class RemoveEofFilter extends FixCrLfFilter.SimpleFilterReader {
      private int lookAhead = -1;

      public RemoveEofFilter(Reader in) {
         super(in);

         try {
            this.lookAhead = in.read();
         } catch (IOException var3) {
            this.lookAhead = -1;
         }

      }

      public int read() throws IOException {
         int lookAhead2 = super.read();
         if (lookAhead2 == -1 && this.lookAhead == 26) {
            return -1;
         } else {
            int i = this.lookAhead;
            this.lookAhead = lookAhead2;
            return i;
         }
      }
   }

   private static class AddEofFilter extends FixCrLfFilter.SimpleFilterReader {
      private int lastChar = -1;

      public AddEofFilter(Reader in) {
         super(in);
      }

      public int read() throws IOException {
         int thisChar = super.read();
         if (thisChar == -1) {
            if (this.lastChar != 26) {
               this.lastChar = 26;
               return this.lastChar;
            }
         } else {
            this.lastChar = thisChar;
         }

         return thisChar;
      }
   }

   private static class NormalizeEolFilter extends FixCrLfFilter.SimpleFilterReader {
      private boolean previousWasEOL;
      private boolean fixLast;
      private int normalizedEOL = 0;
      private char[] eol = null;

      public NormalizeEolFilter(Reader in, String eolString, boolean fixLast) {
         super(in);
         this.eol = eolString.toCharArray();
         this.fixLast = fixLast;
      }

      public int read() throws IOException {
         int thisChar = super.read();
         if (this.normalizedEOL == 0) {
            int numEOL = 0;
            boolean atEnd = false;
            switch(thisChar) {
            case -1:
               atEnd = true;
               if (this.fixLast && !this.previousWasEOL) {
                  numEOL = 1;
               }
               break;
            case 10:
               numEOL = 1;
               break;
            case 13:
               numEOL = 1;
               int c1 = super.read();
               int c2 = super.read();
               if (c1 != 13 || c2 != 10) {
                  if (c1 == 13) {
                     numEOL = 2;
                     this.push(c2);
                  } else if (c1 == 10) {
                     this.push(c2);
                  } else {
                     this.push(c2);
                     this.push(c1);
                  }
               }
               break;
            case 26:
               int c = super.read();
               if (c == -1) {
                  atEnd = true;
                  if (this.fixLast && !this.previousWasEOL) {
                     numEOL = 1;
                     this.push(thisChar);
                  }
               } else {
                  this.push(c);
               }
            }

            if (numEOL <= 0) {
               if (!atEnd) {
                  this.previousWasEOL = false;
               }
            } else {
               while(numEOL-- > 0) {
                  this.push(this.eol);
                  this.normalizedEOL += this.eol.length;
               }

               this.previousWasEOL = true;
               thisChar = this.read();
            }
         } else {
            --this.normalizedEOL;
         }

         return thisChar;
      }
   }

   private static class MaskJavaTabLiteralsFilter extends FixCrLfFilter.SimpleFilterReader {
      private boolean editsBlocked = false;
      private static final int JAVA = 1;
      private static final int IN_CHAR_CONST = 2;
      private static final int IN_STR_CONST = 3;
      private static final int IN_SINGLE_COMMENT = 4;
      private static final int IN_MULTI_COMMENT = 5;
      private static final int TRANS_TO_COMMENT = 6;
      private static final int TRANS_FROM_MULTI = 8;
      private int state = 1;

      public MaskJavaTabLiteralsFilter(Reader in) {
         super(in);
      }

      public boolean editsBlocked() {
         return this.editsBlocked || super.editsBlocked();
      }

      public int read() throws IOException {
         int thisChar = super.read();
         this.editsBlocked = this.state == 2 || this.state == 3;
         switch(this.state) {
         case 1:
            switch(thisChar) {
            case 34:
               this.state = 3;
               return thisChar;
            case 39:
               this.state = 2;
               return thisChar;
            case 47:
               this.state = 6;
               return thisChar;
            default:
               return thisChar;
            }
         case 2:
            switch(thisChar) {
            case 39:
               this.state = 1;
               return thisChar;
            default:
               return thisChar;
            }
         case 3:
            switch(thisChar) {
            case 34:
               this.state = 1;
               return thisChar;
            default:
               return thisChar;
            }
         case 4:
            switch(thisChar) {
            case 10:
            case 13:
               this.state = 1;
               return thisChar;
            default:
               return thisChar;
            }
         case 5:
            switch(thisChar) {
            case 42:
               this.state = 8;
               return thisChar;
            default:
               return thisChar;
            }
         case 6:
            switch(thisChar) {
            case 34:
               this.state = 3;
               break;
            case 39:
               this.state = 2;
               break;
            case 42:
               this.state = 5;
               break;
            case 47:
               this.state = 4;
               break;
            default:
               this.state = 1;
            }
         case 7:
         default:
            break;
         case 8:
            switch(thisChar) {
            case 47:
               this.state = 1;
            }
         }

         return thisChar;
      }
   }

   private static class SimpleFilterReader extends Reader {
      private Reader in;
      private int[] preempt = new int[16];
      private int preemptIndex = 0;

      public SimpleFilterReader(Reader in) {
         this.in = in;
      }

      public void push(char c) {
         this.push((int)c);
      }

      public void push(int c) {
         try {
            this.preempt[this.preemptIndex++] = c;
         } catch (ArrayIndexOutOfBoundsException var4) {
            int[] p2 = new int[this.preempt.length * 2];
            System.arraycopy(this.preempt, 0, p2, 0, this.preempt.length);
            this.preempt = p2;
            this.push(c);
         }

      }

      public void push(char[] cs, int start, int length) {
         int i = start + length - 1;

         while(i >= start) {
            this.push(cs[i--]);
         }

      }

      public void push(char[] cs) {
         this.push(cs, 0, cs.length);
      }

      public void push(String s) {
         this.push(s.toCharArray());
      }

      public boolean editsBlocked() {
         return this.in instanceof FixCrLfFilter.SimpleFilterReader && ((FixCrLfFilter.SimpleFilterReader)this.in).editsBlocked();
      }

      public int read() throws IOException {
         return this.preemptIndex > 0 ? this.preempt[--this.preemptIndex] : this.in.read();
      }

      public void close() throws IOException {
         this.in.close();
      }

      public void reset() throws IOException {
         this.in.reset();
      }

      public boolean markSupported() {
         return this.in.markSupported();
      }

      public boolean ready() throws IOException {
         return this.in.ready();
      }

      public void mark(int i) throws IOException {
         this.in.mark(i);
      }

      public long skip(long i) throws IOException {
         return this.in.skip(i);
      }

      public int read(char[] buf) throws IOException {
         return this.read(buf, 0, buf.length);
      }

      public int read(char[] buf, int start, int length) throws IOException {
         int count = 0;

         int c;
         for(c = 0; length-- > 0 && (c = this.read()) != -1; ++count) {
            buf[start++] = (char)c;
         }

         return count == 0 && c == -1 ? -1 : count;
      }
   }
}
