package org.apache.tools.ant.types;

import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.filters.ClassConstants;
import org.apache.tools.ant.filters.EscapeUnicode;
import org.apache.tools.ant.filters.ExpandProperties;
import org.apache.tools.ant.filters.HeadFilter;
import org.apache.tools.ant.filters.LineContains;
import org.apache.tools.ant.filters.LineContainsRegExp;
import org.apache.tools.ant.filters.PrefixLines;
import org.apache.tools.ant.filters.ReplaceTokens;
import org.apache.tools.ant.filters.StripJavaComments;
import org.apache.tools.ant.filters.StripLineBreaks;
import org.apache.tools.ant.filters.StripLineComments;
import org.apache.tools.ant.filters.TabsToSpaces;
import org.apache.tools.ant.filters.TailFilter;
import org.apache.tools.ant.filters.TokenFilter;

public class FilterChain extends DataType implements Cloneable {
   private Vector filterReaders = new Vector();

   public void addFilterReader(AntFilterReader filterReader) {
      this.filterReaders.addElement(filterReader);
   }

   public Vector getFilterReaders() {
      return this.filterReaders;
   }

   public void addClassConstants(ClassConstants classConstants) {
      this.filterReaders.addElement(classConstants);
   }

   public void addExpandProperties(ExpandProperties expandProperties) {
      this.filterReaders.addElement(expandProperties);
   }

   public void addHeadFilter(HeadFilter headFilter) {
      this.filterReaders.addElement(headFilter);
   }

   public void addLineContains(LineContains lineContains) {
      this.filterReaders.addElement(lineContains);
   }

   public void addLineContainsRegExp(LineContainsRegExp lineContainsRegExp) {
      this.filterReaders.addElement(lineContainsRegExp);
   }

   public void addPrefixLines(PrefixLines prefixLines) {
      this.filterReaders.addElement(prefixLines);
   }

   public void addReplaceTokens(ReplaceTokens replaceTokens) {
      this.filterReaders.addElement(replaceTokens);
   }

   public void addStripJavaComments(StripJavaComments stripJavaComments) {
      this.filterReaders.addElement(stripJavaComments);
   }

   public void addStripLineBreaks(StripLineBreaks stripLineBreaks) {
      this.filterReaders.addElement(stripLineBreaks);
   }

   public void addStripLineComments(StripLineComments stripLineComments) {
      this.filterReaders.addElement(stripLineComments);
   }

   public void addTabsToSpaces(TabsToSpaces tabsToSpaces) {
      this.filterReaders.addElement(tabsToSpaces);
   }

   public void addTailFilter(TailFilter tailFilter) {
      this.filterReaders.addElement(tailFilter);
   }

   public void addEscapeUnicode(EscapeUnicode escapeUnicode) {
      this.filterReaders.addElement(escapeUnicode);
   }

   public void addTokenFilter(TokenFilter tokenFilter) {
      this.filterReaders.addElement(tokenFilter);
   }

   public void addDeleteCharacters(TokenFilter.DeleteCharacters filter) {
      this.filterReaders.addElement(filter);
   }

   public void addContainsRegex(TokenFilter.ContainsRegex filter) {
      this.filterReaders.addElement(filter);
   }

   public void addReplaceRegex(TokenFilter.ReplaceRegex filter) {
      this.filterReaders.addElement(filter);
   }

   public void addTrim(TokenFilter.Trim filter) {
      this.filterReaders.addElement(filter);
   }

   public void addReplaceString(TokenFilter.ReplaceString filter) {
      this.filterReaders.addElement(filter);
   }

   public void addIgnoreBlank(TokenFilter.IgnoreBlank filter) {
      this.filterReaders.addElement(filter);
   }

   public void setRefid(Reference r) throws BuildException {
      if (!this.filterReaders.isEmpty()) {
         throw this.tooManyAttributes();
      } else {
         Object o = r.getReferencedObject(this.getProject());
         if (o instanceof FilterChain) {
            FilterChain fc = (FilterChain)o;
            this.filterReaders = fc.getFilterReaders();
            super.setRefid(r);
         } else {
            String msg = r.getRefId() + " doesn't refer to a FilterChain";
            throw new BuildException(msg);
         }
      }
   }

   public void add(ChainableReader filter) {
      this.filterReaders.addElement(filter);
   }
}
