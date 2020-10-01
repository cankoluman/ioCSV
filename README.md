## ioCVS v 0.4
###### Can Koluman, PhD Candidate, City, University of London, Department of Computer Science

**A simple CVS io Library in Scala**

Introduction:
-
This is a very lightweight csv exporter implementation.
The to be exported data is wrapped in a `Frame[A]` structure, 
which is passed to the `csvRead(source: String, gZip: Boolean): Frame[A]` 
or `csvWrite(source: Frame[A], target: String, gZip: Boolean, append: Boolean): Unit` 
methods. At the moment, we only have 
read / write for `[A] = Vector[Vector[B]]` via the `VectorToCvs` class. More info in the comments.
The implementation will grow slightly over time.  

Customizables:
- 
- Headers: true / false, `Frame.header: Option[Vector[String]]`
- Separator: Char
- Encoding: Codec, e.g. `Codec(StandardCharsets.UTF_8)`
- Gzip: true / false (when reading from / writing to a file)
- Append: true / false (when writing to a file)

Dependencies:
-
- Scala 2.13.3
- Scalatest 2.13:3.2.1 (e.g. Maven)
- Scalatestplus:mockito 3-4_2.13:3.1.3.0 (Maven)
- Mockito Core 3.5.7 (Maven)

###### Copyright Notices
&copy; 2020 Copyright, Can Koluman. MIT Licence for authored components.
For open sourced components, respective origin licenses apply.
