package org.apache.velocity.app.event.implement;

import org.apache.oro.text.perl.MalformedPerl5PatternException;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.velocity.app.event.ReferenceInsertionEventHandler;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.util.RuntimeServicesAware;
import org.apache.velocity.util.StringUtils;

public abstract class EscapeReference implements ReferenceInsertionEventHandler, RuntimeServicesAware {
   private Perl5Util perl = new Perl5Util();
   private RuntimeServices rs;
   private String matchRegExp = null;

   protected abstract String escape(Object var1);

   protected abstract String getMatchAttribute();

   public Object referenceInsert(String reference, Object value) {
      if (value == null) {
         return value;
      } else if (this.matchRegExp == null) {
         return this.escape(value);
      } else {
         return this.perl.match(this.matchRegExp, reference) ? this.escape(value) : value;
      }
   }

   public void setRuntimeServices(RuntimeServices rs) {
      this.rs = rs;
      this.matchRegExp = StringUtils.nullTrim(rs.getConfiguration().getString(this.getMatchAttribute()));
      if (this.matchRegExp != null && this.matchRegExp.length() == 0) {
         this.matchRegExp = null;
      }

      if (this.matchRegExp != null) {
         try {
            this.perl.match(this.matchRegExp, "");
         } catch (MalformedPerl5PatternException var3) {
            rs.getLog().error("Invalid regular expression '" + this.matchRegExp + "'.  No escaping will be performed.", var3);
            this.matchRegExp = null;
         }
      }

   }

   protected RuntimeServices getRuntimeServices() {
      return this.rs;
   }
}
