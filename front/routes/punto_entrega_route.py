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

@punto_entrega.route('/<id>', methods=['GET'])
def punto_entrega_detail(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        punto_entrega = data["data"]
        return render_template('/punto_entrega_templates/punto_entrega_detail.html', punto_entrega=punto_entrega)
    except requests.RequestException:
        return redirect(url_for('punto_entrega.home'))

@punto_entrega.route('/<id>/delete', methods=['GET'])
def delete(id):
    try:
        r = requests.delete(f"{URL_STATIC}/{id}/delete")
        r.raise_for_status()
        return redirect(url_for('punto_entrega.home'))
    except requests.RequestException:
        return redirect(url_for('punto_entrega.home'))


@punto_entrega.route('/<id>/edit', methods=['GET'])
def edit(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        punto_entrega = data["data"]

        return render_template('/punto_entrega_templates/edit_punto_entrega.html', punto_entrega=punto_entrega)
    except requests.RequestException:
        return redirect(url_for('punto_entrega.home'))

@punto_entrega.route('/<id>/edit', methods=['POST'])
def update(id):
    try:
        
        data = {
            "id": request.form.get('id'),
            "nombre": request.form.get('nombre'),
            "ciudad": request.form.get('ciudad'),
            "latitud": request.form.get('latitud'),
            "longitud": request.form.get('longitud'),
            "direccion": request.form.get('direccion')
        }

        r = requests.post(f"{URL_STATIC}/update", json=data)
        r.raise_for_status()

        return redirect(url_for('punto_entrega.home'))
    except requests.RequestException:
        return redirect(url_for('punto_entrega.edit', id=id))

@punto_entrega.route('/save', methods=['GET'])
def save_form():
    punto_entrega = {
        "nombre": "",
        "ciudad": "",
        "latitud": "",
        "longitud": "",
        "direccion": ""
    }
    return render_template('/punto_entrega_templates/registrar_punto_entrega.html', punto_entrega=punto_entrega)

@punto_entrega.route('/save', methods=['POST'])
def save():
    try:

        data = {
            "nombre": request.form.get('nombre'),
            "ciudad": request.form.get('ciudad'),
            "latitud": request.form.get('latitud'),
            "longitud": request.form.get('longitud'),
            "direccion": request.form.get('direccion')
        }
        print(data)
        r = requests.post(f"{URL_STATIC}/save", json=data)
        r.raise_for_status()
        print(r.text)

        return redirect(url_for('punto_entrega.home'))

    except requests.RequestException as e:
        return redirect(url_for('punto_entrega.save_form'))


# def get_options(endpoint):
#     try:
#         r = requests.get(endpoint)
#         r.raise_for_status()
#         data = r.json()
#         options = [(item, format_string(item.replace("_", " "), capitalizar_palabras=True)) for item in data.get("data", [])]
#         return options
#     except requests.RequestException as e:
#         print(f'Error al obtener opciones desde {endpoint}: {e}')
#         return []

# def format_string(cadena, capitalizar_palabras=False):
#     resultado = ''.join([' ' + char if char.isupper() and (i > 0 and not cadena[i - 1].isupper()) else char
#                          for i, char in enumerate(cadena)])
#     if capitalizar_palabras:
#         return ' '.join([palabra.capitalize() for palabra in resultado.split()])
#     else:
#         return resultado.capitalize()
