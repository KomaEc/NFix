package org.apache.velocity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.resource.Resource;

public class Template extends Resource {
   private VelocityException errorCondition = null;

   public boolean process() throws ResourceNotFoundException, ParseErrorException, IOException {
      this.data = null;
      InputStream is = null;
      this.errorCondition = null;

      try {
         is = this.resourceLoader.getResourceStream(this.name);
      } catch (ResourceNotFoundException var16) {
         this.errorCondition = var16;
         throw var16;
      }

      if (is != null) {
         boolean var17;
         try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, this.encoding));
            this.data = this.rsvc.parse(br, this.name);
            this.initDocument();
            var17 = true;
         } catch (UnsupportedEncodingException var11) {
            String msg = "Template.process : Unsupported input encoding : " + this.encoding + " for template " + this.name;
            this.errorCondition = new ParseErrorException(msg);
            throw this.errorCondition;
         } catch (ParseException var12) {
            this.errorCondition = new ParseErrorException(var12);
            throw this.errorCondition;
         } catch (TemplateInitException var13) {
            this.errorCondition = new ParseErrorException(var13);
            throw this.errorCondition;
         } catch (RuntimeException var14) {
            throw var14;
         } finally {
            is.close();
         }

         return var17;
      } else {
         this.errorCondition = new ResourceNotFoundException("Unknown resource error for resource " + this.name);
         throw this.errorCondition;
      }
   }

   public void initDocument() throws TemplateInitException {
      InternalContextAdapterImpl ica = new InternalContextAdapterImpl(new VelocityContext());

      try {
         ica.pushCurrentTemplateName(this.name);
         ((SimpleNode)this.data).init(ica, this.rsvc);
      } finally {
         ica.popCurrentTemplateName();
      }

   }

   public void merge(Context context, Writer writer) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException {
      if (this.errorCondition != null) {
         throw this.errorCondition;
      } else if (this.data != null) {
         InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);

         try {
            ica.pushCurrentTemplateName(this.name);
            ica.setCurrentResource(this);
            ((SimpleNode)this.data).render(ica, writer);
         } finally {
            ica.popCurrentTemplateName();
            ica.setCurrentResource((Resource)null);
         }

      } else {
         String msg = "Template.merge() failure. The document is null, most likely due to parsing error.";
         throw new RuntimeException(msg);
      }
   }
}
