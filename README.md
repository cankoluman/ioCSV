# ioCSV v 0.67 (release)

## Can Koluman, PhD Candidate, City, University of London, Department of Computer Science

**A simple CSV (comma separated values) io Library in Scala**
<https://github.com/cankoluman/ioCSV>

***Also available at***
<https://search.maven.org/artifact/com.cankoluman/ioCSV>
For Maven, Groovy DSL, Kotlin DSL, Scala SBT, Ivy, and others.

### News

Version 0.67 uses Scala 3.2.0. Also, the project now is
IDE independent. Maven is sufficient to run it, even from a bash shell.

### Introduction

This is a very lightweight csv (comma separated values) exporter implementation.
The to be exported data is wrapped in a `Frame[A]` structure,
which is passed to the `csvRead(source: String, gZip: Boolean): Frame[A]`
or `csvWrite(source: Frame[A], target: String, gZip: Boolean, append: Boolean): Unit`
methods. At the moment, we only have
read / write for `[A] = Vector[Vector[B]]` via the `VectorToCsv` class. More info in the comments.
The implementation will grow slightly over time.  

### Customizables

- Headers: true / false, `Frame.header: Option[Vector[String]]`
- Separator: Char
- Encoding: Codec, e.g. `Codec(StandardCharsets.UTF_8)`
- Gzip: true / false (when reading from / writing to a file)
- Append: true / false (when writing to a file)

### Dependencies

- Scala
- Scala Library
- Scalatest 3
- Scalatestplus:mockito 4-5_3
- Mockito Core

### TO DO

- Add csv io for transposed `Vector[Vector[A]]`
- Add csv io for `ListMap[String, Vector[A]]`
- Add csv io for `DenseMatrix[A]` (Breeze)

#### Copyright Notices

&copy; 2022 Copyright, Can Koluman. Apache 2.0 Licence for authored components.

&copy; Copyright 2022 Can Koluman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at_

 <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License._

For  open sourced components / libraries, respective origin licenses apply.
