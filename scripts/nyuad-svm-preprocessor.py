#!/usr/lib/python
# NYUAD SVM Project Preprocessor
# AI - Fall 2013
# Written by Lingliang Zhang

# A very simple Python preprocessor script that normalizes ARFF formatted
# data.  It counts the number of instances in each class, determines the
# minimum. It then takes a random subset of each class of the minimum size.

import sys
import re
import random

def init(file_handle):
  preamble = []
  attributes = []
  attribute_count = 0
  attribute = re.compile('@attribute')
  data = re.compile('@data')
  for i, line in enumerate(file_handle):
    preamble.append(line)
    if data.match(line):
      break
    if attribute.match(line):
      attributes.append(line.split(" ")[1])
      last_attribute_index = i
      attribute_count += 1
  if not attributes:
    raise Exception("No classes found in a file, did you pass a non-ARFF file?")
  question = ""
  for i, attribute_name in enumerate(attributes):
    question += "%d: %s\n" % (i, attribute_name)
  class_index = input("%sPlease select the class field:\n" % question)
  class_abs_index = last_attribute_index - len(attributes) + class_index + 1
  preamble[last_attribute_index], preamble[class_abs_index] =\
      preamble[class_abs_index], preamble[last_attribute_index]
  return class_index, preamble

def gather_classes(file_handle, class_index):
  classes = {}
  number_of_classes = 0
  for line in file_handle:
    data = line.rstrip("\n").split(",")
    if not number_of_classes:
      number_of_classes = len(data)
    data[number_of_classes - 1], data[class_index] =\
            data[class_index], data[number_of_classes - 1]
    if data[number_of_classes - 1] in classes:
      classes[data[number_of_classes - 1]].append(data)
    else:
      classes[data[number_of_classes - 1]] = [data]
  return classes

def main():
  if len(sys.argv) == 0:
    raise Exception("Err: please pass a file or list of files as arguments")
  else:
    for filename in sys.argv[1:]:
      with open(filename) as f:
        class_index, preamble = init(f)
        classes = gather_classes(f, class_index)
        class_count = map(lambda classname: len(classes[classname]), classes)
        min_count = min(class_count)
        min_allowed = input("Please enter the max instances allowed of any class:\n")
        if min_count > min_allowed:
          min_count = min_allowed
        data = []
        for key, value in classes.iteritems():
          print("Class %s has %d entries" % (key, len(value)))
          value = map(lambda lines: ",".join(lines), value)
          random.shuffle(value)
          data.append("\n".join(value[:min_count]))
        random.shuffle(data)
        with open("stripped-" + filename, "w") as ff:
          ff.write("".join(preamble))
          ff.write("\n".join(data))
          print("\nData cleanup complete for %s, all classes have been reduced to %d entries.\
            \nPrevious total entries: %d\nNew total entries: %d\n\
            \nOutput in stripped-%s" % (filename, min_count, sum(class_count),\
            min_count*len(class_count), filename))

if __name__ == "__main__":
  main()
