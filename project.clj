(defproject fetch "0.1.0-SNAPSHOT"
  :description "fetch retirieves, unzips and write tick data to disk"
  :url "http://jonathanbriggs.net/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [clj-time "0.6.0"]
                 [org.clojure/core.async "0.1.222.0-83d0c2-alpha"]
                 [clojure-csv/clojure-csv "2.0.1"]
                 [incanter "1.5.4"]
                 [enlive "1.1.4"]])
