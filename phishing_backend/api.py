from fastapi import FastAPI
import joblib

app = FastAPI()

# Load the model
model = joblib.load("phishing_model.pkl")

@app.get("/")
def home():
    return {"message": "Phishing Detection API is running"}

@app.post("/predict")
def predict(text: str):
    pred = model.predict([text])[0]
    prob = model.predict_proba([text])[0]
    return {
        "prediction": pred,
        "prob_legitimate": prob[0],
        "prob_phishing": prob[1]
    }
