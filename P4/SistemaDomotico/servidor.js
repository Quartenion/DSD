import http from 'node:http';
import { readFile } from 'node:fs';
import { join } from 'node:path';
import { Server } from 'socket.io';
import { getDB, guardarEvento, obtenerHistorial } from './db.js';
import { procesarMedida } from './agente.js';

const PORT = 8080;
const server = http.createServer((req, res) => {
  let file = '';

  if (req.url === '/usuario') {
    file = 'usuario.html';
  } else if (req.url === '/sensores') {
    file = 'sensores.html';
  } else {
    res.writeHead(404, { 'Content-Type': 'text/plain' });
    res.end('Página no encontrada');
    return;
  }

  readFile(join(process.cwd(), file), (err, data) => {
    if (err) {
      res.writeHead(500, { 'Content-Type': 'text/plain' });
      res.end('Error al cargar el archivo');
    } else {
      res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
      res.end(data);
    }
  });
});

const io = new Server(server);
let estado = {
  sensores: { temperatura: null, luminosidad: null },
  actuadores: { persiana: 'abierta', aire: 'apagado' },
};

io.on('connection', (socket) => {
  console.log('Nuevo cliente conectado');

  // Enviar estado inicial
  socket.emit('estado-inicial', estado);
  
  obtenerHistorial().then(eventos => {
    socket.emit('historial', eventos);
  });


  // Evento recibido desde sensores
  socket.on('nueva-medida', async (medida) => {
    const { tipo, valor } = medida;
    const timestamp = new Date();

    estado.sensores[tipo] = valor;
    io.emit('actualizar-sensores', { tipo, valor, timestamp });

    await guardarEvento({ tipo, valor, timestamp });

    const acciones = procesarMedida(tipo, valor, estado.actuadores);
    acciones.forEach(({ tipo, valor, mensaje }) => {
        if (tipo === 'alarma') {
        io.emit('alarma', mensaje);
        } else if (tipo === 'actuador') {
        estado.actuadores[valor.dispositivo] = valor.estado;
        io.emit('actualizar-actuadores', estado.actuadores);
        }
    });

    // 🚀 NUEVO: reenviar historial actualizado a todos los usuarios
    const historial = await obtenerHistorial();
    io.emit('historial', historial);
    });


  // Control manual desde usuario
  socket.on('control-actuador', ({ dispositivo, estado: nuevoEstado }) => {
    estado.actuadores[dispositivo] = nuevoEstado;
    io.emit('actualizar-actuadores', estado.actuadores);
  });
});

getDB().then(() => {
  server.listen(PORT, () => {
    console.log(`Servidor domótico funcionando en http://localhost:${PORT}`);
  });
});
