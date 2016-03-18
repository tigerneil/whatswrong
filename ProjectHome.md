What's Wrong With My NLP?: A visualizer for Natural Language Processing problems.

# Features #
  * (Jointly) visualize
    * syntactic dependency graphs
    * semantic dependency graphs (a la CoNLL 2008)
    * Chunks (such as syntactic chunks, NER chunks, SRL chunks etc.)
    * Bilingual [alignments](AlignmentFormat.md)
    * BioNLP events, proteins, locations
    * Generic [format](GenericFormat.md) to load and visualize your own data.
  * Compare gold standard trees to your generated trees (e.g. highlight false positive and negative dependency edges)
  * Filter trees and visualize only what's necessary, for example
    * only dependency edges with certain labels
    * only the edges between certain tokens
  * Search corpora for sentences with certain attributes using powerful [search expressions](http://lucene.apache.org/java/2_3_1/queryparsersyntax.html), for example
    * search for all sentences that contain the word "vantage" and the pos tag sequence DT NN
    * search for all sentences that contain false positive edges and the word "vantage"
  * Reads
    * CoNLL 2000, 2002, 2003, 2004, 2006 and 2008 format
    * Lisp S-Expressions
    * Malt-Tab format
    * [markov thebeast](http://thebeast.googlecode.com) format
    * [BioNLP 2009 Shared Task](http://www-tsujii.is.s.u-tokyo.ac.jp/GENIA/SharedTask/) format (see example graph below and check [how to load](HowToLoadBioNLPSharedTaskData.md) the annotation files).
  * Export to EPS
  * Provides API that allows you to incorporate NLP visualization in your application

Check this [screenshot](http://whatswrong.googlecode.com/svn/branches/whatswrong-0.1.x/doc/screenshots/Whats%20Wrong%3f.png) to get a better idea.

# News #

## Version 0.2.4 released ##
Some bug fixes, and deployment to maven central. You can now use the whatswrong library in your maven project with

```
<dependency>
  <groupId>org.riedelcastro</groupId>
  <artifactId>whatswrong</artifactId>
  <version>0.2.4</version>
</dependency>
```

## Version 0.2.3 released ##
July 1st 2010: Added support for additional description for edges and spans to be printed when clicking on these. Minor bugfixes.

## Version 0.2.2 released ##
Some minor bugfixes: fixed loading of UTF-8 files, and square brackets in tab files.

## Version 0.2.1 released ##
Some minor bugfixes. See [changes](http://maven.riedelcastro.org/whatswrong/changes-report.html).

## Version 0.2.0 released ##
This version supports the data format of the [BioNLP 2009 Shared Task](http://www-tsujii.is.s.u-tokyo.ac.jp/GENIA/SharedTask/) and jointly displays proteins, cites, events, their arguments and event clues.

Moreover, version 0.2.0 is now built with [maven](http://maven.apache.org/).  A more verbose list of changes can be found [here](http://maven.riedelcastro.org/whatswrong/changes-report.html).

# How to run #
Download the jar file and execute
```
java -jar whatswrong-standalone-x.y.z.jar
```

# Screenshots #
## CoNLL 2008 ##
This is a fraction of a semantic dependency graph that compares a gold labelling to a system labelling. The red edges are false positives, the blue ones false negatives and the black ones are matches:

![http://whatswrong.googlecode.com/svn/branches/whatswrong-0.1.x/doc/screenshots/CoNLL2008.png](http://whatswrong.googlecode.com/svn/branches/whatswrong-0.1.x/doc/screenshots/CoNLL2008.png)

## CoNLL 2003 ##
This shows the comparison of two shallow parses and two NER labellings (again false positives are red, false negatives are blue and matches are black):

![http://whatswrong.googlecode.com/svn/branches/whatswrong-0.1.x/doc/screenshots/CoNLL2003.png](http://whatswrong.googlecode.com/svn/branches/whatswrong-0.1.x/doc/screenshots/CoNLL2003.png)

## Alignment ##
This shows the comparison of two alignments between a German and an English sentence. Again false positive alignments are red, false negatives are blue and matches are black. Note alignment visualization is not available before version 0.2.0a and that we use [this file format](AlignmentFormat.md).

![http://whatswrong.googlecode.com/svn/tags/whatswrong-0.2.0/doc/screenshots/alignment.png](http://whatswrong.googlecode.com/svn/tags/whatswrong-0.2.0/doc/screenshots/alignment.png)

## BioNLP 2009 Event Extraction ##

This shows the comparison between two event annotations for BioNLP 2009 Shared task data. As usual, blue edges and spans are false negatives, red ones are false positives.

![http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/bionlp09.png](http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/bionlp09.png)

Note that the visualizer visualizes a complete abstract (as a opposed to a sentence-based visualization) from left to right. Also note that whatswrong is essentially token-based, so for mentions which do not fully cover tokens (such as "binding" in "DNA-binding" still the complete token is marked as mention.

# Documentation #
Most of the functionality can hopefully be understood by just playing around with the example graph. For source documentation check the [JavaDoc](http://maven.riedelcastro.org/whatswrong/apidocs/index.html).

# Questions? #
Just join the Discussion group and post your question there.
