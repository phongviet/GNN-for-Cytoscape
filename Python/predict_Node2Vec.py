import pickle
from sklearn.cluster import KMeans

def predict_node_label_Node2Vec(node_name):
    # Load the embeddings and node mapping
    try:
        with open("Node2Vec_embeddings.pkl", "rb") as f:
            data = pickle.load(f)
            embeddings = data["embeddings"]
            node_mapping = data["node_mapping"]

        # Check if the node exists in the mapping
        if node_name not in node_mapping:
            return f"Node '{node_name}' not found in the graph."

        # Perform K-Means clustering
        kmeans = KMeans(n_clusters=20, random_state=0)
        kmeans.fit(embeddings)

        # Get the cluster label for the node
        node_idx = node_mapping[node_name]
        cluster_label = kmeans.labels_[node_idx]

        return {"status": "success", "prediction": f"Cluster {cluster_label}"}
    except Exception as e:
        return f"Error during prediction: {e}"
