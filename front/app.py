from flask import Flask


def create_app():
    app = Flask(__name__, instance_relative_config=False);
    with app.app_context():
        from routes.route import router
        from routes.persona_route import persona
        from routes.punto_entrega_route import punto_entrega
        from routes.itinerario_route import itinerario
        from routes.orden_entrega_route import orden_entrega
        from routes.pedido_route import pedido
        app.register_blueprint(router, url_prefix='/admin')
        app.register_blueprint(persona, url_prefix='/admin/persona')
        app.register_blueprint(punto_entrega, url_prefix='/admin/punto_entrega')
        app.register_blueprint(itinerario, url_prefix='/admin/itinerario')
        app.register_blueprint(orden_entrega, url_prefix='/admin/orden_entrega')
        app.register_blueprint(pedido, url_prefix='/admin/pedido')
        
    return app
