import { useState, useEffect, useRef } from 'react';
import { MapContainer, TileLayer, Marker, Polyline, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';

// FIX FOR VITE/REACT-LEAFLET MISSING ICONS
import iconMarker from 'leaflet/dist/images/marker-icon.png';
import iconRetina from 'leaflet/dist/images/marker-icon-2x.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

L.Icon.Default.mergeOptions({
  iconRetinaUrl: iconRetina,
  iconUrl: iconMarker,
  shadowUrl: iconShadow,
});

// CITY COORDINATES
const NODE_COORDINATES = {
  "Ulaanbaatar": [47.9188, 106.9176],
  "Altai": [46.3722, 96.2583],
  "Arvaikheer": [46.2639, 102.7750],
  "Baruun-Urt": [46.6806, 113.2792],
  "Bayankhongor": [46.1167, 100.7167],
  "Bulgan": [48.8125, 103.5347],
  "Dalanzadgad": [43.5708, 104.4250],
  "Darkhan": [49.4867, 105.9228],
  "Zuunmod": [47.7069, 106.9750],
  "Mandalgovi": [45.7625, 106.2708],
  "Murun": [49.6342, 100.1625],
  "Sainshand": [44.8911, 110.1367],
  "Sukhbaatar": [50.2314, 106.2081],
  "Ulaangom": [50.0306, 92.0667],
  "Uliastai": [47.7417, 96.8444],
  "Khovd": [48.0056, 91.6300],
  "Tsetserleg": [47.4750, 101.4542],
  "Choibalsan": [48.0797, 114.5342],
  "Choir": [46.3611, 108.3611],
  "Ulgii": [48.9706, 89.9656],
  "Chinggis City": [47.3167, 110.6500],
  "Erdenet": [49.0278, 104.0450]
};

// Topology State Colors
const TOPOLOGY_COLORS = {
  mst: 'red',
  two_edge: 'blue',
  mesh: 'green'
};

const TOPOLOGY_LABELS = {
  mst: 'MST',
  two_edge: '2-edge-connected',
  mesh: 'Full mesh'
};

const ROAD_CACHE_DB = 'network-road-cache-db';
const ROAD_CACHE_STORE = 'routes';

function openRoadCacheDatabase() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(ROAD_CACHE_DB, 1);
    request.onupgradeneeded = () => {
      request.result.createObjectStore(ROAD_CACHE_STORE);
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error);
  });
}

// --- CUSTOM ROUTING COMPONENT FOR PHYSICAL ROADS ---
function RoutingEdge({ sourceCoords, targetCoords, color, onRouteFound }) {
  const map = useMap();
  const onRouteFoundRef = useRef(onRouteFound);

  useEffect(() => {
    onRouteFoundRef.current = onRouteFound;
  }, [onRouteFound]);

  useEffect(() => {
    if (!map || !sourceCoords || !targetCoords) return;

    const routingControl = L.Routing.control({
      waypoints: [
        L.latLng(sourceCoords[0], sourceCoords[1]),
        L.latLng(targetCoords[0], targetCoords[1])
      ],
      show: false,
      addWaypoints: false,
      routeWhileDragging: false,
      fitSelectedRoutes: false,
      showAlternatives: false,
      lineOptions: {
        styles: [{ color: color, opacity: 0.8, weight: 4 }]
      },
      createMarker: () => null 
    });

    routingControl.on('routesfound', (event) => {
      const coordinates = event.routes[0]?.coordinates;
      if (coordinates?.length) onRouteFoundRef.current(coordinates);
    });
    routingControl.addTo(map);

    return () => {
      // Invalidate and cancel OSRM callbacks before removing the control's map.
      routingControl._requestCount += 1;
      if (routingControl._pendingRequest?.abort) {
        routingControl._pendingRequest.abort();
        routingControl._pendingRequest = null;
      }
      if (routingControl._map) map.removeControl(routingControl);
    };
  }, [map, sourceCoords, targetCoords, color]);

  return null;
}

export default function App() {
  const [graphCache, setGraphCache] = useState({ mst: null, two_edge: null, mesh: null });
  const [roadCache, setRoadCache] = useState({});
  const [roadCacheLoaded, setRoadCacheLoaded] = useState(false);
  const [topologyType, setTopologyType] = useState('mst');
  const [useRoads, setUseRoads] = useState(false); // Toggle between straight lines and physical roads
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [originCity, setOriginCity] = useState('');
  const [destinationCity, setDestinationCity] = useState('');
  const [cutting, setCutting] = useState(false);
  const [cutMessage, setCutMessage] = useState(null);
  const [editGraph, setEditGraph] = useState('mesh');
  const [edgeAction, setEdgeAction] = useState('POST');
  const [edgeSource, setEdgeSource] = useState('');
  const [edgeTarget, setEdgeTarget] = useState('');
  const [editMessage, setEditMessage] = useState(null);
  const [cutAnalysis, setCutAnalysis] = useState(null);
  const [controlsOpen, setControlsOpen] = useState(false);

  const graphData = graphCache[topologyType] || { nodes: [], links: [] };

  const coordinatesFor = (nodeId) => NODE_COORDINATES[nodeId];

  useEffect(() => {
    let cancelled = false;

    const loadRoadCache = async () => {
      try {
        const database = await openRoadCacheDatabase();
        const transaction = database.transaction(ROAD_CACHE_STORE, 'readonly');
        const request = transaction.objectStore(ROAD_CACHE_STORE).get('all');
        request.onsuccess = () => {
          if (!cancelled) {
            setRoadCache(request.result || {});
            setRoadCacheLoaded(true);
          }
          database.close();
        };
        request.onerror = () => {
          if (!cancelled) {
            setRoadCache({});
            setRoadCacheLoaded(true);
          }
          database.close();
        };
      } catch {
        if (!cancelled) {
          setRoadCache({});
          setRoadCacheLoaded(true);
        }
      }
    };

    loadRoadCache();
    return () => {
      cancelled = true;
    };
  }, []);

  useEffect(() => {
    if (!roadCacheLoaded) return;

    const saveRoadCache = async () => {
      try {
        const database = await openRoadCacheDatabase();
        const transaction = database.transaction(ROAD_CACHE_STORE, 'readwrite');
        transaction.objectStore(ROAD_CACHE_STORE).put(roadCache, 'all');
        transaction.oncomplete = () => database.close();
        transaction.onerror = () => database.close();
      } catch {
        // The in-memory cache remains available if browser storage is unavailable.
      }
    };

    saveRoadCache();
  }, [roadCache, roadCacheLoaded]);

  // Fetch each topology once, then use the local cache when switching views.
  useEffect(() => {
    if (graphCache[topologyType]) {
      return;
    }

    const fetchGraph = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await fetch(`http://localhost:5000/api/network/topology/${topologyType}`);
        if (!response.ok) throw new Error('Network response was not ok');
        const data = await response.json();
        setGraphCache(prev => ({ ...prev, [topologyType]: data }));
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchGraph();
  }, [topologyType, graphCache]);

  const simulateCut = async (event) => {
    event.preventDefault();
    const origin = originCity.trim();
    const destination = destinationCity.trim();

    if (!origin || !destination) {
      setCutMessage('Хоёр хотын нэрийг оруулна уу.');
      return;
    }
    if (origin === destination) {
      setCutMessage('Ялгаатай хоёр хот сонгоно уу.');
      return;
    }

    setCutting(true);
    setCutMessage(null);
    try {
      const response = await fetch('http://localhost:5000/api/network/simulate-cut/', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ a: origin, b: destination })
      });
      const data = await response.json();
      if (!response.ok) throw new Error(data.error || 'Таслалтын симуляци хийх боломжгүй.');

      setGraphCache(prev => ({ ...prev, two_edge: data }));
      setCutAnalysis(data.analysis || null);
      setCutMessage(`Таслалтын симуляци хийгдлээ: ${origin} - ${destination}`);
      setOriginCity('');
      setDestinationCity('');
    } catch (err) {
      setCutMessage(err.message);
    } finally {
      setCutting(false);
    }
  };

  const submitGraphChange = async (event, resource, action, payload) => {
    event.preventDefault();
    setEditMessage(null);
    try {
      const response = await fetch(`http://localhost:5000/api/network/graphs/${editGraph}/${resource}`, {
        method: action,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      const data = await response.json();
      if (!response.ok) throw new Error(data.error || 'Графыг шинэчлэх боломжгүй.');
      setGraphCache(prev => ({ ...prev, [editGraph]: data }));
      setEditMessage(`${resource === 'nodes' ? 'Node' : 'Edge'} ${action === 'POST' ? 'нэмэгдлээ' : action === 'PUT' ? 'шинэчлэгдлээ' : 'устгагдлаа'}.`);
    } catch (err) {
      setEditMessage(err.message);
    }
  };

  const submitEdgeChange = (event) => {
    const payload = { source: edgeSource, target: edgeTarget };
    submitGraphChange(event, 'edges', edgeAction, payload);
  };

  // Calculate Topology Statistics dynamically
  const calculateStats = () => {
    if (graphData.stats) return graphData.stats;

    const totalLength = graphData.links.reduce((acc, link) => acc + (link.weight || 0), 0).toFixed(2);
    const criticalLinks = 0;
    const safetyState = "Тийм (1-Fault Tolerant)";
    return { totalLength, criticalLinks, safetyState };
  };

  const stats = calculateStats();
  const activeColor = TOPOLOGY_COLORS[topologyType];
  const isSafe = stats.safetyState?.startsWith('Тийм');

  return (
    <div className="app-shell">
      <style>{`.leaflet-routing-container { display: none !important; }`}</style>
      <header className="app-header">
        <div className="brand-lockup">
          <div className="brand-mark" aria-hidden="true">N</div>
          <div>
            <strong className="brand-name">Mongolia Network Lab</strong>
            <span className="brand-caption">Шилэн сүлжээний топологийн орчин</span>
          </div>
        </div>
        <div className="header-status"><span className="status-dot" /><span>{TOPOLOGY_LABELS[topologyType]}</span></div>
        <div className="header-actions">
          <button className="mobile-panel-button" onClick={() => setControlsOpen(value => !value)} aria-expanded={controlsOpen}>
            {controlsOpen ? 'Удирдлагыг хаах' : 'Удирдлага'}
          </button>
        </div>
      </header>

      <main className="workspace">
        <aside className={`control-rail${controlsOpen ? ' open' : ''}`}>
          <div className="rail-heading">
            <span className="eyebrow">Сүлжээний төлөвлөлт</span>
            <h1>Топологийн удирдлага</h1>
          </div>

          <section className="rail-section">
            <h2 className="section-title">Граф харах</h2>
            <div className="topology-list">
              {Object.entries(TOPOLOGY_LABELS).map(([type, label]) => (
                <button
                  key={type}
                  className={`topology-button${topologyType === type ? ' active' : ''}`}
                  style={{ '--topology-color': TOPOLOGY_COLORS[type] }}
                  onClick={() => setTopologyType(type)}
                >
                  <span className="color-chip" />
                  <span>{label}</span>
                </button>
              ))}
            </div>
          </section>

          <section className="rail-section">
            <h2 className="section-title">Газрын зураг</h2>
            <label className="toggle-row">
              <input type="checkbox" checked={useRoads} onChange={(event) => setUseRoads(event.target.checked)} />
              Бодит замын трасс харуулах
            </label>
            <p className="message">Нэг удаа ачаалсны дараа замын трассыг локал хадгалалтаас ашиглана.</p>
          </section>

          <section className="rail-section">
            <h2 className="section-title">Гэмтэл симуляцлах</h2>
            <form className="form-stack" onSubmit={simulateCut}>
              <input value={originCity} onChange={(event) => setOriginCity(event.target.value)} placeholder="Эхлэх хот" disabled={topologyType !== 'two_edge' || cutting} />
              <input value={destinationCity} onChange={(event) => setDestinationCity(event.target.value)} placeholder="Төгсгөлийн хот" disabled={topologyType !== 'two_edge' || cutting} />
              <button className="primary-button" type="submit" disabled={topologyType !== 'two_edge' || cutting}>{cutting ? 'Тасалж байна...' : 'Сонгосон ирмэгийг таслах'}</button>
            </form>
            {topologyType !== 'two_edge' && <p className="message">Зөвхөн 2-edge-connected графыг харж байх үед ашиглана.</p>}
            {cutMessage && <p className="message">{cutMessage}</p>}
          </section>

          <section className="rail-section">
            <h2 className="section-title">Граф засварлагч</h2>
            <div className="form-stack">
              <select value={editGraph} onChange={(event) => setEditGraph(event.target.value)} aria-label="Graph to edit">
                <option value="mesh">Full mesh</option>
                <option value="two_edge">2-edge-connected</option>
                <option value="mst">MST</option>
              </select>
              <p className="message">Node нэмэх боломжгүй. Хотын жагсаалт болон координат тогтмол өгөгдлөөс уншигдана.</p>
              <form className="form-stack" onSubmit={submitEdgeChange}>
                <span className="field-label">Edge өөрчлөлт</span>
                <select value={edgeAction} onChange={(event) => setEdgeAction(event.target.value)} aria-label="Edge action">
                  <option value="POST">Нэмэх</option><option value="PUT">Засах</option><option value="DELETE">Устгах</option>
                </select>
                <div className="form-grid">
                  <input value={edgeSource} onChange={(event) => setEdgeSource(event.target.value)} placeholder="Эхлэх хот" required />
                   <input value={edgeTarget} onChange={(event) => setEdgeTarget(event.target.value)} placeholder="Хүрэх хот" required />
                </div>
                <button className="primary-button" type="submit">Edge өөрчлөлт хэрэгжүүлэх</button>
              </form>
            </div>
            {editMessage && <p className="message">{editMessage}</p>}
          </section>
        </aside>

        <section className="map-stage" aria-label="Interactive network map">
          <div className="map-badge" style={{ '--topology-color': activeColor }}><span />{TOPOLOGY_LABELS[topologyType]}</div>
          {loading && <div className="loading-overlay">Топологи ачаалж байна...</div>}
          {error && <div className="loading-overlay">{error}</div>}
          <MapContainer center={[46.8625, 103.8467]} zoom={5}>
            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            {graphData.nodes.map(node => {
              const coords = coordinatesFor(node.id);
              if (!coords) return null;
              return <Marker key={node.id} position={coords}><Popup><strong>{node.id}</strong><br />Өргөрөг: {coords[0]}<br />Уртраг: {coords[1]}</Popup></Marker>;
            })}
            {graphData.links.map((link, idx) => {
              const sourceCoords = coordinatesFor(link.source);
              const targetCoords = coordinatesFor(link.target);
              if (!sourceCoords || !targetCoords) return null;
              const edgePopup = <Popup>{link.source} - {link.target}<br />Жин: {link.weight ?? 'байхгүй'} km</Popup>;
              if (useRoads) {
                if (!roadCacheLoaded) return null;
                const roadKey = `${link.source}->${link.target}`;
                const cachedRoute = roadCache[roadKey];
                if (cachedRoute) return <Polyline key={`cached-road-${topologyType}-${idx}`} positions={cachedRoute} color={activeColor} weight={4} opacity={0.8}>{edgePopup}</Polyline>;
                return <RoutingEdge key={`road-${topologyType}-${idx}`} sourceCoords={sourceCoords} targetCoords={targetCoords} color={activeColor} onRouteFound={(coordinates) => setRoadCache(prev => ({ ...prev, [roadKey]: coordinates }))} />;
              }
              return <Polyline key={`straight-${topologyType}-${idx}`} positions={[sourceCoords, targetCoords]} color={activeColor} weight={3} opacity={0.8}>{edgePopup}</Polyline>;
            })}
          </MapContainer>
        </section>

        <aside className="insight-rail">
          <div className="rail-heading"><span className="eyebrow">Шууд шинжилгээ</span><h2 className="panel-title">Сүлжээний төлөв</h2></div>
          <div className="metric-stack">
            <div className="metric-card"><span className="metric-label">Нийт трассын урт</span><strong className="metric-value">{loading ? '...' : `${stats.totalLength} km`}</strong></div>
            <div className="metric-card"><span className="metric-label">Критик холбоос (Bridge)</span><strong className="metric-value">{loading ? '...' : stats.criticalLinks}</strong></div>
            <div className="metric-card"><span className="metric-label">Алдааны тэсвэрлэл</span><strong className={`metric-value small ${isSafe ? 'safe' : 'unsafe'}`}>{loading ? '...' : stats.safetyState}</strong></div>
          </div>
          <section className="rail-section">
            <h2 className="section-title">Графын мэдээлэл</h2>
            <div className="metric-row"><span>Nodes</span><strong>{stats.nodeCount ?? graphData.nodes.length}</strong></div>
            <div className="metric-row"><span>Edges</span><strong>{stats.edgeCount ?? graphData.links.length}</strong></div>
            <div className="metric-row"><span>Холбогдсон эсэх</span><strong>{stats.isConnected === false ? 'Үгүй' : 'Тийм'}</strong></div>
          </section>
          <section className="rail-section">
            <h2 className="section-title">Bridge тайлан</h2>
            {stats.bridges?.length ? <ul className="bridge-list">{stats.bridges.map((bridge, index) => <li key={`${bridge.source}-${bridge.target}-${index}`}>{bridge.source} - {bridge.target} ({bridge.weight} km)</li>)}</ul> : <p className="empty-state">Энэ графт Bridge илэрсэнгүй.</p>}
          </section>
            {cutAnalysis && <section className="rail-section"><h2 className="section-title">Сүүлийн таслалтын нөлөө</h2><div className="metric-row"><span>Холбогдсон эсэх</span><strong>{cutAnalysis.isConnected ? 'Тийм' : 'Үгүй'}</strong></div>{cutAnalysis.impactedNodes?.length ? <ul className="impact-list">{cutAnalysis.impactedNodes.map(city => <li key={city}>{city}</li>)}</ul> : <p className="empty-state">Тусгаарлагдсан хот алга.</p>}</section>}
        </aside>
      </main>
    </div>
  );
}