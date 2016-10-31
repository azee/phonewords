# phonewords

[![Join the chat at https://gitter.im/azee/phonewords](https://badges.gitter.im/azee/phonewords.svg)](https://gitter.im/azee/phonewords?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Jenkins Build](http://azee.people.yandex.net/jenkins/buildStatus/icon?job=phonewords)](http://azee.people.yandex.net/jenkins/job/phonewords)

Reasoning
==========

I have selected 1-800-CODING-CHALLENGE because:
* always wanted to build a real-life application that involves trie-trees
* even if I won't sucseed this code can be used for my future projects

Design
==========

Parts
------
Code is divided into 3 major parts.

1. Core. Business logic of trie-tree traversal
2. Dictionary provider - a possibility to implement words providers from different sources
3. Executor. A possibility to implement different user interaction strategies

Core
------
The core of the solution is a trie-tree. The point is to build a trie-tree out of the list of words in the dictionary. Each edge will be a letter. A node may or may not contain a word from the dictionary. The idea is to traverse all possible branches for each provided number trying to build a list of possible substitutions as we hit a node with a word. 

Dictionary tree is build on application startup and can be used for each phone number provided.

Dictionary provider
------

As we may want to use different dictionary sources (e.g. default, file specific, network specific, database) a dictionary provider factory is implemented. 

If ```-Ddictionary="PATH_TO_DICTIONARY"``` is passed we read data from file. Otherwise we use the default one. It uses a list of 5000 most popular English words and is stored in resources.

Executor
------

We may want to implement different kind of interactions with user. It may be an single-run output for a provided file with phone numbers listed. Or it may be an interactive console application when user enters numbers one by one. We also may want to implement a web service or database scheduled actions or something else. 

Executor factory is implemented for that reason. If ```-Ddata="PATH_TO_NUMBERS_LIST"``` is provided — data will be fetched from a file. If not — STDIN executor will take place. CTRL + C or type «quit» or «exit» to exit.

It is also possible to override printValues method of executor base class so it is possible to work with other output streams (DB, http).

Building
==========

To build a project you have to run maven:
```
mvn clean install
```

Running
==========

Default dictionary and interactive IO:
```
java -jar phonenumbers.jar
```

Customisation - ```dictionary``` and ```data``` parameters are optional
```
java -jar  -Ddictionary="PATH_TO_DICTIONARY" -Ddata="PATH_TO_NUMBERS_LIST"  phonenumbers.jar
```

Source 
==========
Code: https://github.com/azee/phonewords

Release: https://github.com/azee/phonewords/releases/latest

