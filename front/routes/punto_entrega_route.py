from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

punto_entrega = Blueprint('punto_entrega', __name__)
URL_STATIC = 'http://localhost:8090/api/punto-entrega'
@punto_entrega.route('/', methods = ['GET'])
def home():
    r = requests.get(f"{URL_STATIC}/list")
    data = r.json()
    puntos_entrega = data['data']
    return render_template('/punto_entrega_templates/punto_entrega_list.html', puntos_entrega = puntos_entrega)

@punto_entrega.route('/search/<attribute>/<value>', methods = ['GET'])
def search(attribute, value):
    r = requests.get(f"{URL_STATIC}/search/{attribute}/{value}")
    print(r.text)
    print(r.status_code)
    response = r.json()
    data = response.get('data',[])
    if isinstance(data, dict):
        data = [data]
    return render_template('/punto_entrega_templates/punto_entrega_list.html', puntos_entrega = data)

@punto_entrega.route('/order/<attribute>/<type>', methods = ['GET'])
def order(attribute, type):
    r = requests.get(f"{URL_STATIC}/order/{attribute}/{type}")
    response = r.json()
    data = response.get('data',[])
    return render_template('/punto_entrega_templates/punto_entrega_list.html', puntos_entrega = data)
