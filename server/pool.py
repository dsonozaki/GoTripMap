import aiomysql
from aiomysql import Pool, Connection
from environs import Env
from singleton_decorator import singleton

@singleton
class MyPool:
  pool: Pool | None = None

  async def acquireConnection(self) -> Connection:
    if not self.pool:
      env: Env = Env()
      env.read_env(".env")
      self.pool = await aiomysql.create_pool(host='127.0.0.1', port=int(env("SQL_PORT")),
                                        user='root', password=env("PASS"),
                                        db='server')
    return self.pool.acquire()
