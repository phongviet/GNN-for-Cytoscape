import torch
import subprocess
import sys
import re

def get_pytorch_version():
    """Gets the currently installed PyTorch version."""
    try:
        version = torch.__version__
        return version
    except Exception as e:
        print(f"Error getting PyTorch version: {e}")
        return None

def get_cuda_version(pytorch_version):
    """Gets the CUDA version used to build PyTorch."""
    try:
        if "+cpu" in pytorch_version:
            return "cpu"
        else:
            version_str = torch.version.cuda
            if version_str:
                match = re.match(r"(\d+)\.(\d+)", version_str)
                if match:
                    major, minor = match.groups()
                    return f"cu{major}{minor}"
                else:
                    print(f"Could not parse CUDA version from string: {version_str}")
                    return None
            else:
                return "cpu"
    except Exception as e:
        print(f"Error getting CUDA version: {e}")
        return None

def install_pyg_lib(pytorch_version, cuda_version):
    """Installs pyg-lib based on PyTorch and CUDA versions."""
    wheel_url_base = "https://data.pyg.org/whl/torch-${TORCH}+${CUDA}.html"
    # Use full version format (major.minor.patch)
    pytorch_full_version = re.match(r"(\d+\.\d+\.\d+)", pytorch_version).group(1)

    try:
        print(f"Installing pyg-lib for PyTorch {pytorch_version} and CUDA {cuda_version}...")
        command = [
            sys.executable,
            "-m",
            "pip",
            "install",
            "pyg-lib",
            "-f",
            wheel_url_base.replace("${TORCH}", pytorch_full_version).replace("${CUDA}", cuda_version),
        ]
        subprocess.run(command, check=True)
        print("Successfully installed pyg-lib.")
    except subprocess.CalledProcessError as e:
        print(f"Error installing pyg-lib: {e}")

if __name__ == "__main__":
    pytorch_version = get_pytorch_version()
    if pytorch_version:
        cuda_version = get_cuda_version(pytorch_version)
        if cuda_version:
            install_pyg_lib(pytorch_version, cuda_version)
        else:
            print("Failed to determine CUDA version. Aborting.")
    else:
        print("Failed to determine PyTorch version. Aborting.")