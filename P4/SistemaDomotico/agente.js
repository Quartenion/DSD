// Definición de umbrales permitidos para sensores
const UMBRALES = {
  temperaturaAire: { min: 19, max: 36 },     // Rango aceptable de temperatura en °C
  temperaturaCalor: { min: 13, max: 18 },
  luminosidad: { min: 50, max: 70 }      // Rango aceptable de luminosidad (unidad arbitraria)
};

export function procesarMedida(tipo, valor, estadoActual) {
  const acciones = []; // Lista de acciones a devolver

  // Lógica para luminosidad
  if (tipo === 'luminosidad') {
    // Si la luminosidad es demasiado alta y la persiana no está cerrada, se actúa
    if (valor > UMBRALES.luminosidad.max && estadoActual.persiana !== 'cerrada') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Luminosidad demasiado alta! Cerrando persiana automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'persiana', estado: 'cerrada' }
      });
    }
    // Si la luminosidad es demasiado baja y la persiana no está abierta, se actúa
    else if (valor < UMBRALES.luminosidad.min && estadoActual.persiana !== 'abierta') {
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

  // Lógica para temperatura
  if (tipo === 'temperatura') {
    // Si la temperatura es muy baja y el aire aún está encendido, se apaga
    if (valor < UMBRALES.temperaturaAire.min && estadoActual.aire !== 'apagado') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado baja! Apagando aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'apagado' }
      });
    }
    // Si la temperatura es muy alta y el aire aún no está encendido, se enciende
    else if (valor > UMBRALES.temperaturaAire.max && estadoActual.aire !== 'encendido') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura muy alta! Encendiendo aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'encendido' }
      });
    }
    if (valor < UMBRALES.temperaturaCalor.min && estadoActual.calor !== 'encendida') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado baja! Encendiendo calefacción.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'calor', estado: 'encendida' }
      });
    }
    else if (valor > UMBRALES.temperaturaCalor.max && estadoActual.calor !== 'apagada') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado alta! Apagando calefacción.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'calor', estado: 'apagada' }
      });
    }
  }
  // Devuelve todas las acciones sugeridas por el agente
  return acciones;
}
