import streamlit as st
import requests
import json # Import json for parsing the response from Flask

# Backend Flask API URL
FLASK_API_URL = "http://localhost:5000"

st.set_page_config(page_title="Google Login Demo", layout="centered")

def get_user_info():
    """Fetches user information from the Flask backend."""
    try:
        response = requests.get(f"{FLASK_API_URL}/user_info")
        response.raise_for_status() # Raise an exception for HTTP errors
        return response.json()
    except requests.exceptions.RequestException as e:
        st.error(f"Could not connect to backend: {e}")
        return {"logged_in": False}

def display_login_page():
    """Displays the login button."""
    st.title("Welcome to Google Login")
    st.write("Please log in with your Google account to continue.")
    
    # Create a link that redirects to the Flask backend's login endpoint
    st.markdown(f'<a href="{FLASK_API_URL}/login" target="_self" style="display: inline-block; padding: 10px 20px; background-color: #4285F4; color: white; text-align: center; text-decoration: none; border-radius: 5px; font-size: 16px;">Login with Google</a>', unsafe_allow_html=True)
    st.write("") # Add some space

def display_user_dashboard(user_data):
    """Displays user information and a logout button."""
    st.title(f"Welcome, {user_data.get('name', 'User')}!")
    st.write(f"**Email:** {user_data.get('email')}")
    if user_data.get('profile_pic'):
        st.image(user_data['profile_pic'], width=100)
    
    st.markdown(f'<a href="{FLASK_API_URL}/logout" target="_self" style="display: inline-block; padding: 10px 20px; background-color: #EA4335; color: white; text-align: center; text-decoration: none; border-radius: 5px; font-size: 16px;">Logout</a>', unsafe_allow_html=True)

def handle_login_success():
    """Handles redirection after successful login from Flask."""
    st.success("Successfully logged in! Redirecting...")
    st.session_state['logged_in'] = True # Set a session state flag
    st.experimental_rerun() # Rerun to show the dashboard

# --- Main App Logic ---
# Get current path from URL params to handle redirect
query_params = st.query_params

if "login_success" in query_params:
    handle_login_success()
else:
    # Check if user is already logged in
    user_info = get_user_info()

    if user_info.get("logged_in"):
        display_user_dashboard(user_info)
    else:
        display_login_page()
        