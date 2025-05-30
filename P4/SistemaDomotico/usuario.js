// Envía un comando de control manual al servidor
function controlar(dispositivo, estado) {
  socket.emit('control-actuador', { dispositivo, estado });
}

const socket = io(); // Conexión al servidor mediante WebSockets

// Escucha las actualizaciones de sensores desde el servidor
socket.on('actualizar-sensores', ({ tipo, valor }) => {
  if (tipo === 'temperatura') {
    document.getElementById('tempValor').textContent = valor;
  } else if (tipo === 'luminosidad') {
    document.getElementById('luzValor').textContent = valor;
  } else if (tipo === 'humedad') {
    document.getElementById('humedadValor').textContent = valor;
  }
  else if (tipo === 'lluvia') {
    document.getElementById('lluviaValor').textContent = valor ? 'Sí' : 'No';
  }
});

// Escucha cambios en el estado de los actuadores
socket.on('actualizar-actuadores', (estado) => {
  // Actualiza texto
  document.getElementById('persianaEstado').textContent = estado.persiana;
  document.getElementById('aireEstado').textContent = estado.aire;
  document.getElementById('calorEstado').textContent = estado.calor;
  document.getElementById('ventanaEstado').textContent = estado.ventana;
  document.getElementById('riegueEstado').textContent = estado.riegue;

  // Actualiza icono de la persiana
  const persianaIcono = document.getElementById('persianaIcono');
  if (estado.persiana === 'abierta') {
    persianaIcono.src = 'img/persiana_abierta.png';
  } else {
    persianaIcono.src = 'img/persiana_cerrada.png';
  }

  // Actualiza icono del aire acondicionado
  const aireIcono = document.getElementById('aireIcono');
  if (estado.aire === 'encendido') {
    aireIcono.src = 'img/aire_encendido.png';
  } else {
    aireIcono.src = 'img/aire_apagado.png';
  }

  // Actualiza icono de la calefacción
  const calorIcono = document.getElementById('calorIcono');
  if (estado.calor === 'encendida') {
    calorIcono.src = 'img/calor_encendido.png';
  } else {
    calorIcono.src = 'img/calor_apagado.png';
  }

  // Actualiza icono de la ventana
  const ventanaIcono = document.getElementById('ventanaIcono');
  if (estado.ventana === 'abierta') {
    ventanaIcono.src = 'img/ventana_abierta.png';
  } else {
    ventanaIcono.src = 'img/ventana_cerrada.png';
  }

  // Actualiza icono del riegue
  const riegueIcono = document.getElementById('riegueIcono');
  if (estado.riegue === 'encendido') {
    riegueIcono.src = 'img/riegue_encendido.png';
  } else {
    riegueIcono.src = 'img/riegue_apagado.png';
  }
});

// Recibe y muestra mensajes de alerta generados por el agente inteligente
socket.on('alarma', (mensaje) => {
  const zona = document.getElementById('notificaciones');
  const alerta = document.createElement('p');
  alerta.textContent = mensaje;
  alerta.classList.add('alerta');

  zona.appendChild(alerta);

  // Espera 9 segundos antes de empezar a desvanecer el mensaje
  setTimeout(() => {
    alerta.classList.add('fade-out');
  }, 9000);

  // Espera 10 segundos antes de eliminar el mensaje del DOM
  setTimeout(() => {
    zona.removeChild(alerta);
  }, 10000);
});

// Al conectarse, se recibe el estado inicial del sistema
socket.on('estado-inicial', (estado) => {
  document.getElementById('tempValor').textContent = estado.sensores.temperatura ?? '--';
  document.getElementById('luzValor').textContent = estado.sensores.luminosidad ?? '--';
  document.getElementById('persianaEstado').textContent = estado.actuadores.persiana;
  document.getElementById('aireEstado').textContent = estado.actuadores.aire;
  document.getElementById('calorEstado').textContent = estado.actuadores.calor;
  document.getElementById('ventanaEstado').textContent = estado.actuadores.ventana;
  document.getElementById('riegueEstado').textContent = estado.actuadores.riegue;
});

// Recibe y muestra el historial de eventos del sistema
socket.on('historial', (eventos) => {
  const lista = document.getElementById('historial');
  lista.innerHTML = '';

  eventos.forEach(evt => {
    const item = document.createElement('li');
    const fecha = new Date(evt.timestamp).toLocaleString();
    item.textContent = `[${fecha}] ${evt.tipo}: ${evt.valor}`;
    lista.appendChild(item);
  });
});
