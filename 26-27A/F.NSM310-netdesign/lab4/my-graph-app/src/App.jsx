import { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Polyline, useMap } from 'react-leaflet';
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

// Mongolian Cities Coordinates
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
      if (coordinates?.length) onRouteFound(coordinates);
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

  const graphData = graphCache[topologyType] || { nodes: [], links: [] };

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
      setLoading(false);
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
      setCutMessage('Enter both city names.');
      return;
    }
    if (origin === destination) {
      setCutMessage('Choose two different cities.');
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
      if (!response.ok) throw new Error(data.error || 'Unable to simulate cut.');

      setGraphCache(prev => ({ ...prev, two_edge: data }));
      setCutMessage(`Cut simulated: ${origin} - ${destination}`);
      setOriginCity('');
      setDestinationCity('');
    } catch (err) {
      setCutMessage(err.message);
    } finally {
      setCutting(false);
    }
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

  return (
    <div style={{ height: "100vh", display: "flex", flexDirection: "column", fontFamily: "sans-serif" }}>

      {/* Hide Leaflet routing text overlays. */}
      <style>
        {`
          .leaflet-routing-container {
            display: none !important;
          }
        `}
      </style>
      
      {/* --- UI PANEL --- */}
      <div style={{ padding: "15px", background: "#f8f9fa", borderBottom: "2px solid #ddd", flexShrink: 0 }}>
        <h2 style={{ margin: "0 0 15px 0" }}>Сүлжээний Топологи (Network Topology)</h2>
        
        {/* Topology Buttons */}
        <div style={{ display: "flex", gap: "10px", marginBottom: "15px" }}>
          <button 
            onClick={() => setTopologyType('mst')}
            style={{ padding: "8px 16px", backgroundColor: "red", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", opacity: topologyType === 'mst' ? 1 : 0.6 }}
          >
            MST (Улаан)
          </button>
          <button 
            onClick={() => setTopologyType('two_edge')}
            style={{ padding: "8px 16px", backgroundColor: "blue", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", opacity: topologyType === 'two_edge' ? 1 : 0.6 }}
          >
            2-Edge-Connected (Цэнхэр)
          </button>
          <button 
            onClick={() => setTopologyType('mesh')}
            style={{ padding: "8px 16px", backgroundColor: "green", color: "white", border: "none", borderRadius: "4px", cursor: "pointer", opacity: topologyType === 'mesh' ? 1 : 0.6 }}
          >
            Full Mesh (Ногоон)
          </button>
        </div>

        {/* Logical vs Physical Checkbox */}
        <div style={{ marginBottom: "15px" }}>
          <label style={{ cursor: "pointer", fontSize: "14px", fontWeight: "bold" }}>
            <input 
              type="checkbox" 
              checked={useRoads} 
              onChange={(e) => setUseRoads(e.target.checked)} 
              style={{ marginRight: "8px" }}
            />
            Физик зам ашиглах (Show physical roads via OSRM)
          </label>
        </div>

        {/* Two-edge-only cut simulation */}
        <form onSubmit={simulateCut} style={{ marginBottom: "15px" }}>
          <label style={{ display: "block", fontSize: "14px", fontWeight: "bold", marginBottom: "6px" }}>
            Simulate edge cut (2-Edge-Connected only)
          </label>
          <div style={{ display: "flex", flexWrap: "wrap", gap: "8px" }}>
            <input
              value={originCity}
              onChange={(e) => setOriginCity(e.target.value)}
              placeholder="Origin city"
              disabled={topologyType !== 'two_edge' || cutting}
            />
            <input
              value={destinationCity}
              onChange={(e) => setDestinationCity(e.target.value)}
              placeholder="Destination city"
              disabled={topologyType !== 'two_edge' || cutting}
            />
            <button type="submit" disabled={topologyType !== 'two_edge' || cutting}>
              {cutting ? 'Cutting...' : 'Cut edge'}
            </button>
          </div>
          {topologyType !== 'two_edge' && (
            <div style={{ fontSize: "13px", marginTop: "6px" }}>Switch to 2-Edge-Connected to simulate a cut.</div>
          )}
          {cutMessage && <div style={{ color: "#333", fontSize: "13px", marginTop: "6px" }}>{cutMessage}</div>}
        </form>

        {/* Statistics Panel */}
        <div style={{ display: "flex", flexWrap: "wrap", gap: "20px", fontSize: "14px", backgroundColor: "#e9ecef", padding: "10px", borderRadius: "4px" }}>
          <div><strong>Нийт замын урт (km):</strong> {loading ? "..." : stats.totalLength}</div>
          <div><strong>Критик холбоос (Bridge):</strong> {loading ? "..." : stats.criticalLinks}</div>
          <div><strong>1-Fault Tolerant:</strong> {loading ? "..." : stats.safetyState}</div>
        </div>
        
        {error && <div style={{ color: "red", marginTop: "10px" }}>Error: {error}</div>}
      </div>

      {/* --- INTERACTIVE MAP --- */}
      <MapContainer center={[46.8625, 103.8467]} zoom={5} style={{ flexGrow: 1, width: "100%", zIndex: 0 }}>
        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

        {/* Draw City Markers */}
        {graphData.nodes.map(node => {
          const coords = NODE_COORDINATES[node.id];
          if (!coords) return null;
          return <Marker key={node.id} position={coords} />;
        })}

        {/* Draw Edges Based on Toggle */}
        {graphData.links.map((link, idx) => {
          const sourceCoords = NODE_COORDINATES[link.source];
          const targetCoords = NODE_COORDINATES[link.target];
          if (!sourceCoords || !targetCoords) return null;

          if (useRoads) {
            if (!roadCacheLoaded) return null;

            const roadKey = `${link.source}->${link.target}`;
            const cachedRoute = roadCache[roadKey];

            if (cachedRoute) {
              return (
                <Polyline
                  key={`cached-road-${topologyType}-${idx}`}
                  positions={cachedRoute}
                  color={activeColor}
                  weight={4}
                  opacity={0.8}
                />
              );
            }

            return (
              <RoutingEdge 
                key={`road-${topologyType}-${idx}`} 
                sourceCoords={sourceCoords} 
                targetCoords={targetCoords} 
                color={activeColor} 
                onRouteFound={(coordinates) => {
                  setRoadCache(prev => ({ ...prev, [roadKey]: coordinates }));
                }}
              />
            );
          }

          return (
            <Polyline 
              key={`straight-${topologyType}-${idx}`} 
              positions={[sourceCoords, targetCoords]} 
              color={activeColor}
              weight={3}
              opacity={0.8}
            />
          );
        })}
      </MapContainer>

    </div>
  );
}