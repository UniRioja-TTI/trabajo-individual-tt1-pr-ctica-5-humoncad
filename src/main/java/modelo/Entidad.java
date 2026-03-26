package modelo;

public class Entidad {
	private int id;
	private String name;
	private String descripcion;

	public Entidad(int id, String name) {
		this.id = id;
		this.name = name;
		this.descripcion = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
