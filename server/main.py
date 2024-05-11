import uvicorn
from aiomysql import Cursor
from fastapi import FastAPI, HTTPException

from ai_model.keyword_extraction import extract
from ai_model.make_json import make_json
from auth_functions import getOTPCode, generateHash, createOTPKey, checkOTPCode
from my_dataclasses import SearchRequest, RouteResponse, AuthResponse, Profile, OTPResponse, OTPRequest, SearchEntry, \
  Route, RouteUpdate, EntriesUpdate
from pool import MyPool
from environs import Env
app = FastAPI()


async def check_hash(cur: Cursor, profile_id: int, hash: str):
  await cur.execute("SELECT (hash) FROM server.profiles WHERE id=%s", (profile_id,))
  profile = await cur.fetchall()
  if len(profile) == 1:
    hash_compare = profile[0][0]
    if not hash_compare == hash:
      raise HTTPException(status_code=403, detail="Неверный хэш")
  else:
    raise HTTPException(status_code=403, detail=f"Пользователь с id {profile_id} не найден")


@app.post("/search", response_model=RouteResponse)
async def search(search_request: SearchRequest):
  text = search_request.text
  with open('ai_model/request.txt', 'w', encoding='utf-8-sig') as request_file:
    request_file.write(text)
  extract('ai_model/request.txt', "ai_model/keywords.txt")
  route_response = make_json()
  open('ai_model/keywords.txt', 'w', encoding='utf-8-sig').close()
  open('ai_model/request.txt', 'w', encoding='utf-8-sig').close()
  return route_response


@app.post("/auth", response_model=AuthResponse)
async def auth(profile: Profile):
  email = profile.email
  phone = profile.phone
  async with await MyPool().acquireConnection() as conn:
    async with conn.cursor() as cur:
      if len(phone)>0:
        await cur.execute(f"SELECT * FROM server.profiles WHERE phone=%s", (phone,))
      elif len(email)>0:
        await cur.execute(f"SELECT * FROM server.profiles WHERE email=%s", (email,))
      else:
        raise HTTPException(403,"Некорректный профиль")
      profiles = await cur.fetchall()
      if len(profiles) == 1:
        profile_first = profiles[0]
        profile_id = profile_first[0]
        await cur.execute(f"SELECT * FROM server.otp WHERE profile_id=%s", (profile_id,))
        otp = (await cur.fetchall())[0][1]
      elif len(profiles) == 0:
        hash = generateHash()
        await cur.execute(f"INSERT INTO server.profiles (username,phone,email,photo,hash) VALUES (%s,%s,%s,%s,%s)",
                          (profile.username, profile.phone, profile.email, profile.photo, hash))
        await conn.commit()
        profile_id = cur.lastrowid
        otp = createOTPKey()
        await cur.execute(f"INSERT INTO server.otp VALUES (%s,%s)", (profile_id, otp))
        await conn.commit()
  code = getOTPCode(otp)
  return AuthResponse(id=profile_id, code=code)


@app.post("/otp", response_model=OTPResponse)
async def otp(otpRequest: OTPRequest):
  async with await MyPool().acquireConnection() as conn:
    async with conn.cursor() as cur:
      await cur.execute(f"SELECT * FROM server.otp WHERE profile_id=%s", (otpRequest.id,))
      result = await cur.fetchall()
      if len(result) != 1:
        raise HTTPException(status_code=403, detail="Id пользователя не обнаружен")
      otp = result[0][1]
      if checkOTPCode(otp, otpRequest.code):
        await cur.execute(f"SELECT * FROM server.profiles WHERE id=%s", (otpRequest.id,))
        profile_data = (await (cur.fetchall()))[0]
        await cur.execute(f"SELECT * FROM server.entries WHERE profile_id=%s", (otpRequest.id,))
        entries_data = await (cur.fetchall())
        await cur.execute(f"SELECT * FROM server.routes WHERE profile_id=%s", (otpRequest.id,))
        routes_data = await (cur.fetchall())
        profile = Profile(id=profile_data[0], username=profile_data[1], phone=profile_data[2], email=profile_data[3],
                          photo=profile_data[4], hash=profile_data[5], initialized=True)
        entries = [
          SearchEntry(id=entry[0], entry=entry[1], dateTime=entry[2], transport=entry[3], startPointPlace=entry[4],
                      endPointPlace=entry[5], length=entry[6]) for entry in entries_data]
        routes = [
          Route(id=route[0], length=route[1], route=route[2], startPointPlace=route[3], startPointAddress=route[4],
                endPointPlace=route[5], endPointAddress=route[6], imageLink=route[7], timeRequired=route[8],
                transport=route[9], searchEntry=route[10], liked=bool(route[11])) for route in routes_data]
      else:
        raise HTTPException(status_code=403, detail="Неверный OTP код")
  return OTPResponse(profile=profile, entries=entries, routes=routes)


@app.post("/updateRoute", response_model=bool)
async def updateRoute(routeUpdate: RouteUpdate):
  async with await MyPool().acquireConnection() as conn:
    async with conn.cursor() as cur:
      await check_hash(cur, routeUpdate.id, routeUpdate.hash)
      await cur.execute("DELETE FROM server.routes WHERE profile_id=%s", (routeUpdate.id,))
      await cur.executemany("INSERT INTO server.routes (length, route, startPointPlace, startPointAddress, endPointPlace, endPointAddress, imageLink, timeRequired, transport, searchEntry, liked, profile_id) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                            [(route.length, route.route, route.startPointPlace, route.startPointAddress,
                              route.endPointPlace, route.endPointAddress, route.imageLink, route.timeRequired,
                              route.transport, route.searchEntry, route.liked, routeUpdate.id) for
                             route in routeUpdate.routes])
      await conn.commit()
  return True


@app.post("/userData", response_model=bool)
async def updateUserData(profile: Profile):
  async with await MyPool().acquireConnection() as conn:
    async with conn.cursor() as cur:
      await check_hash(cur, profile.id, profile.hash)
      await cur.execute("UPDATE server.profiles SET username=%s,phone=%s,email=%s,photo=%s WHERE id=%s",
                        (profile.username, profile.phone, profile.email, profile.photo, profile.id))
      await conn.commit()
  return True


@app.post("/entry", response_model=bool)
async def updateEntry(entryUpdate: EntriesUpdate):
  async with await MyPool().acquireConnection() as conn:
    async with conn.cursor() as cur:
      await check_hash(cur, entryUpdate.id, entryUpdate.hash)
      await cur.execute("DELETE FROM server.entries WHERE profile_id=%s", (entryUpdate.id,))
      await cur.executemany("INSERT INTO server.entries (entry, dateTime, transport, startPointPlace, endPointPlace, length, profile_id) VALUES (%s,%s,%s,%s,%s,%s,%s)",
                            [(entry.entry, entry.dateTime, entry.transport, entry.startPointPlace,
                              entry.endPointPlace, entry.length, entryUpdate.id) for
                             entry in entryUpdate.entry])
      await conn.commit()
  return True


if __name__ == "__main__":
  env: Env = Env()
  env.read_env(".env")
  uvicorn.run(app=app, host=env("INTERFACE"), port=int(env("SERVER_PORT")))
