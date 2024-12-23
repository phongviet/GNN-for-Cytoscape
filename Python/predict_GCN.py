import pickle
import torch
from torch_geometric.data import Data
from torch_geometric.nn import GCNConv
import torch.nn.functional as F

# GCN Model Definition
class GCN(torch.nn.Module):
    def __init__(self, input_dim, hidden_dim, output_dim):
        super(GCN, self).__init__()
        self.conv1 = GCNConv(input_dim, hidden_dim)
        self.conv2 = GCNConv(hidden_dim, hidden_dim)
        self.conv3 = GCNConv(hidden_dim, output_dim)

    def forward(self, x, edge_index):
        # First GCN layer
        x = F.relu(self.conv1(x, edge_index))
        # Second GCN layer
        x = F.relu(self.conv2(x, edge_index))
        # Third GCN layer
        x = self.conv3(x, edge_index)
        return x, F.log_softmax(x, dim=1)

# Function to load the model
def load_model(model_path):
    model = GCN(input_dim=1433, hidden_dim=128, output_dim=7)
    model.load_state_dict(torch.load(model_path))
    model.eval()
    return model

# Function to predict node label (aligns with Flask route structure)
def predict_node_label_GCN(node_name):
    try:
        # Load the embeddings and node mapping from the pickle file
        with open("GCN_embeddings.pkl", "rb") as f:
            data = pickle.load(f)
            embeddings = data["embeddings"]
            node_mapping = data["node_mapping"]

        with open("features.pkl", "rb") as f:
            features = torch.tensor(pickle.load(f), dtype=torch.float)


        # Load the trained GCN model
        model = load_model("GCN_trained_model.pth")  # Load your trained model here

        # Extract edge_index
        edge_index = torch.tensor(data["edge_index"], dtype=torch.long).contiguous()

        # Check if node exists in the node_mapping
        if node_name not in node_mapping:
            return {"status": "error", "message": f"Node {node_name} not found in node mapping"}

        # Get the corresponding node embedding
        node_idx = node_mapping[node_name]

        # Create Data object with the full set of node features and the edge_index
        data = Data(x=features, edge_index=edge_index)
        print(edge_index.size())
        print(features.size())
        # Predict the label
        with torch.no_grad():
            out, _ = model(data.x, data.edge_index)  # Pass all node features to the model

        # Get the predicted class label for the specific node
        _, predicted_class = out[node_idx].max(dim=0)  # Get prediction for the specific node
        predicted_label = predicted_class.item()

        return {"status": "success", "predicted_label": predicted_label}

    except Exception as e:
        return {"status": "error", "message": str(e)}
