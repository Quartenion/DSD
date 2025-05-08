import java.util.ArrayList;
import java.io.Serializable;

// Clase utilizada para representar a los clientes
public class Cliente implements Serializable{
    private String nombre;
    private ArrayList<Float> donaciones;
    private float donacionTotal;
    private int numeroDonaciones;

    Cliente(String nombre)
    {
        this.nombre = nombre;
        this.donaciones = new ArrayList<>();
        this.donacionTotal = 0.0f;
        this.numeroDonaciones = 0;
    }

    public float getTotal()
    {
        return donacionTotal;
    }

    public int getNumDonaciones()
    {
        return numeroDonaciones;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void donar(float donacion)
    {
        this.donaciones.add(donacion);
        this.donacionTotal += donacion;
        this.incrementarNumeroDonaciones();
    }

    public void incrementarNumeroDonaciones()
    {
        this.numeroDonaciones++;
    }
}