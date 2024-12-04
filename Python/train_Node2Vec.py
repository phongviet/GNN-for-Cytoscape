import torch
from torch_geometric.data import Data
from torch_geometric.nn import Node2Vec
import pickle

node2vec_model = None
node_mapping = {}
embeddings = None

def train_model(edges):
    global node2vec_model, node_mapping, embeddings

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

    # Create a Data object
    num_nodes = len(node_mapping)
    node_features = torch.zeros(num_nodes, 1)  # Placeholder features
    graph_data = Data(x=node_features, edge_index=edge_index)

    # Train Node2Vec
    device = 'cuda' if torch.cuda.is_available() else 'cpu'
    node2vec_model = Node2Vec(graph_data.edge_index, embedding_dim=64,
                              walk_length=20, context_size=10, num_negative_samples=1).to(device)

    optimizer = torch.optim.SGD(node2vec_model.parameters(), lr=0.01)
    loader = node2vec_model.loader(batch_size=128, shuffle=True, num_workers=0)
    node2vec_model.train()

    for epoch in range(1000):  # Adjust as needed
        total_loss = 0
        for pos_rw, neg_rw in loader:
            optimizer.zero_grad()
            loss = node2vec_model.loss(pos_rw.to(device), neg_rw.to(device))
            loss.backward()
            optimizer.step()
            total_loss += loss.item()
        print(f'Epoch: {epoch}, Loss: {total_loss / len(loader)}')

    # Save the embeddings
    embeddings = node2vec_model(torch.arange(graph_data.num_nodes, dtype=torch.long).to(device)).detach().cpu().numpy()
    with open("embeddings.pkl", "wb") as f:
        pickle.dump({"embeddings": embeddings, "node_mapping": node_mapping}, f)

    print("Training complete. Embeddings saved.")
