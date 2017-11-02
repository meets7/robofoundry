import yaml
from os.path import abspath

f_path = abspath("db.yaml")


def get():
    with open(f_path, 'r') as ymlfile:
        cfg = yaml.load(ymlfile)
        return cfg
