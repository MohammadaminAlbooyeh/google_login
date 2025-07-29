# Google Login Demo (Flask + Streamlit)

This project demonstrates how to implement Google OAuth 2.0 login in a Python web application using a Flask backend and a Streamlit frontend.

## Features

- Google OAuth 2.0 authentication
- User info storage with SQLAlchemy (SQLite)
- Streamlit frontend for login/logout and user dashboard
- Secure session management

## Project Structure

```
google_login/
├── backend/
│   ├── app.py              # Flask backend (OAuth, API, DB)
│   ├── config.py           # Configuration (secrets, DB, Google OAuth)
│   ├── credentials.json    # Google OAuth credentials (download from Google Cloud)
│   └── instance/
├── frontend/
│   └── app.py              # Streamlit frontend
├── requirements.txt        # Python dependencies
└── README.md
```

## Setup Instructions

### 1. Clone the repository

```bash
git clone <repo-url>
cd google_login
```

### 2. Create and activate a virtual environment

```bash
python3 -m venv venv
source venv/bin/activate
```

### 3. Install dependencies

```bash
pip install -r requirements.txt
```

### 4. Set up Google OAuth credentials

- Go to [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
- Create OAuth 2.0 credentials (Web application)
- Download `credentials.json` and place it in `backend/`
- Set environment variables for `GOOGLE_CLIENT_ID` and `GOOGLE_CLIENT_SECRET` (from your credentials file):

```bash
export GOOGLE_CLIENT_ID="your-client-id"
export GOOGLE_CLIENT_SECRET="your-client-secret"
export SECRET_KEY="your-flask-secret-key"
```

Or create a `.env` file in `backend/` with:

```
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_SECRET=your-client-secret
SECRET_KEY=your-flask-secret-key
```

### 5. Run the Flask backend

```bash
cd backend
python app.py
```

The backend will run on `http://localhost:5000`.

### 6. Run the Streamlit frontend

In a new terminal:

```bash
cd frontend
streamlit run app.py
```

The frontend will run on `http://localhost:8501`.

## Usage

1. Open `http://localhost:8501` in your browser.
2. Click "Login with Google".
3. Authorize the app with your Google account.
4. After login, you'll see your user info and a logout button.

## Notes

- The backend uses SQLite for simplicity. For production, use a more robust database.
- Make sure your Google OAuth consent screen is configured and the redirect URIs include `http://localhost:5000/login/callback`.
- Never commit your real credentials to version control.

---

Let me know if you want to add more details or usage examples!
