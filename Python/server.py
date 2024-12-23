from flask import Flask, request, jsonify
from train_Node2Vec import train_model_Node2Vec
from predict_Node2Vec import predict_node_label_Node2Vec
from train_GCN import train_model_GCN
from predict_GCN import predict_node_label_GCN

app = Flask(__name__)

@app.route('/receive_edge_indices', methods=['POST'])
def receive_edge_indices():
    try:
        data = request.get_json()
        edges = data.get('edge_index', [])
        if not edges:
            return jsonify({"status": "error", "message": "No edges provided"}), 400

        train_model_Node2Vec(edges)
        return jsonify({"status": "success", "message": "Edges received and Node2Vec model trained"}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

@app.route('/predict_node_label_Node2Vec', methods=['POST'])
def predict_node_label_Node2Vec_endpoint():
    try:
        data = request.get_json()
        node_name = data.get('node_name')
        if not node_name:
            return jsonify({"status": "error", "message": "Node not provided"}), 400

        result = predict_node_label_Node2Vec(node_name)
        return jsonify({"status": "success", "message": result}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400


@app.route('/receive_edge_indices_and_features', methods=['POST'])
def receive_edge_indices_and_features():
    try:
        # Parse the JSON data from the request
        data = request.get_json()

        # Extract edge indices
        edges = data.get('edge_index', [])
        if not edges:
            return jsonify({"status": "error", "message": "No edges provided"}), 400

        # print("Received Edges:")
        # for edge in edges:
        #     print(f"{edge['source']} -> {edge['target']}")

        # Extract node features
        nodes = data.get('node_features', [])
        if not nodes:
            return jsonify({"status": "error", "message": "No node features provided"}), 400

        # print("Received Node Features:")

        # Initialize lists to store features, labels, and splits
        features = []
        labels = []
        splits = []

        for node in nodes:
            node_name = node.get("name")
            node_feats = node.get("features", {})

            # Extract 'label' and 'split' from features
            label = node_feats.get('Label')
            split = node_feats.get('Split')

            # Add to the respective lists
            labels.append(label)
            splits.append(split)

            # Remove 'label' and 'split' from features
            node_feats.pop('Label')
            node_feats.pop('Split')

            features.append(node_feats)
            # print(f"Node: {node_name}, Features: {node_feats}, Label: {label}, Split: {split}")

        # Call the train_model function
        train_model_GCN(edges, features, labels, splits)

        # Return success response
        return jsonify({"status": "success", "message": "Edges and node features received, model trained"}), 200

    except Exception as e:
        # Handle errors
        print(f"Error processing request: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 400


@app.route('/predict_node_label_GCN', methods=['POST'])
def predict_node_label_GCN_endpoint():
    try:
        data = request.get_json()
        node_name = data.get('node_name')
        if not node_name:
            return jsonify({"status": "error", "message": "Node not provided"}), 400

        result = predict_node_label_GCN(node_name)
        return jsonify({"status": "success", "message": result}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
