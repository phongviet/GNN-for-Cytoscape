import torch
import torch.nn.functional as F
from torch_geometric.nn import GCNConv
import torch.optim as optim
from torch_geometric.data import Data
import random
import pickle

# Define the GCN model
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


def train_model_GCN(edges, node_features, labels, train_test_ratio=0.9, num_epochs=100, hidden_dim=128,
                    learning_rate=0.01, weight_decay=5e-4, output_dim=7):
    # Create node mappings
    nodes = set()
    for edge in edges:
        nodes.add(edge['source'])
        nodes.add(edge['target'])
    node_mapping = {node: idx for idx, node in enumerate(nodes)}

    # Convert edges to index pairs
    edge_index = torch.tensor(
        [[node_mapping[edge['source']], node_mapping[edge['target']]] for edge in edges],
        dtype=torch.long
    ).t()

    # Convert node features to tensor (ensure the feature matrix has the correct size)
    num_nodes = len(node_mapping)

    # Assuming node_features is a list of dictionaries with feature values, convert them to a numerical tensor
    features_tensor = []
    for node in node_features:
        node_feats = node.get('Features', '')  # Get the string of features (empty string as default)
        feature_values = list(map(float, node_feats.split()))  # Split string by spaces and convert to floats
        features_tensor.append(feature_values)

    node_features_tensor = torch.tensor(features_tensor, dtype=torch.float)

    # Assuming labels is a list of string labels
    unique_labels = list(set(labels))  # Get unique string labels
    label_mapping = {label: idx for idx, label in enumerate(unique_labels)}  # Map each label to a unique integer

    # Convert string labels to corresponding integer labels using the mapping
    integer_labels = [label_mapping[label] for label in labels]

    # Convert to tensor
    labels_tensor = torch.tensor(integer_labels, dtype=torch.long)

    # Create PyTorch Geometric Data object
    graph = Data(x=node_features_tensor, edge_index=edge_index, y=labels_tensor)
    # Define device
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    graph = graph.to(device)

    # Randomly shuffle the node indices
    indices = list(range(len(node_mapping)))
    random.shuffle(indices)

    # Split the data based on train/test ratio
    train_size = int(len(indices) * train_test_ratio)

    # Generate train and test masks
    train_indices = indices[:train_size]
    test_indices = indices[train_size:]

    # Create boolean masks for train and test sets
    train_mask = torch.tensor([i in train_indices for i in range(len(node_mapping))], dtype=torch.bool)
    test_mask = torch.tensor([i in test_indices for i in range(len(node_mapping))], dtype=torch.bool)

    # Model, loss function, optimizer
    model = GCN(input_dim=node_features_tensor.size(1), hidden_dim=hidden_dim, output_dim=output_dim).to(device)
    optimizer = optim.Adam(model.parameters(), lr=learning_rate, weight_decay=weight_decay)
    criterion = torch.nn.CrossEntropyLoss()
    # print(node_features_tensor.size(1))
    # Training function
    def train():
        model.train()
        optimizer.zero_grad()
        # Forward pass
        _, out = model(graph.x, graph.edge_index)
        loss = criterion(out[train_mask], graph.y[train_mask])  # Compute the loss on training nodes
        # Backward pass and optimization
        loss.backward()
        optimizer.step()
        return loss.item()

    # Test function
    def test():
        model.eval()
        with torch.no_grad():
            _, out = model(graph.x, graph.edge_index)
            test_loss = criterion(out[test_mask], graph.y[test_mask])  # Compute loss on test nodes
            # Calculate accuracy
            _, pred = out[test_mask].max(dim=1)  # Get predicted classes
            correct = pred.eq(graph.y[test_mask]).sum().item()
            accuracy = correct / test_mask.sum().item()
        return test_loss.item(), accuracy

    # Training loop
    for epoch in range(num_epochs):
        train_loss = train()
        if epoch % 5 == 0:
            test_loss, test_acc = test()
            print(
                f"Epoch {epoch}, Train Loss: {train_loss:.4f}, Test Loss: {test_loss:.4f}, Test Accuracy: {test_acc:.4f}")

    # Save the model and embeddings after training
    model_path = 'GCN_trained_model.pth'
    embedding_path = 'GCN_embeddings.pkl'

    # Save the model
    torch.save(model.state_dict(), model_path)
    print(f"Model saved at {model_path}")

    # Get embeddings
    model.eval()
    with torch.no_grad():
        embeddings, _ = model(graph.x, graph.edge_index)

    # Save embeddings as a pickle file
    with open(embedding_path, 'wb') as f:
        pickle.dump({"embeddings": embeddings.cpu().numpy(), "node_mapping": node_mapping, "edge_index": edge_index},
                    f)  # Save embeddings and node mapping in a pickle file

    print(f"Embeddings saved at {embedding_path}")

    # Save features
    with open('features.pkl', 'wb') as f:
        pickle.dump(node_features_tensor.cpu().numpy(), f)  # Save the tensor as a numpy array
    print("Features saved")
