# clj-gson

[![Build Status](https://secure.travis-ci.org/spoon16/clj-gson.png)](http://travis-ci.org/spoon16/clj-gson)

Clojure bindings for Gson, Google's JSON parsing library.
See https://code.google.com/p/google-gson/ and http://www.json.org/

## Dependency Information

clj-gson has been published to [Clojars.org](https://clojars.org/com.spoon16/clj-gson)

### [Leiningen](https://github.com/technomancy/leiningen)

    [com.spoon16/clj-gson "0.0.1"]

### [Maven](http://maven.apache.org/):

    <dependency>
      <groupId>com.spoon16</groupId>
      <artifactId>clj-gson</artifactId>
      <version>0.0.1</version>
    </dependency>

## Usage

### Basics

    => (use '[clj-gson.json :only (to-json from-json)])

    ;; clojure -> json
    => (to-json {:hello [1 "two" 3] :world {"ell" "ee"}})
    {
      "world": {
        "ell": "ee"
      },
      "hello": [
        1,
        "two",
        3
      ]
    }

    ;; json -> clojure
    => (from-json "{ \"hello\": [1,\"two\",3], \"world\": { \"ell\": \"ee\" } }")
    {:world {:ell "ee"}, :hello [1.0 "two" 3.0]}

    ;; string -> json tree
    => (parse-json-tree "{ \"hello\": [1,\"two\",3], \"world\": { \"ell\": \"ee\" } }") 
    #<JsonObject {"hello":[1,"two",3],"world":{"ell":"ee"}}>

    ;; clojure -> json tree
    => (to-json-tree {:hello [1 "two" 3] :world {"ell" "ee"}})
    #<JsonObject {"world":{"ell":"ee"},"hello":[1,"two",3]}>

### GSON Configuration

TODO

## License

Copyright © 2012 Eric Schoonover

Distributed under the Eclipse Public License, the same as Clojure.
