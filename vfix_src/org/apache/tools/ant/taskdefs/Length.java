package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.util.PropertyOutputStream;

public class Length extends Task implements Condition {
   private static final String ALL = "all";
   private static final String EACH = "each";
   private static final String STRING = "string";
   private static final String LENGTH_REQUIRED = "Use of the Length condition requires that the length attribute be set.";
   private String property;
   private String string;
   private Boolean trim;
   private String mode = "all";
   private Comparison when;
   private Long length;
   private Resources resources;

   public Length() {
      this.when = Comparison.EQUAL;
   }

   public synchronized void setProperty(String property) {
      this.property = property;
   }

   public synchronized void setFile(File file) {
      this.add((ResourceCollection)(new FileResource(file)));
   }

   public synchronized void add(FileSet fs) {
      this.add((ResourceCollection)fs);
   }

   public synchronized void add(ResourceCollection c) {
      if (c != null) {
         this.resources = this.resources == null ? new Resources() : this.resources;
         this.resources.add(c);
      }
   }

   public synchronized void setLength(long ell) {
      this.length = new Long(ell);
   }

   public synchronized void setWhen(Length.When w) {
      this.setWhen((Comparison)w);
   }

   public synchronized void setWhen(Comparison c) {
      this.when = c;
   }

   public synchronized void setMode(Length.FileMode m) {
      this.mode = m.getValue();
   }

   public synchronized void setString(String string) {
      this.string = string;
      this.mode = "string";
   }

   public synchronized void setTrim(boolean trim) {
      this.trim = trim ? Boolean.TRUE : Boolean.FALSE;
   }

   public boolean getTrim() {
      return this.trim != null && this.trim;
   }

   public void execute() {
      this.validate();
      PrintStream ps = new PrintStream((OutputStream)(this.property != null ? new PropertyOutputStream(this.getProject(), this.property) : new LogOutputStream(this, 2)));
      if ("string".equals(this.mode)) {
         ps.print(getLength(this.string, this.getTrim()));
         ps.close();
      } else if ("each".equals(this.mode)) {
         this.handleResources(new Length.EachHandler(ps));
      } else if ("all".equals(this.mode)) {
         this.handleResources(new Length.AllHandler(ps));
      }

   }

   public boolean eval() {
      this.validate();
      if (this.length == null) {
         throw new BuildException("Use of the Length condition requires that the length attribute be set.");
      } else {
         Long ell = null;
         if ("string".equals(this.mode)) {
            ell = new Long(getLength(this.string, this.getTrim()));
         } else {
            Length.ConditionHandler h = new Length.ConditionHandler();
            this.handleResources(h);
            ell = new Long(h.getLength());
         }

         return this.when.evaluate(ell.compareTo(this.length));
      }
   }

   private void validate() {
      if (this.string != null) {
         if (this.resources != null) {
            throw new BuildException("the string length function is incompatible with the file/resource length function");
         }

         if (!"string".equals(this.mode)) {
            throw new BuildException("the mode attribute is for use with the file/resource length function");
         }
      } else {
         if (this.resources == null) {
            throw new BuildException("you must set either the string attribute or specify one or more files using the file attribute or nested resource collections");
         }

         if (!"each".equals(this.mode) && !"all".equals(this.mode)) {
            throw new BuildException("invalid mode setting for file/resource length function: \"" + this.mode + "\"");
         }

         if (this.trim != null) {
            throw new BuildException("the trim attribute is for use with the string length function only");
         }
      }

   }

   private void handleResources(Length.Handler h) {
      Iterator i = this.resources.iterator();

      while(i.hasNext()) {
         Resource r = (Resource)i.next();
         if (!r.isExists()) {
            this.log(r + " does not exist", 0);
         } else if (r.isDirectory()) {
            this.log(r + " is a directory; length unspecified", 0);
         } else {
            h.handle(r);
         }
      }

      h.complete();
   }

   private static long getLength(String s, boolean t) {
      return (long)(t ? s.trim() : s).length();
   }

   private class ConditionHandler extends Length.AllHandler {
      ConditionHandler() {
         super((PrintStream)null);
      }

      void complete() {
      }

      long getLength() {
         return this.getAccum();
      }
   }

   private class AllHandler extends Length.Handler {
      private long accum = 0L;

      AllHandler(PrintStream ps) {
         super(ps);
      }

      protected long getAccum() {
         return this.accum;
      }

      protected synchronized void handle(Resource r) {
         long size = r.getSize();
         if (size == -1L) {
            Length.this.log("Size unknown for " + r.toString(), 1);
         } else {
            this.accum += size;
         }

      }

      void complete() {
         this.getPs().print(this.accum);
         super.complete();
      }
   }

   private class EachHandler extends Length.Handler {
      EachHandler(PrintStream ps) {
         super(ps);
      }

      protected void handle(Resource r) {
         this.getPs().print(r.toString());
         this.getPs().print(" : ");
         long size = r.getSize();
         if (size == -1L) {
            this.getPs().println("unknown");
         } else {
            this.getPs().println(size);
         }

      }
   }

   private abstract class Handler {
      private PrintStream ps;

      Handler(PrintStream ps) {
         this.ps = ps;
      }

      protected PrintStream getPs() {
         return this.ps;
      }

      protected abstract void handle(Resource var1);

      void complete() {
         this.ps.close();
      }
   }

   public static class When extends Comparison {
   }

   public static class FileMode extends EnumeratedAttribute {
      static final String[] MODES = new String[]{"each", "all"};

      public String[] getValues() {
         return MODES;
      }
   }
}
