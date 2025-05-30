const socket = io(); // Se conecta al servidor WebSocket
const form = document.getElementById('formSensores'); // Referencia al formulario

// Evento al enviar el formulario
form.addEventListener('submit', (e) => {
  e.preventDefault(); // Evita que se recargue la página

  // Obtener y convertir los valores introducidos por el usuario
  const temperatura = parseFloat(document.getElementById('temperatura').value);
  const luminosidad = parseFloat(document.getElementById('luminosidad').value);
  const humedad = parseFloat(document.getElementById('humedad').value);
  const lluvia = document.getElementById('lluvia').checked; 

  // Enviar temperatura si es válida
  if (!isNaN(temperatura)) {
    socket.emit('nueva-medida', { tipo: 'temperatura', valor: temperatura });
  }

  // Enviar luminosidad si es válida
  if (!isNaN(luminosidad)) {
    socket.emit('nueva-medida', { tipo: 'luminosidad', valor: luminosidad });
  }

  // Enviar humedad si es válida
  if (!isNaN(humedad)) {
    socket.emit('nueva-medida', { tipo: 'humedad', valor: humedad });
  }

  socket.emit('nueva-medida', { tipo: 'lluvia', valor: lluvia });

  // Limpiar los campos del formulario
  form.reset();
});
