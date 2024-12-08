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

@persona.route('/<id>', methods = ['GET'])
def persona_detail(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        persona = data["data"]
        return render_template('/persona_templates/persona_detail.html', persona = persona)
    except requests.RequestException as e:
        
        return redirect(url_for('persona.home') )
    

@persona.route('<id>/delete')
def delete(id):
    try:
        r = requests.delete(f"{URL_STATIC}/{id}/delete")
        r.raise_for_status()
        return redirect(url_for('persona.home'))
    except requests.RequestException as e:
        return redirect(url_for('persona.home'))

@persona.route('/order/<attribute>/<type>', methods = ['GET'])
def order(attribute, type):
    r = requests.get(f"{URL_STATIC}/list/order/{attribute}/{type}")
    response = r.json()
    data = response.get('data',[])
    return render_template('/persona_templates/persona_list.html', personas = data)

@persona.route('<id>/edit', methods=['GET'])
def edit(id):
    try:
        r = requests.get(f"{URL_STATIC}/get/{id}")
        r.raise_for_status()
        data = r.json()
        persona = data["data"]

        if persona.get("fechaNacimiento"):
            try:
                persona["fechaNacimiento"] = datetime.strptime(persona["fechaNacimiento"], "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                persona["fechaNacimiento"] = ""

        sexo = get_options(f'{URL_STATIC}/sexo')
        tipo = get_options(f'{URL_STATIC}/tipoidentificacion')

        print("Persona:", persona)
        print("Sexo:", sexo)
        print("Tipo Identificación:", tipo)

        return render_template('persona_templates/edit_persona.html', persona=persona, sexo=sexo, tipo=tipo)
    except requests.RequestException as e:
        return redirect(url_for('persona.home'))


@persona.route('<id>/edit', methods=['POST'])
def update(id):
    try:
        fecha_nacimiento = request.form.get('fecha_nacimiento')
        if fecha_nacimiento:
            try:
                fecha_nacimiento = datetime.strptime(fecha_nacimiento, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                return redirect(url_for('persona.edit', id=id))

        data = {
            "id": request.form.get('id'),
            "nombre": request.form.get('nombre'),
            "apellido": request.form.get('apellido'),
            "tipoIdentificacion": request.form.get('tipoIdentificacion'),
            "identificacion": request.form.get('identificacion'),
            "fechaNacimiento": fecha_nacimiento,
            "direccion": request.form.get('direccion'),
            "telefono": request.form.get('telefono'),
            "email": request.form.get('email'),
            "sexo": request.form.get('sexo'),
        }

        r = requests.post(f"{URL_STATIC}/update", json=data)
        r.raise_for_status()

        return redirect(url_for('persona.home'))
    except requests.RequestException as e:
        return redirect(url_for('persona.persona_edit', id=id))
    
@persona.route('/save', methods = ['GET'])
def persona_save():
    sexo = get_options(f'{URL_STATIC}/sexo')
    tipo = get_options(f'{URL_STATIC}/tipoidentificacion')
    perosna={
        "nombre": "",
        "apellido": "",
        "tipoIdentificacion": "",
        "identificacion": "",
        "fechaNacimiento": "",
        "direccion": "",
        "telefono": "",
        "email": "",
        "sexo": ""
    }
    return render_template('/persona_templates/registrar_persona.html', persona = persona, sexo = sexo, tipo=tipo)



@persona.route('/save', methods=['POST'])
def save():
    try:
        fecha_nacimiento = request.form.get('fecha_nacimiento')
        if fecha_nacimiento:
            try:
                fecha_nacimiento = datetime.strptime(fecha_nacimiento, "%Y-%m-%d").strftime("%Y-%m-%d")
            except ValueError:
                flash('Fecha de nacimiento inválida. Debe estar en el formato AAAA-MM-DD.', 'error')
                return redirect(url_for('persona.persona_save'))
        else:
            flash('La fecha de nacimiento es obligatoria.', 'error')
            return redirect(url_for('persona.persona_save'))

        data = {
            "nombre": request.form.get('nombre'),
            "apellido": request.form.get('apellido'),
            "tipoIdentificacion": request.form.get('tipo'),
            "identificacion": request.form.get('identificacion'),
            "fechaNacimiento": fecha_nacimiento,
            "direccion": request.form.get('direccion'),
            "telefono": request.form.get('telefono'),
            "email": request.form.get('email'),
            "sexo": request.form.get('sexo'),
        }

        r = requests.post(f"{URL_STATIC}/save", json=data)
        r.raise_for_status()

        flash('Persona guardada exitosamente.', 'success')
        return redirect(url_for('persona.home'))

    except requests.RequestException as e:
        flash(f'Error al guardar la persona: {e}', 'error')
        return redirect(url_for('persona.persona_save'))

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