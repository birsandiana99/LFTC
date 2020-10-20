from SymbolTable import HashTable

symbol_table = HashTable(5)
print('inserted -abc- on pos ' + str(symbol_table.insert('abc')))
print('inserted -variable1- on pos ' + str(symbol_table.insert('variable1')))
print('inserted -variable2- on pos ' + str(symbol_table.insert('variable2')))
print('inserted -variable3- on pos ' + str(symbol_table.insert('variable3')))
print('inserted -variable4- on pos ' + str(symbol_table.insert('variable4')))
print('inserted -123455- on pos ' + str(symbol_table.insert('123455')))

print('-sdfgsdgsdgs- can be found at position ' + str(symbol_table.search('sdfgsdgsdgs')))
print('-123455- can be found at position ' + str(symbol_table.search('123455')))
symbol_table.print_hash()
