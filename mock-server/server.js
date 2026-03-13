import express from 'express';
import { createServer } from 'http';
import { WebSocketServer } from 'ws';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import jwt from 'jsonwebtoken';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const PORT = process.env.PORT || 8080;
const STORE_FILE = path.join(__dirname, 'snapshot-store.json');
const DEVICE_STORE_FILE = path.join(__dirname, 'device-store.json');
const POWER_SCHEDULE_FILE = path.join(__dirname, 'power-schedule-store.json');
const JWT_SECRET = process.env.JWT_SECRET || 'did-dev-secret';
const DEV_JWT_TOKEN = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkaWQtMDAxIiwicm9sZSI6ImRldiJ9.mTWi_MeRhODeQ382jeLB26y2rTgE-kyqOIbovUjKUAM';

const app = express();
app.use(express.json({ limit: '2mb' }));
app.use(express.static(path.join(__dirname, 'public')));

const extractBearerToken = req => {
  const auth = req.headers.authorization || '';
  if (auth.startsWith('Bearer ')) return auth.slice(7).trim();
  return null;
};

const verifyJwt = token => {
  if (!token) return null;
  try {
    return jwt.verify(token, JWT_SECRET, { algorithms: ['HS256'] });
  } catch {
    return null;
  }
};

const requireAuth = (req, res, next) => {
  const token = extractBearerToken(req);
  const decoded = verifyJwt(token);
  if (!decoded) return res.status(401).json({ error: 'Unauthorized' });
  req.jwt = decoded;
  next();
};

const createDefaultSnapshot = () => ({
  version: 1,
  validFromEpochSec: null,
  validToEpochSec: null,
  layout: {
    id: 'layout_three_zone',
    canvas: { width: 1920, height: 1080 },
    coordinateSystem: 'CANVAS_PIXEL',
    zones: [
      { id: 'left', x: 0, y: 0, width: 360, height: 1080, zIndex: 0, backgroundHex: '#111111', fitMode: 'COVER' },
      { id: 'center', x: 360, y: 0, width: 960, height: 1080, zIndex: 1, backgroundHex: '#000000', fitMode: 'COVER' },
      { id: 'right', x: 1320, y: 0, width: 600, height: 1080, zIndex: 0, backgroundHex: '#111111', fitMode: 'COVER' }
    ]
  },
  zonePlaylists: {
    left: [{ assetId: 'video_left_1', order: 0, durationSec: 30, transition: 'NONE' }],
    center: [
      { assetId: 'image_center_1', order: 0, durationSec: 10, transition: 'NONE' },
      { assetId: 'image_center_2', order: 1, durationSec: 10, transition: 'NONE' },
      { assetId: 'text_center_1', order: 2, durationSec: 8, transition: 'NONE' }
    ],
    right: [{ assetId: 'video_right_1', order: 0, durationSec: 30, transition: 'NONE' }]
  },
  assets: {
    video_left_1: {
      id: 'video_left_1',
      type: 'VIDEO',
      source: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4',
      metadata: {},
      defaultDurationSec: 20
    },
    video_right_1: {
      id: 'video_right_1',
      type: 'VIDEO',
      source: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4',
      metadata: {},
      defaultDurationSec: 20
    },
    image_center_1: {
      id: 'image_center_1',
      type: 'IMAGE',
      source: 'https://images.unsplash.com/photo-1502134249126-9f3755a50d78?q=80&w=1920&h=1080&auto=format&fit=crop',
      metadata: {},
      defaultDurationSec: 10
    },
    image_center_2: {
      id: 'image_center_2',
      type: 'IMAGE',
      source: 'https://images.unsplash.com/photo-1542281286-9e0a16bb7366?q=80&w=1920&h=1080&auto=format&fit=crop',
      metadata: {},
      defaultDurationSec: 10
    },
    text_center_1: {
      id: 'text_center_1',
      type: 'TEXT',
      source: 'DID V2 Snapshot from API/WS',
      metadata: {},
      defaultDurationSec: 8
    }
  }
});

const snapshotsByDid = new Map();
const devicesByDid = new Map();

const getReferencedAssetIds = snapshot => {
  const ids = new Set();
  const zonePlaylists = snapshot?.zonePlaylists || {};
  Object.values(zonePlaylists).forEach(items => {
    if (!Array.isArray(items)) return;
    items.forEach(item => {
      if (item?.assetId) ids.add(item.assetId);
    });
  });
  return ids;
};

const pruneUnusedAssets = snapshot => {
  if (!snapshot || typeof snapshot !== 'object') return snapshot;
  const referencedIds = getReferencedAssetIds(snapshot);
  const nextAssets = {};
  const assets = snapshot.assets || {};
  Object.entries(assets).forEach(([assetId, asset]) => {
    if (referencedIds.has(assetId)) nextAssets[assetId] = asset;
  });
  return { ...snapshot, assets: nextAssets };
};

const loadStore = () => {
  try {
    if (!fs.existsSync(STORE_FILE)) return;
    const raw = fs.readFileSync(STORE_FILE, 'utf8');
    const parsed = JSON.parse(raw);
    let pruned = false;
    Object.entries(parsed).forEach(([didId, snapshot]) => {
      const sanitized = pruneUnusedAssets(snapshot);
      const beforeCount = Object.keys(snapshot?.assets || {}).length;
      const afterCount = Object.keys(sanitized?.assets || {}).length;
      if (afterCount < beforeCount) pruned = true;
      snapshotsByDid.set(didId, sanitized);
    });
    if (pruned) saveStore();
  } catch (e) {
    console.error('Failed to load snapshot store:', e.message);
  }
};

const saveStore = () => {
  try {
    const obj = Object.fromEntries(snapshotsByDid.entries());
    fs.writeFileSync(STORE_FILE, JSON.stringify(obj, null, 2), 'utf8');
  } catch (e) {
    console.error('Failed to save snapshot store:', e.message);
  }
};

const loadDeviceStore = () => {
  try {
    if (!fs.existsSync(DEVICE_STORE_FILE)) return;
    const raw = fs.readFileSync(DEVICE_STORE_FILE, 'utf8');
    const parsed = JSON.parse(raw);
    Object.entries(parsed).forEach(([didId, device]) => {
      devicesByDid.set(didId, device);
    });
  } catch (e) {
    console.error('Failed to load device store:', e.message);
  }
};

const saveDeviceStore = () => {
  try {
    const obj = Object.fromEntries(devicesByDid.entries());
    fs.writeFileSync(DEVICE_STORE_FILE, JSON.stringify(obj, null, 2), 'utf8');
  } catch (e) {
    console.error('Failed to save device store:', e.message);
  }
};

const powerSchedulesByDid = new Map();

const loadPowerScheduleStore = () => {
  try {
    if (!fs.existsSync(POWER_SCHEDULE_FILE)) return;
    const raw = fs.readFileSync(POWER_SCHEDULE_FILE, 'utf8');
    const parsed = JSON.parse(raw);
    Object.entries(parsed).forEach(([didId, schedule]) => {
      powerSchedulesByDid.set(didId, schedule);
    });
  } catch (e) {
    console.error('Failed to load power schedule store:', e.message);
  }
};

const savePowerScheduleStore = () => {
  try {
    const obj = Object.fromEntries(powerSchedulesByDid.entries());
    fs.writeFileSync(POWER_SCHEDULE_FILE, JSON.stringify(obj, null, 2), 'utf8');
  } catch (e) {
    console.error('Failed to save power schedule store:', e.message);
  }
};

loadStore();
loadDeviceStore();
loadPowerScheduleStore();
if (!snapshotsByDid.has('did-001')) {
  snapshotsByDid.set('did-001', createDefaultSnapshot());
  saveStore();
}
if (!devicesByDid.has('did-001')) {
  const now = Math.floor(Date.now() / 1000);
  devicesByDid.set('did-001', {
    didId: 'did-001',
    status: 'ACTIVE',
    activatedAtEpochSec: now,
    updatedAtEpochSec: now
  });
  saveDeviceStore();
}

const getSnapshot = didId => snapshotsByDid.get(didId) || createDefaultSnapshot();

app.get('/health', (_req, res) => {
  res.json({ ok: true, ts: Date.now() });
});

app.get('/api/auth/dev-token', (_req, res) => {
  res.json({ token: DEV_JWT_TOKEN });
});

app.use('/api/admin', requireAuth);
app.use('/api/players', requireAuth);
app.use('/api/devices', requireAuth);

app.get('/api/players/:didId/device-status', (req, res) => {
  const didId = req.params.didId;
  const device = devicesByDid.get(didId);
  if (!device) {
    return res.json({ didId, status: 'NOT_FOUND' });
  }
  return res.json(device);
});

app.post('/api/devices/register', (req, res) => {
  const didId = String(req.body?.didId || '').trim();
  if (!didId) {
    return res.status(400).json({ error: 'didId is required' });
  }
  const existing = devicesByDid.get(didId);
  if (existing) {
    return res.json({ ok: true, didId, status: existing.status });
  }
  const now = Math.floor(Date.now() / 1000);
  const created = {
    didId,
    status: 'PENDING_APPROVAL',
    activatedAtEpochSec: null,
    updatedAtEpochSec: now
  };
  devicesByDid.set(didId, created);
  saveDeviceStore();
  console.log('[API] device register requested', { didId, status: created.status });
  return res.json({ ok: true, didId, status: created.status });
});

app.get('/api/players/:didId/snapshot', (req, res) => {
  res.json(getSnapshot(req.params.didId));
});

app.get('/api/admin/snapshots/:didId', (req, res) => {
  res.json(getSnapshot(req.params.didId));
});

app.post('/api/admin/snapshots/:didId', (req, res) => {
  const didId = req.params.didId;
  const incoming = req.body;
  console.log('[API] POST /api/admin/snapshots/:didId', { didId, payload: incoming });
  if (!incoming || typeof incoming !== 'object' || !incoming.layout || !incoming.assets || !incoming.zonePlaylists) {
    return res.status(400).json({ error: 'Invalid snapshot payload' });
  }
  const current = getSnapshot(didId);
  const nextVersion = Number(current.version || 0) + 1;
  const merged = pruneUnusedAssets({ ...incoming, version: nextVersion });
  snapshotsByDid.set(didId, merged);
  saveStore();
  broadcastSnapshot(didId, merged);
  return res.json({ ok: true, didId, version: nextVersion });
});

// --- Power Schedule endpoints ---

app.get('/api/players/:didId/power-schedule', (req, res) => {
  const didId = req.params.didId;
  const schedule = powerSchedulesByDid.get(didId) || { enabled: false, powerOffTime: null, powerOnTime: null };
  return res.json({ didId, ...schedule });
});

app.get('/api/admin/devices/:didId/power-schedule', (req, res) => {
  const didId = req.params.didId;
  const schedule = powerSchedulesByDid.get(didId) || { enabled: false, powerOffTime: null, powerOnTime: null };
  return res.json({ didId, ...schedule });
});

app.post('/api/admin/devices/:didId/power-schedule', (req, res) => {
  const didId = req.params.didId;
  const { enabled, powerOffTime, powerOnTime } = req.body || {};
  const schedule = {
    enabled: !!enabled,
    powerOffTime: powerOffTime || null,   // "HH:mm" format
    powerOnTime: powerOnTime || null      // "HH:mm" format
  };
  powerSchedulesByDid.set(didId, schedule);
  savePowerScheduleStore();
  console.log('[API] Power schedule updated', { didId, schedule });
  broadcastPowerSchedule(didId, schedule);
  return res.json({ ok: true, didId, ...schedule });
});

app.get('/admin', (_req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

const server = createServer(app);
const wss = new WebSocketServer({ server, path: '/ws/player-snapshot' });

const socketsByDid = new Map();

const getSocketSet = didId => {
  if (!socketsByDid.has(didId)) socketsByDid.set(didId, new Set());
  return socketsByDid.get(didId);
};

const broadcastSnapshot = (didId, snapshot) => {
  const set = getSocketSet(didId);
  const payload = JSON.stringify({ type: 'SNAPSHOT_UPDATED', payload: snapshot });
  console.log('[WS] broadcast', { didId, connections: set.size, payload: { type: 'SNAPSHOT_UPDATED', payload: snapshot } });
  for (const ws of set) {
    if (ws.readyState === ws.OPEN) ws.send(payload);
  }
};

const broadcastPowerSchedule = (didId, schedule) => {
  const set = getSocketSet(didId);
  const payload = JSON.stringify({ type: 'POWER_SCHEDULE_UPDATED', payload: { didId, ...schedule } });
  console.log('[WS] power schedule broadcast', { didId, connections: set.size });
  for (const ws of set) {
    if (ws.readyState === ws.OPEN) ws.send(payload);
  }
};

wss.on('connection', (ws, req) => {
  const fullUrl = new URL(req.url, `http://${req.headers.host}`);
  const didId = fullUrl.searchParams.get('didId') || 'did-001';
  const queryToken = fullUrl.searchParams.get('token');
  const headerToken = (() => {
    const auth = req.headers.authorization || '';
    if (auth.startsWith('Bearer ')) return auth.slice(7).trim();
    return null;
  })();
  const decoded = verifyJwt(queryToken || headerToken);
  if (!decoded) {
    ws.close(1008, 'Unauthorized');
    return;
  }

  const set = getSocketSet(didId);
  set.add(ws);

  ws.send(JSON.stringify({ type: 'SNAPSHOT_UPDATED', payload: getSnapshot(didId) }));

  ws.on('close', () => {
    set.delete(ws);
  });
});

server.listen(PORT, () => {
  console.log(`Mock server running on http://localhost:${PORT}`);
  console.log(`Admin web: http://localhost:${PORT}/admin`);
});
