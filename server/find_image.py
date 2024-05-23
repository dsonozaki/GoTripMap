from environs import Env
from google_images_search import GoogleImagesSearch
from singleton_decorator import singleton


@singleton
class ImageFinder:
  gis: GoogleImagesSearch | None = None
  async def load_and_find_image(self,query: str) -> str:
    if not self.gis:
      env: Env = Env()
      env.read_env(".env")
      self.gis = GoogleImagesSearch(env("GOOGLE_SEARCH_API"), env("GOOGLE_SEARCH_ID"))
    search_params = {
      'q': query + " imagesize:1280x720",
      'num': 10,
      'fileType': 'jpg|png',
      'imgType': 'photo'
    }
    self.gis.search(search_params=search_params)
    result = ""
    for image in self.gis.results():
      if not "youtube" in image.referrer_url:
        result = image.url
        break
    return result
