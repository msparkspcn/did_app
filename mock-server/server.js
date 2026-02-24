import express from 'express';
import { createServer } from 'http';
import { WebSocketServer } from 'ws';
import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const PORT = process.env.PORT || 8080;
const STORE_FILE = path.join(__dirname, 'snapshot-store.json');

const app = express();
app.use(express.json({ limit: '2mb' }));
app.use(express.static(path.join(__dirname, 'public')));

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
      source: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4',
      metadata: {},
      defaultDurationSec: 30
    },
    video_right_1: {
      id: 'video_right_1',
      type: 'VIDEO',
      source: 'https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4',
      metadata: {},
      defaultDurationSec: 30
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

const loadStore = () => {
  try {
    if (!fs.existsSync(STORE_FILE)) return;
    const raw = fs.readFileSync(STORE_FILE, 'utf8');
    const parsed = JSON.parse(raw);
    Object.entries(parsed).forEach(([didId, snapshot]) => {
      snapshotsByDid.set(didId, snapshot);
    });
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

loadStore();
if (!snapshotsByDid.has('did-001')) {
  snapshotsByDid.set('did-001', createDefaultSnapshot());
  saveStore();
}

const getSnapshot = didId => snapshotsByDid.get(didId) || createDefaultSnapshot();

app.get('/health', (_req, res) => {
  res.json({ ok: true, ts: Date.now() });
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
  if (!incoming || typeof incoming !== 'object' || !incoming.layout || !incoming.assets || !incoming.zonePlaylists) {
    return res.status(400).json({ error: 'Invalid snapshot payload' });
  }
  const current = getSnapshot(didId);
  const nextVersion = Number(current.version || 0) + 1;
  const merged = { ...incoming, version: nextVersion };
  snapshotsByDid.set(didId, merged);
  saveStore();
  broadcastSnapshot(didId, merged);
  return res.json({ ok: true, didId, version: nextVersion });
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
  for (const ws of set) {
    if (ws.readyState === ws.OPEN) ws.send(payload);
  }
};

wss.on('connection', (ws, req) => {
  const fullUrl = new URL(req.url, `http://${req.headers.host}`);
  const didId = fullUrl.searchParams.get('didId') || 'did-001';
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
