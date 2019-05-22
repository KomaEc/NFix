package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.util.regexp.Regexp;

public final class LineContainsRegExp extends BaseParamFilterReader implements ChainableReader {
   private static final String REGEXP_KEY = "regexp";
   private static final String NEGATE_KEY = "negate";
   private Vector regexps = new Vector();
   private String line = null;
   private boolean negate = false;

   public LineContainsRegExp() {
   }

   public LineContainsRegExp(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      int ch = -1;
      if (this.line != null) {
         ch = this.line.charAt(0);
         if (this.line.length() == 1) {
            this.line = null;
         } else {
            this.line = this.line.substring(1);
         }
      } else {
         int regexpsSize = this.regexps.size();

         for(this.line = this.readLine(); this.line != null; this.line = this.readLine()) {
            boolean matches = true;

            for(int i = 0; matches && i < regexpsSize; ++i) {
               RegularExpression regexp = (RegularExpression)this.regexps.elementAt(i);
               Regexp re = regexp.getRegexp(this.getProject());
               matches = re.matches(this.line);
            }

            if (matches ^ this.isNegated()) {
               break;
            }
         }

         if (this.line != null) {
            return this.read();
         }
      }

      return ch;
   }

   public void addConfiguredRegexp(RegularExpression regExp) {
      this.regexps.addElement(regExp);
   }

   private void setRegexps(Vector regexps) {
      this.regexps = regexps;
   }

   private Vector getRegexps() {
      return this.regexps;
   }

   public Reader chain(Reader rdr) {
      LineContainsRegExp newFilter = new LineContainsRegExp(rdr);
      newFilter.setRegexps(this.getRegexps());
      newFilter.setNegate(this.isNegated());
      return newFilter;
   }

   public void setNegate(boolean b) {
      this.negate = b;
   }

   public boolean isNegated() {
      return this.negate;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if ("regexp".equals(params[i].getType())) {
               String pattern = params[i].getValue();
               RegularExpression regexp = new RegularExpression();
               regexp.setPattern(pattern);
               this.regexps.addElement(regexp);
            } else if ("negate".equals(params[i].getType())) {
               this.setNegate(Project.toBoolean(params[i].getValue()));
            }
         }
      }

   }
}
