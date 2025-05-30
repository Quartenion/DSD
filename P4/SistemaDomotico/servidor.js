// Importación de módulos necesarios del sistema y locales
import http from 'node:http';
import { readFile } from 'node:fs';
import { join } from 'node:path';
import { Server } from 'socket.io';

import { getDB, guardarEvento, obtenerHistorial } from './db.js';
import { procesarMedida } from './agente.js';

const PORT = 8080;

// Servidor HTTP
const server = http.createServer((req, res) => {
  let file = '';
  let contentType = 'text/html; charset=utf-8';

  // Enrutamiento de archivos estáticos
  if (req.url === '/usuario') {
    file = 'usuario.html';
    contentType = 'text/html; charset=utf-8';
  } else if (req.url === '/sensores') {
    file = 'sensores.html';
    contentType = 'text/html; charset=utf-8';
  } else if (req.url === '/usuario.js') {
    file = 'usuario.js';
    contentType = 'application/javascript';
  } else if (req.url === '/sensores.js') {
    file = 'sensores.js';
    contentType = 'application/javascript';
  } else if (req.url.endsWith('.png')) {
    file = req.url.slice(1);
    contentType = 'image/png';
  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Pagina no encontrada');
    return;
  }

  // Leer y servir el archivo solicitado
  readFile(join(process.cwd(), file), (err, data) => {
    if (err) {
      res.writeHead(500, { 'Content-Type': 'text/plain' });
      res.end('Error al cargar el archivo');
    } else {
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(data);
    }
  });
});

// Socket.io sobre el servidor HTTP
const io = new Server(server);

// Estado inicial del sistema
let estado = {
  sensores: { temperatura: null, luminosidad: null, humedad: null, lluvia: false },
  actuadores: { persiana: 'cerrada', aire: 'apagado', calor: 'apagado', ventana: 'cerrada', riegue: 'apagado' },
};

// Conexiones 
io.on('connection', (socket) => {
  console.log('Nuevo cliente conectado');

  // Enviar estado inicial y historial al cliente
  socket.emit('estado-inicial', estado);
  obtenerHistorial().then(eventos => {
    socket.emit('historial', eventos);
  });

  // Nueva medida desde sensores
  socket.on('nueva-medida', async (medida) => {
    const { tipo, valor } = medida;
    const timestamp = new Date();

    estado.sensores[tipo] = valor;
    io.emit('actualizar-sensores', { tipo, valor, timestamp });

    await guardarEvento({ tipo, valor, timestamp });

    const acciones = procesarMedida(tipo, valor, estado);
    acciones.forEach(({ tipo, valor, mensaje }) => {
      if (tipo === 'alarma') {
        io.emit('alarma', mensaje);
      } else if (tipo === 'actuador') {
        estado.actuadores[valor.dispositivo] = valor.estado;
        io.emit('actualizar-actuadores', estado.actuadores);
      }
    });

    const historial = await obtenerHistorial();
    io.emit('historial', historial);
  });

  // Control manual desde interfaz
  socket.on('control-actuador', ({ dispositivo, estado: nuevoEstado }) => {
    estado.actuadores[dispositivo] = nuevoEstado;
    io.emit('actualizar-actuadores', estado.actuadores);
  });
});

// Iniciar el servidor tras conectar a la base de datos
getDB().then(() => {
  server.listen(PORT, () => {
    console.log(`Servidor domótico funcionando en http://localhost:${PORT}`);
  });
});
