class PIF:
    def __init__(self):
        self.__table = []

    def add(self, code, id):
        self.__table.append((code, id))

    def print_pif(self,file):
        for el in self.__table:
            str_to_print = str(el[0]) + "|" + str(el[1])
            file.write(str_to_print)
            file.write("\n")