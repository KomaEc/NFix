package org.testng.internal;

public class EclipseInterface {
   public static final Character OPENING_CHARACTER = '[';
   public static final Character CLOSING_CHARACTER = ']';
   public static final String ASSERT_LEFT;
   public static final String ASSERT_LEFT2;
   public static final String ASSERT_MIDDLE;
   public static final String ASSERT_RIGHT;

   static {
      ASSERT_LEFT = "expected " + OPENING_CHARACTER;
      ASSERT_LEFT2 = "expected not same " + OPENING_CHARACTER;
      ASSERT_MIDDLE = CLOSING_CHARACTER + " but found " + OPENING_CHARACTER;
      ASSERT_RIGHT = Character.toString(CLOSING_CHARACTER);
   }
}
