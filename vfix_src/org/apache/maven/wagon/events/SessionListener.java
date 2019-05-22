package org.apache.maven.wagon.events;

public interface SessionListener {
   void sessionOpening(SessionEvent var1);

   void sessionOpened(SessionEvent var1);

   void sessionDisconnecting(SessionEvent var1);

   void sessionDisconnected(SessionEvent var1);

   void sessionConnectionRefused(SessionEvent var1);

   void sessionLoggedIn(SessionEvent var1);

   void sessionLoggedOff(SessionEvent var1);

   void sessionError(SessionEvent var1);

   void debug(String var1);
}
