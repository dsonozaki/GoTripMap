from typing import List
from pydantic import BaseModel


class SearchRequest(BaseModel):
  text: str
  tsys: str
  coords: str

class DestPoint(BaseModel):
  name: str
  category: str


class Entry(BaseModel):
  entryno: int
  tsys: str
  time: int
  startpoint: str
  destpoint: DestPoint
  img: str


class RouteResponse(BaseModel):
  id: str
  entries: List[Entry]

class Profile(BaseModel):
  id: int
  username: str
  phone: str
  email: str
  photo: str
  hash: str
  initialized: bool

class AuthResponse(BaseModel):
  id: int
  code: str

class OTPRequest(BaseModel):
  id: int
  code: str



class Route(BaseModel):
  id: int
  length: str
  route: str
  startPointPlace: str
  startPointAddress: str
  endPointPlace: str
  endPointAddress: str
  imageLink: str
  timeRequired: str
  transport: str
  searchEntry: int
  liked: bool

class SearchEntry(BaseModel):
  id: int
  entry: str
  dateTime: str
  transport: str
  startPointPlace: str
  endPointPlace: str
  length: str

class EntriesUpdate(BaseModel):
  entry: List[SearchEntry]
  id: int
  hash: str

class OTPResponse(BaseModel):
  profile: Profile
  entries: List[SearchEntry]
  routes: List[Route]

class RouteUpdate(BaseModel):
  routes: List[Route]
  id: int
  hash: str
