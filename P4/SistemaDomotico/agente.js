// agente.js

const UMBRALES = {
  temperatura: { min: 18, max: 36 },     
  luminosidad: { min: 50, max: 70 }
};

// Se añade estadoActual como tercer parámetro
export function procesarMedida(tipo, valor, estadoActual) {
  const acciones = [];

  if (tipo === 'luminosidad') {
    if (valor > UMBRALES.luminosidad.max && estadoActual.persiana !== 'cerrada') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Luminosidad demasiado alta! Cerrando persiana automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'persiana', estado: 'cerrada' }
      });
    } else if (valor < UMBRALES.luminosidad.min && estadoActual.persiana !== 'abierta') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Luminosidad demasiado baja! Subiendo persiana automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'persiana', estado: 'abierta' }
      });
    }
  }

  if (tipo === 'temperatura') {
    if (valor < UMBRALES.temperatura.min && estadoActual.aire !== 'apagado') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado baja! Apagando aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'apagado' }
      });
    } else if (valor > UMBRALES.temperatura.max && estadoActual.aire !== 'encendido') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura muy alta! Encendiendo aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'encendido' }
      });
    }
  }

  return acciones;
}
