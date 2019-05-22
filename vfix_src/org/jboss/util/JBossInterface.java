package org.jboss.util;

public interface JBossInterface extends Cloneable {
   Object clone();

   String toShortString();

   void toShortString(JBossStringBuilder var1);
}
