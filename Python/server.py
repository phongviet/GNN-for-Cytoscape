from flask import Flask, request, jsonify
from train_Node2Vec import train_model
from predict_Node2Vec import predict_node_label

app = Flask(__name__)

@app.route('/receive_edge_indices', methods=['POST'])
def receive_edge_indices():
    try:
        data = request.get_json()
        edges = data.get('edge_index', [])
        if not edges:
            return jsonify({"status": "error", "message": "No edges provided"}), 400

        train_model(edges)
        return jsonify({"status": "success", "message": "Edges received and Node2Vec model trained"}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

@app.route('/predict_node_label', methods=['POST'])
def predict_node_label_endpoint():
    try:
        data = request.get_json()
        node_name = data.get('node_name')
        if not node_name:
            return jsonify({"status": "error", "message": "Node name not provided"}), 400

        result = predict_node_label(node_name)
        return jsonify({"status": "success", "message": result}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
