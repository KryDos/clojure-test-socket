(ns clj-server.connections_list-test
  (:require [clojure.test :refer :all]
            [clj-server.connections_list :as conn_l]))

(deftest add!-test
  (testing "Trying to add element to the agent"
    (conn_l/add! "test string")
    (Thread/sleep 1000)
    (is (= (conn_l/count!) 1))))


