from flask import Flask


def create_app():
    app = Flask(__name__, instance_relative_config=False);
    with app.app_context():
        from routes.route import router
        from routes.persona_route import persona
        app.register_blueprint(router, url_prefix='/admin')
        app.register_blueprint(persona, url_prefix='/admin/persona')
 
        
    return app
