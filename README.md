## Backward-Chaining-Inference-Engine

###Description:
Implementation of an inference engine which takes a knowledge base and a query sentence as input and determines if the query can be inferred from the information given in the knowledge base using backward chaining.

###Input:
The knowledge base and the query in a text file called input.txt (to be updated to take filename from commandline).
The first line of the input file contains the query. The second line contains an integer n specifying the number of clauses in the knowledge base. The remaining lines contain the clauses in the knowledge base, one per line.
Each clause is written in one of the following forms:

1. as an implication of the form p1 ∧ p2 ∧ ... ∧ pn ⇒ q, whose premise is a conjunction of
atomic sentences and whose conclusion is a single atomic sentence.
2. as a fact with a single atomic sentence: q
Each atomic sentence is a predicate applied to a certain number of arguments (negation is not used)

###Output:
If the query sentence can be inferred from the knowledge base, output will return TRUE, otherwise, FALSE. 
The answer (TRUE/FALSE) will be written into output.txt (to be updated to take filename from commandline).

---

###Sample:
####Input:
Diagnosis(John,Infected)
6
HasSymptom(x,Diarrhea)=>LostWeight(x)
LostWeight(x)&Diagnosis(x,LikelyInfected)=>Diagnosis(x,Infected)
HasTraveled(x,Tiberia)&HasFever(x)=>Diagnosis(x,LikelyInfected)
HasTraveled(John,Tiberia)
HasFever(John)
HasSymptom(John,Diarrhea)

####Output:
TRUE

####Note:

1. & denotes the AND operator.
2. => denotes the implication operator.
3. No other operators besides & and => are used.
4. Only single variable is used (denoted by "x" - variables are standardized by the inference engine)
