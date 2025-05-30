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
  }
});

// Escucha cambios en el estado de los actuadores
socket.on('actualizar-actuadores', (estado) => {
  // Actualiza texto
  document.getElementById('persianaEstado').textContent = estado.persiana;
  document.getElementById('aireEstado').textContent = estado.aire;
  document.getElementById('calorEstado').textContent = estado.calor;

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
});

// Recibe y muestra mensajes de alerta generados por el agente inteligente
socket.on('alarma', (mensaje) => {
  const zona = document.getElementById('notificaciones');
  const alerta = document.createElement('p');
  alerta.textContent = mensaje;
  alerta.classList.add('alerta');

  zona.appendChild(alerta);

  // Espera 3 segundos antes de empezar a desvanecer el mensaje
  setTimeout(() => {
    alerta.classList.add('fade-out');
  }, 3000);

  // Espera 2 segundos más (5 segundos en total) y lo elimina del DOM
  setTimeout(() => {
    zona.removeChild(alerta);
  }, 5000);
});

// Al conectarse, se recibe el estado inicial del sistema
socket.on('estado-inicial', (estado) => {
  document.getElementById('tempValor').textContent = estado.sensores.temperatura ?? '--';
  document.getElementById('luzValor').textContent = estado.sensores.luminosidad ?? '--';
  document.getElementById('persianaEstado').textContent = estado.actuadores.persiana;
  document.getElementById('aireEstado').textContent = estado.actuadores.aire;
  document.getElementById('calorEstado').textContent = estado.actuadores.calor;
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
