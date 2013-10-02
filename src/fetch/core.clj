(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help retrieve tick data"}  
  fetch.core
  (:require [clj-http.client :as client]
            [clojure.core.async :as async :refer [<!! >! chan go]]
            [fetch.yahoo :as yahoo]
            [fetch.csv :as csv]))

(defn- fetch-url
  "Fetch URL"
  [url & [req]]
  (let [requirements (merge req {:throw-exceptions false})
        result (client/get url requirements)]
    (if (= (:status result) 200)
      (:body result)
      (:status result))))

(defn fetch-historical-data
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

(def urls (yahoo/create-url-list "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "TRMB"]))
(def data (fetch-historical-data urls {:as "UTF-8"}))
(def dataset (yahoo/stream-to-dataset (csv/process-csv (first data))))
(incanter.core/$ :Adj-Close dataset)