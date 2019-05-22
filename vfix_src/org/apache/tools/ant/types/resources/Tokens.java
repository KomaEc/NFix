package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.util.ConcatResourceInputStream;
import org.apache.tools.ant.util.LineTokenizer;
import org.apache.tools.ant.util.Tokenizer;

public class Tokens extends BaseResourceCollectionWrapper {
   private Tokenizer tokenizer;
   private String encoding;

   protected synchronized Collection getCollection() {
      ResourceCollection rc = this.getResourceCollection();
      if (rc.size() == 0) {
         return Collections.EMPTY_SET;
      } else {
         if (this.tokenizer == null) {
            this.tokenizer = new LineTokenizer();
         }

         ConcatResourceInputStream cat = new ConcatResourceInputStream(rc);
         cat.setManagingComponent(this);
         InputStreamReader rdr = null;
         if (this.encoding == null) {
            rdr = new InputStreamReader(cat);
         } else {
            try {
               rdr = new InputStreamReader(cat, this.encoding);
            } catch (UnsupportedEncodingException var6) {
               throw new BuildException(var6);
            }
         }

         ArrayList result = new ArrayList();

         try {
            for(String s = this.tokenizer.getToken(rdr); s != null; s = this.tokenizer.getToken(rdr)) {
               result.add(new StringResource(s));
            }

            return result;
         } catch (IOException var7) {
            throw new BuildException("Error reading tokens", var7);
         }
      }
   }

   public synchronized void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public synchronized void add(Tokenizer tokenizer) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else if (this.tokenizer != null) {
         throw new BuildException("Only one nested tokenizer allowed.");
      } else {
         this.tokenizer = tokenizer;
      }
   }

   protected synchronized void dieOnCircularReference(Stack stk, Project p) throws BuildException {
      if (!this.isChecked()) {
         if (this.isReference()) {
            super.dieOnCircularReference(stk, p);
         } else {
            if (this.tokenizer instanceof DataType) {
               stk.push(this.tokenizer);
               invokeCircularReferenceCheck((DataType)this.tokenizer, stk, p);
            }

            this.setChecked(true);
         }

      }
   }
}
