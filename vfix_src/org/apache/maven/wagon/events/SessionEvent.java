package org.apache.maven.wagon.events;

import org.apache.maven.wagon.Wagon;

public class SessionEvent extends WagonEvent {
   public static final int SESSION_CLOSED = 1;
   public static final int SESSION_DISCONNECTING = 2;
   public static final int SESSION_DISCONNECTED = 3;
   public static final int SESSION_CONNECTION_REFUSED = 4;
   public static final int SESSION_OPENING = 5;
   public static final int SESSION_OPENED = 6;
   public static final int SESSION_LOGGED_IN = 7;
   public static final int SESSION_LOGGED_OFF = 8;
   public static final int SESSION_ERROR_OCCURRED = 9;
   private int eventType;
   private Exception exception;

   public SessionEvent(Wagon wagon, int eventType) {
      super(wagon);
      this.eventType = eventType;
   }

   public SessionEvent(Wagon wagon, Exception exception) {
      super(wagon);
      this.exception = exception;
      this.eventType = 9;
   }

   public int getEventType() {
      return this.eventType;
   }

   public Exception getException() {
      return this.exception;
   }

   public void setEventType(int eventType) {
      switch(eventType) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
         this.eventType = eventType;
         return;
      default:
         throw new IllegalArgumentException("Illegal event type: " + eventType);
      }
   }

   public void setException(Exception exception) {
      this.exception = exception;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("SessionEvent[");
      switch(this.eventType) {
      case 1:
         sb.append("CONNECTION_CLOSED");
         break;
      case 2:
         sb.append("CONNECTION_DISCONNECTING");
         break;
      case 3:
         sb.append("CONNECTION_DISCONNECTED");
         break;
      case 4:
         sb.append("CONNECTION_CONNECTION_REFUSED");
         break;
      case 5:
         sb.append("CONNECTION_OPENING");
         break;
      case 6:
         sb.append("CONNECTION_OPENED");
         break;
      case 7:
         sb.append("CONNECTION_LOGGED_IN");
         break;
      case 8:
         sb.append("CONNECTION_LOGGED_OFF");
         break;
      case 9:
         sb.append("CONNECTION_ERROR_OCCURRED");
         break;
      default:
         sb.append(this.eventType);
      }

      sb.append("|");
      sb.append(this.getWagon().getRepository()).append("|");
      sb.append(this.source);
      if (this.exception != null) {
         sb.append("|");
         sb.append(this.exception.getClass().getName()).append(":");
         sb.append(this.exception.getMessage());
      }

      sb.append("]");
      return sb.toString();
   }
}
