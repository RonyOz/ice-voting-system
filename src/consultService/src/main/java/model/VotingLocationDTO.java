package model;

public class VotingLocationDTO {
  private String nombrePuesto;
  private String direccion;
  private String ciudad;
  private String departamento;
  private int numeroMesa;

  public VotingLocationDTO(String nombrePuesto, String direccion, String ciudad, String departamento, int numeroMesa) {
    this.nombrePuesto = nombrePuesto;
    this.direccion = direccion;
    this.ciudad = ciudad;
    this.departamento = departamento;
    this.numeroMesa = numeroMesa;
  }

  // Getters
  public String getNombrePuesto() {
    return nombrePuesto;
  }

  public String getDireccion() {
    return direccion;
  }

  public String getCiudad() {
    return ciudad;
  }

  public String getDepartamento() {
    return departamento;
  }

  public int getNumeroMesa() {
    return numeroMesa;
  }

  @Override
  public String toString() {
    return String.format("Puesto: %s, Dirección: %s, Ciudad: %s, Departamento: %s, Mesa: %d",
        nombrePuesto, direccion, ciudad, departamento, numeroMesa);
  }
}
