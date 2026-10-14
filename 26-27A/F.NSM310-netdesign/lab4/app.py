from flask import Flask, request, jsonify
from flask_cors import CORS
import networkx as nx
import json

app = Flask(__name__)
CORS(app)

with open('mesh_graph.json', 'r') as f:
    cities_graph = nx.node_link_graph(json.load(f))

with open('two_edge_graph.json', 'r') as f:
    two_edge_connected_graph = nx.node_link_graph(json.load(f))

with open('mst_graph.json', 'r') as f:
    mst_graph = nx.node_link_graph(json.load(f))

def graph_response(graph):
    graph_data = nx.node_link_data(graph)
    bridge_count = len(list(nx.bridges(graph)))
    graph_data['stats'] = {
        'totalLength': round(sum(data.get('weight', 0) for _, _, data in graph.edges(data=True)), 2),
        'criticalLinks': bridge_count,
        'safetyState': 'Тийм (1-Fault Tolerant)' if bridge_count == 0 else 'Үгүй (0-Fault Tolerant)'
    }
    return graph_data

@app.route('/', methods=['GET'])
def home_page():
    return jsonify({
                    "status: " : "Web running",
                    "port number:" : 5000
                    })
# GET TOPOLOGY
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
    
# SIMULATE CUT
# {"a":<origin>,"b":<destination>}
@app.route('/api/network/simulate-cut/', methods =['POST'])
def cut_edge():
    data = request.get_json(silent=True)
    if not data:
        return jsonify({"error": "Request body is required."}), 400

    origin = data.get('a')
    destination = data.get('b')
    if not origin or not destination:
        return jsonify({"error": "Both city names are required."}), 400
    if origin == destination:
        return jsonify({"error": "The two cities must be different."}), 400
    if not two_edge_connected_graph.has_edge(origin, destination):
        return jsonify({"error": "That edge does not exist in the 2-edge-connected graph."}), 404

    two_edge_connected_graph.remove_edge(origin, destination)
    return jsonify(graph_response(two_edge_connected_graph)), 200

app.run(port=5000)
