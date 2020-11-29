from bs4 import BeautifulSoup
import urllib.request
import re

home_page = input("Web address: ")

req = urllib.request.Request(home_page, headers={
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36'})
html_page = urllib.request.urlopen(req)
soup = BeautifulSoup(html_page, 'html.parser')
title = home_page

for _title in soup.findAll('title'):
    title = _title.text

title = re.sub(r"\s+", ' ', title).replace('\n', '').replace('"', '')

css_components = set()

for link in soup.findAll('link', href=True):
    link_href = link.get('href').split('/')[-1]

    if link_href.endswith('.css'):
        css_components.add(link_href)

js_components = set()

for script in soup.findAll('script', src=True):
    script_src = script.get('src').split('/')[-1]

    if script_src.endswith('.js'):
        js_components.add(script_src)

presentation_weight = 0.17
behavior_weight = 0.83
complexity = presentation_weight * len(css_components) + behavior_weight * len(js_components)

print('@startuml')
print('!includeurl https://raw.githubusercontent.com/ebbypeter/Archimate-PlantUML/master/Archimate.puml')
print('title Complexity = ' + str(round(complexity, 2)))
print('Group([' + home_page + '], "' + home_page + '") {')

behavior = 'Web Page Behavior'
presentation = 'Web Page Presentation'

print('Group([' + presentation.replace(' ', '_') + '], "' + presentation + '") {')

for comp in css_components:
    print('Application_Component([' + comp + '], "' + comp.replace('_', '_\\n').replace('-', '-\\n') + '")')

print('}')
print('Group([' + behavior.replace(' ', '_') + '], "' + behavior + '") {')

for comp in js_components:
    print('Application_Component([' + comp + '], "' + comp.replace('_', '_\\n').replace('-', '-\\n') + '")')

print('}')
print('}')
print('@enduml')
