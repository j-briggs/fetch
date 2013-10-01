(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help retriev tick data"}  
  fetch.core
  (:require [clj-http.client :as client]
            [clojure.core.async :as async :refer [<!! >! chan go]]
            [fetch.yahoo :as yahoo]))

(defn- fetch-url
  "Fetch one URL using HTTP Agent"
  [url]
  (let [result (client/get url {:as "UTF-8" :throw-exceptions false})]
    (if (= (:status result) 200)
      (:body result)
      (:status result))))

(defn fetch-historical-data
  "Fetch historical prices from Yahoo! finance for the given symbols between start and end"
  [start end syms]
  (time 
  (let [c (chan)
        res (atom [])
        urls (yahoo/create-url-list start end syms)]
    (doseq [url urls]
      (go (>! c (fetch-url url))))
    (doseq [_ urls]
      (swap! res conj (<!! c)))
    @res
    )))

;(create-url-list "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "TRMB"])
;(map fetch-url (create-url-list "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "TRMB"]))
;(fetch-historical-data "2009-01-01" "2009-01-31" ["AAPL"])
;(fetch-historical-data "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "IBM" "ZZZZZZ"])