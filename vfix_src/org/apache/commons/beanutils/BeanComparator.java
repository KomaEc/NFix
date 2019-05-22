package org.apache.commons.beanutils;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.collections.comparators.ComparableComparator;

public class BeanComparator implements Comparator, Serializable {
   private String property;
   private Comparator comparator;

   public BeanComparator() {
      this((String)null);
   }

   public BeanComparator(String property) {
      this(property, ComparableComparator.getInstance());
   }

   public BeanComparator(String property, Comparator comparator) {
      this.setProperty(property);
      this.comparator = comparator;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public String getProperty() {
      return this.property;
   }

   public Comparator getComparator() {
      return this.comparator;
   }

   public int compare(Object o1, Object o2) {
      if (this.property == null) {
         return this.comparator.compare(o1, o2);
      } else {
         try {
            Object value1 = PropertyUtils.getProperty(o1, this.property);
            Object value2 = PropertyUtils.getProperty(o2, this.property);
            return this.comparator.compare(value1, value2);
         } catch (Exception var5) {
            throw new ClassCastException(var5.toString());
         }
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof BeanComparator)) {
         return false;
      } else {
         BeanComparator beanComparator = (BeanComparator)o;
         if (!this.comparator.equals(beanComparator.comparator)) {
            return false;
         } else if (this.property != null) {
            return this.property.equals(beanComparator.property);
         } else {
            return beanComparator.property == null;
         }
      }
   }

   public int hashCode() {
      int result = this.comparator.hashCode();
      return result;
   }
}
