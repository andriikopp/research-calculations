from bs4 import BeautifulSoup
import urllib.request
import validators
import re

home_page = 'http://iscrystals.com/?locale=ru'

links = {}
req = urllib.request.Request(home_page, headers={
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36'})
html_page = urllib.request.urlopen(req)
soup = BeautifulSoup(html_page, 'html.parser')
title = home_page

for _title in soup.findAll('title'):
    title = _title.text

title = re.sub(r"\s+", ' ', title).replace('\n', '').replace('"', '')

for link in soup.findAll('a', href=True):
    link_href = link.get('href')

    if not validators.url(link_href):
        link_href = home_page + link_href

    if home_page not in link_href:
        continue

    if link.text is not None and link.text != '' and link.text not in links.values():
        links[link_href] = link.text

print('@startuml')
print('!includeurl https://raw.githubusercontent.com/ebbypeter/Archimate-PlantUML/master/Archimate.puml')
print('Group([' + home_page + '], "' + title + '") {')

for key in links:
    print('Business_Service([' + key + '], "' + re.sub(r"\s+", ' ', links[key])
          .replace('\n', '')
          .replace('"', '')
          .replace(' ', '\\n') + '")')

print('}')
print('@enduml')
