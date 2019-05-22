package org.apache.maven.wagon.events;

import java.io.File;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.resource.Resource;

public class TransferEvent extends WagonEvent {
   public static final int TRANSFER_INITIATED = 0;
   public static final int TRANSFER_STARTED = 1;
   public static final int TRANSFER_COMPLETED = 2;
   public static final int TRANSFER_PROGRESS = 3;
   public static final int TRANSFER_ERROR = 4;
   public static final int REQUEST_GET = 5;
   public static final int REQUEST_PUT = 6;
   private Resource resource;
   private int eventType;
   private int requestType;
   private Exception exception;
   private File localFile;

   public TransferEvent(Wagon wagon, Resource resource, int eventType, int requestType) {
      super(wagon);
      this.resource = resource;
      this.setEventType(eventType);
      this.setRequestType(requestType);
   }

   public TransferEvent(Wagon wagon, Resource resource, Exception exception, int requestType) {
      this(wagon, resource, 4, requestType);
      this.exception = exception;
   }

   public Resource getResource() {
      return this.resource;
   }

   public Exception getException() {
      return this.exception;
   }

   public int getRequestType() {
      return this.requestType;
   }

   public void setRequestType(int requestType) {
      switch(requestType) {
      case 5:
      case 6:
         this.requestType = requestType;
         return;
      default:
         throw new IllegalArgumentException("Illegal request type: " + requestType);
      }
   }

   public int getEventType() {
      return this.eventType;
   }

   public void setEventType(int eventType) {
      switch(eventType) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
         this.eventType = eventType;
         return;
      default:
         throw new IllegalArgumentException("Illegal event type: " + eventType);
      }
   }

   public void setResource(Resource resource) {
      this.resource = resource;
   }

   public File getLocalFile() {
      return this.localFile;
   }

   public void setLocalFile(File localFile) {
      this.localFile = localFile;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("TransferEvent[");
      switch(this.getRequestType()) {
      case 5:
         sb.append("GET");
         break;
      case 6:
         sb.append("PUT");
         break;
      default:
         sb.append(this.getRequestType());
      }

      sb.append("|");
      switch(this.getEventType()) {
      case 0:
         sb.append("INITIATED");
         break;
      case 1:
         sb.append("STARTED");
         break;
      case 2:
         sb.append("COMPLETED");
         break;
      case 3:
         sb.append("PROGRESS");
         break;
      case 4:
         sb.append("ERROR");
         break;
      default:
         sb.append(this.getEventType());
      }

      sb.append("|");
      sb.append(this.getWagon().getRepository()).append("|");
      sb.append(this.getLocalFile()).append("|");
      sb.append(this.getResource().inspect());
      sb.append("]");
      return sb.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.eventType;
      result = 31 * result + (this.exception == null ? 0 : this.exception.hashCode());
      result = 31 * result + (this.localFile == null ? 0 : this.localFile.hashCode());
      result = 31 * result + this.requestType;
      result = 31 * result + (this.resource == null ? 0 : this.resource.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj != null && this.getClass() == obj.getClass()) {
         TransferEvent other = (TransferEvent)obj;
         if (this.eventType != other.eventType) {
            return false;
         } else {
            if (this.exception == null) {
               if (other.exception != null) {
                  return false;
               }
            } else if (!this.exception.getClass().equals(other.exception.getClass())) {
               return false;
            }

            if (this.requestType != other.requestType) {
               return false;
            } else {
               if (this.resource == null) {
                  if (other.resource != null) {
                     return false;
                  }
               } else {
                  if (!this.resource.equals(other.resource)) {
                     return false;
                  }

                  if (!this.source.equals(other.source)) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }
}
