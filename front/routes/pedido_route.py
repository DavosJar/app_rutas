from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
from datetime import datetime

pedido = Blueprint('pedido', __name__)
URL_STATIC = 'http://localhost:8090/api/pedido'
@pedido.route('/', methods = ['GET'])
def home():
    r = requests.get(f"{URL_STATIC}/list")
    data = r.json()
    pedido = data['data']
    return render_template('/pedido_templates/pedido_list.html', pedido= pedido)

@pedido.route('/search/<attribute>/<value>', methods = ['GET'])
def search(attribute, value):
    r = requests.get(f"{URL_STATIC}/search/{attribute}/{value}")
    print(r.text)
    print(r.status_code)
    response = r.json()
    data = response.get('data',[])
    if isinstance(data, dict):
        data = [data]
    return render_template('/pedido_templates/pedido_list.html', pedido = data)


@pedido.route('/<id>', methods = ['GET'])
def pedido_detail(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        pedido = data["data"]
        return render_template('/pedido_templates/pedido_detail.html', pedido = pedido)
    except requests.RequestException as e:
        return redirect(url_for('pedido.home') )
    
@pedido.route('/<id>/delete')
def delete(id):
    try:
        r = requests.delete(f"{URL_STATIC}/{id}/delete")
        r.raise_for_status()
        return redirect(url_for('pedido.home'))
    except requests.RequestException as e:
        return redirect(url_for('pedido.home'))

@pedido.route('/order/<attribute>/<type>', methods = ['GET'])
def order(attribute, type):
    r = requests.get(f"{URL_STATIC}/order/{attribute}/{type}")
    response = r.json()
    data = response.get('data',[])
    return render_template('/pedido_templates/pedido_list.html', pedido = data)


@pedido.route('<id>/edit', methods=['GET'])
def edit(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        pedido = data["data"]

        if pedido.get("fechaRegistro"):
            try:
                pedido["fechaRegistro"] = datetime.strptime(pedido["fechaRegistro"], "%Y-%m-%dT%H:%M:%S.%fZ").strftime("%Y-%m-%d")
            except ValueError:
                pedido["fechaRegistro"] = datetime.strptime(pedido["fechaRegistro"], "%Y-%m-%d").strftime("%Y-%m-%d")

        estado = get_options(f'{URL_STATIC}/listType')
        return render_template('/pedido_templates/edit_pedido.html', pedido=pedido, estado=estado)
    except requests.RequestException as e:
        return redirect(url_for('pedido.home'))


@pedido.route('<id>/edit', methods=['POST'])
def update(id):
    try:
        data = {
            "contenido": request.form.get('contenido'),
            "fechaRegistro": request.form.get('registro'),
            "requiereFrio": request.form.get('refrigeracion'),
            "volumenTotal": request.form.get('volumen'),
            "pesoTotal": request.form.get('peso'),
            "estado": request.form.get('estado'),
            "idOrdenEntrega": request.form.get('entrega'),
            "idPuntoEntrega": request.form.get('punto'),
            "idItinerario": request.form.get('itinerario'),

        }
        r = requests.put(f"{URL_STATIC}/{id}/update", json=data)
        r.raise_for_status()
        return redirect(url_for('pedido.home'))
    except requests.RequestException as e:
        return redirect(url_for('pedido.edit', id=id))

@pedido.route('/save', methods=['GET'])
def save():
    estado = get_options(f'{URL_STATIC}/listType')
    pedido = {
        "contenido": "",
        "fechaRegistro": "",
        "requiereFrio": "",
        "volumenTotal": "",
        "pesoTotal": "",
        "estado": "",
        "idOrdenEntrega": "",
        "idPuntoEntrega": "",
        "idItinerario": "",
    }
    return render_template('/pedido_templates/registrar_pedido.html', estado = estado)


@pedido.route('/save', methods=['POST'])
def save_post():
    try:
       
        data = {
            "punto-entrega": request.form.get('punto'),  
            "orden-entrega": request.form.get('entrega'),  
            "itinerario": request.form.get('itinerario'), 
            "contenido": request.form.get('contenido'),
            "fechaRegistro": request.form.get('registro'),
            "pesoTotal": float(request.form.get('peso')),  
            "volumen": float(request.form.get('volumen')),  
            "estado": request.form.get('estado'),
            "requiereFrio": request.form.get('refrigeracion') == "true",  
        }

      
        r = requests.post(f"{URL_STATIC}/save", json=data)
        r.raise_for_status() 
        return redirect(url_for('pedido.home'))
    except requests.RequestException as e:
        flash(f"Error al guardar el pedido: {e}", "danger")
        return redirect(url_for('pedido.save'))


def get_options(endpoint):
    try:
        r = requests.get(endpoint)
        r.raise_for_status()
        data = r.json()
        
        options = []
        for item in data.get("data", []):
            nombre_formateado = format_string(item.replace("_"," "), capitalizar_palabras=True)
            options.append((item, nombre_formateado))
        return options
    except requests.RequestException as e:
        print(f'Error al optener opciones desde {endpoint}: {e}')
        return[]

def format_string(cadena, capitalizar_palabras=False):
    resultado = ''.join([' ' + char if char.isupper() and (i > 0 and not cadena[i - 1].isupper()) else char
                         for i, char in enumerate(cadena)])
    
    if capitalizar_palabras:
        return ' '.join([palabra.capitalize() for palabra in resultado.split()])
    else:
        return resultado.capitalize()