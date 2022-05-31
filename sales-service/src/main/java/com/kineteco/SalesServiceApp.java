package com.kineteco;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.event.Observes;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(tags = {
      @Tag(name = "sales", description = "Sales operations.")
}, info =
@Info(title = "Sales Service", version = "1.0", description = "Sales operations.")
)
public class SalesServiceApp extends Application {

   void onStart(@Observes StartupEvent ev) {
      Log.info("Sales Service is starting Powered by Quarkus");
      Log.info("  _   _   _   _   _   _   _   _");
      Log.info(" / \\ / \\ / \\ / \\ / \\ / \\ / \\ / \\");
      Log.info("( K | i | n | e | t | e | c | o )");
      Log.info(" \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/ \\_/");
   }

   void onStop(@Observes ShutdownEvent ev) {
      Log.info("Sales Service shutting down...");
   }

}
