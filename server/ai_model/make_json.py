import json
import uuid
from typing import List

from pydantic import BaseModel

from my_dataclasses import RouteResponse, DestPoint, Entry


def make_json() -> RouteResponse:
  # присваиваем каждому запросу свой ID
  id_ = str(uuid.uuid4())
  entries = []
  tsys = ''
  prev_tsys = ''

  # транспортные средства (передается от клиента нажатием на соответствующую иконку, но текст запроса в приоритете)
  tsyssets = {'велосипед': 'bike', 'пешком': 'ped', 'автобус': 'bus', 'автомобиль': 'car',
              'троллейбус': 'trolley', 'трамвай': 'tram', 'машина': 'car', 'такси': 'taxi'}

  # путь к изображению
  image_path = 'http://158.160.116.195/gotripmap/ai_model/images/'

  # числительные
  numbers = {
    'один': 1,
    'два': 2,
    'три': 3,
    'четыре': 4,
    'пять': 5,
    'шесть': 6,
    'семь': 7,
    'восемь': 8,
    'девять': 9,
    'десять': 10,
    'пятнадцать': 15,
    'двадцать': 20,
    'тридцать': 30,
    'сорок': 40,
    'пятьдесят': 50
  }

  # для изображений
  rus_eng_names = {
    'бар': 'bar',
    'кафе': 'cafe',
    'аптека': 'chemistry',
    'кино': 'cinema',
    'лес': 'forest',
    'парикмахерская': 'hairdresser',
    'больница': 'hospital',
    'поликлиника': 'hospital',
    'музей': 'museum',
    'парк': 'park',
    'ресторан': 'restaurant',
    'магазин': 'shop',
    'театр': 'theatre'
  }

  time_units = ['час', 'минута']
  time_units_indices = []
  time_value = 0
  place_words = []

  # открываем файл с ключевыми словами
  with open("ai_model/keywords.txt", "r", encoding='utf-8-sig') as keywords:
    lines = keywords.readlines()
    i = 1
    for line in lines:
      words = line.split(" ")
      words = [word.rstrip('\n') for word in words]
      result_tsys=''
      if tsys == '' and prev_tsys == '':
        result_tsys = 'ped'
      elif tsys == '' and prev_tsys != '':
        result_tsys = prev_tsys

      for word in words:
        if word in tsyssets.keys():
          result_tsys = tsyssets[word]
          tsys = tsyssets[word]
          prev_tsys = tsys

      if tsys in words:
        words.remove(tsys)

      for word in words:
        if word in numbers:
          time_value += numbers[word]
        if word in time_units and time_value:
          if word == 'час':
            time_value *= 60

      time_and_tsyssets = {'car': 15, 'bus': 30, 'ped': 30, 'bike': 30}
      time=0
      if time_value != 0:
        time = time_value
      else:
        if tsys != '':
          time = time_and_tsyssets[tsys]
        else:
          time = 0

      # entry['maxdistance'] = 'NOT_SPECIFIED'
      # entry['mindistance'] = 'NOT_SPECIFIED'
      startpoint='FROM_CLIENT'
      if i == 1:
        startpoint = 'FROM_CLIENT'
      else:
        startpoint = 'PREVIOUS_POINT'

      for word in words:
        if ':place' in word:
          word = word.replace(':place', '')
          place_words.append(word)

      if word in rus_eng_names.keys():
        current_image_path = image_path + rus_eng_names[word] + '.jpg'
      else:
        current_image_path = image_path + 'default.jpg'

      destpoint = DestPoint(name='', category=' '.join(place_words))
      entry = Entry(entryno=i,tsys=result_tsys,time=time,startpoint=startpoint,destpoint=destpoint,img = current_image_path)
      entries.append(entry)
      time_value = 0
      tsys = ''
      i += 1
      place_words = []

    id_ = str(uuid.uuid4())

    response = RouteResponse(id=id_,entries=entries)
    print(response)
  return response
