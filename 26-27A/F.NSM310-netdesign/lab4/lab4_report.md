# Шилэн кабелийн сүлжээний загварчлал ба симуляцийн систем

## Судалгааны тайлан ба дүгнэлт

**Хичээлийн лаборатори:** F.NSM310 Network Design  
**Төслийн нэр:** Монгол Улсын хот хоорондын шилэн кабелийн сүлжээний загварчлал  
**Хэрэгжүүлсэн технологи:** Python, Flask, NetworkX, React, React-Leaflet, Leaflet, OpenStreetMap, OSRM  
**Тайлангийн огноо:** 2026-10-17

---

## Хураангуй

Энэхүү төслөөр Монгол Улсын 22 хотыг чиглэлгүй, жигнэсэн граф хэлбэрээр загварчилж, шилэн кабелийн сүлжээний гурван өөр топологийг харьцуулан дүрслэх систем боловсруулсан. Систем нь Full Mesh, Minimum Spanning Tree (MST), 2-edge-connected графуудыг тусад нь хадгалж, хот хоорондын замын бодит зайг `distance_matrix.csv` файлаас авдаг.

MST нь нийт кабелийн уртыг багасгах зорилготой. Харин 2-edge-connected граф нь нэг Edge тасарсан үед бүх сүлжээ тасрахгүй байх 1-Fault Tolerance зорилготой. Full Mesh нь бүх боломжит хот хоорондын холбоосыг харьцуулах суурь загвар болгон ашиглагдсан.

Системийн Backend нь Flask болон NetworkX дээр, Frontend нь React болон React-Leaflet дээр хэрэгжсэн. Хэрэглэгч топологи солих, статистик харах, Map дээр Node болон Edge-ийн мэдээлэл үзэх, 2-edge-connected граф дээр Edge таслах симуляци хийх, Edge нэмэх/засах/устгах боломжтой. Physical road дүрслэлд OSRM ашиглаж, нэг удаа авсан замын геометрийг IndexedDB-д хадгалснаар Backend дахин асаах үед замын мэдээллийг дахин тооцоолох шаардлагагүй болгосон.

---

## 1. Удиртгал

Монгол Улсын аймгуудын төвүүд болон гол зангилаа хотуудыг холбосон шилэн кабелийн сүлжээ нь өргөн газар нутаг, урт замын зай, байгалийн болон техникийн эрсдэлийг тооцох шаардлагатай. Нэг кабелийн трасс тасрахад тухайн хот эсвэл бүхэл бүтэн бүс сүлжээнээс тусгаарлагдах нь Single Point of Failure үүсгэнэ.

Graph Theory ашиглан сүлжээний Node-уудыг хот, Edge-үүдийг кабелийн холбоос, Edge-ийн жинг бодит замын зай гэж загварчилж болно. Ийм загвар нь дараах асуултад хариулах боломж олгоно.

- Бүх хотыг хамгийн бага нийт зайгаар хэрхэн холбох вэ?
- Ямар Edge-үүд тасарвал сүлжээ салж болох вэ?
- Нөөц холбоос нэмж, 1-Fault Tolerant сүлжээг хэрхэн байгуулах вэ?
- Топологиудын үнэ болон найдвартай байдлыг хэрхэн харьцуулах вэ?

Энэ төслийн гол зорилго нь дээрх асуудлыг тооцоолох Backend болон үр дүнг Map дээр интерактив харуулах Frontend систем бий болгох юм.

---

## 2. Төслийн зорилго ба шаардлага

### 2.1 Үндсэн зорилго

Монгол Улсын 22 хотыг бодит авто замын зайд үндэслэн граф болгон загварчилж, дараах гурван сүлжээний төлөвийг харьцуулах:

1. **MST** - хамгийн бага нийт кабелийн урттай суурь сүлжээ.
2. **2-edge-connected** - аль нэг Edge тасарсан ч сүлжээний холболтыг хадгалах нөөцтэй сүлжээ.
3. **Full Mesh** - бүх боломжит хот хоорондын холбоосыг агуулсан харьцуулалтын загвар.

### 2.2 Функциональ шаардлагын хэрэгжилт

| Шаардлага | Хэрэгжилт |
|---|---|
| 22 хотын координат | `App.jsx` доторх `NODE_COORDINATES` объектод хадгалсан |
| Замын зай | `distance_matrix.csv` файлаас уншсан |
| Undirected weighted graph | NetworkX-ийн `Graph` ашигласан |
| MST тооцоолол | Notebook дотор `nx.minimum_spanning_tree(..., algorithm="prim")` ашигласан |
| Bridge detection | Backend дотор `nx.bridges(graph)` ашигласан |
| 2-edge-connected граф | MST дээр нэмэлт Edge-үүдийг жингээр эрэмбэлэн нэмж үүсгэсэн |
| Failure simulation | `POST /api/network/simulate-cut/` Endpoint |
| Impact analysis | Connected components болон тусгаарлагдсан хотуудыг буцаадаг |
| Map дүрслэл | React-Leaflet, OpenStreetMap, Marker, Polyline |
| Node мэдээлэл | Marker дээрх Popup |
| Edge мэдээлэл | Polyline дээрх Popup |
| Edge CRUD | `POST`, `PUT`, `DELETE /api/network/graphs/<graph>/edges` |
| Топологи сонголт | React-ийн гурван сонголтын удирдлага |
| Physical road | Leaflet Routing Machine болон OSRM |
| Local road cache | IndexedDB |

Төслийн хотууд нь бодит 22 цэгийн өгөгдөлд суурилсан тул Node нэмэх/устгах UI-г зориудаар оруулаагүй. Хотын координат болон хотын жагсаалт нь тогтмол эх өгөгдөл бөгөөд Edge өөрчлөлтийг гол хэрэглэгчийн үйлдэл болгон үлдээсэн.

---

## 3. Системийн ерөнхий архитектур

Системийг дараах гурван түвшинд хуваасан.

```text
+----------------------+       HTTP/JSON       +----------------------+
| React Frontend       | <-------------------> | Flask Backend        |
|                      |                       |                      |
| - Topology selector  |                       | - REST API           |
| - Map interface      |                       | - NetworkX graphs    |
| - Statistics panel   |                       | - Bridge detection   |
| - Cut simulation     |                       | - Cut analysis       |
+----------+-----------+                       +----------+-----------+
           |                                              |
           | IndexedDB                                    |
           v                                              v
+----------------------+                       +----------------------+
| Physical road cache  |                       | JSON graph files     |
| OSRM route geometry  |                       | distance_matrix.csv  |
+----------------------+                       +----------------------+
```

### 3.1 Backend

Backend нь дараах үүргийг гүйцэтгэнэ.

- JSON файлаас графуудыг ачаалах.
- `distance_matrix.csv` файлаас Edge-ийн жинг авах.
- Graph statistics тооцоолох.
- Bridge болон connectivity шалгах.
- Edge нэмэх, засах, устгах.
- 2-edge-connected граф дээр таслалтын симуляци хийх.

### 3.2 Frontend

Frontend нь дараах үүргийг гүйцэтгэнэ.

- Топологиудыг сонгон Map дээр зурах.
- Топологи бүрийн үр дүнг локал React state-д cache хийх.
- Marker болон Polyline Popup харуулах.
- Physical road route-ийг OSRM-ээс авах.
- Road geometry-г IndexedDB-д хадгалах.
- Статистик болон failure analysis-г харуулах.

---

## 4. Өгөгдөл бэлтгэл ба замын матриц

### 4.1 Хотын координат

Хот бүрийг дараах хэлбэрийн `Latitude, Longitude` хосоор тодорхойлсон.

```python
location = {
    "Ulaanbaatar": (47.9188, 106.9176),
    "Altai": (46.3722, 96.2583),
    "Arvaikheer": (46.2639, 102.7750),
    "Baruun-Urt": (46.6806, 113.2792),
    "Bayankhongor": (46.1167, 100.7167),
    "Bulgan": (48.8125, 103.5347),
    "Dalanzadgad": (43.5708, 104.4250),
    "Darkhan": (49.4867, 105.9228),
    "Zuunmod": (47.7069, 106.9750),
    "Mandalgovi": (45.7625, 106.2708),
    "Murun": (49.6342, 100.1625),
    "Sainshand": (44.8911, 110.1367),
    "Sukhbaatar": (50.2314, 106.2081),
    "Ulaangom": (50.0306, 92.0667),
    "Uliastai": (47.7417, 96.8444),
    "Khovd": (48.0056, 91.6300),
    "Tsetserleg": (47.4750, 101.4542),
    "Choibalsan": (48.0797, 114.5342),
    "Choir": (46.3611, 108.3611),
    "Ulgii": (48.9706, 89.9656),
    "Chinggis City": (47.3167, 110.6500),
    "Erdenet": (49.0278, 104.0450)
}
```

Frontend дээр Map Marker зурахдаа эдгээр координатыг ашиглана. Жишээлбэл:

```javascript
const coordinatesFor = (nodeId) => NODE_COORDINATES[nodeId];
```

Энэ хувилбар нь тогтмол 22 хотын өгөгдлийг ойлгомжтой байлгах зорилготой. Шинэ хот нэмэх бол координатын объект болон замын матрицад хамтад нь нэмэх шаардлагатай.

### 4.2 Замын зай авах

`distance_matrix.py` файлд OSRM Table API ашиглан хот хоорондын driving distance-г авсан.

```python
coordinates_list = []

for city in city_names:
    lat = location[city][0]
    lon = location[city][1]
    coordinates_list.append(f"{lon},{lat}")

coords_string = ";".join(coordinates_list)
url = f"http://router.project-osrm.org/table/v1/driving/{coords_string}?annotations=distance"
response = requests.get(url)
```

OSRM нь зайг meter хэлбэрээр буцаадаг тул километр рүү хөрвүүлсэн.

```python
distances_meters = data['distances']
distances_km = []

for row in distances_meters:
    row_km = [round(meters / 1000, 1) for meters in row]
    distances_km.append(row_km)

df_distance_matrix = pd.DataFrame(
    distances_km,
    index=city_names,
    columns=city_names
)
df_distance_matrix.to_csv('distance_matrix.csv', index=True)
```

Ингэснээр Edge үүсгэх үед хэрэглэгч жинг гараар бичих шаардлагагүй болсон. Backend тухайн хоёр хотын зайг автоматаар матрицаас хайна.

```python
DISTANCES = {}
with open('distance_matrix.csv', newline='', encoding='utf-8') as f:
    for row in csv.DictReader(f):
        city = row['']
        DISTANCES[city] = {
            name: float(value)
            for name, value in row.items()
            if name and value
        }
```

---

## 5. Graph үүсгэлт ба алгоритм

### 5.1 Full Mesh graph

Замын матрицын мөр бүрийг уншиж, хотуудын хоорондын холбоосыг жинтэй Edge болгон нэмсэн.

```python
cities_graph = nx.Graph()
edges = []

with open("distance_matrix.csv", mode="r", encoding="utf-8") as file:
    reader = csv.reader(file)
    header = next(reader)

    for row in reader:
        origin_city = row[0]
        for i in range(1, len(row)):
            destination_city = header[i]
            distance = float(row[i])

            if origin_city != destination_city:
                edges.append((origin_city, destination_city, distance))

cities_graph.add_weighted_edges_from(edges)
```

`nx.Graph()` нь чиглэлгүй Graph тул `A -> B` болон `B -> A` холбоосыг нэг ижил Edge гэж үзнэ.

### 5.2 Minimum Spanning Tree

MST нь бүх Node-ыг холбосон боловч цикл агуулаагүй, нийт жингийн хувьд хамгийн бага модон бүтэц юм.

```python
mst_graph = nx.minimum_spanning_tree(
    cities_graph,
    algorithm="prim"
)
```

Prim-ийн алгоритм нь нэг Node-оос эхэлж, одоогийн модтой хамгийн бага жинтэй Edge-ийг дараалан сонгодог. Энэ нь кабелийн нийт урт болон анхны CAPEX зардлыг багасгах үндсэн шийдэл болсон.

MST-ийн давуу тал:

- Нийт замын урт хамгийн бага.
- Бүх хот холбогдоно.
- Тооцоолох бүтэц энгийн.

MST-ийн сул тал:

- Бүх Edge нь Bridge байх боломжтой.
- Нэг кабель тасрахад зарим хот тусгаарлагдана.
- 1-Fault Tolerance хангахгүй.

### 5.3 Bridge detection

Bridge гэдэг нь тухайн Edge-ийг устгахад графын connectivity мууддаг Edge юм.

```python
bridges = list(nx.bridges(graph))
bridge_details = [
    {
        'source': source,
        'target': target,
        'weight': graph[source][target].get('weight', 0)
    }
    for source, target in bridges
]
```

NetworkX-ийн `nx.bridges()` нь Bridge detection-д Tarjan-ийн санаанд суурилсан DFS арга ашигладаг. Backend нь Bridge-ийн тоо, хотууд, жинг UI-д буцаана.

### 5.4 2-edge-connected graph

2-edge-connected граф нь ямар ч нэг Edge тасарсан үед бүх Node хоорондоо холбогдсон хэвээр байх ёстой.

Энэ төслийн graph generation алхамд:

1. Full Mesh-ээс MST үүсгэсэн.
2. MST-д ороогүй Edge-үүдийг ялгасан.
3. Тэдгээрийг жингээр өсөхөөр эрэмбэлсэн.
4. Bridge байгаа үед хамгийн бага жинтэй боломжит Edge-ийг нэмсэн.
5. Нэмсний дараа Bridge-ийн тоог шалгасан.
6. Bridge-ийн тоо сайжраагүй бол тухайн Edge-ийг буцаан устгасан.
7. Bridge үлдээгүй үед зогсоосон.

```python
two_edge_connected_graph = mst_graph.copy()
unused_edges = []

for u, v, data in cities_graph.edges(data=True):
    if not two_edge_connected_graph.has_edge(u, v):
        unused_edges.append((u, v, data["weight"]))

unused_edges.sort(key=lambda item: item[2])

for u, v, weight in unused_edges:
    current_bridges = list(nx.bridges(two_edge_connected_graph))
    if not current_bridges:
        break

    two_edge_connected_graph.add_edge(u, v, weight=weight)
    new_bridges = list(nx.bridges(two_edge_connected_graph))

    if len(new_bridges) >= len(current_bridges):
        two_edge_connected_graph.remove_edge(u, v)
```

Энэ нь MST дээр хамгийн бага нэмэлт кабелиар redundancy үүсгэх greedy стратеги юм. Энэ арга нь практик лабораторийн зорилгод тохиромжтой боловч бүх боломжит 2-edge-connected графуудаас математик утгаараа заавал глобал optimum байх баталгаа биш.

---

## 6. Flask Backend хэрэгжилт

### 6.1 Graph файл ачаалах

Backend эхлэх үед бэлэн JSON графуудыг санах ойд ачаална.

```python
with open('mesh_graph.json', 'r') as f:
    cities_graph = nx.node_link_graph(json.load(f))

with open('two_edge_graph.json', 'r') as f:
    two_edge_connected_graph = nx.node_link_graph(json.load(f))

with open('mst_graph.json', 'r') as f:
    mst_graph = nx.node_link_graph(json.load(f))
```

Графуудыг нэрээр нь сонгоход хялбар байлгахын тулд dictionary ашигласан.

```python
GRAPHS = {
    'mesh': cities_graph,
    'two_edge': two_edge_connected_graph,
    'mst': mst_graph,
}
```

### 6.2 Нэгдсэн статистикийн функц

Бүх API response ижил бүтэцтэй байхын тулд `graph_response()` функцийг ашигласан.

```python
def graph_response(graph):
    graph_data = nx.node_link_data(graph)
    bridges = list(nx.bridges(graph))
  bridge_details = [
    {
      'source': source,
      'target': target,
      'weight': graph[source][target].get('weight', 0)
    }
    for source, target in bridges
  ]
    is_connected = (
        nx.is_connected(graph)
        if graph.number_of_nodes()
        else True
    )

    graph_data['stats'] = {
        'totalLength': round(
            sum(
                data.get('weight', 0)
                for _, _, data in graph.edges(data=True)
            ),
            2
        ),
        'criticalLinks': len(bridges),
        'safetyState': (
            'Тийм (1-Fault Tolerant)'
            if len(bridges) == 0 and is_connected
            else 'Үгүй (0-Fault Tolerant)'
        ),
        'isConnected': is_connected,
        'nodeCount': graph.number_of_nodes(),
        'edgeCount': graph.number_of_edges(),
        'bridges': bridge_details,
    }
    return graph_data
```

Энэ функц дараах мэдээллийг Frontend-д өгнө.

- Нийт Edge-ийн жин буюу нийт кабелийн урт.
- Bridge-ийн тоо.
- Fault tolerance-ийн төлөв.
- Graph connected эсэх.
- Node болон Edge-ийн тоо.
- Bridge бүрийн эхлэх хот, хүрэх хот, жин.

### 6.3 API Endpoint-ууд

| Method | Endpoint | Үүрэг |
|---|---|---|
| GET | `/` | Backend ажиллаж байгаа эсэхийг шалгах |
| GET | `/api/network/topologies` | Бүх топологиудыг нэг response-оор авах |
| GET | `/api/network/topology/mesh` | Full Mesh авах |
| GET | `/api/network/topology/two_edge` | 2-edge-connected граф авах |
| GET | `/api/network/topology/mst` | MST авах |
| POST | `/api/network/graphs/<graph>/edges` | Matrix-ийн жингээр Edge нэмэх |
| PUT | `/api/network/graphs/<graph>/edges` | Existing Edge-ийн жинг matrix-ээс шинэчлэх |
| DELETE | `/api/network/graphs/<graph>/edges` | Edge устгах |
| POST | `/api/network/simulate-cut/` | 2-edge-connected Edge таслах |

### 6.4 Edge нэмэх, засах, устгах

Edge weight-ийг хэрэглэгчээс авахгүй, замын матрицаас автоматаар хайдаг.

```python
source = data.get('source') or data.get('a')
target = data.get('target') or data.get('b')

weight = DISTANCES.get(source, {}).get(target)
if weight is None:
    weight = DISTANCES.get(target, {}).get(source)

if weight is None:
    return error_response(
        'Эдгээр хотын зай distance_matrix.csv файлд байхгүй байна.',
        404
    )
```

Edge нэмэх үед:

```python
if request.method == 'POST':
    if graph.has_edge(source, target):
        return error_response('Энэ Edge аль хэдийн байна.', 409)
    graph.add_edge(source, target, weight=weight)
```

Edge засах үед мөн матрицын утгыг ашиглана.

```python
elif request.method == 'PUT':
    if not graph.has_edge(source, target):
        return error_response('Энэ Edge байхгүй байна.', 404)
    graph[source][target]['weight'] = weight
```

Энэ шийдэл нь хэрэглэгчийн буруу зай оруулах эрсдэлийг арилгаж, бүх Edge-ийн жинг нэг эх сурвалжаас авдаг болгосон.

### 6.5 Cut simulation ба impact analysis

Зөвхөн 2-edge-connected граф дээр Edge таслах үйлдлийг зөвшөөрсөн.

```python
if not two_edge_connected_graph.has_edge(origin, destination):
    return jsonify({
        "error": "Энэ Edge 2-edge-connected графт байхгүй байна."
    }), 404

two_edge_connected_graph.remove_edge(origin, destination)
```

Edge устгасны дараа connected components-ийг олно.

```python
components = [
    sorted(component)
    for component in nx.connected_components(
        two_edge_connected_graph
    )
]

main_component = max(components, key=len) if components else []
impacted_nodes = sorted(
    node
    for component in components
    for node in component
    if node not in main_component
)
```

Frontend-д дараах analysis response буцаана.

```python
response['analysis'] = {
    'isConnected': len(components) <= 1,
    'components': components,
    'impactedNodes': impacted_nodes,
    'removedEdge': {
        'source': origin,
        'target': destination
    },
}
```

Ингэснээр хэрэглэгч Edge тасалсны дараа:

- Сүлжээ бүхэлдээ connected хэвээр үлдсэн эсэх.
- Ямар Node-ууд үндсэн компонентоос салсан эсэх.
- Аль Edge устсан эсэх.
- Шинэ Bridge болон статистик ямар болсон эсэхийг харна.

---

## 7. React Frontend хэрэгжилт

### 7.1 Топологи сонголт

Frontend гурван графын payload-ийг React state-д cache хийнэ.

```javascript
const [graphCache, setGraphCache] = useState({
  mst: null,
  two_edge: null,
  mesh: null
});

const graphData = graphCache[topologyType] || {
  nodes: [],
  links: []
};
```

Топологи солих бүрд тухайн граф өмнө нь ачаалагдсан бол API руу дахин хүсэлт явуулахгүй.

```javascript
useEffect(() => {
  if (graphCache[topologyType]) {
    return;
  }

  const fetchGraph = async () => {
    const response = await fetch(
      `http://localhost:5000/api/network/topology/${topologyType}`
    );
    const data = await response.json();
    setGraphCache(prev => ({
      ...prev,
      [topologyType]: data
    }));
  };

  fetchGraph();
}, [topologyType, graphCache]);
```

Топологи бүр өөр өнгөтэй.

```javascript
const TOPOLOGY_COLORS = {
  mst: 'red',
  two_edge: 'blue',
  mesh: 'green'
};
```

### 7.2 Map дүрслэл

React-Leaflet-ийн `MapContainer`, `TileLayer`, `Marker`, `Polyline` компонентуудыг ашигласан.

```jsx
<MapContainer center={[46.8625, 103.8467]} zoom={5}>
  <TileLayer
    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
  />

  {graphData.nodes.map(node => {
    const coords = coordinatesFor(node.id);
    if (!coords) return null;

    return (
      <Marker key={node.id} position={coords}>
        <Popup>
          <strong>{node.id}</strong><br />
          Өргөрөг: {coords[0]}<br />
          Уртраг: {coords[1]}
        </Popup>
      </Marker>
    );
  })}
</MapContainer>
```

Edge бүрийг straight line эсвэл physical road хэлбэрээр харуулна.

```jsx
return (
  <Polyline
    positions={[sourceCoords, targetCoords]}
    color={activeColor}
    weight={3}
    opacity={0.8}
  >
    <Popup>
      {link.source} - {link.target}<br />
      Жин: {link.weight ?? 'байхгүй'} km
    </Popup>
  </Polyline>
);
```

### 7.3 Responsive UI layout

UI-г гурван хэсэгт хуваасан.

```text
+------------------+------------------------+------------------+
| Control rail     | Main map               | Insight rail     |
|                  |                        |                  |
| Graph select     | Leaflet map            | Total length     |
| Cut simulation   | Markers                | Bridges          |
| Edge editor      | Polylines              | Fault tolerance  |
+------------------+------------------------+------------------+
```

Desktop дэлгэц дээр зүүн талд Control rail, голд Map, баруун талд Insight rail байрлана. Жижиг дэлгэц дээр Control rail нь drawer хэлбэрээр нээгдэж, Insight rail нь Map дээр compact panel хэлбэрээр байрлана.

### 7.4 Statistics panel

Backend-ээс ирсэн `stats` объект шууд UI-д холбогдоно.

```jsx
<div className="metric-card">
  <span className="metric-label">Нийт трассын урт</span>
  <strong className="metric-value">
    {loading ? '...' : `${stats.totalLength} km`}
  </strong>
</div>

<div className="metric-card">
  <span className="metric-label">Критик холбоос (Bridge)</span>
  <strong className="metric-value">
    {loading ? '...' : stats.criticalLinks}
  </strong>
</div>
```

Bridge report хэсэг нь бүх Bridge-ийг жагсаана.

```jsx
{stats.bridges?.length ? (
  <ul className="bridge-list">
    {stats.bridges.map((bridge, index) => (
      <li key={`${bridge.source}-${bridge.target}-${index}`}>
        {bridge.source} - {bridge.target} ({bridge.weight} km)
      </li>
    ))}
  </ul>
) : (
  <p className="empty-state">Энэ графт Bridge илэрсэнгүй.</p>
)}
```

---

## 8. Physical road route ба local cache

Straight geographic line нь хоёр хотын координатыг шууд холбоно. Харин physical road горимд Leaflet Routing Machine ашиглаж, OSRM-ээс авто замын дагуух route geometry авна.

```javascript
const routingControl = L.Routing.control({
  waypoints: [
    L.latLng(sourceCoords[0], sourceCoords[1]),
    L.latLng(targetCoords[0], targetCoords[1])
  ],
  show: false,
  addWaypoints: false,
  routeWhileDragging: false,
  fitSelectedRoutes: false,
  createMarker: () => null
});
```

Анхны хувилбарт зам бүрийг дахин OSRM-ээс авах асуудал байсан. Үүнийг шийдэхийн тулд route coordinates-ийг IndexedDB-д хадгалсан.

```javascript
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
```

Cache-д route байхгүй бол OSRM хүсэлт явуулна. Байвал шууд `Polyline` болгон зурна.

```jsx
const roadKey = `${link.source}->${link.target}`;
const cachedRoute = roadCache[roadKey];

if (cachedRoute) {
  return (
    <Polyline
      positions={cachedRoute}
      color={activeColor}
      weight={4}
      opacity={0.8}
    />
  );
}
```

Full Mesh олон Edge-тэй учраас бүх route-ийг `localStorage`-д хадгалах үед browser quota хэтэрсэн. Тиймээс IndexedDB сонгосон. Энэ нь илүү их structured data хадгалах боломжтой бөгөөд Python Backend дахин асаасан ч browser дахь route cache хэвээр үлдэнэ.

---

## 9. Failure simulation-ийн хэрэглэгчийн урсгал

1. Хэрэглэгч Control rail-аас `2-edge-connected` граф сонгоно.
2. Эхлэх хот болон хүрэх хотын нэрийг оруулна.
3. `Сонгосон ирмэгийг таслах` товч дарна.
4. React `POST /api/network/simulate-cut/` хүсэлт илгээнэ.
5. Flask Edge байгаа эсэхийг шалгана.
6. Edge-ийг устгана.
7. NetworkX ашиглан components болон Bridge-үүдийг дахин тооцно.
8. Backend шинэ graph болон analysis буцаана.
9. React зөвхөн `two_edge` cache-ийг шинэчилнэ.
10. Map, статистик, Bridge report, Impact report дахин дүрслэгдэнэ.

Бусад `MST` болон `Full mesh` графууд таслалтын үйлдлээс хамгаалагдсан. Учир нь failure simulation нь төслийн шаардлагын дагуу 2-edge-connected сүлжээний redundancy-г шалгах зориулалттай.

---

## 10. Хэрэгжүүлэлтийн үр дүн

### 10.1 Хэрэгжсэн үндсэн боломжууд

- 22 хотын координаттай Map.
- Full Mesh, MST, 2-edge-connected гурван topology.
- Prim алгоритм ашигласан MST generation.
- NetworkX Bridge detection.
- Нийт кабелийн урт тооцоолох.
- Bridge count тооцоолох.
- Connectivity шалгах.
- 1-Fault Tolerance төлөв харуулах.
- Edge таслалтын simulation.
- Impacted city жагсаалт.
- Edge нэмэх, засах, устгах.
- `distance_matrix.csv`-ээс автомат Edge weight.
- Node болон Edge Popup.
- Physical road route.
- IndexedDB route cache.
- Responsive side-panel layout.
- Монгол хэлний UI.

### 10.2 Cost болон security-ийн харьцуулалт

| Топологи | Нийт Edge | Кабелийн өртөг | Fault tolerance | Гол хэрэглээ |
|---|---:|---:|---|---|
| MST | Бага | Хамгийн бага | Байхгүй | Хямд суурь сүлжээ |
| 2-edge-connected | MST-ээс их | Дунд | Тийм | Практик найдвартай сүлжээ |
| Full Mesh | Маш их | Хамгийн өндөр | Маш өндөр | Харьцуулалт, redundancy-ийн дээд хязгаар |

MST нь зардал багатай боловч Bridge олонтой байж болно. Full Mesh нь найдвартай боловч хэт олон кабель шаардана. 2-edge-connected граф нь эдгээрийн дундах Cost vs Security Trade-off-ийн практик шийдэл юм.

---

## 11. Шалгалт ба баталгаажуулалт

### 11.1 Static validation

Frontend дээр дараах шалгалтуудыг хийсэн.

```text
npm run lint
```

```text
npm run build
```

Backend дээр:

```text
python3 -m py_compile app.py
```

### 11.2 Функциональ шалгалт

- Топологи солиход граф дахин зөв дүрслэгдсэн.
- Нэг графыг дахин сонгоход React-ийн cache ашиглагдсан.
- Edge нэмэхэд жин `distance_matrix.csv`-ээс автоматаар авсан.
- Existing Edge засахад мөн матрицын зай ашигласан.
- Байхгүй хотын Edge хүсэлт 404 буцаасан.
- Байхгүй Edge устгах хүсэлт 404 буцаасан.
- MST дээр Bridge-үүд харагдсан.
- 2-edge-connected дээр Bridge байхгүй төлөвийг шалгасан.
- Cut simulation-ийн дараа statistics шинэчлэгдсэн.
- Тусгаарлагдсан хотууд Impact report-д гарсан.
- Marker болон Polyline дээр Popup нээгдсэн.
- Physical road route cache IndexedDB-д хадгалагдсан.
- Full Mesh route-үүдийг local cache ашиглан дахин дүрсэлсэн.
- Responsive layout desktop болон mobile хэмжээнд тохируулсан.

### 11.3 Гүйцэтгэлийн үнэлгээ

22 Node бүхий графын Bridge detection, connectivity analysis, statistics calculation нь жижиг хэмжээний өгөгдөл тул локал Backend дээр богино хугацаанд хийгдэнэ. Харин physical road routing нь OSRM-ийн гадаад сүлжээний хүсэлтээс хамаардаг. Иймээс OSRM-ийн анхны хүсэлтийн хугацаа 1 секундээс их байж болох боловч IndexedDB cache ашигласнаар дараагийн дүрслэлүүдэд дахин хүсэлт явуулахгүй.

---

## 12. Хязгаарлалт ба цаашдын сайжруулалт

### 12.1 Хязгаарлалт

1. **Node-ийн тоо тогтмол:** Одоогийн системийн 22 хотын coordinate map тогтмол өгөгдөлтэй. Node add/edit/delete UI-г complexity багасгахын тулд хассан.
2. **2-edge graph generation нь greedy:** Нэмэлт Edge сонголт нь жингийн дараалалд суурилсан практик greedy алгоритм бөгөөд бүх боломжит глобал optimum-г баталгаажуулахгүй.
3. **Latency хэмжилт байхгүй:** Distance болон route geometry байгаа боловч бодит Round-Trip Time хэмжилтийг систем одоогоор хийдэггүй. 50ms шаардлагыг батлахын тулд хот тус бүрийн network ping эсвэл latency model хэрэгтэй.
4. **Traffic rerouting нь simulation түвшинд:** Cut simulation нь graph-ийн шинэ connectivity-г харуулдаг боловч бодит network traffic packet rerouting хэрэгжүүлээгүй.
5. **Natural hazard layer байхгүй:** Үер, нуранги, шороон шуурганы risk layer optional тул одоогийн хувилбарт оруулаагүй.
6. **TailwindCSS ашиглаагүй:** Frontend-ийн responsive styling-г dependency нэмэхгүй байлгахын тулд custom CSS-ээр хийсэн. React-ийн шаардлагатай интерактив бүтэц хэвээр хадгалагдсан.

### 12.2 Цаашдын сайжруулалт

- Node болон coordinate өгөгдлийг тусдаа JSON эсвэл database-д шилжүүлэх.
- Graph generation-ийг Backend startup үед автоматаар хийх.
- 2-edge-connected augmentation-д exact optimization эсвэл integer programming ашиглах.
- Latency API нэмэх.
- Risk score бүхий GIS layer нэмэх.
- Authentication болон user-level graph versions нэмэх.
- Graph edit хийсний дараа MST болон 2-edge-connected графыг автоматаар дахин тооцох.
- OSRM-ийн local instance ашиглах.
- Unit test болон API integration test нэмэх.

---

## 13. Дүгнэлт

Энэ төслөөр Монгол Улсын 22 хотыг замын бодит зайд үндэслэн граф болгон загварчилж, Full Mesh, MST, 2-edge-connected гурван өөр топологийн харьцуулалтын систем боловсруулсан.

Prim алгоритмаар үүсгэсэн MST нь нийт кабелийн уртыг багасгасан боловч Bridge болон Single Point of Failure үүсгэх сул талтай. MST дээр бага жинтэй нэмэлт Edge-үүдийг сонгон нэмж, 2-edge-connected бүтэц үүсгэснээр нэг кабель тасарсан нөхцөлд сүлжээг хадгалах боломж бүрдсэн. NetworkX-ийн Bridge detection болон connected-components analysis нь сүлжээний найдвартай байдлыг тооцоолоход ашиглагдсан.

Flask Backend нь графын тооцоолол, API response, Edge management, failure simulation-г хариуцдаг. React Frontend нь эдгээр үр дүнг интерактив Map, Popup, statistics panel, bridge report хэлбэрээр харуулдаг. `distance_matrix.csv`-ийг Edge weight-ийн ганц эх сурвалж болгосноор хэрэглэгчийн гараар буруу зай оруулах асуудал арилсан. Physical road geometry-г IndexedDB-д хадгалснаар OSRM-ийн давтан хүсэлтийг багасгасан.

Төслийн гол үр дүн нь зардал багатай MST болон найдвартай 2-edge-connected сүлжээний ялгааг нэг интерактив орчинд харуулах боломж бүрдсэн явдал юм. Систем нь сургалтын болон анхан шатны network planning simulation-д ашиглахад тохиромжтой суурь шийдэл болсон.

---

## 14. Ашигласан файлууд

| Файл | Үүрэг |
|---|---|
| `distance_matrix.py` | OSRM-ээс хот хоорондын зай авах |
| `distance_matrix.csv` | 22 хотын замын зайны матриц |
| `netDesign.ipynb` | Full Mesh, MST, 2-edge-connected граф үүсгэх |
| `mesh_graph.json` | Full Mesh графын хадгалсан өгөгдөл |
| `mst_graph.json` | MST графын хадгалсан өгөгдөл |
| `two_edge_graph.json` | 2-edge-connected графын хадгалсан өгөгдөл |
| `app.py` | Flask REST API болон NetworkX analysis |
| `my-graph-app/src/App.jsx` | React UI, Map, simulation, editor |
| `my-graph-app/src/App.css` | Responsive workspace styling |
| `my-graph-app/src/index.css` | Global browser styling |
| `my-graph-app/src/main.jsx` | React entry point |

---

## Эцсийн үнэлгээ

Төслийн үндсэн функциональ шаардлагууд хэрэгжсэн. Үүнд граф үүсгэлт, MST, Bridge detection, 2-edge-connected бүтэц, API, Map дүрслэл, failure simulation, impact analysis, Edge management, автомат зай lookup, caching, responsive UI багтана.

Бодит RTT latency, байгалийн эрсдэлийн GIS layer, бодит traffic rerouting зэрэг нь нэмэлт судалгаа болон гадаад өгөгдөл шаарддаг тул дараагийн хөгжүүлэлтийн чиглэлээр үлдсэн.
