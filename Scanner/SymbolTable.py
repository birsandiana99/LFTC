from SLL import *
class HashTable:
    def __init__(self, capacity=20):
        self.capacity = capacity
        self.dict = {}
        self.size = 0
        #initiate every key with an empty SLL that will later be used for collisions
        for x in range(1,capacity+1):
            self.dict[x] = SLL()

    #hash function - add the ascii codes of all the letters and make it mod capacity + 1
    def hash(self, key):
        key = str(key) #make sure it is a string
        m = self.capacity
        sum = 0
        for character in key:
            sum = sum + ord(character)
        hash_val = sum % m+1
        return hash_val


    #insert value into HT
    def insert(self, key):
        self.size += 1
        index = self.hash(key)
        self.dict[index].insert(key)
        return index

    #search for value in the HT
    def search(self, value):
        value = str(value)
        for key in self.dict.keys():
            if self.dict[key].search(value) != 0:
                return key, self.dict[key].search(value)
        return 0

    #print HT
    def print_hash(self):
        for key in self.dict.keys():
            if self.dict[key] is not None:
                print("At key", str(key), ": ")
                self.dict[key].print_sll()