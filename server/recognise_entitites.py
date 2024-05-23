import json
import uuid
from typing import List
import requests
from environs import Env
from natasha import NewsEmbedding, NewsSyntaxParser, Doc, MorphVocab, NewsMorphTagger, Segmenter
from singleton_decorator import singleton
from my_dataclasses import AiResponse, TimeAndPlace, Entry, DestPoint, RouteResponse
from word_to_number.extractor import NumberExtractor


@singleton
class WordsRecogniser:
  headers: dict[str, str] | None = None
  emb = NewsEmbedding()
  syntax_parser = NewsSyntaxParser(emb)
  morph_tagger = NewsMorphTagger(emb)
  morph_vocab = MorphVocab()
  url = "https://api.edenai.run/v2/text/custom_named_entity_recognition"
  segmenter = Segmenter()
  time_units = {'час': 60,
                'минута': 1,
                'полчаса': 30,
                'день': 1440,
                'сутки': 1440}
  extractor = NumberExtractor()

  async def convertTimeStringToMinutes(self, string: str) -> int:
    print(string)
    doc = Doc(string)
    doc.segment(self.segmenter)
    doc.parse_syntax(self.syntax_parser)
    doc.tag_morph(self.morph_tagger)
    for token in doc.tokens:
      token.lemmatize(self.morph_vocab)
    multiplier = 1
    unit = 1
    for token in doc.tokens:
      if "nummod" in token.rel:
        multiplier = self.extractor(token.lemma)[0].int
      elif token.lemma in self.time_units.keys():
        unit = self.time_units[token.lemma]
    return unit*multiplier

  async def findTimesAndPlaces(self, query: str, ai_response: AiResponse) -> list[TimeAndPlace]:
    times = list()
    locations = list()
    for element in ai_response["openai"]["items"]:
      if element["category"] == "time":
        times.append(element["entity"])
      elif element["category"] == "location":
        locations.append(element["entity"])
    result = list()
    for i, location in enumerate(locations):
      if i < len(times):
        result.append(TimeAndPlace(time=times[i], place=location))
      else:
        result.append(TimeAndPlace(time=None, place=location))
    return result

  async def recognise_words(self, query: str) -> RouteResponse:
    payload = {
      "providers": "openai",
      "entities": ['time', 'location'],
      "text": query,
      "fallback_providers": "cohere"
    }
    if not self.headers:
      env: Env = Env()
      env.read_env(".env")
      self.headers = {"Authorization": "Bearer " + env("AI_API")}
    response = requests.post(self.url, json=payload, headers=self.headers)
    print(response.text)
    ai_response: AiResponse = json.loads(response.text)
    id_ = str(uuid.uuid4())
    entries: List[Entry] = list()
    times_and_places = await self.findTimesAndPlaces(query, ai_response)
    for i,element in enumerate(times_and_places):
      if element.time:
        time = await self.convertTimeStringToMinutes(element.time)
      else:
        time = 0
      entries.append(Entry(entryno=i,time=time,destpoint=DestPoint(category=element.place)))
    return RouteResponse(id=id_,entries=entries)
