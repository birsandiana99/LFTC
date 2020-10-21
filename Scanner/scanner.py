from language import *
import re

def modelate_tokens(line, separators):
    token = ''
    index = 0

    while index < len(line):
        # if string, take whole string constant
        if line[index] == '-':
            if token:
                yield token
            token, index = get_str_token(line, index)
            yield token
            #reset
            token = ''

        elif is_operator(line[index]):
            if token:
                yield token
            token, index = get_full_operator(line, index)
            yield token
            token = ''

        elif line[index] in separators:
            if token:
                yield token
            token, index = line[index], index + 1
            yield token
            token = ''

        else:
            token += line[index]
            index += 1
    if token:
        yield token

def is_identifier(token):
    # numbers, letters and _
    # first must be a letter
    # max length - 10
    return re.match(r"^[a-zA-Z]([a-zA-Z]|[0-9]|_){,9}$", token) is not None

def is_constant(token):
    # int or str between - -
    return re.match('^(0|[+-]?[1-9][0-9]*)$', token) is not None or re.match('^\-.*\"$', token) is not None

def is_operator(token):
    # verify if token is an operator or part of a one (eg: - for string: -abc-)
    return token in operators

def get_full_operator(line, i):
    # get final pos of an operator
    token = ''
    while i < len(line) and is_operator(line[i]):
        token += line[i]
        i += 1
    return token, i

def get_str_token(line, index):
    token = ''
    str_separators = 0

    while index < len(line) and str_separators < 2:
        if line[index] == '-':
            str_separators += 1
        token += line[index]
        index += 1

    return token, index