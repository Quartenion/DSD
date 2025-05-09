import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable {
    private final String nombre;
    private final ArrayList<Float> donaciones = new ArrayList<>();
    private float donacionTotal = 0.0f;
    private int numeroDonaciones = 0;

    Cliente(String nombre) {
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
        donaciones.add(donacion);
        donacionTotal += donacion;
        incrementarDonaciones();
    }

    public void incrementarDonaciones() {
        numeroDonaciones++;
    }
}
