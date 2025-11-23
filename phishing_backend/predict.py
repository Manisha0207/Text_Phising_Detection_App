import joblib

# Load saved model
model = joblib.load("phishing_model.pkl")

def predict_text(text):
    prediction = model.predict([text])[0]
    probability = model.predict_proba([text])[0]
    return {
        "prediction": prediction,
        "confidence_legitimate": round(probability[0], 4),
        "confidence_phishing": round(probability[1], 4)
    }

if __name__ == "__main__":
    sample = input("Enter message: ")
    result = predict_text(sample)
    print(result)
