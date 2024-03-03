import json, pyaudio
from vosk import Model, KaldiRecognizer

model = Model('vosk-model-small-ru-0.4')
rec = KaldiRecognizer(model, 16000)

FRAMES_PER_BUFFER = 8000
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 16000

p = pyaudio.PyAudio()
stream = p.open(
    format=FORMAT,
    channels=CHANNELS,
    rate=RATE,
    input=True,
    frames_per_buffer=FRAMES_PER_BUFFER
)
stream.start_stream()

def listen():
    while True:
        data = stream.read(
            FRAMES_PER_BUFFER,
            exception_on_overflow=False
        )
        if (rec.AcceptWaveform(data)) and (len(data) > 0):
            answer = json.loads(rec.Result())
            if answer['text']:
                yield answer['text']


with open("request.txt", "w") as request_file:
    for text in listen():
        if text == 'конец':
            quit()
        else:
            request_file.writelines(text+ '\n')