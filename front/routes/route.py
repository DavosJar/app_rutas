
from flask import Blueprint, abort, request, render_template, redirect, flash, jsonify
import requests
import json
import datetime

router = Blueprint('router', __name__)

@router.route('/')
def home():
        return render_template('index.html')
