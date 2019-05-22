package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.cli;

import java.util.concurrent.Callable;

public interface CommandLineCallable extends Callable<Integer> {
   Integer call() throws CommandLineException;
}
