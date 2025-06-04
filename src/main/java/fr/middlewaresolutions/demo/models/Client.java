package fr.middlewaresolutions.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * Simple client representation.
 */
@Entity
@Getter @Setter
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Basic
  private String firstName;

  @Basic(optional = false)
  private String lastName;


  @Override
  public @NotNull String toString() {
    return "[" + email + "]";
  }
}
