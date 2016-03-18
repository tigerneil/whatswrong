# Introduction #

whatswrong provides a generic format that you can use to visualize data even if it is not
in one of the existing formats.


# Format #

Let us start by an example:
```
>>
>Word
0 "The"
1 "man"
2 "likes"
3 "football"
4 "."

>Mentions
0  1  "Person"   "Some Description Here, will appear when span is clicked on"
3  3  "Concept"

>Relations
2  1  "Agent"    "Some Description Here, will appear when edge is clicked on"
2  3  "Theme"    "Use-BR-to create line breaks in description"

>>
...
```

Note that whitespace between columns **has to be TABS**. Also note that descriptions can be omitted.

This file defines a sentence start using `>>`. Then it defines a token property `Word` that will result in
tokens with words in the visualizer. Next we define a span property `Mentions` that will yield labelled spans in the visualizer. Finally, we add `Relations` for a labelled edges between tokens. Then we start the next sentence (or visualization unit) with `>>`.

Note that how these token and span/edge properties are visualized is actually **up to you**. That is, when you load a file in _thebeast_ format you can decide which of these properties should be _span_ properties, which _token_ and which _dep_ (for dependency) properties. See the screenshot below for an example setting:

![http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/generic-format.png](http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/generic-format.png)

The above example and load setting would then give us:

![http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/generic-visualize.png](http://whatswrong.googlecode.com/svn/trunk/doc/screenshots/generic-visualize.png)