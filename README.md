# GNN for Cytoscape

* A project for the Object-Oriented Programming class.
* This app works by reading data and interactions from users in Cytoscape and transfer to Python for processing.

## Installation

* Install Cytoscape/target/MyApp-1.0.jar in the Cytoscape app (Apps -> App Store -> Install Apps From File) or drop the file into directory CytoscapeConfiguration/3/apps/installed

* Python version: 3.12.4
* Install Python libraries:
```bash
pip install -r /Python/requirements.txt
```

## Usage
* Activate the server to communicate with Cytoscape:
```bash
python /Python/server.py
```
* In Cytoscape tool bar: Apps -> MyApps

## Tested dataset
* https://snap.stanford.edu/biodata/datasets/10001/10001-ChCh-Miner.html
* https://snap.stanford.edu/biodata/datasets/10006/10006-DD-Miner.html
* https://pytorch-geometric.readthedocs.io/en/2.6.0/generated/torch_geometric.datasets.Planetoid.html#torch_geometric.datasets.Planetoid