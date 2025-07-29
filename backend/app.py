import os
import json
from flask import Flask, redirect, request, url_for, session, jsonify
from flask_sqlalchemy import SQLAlchemy
from oauthlib.oauth2 import WebApplicationClient
import requests
from dotenv import load_dotenv

# Load environment variables from .env file (for local development)
load_dotenv()

# --- App Initialization and Configuration ---
app = Flask(__name__)
# Load configuration from config.py
app.config.from_object('config.Config')

# Initialize SQLAlchemy
db = SQLAlchemy(app)

# OAuth 2.0 client setup for Google
client = WebApplicationClient(app.config['GOOGLE_CLIENT_ID'])

# --- Database Model ---
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    google_id = db.Column(db.String(100), unique=True, nullable=False)
    name = db.Column(db.String(100))
    email = db.Column(db.String(100), unique=True, nullable=False)
    profile_pic = db.Column(db.String(200))

    def __repr__(self):
        return f'<User {self.email}>'

# --- Helper Functions ---
def get_google_provider_cfg():
    """Fetches the Google OpenID Connect discovery document."""
    return requests.get(app.config['GOOGLE_DISCOVERY_URL']).json()

# --- Routes ---

@app.route("/")
def index():
    """Homepage route - for testing, not directly used by Streamlit."""
    if 'google_id' in session:
        return f'Hello, {session["name"]}! <a href="/logout">Logout</a>'
    return '<a href="/login">Login with Google</a>'


@app.route("/login")
def login():
    """Initiates the Google OAuth login flow."""
    google_provider_cfg = get_google_provider_cfg()
    authorization_endpoint = google_provider_cfg["authorization_endpoint"]

    request_uri = client.prepare_request_uri(
        authorization_endpoint,
        redirect_uri=request.base_url + "/callback",
        scope=["openid", "email", "profile"],
    )
    return redirect(request_uri)

@app.route("/login/callback")
def callback():
    """Handles the callback from Google after user authentication."""
    # Get the authorization code from the request
    code = request.args.get("code")

    # Get Google's OpenID Connect discovery document
    google_provider_cfg = get_google_provider_cfg()
    token_endpoint = google_provider_cfg["token_endpoint"]

    # Prepare and send the request to exchange code for tokens
    token_url, headers, body = client.prepare_token_request(
        token_endpoint,
        authorization_response=request.url,
        redirect_url=request.base_url,
        code=code
    )
    token_response = requests.post(
        token_url,
        headers=headers,
        data=body,
        auth=(app.config['GOOGLE_CLIENT_ID'], app.config['GOOGLE_CLIENT_SECRET'])
    )

    # Parse the tokens (Access Token, ID Token)
    client.parse_request_body_response(json.dumps(token_response.json()))

    # Get user info from Google (using ID Token)
    userinfo_endpoint = google_provider_cfg["userinfo_endpoint"]
    uri, headers, body = client.add_token(userinfo_endpoint)
    userinfo_response = requests.get(uri, headers=headers, data=body)

    userinfo = userinfo_response.json()

    if userinfo.get("email_verified"):
        users_email = userinfo["email"]
        # Use 'name' if 'given_name' or 'family_name' are not present
        users_name = userinfo.get("given_name", "") + " " + userinfo.get("family_name", "")
        if not users_name.strip(): # If concatenation is empty, use general 'name'
            users_name = userinfo.get("name", "Unknown User")
        users_profile_pic = userinfo.get("picture")
        users_google_id = userinfo["sub"] # 'sub' is the unique Google ID

        # Check if user exists in our database
        user = User.query.filter_by(google_id=users_google_id).first()

        if not user:
            # Create new user if not exists
            user = User(
                google_id=users_google_id,
                name=users_name,
                email=users_email,
                profile_pic=users_profile_pic
            )
            db.session.add(user)
            db.session.commit()
        else:
            # Update existing user info if needed
            user.name = users_name
            user.email = users_email
            user.profile_pic = users_profile_pic
            db.session.commit()

        # Log in the user by storing info in session
        session["google_id"] = users_google_id
        session["name"] = users_name
        session["email"] = users_email
        session["profile_pic"] = users_profile_pic

        # Redirect to the frontend Streamlit app after successful login
        # This will inform Streamlit that the user is logged in
        return redirect("http://localhost:8501/login_success") # We'll create this Streamlit page
    else:
        return "User email not verified by Google.", 400


@app.route("/logout")
def logout():
    """Logs out the user by clearing the session."""
    session.clear()
    return redirect("http://localhost:8501/") # Redirect to Streamlit homepage


@app.route("/user_info")
def user_info():
    """Provides user information to the frontend (Streamlit)."""
    if 'google_id' in session:
        return jsonify({
            "logged_in": True,
            "name": session.get("name"),
            "email": session.get("email"),
            "profile_pic": session.get("profile_pic"),
            "google_id": session.get("google_id")
        })
    return jsonify({"logged_in": False})


# --- Run the Flask App ---
if __name__ == "__main__":
    # Ensure database tables are created before running
    with app.app_context():
        db.create_all() # This creates tables if they don't exist
    app.run(debug=True, port=5000, ssl_context=None) # Set ssl_context=None for local http