package groovy.lang;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;

public abstract class GString extends GroovyObjectSupport implements Comparable, CharSequence, Writable, Buildable, Serializable {
   static final long serialVersionUID = -2638020355892246323L;
   public static final GString EMPTY = new GString(new Object[0]) {
      public String[] getStrings() {
         return new String[]{""};
      }
   };
   private Object[] values;

   public GString(Object values) {
      this.values = (Object[])((Object[])values);
   }

   public GString(Object[] values) {
      this.values = values;
   }

   public abstract String[] getStrings();

   public Object invokeMethod(String name, Object args) {
      try {
         return super.invokeMethod(name, args);
      } catch (MissingMethodException var4) {
         return InvokerHelper.invokeMethod(this.toString(), name, args);
      }
   }

   public Object[] getValues() {
      return this.values;
   }

   public GString plus(GString that) {
      List<String> stringList = new ArrayList();
      List<Object> valueList = new ArrayList();
      stringList.addAll(Arrays.asList(this.getStrings()));
      valueList.addAll(Arrays.asList(this.getValues()));
      List<String> thatStrings = Arrays.asList(that.getStrings());
      if (stringList.size() > valueList.size()) {
         thatStrings = new ArrayList((Collection)thatStrings);
         String s = (String)stringList.get(stringList.size() - 1);
         s = s + (String)((List)thatStrings).get(0);
         ((List)thatStrings).remove(0);
         stringList.set(stringList.size() - 1, s);
      }

      stringList.addAll((Collection)thatStrings);
      valueList.addAll(Arrays.asList(that.getValues()));
      final String[] newStrings = new String[stringList.size()];
      stringList.toArray(newStrings);
      Object[] newValues = valueList.toArray();
      return new GString(newValues) {
         public String[] getStrings() {
            return newStrings;
         }
      };
   }

   public GString plus(String that) {
      String[] currentStrings = this.getStrings();
      boolean appendToLastString = currentStrings.length > this.getValues().length;
      final String[] newStrings;
      if (appendToLastString) {
         newStrings = new String[currentStrings.length];
      } else {
         newStrings = new String[currentStrings.length + 1];
      }

      Object[] newValues = new Object[this.getValues().length];
      int lastIndex = currentStrings.length;
      System.arraycopy(currentStrings, 0, newStrings, 0, lastIndex);
      System.arraycopy(this.getValues(), 0, newValues, 0, this.getValues().length);
      if (appendToLastString) {
         newStrings[lastIndex - 1] = newStrings[lastIndex - 1] + that;
      } else {
         newStrings[lastIndex] = that;
      }

      return new GString(newValues) {
         public String[] getStrings() {
            return newStrings;
         }
      };
   }

   public int getValueCount() {
      return this.values.length;
   }

   public Object getValue(int idx) {
      return this.values[idx];
   }

   public String toString() {
      StringWriter buffer = new StringWriter();

      try {
         this.writeTo(buffer);
      } catch (IOException var3) {
         throw new StringWriterIOException(var3);
      }

      return buffer.toString();
   }

   public Writer writeTo(Writer out) throws IOException {
      String[] s = this.getStrings();
      int numberOfValues = this.values.length;
      int i = 0;

      for(int size = s.length; i < size; ++i) {
         out.write(s[i]);
         if (i < numberOfValues) {
            Object value = this.values[i];
            if (value instanceof Closure) {
               Closure c = (Closure)value;
               if (c.getMaximumNumberOfParameters() == 0) {
                  InvokerHelper.write(out, c.call((Object[])null));
               } else {
                  if (c.getMaximumNumberOfParameters() != 1) {
                     throw new GroovyRuntimeException("Trying to evaluate a GString containing a Closure taking " + c.getMaximumNumberOfParameters() + " parameters");
                  }

                  c.call(new Object[]{out});
               }
            } else {
               InvokerHelper.write(out, value);
            }
         }
      }

      return out;
   }

   public void build(GroovyObject builder) {
      String[] s = this.getStrings();
      int numberOfValues = this.values.length;
      int i = 0;

      for(int size = s.length; i < size; ++i) {
         builder.getProperty("mkp");
         builder.invokeMethod("yield", new Object[]{s[i]});
         if (i < numberOfValues) {
            builder.getProperty("mkp");
            builder.invokeMethod("yield", new Object[]{this.values[i]});
         }
      }

   }

   public boolean equals(Object that) {
      return that instanceof GString ? this.equals((GString)that) : false;
   }

   public boolean equals(GString that) {
      return this.toString().equals(that.toString());
   }

   public int hashCode() {
      return 37 + this.toString().hashCode();
   }

   public int compareTo(Object that) {
      return this.toString().compareTo(that.toString());
   }

   public char charAt(int index) {
      return this.toString().charAt(index);
   }

   public int length() {
      return this.toString().length();
   }

   public CharSequence subSequence(int start, int end) {
      return this.toString().subSequence(start, end);
   }

   public Pattern negate() {
      return DefaultGroovyMethods.bitwiseNegate(this.toString());
   }
}
