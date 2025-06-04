package fr.middlewaresolutions.demo.handlers;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.json.JSONObject;

/**
 * Provides a handler to Quarkus for any uncaught error.
 */
@Provider
public class ThrowableHandler implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable e) {
    Log.error("Uncaught exception", e);

    // Build nice error
    JSONObject error = new JSONObject();
    error.put("type", e.getClass().getName());
    error.put("message", e.getMessage());

    // Return it
    return Response.status(Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON)
            .entity(error.toString())
            .build();
  }
}
