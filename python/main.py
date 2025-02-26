import json
import yaml
from fastapi import FastAPI
from fastapi.responses import JSONResponse, Response
from typing import List, Dict

app = FastAPI(title="RX Claims API", description="API for serving RX Claims", version="1.0.0")

# Load RX Claims data at startup from an external JSON file
with open("rx_claims.json", "r") as f:
    rx_claims_data: List[Dict] = json.load(f)


@app.get("/rxclaims/member/{member_id}", response_model=List[Dict])
async def get_rx_claims_by_member(member_id: str):
    """
    Retrieves RX Claims for a given member id.
    Returns an empty JSON array if no claims are found.
    """
    member_claims = [claim for claim in rx_claims_data if claim.get("member_id") == member_id]
    return JSONResponse(content=member_claims)

# @app.get("/openapi.yaml", include_in_schema=False)
# async def openapi_yaml():
#     openapi_json = app.openapi()
#     openapi_yaml_str = yaml.dump(openapi_json, sort_keys=False)
#     return Response(content=openapi_yaml_str, media_type="application/x-yaml")