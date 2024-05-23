import string
import random
from secrets import token_hex
import pyotp


def createOTPKey() -> str:
  return pyotp.random_base32()


def getOTPCode(key: str) -> str:
  otp = pyotp.TOTP(key,interval=60)
  return otp.now()


def checkOTPCode(key: str, code: str) -> bool:
  otp = pyotp.TOTP(key,interval=60)
  return otp.now() == code


def generateToken() -> str:
  return token_hex(16)
