package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;

public class SizeSelector extends BaseExtendSelector {
   public static final String SIZE_KEY = "value";
   public static final String UNITS_KEY = "units";
   public static final String WHEN_KEY = "when";
   private long size = -1L;
   private long multiplier = 1L;
   private long sizelimit = -1L;
   private Comparison when;

   public SizeSelector() {
      this.when = Comparison.EQUAL;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("{sizeselector value: ");
      buf.append(this.sizelimit);
      buf.append("compare: ").append(this.when.getValue());
      buf.append("}");
      return buf.toString();
   }

   public void setValue(long size) {
      this.size = size;
      if (this.multiplier != 0L && size > -1L) {
         this.sizelimit = size * this.multiplier;
      }

   }

   public void setUnits(SizeSelector.ByteUnits units) {
      int i = units.getIndex();
      this.multiplier = 0L;
      if (i > -1 && i < 4) {
         this.multiplier = 1000L;
      } else if (i > 3 && i < 9) {
         this.multiplier = 1024L;
      } else if (i > 8 && i < 13) {
         this.multiplier = 1000000L;
      } else if (i > 12 && i < 18) {
         this.multiplier = 1048576L;
      } else if (i > 17 && i < 22) {
         this.multiplier = 1000000000L;
      } else if (i > 21 && i < 27) {
         this.multiplier = 1073741824L;
      } else if (i > 26 && i < 31) {
         this.multiplier = 1000000000000L;
      } else if (i > 30 && i < 36) {
         this.multiplier = 1099511627776L;
      }

      if (this.multiplier > 0L && this.size > -1L) {
         this.sizelimit = this.size * this.multiplier;
      }

   }

   public void setWhen(SizeSelector.SizeComparisons when) {
      this.when = when;
   }

   public void setParameters(Parameter[] parameters) {
      super.setParameters(parameters);
      if (parameters != null) {
         for(int i = 0; i < parameters.length; ++i) {
            String paramname = parameters[i].getName();
            if ("value".equalsIgnoreCase(paramname)) {
               try {
                  this.setValue(new Long(parameters[i].getValue()));
               } catch (NumberFormatException var5) {
                  this.setError("Invalid size setting " + parameters[i].getValue());
               }
            } else if ("units".equalsIgnoreCase(paramname)) {
               SizeSelector.ByteUnits units = new SizeSelector.ByteUnits();
               units.setValue(parameters[i].getValue());
               this.setUnits(units);
            } else if ("when".equalsIgnoreCase(paramname)) {
               SizeSelector.SizeComparisons scmp = new SizeSelector.SizeComparisons();
               scmp.setValue(parameters[i].getValue());
               this.setWhen(scmp);
            } else {
               this.setError("Invalid parameter " + paramname);
            }
         }
      }

   }

   public void verifySettings() {
      if (this.size < 0L) {
         this.setError("The value attribute is required, and must be positive");
      } else if (this.multiplier < 1L) {
         this.setError("Invalid Units supplied, must be K,Ki,M,Mi,G,Gi,T,or Ti");
      } else if (this.sizelimit < 0L) {
         this.setError("Internal error: Code is not setting sizelimit correctly");
      }

   }

   public boolean isSelected(File basedir, String filename, File file) {
      this.validate();
      if (file.isDirectory()) {
         return true;
      } else {
         long diff = file.length() - this.sizelimit;
         return this.when.evaluate(diff == 0L ? 0 : (int)(diff / Math.abs(diff)));
      }
   }

   public static class SizeComparisons extends Comparison {
   }

   public static class ByteUnits extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"K", "k", "kilo", "KILO", "Ki", "KI", "ki", "kibi", "KIBI", "M", "m", "mega", "MEGA", "Mi", "MI", "mi", "mebi", "MEBI", "G", "g", "giga", "GIGA", "Gi", "GI", "gi", "gibi", "GIBI", "T", "t", "tera", "TERA", "Ti", "TI", "ti", "tebi", "TEBI"};
      }
   }
}
