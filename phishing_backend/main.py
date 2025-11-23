from fastapi import FastAPI
from pydantic import BaseModel
from groq import Groq
from dotenv import load_dotenv
import os

load_dotenv()

app = FastAPI()

client = Groq(api_key=os.getenv("GROQ_API_KEY"))

class Message(BaseModel):
    text: str

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

    return {"response": response.choices[0].message.content}

