from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

persona = Blueprint('persona', __name__)
URL_STATIC = 'http://localhost:8090/api/persona'
@persona.route('/', methods = ['GET'])
def home():
    r = requests.get(f"{URL_STATIC}/list")
    data = r.json()
    personas = data['data']
    return render_template('/persona_templates/persona_list.html', personas = personas)

@persona.route('/search/<attribute>/<value>', methods = ['GET'])
def search(attribute, value):
    r = requests.get(f"{URL_STATIC}/list/search/{attribute}/{value}")
    response = r.json()
    data = response.get('data',[])
    if isinstance(data, dict):
        data = [data]
    return render_template('/persona_templates/persona_list.html', personas = data)

@persona.route('/order/<attribute>/<type>', methods = ['GET'])
def order(attribute, type):
    r = requests.get(f"{URL_STATIC}/list/order/{attribute}/{type}")
    response = r.json()
    data = response.get('data',[])
    return render_template('/persona_templates/persona_list.html', personas = data)
