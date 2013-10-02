(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help retriev tick data"}  
  fetch.core
  (:require [clj-http.client :as client]
            [clojure.core.async :as async :refer [<!! >! chan go]]
            [fetch.yahoo :as yahoo]))

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

;(def urls (yahoo/create-url-list "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "TRMB"]))
;(fetch-historical-data urls {:as "UTF-8" :proxy-host "workproxy.net" :proxy-port 3128})