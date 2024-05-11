import ai_model.pymorphy2 as pymorphy2
from ai_model.rutermextract import TermExtractor

morph = pymorphy2.MorphAnalyzer()
term_extractor = TermExtractor()


# функция для разбиения списка на подсписки
def split_list_by_star(input_list):
    result = []
    sublist = []
    for item in input_list:
        if item != '*':
            sublist.append(item)
        else:
            result.append(sublist)
            sublist = []
    if sublist:
        result.append(sublist)
    return result


def flatten_nested_list(nested_list):
    flat_list = []
    for sublist in nested_list:
        for item in sublist:
            flat_list.append(item)
    return flat_list


time_units = ['минута', 'час']
tsyssets = ['велосипед', 'автобус', 'машина', 'пешком', 'автомобиль']
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



# открываем файл с текстом запроса и создаем файл с ключевыми словами
def extract(in_file='request.txt', out_file='keywords.txt'):
    feature_indices = []
    features = []

    with open(in_file, "r", encoding='utf-8-sig') as request, open(out_file, "w", encoding='utf-8-sig') as keywords:
        for line in request.readlines():
            words = line.split(" ")
            # приводим слова в запросе к начальной форме (так как ключевые в нач.форме)
            words = [morph.parse(word)[0].normal_form for word in words]

            # ищем слова-соединители (для составных запросов)
            for i in range(0, len(words)):
                if words[i] in ['затем', 'далее', 'потом', 'после', 'ещё', 'и']:
                    words[i] = '*'

            # разделяем запрос на части по слову-разделителю
            parts = split_list_by_star(words)

            # непосредственно выделяем ключевые слова с помощью нейросети
            for part in parts:
                tmp = []
                part1 = ' '.join(part)
                for term in term_extractor(part1):
                    tmp.append(str(term.normalized))
                for word in tmp:
                    if len(word.split(' ')) > 1:
                        tmp.remove(word)
                        tmp.extend(word.split(' '))

                # слова, которые обычно плохо распознаются
                if 'пешком' in part and not 'пешком' in tmp:
                    tmp.append('пешком')
                if 'парикмахерский' in part and not 'парикмахерская' in tmp:
                    tmp.append('парикмахерская')
                for word in part:
                    if word in ['минута', 'час'] and not word in tmp:
                        time_index = words.index(word)
                        time_unit = words[time_index]
                        time_value = words[time_index - 1]
                        tmp.append(time_unit + ' ' + time_value)
                if 'течение' in tmp:
                    tmp.remove('течение')
                if 'маршрут' in tmp:
                    tmp.remove('маршрут')

                for i in range(0, len(tmp)):
                    if not tmp[i] in time_units and not tmp[i] in tsyssets and not tmp[i] in numbers.keys():
                        tmp[i] = tmp[i] + ':place'
                features.append(tmp)

            # если вдруг к слову присоединилось соседнее (возможно, не ключевое), то удаляем
            for features_part in features:
                for feature in features_part:
                    if len(feature.split(" ")) > 1:
                        features_part.remove(feature)
                        features_part.extend(feature.split(" "))
                    else:
                        continue

            # если образовалось слово, которого нет в изначальном запросе, то удаляем
            # записываем получившееся в файл
            keywords.truncate(0)
            for features_part in features:
                line = ' '.join(features_part)
                keywords.write(line + '\n')

    # для тестов
    return flatten_nested_list(features)

if __name__ == '__main__':
    extract(in_file='request.txt', out_file='keywords.txt')
