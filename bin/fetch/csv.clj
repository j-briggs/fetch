(ns #^{:author "Jonathan Briggs",
       :doc "utilities to help manipulate comma seperated values"}  
  fetch.csv
  (:use clojure-csv.core)
  (:require [clojure.string :as str]))

(defn comment-line? [line]
  (re-matches #"^#.*" line))

(defn split-at-comma [line]
  (str/split line #","))

(defn line-without-comments [line]
  (let [r (str/replace line #"#.*" "")]
    (if (pos? (count r)) r)))

(defn line-without-comments-words [line]
  (let [r (str/replace line #"([A-Z][^\.!?]*[\.!?])" "")]
    (if (pos? (count r)) r)))

(defn strings-to-double [chars]
  (for [l chars]
    (for [v l]
      (Double/valueOf v))))

(defn process-data [data]
  (->> data
    (clojure.java.io/reader)
    (line-seq)
    (keep line-without-comments)
    (map split-at-comma)
    (strings-to-double)))  

(defn process-csv [data]
  (parse-csv data))