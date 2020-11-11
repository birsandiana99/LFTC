from FA import *

finiteAutomata = FiniteAutomata.fromFile('fa.in')
print(finiteAutomata)
print("Enter the sequence: ")
sequence = input()
print(finiteAutomata.check_DFA(sequence))
