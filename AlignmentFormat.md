# Introduction #

This page describes the alignment format ("Gale Alignment") that can be used with whatswrong-0.2.x.

# Format #

The source tag element contains the tokenized source sentence, the translation element contains the target tokenized sentence. The matrix element contains a matrix in which the first row and first column indicate which tokens are null-aligned, and the remainder of the matrix is simply the alignment matrix where each column corresponds to a source token, and each row corresponds to a target token. The seg element can contain the id of the sentence, but doesn't have to. It's only important that there is a seg element for each sentence.

```
<seg id=1>
<source>Ich habe den Fehler in meiner Sprachverarbeitung gefunden .</source>
<translation>I've found the error in my NLP .</translation>
<matrix>
0 0 0 0 0 0 0 0 0 0
0 1 1 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 1 0
0 0 0 1 0 0 0 0 0 0 
0 0 0 0 1 0 0 0 0 0 
0 0 0 0 0 1 0 0 0 0 
0 0 0 0 0 0 1 0 0 0
0 0 0 0 0 0 0 1 0 0
0 0 0 0 0 0 0 0 0 1
</matrix>
<seg id=2>
...


```