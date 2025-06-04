package fr.middlewaresolutions.demo.repositories;

import fr.middlewaresolutions.demo.models.Client;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Repository for {@link Client Clients}.
 */
@ApplicationScoped
public class ClientsRepository implements PanacheRepository<Client> {

  @ConfigProperty(name = "demo.clients-limit")
  Integer defaultLimit;

  /**
   * Find a {@link Client} using an email.
   * @param email non-null email to search with.
   * @return {@code null} if no client could be found.
   */
  public @Nullable Client findByEmail(@NotNull String email) {
    return find("SELECT cl FROM Client cl WHERE cl.email = ?1", email).firstResult();
  }

  /**
   * Find all clients, using pagination.
   * @param rawPage optional page index.
   * @param rawSize optional page size.
   * @return a non-null list of clients.
   */
  public @NotNull List<Client> findAll(@Nullable Integer rawPage, @Nullable Integer rawSize) {
    int page = Objects.requireNonNullElse(rawPage, 0);
    int limit = Objects.requireNonNullElse(rawSize, defaultLimit);
    return find("SELECT cl FROM Client cl").page(page, limit).list();
  }

}
