# Amuyaña
![Amuyaña](http://i64.tinypic.com/24g3loz.png)
Amuyaña is an Application under development that will create visual representations of Systems with the formal logic axioms of the Dynamic Logic of the Contradictory. The word *amuyaña* is a term in Aymara that means "thinking", "reasoning" or "understanding".

It has two main purposes: Generate visual representations of the main concepts of the Logic of the Contradictory and maintain the statistical main.data and its analysis.

You can download the latest version here (v1.3). However the current development version (v.2) differs significantly from that release, which is why the repository has been whiped out and only contains a development branch for the future v.2 release.

If you're interested on this software you can learn how it works in the [wiki section](https://github.com/CREAR-ASBL/amuyana/wiki).

The development of this software parallels the progression of my understanding of the Logic of the Contradictory.

The first version (called Chuyma) would only recreate the *transfinite* development of new branches of the Table of Deductions, because at that time (2016) I thought the ToD was the central concept for describing a Contradictional logic system (it is, not just the only one).

After testing that first release and trying to create logic systems, I realized that the code (written in Python) was not good enough to do what I was expecting (to print in the screen the same figures I draw by hand). That is why I started using Java (first Java Swing and later JavaFX) and renamed the software Amuyaña. One of my main concerns was the relation between the statistics (from the theory to its empirical implementation) and the definition of new elements of the CLS. Later I would get to the conclusion that if one wants to create a visual representation -as detailed as possible but also as simple as it can be-, there are actually three concepts which need to be defined:

- Table of Deductions:
- Tridialectic Apparatus:
- Space-Time Configuration:

These three concepts are direct implications of the Fundamental Postulate. At the moment of compiling main.data, as we associate any phenomenon to one of the three Contradictional conjunctions, we *can* (the proof will be submited to a journal for peer-to-peer review) infer the exact definition of the ToD, the TA and the STC.

It is worth emphazising the fact that these three concepts, which I claim are sufficient to create a detailed representation of a CLS, are deduced, implied, from one axiom only, called by Stéphane Lupasco (1951) the Fundamental Postulate of the Dynamic Logic of the Contradictory. This fundamental Postulate describes how and why the energy orientates to the three polarities, or *devenir*, the eventualities (the thee Contradictional conjunctions) can happen and actually do happen.

Starting from the release v2 the structure of the source code changes completely. I privilege the emulation of a formation of a fundamental duality (two elements becoming associated as in the Fundamental Postulate) and then the graphical engine inmediately creates a visual representation, in contrast to the previous version where statistical main.data introduced (the first step of the "emulation") was a separate task from the creation of the visual representations. This was the case because I wanted to save the raw main.data (the files with the extension .ña which contained the elements of the system), and separating main.data from graphical representation seemed logical to me. Unfortunately this is not the best option when there are more than one representation needed, like when we introduce the Tridialectic Apparatus and the Space-Time Configuration (for a more detailed explanation [visit](at first I thought the ToD was first, then the other two, but actually the three are simultaneous)). That obliged me to rethink the internal logic of the source code, and at the end I decided to start from scratch (for the forth time since 2016).

Amuyaña has the final objective of serving as a tool for organizing and analysing information in scientific investigations, specifically when the Logic of the Contradictory is [required](when is this logic required?).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc


//



(Last update: 30/08/2018)



## Introduction

## History


## Current version
1.3

## Development version
2

## Instructions for developpers
Amuyaña has been written in Java 1.8.0_161 and JavaFX for the graphical user interface.

The simplest way to get the source code is to pull the repository, for example:

	git clone https://github.com/CREAR-ASBL/amuyana.git

You will find a NetBeans project (the version I used was v. 8.2 - build 201609300101).

Visit the [wiki section](https://github.com/CREAR-ASBL/amuyana/wiki) for more information about the source code and how it runs step by step to create the main graphs. A more detailed overview of the classes, methods, variables, etc. is available in the docs.

## Links

The [project's website](http://amuyaña.com).

The [github wiki](https://github.com/CREAR-ASBL/amuyana/wiki) has information about the  structure of the (java) source code.

## Abbreviations
Contradictional Logic System or logic system of the Dynamic Logic of the Contradictory = CLS

Table of Deductions = ToD

FCC = Fundamental Conjunction of contradiction

## Licence

Amuyaña is distributed under the  General Public Licence version 3.

