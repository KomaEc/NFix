package com.gzoltar.shaded.org.jacoco.report.check;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICounter;
import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Limit {
   private static final Map<ICounter.CounterValue, String> VALUE_NAMES;
   private static final Map<ICoverageNode.CounterEntity, String> ENTITY_NAMES;
   private ICoverageNode.CounterEntity entity;
   private ICounter.CounterValue value;
   private BigDecimal minimum;
   private BigDecimal maximum;

   public Limit() {
      this.entity = ICoverageNode.CounterEntity.INSTRUCTION;
      this.value = ICounter.CounterValue.COVEREDRATIO;
   }

   public ICoverageNode.CounterEntity getEntity() {
      return this.entity;
   }

   public void setCounter(String entity) {
      this.entity = ICoverageNode.CounterEntity.valueOf(entity);
   }

   public ICounter.CounterValue getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = ICounter.CounterValue.valueOf(value);
   }

   public String getMinimum() {
      return this.minimum == null ? null : this.minimum.toPlainString();
   }

   public void setMinimum(String minimum) {
      this.minimum = parseValue(minimum);
   }

   public String getMaximum() {
      return this.maximum == null ? null : this.maximum.toPlainString();
   }

   public void setMaximum(String maximum) {
      this.maximum = parseValue(maximum);
   }

   private static BigDecimal parseValue(String value) {
      if (value == null) {
         return null;
      } else {
         String trimmedValue = value.trim();
         if (trimmedValue.endsWith("%")) {
            String percent = trimmedValue.substring(0, trimmedValue.length() - 1);
            return (new BigDecimal(percent)).movePointLeft(2);
         } else {
            return new BigDecimal(trimmedValue);
         }
      }
   }

   String check(ICoverageNode node) {
      double d = node.getCounter(this.entity).getValue(this.value);
      if (Double.isNaN(d)) {
         return null;
      } else {
         BigDecimal bd = BigDecimal.valueOf(d);
         if (this.minimum != null && this.minimum.compareTo(bd) > 0) {
            return this.message("minimum", bd, this.minimum, RoundingMode.FLOOR);
         } else {
            return this.maximum != null && this.maximum.compareTo(bd) < 0 ? this.message("maximum", bd, this.maximum, RoundingMode.CEILING) : null;
         }
      }
   }

   private String message(String minmax, BigDecimal v, BigDecimal ref, RoundingMode mode) {
      BigDecimal rounded = v.setScale(ref.scale(), mode);
      return String.format("%s %s is %s, but expected %s is %s", ENTITY_NAMES.get(this.entity), VALUE_NAMES.get(this.value), rounded.toPlainString(), minmax, ref.toPlainString());
   }

   static {
      Map<ICounter.CounterValue, String> values = new HashMap();
      values.put(ICounter.CounterValue.TOTALCOUNT, "total count");
      values.put(ICounter.CounterValue.MISSEDCOUNT, "missed count");
      values.put(ICounter.CounterValue.COVEREDCOUNT, "covered count");
      values.put(ICounter.CounterValue.MISSEDRATIO, "missed ratio");
      values.put(ICounter.CounterValue.COVEREDRATIO, "covered ratio");
      VALUE_NAMES = Collections.unmodifiableMap(values);
      Map<ICoverageNode.CounterEntity, String> entities = new HashMap();
      entities.put(ICoverageNode.CounterEntity.INSTRUCTION, "instructions");
      entities.put(ICoverageNode.CounterEntity.BRANCH, "branches");
      entities.put(ICoverageNode.CounterEntity.COMPLEXITY, "complexity");
      entities.put(ICoverageNode.CounterEntity.LINE, "lines");
      entities.put(ICoverageNode.CounterEntity.METHOD, "methods");
      entities.put(ICoverageNode.CounterEntity.CLASS, "classes");
      ENTITY_NAMES = Collections.unmodifiableMap(entities);
   }
}
