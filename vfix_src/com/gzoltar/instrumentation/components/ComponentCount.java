package com.gzoltar.instrumentation.components;

import com.gzoltar.shaded.org.apache.commons.lang3.builder.CompareToBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.Serializable;

public class ComponentCount implements Serializable, Comparable<ComponentCount> {
   private static final long serialVersionUID = 6484924431149038519L;
   private Component component;
   private int count;

   public ComponentCount(Component var1, int var2) {
      this.component = var1;
      this.count = var2;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int var1) {
      this.count = var1;
   }

   public Component getComponent() {
      return this.component;
   }

   public ComponentCount clone() {
      return new ComponentCount(this.component.clone(), this.count);
   }

   public int hashCode() {
      HashCodeBuilder var1;
      (var1 = new HashCodeBuilder()).append(this.component.hashCode());
      var1.append(this.count);
      return var1.toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof ComponentCount) {
         ComponentCount var3 = (ComponentCount)var1;
         EqualsBuilder var2;
         (var2 = new EqualsBuilder()).append((Object)this.component.getLabel(), (Object)var3.component.getLabel());
         var2.append(this.count, var3.count);
         return var2.isEquals();
      } else {
         return false;
      }
   }

   public int compareTo(ComponentCount var1) {
      if (var1 instanceof ComponentCount) {
         CompareToBuilder var2;
         (var2 = new CompareToBuilder()).append((Object)this.component.getLabel(), (Object)var1.component.getLabel());
         var2.append(this.count, var1.count);
         return var2.toComparison();
      } else {
         return -1;
      }
   }
}
