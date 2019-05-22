package polyglot.util;

public interface ErrorQueue {
   void enqueue(int var1, String var2);

   void enqueue(int var1, String var2, Position var3);

   void enqueue(ErrorInfo var1);

   void flush();

   boolean hasErrors();

   int errorCount();
}
