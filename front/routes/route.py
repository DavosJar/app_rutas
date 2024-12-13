
from flask import Blueprint, abort, request, render_template, redirect, flash, jsonify
import requests
import json
import datetime

router = Blueprint('router', __name__)
URL_STATIC = 'http://localhost:8090/api'


@router.route('/')
def home():
        num_conductores = get_num_items('conductor')
        num_vehiculos = get_num_items('vehiculo')
        num_pedidos = get_num_items('pedido')
        
        return render_template('index.html', num_conductores=num_conductores, num_vehiculos=num_vehiculos, num_pedidos=num_pedidos)  

def get_num_items(endpoit):
        r = requests.get(f"{URL_STATIC}/{endpoit}/list")
        data = r.json()
        return len(data['data'])