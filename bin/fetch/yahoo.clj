(ns #^{:author "Jonathan Briggs",
       :doc "yahoo specific functions"} 
  fetch.yahoo
   (:use (incanter core stats charts io))
   (:require [clj-time.core :as time]))

(def #^{:private true} +base-url+ "http://itable.finance.yahoo.com/table.csv?s=%s&g=d&a=%d&b=%d&c=%d&d=%d&e=%d&f=%d")

(defn- get-full-url-yahoo
  "Construct the complete URL given the params"
  [y1 m1 d1 y2 m2 d2 sym]
  (let [start (time/date-time y1 m1 d1)
        end (time/date-time y2 m2 d2)]
    (format +base-url+
            sym
            (dec (time/month start))
            (time/day start)
            (time/year start)
            (dec (time/month end))
            (time/day end)
            (time/year end))))

(defn create-url-list
  "given the dates and symbols, construct the urls"
  [start end syms]
  (letfn [(parse-date [dt] (map #(Integer/parseInt %) (.split dt "-")))]
    (let [[y1 m1 d1] (parse-date start)
          [y2 m2 d2] (parse-date end)
          urls (map (partial get-full-url-yahoo y1 m1 d1 y2 m2 d2) syms)]
      urls)))

(defn stream-to-dataset
  "take the data stream and convert it to a data set"
  [data]
  (incanter.core/dataset [:Date :Open :High :Low :Close :Volume :Adj-Close] data))