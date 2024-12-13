from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

orden_entrega = Blueprint('orden_entrega', __name__)
URL_STATIC = 'http://localhost:8090/api/orden-entrega'
@orden_entrega.route('/', methods = ['GET'])
def home():
    r = requests.get(f"{URL_STATIC}/list")
    data = r.json()
    ordenes_entrega = data['data']
    return render_template('/orden_entrega_templates/orden_entrega_list.html', ordenes_entrega = ordenes_entrega)

@orden_entrega.route('/search/<attribute>/<value>', methods = ['GET'])
def search(attribute, value):
    r = requests.get(f"{URL_STATIC}/search/{attribute}/{value}")
    print(r.text)
    print(r.status_code)
    response = r.json()
    data = response.get('data',[])
    if isinstance(data, dict):
        data = [data]
    return render_template('/orden_entrega_templates/orden_entrega_list.html', ordenes_entrega = data)

@orden_entrega.route('/order/<attribute>/<type>', methods = ['GET'])
def order(attribute, type):
    r = requests.get(f"{URL_STATIC}/order/{attribute}/{type}")
    response = r.json()
    data = response.get('data',[])
    return render_template('/orden_entrega_templates/orden_entrega_list.html', ordenes_entrega = data)

@orden_entrega.route('/<id>', methods=['GET'])
def orden_entrega_detail(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        orden_entrega = data["data"]
        return render_template('/orden_entrega_templates/orden_entrega_detail.html', orden_entrega=orden_entrega)
    except requests.RequestException:
        return redirect(url_for('orden_entrega.home'))

@orden_entrega.route('/<id>/delete', methods=['GET'])
def delete(id):
    try:
        r = requests.delete(f"{URL_STATIC}/{id}/delete")
        r.raise_for_status()
        return redirect(url_for('orden_entrega.home'))
    except requests.RequestException:
        return redirect(url_for('orden_entrega.home'))


@orden_entrega.route('/<id>/edit', methods=['GET'])
def edit(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        orden_entrega = data["data"]

        estado = get_options(f"{URL_STATIC}/listType")
        print(estado)

        return render_template('/orden_entrega_templates/edit_orden_entrega.html', orden_entrega=orden_entrega, estado=estado)
    except requests.RequestException:
        return redirect(url_for('orden_entrega.home'))

@orden_entrega.route('/<id>/edit', methods=['POST'])
def update(id):
    try:
        fecha_entrega = request.form.get('fechaEntrega')
        if fecha_entrega:
            try:
                fecha_entrega = datetime.strptime(fecha_entrega, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                return redirect(url_for('orden_entrega.edit', id=id))
        
        data = {
            "id": request.form.get('id'),
            "horaMinima": request.form.get('horaMinima'),
            "horaMaxima": request.form.get('horaMaxima'),   
            "fechaEntrega": fecha_entrega,
            "ubicacion": request.form.get('ubicacionActual'),
            "estado": request.form.get('estado')
        }

        r = requests.post(f"{URL_STATIC}/update", json=data)
        print(r.text)
        r.raise_for_status()

        return redirect(url_for('orden_entrega.home'))
    except requests.RequestException:
        return redirect(url_for('orden_entrega.edit', id=id))

@orden_entrega.route('/save', methods=['GET'])
def save_form():
    estado = get_options(f"{URL_STATIC}/listType")

    orden_entrega = {
        "horaMinima": "",
        "horaMaxima": "",
        "fechaEntrega": "",
        "ubicacion": "",
        "estado": ""
    }
    return render_template('/orden_entrega_templates/registrar_orden_entrega.html', orden_entrega=orden_entrega, estado=estado)

@orden_entrega.route('/save', methods=['POST'])
def save():

    try:
        fecha_entrega = request.form.get('fechaEntrega')
        if fecha_entrega:
            try:
                fecha_entrega = datetime.strptime(fecha_entrega, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                return redirect(url_for('orden_entrega.edit', id=id))

        data = {
            "horaMinima": request.form.get('horaMinima'),
            "horaMaxima": request.form.get('horaMaxima'),   
            "fechaEntrega": fecha_entrega,
            "ubicacion": request.form.get('ubicacionActual'),
            "estado": request.form.get('estado')
        }   
        print(data)
        r = requests.post(f"{URL_STATIC}/save", json=data)
        r.raise_for_status()
        print(r.text)

        return redirect(url_for('orden_entrega.home'))

    except requests.RequestException as e:
        return redirect(url_for('orden_entrega.save_form'))


def get_options(endpoint):
    try:
        r = requests.get(endpoint)
        r.raise_for_status()
        data = r.json()
        options = [(item, format_string(item.replace("_", " "), capitalizar_palabras=True)) for item in data.get("data", [])]
        return options
    except requests.RequestException as e:
        print(f'Error al obtener opciones desde {endpoint}: {e}')
        return []

def format_string(cadena, capitalizar_palabras=False):
    resultado = ''.join([' ' + char if char.isupper() and (i > 0 and not cadena[i - 1].isupper()) else char
                         for i, char in enumerate(cadena)])
    if capitalizar_palabras:
        return ' '.join([palabra.capitalize() for palabra in resultado.split()])
    else:
        return resultado.capitalize()
