package org.apache.commons.beanutils;

public class ConvertUtils {
   /** @deprecated */
   public static boolean getDefaultBoolean() {
      return ConvertUtilsBean.getInstance().getDefaultBoolean();
   }

   /** @deprecated */
   public static void setDefaultBoolean(boolean newDefaultBoolean) {
      ConvertUtilsBean.getInstance().setDefaultBoolean(newDefaultBoolean);
   }

   /** @deprecated */
   public static byte getDefaultByte() {
      return ConvertUtilsBean.getInstance().getDefaultByte();
   }

   /** @deprecated */
   public static void setDefaultByte(byte newDefaultByte) {
      ConvertUtilsBean.getInstance().setDefaultByte(newDefaultByte);
   }

   /** @deprecated */
   public static char getDefaultCharacter() {
      return ConvertUtilsBean.getInstance().getDefaultCharacter();
   }

   /** @deprecated */
   public static void setDefaultCharacter(char newDefaultCharacter) {
      ConvertUtilsBean.getInstance().setDefaultCharacter(newDefaultCharacter);
   }

   /** @deprecated */
   public static double getDefaultDouble() {
      return ConvertUtilsBean.getInstance().getDefaultDouble();
   }

   /** @deprecated */
   public static void setDefaultDouble(double newDefaultDouble) {
      ConvertUtilsBean.getInstance().setDefaultDouble(newDefaultDouble);
   }

   /** @deprecated */
   public static float getDefaultFloat() {
      return ConvertUtilsBean.getInstance().getDefaultFloat();
   }

   /** @deprecated */
   public static void setDefaultFloat(float newDefaultFloat) {
      ConvertUtilsBean.getInstance().setDefaultFloat(newDefaultFloat);
   }

   /** @deprecated */
   public static int getDefaultInteger() {
      return ConvertUtilsBean.getInstance().getDefaultInteger();
   }

   /** @deprecated */
   public static void setDefaultInteger(int newDefaultInteger) {
      ConvertUtilsBean.getInstance().setDefaultInteger(newDefaultInteger);
   }

   /** @deprecated */
   public static long getDefaultLong() {
      return ConvertUtilsBean.getInstance().getDefaultLong();
   }

   /** @deprecated */
   public static void setDefaultLong(long newDefaultLong) {
      ConvertUtilsBean.getInstance().setDefaultLong(newDefaultLong);
   }

   /** @deprecated */
   public static short getDefaultShort() {
      return ConvertUtilsBean.getInstance().getDefaultShort();
   }

   /** @deprecated */
   public static void setDefaultShort(short newDefaultShort) {
      ConvertUtilsBean.getInstance().setDefaultShort(newDefaultShort);
   }

   public static String convert(Object value) {
      return ConvertUtilsBean.getInstance().convert(value);
   }

   public static Object convert(String value, Class clazz) {
      return ConvertUtilsBean.getInstance().convert(value, clazz);
   }

   public static Object convert(String[] values, Class clazz) {
      return ConvertUtilsBean.getInstance().convert(values, clazz);
   }

   public static void deregister() {
      ConvertUtilsBean.getInstance().deregister();
   }

   public static void deregister(Class clazz) {
      ConvertUtilsBean.getInstance().deregister(clazz);
   }

   public static Converter lookup(Class clazz) {
      return ConvertUtilsBean.getInstance().lookup(clazz);
   }

   public static void register(Converter converter, Class clazz) {
      ConvertUtilsBean.getInstance().register(converter, clazz);
   }
}
