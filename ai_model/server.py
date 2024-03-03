import socket
import os
import json
import re
from keyword_extraction import extract
from make_json import make_json


is_image = False

def create_response(status_code, content_type, content):
    response = f"HTTP/1.1 {status_code}\r\n"
    response += f"Content-Type: {content_type}\r\n"
    response += f"Content-Length: {len(content.encode('utf-8'))}\r\n"
    response += "Connection: close\r\n"
    response += "\r\n"  # Empty line to indicate the start of the response body
    response += content
    return response

def normalize_line_endings(s):
    r'''Convert string containing various line endings like \n, \r or \r\n,
    to uniform \n.'''

    return ''.join((line + '\n') for line in s.splitlines())

server = socket.socket()
server.bind(('', 80))
server.listen(1)



while True:

    conn, addr = server.accept()

    request = conn.recv(10240).decode()
    parsed_request = request.split('\r\n')
    method, path, _ = parsed_request[0].split(' ')

    matches = re.findall(r'{[^}]*}', request)
    response = ''

    if method == 'POST':
        if len(matches) > 0:
            data = json.loads(matches[0])
            coords = data['coords']
            tsys = data['tsys']
            with open('request.txt', 'w', encoding='utf-8-sig') as request_file:
                request_file.write(data['text'])

            extract('request.txt', "keywords.txt")
            make_json()
            with open("data.json", 'r', encoding = 'utf-8-sig') as file:
                response = json.load(file)

        else:
            response = ''
    else:
        print(request)
        index = path.find('images')
        path = path[index:]
        with open(path, 'rb') as file:
            image_data = file.read()
        is_image = True


    print(response)
    print('---------------')
    if not is_image:
        response = json.dumps(response, ensure_ascii=False)
        response = create_response("200 OK","application/json; charset=utf-8",response)
        response = response.encode('utf-8')
    else:
        response = create_response("200 OK","image/jpeg",image_data)


    print(response)
    conn.sendall(response)
    conn.close()

