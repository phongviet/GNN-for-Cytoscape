# GNN for Cytoscape

* A project for the Object-Oriented Programming class.
* This app works by reading data and interactions from users in Cytoscape and transfer to Python for processing.

## Installation

* Python version: 3.12.4
* Create virtual environment and activate it (under Python folder, optional, recommended):
```bash
python -m venv venv
venv\Scripts\activate
```
* Install Python libraries (gpu preferred if you have one):
```bash
pip install -r requirements_gpu.txt
```
* or
```bash
pip install -r requirements_cpu.txt 
```
* then (for pyg-lib)
```bash
python req.py
```

* Install Cytoscape/target/MyApp-1.0.jar that you compiled: 
  * Cytoscape app (Apps -> App Store -> Install Apps From File) or drop the file into directory CytoscapeConfiguration/3/apps/installed
  * Alternatively, compile and run installJar.cmd under Cytoscape/src/
## Usage
* Activate the server to communicate with Cytoscape:
```bash
python /Python/server.py
```
* In Cytoscape app:
  * Import a dataset of your choice (see tested datasets for examples)
  * Train the model through the Apps dropdown menu
  * Select nodes and or edges then use the trained model to predict or just view their information

## Tested datasets
* https://snap.stanford.edu/biodata/datasets/10001/10001-ChCh-Miner.html
* https://snap.stanford.edu/biodata/datasets/10006/10006-DD-Miner.html
* https://pytorch-geometric.readthedocs.io/en/2.6.0/generated/torch_geometric.datasets.Planetoid.html#torch_geometric.datasets.Planetoid