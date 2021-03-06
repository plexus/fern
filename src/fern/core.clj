(ns fern.core)

(defmulti attributes first)
(defmulti links first)
(defmulti entities first)
(defmulti controls first)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; HAL

(defmethod attributes :hal [[_ entity]]
  (into {} (remove (comp #{\_} ffirst) entity)))

(defmethod links :hal [[_ entity]]
  (letfn [(link-map [rel link]
            {:rels [ rel ]
             :href (get link "href")})]
    (reduce (fn [acc [rel links]]
              (if (vector? links)
                (into acc (map (partial link-map rel) links))
                (conj acc (link-map rel links))))
            #{}
            (get entity "_links" {}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SIREN

(defmethod attributes :siren [[_ entity]]
  (get entity "properties" {}))

(defmethod links :siren [[_ entity]]
  (for [link (get entity "links")]
    {:rels (get link "rel")
     :href (get link "href")}))
