from flask import Flask, request, jsonify
from flask_cors import CORS
import networkx as nx
import csv
import json

app = Flask(__name__)
CORS(app)

with open('mesh_graph.json', 'r') as f:
    cities_graph = nx.node_link_graph(json.load(f))

with open('two_edge_graph.json', 'r') as f:
    two_edge_connected_graph = nx.node_link_graph(json.load(f))

with open('mst_graph.json', 'r') as f:
    mst_graph = nx.node_link_graph(json.load(f))

DISTANCES = {}
with open('distance_matrix.csv', newline='', encoding='utf-8') as f:
    for row in csv.DictReader(f):
        city = row['']
        DISTANCES[city] = {name: float(value) for name, value in row.items() if name and value}
print(DISTANCES)
GRAPHS = {
    'mesh': cities_graph,
    'two_edge': two_edge_connected_graph,
    'mst': mst_graph,
}

def get_graph(graph_name):
    graph = GRAPHS.get(graph_name)
    if graph is None:
        raise KeyError(graph_name)
    return graph

def graph_response(graph):
    graph_data = nx.node_link_data(graph)
    bridges = list(nx.bridges(graph))
    bridge_details = [
        {'source': source, 'target': target, 'weight': graph[source][target].get('weight', 0)}
        for source, target in bridges
    ]
    is_connected = nx.is_connected(graph) if graph.number_of_nodes() else True
    graph_data['stats'] = {
        'totalLength': round(sum(data.get('weight', 0) for _, _, data in graph.edges(data=True)), 2),
        'criticalLinks': len(bridges),
        'safetyState': 'Тийм (1-Fault Tolerant)' if len(bridges) == 0 and is_connected else 'Үгүй (0-Fault Tolerant)',
        'isConnected': is_connected,
        'nodeCount': graph.number_of_nodes(),
        'edgeCount': graph.number_of_edges(),
        'bridges': bridge_details,
    }
    return graph_data

def error_response(message, status):
    return jsonify({'error': message}), status

@app.route('/', methods=['GET'])
def home_page():
    return jsonify({
                    "status: " : "Web running",
                    "port number:" : 5000
                    })
# GET TOPOLOGY
@app.route('/api/network/topologies', methods=['GET'])
def get_all_topologies():
    return jsonify({name: graph_response(graph) for name, graph in GRAPHS.items()})

@app.route('/api/network/topology/mesh', methods=['GET'])
def get_graph_mesh():
    mesh_dict = graph_response(cities_graph)
    return jsonify(mesh_dict)
@app.route('/api/network/topology/two_edge', methods=['GET'])
def get_graph_two_edge():
    two_edge_dict = graph_response(two_edge_connected_graph)
    return jsonify(two_edge_dict)
@app.route('/api/network/topology/mst', methods=['GET'])
def get_graph_mst():
    mst_dict = graph_response(mst_graph)
    return jsonify(mst_dict)

@app.route('/api/network/graphs/<graph_name>/edges', methods=['POST'])
def manage_edges(graph_name):
    try:
        graph = get_graph(graph_name)
    except KeyError:
        return error_response('Тодорхойгүй граф. mesh, two_edge эсвэл mst ашиглана уу.', 404)

    data = request.get_json(silent=True) or {}
    source = data.get('source') or data.get('a')
    target = data.get('target') or data.get('b')
    if not source or not target or source == target:
        return error_response('Same input.', 400)

    weight = DISTANCES.get(source, {}).get(target)
    if weight is None:
        weight = DISTANCES.get(target, {}).get(source)
    if weight is None:
        return error_response('Эдгээр хотын зай distance_matrix.csv файлд байхгүй байна.', 404)

    if request.method == 'POST':
        if graph.has_edge(source, target):
            return error_response('Энэ Edge аль хэдийн байна.', 409)
        graph.add_edge(source, target, weight=weight)
    else:
        return error_response('Error', 404)

    return jsonify(graph_response(graph))
    
# SIMULATE CUT
# {"a":<origin>,"b":<destination>}
@app.route('/api/network/simulate-cut/', methods =['POST'])
def cut_edge():
    data = request.get_json(silent=True)
    if not data:
        return jsonify({"error": "Хүсэлтийн өгөгдөл шаардлагатай."}), 400

    origin = data.get('a')
    destination = data.get('b')
    if not origin or not destination:
        return jsonify({"error": "Хоёр хотын нэр шаардлагатай."}), 400
    if origin == destination:
        return jsonify({"error": "Хоёр хот ялгаатай байх ёстой."}), 400
    if not two_edge_connected_graph.has_edge(origin, destination):
        return jsonify({"error": "Энэ Edge 2-edge-connected графт байхгүй байна."}), 404

    two_edge_connected_graph.remove_edge(origin, destination)
    components = [sorted(component) for component in nx.connected_components(two_edge_connected_graph)]
    main_component = max(components, key=len) if components else []
    impacted_nodes = sorted(node for component in components for node in component if node not in main_component)
    response = graph_response(two_edge_connected_graph)
    response['analysis'] = {
        'isConnected': len(components) <= 1,
        'components': components,
        'impactedNodes': impacted_nodes,
        'removedEdge': {'source': origin, 'target': destination},
    }
    return jsonify(response), 200

app.run(port=5000)
