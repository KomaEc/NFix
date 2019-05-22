package org.testng;

public interface IExpectedExceptionsHolder {
   String getWrongExceptionMessage(Throwable var1);

   boolean isThrowableMatching(Throwable var1);
}
