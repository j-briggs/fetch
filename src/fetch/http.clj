(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help retrieve tick data"}
  fetch.http
  (:require [clj-http.client :as client]
            [clojure.core.async :as async :refer [<!! >! chan go]]))

(defn- fetch-url
  "Fetch URL"
  [url & [req]]
  (let [requirements (merge req {:throw-exceptions false})
        result (client/get url requirements)]
    (if (= (:status result) 200)
      (:body result)
      (:status result))))

(defn fetch-pages
  "Fetch from the urls provided"
  [urls & [req]]
  (time 
  (let [c (chan)
        res (atom [])]
    (doseq [url urls]
      (go (>! c (fetch-url url req))))
    (doseq [_ urls]
      (swap! res conj (<!! c)))
    @res
    )))

