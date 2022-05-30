package com.kineteco.error;

public class OrderStatsException extends RuntimeException {
   public OrderStatsException() {
   }

   public OrderStatsException(String message) {
      super(message);
   }

   public OrderStatsException(Throwable throwable) {
      super(throwable);
   }
}
