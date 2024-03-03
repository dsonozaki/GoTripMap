import json
import uuid

<<<<<<< HEAD
id_ = str(uuid.uuid4())
entries = []
data = {
    "id": id_,
    "entries": []
}

tsys = ''
#транспортные средства
tsyssets = {'велосипед': 'bike', 'пешком': 'ped','автобус': 'bus',
            'троллейбус': 'trolley', 'трамвай': 'tram', 'машина': 'car'}

with open("keywords.txt", "r") as keywords:
    lines = keywords.readlines()
    i = 1
    for line in lines:
        entry = {}
        words = line.split(" ")
        entry['entryno'] = i
        entry['tsys'] = 'ped'

        for word in words:
            if word in tsyssets.keys():
                entry['tsys'] = tsyssets[word]
                tsys = word

        if tsys in words:
            words.remove(tsys)

        entry['time'] = 'NOT_SPECIFIED'
        entry['maxdistance'] = 'NOT_SPECIFIED'
        entry['mindistance'] = 'NOT_SPECIFIED'

        if i == 1:
            entry['startpoint'] = 'FROM_CLIENT'
        else:
            entry['startpoint'] = 'PREVIOUS_POINT'

        destpoint = {'name': '', 'category': words[0]}
        entry['destpoint'] = destpoint
        entries.append(entry)
        i += 1

    data['entries'] = entries

json_string = json.dumps(data)

with open("data.json", "w") as file:
    file.write(json_string)
=======
def make_json():
    #присваиваем каждому запросу свой ID
    id_ = str(uuid.uuid4())
    entries = []

    #тело запроса
    data = {
        "id": id_,
        "entries": []
    }

    tsys = ''
    prev_tsys = ''

    #транспортные средства (передается от клиента нажатием на соответствующую иконку, но текст запроса в приоритете)
    tsyssets = {'велосипед': 'bike', 'пешком': 'ped', 'автобус': 'bus', 'автомобиль' : 'car',
                'троллейбус': 'trolley', 'трамвай': 'tram', 'машина': 'car', 'такси': 'taxi'}

    #путь к изображению
    image_path = 'http://158.160.116.195/gotripmap/ai_model/images/'

    #числительные
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
    
    #для изображений
    rus_eng_names = {
        'бар' : 'bar',
        'кафе' : 'cafe',
        'аптека' : 'chemistry',
        'кино' : 'cinema',
        'лес' : 'forest',
        'парикмахерская' : 'hairdresser',
        'больница' : 'hospital',
        'поликлиника' : 'hospital',
        'музей' : 'museum',
        'парк' : 'park',
        'ресторан' : 'restaurant',
        'магазин' : 'shop',
        'театр' : 'theatre'
    }

    time_units = ['час', 'минута']
    time_units_indices = []
    time_value = 0
    place_words = []

    #открываем файл с ключевыми словами
    with open("keywords.txt", "r", encoding='utf-8-sig') as keywords:
        lines = keywords.readlines()
        i = 1
        for line in lines:
            entry = {}
            words = line.split(" ")
            words = [word.rstrip('\n') for word in words]
            entry['entryno'] = i

            if tsys == '' and prev_tsys == '':
                entry['tsys'] = 'ped'
            elif tsys == '' and prev_tsys != '':
                entry['tsys'] = prev_tsys

            for word in words:
                if word in tsyssets.keys():
                    entry['tsys'] = tsyssets[word]
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

            if time_value != 0:
                entry['time'] = time_value
            else:
                if tsys != '':
                    entry['time'] = time_and_tsyssets[tsys]
                else:
                    entry['time'] = 0

            #entry['maxdistance'] = 'NOT_SPECIFIED'
            #entry['mindistance'] = 'NOT_SPECIFIED'

            if i == 1:
                entry['startpoint'] = 'FROM_CLIENT'
            else:
                entry['startpoint'] = 'PREVIOUS_POINT'

            for word in words:
                if ':place' in word:
                    word = word.replace(':place', '')
                    place_words.append(word)
            
            if word in rus_eng_names.keys():
                current_image_path = image_path + rus_eng_names[word] + '.jpg'
            else:
                current_image_path = image_path + 'default.jpg'
            
            destpoint = {'name': '', 'category': ' '.join(place_words)}
            entry['destpoint'] = destpoint
            entry['img'] = current_image_path
            entries.append(entry)
            time_value = 0
            tsys = ''
            i += 1
            place_words = []

        data['entries'] = entries
        

    json_string = json.dumps(data, ensure_ascii=False, indent=4)

    #непосредственно создание json
    with open("data.json", "w", encoding='utf-8-sig') as file:
        file.write(json_string)
>>>>>>> ai_model
