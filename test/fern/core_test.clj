(ns fern.core-test
  (:require [speclj.core :refer :all]
            [clojure.test :refer :all]
            [fern.core :refer :all]
            [clojure.java.io :as io]
            [cheshire.core :refer :all]))

(comment
  (require '[speclj.run.standard]))

(defn sample [n type]
  [type
   (parse-string
    (slurp
     (io/resource
      (str "entities/" n "." (name type) ".json"))))])

(describe "HAL"
  (let [sample (sample "confucius" :hal)]

    (it "attributes"
      (should= (attributes sample)
               {"id" 9
                "name" "孔子"
                "pinyin" "Kongzi"
                "latinized" "Confucius"} ))

    (it "links"
      (should= (links sample)
               #{{:rels ["http://literature.example.com/rels/quotes"]
                  :href "http://literature.example.com/quotes/?author=kongzi&q={query}"}
                 {:rels ["profile"]
                  :href "http://literature.example.com/profiles/scholar"}
                 {:rels ["self"]
                  :href "http://literature.example.com/authors/kongzi"}} ))))

(run-specs)
