// Definición de umbrales permitidos para sensores
const UMBRALES = {
  temperaturaAire: { min: 19, max: 30 },     
  temperaturaCalor: { min: 13, max: 18 },
  luminosidad: { min: 50, max: 70 },      
  humedad: { min: 30, max: 70 } 
};

export function procesarMedida(tipo, valor, estado) {
  const acciones = [];
  const { actuadores, sensores } = estado;

  if (tipo === 'luminosidad') {
    if (valor > UMBRALES.luminosidad.max && actuadores.persiana !== 'cerrada') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Luminosidad demasiado alta! Cerrando persiana automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'persiana', estado: 'cerrada' }
      });
    } else if (valor < UMBRALES.luminosidad.min && actuadores.persiana !== 'abierta') {
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
    if (valor < UMBRALES.temperaturaAire.min && actuadores.aire !== 'apagado') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado baja! Apagando aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'apagado' }
      });
    } else if (valor > UMBRALES.temperaturaAire.max && actuadores.aire !== 'encendido') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura muy alta! Encendiendo aire acondicionado.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'aire', estado: 'encendido' }
      });
      if(actuadores.ventana !== 'cerrada') {
        acciones.push({
          tipo: 'alarma',
          mensaje: '¡La ventana está abierta y el aire está encendido! Cerrando ventana automáticamente.'
        });
        acciones.push({
          tipo: 'actuador',
          valor: { dispositivo: 'ventana', estado: 'cerrada' }
        });
      }
    }

    if (valor < UMBRALES.temperaturaCalor.min && actuadores.calor !== 'encendida') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Temperatura demasiado baja! Encendiendo calefacción.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'calor', estado: 'encendida' }
      });
      if(actuadores.ventana !== 'cerrada') {
        acciones.push({
          tipo: 'alarma',
          mensaje: '¡La ventana está abierta y la calefacción está encendida! Cerrando ventana automáticamente.'
        });
        acciones.push({
          tipo: 'actuador',
          valor: { dispositivo: 'ventana', estado: 'cerrada' }
        });
      }
    } else if (valor > UMBRALES.temperaturaCalor.max && actuadores.calor !== 'apagada') {
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

  if (tipo === 'humedad') {
    if (valor < UMBRALES.humedad.min && actuadores.riegue !== 'encendido' && !sensores.lluvia) {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Humedad demasiado baja! Encendiendo riego.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'riegue', estado: 'encendido' }
      });
    } else if (valor > UMBRALES.humedad.max && actuadores.riegue !== 'apagado') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Humedad demasiado alta! Apagando riego.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'riegue', estado: 'apagado' }
      });
    } else if (sensores.lluvia && actuadores.riegue !== 'apagado') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Está lloviendo! Apagando riego automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'riegue', estado: 'apagado' }
      });
    }
  }

  if (tipo === 'lluvia') {
    if (valor && actuadores.ventana !== 'cerrada') {
      acciones.push({
        tipo: 'alarma',
        mensaje: '¡Está lloviendo! Cerrando ventana automáticamente.'
      });
      acciones.push({
        tipo: 'actuador',
        valor: { dispositivo: 'ventana', estado: 'cerrada' }
      });
      if (actuadores.riegue !== 'apagado') {
        acciones.push({
          tipo: 'alarma',
          mensaje: '¡Está lloviendo! Apagando riego automáticamente.'
        });
        acciones.push({
          tipo: 'actuador',
          valor: { dispositivo: 'riegue', estado: 'apagado' }
        });
      }
    } else {
      if (!valor && actuadores.ventana !== 'abierta' && actuadores.aire !== 'encendido' && actuadores.calor !== 'encendida') {
        acciones.push({
          tipo: 'alarma',
          mensaje: '¡No está lloviendo! Abriendo ventana automáticamente.'
        });
        acciones.push({
          tipo: 'actuador',
          valor: { dispositivo: 'ventana', estado: 'abierta' }
        });
      }

      if (!valor && actuadores.riegue !== 'encendido' && estado.sensores.humedad < UMBRALES.humedad.min) {
        acciones.push({
          tipo: 'alarma',
          mensaje: '¡No está lloviendo! Encendiendo riego automáticamente.'
        });
        acciones.push({
          tipo: 'actuador',
          valor: { dispositivo: 'riegue', estado: 'encendido' }
        });
      }
    } 
  }

  return acciones;
}