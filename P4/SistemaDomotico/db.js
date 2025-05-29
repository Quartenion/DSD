// db.js

import { MongoClient } from 'mongodb';

const URI = 'mongodb://localhost:27017';
const DB_NAME = 'domotica';
const COLECCION = 'eventos';

let db = null;

export async function getDB() {
  if (db) return db;
  try {
    const cliente = await MongoClient.connect(URI);
    db = cliente.db(DB_NAME);
    console.log('✅ Conectado a MongoDB');
    return db;
  } catch (err) {
    console.error('❌ Error conectando a MongoDB:', err);
    throw err;
  }
}

export async function guardarEvento(evento) {
  try {
    const database = await getDB();
    const coleccion = database.collection(COLECCION);
    await coleccion.insertOne(evento);
  } catch (err) {
    console.error('❌ Error al guardar evento en MongoDB:', err);
  }
}

export async function obtenerHistorial(limit = 20) {
  try {
    const database = await getDB();
    const coleccion = database.collection(COLECCION);
    return await coleccion.find().sort({ timestamp: -1 }).limit(limit).toArray();
  } catch (err) {
    console.error('❌ Error al obtener historial:', err);
    return [];
  }
}

