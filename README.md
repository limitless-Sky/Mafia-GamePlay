# Mafia-Game
Mafia is a popular party game that requires one to think, manipulate, and deceive others in order to win. In this assignment I had develop a mafia game simulator
OOP concepts, especially inheritance and polymorphism, demonstration of object
comparison, and object equality check.  

`Purpose:` Course Project at [IIITD](https://www.iiitd.ac.in/)

`Instructions:` (Credits- [Advanced Programming](http://techtree.iiitd.edu.in/viewDescription/filename?=CSE201)

# Mafia-GamePlay

Rules of the game:   
Plot: There is a village of N players. A player can be either a commoner, a detective, a healer,or a mafia.    
A commoner only knows that he is a commoner. A detective knows all the other detectives. A mafia knows all the other Mafia players.  
A healer knows all other healers in the game. 

Objectives: The objective for the mafias is to kill or eliminate all the non-mafias such that the
ratio of the alive mafias to all others is 1:1. A player can be eliminated in two ways:   
- By being killed by the Mafia   
- Be eliminated in a vote out.     

Once a player is eliminated, they cannot be brought back to life. Mafias cannot kill themselves. 
The objective for all other players(except the mafias) is to eliminate the mafias through a vote
out(as the Mafias cannot be killed). Therefore, by using special powers of detectives and healers, they are required to save themselves and vote out the Mafias.

Run Command:
```sh
$ javac Mafia.java Player.java voteHandler.java
$ java Mafia
```


![Mafia](https://img.freepik.com/free-vector/mafia-logo_74829-29.jpg?size=338&ext=jpg)  
