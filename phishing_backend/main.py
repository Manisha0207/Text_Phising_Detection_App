from fastapi import FastAPI
from pydantic import BaseModel
import os
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()

client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

app = FastAPI()

class InputText(BaseModel):
    text: str

@app.post("/detect_phishing")
async def detect_phishing(data: InputText):

    prompt = f"""
    You are a phishing detection expert.

    Analyze the following message and determine if it is phishing or safe.
    Provide classification ONLY as:
    - "PHISHING" 
    - "SAFE"

    Then provide a short explanation.

    Text:
    {data.text}

    Respond in JSON format:
    {{
        "result": "...",
        "explanation": "..."
    }}
    """

    response = client.chat.completions.create(
        model="gpt-4o-mini",   
        messages=[{"role": "user", "content": prompt}]
    )

    llm_output = response.choices[0].message["content"]

    return {"response": llm_output}
