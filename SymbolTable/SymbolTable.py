from SLL import *
class HashTable:
    def __init__(self, capacity=20):
        '''
        initiate the hash table with a capacity that is set to 20 if none is provided
        :param capacity: int
        '''
        self.capacity = capacity
        self.dict = {}
        self.size = 0
        #initiate every key with an empty SLL that will later be used for collisions
        for x in range(1,capacity+1):
            self.dict[x] = SLL()

    #hash function - add the ascii codes of all the letters and make it mod capacity + 1
    def hash(self, key):
        '''
        :param key: a string or an integer that will be made a string
        :return: hash_val - an integer that represents the hashing value of the key, represented by a value from 1 to capacity
        '''
        key = str(key) #make sure it is a string
        m = self.capacity
        sum = 0
        for character in key:
            sum = sum + ord(character)
        hash_val = sum % m+1
        return hash_val


    #insert value into HT
    def insert(self, key):
        '''
        :param key: a string - the key that will be inserted in the hash table at a position represented by its hash code
        :return: index - an integer that represents the position the key was inserted at
        '''
        self.size += 1
        index = self.hash(key)
        self.dict[index].insert(key)
        return index

    #search for value in the HT
    def search(self, value):
        '''
        :param value: string - a value that will be searched in the hash table
        :return: a tuple (key, pos), where key is the index of the value in the hash table and pos is the position of the value in the SLL from the key position
        eg: if at pos 2 we have : 2->3->5 and we search for element 3 it will return (2,1)
        '''
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