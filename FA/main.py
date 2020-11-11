from FA import *

def check_DFA(fa):
    print("Enter the sequence: ")
    sequence = input()
    if not self.dfa:
        return False
    for char in sequence:
        if char not in self.alphabet:
            return False
    for char in sequence:
        if not self.check_next_state(char):
            return False
    if not fa.final_states.__contains__(str(self.current_state)):
        return False
    return True






finiteAutomata = FiniteAutomata.fromFile('fa2.in')
print(finiteAutomata)
print("enter seq:")
sequence = input()
print(finiteAutomata.check_DFA(sequence))



