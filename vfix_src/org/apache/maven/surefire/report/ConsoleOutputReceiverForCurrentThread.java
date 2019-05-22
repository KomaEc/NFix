package org.apache.maven.surefire.report;

public final class ConsoleOutputReceiverForCurrentThread {
   private static final ThreadLocal<ConsoleOutputReceiver> current = new InheritableThreadLocal();

   private ConsoleOutputReceiverForCurrentThread() {
   }

   public static ConsoleOutputReceiver get() {
      return (ConsoleOutputReceiver)current.get();
   }

   public static void set(ConsoleOutputReceiver consoleOutputReceiver) {
      current.set(consoleOutputReceiver);
   }

   public static void remove() {
      current.remove();
   }
}
