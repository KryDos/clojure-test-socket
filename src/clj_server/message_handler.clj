(ns clj-server.message_handler
  (:gen-class)
  (:require [clojure.data.json :as json]))

(defn process
  [msg]
  (let [json-message (json/read-str msg)]
    (str "test")))
