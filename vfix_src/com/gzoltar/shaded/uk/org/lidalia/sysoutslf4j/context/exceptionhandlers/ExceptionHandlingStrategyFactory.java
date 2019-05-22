package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.LogLevel;
import java.io.PrintStream;

public interface ExceptionHandlingStrategyFactory {
   ExceptionHandlingStrategy makeExceptionHandlingStrategy(LogLevel var1, PrintStream var2);
}
