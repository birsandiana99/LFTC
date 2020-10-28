# from SymbolTable import HashTable
#
# symbol_table = HashTable(5)
# print('inserted -abc- on pos ' + str(symbol_table.insert('abc')))
# print('inserted -variable1- on pos ' + str(symbol_table.insert('variable1')))
# print('inserted -variable2- on pos ' + str(symbol_table.insert('variable2')))
# print('inserted -variable3- on pos ' + str(symbol_table.insert('variable3')))
# print('inserted -variable4- on pos ' + str(symbol_table.insert('variable4')))
# print('inserted -123455- on pos ' + str(symbol_table.insert('123455')))
#
# print('-sdfgsdgsdgs- can be found at position ' + str(symbol_table.search('sdfgsdgsdgs')))
# print('-123455- can be found at position ' + str(symbol_table.search('123455')))
# symbol_table.print_hash()
from SymbolTable import *
from PIF import *
from scanner import *
from language import *
# file = open("input1.txt", 'r')
# for line in file:
#     print(line)
#
# with open("input1.txt", 'r') as file:
#     for line in file:
#         print([token for token in modelate_tokens(line, separators)])


input_files=["input1.txt","input2.txt","input3.txt","input_err.txt"]
output_files=["output1.txt","output2.txt","output3.txt","output_err.txt"]

def print_to_file(file, symbolTable, pif):
    f1 = file
    f1.write("Program Internal Form: \n")
    pif.print_pif(f1)
    f1.write("\n\n")
    f1.write('Symbol Table: \n')
    symbolTable.print_hash(f1)
    f1.write("\n\n")
    f1.write('\n\nCodification table: ')
    f1.write("\n")
    for e in codify:
        str_to_print = str(e) + "->" + str(codify[e])
        f1.write(str_to_print)
        f1.write("\n")

for i in range(0,4):
    in_f = input_files[i]
    ou_f = output_files[i]
    input_file = open(in_f, "r")
    output_file = open(ou_f, "w")
    is_err = 0
    l = 0
    symbolTable = HashTable()
    pif = PIF()
    for line in input_file:
        l += 1
        for token in modelate_tokens(line[0:-1], separators):
            if token in separators + operators + reserved_words:
                pif.add(codify[token], -1)
            elif is_identifier(token):
                id = symbolTable.insert(token)
                pif.add(codify['identifier'], id)
            elif is_constant(token):
                id = symbolTable.insert(token)
                pif.add(codify['constant'], id)
            else:
                is_err = 1
                str_to_print = "Unknown token " + str(token) + " at line " + str(l) + "\n"
                output_file.write(str_to_print)
                break
    # if not is_err:
    print_to_file(output_file, symbolTable,pif)

