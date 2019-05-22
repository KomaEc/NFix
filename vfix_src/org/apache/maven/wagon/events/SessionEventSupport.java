package org.apache.maven.wagon.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SessionEventSupport {
   private final List listeners = new ArrayList();

   public void addSessionListener(SessionListener listener) {
      if (listener != null) {
         this.listeners.add(listener);
      }

   }

   public void removeSessionListener(SessionListener listener) {
      this.listeners.remove(listener);
   }

   public boolean hasSessionListener(SessionListener listener) {
      return this.listeners.contains(listener);
   }

   public void fireSessionDisconnected(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionDisconnected(sessionEvent);
      }

   }

   public void fireSessionDisconnecting(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionDisconnecting(sessionEvent);
      }

   }

   public void fireSessionLoggedIn(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionLoggedIn(sessionEvent);
      }

   }

   public void fireSessionLoggedOff(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionLoggedOff(sessionEvent);
      }

   }

   public void fireSessionOpened(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionOpened(sessionEvent);
      }

   }

   public void fireSessionOpening(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionOpening(sessionEvent);
      }

   }

   public void fireSessionConnectionRefused(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionConnectionRefused(sessionEvent);
      }

   }

   public void fireDebug(String message) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.debug(message);
      }

   }

   public void fireSessionError(SessionEvent sessionEvent) {
      Iterator iter = this.listeners.iterator();

      while(iter.hasNext()) {
         SessionListener listener = (SessionListener)iter.next();
         listener.sessionError(sessionEvent);
      }

   }
}
