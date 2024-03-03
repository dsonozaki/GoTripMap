from rutermextract import TermExtractor
<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> ai_model
import pymorphy2

morph = pymorphy2.MorphAnalyzer()
term_extractor = TermExtractor()

<<<<<<< HEAD
features = []
feature_indices = []

#открываем файл с текстом запроса и создаем файл с ключевыми словами
with open("request.txt", "r", encoding='utf-8') as request, open("keywords.txt", "w", encoding='utf-8') as keywords:
    for line in request.readlines():
        words = line.split(" ")

        #ищем слова-соединители (для составных запросов)
        for i in range(0, len(words)):
            if words[i] in ['затем', 'далее', 'потом', 'после', 'ещё', 'и']:
                words[i] = '*'

        #непосредственно выделяем ключевые слова с помощью нейросети
        for term in term_extractor(line):
            features.append(str(term.normalized))

        #если вдруг к слову присоединилось соседнее (возможно, не ключевое), то удаляем
        for feature in features:
            if len(feature.split(" ")) > 1:
                features.remove(feature)
                features.extend(feature.split(" "))
            else:
                continue

        #приводим слова в запросе к начальной форме (так как ключевые в нач.форме)
        words = [morph.parse(word)[0].normal_form for word in words]

        #если образовалось слово, которого нет в изначальном запросе, то удаляем
        for feature in features:
            if not feature in words:
                features.remove(feature)

        #заменяем ненужные слова на '-' и удаляем
        words = [word if any([word in features, word == '*']) else '-' for word in words]
        words = [w for w in words if w != '-']

        #собираем список из фич для каждого подзапроса
        output_list = []
        temp_list = []
        for item in words:
            if item == '*':
                output_list.append(temp_list)
                temp_list = []
            else:
                temp_list.append(item)
        if temp_list:
            output_list.append(temp_list)

        for inner_list in output_list:
            line = ' '.join(inner_list)
            keywords.write(line + '\n')
=======
term_extractor = TermExtractor()

with open("request.txt", "r") as request, open("keywords.txt", "w") as keywords:
    for line in request.readlines():
        for term in term_extractor(line):
            keywords.writelines(str(term.normalized) + ' ' + str(term.count) + '\n')

#JSON?
>>>>>>> 83339cca2cecfdc8b3a495fbd6f7995a922737ee
=======
feature_indices = []
features = []

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
>>>>>>> ai_model
