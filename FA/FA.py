class FiniteAutomata:
    def __init__(self, Q, E, T, q0, F, dfa):
        '''
        Initialise a FA with
        S=initial states,
        E=alphabet,
        T=transitions,
        q0=final state,
        F=final state(s)
        '''
        self.Q = Q
        self.E = E
        self.T = T
        self.q0 = q0
        self.F = F
        self.dfa = dfa

    @staticmethod
    def parseLine(line):
        '''
        input line: a line from a file(string)
        output: a list that contains the lines striped of ',' , ' ' and '{'
        '''
        return [value.strip() for value in line.strip().split('=')[1].strip()[1:-1].strip().split(',')]


    @staticmethod
    def fromFile(fileName):
        '''
        input: fileName - a string representing the name of the file we want to read and parse from
        output: an object of type FiniteAutomata that has all the attributes needed (S=initial states, E=alphabet, T=transitions,
                q0=final state, F=final state(s)
        '''
        with open(fileName) as file:
            Q = FiniteAutomata.parseLine(file.readline())
            # print("-----", Q)
            E = FiniteAutomata.parseLine(file.readline())
            q0 = file.readline().split('=')[1].strip()
            F = FiniteAutomata.parseLine(file.readline())
            #concatenate the rest of the elements from the file, transitions are the last in the file
            T = FiniteAutomata.parseTransitions(FiniteAutomata.parseLine(''.join([line for line in file])))

            # self.dfa =
            # print(T[1])
            return FiniteAutomata(Q, E, T[0], q0, F, T[1])


    @staticmethod
    def parseTransitions(parts):
        '''
        input: parts: a list of strings that represent the transitions
        output: result: a list of tuples containing ((state1, route),state2)
        '''
        # print("Parts:",parts)
        result = []
        result2 = {}
        transitions = []
        dfa = True
        index = 0
        #form a list for the transitions
        while index < len(parts):
            transitions.append(parts[index] + ',' + parts[index + 1])
            index += 2
        # print("Transitions", transitions)
        #format the list of transitions: split by '->' and construst the left and right hand side
        for transition in transitions:
            lhs, rhs = transition.split('->')
            state2 = rhs.strip()
            state1, route = [value.strip() for value in lhs.strip()[1:-1].split(',')]
            result.append(((state1, route), state2))
            if (state1, route ) in result2.keys():
                dfa = False
            result2[(state1, route)] = state2
            # print("0000",result2)
        return (result,dfa)

    def check_DFA(self, sequence):
        if self.dfa==False:
            return False
        curr = self.q0
        for char in sequence:
            if char not in self.E:
                return False
            for trans in self.T:
                # print("----",trans)
                if curr == trans[0][0] and trans[0][1] == char:
                    # print("00",trans[0][0])
                    # print("01",trans[0][1])
                    # print("1`",trans[1])

                    curr = trans[1]
                    break
        if curr not in self.F:
            return False
        return True

    def __str__(self):
        return 'States = { ' + ', '.join(self.Q) + ' }\n' \
               + 'Alphabet = { ' + ', '.join(self.E) + ' }\n' \
               + 'Final States = { ' + ', '.join(self.F) + ' }\n' \
               + 'Trans = { ' + ', '.join([' -> '.join([str(part) for part in trans]) for trans in self.T]) + ' }\n' \
               + 'Initial State = ' + str(self.q0) + '\n' \
               + 'DFA = ' + str(self.dfa) + '\n'
