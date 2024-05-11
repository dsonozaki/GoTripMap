import string
import random

import pyotp


def createOTPKey() -> str:
  return pyotp.random_base32()


def getOTPCode(key: str) -> str:
  otp = pyotp.TOTP(key,interval=60)
  return otp.now()


def checkOTPCode(key: str, code: str) -> bool:
  otp = pyotp.TOTP(key,interval=60)
  return otp.now() == code


def generateHash() -> str:
  characters = string.ascii_letters + string.digits
  return ''.join(random.choice(characters) for _ in range(45))
