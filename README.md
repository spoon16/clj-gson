# clj-gson

[![Build Status](https://secure.travis-ci.org/spoon16/clj-gson.png)](http://travis-ci.org/spoon16/clj-gson)

Clojure bindings for Gson, Google's JSON parsing library.
See https://code.google.com/p/google-gson/ and http://www.json.org/

## Dependency Information

### [Leiningen](https://github.com/technomancy/leiningen)

    [com.spoon16/clj-gson "0.0.1"]

### [Maven](http://maven.apache.org/):

    <dependency>
      <groupId>com.spoon16</groupId>
      <artifactId>clj-gson</artifactId>
      <version>0.0.1</version>
    </dependency>

## Usage

    (use '[clj-gson.json :only (to-json from-json)])

    ;; write json
    (to-json {:hello [1 "two" 3] :world {"ell" "ee"}})
    ;;=> {"world":{"ell":"ee"},"hello":[1,"two",3]}"

    ;; read json
    (from-json "{ \"hello\": [1,\"two\",3], \"world\": { \"ell\": \"ee\" } }" )
    ;;=> {:world {:ell "ee"}, :hello [1.0 "two" 3.0]}

## License

Copyright © 2012 Eric Schoonover

Distributed under the Eclipse Public License, the same as Clojure.
