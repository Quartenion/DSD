import java.io.Serializable;

public class Entidad implements Serializable {
    private final String nombre;
    private float donacionTotal = 0.0f;
    private int numeroDonaciones = 0;

    Entidad(String nombre) {
        this.nombre = nombre;
    }

    public float getTotal() {
        return donacionTotal;
    }

    public int getNumDonaciones() {
        return numeroDonaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void donar(float donacion) {
        donacionTotal += donacion;
        incrementarDonaciones();
    }

    public void incrementarDonaciones() {
        numeroDonaciones++;
    }
}