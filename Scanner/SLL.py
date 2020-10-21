# implementation of a singly linked list

class Node:
    def __init__(self, val=None):
        self.val = val
        self.next = None

class SLL:
    def __init__(self, head=None):
        self.head = head

    #search for a value in the SLL
    def search(self, value):
        index = 1
        curr = self.head
        while curr is not None:
            if curr.val == value:
                return index
            else:
                curr = curr.next
                index += 1
        return 0

    #insert a node with a given value into the SLL
    def insert(self, val):
        newNode = Node(val)
        newNode.next = None
        if self.head is None:
            self.head = newNode
            return 1
        else:
            index = 1
            curr = self.head
            while curr.next is not None:
                curr = curr.next
                index += 1
            curr.next = newNode
            return index + 1
        return 0


    #print SLL
    def print_sll(self):
        curr = self.head
        while curr is not None:
            if curr.next is not None:
                print( str(curr.val) + " -> ", end = "")
            else:
                print(str(curr.val))
            curr = curr.next
