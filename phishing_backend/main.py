from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from groq import Groq
from dotenv import load_dotenv
import os
import json

load_dotenv()

app = FastAPI()

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allows all origins (mobile apps, Flutter, etc.)
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class Message(BaseModel):
    text: str

# Root route
@app.get("/")
def root():
    return {"message": "Backend is running!"}

@app.post("/detect_phishing")
async def detect_phishing(data: Message):

    prompt = f"""
    You are a phishing detection expert.

    Analyze the following message and determine if it is:
    - PHISHING
    - SAFE

    Then provide a short explanation.

    Message:
    {data.text}

    Respond ONLY in JSON:
    {{
        "result": "",
        "explanation": ""
    }}
    """

    response = client.chat.completions.create(
        model="llama-3.1-8b-instant",
        messages=[{"role": "user", "content": prompt}],
        temperature=0.3
    )

    try:
        result_json = json.loads(response.choices[0].message.content)
    except json.JSONDecodeError:
        result_json = {"result": "ERROR", "explanation": response.choices[0].message.content}
