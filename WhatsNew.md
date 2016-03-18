# Changes in 0.2.0a #

## Features ##
  * Bilingual alignment visualization (see AlignmentFormat for the data format)
  * Refactoring: now instances of different types can have different renderers

## Enhancements ##
  * Error message when loading a corpus went wrong
  * filter text fields have suggested texts when empty

# Changes in 0.1.3 #

## Enhancements ##
  * Background of canvas now white on all OSs
  * Closing the main window now quits the system.
  * Quit menuitem in file menu introduced

# Changes in 0.1.2 #

## Features ##
  * Reads Lisp S-Expressions
  * Reads Malt-Tab Format

## Enhancements ##
  * Added source more code documentation
  * Refactored code
  * Packages renamed

# Changes in 0.1.1 #

## Enhancements ##
  * separation lines between spans of different (prefix) types
  * can read binary thebeast predicates as spans

## Bugfixes ##
  * edge labels were moved down slightly to be easier to read
  * dep edges can be selected again
  * collapsing to spans does not shorten spans anymore

# Changes in 0.1.0 #

## Features ##
  * Span visualization
  * Reads CoNLL 2000,2002,2003,2004
  * Reads markov thebeast format

## Enhancements ##
  * corpus loader now remembers state from last session
  * Changed edge type filter: now FP,FN and match extra checkboxes, more intuitive
  * file loading progress bar

## Bugfixes ##
  * [Issue 2](https://code.google.com/p/whatswrong/issues/detail?id=2) : drawing nodes with many edges results in edges being shifted to the left
  * [Issue 5](https://code.google.com/p/whatswrong/issues/detail?id=5) : first time filtering of types did not work


# Changes in 0.0.4 #

## Features ##
  * tokens can be removed from the graph using a filter that tests whether token properties contain (or equal) certain strings.
  * remove all tokens that don't have an edge
  * changed lucene analyzer to WhitespaceAnalyzer -> now searching for "role:R-AA" does break up the "R-AA" to find things with "A" in it.
  * gold and guess loader now remember their directory from previous session