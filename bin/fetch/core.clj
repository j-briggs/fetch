(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help retrieve tick data"}  
  fetch.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [clojure.core.async :as async :refer [<!! >! chan go]]
            [fetch.yahoo :as yahoo]
            [fetch.csv :as csv]
            [fetch.http :as http]))

(def ^:dynamic *base-url* "http://somedatasource.com/")

(def ^:dynamic *link-selector*
     [[:a (html/attr? :href)]])

(def ^:dynamic *headline-selector*
     [:a])

(defn fetch-url 
  [url]
  (html/html-resource (java.net.URL. url)))

(defn pages 
  [url]
  (html/select (fetch-url url) *link-selector*))

(defn number-line? 
  [line]
  (re-matches #"^[0-9]{1,45}$*" (first line)))

(defn extract
  [node]
  (let [links (first (html/select [node] *headline-selector*))
        result   (map html/text [links])]
    (map #(str/replace % #"\n" "") result)))

(defn combine-url
  [base paths]
  (let [sep "/"
        clean-list (keep number-line? paths)]
    (map #(str base % sep) clean-list)))

(defn fetch-pages
  "Fetch from the urls provided"
  [fetcher urls]
  (time 
  (let [c (chan)
        res (atom [])]
    (doseq [url urls]
      (go (>! c (fetcher url))))
    (doseq [_ urls]
      (swap! res conj (<!! c)))
    @res
    )))



;(def init-list (map extract (pages *base-url*)))
;(def new-pages (combine-url *base-url* init-list))
;(fetch-pages pages new-pages)




; "An example using clj-http to retrieve yahoo stock data"
;(def urls (yahoo/create-url-list "2009-01-01" "2009-01-31" ["AAPL" "GOOG" "TRMB"]))
;(def data (http/fetch-pages urls {:as "UTF-8"}))
;(def dataset (yahoo/stream-to-dataset (csv/process-csv (first data))))
;(incanter.core/$ :Adj-Close dataset)
