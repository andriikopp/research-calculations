import re
from pattern.text.en import singularize
from flask import Flask, request, render_template

# Business Rules for entities pattern:
# (A | Each) {Entity} {relationship} (one | many) {Entity} .

# Business Rules for attributes pattern:
# (A | Each) {Entity} [is described by] {attr1}, {attr2}, ..., (and) {attrN} .

app = Flask(__name__)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/translate', methods=['GET', 'POST'])
def translate():
    # INPUT: Business rules text
    database_name = 'DB_SYNTHESIS'
    business_rules_text = 'Each Supplier is described by Address, Phone, and Note. A Supplier is a one Company.' \
                          'A Supplier is a one Person. Each Company is described by Title, Bank Account,' \
                          'and Representative. Each Person is described by Full Name, Debit Card, and Email. A ' \
                          'Supplier concludes many Contracts. Each Contract is described by Number, Date, and Comment.' \
                          'Each Contract contains of many Products. A Product is described by SKU, Name, Quantity, and ' \
                          'Price.'

    database_name = request.args.get('name')
    business_rules_text = request.args.get('rules')

    # STEP 1: Split business rules text into statements
    business_rules = business_rules_text.split('.')

    # STEP 2: Clean business rules statements
    clean_business_rules = []

    for business_rule in business_rules:
        # remove extra spaces in each business rule
        clean_business_rule = re.sub(r'\s+', ' ', business_rule).strip()

        # consider only not empty strings
        if clean_business_rule:
            clean_business_rules.append(clean_business_rule)

    # STEP 3: Formalize statements
    entities_relations = []
    entities_attributes = {}

    relations_regex = re.compile(r'(A|Each|a|each) \w+ [\w\s]+ (one|many) \w+')
    attributes_regex = re.compile(r'(A|Each|a|each) \w+ (is described by) [\w\s\,]+')

    for clean_business_rule in clean_business_rules:
        # tokenize statement
        tokens = clean_business_rule.split(' ')

        # check business rule type
        if relations_regex.match(clean_business_rule):
            # parse statement into subject, object, cardinality and relationship
            _subject = singularize(tokens[1])
            _object = singularize(tokens[len(tokens) - 1])
            _cardinality = tokens[len(tokens) - 2]
            _relationship = ' '.join([tokens[i] for i in range(2, len(tokens) - 2)])

            # formalize cardinality
            if _cardinality == 'one':
                _cardinality = '1:1'
            elif _cardinality == 'many':
                _cardinality = '1:M'

            entities_relations.append(
                {
                    'subject': _subject,
                    'relationship': _relationship,
                    'object': _object,
                    'cardinality': _cardinality
                }
            )
        elif attributes_regex.match(clean_business_rule):
            _subject = singularize(tokens[1])

            # parse entity attributes
            _attributes = ' '.join(tokens[i] for i in range(5, len(tokens)))
            _separate_attributes = _attributes.split(',')
            _clean_attributes = [re.sub(r'and\s+', ' ', attr).strip() for attr in _separate_attributes]

            if _subject not in entities_attributes.keys():
                entities_attributes[_subject] = _clean_attributes
            else:
                entities_attributes[_subject] += _clean_attributes

    # STEP 4: Extract entities
    entities = set()

    for entity_relation in entities_relations:
        entities.add(entity_relation['subject'])
        entities.add(entity_relation['object'])

    for entity in entities_attributes:
        entities.add(entity)

    # STEP 5: Map ER to tables
    tables = {}
    relations = []

    for entity in entities:
        key_data_type = 'INTEGER'

        tables[entity] = set()

        # add declared attributes
        if entity in entities_attributes.keys():
            tables[entity].update(['`' + re.sub(r'\s+', '_', attribute) + '` ' + detect_type(attribute) for attribute in
                                   entities_attributes[entity]])

        for relationship in entities_relations:
            if relationship['object'] == entity:
                # establish references according to the cardinality
                if relationship['cardinality'] == '1:M':
                    tables[entity].update(['`' + entity + '_ID`' + ' ' + key_data_type + ' AUTO_INCREMENT PRIMARY KEY'])
                    tables[entity].update(['`' + relationship['subject'] + '_ID` ' + key_data_type])
                elif relationship['cardinality'] == '1:1':
                    tables[entity].update(['`' + relationship['subject'] + '_ID` ' + key_data_type + ' PRIMARY KEY'])

                # establish foreign keys
                relations.append(
                    'ALTER TABLE `' + entity + '` ADD FOREIGN KEY (`' + relationship[
                        'subject'] + '_ID`) REFERENCES `' +
                    relationship['subject'] + '` (`' + relationship['subject'] + '_ID`);')
            elif relationship['subject'] == entity:
                tables[entity].update(['`' + entity + '_ID`' + ' ' + key_data_type + ' AUTO_INCREMENT PRIMARY KEY'])

    # OUTPUT: DDL statements
    output = 'DROP DATABASE IF EXISTS `' + database_name + '`;\n'
    output += 'CREATE DATABASE `' + database_name + '`;\n'
    output += 'USE `' + database_name + '`;\n'

    for table in tables:
        output += 'CREATE TABLE `' + table + '`\n'
        output += '(\n'

        fields = list(tables[table])

        for i in range(0, len(fields)):
            if i == len(fields) - 1:
                output += '\t' + fields[i] + '\n'
            else:
                output += '\t' + fields[i] + ',\n'

        output += ');\n'

    for relation in relations:
        output += relation + '\n'

    return output


# function to suggest data type by field name
def detect_type(filed_name):
    data_type = 'VARCHAR(255)'

    types_tags = {
        # string tags
        'name': 'string', 'title': 'string', 'comment': 'string', 'description': 'string', 'text': 'string',
        'note': 'string',

        # number tags
        'number': 'number', 'count': 'number', 'amount': 'number', 'price': 'number', 'cost': 'number',
        'point': 'number', 'score': 'number', 'quantity': 'number',

        # date/time tags
        'date': 'datetime', 'time': 'datetime'
    }

    types_mysql = {
        'string': 'VARCHAR(255)',
        'number': 'DECIMAL(18,2)',
        'datetime': 'DATETIME',
        'blob': 'BLOB'
    }

    # match tags in field names and suggest data types
    for tag in types_tags:
        if tag in filed_name.lower():
            data_type = types_mysql[types_tags[tag]]
            break

    return data_type


if __name__ == '__main__':
    app.run()
